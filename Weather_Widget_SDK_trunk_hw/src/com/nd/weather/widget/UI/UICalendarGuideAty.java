package com.nd.weather.widget.UI;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nd.calendar.common.ConfigHelper;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.WebViewSecureUtil;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;

public class UICalendarGuideAty extends UIBaseAty {
    public static final int CALENDAR_GUIDE = 1;
    public static final int HUANGLI_GUIDE = 2;
    public static final int CALENDAR_2015_GUIDE = 3;
//    private final String CALENDAR_FILE = "calendar.b";
//    private final String HUANGLI_FILE = "huangli.b";
//    private final String CALENDAR_2015_FILE = "calendar_2015.b";
//    private final String CALENDAR_DOWN_URL = "http://pic.ifjing.com/rbpiczy/pic/2016/03/08/dfcd97d41f054496909c375d75ce62f0.jpg";
//    private final String HUANGLI_DOWN_URL = "http://pic.ifjing.com/rbpiczy/pic/2016/03/08/f44090d85b764ce2824c087a2b4a9d97.jpg";
//    private final String CALENDAR_2015_DOWN_URL = "http://pic.ifjing.com/rbpiczy/pic/2016/03/08/07db0809806e40b8a954ef2f073ee472.jpg";
//    private ImageView mIvImage;
//    private ProgressBar mPbar;
//    private TextView mtvHint;
//    private Button mbtnDownLoad;
//    private View mBottomView;
//
//    private String mDownUrl = null;
//
//    private File mFile = null;
//    
//    private SoftReference<BitmapDrawable> mDrawable = null;
    /**
     * 黄历天气推荐下载Html界面
     */
    public final static String POST_URL = "http://url.ifjing.com/iIRNvu";
    public WebView mWebView;
	private ProgressBar webProgressBar;
	private View webProgressBarFl;
	private LinearLayout noNetworkLayout;//无网络界面
    private String downloadFrom = null; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    
    private void initView(){
        setContentView(R.layout.weather_calendar_guide);
//        mIvImage = (ImageView) findViewById(R.id.ivImage);
//        mPbar = (ProgressBar) findViewById(R.id.pbProgress);
//        mtvHint = (TextView) findViewById(R.id.tvProgressHint);
//        mBottomView =  findViewById(R.id.rlBottom);
//
//        mbtnDownLoad = (Button)findViewById(R.id.btnDownLoad);
//        mbtnDownLoad.setOnClickListener(onAdDownClick);
//        mIvImage.setOnClickListener(onAdDownClick);
//        
        noNetworkLayout = (LinearLayout)findViewById(R.id.nonetwork_layout);
        findViewById(R.id.iv_close).setOnClickListener(onCloseClick);
//        
//        File fdir = FileHelp.getCalendarGuideImageDir(this);
//        int param = getIntent().getIntExtra("param", -1);
        downloadFrom = getIntent().getStringExtra("from");
//        switch (param) {
//        case CALENDAR_GUIDE:
//            mFile = new File(fdir, CALENDAR_FILE);
//            mDownUrl = CALENDAR_DOWN_URL;
//            break;
//        case HUANGLI_GUIDE:
//            mFile = new File(fdir, HUANGLI_FILE);
//            mDownUrl = HUANGLI_DOWN_URL;
//            break;
//        case CALENDAR_2015_GUIDE:
//        	mFile = new File(fdir, CALENDAR_2015_FILE);
//            mDownUrl = CALENDAR_2015_DOWN_URL;
//        default:
//            break;
//        }
//        if ((mFile != null) && (mFile.exists())) {
//            loadImage(mFile);
//            mBottomView.setVisibility(View.GONE);
//        }
        HiAnalytics.submitEvent(this, WidgetUtils.getAnalyticsId(this, R.string.analytics_weather_click_distribute), "7");
        initWebView();
    }
    
