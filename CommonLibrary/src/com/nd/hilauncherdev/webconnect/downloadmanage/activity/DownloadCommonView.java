package com.nd.hilauncherdev.webconnect.downloadmanage.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadManagerActivity.Tab;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.view.SwipeMenu;
import com.nd.hilauncherdev.webconnect.downloadmanage.view.SwipeMenuCreator;
import com.nd.hilauncherdev.webconnect.downloadmanage.view.SwipeMenuItem;
import com.nd.hilauncherdev.webconnect.downloadmanage.view.SwipeMenuListView;

public class DownloadCommonView extends LinearLayout implements OnItemClickListener , View.OnClickListener {

	// 是否处于编辑状态
	boolean mInEditState = false;
	private View mLayoutMain;
	private View mLayoutNoData;
	private View mLayoutTips;
	private View mLayoutFooter;
	private View mLayoutFooterDelete;
	private View mLayoutFooterBatch;
	private TextView mBtnFooterBatchDelete;
	private TextView mBtnFooterBatchDownload;
	private TextView mBtnFooterDelete;
	private TextView mBtnFooterCancel;
	private SwipeMenuListView mListView;
	private View mBtnBack;
	private View mBtnTipsClose;
	private TextView mTvTitle;
	private TextView mTvSelAll;
	private Button mBtnToStore;
	private View mLayoutToStoreForAll;
	private View mBtnToStoreForAllLeft;
	private View mBtnToStoreForAllRight;
	private View mLayoutTitle;
	private DownloadAdapter mListAdapter;
	private ArrayList<BaseDownloadInfo> mData;
	private Tab mTab = null;
	DownloadManagerActivity mActivity;
	private boolean mFinishWhenBack = false;
	private boolean mNeedRedownload = false;
	private boolean mHasCloseTip = false;

	public DownloadCommonView(Context context) {
		super(context, null);
	}

	public DownloadCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof DownloadManagerActivity) {
			mActivity = (DownloadManagerActivity) context;
		}
		initView();
	}

	/**
	 * @param byhand 点击后退键关闭
	 */
	void close(boolean byhand) {
		if (byhand && mFinishWhenBack && mActivity != null) {
			mActivity.finish();
			return;
		}

		exitEditState();
		setVisibility(View.GONE);

		if (mData != null) {
			mData.clear();
			mData = null;
			mListAdapter.setData(null);
			mListAdapter.notifyDataSetChanged();
		}

		if (mTab != null && mActivity != null) {
			mActivity.onCategoryViewClose();
		}
	}

	Tab getCategory() {
		return mTab;
	}

	void setFinishWhenBack() {
		mFinishWhenBack = true;
	}

	void setNeedRedownload() {
		mNeedRedownload = true;
	}

