package com.nd.hilauncherdev.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import com.nd.hilauncherdev.app.DynamicIconHelper;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.support.BaseIconCache;

/**
 * 扩展BaseIconCache，用于应用主题时刷新图标
 */
public class IconCache extends BaseIconCache{
	
	public IconCache(Context context) {
		super(context);
	}
	
	@Override
	public void updateDynamicIcon(Context ctx, CacheEntry cacheEntry, ComponentName comp, boolean needBroadcast){
		DynamicIconHelper.getInstance().updateDynamicIcon(mContext, cacheEntry, comp, false);
	}

	@Override
	public Bitmap get91IconByKey(String key) {
		Bitmap bitmap = mIconCache91.get(key);
		if(bitmap != null) {
			return bitmap;
		}
		return null;
	}

	@Override
	public void put91IconInCache(String key, Bitmap bitmap) {
		mIconCache91.put(key, bitmap);
	}


	@Override
	public Bitmap refreshTheCache(ApplicationInfo app) {
		if (app != null && app.intent != null && app.intent.getComponent() != null){
			ComponentName component = app.intent.getComponent();
			CacheEntry ce = mCache.get(component);
			if (ce == null)
				return null;
			if(LauncherProviderHelper.isReplacedApp(component.getPackageName(), component.getClassName())){
				ce.icon = app.iconBitmap;
				return ce.icon;
			}
		}
		return super.refreshTheCache(app);
	}
	
}
