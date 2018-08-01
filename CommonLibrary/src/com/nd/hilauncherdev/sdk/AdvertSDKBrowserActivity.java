package com.nd.hilauncherdev.sdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.sdk.jd.JDSdkToolForCommon;
import com.nd.hilauncherdev.settings.CustomWebViewClient;

import org.json.JSONObject;

/**
 * Description: 内置webview的Activity
 * Author: guojianyun_dian91 
 * Date: 2016年1月6日 下午6:58:51
 */
public class AdvertSDKBrowserActivity extends HiActivity implements OnClickListener {

	public static final String EXTRA_URL = "url";
	public static final String EXTRA_TITLE = "title";

	/**
	 * 706之前的版本没有传递该参数，因此没有下载的打点统计
	 */
	public static final String EXTRA_AD = "ad";
	public static AdvertInfo advertItem = null;
	public static Intent callBackIntent = null;

	private WebView mWebView;
	private String url;
	private String titleExtra;
	private TextView titleTv;
	private View progressBarContainer;
	private LinearLayout noNetwork;// 无网络界面
	private boolean hasSetTitle = false;
	private String from = JDSdkToolForCommon.FROM_POSITION_LOADING;

	private LinearLayout retreat, advance, home, openBrowser;// 前进，后退，home和打开其他浏览器
    private ImageView retreatImage, advanceImage, homeImage, openBrowserImage;// 控制键对应的image