/*	void showTips() {
		mLayoutTips.setVisibility(View.VISIBLE);
	}*/

	/**
	 * 刷新界面
	 */
	void refreshListView() {
		if (mData == null || mData.size() <= 0) {
			return;
		}
		mListAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 */
	void refreshListViewData(ArrayList<BaseDownloadInfo> data, boolean invalidate) {
		refreshListViewData(mTab, data, invalidate);
	}

	/**
	 * 刷新数据
	 */
	void refreshListViewData(Tab tab, ArrayList<BaseDownloadInfo> data, boolean invalidate) {
		if (tab != null) {
			mTab = tab;
			mTvTitle.setText(getResources().getString(tab.mTitleId));
		}

		mData = data;
		if (mData == null || mData.size() <= 0) {
			showNoData(true);
		} else {
			showNoData(false);

			if (mActivity != null && mActivity.mInSDLose) {
				mNeedRedownload = checkNeedRedownload(mData);
				if (mNeedRedownload && mTab == null && !mHasCloseTip) {
					mLayoutTips.setVisibility(View.VISIBLE);
				} else {
					mLayoutTips.setVisibility(View.GONE);
				}
				refreshFooterLayout();
			}

			mListAdapter.setData(mData);
			if (invalidate) {
				mListAdapter.notifyDataSetInvalidated();
			} else {
				mListAdapter.notifyDataSetChanged();
			}
		}
	}

	private boolean checkNeedRedownload(ArrayList<BaseDownloadInfo> data) {
		if (mData == null || mData.size() <= 0) {
			return false;
		}

		boolean needRedownload = false;
		for (BaseDownloadInfo info : data) {
			if (info != null && info.mNeedRedownload) {
				needRedownload = true;
				break;
			}
		}
		return needRedownload;
	}

	private void initView() {
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(0xffeeeeee);
		setOnClickListener(this);
		LayoutInflater.from(getContext()).inflate(R.layout.downloadmanager_common_view, this, true);
		mLayoutMain = findViewById(R.id.layout_main);
		mLayoutMain.setOnClickListener(this);
		mLayoutNoData = findViewById(R.id.layout_no_data);
		mLayoutNoData.setOnClickListener(this);
		ImageView ivNodata = (ImageView) mLayoutNoData.findViewById(R.id.iv_nodata);
		if (mActivity != null && mActivity.getNoDataImageRes() > 0) {
			ivNodata.setImageResource(mActivity.getNoDataImageRes());
		}

		mLayoutTitle = findViewById(R.id.title_layout);
		mLayoutTips = findViewById(R.id.layout_tips);
		mBtnTipsClose = findViewById(R.id.btn_tips_close);
		mBtnTipsClose.setOnClickListener(this);

		//底部操作区
		mLayoutFooter = findViewById(R.id.layout_footer);
		mLayoutFooterBatch = mLayoutFooter.findViewById(R.id.layout_footer_batch);
		//	批量删除
		mBtnFooterBatchDelete = (TextView) findViewById(R.id.btn_batch_delete);
		mBtnFooterBatchDelete.setOnClickListener(this);
		//	批量下载
		mBtnFooterBatchDownload = (TextView) findViewById(R.id.btn_batch_download);
		mBtnFooterBatchDownload.setOnClickListener(this);
		mLayoutFooterDelete = mLayoutFooter.findViewById(R.id.layout_footer_delete);
		//	删除
		mBtnFooterDelete = (TextView) findViewById(R.id.btn_footer_delete);
		mBtnFooterDelete.setOnClickListener(this);
		// 取消
		mBtnFooterCancel = (TextView) findViewById(R.id.btn_footer_cancle);
		mBtnFooterCancel.setOnClickListener(this);
		refreshFooterLayout();

		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		initListView(mListView);
		mListAdapter = new DownloadAdapter(this);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);

		mBtnToStore = (Button) findViewById(R.id.btn_to_store);
		mBtnToStore.setOnClickListener(this);
		mLayoutToStoreForAll = findViewById(R.id.layout_to_store_for_all);
		mBtnToStoreForAllLeft = findViewById(R.id.btn_to_store_for_all_left);
		mBtnToStoreForAllLeft.setOnClickListener(this);
		mBtnToStoreForAllRight = findViewById(R.id.btn_to_store_for_all_right);
		mBtnToStoreForAllRight.setOnClickListener(this);

		mBtnBack = findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvSelAll = (TextView) findViewById(R.id.tv_sel_all);
		mTvSelAll.setOnClickListener(this);
	}

	private void initListView(SwipeMenuListView listView) {
		try {
			Method method = listView.getClass().getMethod("setOverScrollMode", int.class);
			method.invoke(listView, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
				deleteItem.setBackground(R.drawable.downloadmanager_item_delete_bg);
				deleteItem.setWidth(ScreenUtil.dip2px(getContext(), 80));
				deleteItem.setIcon(R.drawable.downloadmanager_item_delete);
				menu.addMenuItem(deleteItem);
			}
		};
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (mData == null || position >= mData.size()) {
					return false;
				}
				deleteSingle(mData.get(position));
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			close(true);
		} else if (v == mTvSelAll) {
			Resources resources = getResources();
			if (mTvSelAll.getText().toString().equals(resources.getString(R.string.downloadmanager_sel_all))) {
				//全选
				selectAll();
			} else {
				//取消全选
				deselectAll();
			}
		} else if (v == mBtnToStore) {
			if (mActivity != null) {
				mActivity.onNoDataButtonClicked(mTab);
			}
		} else if (v == mBtnFooterBatchDelete) {
			enterEditState();
		} else if (v == mBtnFooterDelete) {
			deleteBatch();
		} else if (v == mBtnFooterCancel) {
			exitEditState();
		} else if (v == mBtnToStoreForAllLeft) {
			if (mActivity != null) {
				mActivity.onNoDataButtonClicked(Tab.TAB_THEME);
			}
		} else if (v == mBtnToStoreForAllRight) {
			if (mActivity != null) {
				mActivity.onNoDataButtonClicked(Tab.TAB_APK);
			}
		} else if (v == mBtnTipsClose) {
			mHasCloseTip = true;
			mLayoutTips.setVisibility(View.GONE);
		} else if (v == mBtnFooterBatchDownload) {
			if (mTab == null) {
				mHasCloseTip = true;
				mLayoutTips.setVisibility(View.GONE);
			}
			mNeedRedownload = false;
			refreshFooterLayout();

			if (mData == null) {
				return;
			}
			for (BaseDownloadInfo info : mData) {
				if (info != null && info.mNeedRedownload) {
					info.mNeedRedownload = false;
					DownloadManager.getInstance().addNormalTask(info, null);
				}
			}
		}
	}

	private void showNoData(boolean show) {
		if (show) {
			mLayoutNoData.setVisibility(View.VISIBLE);
			exitEditState();
			mLayoutMain.setVisibility(View.GONE);
			if (mTab == null) {
				mLayoutToStoreForAll.setVisibility(View.VISIBLE);
				mBtnToStore.setVisibility(View.GONE);
			} else {
				mLayoutToStoreForAll.setVisibility(View.GONE);
				if (mTab == Tab.TAB_APK) {
//					mBtnToStore.setText(R.string.downloadmanager_prompt_to_store_3);
				} else {
					mBtnToStore.setVisibility(View.VISIBLE);
					mBtnToStore.setText(R.string.downloadmanager_prompt_to_store);
				}
			}
		} else {
			mLayoutNoData.setVisibility(View.GONE);
			mLayoutMain.setVisibility(View.VISIBLE);
		}
	}

	private void refreshFooterLayout() {
		if (mInEditState) {
			mLayoutFooterBatch.setVisibility(View.GONE);
			mLayoutFooterDelete.setVisibility(View.VISIBLE);
		} else {
			mLayoutFooterBatch.setVisibility(View.VISIBLE);
			mLayoutFooterDelete.setVisibility(View.GONE);
		}

		View batchDownloadMarginView = findViewById(R.id.btn_batch_download_margin);
		if (mNeedRedownload) {
			mBtnFooterBatchDownload.setVisibility(View.VISIBLE);
			batchDownloadMarginView.setVisibility(View.VISIBLE);
		} else {
			mBtnFooterBatchDownload.setVisibility(View.GONE);
			batchDownloadMarginView.setVisibility(View.GONE);
		}
	}

	/**
	 * 进入批量删除状态
	 */
	private void enterEditState() {
		if (mInEditState) {
			return;
		}

		mInEditState = true;
		refreshFooterLayout();
		mTvTitle.setText(getResources().getString(R.string.downloadmanager_batch_delete));
		mTvSelAll.setVisibility(View.VISIBLE);
		mTvSelAll.setText(getResources().getString(R.string.downloadmanager_sel_all));
		mBtnBack.setVisibility(View.GONE);
		mListAdapter.notifyDataSetChanged();
		if (mActivity != null) {
			mActivity.setTitleLayoutVisibility(View.GONE);
		}
		mListView.setAllowOpenMenu(false);
	}

	/**
	 * 退出批量删除状态
	 */
	void exitEditState() {
		if (!mInEditState) {
			return;
		}

		mInEditState = false;
		refreshFooterLayout();
		mTvSelAll.setVisibility(View.GONE);
		mBtnBack.setVisibility(View.VISIBLE);
		mListAdapter.notifyDataSetChanged();
		if (mActivity != null) {
			mActivity.setTitleLayoutVisibility(View.VISIBLE);
		}
		mListView.setAllowOpenMenu(true);

		deselectAll();
	}

	/**
	 * 全选
	 */
	private void selectAll() {
		if (mData == null || mData.size() <= 0) {
			return;
		}

		for (BaseDownloadInfo info : mData) {
			info.mIsSelected = true;
		}
		onSelectChanged();
		mListAdapter.notifyDataSetChanged();
	}

	/**
	 * 退出全选
	 */
	private void deselectAll() {
		if (mData == null || mData.size() <= 0) {
			return;
		}

		for (BaseDownloadInfo info : mData) {
			info.mIsSelected = false;
		}
		onSelectChanged();
		mListAdapter.notifyDataSetChanged();
	}

	/**
	 * 选择状态改变，更新头部文字及底部删除按钮文字
	 */
	void onSelectChanged() {
		int selectedCount = 0;
		if (mData != null && mData.size() > 0) {
			for (BaseDownloadInfo info : mData) {
				if (info.mIsSelected) {
					selectedCount++;
				}
			}
		}

		Resources resources = getResources();
		if (selectedCount != 0 && selectedCount == mData.size()) {
			mTvSelAll.setText(resources.getString(R.string.downloadmanager_cancel_sel_all));
		} else {
			mTvSelAll.setText(resources.getString(R.string.downloadmanager_sel_all));
		}
		mLayoutTitle.requestLayout();

		String appendText = selectedCount > 0 ? ("(" + selectedCount + ")") : "";
		mBtnFooterDelete.setText(resources.getString(R.string.common_button_delete) + appendText);
		mBtnFooterDelete.setEnabled(selectedCount > 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mInEditState) {
			View v = view.findViewById(R.id.ck_select);
			if (v != null) {
				CheckBox ckSelect = (CheckBox) v;
				ckSelect.setChecked(!ckSelect.isChecked());
			}
		} else {
			mListAdapter.performActionIfNeed(view, position);
		}
	}

	private void deleteSingle(BaseDownloadInfo info) {
		if (mActivity != null) {
			mActivity.deleteSingle(info);
		}
	}

	private void deleteBatch() {
		if (mActivity != null) {
			mActivity.deleteBatch();
		}
	}
}
