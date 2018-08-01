package com.nd.hilauncherdev.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nd.android.pandahome2.shop.R;
import com.nd.hilauncherdev.analysis.AppAnalysisConstant;
import com.nd.hilauncherdev.analysis.AppDistributeStatistics;
import com.nd.hilauncherdev.app.WarnBean.WarnCategory;
import com.nd.hilauncherdev.app.WarnBean.WarnItem;
import com.nd.hilauncherdev.appmarket.AppMarketUtil;
import com.nd.hilauncherdev.framework.view.commonview.HeaderView;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.shop.Global;
import com.nd.hilauncherdev.shop.shop3.AsyncImageLoader;
import com.nd.hilauncherdev.shop.shop3.AsyncImageLoader.ImageCallback;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerService;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerServiceConnection;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

/**
 * 匣子搜索应用详情界面。 Title: DrawerSearchAppDetailView Description: Company: ND
 * 
 * @author MaoLinnan
 * @date 2014年12月12日
 */
public class ShopRecommendAppDetailView extends LinearLayout {
	private Context context;
	private ShopRecommendAppDetailPopupWindow appDetailPopupWindow;
	private ImageAdapter imageAdapter;
	private int state = DownloadState.STATE_NONE;// 下载状态
	private int progress = 0;// 下载进度
	private SoftDetialBean softDetialBean;// 软件详情
	private AsyncImageLoader asyncImageLoader;// 异步加载图片

	private RelativeLayout mRoot;
	private ScrollView scrollView;
	private ImageView icon;// 应用图标
	private ImageView apptype;// 类型标志
	private TextView appTitle;// 应用名
	private RatingBar ratingBar;// 评分

	private LinearLayout scanInfo;// 扫描信息
	private TextView scanIconView;// 扫描标示
	private ImageView scanMoreInfoBtn;// 扫描信息按钮
	private LinearLayout marketPower;// 安全信息列表

	private TextView appSize;// 应用大小
	private TextView downNumber;//下载次数
	private TextView appVersion;// 应用版本号
	private LinearLayout networkTipsLinearlayout;// 网络提醒布局
	private Button networkTipsBtn;// 继续浏览按钮
	private HorizontalListView appThumbnail;// 应用缩略图
	private TextView detailInfoContent;// 应用简介
	private TextView btnDownload;// 应用下载控制按钮

	public ShopRecommendAppDetailView(Context context, ShopRecommendAppDetailPopupWindow appDetailPopupWindow) {
		super(context);
		this.context = context;
		this.appDetailPopupWindow = appDetailPopupWindow;
		this.imageAdapter = new ImageAdapter();
		asyncImageLoader = new AsyncImageLoader();
		initView();
		initListener();
	}

