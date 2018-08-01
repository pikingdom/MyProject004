package com.nd.android.pandahome2.manage.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.dian91.ad.AdvertSDKManager;
import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.dian91.ad.AdvertSDKManager.CountdownCallBack;
import com.nd.android.pandahome2.shop.R;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysis;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysisConstant;
import com.nd.hilauncherdev.analysis.integral.IntegralSubmitUtil;
import com.nd.hilauncherdev.analysis.integral.IntegralTaskIdContent;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.DateUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.sdk.AdvertSDKController;
import com.nd.hilauncherdev.shop.Global;
import com.nd.hilauncherdev.shop.api6.model.AdvertItem;
import com.nd.hilauncherdev.shop.api6.net.DigestUtils;
import com.nd.hilauncherdev.shop.api6.net.ThemeNetOptApi;
import com.nd.hilauncherdev.shop.shop3.ThemeShopV2MainActivity;
import com.nd.hilauncherdev.shop.util.ThemeShopV6PreferencesUtils;
import com.nd.hilauncherdev.uri.UriParser;

import java.io.File;
import java.util.List;


public class ThemeShopLoadingActivity extends HiActivity {
	private Handler handler = new Handler();
	private boolean bClickAd = false;
	private boolean hasGetAdvertInfo = false;
	private boolean hasDownloadAdvert = false;
	private Runnable getAdFailRunnable = null;
	private Runnable downloadFailRunnable = null;
	private Runnable loadingAdRunnable = null;
	
