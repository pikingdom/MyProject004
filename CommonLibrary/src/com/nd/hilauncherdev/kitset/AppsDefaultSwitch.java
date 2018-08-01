package com.nd.hilauncherdev.kitset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.nd.hilauncherdev.datamodel.CommonGlobal;

/**
 * 默认桌面设置工具类
 */
public class AppsDefaultSwitch {

	private final static String LOCK_91_HOME_PKG = "com.baidu.screenlock.homeplugin";

	private Context ctx;
	private IntentFilter filter;
	private Intent intent;
	private PackageManager pm;
	private List<ResolveInfo> ris;
	private int n;

	public AppsDefaultSwitch(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addCategory(Intent.CATEGORY_DEFAULT);

		ctx = context;
		pm = ctx.getPackageManager();
		this.intent = intent;
		ris = pm.queryIntentActivities(intent, 0);
		n = ris.size();
		filter = new IntentFilter();
		if (intent.getAction() != null) {
			filter.addAction(intent.getAction());
		}
		Set<String> categories = intent.getCategories();
		if (categories != null) {
			for (String cat : categories) {
				filter.addCategory(cat);
			}
		}
		filter.addCategory(Intent.CATEGORY_DEFAULT);
	}

	public List<ResolveInfo> getResolveInfoList() {
		return ris;
	}

/*	public Intent getIntent() {
		return intent;
	}*/

	/*public ResolveInfo getResolveInfo(int index) {
		if (index > -1 && index < ris.size()) {
			return ris.get(index);
		}
		return null;
	}*/

	/*public String getAppName(int index) {
		return (String) ris.get(index).activityInfo.loadLabel(pm);
	}*/

	/*public Drawable getAppIcon(int index) {
		return ris.get(index).activityInfo.loadIcon(pm);
	}*/

	/*public int getCount() {
		return n;
	}
*/
	/**
	 * 是否为默认桌面
	 * 
	 * @return boolean
	 */
	public boolean getIsPandaHomeDefault() {
		try {
			int defaultIndex = getDefault();
			int pandaIndex = getIndexOfPandaHome();
			if (defaultIndex == pandaIndex)
				return true;

			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 获取桌面在launcher列表位置
	 * 
	 * @return
	 */
	public int getIndexOfPandaHome() {
		ResolveInfo ri;
		int index = -1;
		for (int i = 0; i < n; i++) {
			ri = ris.get(i);
			if (ri.activityInfo.packageName.equalsIgnoreCase(CommonGlobal.getApplicationContext().getPackageName())) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 获取91智能锁在launcher列表位置
	 * 
	 * @return
	 */
	public int getIndexOf91Lock() {
		ResolveInfo ri;
		int index = -1;
		for (int i = 0; i < n; i++) {
			ri = ris.get(i);
			if (ri.activityInfo.packageName.equalsIgnoreCase(LOCK_91_HOME_PKG)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 设置默认桌面
	 * 
	 * @return
	 */
	/*public boolean setPandaHomeDefault() {
		int index = getIndexOfPandaHome();
		if (index > -1) {
			setDefault(index);
			return true;
		} else {
			return false;
		}
	}*/

	/**
	 * 设置默认应用程序(2.2及以上SDK已失效)
	 * 
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	public void setDefault(int index) {
		ComponentName[] set = new ComponentName[n];
		int bestMatch = 0;
		ResolveInfo ri;
		for (int i = 0; i < n; i++) {
			ri = ris.get(i);
			set[i] = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
			if (ri.match > bestMatch)
				bestMatch = ri.match;
		}
		ri = ris.get(index);
		ComponentName componentName = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
		pm.addPreferredActivity(filter, bestMatch, set, componentName);
	}

	/**
	 * 清除默认应用程序
	 */
	/*public void clearDefaults() {
		for (int i = 0; i < ris.size(); i++) {
			ResolveInfo ri = ris.get(i);
			pm.clearPackagePreferredActivities(ri.activityInfo.packageName);
		}
	}*/

	/**
	 * 清除默认设置
	 * 
	 * @param index
	 */
	/*public void clearDefaults(int index) {
		ResolveInfo ri = ris.get(index);
		pm.clearPackagePreferredActivities(ri.activityInfo.packageName);
	}*/

	/**
	 * 清除默认设置
	 * 
	 * @param ri
	 */
	/*public void clearDefaults(ResolveInfo ri) {
		if (null == ri)
			return;
		pm.clearPackagePreferredActivities(ri.activityInfo.packageName);
	}*/

	public int getDefault() {
		ResolveInfo ri;
		List<ComponentName> prefActList;
		List<IntentFilter> intentList;
		for (int i = 0; i < n; i++) {
			ri = ris.get(i);
			prefActList = new ArrayList<ComponentName>();
			intentList = new ArrayList<IntentFilter>();
			pm.getPreferredActivities(intentList, prefActList, ri.activityInfo.packageName);
			if (prefActList.size() > 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获取当前设置的默认桌面
	 * 
	 * @return ResolveInfo
	 */
	public ResolveInfo getDefaultHome() {
		ResolveInfo ri = null;
		List<ComponentName> prefActList;
		List<IntentFilter> intentList;
		for (int i = 0; i < n; i++) {
			ri = ris.get(i);
			prefActList = new ArrayList<ComponentName>();
			intentList = new ArrayList<IntentFilter>();
			pm.getPreferredActivities(intentList, prefActList, ri.activityInfo.packageName);
			if (prefActList.size() > 0) {
				for (int k = 0; k < intentList.size(); k++) {
					if (intentList.get(k).hasCategory(Intent.CATEGORY_HOME)) {
						return ri;
					}
				}
			}
		}
		return null;
	}

}
