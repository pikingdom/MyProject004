package com.nd.hilauncherdev.drawer.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.analysis.AppDistributeStatistics;
import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.app.SerializableAppInfo;
import com.nd.hilauncherdev.app.data.AppDataBase;
import com.nd.hilauncherdev.app.data.AppTable;
import com.nd.hilauncherdev.app.ui.view.icontype.NotificationAppIconType;
import com.nd.hilauncherdev.app.view.AppDrawerIconMaskTextView;
import com.nd.hilauncherdev.app.view.AppDrawerIconMaskTextView.AppCatchedIconCallback;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.Constants;
import com.nd.hilauncherdev.drawer.UpgradeFolderInfo;
import com.nd.hilauncherdev.drawer.apphide.AppHide;
import com.nd.hilauncherdev.drawer.classify.AppClassifyManager;
import com.nd.hilauncherdev.drawer.data.DrawerDataFactory;
import com.nd.hilauncherdev.drawer.data.DrawerNewInstalledHelper;
import com.nd.hilauncherdev.drawer.data.DrawerPreference;
import com.nd.hilauncherdev.drawer.data.widget.DynamicWidgetManager;
import com.nd.hilauncherdev.drawer.data.widget.LauncherItemInfo;
import com.nd.hilauncherdev.drawer.data.widget.LauncherOnLineWidgetInfo;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetInfo;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetInfoManager;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetNewAddedHelper;
import com.nd.hilauncherdev.drawer.upgrade.AppUpgradeOnLauncherStartListenerImpl;
import com.nd.hilauncherdev.drawer.upgrade.AppUpgradeOnLauncherStartListenerImpl.UpdateAppInfo;
import com.nd.hilauncherdev.drawer.upgrade.InstallAppInfo;
import com.nd.hilauncherdev.drawer.util.DrawerSortHelper;
import com.nd.hilauncherdev.drawer.view.searchbox.VoiceRecognitionListener;
import com.nd.hilauncherdev.drawer.view.searchbox.datamodel.PositionInfo;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.folder.model.FolderEncriptHelper;
import com.nd.hilauncherdev.folder.model.FolderSwitchController;
import com.nd.hilauncherdev.folder.view.FolderViewFactory;
import com.nd.hilauncherdev.framework.AnyCallbacks.CommonSlidingViewCallback;
import com.nd.hilauncherdev.framework.AnyCallbacks.OnFolderDragOutCallback;
import com.nd.hilauncherdev.framework.OnKeyDownListenner;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.choosedialog.AppChooseDialogActivity;
import com.nd.hilauncherdev.framework.effect.DrawerAnimation;
import com.nd.hilauncherdev.framework.effect.EffectsType;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonLayout;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonLightbar;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnCommonSlidingViewClickListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnCommonSlidingViewLongClickListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnFlingListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnSnapToScreenListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnSwitchDataListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonViewHolder;
import com.nd.hilauncherdev.framework.view.commonsliding.LineLightbar;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.CommonSlidingViewData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.framework.view.dialog.CommonDialog;
import com.nd.hilauncherdev.framework.view.draggersliding.DraggerChooseItem;
import com.nd.hilauncherdev.framework.view.draggersliding.DraggerLayout;
import com.nd.hilauncherdev.framework.view.draggersliding.DrawerLayout;
import com.nd.hilauncherdev.framework.view.draggersliding.datamodel.DraggerSlidingViewData;
import com.nd.hilauncherdev.framework.view.draggersliding.datamodel.IDraggerData;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.ControlledAnalyticsUtil;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.GpuControler;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.invoke.ForeignPackage;
import com.nd.hilauncherdev.kitset.util.ActivityActionUtil;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.BrowserUtil;
import com.nd.hilauncherdev.kitset.util.ColorUtil;
import com.nd.hilauncherdev.kitset.util.DefaultLauncherUtil;
import com.nd.hilauncherdev.kitset.util.HiLauncherEXUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StatusBarUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.DragController;
import com.nd.hilauncherdev.launcher.IconCache;
import com.nd.hilauncherdev.launcher.Launcher;
import com.nd.hilauncherdev.launcher.LauncherActionHelper;
import com.nd.hilauncherdev.launcher.LauncherActivityResultHelper;
import com.nd.hilauncherdev.launcher.LauncherAnimationHelp;
import com.nd.hilauncherdev.launcher.LauncherProviderHelper;
import com.nd.hilauncherdev.launcher.LauncherSettings;
import com.nd.hilauncherdev.launcher.WorkspaceHelper;
import com.nd.hilauncherdev.launcher.addboot.LauncherAddMainView;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.config.ConfigFactory.ConfigCallback;
import com.nd.hilauncherdev.launcher.config.IconAndTextSizeConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.search.common.PositioningListener;
import com.nd.hilauncherdev.launcher.search.searchview.SearchView;
import com.nd.hilauncherdev.launcher.touch.DropTarget;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.EditableIconView;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.myphone.data.MyPhoneDataFactory;
import com.nd.hilauncherdev.readme.ReadMeStateManager;
import com.nd.hilauncherdev.recommend.p8custom.CustomDrawerFolderBean;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppBean;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppWrapper;
import com.nd.hilauncherdev.settings.LauncherLayoutSettingsActivity;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.settings.adapter.SettingsScreenEffectsAdapter;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.assit.ThemeCompatibleResAssit;
import com.nd.hilauncherdev.theme.assit.ThemeUIRefreshAssit;
import com.nd.hilauncherdev.theme.assit.ThemeUIRefreshListener;
import com.nd.hilauncherdev.theme.data.ThemeData;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;
import com.nd.hilauncherdev.webconnect.upgradhint.BeautyUpgradeService;
import com.nd.hilauncherdev.widget.shop.enhance.WidgetShopActivity;
import com.nd.hilauncherdev.widget.shop.view.WidgetInfoActivity;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dalvik.system.DexClassLoader;

/**
 * 
 * @author Anson
 */
