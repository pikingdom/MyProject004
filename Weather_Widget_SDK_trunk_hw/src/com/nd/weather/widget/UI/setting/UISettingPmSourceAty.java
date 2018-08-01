package com.nd.weather.widget.UI.setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetGlobal;

public class UISettingPmSourceAty extends Activity implements OnClickListener {

    private ViewGroup btnUsa;
    private ViewGroup btnGov;

    private RadioButton chkUsa;
    private RadioButton chkGov;

    private Button btnBack;

    private String mCurrSource;

    private ConfigHelper mCfgHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.weather_setting_pm_source);

        mCfgHelper = ConfigHelper.getInstance(this);

        btnUsa = (ViewGroup) findViewById(R.id.btn_pm_usa);
        btnUsa.setOnClickListener(this);
        btnGov = (ViewGroup) findViewById(R.id.btn_pm_gov);
        btnGov.setOnClickListener(this);

        chkUsa = (RadioButton) findViewById(R.id.chk_usa);
        chkGov = (RadioButton) findViewById(R.id.chk_gov);

        btnBack = (Button) findViewById(R.id.setting_pm_back);
        btnBack.setOnClickListener(this);

        mCurrSource = mCfgHelper.loadKey(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE);
        setCheckBox(mCurrSource);
    }

    @Override
    public void onClick(View v) {
    	int id = v.getId();
       
        if (id ==  R.id.btn_pm_usa) {
            setCheckBox(ConfigsetData.CONFIG_VALUE_PM_SOURCE_US);
            
        } else if (id ==  R.id.btn_pm_gov) {
            setCheckBox(ConfigsetData.CONFIG_VALUE_PM_SOURCE_GOV);
            
        } else if (id ==  R.id.setting_pm_back) {
            finish();
        }
    }

    private void setCheckBox(String sPMSource) {

        // set radio button by type   	
        if (TextUtils.isEmpty(sPMSource)){
            sPMSource = ConfigsetData.CONFIG_VALUE_PM_SOURCE_US;
        }
        chkUsa.setChecked(ConfigsetData.CONFIG_VALUE_PM_SOURCE_US.equalsIgnoreCase(sPMSource));
        chkGov.setChecked(ConfigsetData.CONFIG_VALUE_PM_SOURCE_GOV.equalsIgnoreCase(sPMSource));

        // save config
        if ((!TextUtils.isEmpty(sPMSource)) && (!sPMSource.equals(mCurrSource))) {
         	mCurrSource = sPMSource;
        	
            mCfgHelper.saveKey(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE, sPMSource);
            mCfgHelper.commit();
            
            WidgetGlobal.updateWidgets(getApplicationContext());
        }
    }
}
