/**   
 *    
 * @file  【应用层网络请求接口】
 * @brief
 *
 * @<b>文件名</b>      : HttpAppFunClient
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-3 下午02:17:07 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.communication.http;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.TextUtils;

import com.calendar.CommData.SendSuggestInfo;
import com.nd.calendar.common.ComDataDef.CalendarData;
import com.nd.calendar.common.ComDataDef.ConfigSet;
import com.nd.calendar.common.ComDataDef.HttpURLData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.StringHelp;
import com.nd.calendar.util.SysHelpFun;
import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.net.ThemeLibUtil;
import com.nd.weather.widget.R;

public class HttpAppFunClient
{
	private final static String WEATHER_OPTION_WEATHER = "weather";
	private final static String WEATHER_OPTION_NOW = "now";
	private final static String WEATHER_OPTION_INDEX = "tqzs";
	private final static String WEATHER_OPTION_WARNING = "tqyj";
	private final static String WEATHER_OPTION_SUN = "sun";
	private final static String WEATHER_OPTION_PM = "pm";
	private final static String WEATHER_REQUEST_KEY = "asdfjklp135890asdfjk133ler89askdf";
	public final static int OPTION_WEATHER = 1;
	public final static int OPTION_NOW = 2;
	private Context mContext = null;
	
	private static final String WEATHER_EXT_OPTION = WEATHER_OPTION_INDEX + "," + WEATHER_OPTION_SUN + ","
			+ WEATHER_OPTION_PM + "," + WEATHER_OPTION_WARNING;

	public HttpAppFunClient(Context context) {
		mContext = context;
		m_httpToolKit = new HttpToolKit(context);
	}

	// ///////////////////////////////////////////////////////////////////

	/**
	 * @brief 【应用功能逻辑层函数访问】
	 * 
	 * 
	 * @n<b>函数名称</b> :GetAstrocommand
	 * @see
	 * 
	 * @param @param jsonObjectIn
	 * @param @param strResOut
	 * @param @param nFunId
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5上午10:17:39
	 */
	public boolean GetAstrocommand(String sid, JSONObject jsonObjectIn, StringBuffer strResOut, int nFunId, Date stdDate) {
		String sUrl = HttpURLData.APPFUN_OUT + "astrocommand";
		boolean bOutline = TextUtils.isEmpty(sid);
		if (!bOutline) {
			sUrl += "?sid=" + sid;
		} else if (stdDate == null) {
			return false;
		}

		JSONObject jsonIn = new JSONObject();
		try {
			jsonIn.put("comdcode", nFunId);
			jsonIn.put("paramcontent", jsonObjectIn.toString());
			jsonIn.put("chkcode", getFortuneCheckCode(nFunId, stdDate));
			
			if (m_httpToolKit.DoPost(sUrl, jsonIn, strResOut) != 200) {
				return false;
			}

			JSONObject jsonObjRes = new JSONObject(strResOut.toString());
			strResOut.delete(0, strResOut.length());
			strResOut.append(jsonObjRes.get("sresponbuf").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @brief 【运势校验码】
	 * @n<b>函数名称</b>     :getFortuneCheckCode
	 * @param nFunId
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-8-13下午03:21:04
	 * @<b>修改时间</b>      :
	*/
	private final String getFortuneCheckCode(int nFunId, Date stdDate) {
		final Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		final String strDate = formatter.format(stdDate);
		final String strS = nFunId + "|" + strDate + "|" + "158dabae317e4f68da7bd0c8e32034bc";
		return ComfunHelp.md5(strS);
	}
	
	/**
	 * @brief 【下载应用层数据】
	 * 
	 * 
	 * @n<b>函数名称</b> :DownDataPostAppFun
	 * @see
	 * 
	 * @param @param stroptPostUrl
	 * @param @param strVerCode
	 * @param @param jsonObjectIn
	 * @param @param strResOut
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5上午10:30:53
	 */
	public boolean DownDataPostAppFun(String sid, String stroptPostUrl, String strVerCode, String strOption,
			JSONObject jsonObjectIn, StringBuffer strResOut) {
		final String sUrl = HttpURLData.APPFUN_OUT + stroptPostUrl + "?sid=" + sid;
		JSONObject jsonIn = new JSONObject();
		try {
			jsonIn.put("option", strOption);
			jsonIn.put("vercode", strVerCode);
			jsonIn.put("param", jsonObjectIn);
			int ret = m_httpToolKit.DoPost(sUrl, jsonIn, strResOut);
			if (ret != 200 && ret != 204) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public boolean UploadDataPostAppFun(String sid, String stroptPostUrl, String strVerCode,
			String strOption, JSONObject jsonParam, JSONArray jsonData) {
		final String sUrl = HttpURLData.APPFUN_OUT + stroptPostUrl + "?sid=" + sid;

		JSONObject jsonIn = new JSONObject();
		StringBuffer strRes = new StringBuffer();

		try {
			jsonIn.put("option", strOption);
			jsonIn.put("vercode", strVerCode);
			jsonIn.put("param", jsonParam);
			jsonIn.put("data", jsonData);

			if (m_httpToolKit.DoPost(sUrl, jsonIn, strRes) != 200) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}
	// //////////////////////////////////// 天气 // ////////////////////////////////////////
	/**
	 * @brief 【获取实时天气-原优先级】
	 * 
	 * 
	 * @n<b>函数名称</b> :DownWeahterNow
	 * @see
	 * 
	 * @param @param strCode
	 * @param @param strResOut
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>修改</b> : 陈希
	 * @<b>创建时间</b> : 2011-7-26上午09:15:24
	 */
	public boolean DownloadWeahterNow(String strCode, StringBuffer strResOut, StringBuffer strMsg) {
		if (HttpURLData.SERVER_TEST_W || // 服务器测试
				DownloadWeatherNowOut(strCode, strResOut)) {
			strMsg.append(true);

			return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_NOW);
		}

		return true;
	}

	/**
	 * @brief 【获取日天气信息-原优先级】
	 * 
	 * @n<b>函数名称</b> :DownloadWeatherDay
	 * @param strCode
	 * @param strResOut
	 * @param sBackup
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-16下午04:45:12
	 */
	public boolean DownloadWeatherDay(String strCode, StringBuffer strResOut, StringBuffer sBackup) {

		if (HttpURLData.SERVER_TEST_W || // 服务器测试
				DownloadWeatherDayOut(strCode, strResOut)) {

			sBackup.append(true);
			return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_WEATHER);
		}

		return true;
	}

	/**
	 * @brief 【下载天气数据，整合版】
	 * 
	 * @n<b>函数名称</b> :DownloadWeahterInfo
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-12下午02:54:44
	 */
    public boolean DownloadWeahterInfo(String strCode, StringBuffer strResOut) {
        final String sOption = WEATHER_OPTION_WEATHER + "," + WEATHER_OPTION_NOW + ","
                + WEATHER_OPTION_INDEX + "," + WEATHER_OPTION_SUN + "," + WEATHER_OPTION_WARNING
                + "," + WEATHER_OPTION_PM;
        
        strResOut.delete(0, strResOut.length());
        // 从黄历天气服务器下载
        boolean bRet = DownloadWeatherFromServer(strCode, strResOut, sOption);
        // 下载错误，但 > 0 可能为参数错误，不需要从服务器再试
        if ((!bRet) && (strResOut.length() == 0)) {
            // 重试 从服务器下载
            bRet = DownloadWeatherFromServer(strCode, strResOut, sOption);
        }

        if (!bRet) {
            // 从外网下载天气与实时天气
            strResOut.delete(0, strResOut.length());

            StringBuffer sWeatherBuf = new StringBuffer();
            StringBuffer sWeatherNowBuf = new StringBuffer();

            boolean bSuccess = (DownloadWeatherDayOut(strCode, sWeatherBuf)
                    && DownloadWeatherNowOut(strCode, sWeatherNowBuf));
            
            if (bSuccess) {
                JSONObject weatherJson = StringHelp.getJSONObject(sWeatherBuf.toString());
                
                // 有可能取不到天气
                if (weatherJson == null) {
                    return false;
                }

                try {
                    JSONObject weatherNowJson = StringHelp.getJSONObject(sWeatherNowBuf.toString());
                    JSONObject dayWeatherJson = weatherJson.getJSONObject("weatherinfo");
                    String time = dayWeatherJson.getString("date_y");
                    JSONObject nowJson = weatherNowJson.getJSONObject("weatherinfo");
                    // 为了跟黄历天气接口一致(转化成 yyyy-MM-dd)
                    time = time.replace("年", "-").replace("月", "-").replace("日", "") + " " + nowJson.getString("time");
                    nowJson.put("time", time);
                    // 紫外线(放实时天气这边)
                    nowJson.put("index_uv", dayWeatherJson.getString("index_uv"));
                    // 实时天气
                    nowJson.put("weather", dayWeatherJson.getString("img_title1"));
                    
                    weatherJson.put("now", nowJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sWeatherBuf = null;
                sWeatherNowBuf = null;
                strResOut.append(weatherJson.toString());
            }
            
            return bSuccess;
        }

        return true;
    }

	// /////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【从外网下载日天气数据】
	 * 
	 * @n<b>函数名称</b> :DownloadWeatherDayOut
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-12下午02:34:23
	 */
	private boolean DownloadWeatherDayOut(String strCode, StringBuffer strResOut) {
		final String sUrl = HttpURLData.WEATHER_OUT + strCode + ".html";
		return (m_httpToolKit.DoGet(sUrl, strResOut, 15000) == 200);
	}

	/**
	 * @brief 【从外网下载实时天气数据】
	 * 
	 * @n<b>函数名称</b> :DownloadWeatherNowOut
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-12下午02:36:43
	 */
	private boolean DownloadWeatherNowOut(String strCode, StringBuffer strResOut) {
		final String sUrl = HttpURLData.WEATHER_NOW_OUT + strCode + ".html";
		return (m_httpToolKit.DoGet(sUrl, strResOut, 15000) == 200);
	}

	/**
	 * @brief 【下载生活指数】
	 * 
	 * @n<b>函数名称</b> :DownloadIndexLiving
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-16下午05:29:06
	 */
	public boolean DownloadIndexLiving(String strCode, StringBuffer strResOut) {
		return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_INDEX + ","
				+ WEATHER_OPTION_SUN);
	}

	/**
	 * @brief 【下载预警信息】
	 * 
	 * @n<b>函数名称</b> :DownloadWarning
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-16下午05:30:19
	 */
	public boolean DownloadWarning(String strCode, StringBuffer strResOut) {
		return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_WARNING);
	}

	/**
	 * @brief【下载日出日落信息】
	 * 
	 * @n<b>函数名称</b> :DownloadSun
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-11下午06:52:17
	 */
	public boolean DownloadSun(String strCode, StringBuffer strResOut) {
		return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_SUN);
	}

	/**
	 * @brief 【下载PM指数】
	 * @n<b>函数名称</b> :DownloadPM
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-6-1上午09:40:50
	 * @<b>修改时间</b> :
	 */
	public boolean DownloadPM(String strCode, StringBuffer strResOut) {
		return DownloadWeatherFromServer(strCode, strResOut, WEATHER_OPTION_PM);
	}

	/**
	 * @brief 【下载指数扩展数据】
	 * @n<b>函数名称</b> :DownloadIndexExt
	 * @param strCode
	 * @param strResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-6-1上午09:41:06
	 * @<b>修改时间</b> :
	 */
	public boolean DownloadIndexExt(String strCode, StringBuffer strResOut) {
		return DownloadWeatherFromServer(strCode, strResOut, WEATHER_EXT_OPTION);
	}

	/**
	 * @brief 【从公司服务器获取数据】
	 * 
	 * @n<b>函数名称</b> :DownloadWeatherFromServer
	 * @param strCode
	 * @param strResOut
	 * @param strOption
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-16下午04:42:51
	 */
	public boolean DownloadWeatherFromServer(String strCode, StringBuffer strResOut, String strOption) {
//		JSONObject jsonIn = new JSONObject();
		final Date sysdate = new Date(System.currentTimeMillis());

		try {
//			jsonIn.put("citycode", strCode);
//			jsonIn.put("option", strOption);
//			jsonIn.put("chkcode", GenerateWeatherCheckCode(strCode, strOption));
//			jsonIn.put("date", ComfunHelp.formatDate(sysdate));
//			jsonIn.put("productid", String.valueOf(CalendarData.GET_APPID()));
//			jsonIn.put("vercode", SysHelpFun.getAppVersionName(mContext));
			String nt = TelephoneUtil.getNT(mContext);
			String imei = HttpCommon.utf8URLencode(ThemeLibUtil.getIMEI(mContext));
			String osv = HttpCommon.utf8URLencode(Build.VERSION.RELEASE);
			String CUID = URLEncoder.encode(ThemeLibUtil.getCUID(mContext), "UTF-8");
			String chl = ChannelUtil.getChannel(mContext);
			
			String strIn = "&mt=4";
			strIn += "&city=" + strCode;
			strIn += "&option=" + strOption;
			strIn += "&chkcode=" + GenerateWeatherCheckCode(strCode, strOption);
			strIn += "&date=" + ComfunHelp.formatDate(sysdate);
			strIn += "&productid=" + String.valueOf(CalendarData.GET_APPID());
			strIn += "&vercode=" + SysHelpFun.getAppVersionName(mContext);
			strIn += "&imei=" + imei;
			strIn += "&dm=" + HttpCommon.utf8URLencode(Build.MODEL);
			strIn += "&osv=" + osv;
			strIn += "&CUID=" + CUID;
			strIn += "&chl=" + chl;
			strIn += "&pid=" + mContext.getResources().getString(R.string.product_id);
			strIn += "&nt=" + nt;
			strIn += "&homesv="+TelephoneUtil.getVersionName(mContext);
			
			String weatherMainStrIn = "&mt=4&isfamily=0";
			weatherMainStrIn += "&city=" + strCode;
			weatherMainStrIn += "&nt=" + nt;
			weatherMainStrIn += "&imei=" + imei;
			weatherMainStrIn += "&pid=118";
			weatherMainStrIn += "&osv=" + osv;
			weatherMainStrIn += "&dm=" + HttpCommon.utf8URLencode(Build.MODEL);
			weatherMainStrIn += "&CUID=" + CUID;
			weatherMainStrIn += "&chl=" + chl;
			weatherMainStrIn += "&ol=" + SHA1(Secure.getString(mContext.getContentResolver(),
		            Secure.ANDROID_ID));
			weatherMainStrIn += "&sv=3.14.4";
			weatherMainStrIn += "&homesv="+TelephoneUtil.getVersionName(mContext);
			
			StringBuffer weatherMainStrResOut = new StringBuffer();
			m_httpToolKit.DoGet(HttpCommon.utf8URLencode(HttpURLData.WEATHER_MAIN_PAGE_BASE_URL+weatherMainStrIn).replaceAll(" ", "%20"), weatherMainStrResOut, 15000);
			if(!TextUtils.isEmpty(weatherMainStrResOut)) {
				try {
					JSONObject data;
					data = new JSONObject(weatherMainStrResOut.toString());
			        JSONArray array = new JSONArray(data.optString("items"));
			        for (int i = 0; i < array.length(); i++) {
			            JSONObject obj = array.optJSONObject(i);
			            if (obj == null) continue;
		                int type = obj.optInt("type");
		                if (type == 300) {// 保存城市24小时天气
		                	ConfigHelper.getInstance(mContext).saveKey(strCode, obj.toString());
		    				ConfigHelper.getInstance(mContext).commit();
		                } else if (type == 100) {// 保存城市天气背景图地址
		                	JSONObject bgObj = obj.optJSONObject("bg");
		                	if (bgObj == null) continue;
		                	JSONObject staticObj = bgObj.optJSONObject("static");
		                	if (staticObj == null) continue;
		                	String clear = staticObj.optString("clear");
		                	if (TextUtils.isEmpty(clear)) continue;
		                	ConfigHelper.getInstance(mContext).saveKey(strCode+"_bg", clear);
		    				ConfigHelper.getInstance(mContext).commit();
		                }
			        }
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
//			int iret = m_httpToolKit.DoPost(HttpURLData.APPFUN_WEATHER, jsonIn, strResOut, 15000);
			int iret = m_httpToolKit.DoGet(HttpCommon.utf8URLencode(HttpURLData.APPFUN_WEATHER+strIn).replaceAll(" ", "%20"), strResOut, 15000);
			return (iret == HttpStatus.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

	/**
	 * @brief 【生成天气校验码】
	 * 
	 * @n<b>函数名称</b> :GenerateWeatherCheckCode
	 * @param sCityCode
	 * @param sOption
	 * @return
	 * @return String
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-4上午10:29:18
	 */
	private String GenerateWeatherCheckCode(String sCityCode, String sOption) {
		String strDate = ComfunHelp.formatDate(new Date(System.currentTimeMillis()));
		String strS = sCityCode + "|" + sOption + "|" + strDate + "|" + getCommKey();
		return ComfunHelp.md5(strS);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【下载 GPS 定位解析数据】
	 * 
	 * @n<b>函数名称</b> :DownloadGpsInfo
	 * @param latitude
	 * @param longitude
	 * @param strResOut
	 * @param strOption
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2011-11-1下午05:01:28
	 */
	public boolean DownloadGpsInfo(double latitude, double longitude, StringBuffer strResOut,
			String strOption) {
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("option", "city");
			jsonIn.put("jd", String.format("%.6f", longitude));
			jsonIn.put("wd", String.format("%.6f", latitude));
			jsonIn.put("chkcode", GenerateToolsCheckCode("city"));

			if (m_httpToolKit.DoPost(HttpURLData.APPFUN_TOOLS, jsonIn, strResOut, 15000) != 200) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

//	/**
//	 * @brief 【下载软件推送信息】
//	 * 
//	 * @n<b>函数名称</b> :DownloadSoftAD
//	 * @param iCurVersion
//	 * @param strResOut
//	 * @return
//	 * @return boolean
//	 * @<b>作者</b> : 陈希
//	 * @<b>创建时间</b> : 2011-11-11上午09:53:38
//	 */
//	public boolean DownloadSoftAD(int iCurVersion, int iUid, StringBuffer strResOut) {
//		String strURL = HttpURLData.APPFUN_SOFTAD;
//
//		m_httpToolKit.setURL(strURL);
//		JSONObject jsonIn = new JSONObject();
//
//		try {
//			jsonIn.put("option", "soft_ad");
//			jsonIn.put("version", iCurVersion);
//			jsonIn.put("uid", iUid); // 指定下载某一个(下载缺失图标)
//
//			if (m_httpToolKit.DoPost(jsonIn, strResOut, 15000) != 200) {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return true;
//	}

	public boolean DownverInfo(int appid, StringBuffer strResOut) {
		JSONObject jsonIn = new JSONObject();
		try {
			jsonIn.put("iSoftware", Integer.toString(appid));
			if (m_httpToolKit.DoPost(HttpURLData.APPFUN_VERINFO, jsonIn, strResOut) != 200) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public boolean DownSuggestanswer(String strContactinfo, SendSuggestInfo suggest) {
		final String sUrl = HttpURLData.APPFUN_SUGGEST + "sendsuggest";
		StringBuffer strResOut = new StringBuffer();
		if (m_httpToolKit.DoPost(sUrl, suggest.ToJsonObject(), strResOut) != 200) {
			return false;
		}
		return true;
	}

	public boolean Getanswer(JSONObject jsonObjectIn, StringBuffer strResOut) {
		final String sUrl = HttpURLData.APPFUN_SUGGEST + "getanswer";
		if (m_httpToolKit.DoPost(sUrl, jsonObjectIn, strResOut) != 200) {
			return false;
		}
		return true;
	}
//
//	public int DownSoftAdInfo(JSONObject jsonObjectIn, StringBuffer strResOut) {
//		String strOut = HttpURLData.HOST_ADAPI_OUT + "uploadversoin";
//		m_httpToolKit.setURL(strOut);
//		return m_httpToolKit.DoPost(jsonObjectIn, strResOut);
//	}

	/**
	 * @brief 【下载桌面皮肤列表】
	 * 
	 * @n<b>函数名称</b> :DownWidgetSkinListInfo
	 * @param jsonObjectIn
	 * @param strResOut
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-2-7下午05:42:26
	 */
	public int DownWidgetSkinListInfo(JSONObject jsonObjectIn, StringBuffer strResOut) {
		return m_httpToolKit.DoPost(HttpURLData.APPFUN_SKINLIST, jsonObjectIn, strResOut);
	}

	private String GenerateToolsCheckCode(String sOption) {
		String strDate = ComfunHelp.formatDate(new Date(System.currentTimeMillis()));
		String strS = sOption + "|" + strDate + "|" + getCommKey();
		return ComfunHelp.md5(strS);
	}

	public String GetLoginServerDate() {
		return m_sLoginServerDate;
	}
	
	/**
	 * @brief 【获取服务器时间】
	 * @n<b>函数名称</b>     :getServerDate
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-8-2下午06:06:49
	 * @<b>修改时间</b>      :
	*/
	public String getServerDate() {
		StringBuffer strRes = new StringBuffer("");
		if (m_httpToolKit.DoPost(HttpURLData.APPFUN_TIME, null, strRes) != 200) {
			return null;
		}
		
		String sServerDate = null;
		
		try {
			JSONObject jsonOut = StringHelp.getJSONObject(strRes.toString());
			sServerDate = jsonOut.getString("sysdate");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sServerDate;
	}
	
	   /*通讯密钥解密*/
    private String keyDecrypt(String sbuf){
        if (TextUtils.isEmpty(sbuf)){
            return sbuf;
        }
        StringBuffer dbuf=new StringBuffer();
        int i,n,dwCount;
        String szPassword = "12ASD9ASDFJE3489JJee12";
        StringBuffer sourbuf=new StringBuffer();
        StringBuffer tmpbuf=new StringBuffer();
        tmpbuf.delete(0, tmpbuf.length());
        dbuf.delete(0, dbuf.length());
        dwCount=sbuf.length()/2;
        for(i=0;i<dwCount;i++){
            sourbuf.append((char)Integer.parseInt(sbuf.substring(i*2,(i+1)*2), 16));
        }
        n=0;
        for(i=0;i<dwCount;i++){
            char ch=(char) (sourbuf.charAt(i)^szPassword.charAt(n++));
            dbuf.append(ch);
            if (n >= (int)szPassword.length())
            {
                n=0;
            }
        }
        return dbuf.toString();
    }
    
    /*从服务器获取key*/
    private String getKeyFormServer(String strDate){
        String strURL = HttpURLData.APPFUN_GETKEY;
        JSONObject jsonIn = new JSONObject();

        try {
            String prodId = String.valueOf(CalendarData.GET_APPID());
            String vercode = SysHelpFun.getAppVersionName(mContext);
            jsonIn.put("productid", prodId);
            jsonIn.put("vercode", vercode);
            jsonIn.put("chkcode", ComfunHelp.md5(prodId + "|" + vercode + "|" + strDate + "|" + WEATHER_REQUEST_KEY));

            StringBuffer receive = new StringBuffer();
            int iret = m_httpToolKit.DoPost(strURL, jsonIn, receive, 15000);
            if (iret == HttpStatus.SC_OK) {
                JSONObject json = new JSONObject(receive.toString());
                String key = json.getString("key");
                if (!TextUtils.isEmpty(key)){
                    // 保存
                    saveKey(strDate, key);
                }
                return keyDecrypt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /*获取key*/
    private synchronized String getCommKey(){
//        long a = System.currentTimeMillis();
//        try{
        String strDate = ComfunHelp.formatDate(new Date(System.currentTimeMillis()));
        ConfigHelper cfg = ConfigHelper.getInstance(mContext);
        String key = cfg.loadKey(ConfigSet.CONFIG_NAME_KEY_COMM_KEY, null);
        if ((!TextUtils.isEmpty(key)) && (key.startsWith(strDate))){
            return keyDecrypt(key.replace(strDate, ""));
        }else{
            return getKeyFormServer(strDate);
        }
//        }finally{
//            Log.e("HttpAppFunClient", (System.currentTimeMillis() - a) + "");
//        }
    }
    
    /*保存key*/
    private void saveKey(String strDate, String key){
        ConfigHelper cfg = ConfigHelper.getInstance(mContext);
        cfg.saveKey(ConfigSet.CONFIG_NAME_KEY_COMM_KEY, strDate + key);
        cfg.commit();
    }

	private HttpToolKit m_httpToolKit = null;
	private String m_sLoginServerDate = null;
	
}
