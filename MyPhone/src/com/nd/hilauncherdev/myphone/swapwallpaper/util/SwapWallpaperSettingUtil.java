package com.nd.hilauncherdev.myphone.swapwallpaper.util;

import java.util.ArrayList;
import java.util.List;

import com.nd.hilauncherdev.url.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.DialogUtil.KeyValue;
import com.nd.hilauncherdev.myphone.swapwallpaper.wpaperinterface.GetWallpaperTypeListener;

/**
 * 一键换壁纸设置工具类
 *  fmj
 */
public class SwapWallpaperSettingUtil {

	// 网上壁纸缓存大小
	public static final String WALLPAPER_CACHE_SIZE = "wallpaper_cache_size";
	// 网上壁纸分类（包括壁纸id和壁纸类型）
	public static final String WALLPAPER_TYPE = "wallpaper_type_info";
	// 壁纸类型id
	public static final String WALLPAPER_TYPE_ID = "wallpaper_type_id";
	// 壁纸类型
	public static final String WALLPAPER_TYPE_VALUE = "wallpaper_type_value";
	// 默认壁纸缓存数量
	public static final String ONLINE_WALLPAPER_CACHE_SIZE = "50";
	//默认壁纸类型id
	public static final String ONLINE_WALLPAPER_TYPE_ID = "0";
	
	public static final String DELETE_ONE_KEY_WPAPER = "delete_one_key_wpaper";

