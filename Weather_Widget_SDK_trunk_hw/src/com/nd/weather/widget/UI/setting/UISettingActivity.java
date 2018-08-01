package com.nd.weather.widget.UI.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.calendar.Control.GetWeatherControler;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.shop.api6.model.AdvertItem;
import com.nd.hilauncherdev.shop.api6.net.ServerResult;
import com.nd.weather.widget.NetOptApi;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.banner.BannerView;
import com.nd.weather.widget.UI.weather.UIWeatherSetAty;

public class UISettingActivity extends Activity implements OnClickListener {
	
    private TextView txtWeatherRefresh;
    private TextView txtVersion;
    private TextView txtPmSource;
    private View vNewFlag;
    private LinearLayout bannerLayout;
    private BannerView bannerView;

    private ConfigHelper mCfgHelper = null;
    
    private Context mContext;
    
    private static boolean bneedClose = false;
    
    private Handler mHandler = new Handler();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = getApplicationContext();
        mCfgHelper = ConfigHelper.getInstance(mContext);
        initView();
        bneedClose = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (bneedClose){
		    finish();
		}else{
		    refreshText();
		}
	}


    private void initView() {
        setContentView(R.layout.weather_setting);

        // 天气
        findViewById(R.id.setting_weather_citys_ll).setOnClickListener(this);
        findViewById(R.id.setting_weather_refresh_ll).setOnClickListener(this);
        findViewById(R.id.setting_pm_mgr_ll).setOnClickListener(this);
        findViewById(R.id.setting_back).setOnClickListener(this);
        
        txtPmSource = (TextView) findViewById(R.id.setting_pm_source);
        
        // 关于
        findViewById(R.id.btn_commit_suggestion).setOnClickListener(this);
        findViewById(R.id.btn_about).setOnClickListener(this);

        txtWeatherRefresh = (TextView) findViewById(R.id.setting_weather_refresh_state);
        txtVersion = (TextView) findViewById(R.id.txt_version);
        txtVersion.setText("v" + ComDataDef.VERSION_NAME);      
        
        vNewFlag = findViewById(R.id.commit_suggestion_new_flag);
        bannerLayout = (LinearLayout) findViewById(R.id.banner);
        bannerView = new BannerView(mContext);
        bannerLayout.addView(bannerView);
        ThreadUtil.executeMore(new Runnable() {
			public void run() {
				//广告数据获取
				final ServerResult<AdvertItem> adList = NetOptApi.getAdInfoList_9004(mContext, ""+AdvertItem.AD_POSTION_WEATHER_BANNER);
				final ServerResult<ICommonDataItem> bList = new ServerResult<ICommonDataItem>();
				
				//Banner广告填充
				NetOptApi.addAdInfoToBanner(mContext, bList.itemList, adList.itemList);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (bList.itemList!=null && bList.itemList.size() > 0 ) {
							bannerLayout.setVisibility(View.VISIBLE);
							bannerView.setBannerData(bList.itemList);
							bannerView.setScroolState(true);
							bannerView.startScroll();
						} else {
							bannerLayout.setVisibility(View.GONE);
						}
					}
				});
			}
		});
    }

    public void refreshText() {

        // set auto refresh weather text
        boolean bAuto = mCfgHelper.loadBooleanKey(
                ComDataDef.ConfigsetData.CONFIG_NAME_KEY_AUTOUPDATE, true);
        if (bAuto) {
            txtWeatherRefresh.setText("自动");
        } else {
            txtWeatherRefresh.setText("手动");
        }

        String sPMSource = mCfgHelper.loadKey(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE);

        if (TextUtils.isEmpty(sPMSource)
                || ConfigsetData.CONFIG_VALUE_PM_SOURCE_US.equalsIgnoreCase(sPMSource)) {
            txtPmSource.setText("美使馆");
        } else {
            txtPmSource.setText("环保部");
        }
        
        boolean newflag = mCfgHelper.loadBooleanKey(ConfigHelper.KEY_NEW_FLAG, false);
        if (newflag){
            vNewFlag.setVisibility(View.VISIBLE);
        }else{
            vNewFlag.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v) {
        Intent i = null;
        int id = v.getId();
        
        if (id == R.id.setting_weather_refresh_ll) {  // 自动更新天气
            i = new Intent(mContext, UISettingWeatherAty.class);
            startActivity(i);
            
        } else if (id == R.id.setting_pm_mgr_ll) { // 默认空气指数
            i = new Intent(mContext, UISettingPmSourceAty.class);
            startActivity(i);

//        case R.id.setting_weibo_ll) {
//            SNSModule.getInstance(mContext); // 数据初始化
//            SNSShare.getInstance().ShowSnsSet(mContext, 2);
//            break;

        } else if (id ==  R.id.btn_commit_suggestion) {
            i = new Intent(mContext, UISubmitOpinionAty.class);
            startActivity(i);
            mCfgHelper.saveBooleanKey(ConfigHelper.KEY_NEW_FLAG, false);
            mCfgHelper.commit();

        } else if (id ==  R.id.setting_weather_citys_ll) {
            doShowSetAty();

        } else if (id ==  R.id.btn_about) {
            i = new Intent(mContext, UISettingAboutAty.class);
            startActivity(i);
            
        } else if (id == R.id.setting_back) {
        	finish();
        }
    }
    
    /**
     * @brief 【显示城市管理】
     * @n<b>函数名称</b> :doShowSetAty
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-5下午07:23:28
     */
    void doShowSetAty() {
        Intent intent = new Intent(this, UIWeatherSetAty.class);
        // 把更新控制器停了(任务清理),为了防止删除了，又添加进去
        GetWeatherControler.getInstance(this).clearTask();
        startActivity(intent);
    }
    public static void setNeedClose(){
        bneedClose = true;
    }
}
