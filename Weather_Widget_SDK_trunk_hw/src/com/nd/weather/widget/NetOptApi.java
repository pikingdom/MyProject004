package com.nd.weather.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.net.ResultCodeMap;
import com.nd.hilauncherdev.net.ServerResultHeader;
import com.nd.hilauncherdev.net.ThemeHttpCommon;
import com.nd.hilauncherdev.shop.api6.model.AdvertItem;
import com.nd.hilauncherdev.shop.api6.model.ThemeBannerItem;
import com.nd.hilauncherdev.shop.api6.net.ServerDetailResult;
import com.nd.hilauncherdev.shop.api6.net.ServerResult;

public class NetOptApi {
	
	/**Android 专辑 专题*/
	public static final int PLACEID_TAG = 50000170;	
	public static final String URI_91_KEY = "zm91txwd20141231";
	/**
	 * 91桌面资源页面调用的Uri头部
	 */
	public static final String URI_HEADER = URI_91_KEY + "://";
	public static final String INTENT_HEADER = "#Intent";
	public static final String HTTP_HEADER = "http://";
	
	/**国内服务器*/
	public final static String Panda_Space_Inland_Server = "http://pandahome.sj.91.com/";
	
	/**
	 * 八、	其它接口(9001-10000)
	 * @param actionCode
	 * @return
	 */
	private static String getOtherActionUrl(int actionCode){
		return Panda_Space_Inland_Server+"action.ashx/otheraction/"+actionCode;
	}
	
