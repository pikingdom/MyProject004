package com.nd.hilauncherdev.myphone.swapwallpaper;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.view.commonview.HeaderView;
import com.nd.hilauncherdev.kitset.util.MyPhoneUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.DialogUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.SwapWallpaperSettingUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.DialogUtil.SelectedDialogCallBack;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.SwapWallpaperSettingUtil.WallpaperTypeBean;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.WallpaperTool;


/**
 * 网上壁纸设置类
 * @author fmj
 */
public class SwapWallpaperSettingActivity extends PreferenceActivity{
	
	private Context context;
	private Preference mWallpaperCachePreference;
	private Preference mWallpaperStylePreference;
	private static final String SETTINGS_SAVE_WALLPAPER_CACHE_SIZE = "settings_save_wallpaper_cache_size";
	private static final String SETTINGS_WALLPAPER_STYLE = "settings_swap_wallpaper_type";
	
	private SwapWallpaperSettingUtil swapWallpaperSettingUtil;
	private String lastDisplayName = "";
	private String wallpaperSize = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (MyPhoneUtil.isNoCustomActivityTitleExceptionTelephone()){
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		}
		super.onCreate(savedInstanceState);
		context = this;
		getPreferenceManager().setSharedPreferencesName(WallpaperTool.SWAP_WALLPAPER);
		addPreferencesFromResource(R.xml.preferences_wallpaper_settings);
		if (MyPhoneUtil.isNoCustomActivityTitleExceptionTelephone()){
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.preference_activity_title);
			
			HeaderView headView = (HeaderView) findViewById(R.id.head_view);
			headView.setTitle(getResources().getString(R.string.swap_wallpaper_wallpaper_setting));
			headView.setGoBackListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();	
				}
			});
		}
		createManager();
		initView();
		initData();
	}
	
	private void createManager(){
		if(swapWallpaperSettingUtil == null){
			swapWallpaperSettingUtil = new SwapWallpaperSettingUtil(context);
		}
	}
	
	private void initView(){
		mWallpaperCachePreference = (Preference)findPreference(SETTINGS_SAVE_WALLPAPER_CACHE_SIZE);
		//保存壁纸数量
		mWallpaperCachePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				DialogUtil.showSimpleDialog(context, R.string.swap_wallpaper_cache_message, swapWallpaperSettingUtil.getOnLineCacheSize(context),
						new SelectedDialogCallBack() {
							@Override
							public void getData(com.nd.hilauncherdev.myphone.swapwallpaper.util.DialogUtil.KeyValue value, List<com.nd.hilauncherdev.myphone.swapwallpaper.util.DialogUtil.KeyValue> list) {
								if(value.displayName == null || value.displayName.equals("")){
									value.displayName = SwapWallpaperSettingUtil.ONLINE_WALLPAPER_CACHE_SIZE;
								}
								lastDisplayName = swapWallpaperSettingUtil.getString(SwapWallpaperSettingUtil.WALLPAPER_CACHE_SIZE, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_CACHE_SIZE);
								if(!lastDisplayName.equals(value.displayName)){
									swapWallpaperSettingUtil.putBoolean(SwapWallpaperSettingUtil.DELETE_ONE_KEY_WPAPER, true);
								}
								swapWallpaperSettingUtil.setString(SwapWallpaperSettingUtil.WALLPAPER_CACHE_SIZE, value.displayName);
								mWallpaperCachePreference.setSummary(String.format(context.getString(R.string.swap_wallpaper_cache_message_size), value.displayName));
							}
						});
				return false;
			}
		});
		
		mWallpaperStylePreference = (Preference)findPreference(SETTINGS_WALLPAPER_STYLE);
		mWallpaperStylePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent();
				intent.setClass(context, WallpaperStyleActivity.class);
				startActivity(intent);
				finish();
				return true;
			}
		});
	}
	
	private void initData(){
		wallpaperSize = swapWallpaperSettingUtil.getString(SwapWallpaperSettingUtil.WALLPAPER_CACHE_SIZE,SwapWallpaperSettingUtil.ONLINE_WALLPAPER_CACHE_SIZE);
		mWallpaperCachePreference.setSummary(String.format(context.getString(R.string.swap_wallpaper_cache_message_size),wallpaperSize));
		
		String type = context.getString(R.string.settings_swap_wallpaper_type_default);
		List<WallpaperTypeBean> tempList = swapWallpaperSettingUtil.getOnLineWallpaperType();
		if(tempList != null && tempList.size() > 0){			
			String str = swapWallpaperSettingUtil.getString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID);
			String typeId[] = str.split(WallpaperStyleActivity.SPLIT_STR);
			String tempType = "";
			for(String temp : typeId){
				if(!StringUtil.isEmpty(temp) && !"0".equals(temp)){
					for(int i = 0; i < tempList.size(); i ++){
						if(tempList.get(i).typeId.equals(temp)){
							tempType += tempList.get(i).typeValue+" ";
							break;
						}
					}
				}
			}
			if(!StringUtil.isEmpty(tempType)){
				type = tempType;
			}
		}
		mWallpaperStylePreference.setSummary(String.format(context.getString(R.string.settings_swap_wallpaper_type_detail),type));
	}
}
