package com.nd.hilauncherdev.folder.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.analysis.AppAnalysisConstant;
import com.nd.hilauncherdev.analysis.AppDistributeStatistics;
import com.nd.hilauncherdev.app.AppInfoIntentCommandAdapter;
import com.nd.hilauncherdev.app.CustomIntent;
import com.nd.hilauncherdev.app.CustomIntentSwitcherController;
import com.nd.hilauncherdev.app.IntentCommand;
import com.nd.hilauncherdev.app.SerializableAppInfo;
import com.nd.hilauncherdev.app.lisener.font.FontRefreshAssist;
import com.nd.hilauncherdev.app.lisener.font.FontRefreshLisener;
import com.nd.hilauncherdev.app.recommend.RecommendAppInfo;
import com.nd.hilauncherdev.app.ui.view.icontype.RecommendIconType;
import com.nd.hilauncherdev.app.ui.view.util.ApplicationInfoUtil;
import com.nd.hilauncherdev.app.view.FolderAppTextView;
import com.nd.hilauncherdev.appstore.AppStoreSwitchActivity;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.UpgradeFolderInfo;
import com.nd.hilauncherdev.drawer.data.DrawerDataFactory;
import com.nd.hilauncherdev.drawer.upgrade.AppUpgradeOnLauncherStartListenerImpl;
import com.nd.hilauncherdev.drawer.upgrade.UpgradeApplicationInfo;
import com.nd.hilauncherdev.folder.activity.FolderEncriptSettingActivity;
import com.nd.hilauncherdev.folder.activity.FolderEncriptTypeChooseActivity;
import com.nd.hilauncherdev.folder.activity.FolderRenameActivity;
import com.nd.hilauncherdev.folder.model.FolderEncriptHelper;
import com.nd.hilauncherdev.folder.model.FolderSwitchController;
import com.nd.hilauncherdev.folder.model.FullScreenFolderStyleHelper;
import com.nd.hilauncherdev.framework.AnyCallbacks.CommonSlidingViewCallback;
import com.nd.hilauncherdev.framework.AnyCallbacks.OnFolderDragOutCallback;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.choosedialog.AppChooseDialogActivity;
import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonLayout;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnCommonSlidingViewClickListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView.OnCommonSlidingViewLongClickListener;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonViewHolder;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.framework.view.dialog.CommonDialog;
import com.nd.hilauncherdev.framework.view.draggersliding.DraggerChooseItem;
import com.nd.hilauncherdev.framework.view.draggersliding.datamodel.DraggerSlidingViewData;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ActivityActionUtil;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.ApkInstaller;
import com.nd.hilauncherdev.kitset.util.ApkTools;
import com.nd.hilauncherdev.kitset.util.BrowserUtil;
import com.nd.hilauncherdev.kitset.util.DigestUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.PaintUtils2;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.AnythingInfo;
import com.nd.hilauncherdev.launcher.CustomChannelController;
import com.nd.hilauncherdev.launcher.DragController;
import com.nd.hilauncherdev.launcher.Launcher;
import com.nd.hilauncherdev.launcher.LauncherActivityResultHelper;
import com.nd.hilauncherdev.launcher.LauncherAnimationHelp;
import com.nd.hilauncherdev.launcher.LauncherBranchController;
import com.nd.hilauncherdev.launcher.LauncherCustomConfig;
import com.nd.hilauncherdev.launcher.LauncherSettings;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.config.IconAndTextSizeConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.touch.DragSource;
import com.nd.hilauncherdev.launcher.touch.DropTarget;
import com.nd.hilauncherdev.launcher.view.DragView;
import com.nd.hilauncherdev.launcher.view.icon.icontype.IconType;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.myphone.mycleaner.MyCleanerHelper;
import com.nd.hilauncherdev.recommend.LauncherRecommendApps;
import com.nd.hilauncherdev.recommend.RecommendManager;
import com.nd.hilauncherdev.recommend.newrecommend.RecommendNetWorkManager;
import com.nd.hilauncherdev.recommend.newrecommend.ThemeRecommendApp;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.webconnect.downloadmanage.DownloadUrlManage;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerService;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;
import com.nineoldandroids.animation.ValueAnimator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 文件夹显示视图，包括文件夹名字输入框，添加app按钮，横向滑屏的app列表
 *
 * @author pdw
 * @date 2012-5-24 下午04:39:43
 **/
public class FolderView extends RelativeLayout implements
		OnCommonSlidingViewClickListener, OnCommonSlidingViewLongClickListener,
		DropTarget , FontRefreshLisener{

	private TextView mFolderName;
	// 指示灯
	private CustomDrawableLightbar mLightbar;
	// 横向滑屏app列表
	private FolderSlidingView mFolderGirdView;
	// 主题推荐app
	//private LinearLayout themeRecommendAppView;
	//private ThemeRecommendAppView themeRecomendAppGridView;
	// 推荐文件夹为空的提醒
	private TextView recommendEmptyRemind;
	// 打开文件夹共享，指示灯图片
	private static Drawable mLightChecked;
	private static Drawable mLightNormal;
	// 最大列数
	private int mMaxCols = FolderSwitchController.MAX_COL ;
	// 最大行数
	private int mMaxRows = FolderSwitchController.MAX_ROW ;

	private int mIconSize ;
	// 文件夹内的应用数据集
	private DraggerSlidingViewData mFolderData ;
	// 文件夹信息
	private FolderInfo mFolderInfo;

	private Launcher mLauncher;

	private TextView mAddMore ;

	private TextView mEncript ;

	private TextClickListener mListener = new TextClickListener();

	private List<SerializableAppInfo> mTempSerAppsList = new ArrayList<SerializableAppInfo>();

	private int mDoubleTapTimeout ;

	//target状态
	private int state ;

	private DragController mDragController;

	private boolean isSetup = false;

	private View clickView;
	private View mLongClickView;
	private  boolean isModeForAndroid6Style=true;

	/**
	 * 是否可点击，用于在文件夹打开动画未完成前屏蔽单击事件
	 */
	private boolean isClickable = true;

	private boolean ignoreSDCardSpace = false;

	Drawable hWdrawable;

	private boolean isShowBlurBG=false;
	
	/**
	 * 文件夹内所有view的高度总和
	 * */
	private int allChildHeight=0;
	private int allChildWidth=0;
	public FolderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources res = context.getResources();
		mLightChecked = res.getDrawable(R.drawable.drawer_lightbar_checked);
		mLightNormal = res.getDrawable(R.drawable.drawer_lightbar_normal);
		if(SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN){
			mMaxCols = 3;
		}else{
			mMaxCols = res.getInteger(R.integer.workspace_cell_col);
		}
		mIconSize = IconAndTextSizeConfig.getSmallIconSize(getContext());
		if (ScreenUtil.isLowScreen()) {
			mMaxRows = 2 ;
		}
//		int childViewWidth = (int) (mIconSize * 1.68f);
		int childViewWidth = CellLayoutConfig.getCellWidth();
		mFolderData = new DraggerSlidingViewData(
				childViewWidth, mIconSize * 2, mMaxCols, mMaxRows, new ArrayList<ICommonDataItem>());
		mFolderData.setAcceptDrop(true);
		mDoubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();

		if (LauncherCustomConfig.isDefaultFolderStyle()) {
			hWdrawable = getResources().getDrawable(R.drawable.hw_folder_bg);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mFolderName = (TextView) findViewById(R.id.folder_name);
		mLightbar = (CustomDrawableLightbar) findViewById(R.id.light_bar);

		mFolderGirdView = (FolderSlidingView) findViewById(R.id.folder_scroll_view);
		mAddMore = (TextView) findViewById(R.id.add_more);
		mEncript = (TextView) findViewById(R.id.folder_encript);
		mFolderGirdView.setOnItemClickListener(this);
		mFolderGirdView.setOnItemLongClickListener(this);
		mLightbar.setNormalLighter(mLightNormal);
		mLightbar.setSelectedLighter(mLightChecked);
		mFolderGirdView.setCommonLightbar(mLightbar);
		//themeRecommendAppView = (LinearLayout) findViewById(R.id.folder_theme_recommend);
		recommendEmptyRemind = (TextView) findViewById(R.id.recommend_empty_remind);
		/** 设置点击监听器 **/
		mAddMore.setOnClickListener(mListener);
		mFolderName.setOnClickListener(mListener);
		mEncript.setOnClickListener(mListener);
		if(SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN){
			mAddMore.setVisibility(View.GONE);
		}
		if(SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_ANDROID_6){
			mAddMore.setVisibility(View.GONE);
			mEncript.setVisibility(View.GONE);
			mFolderName.setVisibility(View.GONE);
			View view = findViewById(R.id.title_layout);
			view.setVisibility(View.GONE);
		}
        mLightbar.setNormal_lighters(new Drawable[]{
                getResources().getDrawable(R.drawable.recent_install_normal),
                getResources().getDrawable(R.drawable.recent_open_normal),
                getResources().getDrawable(R.drawable.recent_use_normal)});
        mLightbar.setSelected_lighters(new Drawable[]{
                getResources().getDrawable(R.drawable.recent_install_pressed),
                getResources().getDrawable(R.drawable.recent_open_pressed),
                getResources().getDrawable(R.drawable.recent_use_pressed)
        });

        if (LauncherCustomConfig.isSimpleFolderStyle()) {
            mEncript.setVisibility(View.GONE);
            mLightbar.setVisibility(View.GONE);
        }
		if (LauncherCustomConfig.isDefaultFolderStyle()) {
			mEncript.setVisibility(View.GONE);
		}

		if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_ANDROID_6) {
			isModeForAndroid6Style = true;
		}
		if(isModeForAndroid6Style) {
			onFinishInflateForAandroid6();
		}
		refreshPaintAndView();
	}

	class TextClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
