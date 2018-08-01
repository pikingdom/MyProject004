package com.nd.hilauncherdev.launcher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.launcher8.PluginHelper;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.once.Once;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.applicationaop.uncaught.CrashHandler;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.model.load.LauncherLoaderHelper;
import com.nd.hilauncherdev.launcher.support.BaseIconCache;
import com.nd.hilauncherdev.theme.LauncherThemeManagerHelper;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeData;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class LauncherApplication extends BaseLauncherApplication {

	@Override
	public void onCreate() {
//        modifyTargetVersion();
		super.onCreate();
//		if(Build.VERSION.SDK_INT<14)return;
//		long t=System.currentTimeMillis();
//		try {
//			if (!FileUtil.isFileExits(getCacheDir().toString()+"/class3.dex")) {
//				FileUtil.copyAssetsFile(getApplicationContext(), "classes3.dex", getCacheDir().toString(), "class3.dex");
//			}
//			if (!FileUtil.isFileExits(getCacheDir().toString()+"/class2.dex")) {
//				FileUtil.copyAssetsFile(getApplicationContext(), "classes2.dex", getCacheDir().toString(), "class2.dex");
//			}
//			//Log.e("zhou", "文件=" + getCacheDir().toString());
//			String path = getCacheDir().toString()+"/class3.dex";
//			String path2 = getCacheDir().toString()+"/class2.dex";
//			inject(path,"com.nd.android.pandahome2");
//			inject(path2,"com.nd.android.pandahome2");
//		}catch (Throwable a){
//			HiAnalytics.submitEvent(getApplicationContext(), AnalyticsConstant.DEX_SUBPACKAGE_EXCEPTION_FOR_LOAD_FRAME, ""+Build.VERSION.SDK_INT);
//			a.printStackTrace();
//		}

		CustomChannelController.onLauncherApplicationCreate(this);

		if (TelephoneUtil.getApiLevel() < 24 && !TelephoneUtil.getMachineName().toLowerCase().contains("vivo x9")) {//7.0以上不支持
			PluginHelper.getInstance().applicationOnCreate(getBaseContext());
		}
		initOnce();
	}

	/**
	 * 初始化Once
	 */
	private void initOnce() {
		try {
			Once.initialise(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		if (TelephoneUtil.getApiLevel() < 24 && !TelephoneUtil.getMachineName().toLowerCase().contains("vivo x9")) {//7.0以上不支持
			PluginHelper.getInstance().applicationAttachBaseContext(getBaseContext());
		}
		super.attachBaseContext(base);
		MultiDex.install(base);
		MultiAppController.initQuickHelper(this);
	}

	private void modifyTargetVersion() {
		try {
			if (TelephoneUtil.isHuaweiPhone() || TelephoneUtil.isSonyPhone()) {
				getApplicationInfo().targetSdkVersion = 8;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据库辅助类
	 */
	@Override
	public void initDBHelper() {
		LauncherProviderHelper.initDBHelper();
	}

	/**
	 * 初始化底层抽离辅助类
	 */
	@Override
	public LauncherLoaderHelper getLauncherHelper() {
		return LauncherLoaderHelperImpl.getInstance();
	}

	/**
	 * 初始化LauncherModel
	 * 
	 * @param iconCache
	 * @return
	 */
	@Override
	public BaseLauncherModel createLauncherModel(BaseIconCache iconCache) {
		return new LauncherModel(this, iconCache);
	}

	/**
	 * 初始化IconCache
	 * 
	 * @param mContext
	 * @return
	 */
	@Override
	public BaseIconCache createIconCache(Context mContext) {
		return new IconCache(mContext);
	}

	/**
	 * 初始化异常捕获
	 */
	@Override
	public void initCrashHandler() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	/**
	 * 初始化基础目录
	 */
	@Override
	public void createDefaultDir() {
		Global.createDefaultDir();
	}

	/**
	 * 加载主题数据
	 */
	@Override
	public void loadThemeIntentData() {
		// 主进程与主题商城进程才需要加载主题数据
		String processName = SystemUtil.getCurProcessName(this);
		
		if (StringUtil.isEmpty(processName) || getPackageName().equals(processName)|| processName.contains("hilauncherex_start")) {
			super.loadThemeIntentData();
			new ThemeData().buildThemeData();
			ThemeManagerFactory.getInstance().setThemeManagerHelper(new LauncherThemeManagerHelper());
		} else if (processName.contains("hilauncherex_shopv2_process")) {
			new ThemeData().buildThemeData();
			ThemeManagerFactory.getInstance().setThemeManagerHelper(new LauncherThemeManagerHelper());
		}
	}



	String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public String inject(String libPath, String loadPacketName) {

		String pn = getCurProcessName(this);
		// Log.e("zhou", "PN="+pn);
		if (!(loadPacketName.equals(pn))) {
			return null;
		}

		Application sApplication = this;
		boolean hasBaseDexClassLoader = true;
		try {
			Class.forName("dalvik.system.BaseDexClassLoader");
		} catch (ClassNotFoundException e) {
			hasBaseDexClassLoader = false;
		}
		// Log.e("zhou", "加载第二个dex"+libPath+"包名"+loadPacketName);
		// Log.e("zhou", "优化目录"+sApplication.getDir("dex",
		// 0).getAbsolutePath());

		String optdex = sApplication.getDir("dex", 0).getAbsolutePath();
		if ("com.nd.android.pandahome2".equals(pn)) {
			optdex = optdex + "/p1";
		} else {
			optdex = optdex + "/p2";
		}
		File file = new File(optdex);
		if (!file.exists()) {
			file.mkdirs();
			// Log.e("zhou", "创建文件夹="+optdex);
		}
		// Log.e("zhou", "创建文件夹="+optdex);
		if (hasBaseDexClassLoader) {
			PathClassLoader pathClassLoader = (PathClassLoader) sApplication.getClassLoader();
			DexClassLoader dexClassLoader = new DexClassLoader(libPath, optdex, libPath, sApplication.getClassLoader());
			try {
				// Log.e("zhou", "获取dex数组");
				Object dexElements[] = combineArray(getDexElements(getPathList(pathClassLoader)),
						getDexElements(getPathList(dexClassLoader)));
				Object pathList = getPathList(pathClassLoader);
				setField(pathList, pathList.getClass(), "dexElements", dexElements);
				return "SUCCESS";
			} catch (Throwable e) {
				// Log.e("zhou", "加载出错");
				e.printStackTrace();
				return Log.getStackTraceString(e);
			}
		}
		// Log.e("zhou", "加载成功");
		return "SUCCESS";
	}

	public Object getPathList(ClassLoader loader) {
		Object object = reflectField(loader, "dalvik.system.BaseDexClassLoader", "pathList");
		return object;
	}

	public Object getDexElements(Object object) throws Exception {
		Object object2 = reflectField(object, "dalvik.system.DexPathList", "dexElements");

		Object dexs[] = (Object[]) object2;
		// Log.e("zhou", "数量="+dexs.length);
		for (int i = 0; i < dexs.length; i++) {
			File file = (File) reflectField(dexs[i], "dalvik.system.DexPathList$Element", "file");
			// Log.e("zhou", "文件路径=" + file.toURL());
		}
		return object2;
	}

	public Object[] combineArray(Object element1, Object element2) {
		Object[] e1 = (Object[]) element1;
		Object[] e2 = (Object[]) element2;
		int count = e1.length + e2.length;

		Object result[] = null;
		try {
			result = (Object[]) Array.newInstance(Class.forName("dalvik.system.DexPathList$Element"), count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < e1.length; i++) {
			result[i] = e1[i];
		}
		for (int i = 0; i < e2.length; i++) {
			result[i + e1.length] = e2[i];
		}
		return result;
	}

	// 反射调用
	public static Object reflectField(Object object, String classname, String fieldName) {
		Object object2 = null;
		try {
			// Log.e("zhou", "开妈获取字段"+object.toString());
			Class<?> resClass = Class.forName(classname);
			Field field = resClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			object2 = field.get(object);
			// Log.e("zhou", "获取字段"+object2.toString());
		} catch (Exception e) {
			// Log.e("zhou", "反射出错");
			e.printStackTrace();
		}
		return object2;
	}

	public static void setField(Object object, Class<?> classes, String name, Object param) {
		try {
			Field field = classes.getDeclaredField(name);
			field.setAccessible(true);
			// Log.e("zhou", "参数="+param);
			field.set(object, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