	private View loadingAdView;
	private WebView loadingWebView;
	private View skipWebView;
	private boolean hasClicked = false;
	private FrameLayout loadingContainerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.theme_shop_v6_splash);

		if( ThemeShopV6PreferencesUtils.isLoadedThemeShopProcess(ThemeShopLoadingActivity.this)
				|| !ThemeShopV6PreferencesUtils.isShowShopLoadingForDZ(ThemeShopLoadingActivity.this)){
			startMainActivity();
			return;
		}
		
		//加载loading广告图片
		loadingAdView = findViewById(R.id.loading_image);
		loadingContainerView = (FrameLayout)findViewById(R.id.loading_container);
		loadingWebView = (WebView)findViewById(R.id.loading_webview);
		skipWebView = findViewById(R.id.loading_webview_skip);
		skipWebView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startMainActivity();
			}
		});
		ThreadUtil.executeMore(new Runnable() {
			public void run() {
				showRealtimeAdOnEnter();
			}
		});
		
		//加载底部logo图片
		final View logoView = findViewById(R.id.bottom_logo);
		logoView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		//若2s内没获取到广告数据，自动跳转
		getAdFailRunnable = new Runnable() {
			@Override
			public void run() {
				if (!hasGetAdvertInfo) {
					startMainActivity();
				}
			}
		};
		handler.postDelayed(getAdFailRunnable, 2000);
		IntegralSubmitUtil.getInstance(this).submit(this, IntegralTaskIdContent.ADMIRE_LOADING);
		CvAnalysis.submitPageStartEvent(this, CvAnalysisConstant.THEME_SHOP_LOADING);
	}
	
	private void showRealtimeAdOnEnter(){
		// 广告数据获取
		List<AdvertInfo> loadingList = AdvertSDKController.getAdvertInfos(ThemeShopLoadingActivity.this, AdvertSDKManager.TYPE_THEMESHOP_LOADING);
		if(loadingList == null || loadingList.size() == 0)
			return;
		//获取要轮播的广告
		AdvertInfo loadingItem = getAdvertInfoFromList(loadingList);
		if (loadingItem == null 
				|| (TextUtils.isEmpty(loadingItem.picUrl) && TextUtils.isEmpty(loadingItem.h5Url) && TextUtils.isEmpty(loadingItem.h5Data))){	
			handler.removeCallbacks(getAdFailRunnable);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					startMainActivity();
				}
			}, 1000);
			return;
		}
		
		hasGetAdvertInfo = true;
		final AdvertInfo advertItem = loadingItem;
		//advertItem.actionIntent = "http://blr.0708.com/t2/99/2316.html";
		if(!TextUtils.isEmpty(advertItem.picUrl)){
			showImageAd(loadingAdView, advertItem);
		}else if(!TextUtils.isEmpty(advertItem.h5Url) || !TextUtils.isEmpty(advertItem.h5Data)){
			showH5Ad(loadingWebView, advertItem);
		}
		//统计
		submitAdShowEvent(advertItem);
	}

	private boolean isLinkUrlOnNotWifiNetwork(AdvertInfo loadingItem) {
		try{
			if(TelephoneUtil.isWifiEnable(this))
				return false;
			if( loadingItem.actionIntent != null && (loadingItem.actionIntent.contains(UriParser.ACTION_DOWNLOADMANAGER_MAIN)
						|| loadingItem.actionIntent.contains(UriParser.HTTP_HEADER)
						|| loadingItem.actionIntent.contains(UriParser.HTTPS_HEADER))){
				return true;
			}
			if(!TextUtils.isEmpty(loadingItem.h5Url) || !TextUtils.isEmpty(loadingItem.h5Data))
				return true;
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	private AdvertInfo getAdvertInfoFromList(List<AdvertInfo> loadingList) {
		AdvertInfo loadingItem = null;
		if(loadingList.size() == 1){
			loadingItem = loadingList.get(0);
		} else {
			long currentTime = DateUtil.getTodayTime();
			if (currentTime != ThemeShopV6PreferencesUtils.getLastThemeLoadingShowTime(this)) {// 每天第一次查看，都是现实第一个广告
				loadingItem = loadingList.get(0);
				ThemeShopV6PreferencesUtils.setLastThemeLoadingIndex(this, loadingItem.showId);
			} else {// 循环播放广告
				String lastIndex = ThemeShopV6PreferencesUtils.getLastThemeLoadingIndex(this);
				if(TextUtils.isEmpty(lastIndex)){
					loadingItem = loadingList.get(0);
					ThemeShopV6PreferencesUtils.setLastThemeLoadingIndex(this, loadingItem.showId);
				}else{
					String[] indexArray = lastIndex.split(",");
					boolean hasFound = false;
					for (AdvertInfo item : loadingList) {
						boolean isTarget = true;
						for(String index : indexArray){
							if (index.equals(item.showId) || isLinkUrlOnNotWifiNetwork(item)) {
								isTarget = false;
								break;
							}
						}
						if(isTarget){
							loadingItem = item;
							hasFound = true;
							ThemeShopV6PreferencesUtils.setLastThemeLoadingIndex(this, lastIndex + "," +loadingItem.showId);
							break;
						}
					}
					if(!hasFound){
						loadingItem = loadingList.get(0);
						ThemeShopV6PreferencesUtils.setLastThemeLoadingIndex(this, loadingItem.showId);
					}
				}
			}
			ThemeShopV6PreferencesUtils.setLastThemeLoadingShowTime(this, currentTime);
		}
		return loadingItem;
	}

	private void showH5Ad(final WebView mWebView, final AdvertInfo advertItem) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				skipWebView.setVisibility(View.VISIBLE);
				AdvertSDKController.initWebView(ThemeShopLoadingActivity.this, mWebView, advertItem, getThemeShopIntent());
				mWebView.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(advertItem.h5Url)){					
					mWebView.loadUrl(advertItem.h5Url);   
				}else if(!TextUtils.isEmpty(advertItem.h5Data)){
					mWebView.loadData(advertItem.h5Data,  "text/html", "UTF-8");
				}
			}
		});
		
		long showTime = advertItem.splashTime;
		if(showTime <= 0){
			showTime = 3000;
		}
		//展示广告后，跳转
		loadingAdRunnable = new Runnable() {
			@Override
			public void run() {
				if (!bClickAd) {
					startMainActivity();
				}
			}
		};
		handler.postDelayed(loadingAdRunnable, showTime);
	}
	
	private void showImageAd(final View loadingAdView, final AdvertInfo advertItem) {
		//若图片2.5s内没下载成功，自动跳转
		downloadFailRunnable = new Runnable() {
			@Override
			public void run() {
				if (!hasDownloadAdvert) {
					startMainActivity();
				}
			}
		};
		handler.postDelayed(downloadFailRunnable, 2500);
		
		final String adPicUrl = advertItem.picUrl;
		final String path = Global.CACHES_CATEGORY_PIC + DigestUtils.md5Hex(adPicUrl);
		if(!new File(path).exists()){
			String result = BitmapUtils.saveInternateImage(adPicUrl, path);
			if(!TextUtils.isEmpty(result)) {
				hasDownloadAdvert = true;
			}
		} else {
			hasDownloadAdvert = true;
		}
		
		//设置广告图片
		handler.post(new Runnable() {
			@Override
			public void run() {
				Drawable cachedImage = Global.getDrawableFromPath(path);
				if(cachedImage == null){
					FileUtil.delFile(path);
					return;
				}
				AdvertSDKController.addAdvertLogoAndCountdownView(ThemeShopLoadingActivity.this, loadingContainerView, advertItem,
					new CountdownCallBack() {
						@Override
						public void onCallback() {
							if (!bClickAd) {
								startMainActivity();
							}
						}
					});
				loadingAdView.setVisibility(View.VISIBLE);
				loadingAdView.setBackgroundDrawable(cachedImage);
				loadingAdView.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						boolean allowActionClick = true;
						if (advertItem.actionArea != null){
							float touchX = event.getX();
							float touchY = event.getY();
							Rect r = new Rect(advertItem.actionArea);
							float xRate = loadingAdView.getWidth() / 720f;
							float yRate = loadingAdView.getHeight() / 1200f;
							r.left *= xRate;
							r.right *= xRate;
							r.top *= yRate;
							r.bottom *= yRate;
							if(!r.contains((int) touchX, (int) touchY)){
								allowActionClick = false;
							}
						}
						if(allowActionClick && !hasClicked){
							hasClicked = true;
							actionAdClick(getApplicationContext(), advertItem);
							submitAdClickEvent(advertItem);
						}
						return false;
					}
				});
				Animation scaleAnimation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.themeshop_loading);
				scaleAnimation.setDuration(2500);
				loadingAdView.startAnimation(scaleAnimation);

			}
		});
	}
	
