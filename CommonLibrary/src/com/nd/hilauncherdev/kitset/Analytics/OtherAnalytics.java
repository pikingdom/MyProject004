package com.nd.hilauncherdev.kitset.Analytics;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

/**
 * 新增的数据统类，不依赖于数据统计的jar包
 */
public class OtherAnalytics {

	private final static String TAG="OtherAnalytics";
	/**
	 * 统计一键装机界面打开次数
	 * @param context
	 * @return boolean
	 */
	public static boolean submitAppNecessaryOpen(Context context)
	{
		String format=OtherAnalyticsConstants.FORMAT_JSON;
		int fid=OtherAnalyticsConstants.FUNC_ID_APP_MARKET_APP_NECESSARY_OPEN;
		boolean res=submitNormalAnalyticsEvent(format, fid,context,null);
		return res;
	}

	/**
	 * 统计热门游戏界面打开次数
	 * @param context
	 * @return boolean
	 */
	public static boolean submitAppGameOpen(Context context)
	{
		String format=OtherAnalyticsConstants.FORMAT_JSON;
		int fid=OtherAnalyticsConstants.FUNC_ID_APP_MARKET_APP_GAME_OPEN;
		boolean res=submitNormalAnalyticsEvent(format, fid,context,null);
		return res;
	}

	/**
	 * 获取桌面分享带统计功能的资源
	 * @param context
	 * @return String
	 */
	public static String getLaucherShareResContent(Context context)
	{
		String format=OtherAnalyticsConstants.FORMAT_JSON;
		int fid=OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_LAUNCHER_SHARE;
		int act=OtherAnalyticsConstants.ACT_ID_LAUNCHER_SHARE;
		return submitResContentAnalyticsEvent(format, fid, act, context,null);
	}

	/**
	 * 获取带统计的'91助手'下载地址
	 * @param context
	 * @return String
	 */
	public static String get91AssistAppDownloadUrl(Context context)
	{
		String format=OtherAnalyticsConstants.FORMAT_JSON;
		int fid=OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_91_ASSIT_APP_URL;
		int act=OtherAnalyticsConstants.ACT_ID_GET_91_ASSIT_APP_URL;
		int extName=OtherAnalyticsConstants.EXT_NAME_APK;
		return submitResDownloadUrlContentAnalyticsEvent(format, fid, act, context, null, extName);
	}

