package com.nd.weather.widget.UI.banner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.framework.view.MyPhoneLazyViewPager;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonLightbar;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.CommonSlidingViewData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.banner.NestedSlidingView.OnSnapToScreenListener;

/**
 * 广告轮播View
 */
public class BannerView extends LinearLayout implements OnSnapToScreenListener{

	public final static String TAG = "BannerView";
	
	private Context ctx;
	
	private float mLastMotionX, mLastMotionY;
	
	private RelativeLayout presonal_compaign_mainview_header;
	
	private CompaignSlidingView slidingView;
	
	private CommonLightbar lightBar;
	
	private CommonSlidingViewData slidingViewData;
	
	private ArrayList<ICommonDataItem> dataItemOfHeaderList = new ArrayList<ICommonDataItem>();
	
	/**轮播方向设置*/
	private boolean bSetGoDirection = false;
	/**自动轮播动*/
	private boolean isScrool = true;
	/**播动方向*/
	private boolean isRight = true;
	/**定时刷新的频率*/
	private final int FIRST_DELAY_TIME = 2000;
	private Handler handler = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			try {
				handler.post(new Runnable() {
					@Override
					public void run() {
						autoScroll();
					}
				});
				handler.postDelayed(task, FIRST_DELAY_TIME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public BannerView(Context context) {
		super(context);
		ctx = context;
		
		addView(R.layout.recommend_banner_view);

		setupViews();		
	}
	
	public void addView(int paramInt) {
		LayoutInflater.from( ctx ).inflate(paramInt, this);
	}
	
	public void setSlidingParent(MyPhoneLazyViewPager viewPager){
		slidingView.setSlidingParent(viewPager);
	}
	
	private void setupViews() {
		presonal_compaign_mainview_header = (RelativeLayout) findViewById(R.id.presonal_compaign_mainview_header);
		ViewGroup.LayoutParams layoutParams = presonal_compaign_mainview_header.getLayoutParams();
        layoutParams.height = ScreenUtil.dip2px(ctx, 115);
        layoutParams.width = ScreenUtil.getCurrentScreenWidth(ctx);
        presonal_compaign_mainview_header.setLayoutParams(layoutParams);
		slidingView = (CompaignSlidingView)findViewById(R.id.presonal_compaign_mainview_header_slidingview);
		lightBar = (CommonLightbar)findViewById(R.id.lightbar);
		lightBar.setNormalLighter(this.getResources().getDrawable(R.drawable.launcher_menu_presonal_compaign_rightbar_default));
		lightBar.setSelectedLighter(this.getResources().getDrawable(R.drawable.launcher_menu_presonal_compaign_rightbar_check));
		
		initHeaderSlidingView();
	}
	
	private void initHeaderSlidingView() {
		List<ICommonData> list = new ArrayList<ICommonData>();
		int sw = ScreenUtil.getCurrentScreenWidth(ctx);
		int sh = ScreenUtil.getCurrentScreenHeight(ctx);
		slidingViewData = new CommonSlidingViewData(sw,sh,1,1,new ArrayList<ICommonDataItem>());
		list.add(slidingViewData);
		slidingView.setList(list);
		slidingView.setOnItemClickListener(slidingView);
		slidingView.setCommonLightbar(lightBar);
		slidingView.setOnSnapToScreenListener(this);
	}
	
	/**
	 * 设置主题活动数据
	 * @param dataItemList
	 */
	public void setBannerData(ArrayList<ICommonDataItem> dataItemList){
		if (dataItemList==null || dataItemList.size()==0){
			presonal_compaign_mainview_header.setVisibility(View.GONE);
			return;
		}
		if (dataItemList!=null && dataItemList.size()>0) {
			presonal_compaign_mainview_header.setVisibility(View.VISIBLE);
			dataItemOfHeaderList.clear();
			dataItemOfHeaderList.addAll(dataItemList);
			slidingViewData.setDataList(dataItemOfHeaderList);
			slidingView.clearLayout();
			slidingView.reLayout();
		}
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			break;
			
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			final int yDiff = (int) Math.abs(mLastMotionY - y);
			if ( xDiff > yDiff ) {
				bSetGoDirection = true;
				stopScroll();
			}
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			startScroll();
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	private void autoScroll(){
		if (!isScrool) {
			return;
		}
		if (isRight){
			slidingView.scrollRight();
		}else{
			slidingView.scrollLeft();
		}
	}

	public void setScroolState(boolean bScrool){
		isScrool = bScrool;
	}
	
	public void stopScroll(){
		if (isScrool) {
			handler.removeCallbacks(task);
		}
	}

	public void startScroll(){
		if (isScrool) {
			handler.removeCallbacks(task);
			handler.postDelayed(task, FIRST_DELAY_TIME);
		}
	}
	
	@Override
	public void onSnapToScreen(List<ICommonData> list, int fromScreen,
			int toScreen) {
		if (!bSetGoDirection) {
			return; 
		}
		isRight = toScreen-fromScreen>=0;
		bSetGoDirection = false;
	}
}
