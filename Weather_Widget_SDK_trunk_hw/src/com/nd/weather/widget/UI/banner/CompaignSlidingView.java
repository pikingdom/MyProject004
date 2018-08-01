package com.nd.weather.widget.UI.banner;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nd.hilauncherdev.analysis.ThemeDownloadPathAnalysis;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.shop.api6.model.AdvertItem;
import com.nd.hilauncherdev.shop.api6.model.ThemeBannerItem;
import com.nd.weather.widget.NetOptApi;
import com.nd.weather.widget.R;

public class CompaignSlidingView extends NestedSlidingView implements NestedSlidingView.OnCommonSlidingViewClickListener {

	final static String TAG = "CompaignSlidingView";

	private Context context;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				scrollRight();
				Message msg2 = new Message();
				msg2.what = 0;
				sendMessageDelayed(msg2, 3000);
			}
		}

	};

	public CompaignSlidingView(Context context) {
		super(context);
		this.context = context;
	}

	public CompaignSlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CompaignSlidingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void initSelf(Context ctx) {
	}

	@Override
	public View onGetItemView(ICommonData data, int position) {

		final ThemeBannerItem dataItem = (ThemeBannerItem) data.getDataList().get(position);

		final ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.banner_loading));
		imageView.setScaleType(ScaleType.FIT_XY);

		ThreadUtil.executeOther(new Runnable() {
			@Override
			public void run() {
				try {
					if (dataItem.bannerIconUrl!=null && !dataItem.bannerIconUrl.trim().equals("")) {
						final Drawable drawable = AsyncImageLoader.loadImageFromUrl(dataItem.bannerIconUrl);
						if (drawable != null) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									imageView.setImageDrawable(drawable);
									// dataItem.setLazyBitmap(remoteBitmap);
								}
							});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return imageView;
	}

	@Override
	protected void onLayoutChildrenAfter() {
		super.onLayoutChildrenAfter();

		/*
		 * if (!this.list.get(0).getDataList().isEmpty()) {
		 * handler.sendEmptyMessage(0); }
		 */
	}

	@Override
	public void onItemClick(View v, int positionInData, int positionInScreen, int screen, ICommonData data) {
		ThemeBannerItem dataItem = (ThemeBannerItem) data.getDataList().get(screen);
		if (dataItem == null)
			return;

		if (dataItem.bannerIconUrl == null || dataItem.bannerIconUrl.trim().equals(""))
			return;

		Uri postUri = null;
		try {
			postUri = Uri.parse(dataItem.bannerIconUrl);
		} catch (Exception e) {
			postUri = null;
		}
		if (postUri == null)
			return;

		switch (dataItem.bannerType) {
		case ThemeBannerItem.BANNER_FLAG_PLANEVENT:
			
			ThemeShopActivityOpenApi.startCompaignActivity(context, dataItem.bannerLinkUrl);
			break;
			
		case ThemeBannerItem.BANNER_FLAG_THEME_TAG:
			
			ThemeShopActivityOpenApi.startThemeTag(context, dataItem.bannerName, dataItem.bannerId);
			break;
			
		case ThemeBannerItem.BANNER_FLAG_THEME_SINGLE:
			
			ThemeShopActivityOpenApi.startThemePreview(context, dataItem.bannerId+"", NetOptApi.PLACEID_TAG);
			break;
			
		case ThemeBannerItem.BANNER_FLAG_THEME_TOPIC:
			
			ThemeShopActivityOpenApi.startThemeWebTag(context, dataItem.bannerLinkUrl, dataItem.bannerName);
			break;
		
		case ThemeBannerItem.BANNER_FLAG_THEME_AD:
			NetOptApi.handleUriEx(dataItem.bannerLinkUrl, false);
			NetOptApi.submitAdReport(context, dataItem.bannerId, AdvertItem.AD_REPORT_RESTYPE_AD,  
					AdvertItem.AD_POSTION_WEATHER_BANNER, AdvertItem.AD_REPORT_STATTYPE_CLICK);
			break;
		}
		HiAnalytics.submitEvent(context, 61101401, "weather_ban1");
		ThemeDownloadPathAnalysis.setThemeDownloadPathAnalysisSP(ThemeDownloadPathAnalysis.SP_TOP_BANNER);
	}

}
