package com.nd.weather.widget.UI.weather.ad;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.nd.hilauncherdev.analysis.integral.IntegralSubmitUtil;
import com.nd.hilauncherdev.analysis.integral.IntegralTaskIdContent;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.sdk.AdvertSDKController;
import com.nd.weather.widget.UI.weather.UIWeatherFragmentAty;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 广告封装View
 */
public class AdImageView extends ImageView implements View.OnClickListener {
	
	/**
	 * 天气详情页悬浮图标广告位置，由服务端分配
	 */
	public final static String TYPE_WEATHER_DETAIL_ICON = "34";
	
	/**
	 * 天气详情页底部banner广告位置，由服务端分配
	 */
	public final static String TYPE_WEATHER_DETAIL_BOTTOM_BANNER = "35";

    private static final int UPLOAD_ADS_DATA = 1 << 2;

    private Context ctx;

    //广告跳转URL
    private String actionUrl;
   
	private String picUrl;
    
    private String adPos;
    public AdvertInfo mAdvertInfo = null;
    private String title;
    public int resId;
    public int sourceId = 0;
    private boolean autoAdjustHeight = true;
    
    public boolean isAutoAdjustHeight() {
		return autoAdjustHeight;
	}

	public void setAutoAdjustHeight(boolean autoAdjustHeight) {
		this.autoAdjustHeight = autoAdjustHeight;
	}

	public interface OnclickCallBack{
    	public void callback();
    }
    private OnclickCallBack onclickCallBack;
    public void setOnclickCallBack(OnclickCallBack onclickCallBack){
    	this.onclickCallBack = onclickCallBack;
    }
    
    public interface OnShowCallBack{
    	public void callback();
    }
    private OnShowCallBack onShowCallBack;
    public void setOnShowCallBack(OnShowCallBack onShowCallBack){
    	this.onShowCallBack = onShowCallBack;
    }
    public interface OnNoAdCallBack{
    	public void callback();
    }
    private OnNoAdCallBack onNoAdCallBack;
    public void setOnNoAdCallBack(OnNoAdCallBack onNoAdCallBack){
    	this.onNoAdCallBack = onNoAdCallBack;
    }
    
    public AdImageView(Context context) {
        this(context, null);
    }