	private CustomWebViewClient mWebViewClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advert_sdk_browser);

		titleExtra = getIntent().getStringExtra(EXTRA_TITLE);
		url = getIntent().getStringExtra(EXTRA_URL);
		from = getIntent().getStringExtra(JDSdkToolForCommon.INTENT_FROM_TAG);
		if(TextUtils.isEmpty(from)){
			from = JDSdkToolForCommon.FROM_POSITION_LOADING;
		}
		String adString = getIntent().getStringExtra(EXTRA_AD);
		if(TextUtils.isEmpty(url)){
			finish();
		}

		if(JDSdkToolForCommon.isUrlViaJDSdk(url)){//如果是京东url调用
			JDSdkToolForCommon.startJdSDKWithRawUrl(this, url,from);
			finish();
		}
		if(!TextUtils.isEmpty(adString)){
			initAdBeanFromString(adString);
		}

		findViewById(R.id.top_pannel_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startCallBackIntent();
			}
		});
		titleTv = (TextView)findViewById(R.id.top_panel_title);
		if(!TextUtils.isEmpty(titleExtra)){
			hasSetTitle = true;
			titleTv.setText(titleExtra);
		}

		mWebViewClient = new CustomWebViewClient(this);

		noNetwork = (LinearLayout) findViewById(R.id.advert_sdk_browser_nonetwork);
		mWebView = (WebView)findViewById(R.id.ad_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);//打开JS
		webSettings.setAllowFileAccess(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//
		webSettings.setSupportZoom(true);//支持拉伸
		webSettings.setBuiltInZoomControls(true);//加入拉伸工具条
		webSettings.setUseWideViewPort(true);//
		webSettings.setSupportMultipleWindows(false);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
		webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        mWebView.setWebViewClient(mWebViewClient);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.browser_web_progressbar);
        progressBarContainer = findViewById(R.id.wait_layout);
        mWebView.setWebChromeClient(new WebChromeClient() {
        	@Override
            public void onProgressChanged(WebView view, int newProgress) {
//        		int value = Math.min(100, newProgress + 15);
//        		progressBar.setProgress(value);
//        		if (value == 100) {
//        			progressBarContainer.setVisibility(View.GONE);
//        		}
                if (progressBar != null) {
                    if (newProgress != 100) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        if (advanceImage != null) {
                            if (mWebView.canGoForward()) {
                                advanceImage.setImageResource(R.drawable.webview_right_button);
                            } else {
                                advanceImage.setImageResource(R.drawable.webview_right_button_unclick);
                            }
                        }
                    }
                }

			}
        	 @Override
             public void onReceivedTitle(WebView view, String title) {
        		 if(!TextUtils.isEmpty(title) && !hasSetTitle){
        			 hasSetTitle = true;
        			 titleTv.setText(title);
        		 }
        	 }
        });

		mWebView.setDownloadListener(new android.webkit.DownloadListener() {
			@Override
			public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				if(TelephoneUtil.isWifiEnable(AdvertSDKBrowserActivity.this)){
					startDownloadService(url);
				} else {
					ViewFactory.getAlertDialog(AdvertSDKBrowserActivity.this, getString(R.string.download_delete_title),
							getString(R.string.download_not_wifi_alert), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									startDownloadService(url);
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									startCallBackIntent();
								}
							}).show();
				}
			}

			private void startDownloadService(String url) {
				String identification = "download_" +System.currentTimeMillis();
				String title = null;
				if(advertItem != null){
					AdvertSDKController.submitStartDownloadEvent(getApplicationContext(), advertItem, identification);
					if(!TextUtils.isEmpty(advertItem.name))
						title = advertItem.name;
				}

				if(TextUtils.isEmpty(title) || "null".equalsIgnoreCase(title)){
					if(titleTv != null && !TextUtils.isEmpty(titleTv.getText())){
						title = titleTv.getText() + "_" +System.currentTimeMillis();
					}else {
						title = "来自网页_" +System.currentTimeMillis();
					}
				}
				Intent intent = new Intent(getPackageName() + ".FORWARD_SERVICE");
                intent.putExtra("identification", identification);
                intent.putExtra("fileType", 0);
                intent.putExtra("downloadUrl", url);
                intent.putExtra("title", title);
                intent.putExtra("savedDir",  BaseConfig.WIFI_DOWNLOAD_PATH);
                intent.putExtra("savedName", identification);
                intent.putExtra("iconPath", "");
                intent.putExtra("totalSize", "");
                startService(intent);
				startCallBackIntent();
			}
		});

		// 工具栏
		retreat = (LinearLayout) findViewById(R.id.advert_sdk_browser_retreat);
        advance = (LinearLayout) findViewById(R.id.advert_sdk_browser_advance);
        home = (LinearLayout) findViewById(R.id.advert_sdk_browser_home);
        openBrowser = (LinearLayout) findViewById(R.id.advert_sdk_browser_open_browser);
        retreatImage = (ImageView) findViewById(R.id.advert_sdk_browser_retreat_image);
        advanceImage = (ImageView) findViewById(R.id.advert_sdk_browser_advance_image);
        homeImage = (ImageView) findViewById(R.id.advert_sdk_browser_home_image);
        openBrowserImage = (ImageView) findViewById(R.id.advert_sdk_browser_open_browser_image);
        retreat.setOnClickListener(this);
        advance.setOnClickListener(this);
        home.setOnClickListener(this);
        openBrowser.setOnClickListener(this);

        mWebView.loadUrl(url);
	}

	@Override
    protected void onResume() {
        // 检测是否有网络
        checkNetwork();
        super.onResume();
    }

	/**
     * 检测网络是否可用
     */
    private void checkNetwork() {
        if (!TelephoneUtil.isNetworkAvailable(this)) {
            noNetwork.removeAllViews();
            ViewFactory.getNomalErrInfoView(this, noNetwork, ViewFactory.NET_BREAK_VIEW);
            noNetwork.setVisibility(View.VISIBLE);
            progressBarContainer.setVisibility(View.GONE);
        } else {
            noNetwork.setVisibility(View.GONE);
        }
    }

	@Override
	public void onBackPressed() {
		if(mWebView != null && mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			startCallBackIntent();
		}
	}

	private void startCallBackIntent() {
		if(callBackIntent != null){
			startActivity(callBackIntent);
			callBackIntent = null;
		}
		finish();
	}

	private static void initAdBeanFromString(String adString){
		try {
			advertItem = null;
			AdvertInfo advertInfo = new AdvertInfo();
			JSONObject jo = new JSONObject(adString);
			advertInfo.id = jo.optInt("id");
			advertInfo.showId = jo.optString("showId");
			advertInfo.eventId = jo.optString("eventId");
			advertInfo.pos = jo.optInt("pos");
			advertInfo.name = jo.optString("name");
			advertInfo.height = jo.optInt("height");
			advertInfo.width = jo.optInt("width");
			advertInfo.picUrl = jo.optString("picUrl");
			advertInfo.h5Url = jo.optString("h5Url");
			advertInfo.h5Data = jo.optString("h5Data");
			advertInfo.linkUrl = jo.optString("linkUrl");
			advertInfo.actionIntent = jo.optString("actionIntent");
			advertInfo.splashTime = jo.optLong("splashTime");
			advertInfo.endTime = jo.optString("endTime");
			advertInfo.sourceId = jo.optInt("sourceId");
			advertInfo.desc = jo.optString("desc");
			advertInfo.type = jo.optInt("type");
			advertInfo.showUrl = jo.optString("showUrl");
			advertInfo.clickUrl = jo.optString("clickUrl");
			advertInfo.trackUrl = jo.optString("trackUrl");
			advertInfo.passBack = jo.optString("passBack");
			advertInfo.opt = jo.optString("opt");
			advertInfo.inmobiContextCode = jo.optString("inmobiContextCode");
			advertInfo.inmobiShowAction = jo.optString("inmobiShowAction");
			advertInfo.inmobiClickAction = jo.optString("inmobiClickAction");
			advertItem = advertInfo;
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
        checkNetwork();
        if (!TelephoneUtil.isNetworkAvailable(this)) {
            return;
        }
        if (viewId == retreat.getId()) {// 后退
            retreatImage.performClick();
            onBackPressed();
        } else if (viewId == advance.getId()) {// 前进
            advanceImage.performClick();
            if (mWebView.canGoForward()) {
            	mWebView.goForward();
            }
        } else if (viewId == home.getId()) {// 主页
            homeImage.performClick();
            mWebView.loadUrl(url);
        } else if (viewId == openBrowser.getId()) {// 打开其他浏览器
            openBrowserImage.performClick();
            SystemUtil.openPage(this, mWebView.getOriginalUrl());
        }
	}

	@Override
	protected void onPause() {
		mWebViewClient.onPause();
		super.onPause();
	}
}