//	public void showCountDownView(final int index){
//		countDownTV.setVisibility(View.VISIBLE);
//		countDownTV.setText("0" + index);
//		long delay = 1000;
//		if(index == 1){//最后一秒，减少延迟
//			delay = 500;
//		}
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				int newIndex = index - 1;
//				if(newIndex  > 0){
//					showCountDownView(newIndex);
//				}else{
//					if (!bClickAd) {						
//						startMainActivity();
//					}
//				}
//			}
//		}, delay);
//	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if ( bClickAd ){
			startMainActivity();
		}
	}
	
	public void startMainActivity(){
		Intent intent = getThemeShopIntent();
		startActivity(intent);
		finish();
	}

	private Intent getThemeShopIntent() {
		Intent intent = new Intent(ThemeShopLoadingActivity.this, ThemeShopV2MainActivity.class);
		intent.putExtra("jumpToRecommendVideoPaper", getIntent().getBooleanExtra("jumpToRecommendVideoPaper", false));
        Intent invokeIntent = getIntent();
        if(invokeIntent != null) {
            intent.putExtra("from_third_launcher_shortcut_invoke",
                    invokeIntent.getBooleanExtra("from_third_launcher_shortcut_invoke", false));
        }
		return intent;
	}
	
	private void actionAdClick(Context ctx, AdvertInfo advertItem){
		String actionIntent = advertItem.actionIntent;
		
		if (!StringUtil.isEmpty(actionIntent)){
			bClickAd = true;
			if(UriParser.isDownloadAction(actionIntent)){//下载调用，使用service处理协议，使页面继续跳转
				actionIntent = UriParser.changeActivityUriToService(actionIntent);
				bClickAd = false;
			}
			if(actionIntent.startsWith(UriParser.HTTP_HEADER) || actionIntent.startsWith(UriParser.HTTPS_HEADER)){//用广告SDK浏览器打开
				AdvertSDKController.startAdSdkBrowserActivity(ThemeShopLoadingActivity.this, advertItem, actionIntent, getThemeShopIntent());
				finish();
			}else{				
				UriParser.handleUriEx(actionIntent);
			}
		}
	}

	private void submitAdShowEvent(AdvertInfo advertItem) {
		if (!TextUtils.isEmpty(advertItem.picUrl)) {
			String serverImgName = Global.getPicNameFromUrlWithSuff(advertItem.picUrl);
			HiAnalytics.submitEvent(ThemeShopLoadingActivity.this, AnalyticsConstant.LAUNCHER_THEME_SHOP_AD_SHOW, serverImgName);
		}
		ThemeNetOptApi.submitAdReport(ThemeShopLoadingActivity.this, advertItem.id, AdvertItem.AD_REPORT_RESTYPE_AD,
				AdvertItem.AD_POSTION_LOADING, AdvertItem.AD_REPORT_STATTYPE_SHOW);
		CvAnalysis.submitShowEvent(this, CvAnalysisConstant.THEME_SHOP_LOADING, 0, advertItem.id, CvAnalysisConstant.RESTYPE_ADS, advertItem.sourceId);
		AdvertSDKController.submitShowEvent(this, handler, advertItem);
	}
	
	private void submitAdClickEvent(AdvertInfo advertItem) {
		if (!TextUtils.isEmpty(advertItem.picUrl)) {
			String serverImgName = Global.getPicNameFromUrlWithSuff(advertItem.picUrl);
			HiAnalytics.submitEvent(ThemeShopLoadingActivity.this, AnalyticsConstant.LAUNCHER_THEME_SHOP_AD_CLICK, serverImgName);
		}
		ThemeNetOptApi.submitAdReport(this, advertItem.id, AdvertItem.AD_REPORT_RESTYPE_AD, AdvertItem.AD_POSTION_LOADING,
				AdvertItem.AD_REPORT_STATTYPE_CLICK);
		CvAnalysis.submitClickEvent(getApplicationContext(), CvAnalysisConstant.THEME_SHOP_LOADING, 0, advertItem.id,
				CvAnalysisConstant.RESTYPE_ADS, advertItem.sourceId);
		AdvertSDKController.submitClickEvent(ThemeShopLoadingActivity.this, handler, advertItem);
		
		String downloadActionKey = UriParser.getDownloadActionKey(advertItem.actionIntent);
		if(!TextUtils.isEmpty(downloadActionKey)){			
			AdvertSDKController.submitStartDownloadEvent(ThemeShopLoadingActivity.this, advertItem, downloadActionKey);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(loadingAdRunnable != null)
			handler.removeCallbacks(loadingAdRunnable);
		CvAnalysis.submitPageEndEvent(this, CvAnalysisConstant.THEME_SHOP_LOADING);
	}
}