public class DrawerMainView extends RelativeLayout implements AllAppsView, OnSwitchDataListener, OnSnapToScreenListener,
		OnCommonSlidingViewClickListener, OnCommonSlidingViewLongClickListener, OnFlingListener, OnKeyDownListenner, CommonSlidingViewCallback,
		OnFolderDragOutCallback, PositioningListener, ThemeUIRefreshListener {
	private static final String TAG = "DrawerMainView";

	public Launcher mLauncher;

	private DragController mDragController;

	// region 匣子中的view
	// 顶部tab栏
	private NormalTopLayoutView normalTopLayoutView;

	// 编辑状态下的顶部栏 包括详情和备份
	private EditTopLayoutView editTopLayoutView;

	// 底部工具栏
	private BottomLayoutView bottomLayoutView;

	// 匣子菜单
	private DrawerMenu drawerMenu;

	// 搜索View
	private SearchView searchView;

	// 图标排序对话框
	private CommonDialog iconSortTypeDialog;

	// 匣子中编辑状态下底部workspace缩略图
	private DrawerEditZoneController drawerEditZoneController;

	public ProgressDialog progressDialog;

	public ViewGroup classifyPreviewBottomLayout;

	private DrawerSlidingView drawerSlidingView;

	// endregion

	// region 使用的数据
	private List<ICommonData> drawerList = new ArrayList<ICommonData>();

	private IDraggerData allAppsData;

	private IDraggerData widgetsData;

	private IDraggerData myPhoneData;

	private int[] allAppsAndMyPhoneRowCols;

	private List<ICommonDataItem> tempList;
	// endregion

	// region 文件夹操作相关
	/**
	 * 文件夹加密类
	 */
	private FolderEncriptHelper mFolderEncriptHelper;

	// pdw添加，文件夹打开控制器
	private FolderSwitchController mFolderOpenController;

	public View mClickFolderView;

	private FolderInfo mUserFolderInfo;
	// endregion

	// region 标志变量

	/**
	 * 是否要显示匣子中的顶部tab页面
	 * */

	private float mZoom;
	/**
	 * 程序匣子是否被打开，打开一次后即true
	 */
	private boolean hasbeenOpened;
	/**
	 * 是否已经初始化匣子数据库
	 */
	private boolean hasbeenInitDatabase;
	/**
	 * 应用程序总数与已刷新图标数量
	 */
	private int allAppsCount, cachedAppsCount;

	/**
	 * 是否进行的是删除操作
	 */
	private boolean isActionRemoveApp;

	/**
	 * 删除的程序包名
	 */
	private String removeAppPackageName;
	// endregion

	private SettingsPreference settingsPreference;

	private Vibrator mVibrator;

	private BroadcastReceiver refreshWidgetsReceiver;

	public boolean isFirstIntoWidgets = true;// 是否第一次显示小部件

	private SamsungDrawerSearchLayout mSamsungDrawerSearchLayout;
	private CommonLightbar mCommonLightbar;
	private LineLightbar mLightbar;

	public Drawable drawerBgDrawable; // 匣子背景Drawable

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.RELAYOUT) {
				relayoutSldingView();
			} else if (msg.what == Constants.ENTER_PREVIEW_MODE) {
				relayoutSldingView();
				AppClassifyManager.enterClassifyPreviewMode(DrawerMainView.this);
			} else if (msg.what == Constants.EXIT_PREVIEW_MODE) {
				exitClassifyPreviewMode();
				relayoutSldingView();
				drawerSlidingView.snapToScreen(0);
				drawerSlidingView.lockData(SettingsPreference.getInstance().isDrawerTabsLock());
			} else if (msg.what == Constants.RESTORE_LAYOUT) {
				relayoutSldingView();
				drawerSlidingView.snapToScreen(0);
			} else if (msg.what == Constants.UPGRADE_FOLDER) {
				if (hasbeenInitDatabase) {
					if (msg.arg1 == Constants.HIDE_UPGRADE_FOLDER) { // 隐藏
						DrawerDataFactory.hideUpgradeFolder(mLauncher);
					} else { // 显示
						DrawerDataFactory.showUpgradeFolder(mLauncher);
					}
					// 重新加载
					allAppsData.getDataList().clear();
					allAppsData.getDataList().addAll(assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true)));
					drawerSlidingView.reLayout();
				}
			}
			super.handleMessage(msg);
		}

		private void relayoutSldingView() {
			drawerSlidingView.clearLayout();
			drawerSlidingView.reLayout();

			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	};

	/** 匣子小部件内存优化：标志是否将小部件中的子view的绘空 */
	public static boolean drawBlankCanvasForWidgetPreviewView = false;

	private class RefreshWidgetsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BeautyUpgradeService.ACTION_REFRESH_WIDGETS.equals(action)) {
				refreshWidgets(true);
			} else if (BeautyUpgradeService.ACTION_REFRESH_WIDGETS_UPDATE_COUNT.equals(action)) {
				refreshWidgetsInfo(LauncherWidgetInfoManager.getInstance().getAllWidgetInfos());
			} else if (DownloadManager.ACTION_DOWNLOAD_STATE.equals(action)) {
				if (checkIsDynamicComplete(intent)) {
					refreshWidgets(true);
				}
			}
		}
	}

	public DrawerMainView(Context context) {
		super(context);
	}

	public DrawerMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawerMainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mFolderEncriptHelper = FolderEncriptHelper.getNewInstance();
		settingsPreference = SettingsPreference.getInstance();
		mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

		/**
		 * 初始化控件
		 */
		normalTopLayoutView = new NormalTopLayoutView();
		bottomLayoutView = new BottomLayoutView();
		editTopLayoutView = new EditTopLayoutView();
		drawerMenu = new DrawerMenu();
		normalTopLayoutView.onFinishInflate();
		bottomLayoutView.onFinishInflate();
		drawerEditZoneController = new DrawerEditZoneController(this);
		drawerSlidingView = (DrawerSlidingView) findViewById(R.id.drawer_sliding_view);

		/**
		 * 设置匣子背景
		 */
		setBackgroundDrawable(null);

		/**
		 * 初始化匣子行列数
		 */
		allAppsAndMyPhoneRowCols = getDrawerTargetRowCols();

		drawerSlidingView.setEndlessScrolling(settingsPreference.isDrawerRollingCycle());
		mLightbar = (LineLightbar) findViewById(R.id.drawer_lightbar);
		mCommonLightbar = (CommonLightbar) findViewById(R.id.drawer_sansum_bar);

		mSamsungDrawerSearchLayout = (SamsungDrawerSearchLayout) findViewById(R.id.samsung_drawer_search_rl);
		if(YinniSansumHelper.isYinniSamsung()){
			mSamsungDrawerSearchLayout.setVisibility(View.VISIBLE);
			mSamsungDrawerSearchLayout.setDrawerMainView(this);
			drawerSlidingView.setCommonLightbar(mCommonLightbar);
			drawerSlidingView.setLineLightbar(mLightbar);
			YinniSansumHelper.hideView(mLightbar);

			mCommonLightbar.setNormalLighter(getResources().getDrawable(R.drawable.launcher_light_normal));
			mCommonLightbar.setSelectedLighter(getResources().getDrawable(R.drawable.launcher_light_selected));
			mCommonLightbar.setGap(ScreenUtil.dip2px(getContext(), 5));
			mCommonLightbar.setGravity(Gravity.CENTER);
			YinniSansumHelper.configDrawerLightBar(findViewById(R.id.drawer_sansum_bar));
			YinniSansumHelper.configDrawerLightBar(findViewById(R.id.drawer_lightbar));
		} else {
			mSamsungDrawerSearchLayout.setVisibility(View.GONE);
			drawerSlidingView.setLineLightbar(mLightbar);
//			mLightbar.setVisibility(View.VISIBLE);
			mCommonLightbar.setVisibility(View.INVISIBLE);
		}

		drawerSlidingView.setList(assembleList());
		drawerSlidingView.setOnSwitchDataListener(this);
		drawerSlidingView.setOnSnapToScreenListener(this);
		drawerSlidingView.setOnItemClickListener(this);
		drawerSlidingView.setOnItemLongClickListener(this);
		drawerSlidingView.setOnFlingListener(this);
		lockTabs(settingsPreference.isDrawerTabsLock());

		restoreViews();
		showDefaultView();
		// 应用匣子皮肤
		applyTheme();
		ThemeUIRefreshAssit.getInstance().registerRefreshListener(this);
		// 根据状态栏是否显示来设置toppadding
		int top = 0;
		if (SettingsPreference.getInstance().isNotificationBarVisible()) {
			top = getResources().getDimensionPixelSize(R.dimen.status_bar_height);
		}
		setPadding(getPaddingLeft(), top, getPaddingRight(), getPaddingBottom());

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BeautyUpgradeService.ACTION_REFRESH_WIDGETS);
		filter.addAction(BeautyUpgradeService.ACTION_REFRESH_WIDGETS_UPDATE_COUNT);
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_STATE);
		if (refreshWidgetsReceiver == null) {
			refreshWidgetsReceiver = new RefreshWidgetsReceiver();
			getContext().registerReceiver(refreshWidgetsReceiver, filter);
		}
		drawBlankCanvasForWidgetPreviewView = false;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (refreshWidgetsReceiver != null) {
			try {
				getContext().unregisterReceiver(refreshWidgetsReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				refreshWidgetsReceiver = null;
			}
		}
	}

	/**
	 * 将Tab恢复默认状态
	 */
	private void restoreViews() {
		normalTopLayoutView.restoreViews();
	}

	private void showDefaultView() {
		normalTopLayoutView.allAppsBtn.setChecked(true);
		drawerSlidingView.snapToData(allAppsData);
	}

	@Override
	public void setLauncher(Launcher launcher) {
		mLauncher = launcher;
		drawerSlidingView.setLauncher(launcher);
		drawerEditZoneController.setLauncher(launcher);
	}

	@Override
	public void setDragController(DragController dragger) {
		mDragController = dragger;
		drawerSlidingView.setDragController(mDragController);
		mDragController.addDragScoller(drawerSlidingView);
		mDragController.addDropTarget(drawerSlidingView);
	}

	public DrawerSlidingView getDrawerSlidingView() {
		return drawerSlidingView;
	}

	@Override
	public void zoom(float zoom, boolean animate) {
		hasbeenOpened = true;
		if (isEditMode()) {
			setEditMode(DrawerSlidingView.NORMAL_MODE, false);
		}
		cancelLongPress();
		mZoom = zoom;
		if (isVisible()) {
			isFirstIntoWidgets = true;
			if (normalTopLayoutView.widgetsBtn.isChecked()) {
//				AppDistributeStatistics.AppDistributePercentConversionStatistics(mContext, "1001");
				isFirstIntoWidgets = false;
			}
			setVisibility(View.VISIBLE);
			animateDrawer(animate, DrawerAnimation.ACTION_SHOW);
		} else {
			animateDrawer(animate, DrawerAnimation.ACTION_HIDE);
		}
	}

	private void animateDrawer(boolean animate, int action) {
		if (animate) {
			AnimationSet set = new AnimationSet(true);
			if (action == DrawerAnimation.ACTION_SHOW) {
				DrawerAnimation ani = new DrawerAnimation(settingsPreference.getDrawerEffectsShowHide(), action);
				DrawerAnimation.lastAnimationType = ani.getAnimationType();
				set.addAnimation(ani);
			} else {
				DrawerAnimation ani = new DrawerAnimation(DrawerAnimation.lastAnimationType, action);
				set.addAnimation(ani);
			}
			set.setDuration(300);
			startAnimation(set);
		} else {
			onAnimationEnd();
		}
	}

	protected void onAnimationEnd() {
		mLauncher.zoomed(mZoom);
		if (!isVisible()) {
			mZoom = 0.0f;
			drawerSlidingView.clearFocusedView();
			setVisibility(View.GONE);
		} else {
			mZoom = 1.0f;
		}
		updateAppsLayout();
	}

	/**
	 * 跳转至最新安装程序页面
	 */
	public boolean snapToLatestAppScreen() {
		DrawerNewInstalledHelper helper = DrawerNewInstalledHelper.getInstance();
		if (!helper.isNeedToFindLatestApp() || allAppsData.getDataList().size() == 0)
			return false;
		String[] latestApp = helper.getLatestInstalledApp();
		if (latestApp != null) {
			String packageName = latestApp[0];
			String className = latestApp[1];
			ApplicationInfo info = new ApplicationInfo();
			info.intent = new Intent();
			info.intent.setComponent(new ComponentName(packageName, className));
			int index = findAppByComponent(allAppsData.getDataList(), info);
			if (index != -1) {
				int screen = index / (allAppsData.getColumnNum() * allAppsData.getRowNum());
				scrollToScreen(screen);
			}
		}
		helper.clearFindLatestApp();
		return true;
	}

	public void scrollToScreen(int whichScreen) {
		drawerSlidingView.scrollToScreen(whichScreen);
		drawerSlidingView.getLineLightbar().update(whichScreen * drawerSlidingView.getPageWidth());
		ICommonData data = drawerSlidingView.getData(whichScreen);
		restoreViews();
		if (data == allAppsData) {
			normalTopLayoutView.allAppsBtn.setChecked(true);
		} else if (data == widgetsData) {
			normalTopLayoutView.widgetsBtn.setChecked(true);
			if (normalTopLayoutView.widgetsUpdateInfo.getVisibility() == View.VISIBLE) {
				normalTopLayoutView.widgetsUpdateInfo.setVisibility(View.INVISIBLE);
			}
		} else if (data == myPhoneData) {
			normalTopLayoutView.myPhoneBtn.setChecked(true);
		}
	}

	@Override
	public boolean isVisible() {
		return mZoom > 0.001f;
	}

	/**
	 * 更新所有程序布局
	 */
	private void updateAppsLayout() {
		if (tempList == null) {
			snapToLatestAppScreen();
			return;
		}
		buildAppsData();
		drawerSlidingView.clearLayout();
		drawerSlidingView.reLayout();
	}

	/**
	 * 组装应用程序数据 <br>
	 * Author:caizp <br>
	 * Date:2012-10-12下午06:18:04
	 */
	public void buildAppsData() {
		/**
		 * 传入的数据需已经根据container与pos排序
		 */
		if (null != tempList) {
			List<ICommonDataItem> allAppsList = allAppsData.getDataList();
			allAppsList.clear();
			allAppsList.addAll(assembleAppList(tempList));
			tempList.clear();
			tempList = null;
		}
	}

	@Override
	public void setApps(List<ApplicationInfo> list) {
		/**
		 * 设置程序列表
		 */
		tempList = new ArrayList<ICommonDataItem>();
		tempList.addAll(list);
		allAppsCount = 0;
		cachedAppsCount = 0;
		allAppsCount = tempList.size();

		// 如果已经初始化数据库，则不再需要回调处理
		hasbeenInitDatabase = ConfigFactory.isInitApps(getContext());

		/**
		 * 每个应用程序图标刷新后的回调方法
		 */
		if (!hasbeenInitDatabase) {
			DrawerCatchedIconCallback cachedIconCallback = new DrawerCatchedIconCallback();
			drawerSlidingView.setAppCatchedIconCallback(cachedIconCallback);
		} else {
//			if (!AppDataFactory.getInstance().isUpgradeFolderExist(mLauncher)) {// 如果不存在应用升级文件夹
//				SettingsPreference.getInstance().setShowUpgradeFolder(true);
//				AppDataFactory.getInstance().updateExistRecordPos(mLauncher);
//				AppDataFactory.getInstance().addUpgradeFolder(mLauncher);
//				initInstallAppInfo();
//			}else{
//				SettingsPreference.getInstance().setIsAllowQuery(true);
//			}
		}

		final List<ApplicationInfo> allapps = new ArrayList<ApplicationInfo>();
		for (ICommonDataItem item : tempList) {
			if (item instanceof ApplicationInfo) {
				allapps.add((ApplicationInfo) item);
			}
		}

		// 未打开匣子时才去刷新应用程序图标
		if (!hasbeenOpened) {
			ThreadUtil.executeDrawer(new Runnable() {
				@Override
				public void run() {
					final IconCache iconCache = (IconCache) Global.getIconCache();
					for (ICommonDataItem item : allapps) {
						if (item instanceof ApplicationInfo
								&& ((ApplicationInfo) item).itemType == BaseLauncherSettings.Favorites.ITEM_TYPE_APPLICATION) {
							iconCache.makeCache((ApplicationInfo) item);
						}
					}
				}
			});
		}

		if (isVisible()) {
			updateAppsLayout();
		}
	}

	@Override
	public void addApps(List<ApplicationInfo> list) {
		if (list == null || list.size() == 0)
			return;
		List<ApplicationInfo> addAppList = filterHiddenApps(list);
		if (addAppList == null || addAppList.size() == 0)
			return;

		allAppsCount += addAppList.size();
		/**
		 * 处理新安装程序标记
		 */
		dealNewInstalledMark();

		/**
		 * 新增程序添加至匣子最后
		 */
		int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
		List<ICommonDataItem> appList = tempList != null ? tempList : allAppsData.getDataList();
		for (ApplicationInfo item : addAppList) {
			item.setPosition(appList.size());
			if (item.id == -1) {
				item.id = DrawerDataFactory.selectAppId(getContext(), item);
			}
			appList.add(item);
		}
		drawerSlidingView.reLayout(allAppsData, oldInfo);
	}

	@Override
	public void removeApps(String packageName) {
		/**
		 * 删除程序
		 */
		int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
		List<ICommonDataItem> appList = tempList != null ? tempList : allAppsData.getDataList();

		if (removeItems(appList, packageName)) {
			drawerSlidingView.reLayout(allAppsData, oldInfo);
		}
	}

	/**
	 * 供删除程序调用
	 * 
	 * @param appList
	 *            - 程序列表
	 * @param itemObj
	 *            - ComponentName对象或程序的packageName
	 */
	private boolean removeItems(List<ICommonDataItem> appList, Object itemObj) {
		boolean isRemoved = false;
		Iterator<ICommonDataItem> it = appList.iterator();
		while (it.hasNext()) {
			ICommonDataItem item = it.next();
			if (item instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) item;
				if (info.intent == null || info.intent.getComponent() == null) {
					Log.e(TAG, info.toString());
					continue;
				}

				if (itemObj instanceof ComponentName) {
					if (info.intent.getComponent().equals(itemObj)) {
						it.remove();
						allAppsCount--;
						if (!isRemoved) {
							isRemoved = true;
						}
					}
				} else if (itemObj instanceof String) {
					if ((info.intent.getComponent() != null && info.intent.getComponent().getPackageName().equals(itemObj))
							|| (info.iconResource != null && itemObj.equals(info.iconResource.resourceName))) {
						it.remove();
						allAppsCount--;
						if (!isRemoved) {
							isRemoved = true;
						}
					}
				}
			} else if (item instanceof FolderInfo) {
				FolderInfo folderInfo = (FolderInfo) item;
				for (int k = folderInfo.contents.size() - 1; k >= 0; k--) {
					ApplicationInfo info = folderInfo.contents.get(k);
					if (info.intent == null || info.intent.getComponent() == null) {
						Log.e(TAG, info.toString());
						continue;
					}

					if (itemObj instanceof ComponentName) {
						if (info.intent.getComponent().equals(itemObj)) {
							folderInfo.contents.remove(info);
							allAppsCount--;
							if (!isRemoved) {
								isRemoved = true;
							}
						}
					} else if (itemObj instanceof String) {
						if ((info.intent.getComponent() != null && info.intent.getComponent().getPackageName().equals(itemObj))
								|| (info.iconResource != null && itemObj.equals(info.iconResource.resourceName))) {
							folderInfo.contents.remove(info);
							allAppsCount--;
							if (!isRemoved) {
								isRemoved = true;
							}
						}
					}
				}

				if (folderInfo.getSize() <= 0 && !(folderInfo instanceof UpgradeFolderInfo)) {
					deleteFolder(appList, folderInfo);
					it.remove();
					break;
				}
			}
		}
		return isRemoved;
	}

	@Override
	public void updateApps(List<ApplicationInfo> list) {
		if (list == null || list.size() == 0)
			return;
		List<ApplicationInfo> updateAppsList = filterHiddenApps(list);
		if (updateAppsList == null || updateAppsList.size() == 0)
			return;

		/**
		 * 更新程序记录, 未变化则不作任何操作, 否则删除程序记录后将新记录加至最后
		 */
		boolean isNeedUpdate = false;
		List<ICommonDataItem> appList = tempList != null ? tempList : allAppsData.getDataList();
		if (getAppsCountForPackageName(appList, updateAppsList.get(0).intent.getComponent().getPackageName()) != updateAppsList.size()) {
			/**
			 * 个数不同
			 */
			isNeedUpdate = true;
		} else {
			/**
			 * 个数相同，则检查入口是否一致
			 */
			for (ApplicationInfo item : updateAppsList) {
				if (!contains(appList, item)) {
					isNeedUpdate = true;
					break;
				}
			}
		}

		if (!isNeedUpdate) {
			final int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
			drawerSlidingView.reLayout(allAppsData, oldInfo);
			return;
		}

		removeApps(updateAppsList.get(0).intent.getComponent().getPackageName());
		addApps(updateAppsList);
	}

	private int findAppByComponent(List<ICommonDataItem> list, ApplicationInfo item) {
		ComponentName component = item.intent.getComponent();
		final int N = list.size();
		for (int i = 0; i < N; i++) {
			ICommonDataItem x = list.get(i);
			if (!(x instanceof ApplicationInfo)) {
				continue;
			}
			ApplicationInfo info = (ApplicationInfo) x;
			if (info.intent.getComponent().equals(component)) {
				return i;
			}
		}
		return -1;
	}

	private boolean contains(List<ICommonDataItem> list, ApplicationInfo app) {
		for (ICommonDataItem item : list) {
			if (item instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) item;
				if (info.intent == null || info.intent.getComponent() == null) {
					Log.e(TAG, info.toString());
					continue;
				}

				if (info.intent.getComponent().equals(app.intent.getComponent())) {
					return true;
				}
			} else if (item instanceof FolderInfo) {
				FolderInfo folderInfo = (FolderInfo) item;
				for (int j = folderInfo.contents.size() - 1; j >= 0; j--) {
					ApplicationInfo info = folderInfo.contents.get(j);
					if (info.intent == null || info.intent.getComponent() == null) {
						Log.e(TAG, info.toString());
						continue;
					}

					if (info.intent.getComponent().equals(app.intent.getComponent())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private int getAppsCountForPackageName(List<ICommonDataItem> list, String packageName) {
		int count = 0;
		if (list == null || packageName == null) {
			return count;
		}
		for (ICommonDataItem item : list) {
			if (item instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) item;
				if (info.intent == null || info.intent.getComponent() == null) {
					Log.e(TAG, info.toString());
					continue;
				}

				if (packageName.equals(info.intent.getComponent().getPackageName())) {
					count++;
				}
			} else if (item instanceof FolderInfo) {
				FolderInfo folderInfo = (FolderInfo) item;
				for (int j = folderInfo.contents.size() - 1; j >= 0; j--) {
					ApplicationInfo info = folderInfo.contents.get(j);
					if (info.intent == null || info.intent.getComponent() == null) {
						Log.e(TAG, info.toString());
						continue;
					}

					if (packageName.equals(info.intent.getComponent().getPackageName())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 刷新小部件数据集列表
	 */
	public void refreshWidgets(boolean isClear) {
		if (Global.isBaiduLauncher() || widgetsData == null) {
			return;
		}
		CommonSlidingViewData viewData = (CommonSlidingViewData) widgetsData;
		viewData.setRowNum(3);
		viewData.setColumnNum(2);
		int[] widgetsOldInfo = drawerSlidingView.getDataPageInfo(widgetsData);
		if (isClear)
			LauncherWidgetInfoManager.getInstance().clearAllLauncherItemInfos();
		List<ICommonDataItem> widgetsList = widgetsData.getDataList();
		widgetsList.clear();
		widgetsList.addAll(loadAllWidgetsData());
		drawerSlidingView.setScrollToCurrentScreen(true);
		drawerSlidingView.reLayout(widgetsData, widgetsOldInfo);
		if (normalTopLayoutView.widgetsBtn.isChecked()) {
			lazyLoadWidget();
		}
	}

	private void lazyLoadWidget() {
		if (Global.isBaiduLauncher()) {
			return;
		}
		int currentS = drawerSlidingView.getDataPageInfo(widgetsData)[0];
		DrawerLayout layout = (DrawerLayout) drawerSlidingView.getChildAt(currentS);
		if (null == layout || layout.getChildCount() == 0) {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lazyLoadWidget();
				}
			}, 500);
			return;
		}
		for (int i = 0; i < layout.getChildCount(); i++) {
			View view = layout.getChildAt(i);
			if (view instanceof WidgetPreviewView) {
				WidgetPreviewView widgetView = (WidgetPreviewView) view;
				widgetView.isLazyLoad = false;
				widgetView.postInvalidate();
			}
		}
	}

	/**
	 * 组装数据集列表
	 */
	private List<ICommonData> assembleList() {
		drawerList.clear();
		drawerList.add(assembleAppsData());
		if (!Global.isBaiduLauncher()) {
			if(!YinniSansumHelper.isYinniSamsung()){
				drawerList.add(assembleLazyWidgetsData());
			}
		}
		if (isShowMyphone()) {
			if(!YinniSansumHelper.isYinniSamsung()){
				drawerList.add(assembleMyPhoneData());
			}
		}
		return drawerList;
	}

	/**
	 * 组装应用程序数据集
	 */
	private ICommonData assembleAppsData() {
		if (allAppsData != null) {
			return allAppsData;
		}

		int iconSize = IconAndTextSizeConfig.getSmallIconSize(getContext());
		allAppsData = new DraggerSlidingViewData((int) (iconSize * 1.68f), iconSize * 2, allAppsAndMyPhoneRowCols[1], allAppsAndMyPhoneRowCols[0],
				new ArrayList<ICommonDataItem>());
		allAppsData.setTag(Constants.TAB_APP);
		allAppsData.setAcceptDrop(true);
		allAppsData.setKeepChildViewWidthAndHeight(false);
		return allAppsData;
	}

	/**
	 * 组装小部件数据集
	 */
	private ICommonData assembleLazyWidgetsData() {
		if (widgetsData != null) {
			return widgetsData;
		}

		List<ICommonDataItem> itemList = new ArrayList<ICommonDataItem>();
		if (!Global.isBaiduLauncher()) {
			itemList.add(LauncherWidgetInfo.makeDownloadMoreWidgetInfo());
		}

		widgetsData = new DraggerSlidingViewData(ScreenUtil.dip2px(mLauncher, 320), ScreenUtil.dip2px(mLauncher, 320), 1, 1, itemList);
		widgetsData.setTag(Constants.TAB_WIDGET);
		return widgetsData;
	}

	/**
	 * 组装我的手机数据集
	 */
	private ICommonData assembleMyPhoneData() {
		// 安装桌面，概率出现pandahome2文件夹没有创建
		Global.createDefaultDir();
		if (myPhoneData != null) {
			return myPhoneData;
		}

		List<ICommonDataItem> itemList = new ArrayList<ICommonDataItem>();
		itemList.addAll(MyPhoneDataFactory.getInfosListV6(getContext()));

		int iconSize = IconAndTextSizeConfig.getSmallIconSize(getContext());
		myPhoneData = new DraggerSlidingViewData((int) (iconSize * 1.68f), iconSize * 2, allAppsAndMyPhoneRowCols[1], allAppsAndMyPhoneRowCols[0],
				itemList);
		myPhoneData.setTag(Constants.TAB_MY_PHONE);
		myPhoneData.setKeepChildViewWidthAndHeight(false);
		return myPhoneData;
	}

	private List<ICommonDataItem> loadAllWidgetsData() {
		// 安装桌面，概率出现pandahome2文件夹没有创建
		Global.createDefaultDir();
		ArrayList<ICommonDataItem> widgetList = new ArrayList<ICommonDataItem>();
		widgetList.addAll(LauncherWidgetInfoManager.getInstance().getAllWidgetInfos());
		refreshWidgetsInfo(widgetList);
		return widgetList;
	}

	/**
	 * 刷新小部件 tab信息 优先显示新widget标识 无新添加标识则显示更新数量
	 */
	private void refreshWidgetsInfo(ArrayList<? extends ICommonDataItem> widgetList) {
		int newWidgetCount = LauncherWidgetNewAddedHelper.getInstance().getNewWidgetCount();

		/**
		 * 小部件更新数量提示
		 */
		try {
			ArrayList<String> packageNames = new ArrayList<String>();
			for (ICommonDataItem item : widgetList) {
				if (!(item instanceof LauncherWidgetInfo))
					continue;
				LauncherWidgetInfo info = (LauncherWidgetInfo) item;

				if (info.type != LauncherWidgetInfo.TYPE_OUTSIDE)
					continue;
				if (info.hasNewVersion && !packageNames.contains(info.packageName)) {
					packageNames.add(info.packageName);
				}
			}

			/**
			 * 小部件升级提示信息
			 */
			final DrawerPreference helper = DrawerPreference.getInstance();
			if (packageNames.size() > 0) {
				boolean hasNewWidgetPackage;
				for (String packageName : packageNames) {
					hasNewWidgetPackage = helper.isNewWidgetPackage(packageName);
					if (hasNewWidgetPackage) {
						break;
					}
				}
				helper.clearWidgetUpgradePackages();
				helper.addWidgetPackages(packageNames);
			} else {
				helper.clearWidgetUpgradePackages();
			}
			showWidgetUpdateAndNewCount(packageNames.size() + newWidgetCount);
			Intent intent = new Intent(WidgetPreviewView.ACTION_REFRESH_WIDGETS_UPDATE_VIEW);
			getContext().sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示小部件可更新数量+new的数量
	 * 
	 * @param count
	 *            int
	 */
	private void showWidgetUpdateAndNewCount(int count) {
		if (count <= 0) {
			normalTopLayoutView.widgetsUpdateInfo.setVisibility(INVISIBLE);
		} else {
			normalTopLayoutView.widgetsUpdateInfo.setVisibility(VISIBLE);
			normalTopLayoutView.widgetsUpdateInfo.setText(String.valueOf(count));
		}
	}

	/**
	 * 匣子是否处于编辑模式
	 */
	public boolean isEditMode() {
		return drawerSlidingView.isEditMode();
	}

	/**
	 * 匣子是否处于智能分类预览模式
	 */
	public boolean isClassifyPreviewMode() {
		return drawerSlidingView.isOnAppClassifyPreviewMode();
	}

	/**
	 * 
	 * 匣子进入/退出编辑模式统一入口
	 * 
	 * @author wangguomei 2014.04.14
	 * 
	 * @param editModeAt
	 *            编辑模式的类型： 1.DrawerSlidingView.APPS_EDIT_MODE 应用程序编辑模式
	 *            2.DrawerSlidingView.WIDGET_EDIT_MODE 小部件编辑模式
	 *            3.DrawerSlidingView.MYPHONE_EDIT_MODE 我的手机编辑模式
	 *            4.DrawerSlidingView.NORMAL_MODE 退出编辑模式
	 * 
	 */
	public void setEditMode(int editModeAt, boolean isAnimateForEditZone) {

		boolean isEditMode = editModeAt != DrawerSlidingView.NORMAL_MODE;

		if (isEditMode) {
			drawerSlidingView.setEditModeAt(editModeAt);
		} else {
			if (mLauncher.getFolderCotroller().isFolderOpened() && !(mLauncher.getFolderCotroller().getFolderInfo() instanceof UpgradeFolderInfo)) {
				mLauncher.getFolderCotroller().setBottomLayoutVisibility(View.VISIBLE);
			}
		}

		if (isOnAppsEditMode()) {
			setAppsEditMode(isEditMode, isAnimateForEditZone);
		} else if (isOnWidgetEditMode()) {
			setWidgetEditMode(isEditMode, isAnimateForEditZone);
		} else if (isOnMyPhoneEditMode()) {
			setMyPhoneEditMode(isEditMode, isAnimateForEditZone);
		}

		if (!isEditMode) {
			drawerSlidingView.setEditModeAt(editModeAt);
		}
	}

	/**
	 * 设置应用程序编辑模式
	 */
	private void setAppsEditMode(final boolean isEditMode, final boolean isAnimateForMoveZone) {

		editTopLayoutView.setupEditToolsLayout();

		if (isEditMode) {
			/**
			 * 设为编辑模式
			 */
			bottomLayoutView.hideToolsLayout();
			drawerSlidingView.lockData(true);
			if (isAnimateForMoveZone) {
				bottomLayoutView.toolsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_exit));
			}

			drawerEditZoneController.showDrawerEditZone();

			// 编辑模式新手引导
			mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_DRAWER_EDIT_MODE);

		} else {
			/**
			 * 设为非编辑模式
			 */
			bottomLayoutView.showToolsLayout();
			if (!settingsPreference.isDrawerTabsLock()) {
				drawerSlidingView.lockData(false);
			}
			if (isAnimateForMoveZone) {
				bottomLayoutView.toolsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_enter));
			}

			drawerEditZoneController.closeDrawerEditZone();

			// 退出编辑模式新手引导
			mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_DRAWER_NORMAL_MODE);
		}
		drawerSlidingView.setEditMode(isEditMode, drawerSlidingView.getDataPageInfo(allAppsData));

		mLauncher.setupFolder();
		mFolderOpenController.setEditMode(isEditMode);
	}

	/**
	 * 设置小部件编辑模式
	 */
	private void setWidgetEditMode(final boolean isEditMode, final boolean isAnimateForMoveZone) {
		if (isEditMode) {
			bottomLayoutView.hideToolsLayout();
			drawerSlidingView.lockData(true);
			if (isAnimateForMoveZone) {
				bottomLayoutView.toolsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_exit));
			}
			drawerEditZoneController.showDrawerEditZone();
		} else {
			bottomLayoutView.showToolsLayout();
			if (!settingsPreference.isDrawerTabsLock()) {
				drawerSlidingView.lockData(false);
			}
			if (isAnimateForMoveZone) {
				bottomLayoutView.toolsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_enter));
			}
			drawerEditZoneController.closeDrawerEditZone();
		}
	}

	/**
	 * 设置我的的手机编辑模式
	 */
	private void setMyPhoneEditMode(final boolean isEditMode, final boolean isAnimateForMoveZone) {
		setWidgetEditMode(isEditMode, isAnimateForMoveZone);
		drawerSlidingView.setEditMode(isEditMode, drawerSlidingView.getDataPageInfo(myPhoneData));
	}

	public ViewGroup getEditToolsLayout() {
		return editTopLayoutView.editToolsLayout;
	}

	/**
	 * 锁定tab
	 * 
	 * @param isLock
	 *            boolean
	 */
	public void lockTabs(boolean isLock) {
		allAppsData.setLock(isLock);
		if (widgetsData != null) {
			widgetsData.setLock(isLock);
		}
		if (myPhoneData!=null && isShowMyphone()) {
			myPhoneData.setLock(isLock);
		}
		drawerSlidingView.lockData(isLock);
	}

	/**
	 * 初始化搜索界面
	 */
	private void setupSearchView(Context context) {
		if (searchView != null)
			return;

		searchView = (SearchView) LayoutInflater.from(context).inflate(R.layout.searchbox_main, null);
		this.addView(searchView);
		searchView.setActivity(mLauncher);
		drawerSlidingView.setSearchView(searchView);
		searchView.setPositioningListener(this);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		searchView.setLayoutParams(lp);
	}

	public void snapToData(String tab) {
		if (Constants.TAB_APP.equals(tab)) {
			restoreViews();
			if (isEditMode()) {
				setEditMode(DrawerSlidingView.NORMAL_MODE, true);
			}
			normalTopLayoutView.allAppsBtn.setChecked(true);
			if (hasbeenOpened) {
				drawerSlidingView.snapToData(allAppsData);
			} else {
				drawerSlidingView.setDestScreen(drawerSlidingView.getDataPageInfo(allAppsData)[0]);
				drawerSlidingView.setScrollToCurrentScreen(true);
			}
		} else if (Constants.TAB_WIDGET.equals(tab)) {

			int currentS = drawerSlidingView.getDataPageInfo(widgetsData)[0];
			if (drawerSlidingView.getCurrentScreen() == drawerSlidingView.getDataPageInfo(widgetsData)[0]) {
				currentS = drawerSlidingView.getDataPageInfo(widgetsData)[1] - 1;
			}

			DrawerLayout layout = (DrawerLayout) drawerSlidingView.getChildAt(currentS);
			if (layout != null) {
				for (int i = 0; i < layout.getChildCount(); i++) {
					View view = layout.getChildAt(i);
					if (view instanceof WidgetPreviewView) {
						((WidgetPreviewView) view).isLazyLoad = false;
						view.postInvalidate();
					}
				}
			}

			restoreViews();
			if (isEditMode()) {
				setEditMode(DrawerSlidingView.NORMAL_MODE, true);
			}
			normalTopLayoutView.widgetsBtn.setChecked(true);
			if (normalTopLayoutView.widgetsUpdateInfo.getVisibility() == View.VISIBLE) {
				normalTopLayoutView.widgetsUpdateInfo.setVisibility(View.INVISIBLE);
			}
			if (hasbeenOpened) {
				drawerSlidingView.snapToData(widgetsData);
			} else {
				drawerSlidingView.setDestScreen(drawerSlidingView.getDataPageInfo(widgetsData)[0]);
				drawerSlidingView.setScrollToCurrentScreen(true);
			}

			if (widgetsData.getDataList().size() <= 1) {
				/**
				 * 小部件数据集若无数据则组装后刷新
				 */
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						refreshWidgets(true);
					}
				}, 300);
			}

		} else if (Constants.TAB_MY_PHONE.equals(tab)) {
			restoreViews();
			if (isEditMode()) {
				setEditMode(DrawerSlidingView.NORMAL_MODE, true);
			}
			normalTopLayoutView.myPhoneBtn.setChecked(true);
			if (hasbeenOpened) {
				drawerSlidingView.snapToData(myPhoneData);
			} else {
				drawerSlidingView.setDestScreen(drawerSlidingView.getDataPageInfo(myPhoneData)[0]);
				drawerSlidingView.setScrollToCurrentScreen(true);
			}
		}
	}

	@Override
	public void onSwitchData(List<ICommonData> list, int fromPosition, int toPosition) {
		/**
		 * 切换数据集
		 */
		restoreViews();
		ICommonData data = list.get(toPosition);
		if (data == allAppsData) {
			normalTopLayoutView.allAppsBtn.setChecked(true);
		} else if (data == widgetsData) {
			normalTopLayoutView.widgetsBtn.setChecked(true);
			if (normalTopLayoutView.widgetsUpdateInfo.getVisibility() == View.VISIBLE) {
				normalTopLayoutView.widgetsUpdateInfo.setVisibility(View.INVISIBLE);
			}
			// 打开匣子第一次切换到小插件的时候打点
			if (isFirstIntoWidgets) {
//				AppDistributeStatistics.AppDistributePercentConversionStatistics(mLauncher, "1001");
				isFirstIntoWidgets = false;
			}
		} else if (data == myPhoneData) {
			normalTopLayoutView.myPhoneBtn.setChecked(true);
		}
	}

	@Override
	public void onSnapToScreen(List<ICommonData> list, int fromScreen, int toScreen) {
		ICommonData data = drawerSlidingView.getData(toScreen);
		if (data == allAppsData && !normalTopLayoutView.allAppsBtn.isChecked()) {
			restoreViews();
			normalTopLayoutView.allAppsBtn.setChecked(true);
		} else if (data == widgetsData) {

			restoreCanvasForWidgetPreviewView();

			DrawerLayout layout = (DrawerLayout) drawerSlidingView.getChildAt(toScreen);
			for (int i = 0; i < layout.getChildCount(); i++) {
				View view = layout.getChildAt(i);
				if (view instanceof WidgetPreviewView) {
					((WidgetPreviewView) view).isLazyLoad = false;
					view.postInvalidate();
				}
			}
			if (!normalTopLayoutView.widgetsBtn.isChecked()) {
				restoreViews();
				normalTopLayoutView.widgetsBtn.setChecked(true);
				if (normalTopLayoutView.widgetsUpdateInfo.getVisibility() == View.VISIBLE) {
					normalTopLayoutView.widgetsUpdateInfo.setVisibility(View.INVISIBLE);
				}
			}

			if (widgetsData.getDataList().size() <= 1) {
				/**
				 * 小部件数据集若无数据则组装后刷新
				 */
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						refreshWidgets(true);
					}
				}, 300);
			}

		} else if (data == myPhoneData && !normalTopLayoutView.myPhoneBtn.isChecked()) {
			restoreViews();
			normalTopLayoutView.myPhoneBtn.setChecked(true);
		}

		if (data == allAppsData) {
			mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_IN_DRAWER_ALL_APP);
		} else if (data == widgetsData) {
			mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_OUT_DRAWER_ALL_APP);
		} else if (data == myPhoneData) {
			mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_OUT_DRAWER_ALL_APP);
		}
	}

	@Override
	public boolean onItemLongClick(View v, int positionInData, int positionInScreen, int screen, ICommonData data) {

		if (isClassifyPreviewMode() || !Global.allowEdit(getContext()) || isDemonstratingEffect() ||
		drawerSlidingView.getTouchState() != 0) {// 不响应长按事件
			return true;
		}

		if (Constants.TAB_APP.equals(data.getTag())) {
			return appTabOnItemLongClick(v, positionInData, positionInScreen, data);
		} else if (Constants.TAB_WIDGET.equals(data.getTag())) {
			return widgetTabOnItemLongClick(v, positionInData, positionInScreen, data);
		} else {
			myphoneTabOnItemLongClick(v, positionInData, positionInScreen, data);
		}
		return true;
	}

	private void myphoneTabOnItemLongClick(View v, int positionInData, int positionInScreen, ICommonData data) {
		ICommonDataItem item = data.getDataList().get(positionInData);
		ApplicationInfo app = (ApplicationInfo) item;
		if (drawerSlidingView.getDraggerChooseItem(app) != null) {
			ArrayList<DraggerChooseItem> list = drawerSlidingView.getDraggerChooseList();
			for (DraggerChooseItem cItem : list) {
				ApplicationInfo aInfo = cItem.getInfo();
				if (aInfo == app) {
					list.remove(cItem);
					list.add(0, cItem);
					break;
				}
			}
			drawerSlidingView.startDrag(v, positionInData, positionInScreen, item, list);
		} else {
			drawerSlidingView.startDrag(v, positionInData, positionInScreen, data.getDataList().get(positionInData));
		}

		if (!isEditMode()) {
			mLauncher.setupFolder();
			setEditMode(DrawerSlidingView.MYPHONE_EDIT_MODE, true);
		}
	}

	private boolean widgetTabOnItemLongClick(View v, int positionInData, int positionInScreen, ICommonData data) {
		/**
		 * 小部件数据集需判断是否已安装，且是否可拖拽
		 */
		final LauncherItemInfo info = (LauncherItemInfo) data.getDataList().get(positionInData);
		if (info == null) {
			return true;
		}

		if (info.catagoryNo == LauncherItemInfo.CATAGORY_MORE_WIDGET) {
			/**
			 * 系统小部件
			 */
			if (mLauncher != null) {
				LauncherAnimationHelp.AddBlurWallpaper(Global.getLauncher());
				mLauncher.showLauncherAddMainView(LauncherAddMainView.WIDGET_SYS_CATEGORY_MODE, LauncherAddMainView.FROM_DRAWER);
			}

			return true;
		} else if (info.catagoryNo == LauncherItemInfo.CATAGORY_MORE_DOWNLOAD) {
//			if (PromptServiceManager.isShopShowNew()) {
//				PromptServiceManager.setShopShowNew(false);
//			}
			/**
			 * 下载更多
			 */
			Intent intent = new Intent();
			intent.setClass(getContext(), WidgetShopActivity.class);
			SystemUtil.startActivitySafely(mLauncher, intent);
			mVibrator.vibrate(Constants.VIBRATE_DURATION);
			return true;
		}
		// 在线插件
		if (info instanceof LauncherOnLineWidgetInfo) {
			LauncherOnLineWidgetInfo onLineWidgetInfo = (LauncherOnLineWidgetInfo) info;
			intoWidgetInfoView(onLineWidgetInfo);
			return true;
		} else if (info instanceof LauncherWidgetInfo) {
			/**
			 * 判断小部件是否已安装
			 */
			final LauncherWidgetInfo widgetInfo = (LauncherWidgetInfo) info;
			LauncherWidgetNewAddedHelper.getInstance().removeWidgetNewFlag(widgetInfo);
			if (LauncherWidgetInfoManager.DEBUG) {
				if (DynamicWidgetManager.getClient(Global.DOWNLOAD_DIR + widgetInfo.packageName + ".apk") != null) {
					mLauncher.getWorkspace().setDragInfoByWidgetFromDrawer(widgetInfo);
					drawerSlidingView.startDrag(v, positionInData, positionInScreen, data.getDataList().get(positionInData));
					mLauncher.enterWorkspaceFromDrawer(2);
					return true;
				}
			}

			if (LauncherWidgetInfo.isNormalInstalledWidget(widgetInfo)) {
				if (!AndroidPackageUtils.isPkgInstalled(getContext(), widgetInfo.packageName)) {
					mVibrator.vibrate(Constants.VIBRATE_DURATION);
					Toast.makeText(getContext(), R.string.drawer_not_found_widget_tips, Toast.LENGTH_SHORT).show();
					return true;
				}
			} else if (LauncherWidgetInfo.isDynamicWidget(widgetInfo)) {
				if (LauncherWidgetInfoManager.isDynamicWidgetReady(this.getContext(), widgetInfo.packageName))
					handlerClick(v, widgetInfo, false);
			}

			mLauncher.getWorkspace().setDragInfoByWidgetFromDrawer(widgetInfo);

		}
		drawerSlidingView.startDrag(v, positionInData, positionInScreen, data.getDataList().get(positionInData));
		mLauncher.enterSpringModeFromDrawer(2);
		return false;
	}

	private boolean appTabOnItemLongClick(View v, int positionInData, int positionInScreen, ICommonData data) {
		/**
		 * 查询程序是否已完成初始化
		 */
		if (!hasbeenInitDatabase) {
			Toast.makeText(getContext(), R.string.drawer_apps_not_init_tips, Toast.LENGTH_SHORT).show();
			return true;
		}

		/**
		 * Drop后是否已完成重新布局 且 DragView是否已完成动画
		 */
		if (drawerSlidingView.isNeedRelayoutAfterDrop() || drawerSlidingView.isInAnimation())
			return true;

		// 如果出现下标越界 则返回
		if (positionInData >= data.getDataList().size())
			return true;


		ICommonDataItem item = data.getDataList().get(positionInData);
		if (item instanceof ApplicationInfo) {
			ApplicationInfo app = (ApplicationInfo) item;
			DrawerNewInstalledHelper helper = DrawerNewInstalledHelper.getInstance();
			if (helper.isNewInstalledApp(app.intent.getComponent().getPackageName(), app.intent.getComponent().getClassName())) {
				/**
				 * 移除新安装程序标记
				 */
				helper.removeNewInstalledApp(app.intent.getComponent().getPackageName(), app.intent.getComponent().getClassName());
				if (v instanceof EditableIconView) {
					((EditableIconView) v).setIsNewInstall(false);
				}
			}

			if (drawerSlidingView.getDraggerChooseItem(app) != null) {
				/**
				 * 拖拽多项
				 */
				ArrayList<DraggerChooseItem> list = drawerSlidingView.getDraggerChooseList();
				for (DraggerChooseItem cItem : list) {
					ApplicationInfo aInfo = cItem.getInfo();
					if (aInfo == app) {
						list.remove(cItem);
						list.add(0, cItem);
						break;
					}
				}
				drawerSlidingView.startDrag(v, positionInData, positionInScreen, item, list);
				editTopLayoutView.showAppDetailsZone.setModel(ShowAppDetailsZone.Model.FOLDER);
			} else {
				/**
				 * 拖拽单项
				 */
				if(YinniSansumHelper.isYinniSamsung()){
					drawerSlidingView.startDrag(v, positionInData, positionInScreen, item);
					mLauncher.enterSpringModeFromDrawer(1);
				} else {
					drawerSlidingView.startDrag(v, positionInData, positionInScreen, item);
				}
			}
		} else {
			/**
			 * 拖拽文件夹
			 */
			// 如果是拖拽应用升级文件夹复制一份 并改成正常的文件夹
			if (item instanceof UpgradeFolderInfo) {
				item = ((UpgradeFolderInfo) item).copy();
				((FolderInfo) item).itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;
			}
			drawerSlidingView.startDrag(v, positionInData, positionInScreen, item);
		}
		v.clearAnimation();
		/**
		 * 开启编辑模式
		 */
//		if (!isEditMode()) {
//			mLauncher.setupFolder();
//			setEditMode(DrawerSlidingView.APPS_EDIT_MODE, true);
//
//		}
		return false;
	}

	@Override
	public void onItemClick(final View v, int positionInData, int positionInScreen, int screen, final ICommonData data) {
		if (isDemonstratingEffect()) {
			return;
		}
		if (positionInData >= data.getDataList().size())
			return;
		drawerSlidingView.setClickItem(((CommonViewHolder) v.getTag(R.id.common_view_holder)).item);

		if (Constants.TAB_APP.equals(data.getTag())) {
			ICommonDataItem item = data.getDataList().get(positionInData);
			// 转成可编辑图标对象
			EditableIconView iconView = (EditableIconView) v;

			if (isClassifyPreviewMode()) {// 当处于预览模式时不响应单击事件
				return;
			}

			if (!item.isFolder()) {
				final ApplicationInfo app = (ApplicationInfo) item;

				DrawerNewInstalledHelper helper = DrawerNewInstalledHelper.getInstance();
				if (helper.isNewInstalledApp(app.intent.getComponent().getPackageName(), app.intent.getComponent().getClassName())) {
					/**
					 * 移除新安装程序标记
					 */
					helper.removeNewInstalledApp(app.intent.getComponent().getPackageName(), app.intent.getComponent().getClassName());
					iconView.setIsNewInstall(false);
				}

				if (!isEditMode()) {
					/**
					 * 非编辑状态下点击应用程序
					 */
					if (app.itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT) {
						ActivityActionUtil.startActivitySafelyForRecoredOnlyUseInDrawer(v, mLauncher, app.intent);
						return;
					}
					if (DefaultLauncherUtil.isProperPhone() && app.componentName.getPackageName().equals(Global.PKG_NAME)) {
						if (DefaultLauncherUtil.popupSetDefaultLauncherFromDrawer(mLauncher)) {
							return;
						}
					}

					if (app != null && app.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION && HiLauncherEXUtil.isUseMIUINotification()
							&& app.componentName != null && app.componentName.getPackageName() != null) {
						Intent broadcastIntent = new Intent(NotificationAppIconType.NOTIFICATION_MESSAGE_CANCEL_BROADCAST);
						broadcastIntent.putExtra("packageName", app.componentName.getPackageName());
						getContext().sendBroadcast(broadcastIntent);
					}

					Intent intent = BrowserUtil.getInstance().interceptLauncherApp(app);
					ActivityActionUtil.startActivitySafelyForRecoredOnlyUseInDrawer(v, mLauncher, intent);
					
					ActivityActionUtil.statCPCEventForDrawer(getContext(), app);
					// 移除91助手更新提示标记
					// if ( ThirdPackageUtil.is91Asistant(app) ) {
					// ThirdPackageUtil.clearUpdatableAppNum(getContext());
					// }
				} else {
					// 编辑模式下，且点中的是打叉的位置，下卸应用
					if (iconView.isTouchUpInEditFlagRect()) {
						final Context context = getContext();
						if (AndroidPackageUtils.isPkgInstalled(context, app.componentName.getPackageName())) {
							drawerSlidingView.unInstallApp(data.getDataList(), app, data);
						} else {

							/**
							 * 程序不存在, 直接删除记录
							 */
							Dialog dialog = ViewFactory.getAlertDialog(context, context.getText(R.string.drawer_delete_not_found_app_tips_title),
									context.getText(R.string.drawer_delete_not_found_app_tips),

									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();

											if (progressDialog == null) {
												progressDialog = new ProgressDialog(context);
												progressDialog.setMessage(context.getText(R.string.drawer_delete_dialog_message));
												progressDialog.setCancelable(false);
											} else {
												progressDialog.setMessage(context.getText(R.string.drawer_delete_dialog_message));
											}

											progressDialog.show();
											ThreadUtil.executeDrawer(new Runnable() {

												@Override
												public void run() {
													final int[] oldInfo = drawerSlidingView.getDataPageInfo(data);
													List<ApplicationInfo> listAppInfos = new ArrayList<ApplicationInfo>();
													listAppInfos.add(app);
													DrawerDataFactory.deleteApps(context, listAppInfos);
													handler.post(new Runnable() {

														@Override
														public void run() {
															if (app.iconResource != null && app.iconResource.resourceName != null) {
																removeApps(app.iconResource.resourceName);
																WorkspaceHelper.removeAppsInWorkspaceByIconResource(app.iconResource.resourceName);
															} else if (app.componentName != null) {
																removeApps(app.componentName.getPackageName());
															}
															if (progressDialog != null && progressDialog.isShowing()) {
																progressDialog.dismiss();
															}
															drawerSlidingView.reLayout(data, oldInfo);
														}
													});
												} // end run
											});

										}// end onClick

									});// end DialogInterface.OnClickListener

							dialog.show();

							// HiAnalytics.submitEvent(getContext(),
							// AnalyticsConstant.DRAWER_ICON_LONGPRESS_UNINSTALL);

						}// end if

					} else if (v instanceof AppDrawerIconMaskTextView) {// 根据bug邮件，v
																		// 有可能是FolderIconTextView
						/**
						 * 选择程序
						 */
						boolean isSuccess = drawerSlidingView.chooseItem(data, (AppDrawerIconMaskTextView) v, app);
						if (!isSuccess) {
							/**
							 * 多选项个数已达到最大限制
							 */
							Toast.makeText(getContext(), R.string.drawer_multi_choose_reached_limit_tips, Toast.LENGTH_SHORT).show();
						}
					}

				}// end if

			} else {
				if (!isVisible()) {// 防止退出匣子时打开文件夹异常
					return;
				}
				mLauncher.setupFolder();
				if (isEditMode()) {
					/**
					 * 编辑模式下，且点中的是编辑图标的位置，删除文件夹
					 */
					if (iconView.isTouchUpInEditFlagRect()) {
						// todo 如果是应用升级文件夹 隐藏
						if (((FolderInfo) item).itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER) {
							drawerSlidingView.removeUpgradeFolder((FolderInfo) item, data);
						} else if (((FolderInfo) item).itemType == LauncherSettings.Favorites.ITEM_TYPE_MYPHONE_FOLDER) {
							drawerSlidingView.removeMyphoneFolder((FolderInfo) item, data);
						} else {
							drawerSlidingView.removeFolder((FolderInfo) item, data);
						}
					} else {
						if (drawerSlidingView.getDraggerChooseList().size() == 0) {
							/**
							 * 打开文件夹
							 */
							handleFolderClick(v, (FolderInfo) item);
							// 如果是应用升级文件夹 打开时推出编辑状态
							if (item instanceof UpgradeFolderInfo) {
								setEditMode(DrawerSlidingView.NORMAL_MODE, true);
							}
						} else if (drawerSlidingView.getDraggerChooseList().size() > 0 &&
								                     (v instanceof FolderIconTextView) &&
								                 !((FolderIconTextView) v).isDisable()) {
							/**
							 * 文件夹不可打开，但绘画状态不正确，则重绘文件夹
							 */
							drawerSlidingView.setFolderDisable(true);
							showOnlyToast(getContext().getString(R.string.drawer_multi_choose_folder_unavailable_tips));
						} else {
							showOnlyToast(getContext().getString(R.string.drawer_multi_choose_folder_unavailable_tips));
						}
					}
				} else {
					/**
					 * 打开文件夹
					 */
					handleFolderClick(v, (FolderInfo) item);
				}
			}
		} else if (Constants.TAB_WIDGET.equals(data.getTag())) {
			LauncherItemInfo info = (LauncherItemInfo) data.getDataList().get(positionInData);
			if (info == null) {
				return;
			}
			if (info instanceof LauncherWidgetInfo) {
				final LauncherWidgetInfo widgetInfo = (LauncherWidgetInfo) info;
				if (widgetInfo.catagoryNo == LauncherItemInfo.CATAGORY_MORE_WIDGET) {
					// 编辑被锁定 系统小部件不允许点击（点击进入编辑模式）
					if (!Global.allowEdit(getContext())) {
						return;
					}

					if (mLauncher != null) {
						LauncherAnimationHelp.AddBlurWallpaper(Global.getLauncher());
						mLauncher.showLauncherAddMainView(LauncherAddMainView.WIDGET_SYS_CATEGORY_MODE, LauncherAddMainView.FROM_DRAWER);
					}

				} else if (widgetInfo.catagoryNo == LauncherItemInfo.CATAGORY_MORE_DOWNLOAD) {
//					if (PromptServiceManager.isShopShowNew()) {
//						PromptServiceManager.setShopShowNew(false);
//					}
					/**
					 * 下载更多
					 */
					Intent intent = new Intent();
					intent.setClass(getContext(), WidgetShopActivity.class);
					SystemUtil.startActivitySafely(mLauncher, intent);
				} else {
					if (info instanceof LauncherOnLineWidgetInfo) {
						LauncherOnLineWidgetInfo onLineWidgetInfo = (LauncherOnLineWidgetInfo) info;
						intoWidgetInfoView(onLineWidgetInfo);
					} else if (info instanceof LauncherWidgetInfo) {
						// 如果有新版本则直接跳转到插件商城界面, 不进行下一步操作
						if (widgetInfo.hasNewVersion) {
							Intent intent = new Intent();
							intent.setClass(getContext(), WidgetShopActivity.class);
							intent.putExtra(WidgetShopActivity.CONTAINER_INDEX, WidgetShopActivity.LOCAL);
							SystemUtil.startActivitySafely(mLauncher, intent);
							return;
						}
						if (LauncherWidgetInfo.isNormalInstalledWidget(widgetInfo)) {
							if (!AndroidPackageUtils.isPkgInstalled(getContext(), widgetInfo.packageName)) {
								mVibrator.vibrate(Constants.VIBRATE_DURATION);
								Toast.makeText(getContext(), R.string.drawer_not_found_widget_tips, Toast.LENGTH_SHORT).show();
								return;
							}
						} else if (LauncherWidgetInfo.isDynamicWidget(widgetInfo)) {
							if (!LauncherWidgetInfoManager.isDynamicWidgetReady(this.getContext(), widgetInfo.packageName)) {
								handlerClick(v, widgetInfo, true);
								return;
							}
						}
						LauncherWidgetNewAddedHelper.getInstance().removeWidgetNewFlag(widgetInfo);
						showOnlyToast(getContext().getString(R.string.drawer_widget_click_tips));
					}
				}
			}
		} else if (Constants.TAB_MY_PHONE.equals(data.getTag())) {

			ApplicationInfo app = (ApplicationInfo) data.getDataList().get(positionInData);
			if (!isEditMode()) {
				ActivityActionUtil.startActivitySafelyForRecored(mLauncher, app.intent);
				/**
				 * add by linqiang(866116) 单击后去掉我的手机新功能(new)标志
				 */
				String key = app.componentName.getClassName();
				if (ConfigPreferences.getInstance().isShowMyPhoneNew(key)) {
					ConfigPreferences.getInstance().alreadyShowMyPhoneNew(key);
					((EditableIconView) v).setIsNewFunction(false);
				}
				
				mLauncher.getTagViewController().setCallStateIfNecessary(app);
			} else {
				boolean isSuccess = drawerSlidingView.chooseItem(data, (AppDrawerIconMaskTextView) v, app);
				if (!isSuccess) {
					showOnlyToast(getContext().getString(R.string.drawer_multi_choose_reached_limit_tips));
				}
			}
		}
	}

	private void handleFolderClick(View view, FolderInfo folderInfo) {
		mClickFolderView = view;
		if (isClassifyPreviewMode()) {// 如果处于预览状态 以只读模式打开
			mFolderOpenController.setClickFolder(view, folderInfo);
			mFolderOpenController.openFolder(FolderSwitchController.OPEN_FOLDER_FROM_DRAWER, false, false);
		} else {
			String encript = FolderEncriptHelper.getNewInstance().getFolderEncript(folderInfo, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
			if (encript == null) {
				boolean editMode = isEditMode();
				mFolderOpenController.setClickFolder(view, folderInfo);
				if (folderInfo.itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER) {
					// 应用升级文件夹 以只读模式打开
					mFolderOpenController.openFolder(FolderSwitchController.OPEN_FOLDER_FROM_DRAWER, false, false);
					HiAnalytics.submitEvent(mLauncher, AnalyticsConstant.DRAWER_UPDATE_CLICK_FOLDER);
				} else {
					mFolderOpenController.openFolder(FolderSwitchController.OPEN_FOLDER_FROM_DRAWER, editMode);
				}
			} else {
				mUserFolderInfo = folderInfo;
				FolderViewFactory.startEntranceActivity(mLauncher, folderInfo, LauncherActivityResultHelper.REQUEST_FOLDER_OPEN,
						FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
			}
		}
	}

	public void handleFolderClickDirectly(int delayMillis) {
		if (mClickFolderView != null && mUserFolderInfo != null) {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					boolean editMode = isEditMode();
					mFolderOpenController.setClickFolder(mClickFolderView, mUserFolderInfo);
					mFolderOpenController.openFolder(FolderSwitchController.OPEN_FOLDER_FROM_DRAWER, editMode);
				}
			}, delayMillis);
		}
	}

	@Override
	public void onFlingUp() {
		// toggleSeachView();
	}

	@Override
	public void onFlingDown(int y) {
		LauncherActionHelper.getInstance().actionDownInDrawer(mLauncher);
	}

	@Override
	public boolean onKeyDownProcess(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isVisible()) {
				if (mLauncher.isLauncherAddMainViewVisible()) {
					return false;
				}
				if (mLauncher.isTopMenuShow()) {
					return false;
				}
				if (isClassifyPreviewMode() && (mFolderOpenController == null || !mFolderOpenController.isFolderOpened())) { // 处于分类预览模式时且文件夹关闭
																																// 屏蔽back事件
					return true;
				} else if (searchView != null && searchView.isVisible()) { // 焦点在searchbox中时
					return searchView.onKeyDownProcess(keyCode, event);
				} else if (isEditMode()) {
					setEditMode(DrawerSlidingView.NORMAL_MODE, true);
				} else if (mFolderOpenController != null && mFolderOpenController.isFolderOpened()) {
					mFolderOpenController.closeFolder();
				} else if (drawerMenu.menuPopupWindow != null && drawerMenu.menuPopupWindow.isShowing()) {
					drawerMenu.menuPopupWindow.dismiss();
				} else {
					mLauncher.closeAllApps(true);
				}
				return true;
			}
		} else if (searchView != null && keyCode == KeyEvent.KEYCODE_SEARCH && isVisible()) {
			return searchView.onKeyDownProcess(keyCode, event);
		}
		return false;
	}

	/**
	 * 是否处于app tab页
	 * 
	 * @return
	 */
	public boolean isOnAppTab() {
		return drawerSlidingView.getCurrentData() == allAppsData;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/**
		 * 匣子菜单处于打开状态则关闭
		 */
		if (drawerMenu.menuPopupWindow != null && drawerMenu.menuPopupWindow.isShowing()) {
			drawerMenu.menuPopupWindow.dismiss();
			return true;
		}
		/**
		 * 文件夹处于打开状态则关闭
		 */
		if (mLauncher.isFolderOpened()) {
			mLauncher.getFolderCotroller().closeFolder();
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void setBackgroundDrawable(Drawable d) {
		drawerBgDrawable = d;
		if (mLauncher == null) {
			mLauncher = Global.getLauncher();
		}
		if (mLauncher.isTranslucentNavigationBar()) {// 导航栏透明，改写匣子背景设置
			return;
		}
		if (d == null) {
			super.setBackgroundColor(getDefaultDrawerBgColor());
		} else {
			super.setBackgroundDrawable(d);
		}
	}

	public int getDefaultDrawerBgColor() {
		int color = Color.parseColor(Constants.BACKGROUND_COLOR);
		return ColorUtil.argbColorAlpha(color, Constants.BACKGROUND_ALPHA);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);

		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);

			if ((v == searchView) && visibility == View.VISIBLE)
				continue;

			if (v == normalTopLayoutView.topLayout && drawerEditZoneController.isEditable())
				continue;
			if (v == normalTopLayoutView.topLayout && !isShowTopLayout()) {
				continue;
			}
			// 预览模式下不显示头部和底部
			if ((v == normalTopLayoutView.topLayout || v == bottomLayoutView.bottomLayout) && isClassifyPreviewMode()) {
				continue;
			}

			v.setVisibility(visibility);
		}

		if (visibility == View.VISIBLE) {
			if (mLauncher != null && mLauncher.getWorkspace() != null) {
				mLauncher.getWorkspace().setState(DropTarget.UNAVAIABLE);
			}
			if (isClassifyPreviewMode()) {
				if (classifyPreviewBottomLayout != null) {
					classifyPreviewBottomLayout.setVisibility(View.VISIBLE);
				}
			}
			if (Global.getLauncher() != null && Global.getLauncher().topMenuDrawstringView != null){
				Global.getLauncher().topMenuDrawstringView.hideDrawstring();
			}
		} else {
			if (mLauncher != null && mLauncher.getWorkspace() != null) {
				mLauncher.getWorkspace().setState(DropTarget.AVAIABLE);
			}
			if (drawerMenu.menuPopupWindow != null && drawerMenu.menuPopupWindow.isShowing()) {
				drawerMenu.menuPopupWindow.dismiss();
			}
			if (iconSortTypeDialog != null && iconSortTypeDialog.isShowing()) {
				iconSortTypeDialog.dismiss();
			}
			if (isClassifyPreviewMode()) {
				if (classifyPreviewBottomLayout != null) {
					classifyPreviewBottomLayout.setVisibility(View.GONE);
				}
			}
			if (Global.getLauncher() != null && Global.getLauncher().topMenuDrawstringView != null){
				Global.getLauncher().topMenuDrawstringView.showDrawstring();
			}
		}
	}

	/**
	 * @param mFolderOpenController
	 *            the mFolderOpenController to set
	 */
	public void setFolderOpenController(FolderSwitchController mFolderOpenController) {
		this.mFolderOpenController = mFolderOpenController;
		this.drawerEditZoneController.setFolderOpenController(mFolderOpenController);
	}

	@Override
	public void onViewLongClick(View v) {
//		if (!isEditMode()) {
//			setEditMode(DrawerSlidingView.APPS_EDIT_MODE, false);
//		}
	}

	@Override
	public void onViewClick(View v) {

	}

	@Override
	public void onDragOut(FolderInfo folder, ArrayList<Object> items) {
		// do nothing
	}

	@Override
	public boolean onFlingOut(FolderInfo folderInfo, ArrayList<Object> items) {
		FolderInfo folder = (FolderInfo) folderInfo;
		boolean isIngoreTopItem = false;
		if (folder.contents.size() == items.size() && folder.contents.size() > 1) {
			isIngoreTopItem = true;
		}
		for (int i = items.size() - 1; i >= 0; i--) {
			if (isIngoreTopItem && i == 0) {
				break;
			}
			Object item = items.get(i);
			folder.remove((ApplicationInfo) item);
			boolean isReLayout = (i == 0) || (isIngoreTopItem && i == 1) ? true : false;
			addAppFromFolder(FolderSwitchController.FOLDER_DRAG_TYPE_FLING, folder, item, isReLayout);
		}
		return true;
	}

	@Override
	public void onBeforeDragOut(FolderInfo folderInfo, ArrayList<Object> items) {
		FolderInfo folder = (FolderInfo) folderInfo;
		if (items != null && items.size() > 1) {
			drawerEditZoneController.restoreDrawerTopEditToolZone();
			/**
			 * 拖拽多选项至匣子
			 */
			editTopLayoutView.showAppDetailsZone.setModel(ShowAppDetailsZone.Model.FOLDER);
			drawerSlidingView.clearDraggerChooseList();
			for (int i = 0; i < items.size(); i++) {
				drawerSlidingView.chooseItem(allAppsData, null, (ApplicationInfo) items.get(i));
			}
		}

		boolean isIngoreTopItem = false;
		if (folder.contents.size() == items.size() && folder.contents.size() > 1) {
			isIngoreTopItem = true;
		}
		for (int i = items.size() - 1; i >= 0; i--) {
			if (isIngoreTopItem && i == 0) {
				break;
			}
			Object item = items.get(i);
			folder.remove((ApplicationInfo) item);
			boolean isReLayout = (i == 0) || (isIngoreTopItem && i == 1) ? true : false;
			addAppFromFolder(FolderSwitchController.FOLDER_DRAG_TYPE_DRAG_OUT, folder, item, isReLayout);
		}
	}

	private void addAppFromFolder(int dragType, final FolderInfo folder, Object item, boolean isReLayout) {
		if (item == null || !(item instanceof ApplicationInfo))
			return;
		final ApplicationInfo addOne = (ApplicationInfo) item;
		/**
		 * 从文件夹拖拽出的app放置在文件夹之后一个位置
		 * 
		 * @todo 优化添加策略
		 */
		List<ICommonDataItem> appList = allAppsData.getDataList();

		// 计算添加app的位置
		int newPos = folder.pos + 1;
		addOne.setPosition(newPos);
		addOne.drawerContainer = ApplicationInfo.CONTAINER_DRAWER;
		appList.add(newPos, addOne);

		if (folder.getSize() <= 1) {
			if (folder.getSize() == 1) {
				ApplicationInfo lastOne = folder.contents.get(0);
				lastOne.setPosition(folder.pos);
				lastOne.drawerContainer = ApplicationInfo.CONTAINER_DRAWER;
				appList.add(folder.pos, lastOne);

			}
			appList.remove(folder);
			ThreadUtil.executeDrawer(new Runnable() {
				@Override
				public void run() {
					DrawerDataFactory.updateAppPositionCascade(getContext(), addOne);
					DrawerDataFactory.deleteFolder(getContext(), folder);
					FolderEncriptHelper.getNewInstance().clearEncript(folder.id, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
				}
			});
		} else {
			ThreadUtil.executeDrawer(new Runnable() {
				@Override
				public void run() {
					DrawerDataFactory.updateAppPositionCascade(getContext(), addOne);
				}
			});
		}
		if (isReLayout) {
			drawerSlidingView.reLayoutForFolder(dragType);
		}
	}

	public ICommonData getAllAppData() {
		return allAppsData;
	}

	public ICommonData getMyPhoneData() {
		return assembleMyPhoneData();
	}

	@Override
	public List<ICommonData> getDataList() {
		return drawerList;
	}

	@Override
	public void poistioning(PositionInfo positionInfo) {
		if (hasbeenOpened) {
			positionItem(positionInfo);
		} else {
			drawerSlidingView.setDestPositionInfo(positionInfo);
		}
	}

	/**
	 * 定位方法
	 * 
	 * @param positionInfo
	 */
	private void positionItem(PositionInfo positionInfo) {
		/**
		 * 定位搜索出的应用
		 */

		/**
		 * 隐藏键盘
		 */
		InputMethodManager mInputMethodManager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
		mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);

		drawerSlidingView.clearFocusedView();

		Object item = positionInfo.getItem();
		ICommonData data = positionInfo.getDataSet();
		List<ICommonDataItem> dataList = data.getDataList();

		boolean isFound = false;
		FolderInfo folderInfo = null;
		int screen = 0;
		int folderIndex = 0;
		int index = 0;
		for (int i = 0; i < dataList.size(); i++) {
			ItemInfo info = (ItemInfo) dataList.get(i);
			if (item instanceof ApplicationInfo) {
				// 过滤应用升级文件夹
				if (info instanceof UpgradeFolderInfo)
					continue;
				if (info instanceof FolderInfo) {
					/**
					 * 匹配文件夹中的程序
					 */
					FolderInfo userFolderInfo = ((FolderInfo) info);
					for (int j = 0; j < userFolderInfo.contents.size(); j++) {
						ItemInfo info2 = userFolderInfo.contents.get(j);
						if (info2.equals(item)) {
							isFound = true;
							folderInfo = userFolderInfo;
							folderIndex = i;
							index = j;
							int[] pageInfo = drawerSlidingView.getDataPageInfo(data);
							screen = pageInfo[0] + folderIndex / (data.getRowNum() * data.getColumnNum());
							break;
						}
					}
				} else {
					/**
					 * 匹配非文件夹中程序（包括我的手机中的程序图标）
					 */
					if (info.equals(item)) {
						isFound = true;
						index = i;
						int[] pageInfo = drawerSlidingView.getDataPageInfo(data);
						screen = pageInfo[0] + index / (data.getRowNum() * data.getColumnNum());
						break;
					}
					
					//处理适配,找不到天猫精选的话就拿包名找
					if (info instanceof ApplicationInfo
							&& (TelephoneUtil.getMachineName().equals("SM-J7008") || TelephoneUtil.getMachineName().equals("SM-J5008"))
							&& ((ApplicationInfo) info).componentName.getPackageName()
									.equals(((ApplicationInfo) item).componentName.getPackageName())
							&& ((ApplicationInfo) info).componentName.getPackageName().equals(DrawerDataFactory.CUSTOM_AITAOBAO_PCK_NAME)) {
						isFound = true;
						index = i;
						int[] pageInfo = drawerSlidingView.getDataPageInfo(data);
						screen = pageInfo[0] + index / (data.getRowNum() * data.getColumnNum());
						break;
					}
				}
			} else {
				/**
				 * 匹配其他
				 */
				if (info == item) {
					isFound = true;
					index = i;
					int[] pageInfo = drawerSlidingView.getDataPageInfo(data);
					screen = pageInfo[0] + index / (data.getRowNum() * data.getColumnNum());
					break;
				}
			}
		}

		if (isFound) {
			if (searchView != null)
				searchView.hideWithoutAnim();

			DraggerLayout layout = (DraggerLayout) drawerSlidingView.getChildAt(screen);
			if (folderInfo == null) {
				/**
				 * 非文件夹中的程序
				 */
				ICommonData mData = drawerSlidingView.getCurrentData();
				boolean isLock = mData.isLock();
				drawerSlidingView.lockData(false);
				drawerSlidingView.snapToScreen(screen);
				mData.setLock(isLock);
				View v = layout.getChildAt(index % (data.getRowNum() * data.getColumnNum()));
				drawerSlidingView.setFocusedView(v);
			} else {
				/**
				 * 文件夹中的程序
				 */
				scrollToScreen(screen);
				View v = layout.getChildAt(folderIndex % (data.getRowNum() * data.getColumnNum()));
				drawerSlidingView.setClickItem(((CommonViewHolder) v.getTag(R.id.common_view_holder)).item);
				mClickFolderView = v;
				if (folderInfo.isEncript) { // 加密只高亮文件夹
					drawerSlidingView.setFocusedView(v);
				} else {
					mLauncher.setupFolder();

					mFolderOpenController.setClickFolder(v, folderInfo, index);

					getHandler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mFolderOpenController.getFolderView() != null && mFolderOpenController.getFolderView().getFolderSlidingView() != null) {
								mFolderOpenController.getFolderView().getFolderSlidingView().setNeedReLoadContent(true);
							}
							mFolderOpenController.openFolder(FolderSwitchController.OPEN_FOLDER_FROM_DRAWER, isEditMode());
						}
					}, 500);
				}
			}
		} else {
			/**
			 * 未找到匹配项
			 */
			Toast.makeText(getContext(), R.string.drawer_not_found_focused_item_tips, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 更新匣子行列数
	 */
	public void updateDrawerRowCols() {
		((CommonSlidingViewData) allAppsData).setRowNum(allAppsAndMyPhoneRowCols[0]);
		((CommonSlidingViewData) allAppsData).setColumnNum(allAppsAndMyPhoneRowCols[1]);
		if (myPhoneData != null) {
			((CommonSlidingViewData) myPhoneData).setRowNum(allAppsAndMyPhoneRowCols[0]);
			((CommonSlidingViewData) myPhoneData).setColumnNum(allAppsAndMyPhoneRowCols[1]);
		}
		drawerSlidingView.clearLayout();
		drawerSlidingView.reLayout();
	}

	/**
	 * 获取匣子行列数设置
	 * 
	 * @return
	 */
	public int[] getDrawerTargetRowCols() {
		int[] rowCols = new int[2];
		int drawerCountXY = settingsPreference.getDrawerCountXY();
		switch (drawerCountXY) {
		case 0:
			rowCols[0] = 4;
			rowCols[1] = 4;
			break;
		case 1:
			rowCols[0] = 5;
			rowCols[1] = 4;
			break;
		case 2:
			rowCols[0] = 5;
			rowCols[1] = 5;
			break;
		default:
			rowCols[0] = 4;
			rowCols[1] = 4;
			break;
		}
		if(YinniSansumHelper.isYinniSamsung()){
			rowCols[0] = 5;
			rowCols[1] = 4;
		}
		return rowCols;
	}

	public void setDrawerRowCols(int rowCols[]) {
		this.allAppsAndMyPhoneRowCols = rowCols;
	}

	/**
	 * 隐藏或显示搜索视图
	 */
	public void toggleSeachView() {
		if (searchView == null)
			return;

		if (searchView.isVisible()) {
			searchView.hide();
		} else if (!isEditMode() && searchViewInvisiable()) {
			searchView.show();
			AppDistributeStatistics.AppDistributePercentConversionStatistics(getContext(), "601");
		}
	}

	@Override
	public void onAppAdded2Folder(final FolderInfo folderInfo, ArrayList<SerializableAppInfo> list) {

		final ArrayList<ICommonDataItem> appList = (ArrayList<ICommonDataItem>) allAppsData.getDataList();
		if (list == null || list.size() == 0) {
			/**
			 * 删除掉该文件夹
			 */
			deleteFolder(appList, (FolderInfo) folderInfo);
			appList.remove(folderInfo);
			if (folderInfo != null) {
				folderInfo.clear(); // hjiang，清空folderInfo的数据，后续FolderSwitchController才会将其关闭
			}
		} else {

			/**
			 * 添加的逻辑是模拟先删除掉该文件夹，在添加一个文件夹的过程
			 */
			int folderIndex = appList.indexOf(folderInfo);

			if (folderIndex == -1)
				return;
			/**
			 * 查找匣子中对应的文件夹
			 */
			final FolderInfo theFolder = (FolderInfo) appList.get(folderIndex);

			final List<ApplicationInfo> contents = theFolder.contents;
			// 批量添加的app列表
			final ArrayList<SerializableAppInfo> batchAddList = list;
			// 迭代文件夹中的app
			Iterator<ApplicationInfo> iterator = contents.iterator();
			while (iterator.hasNext()) {
				ApplicationInfo app = iterator.next();
				SerializableAppInfo seriInfo = new SerializableAppInfo(app);
				// 如果文件夹中已存在该app，则从批量添加列表removedList中移除
				if (batchAddList.contains(seriInfo)) {
					batchAddList.remove(seriInfo);
					continue;
				}
				// 如果该app不在removedList中，将其从文件夹中移除
				app.drawerContainer = ItemInfo.CONTAINER_DRAWER;
				iterator.remove();
				appList.add(++folderIndex, app);
			}

			// 将定制类型icon的componentName转换，以便匹配
			/*
			 * final ArrayList<ICommonDataItem> compList =
			 * (ArrayList<ICommonDataItem>) appList.clone(); for
			 * (ICommonDataItem item : compList) { if (item instanceof
			 * ApplicationInfo && ((ApplicationInfo) item).itemType ==
			 * LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT) {
			 * ApplicationInfo info = (ApplicationInfo) item; info.componentName
			 * = info.intent.getComponent(); } }
			 */
			for (SerializableAppInfo seriInfo : batchAddList) {
				final int index = appList.lastIndexOf(new ApplicationInfo(seriInfo));
				if (index != -1) {
					final ApplicationInfo app = (ApplicationInfo) appList.get(index);
					appList.remove(app);
					contents.add(app);
				}
			}
			int index = 0;
			for (ApplicationInfo info : contents) {
				info.pos = index++;
				info.drawerContainer = theFolder.id;
			}

			index = 0;
			for (ICommonDataItem item : appList) {
				item.setPosition(index++);
				if (item instanceof FolderInfo) {
					((FolderInfo) item).drawerContainer = ItemInfo.CONTAINER_DRAWER;
				} else if (item instanceof ApplicationInfo) {
					((ApplicationInfo) item).drawerContainer = ItemInfo.CONTAINER_DRAWER;
				}
			}

			/**
			 * 生成新的文件夹
			 */

			// 更新db
			ThreadUtil.executeDrawer(new Runnable() {
				@Override
				public void run() {
					DrawerDataFactory.updateAppInfosContainerAndPos(getContext(), appList);
				}
			});

		}

		drawerSlidingView.reLayoutForFolder(FolderSwitchController.FOLDER_ADD_APPS_BATCH);
	}

	private void deleteFolder(List<ICommonDataItem> appList, final FolderInfo folder) {
		DrawerDataFactory.deleteFolder(getContext(), folder);
		mFolderEncriptHelper.clearEncript(folder.id, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
		int folderIndex = appList.indexOf(folder);
		List<ApplicationInfo> itemList = folder.contents;
		for (ApplicationInfo appInfo : itemList) {
			appList.add(++folderIndex, appInfo);
		}
	}

	@Override
	public void onDrop(View target, FolderInfo folder, ArrayList<Object> items) {

	}

	/**
	 * 处理新安装程序标记
	 */
	private void dealNewInstalledMark() {
		if (isVisible()) {
			/**
			 * 匣子处于开启状态下，下次开启匣子时不需要跳转到最新安装程序所在页
			 */
			DrawerNewInstalledHelper.getInstance().clearFindLatestApp();
			/**
			 * 匣子处于开启状态下，DockBar不需要显示新安装程序标记
			 */
			DrawerNewInstalledHelper.getInstance().clearShowNewMarkInDock();
		}
	}

	public VoiceRecognitionListener getVoiceRecognitionListener() {
		setupSearchView(mLauncher);
		return this.searchView;
	}

	/**
	 * 组装应用程序数据
	 * 
	 * @param list
	 * @return
	 */
	public List<ICommonDataItem> assembleAppList(List<? extends ICommonDataItem> list) {
		List<ICommonDataItem> appList = new ArrayList<ICommonDataItem>();
		HashMap<Long, FolderInfo> map = new HashMap<Long, FolderInfo>();
		for (ICommonDataItem item : list) {
			ApplicationInfo info = (ApplicationInfo) item;
			if (info.isHidden == 1)
				continue;
			if (info.drawerContainer == ItemInfo.CONTAINER_DRAWER) {
				boolean isMyPhoneFolder = info.itemType == LauncherSettings.Favorites.ITEM_TYPE_MYPHONE_FOLDER;
				if (info.isFolder() || info.itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER || isMyPhoneFolder) {
					FolderInfo folderInfo;
					if (info.isFolder()) {
						folderInfo = new FolderInfo();
					} else {
						folderInfo = new UpgradeFolderInfo();
					}

					if (isMyPhoneFolder) {
						folderInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_MYPHONE_FOLDER;
					}
					if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER)
						folderInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER;
					folderInfo.id = info.id;
					folderInfo.title = info.title;
					folderInfo.drawerContainer = info.drawerContainer;
					folderInfo.pos = info.pos;
					if (mFolderEncriptHelper.isFolderEncript(info.id, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER)) {
						folderInfo.isEncript = true;
					}
					appList.add(folderInfo);
					map.put(info.id, folderInfo);

					if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER) {
						initUpgradeFolderContent(getContext(), folderInfo);
					}
					if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_MYPHONE_FOLDER) {
						List<ApplicationInfo> myphoneList = MyPhoneDataFactory.getInfosListV6(getContext());
						folderInfo.contents.addAll(myphoneList);
					}
				} else {
					appList.add(info);
				}
			} else {
				FolderInfo folderInfo = map.get(info.drawerContainer);
				if (folderInfo != null) {
					folderInfo.contents.add(info);
				}
			}
		}

		/**
		 * 若文件夹为空，或者文件夹中的程序都已被隐藏且不是应用升级文件夹，则文件夹不显示
		 */
		Set<Long> keys = map.keySet();
		for (Long key : keys) {
			FolderInfo folderInfo = map.get(key);
			if (folderInfo != null && folderInfo.contents.size() == 0 && !(folderInfo instanceof UpgradeFolderInfo)) {
				appList.remove(folderInfo);
			}
		}

		return appList;
	}

	public static void initUpgradeFolderContent(Context context, FolderInfo folderInfo) {
		List<UpdateAppInfo> updateInfos = AppDataFactory.getInstance().loadUpdateAppInfoFromDB(context);
		if (updateInfos == null)
			return;
		long savePatchSize = 0;

		for (UpdateAppInfo updateAppInfo : updateInfos) {
			try {
				// 过滤掉已更新的应用
				try {
					if (TelephoneUtil.getVersionCode(context, updateAppInfo.getPkg()) >= updateAppInfo.getVersionCode()) {
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				folderInfo.contents.add(UpdateAppInfo.toApplicationInfo(updateAppInfo));
			} catch (Exception e) {
				if (updateAppInfo != null)
					HiAnalytics.submitEvent(context, AnalyticsConstant.DRAWER_UPDATE_CLICK_UNFIND_CLASSNAME, updateAppInfo.toString());
				e.printStackTrace();
			}
			savePatchSize = savePatchSize + updateAppInfo.getPatchSize();
		}
		if (savePatchSize == 0 || savePatchSize < 0) {
			((UpgradeFolderInfo) folderInfo).setSavePatchSize("");
		} else {
			if (savePatchSize < 1024) {
				String patchSize = savePatchSize + "KB";
				((UpgradeFolderInfo) folderInfo).setSavePatchSize(patchSize);
				return;
			}
			float size = savePatchSize / 1024f;
			if (size < 1024) {
				String patchSize = String.format("%.2f", size * 10f / 10f) + "MB";
				((UpgradeFolderInfo) folderInfo).setSavePatchSize(patchSize);
				return;
			}
			size = savePatchSize / (1024 * 1024f);
			((UpgradeFolderInfo) folderInfo).setSavePatchSize(String.format("%.2f", size * 10f / 10f) + "GB");
		}
	}

	/**
	 * 过滤隐藏的程序
	 * 
	 * @param list
	 */
	private List<ApplicationInfo> filterHiddenApps(List<ApplicationInfo> list) {
		List<ApplicationInfo> appsList = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo applicationInfo : list) {
			if (!DrawerDataFactory.isAppHidden(getContext(), applicationInfo)) {
				appsList.add(applicationInfo);
			}
		}
		return appsList;
	}

	/**
	 * 隐藏程序的回调函数
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void updateHiddenApps(Intent data) {
		final int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
		List<SerializableAppInfo> results = (List<SerializableAppInfo>) data.getSerializableExtra(AppChooseDialogActivity.RESULT);
		List<ApplicationInfo> hiddenApps = new ArrayList<ApplicationInfo>();
		for (SerializableAppInfo sInfo : results) {
			hiddenApps.add(new ApplicationInfo(sInfo));
		}
		if(SettingsPreference.getInstance().isShowUpgradeFolder()) {
			DrawerDataFactory.clearAppInfosHidden(getContext());
		}else{
			DrawerDataFactory.clearAppInfosHiddenExceptUpgradeFolder(getContext());
		}

		DrawerDataFactory.hideAppInfos(getContext(), hiddenApps);
		List<ICommonDataItem> dataList = allAppsData.getDataList();
		List<ICommonDataItem> list = assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true));
		dataList.clear();
		dataList.addAll(list);
		drawerSlidingView.reLayout(allAppsData, oldInfo);
	}

	public void clearHiddenApps() {
		DrawerDataFactory.clearAppInfosHidden(getContext());
		List<ICommonDataItem> dataList = allAppsData.getDataList();
		List<ICommonDataItem> list = assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true));
		dataList.clear();
		dataList.addAll(list);
		drawerSlidingView.reLayout();
	}

	/**
	 * 新建文件夹
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void createFolder(Intent data) {
		final int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
		List<ICommonDataItem> dataList = allAppsData.getDataList();

		/**
		 * 组装返回的选中列表
		 */
		String folderTitle = data.getStringExtra(AppChooseDialogActivity.TITLE);
		List<SerializableAppInfo> results = (List<SerializableAppInfo>) data.getSerializableExtra(AppChooseDialogActivity.RESULT);
		List<ApplicationInfo> selectedApps = new ArrayList<ApplicationInfo>();
		for (int i = 0; i < results.size(); i++) {
			ApplicationInfo info = new ApplicationInfo(results.get(i));
			selectedApps.add(info);
		}

		/**
		 * 新建文件夹对象
		 */
		int index = 0;
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.title = StringUtil.isEmpty(folderTitle) ? getContext().getText(R.string.folder_name) : folderTitle;
		folderInfo.setPosition(index++);
		folderInfo.drawerContainer = ItemInfo.CONTAINER_DRAWER;

		/**
		 * 组装数据列表
		 */
		List<ICommonDataItem> destList = new ArrayList<ICommonDataItem>();
		destList.add(folderInfo);
		for (ICommonDataItem item : dataList) {
			if (item instanceof FolderInfo) {
				item.setPosition(index++);
				destList.add(item);
			} else if (item instanceof ApplicationInfo) {
				if (selectedApps.contains(item)) {
					item.setPosition(folderInfo.contents.size());
					folderInfo.add((ApplicationInfo) item);
				} else {
					item.setPosition(index++);
					destList.add(item);
				}
			}
		}

		/**
		 * 创建文件夹记录
		 */
		long id = DrawerDataFactory.createFolder(getContext(), folderInfo);
		if (id == -1) {
			Toast.makeText(getContext(), R.string.drawer_new_folder_failed_tips, Toast.LENGTH_SHORT).show();
			return;
		}

		/**
		 * 更新位置信息
		 */
		DrawerDataFactory.updateAppInfosPosition(getContext(), destList);

		List<ICommonDataItem> list = assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true));
		dataList.clear();
		dataList.addAll(list);
		drawerSlidingView.setDestScreen(0);
		drawerSlidingView.reLayout(allAppsData, oldInfo);
	}

	/**
	 * <br>
	 * Description:应用主题皮肤 <br>
	 * Author:caizp <br>
	 * Date:2012-7-19上午10:47:06
	 * 
	 * @see com.nd.hilauncherdev.theme.assit.ThemeUIRefreshListener#applyTheme()
	 */
	@Override
	public void applyTheme() {
		Drawable d = null;
		// 匣子背景
		if (!SettingsPreference.getInstance().getDrawerBgTransparent()) {
			setBackgroundDrawable(ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.DRAWER));
		} else {
			setBackgroundDrawable(null);
		}

		normalTopLayoutView.applyTheme();
		bottomLayoutView.applyTheme();
	}

	/**
	 * 由于图标刷新线程池为单线程，因此暂时不考虑同步问题 <br>
	 * Author:ryan <br>
	 * Date:2012-7-27下午02:58:35
	 */
	class DrawerCatchedIconCallback implements AppCatchedIconCallback {
		@Override
		public void cachedIcon() {
			if (hasbeenInitDatabase) {
				return;
			}

			cachedAppsCount++;
			if (cachedAppsCount >= allAppsCount) {
				// app根据名称排序
				final List<ApplicationInfo> allapps = new ArrayList<ApplicationInfo>();

				//隐藏图标
				final String hideApps = SettingsPreference.getInstance().getHideAppPkgNames();
				ArrayList<String> hideAppList = new ArrayList<String>();
				if(!TextUtils.isEmpty(hideApps)){
					String[] hideAppsArray = hideApps.split(";");
					for(String compName : hideAppsArray){
						if(!TextUtils.isEmpty(compName)){
							hideAppList.add(compName);
						}
					}
				}

				for (ICommonDataItem item : allAppsData.getDataList()) {
					if (!(item instanceof ApplicationInfo))
						continue;

					ApplicationInfo appInfo = (ApplicationInfo) item;
					if (appInfo.componentName != null && appInfo.componentName.getPackageName() != null && hideAppList.size() > 0 &&
							(hideAppList.contains(appInfo.componentName.getPackageName())
								|| hideAppList.contains(appInfo.componentName.getPackageName() +"/" + appInfo.componentName.getClassName())))
						continue;

					allapps.add(appInfo);
				}
//				addCustomShortcutToListTop(allapps);
				Collections.sort(allapps, DrawerSortHelper.APP_TITLE_COMPARATOR);
				//匣子图标顺序
				List<ComponentName> drawerConfCnList = LauncherProviderHelper.genDrawerConfApps(mLauncher);
				ArrayList<ApplicationInfo> drawerConfAppList = new ArrayList<ApplicationInfo>();
				for (ComponentName key : drawerConfCnList) {
					ApplicationInfo match = new ApplicationInfo();
					match.componentName = key;
					if(allapps.contains(match)){
						int indext = allapps.indexOf(match);
						drawerConfAppList.add(allapps.get(indext));
					}
				}
//				for(ApplicationInfo app:allapps){
//					if(app!=null && app.componentName!=null){
//						LogUtils.e(app.componentName.getPackageName()+"/"+app.componentName.getClassName());
//					}
//				}
				allapps.removeAll(drawerConfAppList);
				int folderNums =addCustomFolders(allapps);
				allapps.addAll(0,drawerConfAppList);
				int i = folderNums;
				for (ApplicationInfo info : allapps) {
					info.pos = i++;
					// info.id = i;
				}

				ConfigFactory.maybeSaveApps(getContext(), new ConfigCallback() {
					public boolean onAction() {
						boolean result = AppDataFactory.getInstance().saveApptoDB(getContext(), allapps);
						hasbeenInitDatabase = result;
						return result;
					}
				});

				allAppsData.getDataList().clear();
				allAppsData.getDataList().addAll(assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true)));
				drawerSlidingView.reLayout();

//				if (!AppDataFactory.getInstance().isUpgradeFolderExist(mLauncher)) {// 如果不存在应用升级文件夹
//					SettingsPreference.getInstance().setShowUpgradeFolder(true);
//					AppDataFactory.getInstance().updateExistRecordPos(mLauncher);
//					AppDataFactory.getInstance().addUpgradeFolder(mLauncher);
//					initInstallAppInfo();
//				}

			}
		}
    }

	private int addCustomFolders(List<ApplicationInfo> allapps){
		if(allapps == null || allapps.size() == 0){
			return 0;
		}
		ArrayList<CustomDrawerFolderBean> mDrawerFolderList = CustomRecommendAppWrapper.mDrawerFolderList;
		if(mDrawerFolderList == null || mDrawerFolderList.size() == 0){
			return 0;
		}
		int success = 0;
		int position =0;
		for(int i=0; i<mDrawerFolderList.size(); i++){
			CustomDrawerFolderBean folderBean=mDrawerFolderList.get(i);
			if(folderBean == null || folderBean.appList == null || folderBean.appList.size() == 0){
				continue;
			}
			boolean result  = addCustomFolder(mLauncher,allapps,folderBean,position);
			if(result){
				success++;
				position++;
			} else {
				continue;
			}
		}
		return success;
	}

	private boolean addCustomFolder(Context ctx, List<ApplicationInfo> allapps,CustomDrawerFolderBean folderBean,int position) {
		if(allapps == null || allapps.size() == 0){
			return false;
		}
		if(folderBean == null || folderBean.appList == null || folderBean.appList.size() == 0){
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(AppTable.PCK, "");
		values.put(AppTable.CLS, "");
		values.put(AppTable.TITLE, folderBean.folderName);
		values.put(AppTable.POS, position);
		values.put(AppTable.TYPE, ApplicationInfo.APP_TYPE_FOLDER);
		AppDataBase db = null;
		try {
			db = AppDataBase.getInstance(ctx);
			long id = db.add(AppTable.TABLE_NAME, values);

			if(folderBean.appList  != null && folderBean.appList .size() > 0) {
				int i = 0;
				List<ApplicationInfo> addApps = new ArrayList<ApplicationInfo>();
				for (CustomRecommendAppBean appBean : folderBean.appList ) {
					ApplicationInfo tempInfo = new ApplicationInfo();
					tempInfo.componentName = new ComponentName(appBean.packageName,appBean.className);
					if(!allapps.contains(tempInfo)){
						continue;
					}
					int index = allapps.indexOf(tempInfo);
					ApplicationInfo appInfo = allapps.get(index);
					values = new ContentValues();
					values.put(AppTable.PCK, appInfo.componentName.getPackageName());
					values.put(AppTable.CLS, appInfo.componentName.getClassName());
					values.put(AppTable.TITLE, appInfo.title.toString());
					values.put(AppTable.POS, i);
					values.put(AppTable.TYPE, ApplicationInfo.APP_TYPE_APP);
					values.put(AppTable.CONTAINER, id);
					values.put(AppTable.ISSYSTEM, appInfo.isSystem);
					values.put(AppTable.ISHIDDEN, appInfo.isHidden);
					values.put(AppTable.INSTALL_TIME, appInfo.installTime);
					db.add(AppTable.TABLE_NAME, values);
					addApps.add(appInfo);
					i++;
				}
				allapps.removeAll(addApps);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return false;
	}


	private void initInstallAppInfo() {
		final List<ApplicationInfo> apps = AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), false);
		// 将所有的应用的md5值 versioncode 存入对应的数据库中
		final Set<String> set = new HashSet<String>();
		final Set<String> ignoreSet = new HashSet<String>();
		ignoreSet.addAll(Arrays.asList(Constants.IGNORE_PACKAGES));
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				List<InstallAppInfo> installAppInfos = new ArrayList<InstallAppInfo>();
				for (ApplicationInfo app : apps) {
					try {
						// 过滤系统app
						if (ignoreSet.contains(app.componentName.getPackageName()))
							continue;
						if (set.contains(app.componentName.getPackageName()))
							continue;
						set.add(app.componentName.getPackageName());
						ForeignPackage foreignPackage = new ForeignPackage(getContext(), app.componentName.getPackageName(), false);
						String md5 = foreignPackage.getMD5();
						PackageInfo packageInfo = ForeignPackage.getPackageInfo(getContext(), app.componentName.getPackageName());
						if (packageInfo == null)
							continue;
						InstallAppInfo installAppInfo = new InstallAppInfo(app.componentName.getClassName(), md5, app.componentName.getPackageName(),
								app.title.toString(), packageInfo.versionCode, packageInfo.versionName);
						installAppInfos.add(installAppInfo);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				AppDataFactory.getInstance().saveInstallAppInfoToDB(getContext(), installAppInfos);

				// 请求一次数据库 并将开关设置为true
				AppUpgradeOnLauncherStartListenerImpl.getInstance().parseQuery();
				SettingsPreference.getInstance().setIsAllowQuery(true);

				// 重新布局
				handler.post(new Runnable() {
					@Override
					public void run() {
						allAppsData.getDataList().clear();
						allAppsData.getDataList().addAll(assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true)));
						drawerSlidingView.reLayout();
					}
				});
			}
		});
	}

	private void addCustomShortcutToListTop(List<ApplicationInfo> allapps) {
		try {
			// "个性化"
			if (Global.isBaiduLauncher()) {
				ApplicationInfo themeInfo = new ApplicationInfo();
				themeInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT;
				themeInfo.title = getContext().getString(DrawerDataFactory.CUSTOM_INDIVIDUAL_TITLE);
				themeInfo.intent = Intent.parseUri(DrawerDataFactory.CUSTOM_INDIVIDUAL_CLS_NAME, 0);
				themeInfo.iconResource = Intent.ShortcutIconResource.fromContext(getContext(), DrawerDataFactory.CUSTOM_INDIVIDUAL_DRAWABLE);
				allapps.add(0, themeInfo);
			}

			if (!Global.isBaiduLauncher()) {
				// "爱淘宝"或"天猫精选"
				ApplicationInfo taobaoInfo = new ApplicationInfo();
				taobaoInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT;
				taobaoInfo.title = getContext().getString(DrawerDataFactory.CUSTOM_AITAOBAO_TITLE);
				taobaoInfo.intent = Intent.parseUri(DrawerDataFactory.CUSTOM_AITAOBAO_CLS_NAME, 0);
				taobaoInfo.iconResource = Intent.ShortcutIconResource.fromContext(getContext(), DrawerDataFactory.CUSTOM_AITAOBAO_DRAWABLE);
				allapps.add(0, taobaoInfo);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 删除加密文件夹回调
	 * </p>
	 * 
	 * <p>
	 * date: 2012-7-29 下午01:47:45
	 */
	public void handleFolderDeleteDirectly() {
		if (mClickFolderView != null) {
			CommonViewHolder tag = (CommonViewHolder) mClickFolderView.getTag(R.id.common_view_holder);
			drawerSlidingView.releaseFolder((FolderInfo) tag.item, allAppsData);
			mFolderEncriptHelper.clearEncript(((FolderInfo) tag.item).id, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
		}
	}

	/**
	 * <p>
	 * 文件夹加密成功后回调
	 * </p>
	 * 
	 * <p>
	 * date: 2012-7-29 下午01:48:03
	 */
	public void handleFolderEncriptDirectly() {
		if (mClickFolderView != null) {
			CommonViewHolder tag = (CommonViewHolder) mClickFolderView.getTag(R.id.common_view_holder);
			final FolderInfo folderInfo = (FolderInfo) tag.item;
			folderInfo.isEncript = mFolderEncriptHelper.isFolderEncript(folderInfo.id, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
			mClickFolderView.invalidate();
		}
	}

	/**
	 * 记录要删除的程序包名
	 * 
	 * @param packageName
	 */
	public void recordRemovePackageName(String packageName) {
		isActionRemoveApp = true;
		removeAppPackageName = packageName;
	}

	/**
	 * 检测是否删除了程序，若是则更新视图
	 */
	public void checkRemoveAppAction() {
		if (isActionRemoveApp) {
			if (removeAppPackageName == null || "".equals(removeAppPackageName)) {
				isActionRemoveApp = false;
				return;
			}
			if (!AndroidPackageUtils.isPkgInstalled(getContext(), removeAppPackageName)) {
				removeApps(removeAppPackageName);
			}
			isActionRemoveApp = false;
		}
	}

//	public boolean isHasbeenOpened() {
//		return hasbeenOpened;
//	}

	/**
	 * 描述:
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-10-23
	 * @param text
	 */
	private void showOnlyToast(final String text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Description: 清除Gpu开启时的硬件层 Author: guojy Date: 2012-11-23 下午4:00:00
	 */
	public void destroySlidingViewHardwareLayer() {
		if (!GpuControler.isOpenGpu(this))
			return;

		int cur = drawerSlidingView.getCurrentScreen();
		CommonLayout cl = drawerSlidingView.getCommonLayout(cur);
		((DrawerLayout) cl).destroyHardwareLayer();
	}

	public void watcherFolderSlidingViewChooseItems(final List<DraggerChooseItem> chooseItems) {
		if (drawerEditZoneController != null) {
			drawerEditZoneController.watcherFolderSlidingViewChooseItems(chooseItems);
		}
	}

	/**
	 * 判断是否动态插件下载完成
	 * 
	 * @param intent
	 * @return
	 */
	private boolean checkIsDynamicComplete(Intent intent) {
		int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
		if (state == DownloadState.STATE_FINISHED) {
			String identification = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
			String path = Global.DOWNLOAD_DIR + identification + ".apk";
			if (DynamicWidgetManager.getClient(path) != null)
				return true;
		}
		return false;
	}

	/**
	 * 进入插件详情界面
	 */
	private void intoWidgetInfoView(LauncherOnLineWidgetInfo widgetInfo) {
		Intent i = new Intent(getContext(), WidgetInfoActivity.class);
		i.putExtra(WidgetInfoActivity.EXTRA_TYPE, 1);
		i.putExtra(WidgetInfoActivity.EXTRA_NAME, widgetInfo.title);
		i.putExtra(WidgetInfoActivity.EXTRA_PAKAGENAME, widgetInfo.packageName);
		i.putExtra(WidgetInfoActivity.EXTRA_SIZE, widgetInfo.size);
		i.putExtra(WidgetInfoActivity.EXTRA_DESC, widgetInfo.description);
		i.putExtra(WidgetInfoActivity.EXTRA_VERSION, widgetInfo.versionCode);
		i.putExtra(WidgetInfoActivity.EXTRA_WIDGET_TYPE, widgetInfo.widgetType);
		i.putExtra(WidgetInfoActivity.EXTRA_INSTALL_TYPE, widgetInfo.isInstallNeeded);
		i.putStringArrayListExtra(WidgetInfoActivity.EXTRA_PREVIEWS, widgetInfo.previewList);
		getContext().startActivity(i);
	}

	static class MyRunnable implements Runnable {

		private String percentNum;

		private WidgetPreviewView widgetPreView;

		/**
		 * @param widgetPreView
		 */
		public MyRunnable(WidgetPreviewView widgetPreView) {
			super();
			this.widgetPreView = widgetPreView;
		}

		public void setPercentNum(String percentNum) {
			this.percentNum = percentNum;
		}

		@Override
		public void run() {
			widgetPreView.mDownloadPercent = percentNum;
			widgetPreView.invalidate();
		}
	}

	private void handlerClick(final View v, final LauncherWidgetInfo widgetInfo, final boolean isShowToast) {
		if (v instanceof WidgetPreviewView) {
			final MyRunnable r = new MyRunnable((WidgetPreviewView) v);
			if (isShowToast) {
				r.setPercentNum("10%");
				handler.post(r);
			}
			ThreadUtil.executeDrawer(new Runnable() {
				@Override
				public void run() {
					if (isShowToast) {
						r.setPercentNum("30%");
						handler.post(r);
					}
					new DexClassLoader(Global.WIDGET_PLUGIN_DIR + widgetInfo.packageName + ".jar", PluginLoaderUtil.getPluginDexOptPath(
							getContext(), widgetInfo.packageName), PluginLoaderUtil.getPluginLibPath(getContext(), widgetInfo.packageName),
							ClassLoader.getSystemClassLoader());
					if (isShowToast) {
						r.setPercentNum("100%");
						handler.post(r);
					}
					if (isShowToast) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getContext(), R.string.drawer_widget_click_ready_tips, Toast.LENGTH_SHORT).show();

							}
						});
						r.setPercentNum(null);
						handler.postDelayed(r, 100);
					}
				}
			});
		}
	}

	/**
	 * 是否处于应用程序编辑模式
	 * 
	 * @author wangguomei 2014-4-14
	 */
	public boolean isOnAppsEditMode() {
		return drawerSlidingView.isOnAppsEditMode();
	}

	/**
	 * 是否处于小部件编辑模式
	 * 
	 * @author wangguomei 2014-4-14
	 */
	public boolean isOnWidgetEditMode() {
		return drawerSlidingView.isOnWidgetEditMode();
	}

	/**
	 * 是否处于我的手机编辑模式
	 * 
	 * @author wangguomei 2014-4-14
	 */
	public boolean isOnMyPhoneEditMode() {
		return drawerSlidingView.isOnMyPhoneEditMode();
	}

	/**
	 * 
	 * 匣子小部件内存优化： 退出匣子时，若小部件列表已加载完成,同时匣子的当前页签模块不处于小部件上时, 重新将小部件列表中的子view画布绘制成空白
	 * 
	 * @author wangguomei 2014.04.04
	 * 
	 */
	public void drawBlankCanvasForWidgetPreviewView() {

		// 若固件版本为4.1及以下的，不做优化
		if (com.nd.hilauncherdev.kitset.util.TelephoneUtil.getApiLevel() <= 16) {
			return;
		}

		if (drawBlankCanvasForWidgetPreviewView) {
			return;
		}

		if (widgetsData != null && widgetsData.getRowNum() == 1) {
			return;
		}

		if (drawerSlidingView != null && drawerSlidingView.getCurrentData() == widgetsData) {
			return;
		}

		drawBlankCanvasForWidgetPreviewView = true;

		postInvalidateForWidgetPreviewViews();

	}

	/**
	 * 
	 * 匣子小部件内存优化： 退出匣子时，若小部件列表中的子view画布已被绘制成空白， 在进入的时候，需要还原重绘各小部件视图内容
	 * 
	 * @author wangguomei 2014.04.04
	 * 
	 */
	public void restoreCanvasForWidgetPreviewView() {

		if (drawBlankCanvasForWidgetPreviewView) {
			drawBlankCanvasForWidgetPreviewView = false;
			postInvalidateForWidgetPreviewViews();
		}

	}

	private void postInvalidateForWidgetPreviewViews() {
		if (Global.isBaiduLauncher())
			return;

		int[] pageRang = drawerSlidingView.getDataPageInfo(widgetsData);
		if (pageRang == null) {
			return;
		}

		for (int i = pageRang[0]; i < pageRang[1]; i++) {
			DrawerLayout layout = (DrawerLayout) drawerSlidingView.getChildAt(i);
			if (layout == null)
				continue;
			for (int j = 0; j < layout.getChildCount(); j++) {
				View view = layout.getChildAt(j);
				if (view instanceof WidgetPreviewView) {
					view.postInvalidate();
				}
			}
		}

	}

	/**
	 * 展示滑屏特效
	 */
	public void demonstrateSlideEffect(final int effect) {
		if (!mLauncher.isAllAppsVisible()) {
			mLauncher.showAllApps(false);
		}

		// 特效演示，先左滑再右滑
		final int currentScreen = drawerSlidingView.getCurrentScreen();
		final int velocity = 500;
		final long interval = 800L;
		EffectsType.setCurrentDrawerEffect(effect);
		if (currentScreen == 0) {// 第一屏先右滑再左滑
			drawerSlidingView.snapToScreen(currentScreen + 1, velocity);
			postDelayed(new Runnable() {
				public void run() {
					EffectsType.setCurrentDrawerEffect(effect);// 使得随机特效往回滑时有不同特效
					drawerSlidingView.snapToScreen(currentScreen, velocity);
				}
			}, interval);
		} else {
			drawerSlidingView.snapToScreen(currentScreen - 1, velocity);
			postDelayed(new Runnable() {
				public void run() {
					EffectsType.setCurrentDrawerEffect(effect);
					drawerSlidingView.snapToScreen(currentScreen, velocity);
				}
			}, interval);
		}
	}

	/**
	 * 展示进出特效
	 */
	public void demonstrateInOutEffect() {
		final boolean shown = mLauncher.isAllAppsVisible();
		if (shown) {
			mLauncher.closeAllApps(true);
		} else {
			mLauncher.showAllApps(true);
		}
		postDelayed(new Runnable() {
			@Override
			public void run() {
				if (shown) {
					mLauncher.showAllApps(true);
				} else {
					mLauncher.closeAllApps(true);
				}
			}
		}, 800);
	}

	private boolean isDemonstratingEffect() {
		return (mLauncher.editor != null && mLauncher.editor.getVisibility() == View.VISIBLE);
	}

	/**
	 * 隐藏Widget和我的手机
	 * 
	 * @author dingdj Date:2014-6-27上午11:33:51
	 */
	public void hideWidgetAndMyPhoneTab() {
		drawerSlidingView.setEditModeAt(DrawerSlidingView.CLASSIFY_PREVIEW_MODE);
		drawerSlidingView.snapToScreen(0);
		drawerSlidingView.lockData(true);

		// 隐藏头部和底部
		normalTopLayoutView.hideTopLayout();
		bottomLayoutView.hideBottomLayout();
	}

	/**
	 * 退出分类预览模式
	 * 
	 * @author dingdj Date:2014-6-20上午10:34:46
	 */
	public void exitClassifyPreviewMode() {
		drawerSlidingView.setEditModeAt(DrawerSlidingView.NORMAL_MODE);

		normalTopLayoutView.showTopLayout();
		bottomLayoutView.showBottomLayout();

		if (classifyPreviewBottomLayout != null) {
			classifyPreviewBottomLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 对应用重新布局
	 * 
	 * @author dingdj Date:2014-6-27上午9:04:02
	 */
	public void refreshAppListAndLayout() {

		ThreadUtil.executeDrawer(new Runnable() {
			@Override
			public void run() {
				List<ICommonDataItem> allAppsList = allAppsData.getDataList();
				allAppsList.clear();
				allAppsList.addAll(assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true)));
				handler.sendEmptyMessageDelayed(Constants.RELAYOUT, 1000);
			}
		});
	}

	public void toggleMenu() {
		drawerMenu.toggleMenu();
	}

	public Handler getHandler() {
		return handler;
	}

	public ImageView getMoreBtn() {
		return bottomLayoutView.moreBtn;
	}

	public ImageView getSearchBtn() {
		return bottomLayoutView.searchBtn;
	}

	private boolean searchViewInvisiable() {
		return (searchView == null || !searchView.isVisible());
	}

	public SearchView getSearchView() {
		return searchView;
	}

	class NormalTopLayoutView {

		public ViewGroup topLayout;

		public CheckedTextView allAppsBtn;

		public CheckedTextView widgetsBtn;

		public CheckedTextView myPhoneBtn;

		/**
		 * 显示可升级数量+new的数量
		 */
		private TextView widgetsUpdateInfo;

		public void onFinishInflate() {
			topLayout = (ViewGroup) findViewById(R.id.drawer_top_layout);
			if(YinniSansumHelper.isYinniSamsung()){
				YinniSansumHelper.hideView(topLayout);
			}
			if (!isShowTopLayout()) {
				topLayout.setVisibility(View.GONE);
			} else if (!isShowMyphone()) {
				View view = findViewById(R.id.drawer_my_phone_btn);
				view.setVisibility(View.GONE);
			}
			/**
			 * 设置头部Tab栏背景
			 */
			topLayout.setBackgroundResource(R.drawable.drawer_tab_bar_background);

			allAppsBtn = (CheckedTextView) findViewById(R.id.drawer_all_apps_btn);
			widgetsBtn = (CheckedTextView) findViewById(R.id.drawer_widgets_btn);
			myPhoneBtn = (CheckedTextView) findViewById(R.id.drawer_my_phone_btn);

			initTabBtn(allAppsBtn);
			initTabBtn(widgetsBtn);
			initTabBtn(myPhoneBtn);

			widgetsUpdateInfo = (TextView) findViewById(R.id.drawer_widgets_info);
		}

		public void applyTheme() {
			// Tab栏背景
			Drawable d = ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.PANDA_BOX_TAB_BAR_BACKGROUND);
			if (null == d) {
				topLayout.setBackgroundResource(R.drawable.drawer_tab_bar_background);
			} else {
				topLayout.setBackgroundDrawable(d);
			}

			// Tab栏点击选中背景
			d = ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.PANDA_BOX_TAB_PRESSED_BACKGROUND);
			if (null == d) {
				allAppsBtn.setBackgroundResource(R.drawable.drawer_tab_background);
				widgetsBtn.setBackgroundResource(R.drawable.drawer_tab_background);
				myPhoneBtn.setBackgroundResource(R.drawable.drawer_tab_background);
			} else {
				ColorDrawable transparentDrawable = new ColorDrawable(0x00000000);
				StateListDrawable btnBg = buildStateListDrawable(d, transparentDrawable);
				allAppsBtn.setBackgroundDrawable(btnBg);

				btnBg = buildStateListDrawable(d, transparentDrawable);
				widgetsBtn.setBackgroundDrawable(btnBg);

				btnBg = buildStateListDrawable(d, transparentDrawable);
				myPhoneBtn.setBackgroundDrawable(btnBg);
			}
		}

		public void showTopLayout() {
			if (!isShowTopLayout())
				return;
			topLayout.setVisibility(View.VISIBLE);
		}

		public void hideTopLayout() {
			topLayout.setVisibility(View.GONE);
		}

		/**
		 * 设置Tab文字大小、颜色、背景
		 */
		private void initTabBtn(TextView textView) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			textView.setTextColor(Color.WHITE);
			textView.setShadowLayer(1, 1, 1, ColorUtil.antiColorAlpha(200, Color.WHITE));
			textView.setBackgroundResource(R.drawable.drawer_tab_background);
			int padding = ScreenUtil.dip2px(getContext(), 15);
			textView.setPadding(padding, 0, padding, 0);
			textView.setOnClickListener(onTabClickListener);
		}

		private StateListDrawable buildStateListDrawable(Drawable d, ColorDrawable transparentDrawable) {
			StateListDrawable allAppsBtnBg = new StateListDrawable();
			int[] stateSet = new int[1];
			stateSet[0] = android.R.attr.state_empty;
			allAppsBtnBg.addState(stateSet, transparentDrawable);
			stateSet = new int[1];
			stateSet[0] = android.R.attr.state_pressed;
			allAppsBtnBg.addState(stateSet, d);
			stateSet = new int[1];
			stateSet[0] = android.R.attr.state_checked;
			allAppsBtnBg.addState(stateSet, d);
			return allAppsBtnBg;
		}

		public void restoreViews() {
			allAppsBtn.setChecked(false);
			widgetsBtn.setChecked(false);
			myPhoneBtn.setChecked(false);
		}

		/**
		 * Tab点击事件
		 */
		private OnClickListener onTabClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				drawerSlidingView.clearFocusedView();
				if (v == allAppsBtn) {
					snapToData(Constants.TAB_APP);
					mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_IN_DRAWER_ALL_APP);
				} else if (v == widgetsBtn) {
					snapToData(Constants.TAB_WIDGET);
					mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_OUT_DRAWER_ALL_APP);
					DrawerMainView.this.restoreCanvasForWidgetPreviewView();
					// 打开匣子第一次切换到小插件的时候打点
					if (isFirstIntoWidgets) {
//						AppDistributeStatistics.AppDistributePercentConversionStatistics(mLauncher, "1001");
						isFirstIntoWidgets = false;
					}
				} else if (v == myPhoneBtn) {
					snapToData(Constants.TAB_MY_PHONE);
					mLauncher.trackLauncherCurOperSrcAct(ReadMeStateManager.OPER_SRC_ACT_OUT_DRAWER_ALL_APP);
					// ControlledAnalyticsUtil.addCommonAnalytics(getContext(),
					// AnalyticsConstant.LAUNCHER_DRAWER_MYPHONE);
				}
			}
		};
	}

	class BottomLayoutView {

		public ViewGroup bottomLayout;

		public ViewGroup toolsLayout;

		public ImageView homeBtn;

		public ImageView searchBtn;

		public ImageView moreBtn;

		public void onFinishInflate() {

			bottomLayout = (ViewGroup) findViewById(R.id.drawer_bottom_layout);
			if(YinniSansumHelper.isYinniSamsung()){
				YinniSansumHelper.hideView(bottomLayout);
			}
			toolsLayout = (ViewGroup) findViewById(R.id.drawer_tools_layout);
			/**
			 * 设置底部工具栏背景
			 */
			bottomLayout.setBackgroundDrawable(null);

			homeBtn = (ImageView) findViewById(R.id.drawer_home_btn);
			searchBtn = (ImageView) findViewById(R.id.drawer_search_btn);
			moreBtn = (ImageView) findViewById(R.id.drawer_more_btn);

			homeBtn.setOnClickListener(onToolsClickListener);
			searchBtn.setOnClickListener(onToolsClickListener);
			moreBtn.setOnClickListener(onToolsClickListener);
		}

		public void applyTheme() {

			// 底部工具栏背景
			ThemeManagerFactory.getInstance().loadThemeResource(ThemeData.PANDA_BOX_TOOLS_BAR_BACKGROUND, bottomLayout,
					ThemeManagerFactory.THEME_ITEM_BACKGROUND);

			// 搜索按钮
			searchBtn.setImageDrawable(ThemeCompatibleResAssit.getDrawerSearchButton(getContext()));
			// 关闭匣子按钮
			homeBtn.setImageDrawable(ThemeCompatibleResAssit.getCloseDrawerIcon(getContext()));
			// 更多按钮
			moreBtn.setImageDrawable(ThemeCompatibleResAssit.getDrawerMoreButton(getContext()));
		}

		public void showToolsLayout() {
			toolsLayout.setVisibility(View.VISIBLE);
		}

		public void hideToolsLayout() {
			toolsLayout.setVisibility(View.GONE);
		}

		public void showBottomLayout() {
			bottomLayout.setVisibility(View.VISIBLE);
		}

		public void hideBottomLayout() {
			bottomLayout.setVisibility(View.GONE);
		}

		/**
		 * 工具栏点击事件
		 */
		private OnClickListener onToolsClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == homeBtn) {
					mLauncher.closeAllApps(true);
				} else if (v == moreBtn) {
					/**
					 * 查询程序是否已完成初始化
					 */
					if (!hasbeenInitDatabase) {
						Toast.makeText(getContext(), R.string.drawer_apps_not_init_tips, Toast.LENGTH_SHORT).show();
						return;
					}

					drawerMenu.toggleMenu();
					/**
					 * maolinnan_350804于14.1.13日添加的统计信息
					 */
					HiAnalytics.submitEvent(getContext(), AnalyticsConstant.MENU_DRAWER_MENU, "1");
				} else if (v == searchBtn) {
					/**
					 * 查询程序是否已完成初始化
					 */
					if (!hasbeenInitDatabase) {
						Toast.makeText(getContext(), R.string.drawer_apps_not_init_tips, Toast.LENGTH_SHORT).show();
						return;
					}
					// 用户已经使用过搜索功能将不再提示搜索
					if (!ConfigPreferences.getInstance().getToastSearcher()) {
						ConfigPreferences.getInstance().setToastSearcher2True();
					}
					searchBtn.setImageDrawable(ThemeCompatibleResAssit.getDrawerSearchButton(getContext()));
					setupSearchView(getContext());
					toggleSeachView();
					HiAnalytics.submitEvent(getContext(), AnalyticsConstant.LAUNCHER_DRAWER_SEARCH);
				} else if (v == editTopLayoutView.showAppDetailsZone) {
					Toast.makeText(getContext(), R.string.drawer_tools_btn_click_tips, Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	class EditTopLayoutView {

		public ViewGroup editToolsLayout;

		private ShowAppDetailsZone showAppDetailsZone;

		private void setupEditToolsLayout() {
			if (editToolsLayout != null)
				return;

			editToolsLayout = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.drawer_main_bottom_child_view, null);
			showAppDetailsZone = (ShowAppDetailsZone) editToolsLayout.findViewById(R.id.drawer_details_btn);

			showAppDetailsZone.setOnClickListener(bottomLayoutView.onToolsClickListener);

			mDragController.addDropTarget(showAppDetailsZone);

			showAppDetailsZone.setLauncher(mLauncher);
		}

	}

	class DrawerMenu {

		private PopupWindow menuPopupWindow;

		private TextView classifyBtn;
		private TextView settingBtn;
		private TextView hideBtn;
		private ImageView classify_split;
		private ImageView drawer_menu_angle_img;
		public void toggleMenu() {
			if (isClassifyPreviewMode()) {// 处于分类预览模式不显示菜单
				return;
			}
			if (menuPopupWindow == null) {
				setupMenuPopup(getContext());
			}

			if (!isEditMode()) {
				if (menuPopupWindow.isShowing()) {
					menuPopupWindow.dismiss();
				} else {
					if (DrawerMainView.this.isVisible() && searchViewInvisiable()) {
						// HiAnalytics.submitEvent(getContext(),
						// AnalyticsConstant.DRAWER_MENU);
						if(YinniSansumHelper.isYinniSamsung()){
							menuPopupWindow.showAtLocation(
									mSamsungDrawerSearchLayout.getMoreView(),
									Gravity.RIGHT | Gravity.TOP, ScreenUtil.dip2px(getContext(), 10), (int) (1.8f*mSamsungDrawerSearchLayout.getHeight()+ StatusBarUtil.getStatusBarHeight(mLauncher)));
						} else {
							menuPopupWindow.showAtLocation(
									DrawerMainView.this,
									Gravity.RIGHT | Gravity.BOTTOM,
									ScreenUtil.dip2px(getContext(), 5),
									getContext().getResources().getDimensionPixelSize(R.dimen.drawer_button_bar_height)
											+ getContext().getResources().getDimensionPixelSize(R.dimen.linelight_light_height)
											+ ScreenUtil.dip2px(getContext(), 5));
						}
					}
				}
			}

			if (isOnAppTab()) {// 显示智能分类
				classifyBtn.setVisibility(View.VISIBLE);
				classify_split.setVisibility(View.VISIBLE);
			} else {// 不显示智能分类
				classifyBtn.setVisibility(View.GONE);
				classify_split.setVisibility(View.GONE);
			}
			if(YinniSansumHelper.isYinniSamsung()){
				classifyBtn.setVisibility(View.GONE);
//				classify_split.setVisibility(View.GONE);
				settingBtn.setVisibility(View.GONE);
				hideBtn.setVisibility(View.GONE);
			}
		}

		/**
		 * 初始化匣子菜单
		 */
		private void setupMenuPopup(Context context) {
			if (menuPopupWindow != null)
				return;

			View view = LayoutInflater.from(context).inflate(R.layout.drawer_menu_popup, null);
			menuPopupWindow = new PopupWindow(view);
			drawer_menu_angle_img = (ImageView) view.findViewById(R.id.drawer_menu_angle_img);
			if(YinniSansumHelper.isYinniSamsung()){
				menuPopupWindow.setAnimationStyle(R.style.SamsungDrawerMenuPopupAnimation);
				drawer_menu_angle_img.setVisibility(View.GONE);
			} else {
				drawer_menu_angle_img.setVisibility(View.VISIBLE);
				menuPopupWindow.setAnimationStyle(R.style.DrawerMenuPopupAnimation);
			}
			menuPopupWindow.setWidth(context.getResources().getDimensionPixelSize(R.dimen.drawer_menu_width));
			menuPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);

			TextView iconSortBtn = (TextView) view.findViewById(R.id.drawer_menu_icon_sort);
			TextView newFolderBtn = (TextView) view.findViewById(R.id.drawer_menu_new_folder);
			TextView hideAppBtn = (TextView) view.findViewById(R.id.drawer_menu_hide_app);
			TextView settingsBtn = (TextView) view.findViewById(R.id.drawer_menu_settings);
			classifyBtn = (TextView) view.findViewById(R.id.drawer_menu_app_classify);
			classify_split = (ImageView) view.findViewById(R.id.classify_split);

			settingBtn = (TextView) view.findViewById(R.id.drawer_menu_settings);
			hideBtn = (TextView) view.findViewById(R.id.drawer_menu_hide_app);

			iconSortBtn.setOnClickListener(onMenuClickListener);
			newFolderBtn.setOnClickListener(onMenuClickListener);
			hideAppBtn.setOnClickListener(onMenuClickListener);
			settingsBtn.setOnClickListener(onMenuClickListener);
			classifyBtn.setOnClickListener(onMenuClickListener);
		}

		/**
		 * 菜单点击事件
		 */
		private OnClickListener onMenuClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.drawer_menu_icon_sort) {
					/**
					 * 图标排序
					 */
					if (menuPopupWindow != null) {
						menuPopupWindow.dismiss();
					}
					CommonDialog.Builder result = new CommonDialog.Builder(getContext());
					result.setTitle(getContext().getText(R.string.drawer_menu_sort_mode));
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.setting_screen_effects_layout, null);
					ListView listView = (ListView) relativeLayout.findViewById(R.id.setting_screen_effects_layout_listview);
					Button button = (Button) relativeLayout.findViewById(R.id.setting_screen_effects_layout_button);
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// 点击按钮
							iconSortTypeDialog.dismiss();
						}
					});
					String[] array = getContext().getResources().getStringArray(R.array.drawer_app_sort_rule);
					SettingsScreenEffectsAdapter settingsScreenEffectsAdapter = new SettingsScreenEffectsAdapter(getContext(), array, null,
							R.layout.setting_screen_effects_item, Constants.KEY_DRAWER_APP_LAST_SORT_TYPE);
					listView.setAdapter(settingsScreenEffectsAdapter);
					result.setContentView(relativeLayout);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							final int[] oldInfo = drawerSlidingView.getDataPageInfo(allAppsData);
							List<ICommonDataItem> list = allAppsData.getDataList();
							List<ICommonDataItem> folderContentsList = new ArrayList<ICommonDataItem>();
							int index = position;
							settingsPreference.getSP().edit().putInt(Constants.KEY_DRAWER_APP_LAST_SORT_TYPE, index).commit();
							switch (index) {
							case 0:
								/**
								 * 按字母排序
								 */
								Collections.sort(list, DrawerSortHelper.APP_TITLE_COMPARATOR);
								DrawerDataFactory.updateAppInfosPosition(getContext(), list);
								for (ICommonDataItem item : list) {
									if (item instanceof FolderInfo) {
										FolderInfo folderInfo = (FolderInfo) item;
										Collections.sort(folderInfo.contents, DrawerSortHelper.APP_TITLE_COMPARATOR);
										folderContentsList.clear();
										folderContentsList.addAll(folderInfo.contents);
										DrawerDataFactory.updateAppInfosPosition(getContext(), folderContentsList);
									}
								}
								drawerSlidingView.reLayout(allAppsData, oldInfo);
								break;
							case 1:
								/**
								 * 按安装时间从近到远排序
								 */
								Collections.sort(list, DrawerSortHelper.APP_INSTALL_TIME_INVERSE_COMPARATOR);
								DrawerDataFactory.updateAppInfosPosition(getContext(), list);
								for (ICommonDataItem item : list) {
									if (item instanceof FolderInfo) {
										FolderInfo folderInfo = (FolderInfo) item;
										Collections.sort(folderInfo.contents, DrawerSortHelper.APP_INSTALL_TIME_INVERSE_COMPARATOR);
										folderContentsList.clear();
										folderContentsList.addAll(folderInfo.contents);
										DrawerDataFactory.updateAppInfosPosition(getContext(), folderContentsList);
									}
								}
								drawerSlidingView.reLayout(allAppsData, oldInfo);
								break;
							case 2:
								/**
								 * 按安装时间从远到近排序
								 */
								Collections.sort(list, DrawerSortHelper.APP_INSTALL_TIME_COMPARATOR);
								DrawerDataFactory.updateAppInfosPosition(getContext(), list);
								for (ICommonDataItem item : list) {
									if (item instanceof FolderInfo) {
										FolderInfo folderInfo = (FolderInfo) item;
										Collections.sort(folderInfo.contents, DrawerSortHelper.APP_INSTALL_TIME_COMPARATOR);
										folderContentsList.clear();
										folderContentsList.addAll(folderInfo.contents);
										DrawerDataFactory.updateAppInfosPosition(getContext(), folderContentsList);
									}
								}
								drawerSlidingView.reLayout(allAppsData, oldInfo);
								break;
							case 3:
								/**
								 * 按使用次数从多到少排序
								 */
								List<ICommonDataItem> list2 = assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true));
								Collections.sort(list2, DrawerSortHelper.APP_USED_TIMES_INVERSE_COMPARATOR);
								DrawerDataFactory.updateAppInfosPosition(getContext(), list2);
								for (ICommonDataItem item : list2) {
									if (item instanceof FolderInfo) {
										FolderInfo folderInfo = (FolderInfo) item;
										Collections.sort(folderInfo.contents, DrawerSortHelper.APP_USED_TIMES_INVERSE_COMPARATOR);
										folderContentsList.clear();
										folderContentsList.addAll(folderInfo.contents);
										DrawerDataFactory.updateAppInfosPosition(getContext(), folderContentsList);
									}
								}
								list.clear();
								list.addAll(list2);
								drawerSlidingView.reLayout(allAppsData, oldInfo);
								break;
							case 4:
								/**
								 * 按使用次数从少到多排序
								 */
								List<ICommonDataItem> list3 = assembleAppList(AppDataFactory.getInstance().loadDrawerAppFromDB(getContext(), true));
								Collections.sort(list3, DrawerSortHelper.APP_USED_TIMES_COMPARATOR);
								DrawerDataFactory.updateAppInfosPosition(getContext(), list3);
								for (ICommonDataItem item : list3) {
									if (item instanceof FolderInfo) {
										FolderInfo folderInfo = (FolderInfo) item;
										Collections.sort(folderInfo.contents, DrawerSortHelper.APP_USED_TIMES_COMPARATOR);
										folderContentsList.clear();
										folderContentsList.addAll(folderInfo.contents);
										DrawerDataFactory.updateAppInfosPosition(getContext(), folderContentsList);
									}
								}
								list.clear();
								list.addAll(list3);
								drawerSlidingView.reLayout(allAppsData, oldInfo);
								break;
							default:
								break;
							}
							ControlledAnalyticsUtil.addCommonAnalytics(getContext(), AnalyticsConstant.LAUNCHER_DRAWER_ICONSORT, index + "");
							iconSortTypeDialog.dismiss();
						}
					});
					iconSortTypeDialog = result.create();
					WindowManager.LayoutParams p = iconSortTypeDialog.getWindow().getAttributes();
					p.height = ScreenUtil.dip2px(getContext(), 286);
					iconSortTypeDialog.show();
					LinearLayout layout = (LinearLayout) iconSortTypeDialog.findViewById(R.id.common_dialog_custom_view_layout);
					layout.setPadding(0, 0, 0, 0);
					iconSortTypeDialog.getWindow().setAttributes(p);

					HiAnalytics.submitEvent(getContext(), AnalyticsConstant.DRAWER_MENU_USAGE, "1");
				} else if (v.getId() == R.id.drawer_menu_new_folder) {
					/**
					 * 新建文件夹
					 */
					menuPopupWindow.dismiss();

					Intent intent = new Intent();
					intent.setClass(getContext(), AppChooseDialogActivity.class);
					intent.putExtra(AppChooseDialogActivity.TITLE, getContext().getString(R.string.drawer_menu_new_folder));
					intent.putExtra(AppChooseDialogActivity.RENAMEFLAG, true);

					/**
					 * 过滤其他文件夹中的程序
					 */
					List<SerializableAppInfo> filterList = new ArrayList<SerializableAppInfo>();
					DrawerDataFactory.loadAppsInOtherFoldersOrIsHidden(getContext(), null, filterList);
					intent.putExtra(AppChooseDialogActivity.FILTER_LIST, (Serializable) filterList);

					SystemUtil.startActivityForResultSafely(mLauncher, intent, LauncherActivityResultHelper.REQUEST_CREATE_FOLDER_IN_DRAWER);
					HiAnalytics.submitEvent(getContext(), AnalyticsConstant.DRAWER_MENU_USAGE, "2");
				} else if (v.getId() == R.id.drawer_menu_hide_app) {
					/**
					 * 隐藏图标
					 */
					menuPopupWindow.dismiss();
					AppHide.enterHideApp(AppHide.FROMDRAW);
					ControlledAnalyticsUtil.addCommonAnalytics(getContext(), AnalyticsConstant.LAUNCHER_DRAWER_HIDING_PROGRAM);

				} else if (v.getId() == R.id.drawer_menu_settings) {
					/**
					 * 应用列表设置
					 */
					menuPopupWindow.dismiss();
					Intent intent = new Intent();
					intent.setClass(mLauncher, LauncherLayoutSettingsActivity.class);
					intent.putExtra(LauncherLayoutSettingsActivity.SCROLL_TO_APP, true);
					mLauncher.startActivity(intent);
					HiAnalytics.submitEvent(getContext(), AnalyticsConstant.DRAWER_MENU_USAGE, "4");
				} else if (v.getId() == R.id.drawer_menu_app_classify) {
					ControlledAnalyticsUtil.addCommonAnalytics(getContext(), AnalyticsConstant.LAUNCHER_DRAWER_INTELLIGENT_CLASSIFICATION);
					classifyBtn.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.drawer_menu_item_background));
					menuPopupWindow.dismiss();
					AppClassifyManager.clickClassifyButton(DrawerMainView.this);
				}
			}
		};

	}

	public LineLightbar getLightBar() {
		return mLightbar;
	}

	public boolean isBottomLayoutErr() {
		int[] loc = new int[2];
		View v = bottomLayoutView.bottomLayout;
		v.getLocationOnScreen(loc);
		return loc[1] + v.getHeight() < mLauncher.getDragLayer().getHeight();
	}

	public static boolean isShowTopLayout() {
		if (Global.isBaiduLauncher())// 百度桌面不显示
			return false;
		return true;
	}

	public static boolean isShowMyphone() {
		if (Global.isBaiduLauncher())// 百度桌面不显示
			return false;
		if (SettingsPreference.getInstance().getIsShowMyphone()) {
			return true;
		} else {
			return false;
		}
	}
}
