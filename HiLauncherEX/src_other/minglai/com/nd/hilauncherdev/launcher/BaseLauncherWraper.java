package com.nd.hilauncherdev.launcher;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.dx.libagent2.Agent2Desktop;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.launcher.config.LauncherConfig;

import org.phone.guide.GuideListener;
import org.phone.guide.GuideService;
import org.phone.guide.fragment.BaseFragment;

/**
 * Created by guojianyun on 2017/2/24.
 */

public class BaseLauncherWraper extends BaseLauncher implements GuideListener {
    //===========力天开机引导==============//
    private FragmentManager mFM;
    private BaseFragment mCrrFragment;

    /**
     * 开机时间是否超过一分钟
     */
    public static boolean isElapsedRealtimeExceed = false;

    protected void handleCustomStuffOnPreCreateStart() {
        startService(new Intent(this, Agent2Desktop.class));
        if (!ConfigPreferences.getInstance().showLiTianGuide())
            return;
        // 铭来定制版第一次启动或开机时间大于1分钟，不显示开机引导 caizp 2017-06-25
        isElapsedRealtimeExceed = ConfigPreferences.getInstance().isFirstLaunch() || SystemClock.elapsedRealtime() > 60 * 1000L;
        if(isElapsedRealtimeExceed) {
            return;
        }

        if (LauncherConfig.getLauncherHelper().showCustomGuide(this)) {
            isShowCustomGuide = true;
            if(mFM == null)
                mFM = getFragmentManager();
            BaseFragment fragment = GuideService.getStartFragment();
            if(null != fragment) {
                forwardSendPage(fragment);
            }
//            OneKeySetActivity.showActivity(getApplicationContext());
        }else {
            ConfigPreferences.getInstance().setShowLiTianGuide(false);
        }
    }

    protected void handleCustomStuffOnNewIntent() {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (BaseFragment.REQUEST_WIFI_PERMISSION_CODE == requestCode) {
            if (grantResults.length>0&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(null != mCrrFragment) {
                    mCrrFragment.onRequestPermissionsResult();
                }
            } else {
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LauncherConfig.getLauncherHelper().showCustomGuide(this) && keyCode == KeyEvent.KEYCODE_BACK) {
            if(null != mCrrFragment) {
                return mCrrFragment.onBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void forwardSendPage(BaseFragment fragment) {
        if(mFM == null)
            return;
        BaseFragment tmp = mCrrFragment;
        FragmentTransaction ft = mFM.beginTransaction();
        mCrrFragment = fragment;
        ft.replace(R.id.guide_container, mCrrFragment);
        if (null != tmp) {
            ft.remove(tmp);
        }

        int uiFlags = 256
                | 512
                | 1024
                | 2 // hide nav bar
                | 4; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onGuideFinished(){
        ConfigPreferences.getInstance().setShowLiTianGuide(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "正在初始化，请稍候...", Toast.LENGTH_LONG).show();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 6000);

    }

//    private HomeBroadcastReceiver mHomeKeyReceiver;
//
//    public void registerHomeListen() {
//        if (mHomeKeyReceiver != null)
//            return;
//        mHomeKeyReceiver = new HomeBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        try {
//            this.registerReceiver(mHomeKeyReceiver, filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    void register() {
//        super.register();
//        if (SettingsPreference.getInstance().getPreesHomeIsForcedJump()) {
//            registerHomeListen();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            if (mHomeKeyReceiver != null) {
//                this.unregisterReceiver(mHomeKeyReceiver);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (SettingsPreference.getInstance().getPreesHomeIsForcedJump()) {
//            registerHomeListen();
//        }
//    }
//    private  class HomeBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent i) {
//            if (SettingsPreference.getInstance().getPreesHomeIsForcedJump()) {
//                //判断当前是否是跳转到其他桌面
////				if (ConfigPreferences.getInstance().getRemindSetDefaultLauncherCount() <= 3) {
////					return;
////				}
//                Intent intent = new Intent(context, Launcher.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                try {
//                    pendingIntent.send();
//                } catch (PendingIntent.CanceledException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
