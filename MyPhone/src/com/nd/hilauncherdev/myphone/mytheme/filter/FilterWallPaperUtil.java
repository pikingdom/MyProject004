package com.nd.hilauncherdev.myphone.mytheme.filter;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.WallpaperUtil;
import com.nd.hilauncherdev.myphone.mytheme.net.NetOptCommonApi;
import com.nd.hilauncherdev.myphone.mytheme.net.ResultCodeMap;
import com.nd.hilauncherdev.myphone.mytheme.net.ServerResult;

public class FilterWallPaperUtil {


	public static String buildFilterUrl(Context ctx, String baseUrl){
		StringBuffer buffer = new StringBuffer();
		buffer.append(baseUrl);
		buffer.append("&divideversion=");
		buffer.append(com.nd.hilauncherdev.kitset.util.TelephoneUtil.getVersionName(ctx, ctx.getPackageName()));
        buffer.append(CUIDUtil.getCUIDPART());
		return buffer.toString();
	}

	public static ServerResult<FilterItem> getFilterWallPaperList(Context ctx,String requestURL, int iPageIndex, int iPageSize){
		if(requestURL.indexOf("&rslt=") <= -1){
			String rslt = "480*800";
			int w = ScreenUtil.getScreenWH()[0];
			if (w > 320){
				int[] sWH = WallpaperUtil.CoverToNetWallPaperWH(ScreenUtil.getCurrentScreenWidth(ctx), ScreenUtil.getCurrentScreenHeight(ctx));
				rslt = sWH[0] + "*" + sWH[1];
			}
			requestURL = requestURL + "&rslt=" + rslt;
		}
		requestURL = requestURL + getCommonUrl()+"&pi="+iPageIndex;
		ServerResult<FilterItem> softListResult = new ServerResult<FilterItem>();
		String jsonString = NetOptCommonApi.getURLContent(requestURL);
		if( null == jsonString ) {
			softListResult.bNetworkProblem = true;
			return softListResult;
		}
		try{
			JSONObject jsonData = new JSONObject(jsonString);
			int code = jsonData.getInt("Code");
			softListResult.resultCode = code;
			if(code == ResultCodeMap.APP_SUCCESS_CODE){
				JSONObject responseObj = jsonData.getJSONObject("Result");
				softListResult.atLastPage = responseObj.getBoolean("atLastPage");
				JSONArray listJson = responseObj.getJSONArray("items");
				for(int i=0;i<listJson.length();i++){
					JSONObject item =  listJson.getJSONObject(i);
					FilterItem wpItem = jsonToWallpaperItemForList(item);
					if ( wpItem!=null ){
						softListResult.itemList.add(wpItem);
					}
				}
			}
		}catch(Exception ex){
			softListResult.resultCode = ResultCodeMap.GET_LIST_FAIL_CODE;
			ex.printStackTrace();
		}

		return softListResult;
	}

	/**
	 * 滤镜解析
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	private static FilterItem jsonToWallpaperItemForList(JSONObject json) throws Exception{
		String detailUrl= "";
		FilterItem wpItem = new FilterItem();
		wpItem.id = json.getString("resId");
		wpItem.name = json.getString("name");
		wpItem.size = json.getString("size");

		detailUrl = json.getString("detailUrl");
		if(detailUrl.equals("") || detailUrl == null){
			wpItem.detailUrl = json.getString("downloadUrl");
		}
		String strIcon = json.getString("icon");
		String strDownloadUrl = json.getString("downloadUrl");
		// URL地址为空情况
		if (strIcon == null || strIcon.trim().length() == 0){
			return null;
		}

		if (strDownloadUrl == null || strDownloadUrl.trim().length() == 0){
			return null;
		}

		URL sURL = new URL(strIcon.trim());
		wpItem.thumbUrl = sURL;

		URL rURL = new URL(strDownloadUrl);
		wpItem.downloadUrl = rURL;

		return wpItem;
	}

	public static String getCommonUrl() {
		Context ctx = CommonGlobal.getApplicationContext();
		StringBuilder sb = new StringBuilder("&sv=");
		try {
			sb.append(URLEncoder.encode(TelephoneUtil.getVersionName(ctx), "UTF-8"));
			sb.append("&osv=");// 固件
			sb.append(URLEncoder.encode("" + Build.VERSION.SDK_INT, "UTF-8"));
			sb.append("&cpu=");
			sb.append(URLEncoder.encode(TelephoneUtil.getCPUABI(), "UTF-8"));
			sb.append("&imei=");
			String tempImei = TelephoneUtil.getIMEI(ctx);
			if (StringUtil.isEmpty(tempImei)) {
				sb.append(URLEncoder.encode("0", "UTF-8"));
			} else {
				sb.append(URLEncoder.encode(tempImei, "UTF-8"));
			}
			sb.append("&imsi=");
			String tempImsi = TelephoneUtil.getIMSI(ctx);
			if (StringUtil.isEmpty(tempImsi)) {
				sb.append(URLEncoder.encode("0", "UTF-8"));
			} else {
				sb.append(URLEncoder.encode(tempImsi, "UTF-8"));
			}
			sb.append("&nt=");
			sb.append(URLEncoder.encode(TelephoneUtil.getNT(ctx), "UTF-8"));
			sb.append("&dm=");
			sb.append(URLEncoder.encode(Build.MODEL, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}// 客户端版本号

		return sb.toString();
	}
}