//			int analysisId = AnalyticsConstant.INVALID ;

			switch (v.getId()) {

			/**
			 * 批量添加
			 */
			case R.id.add_more:
				onAddMoreClicked(mAddMore);
				HiAnalytics.submitEvent(mLauncher, AnalyticsConstant.LAUNCHER_FOLDER_OPERATE, "t");
				break;

			/**
			 * 文件夹重命名
			 */
			case R.id.folder_name:
				if (isFolderEditable()) {
					onFolderNameClicked();
				}
				break;

			/**
			 * 文件夹加密
			 */
			case R.id.folder_encript:
				onEncritpClicked(mEncript);
				HiAnalytics.submitEvent(mLauncher, AnalyticsConstant.LAUNCHER_FOLDER_OPERATE, "j");
				break;
			case R.id.folder_name_android6:
				if (isFolderEditable()) {
					onAndroid6FolderNameClicked();
				}
				break;
			default:
				break;
			}

			/**
			 * 行为统计
			 */
//			if (analysisId != AnalyticsConstant.INVALID)
//				HiAnalytics.submitEvent(getContext(), analysisId);
		}

	}

	private boolean isFolderEditable() {
		boolean isEditable = true;

		Launcher launcher = Global.getLauncher();
		if (launcher != null) {
			FolderSwitchController controller = launcher.getFolderCotroller();
			if (controller != null) {
				isEditable = controller.isFolderEditable();
			}
		}

		return isEditable;
	}

	private void onFolderNameClicked() {
//		analysisId = AnalyticsConstant.FOLDER_RENAME ;
		mFolderName.setClickable(false);
		getHandler().postDelayed(new EnableClickRunnable(mFolderName), mDoubleTapTimeout);
		//if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {
			//renameFullStyleFolder();
		//} else {
			Intent intent = new Intent(getContext(), FolderRenameActivity.class);
			intent.putExtra("name", mFolderInfo.title);
			intent.putExtra("id", mFolderInfo.id);
			SystemUtil.startActivityForResultSafely(mLauncher, intent, LauncherActivityResultHelper.REQUEST_FOLDER_RENAME);
			mLauncher.overridePendingTransition(R.anim.zoom_enter_activity, 0);
		//}
	}

	public void onAddMoreClicked(View addMoreView) {
		if (addMoreView == null) {
			return;
		}

		if (mLauncher.getFolderCotroller().getOpenFolderFrom() == FolderSwitchController.OPEN_FOLDER_FROM_DRAWER &&
				!ConfigFactory.isInitApps(getContext())) {
			MessageUtils.makeShortToast(getContext(), R.string.drawer_apps_not_init_tips);
			return;
		}
//		analysisId = AnalyticsConstant.FOLDER_ICON_MORE ;
		addMoreView.setClickable(false);
		getHandler().postDelayed(new EnableClickRunnable(addMoreView), mDoubleTapTimeout);

		mFolderGirdView.clearDraggerChooseList();

		Intent intent = createAddMoreIntent();
		SystemUtil.startActivityForResultSafely(mLauncher,
				                                intent,
				                                LauncherActivityResultHelper.REQUEST_FOLDER_ADD_MORE);
		mLauncher.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_stay_in);
	}

	public void onEncritpClicked(View encriptView) {
		if (encriptView == null) {
			return;
		}

//		analysisId = AnalyticsConstant.FOLDER_ENCRYPT ;
		mEncript.setClickable(false);
		getHandler().postDelayed(new EnableClickRunnable(mEncript), mDoubleTapTimeout);

		Intent intent = null;
		boolean isEncripted = isFolderEncripted();
		if (!isEncripted) {
			intent = new Intent(getContext(), FolderEncriptSettingActivity.class);
		} else {
			intent = new Intent(getContext(), FolderEncriptTypeChooseActivity.class);
		}
		intent.putExtra("id", mFolderInfo.id);
		intent.putExtra("type", mLauncher.getFolderCotroller().getOpenFolderFrom());
		intent.putExtra("name", mFolderInfo.title);
		SystemUtil.startActivityForResultSafely(mLauncher,
											    intent,
				                                LauncherActivityResultHelper.REQUEST_FOLDER_ENCRIPT_CHOOSE);
		if (isEncripted) {
			mLauncher.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_stay_in);
		}
	}

	/**
	 * “更多应用”点击，跳转到应用商店
	 */
	public void onForMoreApps(View gotoView){
		Intent intent = new Intent();
		intent = new Intent(Global.getApplicationContext(), AppStoreSwitchActivity.class);
		intent.putExtra("MYACTION", 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
	}

	/**
	 * “换一批”点击
	 */
	public void onChangeApps(View changeView){
		if (!TelephoneUtil.isNetworkAvailable(getContext())) {
			MessageUtils.showOnlyToast(getContext(), R.string.soft_update_cant_connect);
			return;
		}
		List<ApplicationInfo> aInfo = LauncherRecommendApps.getInstance(mLauncher).getNewLauncherRecommendAppInfos(getContext());
		//showFolderApps(aInfo);
		int size = aInfo.size();
		List<ICommonDataItem> items = mFolderData.getDataList();
		items.clear();
		items.addAll(aInfo);

		initRecommendView(mFolderInfo);
		//mFolderData.setRowNum(getRows(size));
		//mFolderData.setColumnNum(getMaxCols());
		ArrayList<ICommonData> datas = new ArrayList<ICommonData>();
		datas.add(mFolderData);
		mFolderGirdView.setList(datas);

		afreshReckonFolderHight();
		mFolderGirdView.requestLayout();
		//mFolderGirdView.invalidate();
	}

	public boolean isFolderEncripted() {
		if (mFolderInfo != null) {
			return FolderEncriptHelper.getNewInstance().getFolderEncript(mFolderInfo.id, mLauncher.getFolderCotroller().getOpenFolderFrom()) != null;
		}

		return false;
	}

	/**
	 * 防止用户多次单击重命名，批量添加按钮
	 * @author pdw
	 */
	private class EnableClickRunnable implements Runnable{

		private View mView ;

		public EnableClickRunnable(View mView){
			this.mView = mView ;
		}

		@Override
		public void run() {
			mView.setClickable(true);
		}
	}

	private Intent createAddMoreIntent(){
		int openSrc = mLauncher.getFolderCotroller().getOpenFolderFrom();
		Intent intent = new Intent(getContext(), AppChooseDialogActivity.class);
        //判断是否是91快捷文件夹
        boolean isShortcut91Flag = isShortcut91Folder();
        if(isShortcut91Flag && openSrc == FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER){
            intent.putExtra(AppChooseDialogActivity.SHORTCUT91FLAG, true);
        }
		final List<SerializableAppInfo> selectedAppsInfoList = mTempSerAppsList ;
		selectedAppsInfoList.clear();

		for(ApplicationInfo app : mFolderInfo.contents){
//			if (openSrc == FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER &&
//                    !isShortcut91Flag && app.itemType != Favorites.ITEM_TYPE_APPLICATION) //过滤掉文件夹中的快捷方式
//				continue ;
			selectedAppsInfoList.add(app.makeSerializable());
		}

		/**
		 * 如果是匣子得过滤掉其他文件夹的app
		 */
		if(openSrc == FolderSwitchController.OPEN_FOLDER_FROM_DRAWER) {
			List<SerializableAppInfo> filterList = new ArrayList<SerializableAppInfo>() ;
			DrawerDataFactory.loadAppsInOtherFoldersOrIsHidden(getContext(),mFolderInfo,filterList);
			intent.putExtra(AppChooseDialogActivity.FILTER_LIST, (Serializable) filterList);
		}
//		intent.putExtra(AppChooseDialogActivity.TITLE, mFolderInfo.title);
		intent.putExtra(AppChooseDialogActivity.SELECTED_LIST, (Serializable) selectedAppsInfoList);

//		if (openSrc == FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER) {
//            intent.putExtra(AppChooseDialogActivity.NEED_SHORTCUT91_FLAG, true);
//		}

		return intent ;
	}

	/**
	 * 隐藏指示灯 <br>
	 * create at 2012-5-24 下午05:09:30 <br>
	 * modify at 2012-5-24 下午05:09:30
	 */
	public void invisibleLightbar() {
		mLightbar.setVisibility(GONE);
	}


	/**
	 * 显示指示灯 <br>
	 * create at 2012-5-24 下午05:09:58 <br>
	 * modify at 2012-5-24 下午05:09:58
	 */
	public void visibleLightbar() {
        if (LauncherCustomConfig.isSimpleFolderStyle()) {
            return;
        }
		mLightbar.setVisibility(VISIBLE);
	}

	/**
	 * 显示文件夹程序图标 <br>
	 * create at 2012-5-24 下午05:11:38 <br>
	 * modify at 2012-5-24 下午05:11:38
	 */
	public void showFolderApps(List<ApplicationInfo> apps) {
		int size = apps.size();
		List<ICommonDataItem> items = mFolderData.getDataList();
		items.clear();
		items.addAll(apps);
		if(hasAddAppIcon()){
			items.add(mFolderGirdView.getAddAppInfo());
			size = size + 1;
		}

		initRecommendView(mFolderInfo);
		if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_ANDROID_6) {
			mMaxCols=getSuitableCol(apps.size());
			mMaxRows=4;
			mFolderData.setColumnNum(mMaxCols);

		}
		
        if (LauncherCustomConfig.isSimpleFolderStyle()) {
            mMaxRows = size / mMaxCols;
            mMaxRows = size%mMaxCols>0?mMaxRows+1:mMaxRows;
            mFolderData.setKeepChildViewWidthAndHeight(true);
        }
		mFolderData.setRowNum(getRows(size));
		mFolderData.setColumnNum(getMaxCols());
		ArrayList<ICommonData> datas = new ArrayList<ICommonData>();
		datas.add(mFolderData);
		mFolderGirdView.go2FirstScreen();
		mLightbar.update(0);
		mFolderGirdView.setList(datas);

		afreshReckonFolderHight();
	}

	private boolean hasAddAppIcon() {
		try {
			if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_ANDROID_6) {
				return false;
			}
			return true;
//			return !LauncherShortcutHelper.getInstance().isRecentView(mLauncher.getClickView().getTag()) &&
//					!isRecommandAppFolder(mFolderInfo) && mFolderInfo.itemType != Favorites.ITEM_TYPE_UPGRADE_FOLDER;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public int getRowNum() {
		if (isRecommandAppFolder(mFolderInfo)) {// 是推荐文件夹最少要返回一行，给推荐完的文字显示
			return mFolderData.getRowNum() > 1 ? mFolderData.getRowNum() : 1;
		}
		return mFolderData.getRowNum();
	}

	/**
	 * 设置行数 <br>
	 * create at 2012-5-24 下午05:15:24 <br>
	 * modify at 2012-5-24 下午05:15:24
	 *
	 * @param
	 */
