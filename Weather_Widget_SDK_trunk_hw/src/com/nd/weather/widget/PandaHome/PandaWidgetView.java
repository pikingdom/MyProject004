/**   
 *    
 * @file 【91桌面插件视图基类】
 * @brief
 *
 * @<b>文件名</b>      : PandaWidgetView
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-7-11 下午03:07:16 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.weather.widget.PandaHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.provider.CalendarDatas;
import com.nd.calendar.provider.CalendarDatas.CityDataColumns;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;
import com.nd.hilauncherdev.net.ThemeLibUtil;
import com.nd.weather.widget.PandaHome.Receiver.PandaHomeThemeChangeReceiver;
import com.nd.weather.widget.R;
import com.nd.weather.widget.ReflectUtils;
import com.nd.weather.widget.TimeService;
import com.nd.weather.widget.UI.weather.UIWidgetCityMgrAty;
import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetTask;
import com.nd.weather.widget.WidgetTask.WidgetUpdateInterface;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.skin.WidgetLoadedSkinInfo;
import com.nd.weather.widget.skin.WidgetSkinConfig;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.UUID;

public abstract class PandaWidgetView extends RelativeLayout
{
	private static final String TAG = PandaWidgetView.class.getSimpleName();
	public final static int WIDGET_4X1 = 0;
	public final static int WIDGET_4X2 = 1;

//	protected static final String WIDGET_SKIN_FILE_MARK = "widget_panda_";
//	protected static final String[] WIDGET_CFG_KEY = { "panda_widget_4x1_ids", "panda_widget_4x2_ids" };

	protected static final String[] WIDGET_NAME = { "4x1", "4x2" };

	public final static int UPDATE_MSG_UPDATE_WIDGET = 1;
	public final static int UPDATE_MSG_HOTAREA = 2;
	public final static int SHOW_MSG_SKIN_TIPS = 5;
	public final static int UPDATE_MSG_SKIN = 6;
	public final static int UPDATE_MSG_FREE_CLICK = 7;
	public final static int UPDATE_MSG_INITDB = 8;
	public final static int UPDATE_MSG_VIEW = 9;
	public final static int UPDATE_MSG_START_SERVICE = 10;
		
	//////////////////////////////////////////////
	protected Context mContext;
	protected int	mWidgetId;
	private boolean mAttached;

	protected int	mWidgetType = WIDGET_4X1;

	/**
	 * V2.7.0以下完整版替换的天气插件View
	 */
	protected View mCalendarView = null;
	protected boolean mbCalendarRemove = false;
	protected SoftReference<Bitmap> bitmapCache = null;

	/**
	 * 插件图片宽
	 */
	protected int mSkinWidth = 0;
	/**
	 * 插件图片高
	 */
	protected int mSkinHeight = 0;
	/**
	 * 插件图片宽高比
	 */
	protected float mSkinInc = 0;
	/**
	 * 插件View宽高比
	 */
	protected float mWidgetInc = 0;
	
	protected int mSkinPaddingLeft = 0;
	protected int mSkinPaddingTop = 0;
	
	float mX, mY;
	/**
	 * 图片在插件View上实际显示的宽高
	 */
    int mSkinRealHeight = 0, mSkinRealWidth = 0;
    float mSkinHeightScale = 1, mSkinWidthScale = 1;
	
    ContentObserver mContentObserver = null;
    
    /**
     * onLoad是否被执行过(解决桌面插件跨屏拖动后无响应的问题 caizp 2014-10-10)
     */
    private boolean bOnload = false;
    
    //////////////////////////////////////////////////
	public abstract void onLoad(int widgetId);

	protected abstract WidgetLoadedSkinInfo getWidgetSkinInfo();
	protected abstract void setWidgetSkinInfo(WidgetLoadedSkinInfo skinInfo);
	
	protected abstract void setWidgetBuilder(WidgetTask widgetBuilder);
	protected abstract WidgetTask getWidgetBuilder();

	protected abstract int getWidgetLayout();
	protected abstract int getWidgetLoadingBackround();
		
	public PandaWidgetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "PandaWidgetView");
		mContext = context;
		mWidgetId = -1;
	}

	@Override
	protected void onAttachedToWindow() {
		if(!bOnload) {
			onLoad(mWidgetId);
		}
		
		super.onAttachedToWindow();
		Log.d(TAG, "onAttachedToWindow");
	}

	/**
	 * @brief 【反注册广播监听器】
	 * 
	 * @n<b>函数名称</b> :onDetachedFromWindow
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-7-11下午04:17:25
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d(TAG, "onDetachedFromWindow");

		unRegReceiver();
		
		bOnload = false;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	public void onDestory(int widgetId) {
		mWidgetId = widgetId;
		if (mCalendarView != null) {
			try {
				ReflectUtils.invokeMethod(mCalendarView, "onDestory", new Class<?>[] {int.class}, new Object[] {mWidgetId});
			} catch (Exception e) {
			}
		} else {
			
			// 2.7.0 以上调用状态设置
			WeatherLinkTools.getInstance(mContext).setWidgetState(widgetId, mWidgetType, false);
		}
	}
	
	private void callOnLoad() {
		try {
			ReflectUtils.invokeMethod(mCalendarView, "onLoad", new Class<?>[] {int.class}, new Object[] {mWidgetId});
		} catch (Exception e) {
		}
	}

	/**
	 * @brief 【插件加载】
	 * @n<b>函数名称</b>     :onLoad
	 * @param widgetId
	 * @param sType
	 * @param iRes
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-4-15下午07:24:38
	 * @<b>修改时间</b>      :
	*/
	public void onLoad(int widgetId, String sType, int iRes) {
		bOnload = true;
		mWidgetId = widgetId;
		Log.d(TAG, "onLoad");
		
		Context ctxCalendar = null;
		final WeatherLinkTools wlt = WeatherLinkTools.getInstance(mContext);
		
		try {
			if (mbCalendarRemove) {	// 主版被删除
				mbCalendarRemove = false;

				unRegReceiver();
				wlt.restoreSelf();			// 重置链接，恢复SDK处理过程
				
				if (mCalendarView != null) {
					if (!wlt.canLink()) {	// 小于 2.5.0 的，恢复SDK视图
						restoreView(iRes);
					}
					
					mCalendarView = null;
				}
				
			} else {
				// 检测是否有正式版
				ctxCalendar = wlt.getCalendarContext(true);
				if (ctxCalendar != null && !wlt.canLink_2_7_0()) {	// 2.7.0 以下启用View
					try {
						mCalendarView = getCalendarWidget(sType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				unRegReceiver();
			}
			
			regReceiver(ctxCalendar == null, wlt.canLink());
			
			if (ctxCalendar != null) {
				if (wlt.canLink()) {					// 2.5 以上启用链接功能
					if (mCalendarView != null) {		// 2.7.0 以下调用View的onLoad
						callOnLoad();
					} else 	if (wlt.canLink_2_7_0()) {	// 2.7.0 以上调用状态设置
						wlt.setWidgetState(widgetId, mWidgetType, true);
					}
					
					updateAppWidget(mContext, WidgetUtils.ACTION_INVALIDATE);
				} else {								// 2.5 以下启用View模式
					switchToCalendarView();
				}
				
				//通知初始化 本地数据，把标准版的数据写到91桌面
				mHandler.sendEmptyMessage(UPDATE_MSG_INITDB);
			} else {
				updateAppWidget(mContext, WidgetUtils.ACTION_INVALIDATE);
			}
			
			// 向91桌面请求当前主题信息(放在此处，也可以保证安装完整版后，完整版可以收到)
			WidgetGlobal.askPandaTheme(mContext);
			setClickable(true);
		    requestFocusFromTouch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief 【切换到天气主版】
	 * @n<b>函数名称</b>     :switchToCalendar
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-5下午6:00:27
	 * @<b>修改时间</b>      :
	*/
	private final void switchToCalendarView() {
		unRegReceiver();
		regReceiver(false, false);
		
		removeAllViews();
		addView(mCalendarView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		callOnLoad();
	}

	private PandaHomeThemeChangeReceiver mThemeReceiver;
	
	/**
	 * @brief 【注册必要的广播】
	 * @n<b>函数名称</b>     :regReceiver
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-6-24下午6:17:44
	 * @<b>修改时间</b>      :
	*/
	void regReceiver(boolean bRunOnSDK, boolean bLinked) {
		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter(WidgetUtils.WIDGET_REFRESH_ACTION);	// 
			boolean bUse = (bRunOnSDK || bLinked);
			
			if (bUse) {	// 2.5.0 以上 或 为安装
				filter.addAction(Intent.ACTION_TIME_TICK);
				filter.addAction(Intent.ACTION_SCREEN_ON);
				filter.addAction(Intent.ACTION_SCREEN_OFF);
				filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
				filter.addAction(Intent.ACTION_DATE_CHANGED);
				filter.addAction(Intent.ACTION_TIME_CHANGED);
				filter.setPriority(20);
			}
			
			mContext.registerReceiver(mIntentReceiver, filter);
			if(null == mThemeReceiver) {
				mThemeReceiver = new PandaHomeThemeChangeReceiver();
				IntentFilter themeFilter = new IntentFilter(mContext.getResources().getString(R.string.thenme_change_action));
				themeFilter.addAction("nd.panda.action.internal.ACTION_MODIFY_APP_NAME_FONT");
				mContext.registerReceiver(mThemeReceiver, themeFilter);
			}
			if (bUse) {
				IntentFilter intentFilterSD = new IntentFilter();
				intentFilterSD.addAction(Intent.ACTION_MEDIA_MOUNTED);
				intentFilterSD.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
				intentFilterSD.addDataScheme("file");
				
				mContext.registerReceiver(mSDReceiver, intentFilterSD);
			}
			
			if (bLinked) {
				filter = new IntentFilter(WeatherLinkTools.ACTION_UPDATE_WEATHER);
				filter.addAction(WeatherLinkTools.WIDGET_REFRESH_ACTION);
				mContext.registerReceiver(mWeatherUpdaterReceiver, filter);
				
				mContentObserver = new WeatherContentObserver(mHandler);
				registerContentObservers();
			}
		}
	}
	
	/**
	 * @brief 【解除祝福】
	 * @n<b>函数名称</b>     :unRegReceiver
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-6-25下午2:27:38
	 * @<b>修改时间</b>      :
	*/
	void unRegReceiver() { 
		try {
			mContext.unregisterReceiver(mIntentReceiver);
			mContext.unregisterReceiver(mSDReceiver);
			if(null != mThemeReceiver) {
				mContext.unregisterReceiver(mThemeReceiver);
				mThemeReceiver = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			mContext.unregisterReceiver(mWeatherUpdaterReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mContentObserver != null) {
			try {
				mContext.getContentResolver().unregisterContentObserver(mContentObserver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mContentObserver = null;
		}
		mAttached = false;
	}
	
	//////////////////////////////////////////////////////////////////
	/**
	 * @brief 【通知更新插件】
	 *
	 * @n<b>函数名称</b>     :UpdateData
	 * @param context
	 * @return    void   
	 * @<b>作者</b>          :  
	 * @<b>创建时间</b>      :  2012-4-17下午02:16:11      
	*/
	public static boolean updateWidgets(Context context, int iRefreshType) {
		WidgetUtils.sendBroadcast(context, iRefreshType);
		return true;
	}
	
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_MSG_UPDATE_WIDGET:
				updateAppWidget(mContext, msg.arg1);
				break;
				
			case SHOW_MSG_SKIN_TIPS:
				WidgetUtils.showSkinTips(mContext, msg.arg1);
				break;
				
			case UPDATE_MSG_FREE_CLICK:
				freeClick(msg.arg1, msg.arg2);
				break;
			case UPDATE_MSG_INITDB:
				CalendarContext.getInstance(mContext).Get_UserMdl_Interface();
				break;
			case UPDATE_MSG_VIEW:
				
				if (msg.obj != null) {
			        // 设置插件皮肤
			        setImageViewBitmap(R.id.IdImageViewBk, (Bitmap)msg.obj);
			        setTextView(R.id.TextViewMessage, "");
				} else {
					setTextView(R.id.TextViewMessage, "皮肤载入失败");
					setImageViewResource(R.id.IdImageViewBk,  getWidgetLoadingBackround());
				}
				break;
			}
		}
	};
	
	/**
	 * @brief 【更新插件視圖】
	 *
	 * @n<b>函数名称</b>     :updateAppWidget
	 * @param context
	 * @param appWidgetManager
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-4-27上午10:43:22      
	*/
	synchronized void updateAppWidget(Context context, int type) {
		WidgetTask skinBuilderThread = getWidgetBuilder();
		 
		if (skinBuilderThread == null || !skinBuilderThread.isAlive()) {
			skinBuilderThread = new WidgetTask();
			skinBuilderThread.SetBaseData(context, mWidgetType, mWidgetUpdateInf);
			setWidgetBuilder(skinBuilderThread);
			
			if (type == WidgetUtils.ACTION_SKIN_CHANGED 
				|| type == WidgetUtils.ACTION_CITY_SWITCH
				|| type == WidgetUtils.ACTION_UPDATE_WEATHER_UPDATING) {
				skinBuilderThread.setUpdateState(true);
			}
			skinBuilderThread.start();
			
		} else {
			if (type == WidgetUtils.ACTION_SKIN_CHANGED) {
				skinBuilderThread.setUpdateState(true);
				skinBuilderThread.setSkinChanged();
			} else {
				if (type == WidgetUtils.ACTION_CITY_SWITCH
					|| type == WidgetUtils.ACTION_UPDATE_WEATHER_UPDATING) {
					skinBuilderThread.setUpdateState(true);
				}
				
				skinBuilderThread.builderAgain();
			}
		}
	}
	
	/**
	 * @brief 【加载皮肤文件】
	 * @n<b>函数名称</b>     :loadSkin
	 * @param context
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-14下午04:09:13
	 * @<b>修改时间</b>      :
	*/
	WidgetLoadedSkinInfo loadWidgetSkin(Context context, int widgetType, boolean bNoCity) {
		WidgetLoadedSkinInfo widgetSkinInfo = getWidgetSkinInfo();
		
		int iRet;
		if (bNoCity) {
			iRet = widgetSkinInfo.loadWidgetSkin(context,
				WidgetGlobal.getDefaultNoCitySkinPath(context),
				WIDGET_NAME[widgetType],
				WidgetGlobal.getDefaultNoCitySkinPath(context));
		} else {
			 iRet = widgetSkinInfo.loadWidgetSkin(context,
				WeatherLinkTools.getInstance(context).getWidgetPandaSkinPath(),
				WIDGET_NAME[widgetType],
				WidgetGlobal.getDefaultSkinPath(context));
		}
		
		mHandler.sendMessage(mHandler.obtainMessage(SHOW_MSG_SKIN_TIPS, iRet, 0));
		
		return widgetSkinInfo;
	}

	// 更新回调事件
	protected WidgetUpdateInterface mWidgetUpdateInf = new WidgetUpdateInterface() {
		@Override
		public boolean setWidgetSkin(Bitmap bitmap) {
			mHandler.sendMessage(mHandler.obtainMessage(UPDATE_MSG_VIEW, bitmap));
			return false;
		}

		@Override
		public WidgetLoadedSkinInfo loadSkin(boolean bNoCity) {
			return loadWidgetSkin(mContext, mWidgetType, bNoCity);
		}
	};
	
	///////////////////////////////////////////////////////////////////////
	
	// 结果接收，用于视图更新
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		protected boolean mScreenOff = false;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null == intent) {
				return;
			}
			
			try {
				final String sAction = intent.getAction();
				int refTpye = -1;
				
				 if (Intent.ACTION_TIME_TICK.equals(sAction)
					|| Intent.ACTION_TIMEZONE_CHANGED.equals(sAction)			// 时区改变
					|| Intent.ACTION_DATE_CHANGED.equals(sAction)				// 日期改变
					|| Intent.ACTION_TIME_CHANGED.equals(sAction)) {
					if (!mScreenOff) {
						refTpye = WidgetUtils.ACTION_TIMER;
					}
				} else if (WidgetUtils.WIDGET_REFRESH_ACTION.equals(sAction)) {
					refTpye = intent.getIntExtra(WidgetUtils.REFRESH_TYPE, WidgetUtils.ACTION_INVALIDATE);
				} else if (Intent.ACTION_SCREEN_OFF.equals(sAction)) {
					mScreenOff = true;
				} else if (Intent.ACTION_SCREEN_ON.equals(sAction)) {
					mScreenOff = false;
					refTpye = WidgetUtils.ACTION_INVALIDATE;
				}
				
				optReceiverUpdate(context, intent, refTpye);
			} catch (Exception e) {
			}
		}
	};

	private final BroadcastReceiver mSDReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String sAction = intent.getAction();
			
			if (sAction.equalsIgnoreCase(Intent.ACTION_MEDIA_MOUNTED) ||
				sAction.equalsIgnoreCase(Intent.ACTION_MEDIA_UNMOUNTED)) {
				optReceiverUpdate(context, intent, WidgetUtils.ACTION_INVALIDATE);
			}
		}
	};
	
	private BroadcastReceiver mWeatherUpdaterReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String sAction = intent.getAction();
			if (WeatherLinkTools.ACTION_UPDATE_WEATHER.equals(sAction)) {
				// 更新天气
				final int id = intent.getIntExtra(WeatherLinkTools.PARAM_ID, 0);
				if (id <= 0) {
					return;
				}

				int state = intent.getIntExtra(WeatherLinkTools.PARAM_STATE, WeatherLinkTools.STATE_NORMAL);
				switch (state) {
				case WeatherLinkTools.STATE_POSTION_FAIL:
				case WeatherLinkTools.STATE_UPDATE_FAIL:
				case WeatherLinkTools.STATE_UPDATE_SUCCESS:
					optReceiverUpdate(context, intent, WidgetUtils.ACTION_UPDATE_WEATHER);
					break;
				case WeatherLinkTools.STATE_UPDATE_ING:
					optReceiverUpdate(context, intent, WidgetUtils.ACTION_UPDATE_WEATHER_UPDATING);
					break;
				}
			} else {
				int type = intent.getIntExtra(WeatherLinkTools.REFRESH_TYPE, 0);
				if (type == WeatherLinkTools.ACTION_CITY_SWITCH) {
					optReceiverUpdate(context, intent, WidgetUtils.ACTION_CITY_SWITCH);
				}
			}
		}
	};
	
	
	/**
	 * @brief 【主版数据库变更事件】
	 * @n<b>类名称</b>     :PandaWidgetView
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-19上午11:07:24
	 * @<b>修改时间</b>      :
	*/
	class WeatherContentObserver extends ContentObserver {
		public WeatherContentObserver(Handler handler) {
			super(handler);
		}

		@Override  
	    public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.d(TAG, "onChange");
			mHandler.sendMessage(mHandler.obtainMessage(UPDATE_MSG_UPDATE_WIDGET, WidgetUtils.ACTION_UPDATE_WEATHER, 0));
		}
	}
	
	private void registerContentObservers() {
		WeatherContentObserver wco = new WeatherContentObserver(mHandler);
		// 注册内容观察者
		try {
			mContext.getContentResolver().registerContentObserver(CityDataColumns.CONTENT_URI_CALENDAR, true, wco);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据IMEI及CUID获取随机数
	 * @param context
	 * @param range
	 * @return
	 */
	private int randomByIMEI(Context context, int range) {
		String randomString = ThemeLibUtil.getIMEI(context) + ThemeLibUtil.getCUID(context);
		if(TextUtils.isEmpty(randomString)) {
			randomString = UUID.randomUUID().toString().replace("-", "");
		}
		int randomValue = Math.abs(randomString.hashCode() % range);
		return randomValue;
	}

	private int randomUpdateDelay = -1;

	/**
	 * @brief 【】
	 * @n<b>函数名称</b>     :optReceiverUpdate
	 * @param context
	 * @param intent
	 * @param refTpye
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-6-25下午7:55:44
	 * @<b>修改时间</b>      :
	*/
	void optReceiverUpdate(Context context, Intent intent, int refTpye) {
		try {
			if (refTpye != -1) {
				Log.d(TAG, "[" + WIDGET_NAME[mWidgetType] + "] refTpye= " + refTpye);

				if (refTpye == WidgetUtils.ACTION_WIDGET_RELOAD) {
					mbCalendarRemove = intent.getBooleanExtra(WidgetUtils.WIDGET_RELOAD_PARAM, false);
					onLoad(mWidgetId);
					return;
				}

				if (refTpye == WidgetUtils.ACTION_TIMER) {
					// 解决天气数据更新请求出现分钟级脉冲的问题 caizp 2017-07-12
					if(-1 == randomUpdateDelay) {
						randomUpdateDelay = randomByIMEI(context, 60 * 100) * 10;
					}
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							TimeService.autoUpdateFirstWeather(mContext, false);
						}
					}, randomUpdateDelay);
					// 每天统计一次
					WidgetUtils.statWidget(mContext, "WidgetDesk_Panda");
				}
                
				updateAppWidget(context, refTpye);
			}
			
		} catch (Exception e) {
		}
	}
	/////////////////////////////////////////////

	/**
	 * @brief 【设置图片】
	 *
	 * @n<b>函数名称</b>     :setWidgetImage
	 * @param id
	 * @param imageBmp
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-7-13下午03:02:01      
	*/
	protected void setWidgetImageView(int id, Bitmap imageBmp) {
		try {
			ImageView imView = (ImageView)findViewById(id);
			if (imView != null) {
				imView.setImageBitmap(imageBmp);

				mSkinHeight = imageBmp.getHeight();
				mSkinWidth = imageBmp.getWidth();
//				float g = mSkinInc;
				
				mSkinInc = (float)mSkinWidth / mSkinHeight;
				//Log.e("WidgetClick", "setWidgetImageView+++++++++ mSkinInc="+mSkinInc);
				//修复天气插件随机不响应点击事件的BUG caizp 2014-10-31
//				if (g == 0.0f) {
				culateInc();
//				}
				
//				mSkinInc = g;
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * @brief 【设置图片】
	 * @n<b>函数名称</b>     :setImageViewBitmap
	 * @param id
	 * @param bitmap
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-4-18上午10:26:21
	 * @<b>修改时间</b>      :
	*/
	protected void setImageViewBitmap(int id, Bitmap bitmap) {
		if (bitmap != null) {
			SoftReference<Bitmap> newCache = new SoftReference<Bitmap>(bitmap);
			setWidgetImageView(id, newCache.get());
			
			if(bitmapCache != null){
				Bitmap bmpPrev = bitmapCache.get();
				if (bmpPrev != null) {
					bmpPrev.recycle();
				}
				
				bitmapCache.clear();
			}
			
			bitmapCache = newCache;
			System.gc();
		}
	}
	
	protected void setImageViewResource(int id, int drawableId) {
		try {
			if (id != 0) {
				ImageView imView = (ImageView)findViewById(id);
				if (imView != null) {
					imView.setImageResource(drawableId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTextView(int id, String sText) {
		if (sText != null) {
			try {
				TextView tvText = (TextView)findViewById(id);
				if (tvText != null) {
					tvText.setText(sText);
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * @brief 【重新加载】
	 * @n<b>函数名称</b>     :requreWidgetReload
	 * @param context
	 * @param bUninstall
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-4-17下午03:44:53
	 * @<b>修改时间</b>      :
	*/
	public static void requreWidgetReload(Context context, boolean bUninstall) {
		Intent intent = new Intent(WidgetUtils.WIDGET_REFRESH_ACTION);
		intent.addFlags(32);
		intent.putExtra(WidgetUtils.REFRESH_TYPE, WidgetUtils.ACTION_WIDGET_RELOAD);
		intent.putExtra(WidgetUtils.WIDGET_RELOAD_PARAM, bUninstall);
		context.sendBroadcast(intent);
	}
	
	///////////////////////////////////////////////////////////////////
	public final static String CALENDAR_WIDGET_LAYOUT_4x1 = "widget_panda_4x1";
	public final static String CALENDAR_WIDGET_LAYOUT_4x2 = "widget_panda_4x2";	
	
	private final void restoreView(int iRes) {
		Log.d("widget", "restoreView");
		removeAllViews();
		LayoutInflater.from(mContext).inflate(iRes, this, true);
	}
	
	/**
	 * @brief 【】
	 * @n<b>函数名称</b>     :getCalendarWidget
	 * @param sLayoutName
	 * @return
	 * @return    View   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-12-4上午11:33:19
	 * @<b>修改时间</b>      :
	*/
	private final View getCalendarWidget(String sLayoutName) {
		
		Context ctx = WeatherLinkTools.getInstance(mContext).getCalendarContext(true);
		if (ctx != null) {
			Resources othRes = ctx.getResources();
			if (othRes != null) {
				// 读取其他APK资源，需要先取得ID
				int layoutId = othRes.getIdentifier(sLayoutName, "layout", ComDataDef.CALENDAR_PACKAGE);
				if (layoutId != 0) {
					return View.inflate(ctx, layoutId, null);
				}
			}
		}

		return null;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		try {
			mWidgetInc = (float)w / h;
			//Log.e("WidgetClick", "onSizeChanged++++++ mWidgetInc="+mWidgetInc+",w="+w+",h="+h);
			culateInc();
			//Log.e("WidgetClick", "onSizeChanged++++++ culateInc ok...");
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onSizeChanged(w, h, oldw, oldh);
	}

	void culateInc() {
		try {
			if (mSkinInc > 0) {
				int h = getMeasuredHeight();
				int w = getMeasuredWidth();
//				Log.e("WidgetClick", "culateInc++++++ getMeasuredHeight="+getMeasuredHeight()+",getMeasuredWidth="+getMeasuredWidth());
				if (mWidgetInc > mSkinInc) {
					mSkinRealHeight = h;
					mSkinRealWidth = (int) (mSkinInc * mSkinRealHeight);
				} else {
					mSkinRealWidth = w;
					mSkinRealHeight = (int) (mSkinRealWidth / mSkinInc);
				}

				mSkinPaddingLeft = (w - mSkinRealWidth) / 2;
				mSkinPaddingTop = (h - mSkinRealHeight) / 2;
				
				// 皮肤坐标比例
				mSkinHeightScale = (float) mSkinHeight / mSkinRealHeight;
				mSkinWidthScale = (float) mSkinWidth / mSkinRealWidth;
//				Log.e("WidgetClick", "culateInc++++++ mSkinHeight="+mSkinHeight+",mSkinRealHeight="+mSkinRealHeight);
//				Log.e("WidgetClick", "culateInc------ mSkinWidth="+mSkinWidth+",mSkinRealWidth="+mSkinRealWidth);
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * @brief 【单击的时候触发事件】
	 *
	 * @n<b>函数名称</b>     :performClick
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2013-1-15下午06:20:46      
	*/
	@Override
	public boolean performClick() {
		actionDown();
		return super.performClick();
	}

	/**
	 * @brief 【获取坐标】
	 *
	 * @n<b>函数名称</b>     :onTouchEvent
	 * @param event
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2013-1-17上午11:22:00      
	*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mX = event.getX();
		mY = event.getY();
		Log.e(TAG, "height="+getMeasuredHeight());
		return super.onTouchEvent(event);
	}

	void actionDown() {
		try {
			int clkX = (int)mX;
			int clkY = (int)mY;
			//Log.e("WidgetClick", "mX="+mX+",mY="+mY);
			// 校正有效坐标
			if (mSkinPaddingLeft != 0) {
				clkX -= mSkinPaddingLeft;
			}
			
			if (mSkinPaddingTop != 0) {
				clkY -= mSkinPaddingTop;			
			}

			if (clkX >= 0 && clkY >= 0) {
				// 转换视图坐标到皮肤坐标
				if (mSkinWidthScale != 1.0f) {
					clkX = (int) (mSkinWidthScale * clkX);
				}
				
				if (mSkinHeightScale != 1.0f) {
					clkY = (int) (mSkinHeightScale * clkY);
				}
				//Log.e("WidgetClick", "mSkinPaddingLeft="+mSkinPaddingLeft+",mSkinPaddingTop="+mSkinPaddingTop);
				//Log.e("WidgetClick", "mSkinWidthScale="+mSkinWidthScale+",mSkinHeightScale="+mSkinHeightScale);
				mHandler.sendMessage(mHandler.obtainMessage(UPDATE_MSG_FREE_CLICK, clkX, clkY));
				//sendClickEventToApp(clkX, clkY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @brief 【动态热区自由点击事件】
	 * @n<b>函数名称</b>     :freeClick
	 * @param x
	 * @param y
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-16下午07:17:14
	 * @<b>修改时间</b>      :
	*/
	void freeClick(int x, int y) {
		WidgetLoadedSkinInfo skinInfo = getWidgetSkinInfo();
		if (skinInfo == null) {
			return;
		}
		
		WidgetSkinConfig skinCfg = skinInfo.skinConfig;
		if (skinCfg == null) {
			return;
		}

		int drawImage = skinCfg.getHotElementFromPos(x, y);

		Log.i("WidgetClick", "clickResult="+drawImage);
		switch (drawImage) {
		case WidgetSkinConfig.SkinHotArea.HOT_AREA_NULL:
			break;
		case WidgetSkinConfig.SkinHotArea.HOT_AREA_CITY: {
			Intent intent = WeatherLinkTools.getInstance(mContext).getCityMgrIntent();
			if (intent == null || android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
				intent = new Intent(mContext, UIWidgetCityMgrAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(UUID.randomUUID().toString());
			}
			
			try {
				HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_click_distribute), "5");
				mContext.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				intent = new Intent(mContext, UIWidgetCityMgrAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(UUID.randomUUID().toString());
				mContext.startActivity(intent);
			}
			
		}
			break;
		case WidgetSkinConfig.SkinHotArea.HOT_AREA_DATE: {
			if(processFanYueClick(mContext, "fanyue_weather_widget_date_action_list")) {
				break;
			}
			if(WeatherPluginManger.getInstance().openWeatherPlugin(mContext)) break;
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_click_distribute), "2");
			Intent intent = WeatherLinkTools.getInstance(mContext).getCalendarIntent();
			if (intent == null || android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
				WidgetHotAreaEvent.startCalendar(mContext);
			} else {
				try {
					mContext.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			break;
			
		case WidgetSkinConfig.SkinHotArea.HOT_AREA_TIME:
			if(processFanYueClick(mContext, "fanyue_weather_widget_time_action_list")) {
				break;
			}
			if(WeatherPluginManger.getInstance().openWeatherPlugin(mContext)) break;
			WidgetHotAreaEvent.startAlarmClock(mContext);
			break;
			
		case WidgetSkinConfig.SkinHotArea.HOT_AREA_WEATHER:
			if(processFanYueClick(mContext, "fanyue_weather_widget_weather_action_list")) {
				break;
			}
			WeatherLinkTools wlt = WeatherLinkTools.getInstance(mContext);
			// 无城市时进入天气详情界面
			if(CalendarContext.getInstance(mContext).Get_WeatherMdl_Interface().getCityCount() == 0) {
				wlt.startWeatherPage();
				break;
			}
			if(WeatherPluginManger.getInstance().openWeatherPlugin(mContext)) break;
			// 有网络，则更新；没网络，则显示直接进入APP
			if (HttpToolKit.isNetworkAvailable(mContext)) {
				WidgetTask skinBuilderThread = getWidgetBuilder();
				if (skinBuilderThread != null && skinBuilderThread.isFailure()) {
					updateAppWidget(mContext, WidgetUtils.ACTION_UPDATE_WEATHER_UPDATING);
					wlt.autoUpdateFirstWeather(true);
				} else {
					wlt.startWeatherPage();
				}
			} else {
				wlt.startWeatherPage();
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * 处理帆悦定制版配置的点击轮洵事件
	 * @param context
	 * @param loadSpKey
     * @return
     */
	private static boolean processFanYueClick(Context context, String loadSpKey) {
		String switchAction = BaseConfigPreferences.getInstance().getBaseSP().getString(loadSpKey, "");
		if(TextUtils.isEmpty(switchAction)) {// 未配置打开轮洵列表，打开默认界面
			return false;
		} else {
			String[] actions = switchAction.split(";");
			int index = Integer.valueOf(actions[actions.length - 1]);
			int sourceIndex = index;
			while(true) {
				String action = actions[index];
				Intent intent = new Intent();
				if (action.contains("|")) {
					String[] actionComp = action.split("\\|");
					intent.setClassName(actionComp[0], actionComp[1]);
				} else {
					intent = context.getPackageManager().getLaunchIntentForPackage(action);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				index++;
				if (index >= actions.length - 1) {
					index = 0;
				}
				try {
					context.startActivity(intent);
					eventClick(intent.getComponent().getPackageName(), loadSpKey);
					break;
				} catch (Exception e) {
					e.printStackTrace();
					if(sourceIndex == index) return false;
				}
			}

			actions[actions.length - 1] = index + "";
			String newSwtichAction = "";
			for(String actionValue : actions){
				newSwtichAction += actionValue +";";
			}
			newSwtichAction = newSwtichAction.substring(0, newSwtichAction.length() - 1);
			BaseConfigPreferences.getInstance().getBaseSP().edit().putString(loadSpKey, newSwtichAction).commit();
			return true;
		}
	}

	private static void eventClick(String packageName, String loadSpKey) {
		try {
			int type = 0;
			if("fanyue_weather_widget_date_action_list".equals(loadSpKey)) {// 日期打点
				type = 3;
			} else if("fanyue_weather_widget_time_action_list".equals(loadSpKey)) {// 时间打点
				type = 2;
			} else if("fanyue_weather_widget_weather_action_list".equals(loadSpKey)) {// 天气打点
				type = 4;
			}
			if(0 == type) return;
			Class customChannelController = Class.forName("com.nd.hilauncherdev.launcher.CustomChannelController");
			Method eventWidget = customChannelController.getMethod("eventNavigationWidget", String.class, int.class);
			eventWidget.invoke(null, packageName, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
