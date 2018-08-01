/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : CalendarModule
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2013-3-1 下午05:24:59 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.calendar.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.calendar.CommData.DateInfo;
import com.calendar.CommData.YjcInfo;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ComDataDef.DbInfo;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.dbrepoist.DBCalendarResult;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.FileHelp;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.UI.weather.UIWeatherFragmentAty;


public class CalendarModule
{
	private Context mContext = null;
	private DBCalendarResult mCalendarResult = null;
    private int mCalendarYear = 0;
    private DownloadThread mDownloadThread = null;

    public static final int CM_SUCCESS = 0;
    public static final int CM_DOWNLOADING = 1;			// 数据库正在下载
    public static final int CM_ERROR_UNKNOWN = 2;
    public static final int CM_ERROR_DB_INVALID = 3;	// 当前数据库无效，需要重新下载
    public static final int CM_ERROR_DB_OPT = 4;		// 数据库操作失败
    public static final int CM_ERROR_NO_NETWORK = 5;	// 无网络
    public static final int CM_ERROR_DB_DOWNLOAD = 6;	// 数据库下载失败

    private int mLastState = CM_SUCCESS;
    
    private DateInfo mLastDateInfo = null;
    private YjcInfo mLastYjcInfo = null;
    
	public CalendarModule(Context context) {
		mContext = context.getApplicationContext();
		if (mContext == null) {
			mContext = context;
		}
	}
	