//	void setRowNum(int row) {
//		mFolderData.setRowNum(row);
//	}

	public void setChildViewHeight(int height) {
		mFolderData.setChildViewHeight(height);
	}

	/**
	 * 获取行数 <br>
	 * create at 2012-5-24 下午05:21:40 <br>
	 * modify at 2012-5-24 下午05:21:40
	 *
	 * @param size
	 * @return
	 */
	private int getRows(int size) {
		int row = size / mMaxCols;
		if (size % mMaxCols != 0) {
			row++;
		}
		if (row > mMaxRows) {
			row = mMaxRows;
		}
		return row;
	}

	@Override
	public boolean onItemLongClick(View v, int positionInData,
			int positionInScreen, int screen, ICommonData data) {
		if (mLauncher.getFolderCotroller().getFolderInfo().itemType == Favorites.ITEM_TYPE_MYPHONE_FOLDER
				|| mLauncher.getFolderCotroller().getFolderInfo().itemType == Favorites.ITEM_TYPE_UPGRADE_FOLDER) {
			return true;
		}
        CommonViewHolder holder = (CommonViewHolder) v
                .getTag(R.id.common_view_holder);

        if(holder != null && holder.item != null && holder.item instanceof ApplicationInfo) {
            ApplicationInfo info = (ApplicationInfo) holder.item;
            if (info.itemType == BaseLauncherSettings.Favorites.ITEM_TYPE_PLACE_HOLDER_APPLICATION) {
                return true;
            }
        }

		mLongClickView = v;

		if(!mLauncher.getFolderCotroller().isFolderEditable() && mLauncher.getFolderCotroller().getOpenFolderFrom() ==
				FolderSwitchController.OPEN_FOLDER_FROM_DRAWER){//处于分类预览状态 不响应单击事件											// 不响应单击事件
			return true;
		}

		if (isRecommandAppFolder(mFolderInfo)) {// 是推荐文件夹，则长按事件也变成下载
			onItemClick(v, positionInData, positionInScreen, screen, data);
			return true;
		}
		if(SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN){
			if(mFolderGirdView.getAddView() == v){
				mAddMore.performClick();
				return true;
			}
			if(!isDynamic(mFolderInfo) && mFolderGirdView.getAddView() != null){
				CommonLayout layout = mFolderGirdView.getCommonLayout(mFolderGirdView.getPage() - 1);
				layout.removeView(mFolderGirdView.getAddView());
			}
		}

		//编辑被锁定文件夹中的任何长按事件都不响应
		if (!Global.allowEdit(getContext()) || !mLauncher.isFolderOpened()) {
			return true;
		}

		if (mCallbacks != null)
			mCallbacks.onViewLongClick(v);
//		v.findViewById(R.id.animation_layout).clearAnimation();

		//if(mLauncher.getFolderCotroller().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN){
			//mFolderGirdView.settleBackground();
		//}

		if(isDynamic(mFolderInfo)){
			mLauncher.closeFolder();
			//modify by linqiang 桌面动态文件夹内图标长按后不显示垃圾桶,拖拽出来后显示垃圾桶
			/*if (mLauncher.getFolderCotroller().getOpenFolderFrom() == FolderSwitchController.OPEN_FOLDER_FROM_LAUNCHER){
				
			}*/
		}else{
			if(mLauncher.getFolderCotroller().getOpenFolderFrom() == FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER){
				if(holder.item instanceof ApplicationInfo){
					clickView = v;
					mDragController.setShowShortcutMenu(true);
					mDragController.setMenuTriggerSource(this);
				}
			}
		}

		/**
		 * Drop后是否已完成重新布局 且 DragView是否已完成动画
		 */
		if (mFolderGirdView.isNeedRelayoutAfterDrop() || mFolderGirdView.isInAnimation())
			return true;

		ApplicationInfo app = (ApplicationInfo) holder.item;
		if (mFolderGirdView.getDraggerChooseItem(app) != null) {
			/**
			 * 拖拽多项
			 */
			ArrayList<DraggerChooseItem> list = mFolderGirdView.getDraggerChooseList();
			for (DraggerChooseItem cItem : list) {
				ApplicationInfo aInfo = cItem.getInfo();
				if (aInfo == app) {
					list.remove(cItem);
					list.add(0, cItem);
					break;
				}
			}
			mFolderGirdView.startDrag(v, positionInData, positionInScreen, holder.item, list);
		} else {
			/**
			 * 拖拽单项
			 */
			mFolderGirdView.startDrag(v, positionInData, positionInScreen, holder.item);
		}

		return true;
	}

	private void actionCustomIntent(ApplicationInfo info) {
		CustomIntentSwitcherController customIntentSwitcherController = CustomIntentSwitcherController.getNewInstance();
		customIntentSwitcherController.registerCustomIntent(new AppInfoIntentCommandAdapter(info));
		customIntentSwitcherController.onAction(mLauncher,info, IntentCommand.ACTION_FROM_UNKNOW);
	}

	@Override
	public void onItemClick(final View v,int positionInData,int positionInScreen,
			int screen, ICommonData data) {
		if(!mLauncher.getFolderCotroller().isFolderEditable() && mLauncher.getFolderCotroller().getOpenFolderFrom() ==
								FolderSwitchController.OPEN_FOLDER_FROM_DRAWER){//处于分类预览状态 不响应单击事件
								// 不响应单击事件
            if(mLauncher.getFolderCotroller().getFolderInfo().itemType != Favorites.ITEM_TYPE_UPGRADE_FOLDER)
			    return;
		}

		if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {// 全屏下的添加按钮
			if(mFolderGirdView.getAddView() == v){
				mAddMore.performClick();
				return;
			}
		}

		if (!isClickable) {
			return;
		}

		final CommonViewHolder holder = (CommonViewHolder) v.getTag(R.id.common_view_holder);
		final ApplicationInfo info = (ApplicationInfo) holder.item;

        if(info.itemType == BaseLauncherSettings.Favorites.ITEM_TYPE_PLACE_HOLDER_APPLICATION) {
            return;
        }

		//编辑模式下不允许点击app
		if (mFolderGirdView.isEditMode()) {
			/**
			 * 选择程序
			 */
			boolean isSuccess = mFolderGirdView.chooseItem(data, (FolderBoxedViewGroup) v, info);
			if (!isSuccess) {
				/**
				 * 多选项个数已达到最大限制
				 */
				MessageUtils.makeShortToast(getContext(), R.string.drawer_multi_choose_reached_limit_tips);
			}
			return ;
		}

		if (!isRecommandAppFolder(mFolderInfo) && mFolderInfo.itemType != Favorites.ITEM_TYPE_UPGRADE_FOLDER) {// 是推荐文件夹占不关闭
			mLauncher.getFolderCotroller().closeFolderWithoutAnimation(false);
			Launcher.isExitFolderOnClickApp = true;
		}

		if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT
			|| info.itemType == Favorites.ITEM_TYPE_INDEPENDENCE) {
			if (isLauncherMenuShortcut(info)) {
				postDelayed(new Runnable() {
					@Override
					public void run() {
						actionCustomIntent(info);
					}
				}, FullScreenFolderStyleHelper.ANI_DURATION + 50);
			} else {
				actionCustomIntent(info);
				ActivityActionUtil.statCPCEvent(getContext(), info);
			}
		} else {
//			mLauncher.startActivitySafely(info.intent, info);
            if(mFolderInfo.itemType == Favorites.ITEM_TYPE_UPGRADE_FOLDER) { //应用升级文件夹
                //应用升级文件夹点击
                if(info != null && ApplicationInfoUtil.isUpgradeIcon(info)) {
                    final UpgradeApplicationInfo upgradeApplicationInfo = (UpgradeApplicationInfo)info;
                    if(info.componentName != null) {
                        upgradeAppOnClick(v, info, upgradeApplicationInfo);
                    }
                    return;
                }
            }
			if (!isRecommandAppFolder(mFolderInfo) || AndroidPackageUtils.isPkgInstalled(getContext(), info.componentName.getPackageName())) {
				if(!ActivityActionUtil.actionByClickPosition(v, info, this)){
					//如果是浏览器获取指定的地址
					Intent intent = BrowserUtil.getInstance().interceptLauncherApp(info);
					ActivityActionUtil.startActivityByFilter(null, mLauncher, intent);
					ActivityActionUtil.statCPCEventForDrawer(mLauncher, info);
					mLauncher.getTagViewController().setCallStateIfNecessary(info);
					try {
						CustomChannelController.eventFolderAppClick(info.componentName.getPackageName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {// 推荐文件夹点击图标操作。
				if (info == null || info.componentName == null) {
					return;
				}
				final RecommendAppInfo recommendAppInfo = LauncherRecommendApps.getInstance(mLauncher).getLauncherRecommendAppInfo(
						info.componentName.getPackageName());
				if (recommendAppInfo == null) {
					return;
				}
				if (!TelephoneUtil.isNetworkAvailable(getContext())) {
					MessageUtils.showOnlyToast(getContext(), R.string.recommend_app_download_not_network);
					return;
				}

				if (!ignoreSDCardSpace) {
					boolean isShow = MyCleanerHelper.showDialogBeforeDownloadStuff(getContext(), null,
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									ignoreSDCardSpace = true;
								}
							});
					if(isShow){
						return;
					}
				}
				// 弹出对话框
				StringBuffer title = new StringBuffer(getContext().getString(R.string.common_button_download)).append(recommendAppInfo.title);
				CommonDialog alertd = ViewFactory.getAlertDialog(getContext(), title, recommendAppInfo.description,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {// 点击确定
								//downloadRecommendFolderApp(v,info,recommendAppInfo);
								recommendAppOnClick(v, info, recommendAppInfo);
								HiAnalytics.submitEvent(getContext(), AnalyticsConstant.RECOMMEND_FOLDER_DOWNLOAD_LAUNCHER,recommendAppInfo.title);
								AppDistributeStatistics.AppDistributePercentConversionStatistics(getContext(), "104");
								dialog.dismiss();
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
						});
				if (TelephoneUtil.isWifiEnable(getContext())){//wifi环境且是sem渠道包点击就直接下载
					//downloadRecommendFolderApp(v,info,recommendAppInfo);
					recommendAppOnClick(v, info, recommendAppInfo);
				}else{
					if(isAppDownloading(info)){
						recommendAppOnClick(v, info, recommendAppInfo);
					}else{
						alertd.show();
					}
				}
				AppDistributeStatistics.AppDistributePercentConversionStatistics(getContext(), "103");

			}
		}

		if (mCallbacks != null)
			mCallbacks.onViewClick(v);

		if(!Launcher.hasDrawer){//消除无匣子查找应用后的选中态
			v.setSelected(false);
		}
	}

	private void downloadRecommendFolderApp(View v,ApplicationInfo info,RecommendAppInfo recommendAppInfo){
		// 删除推荐列表中的应用
		LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().remove(recommendAppInfo);
		// 删除文件夹中的应用数据
		CommonLayout layout = mFolderGirdView.getCommonLayout(mFolderGirdView.getPage() - 1);
		layout.removeView(v);
		// 刷新布局
		mFolderData.getDataList().remove(info);
		mFolderGirdView.reLayout();
		// 下载
		if (recommendAppInfo.iconPath!=null && !recommendAppInfo.iconPath.contains("drawable:")){
			recommendAppInfo.iconPath = BaseConfig.getBaseDir() + "/download/icon/"+ recommendAppInfo.iconPath;
		}
		if (LauncherRecommendApps.getInstance(getContext()).isLocalLauncherRecommendApp(getContext(), recommendAppInfo.packageName)){//本地自带的去取渠道7的包
			RecommendManager.download(getContext(), info.componentName.getPackageName(), recommendAppInfo , AppAnalysisConstant.SP_OLD_LAUNCHER_ICON_RECOMMEND);
		} else {
			RecommendManager.download(getContext(), info.componentName.getPackageName(), recommendAppInfo , AppAnalysisConstant.SP_NEW_LAUNCHER_ICON_RECOMMEND);
		}
		if (LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().size() == 0) {// 没有推荐的时候显示
			recommendEmptyRemind.setVisibility(View.VISIBLE);
			HiAnalytics.submitEvent(getContext(), AnalyticsConstant.RECOMMEND_FOLDER_DOWNLOAD_ALL_LAUNCHER,"zm");
		} else {
			recommendEmptyRemind.setVisibility(View.GONE);
		}
	}

    private void upgradeAppOnClick(final View v, final ApplicationInfo info, final UpgradeApplicationInfo upgradeApplicationInfo) {
        HiAnalytics.submitEvent(mLauncher, AnalyticsConstant.DRAWER_UPDATE_CLICK_FOLDER_APP);
        final String pkg = info.componentName.getPackageName();
        BaseDownloadInfo baseDownloadInfo = AppUpgradeOnLauncherStartListenerImpl.getInstance().
                getmDownloadService().
                getDownloadTask(AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg);
        if(baseDownloadInfo == null) { //开始下载
            if(!TelephoneUtil.isWifiEnable(mLauncher)) {
                ViewFactory.getAlertDialog(mLauncher, "下载", "当前处于非WiFi网络是否继续下载?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApp(v, info, upgradeApplicationInfo, pkg);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }else{
                downloadApp(v, info, upgradeApplicationInfo, pkg);
            }
        }else {
            int state = baseDownloadInfo.getState();
            switch (state) {
                case DownloadState.STATE_DOWNLOADING:
                    AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService()
                            .pause(AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg);
                    View view = v.findViewById(R.id.item_view);
                    if(view instanceof FolderAppTextView) {
                        ((FolderAppTextView)view).setDownloadState(DownloadState.STATE_PAUSE);
                    }
                    break;
                case DownloadState.STATE_PAUSE:
                case DownloadState.STATE_CANCLE:
                    AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService()
                            .continueDownload(AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg);
                    view = v.findViewById(R.id.item_view);
                    if(view instanceof FolderAppTextView) {
                        ((FolderAppTextView)view).setDownloadState(DownloadState.STATE_DOWNLOADING);
                    }
                    break;
                case DownloadState.STATE_FINISHED:
                    String filePath = BaseConfig.WIFI_DOWNLOAD_PATH + AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg+".apk";
                    try {
                        if (ApkTools.checkApkIfValidity(mLauncher, filePath)) {
                            ApkInstaller.installApplicationNormal(mLauncher, new File(filePath));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view = v.findViewById(R.id.item_view);
                    if(v instanceof FolderAppTextView) {
                        ((FolderAppTextView)v).setDownloadState(DownloadState.STATE_INSTALLED);
                    }
                    break;
            }
        }
    }

    /**
	 * 推荐应用图标点击
	 * @param v
	 * @param info
	 * @param recommendAppInfo
	 */
	private void recommendAppOnClick(final View v, final ApplicationInfo info, final RecommendAppInfo recommendAppInfo) {
		final String pkg = info.componentName.getPackageName();
		BaseDownloadInfo baseDownloadInfo = AppUpgradeOnLauncherStartListenerImpl.getInstance().
				getmDownloadService().
				getDownloadTask(DownloadServerService.RECOMMEND_PREFIX + pkg);

		int sp = 0;
		if (LauncherRecommendApps.getInstance(getContext()).isLocalLauncherRecommendApp(getContext(), recommendAppInfo.packageName)){//本地自带的去取渠道7的包
			sp =  AppAnalysisConstant.SP_OLD_LAUNCHER_ICON_RECOMMEND;
		} else {
			sp = AppAnalysisConstant.SP_NEW_LAUNCHER_ICON_RECOMMEND;
		}
		final int tmpSp = sp;
		View view = v.findViewById(R.id.item_view);
		if (view instanceof FolderAppTextView) {
			FolderAppTextView folderView = (FolderAppTextView) view;
			IconType iconType = folderView.getIconType();
			if (iconType != null && iconType instanceof RecommendIconType) {
				((RecommendIconType) iconType).setDownloadCallback(new RecommendIconType.RecommendAppDownloadCallback() {
					@Override
					public void onDownload(int state) {
						if (state == DownloadState.STATE_FINISHED && mFolderGirdView.getPage() > 0) {
							// 删除推荐列表中的应用
							LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().remove(recommendAppInfo);
							// 删除文件夹中的应用数据
							CommonLayout layout = mFolderGirdView.getCommonLayout(mFolderGirdView.getPage() - 1);
							layout.removeView(v);
							// 刷新布局
							mFolderData.getDataList().remove(info);
							mFolderGirdView.reLayout();
						}
					}
				});
			}
		}
		if(baseDownloadInfo == null) { //开始下载
			downloadApp(v, info, recommendAppInfo, pkg, tmpSp);
		}else {
			int state = baseDownloadInfo.getState();
			switch (state) {
				case DownloadState.STATE_START:
				case DownloadState.STATE_DOWNLOADING:
					AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService()
							.pause(DownloadServerService.RECOMMEND_PREFIX + pkg);
					view = v.findViewById(R.id.item_view);
					if(view instanceof FolderAppTextView) {
						((FolderAppTextView)view).setDownloadState(DownloadState.STATE_PAUSE);
					}
					break;
				case DownloadState.STATE_PAUSE:
				case DownloadState.STATE_CANCLE:
					AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService()
							.continueDownload(DownloadServerService.RECOMMEND_PREFIX + pkg);
					view = v.findViewById(R.id.item_view);
					if(view instanceof FolderAppTextView) {
						((FolderAppTextView)view).setDownloadState(DownloadState.STATE_DOWNLOADING);
					}
					break;
				case DownloadState.STATE_FINISHED:
					String filePath = BaseConfig.WIFI_DOWNLOAD_PATH + DownloadServerService.RECOMMEND_PREFIX + pkg+".apk";
					//点击时，如果原先包已下载完成，但是被意外删除了，那么重新起一个下载
					if(!FileUtil.isFileExits(filePath)){
						AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService().cancel(DownloadServerService.RECOMMEND_PREFIX + pkg);
						downloadApp(v, info, recommendAppInfo, pkg, tmpSp);
						return;
					}
					if(mFolderGirdView.getPage() < 1){
						return ;
					}
					// 删除推荐列表中的应用
					LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().remove(recommendAppInfo);
					// 删除文件夹中的应用数据
					CommonLayout layout = mFolderGirdView.getCommonLayout(mFolderGirdView.getPage() - 1);
					layout.removeView(v);
					// 刷新布局
					mFolderData.getDataList().remove(info);
					mFolderGirdView.reLayout();
					try {
						if (ApkTools.checkApkIfValidity(mLauncher, filePath)) {
							ApkInstaller.installApplicationNormal(mLauncher, new File(filePath));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
			}
		}
//		if (LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().size() == 0) {// 没有推荐的时候显示
//			recommendEmptyRemind.setVisibility(View.VISIBLE);
//			HiAnalytics.submitEvent(getContext(), AnalyticsConstant.RECOMMEND_FOLDER_DOWNLOAD_ALL_LAUNCHER, "zm");
//		} else {
//
//		}
		recommendEmptyRemind.setVisibility(View.GONE);
	}

	/**
	 * 判断应用当前是否在下载状态
	 * @param info
	 * @return
	 */
	private boolean isAppDownloading(ApplicationInfo info){
		final String pkg = info.componentName.getPackageName();
		BaseDownloadInfo baseDownloadInfo = AppUpgradeOnLauncherStartListenerImpl.getInstance().
				getmDownloadService().
				getDownloadTask(DownloadServerService.RECOMMEND_PREFIX + pkg);
		if(baseDownloadInfo == null){//此时还未添加下载任务
			return false;
		}
		return true;
	}

    private void downloadApp(final View v, final ApplicationInfo info, final UpgradeApplicationInfo upgradeApplicationInfo, final String pkg) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                String iconUrl = upgradeApplicationInfo.getUpdateAppInfo().getIconUrl();
                String iconFilePath = "";
                if (!StringUtil.isEmpty(iconUrl)) {
                    iconFilePath = RecommendNetWorkManager.IconSavePath + "/" + RecommendNetWorkManager.getIconFromPath(Global.getApplicationContext(),
                            upgradeApplicationInfo.componentName.getPackageName(), iconUrl);
                }
                final BaseDownloadInfo dlInfo = new BaseDownloadInfo(AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg,
                        FileType.FILE_APK.getId(), upgradeApplicationInfo.getUpdateAppInfo().getDownloadUrl(),
                        info.title.toString(),
                        BaseConfig.WIFI_DOWNLOAD_PATH, AppUpgradeOnLauncherStartListenerImpl.UPGRADE_PREFIX + pkg + ".apk",
                        iconFilePath);

                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService().addDownloadTask(dlInfo);
                        View view = v.findViewById(R.id.item_view);
                        if (view instanceof FolderAppTextView) {
                            ((FolderAppTextView) view).setDownloadState(DownloadState.STATE_DOWNLOADING);
                        }
                    }
                });

                //开始调用回调接口
                if (upgradeApplicationInfo.getUpdateAppInfo() != null &&
                        !StringUtil.isEmpty(upgradeApplicationInfo.getUpdateAppInfo().getCallbackUrl())) {
                    final String callbackUrl = upgradeApplicationInfo.getUpdateAppInfo().getCallbackUrl();
                    HttpCommon httpCommon = new HttpCommon(callbackUrl);
                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                    int ts = ((int)(Math.random()*10));
                    paramsMap.put("sign", DigestUtil.md5Encode(ts + "hiziyuan"));
                    HashMap<String, String> headerMap = new HashMap<String, String>();
                    headerMap.put("ts", ts+"");
                    httpCommon.executeRequestIgnoreResponse(paramsMap, headerMap);
                }
            }
        });
    }

    /**
	 * 推荐应用下载
	 * @param v
	 * @param info
	 * @param recommendAppInfo
	 * @param pkg
	 * @param sp
	 */
	private void downloadApp(final View v, final ApplicationInfo info, final RecommendAppInfo recommendAppInfo, final String pkg, final int sp) {
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				String iconUrl = recommendAppInfo.iconPath;
				if (recommendAppInfo.iconPath != null && !recommendAppInfo.iconPath.contains("drawable:")) {
					recommendAppInfo.iconPath = BaseConfig.getBaseDir() + "/download/icon/" + recommendAppInfo.iconPath;
				}
				String downloadUrl = DownloadUrlManage.getDownloadUrlFromPackageName(getContext(), pkg, null, sp);
				final BaseDownloadInfo dlInfo = new BaseDownloadInfo(DownloadServerService.RECOMMEND_PREFIX + pkg,
						FileType.FILE_APK.getId(), downloadUrl,
						info.title.toString(),
						BaseConfig.WIFI_DOWNLOAD_PATH, DownloadServerService.RECOMMEND_PREFIX + pkg + ".apk",
						recommendAppInfo.iconPath);
				dlInfo.setDisId(pkg);
				dlInfo.setDisSp(sp);

				getHandler().post(new Runnable() {
					@Override
					public void run() {
						AppUpgradeOnLauncherStartListenerImpl.getInstance().getmDownloadService().addDownloadTask(dlInfo);
						View view = v.findViewById(R.id.item_view);
						if (view instanceof FolderAppTextView) {
							((FolderAppTextView) view).setDownloadState(DownloadState.STATE_DOWNLOADING);
						}
					}
				});
			}
		});
	}

    /**
	 * 是否为“桌面菜单”快捷方式
	 */
	private boolean isLauncherMenuShortcut(ApplicationInfo info) {
		return (info != null
				 && info.itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT
				 && info.intent != null
				 && info.intent.getAction() != null
				 && info.intent.getAction().equals(CustomIntent.ACTION_OPEN_DESKTOP_MENU));
	}

