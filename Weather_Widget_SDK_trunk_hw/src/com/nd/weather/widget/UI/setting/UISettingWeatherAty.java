/**   
*    
* @file 【天气设置】
* @brief
*
* @<b>文件名</b>      : UISettingWeatherAty
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-2-14 下午05:52:34 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.UI.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.weather.widget.Ctrl.SinglePopupWindow;
import com.nd.weather.widget.Ctrl.TimePopupWindow;
import com.nd.weather.widget.R;

import java.text.NumberFormat;

public class UISettingWeatherAty extends Activity 
	implements OnClickListener, OnCheckedChangeListener
{
	private TextView mtvUpdateState;
	private TextView mtvStartEndTimeState;
	private TextView mtv4IntervalState;
	private ConfigHelper mCfgHelper;
	private CheckBox mcbUpdate;
	
	private SinglePopupWindow<Float> mPwInveralTime = null;
	
	private TimePopupWindow mPwStartEndTime = null;
	
	int iBeginHour;
	int iBeginMin;
	int iEndHour;
	int iEndMin;
	float i4Interval;
	Float [] mInveral;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.weather_setting_weather);
		
		SetCtrl();
		InitData();
	}
	
	void SetCtrl() {
		mtvUpdateState = (TextView)findViewById(R.id.setting_weather_update_state);
		mtvStartEndTimeState = (TextView)findViewById(R.id.setting_weather_time_state);
		mtv4IntervalState = (TextView)findViewById(R.id.setting_weather_4interval_state);
		mcbUpdate = (CheckBox) findViewById(R.id.setting_weather_update_check);
		mcbUpdate.setOnCheckedChangeListener(this);
		
		((ViewGroup)findViewById(R.id.setting_weather_time_ll)).setOnClickListener(this);
		((ViewGroup)findViewById(R.id.setting_weather_update_ll)).setOnClickListener(this);
		((ViewGroup)findViewById(R.id.setting_weather_4interval_ll)).setOnClickListener(this);
		findViewById(R.id.setting_weather_btn_back).setOnClickListener(this);
	}
	
	void InitData() {
		mCfgHelper = ConfigHelper.getInstance(this);
		mcbUpdate.setChecked(mCfgHelper.loadBooleanKey(
				ConfigsetData.CONFIG_NAME_KEY_AUTOUPDATE,
				ConfigsetData.CONFIG_DEFAULT_AUTOUPDATE));
		
		iBeginHour = mCfgHelper.loadIntKey(ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEH,
										ConfigsetData.CONFIG_DEFAULT_BEGINGTIEMEH);
		iBeginMin = mCfgHelper.loadIntKey(ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEM,
										ConfigsetData.CONFIG_DEFAULT_BEGINGTIEMEM);

		iEndHour = mCfgHelper.loadIntKey(ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEH,
										ConfigsetData.CONFIG_DEFAULT_ENDTIEMEH);
		iEndMin = mCfgHelper.loadIntKey(ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEM,
										ConfigsetData.CONFIG_DEFAULT_ENDTIEMEM);
		i4Interval = mCfgHelper.loadFloatKey(ConfigsetData.CONFIG_NAME_KEY_TIEMUPDATE,
										ConfigsetData.CONFIG_DEFAULT_TIEMUPDATE);
		
		if (iEndHour < iBeginHour) {
			iBeginHour = iEndHour;
		}
		
		if (iEndHour == iBeginHour && iBeginMin > iEndMin) {
			iBeginMin = iEndMin;
		}
		
		mtvStartEndTimeState.setText(iBeginHour + String.format(":%02d", iBeginMin)
									+ " ~ " + iEndHour + String.format(":%02d", iEndMin));
		
		mtv4IntervalState.setText(NumberFormat.getInstance().format(i4Interval) + "小时");
	}
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.setting_weather_btn_back) {
			setResult(RESULT_OK, null);
			this.finish();

		} else if (id == R.id.setting_weather_time_ll) {
			SetStartEndTime();
		} else if (id == R.id.setting_weather_4interval_ll) {
			SetInveralTime();
		} else if (id == R.id.setting_weather_update_ll) {
			mcbUpdate.setChecked(!mcbUpdate.isChecked());
		}
	}

	void SetStartEndTime() {
	    if (mPwStartEndTime == null){
	        mPwStartEndTime = new TimePopupWindow(this, false);
		
	        mPwStartEndTime.setFocusable(true);
	        mPwStartEndTime.setAnimationStyle(R.style.PopupAnimation);
	        mPwStartEndTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if (view.getId() == R.id.pop_select_btn_ok) {
					iBeginHour = mPwStartEndTime.getStartHour();
					iBeginMin = mPwStartEndTime.getStartMins();
					iEndHour = mPwStartEndTime.getEndHour();
					iEndMin = mPwStartEndTime.getEndMins();
                    if ((iBeginHour == iEndHour) && (iBeginMin == iEndMin)){
                        Toast.makeText(UISettingWeatherAty.this, "开始时间和结束时间不能一样！", Toast.LENGTH_SHORT).show();
                        return;
                    }
					mCfgHelper.saveIntKey(ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEH, iBeginHour);
					mCfgHelper.saveIntKey(ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEM, iBeginMin);
					mCfgHelper.saveIntKey(ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEH, iEndHour);
					mCfgHelper.saveIntKey(ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEM, iEndMin);
					mCfgHelper.commit();
					
					mtvStartEndTimeState.setText(iBeginHour + String.format(":%02d", iBeginMin)
							+ " ~ " + iEndHour + String.format(":%02d", iEndMin));
				}
			}
		});
	    }
	    mPwStartEndTime.setStartTime(iBeginHour, iBeginMin);
	    mPwStartEndTime.setEndTime(iEndHour, iEndMin);
		
		// 显示RoundCorner对话框
	    mPwStartEndTime.showAtLocation(mtvStartEndTimeState, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
	}
	
	void SetInveralTime() {

		if (mInveral == null) {
			mInveral = new Float[48];

			for (int i = 0; i < mInveral.length; i++) {
				mInveral[i] = 0.5f * (i + 1);
			}
		}

		if (mPwInveralTime == null) {
			mPwInveralTime = new SinglePopupWindow<Float>(this, false);
			mPwInveralTime.setAnimationStyle(R.style.PopupAnimation);
			mPwInveralTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (view.getId() == R.id.pop_select_btn_ok) {

						i4Interval = 0.5f * (mPwInveralTime.getCurrentItem() + 1);
						mCfgHelper.saveFloatKey(ConfigsetData.CONFIG_NAME_KEY_TIEMUPDATE,
								i4Interval);
						mCfgHelper.commit();

						mtv4IntervalState.setText(NumberFormat.getInstance().format(i4Interval)
								+ "小时");
					}

					mPwInveralTime.dismiss();
				}
			});
		}
		mPwInveralTime.setArrayData(mInveral);
		mPwInveralTime.setCurrentItem((int) (i4Interval / 0.5f - 1));

		// 显示RoundCorner对话框
		mPwInveralTime.showAtLocation(mtv4IntervalState, Gravity.CENTER | Gravity.BOTTOM, 0, 0);

	}
	
	/**
	 * @brief
	 *
	 *
	 * @n<b>函数名称</b>     :onCheckedChanged
	 * @see
	
	 * @param  @param buttonView
	 * @param  @param isChecked
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-2-14下午08:09:46      
	*/
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mtvUpdateState.setText(isChecked? "自动": "手动");
		mCfgHelper.saveBooleanKey(ConfigsetData.CONFIG_NAME_KEY_AUTOUPDATE, isChecked);
		mCfgHelper.commit();
	}
}