	/**
	 * 获取广告数据
	 * @param ctx
	 * @param sPositions  广告发布位置(多个用逗号隔开例如1,2) 位置参数见 @AdvertItem
	 * @return
	 */
	public static ServerResult<AdvertItem> getAdInfoList_9004(Context ctx, String sPositions){
		
		int acitonCode = 9004;
		String jsonParams = "";
		JSONObject jsonObject = new JSONObject();		
		try {
			jsonObject.put("Position", sPositions);
			jsonParams = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		ThemeHttpCommon.addGlobalRequestValue(paramsMap, ctx.getApplicationContext(), jsonParams);
		ThemeHttpCommon httpCommon = new ThemeHttpCommon(getOtherActionUrl(acitonCode));
		ServerResultHeader csResult = httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
		ServerResult<AdvertItem> adListResult = new ServerResult<AdvertItem>();
		if(csResult!=null){
			adListResult.setCsResult(csResult);
			if(adListResult.getCsResult().isRequestOK()){
				parseAdInfoList(adListResult);
			}
		}
		return adListResult;
	}
	
	/**
	 * 广告分发统计
	 * @param ctx
	 * @param resId 资源Id
	 * @param resType 资源类型 1=生活助手（包括一级列表，二级列表和Banner）2=广告
	 * @param position @AdvertItem 统计位置
	 * @param StatType  统计类型 1=点击 2=展示
	 * @return
	 */
	private static ServerDetailResult<Boolean> submitAdReport_9005(Context ctx, int resId, int resType, int position,int statType){
		
		int acitonCode = 9005;
		String jsonParams = "";
		JSONObject jsonObject = new JSONObject();		
		try {
			jsonObject.put("ResId", resId);
			jsonObject.put("ResType", resType);
			jsonObject.put("Position", position);
			jsonObject.put("StatType", statType);
			jsonParams = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		ThemeHttpCommon.addGlobalRequestValue(paramsMap, ctx.getApplicationContext(), jsonParams);
		ThemeHttpCommon httpCommon = new ThemeHttpCommon(getOtherActionUrl(acitonCode));
		ServerResultHeader csResult = httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
		ServerDetailResult<Boolean> adReportResult = new ServerDetailResult<Boolean>();
		adReportResult.detailItem = false;
		if(csResult!=null){
			adReportResult.setCsResult(csResult);
			if(adReportResult.getCsResult().isRequestOK()){
				adReportResult.detailItem = true;
			}
		}
		return adReportResult;
	}
	
	/**
	 * 广告分发统计
	 * @param ctx
	 * @param resId 资源Id
	 * @param resType 资源类型 1=生活助手（包括一级列表，二级列表和Banner）2=广告
	 * @param position @AdvertItem 统计位置
	 * @param StatType  统计类型 1=点击 2=展示
	 * @return
	 */
	public static void submitAdReport(final Context ctx, final int resId, final int resType, final int position, final int statType){
		
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				submitAdReport_9005(ctx, resId, resType, position, statType);
			}
		});
	}
	
	/**
	 * 列表主题对象解析
	 * @param sr
	 */
	private static void parseAdInfoList(ServerResult<AdvertItem> sr){
		String responseJson = sr.getCsResult().getResponseJson();
		try{
			JSONObject responseObj = new JSONObject(responseJson);
			JSONArray listJson = responseObj.optJSONArray("AdList");
			if (listJson!=null) {
				for(int i=0;i<listJson.length();i++){
					JSONObject jsonData = listJson.getJSONObject(i);
					
					AdvertItem adItem  = new AdvertItem();
					adItem.adItemId = jsonData.getInt("AdId");
					adItem.adPostion = jsonData.getInt("Position");
					adItem.adName = jsonData.optString("Title");
					adItem.adHeight = jsonData.getInt("Height");
					adItem.adWidth = jsonData.getInt("Width");
					adItem.adPicUrl = jsonData.optString("ImgUrl");
					adItem.actionUrl = jsonData.optString("LinkUrl");
					adItem.actionIntent = jsonData.optString("Action");
					adItem.splashTime = jsonData.getInt("ShowTime");
					sr.itemList.add(adItem);
				}
			}
		}catch(Exception ex){
			sr.getCsResult().setResultCode(ResultCodeMap.SERVER_RESPONSE_CODE_8800);
			ex.printStackTrace();
		}
	}
	
	/**
	 * 填充banner广告
	 * @param dataItemList
	 * @param adInfoList
	 */
	public static void addAdInfoToBanner(Context ctx, ArrayList<ICommonDataItem> dataItemList, ArrayList<AdvertItem> adInfoList){
		if (adInfoList!=null && adInfoList.size()>0){
			if (dataItemList==null){
				dataItemList = new ArrayList<ICommonDataItem>();
			}
			ArrayList<ICommonDataItem> tmpItemList = new ArrayList<ICommonDataItem>();
			AdvertItem adItem;
			for (int i = 0; i < adInfoList.size(); i++) {
				adItem = adInfoList.get(i);
				if (adItem.adPostion==AdvertItem.AD_POSTION_WEATHER_BANNER){
					ThemeBannerItem bannerItem = new ThemeBannerItem();
					bannerItem.bannerId = adItem.adItemId;
					bannerItem.bannerName = adItem.adName;
					bannerItem.bannerLinkUrl = adItem.actionIntent;
					bannerItem.bannerIconUrl = adItem.adPicUrl;
					bannerItem.bannerType = ThemeBannerItem.BANNER_FLAG_THEME_AD;
					tmpItemList.add(bannerItem);
					
					submitAdReport(ctx, adItem.adItemId, AdvertItem.AD_REPORT_RESTYPE_AD, 
							adItem.adPostion, AdvertItem.AD_REPORT_STATTYPE_SHOW);
				}
			}
			tmpItemList.addAll(dataItemList);
			dataItemList.clear();
			dataItemList.addAll(tmpItemList);
		}
	}
	
	/**
	 * 处理头部为zm91txwd20141231://、http://、#Intent的uri
	 * @param uri
	 * @param notHandleHttp 是否处理http://
	 * @return
	 */
	public static boolean handleUriEx(String uri, boolean notHandleHttp){
		boolean success = false;
		if(StringUtil.isEmpty(uri))
			return success;
		Intent intent = null;
		Context mContext = BaseConfig.getApplicationContext();
		try{
			if(uri.startsWith(URI_HEADER)){
				intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
			} else if(uri.startsWith(INTENT_HEADER)){
				intent = Intent.parseUri(uri, 0);
			} else if(uri.startsWith(HTTP_HEADER) && !notHandleHttp){
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			}
			if(intent != null){
				SystemUtil.startActivitySafely(mContext, intent);
				success = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return success;
	}
	
}
