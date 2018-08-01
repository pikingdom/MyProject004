package com.nd.hilauncherdev.webconnect.downloadmanage.activity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonApplicationWeakReferences;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadManagerActivity.Tab;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.IFileTypeHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.state.IDownloadStateHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.state.StateHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

public class DownloadAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<BaseDownloadInfo> mData = new ArrayList<BaseDownloadInfo>();
	private DownloadCommonView mCommonView;
	private Context mContext;
	private boolean mUseDefIcon = false;

	public DownloadAdapter(DownloadCommonView commonView) {
		mCommonView = commonView;
		mContext = commonView.getContext();
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(BaseDownloadInfo info) {
		mData.remove(info);
	}

	/**
	 * 设置数据
	 */
	public void setData(ArrayList<BaseDownloadInfo> data) {
		mData.clear();
		if (data != null && data.size() > 0) {
			mData.addAll(data);
		}
	}

	void performActionIfNeed(View view, int position) {
		View funLayout = view.findViewById(R.id.app_item_fun_layout);
		if (view == null || position >= mData.size()) {
			return;
		}

		BaseDownloadInfo info = mData.get(position);
		if (info.getState() == DownloadState.STATE_FINISHED
			|| info.getState() == DownloadState.STATE_INSTALLED) {
			funLayout.performClick();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.downloadmanager_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.app_item_image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.app_item_name);
			viewHolder.desc = (TextView) convertView.findViewById(R.id.app_item_progress_desc);
			viewHolder.state = (TextView) convertView.findViewById(R.id.app_item_state);
			viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.download_progress);
			viewHolder.funBtn = (TextView) convertView.findViewById(R.id.app_item_fun_btn);
			viewHolder.funBtnImage = (ImageView) convertView.findViewById(R.id.app_item_fun_btn_image);
			viewHolder.layoutAction = convertView.findViewById(R.id.layout_action);
			viewHolder.layoutSelect = convertView.findViewById(R.id.layout_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		DownloadManagerActivity activity = mCommonView.mActivity;
		if (activity == null) {
			return convertView;
		}

		BaseDownloadInfo downloadInfo = mData.get(position);
		activity.setDownloadState(downloadInfo);
		FileType fileType = FileType.fromId(downloadInfo.getFileType());
		Bitmap icon = getIcon(activity, downloadInfo);
		if (icon != null) {
			viewHolder.icon.setImageBitmap(icon);
		}
		viewHolder.title.setText(getTypePrefix(fileType) + downloadInfo.getTitle());
		IDownloadStateHelper stateHelper = StateHelper.fromState(downloadInfo.getState()).getHelper();
		if (stateHelper != null) {
			stateHelper.initView(viewHolder, downloadInfo);
		}

		if (needModifyIcon(fileType)
			|| (downloadInfo.getFileType() == FileType.FILE_APK.getId() && !mUseDefIcon)) {
			viewHolder.icon.setScaleType(ScaleType.FIT_XY);
		} else {
			viewHolder.icon.setScaleType(ScaleType.CENTER_INSIDE);
		}

		((DownloadItemView) convertView).onGetView(downloadInfo);

		View funLayout = convertView.findViewById(R.id.app_item_fun_layout);
		funLayout.setTag(R.id.downloadmanager_adapter_tag_1, viewHolder);
		funLayout.setTag(R.id.downloadmanager_adapter_tag_2, downloadInfo);
		funLayout.setTag(R.id.downloadmanager_adapter_tag_3, convertView);
		funLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ViewHolder holder = (ViewHolder) v.getTag(R.id.downloadmanager_adapter_tag_1);
					BaseDownloadInfo info = (BaseDownloadInfo) v.getTag(R.id.downloadmanager_adapter_tag_2);
					DownloadItemView itemView = (DownloadItemView) v.getTag(R.id.downloadmanager_adapter_tag_3);
					if (info.getState() == DownloadState.STATE_DOWNLOADING) {
						itemView.isClicked = true;
					}

					IDownloadStateHelper stateHelper = StateHelper.fromState(info.getState()).getHelper();
					if (stateHelper != null) {
						stateHelper.action(mContext, holder, info);
					}
					itemView.onAction();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		if (mCommonView.mInEditState) {
			viewHolder.layoutAction.setVisibility(View.GONE);
			viewHolder.layoutSelect.setVisibility(View.VISIBLE);
		} else {
			viewHolder.layoutAction.setVisibility(View.VISIBLE);
			viewHolder.layoutSelect.setVisibility(View.GONE);
		}

		viewHolder.mCkSelect = (CheckBox) convertView.findViewById(R.id.ck_select);
		viewHolder.mCkSelect.setTag(downloadInfo);
		viewHolder.mCkSelect.setChecked(downloadInfo.mIsSelected);
		viewHolder.mCkSelect.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BaseDownloadInfo info = (BaseDownloadInfo) buttonView.getTag();
				if (info != null) {
					info.mIsSelected = isChecked;
					mCommonView.onSelectChanged();
				}
			}
		});

		return convertView;
	}

	private boolean needModifyIcon(FileType fileType) {
		return (fileType == FileType.FILE_ICON
				 || fileType == FileType.FILE_INPUT
				 || fileType == FileType.FILE_LOCK
				 || fileType == FileType.FILE_SMS
				 || fileType == FileType.FILE_THEME
				 || fileType == FileType.FILE_WEATHER);
	}

	private String getTypePrefix(FileType fileType) {
		int resId = -1;
		Tab tab = DownloadManagerActivity.mapFileTypeToTab(fileType);
		if (tab != null) {
			resId = tab.mTitleId;
		}
		if (resId > 0) {
			return mContext.getResources().getString(resId) + "：";
		}

		return "";
	}

	private Bitmap getIcon(DownloadManagerActivity activity, BaseDownloadInfo downloadInfo) {
		Bitmap icon = null;
		mUseDefIcon = false;
		FileType fileType = FileType.fromId(downloadInfo.getFileType());

		if (downloadInfo.getIconPath() != null) {
			icon = activity.getIcon(fileType, downloadInfo.getIconPath());
		}
		if (icon == null) {
			mUseDefIcon = true;

			IFileTypeHelper helper = fileType.getHelper();
			if (null != helper) {
				icon = activity.getIcon(fileType, helper.getItemDefIconPath(downloadInfo));
			}
		}
		if (icon == null) {
			icon = CommonApplicationWeakReferences.getInstance().getDefAppIcon(mCommonView.getResources());
		}
		if (icon == null) {
			// 用于下载二维码扫描结果
			icon = ((BitmapDrawable) mCommonView.getResources().getDrawable(R.drawable.app_market_qrcode_scan_download_icon)).getBitmap();
		}

		return icon;
	}

	public static class ViewHolder {
		public ImageView icon;
		public TextView title;
		public TextView desc;
		public TextView state;
		public ProgressBar progress;
		public TextView funBtn;
		public ImageView funBtnImage;
		public View layoutAction;
		public View layoutSelect;
		private CheckBox mCkSelect;

		public void setFunButtonContent(int textId) {
			try {
				 String text = CommonGlobal.getApplicationContext().getResources().getString(textId);
				 setFunButtonContent(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void setFunButtonContent(String text) {
			if (funBtn == null || funBtnImage == null || text == null)
				return;

//			String strSetting = CommonGlobal.getApplicationContext().getResources().getString(R.string.common_button_set);
			String strPreview = CommonGlobal.getApplicationContext().getResources().getString(R.string.downloadmanager_preview);
			String strPause = CommonGlobal.getApplicationContext().getResources().getString(R.string.myphone_download_parse);
//			if (strSetting.equals(text)) {
//				funBtnImage.setVisibility(View.VISIBLE);
//				funBtnImage.setImageResource(R.drawable.downloadmanager_btn_setting_selector);
//				funBtn.setVisibility(View.GONE);
//				funBtn.setText("");
//			} else {
				funBtn.setVisibility(View.VISIBLE);
				funBtn.setText(text);
				funBtnImage.setVisibility(View.GONE);

				if (strPreview.equals(text)) {
					funBtn.setTextColor(0xff918fed);
				} else if (strPause.equals(text)) {
					funBtn.setTextColor(0xffee8278);
				} else {
					funBtn.setTextColor(0xff454545);
				}
//			}
		}
	}
}
