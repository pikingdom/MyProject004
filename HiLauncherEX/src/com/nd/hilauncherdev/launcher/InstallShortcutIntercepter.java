package com.nd.hilauncherdev.launcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.activity.InstallShortcutInterceptActivity;
import com.nd.hilauncherdev.kitset.util.BaseBitmapUtils;
import com.nd.hilauncherdev.launcher.broadcast.InstallShortcutReceiver;
import com.nd.hilauncherdev.launcher.support.FastBitmapDrawable;
import com.nd.hilauncherdev.settings.SettingsPreference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 桌面安装快捷图标拦截器 <br/>
 * author: dingdj<br/>
 * data: 2014/9/17<br/>
 */
public class InstallShortcutIntercepter implements InstallShortcutReceiver.InterceptNotification {

    public static final String TAG = InstallShortcutIntercepter.class.getSimpleName();

    public static final String INTERCEPT_INTENT = "intercept_intent";

    public static ArrayList<Intent> intents = new ArrayList<Intent>();

    public static WeakReference<Notification> weakReference;

    /**
     * 发送拦截通知
     */
    @Override
    public void sendInterceptNotifications(Context mContext, Intent dataIntent) {
        try {
            if(weakReference == null || weakReference.get() == null) {
                intents.clear();
            }
            intents.add(dataIntent);
            NotificationManager mNotifyManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            String desc = String.format(mContext.getString(R.string.intercept_notification_num), intents.size());

            Notification notification = new Notification(R.drawable.logo_mini, desc, System.currentTimeMillis());
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            weakReference = new WeakReference<Notification>(notification);

            Intent resultIntent = new Intent(mContext, InstallShortcutInterceptActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.putParcelableArrayListExtra(INTERCEPT_INTENT, intents);

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 1, resultIntent, 0);
            desc = String.format(mContext.getString(R.string.intercept_notification_detail_num), intents.size());
            notification.setLatestEventInfo(mContext, desc, mContext.getString(R.string.click_to_detail), contentIntent);
            mNotifyManager.notify(R.layout.intercept_shortcut_install, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通知栏中的图标
     *
     * @param mContext   Context
     * @param parcelData parcelData
     * @return Bitmap
     */
    public static Bitmap getShortcutBitmap(Context mContext, Parcelable parcelData) {
        if (parcelData instanceof Bitmap) {
            return BaseBitmapUtils.createIconBitmapThumbnail(new FastBitmapDrawable((Bitmap) parcelData), mContext);
        } else if (parcelData instanceof Intent.ShortcutIconResource) {
            try {
                Intent.ShortcutIconResource iconResource = (Intent.ShortcutIconResource) parcelData;
                final PackageManager packageManager = mContext.getPackageManager();
                Resources resources = packageManager.getResourcesForApplication(iconResource.packageName);
                final int id = resources.getIdentifier(iconResource.resourceName, null, null);
                return BaseBitmapUtils.createIconBitmapThumbnail(resources.getDrawable(id), mContext);
            } catch (Exception e) {
                Log.w(TAG, "Could not load shortcut icon: " + parcelData);
            }
        }
        return null;
    }

    public static String getAppNameFromIntent(Context mContext, Intent intent) {
        if (mContext == null || intent == null) return "";
        try {
            String packageName = intent.getComponent().getPackageName();
            return findNameForPackage(mContext, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String findNameForPackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        if (apps != null) {
            // Find all activities that match the packageName
            int count = apps.size();
            for (int i = 0; i < count; i++) {
                final ResolveInfo resInfo = apps.get(i);
                final ActivityInfo activityInfo = resInfo.activityInfo;
                if (packageName.equals(activityInfo.packageName)) {
                    return activityInfo.loadLabel(packageManager).toString();
                }

            }
        }
        return "";
    }


    @Override
    public boolean isUse() {
        return SettingsPreference.getInstance().getShortcutIconIntercept();
    }
}
