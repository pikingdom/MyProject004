package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.widget.util.DownloadMoreWidgetWorker;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.launcher.broadcast.HiBroadcastReceiver;
import com.nd.hilauncherdev.plugin.ThirdApplicationAssit;
import com.nd.hilauncherdev.webconnect.upgradhint.BeautyUpgradeManager;
import com.nd.hilauncherdev.webconnect.upgradhint.BeautyUpgradeMeta;

/**
 * 软件升级辅助类
 * 
 * @author Anson
 */
public class LauncherSoftUpgradeHelper {

	private Context mContext;

	private SoftUpgradeReceiver receiver;
	
	private static LauncherSoftUpgradeHelper helper;

	private LauncherSoftUpgradeHelper(Context context) {
		mContext = context;
	}
	
	public synchronized static LauncherSoftUpgradeHelper getInstance(Context context) {
		if (helper == null) {
			helper = new LauncherSoftUpgradeHelper(context);
		}
		return helper;
	}
	
	public void detectUpgrade(String packageName, String softName, int title, int msg) {
		if (AndroidPackageUtils.isPkgInstalled(mContext, packageName)) {
			BeautyUpgradeMeta meta = BeautyUpgradeManager.getBeautyInfo(BeautyUpgradeManager.BEAUTY_WIDGET_UPGRADE, packageName);
			if (meta != null) {
				try {
					PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
					if (Integer.parseInt(meta.version) > pInfo.versionCode) {
						Intent intent = new Intent(HiBroadcastReceiver.SOFT_UPGRADE_ACTION);	
						intent.putExtra("packageName", packageName);
						intent.putExtra("softName", softName);
						MessageUtils.makeBroadcastNotification(mContext, R.drawable.logo_mini, title, msg, intent);
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void registerSoftUpgradeReceiver() {
		if(null == receiver) {
			receiver = new SoftUpgradeReceiver();
			IntentFilter filter = new IntentFilter(HiBroadcastReceiver.SOFT_UPGRADE_ACTION);
			mContext.registerReceiver(receiver, filter);
		}
	}

	public void unregisterSoftUpgradeReceiver() {
		if (receiver != null) {
			try {
				mContext.unregisterReceiver(receiver);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				receiver = null;
			}
		}
	}

	public class SoftUpgradeReceiver extends HiBroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (null == intent)
				return;
			
			String packageName = intent.getStringExtra("packageName");
			String softName = intent.getStringExtra("softName");
			
			if (packageName == null || "".equals(packageName))
				return;
			
			final String url = ThirdApplicationAssit.getApkDownloadUrl(ctx, packageName);
			final String name = softName == null || "".equals(softName) ? packageName : softName;
			DownloadMoreWidgetWorker downloadMoreWidgetWorker = new DownloadMoreWidgetWorker(url, Global.DOWNLOAD_DIR, packageName + ".apk", name + ".apk");
			downloadMoreWidgetWorker.deleteTempFile();
			downloadMoreWidgetWorker.start();
		}
	}
}