	private SharedPreferences sp;
	private static final String WALLPAPER_TYPE_URL = URLs.PANDAHOME_BASE_URL + "android/pic.aspx?";
	private GetWallpaperTypeListener getWallpaperTypeListener;
	// 壁纸类型请求成功
	private static final int GET_WALLPAPERTYPE_ITEM_SUCCESS = 0;
	// 壁纸类型请求失败
	private static final int GET_WALLPAPERTYPE_ITEM_FAIL = -1;
	private static Toast toast;

	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int resCode = msg.what;
			Object obj = msg.obj;
			switch (resCode) {
			case GET_WALLPAPERTYPE_ITEM_SUCCESS:
				if (getWallpaperTypeListener != null) {
					getWallpaperTypeListener.onGetWallPaperItemSuccess(obj);
				}
				break;
			case GET_WALLPAPERTYPE_ITEM_FAIL:
				if (getWallpaperTypeListener != null) {
					getWallpaperTypeListener.onGetWallPaperItemFailed();
				}
				break;

			default:
				break;
			}

		};
	};

	public SwapWallpaperSettingUtil(Context context) {
		sp = context.getSharedPreferences(WallpaperTool.SWAP_WALLPAPER, Activity.MODE_PRIVATE);
	}

	public void setGetWallpaperTypeListener(GetWallpaperTypeListener getWallpaperTypeListener) {
		this.getWallpaperTypeListener = getWallpaperTypeListener;
	}

	/**
	 * 壁纸分类请求 List<WallpaperTypeBean>
	 */
	@SuppressWarnings("unused")
	public void sendWallpaperTypeRequest() {
		String url = WALLPAPER_TYPE_URL + "pid="+CommonGlobal.PID+"&mt=4&tfv=40000&Action=1";
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		int code = -1;
		Message msg;
		List<WallpaperTypeBean> wallPaperTypeList = new ArrayList<SwapWallpaperSettingUtil.WallpaperTypeBean>();
		try {
			// HttpCommon http = new
			// HttpCommon(url,org.apache.http.protocol.HTTP.UTF_8);
			// String visitaResult = http.getResponseAsStringPost(new
			// HashMap<String, String>());
			String visitaResult = WallpaperTool.getURLContent(url, "utf-8");
			jsonObject = new JSONObject(visitaResult);
			if (StringUtil.isAnyEmpty(visitaResult)) {
				handler.sendEmptyMessage(GET_WALLPAPERTYPE_ITEM_FAIL);
				return;
			}
			if (jsonObject == null) {
				handler.sendEmptyMessage(GET_WALLPAPERTYPE_ITEM_FAIL);
				return;
			}
			if (jsonObject.has("Code")) {
				code = jsonObject.getInt("Code");
			}
			if (code == -1) {
				handler.sendEmptyMessage(GET_WALLPAPERTYPE_ITEM_FAIL);
				return;
			}
			if (jsonObject.has("Result")) {
				jsonObject = jsonObject.getJSONObject("Result");
				if (jsonObject.has("items")) {
					jsonArray = jsonObject.getJSONArray("items");
					if (jsonArray != null) {
						for (int i = 0; null != jsonArray && i < jsonArray.length(); i++) {
							WallpaperTypeBean wallpaperTypeBean = new WallpaperTypeBean();
							jsonObject = jsonArray.getJSONObject(i);
							if (jsonObject.has("id")) {
								wallpaperTypeBean.typeId = jsonObject.getString("id");
							}
							if (jsonObject.has("name")) {
								wallpaperTypeBean.typeValue = jsonObject.getString("name");
							}
							if(jsonObject.has("url")){
								wallpaperTypeBean.url = jsonObject.getString("url");
							}
							wallPaperTypeList.add(wallpaperTypeBean);
						}
						msg = new Message();
						msg.what = GET_WALLPAPERTYPE_ITEM_SUCCESS;
						msg.obj = wallPaperTypeList;
						handler.sendMessage(msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 网上壁纸缓存数量
	 * 
	 * @param context
	 * @return
	 */
	public List<KeyValue> getOnLineCacheSize(Context context) {
		List<KeyValue> cycleDateList = new ArrayList<KeyValue>();
		String cycleStr[] = context.getResources().getStringArray(R.array.online_wallpaper_cache_count);
		String cacheSize = sp.getString(WALLPAPER_CACHE_SIZE, ONLINE_WALLPAPER_CACHE_SIZE);
		for (String str : cycleStr) {
			KeyValue keyValue = new KeyValue();
			keyValue.displayName = str;
			if (cacheSize.equals(str)) {
				keyValue.isCheck = true;
			} else {
				keyValue.isCheck = false;
			}
			cycleDateList.add(keyValue);
		}
		return cycleDateList;
	}

	/**
	 * 保存壁纸类型信息到配置文件
	 * 
	 * @param lists
	 */
	public void savaOnLineWallpaperType(List<WallpaperTypeBean> lists) {
		StringBuffer buffer = new StringBuffer();
		if (lists == null || lists.size() < 1)
			return;
		for (WallpaperTypeBean item : lists) {
			buffer.append(item.typeId);
			buffer.append(",");
			buffer.append(item.typeValue);
			buffer.append(",");
			buffer.append(item.url);
			buffer.append(";");
		}
		setString(WALLPAPER_TYPE, buffer.toString());
	}

	/**
	 * 获取壁纸类型信息
	 * 
	 * @return
	 */
	public List<WallpaperTypeBean> getOnLineWallpaperType() {
		List<WallpaperTypeBean> list = new ArrayList<WallpaperTypeBean>();
		String str = getString(WALLPAPER_TYPE, "");
		if (str.equals(""))
			return null;
		String type[] = str.split(";");
		if (type.length < 1)
			return null;
		for (String item : type) {
			String value[] = item.split(",");
			WallpaperTypeBean bean = new WallpaperTypeBean();
			bean.typeId = value[0];
			bean.typeValue = value[1];
			bean.url = value[2];
			list.add(bean);
		}
		return list;
	}

	/**
	 * toast提示
	 * 
	 * @param msg
	 */
	public static void showToast(Context cxt, String msg) {
		if (toast == null) {
			toast = Toast.makeText(cxt, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	public void setString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public String getString(String key, String value) {
		return sp.getString(key, value);
	}

	public void setLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public long getLong(String key, long value) {
		return sp.getLong(key, value);
	}
	
	public boolean getBoolean(String key, boolean defaultValue){
		return sp.getBoolean(key, defaultValue);
	}
	
	public void putBoolean(String key, boolean defaultValue){
		sp.edit().putBoolean(key, defaultValue).commit();
	}

	public class WallpaperTypeBean {
		public String typeId;
		public String typeValue;
		public String url;
		public String localPath;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WallpaperTypeBean other = (WallpaperTypeBean) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (typeId == null) {
				if (other.typeId != null)
					return false;
			} else if (!typeId.equals(other.typeId))
				return false;
			return true;
		}
		private SwapWallpaperSettingUtil getOuterType() {
			return SwapWallpaperSettingUtil.this;
		}
		
	}

}
