package com.nd.hilauncherdev.drawer.apphide;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.DrawerDataFactory;
import com.nd.hilauncherdev.drawer.data.DrawerPreference;
import com.nd.hilauncherdev.drawer.view.DrawerMainView;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.view.bubble.LauncherBubbleView;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonLightbar;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.CommonSlidingViewData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.launcher.Launcher;
import com.nd.hilauncherdev.launcher.LauncherActivityResultHelper;
import com.nd.hilauncherdev.launcher.LauncherAnimationHelp;
import com.nd.hilauncherdev.launcher.LauncherViewHelper;
import com.nd.hilauncherdev.launcher.config.IconAndTextSizeConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 隐藏应用主Activity<br/>
 * author: dingdj<br/>
 * date: 2014/10/13<br/>
 */
public class AppHideActivity extends Activity implements View.OnClickListener, CommonSlidingView.OnCommonSlidingViewClickListener {

//    public static final String TAG = AppHideActivity.class.getSimpleName();

    public static final String FROMLAUNCHER = "fromLauncher";

    private CommonLightbar lightbar;

    private AppHideSlidingView slidingView;

    private TextView encriptBtn;

    public static final int SET_PWD = 1;

    public static final int SET_MIBAO = 2;

    public static final int GOTOSETTING = 3;

    public static final String SHOWGESTURETIP = "showgesturetip";

    LauncherBubbleView bubbleView;

