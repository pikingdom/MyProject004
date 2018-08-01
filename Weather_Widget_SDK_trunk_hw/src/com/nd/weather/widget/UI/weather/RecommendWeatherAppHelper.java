package com.nd.weather.widget.UI.weather;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.framework.httplib.GZipHttpUtil;
import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.url.URLs;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.UICalendarGuideAty;

public class RecommendWeatherAppHelper {
	
private static RecommendWeatherAppHelper helper;

	public static RecommendWeatherAppHelper getInstance() {
		if(null == helper) {
			helper = new RecommendWeatherAppHelper();
		}
		return helper;
	}
	
	/**
	 * <br>Description: 获取推荐天气app信息
	 * <br>Author:caizp
	 * <br>Date:2016年5月13日下午2:54:13
	 * @param ctx
	 */
	public void fetchRecommendWeatherAppInfo(final Context ctx) {
		ThreadUtil.executeOther(new Runnable() {
			@Override
			public void run() {
				try {
					ConfigHelper mCfgHelper = ConfigHelper.getInstance(ctx);
					// 已下载过推荐APP数据，则不再从服务端摘取推荐数据  caizp 2016-05-16
					String savedAppIdentifier =  mCfgHelper.loadKey(ConfigHelper.RE_APP_IDENTIFIER, "");
					if(!TextUtils.isEmpty(savedAppIdentifier)) return;
					String url = String.format(URLs.PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=RecommendWeatherApp&ver=%s", 0);
					url = url + CUIDUtil.getCUIDPART() + "&pid=6&mt=4" + "&DivideVersion="+TelephoneUtil.getVersionName(ctx, ctx.getPackageName());
                    String resultJson = GZipHttpUtil.get(url);
                    if(TextUtils.isEmpty(resultJson))return;
        			JSONObject jsonObject = new JSONObject(StringUtil.removeBomHeader(resultJson));
        			JSONObject itemObject = jsonObject.optJSONObject("item");
        			if(null == itemObject) return;
        			String name = itemObject.optString("name");
        			String identifier = itemObject.optString("identifier");
        			String recommendHtml = itemObject.optString("recommendHtml");
        			String downloadUrl = itemObject.optString("downloadUrl");
        			if(TextUtils.isEmpty(name) || TextUtils.isEmpty(identifier) || 
        					TextUtils.isEmpty(recommendHtml) || TextUtils.isEmpty(downloadUrl)) {
        				return;
        			}
        			// 未安装推荐app则推荐，否则推荐黄历天气  caizp 2016-05-17
        			if(ComfunHelp.checkApkExist(ctx, identifier, 0)) {
        				name = ctx.getResources().getString(R.string.sdk_app_name);
        				identifier = ComDataDef.CALENDAR_PACKAGE;
        				recommendHtml = UICalendarGuideAty.POST_URL;
        				downloadUrl = ComDataDef.CALENDAR_DOWN_URL;
        			}
        			// 保存天气app推荐信息
        			mCfgHelper.saveKey(ConfigHelper.RE_APP_NAME, name);
        			mCfgHelper.saveKey(ConfigHelper.RE_APP_IDENTIFIER, identifier);
        			mCfgHelper.saveKey(ConfigHelper.RE_APP_RECOMMEND_HTML, recommendHtml);
        			mCfgHelper.saveKey(ConfigHelper.RE_APP_DOWNLOAD_URL, downloadUrl);
        			mCfgHelper.commit();
                } catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}

}
