/**   
*    
* @file 【时间范围设置】
* @brief
*
* @<b>文件名</b>      : TimePopupWindow
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-2-15 上午11:03:09 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.Ctrl;

import thirdParty.WheelView.NumericWheelAdapter;
import thirdParty.WheelView.OnWheelScrollListener;
import thirdParty.WheelView.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.nd.weather.widget.R;

public class TimePopupWindow extends PopupWindow
{
	private View		mContentView = null;
	private Context		mContext;
	private WheelView	mWheelStartHours;
	private WheelView	mWheelStartMins;
	private WheelView	mWheelEndHours;
	private WheelView	mWheelEndMins;
	
	private int			mStartHour;
	private int			mStartMins;
	private int			mEndHour;
	private int			mEndMins;
	
	private OnClickListener mOnClickListener;
	
	
	public TimePopupWindow(View contentView, int width, int height, boolean focusable)
	{
		super(contentView, width, height, focusable);
		mContentView = contentView;
		mContext = mContentView.getContext();
		setFocusable(true);
		SetCtrl();
	}
	
	public TimePopupWindow(Context context, boolean focusable)
	{
		this(((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.weather_time_select_pop, null, false),
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, focusable);
	}

	void SetCtrl()
	{
		mWheelStartHours = (WheelView) mContentView.findViewById(R.id.time_select_ww_start_hour);
		mWheelStartMins = (WheelView) mContentView.findViewById(R.id.time_select_ww_start_minute);
		mWheelEndHours = (WheelView) mContentView.findViewById(R.id.time_select_ww_end_hour);
		mWheelEndMins = (WheelView) mContentView.findViewById(R.id.time_select_ww_end_minute);

		mContentView.findViewById(R.id.pop_select_btn_ok).setOnClickListener(clickListener);
		mContentView.findViewById(R.id.pop_select_btn_cancel).setOnClickListener(clickListener);
		
		initHour(mWheelStartHours);
		initMin(mWheelStartMins);
		initHour(mWheelEndHours);
		initMin(mWheelEndMins);
	}
	
	public void setStartTime(int hours, int mins) {
		mStartHour = hours;
		mStartMins = mins;		
		
		mWheelStartHours.setCurrentItem(hours);
		mWheelStartMins.setCurrentItem(mins);
	}
	
	public void setEndTime(int hours, int mins) {
		mEndHour = hours;
		mEndMins = mins;

		mWheelEndHours.setCurrentItem(hours);
		mWheelEndMins.setCurrentItem(mins);
	}
	
	public int getStartHour() {
		return mStartHour;
	}

	public int getStartMins() {
		return mStartMins;
	}

	public int getEndHour() {
		return mEndHour;
	}

	public int getEndMins() {
		return mEndMins;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	void initHour(WheelView wheelView) {
		initWheel(wheelView, 0, 23, false);
	}
	
	void initMin(WheelView wheelView) {
		initWheel(wheelView, 0, 59, true);
	}
	
	NumericWheelAdapter initWheel(WheelView wheelView, int iStart, int iEnd, boolean bCyclic) {
		NumericWheelAdapter wheelAdapter = new NumericWheelAdapter(mContext, iStart, iEnd, "%02d");
		wheelAdapter.setTextColor(0xff515151);
		wheelView.setViewAdapter(wheelAdapter);
		wheelView.setCyclic(bCyclic);
		wheelView.setVisibleItems(3);
		wheelView.addScrollingListener(scrollListener);
		wheelView.setShadowsScale(0.0f);
		
		return wheelAdapter;
	}

	////////////////////////////////////////////////////////////////////////////////
	
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			mStartHour = mWheelStartHours.getCurrentItem();
			mStartMins = mWheelStartMins.getCurrentItem();
			mEndHour = mWheelEndHours.getCurrentItem();
			mEndMins = mWheelEndMins.getCurrentItem();
			dismiss();
			
			if (mOnClickListener != null) {
				mOnClickListener.onClick(view);
			}
		}
	};
	
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
		}
		
		public void onScrollingFinished(WheelView wheel) {
			
			int iEndHour = mWheelEndHours.getCurrentItem();
			int id = wheel.getId();
			
			if (id == R.id.time_select_ww_start_hour ||
				id == R.id.time_select_ww_end_hour) {
				if (mWheelStartHours.getCurrentItem() > iEndHour) {
					mWheelStartHours.setCurrentItem(iEndHour, true);
				}
			}
			
			if (id == R.id.time_select_ww_start_minute ||
				id == R.id.time_select_ww_end_minute) {
				if (iEndHour == mWheelStartHours.getCurrentItem()) {
					
					int iEndMin = mWheelEndMins.getCurrentItem();
					
					if (mWheelStartMins.getCurrentItem() > iEndMin) {
						mWheelStartMins.setCurrentItem(iEndMin, true);
					}
				}
			}
			
//			switch (id) {
//			case R.id.time_select_ww_start_hour:
//			case R.id.time_select_ww_end_hour:
//				
//				if (mWheelStartHours.getCurrentItem() > iEndHour) {
//					mWheelStartHours.setCurrentItem(iEndHour, true);
//				}
//				//break;
//			
//			case R.id.time_select_ww_start_minute:
//			case R.id.time_select_ww_end_minute:
//				
//				if (iEndHour == mWheelStartHours.getCurrentItem()) {
//					
//					int iEndMin = mWheelEndMins.getCurrentItem();
//					
//					if (mWheelStartMins.getCurrentItem() > iEndMin) {
//						mWheelStartMins.setCurrentItem(iEndMin, true);
//					}
//				}
//
//				break;
//			default:
//				break;
//			}
		}
	};
}
