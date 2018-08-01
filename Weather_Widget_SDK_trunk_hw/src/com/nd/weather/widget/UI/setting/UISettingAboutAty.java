package com.nd.weather.widget.UI.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nd.calendar.common.ComDataDef;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.UI.UICalendarGuideAty;

public class UISettingAboutAty extends Activity implements OnClickListener {
    private Button m_btnUpdate;

    private TextView m_verTextView;
    private TextView m_htmlTextView;
    private TextView m_aboutTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.weather_setting_about);

        ((Button) findViewById(R.id.setting_about_back)).setOnClickListener(this);

        m_verTextView = (TextView) findViewById(R.id.verTextid);
        m_htmlTextView = (TextView) findViewById(R.id.htmlTextId);
        m_aboutTextView = (TextView) findViewById(R.id.aboutTextId);

        m_verTextView.setText(ComDataDef.VERSION_NAME);
        m_aboutTextView.setText(R.string.about);
        m_aboutTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        m_htmlTextView.setText(""/*Html
                .fromHtml("91算命官网：<font color='#feb500'><u>http://sm.91.com</u></font>")*/);

        m_btnUpdate = (Button) findViewById(R.id.updateId);
        m_btnUpdate.setOnClickListener(this);
        m_htmlTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    	int id = v.getId();
        if (id == R.id.htmlTextId) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://sm.91.com"));
                startActivity(intent);
            } catch (Exception e) {
            }
        } else if (id == R.id.setting_about_back) {
            finish();
        }  else if (id == R.id.updateId) {
//        	CommonUI.downCalendarApk(UISettingAboutAty.this);
        	WidgetUtils.startGuide(this, UICalendarGuideAty.CALENDAR_2015_GUIDE, null, WidgetUtils.DOWNLOAD_CLICK_FROM_ABOUT);
        	HiAnalytics.submitEvent(this, WidgetUtils.getAnalyticsId(this, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_ABOUT);
        }
    }

}
