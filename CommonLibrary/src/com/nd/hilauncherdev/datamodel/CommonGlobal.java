package com.nd.hilauncherdev.datamodel;

import android.content.Context;
import android.os.Environment;

//import com.baidu91.account.login.LoginManager;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysis;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.sdk.AdvertSDKController;
import com.nd.hilauncherdev.sdk.AppDistributionSDKController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CommonGlobal extends BaseConfig{
	/** OLD_DIR */
	public final static String OLD_BASE_DIR = Environment.getExternalStorageDirectory() + "/PandaHome2";
	
	/** BASE_DIR */
	//public final static String BASE_91_DIR = Environment.getExternalStorageDirectory() + "/91 WireLess";
	
//	/** BASE_DIR */
//	public final static String BASE_DIR = Environment.getExternalStorageDirectory() + "/PandaHome2";

    //public static final String INTENT_USER_STAT = "pandahome.intent.userStat";

//	/**
//	 * root权限的命令行文件名，可执行root权限的命令
//	 */
//	public static final String SUPER_SHELL_FILE_NAME = "panda_super_shell";
//	/**
//	 * 调用 root权限的shell时传进的参数，过滤权限，防止其他应用的操作
//	 */
//	public static final String SUPER_SHELL_PERMISSION = "com.nd.android.launcher.permission.SUPER_SHELL";

	/**
	 * 关闭百变锁屏LockActivity Action
	 */
	//public static final String COM_ND_ANDROID_PANDALOCK_CLOSE_LOCK_ACTIVITY = "com.nd.android.pandahome.close_lock_activity";
	
	/**
	 * 开启百变锁屏LockActivity Action
	 */
	//public static final String COM_ND_ANDROID_PANDALOCK_OPEN_LOCK_ACTIVITY = "com.nd.android.pandahome.open_lock_activity";
	
	/**
	 * Description:是否为百度桌面
	 * Author: guojianyun_91 
	 * Date: 2015年4月22日 下午1:47:23
	 * @return
	 */
	public static boolean isBaiduLauncher(){
		return !is91Launcher && "com.baidu.android.launcher".equals(PKG_NAME);
	}

	/**
	 * Description:非91桌面
	 * Author: chensuqi
	 * Date: 2016年05月6日
	 * @return
	 */
	public static boolean isAndroidLauncher(){
		return !is91Launcher;
	}
	
	public static void initDain91SDK(Context context){
		HiAnalytics.init(context);
		CvAnalysis.init(context);
		AdvertSDKController.init(context);
//		LoginManager.getInstance().init(context);
		AppDistributionSDKController.getInstance().init(context);
	}
	
	public static ImageLoader getImageLoader(){
		ImageLoader imageLoader = ImageLoader.getInstance();
		if(!imageLoader.isInited()){
			imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		}
		return imageLoader;
	}
}
