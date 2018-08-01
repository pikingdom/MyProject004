package com.nd.hilauncherdev.webconnect.downloadmanage.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import android.widget.Toast;
import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.GpuControler;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.db.ConfigDataBase;
import com.nd.hilauncherdev.launcher.config.preference.BaseSettingsPreference;
import com.nd.hilauncherdev.launcher.config.preference.SettingsConstants;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerService;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.IFileTypeHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

/**
 * 下载管理界面
 */
public class DownloadManagerActivity extends Activity implements OnClickListener {

	protected enum Tab {

		TAB_APK(DownloadServerService.SHOW_TYPE_APK, new FileType[] { FileType.FILE_APK, FileType.FILE_DYNAMIC_APK },
				 R.string.downloadmanager_soft_tab_title, R.drawable.downloadmanager_apk_icon, "apk"),
		TAB_RING(DownloadServerService.SHOW_TYPE_RING, new FileType[] { FileType.FILE_RING },
				R.string.downloadmanager_ring_tab_title, R.drawable.downloadmanager_ring_icon, "ring"),
		TAB_FONT(DownloadServerService.SHOW_TYPE_FONT, new FileType[] { FileType.FILE_FONT },
				R.string.downloadmanager_font_tab_title, R.drawable.downloadmanager_font_icon, "font"),
		TAB_THEME(DownloadServerService.SHOW_TYPE_THEME, new FileType[] { FileType.FILE_THEME, FileType.FILE_THEME_SERIES },
				R.string.downloadmanager_theme_tab_title, R.drawable.downloadmanager_theme_icon, "theme"),
		TAB_WALLPAPER(DownloadServerService.SHOW_TYPE_WALLPAPER,new FileType[] { FileType.FILE_WALLPAPER, FileType.FILE_VIDEO_PAPER },
				R.string.downloadmanager_wallpaper_tab_title, R.drawable.downloadmanager_wallpaper_icon, "wallpaper"),
		TAB_LOCK(DownloadServerService.SHOW_TYPE_LOCK,new FileType[] { FileType.FILE_LOCK },
				R.string.downloadmanager_lock_tab_title, R.drawable.downloadmanager_lock_icon, "lock"),
		TAB_ICON(DownloadServerService.SHOW_TYPE_ICON,new FileType[] { FileType.FILE_ICON },
				R.string.downloadmanager_icon_tab_title, R.drawable.downloadmanager_icon_icon, "icon"),
		TAB_INPUT(DownloadServerService.SHOW_TYPE_INPUT,new FileType[] { FileType.FILE_INPUT, FileType.FILE_INPUT_IFLY },
				R.string.downloadmanager_input_tab_title, R.drawable.downloadmanager_input_icon, "input"),
		TAB_SMS(DownloadServerService.SHOW_TYPE_SMS,new FileType[] { FileType.FILE_SMS, FileType.FILE_SMS_COOTEK },
				R.string.downloadmanager_sms_tab_title, R.drawable.downloadmanager_sms_icon, "sms"),
		TAB_WEATHER(DownloadServerService.SHOW_TYPE_WEATHER,new FileType[] { FileType.FILE_WEATHER },
				R.string.downloadmanager_weather_tab_title, R.drawable.downloadmanager_weather_icon, "weather");

		FileType[] mFileTypes = null;
		int mShowType;
		int mTitleId;
		int mIconId;
		String mTag;

		Tab(int showType, FileType[] fileTypes, int titleId, int iconId, String tag) {
			mShowType = showType;
			mFileTypes = fileTypes;
			mTitleId = titleId;
			mIconId = iconId;
			mTag = tag;
		};
	};

	public static final String THEME_APT_INSTALL_RESULT = "nd.pandahome.response.theme.apt.install";
	public static final String THEME_APT_INSTALL_RESULT_FAIL = "nd.pandahome.response.theme.apt.install.fail";

	//从哪里打开下载管理
	public static final String EXTRA_FROM = "open_from";
	//从通知栏打开
	public static final String FROM_TZL = "tzl";
	//从个人中心打开
	public static final String FROM_GRZX = "grzx";
	//从个性化我的打开
	public static final String FROM_WD = "wd";
	//本地目录丢失，需要批量下载
	public static final String EXTRA_SDCARD_LOSE = "extra_sdcard_lose";