    public AdImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
        init();
    }

    private void init() {
        this.setOnClickListener(this);
        UIWeatherFragmentAty.initImageLoaderConfig(ctx);
    }
    
    public void loadPosAd(String pos){
    	load(pos);
    }
    
    /**
     * <br>Description: 设置广告
     * <br>Author:caizp
     * <br>Date:2016年5月30日下午2:38:35
     * @param pos
     * @param advertInfo
     */
    public void setAdvertInfo(String pos, AdvertInfo advertInfo) {
    	if(null == advertInfo || TextUtils.isEmpty(advertInfo.picUrl) || TextUtils.isEmpty(pos)) return;
    	adPos = pos;
		resId = advertInfo.id;
		sourceId = advertInfo.sourceId;
		title = advertInfo.name;
        //广告加载完毕，更新UI
        handler.obtainMessage(UPLOAD_ADS_DATA, advertInfo).sendToTarget();
    }
    
    /**
     * 加载广告
     */
    private void load(String pos) {
    	mAdvertInfo = null;
    	adPos = pos;
        setVisibility(View.GONE);
        loadAds();
    }

	public String getTitle() {
		return title;
	}

    /**
     * 加载广告数据
     */
    private void loadAds() {
        Drawable drawable = null;
        if(!TextUtils.isEmpty(picUrl)){
            //事先加载缓存
            setImageDrawable(drawable);
            setVisibility(View.VISIBLE);
            invalidate();
            if (onShowCallBack != null){
            	onShowCallBack.callback();
            }
            return;
        }

        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                Object adsObj = null;
                //加载广告
            	List<AdvertInfo> list = AdvertSDKController.getAdvertInfos(ctx, adPos);
            	if (list == null || list.size() == 0) {
            		return;
            	}
            	for(int i = list.size() - 1; i >= 0; i --){
            		if(!TextUtils.isEmpty(list.get(i).picUrl)){
            			adsObj = list.get(i);
            			resId = ((AdvertInfo)adsObj).id;
            			sourceId = ((AdvertInfo)adsObj).sourceId;
            			title = ((AdvertInfo)adsObj).name;
            			break;
            		}
            	}
                //广告加载完毕，更新UI
                handler.obtainMessage(UPLOAD_ADS_DATA, adsObj).sendToTarget();
            }
        });
    }

    @Override
    public void onClick(View view) {
    	doClick();
    }
    
    public void doClick() {
    	 //处理广告点击事件
     	if(mAdvertInfo != null){
     		AdvertSDKController.submitClickEvent(getContext(), handler, mAdvertInfo);
     		AdvertSDKController.startAdSdkBrowserActivityForLauncherProcess(ctx, mAdvertInfo, actionUrl, null);
     	}else{
     		startBrowserActivity(ctx, actionUrl, "from_ad_banner");
     	}
         if (onclickCallBack != null){
         	onclickCallBack.callback();
         }
         IntegralSubmitUtil.getInstance(ctx).submit(ctx, IntegralTaskIdContent.ADMIRE_ADS_TENCENT_OR_TAOBAO_BANNER);
    }

    /**
     * <br>Description: 用浏览器打开网页
     * <br>Author:caizp
     * <br>Date:2014-5-15上午9:41:56
     */
    private void startBrowserActivity(Context ctx, String url, String from) {
    	 Intent intent = new Intent("com.nd.hilauncherdev.app.activity.X5Launcher");
         try {
             //intent = new Intent(Intent.ACTION_VIEW);
            // intent.setData(Uri.parse("http://www.google.com"));
           //  intent.setClassName(ctx.getPackageName(), "com.nd.hilauncherdev.app.activity.X5Launcher");
             intent.putExtra("url", url);
             intent.putExtra("from", from);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             ctx.startActivity(intent);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Object obj = msg.obj;
            switch (msg.what) {
                case UPLOAD_ADS_DATA://广告数据加载完成
                    if (obj != null) {
                        notifyViewRefresh(obj);
                    } else {// 无广告时的回调
                    	if (onNoAdCallBack != null){
                    		onNoAdCallBack.callback();
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 刷新View
     *
     * @param ads 广告实体
     */
    private void notifyViewRefresh(Object ads) {
    	try {
			if (ads instanceof AdvertInfo) {
				mAdvertInfo = ((AdvertInfo) ads);
				picUrl = mAdvertInfo.picUrl;
				actionUrl = mAdvertInfo.actionIntent;
			}
	        if (!TextUtils.isEmpty(picUrl)) {
	        	ImageLoader.getInstance().displayImage(picUrl, this, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}
					@Override
					public void onLoadingComplete(String imageUrl, View view, Bitmap bm) {
						AdImageView.this.setVisibility(View.VISIBLE);
						AdImageView.this.invalidate();
						if (onShowCallBack != null){
							onShowCallBack.callback();
						}
					}
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if(null == drawable) return;
		if(autoAdjustHeight) {
			int drawableWidth = (int)(drawable.getIntrinsicWidth() * ScreenUtil.getDensity());
			int drawableHeight = (int)(drawable.getIntrinsicHeight() * ScreenUtil.getDensity());
			int screenWidth = ScreenUtil.getCurrentScreenWidth(ctx);
			int screenHeight = ScreenUtil.getCurrentScreenHeight(ctx);
			int width = screenWidth;
			if(drawableWidth <= 0 || width <= 0 || drawableHeight <= 0) return;
			int height = (int)(drawableHeight * width * 1.0f / drawableWidth);
			if(height > screenHeight/2) {
				setScaleType(ScaleType.CENTER_CROP);
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.AD_BANNER_HEIGHT_EXCEED_HALF_SCREEN, screenWidth+"_"+screenHeight);
				return;
			}
			getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
			getLayoutParams().height = height;
			setScaleType(ScaleType.FIT_XY);
			requestLayout();
		}
	}
    
}

