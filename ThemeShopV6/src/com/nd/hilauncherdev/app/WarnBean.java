package com.nd.hilauncherdev.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 软件风险信息
 * @author 郑定国
 *
 */
public class WarnBean {
	public final static String WARN_SCANPROVIDER = "scanProvider";//病毒
	public final static String WARN_AD = "ad";//广告
	public final static String WARN_PRIVACY = "privacy";//隐私
	
	public HashMap<String, WarnCategory> warnCategorys = new HashMap<String, WarnCategory>();
	
	public void initDataFormJSONObject(JSONObject object) throws JSONException {
		//病毒部分
		JSONObject scanObj = object.optJSONObject(WARN_SCANPROVIDER);
		if(scanObj != null) {
			WarnCategory scanCategory = new WarnCategory();
			scanCategory.initDataFormJSONObject(scanObj);
			warnCategorys.put(WARN_SCANPROVIDER, scanCategory);
		}
		//广告部分
		JSONObject adObj = object.optJSONObject(WARN_AD);
		if(adObj != null) {
			WarnCategory adCategory = new WarnCategory();
			adCategory.initDataFormJSONObject(adObj);
			warnCategorys.put(WARN_AD, adCategory);
		}
		//隐私部分
		JSONObject privacyObj = object.optJSONObject(WARN_PRIVACY);
		if(privacyObj != null) {
			WarnCategory privacyCategory = new WarnCategory();
			privacyCategory.initDataFormJSONObject(privacyObj);
			warnCategorys.put(WARN_PRIVACY, privacyCategory);
		}
	}
	
	public class WarnCategory {
		public String title;
		public int state = 0;
		public List<WarnItem> warnItems = new ArrayList<WarnItem>();
		
		public void initDataFormJSONObject(JSONObject object) throws JSONException {
			title = object.optString("title");
			state = object.optInt("state");
			Log.e("数据库", "title = " + title + ", state = " + state);
			JSONArray itemArray = object.optJSONArray("items");
			if(itemArray != null) {
				for(int i=0; i<itemArray.length(); i++) {
					JSONObject itemObj = itemArray.getJSONObject(i);
					if(itemObj != null) {
						Log.e("数据库", "i = " + i + ", json = " + itemObj.toString());
						WarnItem item = new WarnItem();
						item.initDataFormJSONObject(itemObj);
						warnItems.add(item);
					}
				}
			}
		}
	}
	
	public class WarnItem {
		public String icon;
		public String title;
		public int state = 0;
		public String context;
		
		public void initDataFormJSONObject(JSONObject object) throws JSONException {
			icon = object.optString("icon");
			title = object.optString("title");
			state = object.optInt("state");
			context = object.optString("context");
		}
	}

}
