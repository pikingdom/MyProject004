package com.nd.hilauncherdev.kitset.util;

import java.io.File;

import android.content.Context;

public class DynamicPluginUtil {

	/**
	 * 获取加载端包的file路径,如果不存在file路径,则创建
	 * 
	 * @Title: getRootApkFile
	 * @author lytjackson@gmail.com
	 * @date 2013-12-13
	 * @param ctx
	 * @return
	 */
	public static String getRootApkFile(Context ctx) {
		File root = ctx.getFilesDir();
		if (null == root) {
			root = new File("/data/data/" + ctx.getPackageName() + "/files");
		}
		if (!root.exists()) {
			root.mkdir();
		}
		return root.getAbsolutePath();
	}

	/**
	 * 清理插件，不保留数据
	 * 
	 * @Function: 
	 *            com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil.clearPluginDir
	 * @Description:
	 * @param ctx
	 * @param pluginPackageName
	 * 
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月7日 下午3:43:28
	 * 
	 */
	public static void clearPluginDir(Context ctx, String pluginPackageName) {
		String path = getRootApkFile(ctx) + "/" + pluginPackageName;
		File pluginFolder = new File(path);
		if (null != pluginFolder && pluginFolder.exists()) {
			FileUtil.delFolder(path);
		}
	}

	/**
	 * 清理插件旧版dex代码，保留旧数据
	 * 
	 * @Function: com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil.
	 *            clearPluginDirWithoutData
	 * @Description:
	 * @param ctx
	 * @param pluginPackageName
	 * 
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月7日 下午3:43:02
	 * 
	 */
/*	public static void clearPluginDirWithoutData(Context ctx, String pluginPackageName) {
		FileUtil.delFile(getRootApkFile(ctx) + "/" + pluginPackageName + "/" + pluginPackageName + ".dex");
	}*/

	/**
	 * 查询动态插件的dex是否已经解压到file目录下
	 * 
	 * @Title: isPluginDexExisted
	 * @author lytjackson@gmail.com
	 * @date 2013-12-19
	 * @param ctx
	 * @param pluginPackageName
	 * @return
	 */
	public static boolean isPluginDexExisted(Context ctx, String pluginPackageName) {
		boolean isExisted = false;
		File dex = new File(getRootApkFile(ctx) + "/" + pluginPackageName + "/" + pluginPackageName + ".dex");
		if (null != dex && dex.exists()) {
			isExisted = true;
		}
		return isExisted;
	}

	public static boolean isStringEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * 动态插件是否已经启用
	 * 
	 * @Title: isPluginEnabled
	 * @author lytjackson@gmail.com
	 * @date 2014-4-8
	 * @param ctx
	 * @param pckageName
	 * @return
	 */
	public static boolean isPluginEnabled(Context ctx, String path, String packageName) {
		return isPluginEnabled(ctx, path, packageName, null);
	}

	/**
	 * 动态插件是否已经启用
	 * 支持多个包名查询
	 * @Title: isPluginEnabled
	 * @author lytjackson@gmail.com
	 * @date 2014-4-8
	 * @param ctx
	 * @param pckageNames[]
	 * @return
	 */
	public static boolean isPluginEnabled(Context ctx, String path, String packageName, String[] packageNames) {
		if (isStringEmpty(path) || isStringEmpty(packageName)) {
			return false;
		}
		if (checkPackageEnabled(ctx, path, packageName)) {
			return true;
		}
		if (null == packageNames || packageNames.length < 1) {
			return false;
		}
		for (int i = 0; i < packageNames.length; i++) {
			if (checkPackageEnabled(ctx, path, packageNames[i])) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkPackageEnabled(Context ctx, String path, String packageName) {
		String plugin = path + packageName + ".jar";
		File apkFile = new File(plugin);
		if (apkFile.exists() && isPluginDexExisted(ctx, packageName)) {
			return true;
		}
		return false;
	}
}
