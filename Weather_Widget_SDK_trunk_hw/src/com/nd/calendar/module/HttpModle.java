/**   
 *    
 * @file 【网络请求内容类】
 * @brief
 *
 * @<b>文件名</b>      : HttpModle
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-3 上午11:11:47 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

import com.calendar.CommData.DateInfo;
import com.calendar.CommData.ICommData;
import com.calendar.CommData.SendSuggestInfo;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.communication.http.HttpAppFunClient;
import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.dbrepoist.IUserInfo;
import com.nd.calendar.dbrepoist.UserInfo;
import com.nd.calendar.util.CalendarInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.StringHelp;
import com.nd.calendar.util.SysHelpFun;

public class HttpModle implements IHttpModle
{
	public final static int HTTP_SUCCESS = 1;
	public final static int HTTP_FAILURE = 0;
	public final static int HTTP_PARAM_ERROR = -1;
	public final static int HTTP_UNKNOWN_ERROR = -2;
	public final static int HTTP_WIDGET_SKIN_NO_MORE = -4;

	private Context mContext = null;

	public HttpModle(Context context, IDatabaseRef dataBaseRef) {
		mContext = context.getApplicationContext();
		if (mContext == null) {
			mContext = context;
		}
		m_AppFunClient = new HttpAppFunClient(mContext);
		m_dbUserInfo = UserInfo.getInstance(mContext, dataBaseRef);
	}

	/**
	 * @brief 【调用网络功能函数】
	 * 
	 * 
	 * @n<b>函数名称</b> :InvokeNetWorkAppFun
	 * @see
	 * 
	 * @param @param nFunId
	 * @param @param param
	 * @param @param result
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午04:13:04
	 */
	@Override
	public boolean InvokeNetWorkAppFun(String sid, int nFunId, ICommData param, ICommData result, Date stdDate) {
		StringBuffer strResOut = new StringBuffer("");
		if (!m_AppFunClient.GetAstrocommand(sid, param.ToJsonObject(), strResOut, nFunId, stdDate)) {
			return false;
		}

		result.SetJsonString(strResOut.toString());
		return true;

	}

	/////////////////////////////////////////////////////////////////////////////////