	private static final int ANI_DURATION = 250;

	private static final String KEY_UPGRADE_MASK = "downloadmanager_upgrade_mask";

	/**
	 * 下载服务
	 */
	private BroadcastReceiver mNewAppInstallReceiver;
	private ThemeStateReceiver mThemeStateReceiver;
	Handler mHandler = new Handler();
	private DownloadCommonView mCommonViewAll;
	private DownloadCommonView mCommonViewCategory;
	private ArrayList<BaseDownloadInfo> mData;
	private View mLayoutTitle;
	private View mBtnBack;
	private View mBtnSearch;
	private View mBtnMenu;
	private TextView mTvTitle;
	private DownloadCategoryMenu mCategoryMenu;
	private View mMaskingView;
	private boolean mIsPerformingAni = false;
	private boolean mIsFirstShowMenu = true;
	private View mIvGuide;
	//如果有新下载任务，则onResume时要关掉分类界面
	private boolean mNeedInvalidate = false;
	private int mIconSize = 0;
	private DownloadManager mDownloadManager;
	private boolean mIsVisiable = false;
	private String mOpenFrom = null;
	protected int mActionForAppStore = -1;
	boolean mInSDLose = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.downloadmanager_container);

		mIconSize = ScreenUtil.dip2px(this, 50);

		GpuControler.openGpu(this);
		mDownloadManager = DownloadManager.getInstance();

		initReceiver();
		initView();

		if (!processSDLoseFromOther(getIntent())) {
			statOpenFrom(getIntent());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (mCommonViewCategory.getVisibility() == View.VISIBLE) {
			mCommonViewCategory.close(false);
		}
		statOpenFrom(intent);
	}

	/**
	 * 处理由于本地目录丢失导致从壁纸、铃声等模块跳转到下载管理界面进行批量下载
	 */
	private boolean processSDLoseFromOther(Intent intent) {
		if (intent == null) {
			return false;
		}

		FileType fileType = FileType.fromId(intent.getIntExtra(EXTRA_SDCARD_LOSE, -1));
		Tab tab = mapFileTypeToTab(fileType);
		if (tab != null) {
			mInSDLose = true;
			mCommonViewCategory.setNeedRedownload();
			mCommonViewCategory.setFinishWhenBack();
			showCategoryView(tab);
		}

		return false;
	}

	private void statOpenFrom(Intent intent) {
		if (intent == null) {
			return;
		}

		mOpenFrom = intent.getStringExtra(EXTRA_FROM);
		if (mOpenFrom != null) {
			HiAnalytics.submitEvent(this, AnalyticsConstant.DOWNLOAD_MANAGER_USE, mOpenFrom);
		};

		mActionForAppStore = intent.getIntExtra("MYACTION", -1);

		if (!mIsVisiable) {
			FileType fileType = FileType.fromId(intent.getIntExtra(DownloadServerService.EXTRA_SHOW_TYPE, -1));
			Tab tab = mapFileTypeToTab(fileType);
			if (tab == Tab.TAB_APK) {
				showCategoryView(tab);
				mCommonViewCategory.setFinishWhenBack();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mNeedInvalidate && mCommonViewCategory.getVisibility() == View.VISIBLE) {
			mCommonViewCategory.close(false);
		}

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				loadDatas();
			}
		});


		mIsVisiable = true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		mNeedInvalidate = false;
	}

	@Override
	protected void onStop() {
		super.onStop();

		mIsVisiable = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mNewAppInstallReceiver != null) {
			unregisterReceiver(mNewAppInstallReceiver);
			mNewAppInstallReceiver = null;
		}

		if (mAddNewReceiver != null) {
			unregisterReceiver(mAddNewReceiver);
			mAddNewReceiver = null;
		}

		if (mThemeStateReceiver != null) {
			unregisterReceiver(mThemeStateReceiver);
			mThemeStateReceiver = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mCommonViewCategory.getVisibility() == View.VISIBLE) {
				mCommonViewCategory.close(true);
				return true;
			}

			if (isCategoryMenuShown()) {
				hideCategoryMenu(true);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	public static void setUpgradeMask(Context context) {
		ConfigDataBase db = ConfigDataBase.getInstance(context);
		db.addConfigData(KEY_UPGRADE_MASK, "true");
	}

	public static void cancelUpgradeMask(Context context) {
		ConfigDataBase db = ConfigDataBase.getInstance(context);
		db.updateConfigData(KEY_UPGRADE_MASK, "false");
	}

	public static boolean getUpgradeMask(Context context) {
		ConfigDataBase db = ConfigDataBase.getInstance(context);
		String value = db.getConfigData(KEY_UPGRADE_MASK);
		return (value != null && value.equals("true"));
	}

	public static Tab mapFileTypeToTab(FileType fileType) {
		for (Tab t : Tab.values()) {
			FileType[] types = t.mFileTypes;
			if (types == null) {
				continue;
			}

			for (int i = 0; i < types.length; i++) {
				if (types[i] == fileType) {
					return t;
				}
			}
		}

		return null;
	}

	private void initView() {
		mLayoutTitle = findViewById(R.id.layout_title);
		mBtnBack = mLayoutTitle.findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnSearch = mLayoutTitle.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(this);
		mBtnMenu = mLayoutTitle.findViewById(R.id.btn_menu);
		mBtnMenu.setOnClickListener(this);
		if (CommonGlobal.isBaiduLauncher()) {
			mBtnMenu.setVisibility(View.GONE);
		}

		mTvTitle = (TextView) mLayoutTitle.findViewById(R.id.tv_title);
		mTvTitle.setText(getResources().getString(R.string.downloadmanager_title));

		mCommonViewAll = (DownloadCommonView) findViewById(R.id.common_view_all);
		mCommonViewCategory = (DownloadCommonView) findViewById(R.id.common_view_category);
		if (!FileUtil.isFileExits(BaseConfig.getBaseDir())) {
			mInSDLose = true;
			mCommonViewAll.setNeedRedownload();
			mCommonViewCategory.setNeedRedownload();
		}

		mCategoryMenu = (DownloadCategoryMenu) findViewById(R.id.category_menu);
		mCategoryMenu.setItems(Tab.values());
		mMaskingView = findViewById(R.id.masking_view);
		mMaskingView.setOnClickListener(this);

		mIvGuide = findViewById(R.id.iv_guide);
		if (getUpgradeMask(this)) {
			mIvGuide.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		} else if (v == mBtnSearch) {

		} else if (v == mBtnMenu) {
			toggleCategoryMenu();
		} else if (v == mMaskingView) {
			hideCategoryMenu(true);
		}
	}

	void setTitleLayoutVisibility(int visibility) {
		mLayoutTitle.setVisibility(visibility);
	}

	/**
	 * 跳转到分类下载界面
	 */
	void showCategoryView(Tab tab) {
		if (tab == null) {
			return;
		}

		mCommonViewCategory.bringToFront();
		mCommonViewCategory.setVisibility(View.VISIBLE);
		mCommonViewCategory.refreshListViewData(tab, getCategoryData(tab), true);
		hideCategoryMenu(false);

		//解决HUAWEI D2-0082上显示错误的问题，bug14763
		mCommonViewAll.setVisibility(View.GONE);

		HiAnalytics.submitEvent(this, AnalyticsConstant.DOWNLOAD_MANAGER_TYPE, tab.mTag);
	}

	void onCategoryViewClose() {
		mCommonViewAll.setVisibility(View.VISIBLE);
	}

	/**
	 * 过滤出分类数据
	 */
	private ArrayList<BaseDownloadInfo> getCategoryData(Tab category) {
		if (mData == null
			|| mData.size() <= 0
			|| category.mFileTypes == null
			|| category.mFileTypes.length <= 0) {
			return null;
		}

		ArrayList<BaseDownloadInfo> categoryData = new ArrayList<BaseDownloadInfo>();
		for (BaseDownloadInfo info : mData) {
			for (int i=0; i<category.mFileTypes.length; i++) {
				if (info.getFileType() == category.mFileTypes[i].getId()) {
					categoryData.add(info);
				}
			}
		}

		return categoryData;
	}

	/**
	 * 显示/关闭分类菜单
	 */
	private void toggleCategoryMenu() {
		if (mIvGuide.getVisibility() == View.VISIBLE) {
			mIvGuide.setVisibility(View.GONE);
			cancelUpgradeMask(this);
		}

		if (isCategoryMenuShown()) {
			hideCategoryMenu(true);
		} else {
			showCategoryMenu();
		}
	}

	private boolean isCategoryMenuShown() {
		return mMaskingView.getVisibility() == View.VISIBLE;
	}

	private void showCategoryMenu() {
		if (isCategoryMenuShown() || mIsPerformingAni) {
			return;
		}

		if (mIsFirstShowMenu) {
			mCategoryMenu.bringToFront();
			mLayoutTitle.bringToFront();
			mIsFirstShowMenu = false;
		}

		mCategoryMenu.setVisibility(View.VISIBLE);
		mMaskingView.setVisibility(View.VISIBLE);
		mIsPerformingAni = true;

		int yDelta = mCategoryMenu.getHeight() * -1;
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, yDelta, 0);
		translateAnimation.setFillAfter(false);
		translateAnimation.setDuration(ANI_DURATION);
		mCategoryMenu.startAnimation(translateAnimation);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mIsPerformingAni = false;
			}
		});
		alphaAnimation.setFillAfter(false);
		alphaAnimation.setDuration(ANI_DURATION);
		mMaskingView.startAnimation(alphaAnimation);
	}

	private void hideCategoryMenu(boolean needAni) {
		if (!isCategoryMenuShown()) {
			return;
		}
		if (!needAni) {
			mCategoryMenu.setVisibility(View.GONE);
			mMaskingView.setVisibility(View.GONE);
			return;
		} else if (mIsPerformingAni) {
			return;
		}

		int yDelta = mCategoryMenu.getHeight() * -1;
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, yDelta);
		translateAnimation.setFillAfter(false);
		translateAnimation.setDuration(ANI_DURATION);
		mCategoryMenu.startAnimation(translateAnimation);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mIsPerformingAni = false;
				mMaskingView.setVisibility(View.GONE);
				mCategoryMenu.setVisibility(View.GONE);
			}
		});
		alphaAnimation.setFillAfter(false);
		alphaAnimation.setDuration(ANI_DURATION);
		mMaskingView.startAnimation(alphaAnimation);
	}

	protected Bitmap getIcon(FileType fileType, String iconPath) {
		if (iconPath == null)
			return null;

		if (iconPath.startsWith("drawable:")) {
			Resources resources = getResources();
			String name = iconPath.substring(iconPath.lastIndexOf(":") + 1);

			try {
				if (Long.parseLong(name) > 0) {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			int resId = resources.getIdentifier(name, "drawable", getPackageName());
			if (resId == 0)
				return null;
			return BitmapFactory.decodeResource(resources, resId);
		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
			if (isIconNeedCrop(fileType)) {
				Bitmap cropBitmap = cropPreview(bitmap);
				if (cropBitmap != null) {
					bitmap.recycle();
					bitmap = cropBitmap;
				}
			}
			if (isIconNeedMask(fileType)) {
				Bitmap cutBitmap = cutPreview(bitmap);
				if (cutBitmap != null) {
					bitmap.recycle();
					bitmap = cutBitmap;
				}
			}

			return bitmap;
		}
	}

	private boolean isIconNeedCrop(FileType fileType) {
		return (fileType != null
				&& (fileType == FileType.FILE_THEME || fileType == FileType.FILE_LOCK));
	}

	private boolean isIconNeedMask(FileType fileType) {
		return (fileType != null
				&& (fileType == FileType.FILE_THEME
				    || fileType == FileType.FILE_LOCK
				    || fileType == FileType.FILE_ICON
				    || fileType == FileType.FILE_INPUT
				    || fileType == FileType.FILE_WEATHER
				    || fileType == FileType.FILE_SMS));
	}

	private Bitmap cropPreview(Bitmap srcBitmap) {
		Bitmap cropBitmap = null;
		if (srcBitmap != null) {
			int width = Math.min((srcBitmap.getWidth() - 2*13), srcBitmap.getHeight());
			int height = width;
			if (width > 0 && height > 0) {
				cropBitmap = Bitmap.createBitmap(srcBitmap,
						                         (srcBitmap.getWidth()-width)/2,
						                         (srcBitmap.getHeight()-height)/2,
						                         width,
						                         height);


			}
		}

		return cropBitmap;
	}

	/**
	 * 切割蒙版
	 */
	private Bitmap cutPreview(Bitmap srcBitmap) {
		Bitmap curBitmap = null;
		if (srcBitmap != null) {
			curBitmap = Bitmap.createBitmap(mIconSize, mIconSize, Bitmap.Config.ARGB_8888);
			Bitmap maskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.downloadmanager_preview_mask);
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			paint.setAntiAlias(true);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			Canvas canvas = new Canvas(curBitmap);
			Rect rect = new Rect(0, 0, mIconSize, mIconSize);
			canvas.drawBitmap(maskBitmap, null, rect, null);
			canvas.drawBitmap(srcBitmap, null, rect, paint);
		}

		return curBitmap;
	}

	/**
	 * 设置下载状态
	 * @param downloadInfo
	 */
	protected void setDownloadState(final BaseDownloadInfo downloadInfo) {
		int state = downloadInfo.getState();
		switch (state) {

		case DownloadState.STATE_DOWNLOADING:
			downloadInfo.setState(downloadInfo.getDownloadingState());
			break;
		case DownloadState.STATE_FINISHED:
			downloadInfo.setState(downloadInfo.getFinishedUninstalled());
			break;
		case DownloadState.STATE_INSTALLED:
			downloadInfo.setState(downloadInfo.getFinishedInstalled());
			break;
		case DownloadState.STATE_PAUSE:
			downloadInfo.setState(downloadInfo.getPauseState());
			break;
		case DownloadState.STATE_WAITING:
			downloadInfo.setState(downloadInfo.getWaitingState());
			break;
		}

		// 下载完成、已安装、未下载状态下判断是否正在安装，是否已安装，如果是其他状态，保持不变
		switch (state) {
		case DownloadState.STATE_CANCLE:
		case DownloadState.STATE_FINISHED:
		case DownloadState.STATE_INSTALLED:
		case DownloadState.INSTALL_STATE_INSTALLING:
		case DownloadState.STATE_NONE:

			if (downloadInfo.getFileType() == FileType.FILE_APK.getId()) {
				// 获取包信息
				final String packageName = downloadInfo.getPacakgeName(this);
				final int versionCode = downloadInfo.getVersionCode(this);

				mDownloadManager.isApkInstalling(packageName, new DownloadManager.ResultCallback() {
					@Override
					public void getResult(Object arg0) {
						if (arg0 == null || !(arg0 instanceof Boolean)) {
							return;
						}
						if (((Boolean) arg0).booleanValue()) {
							// 正在安装
							downloadInfo.setState(downloadInfo.getInstallingState());
						} else if (AndroidPackageUtils.isPkgInstalled(DownloadManagerActivity.this, packageName, versionCode)) {
//						} else if (AndroidPackageUtils.isPkgInstalled(this, packageName)) {
							// 已安装
							downloadInfo.setState(downloadInfo.getFinishedInstalled());
							downloadInfo.mNeedRedownload = false;
						}
					}
				});
			}

			break;
		}
	}

	protected void onNoDataButtonClicked(Tab category) {
	}

	protected int getNoDataImageRes() {
		return -1;
	}

	private void initReceiver() {
		mNewAppInstallReceiver = new NewAppInstallReceiver();
		IntentFilter itFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		itFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		itFilter.addDataScheme("package");
		registerReceiver(mNewAppInstallReceiver, itFilter);

		if (mAddNewReceiver != null) {
			IntentFilter filter = new IntentFilter(DownloadServerService.ACTION_ADD_NEW);
			registerReceiver(mAddNewReceiver, filter);
		}

		try {
			if (mThemeStateReceiver == null)
				mThemeStateReceiver = new ThemeStateReceiver();
			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE);
			filter.addAction(THEME_APT_INSTALL_RESULT);
			filter.addAction(THEME_APT_INSTALL_RESULT_FAIL);
			filter.addAction(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_SUCCESS);
			filter.addAction(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_FAIL);
			this.registerReceiver(mThemeStateReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制一份数据
	 *
	 * @param srcMap
	 * @return
	 */
	private void copyDonwloadInfo(Map<String, BaseDownloadInfo> srcMap) {
		if (srcMap == null)
			return;

		synchronized (this) {
			if (mData != null) {
				mData.clear();
			}
			mData = new ArrayList<BaseDownloadInfo>();

			Set<Entry<String, BaseDownloadInfo>> entrySet = srcMap.entrySet();
			for (Entry<String, BaseDownloadInfo> entry : entrySet) {
				BaseDownloadInfo toCopyInfo = entry.getValue();
				if (toCopyInfo == null
					|| toCopyInfo.getFileType() == FileType.FILE_NONE.getId()) {
					continue;
				}

				BaseDownloadInfo newInfo = new BaseDownloadInfo(toCopyInfo, CommonGlobal.getApplicationContext());

				if (newInfo.getState() == DownloadState.STATE_FINISHED) {
					IFileTypeHelper helper = FileType.fromId(newInfo.getFileType()).getHelper();
					if (helper != null && !helper.fileExists(newInfo)) {
						newInfo.mNeedRedownload = true;
					}
				}

				if (mData.size() <= 0) {
					mData.add(newInfo);
					continue;
				}
				if (newInfo.getState() == DownloadState.STATE_DOWNLOADING) {
					mData.add(0, newInfo);
					continue;
				}

				int insertIndex = -1;
				for (int i = 0; i<mData.size(); i++) {
					if (newInfo.getBeginTime() >= mData.get(i).getBeginTime()
						&& mData.get(i).getState() != DownloadState.STATE_DOWNLOADING) {
						insertIndex = i;
						break;
					}
				}
				if (insertIndex == -1) {
					mData.add(newInfo);
				} else {
					mData.add(insertIndex, newInfo);
				}
			}
		}
	}

	protected void refreshListView() {
		mCommonViewAll.refreshListView();
		if (mCommonViewCategory.getVisibility() == View.VISIBLE) {
			mCommonViewCategory.refreshListView();
		}
	}

	private void refreshListViewData(boolean invalidate) {
		synchronized (this) {
			if (mCommonViewCategory.getVisibility() == View.VISIBLE) {
				mCommonViewCategory.refreshListViewData(getCategoryData(mCommonViewCategory.getCategory()), invalidate);
			}

			mCommonViewAll.refreshListViewData(mData, invalidate);
		}
	}

	private void exitEditState() {
		mCommonViewAll.exitEditState();
		if (mCommonViewCategory.getVisibility() == View.VISIBLE) {
			mCommonViewCategory.exitEditState();
		}
	}

	private synchronized void loadDatas() {
		mDownloadManager.getNormalDownloadTasks(new DownloadManager.ResultCallback() {
			@Override
			public void getResult(final Object arg0) {
				ThreadUtil.executeMore(new Runnable() {
					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						try {
							copyDonwloadInfo((Map<String, BaseDownloadInfo>) arg0);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								refreshListViewData(mNeedInvalidate);
								mNeedInvalidate = false;
							}
						});
					}
				});
			}
		});
	}

	/**
	 * 新应用安装监听
	 */
	private class NewAppInstallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String packageName = intent.getData().getSchemeSpecificPart();
				if (StringUtil.isEmpty(packageName)) {
					return;
				}
				refreshListView();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据包名遍历查询是否存在下载任务，有则返回下载下的保存文件地址
	 * 阻塞方法，需在线程中执行
	 * @param pkg
	 * @return
	 */
	public static File getApkFileViaPkgName(final Context context , final String pkg){
		DownloadManager downloadManager = DownloadManager.getInstance();
		if(downloadManager == null || TextUtils.isEmpty(pkg))
			return null;
		final File[] file = new File[1];
		downloadManager.getNormalDownloadTasks(new DownloadManager.ResultCallback() {
			public void getResult(final Object arg0) {
				try {
					Map<String, BaseDownloadInfo> map = (Map<String, BaseDownloadInfo>) arg0;
					for (Entry<String, BaseDownloadInfo> entry : map.entrySet()) {
						BaseDownloadInfo baseDownloadInfo = entry.getValue();
						String curPkgName = baseDownloadInfo.getPacakgeName(context);
						String filePath = baseDownloadInfo.getFilePath();
						if (!TextUtils.isEmpty(curPkgName) && curPkgName.equals(pkg)) {
							if (!TextUtils.isEmpty(filePath)) {
								file[0] = new File(filePath);
								return;
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
		return file[0];
	}

	/**
	 * 监听下载管理加入新任务
	 */
	private BroadcastReceiver mAddNewReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			DownloadManagerActivity.this.mNeedInvalidate = true;
		}
	};

//	/**
//	 * 绑定下载服务的回调
//	 */
//	private class DownloadServiceBindCallBack implements CommonCallBack<Boolean> {
//
//		@Override
//		public void invoke(final Boolean... arg) {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					boolean bindSuccess = false;
//					if (arg != null && arg.length > 0) {
//						bindSuccess = arg[0].booleanValue();
//					}
//					if (bindSuccess) {
//						loadDatas();
//					} else {
//						mData = null;
//						refreshListViewData(false);
//					}
//				}
//			});
//		}
//	}

	/**
	 * 删除单个
	 */
	void deleteSingle(BaseDownloadInfo info) {
		if (info == null || mData == null || mData.size() <= 0) {
			return;
		}

		if (mData.remove(info)) {
			mDownloadManager.cancelNormalTask(info.getIdentification(), null);
			refreshListViewData(false);
		}

		if (isNeedRecordDelete(info)) {
			HiAnalytics.submitEvent(DownloadManagerActivity.this, AnalyticsConstant.DOWNLOAD_MANAGER_DOWNLOAD_STATE, "sc");
		}
	}

	private boolean isNeedRecordDelete(BaseDownloadInfo info) {
		if (info.getState() == DownloadState.STATE_DOWNLOADING || info.getState() == DownloadState.STATE_PAUSE) {
			return true;
		}

		return false;
	}

	/**
	 * 批量删除
	 */
	void deleteBatch() {
		if (mData == null || mData.size() <= 0) {
			return;
		}
		int deleteCount = 0;
		for (BaseDownloadInfo info : mData) {
			if (info.mIsSelected) {
				deleteCount++;
			}
		}
		if (deleteCount <= 0) {
			return;
		}

		ViewFactory.getAlertDialog(this, getString(R.string.download_delete_title), String.format(getString(R.string.download_delete_msg), deleteCount),
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				doBatchDelete();
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	/**
	 * 批量删除
	 */
	private void doBatchDelete() {
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					if (mData == null || mData.size() <= 0) {
						return;
					}

					boolean isNeedRecordDelete = false;
					Iterator<BaseDownloadInfo> it = mData.iterator();
					try {
						while (it.hasNext()) {
							BaseDownloadInfo info = it.next();
							if (info.mIsSelected) {
								mDownloadManager.cancelNormalTask(info.getIdentification(), null);
								it.remove();
								if (isNeedRecordDelete(info)) {
									isNeedRecordDelete = true;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (isNeedRecordDelete) {
						HiAnalytics.submitEvent(DownloadManagerActivity.this, AnalyticsConstant.DOWNLOAD_MANAGER_DOWNLOAD_STATE, "sc");
					}

					mHandler.post(new Runnable() {
						@Override
						public void run() {
							refreshListViewData(false);
							exitEditState();
						}
					});;
				}
			}
		});
	}

	class ThemeStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null == intent) {
				return;
			}

			String action = intent.getAction();
			if (null == action) {
				return;
			}

			if (THEME_APT_INSTALL_RESULT_FAIL.equals(action) || THEME_APT_INSTALL_RESULT.equals(action) || ThemeGlobal.ACTION_INSTALL_THEME_SERIES_SUCCESS.equals(action)
					|| ThemeGlobal.ACTION_INSTALL_THEME_SERIES_FAIL.equals(action)) {
				refreshListView();
			}
		}
	}
}