    /**
	 * 重新加载网页
	 */
	private void loadWebView(){
		mWebView.setVisibility(View.VISIBLE);
		String appUrl = ConfigHelper.getInstance(this).getReCommendWeatherAppHtml();
		if ( WebViewSecureUtil.checkWebUrlSecure(appUrl) ){
			mWebView.loadUrl(appUrl);
		}
	}

	private void initWebView(){
		webProgressBarFl = findViewById(R.id.web_progress_bar_fl);
  		webProgressBar = (ProgressBar) findViewById(R.id.web_progress_bar);
  		mWebView = (WebView) findViewById(R.id.webview);
		WebViewSecureUtil.checkWebViewSecure(mWebView);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLightTouchEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(true);//支持拉伸
		settings.setUseWideViewPort(true);
		//settings.setPluginsEnabled(true);

		//Web加载进度更新
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				webProgressBar.setProgress(progress);
				if (progress >= 100) {
					webProgressBar.setVisibility(View.GONE);
					webProgressBarFl.setVisibility(View.GONE);
				} else {
					webProgressBarFl.setVisibility(View.VISIBLE);
					webProgressBar.setVisibility(View.VISIBLE);
				}
			}
		});
		
		mWebView.setWebViewClient(new WebViewClient() {// 在当前浏览器打开url
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
		
		mWebView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition,
					String mimetype, long contentLength) {
				CommonUI.downCalendarApk(UICalendarGuideAty.this, downloadFrom);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (TelephoneUtil.isNetworkAvailable(this)){//网络可用
			loadWebView();
		}else{//网络不可用
			webProgressBarFl.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
			noNetworkLayout.removeAllViews();
			ViewFactory.getNomalErrInfoView(this, noNetworkLayout, ViewFactory.NET_BREAK_VIEW);
		}
	}
    
    /*private void loadImage(File f){
        try{
            BitmapFactory.Options opt = new BitmapFactory.Options(); 
            opt.inPreferredConfig = Bitmap.Config.RGB_565; 
            opt.inPurgeable = true; 
            opt.inInputShareable = true; 
            //获取资源图片
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath(),opt); 
            BitmapDrawable db = new BitmapDrawable(bmp);
            mDrawable = new SoftReference<BitmapDrawable>(db);
            mIvImage.setImageDrawable(db);
        }catch (Exception e) {
            //不是有效图片就删除
            f.delete();
        }
    }

    private void downImage(final String url, final File savePath) {
        if (HttpToolKit.isNetworkAvailable(this)) {
            mPbar.setVisibility(View.VISIBLE);
            mtvHint.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final boolean downRet = HttpToolKit.saveToFile(UICalendarGuideAty.this,
                                url, savePath.getAbsolutePath());
                        mIvImage.post(new Runnable() {
                            @Override
                            public void run() {
                                if ((downRet) && (savePath.exists())) {
                                    loadImage(savePath);
                                }
                                mtvHint.setVisibility(View.GONE);
                                mPbar.setVisibility(View.GONE);
                                mBottomView.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e) {
                        FileHelp.DeleteFile(savePath);
                    }
                }
            }).start();
        } else {
            mtvHint.setVisibility(View.VISIBLE);
            mtvHint.setText("请连接网络后再尝试！");
        }
    }
    
    private OnClickListener onAdDownClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            CommonUI.downCalendarApk(UICalendarGuideAty.this, downloadFrom);
        }
    };
*/    
    private OnClickListener onCloseClick = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            finish();
        }
    };

//    @Override
//    public void finish() {
//        super.finish();
//        try {
//            BitmapDrawable db = mDrawable != null ? mDrawable.get() : null;
//            if ((db != null) && (db.getBitmap() != null)) {
//                getWindow().getDecorView().setVisibility(View.GONE);
//                db.getBitmap().recycle();
//            }
//        } catch (Exception e) {
//
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((KeyEvent.KEYCODE_BACK == keyCode) && (event.getRepeatCount() == 0)) {
            finish();
        } 
        return super.onKeyDown(keyCode, event);
    }
}
