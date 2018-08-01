package com.nd.hilauncherdev.launcher;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.OnKeyDownListenner;
import com.nd.hilauncherdev.kitset.GpuControler;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StatusBarUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.broadcast.LauncherBroadcastControl;
import com.nd.hilauncherdev.launcher.broadcast.LauncherBroadcastReceiverManager;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.config.ConfigFactory.ConfigCallback;
import com.nd.hilauncherdev.launcher.config.LauncherConfig;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;
import com.nd.hilauncherdev.launcher.config.preference.BaseSettingsPreference;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.model.load.LauncherBinder;
import com.nd.hilauncherdev.launcher.model.load.LauncherLoaderHelper;
import com.nd.hilauncherdev.launcher.model.load.LauncherPreLoader;
import com.nd.hilauncherdev.launcher.screens.*;
import com.nd.hilauncherdev.launcher.screens.dockbar.BaseLineLightBar;
import com.nd.hilauncherdev.launcher.screens.dockbar.BaseMagicDockbar;
import com.nd.hilauncherdev.launcher.screens.dockbar.DockbarCellLayoutConfig;
import com.nd.hilauncherdev.launcher.screens.dockbar.MagicDockbarRelativeLayout;
import com.nd.hilauncherdev.launcher.screens.preview.PreviewEditAdvancedController;
import com.nd.hilauncherdev.launcher.support.*;
import com.nd.hilauncherdev.launcher.touch.BaseDragController;
import com.nd.hilauncherdev.launcher.view.BaseDeleteZoneTextView;
import com.nd.hilauncherdev.launcher.view.PandaWidgetViewContainer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BaseLauncher extends Activity implements View.OnClickListener, OnLongClickListener{

	static final boolean PROFILE_STARTUP = false;
	
	private boolean mWorkspaceLoading = true;
	/**
	 * 是否加载完毕
	 */
	private boolean mIsFinishBinding = false;
	/**
	 * 是否已初始化workspace
	 */
	private boolean hasSetupWorkspace = false;
	private final Object hasSetupWorkspaceLock = new Object();
	
	/**
	 * 是否已加载workspace数据
	 */
	private boolean hasLoadWorkspace = false;
	private final Object hasLoadWorkspaceLock = new Object();
	/**
	 * 是否已绑定workspace上各应用
	 */
	private boolean startBindWorkspace = false;
	private final Object startBindWorkspaceLock = new Object();


	private Bundle mSavedState;
//	private Bundle mSavedInstanceState;
	
	
	public BaseDragController mDragController;
	
	public DragLayer mDragLayer;
	public BaseLauncherModel mModel;
	
	public static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
	
	private boolean isWorkspaceLocked;
	public ScreenViewGroup mWorkspace;
	public WorkspaceLayer mWorkspaceLayer;
	public BaseMagicDockbar mDockbar;
	public BaseLineLightBar lightbar;
	public DeleteZone mDeleteZone;
	public ViewGroup mZeroViewGroup;
	
	BaseIconCache mIconCache;
	LauncherBinder mLauncherBinder;
	protected MagicDockbarRelativeLayout bottomContainer;
	
	public LauncherAppWidgetHost mAppWidgetHost;
	
	/**
	 * 桌面启动时，异步数据加载
	 */
	private LauncherPreLoader mLauncherPreLoader;
	
	private AppWidgetManager mAppWidgetManager;
	
	public static final int APPWIDGET_HOST_ID = 1024;
	
	private SpannableStringBuilder mDefaultKeySsb = null;
	private boolean mRestoring;
	
	private static final String PREFERENCES = "launcher.preferences";
	
	private String uninstallPackageName = null;// 在桌面上或匣子里被卸载的应用的包名
	protected WallpaperHelper mWallpaperHelper;
	
	public List<PandaWidgetViewContainer> pandaWidgets = new ArrayList<PandaWidgetViewContainer>();
	
	/**
	 * 屏幕预览/管理控制器
	 */
	public PreviewEditAdvancedController previewEditController;
	public LauncherWidgetEditHelper widgetEditHelper; // 小插件处理
	
	public boolean isDeleteZone; // 放入回收站与卸载类型
	
	private List<OnKeyDownListenner> onkeydownLisList = new ArrayList<OnKeyDownListenner>();
	
	/**
	 * 快捷菜单与menu时盖上一层阴影
	 */
	private View topShadowView;
	
	public static boolean hasDrawer = true;//是否有匣子
	
	protected boolean isNewInstall = false;//是否初次安装加载桌面
	protected boolean isUpdateInstall = false;//是否升级安装加载桌面

	public static boolean isIconViewSupportShadowlayer = true;//图标文字是否支持阴影效果
	
	protected boolean reLayoutWorkspaceAndDockbar = false;
	
	protected boolean isTranslucentStatusBar = false;
	
	/** 导航栏是否透明  */
	protected boolean isTranslucentNavigationBar = false;
	private View bgView;

	protected boolean isShowCustomGuide = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PROFILE_STARTUP) {
			android.os.Debug.startMethodTracing(Environment.getExternalStorageDirectory().getAbsolutePath() + "/launcher");
		}
		super.onCreate(savedInstanceState);
		
		onPreCreateStart();
		
		onCreateStart();
		initGlobalSomething();
		processThemeIssue();//待修改
		loadWorspaceFirst();
		setupHelper();//
		
		setContentView();
		
		setupViews();//
		register();//

		loadDataIfNeed(savedInstanceState);

		bindService();//
		
		GpuControler.openGpu(this);

		TelephoneUtil.logPhoneState();
		
		if (PROFILE_STARTUP) {
			android.os.Debug.stopMethodTracing();
		}
		recordLauncherCreateTime();
		
		onCreateEnd();
	}
	
	/**
	 * 优先级最高的操作
	 */
	protected void onPreCreateStart(){
//		isNewInstall = ConfigFactory.isNewInstall(this);
		//确定是否升级或新安装
		welcomeOrUpdate();
		BaseConfig.setBaseLauncher(this);
		LauncherConfig.initCellConfig(this, isNewInstall);
	}
	
	/**
	 * 打开新手欢迎页或升级提示页，升级用户还会进行数据升级、更换默认壁纸等操作
	 */
	protected void welcomeOrUpdate() {
		ConfigFactory.maybeShowReadme(this, new ConfigCallback() {
			@Override
			public boolean onAction() {
				isNewInstall = true;
				Log.w("Launcher", "isNewInstall");
				// 记录用户第一次使用桌面的时间 caizp 2012-12-4
				BaseConfigPreferences.getInstance().setFirstLaunchTime(System.currentTimeMillis());
				// 新安装用户不显示更新日志
				String curVersionName = TelephoneUtil.getVersionName(BaseLauncher.this);
				BaseConfigPreferences.getInstance().setVersionShowed(curVersionName, true);
				
				int curVersionCode = TelephoneUtil.getVersionCode((BaseLauncher.this));
				// 记录版本号
				BaseConfigPreferences.getInstance().setVersionCodeShowed(curVersionCode, true);
				// 记录新用户初次版本号 
				BaseConfigPreferences.getInstance().setVersionCodeFrom(curVersionCode);

				setupReadMeForNewUser();
				
				BaseConfigPreferences.getInstance().setLastVersionCode(curVersionCode);
				
				BaseConfigPreferences.getInstance().setDefaultScreen(ScreenViewGroup.DEFAULT_SCREEN);
				return true;
			}
		}, new ConfigCallback() {
			@Override
			public boolean onAction() {
				int curVersionCode = TelephoneUtil.getVersionCode((BaseLauncher.this));
				if (!BaseConfigPreferences.getInstance().isVersionCodeShowed(curVersionCode)) {
					isUpdateInstall = true;
					Log.w("Launcher", "isUpdateInstall");
					String curVersionName = TelephoneUtil.getVersionName(BaseLauncher.this);
					
					BaseConfigPreferences.getInstance().setVersionShowed(curVersionName, true);
					// 记录版本号
					BaseConfigPreferences.getInstance().setVersionCodeShowed(curVersionCode, true);

					setupReadMeForOldUser();
					
					BaseConfigPreferences.getInstance().setLastVersionCode(curVersionCode);
				}
				return false;
			}
		});
		
	}
	
	/**
	 * 初始化数据，拖动，图标，字体等 <br>
	 * Author:ryan <br>
	 * Date:2012-12-22下午02:34:00
	 */
	protected void initGlobalSomething() {
		BaseLauncherApplication app = ((BaseLauncherApplication) getApplication());
		mLauncherBinder = new LauncherBinder(this);
		mModel = app.setLauncher(mLauncherBinder);
		mModel.setLauncher(this);
		mIconCache = app.getIconCache();
		mDragController = createDragController();
		BaseConfig.resetDefaultFontMeasureSize();
		LauncherBroadcastReceiverManager.getInstance().setLauncher(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); 
		WallpaperHelper.getInstance().suggestWallpaperDimensions(this);
	}
	
	/**
	 * 预加载桌面数据 <br>
	 * Author:ryan <br>
	 * Date:2012-12-22下午02:34:32
	 */
	private void loadWorspaceFirst() {
		if(isAsyncLoadLauncherData()){
			mLauncherPreLoader = new LauncherPreLoader(mLauncherBinder, mModel, this);
			mLauncherPreLoader.start();
		}
	}
	
	/**
	 * 是否异步加载桌面数据
	 * @return
	 */
	private boolean isAsyncLoadLauncherData(){
		return BaseSettingsPreference.getInstance().isAsyncLoadLauncherData() && !isNewInstall && !isUpdateInstall;
	}
	
	void processThemeIssue() {
		
	}
	
	void setupHelper() {
		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new LauncherAppWidgetHost(this, APPWIDGET_HOST_ID);
		mAppWidgetHost.startListening();
		
		widgetEditHelper = new LauncherWidgetEditHelper(this);
	}
	
	void setContentView(){
		setContentView(R.layout.launcher);
	}
	
	void setupViews() {// long start = System.currentTimeMillis();
		BaseDragController dragController = mDragController;

		mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
		final DragLayer dragLayer = mDragLayer;

		dragController.setDragLayer(dragLayer);		
		dragLayer.setDragController(dragController);
		
		DragLayerStuffDrawer mDragLayerStuff = new DragLayerStuffDrawer();
		mDragLayerStuff.setDragLayer(dragLayer);
		dragLayer.setDragLayerStuff(mDragLayerStuff);


		mWorkspaceLayer = (WorkspaceLayer) dragLayer.findViewById(R.id.workspace_layer);
		mWorkspaceLayer.setLauncher(this);

		mWorkspace = (ScreenViewGroup) dragLayer.findViewById(R.id.workspace);
//		final ScreenViewGroup workspace = (ScreenViewGroup) mWorkspace;
		mWorkspace.setHapticFeedbackEnabled(false);
		// Log.e(TAG, "mWorkspace:" + (System.currentTimeMillis() - start));
		mWallpaperHelper = WallpaperHelper.getInstance();
		mWallpaperHelper.setWorkspaceLayer(mWorkspaceLayer);
		mWallpaperHelper.setWorkspace(mWorkspace);
		mWallpaperHelper.setDragLayer(mDragLayer);
		mWorkspace.setWallpaperHelper(mWallpaperHelper);
		mWorkspaceLayer.setWallpaperHelper(mWallpaperHelper);
		mDragLayerStuff.setWallPaperHelper(mWallpaperHelper);

		mDockbar = (BaseMagicDockbar) dragLayer.findViewById(R.id.quick_start_bar);
		bottomContainer = (MagicDockbarRelativeLayout) dragLayer.findViewById(R.id.lightbar_container);
		
		mWorkspace.setOnLongClickListener(this);
		mWorkspace.setDragController(dragController);
		mWorkspace.setLauncher(this);

		dragController.addDragScoller(mWorkspace);
		// dragController.setDragListener(deleteZone);
		dragController.setWorkspace(mWorkspace);
		dragController.setMoveTarget(mWorkspace);

		// The order here is bottom to top.
		dragController.addDropTarget(mWorkspace);
		// dragController.addDropTarget(deleteZone);
		//条形指示灯
		lightbar = (BaseLineLightBar) dragLayer.findViewById(R.id.lightbar);
		lightbar.setLinkedView(mWorkspace);
		lightbar.setLauncher(this);
		mWorkspace.setLightBar(lightbar);
		
		bottomContainer.setLauncher(this);
		mDockbar.setDragController(mDragController);
		mDockbar.setLauncher(this);
		dragController.addDropTarget(mDockbar);
		
		previewEditController = new PreviewEditAdvancedController(this);
		previewEditController.setDragController(mDragController);
		
		setupZeroView();
		
		if(BaseSettingsPreference.getInstance().isTranslucentStatusBar()){			
			isTranslucentStatusBar = StatusBarUtil.translucentStatusBar(this);
		}
		
		//校正dock栏高度
		fixDockBarLayout();
		
		//导航栏透明
		if(isTranslucentNavigationBar){
			bgView = new View(BaseLauncher.this);
			addLauncherBgView(bgView, 0);
			hideLauncherBgView();
			
			try{
				Method m = View.class.getMethod("setFitsSystemWindows", boolean.class);
				m.invoke(mDragLayer, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		if(LauncherConfig.getLauncherHelper().showCustomGuide(this)){
			mWorkspaceLayer.setVisibility(View.GONE);
			bottomContainer.setVisibility(View.GONE);
		}
	}
	
	void register() {
		
	}
	
	/**
	 * 预加载未完成则重新加载 <br>
	 * Author:ryan <br>
	 * Date:2012-12-22下午02:38:34
	 */
	private void loadDataIfNeed(Bundle savedInstanceState) {
		setSavedState(savedInstanceState);
		setHasSetupWorkspace(true);

		if (!mRestoring) {
			mModel.startLoader(this, true, false, isAsyncLoadLauncherData());
		}
		// For handling default keys
		try{
			mDefaultKeySsb = new SpannableStringBuilder();
			Selection.setSelection(mDefaultKeySsb, 0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动服务
	 */
	void bindService() {
		
	}
	
	/**
	 * 先填充0屏 <br>
	 * Author:ryan <br>
	 * Date:2013-1-19下午06:43:00
	 */
	private void setupZeroView() {
		if (mZeroViewGroup != null)
			return;

		mZeroViewGroup = new SingleViewGroup(this);
		if (mWorkspaceLayer.getChildCount() <= 1) {
			mWorkspaceLayer.addView(mZeroViewGroup, 0);
			mWorkspaceLayer.setZeroView();
			mWorkspaceLayer.setLoadZeroView(true);
		}
		inflateZeroView();
	}
	
	public void setupDeleteZone() {
		if(mDeleteZone == null){			
			mDeleteZone = inflateDeleteZone();
			if(mDeleteZone != null){
				mDeleteZone.setLauncher(this);
				mDeleteZone.setDragController(mDragController);
			}
		}else{
			mDeleteZone.reset();
			mDeleteZone.bringToFront();
		}
	}
	
	public DeleteZone getDeleteZone() {
		return mDeleteZone;
	}

	public ViewGroup getZeroViewGroup() {
		return mZeroViewGroup;
	}
	
	/**
	 * 记录用户启动时间
	 * @author Ryan
	 */
	private void recordLauncherCreateTime() {
		// 保存桌面显示完成的时间
		BaseConfigPreferences.getInstance().setLauncherCreateTime(System.currentTimeMillis());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// 清除可能存在的celllayout图标移动动画
		mWorkspace.cleanReorderAllState();
		mWorkspace.restoreFolderAnimation();
		if(mDockbar != null){			
			mDockbar.restoreReorderEx();
		}
		refreshWorkspaceSpringScreen();
		if (isRestoring()) {
			final int[] countXY = BaseSettingsPreference.getInstance().getScreenCountXY();
			if (countXY[0] != CellLayoutConfig.getCountX() || countXY[1] != CellLayoutConfig.getCountY()) {
				// 重置CellLayout的XY行列数
				CellLayoutConfig.resetXYCount(countXY[0], countXY[1]);
				mWorkspace.reLayoutAllCellLayout();
			}
			setRestoring(false);
		}

		// 如果之前进行卸载应用操作，删除该应用在桌面和匣子里的图标
		if (uninstallPackageName != null) {
			mLauncherBinder.bindAppsRemoved(uninstallPackageName);
			uninstallPackageName = null;
		}
		
		// 在屏幕预览模式下进入其他界面返回，刷新屏幕预览界面，解决添加按钮不显示的问题 caizp 2013-9-11
		if (null != previewEditController && previewEditController.isPreviewMode()) {
			previewEditController.refreshPreviewView();
		}
				
		if(!isOnSpringMode() && BaseConfigPreferences.getInstance().hasSpringAddScreen()){
			int index = mWorkspace.getChildCount() - 1;
			if(mWorkspace.getCellLayoutAt(index).getChildCount() == 0){                				
				BaseConfigPreferences.getInstance().setHasSpringAddScreen(false);
				mWorkspace.removeScreenFromWorkspace(index);
			}
		}
		
//		if (Build.VERSION.SDK_INT >= 14) {// android4.0以上，壁纸自绘
//			BaseConfig.isDrawWallPaper = true;
//			WallpaperInfo infoHelper = mWallpaperHelper.getWallpaperManager().getWallpaperInfo();
//			if (infoHelper != null) {
//				BaseConfig.isDrawWallPaper = false;
//			}
//		}
		int wh[];
		wh=ScreenUtil.getScreenWH();
		if (wh[0] == 320 && wh[1] == 480 ) {
			BaseConfig.isDrawWallPaper = true;
			WallpaperInfo infoHelper = mWallpaperHelper.getWallpaperManager().getWallpaperInfo();
			if (infoHelper != null) {
				BaseConfig.isDrawWallPaper = false;
			}
		}
		
		if (reLayoutWorkspaceAndDockbar) {
			showOrHideDockBarText();
			reLayoutWorkspaceAndDockbar = false;
		}
		
		if(!updateViewLayoutOnWindowLevel() && !LauncherConfig.getLauncherHelper().showCustomGuide(this)){
			StatusBarUtil.toggleStateBar(this, true);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// 用户统计数据上传
		LauncherConfig.getLauncherHelper().startUpHiAnalytics(this);
		
		LauncherBroadcastControl.sendBrocdcastLauncherOnstart(this);//桌面启动广播
		
		
	}
	
	/**
	 * 桌面数据是否加载完成 <br>
	 * Author:ryan <br>
	 * Date:2012-11-5下午06:08:26
	 */
	public boolean hasLoadWorkspace() {
		synchronized (hasLoadWorkspaceLock) {
			return hasLoadWorkspace;
		}
	}

	public void setHasLoadWorkspace(boolean hasLoadWorkspace) {
		synchronized (hasLoadWorkspaceLock) {
			this.hasLoadWorkspace = hasLoadWorkspace;
		}
	}
	
	public boolean hasSetupWorkspace() {
		synchronized (hasSetupWorkspaceLock) {
			return hasSetupWorkspace;
		}
	}

	public void setHasSetupWorkspace(boolean hasSetupWorkspace) {
		synchronized (hasSetupWorkspaceLock) {
			this.hasSetupWorkspace = hasSetupWorkspace;
		}
	}
	
	public void setIsFinishBinding(boolean isFinish) {
		mIsFinishBinding = isFinish;
	}
	
	public boolean isFinishBinding() {
		return mIsFinishBinding;
	}
	
	public boolean isWorkspaceLoading() {
		return mWorkspaceLoading;
	}

	public void setWorkspaceLoading(boolean mWorkspaceLoading) {
		this.mWorkspaceLoading = mWorkspaceLoading;
	}
	
	/**
	 * 是否允许绑定桌面 <br>
	 * Author:ryan <br>
	 * Date:2012-11-5下午06:08:04
	 */
	public boolean allowToBindWorkspace() {
		synchronized (startBindWorkspaceLock) {
			if (!hasLoadWorkspace() || startBindWorkspace) {
				return false;
			} else {
				startBindWorkspace = true;
				return true;
			}
		}
	}
	
	
	public Bundle getSavedState() {
		return mSavedState;
	}

	public void setSavedState(Bundle mSavedState) {
		this.mSavedState = mSavedState;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Do not call super here
//		mSavedInstanceState = savedInstanceState;
	}
	
	public BaseDragController getDragController() {
		return mDragController;
	}
	
	public DragLayer getDragLayer(){
		return mDragLayer;
	}
	
	public BaseLauncherModel getLauncherModel() {
		return mModel;
	}
	
	public void setWorkspaceLocked(boolean locked){
		isWorkspaceLocked = locked;
	}
	
	public boolean isWorkspaceLocked() {
		return isWorkspaceLocked;
	}
	
	public void lockSnapToScreenTmp(){
		lockSnapToScreenTmp(500);
	}
	
	public void lockSnapToScreenTmp(int duration){
		if(mWorkspace != null){			
			isWorkspaceLocked = true;
			mWorkspace.postDelayed(new Runnable(){
				@Override
				public void run() {
					isWorkspaceLocked = false;
				}
			}, duration);
		}
		
		if(mWorkspaceLayer != null){
			mWorkspaceLayer.setLockSnapToScreen(true);
			mWorkspaceLayer.postDelayed(new Runnable(){
				@Override
				public void run() {
					mWorkspaceLayer.setLockSnapToScreen(false);
				}
			}, duration);
		}
	}
	
	public void delayRefreshWorkspaceSpringScreen(int interval) {
		if (mWorkspace.isOnSpringMode()) {
			mWorkspace.delayRefreshSpringScreen(interval);
		}
	}
	
	public WorkspaceLayer getWorkspaceLayer() {
		return mWorkspaceLayer;
	}
	
	public BaseMagicDockbar getDockbar() {
		return mDockbar;
	}
	
	public BaseLineLightBar getLightbar() {
		return lightbar;
	}
	
	public LauncherAppWidgetHost getAppWidgetHost() {
		return mAppWidgetHost;
	}
	
	public LauncherBinder getLauncherBinder() {
		return mLauncherBinder;
	}
	
	public void setShowLoadingProgress(boolean isShowLoadingProgress) {
		mLauncherBinder.setShowLoadingProgress(isShowLoadingProgress);
	}
	
	public AppWidgetManager getAppWidgetManager() {
		return mAppWidgetManager;
	}
	
	public String getTypedText() {
		return mDefaultKeySsb.toString();
	}

	public void clearTypedText() {
		try{
			mDefaultKeySsb.clear();
			mDefaultKeySsb.clearSpans();
			Selection.setSelection(mDefaultKeySsb, 0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public SpannableStringBuilder getDefaultKeySsb() {
		return mDefaultKeySsb;
	}

	public boolean isRestoring() {
		return mRestoring;
	}

	public void setRestoring(boolean mRestoring) {
		this.mRestoring = mRestoring;
	}
	
	/**
	 * 语言变化不做动作，先屏蔽 <br>
	 * Author:ryan <br>
	 * Date:2012-10-31上午11:33:33
	 * 
	 * @deprecated
	 */
	void checkForLocaleChange() {
		final LocaleConfiguration localeConfiguration = new LocaleConfiguration();
		readConfiguration(this, localeConfiguration);

		final Configuration configuration = getResources().getConfiguration();

		final String previousLocale = localeConfiguration.locale;
		final String locale = configuration.locale.toString();

		final int previousMcc = localeConfiguration.mcc;
		final int mcc = configuration.mcc;

		final int previousMnc = localeConfiguration.mnc;
		final int mnc = configuration.mnc;

		boolean localeChanged = !locale.equals(previousLocale) || mcc != previousMcc || mnc != previousMnc;

		if (localeChanged) {
			localeConfiguration.locale = locale;
			localeConfiguration.mcc = mcc;
			localeConfiguration.mnc = mnc;

			writeConfiguration(this, localeConfiguration);
			mIconCache.flush();
		}
	}

	private static class LocaleConfiguration {
		public String locale;
		public int mcc = -1;
		public int mnc = -1;
	}

	private static void readConfiguration(Context context, LocaleConfiguration configuration) {
		DataInputStream in = null;
		try {
			in = new DataInputStream(context.openFileInput(PREFERENCES));
			configuration.locale = in.readUTF();
			configuration.mcc = in.readInt();
			configuration.mnc = in.readInt();
		} catch (FileNotFoundException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	private static void writeConfiguration(Context context, LocaleConfiguration configuration) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(context.openFileOutput(PREFERENCES, MODE_PRIVATE));
			out.writeUTF(configuration.locale);
			out.writeInt(configuration.mcc);
			out.writeInt(configuration.mnc);
			out.flush();
		} catch (FileNotFoundException e) {
			// Ignore
		} catch (IOException e) {
			// noinspection ResultOfMethodCallIgnored
			context.getFileStreamPath(PREFERENCES).delete();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}
	
	public void setUninstallPackageName(String uninstallPackageName) {
		this.uninstallPackageName = uninstallPackageName;
	}
	
	/**
	 * Description: 刷新编辑模式页面 Author: guojy Date: 2012-7-26 下午05:05:00
	 */
	public void refreshWorkspaceSpringScreen() {
		if (mWorkspace.isOnSpringMode()) {
			mWorkspace.refreshSpringScreen();
		}
	}
	
	/**
	 * Description: 是否处于屏幕编辑模式 Author: guojy Date: 2012-8-17 下午04:54:06
	 */
	public boolean isOnSpringMode() {
		return mWorkspace.isOnSpringMode();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		// Flag the loader to stop early before switching
		mModel.stopLoader();
		return Boolean.TRUE;
	}
	
	/**
	 * Restores the previous state, if it exists.
	 * 
	 * @param savedState
	 *            The previous state. 减少加载时间，先屏蔽
	 * @deprecated
	 */
	void restoreState(Bundle savedState) {
//		if (savedState == null) {
//			return;
//		}
//
//		final boolean allApps = savedState.getBoolean(RUNTIME_STATE_ALL_APPS_FOLDER, false);
//		if (allApps) {
//			showAllApps(false);
//		}
//
//		final int currentScreen = savedState.getInt(RUNTIME_STATE_CURRENT_SCREEN, -1);
//		if (currentScreen > -1) {
//			mWorkspace.setCurrentScreen(currentScreen);
//		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		
//		outState.putInt(RUNTIME_STATE_CURRENT_SCREEN, mWorkspace.getCurrentScreen());
//		// should not do this if the drawer is currently closing.
//		if (isAllAppsVisible()) {
//			outState.putBoolean(RUNTIME_STATE_ALL_APPS_FOLDER, true);
//		}
//
//		if (mFolderInfo != null && isWorkspaceLocked()) {
//			outState.putBoolean(RUNTIME_STATE_PENDING_FOLDER_RENAME, true);
//			outState.putLong(RUNTIME_STATE_PENDING_FOLDER_RENAME_ID, mFolderInfo.id);
//		}
	}
	
	public WallpaperHelper getWallpaperHelper() {
		return mWallpaperHelper;
	}
	
	public void showNavigation(){
		getWorkspaceLayer().showZeroView();
	}
	
	public void hideNavigation(){
		getWorkspaceLayer().hideZeroView();
	}
	
	public void showBottomContainer(){
		bottomContainer.setVisibility(View.VISIBLE);
	}
	
	public void hideBottomContainer(){
		bottomContainer.setVisibility(View.INVISIBLE);
	}
	
	public ScreenViewGroup getScreenViewGroup(){
		return mWorkspace;
	}
	
	public boolean isWorkspaceVisable(){
		return mWorkspace.getVisibility() == View.VISIBLE;
	}
	
	public ViewGroup getBottomContainer() {
		return bottomContainer;
	}
	
	/**
	 * 缓存非标小部件
	 */
	public void ifNeedCache(WidgetInfo item, View view) {
		if (item.itemType != BaseLauncherSettings.Favorites.ITEM_TYPE_PANDA_WIDGET)
			return;
		
		if (!(view instanceof PandaWidgetViewContainer))
			return;
		
		PandaWidgetViewContainer result = (PandaWidgetViewContainer) view;
		if (getPackageName().equals(result.getWidgetPackage()))
			return;
		
		this.pandaWidgets.add(result);
		result.setHiViewGroup(mWorkspace);
	}
	
	/**
	 * 清理非标小部件
	 */
	public void ifNeedClearCache(View view) {
		if (view == null)
			return;
		
		if (!(view instanceof PandaWidgetViewContainer))
			return;
		
		this.pandaWidgets.remove(view);
	}
	
	public void invisiableWorkspace() {
		mWorkspaceLayer.setVisibility(View.INVISIBLE);
		mWorkspace.setVisibility(View.INVISIBLE);
		hideBottomContainer();
		if (mDeleteZone != null)
			mDeleteZone.setVisibility(View.INVISIBLE);

		mWorkspace.setFocusable(false);
		bottomContainer.setFocusable(false);
		
		mWorkspace.closeOnWorkspaceScreenListener();
	}
	
	public void visiableWorkspace() {
		mWorkspaceLayer.setVisibility(View.VISIBLE);
		mWorkspace.setVisibility(View.VISIBLE);
		if (!mWorkspace.isOnSpringMode()) {
			showBottomContainer();
		}
		mWorkspace.setFocusable(true);
		bottomContainer.setFocusable(true);
		
		mWorkspace.startOnWorkspaceScreenListener();
	}
	
	/**
	 * <br>
	 * Description: 获取屏幕预览/管理控制器 <br>
	 * Author:caizp <br>
	 * Date:2012-6-25上午10:44:13
	 * 
	 * @return
	 */
	public PreviewEditAdvancedController getPreviewEditController() {
		return previewEditController;
	}
	
	public boolean isDeleteZoneVisible() {
		if (mDeleteZone == null)
			return false;

		return mDeleteZone.getVisibility() == View.VISIBLE;
	}
	
	public void stopWidgetEdit(){
		if(widgetEditHelper != null){			
			widgetEditHelper.stopWidgetEdit();
		}
	}
	
	public boolean isOnWidgetEditMode(){
		if(widgetEditHelper == null){	
			return false;
		}
		return widgetEditHelper.mIsWidgetEditMode;
	}
	
	public boolean isPreviewMode(){
		return getPreviewEditController().isPreviewMode();
	}
	
	public boolean notActionWorkspaceLayerTouch(){
		if (isFolderOpened()) {
			closeFolder();
			return true;
		}
		if(isOnSpringMode() && isWorkspaceLocked()){
			return true;
		}
		return false;
	}
	
	public void addOnKeyDownListener(OnKeyDownListenner listener) {
		onkeydownLisList.add(listener);
	}
	
	public void addOnKeyDownListenerFirst(OnKeyDownListenner listener) {
		onkeydownLisList.add(0, listener);
	}
	
	public void removeOnKeyDownListener(OnKeyDownListenner listener) {
		onkeydownLisList.remove(listener);
	}
	
	public List<OnKeyDownListenner> getOnkeydownLisList() {
		return onkeydownLisList;
	}
	
	// We can't hide the IME if it was forced open. So don't bother
	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus);
	 * 
	 * if (hasFocus) { final InputMethodManager inputManager =
	 * (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 * WindowManager.LayoutParams lp = getWindow().getAttributes();
	 * inputManager.hideSoftInputFromWindow(lp.token, 0, new
	 * android.os.ResultReceiver(new android.os.Handler()) { protected void
	 * onReceiveResult(int resultCode, Bundle resultData) { Log.d(TAG,
	 * "ResultReceiver got resultCode=" + resultCode); } }); Log.d(TAG,
	 * "called hideSoftInputFromWindow from onWindowFocusChanged"); } }
	 */

	public boolean acceptFilter() {
		final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		return !inputManager.isFullscreenMode();
	}
	
	public View getTopShadowView() {
		if (topShadowView != null)
			return topShadowView;

		topShadowView = new View(this);
		topShadowView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		topShadowView.setBackgroundColor(Color.parseColor("#AA000000"));
		topShadowView.setVisibility(View.GONE);

		mDragLayer.addView(topShadowView);
		return topShadowView;
	}
	
	public View createCommonAppView(ItemInfo itemInfo){
		return BaseLauncherViewHelper.createCommonAppView(this, (ApplicationInfo)itemInfo);
	}
	
	public View createFolderIconTextViewFromXML(ViewGroup parent, ItemInfo itemInfo){
		return BaseLauncherViewHelper.createFolderIconTextViewFromXML(this, parent, ((FolderInfo) itemInfo));
	}
	
	@Override
	public void onBackPressed() {

	}
	
	protected void fixDockBarLayout() {
		if (BaseConfig.isOnScene()) {
			return;
		}

		int bottomM = DockbarCellLayoutConfig.getMarginBottom();

		LauncherLoaderHelper launcherHelper = LauncherConfig.getLauncherHelper();
		int dockbarHeight = launcherHelper.getDockbarHeight(this);
		int linebarHeight = launcherHelper.getLinebarHeight(this);
		ViewGroup.MarginLayoutParams dockbarLp = (MarginLayoutParams) mDockbar.getLayoutParams();
		dockbarLp.height = dockbarHeight;
		FrameLayout.LayoutParams bottomContainerLp = (android.widget.FrameLayout.LayoutParams) bottomContainer.getLayoutParams();
		bottomContainerLp.height = dockbarHeight + linebarHeight;
		if (!launcherHelper.isShowDockbarText() || bottomM != 0) {
			int margin = launcherHelper.isShowDockbarText() ?
					0 : -DockbarCellLayoutConfig.getDockBarHeightMargin(this);
			dockbarLp.height += margin;
			dockbarLp.bottomMargin = bottomM;
			mDockbar.requestLayout();

			bottomContainerLp.height += margin + bottomM;
			bottomContainer.requestLayout();
		}
	}
	
	//显示或隐藏dock上图标的title
	public void showOrHideDockBarText(){
		if(mDockbar == null || bottomContainer == null || mWorkspace == null || BaseConfig.isOnScene())
			return;
		boolean isShow = LauncherConfig.getLauncherHelper().isShowDockbarText();
		ViewGroup.LayoutParams lp = mDockbar.getLayoutParams();
		if(isShow == mDockbar.isShowAppTitle()){
			return;
		}
		mDockbar.setShowAppTitle(isShow);
		
		int margin  = isShow ? DockbarCellLayoutConfig.getDockBarHeightMargin(this) 
				: -DockbarCellLayoutConfig.getDockBarHeightMargin(this);
		lp.height += margin;
		mDockbar.changeCellLayoutConfig(DockbarCellLayoutConfig.getMarginTop() - margin);
		mDockbar.requestLayout();
		
		lp = bottomContainer.getLayoutParams();
		lp.height += margin;
		bottomContainer.requestLayout();
		
		mWorkspace.changeCellLayoutMarginBottom(CellLayoutConfig.getMarginBottom() + margin);
		mWorkspace.requestLayout();
		
		if(isOnSpringMode()){//校正编辑模式布局			
			mWorkspace.reLayoutSpringMode(margin);
		}
	}
	
	/**
	 * 重新进行Window布局，修正如输入框产生的布局异常
	 */
	public boolean updateViewLayoutOnWindowLevel(){
		if(bottomContainer == null || !mIsFinishBinding || mDragLayer == null)
			return false;
		int[] loc = new int[2];
		bottomContainer.getLocationOnScreen(loc);
		if(loc[1] + bottomContainer.getHeight() < mDragLayer.getHeight()){
			Log.e("fix dockbar", "relayout");
			final boolean toggle = BaseSettingsPreference.getInstance().isNotificationBarVisible() ? false : true;
			StatusBarUtil.toggleStateBar(BaseConfig.getBaseLauncher(), toggle);
			bottomContainer.postDelayed(new Runnable(){
				@Override
				public void run() {
					StatusBarUtil.toggleStateBar(BaseConfig.getBaseLauncher(), !toggle);
				}
			}, 700);
			return true;
		}
		return false;
	}

	/**
	 * 是否通知栏透明
	 * @return
	 */
	public boolean isTranslucentStatusBar() {
		return isTranslucentStatusBar;
	}
	
	/**
	 * 是否导航栏透明
	 * @return
	 */
	public boolean isTranslucentNavigationBar() {
		return isTranslucentNavigationBar;
	}
	
	/**
	 * Description: 桌面导航栏透明时，添加桌面背景视图,pos=0用于通用背景，pos=1用于文件夹背景
	 * Author: guojianyun_91 
	 * Date: 2015年6月19日 下午4:48:02
	 * @param view
	 * @param pos
	 */
	public void addLauncherBgView(View view, int pos){
		if(!isTranslucentNavigationBar){
			return;
		}
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		((ViewGroup) mDragLayer.getParent()).addView(view, pos, lp);
	}
	
	public void showLauncherBgView() {
		if (bgView != null) {
			bgView.setVisibility(View.VISIBLE);
		}
	}

	public void hideLauncherBgView() {
		if (bgView != null) {
			bgView.setVisibility(View.INVISIBLE);
		}
	}

	public void setLauncherBgViewColor(int color) {
		if (bgView != null) {
			bgView.setVisibility(View.VISIBLE);
			bgView.setBackgroundDrawable(null);
			bgView.setBackgroundColor(color);
		}
	}

	public void setLauncherBgViewDrawable(Drawable d) {
		if (bgView != null) {
			bgView.setBackgroundColor(0);
			bgView.setBackgroundDrawable(d);
		}
	}
	
	//===========================桌面加载时使用============================//
	/**
	 * Launcher的oncreate()开始调用时
	 */
	public void onCreateStart(){
		
	}
	
	/**
	 *  Launcher的oncreate()调用结束后
	 */
	public void onCreateEnd(){
		
	}
	
	/**
	 * 新用户初次启动桌面时
	 */
	public void setupReadMeForNewUser(){
		
	}
	
	/**
	 * 升级户启动桌面时
	 */
	public void setupReadMeForOldUser(){
		
	}
	
	//======================用于渲染桌面其它视图组件=========================//
	/**
	 * 渲染顶部删除区
	 */
	public DeleteZone inflateDeleteZone() {
		return null;
	}
	
	/**
	 * 渲染0屏内容
	 */
	public void inflateZeroView() {
		
	}
	
	/**
	 * 渲染菜单栏
	 * @return
	 */
	public View inflateLauncherMenu(){
		return null;
	}
	
	
	
	/**
	 * 是否匣子可见
	 * @return
	 */
	public boolean isAllAppsVisible() {
		return false;
	}
	
	/**
	 * 滑向0屏隐藏dock栏时调用
	 * @param vg
	 */
	public void onHideDockbarForNavigation(ViewGroup vg){
		
	}

	/**
	 * 滑向第0屏时回调
	 */
	public void onSnapToNavigation(){
		
	}
	
	/**
	 * 从第0屏滑向Workspace时回调
	 */
	public void onSnapToWorkspace(){
		
	}
	
	/**
	 * 隐藏菜单栏
	 */
	public void dismissBottomMenu(){
		
	}
	
	/**
	 * 是否打开文件夹
	 * @return
	 */
	public boolean isFolderOpened() {
		return false;
	}
	
	public BaseDeleteZoneTextView getDeleteZoneTextView(){
		return null;
	}
	
	public BaseDeleteZoneTextView getUninstallZoneTextView(){
		return null;
	}

	public BaseDeleteZoneTextView getHintZoneTextView(){
		return null;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	public void closeFolder() {
		
	}

	public BaseDragController createDragController(){
		return null;
	}
	
	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	
	public void onScrollerFinished(){
		
	}
	
	//=========================以下接口用于当app安装/卸载/桌面加载时，重新绑定小部件、匣子内app=========================//
	public void updatePandaWidget(String packageName){
		
	}
	
	public void removePandaWidget(String packageName){
		
	}
	
	public void bindAllAppsForDrawer(List<ApplicationInfo> apps){
		
	}
	
	/**
	 * Description:app安装时，额外处理一些事情，如匣子里的操作
	 * Author: guojianyun_91 
	 * Date: 2015年5月18日 下午2:14:00
	 * @param apps
	 * @param packageName
	 */
	public void handleExtraOnAddApps(List<ApplicationInfo> apps, String packageName){
		
	}
	
	/**
	 * Description:app更新时，额外处理一些事情，如匣子里的操作
	 * Author: guojianyun_91 
	 * Date: 2015年5月18日 下午2:10:15
	 * @param apps
	 * @param packageName
	 */
	public void handleExtraOnUpdateApps(List<ApplicationInfo> apps, String packageName){
		
	}
	
	/**
	 * Description:app卸载时，额外处理一些事情，如匣子里的操作
	 * Author: guojianyun_91 
	 * Date: 2015年5月18日 下午2:06:12
	 * @param packageName
	 */
	public void handleExtraOnRemoveApps(String packageName){
		
	}
	
	/**
	 * 创建小部件View
	 * @param itemInfo
	 * @return
	 */
	public View createAppWidgetView(ItemInfo itemInfo){
		return null;
	}
	
	/**
	 * Description: 是否允许删除文件夹
	 * Author: guojianyun_dian91 
	 * Date: 2015年11月3日 下午4:39:50
	 * @param info
	 * @return 允许删除返回true，否则false
	 */
	public boolean isAllowDeleteFolder(FolderInfo info){
		return hasDrawer;
	}

	public void hideGoogleSearchBar(){

	}

	public void showGoogleSearchBar(){

	}
}