    private boolean fromLauncher = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_hide_main);

        TextView addBtn = (TextView) findViewById(R.id.btn_add);
        encriptBtn = (TextView) findViewById(R.id.btn_encript);
        TextView settingBtn = (TextView) findViewById(R.id.btn_setting);

        addBtn.setOnClickListener(this);
        encriptBtn.setOnClickListener(this);
        encriptBtn.setCompoundDrawablesWithIntrinsicBounds(null, getEncriptDrawable(), null, null);
        settingBtn.setOnClickListener(this);

        slidingView = (AppHideSlidingView) findViewById(R.id.app_hide_sliding_view);
        slidingView.setOnItemClickListener(this);
        setupLightbar(this);
        slidingView.setEndlessScrolling(false);
        slidingView.setSplitCommonLightbar(lightbar);

        fromLauncher = getIntent().getBooleanExtra(FROMLAUNCHER, true);

        initData();
        showTip(getIntent());
    }

    private void showTip(Intent intent) {
        if (intent != null
                && intent.getBooleanExtra(SHOWGESTURETIP, false)
                && !DrawerPreference.getInstance().getAppHideGestureTip()) {
            DrawerPreference.getInstance().setAppHideGestureTip(true);
            showGestureTip();
        }
    }

    public void showEncriptToast() {
        bubbleView = LauncherViewHelper.createBubbleViewForAppHide(
                AppHideActivity.this,
                AppHideActivity.this.getResources().getString(R.string.app_hide_encript),
                encriptBtn);
        bubbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubbleView.dismiss();
            }
        });
        bubbleView.addToParent((ViewGroup) findViewById(R.id.app_hide_main_view));
    }

    /**
     * 初始化指示灯
     */
    private void setupLightbar(Context context) {
        lightbar = (CommonLightbar) findViewById(R.id.light_bar);
        lightbar.setNormalLighter(context.getResources().getDrawable(R.drawable.screen_choose_app_lightbar_normal));
        lightbar.setSelectedLighter(context.getResources().getDrawable(R.drawable.screen_choose_app_lightbar_selected));
        lightbar.setGap(ScreenUtil.dip2px(context, 3));
        lightbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (bubbleView != null) {
            bubbleView.dismiss();
        }
        if (v.getId() == R.id.btn_add) {
            addHideApps(this);
        } else if (v.getId() == R.id.btn_encript) {
            clickEncriptBtn();
        } else if (v.getId() == R.id.btn_setting) {
            gotoSetting();
        }
    }

    private void addHideApps(AppHideActivity appHideActivity) {
        List<ApplicationInfo> hideAppsList = DrawerDataFactory.getHiddenAppsList(this);
        AppHide.showSelectAppActivity(appHideActivity, hideAppsList);
    }

    private void clickEncriptBtn() {
        int state = AppHideStateManager.getAppHideState();
        switch (state) {
            case AppHideStateManager.UN_USE_APP_HIDE_FUNC:
            case AppHideStateManager.USED_APP_HIDE_FUN_AND_UNLOCK://加锁
                encriptAppHide();
                break;
            case AppHideStateManager.USED_APP_HIDE_FUN_AND_LOCK://解锁
                showUnEncriptDialog();
                break;
            default:
                break;

        }

    }

    private void encriptAppHide() {
        if (AppHide.isLastEncriptPwdExist()) {
            findViewById(R.id.app_hide_main_view).setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.setClass(this, AppHideEncriptTypeChooseActivity.class);
            intent.putExtra(AppHideActivity.FROMLAUNCHER, fromLauncher);
            SystemUtil.startActivityForResultSafely(this, intent, SET_PWD);
        } else { //跳转到设置密码页面
            gotoSetPwd();
        }
    }

    /**
     * 显示解锁对话框
     */
    private void showUnEncriptDialog() {
        ViewFactory.getAlertDialog(this, this.getString(R.string.encript_lock_title),
                this.getString(R.string.encript_unlock_desc),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppHide.unEncriptAppHideFunc();
                        modifyEncriptBtnState();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void modifyEncriptBtnState() {
        encriptBtn.setCompoundDrawablesWithIntrinsicBounds(null, getEncriptDrawable(), null, null);
}

    private void gotoSetting() {
        findViewById(R.id.app_hide_main_view).setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.setClass(AppHideActivity.this, AppHideSettingActivity.class);
        SystemUtil.startActivityForResultSafely(AppHideActivity.this, intent, GOTOSETTING);
    }

    private void gotoSetPwd() {
        findViewById(R.id.app_hide_main_view).setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.setClass(this, AppHideEncriptSettingActivity.class);
        intent.putExtra(FROMLAUNCHER, fromLauncher);
        SystemUtil.startActivityForResultSafely(this, intent, SET_PWD);
    }


    private void initData() {
        /**
         * 准备数据集
         */
        List<ICommonData> list = new ArrayList<ICommonData>();
        int iconSize = IconAndTextSizeConfig.getSmallIconSize(AppHideActivity.this);
        CommonSlidingViewData data = new CommonSlidingViewData((int) (iconSize * 1.5f), (int) (iconSize * 1.8f), 3, 3, new ArrayList<ICommonDataItem>());
        list.add(data);
        slidingView.setList(list);

        List<ICommonDataItem> itemList = data.getDataList();
        List<ApplicationInfo> hideAppsList = DrawerDataFactory.getHiddenAppsList(Global.getApplicationContext());
        if (hideAppsList == null) {
            lightbar.setVisibility(View.GONE);
            return;
        }
        if (hideAppsList.size() < 10) {
            lightbar.setVisibility(View.GONE);
        } else {
            lightbar.setVisibility(View.VISIBLE);
        }
        itemList.addAll(hideAppsList);
        slidingView.clearLayout();
        slidingView.reLayout();
    }


    private void updateData() {
        slidingView.getCurrentData().getDataList().clear();
        List<ApplicationInfo> hideAppsList = DrawerDataFactory.getHiddenAppsList(Global.getApplicationContext());
        if (hideAppsList == null) {
            lightbar.setVisibility(View.GONE);
            return;
        }
        if (hideAppsList.size() < 10) {
            lightbar.setVisibility(View.GONE);
        } else {
            lightbar.setVisibility(View.VISIBLE);
        }
        slidingView.getCurrentData().getDataList().addAll(hideAppsList);
        slidingView.clearLayout();
        slidingView.reLayout();
    }


    @Override
    public void onBackPressed() {
        LauncherAnimationHelp.blankAnimation(Global.getLauncher(), fromLauncher, false);
        super.onBackPressed();
    }

    private Drawable getEncriptDrawable() {
        int state = AppHideStateManager.getAppHideState();
        switch (state) {
            case AppHideStateManager.UN_USE_APP_HIDE_FUNC:
            case AppHideStateManager.USED_APP_HIDE_FUN_AND_UNLOCK:
                return getResources().getDrawable(R.drawable.folder_full_btn_noencript_selector);

            case AppHideStateManager.USED_APP_HIDE_FUN_AND_LOCK:
                return getResources().getDrawable(R.drawable.folder_full_btn_encript_selector);
            default:
                return getResources().getDrawable(R.drawable.folder_full_btn_noencript_selector);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == LauncherActivityResultHelper.REQUEST_HIDE_APP) {
            Launcher launcher = Global.getLauncher();
            if (launcher != null && launcher.getDrawer() != null && launcher.getDrawer() instanceof DrawerMainView) {
                ((DrawerMainView) launcher.getDrawer()).updateHiddenApps(data);
            }
            Context context = Global.getApplicationContext();
            List list = DrawerDataFactory.getHiddenAppsList(context);
            if (list == null || list.size() == 0) {
                this.finish();
            } else {
                updateData();
            }
        }

        if (resultCode == Activity.RESULT_CANCELED && requestCode == LauncherActivityResultHelper.REQUEST_HIDE_APP) {
            Context context = Global.getApplicationContext();
            List list = DrawerDataFactory.getHiddenAppsList(context);
            if (list == null || list.size() == 0) {
                this.finish();
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == SET_PWD) {
            if (!DrawerPreference.getInstance().getAppHideGestureTip()) {
                DrawerPreference.getInstance().setAppHideGestureTip(true);
                showGestureTip();
                return;
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == GOTOSETTING) {
            this.finish();
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            modifyEncriptBtnState();
        }
        if (data != null) {
            if (data.getBooleanExtra(AppHideEncriptTypeChooseActivity.USE_LAST_PWD, false)) {
                useLastPwd();
            }
        }
    }


    private void useLastPwd() {
        MessageUtils.makeShortToast(this, R.string.folder_encript_setting_pwd_success);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LauncherAnimationHelp.displayAnimation(Global.getLauncher(), fromLauncher, false);
        modifyEncriptBtnState();
        findViewById(R.id.app_hide_main_view).setVisibility(View.VISIBLE);
        showTip(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showTip(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LauncherAnimationHelp.blankAnimation(Global.getLauncher(), fromLauncher, false);
    }

    /**
     * 弹出手势提示框
     */
    @SuppressWarnings("all")
    private void showGestureTip() {
        View view = this.getLayoutInflater().inflate(R.layout.app_hide_gesture_tip, null);
        AppHideDialog dialog = new AppHideDialog.Builder(this).
                setMessage(R.string.mibao_set_tip).
                setPositiveButton(R.string.try_app_hide_func, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppHideActivity.this.finish();
                        if (Global.getLauncher().isAllAppsVisible()) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Global.getLauncher().toogleDrawer();
                                }
                            }, 500);

                        }
                    }
                }).setContentView(view).create();

        dialog.show();
    }

    @Override
    public void onItemClick(View v, int positionInData, int positionInScreen, int screen, ICommonData data) {
        ICommonDataItem item = data.getDataList().get(positionInData);
        if (item instanceof ApplicationInfo) {
            ApplicationInfo info = (ApplicationInfo) item;
            if (info.intent != null) {
                SystemUtil.startActivitySafely(this, info.intent);
                this.finish();
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (AppHideStateManager.UN_USE_APP_HIDE_FUNC == AppHideStateManager.getAppHideState()) {
            showEncriptToast();
        }
        DrawerPreference.getInstance().setUsedHideAppFunc(true);
    }


    /*@Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceiver(oneKeyShotReceiver);
    }

    private BroadcastReceiver oneKeyShotReceiver = new HiBroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (SystemSettingUtil.ACTION_REDIRECT_TOOLBOX.equals(intent.getAction())) {
                AppHideActivity.this.finish();
            }
        }
    };*/
}

