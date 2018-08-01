package com.nd.hilauncherdev.launcher;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.nd.hilauncherdev.kitset.config.ConfigPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SelfProtectService extends AccessibilityService {
    public static final String TAG = SelfProtectService.class.getSimpleName();
    private static final String KEY_USB = "key_accessibility_usb_toast_count";
    private static final String KEY_SERVICE = "key_accessibility_service_toast_count";
    private static final String KEY_RESET = "key_accessibility_reset_toast_count";

    @Override
    protected void onServiceConnected() {
        // 绑定服务所用方法，一些初始化操作
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 捕捉AccessibilityEvent的事件，进行处理
        int eventType = event.getEventType();
        if (eventType == 2048/*AccessibilityEventCompat.TYPE_WINDOW_CONTENT_CHANGED*/ || eventType == 32) {
            String pkgClass = event.getPackageName() + ":" + event.getClassName();
            if (pkgClass.startsWith("com.android.settings")) {
                this.accessibilitySettings(event);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // 打断获取事件的过程
    }

    private void accessibilitySettings(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            if (subFindByText(source, "USB调试") || subFindByText(source, "蓝牙 HCI 信息收集日志")) {
                if (nextDo(KEY_USB, event))
                    Toast.makeText(this, "未插卡联网激活，禁止使用开发者选项！", Toast.LENGTH_SHORT).show();
            } else if (subFindByText(source, "开启该服务后，会禁止一些非必要操作。")) {
                if (nextDo(KEY_SERVICE, event))
                    Toast.makeText(this, "未插卡联网激活，禁止操作！", Toast.LENGTH_SHORT).show();
            } else if (subFindByText(source, getResetText())) {
                if (nextDo(KEY_RESET, event))
                    Toast.makeText(this, "未插卡联网激活，禁止重置！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getResetText() {
        String brand = Build.BRAND;
        if (brand.equalsIgnoreCase("Vivo")) {
            return "清除所有";
        } else if (brand.equalsIgnoreCase("Oppo")) {
            return "抹掉全部";
        } else {// Honor,Huawei,Xiaomi,Other
            return "恢复出厂设置";
        }
    }

    private boolean nextDo(String key, AccessibilityEvent event) {
        // TODO 通过SharedPreferences获取该处监控的地方已提示干扰次数，默认值为0
        int count = ConfigPreferences.getInstance().getSP().getInt(key, 0);// PreferencesUtils.getInt(key, 0)
        if (count >= 5) {// 该处监控的地方，拦截提示已不少于5次，不再干扰
            Log.e(TAG, key + " >= 5");
            return false;
        }
        if (!isActivate()) {// 判断该手机是否已插卡联网激活，如果已激活，则不再Toast提示和执行阻止操作
            if (toHome()) {
                count++;
                Log.e(TAG, key + " = " + count);
                // TODO 通过SharedPreferences保存该处监控的地方已提示干扰次数
                // PreferencesUtils.putInt(key, count);
                ConfigPreferences.getInstance().getSP().edit().putInt(key, count).commit();
                return true;
            }
        }
        return false;
    }

    private boolean isActivate() {
        String log = "";
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {// SIM卡就绪
            log += "SimStateReady";
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable() && ni.isConnected()) {// 网络可用
                log += "+ NetworkIsConnected";
                long flow = getAppFlow(this.getPackageName(), this);
                if (flow >= 10240000) {// 产生10K流量
                    log += "+ Flow10K = Activated";
                    Log.e(TAG, log);
                    return true;
                }
            }
        }
        return false;
    }

    boolean toHome() {
        PackageManager pm = this.getPackageManager();
        Intent it = pm.getLaunchIntentForPackage("com.android.settings");
        if (it != null) {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(it);
            Intent it2 = new Intent(Intent.ACTION_MAIN);
            it2.addCategory(Intent.CATEGORY_HOME);
            it2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(it2);
            return true;
        }
        return false;
    }

    boolean subFindByText(AccessibilityNodeInfo source, String txt) {
        List<AccessibilityNodeInfo> nodes = source.findAccessibilityNodeInfosByText(txt);
        if (nodes != null && !nodes.isEmpty()) {
            return true;
        }
        return false;
    }

    /************************************** 根据包名获取该应用的流量值 **************************************/
    public static long getAppFlow(String packageName, Context context) {
        long flow = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(packageName, 0);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            if (appInfo != null) {
                flow = getAppFlow(appInfo);
            }
        } catch (NameNotFoundException e) {
            Log.v(TAG, packageName + " 已卸载！");
            flow = -1;
        }
        return flow;
    }

    private static long getAppFlow(ApplicationInfo appInfo) {
        if (Build.VERSION.SDK_INT < 8 || TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED)
            return 0;
        try {
            int uid = appInfo.uid;
            long flow4Rx = TrafficStats.getUidRxBytes(uid);// 某一个进程的总接收量+总发送量
            long flow4Tx = TrafficStats.getUidTxBytes(uid);
            long flow = flow4Rx == TrafficStats.UNSUPPORTED ? 0 : flow4Rx + flow4Tx == TrafficStats.UNSUPPORTED ? 0
                    : flow4Tx;
            if (flow == 0) {
                flow = getTotalBytesManual(uid);
            }
            Log.v(TAG, "getAppFlow() " + appInfo.packageName + " , flow = " + flow);
            return flow;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Long getTotalBytesManual(int localUid) {
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        if (!Arrays.asList(children).contains(String.valueOf(localUid))) {
            return 0L;
        }
        File uidFileDir = new File("/proc/uid_stat/" + String.valueOf(localUid));
        File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
        File uidActualFileSent = new File(uidFileDir, "tcp_snd");
        String textReceived = "0";
        String textSent = "0";
        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;
            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
            }
            brReceived.close();
            brSent.close();
        } catch (IOException e) {
        }
        return Long.valueOf(textReceived).longValue() + Long.valueOf(textSent).longValue();
    }
    /************************************** 根据包名获取该应用的流量值 **************************************/

}