	public static String getYJCState(int type) {
		switch (type) {
		case CM_ERROR_UNKNOWN:
		case CM_ERROR_DB_OPT:
			return "加载失败";
		case CM_ERROR_NO_NETWORK:
			return "需联网获取";
		case CM_DOWNLOADING:
			return "联网获取中...";
		case CM_ERROR_DB_DOWNLOAD:
			return "联网获取失败";
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * @brief 【获取宜忌数据】
	 * @n<b>函数名称</b>     :getYJCInfo
	 * @param dateInfo
	 * @param yjcInfo
	 * @return
	 * @return    int   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-3-4下午05:39:47
	 * @<b>修改时间</b>      :
	*/
	public int getYJCInfo(DateInfo dateInfo, YjcInfo yjcInfo) {
		if (dateInfo == null || yjcInfo == null) {
			return CM_ERROR_UNKNOWN;
		}
		
		//到了晚上11点就换成第二天的数据
        if (dateInfo.hour >= 23){
        	dateInfo = new DateInfo(dateInfo);
        	dateInfo = ComfunHelp.SetDateDeiff(1, dateInfo);
        	dateInfo.hour = 0;
        }
		
        // 使用上次缓存的数据
        if (mLastDateInfo != null && mLastYjcInfo != null
        		&& mLastDateInfo.year == dateInfo.year
        		&& mLastDateInfo.month == dateInfo.month
        		&& mLastDateInfo.day == dateInfo.day) {
        	
        	yjcInfo.setStrYi(mLastYjcInfo.getStrYi());
        	yjcInfo.setStrJi(mLastYjcInfo.getStrJi());
        	yjcInfo.setStrChong(mLastYjcInfo.getStrChong());
        	
        	return CM_SUCCESS;
		}
        
		// 检查当前打开的数据库与请求的年份是否一致，不一致需要重新打开对应的数据库
		if (dateInfo.year != mCalendarYear && mCalendarResult != null) {
			synchronized (mCalendarResult) {
				mCalendarResult.close();
			}

			mCalendarResult = null;
		}

		// 数据库无效，则打开
		if (mCalendarResult == null) {

			// 获得正确的数据库文件名
			String sMark = "_" + dateInfo.year + "_" + DbInfo.R_DB_CALENDAR_VERSION;
			String sDBName = FileHelp.GetDBNameWithVer(DbInfo.R_DB_CALENDAR, sMark);
			String sDbFile = FileHelp.GetPhoneDatabase(mContext, sDBName);
			File file = new File(sDbFile);

			if (file.exists()) {
				// 文件存在，打开
				mCalendarResult = new DBCalendarResult(mContext);
				synchronized (mCalendarResult) {
					if (!mCalendarResult.open(sDbFile)) {
						return CM_ERROR_DB_OPT;
					}
				}
			} else {
				// 检查网络				
				if (!HttpToolKit.isNetworkAvailable(mContext)) {
					return CM_ERROR_NO_NETWORK;
				}
				
				if (mLastState == CM_ERROR_DB_DOWNLOAD) {
					SharedPreferences set = WidgetUtils.getSetting(mContext, WidgetGlobal.WIDGET_SETTING);;
					String sLastTime = set.getString(ConfigsetData.CONFIG_NAME_KEY_HUANGLI_LAST, null);
					Date lastDate = ComfunHelp.getTimeDate(sLastTime);
					if (lastDate != null) {
						Date nowDate = new Date(System.currentTimeMillis());
						
						try {
							long diffTime = Math.abs(nowDate.getTime() - lastDate.getTime());

							if ((diffTime < 30 * 60 * 1000)) {
								return CM_ERROR_DB_DOWNLOAD;
							} else {
								
								set.edit().putString(ConfigsetData.CONFIG_NAME_KEY_HUANGLI_LAST, "").commit();
								mLastState = CM_SUCCESS;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				// 文件不存在，下载 -> 通知调用者再调用一次
				if (mDownloadThread == null || !mDownloadThread.isAlive()) {
					mDownloadThread = new DownloadThread(sDBName, file.getParent());
					mDownloadThread.start();
				}
				
				// 如果上一次获取失败，则返回下载失败状态
				if (mLastState == CM_ERROR_DB_DOWNLOAD) {
					mLastState = CM_SUCCESS;
					return CM_ERROR_DB_DOWNLOAD;
				} else {
					return CM_DOWNLOADING;
				}
			}
		}
		
		synchronized (mCalendarResult) {
			if (mCalendarResult.getYiJiChong(dateInfo, yjcInfo)) {
				
				mLastDateInfo = new DateInfo(dateInfo);
				mLastYjcInfo = new YjcInfo();
				mLastYjcInfo.setStrYi(yjcInfo.getStrYi());
				mLastYjcInfo.setStrJi(yjcInfo.getStrJi());
				mLastYjcInfo.setStrChong(yjcInfo.getStrChong());
				
				return CM_SUCCESS;
			}
		}
		
		return CM_ERROR_DB_OPT;
	}
	
	/**
	 * @brief 【下载类】
	 * @n<b>类名称</b>     :DownloadThread
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-3-5下午08:33:30
	 * @<b>修改时间</b>      :
	*/
	private class DownloadThread extends Thread {
	    
		private final static String DOWN_URL = "http://hltq.91.com/file/CalendarData/";
		String mFileName;
		String mTargetDir;
		
		public DownloadThread(String sFileName, String sTargetDir) {
			mFileName = sFileName;
			mTargetDir = sTargetDir;
		}
		
		@Override
		public void run() {
			String sSvrFileName = mFileName.replace(".db", ".zip");
			File downf = new File(mTargetDir, sSvrFileName);
			
			try {
				FileHelp.DeleteFile(downf);
				
				String sb = DOWN_URL + sSvrFileName;
				String sZipFilePath = downf.getAbsolutePath();
				boolean bRet = HttpToolKit.saveToFile(mContext, sb, sZipFilePath);
				if (!bRet) {
					bRet = HttpToolKit.saveToFile(mContext, sb, sZipFilePath);
				}
				
				if (bRet && downf.exists() &&
					UnZipFile(sZipFilePath, new File(mTargetDir, mFileName).getAbsolutePath())) {
					mLastState = CM_SUCCESS;
				} else {
					// 失败
					mLastState = CM_ERROR_DB_DOWNLOAD;
					SharedPreferences set = WidgetUtils.getSetting(mContext, WidgetGlobal.WIDGET_SETTING);;
					set.edit().putString(ConfigsetData.CONFIG_NAME_KEY_HUANGLI_LAST, ComfunHelp.getSystemTime()).commit();
				}
				
				Log.d("clendar", "send");
				// 发送广播通知下载状态
				WidgetGlobal.updateWidgets(mContext);
				Intent intent = new Intent(UIWeatherFragmentAty.ACTION_REFRESH_HUANGLI);
				mContext.sendBroadcast(intent);
				
			} catch (Exception e) {
			} finally {
				
				// 删除临时文件
				FileHelp.DeleteFile(downf);
			}
		}
		
	    /**  
	     * 解压一个压缩文档 到指定位置  
	     * @param zipFileString 压缩包的名字  
	     * @param outFileString 指定的路径  
	     * @throws Exception  
	     */  
		public boolean UnZipFile(String zipFileString, String outFileString) {
			File file = new File(outFileString + ".tmp");
			try {
				java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
						new FileInputStream(zipFileString));
				java.util.zip.ZipEntry zipEntry;
				String szName = "";

				while ((zipEntry = inZip.getNextEntry()) != null) {
					szName = zipEntry.getName();

					if (!zipEntry.isDirectory() && "CalendarData.db".equals(szName)) {
						file.createNewFile();
						FileOutputStream out = new FileOutputStream(file);
						int len;
						byte[] buffer = new byte[1024];
						while ((len = inZip.read(buffer)) != -1) {
							out.write(buffer, 0, len);
							out.flush();
						}
						out.close();
						
						file.renameTo(new File(outFileString));
					}
				}

				inZip.close();
				return true;
			} catch (Exception e) {
			} finally {
				FileHelp.DeleteFile(file);
			}
			
			return false;
		}
	}

}
