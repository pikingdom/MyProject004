package com.nd.hilauncherdev.webconnect.downloadmanage.activity;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadManagerActivity.Tab;

public class DownloadCategoryMenu extends LinearLayout implements View.OnClickListener {

	private static final int COLUMN = 5;
	private static final int SEP_COLOR = 0xffdfdfdf;
	private static final int TEXT_COLOR = 0xff7a7a7a;
	private static final int TEXT_SIZE = 15;
	private int mIconPadding = 0;
	private int mRowTopPadding  = 0;
	private int mRowBottomPadding = 0;
	private int mSepSize = 0;
	private int mSepMargin = 0;
	private Tab[] mTabs;
	private DownloadManagerActivity mActivity;
	
	public DownloadCategoryMenu(Context context) {
		this(context, null);
	}

	public DownloadCategoryMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if (context instanceof DownloadManagerActivity) {
			mActivity = (DownloadManagerActivity) context;
		}
		
		mIconPadding = ScreenUtil.dip2px(context, 7);
		mRowTopPadding = ScreenUtil.dip2px(context, 15);
		mRowBottomPadding = ScreenUtil.dip2px(context, 10);
		mSepSize = ScreenUtil.dip2px(context, 1);
		mSepMargin = ScreenUtil.dip2px(context, 10);
		
		initView(context);
	}
	
	private void initView(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(0xffffffff);
	}

	public void setItems(Tab[] tabs) {
		if (mTabs != null || tabs == null || tabs.length <= 0) {
			return;
		}
		removeAllViews();
		
		Context context = getContext();
		Resources resources = getResources();
		LinearLayout rowLayout = null;
		for (int i=0; i<tabs.length; i++) {
			if (i % COLUMN == 0) {
				rowLayout = addRowLayout(context, (i != 0));
			}
			if (rowLayout == null) {
				break;
			}
			
			addItemToRowLayout(context, resources, rowLayout, tabs[i]);
		}
	}
	
	private LinearLayout addRowLayout(Context context, boolean needSep) {
		LayoutParams lp = null;
		
		if (needSep) {
			//横向分割线
			View sep = new View(context);
			sep.setBackgroundColor(SEP_COLOR);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, mSepSize);
			lp.leftMargin = mSepMargin;
			lp.rightMargin = mSepMargin;
			addView(sep, lp);
		}
		
		LinearLayout rowLayout = new LinearLayout(context);
		rowLayout.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(rowLayout, lp);
		
		return rowLayout;
	}
	
	private void addItemToRowLayout(Context context, Resources resources, LinearLayout rowLayout, Tab tab) {
		LayoutParams lp = null;
		
		//添加item
		LinearLayout container = new LinearLayout(context);
		container.setTag(tab);
		container.setOnClickListener(this);
		container.setPadding(0, mRowTopPadding, 0, mRowBottomPadding);
		container.setGravity(Gravity.CENTER);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		rowLayout.addView(container, lp);
		
		TextView tv = new TextView(context);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv.setText(resources.getString(tab.mTitleId));
		tv.setTextColor(TEXT_COLOR);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
		tv.setCompoundDrawablesWithIntrinsicBounds(0, tab.mIconId, 0, 0);
		tv.setCompoundDrawablePadding(mIconPadding);
		tv.setGravity(Gravity.CENTER);
		container.addView(tv, lp);
		
		//纵向分割线
		View sep = new View(context);
		sep.setBackgroundColor(SEP_COLOR);
		lp = new LayoutParams(mSepSize, LayoutParams.MATCH_PARENT);
		lp.topMargin = mRowTopPadding;
		lp.bottomMargin = mRowBottomPadding;
		rowLayout.addView(sep, lp);
	}
	
	@Override
	public void onClick(View v) {
		if (mActivity != null) {
			mActivity.showCategoryView((Tab) v.getTag());
		}
	}
}