//	public int initPeopleList(String strUserName, String sid) {
//		JSONObject jsonInit = new JSONObject();
//		try {
//			jsonInit.put("username", strUserName);
//			jsonInit.put("vercode", "301");
//		} catch (Exception e) {
//		}
//		
//		return m_AppFunClient.UserInit(jsonInit, sid);
//	}
//	
//	/**
//	 * @brief 从服务端下载人员列表
//	 * 
//	 * @n<b>函数名称</b> :GetUserList
//	 * @param result
//	 * @param iVersion
//	 * @return :
//	 * @return boolean
//	 * @<b>作者</b> : 陈希
//	 * @<b>创建时间</b> : 2011-7-26下午06:40:15
//	 */
//	public boolean GetPeopleList(String sid, ICommData result, int iVersion) {
//		JSONObject jsonObjectIn = new JSONObject();
//
//		try {
//			jsonObjectIn.put("iVersion", iVersion);
//			jsonObjectIn.put("szTabname", m_AppFunClient.getUserTbName());
//			jsonObjectIn.put("iVerType", 0);
//
//			StringBuffer strResOut = new StringBuffer("");
//
//			int iRetry = 0;
//			while (!m_AppFunClient.DownDataPostAppFun(sid, "downloaddata", "1.0", "UapDl_PepList",
//					jsonObjectIn, strResOut) && (iRetry++ < 3))
//				;
//
//			if (iRetry > 3) {
//				return false;
//			}
//
//			result.SetJsonString(strResOut.toString());
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return true;
//	}
//
//	/**
//	 * @brief 向服务端上传人员数据
//	 * 
//	 * 
//	 * @n<b>函数名称</b> :UploadPeopleList
//	 * @see
//	 * 
//	 * @param @param result
//	 * @param @return
//	 * @<b>作者</b> : 陈希
//	 * @<b>创建时间</b> : 2011-8-10上午11:34:03
//	 */
//	public boolean UploadPeopleList(String sid, ICommData result) {
//		JSONObject jsonData = result.ToJsonObject();
//
//		if (jsonData != null) {
//			try {
//				JSONObject jsonParam = new JSONObject();
//
//				jsonParam.put("szTabname", m_AppFunClient.getUserTbName());
//
//				int iRetry = 0;
//				JSONArray jaArray = jsonData.getJSONArray("data");
//
//				// 重试3次后，失败
//				while (!m_AppFunClient.UploadDataPostAppFun(sid, "uploaddata", "2.0", "UapUl_PepList",
//						jsonParam, jaArray) && (iRetry++ < 3))
//					;
//
//				if (iRetry > 3) {
//					return false;
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//
//		return true;
//	}

	/**
	 * @brief 【获取系统时间】
	 * 
	 * 
	 * @n<b>函数名称</b> :GetSysDate
	 * @see
	 * 
	 * @param @param date
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午04:12:11
	 */
	@Override
	public String GetServerDate() {
		return m_AppFunClient.getServerDate();
	}

	public int DownloadIcoFromNet(String sURL, String sFilePath) {
		return HTTP_SUCCESS;
	}

	/**
	 * @brief 【获取版本信息】
	 * 
	 * 
	 * @n<b>函数名称</b> :DownverInfo
	 * @see
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-7-26上午09:39:41
	 */
	public boolean DownverInfo(int appid) {
		StringBuffer strResOut = new StringBuffer("");
		if (m_AppFunClient.DownverInfo(appid, strResOut)) {
			try {
				JSONArray array = new JSONArray(strResOut.toString());
				String strOutTemp = array.get(0).toString();
				JSONObject jsonOut = StringHelp.getJSONObject(strOutTemp);
				m_iServerVer = jsonOut.getInt("iServerVer");
				m_sVerCode = jsonOut.getString("sVerCode");
				m_sDownUrl = jsonOut.getString("sDownUrl");
			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;

			}

		}
		return true;
	}

	/**
	 * @brief【提交意见】
	 * 
	 * 
	 * @n<b>函数名称</b> :DownSuggestanswer
	 * @see
	 * 
	 * @param @param strContactinfo
	 * @param @param strSuggest
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-11-21下午05:45:57
	 */
	public boolean DownSuggestanswer(int appid, String strContactinfo, String strSuggest) {
		String cudateStr = ComfunHelp.formatDate(new Date(System.currentTimeMillis()));
		SendSuggestInfo suggestInfo = new SendSuggestInfo();

		suggestInfo.setQuest(strSuggest);
		suggestInfo.setProductid(Integer.toString(appid));
		suggestInfo.setProduct("黄历天气(91桌面插件版)");
		suggestInfo.setOS("android");
		suggestInfo.setOs_version(SysHelpFun.GetandroidSDk());
		suggestInfo.setApp_version(ComDataDef.VERSION_NAME);
		suggestInfo.setLocalid(SysHelpFun.getDeviceId(mContext));
		suggestInfo.setQuestno(SysHelpFun.getNewIdentifyID());
		suggestInfo.setType(Build.MODEL);
		suggestInfo.setCudate(cudateStr);
		if (!m_AppFunClient.DownSuggestanswer(strContactinfo, suggestInfo)) {
			return false;
		}

		// 保存当前意见
		String strSystemTiem = ComfunHelp.formatDateTime(new Date(System.currentTimeMillis()));
		suggestInfo.setAsk_time(strSystemTiem);
		suggestInfo.setFlag(0);
		m_dbUserInfo.GetUserSuggestIf().InsertSuggestInfo(suggestInfo);
		// GetListAnswer();
		return true;
	}

	/**
	 * @brief 【根据内容Id获取答案】
	 * 
	 * 
	 * @n<b>函数名称</b> :GetListAnswer
	 * @see
	 * 
	 * @param @param listSendSuggestInfo
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-11-21下午05:45:12
	 */
	@Override
	public boolean GetListAnswer(int appid) {
		List<SendSuggestInfo> listSendSuggestInfo = new ArrayList<SendSuggestInfo>();
		if (!m_dbUserInfo.GetUserSuggestIf().GetIsHasNoAnser(listSendSuggestInfo)) {
			return false;
		}
		boolean bIsRish = false;
		JSONObject jsonObjectIn = new JSONObject();
		try {
			jsonObjectIn.put("productid", Integer.toString(appid));
			jsonObjectIn.put("localid", SysHelpFun.getDeviceId(mContext));
			JSONArray jonsArry = new JSONArray();
			for (int i = 0; i < listSendSuggestInfo.size(); i++) {
				JSONObject jsonObjectTemp = new JSONObject();
				jsonObjectTemp.put("questno", listSendSuggestInfo.get(i).getQuestno());
				jonsArry.put(jsonObjectTemp);
			}
			jsonObjectIn.put("vecquestno", jonsArry);
			StringBuffer strResOut = new StringBuffer();
			if (!m_AppFunClient.Getanswer(jsonObjectIn, strResOut)) {
				return false;
			}
			// 根据内容重新更新缓存
			JSONObject jsonObject = StringHelp.getJSONObject(strResOut.toString());
			if (jsonObject != null) {
				String strList = jsonObject.getString("vecanswer");
				JSONArray jonsArryAnser = new JSONArray(strList);
				for (int i = 0; i < jonsArryAnser.length(); i++) {
					SendSuggestInfo senduggestInfo = new SendSuggestInfo();
					JSONObject jsonObjectAnser = (JSONObject) jonsArryAnser.get(i);
					senduggestInfo.setQuestno(jsonObjectAnser.getString("questno"));
					int nFlag = jsonObjectAnser.getInt("flag");
					if (nFlag != 0) {
						senduggestInfo.setAnswer(jsonObjectAnser.getString("answer"));
						String strTime = CalendarInfo.getDate(jsonObjectAnser.getString("answer_time"));
						senduggestInfo.setAnswer_time(strTime);
						senduggestInfo.setFlag(nFlag);
						m_dbUserInfo.GetUserSuggestIf().UpdateItemSuggestInfo(senduggestInfo);
						bIsRish = true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bIsRish;
	}

//	@Override
//	public boolean DownSoftAdInfo(int nAppId, int nAppVersion, int nSoftVersoin) {
//		JSONObject jsonObjectIn = new JSONObject();
//		try {
//			jsonObjectIn.put("nAppId", nAppId);
//			jsonObjectIn.put("nAppVersoin", nAppVersion);
//			jsonObjectIn.put("nSoftVersoin", nSoftVersoin);
//			jsonObjectIn.put("nBreakFlag", 0);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		StringBuffer strResOut = new StringBuffer();
//		int bFlag = m_AppFunClient.DownSoftAdInfo(jsonObjectIn, strResOut);
//		if (bFlag == 206) {
//			try {
//				jsonObjectIn.put("nAppVersoin", 1);
//				// 同时删除旧的数据
//				m_dbUserInfo.GetSoftADIf().DeleteAllSoftInfoExt();
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			strResOut = new StringBuffer();
//			bFlag = m_AppFunClient.DownSoftAdInfo(jsonObjectIn, strResOut);
//		}
//		if (bFlag == 200) {
//			try {
//				JSONObject jsonObject = StringHelp.getJSONObject(strResOut.toString());
//				JSONArray jsonArry = jsonObject.getJSONArray("arrSoftInfo");
//				String strBaseHost = jsonObject.getString("nBaseHost");
//				// m_dbUserInfo.GetSoftADIf().GetDatabaseRef().beginTransaction();
//				for (int i = 0; i < jsonArry.length(); i++) {
//					SoftInfoExt softInfoExt = new SoftInfoExt();
//					JSONObject jsonObjectItem = (JSONObject) jsonArry.get(i);
//					softInfoExt.SetJsonString(jsonObjectItem.toString());
//					// 保存数据推荐列表信息
//					softInfoExt.setSoftIcoPath(strBaseHost + softInfoExt.getSoftIcoPath());
//					switch (softInfoExt.getOpt()) {
//					case 0:
//						m_dbUserInfo.GetSoftADIf().setSoftInfoExt(softInfoExt);
//						break;
//					case 1:
//						softInfoExt.setOpt(0);
//						// 防止服务端没有备份导致出错,先进行查询
//						// 判断是否存在改信息
//						m_dbUserInfo.GetSoftADIf().setSoftInfoExt(softInfoExt);
//						
////						if (m_dbUserInfo.GetSoftADIf().isHasItemInfo(softInfoExt)) {
////							m_dbUserInfo.GetSoftADIf().UpdateSoftInfoExt(softInfoExt);
////						} else {
////							m_dbUserInfo.GetSoftADIf().InsertSoftInfoExt(softInfoExt);
////						}
//
//						break;
//					case 2:
//						m_dbUserInfo.GetSoftADIf().DeleteSoftInfoExt(softInfoExt);
//						break;
//					default:
//						break;
//					}
//
//				}
//				// 修改推荐版本号
//				int nVers = jsonObject.getInt("nAppVersoin");
//				m_dbUserInfo.GetSoftADIf().SetSoftInfoVersion(nVers);
//				return true;
//				// m_dbUserInfo.GetSoftADIf().GetDatabaseRef().setTransactionSuccessful();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			}
//
//		} else {
//			return false;
//		}
//
//	}
//
//	/**
//	 * @brief 【下载电商列表】
//	 * @n<b>函数名称</b>     :getECommerceInfo
//	 * @param appid
//	 * @param maxVer
//	 * @param sMaxVer
//	 * @return
//	 * @return    boolean   
//	 * @<b>作者</b>          :  陈希
//	 * @<b>修改</b>          :
//	 * @<b>创建时间</b>      :  2012-11-6下午04:25:42
//	 * @<b>修改时间</b>      :
//	*/
//	public boolean getECommerceInfo(int appid, String maxVer, ArrayList<ElecSupplierInfo> outList, StringBuilder sOutMaxVer) {
//		
//		StringBuffer sOut = new StringBuffer();
//		boolean bRet = m_AppFunClient.getECommerceInfo(appid, maxVer, sOut);
//		
//		if (bRet) {
//			JSONObject json = StringHelp.getJSONObject(sOut.toString());
//			if (json == null) {
//				return false;
//			}
//			
//			try {
//				sOutMaxVer.append(json.getString("maxver")) ;
//				JSONArray list = json.getJSONArray("ebslist");
//				int size = list.length();
//				
//				for (int i=0; i<size; i++) {
//					ElecSupplierInfo info = new ElecSupplierInfo();
//					info.setJson(list.getJSONObject(i));
//					
//					outList.add(info);
//				}
//				
//			} catch (Exception e) {
//			}
//		}
//		
//		return bRet;
//	}
//	
	/**
	 * @brief 【下载桌面皮肤列表】
	 * 
	 * @n<b>函数名称</b> :DownWidgetSkinListInfo
	 * @param widgetSkinListOut
	 * @param nPage
	 * @param nPageSize
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-2-7下午05:22:40
	 */
	public int DownWidgetSkinListInfo(ICommData widgetSkinListOut, int nPage, int nPageSize) {
		if (widgetSkinListOut == null) {
			return HTTP_PARAM_ERROR;
		}

		JSONObject jsonObjectIn = new JSONObject();
		try {
			jsonObjectIn.put("page", nPage);
			jsonObjectIn.put("pageSize", nPageSize);
		} catch (JSONException e) {
			e.printStackTrace();
			return HTTP_UNKNOWN_ERROR;
		}

		StringBuffer strResOut = new StringBuffer();
		int iFlag = m_AppFunClient.DownWidgetSkinListInfo(jsonObjectIn, strResOut);
		if (iFlag == 200) {

			widgetSkinListOut.SetJsonString(strResOut.toString());
			return HTTP_SUCCESS;
		} else if (iFlag == 204) {
			return HTTP_WIDGET_SKIN_NO_MORE;
		}

		return HTTP_FAILURE;
	}
	
	/**
	 * @brief 【】
	 * 
	 * @n<b>函数名称</b> :GetLoginServerDate
	 * @param date
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-8-15上午11:33:54
	 */

	@Override
	public void GetLoginServerDate(DateInfo dateInfo) {
		String sDate = m_AppFunClient.GetLoginServerDate();
		Date date = ComfunHelp.getTimeDate(sDate);
		if (date != null) {
			dateInfo.setYear(date.getYear() + 1900);
			dateInfo.setMonth(date.getMonth() + 1);
			dateInfo.setDay(date.getDate());
		}
	}

	public String getM_sVerCode() {
		if (m_sVerCode == null || m_sVerCode.equals("")) {
			m_sVerCode = ComDataDef.VERSION_NAME;
		}
		return m_sVerCode;
	}

	public int getM_iServerVer() {
		return m_iServerVer;
	}

	public String getM_sDownUrl() {
		return m_sDownUrl;
	}

	// 应用层网络访问对象
	private HttpAppFunClient m_AppFunClient = null;
	private String m_sVerCode = null;
	private String m_sDownUrl = null;
	private int m_iServerVer = 0;
	// 数据库层接口
    private IUserInfo m_dbUserInfo = null;
}