//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		int sc = -1;
//		if (Global.iconMask != null/* && SettingsPreference.getInstance().isIconMaskEnabled()*/)
//			sc = canvas.saveLayer(this.getLeft(), 0, this.getRight(), this.getBottom(), null, Canvas.ALL_SAVE_FLAG);
//
//		super.dispatchDraw(canvas);
//
//		if (sc != -1 && Global.iconMask != null /*&& SettingsPreference.getInstance().isIconMaskEnabled()*/)
//			canvas.restoreToCount(sc);
//	}

	/**
	 * @param mLauncher
	 *            the mLauncher to set
	 */
	public void setLauncher(Launcher mLauncher) {
		this.mLauncher = mLauncher;
		mFolderGirdView.setLauncher(mLauncher);
	}

	/**
	 * @param mDragController
	 *            the mDragController to set
	 */
	public void setDragController(DragController mDragController) {
		this.mDragController = mDragController;
		mFolderGirdView.setDragController(mDragController);
	}

	public void setupDragController() {
		if (!isSetup) {
			this.mDragController.addDragScoller(mFolderGirdView);
			// 设置droptarget
			this.mDragController.addDropTarget(this);
			this.mDragController.addDropTarget(mFolderGirdView);
			isSetup = true;
		}
	}

	/**
	 * @return the mMaxCols
	 */
	public int getMaxCols() {
		return mMaxCols;
	}

	/**
	 * @return the mMaxRows
	 */
	public int getMaxRows() {
		return mMaxRows;
	}

	/**
	 * 绑定文件夹信息 <br>
	 * create at 2012-5-29 上午11:09:18 <br>
	 * modify at 2012-5-29 上午11:09:18
	 *
	 * @param folderInfo
	 * @param focusIndex 文件夹中需被高亮的app索引
	 */
	public void bind(FolderInfo folderInfo,int focusIndex) {
		this.mFolderInfo = folderInfo;
		mFolderGirdView.bind(folderInfo,focusIndex);
		mFolderName.setText(folderInfo.title);
		if (folderNameForAndroid6 != null) {
			folderNameForAndroid6.setText(folderInfo.title);
		}
	}

	public void onClose() {
		if (mFolderInfo != null) {
			mFolderInfo.opened = false;
		}
		//移除主题推荐内容
		//themeRecommendAppView.removeAllViews();
		//themeRecomendAppGridView = null;
		//themeRecommendAppView.setVisibility(View.GONE);
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public void onDrop(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {

	}

	@Override
	public void onDragEnter(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		mFolderGirdView.onDragExit(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}

	@Override
	public void onDragOver(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {

	}

	@Override
	public void onDragExit(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
	}

	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		return false;
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			setState(AVAIABLE);
			if (Global.getLauncher() != null && Global.getLauncher().topMenuDrawstringView != null){
				Global.getLauncher().topMenuDrawstringView.hideDrawstring();
			}
		} else {
			setState(UNAVAIABLE);
			if (Global.getLauncher() != null && Global.getLauncher().topMenuDrawstringView != null
					&& Global.getLauncher().getDrawer()!= null && !Global.getLauncher().getDrawer().isVisible()){
				Global.getLauncher().topMenuDrawstringView.showDrawstring();
			}
		}
		mFolderGirdView.setVisibility(visibility);
	}

	private CommonSlidingViewCallback mCallbacks;

	public void setCallback(CommonSlidingViewCallback callback) {
		mCallbacks = callback;
	}

	/**
	 * @return the mIsEditMode
	 */
//	public boolean isEditMode() {
//		return mFolderGirdView.isEditMode();
//	}

	/**
	 * @param mIsEditMode
	 *            the mIsEditMode to set
	 */
	public void setEditMode(boolean mIsEditMode) {
		mFolderGirdView.setIsEditMode(mIsEditMode);
	}

	public void setDragOutCallback(OnFolderDragOutCallback callback){
		mFolderGirdView.setDragoutCallback(callback);
	}

	public void setTitle(String name) {
		mFolderName.setText(name);
		if (folderNameForAndroid6 != null) {
			folderNameForAndroid6.setText(name);
		}
	}


//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		/**
//		 * 拦截掉文件夹内没有view响应的触屏事件，防止误操作关闭文件夹
//		 */
//		return true ;
//	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state ;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public FolderInfo getUserFolderInfo(){
		return mFolderInfo;
	}

	public FolderSlidingView getFolderSlidingView(){
		return mFolderGirdView;
	}

	public void removeCloseFolderRunnable() {
		mFolderGirdView.removeCloseFolderRunnable();
	}

	/**
	 * 显示长按菜单
	 * @author dingdj
	 * @createtime 2013-7-17
	 */
	public void showShortcutMenu(){
		if(clickView != null){
//			ItemInfo item = (ItemInfo) clickView.getTag();
//			if(!needShowShortcutMenu(mLauncher, item)) return;
			mDragController.showShortcutMenu(null, clickView, this);
			clickView = null;
		}
	}

	public void setMaxCols(int maxCols) {
		this.mMaxCols = maxCols;
	}

	/**
	 * 刷新视图
	 */
	public void refresh() {
		if (shouldShowLightbar()) {
			if (!FolderView.isRecommandAppFolder(mFolderInfo)) {
				visibleLightbar();
			}
		} else {
			invisibleLightbar();
		}
	}

	/**
	 * 获取文件夹视图布局页数
	 * @param appCount 文件夹内的应用数
	 * @return 文件夹视图布局页数
	 */
	private int getPageCount(int appCount) {
		if (appCount <= 1)
			return 0 ;
		return 1 + (appCount - 1 ) / (getMaxCols() * getMaxRows()) ;
	}

	/**
	 * 是否显示指示器
	 */
	public boolean shouldShowLightbar() {
		if (mFolderInfo == null)
			return false ;
		int size = mFolderInfo.getSize();
		if(hasAddAppIcon()){
			size += 1;
		}

		return getPageCount(size) > 1 ;
	}

	private void renameFullStyleFolder(){
		final TextView save = (TextView) findViewById(R.id.folder_rename_ok);
		final EditText edit = (EditText) findViewById(R.id.edit_folder_name);
		save.setVisibility(View.VISIBLE);
		edit.setVisibility(View.VISIBLE);
		mEncript.setVisibility(View.GONE);
		mFolderName.setVisibility(View.GONE);
		edit.setText(mFolderInfo.title);
		Editable editable = edit.getText();
		Selection.setSelection(editable, 0,editable.length());
		edit.setFocusable(true);
		edit.requestFocus();

		final View mEtSelection = findViewById(R.id.edit_name_selection);
		mEtSelection.setVisibility(View.VISIBLE);

		((InputMethodManager) mLauncher.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edit,InputMethodManager.SHOW_FORCED);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((InputMethodManager) mLauncher.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit.getWindowToken(),0);
//				if (mLauncher.isFolderOpened()) {
                String title = edit.getText().toString();
                if(!StringUtil.isEmpty(title)) {
                    mLauncher.getFolderCotroller().renameFolder(title);
                }
//				} else {
//					mLauncher.renameFolder(id, edit.getText().toString());
//				}
				save.setVisibility(View.GONE);
				edit.setVisibility(View.GONE);
				mEtSelection.setVisibility(View.GONE);
				if (!isDynamic(mFolderInfo)
						&& mFolderInfo.itemType != LauncherSettings.Favorites.ITEM_TYPE_THEME_APP_FOLDER
						&& mFolderInfo.itemType != LauncherSettings.Favorites.ITEM_TYPE_LAUNCHER_RECOMMEND_FOLDER){//这行是为了兼容5.6旧的推荐文件夹

//					if (SettingsPreference.getInstance().getFolderStyle() != FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {	
//                    if (!LauncherCustomConfig.isCustomMade()) {
//                        mEncript.setVisibility(View.VISIBLE);
//                    }
//					}
				}
				mFolderName.setVisibility(View.VISIBLE);
			}
		});
	}

	public static boolean isDynamic(FolderInfo folder) {
		if (folder == null) {
			return false;
		}

		return (folder.getProxyView() != null && folder.getProxyView().getTag() instanceof AnythingInfo);
	}

	/**
	 * 检测是否推荐文件夹
	 * <p>Title: isRecommandAppFolder</p>
	 * <p>Description: </p>
	 * @param folder
	 * @return
	 * @author maolinnan_350804
	 */
	public static boolean isRecommandAppFolder(FolderInfo folder) {
		if (folder == null) {
			return false;
		}

		if (folder.getProxyView() != null && folder.getProxyView().getTag() instanceof AnythingInfo) {// 是anything类型
			if (((AnythingInfo) (folder.getProxyView().getTag())).flag == AnythingInfo.FLAG_FOLDER_RECOMMEND_APPS) {// 是推荐文件夹
				return true;
			}
		}
		return false;
	}

    /**
     * 检测是否应用升级文件夹
     */
    public static boolean isUpgradeAppFolder(FolderInfo folder) {
        if (folder == null) {
            return false;
        }
        return  (folder instanceof UpgradeFolderInfo);
    }

	public static boolean hasThemeRecommandApp(Context context, FolderInfo folder) {
		if (context == null || folder == null) {
			return false;
		}

		return FolderView.isRecommandAppFolder(folder) && ThemeRecommendApp.getInstance(context).getThemeRecomendAppList(context).size() > 0;
	}

	/**
	 * 重新计算文件夹原有高度
	 */
	private void afreshReckonFolderHight() {
		RelativeLayout.LayoutParams lp = null;
		int cellHeiht = mIconSize * 2;
		if (SettingsPreference.getInstance().getFolderStyle() != FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {
			// 重新算布局高度
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, cellHeiht * getRowNum());
			if (shouldShowLightbar()) {// 有指示灯
				lp.topMargin = (int) ((35 + 15) * ScreenUtil.getDensity());// 35:文件夹数据框高，15: 代表指示灯的高度
																			
			} else {
				lp.topMargin = (int) (35 * ScreenUtil.getDensity());// 35:文件夹数据框高,没有指示灯，所以不需要再加15dp
			}

			if (isModeForAndroid6Style) {
				lp.width = (int) (mIconSize * 1.5f * mFolderData.getColumnNum());
				if (getRowNum() > 3) {
					lp.height = (int) (mIconSize * 1.8f * getRowNum());
				}
				if (getMaxCols() <= 2) {
					lp.width = (int) (mIconSize * 1.8f * mFolderData.getColumnNum());
				}
				lp.topMargin = 0;
				int folderNameHeight = folderNameForAndroid6.getMeasuredHeight();
				if (folderNameHeight <= 0) {
					folderNameHeight = (int) ((14 + 12) * ScreenUtil.getDensity());//14字体高度，12是上下padding 总和
				}else {
					folderNameHeight = folderNameHeight + (int) (0 * ScreenUtil.getDensity());//14字体高度，12是上下padding 总和
				}
				allChildHeight =lp.height+folderNameHeight;
				allChildWidth = lp.width;
			}
			mFolderGirdView.setLayoutParams(lp);
			
		}

		// 没有推荐的时候的提醒文字
		if (isRecommandAppFolder(mFolderInfo)) {
			if (SettingsPreference.getInstance().getFolderStyle() != FolderIconTextView.FOLDER_STYLE_FULL_SCREEN && lp != null) {
				RelativeLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, cellHeiht);
				lp1.topMargin = lp.topMargin;
				lp1.leftMargin = lp.leftMargin;
				lp1.rightMargin = lp.rightMargin;
				recommendEmptyRemind.setLayoutParams(lp1);
			}
//			V6.6应为有常驻的应用搜索，所以不再显示提示文字了。
//			String[] array = mLauncher.getResources().getStringArray(R.array.recommend_empty_info);
//			Random random = new Random(System.currentTimeMillis());
//			int index = random.nextInt(array.length);
//			recommendEmptyRemind.setText(array[index]);
//			if (LauncherRecommendApps.getInstance(mLauncher).getRecommendAppInfoList().size() == 0) {// 没有推荐的时候显示
//				recommendEmptyRemind.setVisibility(View.VISIBLE);
//			} else {
				recommendEmptyRemind.setVisibility(View.GONE);
//			}
		} else {
			recommendEmptyRemind.setVisibility(View.GONE);
		}
		refresh();// 主动刷一次指示灯。
	}

	/**
	 * 添加主题推荐视图
	 */
	private void initRecommendView(FolderInfo mFolderInfo){
		// 这段代码要在setRowNum之前运行。因为getRows中已经利用到了mMaxRows。
//		if (isRecommandAppFolder(mFolderInfo) && ThemeRecommendApp.getInstance(mLauncher).getThemeRecomendAppList(Global.getApplicationContext()).size() != 0) {// 是推荐文件夹且主题推荐有推荐内容
//			themeRecomendAppGridView = new ThemeRecommendAppView(mLauncher);
//			themeRecommendAppView.addView(themeRecomendAppGridView);
//			themeRecommendAppView.setVisibility(View.VISIBLE);
//			if (!ScreenUtil.isLowScreen()) {
//				mMaxRows = 2;
//			} else {
//				mMaxRows = 1;
//			}
//		} else {
//			themeRecommendAppView.removeAllViews();
//			themeRecommendAppView.setVisibility(View.GONE);
//			if (!ScreenUtil.isLowScreen()) {
//				mMaxRows = 3;
//			} else {
//				mMaxRows = 2;
//			}
//		}

		if (!ScreenUtil.isLowScreen()) {
			mMaxRows = 3;
		} else {
			mMaxRows = 2;
		}
	}

    /**
     * 是否是91快捷文件夹
     * @return
     */
    private boolean isShortcut91Folder(){
       return mLauncher.getResources().getString(R.string.folder_91shortcut).equals(mFolderInfo.title);
    }

    public View getLongClickedView() {
    	return mLongClickView;
    }

    public CustomDrawableLightbar getmLightbar() {
        return mLightbar;
    }

    public TextView getmFolderName() {
        return mFolderName;
    }

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		FontRefreshAssist.getInstance().register(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		FontRefreshAssist.getInstance().unRegister(this);
	}

	@Override
	public void refreshPaintAndView() {
		if (mFolderName != null) {
			PaintUtils2.assemblyTypeface(mFolderName.getPaint());
			mFolderName.invalidate();
		}

		View etFolderName = findViewById(R.id.edit_folder_name);
		if (etFolderName != null) {
			PaintUtils2.assemblyTypeface(((EditText) etFolderName).getPaint());
			etFolderName.invalidate();
		}
	}

    @Override
    protected void dispatchDraw(Canvas canvas) {
        boolean shouldDrawMask = isDrawerUpdateFolder();
        int sc = -1;
        if (shouldDrawMask) {
            sc = canvas.saveLayer(this.getLeft(), 0, this.getRight(), this.getBottom(), null, Canvas.ALL_SAVE_FLAG);
        }
		if(isShowBlurBG && !LauncherCustomConfig.isSamsungStyle()) {//三星风格不绘制模糊背景框
			drawBlur(canvas);
		}
        super.dispatchDraw(canvas);

        if (sc != -1 && shouldDrawMask) {
            canvas.restoreToCount(sc);
        }
    }
	Paint mPaint=new Paint();
	Paint paint=new Paint();
	PorterDuffXfermode xfermode=new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	Rect src=new Rect();
	Rect dst=new Rect();
	int bgAlpha=255;
	boolean isdrawBlur=true;
	Bitmap mBlurBitmap;


	public void showFolderName() {
		if(mFolderName==null)return;
		mFolderName.setVisibility(View.VISIBLE);
	}

	public void hideFolderName() {
		if(mFolderName==null)return;
		mFolderName.setVisibility(View.INVISIBLE);
	}
	
	public void removeBlurBg() {
		isShowBlurBG = false;
		mBlurBitmap = null;
	}
	public void showBlurBg() {
		isShowBlurBG = true;
		mBlurBitmap=getBlurBg();
	}
	protected void drawBlur(Canvas canvas) {
		LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)getLayoutParams();
		int x = lp.leftMargin;
		int y = lp.topMargin;
		int w=getWidth();
		int h=getHeight()-mFolderName.getHeight();