	/**
	 * 通过主题商城下载应用
	 * @param context
	 * @param fid
	 * @param act
	 * @return String
	 */
	public static String get91ThemeShopAppDownloadUrl(Context context, int fid, int act){
		String lbl = "FromThemeShop";
		String format=OtherAnalyticsConstants.FORMAT_JSON;
		int extName=OtherAnalyticsConstants.EXT_NAME_APK;

		String url=null;
		try {
			//产品ID
			int pid=Integer.parseInt(context.getString(R.string.app_pid));
			//获取接口地址
			url=OtherAnalyticsConstants.getDownloadUrlResContentAnalyticsUrl(fid, act, format, context,pid, lbl, extName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * 提交统计数据
	 * @param format 返回数据的格式
	 * @param fid 功能ID
	 * @param context
	 * @param lbl 同个功能id不同拓展的请传入此参数,汉字请urlencode编码
	 * @return boolean
	 */
	private static boolean submitNormalAnalyticsEvent(String format,int fid,Context context,String lbl)
	{
		try {
			if(!TelephoneUtil.isNetworkAvailable(context))
				return false;
			//产品ID
			int pid=Integer.parseInt(context.getString(R.string.app_pid));
			//获取接口地址
			String url=OtherAnalyticsConstants.getNormalAnalyticsUrl(fid, format, context,pid,lbl);
			logDebug("OtherAnalytics submitNormalAnalyticsEvent url:"+url);
			HttpCommon httpCommon=new HttpCommon(url);
			String responseString=httpCommon.getResponseAsStringGET(null);
			logDebug("OtherAnalytics submitNormalAnalyticsEvent response string:"+responseString);

			//校验返回结果，分JSON和XML格式两种
			Object[] result=null;
			if(format.equals(OtherAnalyticsConstants.FORMAT_JSON)){
				result= checkResponseJSONValidate(responseString);
			}else if(format.equals(OtherAnalyticsConstants.FORMAT_XML))
				result= checkResponseXMLValidate(responseString);
			if(result!=null && ((Integer)result[0])==0)
				return true;

		} catch (Exception e) {
			Log.w(TAG, "submitNormalAnalyticsEvent expose exception!",e);
		}

		return false;
	}

	/**
	 * 提交带返回资源的统计接口
	 * @param format 返回数据的格式
	 * @param fid 功能ID
	 * @param act 动作值
	 * @param context
	 * @param lbl 同个功能id不同拓展的请传入此参数,汉字请urlencode编码
	 * @return String
	 */
	private static String submitResContentAnalyticsEvent(String format,int fid,int act,Context context,String lbl)
	{

		try {
			if(!TelephoneUtil.isNetworkAvailable(context))
				return null;

			//产品ID
			int pid=Integer.parseInt(context.getString(R.string.app_pid));
			//获取接口地址
			String url=OtherAnalyticsConstants.getResContentAnalyticsUrl(fid, act, format, context,pid,lbl);
			logDebug("OtherAnalytics submitResContentAnalyticsEvent url:"+url);
			HttpCommon httpCommon=new HttpCommon(url);
			String responseString=httpCommon.getResponseAsStringGET(null);
			logDebug("OtherAnalytics submitResContentAnalyticsEvent response string:"+responseString);
			//校验返回结果，分JSON和XML格式两种
			Object[] result=null;
			if(format.equals(OtherAnalyticsConstants.FORMAT_JSON)){
				result= checkResponseJSONValidate(responseString);
			}else if(format.equals(OtherAnalyticsConstants.FORMAT_XML))
				result= checkResponseXMLValidate(responseString);
			if(result!=null && ((Integer)result[0])==0)
				return (String)result[1];
		} catch (Exception e) {
			Log.w(TAG, "submitResContentAnalyticsEvent expose exception!",e);
		}
		return null;
	}

	/**
	 *
	 * 提交带返回资源的统计接口
	 * @param format 返回数据的格式
	 * @param fid 功能ID
	 * @param act 动作值
	 * @param context
	 * @param lbl 同个功能id不同拓展的请传入此参数,汉字请urlencode编码
	 * @param extName 下载资源的扩展名标识 Pxl 1 ,ipa 2,apk 3
	 * @return String
	 */
	private static String submitResDownloadUrlContentAnalyticsEvent(String format,int fid,int act,Context context,String lbl,int extName)
	{
		try {
			//产品ID
			int pid=Integer.parseInt(context.getString(R.string.app_pid));
			//获取接口地址
			String url=OtherAnalyticsConstants.getDownloadUrlResContentAnalyticsUrl(fid, act, format, context,pid, lbl, extName);
			logDebug("OtherAnalytics submitResDownloadUrlContentAnalyticsEvent url:"+url);
			HttpCommon httpCommon=new HttpCommon(url);
			String responseString=httpCommon.getResponseAsStringGET(null);
			logDebug("OtherAnalytics submitResDownloadUrlContentAnalyticsEvent response string:"+responseString);
			//校验返回结果，分JSON和XML格式两种
			Object[] result=null;
			if(format.equals(OtherAnalyticsConstants.FORMAT_JSON)){
				result= checkResponseJSONValidate(responseString);
			}else if(format.equals(OtherAnalyticsConstants.FORMAT_XML))
				result= checkResponseXMLValidate(responseString);
			if(result!=null && ((Integer)result[0])==0)
				return (String)result[1];
		} catch (Exception e) {
			Log.w(TAG, "submitResDownloadUrlContentAnalyticsEvent expose exception!",e);
		}
		if(!TelephoneUtil.isNetworkAvailable(context))
			return null;

		return null;
	}



	/**
	 * 检验提交统计是否成功(JSON结果)
	 * @param responseString 返回的数据
	 * @return Object[] 返回的数据数组，第一个元素：返回的Code：0成功，否则失败;第二个元素：成功且有带返回资源的情况下的资源数据
	 */
	private static Object[] checkResponseJSONValidate(String responseString)
	{
		//返回的数据数组，第一个元素：返回的Code：0成功，否则失败;第二个元素：成功且有带返回资源的情况下的资源数据
		Object[] resArray=new Object[]{-1,null};

		try {
			JSONObject jsonObj=new JSONObject(responseString);
			int code=jsonObj.getInt("Code");
			resArray[0]=code;
			if(code==0){ //为0表示提交成功

				//有返回资源的情况获取返回的资源
				JSONObject resultJson=jsonObj.getJSONObject("Result");
				if(resultJson!=null){
					//返回的Content结节
					String content=resultJson.getString("Content");
					//返回的是DownloadUrl节点
					String downloadUrl=resultJson.getString("DownloadUrl");
					if(!TextUtils.isEmpty(content))
						resArray[1]=content;
					else if(!TextUtils.isEmpty(downloadUrl))
						resArray[1]=downloadUrl;
				}

			}else{
				String msg=jsonObj.getString("Msg");//失败的消息
				Log.w(CommonGlobal.TAG, "OtherAnalytics checkResponseJSONValidate invalidate:"+responseString+" message:"+msg);
			}

		} catch (Exception e) {
			Log.w(CommonGlobal.TAG, "OtherAnalytics checkResponseJSONValidate failed:"+e.toString());
		}

		return resArray;

	}//end checkResponseJSONValidate

	/**
	 * 检验提交统计是否成功(XML结果)
	 * @param responseString 返回的数据
	 * @return @return Object[] 返回的数据数组，第一个元素：返回的Code：0成功，否则失败;第二个元素：成功且有带返回资源的情况下的资源数据
	 */
	private static Object[] checkResponseXMLValidate(String responseString)
	{
		//返回的数据数组，第一个元素：返回的Code：0成功，否则失败;第二个元素：成功且有带返回资源的情况下的资源数据
		Object[] resArray=new Object[]{-1,null};

		try {
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         Document document = builder.parse(new ByteArrayInputStream(responseString.getBytes()));
	         // 得到根元素
	         Element root = document.getDocumentElement();
	         NodeList nodeList = root.getElementsByTagName("code");
	         Node codeNode=nodeList.item(0);
	         String code=codeNode.getFirstChild().getNodeValue();
	         int intCode=Integer.parseInt(code);
	         resArray[0]=intCode;
	         if(intCode==0){

	        	 //返回资源,<content>节点
	        	 NodeList contentNodeList=root.getElementsByTagName("content");
	        	//返回资源,<DownloadUrl>节点
	        	 NodeList DownloadUrlNodeList=root.getElementsByTagName("DownloadUrl");

	        	 if(contentNodeList!=null && contentNodeList.getLength()>0)
	        	 {
	        		 Node contentNode=contentNodeList.item(0);
	        		 resArray[1]=contentNode.getFirstChild().getNodeValue();
	        	 }else if(DownloadUrlNodeList!=null && DownloadUrlNodeList.getLength()>0){
	        		 Node DownloadUrlNode=DownloadUrlNodeList.item(0);
	        		 resArray[1]=DownloadUrlNode.getFirstChild().getNodeValue();
	        	 }

	         }else
	        	 Log.w(CommonGlobal.TAG, "OtherAnalytics checkResponseXMLValidate invalidate:"+responseString);

		} catch (Exception e) {
			Log.w(CommonGlobal.TAG, "OtherAnalytics checkResponseXMLValidate failed:"+e.toString());
		}

		return resArray;
	}

	private static void logDebug(String msg)
	{
		boolean logOpen=false;
		if(logOpen)
			Log.d(CommonGlobal.TAG, msg);
	}
}





