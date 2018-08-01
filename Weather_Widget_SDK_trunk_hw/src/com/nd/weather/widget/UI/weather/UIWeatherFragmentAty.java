package com.nd.weather.widget.UI.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.DateInfo;
import com.calendar.CommData.DayWeatherInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.GetWeatherControler;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ComDataDef.ConfigSet;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.util.CalendarInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.FileHelp;
import com.nd.hilauncherdev.analysis.ActualTimeAnalysis;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysis;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysisConstant;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.sdk.AdvertSDKController;
import com.nd.weather.widget.PandaHome.WeatherPluginManger;
import com.nd.weather.widget.PandaHome.WidgetHotAreaEvent;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.CommonUI;
import com.nd.weather.widget.UI.UIBaseAty;
import com.nd.weather.widget.UI.UICalendarGuideAty;
import com.nd.weather.widget.UI.setting.UISettingActivity;
import com.nd.weather.widget.UI.weather.AllDayWeatherView.IOnDayWeatherListener;
import com.nd.weather.widget.UI.weather.CityViewFlipper.IOnFlipperCity;
import com.nd.weather.widget.UI.weather.WeatherScrollView.WeatherScrollViewListener;
import com.nd.weather.widget.UI.weather.ad.AdImageView;
import com.nd.weather.widget.UI.weather.ad.AdImageView.OnShowCallBack;
import com.nd.weather.widget.UI.weather.ad.AdImageView.OnclickCallBack;
import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetUtils;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import static com.nd.weather.widget.PandaHome.WeatherPluginManger.WEATHER_PACKAGE_NAME;

/**
 * @brief 【】
 * @n<b>类名称</b> :
 * @<b>作者</b> : 陈希
 * @<b>修改</b> :
 * @<b>创建时间</b> : 2012-9-11下午07:41:43
 * @<b>修改时间</b> :
 */
