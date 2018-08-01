package com.nd.hilauncherdev.webconnect.downloadmanage.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.IFileTypeHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

/**
 * 下载项view
 */
public class DownloadItemView extends RelativeLayout {

	static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

	private TextView mDownloadProgressDesc;
	private TextView mDownloadState;
	private TextView mActionBtn;
	private ImageView mActionBtnImage;
	private ProgressBar mProgress;
	private ViewHolder mHolder = new ViewHolder();
	private View mIvIcon;
	private Bitmap mIconNew;
	private Bitmap mIconPause;
	private BaseDownloadInfo mDownloadInfo;
	private boolean mClickedAfterComplete = false;
	private DownloadManagerActivity mActivity;

	private BroadcastReceiver mReceiver;
	public boolean isClicked = false;

	/**
	 * 静默安装监听
	 */
	private BroadcastReceiver mSilentInstallReceiver;

	private String mFormatDownload;

	public DownloadItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (context instanceof DownloadManagerActivity) {
			mActivity = (DownloadManagerActivity) context;
		}
	}

	@Override
	protected void onFinishInflate() {
		mDownloadProgressDesc = (TextView) findViewById(R.id.app_item_progress_desc);
		mDownloadState = (TextView) findViewById(R.id.app_item_state);
		mProgress = (ProgressBar) findViewById(R.id.download_progress);
		mActionBtn = (TextView) findViewById(R.id.app_item_fun_btn);
		mActionBtnImage = (ImageView) findViewById(R.id.app_item_fun_btn_image);
		mIvIcon = findViewById(R.id.app_item_image);

		mHolder.funBtn = mActionBtn;
		mHolder.funBtnImage = mActionBtnImage;

		mIconNew =  BitmapFactory.decodeResource(getResources(), R.drawable.downloadmanager_new_mask);
		mIconPause =  BitmapFactory.decodeResource(getResources(), R.drawable.downloadmanager_pause);
	}

	void onGetView(BaseDownloadInfo info) {
		mDownloadInfo = info;
		mClickedAfterComplete = info.isClickedAfterComplete();
	}

	void onAction() {
		if (!needDrawNewMask()) {
			return;
		}

		setClickedAfterComplete();
	}

	private void unRegistReceiver(BroadcastReceiver receiver) {
		try {
			if (receiver != null) {
				getContext().unregisterReceiver(receiver);
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		unRegistReceiver(mReceiver);
		unRegistReceiver(mSilentInstallReceiver);
		mReceiver = null;
		mSilentInstallReceiver = null;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		// 注册静默安装监听
		if (mSilentInstallReceiver == null) {
			mSilentInstallReceiver = new SilentInstallReceiver();
			IntentFilter silentInstallFilter = new IntentFilter(DownloadState.RECEIVER_APP_SILENT_INSTALL);
			getContext().registerReceiver(mSilentInstallReceiver, silentInstallFilter);
		}

		//final ApkDownloadInfo downloadInfo = (ApkDownloadInfo) obj;
		/*
		 * final int state = downloadInfo.getState() ; if (state ==
		 * DownloadState.STATE_INSTALLED || state ==
		 * DownloadState.STATE_FINISHED ) //已下载完成的不监听下载广播 return ;
		 */
		mFormatDownload = new StringBuffer("%s").append("/").append("%s").toString();

		if (mReceiver == null) {
			mReceiver = new UIRefreshReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(DownloadManager.ACTION_DOWNLOAD_STATE);
			getContext().registerReceiver(mReceiver, filter);
		}
	}

	/**
	 * 监听下载广播更新ui
	 */
	class UIRefreshReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			freshItemState(intent);
		}
	}

	/**
	 * 静默安装的监听器
	 *
	 * @author zhuchenghua
	 */
	private class SilentInstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (mDownloadInfo == null
				|| (mDownloadInfo.getState() != DownloadState.STATE_FINISHED && mDownloadInfo.getState() != DownloadState.INSTALL_STATE_INSTALLING))
				return;

			String packageName = intent.getStringExtra(DownloadState.EXTRA_APP_INSTALL_PACAKGE_NAME);
			if (TextUtils.isEmpty(packageName))
				return;

			try {
				if (packageName.equals(mDownloadInfo.getPacakgeName(getContext()))) {
					int installState = intent.getIntExtra(DownloadState.EXTRA_APP_INSTALL_STATE, -1);
					switch (installState) {
					// 安装失败
					case DownloadState.INSTALL_STATE_INSTALL_FAILED:
						mDownloadInfo.setState(mDownloadInfo.getFinishedUninstalled());
						mHolder.setFunButtonContent(R.string.common_button_install);
						break;

					// 正在安装
					case DownloadState.INSTALL_STATE_INSTALLING:
						mHolder.setFunButtonContent(R.string.app_market_installing);
						mDownloadInfo.setState(mDownloadInfo.getInstallingState());
						break;
					}

				}// end if

			} catch (Exception e) {
				e.printStackTrace();
			}

		}// end onReceive

	}// end SilentInstallReceiver

	/**
	 * 刷新进度，状态
	 */
	private void freshItemState(Intent intent) {
		final String identification = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
		if (TextUtils.isEmpty(identification)
			|| mDownloadInfo == null
			|| TextUtils.isEmpty(mDownloadInfo.getIdentification())
			|| !identification.equals(mDownloadInfo.getIdentification())) {
			return;
		}

		final String additionInfo = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_ADDITION);
		if (additionInfo != null) {
			mDownloadInfo.setAdditionInfo(additionInfo);
			return;
		}

		final int progress = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_PROGRESS, 0);
		final int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
		final String downloadSize = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_DOWNLOAD_SIZE);
		final String totalSize = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_TOTAL_SIZE);

		mDownloadInfo.progress = progress;
		if (downloadSize != null)
			mDownloadInfo.downloadSize = downloadSize;
		if (totalSize != null)
			mDownloadInfo.totalSize = totalSize;

		if (state != DownloadState.STATE_FAILED) {
			mProgress.setProgress(progress);
			mDownloadState.setText(progress + "%");
			mDownloadProgressDesc.setText(String.format(mFormatDownload, mDownloadInfo.downloadSize,mDownloadInfo.totalSize));
		}

		if (progress == 100 && state == DownloadState.STATE_FINISHED) {
			mDownloadInfo.setState(mDownloadInfo.getFinishedUninstalled());
			mDownloadProgressDesc.setText(R.string.download_finished);
			mProgress.setVisibility(View.GONE);
			mDownloadState.setVisibility(View.INVISIBLE);
//			mActionBtn.setText(R.string.common_button_install);

			FileType fileType = FileType.fromId(mDownloadInfo.getFileType());
			IFileTypeHelper helper = fileType.getHelper();
			mHolder.setFunButtonContent(null != helper ? helper.getItemTextWhenFinished(mDownloadInfo) : "");

			invalidate();
		}
		if (state == DownloadState.STATE_DOWNLOADING) {
			mProgress.setVisibility(View.VISIBLE);
			mDownloadState.setVisibility(View.VISIBLE);

			mHolder.setFunButtonContent(R.string.myphone_download_parse);
			mDownloadInfo.setState(mDownloadInfo.getDownloadingState());
		}
		if (state == DownloadState.STATE_PAUSE || state == DownloadState.STATE_FAILED) {
			mProgress.setVisibility(View.VISIBLE);
			mDownloadState.setVisibility(View.VISIBLE);

			mDownloadState.setText(R.string.myphone_download_parse);
			mHolder.setFunButtonContent(R.string.common_button_continue);
			mDownloadInfo.setState(mDownloadInfo.getPauseState());
		}
		if (state == DownloadState.STATE_WAITING) {
			mProgress.setVisibility(View.VISIBLE);
			mDownloadState.setVisibility(View.VISIBLE);

			mDownloadState.setText(R.string.download_waiting);
			mHolder.setFunButtonContent(R.string.myphone_download_parse);
			mDownloadInfo.setState(mDownloadInfo.getWaitingState());
		}
	}

	private boolean needDrawNewMask() {
		boolean need = (!mClickedAfterComplete
						 && mDownloadInfo != null
						 && (mDownloadInfo.getState() == DownloadState.STATE_FINISHED
						     || mDownloadInfo.getState() == DownloadState.STATE_INSTALLED));
		if (need && !isToday(mDownloadInfo.getCompleteTime())) {
			need = false;
			setClickedAfterComplete();
		}

		return need;
	}

	private boolean isToday(long time) {
		if (time > 0) {
			String date = sFormat.format(new Date(time));
			String curDate = sFormat.format(new Date(System.currentTimeMillis()));
			if (!date.equals(curDate)) {
				return false;
			}
		}

		return true;
	}

	private void setClickedAfterComplete() {
		mClickedAfterComplete = true;
		mDownloadInfo.setClickedAfterComplete(CommonGlobal.getApplicationContext());
		DownloadManager.getInstance().modifyAdditionInfo(mDownloadInfo);
		if (mActivity != null) {
			mActivity.refreshListView();
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (needDrawNewMask()) {
			canvas.drawBitmap(mIconNew,
			          (mIvIcon.getRight()-mIconNew.getWidth()/2),
			          (mIvIcon.getTop()-mIconNew.getHeight()/2),
			          null);
		}

		if (mDownloadInfo.getState() == DownloadState.STATE_PAUSE) {
			canvas.drawBitmap(mIconPause, (mIvIcon.getRight() - mIconPause.getWidth() / 2), (mIvIcon.getTop() - mIconPause.getHeight() / 2), null);
		}
	}
}