//		int leftPadding = ScreenUtil.dip2px(getContext(), 10);
		int leftPadding = 0;
		int topPadding = 0;
		if(LauncherBranchController.isShenLong() || LauncherCustomConfig.isFolderTitleOnTop()){
			topPadding = mFolderName.getHeight();
		}
		dst.set(leftPadding, topPadding, w-leftPadding, topPadding + h);
		if(!isdrawBlur) {
			hWdrawable.setBounds(dst);
			hWdrawable.draw(canvas);
		}else {
			Bitmap bitmap;
			if(mBlurBitmap!=null)
				bitmap=mBlurBitmap;
			else {
				bitmap=getBlurBg();
				mBlurBitmap=bitmap;
			}
			float screenX = ScreenUtil.getCurrentScreenWidth(getContext());
			float screenY = ScreenUtil.getCurrentScreenHeight(getContext());

			//Log.e("zhou", "x=" + x + " y=" + y + " w=" + w + " h=" + h + "marg=" + lp);
			src.left = (int) (x / screenX * bitmap.getWidth());
			src.right = (int) ((x + w) / screenX * bitmap.getWidth());
			src.top = (int) (y / screenY * bitmap.getHeight());
			src.bottom = (int) ((y + h) / screenY * bitmap.getHeight());

			canvas.saveLayer(0, 0, getWidth(), getHeight(), null, canvas.ALL_SAVE_FLAG);
			paint.setColor(0xffffffff);
			mPaint.setAlpha(bgAlpha);
			//Log.e("zhou", "drawBlur bgAlpha=" + bgAlpha);
			canvas.drawRoundRect(new RectF(dst.left, dst.top, dst.right, dst.bottom), 50, 50, paint);
			mPaint.setXfermode(xfermode);
			canvas.drawBitmap(bitmap, src, dst, mPaint);
			canvas.restore();
		}

	}

	public void setBackgroundAlpha(float alpha) {
		bgAlpha = (int)(alpha*255);
	}
	/**
	 * 背景动画
	 */
	public void startBackgroundAni(int mode) {
		ValueAnimator ani;
		if (mode == 1) {
			ani = ValueAnimator.ofInt(255, 0);
		} else {
			isShowBlurBG=true;
			bgAlpha=0;
			ani = ValueAnimator.ofInt(0, 255);
		}
		ani.setDuration(2000);
		ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int v = (Integer) valueAnimator.getAnimatedValue();
				bgAlpha = v;
				isShowBlurBG=true;
				FolderView.this.invalidate();
				//Log.e("zhou","bgAlpha="+bgAlpha);
			}
		});
		ani.start();
	}
	
    private boolean isDrawerUpdateFolder() {
        return mFolderInfo instanceof UpgradeFolderInfo || isRecommandAppFolder(mFolderInfo);
    }

	/**
	 * 为android 6风格新增的
	 * */
	private TextView.OnEditorActionListener editAction=new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				//dismissEditingName();
				return true;
			}
			return false;
		}
	};

	public int getAllViewWidth() {
		return allChildWidth;
	}
	
	public int getAllViewHeight() {
		return allChildHeight;
	}
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return false;
		}

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		public void onDestroyActionMode(ActionMode mode) {
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
	};
	private TextView folderNameForAndroid6;
	
	public void onFinishInflateForAandroid6() {
		folderNameForAndroid6 =(TextView)findViewById(R.id.folder_name_android6);
		if (folderNameForAndroid6 == null) {
			isModeForAndroid6Style = false;
			return;
		}
		folderNameForAndroid6.setOnClickListener(mListener);
	}
	private void onAndroid6FolderNameClicked() {
//		analysisId = AnalyticsConstant.FOLDER_RENAME ;
		folderNameForAndroid6.setClickable(false);
		getHandler().postDelayed(new EnableClickRunnable(folderNameForAndroid6), mDoubleTapTimeout);
		//if (SettingsPreference.getInstance().getFolderStyle() == FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {
		//renameFullStyleFolder();
		//} else {
		Intent intent = new Intent(getContext(), FolderRenameActivity.class);
		intent.putExtra("name", mFolderInfo.title);
		intent.putExtra("id", mFolderInfo.id);
		SystemUtil.startActivityForResultSafely(mLauncher, intent, LauncherActivityResultHelper.REQUEST_FOLDER_RENAME);
		mLauncher.overridePendingTransition(R.anim.zoom_enter_activity, 0);
		//}
	}
	
	/***
	 * 获取合适的行
	 * @param size  app的个数
	 * */
	public int getSuitableCol(int size) {
		if(size<2)return 1;
		if (size <= 4) return 2;
		if (size <= 9) return 3;
		return 4;
	}
	/**
	 * 完成重命名的功能
	 * */
	public void doneRename() {
		if(!isModeForAndroid6Style)return;
		Intent data = new Intent();
		String text= folderNameForAndroid6.getText().toString();
		if (TextUtils.isEmpty(text)) {
			text = getResources().getString(R.string.folder_name);
		}
		data.putExtra("name",text );
		data.putExtra("id", mFolderInfo.id);
		LauncherActivityResultHelper.process(mLauncher, data,LauncherActivityResultHelper.REQUEST_FOLDER_RENAME,RESULT_OK);

	}

	private Bitmap getBlurBg() {
		if (LauncherBranchController.isFanYue() || LauncherBranchController.isShenLong()) {
			return LauncherAnimationHelp.getBlurBitmap(true, Global.getLauncher(), false,30,15);
		} else {
			return LauncherAnimationHelp.getBlurBitmap(true, Global.getLauncher(), false,20,3);
		}
	}
	
}
