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

import thirdParty.WheelView.ArrayWheelAdapter;
import thirdParty.WheelView.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.nd.weather.widget.R;

public class SinglePopupWindow<T> extends PopupWindow
{
	private View mContentView = null;
	private Context mContext;
	private WheelView mWheel;

	public SinglePopupWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
		mContentView = contentView;
		mContext = mContentView.getContext();
		setFocusable(true);
		SetCtrl();
	}

	public SinglePopupWindow(Context context, boolean focusable) {
		this(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.weather_single_select_pop, null, false), LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, focusable);
	}

	void SetCtrl() {
		mWheel = (WheelView) mContentView.findViewById(R.id.single_select_ww_list);
	}

	public void setArrayData(T[] arrayData) {
		if (mWheel != null) {
			ArrayWheelAdapter<T> arrayAdapter = new ArrayWheelAdapter<T>(mContext, arrayData);
			arrayAdapter.setTextColor(0xff515151);
			mWheel.setViewAdapter(arrayAdapter);
			mWheel.setVisibleItems(5);
			mWheel.setShadowsScale(0.0f);
		}
	}

	public void setWheelCyclic(boolean bCyclic) {
		if (mWheel != null) {
			mWheel.setCyclic(bCyclic);
		}
	}

	public void setWheelItemCount(int nCount) {
		if (mWheel != null) {
			mWheel.setVisibleItems(nCount);
		}
	}

	public void setCurrentItem(int iIndex) {
		if (mWheel != null) {
			mWheel.setCurrentItem(iIndex);
		}
	}

	public int getCurrentItem() {
		if (mWheel != null) {
			return mWheel.getCurrentItem();
		}
		return -1;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		mContentView.findViewById(R.id.pop_select_btn_ok).setOnClickListener(onClickListener);
		mContentView.findViewById(R.id.pop_select_btn_cancel).setOnClickListener(onClickListener);
	}

}