	private void initView() {
		mRoot = (RelativeLayout) View.inflate(context, R.layout.searchbox_hotword_detail_info_view_shop_v6, null);
		HeaderView headView = (HeaderView) mRoot.findViewById(R.id.head_view);
		headView.setTitle(getResources().getString(R.string.searchbox_hotword_detail_info));
		headView.setGoBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				appDetailPopupWindow.cancelDrawerSearchAppDetailPopupWindow();
			}
		});
		
		scrollView = (ScrollView) mRoot.findViewById(R.id.drawer_search_app_detail_scrollview);
		icon = (ImageView) mRoot.findViewById(R.id.drawer_search_app_detail_icon);
		apptype = (ImageView) mRoot.findViewById(R.id.drawer_search_app_detail_apptype_imageview);
		appTitle = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_app_title);
		downNumber = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_app_downnumber);
		ratingBar = (RatingBar) mRoot.findViewById(R.id.drawer_search_app_detail_ratingBar);
		scanInfo = (LinearLayout) mRoot.findViewById(R.id.drawer_search_app_detail_scan_info);
		scanIconView = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_scan_icon_view);
		scanMoreInfoBtn = (ImageView) mRoot.findViewById(R.id.drawer_search_app_detail_scan_more_info_btn);
		marketPower = (LinearLayout) mRoot.findViewById(R.id.rawer_search_app_detail_market_power);
		appSize = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_app_size);
		appVersion = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_app_version);
		networkTipsLinearlayout = (LinearLayout) mRoot.findViewById(R.id.drawer_search_app_detail_network_tips_linearlayout);
		networkTipsBtn = (Button) mRoot.findViewById(R.id.drawer_search_app_detail_network_tips_btn);
		appThumbnail = (HorizontalListView) mRoot.findViewById(R.id.drawer_search_app_detail_app_thumbnail);
		detailInfoContent = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_detail_info_content);
		btnDownload = (TextView) mRoot.findViewById(R.id.drawer_search_app_detail_btnDownload);
		appThumbnail.setAdapter(imageAdapter);
		this.addView(mRoot, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void initListener() {
		// 扫描信息
		scanInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (marketPower.getVisibility() == View.GONE) {// 要显示
					marketPower.setVisibility(View.VISIBLE);
					scanMoreInfoBtn.setBackgroundResource(R.drawable.app_market_detail_security_hide);
				} else {// 要隐藏
					marketPower.setVisibility(View.GONE);
					scanMoreInfoBtn.setBackgroundResource(R.drawable.app_market_detail_security_expand);
				}
			}
		});
		// 继续浏览缩略图
		networkTipsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				networkTipsLinearlayout.setVisibility(View.GONE);
				appThumbnail.setVisibility(View.VISIBLE);
				// 加载缩略图
				initAppThumbnail();
			}
		});
		// 下载按钮
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(softDetialBean.identifier)) {// 已安装应用直接打开
					if (AndroidPackageUtils.isPkgInstalled(getContext(), softDetialBean.identifier)) {
						AndroidPackageUtils.runApplication(getContext(), softDetialBean.identifier);
						appDetailPopupWindow.cancelDrawerSearchAppDetailPopupWindow();
						return;
					}
				}
				AppDistributeStatistics.AppDistributePercentConversionStatistics(getContext(), "606");
				
				Resources r = getContext().getResources();

				switch (state) {
				case DownloadState.STATE_PAUSE:
				case DownloadState.STATE_CANCLE:
				case DownloadState.STATE_NONE: // to start download
				case -1:
					btnDownload.setText(r.getString(R.string.searchbox_waiting));
					if (mConnection != null) {
						// 添加到下载管理器
						BaseDownloadInfo dlInfo = new BaseDownloadInfo(softDetialBean.getKey(), FileType.FILE_APK.getId(),
								softDetialBean.downloadUrl, softDetialBean.resName, AppMarketUtil.PACKAGE_DOWNLOAD_DIR,
								(softDetialBean.getKey() + ".apk"), Global.url2path(softDetialBean.downloadUrl, Global.CACHES_HOME_MARKET));
						dlInfo.setDisId(softDetialBean.resName);
						dlInfo.setDisSp(AppAnalysisConstant.SP_HOTWORD_RECOMMEND_APP);
						mConnection.addDownloadTask(dlInfo);
					}
					break;

				case DownloadState.STATE_DOWNLOADING: // to pause download
					btnDownload.setText(r.getString(R.string.searchbox_pause_with_progress, progress));
					if (mConnection != null)
						mConnection.pause(softDetialBean.getKey());
					break;
				case DownloadState.STATE_FINISHED:// 下载完成
					btnDownload.setText(r.getString(R.string.searchbox_download_completed));
					//假如有下载好了，就直接安装
					try{
						File file = new File(AppMarketUtil.PACKAGE_DOWNLOAD_DIR + softDetialBean.getKey() + ".apk");
						if (file.exists()) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
							context.startActivity(intent);
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				default:
					break;
				}
			}
		});
	}

	/**
	 * 请求更新详情内容 Title: requestUpdateDrawerSearchAppDetailView Description:
	 * 
	 * @param packageName
	 * @author maolinnan_350804
	 */
	public void requestUpdateDrawerSearchAppDetailView(final SoftDetialBean softDetialBean) {
		if (softDetialBean == null) {
			return;
		}
		this.softDetialBean = softDetialBean;
		// 填入对应信息
		icon.setImageResource(R.drawable.app_market_default_icon);
		Drawable tmp = asyncImageLoader.loadDrawable(softDetialBean.iconUrl, new ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				icon.setImageDrawable(imageDrawable);
			}

		});
		if(tmp != null){
			icon.setImageDrawable(tmp);
		}
		AppTypeBusiness.showType(apptype, softDetialBean.appType);
		appTitle.setText(softDetialBean.resName);
		ratingBar.setProgress(softDetialBean.star);
		appSize.setText(softDetialBean.size);
		appVersion.setText(String.format(context.getString(R.string.searchbox_hotword_detail_info_app_version), softDetialBean.version));
		detailInfoContent.setText(Html.fromHtml(softDetialBean.getDescript()));
		downNumber.setText(context.getString(R.string.searchbox_hotword_detail_downnumber) + softDetialBean.downloadNumber);
		// 填充安全信息
		initWarnInfo();
		if (TelephoneUtil.isWifiEnable(context)) {// 在wifi环境下
			networkTipsLinearlayout.setVisibility(View.GONE);
			appThumbnail.setVisibility(View.VISIBLE);
			// 加载缩略图
			initAppThumbnail();
		}
		// 恢复布局状态
		scrollView.scrollTo(0, 0);
		marketPower.setVisibility(View.GONE);
		scanMoreInfoBtn.setBackgroundResource(R.drawable.app_market_detail_security_expand);
		btnDownload.setText(R.string.common_button_download);
		// 获取下载状态
		if (mConnection != null) {
			BaseDownloadInfo baseDownloadInfo = mConnection.getDownloadTask(softDetialBean.getKey());
			if (baseDownloadInfo != null) {
				state = baseDownloadInfo.getState();
				progress = baseDownloadInfo.progress;
			}else{
				state = DownloadState.STATE_NONE;
				progress = 0;
			}
		}else{//清空当前状态
			state = DownloadState.STATE_NONE;
			progress = 0;
		}
		updateDownloadUI();
		// 已经安装直接显示打开
		if (!TextUtils.isEmpty(softDetialBean.identifier)) {// 已安装应用直接打开
			if (AndroidPackageUtils.isPkgInstalled(getContext(), softDetialBean.identifier)) {
				btnDownload.setText(R.string.common_button_open);
			}
		}
	}

	/**
	 * 初始化缩略图 Title: initAppThumbnail Description:
	 * 
	 * @author maolinnan_350804
	 */
	private void initAppThumbnail() {
		if (softDetialBean == null) {
			return;
		}
		imageAdapter.setDate(softDetialBean.snapshots);
	}

	private class ImageAdapter extends BaseAdapter {
		private ArrayList<String> snapshots = new ArrayList<String>();
		private Map<String, Drawable> imageThumbnail;

		public ImageAdapter() {
			imageThumbnail = new HashMap<String, Drawable>();
		}

		public void setDate(ArrayList<String> snapshots) {
			if (snapshots == null){
				return;
			}
			this.snapshots = snapshots;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return snapshots.size();
		}

		@Override
		public Object getItem(int position) {
			return snapshots.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView image;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.searchbox_hotword_detail_info_view_v6_item, null);
				image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(image);
			} else {
				image = (ImageView) convertView.getTag();
			}
			Drawable drawable = imageThumbnail.get(snapshots.get(position));
			if (drawable != null) {
				image.setBackgroundDrawable(drawable);
			} else {
				image.setBackgroundResource(R.drawable.framwork_viewfactory_load_data_img);
				asyncImageLoader.loadDrawable(snapshots.get(position), new ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
						image.setBackgroundDrawable(imageDrawable);
						imageThumbnail.put(imageUrl, imageDrawable);
					}
				});
			}
			return convertView;
		}
	}

	/**
	 * 填充安全信息 Title: initWarnInfo Description:
	 * 
	 * @author maolinnan_350804
	 */
	private void initWarnInfo() {
		if (softDetialBean == null) {
			return;
		}
		WarnBean bean = this.softDetialBean.safeInfo;
		if (bean == null) {
			return;
		}
		marketPower.removeAllViews();
		WarnCategory scanCate = bean.warnCategorys.get(WarnBean.WARN_SCANPROVIDER);
		float commTextsize = 14;
		if (scanCate != null && scanCate.state != 0) {
			formatWarnCateTextView(scanIconView, scanCate, 0, 0, 0, 0);
			List<WarnItem> items = scanCate.warnItems;
			for (Iterator<WarnItem> it = items.iterator(); it.hasNext();) {
				WarnItem item = (WarnItem) it.next();
				if (item != null && item.state != 0) {
					String title = item.title;
					if (!TextUtils.isEmpty(title)) {
						LinearLayout itemLayout = new LinearLayout(getContext());
						itemLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
						itemLayout.setOrientation(LinearLayout.HORIZONTAL);
						itemLayout.setGravity(Gravity.CENTER_VERTICAL);
						marketPower.addView(itemLayout);

						ImageView iconView = new ImageView(getContext());
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						lp.setMargins(40, 0, 0, 0);
						iconView.setLayoutParams(lp);

						int defaultImage = 0;
						if (!TextUtils.isEmpty(title)) {
							if (title.contains(context.getText(R.string.searchbox_hotword_detail_info_warn_qq))) {
								defaultImage = R.drawable.market_warn_qq;
							} else if (title.contains(context.getText(R.string.searchbox_hotword_detail_info_warn_kingsoft))) {
								defaultImage = R.drawable.market_warn_kingsoft;
							} else if (title.contains(context.getText(R.string.searchbox_hotword_detail_info_warn_lbe))) {
								defaultImage = R.drawable.market_warn_lbe;
							} else {
								defaultImage = R.drawable.market_warn_360;
							}
						}
						if (defaultImage == 0) {
							iconView.setImageResource(R.drawable.market_warn_gray);
						} else {
							iconView.setImageResource(defaultImage);
						}
						itemLayout.addView(iconView);

						TextView itemView = new TextView(getContext());
						itemView.setTextColor(Color.BLACK);
						itemView.setTextSize(commTextsize);
						String str = title
								+ context.getText(R.string.searchbox_hotword_detail_info_warn_fruit)
								+ (item.state == 1 ? context.getText(R.string.searchbox_hotword_detail_info_warn_pass) : context
										.getText(R.string.searchbox_hotword_detail_info_warn_never_tested));
						SpannableStringBuilder style = new SpannableStringBuilder(str);
						int iconlen = 0;

						int titlelen = iconlen + title.length() + context.getText(R.string.searchbox_hotword_detail_info_warn_fruit).length();
						style.setSpan(new ForegroundColorSpan(getStateColor(item.state)), titlelen, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						itemView.setText(style);
						itemLayout.addView(itemView);
					}
				}
			}
		}
	}

	/** 填充安全提示 */
	public void formatWarnCateTextView(TextView textView, WarnCategory cate, int marginleft, int marginTop, int marginRight, int marginBottom) {
		if (cate == null || cate.state == 0) {
			textView.setVisibility(View.GONE);
			return;
		}
		if (cate.state == 1) {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.market_warn_green, 0, 0, 0);
		} else if (cate.state == 2) {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.market_warn_yellow, 0, 0, 0);
		} else if (cate.state == 3) {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.market_warn_red, 0, 0, 0);
		} else if (cate.state == 4) {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.market_warn_gray, 0, 0, 0);
		}
		textView.setCompoundDrawablePadding(8);
		textView.setTextColor(getStateColor(cate.state));
		textView.setText(cate.title);
		textView.setTextSize(14);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(marginleft, marginTop, marginRight, marginBottom);
		textView.setLayoutParams(lp);
	}

	private int getStateColor(int state) {
		switch (state) {
		case 0:
		case 1:
			return Color.rgb(0x71, 0xa7, 0x01);
		case 2:
			return Color.rgb(0xf8, 0x69, 0x00);
		case 3:
			return Color.rgb(0xdd, 0x03, 0x03);
		case 4:
			return Color.rgb(0x76, 0x76, 0x76);
		}
		return Color.rgb(0x71, 0xa7, 0x01);
	}

	/**
	 * 下载状态监听
	 */
	private DownloadProgressReceiver mProgressReceiver;

	/**
	 * 下载管理进程通讯连接
	 */
	private DownloadServerServiceConnection mConnection;

	/**
	 * 绑定下载服务
	 */
	private void bindService() {
		if (mConnection != null)
			return;

		ThreadUtil.executeDrawer(new Runnable() {
			@Override
			public void run() {
				try {
					// 绑定下载进程
					mConnection = new DownloadServerServiceConnection(getContext());
					getContext().bindService(new Intent(getContext(), DownloadServerService.class), mConnection, Context.BIND_AUTO_CREATE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * 解除绑定
	 */
	private void unbindService() {
		if (mConnection == null)
			return;

		ThreadUtil.executeDrawer(new Runnable() {
			@Override
			public void run() {
				try {
					getContext().unbindService(mConnection);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void registerDownloadProgressReceiver() {
		try { // 注册下载状态监听
			if (mProgressReceiver == null)
				mProgressReceiver = new DownloadProgressReceiver();

			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE);
			getContext().registerReceiver(mProgressReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unregisterDownloadProgressReceiver() {
		try {
			if (mProgressReceiver != null)
				getContext().unregisterReceiver(mProgressReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		this.registerDownloadProgressReceiver();
		this.bindService();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.unregisterDownloadProgressReceiver();
		this.unbindService();
	}

	/**
	 * 下载进度广播监听
	 */
	private class DownloadProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String identification = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
				if (identification == null || !identification.equals(softDetialBean.getKey()))
					return;
				state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
				progress = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_PROGRESS, 0);

				updateDownloadUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}// end DownloadReceiver

	private void updateDownloadUI() {

		if (!TextUtils.isEmpty(softDetialBean.identifier)) {// 已安装应用直接打开
			if (AndroidPackageUtils.isPkgInstalled(getContext(), softDetialBean.identifier)) {
				btnDownload.setText(R.string.common_button_open);
			}
		}

		Resources r = getContext().getResources();
		switch (state) {
		case DownloadState.STATE_DOWNLOADING: // ■ 下载中
			btnDownload.setText(r.getString(R.string.searchbox_downloading, progress));
			break;
		case DownloadState.STATE_PAUSE: // ■ 暂停
			btnDownload.setText(r.getString(R.string.searchbox_pause_with_progress, progress));
			break;
		case DownloadState.STATE_WAITING: // ■ 等待下载
			btnDownload.setText(r.getString(R.string.searchbox_waiting));
			break;
		case DownloadState.STATE_FINISHED: // ■ 下载完成
			btnDownload.setText(r.getString(R.string.searchbox_download_completed));
			break;
		case DownloadState.STATE_CANCLE: // ■ 取消
			btnDownload.setText(r.getString(R.string.searchbox_canceled));
			break;
		default:
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus) {// 界面显示的时候
			// 已经安装直接显示打开
			if (!TextUtils.isEmpty(softDetialBean.identifier)) {// 已安装应用直接打开
				if (AndroidPackageUtils.isPkgInstalled(getContext(), softDetialBean.identifier)) {
					btnDownload.setText(R.string.common_button_open);
				}
			}
		}
	}
}
