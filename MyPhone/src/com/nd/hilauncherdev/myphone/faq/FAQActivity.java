package com.nd.hilauncherdev.myphone.faq;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.framework.view.commonview.MyphoneContainer;
import com.nd.hilauncherdev.kitset.util.TabContainerUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

/**
 * description: 桌面FAQ<br/>
 * author: dingdj<br/>
 * data: 2014/9/2<br/>
 */
public class FAQActivity extends HiActivity {
    public static final String TAG = FAQActivity.class.getSimpleName();

    private MyphoneContainer container;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabContainerUtil.fullscreen(this);
        container = new MyphoneContainer(this);
        setContentView(container);
        init();
//        Intent intent = getIntent();
//        if ( intent != null ){
//        	boolean fromMyPhone = false;
//        	try{
//        		fromMyPhone = intent.getBooleanExtra("fromMyPhone", false);
//        	}catch(Exception e){
//        		e.printStackTrace();
//        	}
//        	if ( fromMyPhone ){//来自我的手机中点击
//        		HiAnalytics.submitEvent(this, AnalyticsConstant.LAUNCHER_DRAWER_MYPHONE_DETAILED, "12");
//        	}
//        }
    }


    private void init() {
        View view = getLayoutInflater().inflate(R.layout.activity_faq, null);
        container.initContainer(this.getString(R.string.myphone_faq), view, MyphoneContainer.DEFALUT_THEME);

        container.setGoBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (goPoContributeDetailPage()) {
            return;
        }
        if(TelephoneUtil.isNetworkAvailable(this)) {
            webView.loadUrl("http://zm.felink.com/appfaqs/91zm.shtml");
        }else{
            webView.loadUrl("file:///android_asset/faq/faq.htm");
        }
    }

    //直接跳转至美图投稿指南页面
    private boolean goPoContributeDetailPage() {
        Intent intent = getIntent();
        boolean isFromPo = intent.getBooleanExtra("isGoPoGuide", false);
        if (isFromPo) {
            if (TelephoneUtil.isNetworkAvailable(this)) {
                webView.loadUrl("http://zm.felink.com/appfaqs/91zm/201509172258768.shtml");
            }
        }
        return isFromPo && TelephoneUtil.isNetworkAvailable(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
