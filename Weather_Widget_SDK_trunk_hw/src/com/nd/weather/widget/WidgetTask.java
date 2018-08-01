/**   
 *    
 * @file 【桌面插件处理线程】
 * @brief
 *
 * @<b>文件名</b>      : ProgressWidgeTask
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>修改</b>   : 陈希
 * @n@n<b>创建时间</b>: 2011-7-5 下午07:00:27 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.CityWeatherJson;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.WeatherModule;
import com.nd.weather.widget.skin.WidgetLoadedSkinInfo;
import com.nd.weather.widget.skin.WidgetSkinBuilder;

public class WidgetTask extends Thread {
	private Context mContext;
	private ICalendarContext mCalCxt;

//	private String sSkinFileName;
	private Integer iBuilder = 0;
	private boolean bSkinChanged = false;

	private WidgetSkinBuilder mWidgetSkinBuilder;
	private CityWeatherInfo mCityWeather = null;
	private WidgetUpdateInterface mWidgetUpdateInf;
	WeatherModule weatherModle;
	private boolean mUpdating = false;
	private boolean bFailure = false;

//	private static Thread mUpdateThread = null;
//	static Object mLock = new Object();

	/**
	 * @brief 【更新回调接口】
	 * @n<b>类名称</b> :WidgetTask
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-7-13上午11:10:29
	 * @<b>修改时间</b> :
	 */
	public interface WidgetUpdateInterface {
		public WidgetLoadedSkinInfo loadSkin(boolean bNoCity);
		public boolean setWidgetSkin(Bitmap bitmap);
	}

	// 设置基本参数数据
	public void SetBaseData(Context context, int nType, WidgetUpdateInterface widgetUpdateInterface) {
		mContext = context.getApplicationContext();
		if (mContext == null) {
			mContext = context;
		}
		
		mWidgetUpdateInf = widgetUpdateInterface;
		
		mWidgetSkinBuilder = new WidgetSkinBuilder(context, nType);
		mCalCxt = CalendarContext.getInstance(mContext);
	}

	public void setUpdateState(boolean bUpdating) {
		mUpdating = bUpdating; 
	}
	
	public boolean isFailure() {
		return bFailure;
	}
	
	public void setSkinChanged() {
		iBuilder++;
		bSkinChanged = true;
	}

	public void builderAgain() {
		iBuilder++;
	}
	
	/**
	 * @brief
	 * @n<b>函数名称</b> :run
	 * @see
	 * @param
	 * @<b>修改</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-17下午02:35:21
	 */
	@Override
	public void run() {
		if (mWidgetUpdateInf == null) {
			return;
		}
		
		weatherModle = mCalCxt.Get_WeatherMdl_Interface();
		boolean bDefaultSkin = false;
		boolean bLoadSkin = true;
	
		WeatherLinkTools wlt = WeatherLinkTools.getInstance(mContext);
		// 没有城市的时候自动定位
		if (weatherModle.getCityCount() == 0) {
			if (HttpToolKit.isNetworkAvailable(mContext)) {
				wlt.localCity();
			}

			// 加载默认皮肤
			bDefaultSkin = true;
		}
		
		mCityWeather = new CityWeatherInfo(CityWeatherInfo.TYPE_WIDGET);
		mWidgetSkinBuilder.setWeatherInfo(mCityWeather);
		
		do {
			// 获取城市天气数据
			try {
				int id = wlt.getFirstCityID();
				Log.e("WeatherWidget", "getFirstCityID="+id);
				if (weatherModle.getCityWeatherWidget(id, mCityWeather)) {
					// 有可能城市id变了
					int newId = mCityWeather.getId();
					if (newId != id) {
						wlt.setFirstCityID(newId);
					}
				} else {
					mCityWeather.setCityJson(new CityWeatherJson());
					bDefaultSkin = true;// 加载默认皮肤
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 设置更新状态
			mWidgetSkinBuilder.setUpdating(mUpdating);
			bFailure = mCityWeather.updateFailure();
			
			if (!bDefaultSkin) {
				bDefaultSkin = //(mCityWeather.inavailable() && !HttpToolKit.isNetworkAvailable(mContext))|| 
					(weatherModle.getCityCount() == 0);
			}
			
			// 换肤
			if (bLoadSkin || bSkinChanged || bDefaultSkin) {
				mWidgetSkinBuilder.SetSkinInfo(mWidgetUpdateInf.loadSkin(bDefaultSkin));
				bLoadSkin = false;
			}
			
			//////// 绘制插件 ////////
			UpdateWidget();
			/////////////////////////
			
			if (bSkinChanged) {
//				if (bDefaultSkin && weatherModle.getCityCount() != 0) {
//					bDefaultSkin = false;
//				}
//				
//				mWidgetSkinBuilder.SetSkinInfo(mWidgetUpdateInf.loadSkin(bDefaultSkin));
				bSkinChanged = false;
			}
			
			if (iBuilder-- <= 0) {
				break;
			}

		} while (true);
	}

	/**
	 * @brief 【更新插件】
	 * @n<b>函数名称</b> :UpdateWidget
	 * @return void
	 * @<b>作者</b> :
	 * @<b>修改</b> : 陈希
	 * @<b>创建时间</b> : 2012-5-15下午05:14:43
	 * @<b>修改时间</b> :
	 */
	private void UpdateWidget() {
		// 1.构建插件图片
		Bitmap bitmap = mWidgetSkinBuilder.getWidgetBitmap();
		mWidgetUpdateInf.setWidgetSkin(bitmap);

		Log.d("Widget", "[" + (mWidgetSkinBuilder.getWigetType() == 0 ? "4x1": "4x2") + "] --- updated ---");
	}
	
}