public class UIWeatherFragmentAty extends UIBaseAty implements OnClickListener,
		IOnDayWeatherListener, IOnFlipperCity {
	static final String TAG = "UIWeatherFragmentAty";

	// 刷新界面
	public static final String ACTION_REFRESH_VIEW = "com.calendar.action.REFRESH_VIEW";
	public static final String ACTION_REFRESH_HUANGLI = "com.calendar.action.REFRESH_HUANGLI";

	private ICalendarContext m_calendarMgr = null;
	private CityViewFlipper mPager;
	private View mMainBk;
	private BlurImageSwitcher mImBackground;
	private WeatherScrollView middleContentScrollView;

	private TextView tvWeatherCity;
	private TextView tvWeatherDate;

	private CommonLightbar cityLightbar;
	
	private AdImageView bottomAdBanner;

	public boolean m_bFinshUpdate = true;
	private static boolean mToRefresh = false;
//	private boolean isAdd = false;

	private String mLastPMSource = "";// ConfigsetData.CONFIG_DEFAULT_PM_SOURCE;

	private ConfigHelper mCfgHelper;

	private AllDayWeatherView mAllDayWeatherView;
	private List<CityWeatherInfo> m_arrListInfo = new Vector<CityWeatherInfo>();

	private boolean mbRefreshWidget = false; // 需要刷新桌面插件

	private final int MSG_WEATHER_PROGRESS = 1;
	private final int MSG_WEATHER_FINISH = 2;
	private final int MSG_WARNING_FINISH = 3;
	private final int MSG_SYSTIME_TIME_ERROR = 4;
	private final int MSG_REFRESH_PM_PARAM = 5;

	private static int mLastGetDate;
	private static boolean bFromWidget = true;
	/* 外面取了数据进来，要刷新界面 getCityWeatherList 这个是在Welcome调用 */
	/* 因为getCityWeatherList这个方法里面没办法刷新界面 */
	private static boolean mNeedRefreshView = false;

	/* 从插件或者城市设置界面过来指定要显示哪个城市 */
	private static int mCityIndex = -1;

	/* 最后一次时区 */
	private static int mTimeZoneOffset = TimeZone.getDefault().getRawOffset();

	// 线程刷新数据
	private Handler initHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WEATHER_PROGRESS:
				/* 状态反馈 */
				if (mPager != null) {
					final CityWeatherInfo c = (CityWeatherInfo) msg.obj;
					mPager.refreshState(c);
				}
				break;
			case MSG_WEATHER_FINISH:
				/* 更新完成 */
				if (mPager != null) {// && !mActPaused
					final CityWeatherInfo cityInfo = (CityWeatherInfo) msg.obj;
					UIWeatherFragmentAty.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// long a = System.currentTimeMillis();
							CityWeatherInfo c = mPager.getDisplayCityInfo();
							if (c != null) {
								if (cityInfo.getId() == c.getId()) {
									// 更新滑动界面
									mPager.refreshDisplayCity();
									// 更新四天 天气
									// System.out.println("MSG_WEATHER_FINISH setAllDayWeatherView");
									// (System.currentTimeMillis() - a) + "");
									setAllDayWeatherView(cityInfo);
								}
							}
						}
					});

				}

				if (msg.arg1 == 1) {
					if (msg.arg2 == 1) {
						mbRefreshWidget = true;// 需要刷新桌面插件
					} else if (msg.arg2 == 0) {

					}
				}

				break;
			case MSG_WARNING_FINISH:
				/* 更新预警 */
				if (mPager != null) {
					final CityWeatherInfo cw = (CityWeatherInfo) msg.obj;
					mPager.refreshWarning(cw);
				}
				break;
			case MSG_SYSTIME_TIME_ERROR:
				/* 更新失败 */
				if (msg.arg1 == 1) {
					Toast.makeText(UIWeatherFragmentAty.this,
							R.string.weather_wrong_time_msg, Toast.LENGTH_SHORT)
							.show();
				} else if (msg.arg2 == 1) {
					Toast.makeText(UIWeatherFragmentAty.this,
							R.string.waring_wrong_time_msg, Toast.LENGTH_SHORT)
							.show();
				}
				break;

			case MSG_REFRESH_PM_PARAM:
				if (mPager != null) {
					mPager.refreshPMIndex();
				}
				break;
			}

		}
	};

	// ///////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		HiAnalytics.init(this);
		initImageLoaderConfig(this);
		CvAnalysis.submitPageStartEvent(this, WidgetUtils.getAnalyticsId(this, R.string.weather_detail_ad_cv_page_id));
		initData();
		initView();
		RecommendWeatherAppHelper.getInstance().fetchRecommendWeatherAppInfo(getApplicationContext());
		bFromWidget = true;
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				WeatherPluginManger.getInstance().prestrainWeather(UIWeatherFragmentAty.this);
				if (AndroidPackageUtils.isPkgInstalled(UIWeatherFragmentAty.this, WEATHER_PACKAGE_NAME) || WeatherPluginManger.getInstance().isInstallApp(WEATHER_PACKAGE_NAME)){
					return;
				}
				//能进入中间页还没安装成功就尝试下载，下次进入的时候就直接装上了
				WeatherPluginManger.getInstance().tryWifiDownloadWeather(UIWeatherFragmentAty.this);
			}
		});
	}
	
	
	 /**
     * 图片缓存配置参数
     */
    public static final int DEFAULT_DISKCACHE_SIZE = 50*1024*1024;//默认sd卡的缓存设置为50M
    public static final int DISK_FILE_CACHE_COUNT = 750; //默认缓存文件数量
    
	/**
     * 初始化图片缓存配置
     * @param mContext
     */
    public static void initImageLoaderConfig(Context mContext) {
        try {
            if (!ImageLoader.getInstance().isInited()) {
                File file = FileHelp.getCalendarGuideImageDir(mContext);
                LruDiskCache lruDiskCache = new LruDiskCache(file, null, DefaultConfigurationFactory.createFileNameGenerator(), DEFAULT_DISKCACHE_SIZE, DISK_FILE_CACHE_COUNT);
                ImageLoaderConfiguration imageLoaderConfiguration =
                        new ImageLoaderConfiguration.Builder(mContext.getApplicationContext())
                                .diskCache(lruDiskCache).build();
                ImageLoader.getInstance().init(imageLoaderConfiguration);
                L.disableLogging();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

	@Override
	protected void onResume() {
		super.onResume();

		// 注册广播，监听时间变化
		registerActionReceiver();

		Intent intent = WeatherLinkTools.getInstance(getApplicationContext())
				.getWeatherIntent();
		if (intent != null) {
			try {
				startActivity(intent);
				finish();
				return;
			} catch (Exception e) {
			}
		}

		initHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 当没有天气数据时，快速刷新，避免空白界面
				if ((m_arrListInfo.size() < 1)) {
					RefreshData();
				} else {

					// 刷新 PM 指数
					String sPMSource = mCfgHelper
							.loadKey(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE);
					if (TextUtils.isEmpty(sPMSource)) {
						sPMSource = ConfigsetData.CONFIG_VALUE_PM_SOURCE_US;
					}

					if (!mLastPMSource.equalsIgnoreCase(sPMSource)) {
						mLastPMSource = sPMSource;
						// 清理缓存
						clearWeatherIndex();
						// 通知界面刷新
						initHandler.sendEmptyMessage(MSG_REFRESH_PM_PARAM);
					}

					RefreshData();

				}
				cityLightbar.refresh(m_arrListInfo.size() < 0 ? 1
						: m_arrListInfo.size(), getCityIndex(mPager
						.getDisplayCityInfo() == null? 0:mPager.getDisplayCityInfo().getId()));
				// 刷新宜忌数据
				refreshYiJiData(true);
			}
		}, 80);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 离开界面的时候停止 监听广播
		try {
			unregisterReceiver(mReceiver);
		} catch (Exception e) {
		}

		refreshWidget();
	}

	// 注册广播，监听时间变化
	private void registerActionReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK);
		intentFilter.addAction(ACTION_REFRESH_VIEW);

		registerReceiver(mReceiver, intentFilter);
	}

	// 清理缓存(生活指数)
	private void clearWeatherIndex() {
		for (CityWeatherInfo c : m_arrListInfo) {
			if (c != null) {
				c.setWeatherIndex(null);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【初始化数据】
	 * @n<b>函数名称</b> :initData
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-18上午11:22:17
	 */
	void initData() {
		Context context = getApplicationContext();
		m_calendarMgr = CalendarContext.getInstance(context);
		mCfgHelper = ConfigHelper.getInstance(context);
	}

	void initView() {
		setContentView(R.layout.weather_main);
		middleContentScrollView = (WeatherScrollView) findViewById(R.id.weather_scrollview);
		mPager = (CityViewFlipper) findViewById(R.id.FlingGalleryId);
		mAllDayWeatherView = (AllDayWeatherView) findViewById(R.id.all_day_weather_view);
		mMainBk = findViewById(R.id.weather_main_bk);
		mImBackground = (BlurImageSwitcher) findViewById(R.id.imgBackground);
		mImBackground.setCurrentBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mh_wip_bk_na));
		Context context = getApplicationContext();
		Drawable cityPlug = null;
		int size = ComfunHelp.dip2px(context, 15);
		int padding = ComfunHelp.dip2px(context, 5);
		cityPlug = context.getResources().getDrawable(R.drawable.city_plug);
		cityPlug.setBounds(0, 0, size, size);

		tvWeatherCity = (TextView) findViewById(R.id.weather_city);
		tvWeatherDate = (TextView) findViewById(R.id.weather_date);
		tvWeatherCity.setCompoundDrawables(null, null, cityPlug, null);
		tvWeatherCity.setCompoundDrawablePadding(padding);

		tvWeatherCity.setOnClickListener(this);
		tvWeatherDate.setOnClickListener(this);

		findViewById(R.id.weather_btn_setting).setOnClickListener(this);
		findViewById(R.id.weather_btn_refresh).setOnClickListener(this);
		findViewById(R.id.weather_btn_more).setOnClickListener(this);
		findViewById(R.id.weather_more_layout).setOnClickListener(this);

		mAllDayWeatherView.setOnDayWeatherListener(this);

		mPager.setOnFlipperCity(this);
		mPager.setData(m_arrListInfo);
		/* 判断是否有指定城市 */
		if ((mCityIndex == -1) || (mCityIndex > m_arrListInfo.size() - 1)) {
			mCityIndex = 0;
		}

		cityLightbar = (CommonLightbar) findViewById(R.id.weather_city_flipper_lightbar);
		cityLightbar.setNormalLighter(context.getResources().getDrawable(
				R.drawable.launcher_menu_presonal_compaign_rightbar_default));
		cityLightbar.setSelectedLighter(context.getResources().getDrawable(
				R.drawable.launcher_menu_presonal_compaign_rightbar_check));
		cityLightbar.refresh(1, mCityIndex);
		cityLightbar.setGap(ComfunHelp.dip2px(context, 5));
		mPager.setCurrentItem(mCityIndex);

		// 初始化四天天气(有数据并且不需要刷新) 是出现在当退出软件m_arrListInfo数据没有过期的时候
		if (!bFromWidget && (m_arrListInfo.size() > 0) && (!mNeedRefreshView)) {
			CityWeatherInfo c = m_arrListInfo.get(mCityIndex);
			setAllDayWeatherView(c);
		}

		mCityIndex = -1;
		// 设置滚动监听，用户有滚动上拉200dip距离后，下次进来不自动滚动 caizp 2016-05-17
		middleContentScrollView.setScrollViewListener(new WeatherScrollViewListener() {
			@Override
			public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx,
					int oldy) {
				boolean needAutoScroll = mCfgHelper.loadBooleanKey("need_auto_scroll", true);
				if(!needAutoScroll) return;
				if(y > ScreenUtil.dip2px(getApplicationContext(), 200)) {
					mCfgHelper.saveBooleanKey("need_auto_scroll", false);
					mCfgHelper.commit();
				}
			}
		});
		// 自动滚动，提示用户可上拉滚动 caizp 2016-05-17
		if(mCfgHelper.loadBooleanKey("need_auto_scroll", true)) {
			initHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					middleContentScrollView.smoothScrollBy(0, ScreenUtil.dip2px(getApplicationContext(), 120));
					initHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							middleContentScrollView.smoothScrollBy(0, -ScreenUtil.dip2px(getApplicationContext(), 120));
						}
					}, 750);
				}
			}, 500);
		}
		// 请求悬浮图标广告
		ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
            	AdvertInfo advertInfo = null;
                //加载广告
            	List<AdvertInfo> list = AdvertSDKController.getAdvertInfos(getApplicationContext(), AdImageView.TYPE_WEATHER_DETAIL_ICON);
            	if (list == null || list.size() == 0) {
            		return;
            	}
            	for(int i = list.size() - 1; i >= 0; i --){
            		if(!TextUtils.isEmpty(list.get(i).picUrl)){
            			advertInfo = list.get(i);
            			if(null != mPager && null != advertInfo) {
            				mPager.refreshAdImage(AdImageView.TYPE_WEATHER_DETAIL_ICON, advertInfo);
            			}
            			break;
            		}
            	}
            }
        });
		// 请求底部banner广告
		bottomAdBanner = (AdImageView) findViewById(R.id.bottom_ad_banner);
		bottomAdBanner.loadPosAd(AdImageView.TYPE_WEATHER_DETAIL_BOTTOM_BANNER);
		bottomAdBanner.setOnclickCallBack(new OnclickCallBack() {
			@Override
			public void callback() {
				int	functionId = ActualTimeAnalysis.TENCENT_VALID_HITS;
				HiAnalytics.submitEvent(UIWeatherFragmentAty.this, AnalyticsConstant.LAUNCHER_THEME_MENU, "gg");
				ActualTimeAnalysis.sendActualTimeAnalysis(UIWeatherFragmentAty.this, functionId, "ztc");
				CvAnalysis.submitClickEvent(UIWeatherFragmentAty.this, WidgetUtils.getAnalyticsId(UIWeatherFragmentAty.this, R.string.weather_detail_ad_cv_page_id), WidgetUtils.getAnalyticsId(UIWeatherFragmentAty.this, R.string.weather_detail_bottom_banner_cv_pos_id), bottomAdBanner.resId, CvAnalysisConstant.RESTYPE_ADS);
			}
		});
		bottomAdBanner.setOnShowCallBack(new OnShowCallBack() {
			@Override
			public void callback() {
				CvAnalysis.submitShowEvent(UIWeatherFragmentAty.this, WidgetUtils.getAnalyticsId(UIWeatherFragmentAty.this, R.string.weather_detail_ad_cv_page_id), WidgetUtils.getAnalyticsId(UIWeatherFragmentAty.this, R.string.weather_detail_bottom_banner_cv_pos_id), bottomAdBanner.resId, CvAnalysisConstant.RESTYPE_ADS);
			}
		});
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 */
	public int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.weather_btn_setting) {
			startActivity(new Intent(this, UISettingActivity.class));
		} else if (id == R.id.weather_btn_refresh) {
			mPager.refreshCurrentWeather();
		} else if (id == R.id.weather_btn_more) {
			if (WeatherPluginManger.getInstance().openWeatherPlugin(getApplicationContext())){//能打开黄历或黄历插件就不再去下载完整版插件
				return;
			}
			if(TelephoneUtil.isWifiEnable(getApplicationContext())) {
				final String appIdentifier = ConfigHelper.getInstance(this).getReCommendWeatherAppIdentifier();
				if (ComfunHelp.checkApkExist(getApplicationContext(), appIdentifier, 0)) {
					PackageManager pm = getApplicationContext().getPackageManager();
					Intent intent = pm.getLaunchIntentForPackage(appIdentifier);
					if(null == intent) return;
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					SystemUtil.startActivitySafely(getApplicationContext(), intent);
				} else {
					// wifi情况下直接下载
					CommonUI.downCalendarApk(this, WidgetUtils.DOWNLOAD_CLICK_FROM_MORE_DOWNLOAD_IMMED);
				}
			} else {
				WidgetUtils.startGuide(this,
						UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
						WidgetUtils.DOWNLOAD_CLICK_FROM_MORE);
			}
		} else if (id == R.id.weather_city) {
			DoShowSetAty();
		} else if (id == R.id.weather_date) {
//			WidgetUtils.startGuide(this,
//					UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
//					WidgetUtils.DOWNLOAD_CLICK_FROM_HUANGLI);
//			HiAnalytics.submitEvent(this, WidgetUtils.getAnalyticsId(this, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_NONGLI);
			// 点天气详情的日期固定进入系统日历 caizp 2016-5-24 V7.5.2 改动
			WidgetHotAreaEvent.startCalendar(this);
		}
	}

	void setCurrentItem() {

		int index = 0;
		if (bFromWidget) {
			// 根據桌面插件当前城市设置视图
			bFromWidget = false;
			SharedPreferences set = WidgetUtils.getSetting(
					getApplicationContext(),
					ComDataDef.ConfigsetData.CONFIG_NAME);
			int id = set.getInt(ConfigSet.CONFIG_KEY_WIDGET_CITY_ID, 0);

			if (id > 0) {
				index = getCityIndex(id);
			}
		} else if (mCityIndex != -1) {
			// 从设置界面过来的
			index = mCityIndex;
		}

		mCityIndex = -1;
		mPager.setCurrentItem(index);
		if (m_arrListInfo.size() - 1 >= index) {
			CityWeatherInfo c = m_arrListInfo.get(index);
			// mAdapter.getCurrCityWeatherInfo(mPager.getCurrentItem());
			if (c != null && c.getWeatherInfo() != null) {
				setAllDayWeatherView(c);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	void RefreshData() {
		if ((mToRefresh) || isNewDay() || isNeedReadCache(m_calendarMgr)) {
			// 没数据或者数据隔天了
			getCityList();
			// 确保有城市
			if (checkState()) {
				setCurrentItem();
			}

		} else {
			if (mNeedRefreshView) {
				mNeedRefreshView = false;
				// 刷新所有城市(主要考虑在welcome界面取完数据进来界面需要刷新)
				setCurrentItem();
			} else {
				CityWeatherInfo c = mPager.getDisplayCityInfo();

				// 数据有变化才刷新
				if (!c.getIsRefreshToView()) {
					// 刷新多天天气
					setAllDayWeatherView(c);
					// 刷新当前城市
					mPager.refreshDisplayCity();
				}

				// 刷新实时天气图标(必须判断是否到日落时间)
				mPager.refreshWeatherImgWithSun();
			}
		}

		if ((bFromWidget) || (mCityIndex != -1)) {
			// 如果有重新查询城市列表，前面已经定位当前城市，并且bFromWidget设置为false
			// 这里只是为了处理没有重新查询，从桌面插件进来的，这样可以避免不必要的刷新
			setCurrentItem();
		}

		final GetWeatherControler control = GetWeatherControler
				.getInstance(this);
		control.setHandler(initHandler);
		control.setErrorTimeMsg(MSG_SYSTIME_TIME_ERROR);
		control.setFinishMsg(MSG_WEATHER_FINISH);
		control.setRefreshInProcessMsg(MSG_WEATHER_PROGRESS);
		control.setWarnFinishMsg(MSG_WARNING_FINISH);

		if ((m_arrListInfo.size() > 0)
				&& (HttpToolKit.isNetworkAvailable(this))) {
			boolean bAutoUpdate = mCfgHelper.loadBooleanKey(
					ConfigsetData.CONFIG_NAME_KEY_AUTOUPDATE, true);
			if (bAutoUpdate) {
				// 有自动更新

				// 判断是更新首城市还是全部
				final boolean bUpdateAll = mCfgHelper.loadBooleanKey(
						ConfigsetData.CONFIG_NAME_KEY_UPDATE_ALL, true);
				if (bUpdateAll) {
					// 更新全部
					control.addTasks(m_arrListInfo);
				} else {
					// 更新首城市
					control.addTask(m_arrListInfo.get(0));
				}
			}
		}

	}

	// 更新头部信息
	private void refreshTop() {
		String gmt = null;
		CityWeatherInfo c = null;
		if ((m_arrListInfo.size() > 0) && (mPager != null)) {
			c = mPager.getDisplayCityInfo();
			if (c != null) {
				gmt = c.getCityGMT();
			}
		}
		DateInfo mDateInfo = CalendarInfo.getSysDateInfo(gmt);
		StringBuffer weatherDateInfo = new StringBuffer();
		weatherDateInfo.append(mDateInfo.getMonth() + "月" + mDateInfo.getDay()
				+ "日 ");
		weatherDateInfo.append(CalendarInfo.DayOfWeek(mDateInfo));
		weatherDateInfo.append("\n");
		weatherDateInfo
				.append(CalendarInfo.getLunarEx(mDateInfo).split(" ")[1]);

		tvWeatherDate.setText(weatherDateInfo.toString());
		tvWeatherCity.setText(c.getCityName());
	}

	private void refreshYiJiData(boolean bRefresh) {
		String gmt = null;
		if ((m_arrListInfo.size() > 0) && (mPager != null)) {
			CityWeatherInfo c = mPager.getDisplayCityInfo();
			if (c != null) {
				gmt = c.getCityGMT();
			}
		}
		mAllDayWeatherView.refreshYiJiData(gmt, bRefresh);
	}

	/**
	 * @brief 【防止过频繁刷新，影响效率】
	 * @n<b>函数名称</b> :setToRefresh
	 * @param bRefresh
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-18上午11:22:17
	 */
	public static void setRefreshRetrunFromSubAty(boolean bRefresh) {
		// 城市数据出现变化（用于城市设置后返回刷新）
		mToRefresh = bRefresh;
	}

	// ///////////////////////////////////////////////////////////////////////////////

	/**
	 * @brief 【显示城市设置】
	 * @n<b>函数名称</b> :DoShowSetAty
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-3-31上午10:33:01
	 */
	void DoShowSetAty() {
		Intent intent = new Intent(this, UIWeatherSetAty.class);
		intent.setClassName(getPackageName(), UIWeatherSetAty.class.getName());
		if (!ComfunHelp.checkActivity(this, intent)) {
			GetWeatherControler.getInstance(this).clearTask();
			startActivity(intent);
		}
	}

	boolean checkState() {
		// 没有城市
		if (m_arrListInfo.size() < 1) {
			// 如果是第一次进入天气就进入设置界面
			boolean bFirstRun = mCfgHelper.loadBooleanKey(
					ConfigSet.CONFIG_KEY_FIRST_TO_WEATHER, true);
			if (bFirstRun) {
				DoShowSetAty();
				return false;
			} else {
				// 生成空的城市信息
				setNullCityWeather();
			}
		}
		return true;
	}

	void setNullCityWeather() {
		CityWeatherInfo c = new CityWeatherInfo();
		c.setCityName("添加城市");
		c.setRealTimeWeather(new RealTimeWeatherInfo());
		c.setWeatherInfo(new DayWeatherInfo());
		m_arrListInfo.add(c);
	}

	final int getCityIndex(int id) {
		final int size = m_arrListInfo.size();
		for (int i = 0; i < size; i++) {
			if (m_arrListInfo.get(i).getId() == id) {
				return i;
			}
		}
		return 0;
	}

	private void getCityList() {
		mToRefresh = false;
		mTimeZoneOffset = TimeZone.getDefault().getRawOffset();
		// 记录取数据的时间
		mLastGetDate = Calendar.getInstance().getTime().getDate();
		clearCacheList();
		m_calendarMgr.Get_WeatherMdl_Interface().getCityWeatherList(
				m_arrListInfo);
	}

	// 判断是否隔天了
	private static boolean isNewDay() {
		// 时区变了，或者不是统一天了
		return ((mTimeZoneOffset != TimeZone.getDefault().getRawOffset()) || (mLastGetDate != new Date(
				System.currentTimeMillis()).getDate()));
	}

	// 判断是否需要重新读取数据
	private boolean isNeedReadCache(ICalendarContext cc) {
		try {
			// 判断首城市
			if (m_arrListInfo.size() > 0) {
				final CityWeatherInfo c = m_arrListInfo.get(0);
				boolean breturn = cc.Get_WeatherMdl_Interface()
						.isNeedReadCache(c.getId(), c.getNowWeatherTime(),
								WeatherModule.TYPE_NOW);
				return breturn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @Title: clearCacheList
	 * @Description: TODO(清理缓存)
	 * @author yanyy
	 * @date 2012-5-14 下午05:35:30
	 * 
	 * @return void
	 * @throws
	 */
	public void clearCacheList() {
		try {
			m_arrListInfo.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.gc();
	}

	/* 设置要显示的城市 */
	public static void setCityIndex(int i) {
		mCityIndex = i;
	}

	private void setAllDayWeatherView(final CityWeatherInfo c) {
		if (c != null && c.getWeatherInfo() != null
				&& mAllDayWeatherView != null) {
			initHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mAllDayWeatherView.setDayWeatherInfo(c.getWeatherInfo(),
							getViewType());
					// 刷新宜忌数据和农历日期(不同城市时区不一样)
					refreshYiJiData(false);
					refreshTop();
				}
			}, 100);
		}

		cityLightbar.update(getCityIndex(c.getId()));
		refreshWeatherColor(c);
	}

	private void refreshWeatherColor(CityWeatherInfo c) {
		try {
			final String info = c.getRealTimeWeather().getNowWeather();
			boolean bNight = c.isNight();
			int color = WeatherModule.GetFinalWeathColor(info, bNight);
			mMainBk.setBackgroundColor(color);
			String bgUrl = mCfgHelper.loadKey(c.getCityCode()+"_bg");
			if(TextUtils.isEmpty(bgUrl)) return;
			ImageLoader.getInstance().loadImage(bgUrl, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
				}
				@Override
				public void onLoadingComplete(String imageUrl, View view, Bitmap bm) {
					mImBackground.switchTo(bm);
				}
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getViewType() {
		int type = AllDayWeatherView.VIEW_TYPE_DAY;
		// int id = mRgViewType.getCheckedRadioButtonId();
		//
		// if (id == R.id.rb_change_to_weather) {
		// type = AllDayWeatherView.VIEW_TYPE_DAY;
		//
		// } else if (id == R.id.rb_change_to_temp) {
		// type = AllDayWeatherView.VIEW_TYPE_TEMP;
		// }

		return type;
	}

	@Override
	public void onDayWeatherFling() {
		// int id = (mRgViewType.getCheckedRadioButtonId());
		//
		// if (id == R.id.rb_change_to_weather) {
		// mRgViewType.check(R.id.rb_change_to_temp);
		// } else if (id == R.id.rb_change_to_temp) {
		// mRgViewType.check(R.id.rb_change_to_weather);
		// }
	}

	@Override
	public void onShowTemperatureCurveDetail() {
		// 点击多日温度图表推荐下载黄历客户端
		WidgetUtils.startGuide(this, UICalendarGuideAty.CALENDAR_2015_GUIDE,
				null, WidgetUtils.DOWNLOAD_CLICK_FROM_TEMP);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String sAction = intent.getAction();

			// receive start slide show intent or screen_off message
			if (Intent.ACTION_TIME_TICK.equals(sAction)) {
				// 时间变了 判断是否隔天
				if (isNewDay()) {
					mToRefresh = true;
					// 刷新数据
					RefreshData();
				}
			} else if (ACTION_REFRESH_VIEW.equals(sAction)) {
				// 刷新当前界面
				if (mPager != null) {
					CityWeatherInfo c = mPager.getDisplayCityInfo();
					if (c != null) {
						// 更新滑动界面
						mPager.refreshDisplayCity();
						// 更新四天 天气
						setAllDayWeatherView(c);
					}
				}
			} else if (ACTION_REFRESH_HUANGLI.equals(sAction)) {
				refreshYiJiData(true);
			}
		}

	};

	@Override
	public void onFilpperCityEnd(CityWeatherInfo c) {
		setAllDayWeatherView(c);
	}

	private void refreshWidget() {
		if (mbRefreshWidget) {
			mbRefreshWidget = false;
			WidgetGlobal.updateWidgets(getApplicationContext());
		}
	}

	private void clearBitmap() {
		try {
			if (mAllDayWeatherView != null) {
				mAllDayWeatherView.clearBitmap();
			}
			if (mPager != null) {
				mPager.clearBitmap();
			}
		} catch (Exception e) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CvAnalysis.submitPageEndEvent(this, WidgetUtils.getAnalyticsId(this, R.string.weather_detail_ad_cv_page_id));
		clearBitmap();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((KeyEvent.KEYCODE_BACK == keyCode) && (event.getRepeatCount() == 0)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
