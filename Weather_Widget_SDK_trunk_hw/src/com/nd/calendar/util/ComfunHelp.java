/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : ComfunHelp
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-7-7 下午05:49:36 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.util;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.calendar.CommData.DateInfo;

public class ComfunHelp {
    /*使用这个静态变量一定要记得重新设置时区 SDF_DATE.getCalendar().setTimeZone(TimeZone.getDefault());*/
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat SDF_TIMESS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int SERVER_TIME_ZOME = TimeZone.getTimeZone("GMT+8").getRawOffset();

    public static Date GetDate(String str, String strType, boolean bFlag) {
        SimpleDateFormat df = new SimpleDateFormat(strType);
        Date dateInfo;
        try {
            dateInfo = df.parse(str);
            if (bFlag) {
                dateInfo.setYear(dateInfo.getYear() + 1900);
                dateInfo.setMonth(dateInfo.getMonth() + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dateInfo;
    }

    public static Date GetSystemTime() {
        Time t = new Time();
        t.setToNow(); // 取得系统时间
        Date date = new Date();
        date.setYear(t.year);
        date.setMonth(t.month);
        date.setDate(t.monthDay);
        date.setHours(t.hour);
        date.setMinutes(t.minute);
        return date;
    }

    public static Date GetSystemDate() {
        Time t = new Time();
        t.setToNow(); // 取得系统时间
        Date date = new Date();
        date.setYear(t.year);
        date.setMonth(t.month);
        date.setDate(t.monthDay);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    public static boolean isToday(String date) {
        boolean result = false;
        if (!TextUtils.isEmpty(date)) {
            try {
                //时区有的时候会被改变
                SDF_DATE.getCalendar().setTimeZone(TimeZone.getDefault());
                Date date1 = SDF_DATE.parse(date);
                Date date2 = new Date(System.currentTimeMillis());
                
                result = (date1.getYear() == date2.getYear()
                		&& date1.getMonth() == date2.getMonth()
                		&& date1.getDate() == date2.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    
    /**
     * @brief 【转换】
     * @n<b>函数名称</b>     :getTimeDate
     * @param sDate
     * @return
     * @return    Date   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-8-15上午11:40:32
     * @<b>修改时间</b>      :
    */
    public static final Date getTimeDate(String sDate) {
        Date date = null;
        try {
            SimpleDateFormat sdf = null;
            int len = sDate.length();
            if (len > 16){
                sdf = SDF_TIMESS;
            }else if (len == 16){
                sdf = SDF_TIME;
            }else{
                sdf = SDF_DATE;
            }
            sdf.getCalendar().setTimeZone(TimeZone.getDefault());
            date = sdf.parse(sDate);
        } catch (Exception e) {
        }
        
        return date;
    }
    
    public static final Date getDate(String sDate) {
        Date date = null;
        try {
            date = SDF_DATE.parse(sDate);
        } catch (Exception e) {
        }
        
        return date;
    }
    /**
     * @brief 【】
     * @n<b>函数名称</b>     :formatTimeDate
     * @param date
     * @return
     * @return    String   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-8-15下午01:41:12
     * @<b>修改时间</b>      :
    */
    public static final String formatDateTime(Date date) {
        try {
            SDF_TIMESS.getCalendar().setTimeZone(TimeZone.getDefault());
            return SDF_TIMESS.format(date);
        } catch (Exception e) {
        }
        return null;
    }
    
    public static final String formatDate(Date date) {
        try {
            return SDF_DATE.format(date);
        } catch (Exception e) {
        }
        return null;
    }
    
    public static String getSystemDate(String format) {
        final SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(new Date(System.currentTimeMillis()));
    }
    
    public static String getSystemDate() {
        return formatDate(new Date(System.currentTimeMillis()));
    }

    public static String getSystemTime() {
        return formatDateTime(new Date(System.currentTimeMillis()));
    }
    
    public static DateInfo SetDateDeiff(int nCount, DateInfo dateInfo) {
        try {
            Date date = new Date(dateInfo.getYear() - 1900, dateInfo.getMonth() - 1, dateInfo.getDay());    
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, nCount);
            date = c.getTime();
            dateInfo.setYear(date.getYear() + 1900);
            dateInfo.setMonth(date.getMonth() + 1);
            dateInfo.setDay(date.getDate());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return dateInfo;
    }
    
    public static int dip2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);

    }
    
    public static int dip2px(float dipValue) {
        return (int) (getRawSize(TypedValue.COMPLEX_UNIT_DIP, dipValue) + 0.5f );
    }
    
    private static float mw = 0.0F;
	
	/**
	 * <br>Description: 根据屏幕宽比例转化插件图片大小
	 * <br>Author:caizp
	 * <br>Date:2014年10月10日下午4:53:38
	 * @param size
	 * @return
	 */
    public static int convertSize(int size) {
		if (mw == 0.0F) {
			DisplayMetrics localDisplayMetrics;
			mw = Math.min((localDisplayMetrics = Resources.getSystem()
					.getDisplayMetrics()).widthPixels,
					localDisplayMetrics.heightPixels) / 320.0F;
		}
		return (int) (size * mw);
	}
    
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString((messageDigest[i] & 0x000000FF) | 0xFFFFFF00)
                        .substring(6));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @brief 【检查activity是否已启动】
     * 
     * @n<b>函数名称</b> :checkActivity
     * @param context
     * @param intent
     * @return
     * @return boolean
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-2-17下午02:33:32
     */
    public static boolean checkActivity(Context context, Intent intent) {
        try {
            if (context.getPackageManager().resolveActivity(intent, 0) != null) {
                return false;
            }
        } catch (Exception e) {
        }

        return true;
    }
    
    /**
     * 以最省内存的方式读取本地资源的图片
     * 
     * @param context
     *@param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
    
	public static float getRawSize(int unit, float size) {

		Resources r = Resources.getSystem();
		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
	
    /**
     * @brief 【根据报名检查apk是否已经安装】
     * @n<b>函数名称</b> :checkApkExist
     * @param context
     * @param packageName
     * @return
     * @return boolean
     * @<b>作者</b> : 游东宝
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-8-20下午05:08:34
     * @<b>修改时间</b> :
     */
    public static boolean checkApkExist(Context context, String packageName) {
        return checkApkExist(context, packageName, 0);
    }
    
    /**
     * @Title: checkApkExist  
     * @Description: (判断是否存在应用，并判断版本号)  
     * @author yanyy
     * @date 2012-12-5 下午07:10:47 
     *
     * @param context
     * @param packageName
     * @param verisonCode
     * @return       
     * @return boolean
     * @throws
     */
    public static boolean checkApkExist(Context context, String packageName, int verisonCode) {
        return (/*verisonCode > 0 && */getAppVersion(context, packageName) >= verisonCode);
    }
    
    public static int getAppVersion(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            try {
                PackageInfo app = context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.GET_UNINSTALLED_PACKAGES);
                return app.versionCode;
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return -1;
    }
    
    /**
     * @Title: checkProviderExists  
     * @Description: TODO(检查ContentProvider是否有存在)  
     * @author yanyy
     * @date 2012-11-21 上午11:46:06 
     *
     * @param context
     * @param authority
     * @return       
     * @return boolean
     * @throws
     */
    public static boolean checkProviderExists(Context context, String authority) {
        try{
            ProviderInfo pi = context.getPackageManager().resolveContentProvider(authority, 0);
            return (pi != null);
        }catch (Exception e) {
        }
        return false;
    }
    
    /**
     * @Title: getTimeZomeRawOffset  
     * @Description: TODO(获取时区标准时间偏差)  
     * @author yanyy
     * @date 2012-11-26 上午09:36:51 
     *
     * @param gmt
     * @return       
     * @return int
     * @throws
     */
    private static int getTimeZomeRawOffset(String gmt) {
        try {
            if (!TextUtils.isEmpty(gmt)) {
                if ((gmt.indexOf('-') == -1) && (gmt.indexOf('+') == -1)) {
                    gmt = '+' + gmt;
                }
                return TimeZone.getTimeZone("GMT" + gmt).getRawOffset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 无法处理的都当作是本地时区
        return TimeZone.getDefault().getRawOffset();
    }
    
    /**
     * @Title: isChinaTimeZome  
     * @Description: TODO(是否是本地时区)  
     * @author yanyy
     * @date 2012-11-25 上午11:06:12 
     *
     * @return       
     * @return boolean
     * @throws
     */
    public static boolean isLocalTimeZome(String gmt) {
        return TimeZone.getDefault().getRawOffset() == getTimeZomeRawOffset(gmt);
    }
    
    /**
     * @Title: getLocalServerDate  
     * @Description: TODO(把服务器时间转化成本地时间)  
     * @author yanyy
     * @date 2012-11-26 上午09:29:27 
     *
     * @param date
     * @return       
     * @return Date
     * @throws
     */
    public static Date getLocalServerDate(Date date){
        TimeZone tZoneLocal = TimeZone.getDefault();
        if (tZoneLocal.getRawOffset() != SERVER_TIME_ZOME) {
            return new Date(date.getTime() - SERVER_TIME_ZOME + tZoneLocal.getRawOffset());
        } else {
            return date;
        } 
    }
    
    /**
     * @Title: getLocalCityDate  
     * @Description: TODO(获取城市的当地时间)  
     * @author yanyy
     * @date 2012-11-26 上午09:39:50 
     *
     * @param gmt
     * @return       
     * @return Date
     * @throws
     */
    public static Date getLocalCityDate(String gmt){
        Date now = new Date(System.currentTimeMillis());
        TimeZone tZoneLocal = TimeZone.getDefault();
        int cityRawOffset = getTimeZomeRawOffset(gmt);
        if (tZoneLocal.getRawOffset() != cityRawOffset) {
            return new Date(now.getTime() - tZoneLocal.getRawOffset() + cityRawOffset);
        } else {
            return now;
        } 
    }
    
    /**
     * @Title: getCityServerDate  
     * @Description: TODO(把服务器时间转化成城市时区对应的时间)  
     * @author yanyy
     * @date 2012-11-26 上午10:54:56 
     *
     * @param date
     * @param gmt
     * @return       
     * @return Date
     * @throws
     */
    public static Date getCityServerDate(Date date, String gmt){
        int tZoneLocal = getTimeZomeRawOffset(gmt);
        if (tZoneLocal != SERVER_TIME_ZOME) {
            return new Date(date.getTime() - SERVER_TIME_ZOME + tZoneLocal);
        } else {
            return date;
        } 
    }
    
    /**根据资源名获得资源
     * @param ctx
     * @param resName
     * @return
     */
    public static int getResIdByName(Context ctx, String resName){
    	int resId = 0;
    	try{
    		resId = ctx.getResources().getIdentifier(resName,
				"drawable", ctx.getPackageName());
    	}catch (Exception e){
    		
    	}
    	return resId;
    }
}
