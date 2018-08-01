package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nd.hilauncherdev.analysis.ThemeDownloadPathAnalysis;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.module.ModuleConstant;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * 与6.0桌面的交互接口
 * @author cfb
 *
 */
public class ThemeShopV6ThemeInstallAPI {

	/**
	 * 接收商城安装APT主题请求后返回处理结果(参数为themeid)
	 */
	public static final String THEME_APT_INSTALL_RESULT = "nd.pandahome.response.theme.apt.install";
	
	/**安装失败*/
	public static final String THEME_APT_INSTALL_RESULT_FAIL = "nd.pandahome.response.theme.apt.install.fail";

	/**
	 * 广播参数主题ID
	 */
	public static final String THEME_PARAMETER_THEME_ID = "themeid";
	
	/**
	 * 广播参数服务端主题ID
	 */
	private static final String THEME_PARAMETER_SERVER_THEME_ID = "serverThemeID";
	
	public static final Set<String> installIngSet = Collections.synchronizedSet(new HashSet<String>());
	
	public static void sendInstallStyle(final Context context, final String aptPath, final String serverThemeID, final BaseDownloadInfo downloadInfo){
		
		if (installIngSet.contains(serverThemeID)){
			return;
		}
		
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				try {
					if (aptPath==null){
						return;
					}
					String diskFile = aptPath;
					if (diskFile.endsWith(".temp")) {
						diskFile = diskFile.substring(0, diskFile.indexOf(".temp"));
					}
					if (diskFile.endsWith(ThemeGlobal.SUFFIX_APT_THEME)) {
						installIngSet.add(serverThemeID);
						final String resultThemeId = ThemeManagerFactory.getInstance().installAptTheme(diskFile);
						installIngSet.remove(serverThemeID);
						//提交主题安装成功统计
						HashMap<String, String> infoMap = downloadInfo.getAdditionInfo();
						String serThemeId = infoMap.get("serThemeId");
						String themeSp = infoMap.get("themeSp");
						String postionTypeString = infoMap.get("postionType");
						String postionTypeIdString = infoMap.get("postionTypeId");
						String resAttributesString = infoMap.get("ResAttributes");
						String themeAttributesString = infoMap.get("ThemeAttributes");
						int resAttributes = ThemeDownloadPathAnalysis.RES_ATTRIBUTES_FREE;
						if(!TextUtils.isEmpty(resAttributesString)) {
							try {
								resAttributes = Integer.parseInt(resAttributesString);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						int postionType = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
						int postionTypeId = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
						if(postionTypeString != null && postionTypeIdString != null){
							try{
								postionType = Integer.parseInt(postionTypeString);
								postionTypeId = Integer.parseInt(postionTypeIdString);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
						if(!StringUtil.isEmpty(resultThemeId)){
							
							downloadInfo.putAdditionInfo(StyleFileHelper.additionThemeKey, resultThemeId);
							DownloadManager.getInstance().modifyAdditionInfo(downloadInfo);
							
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							
							Intent themeListRefreshIntent = new Intent(ThemeGlobal.INTENT_THEME_LIST_REFRESH);
							context.sendBroadcast(themeListRefreshIntent);
							
							Intent styleListRefreshIntent = new Intent("nd.panda.style.list.refresh");
							context.sendBroadcast(styleListRefreshIntent);
							FileUtil.delFile(aptPath);
							if (serThemeId != null && themeSp != null){
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, ThemeDownloadPathAnalysis.RES_TYPE_STYLE, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_SUCCESS,postionType,postionTypeId,resAttributes);
							}
							// 重新下载主题后，如果是当前主题则重新应用一次  caizp 2015-4-3
							if(ThemeManagerFactory.getInstance().getCurrentTheme().getThemeId().equals(resultThemeId)) {
								ThreadUtil.execute(new Runnable() {
									@Override
									public void run() {
										ThemeManagerFactory.getInstance().applyThemeWithOutWaitDialog(context, resultThemeId, false, false, false, true);
									}
								});
							}
						}else{
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT_FAIL);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							Log.d("ThemeShopV3LauncherExAPI", "风格主题安装失败");
							if (serThemeId != null && themeSp != null){
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, ThemeDownloadPathAnalysis.RES_TYPE_STYLE, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void sendInstallAPT(final Context context, final String aptPath, final String serverThemeID, final BaseDownloadInfo downloadInfo){
		
		if (installIngSet.contains(serverThemeID)){
			return;
		}
		
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				try {
					if (aptPath==null){
						return;
					}
					String diskFile = aptPath;
					if (diskFile.endsWith(".temp")) {
						diskFile = diskFile.substring(0, diskFile.indexOf(".temp"));
					}
					if (diskFile.endsWith(ThemeGlobal.SUFFIX_APT_THEME)) {
						installIngSet.add(serverThemeID);
						final String resultThemeId = ThemeManagerFactory.getInstance().installAptTheme(diskFile);
						installIngSet.remove(serverThemeID);
						//提交主题安装成功统计
						HashMap<String, String> infoMap = downloadInfo.getAdditionInfo();
						String serThemeId = infoMap.get("serThemeId");
						String themeSp = infoMap.get("themeSp");
						String postionTypeString = infoMap.get("postionType");
						String postionTypeIdString = infoMap.get("postionTypeId");
						String resAttributesString = infoMap.get("ResAttributes");
						String themeAttributesString = infoMap.get("ThemeAttributes");
						int resAttributes = ThemeDownloadPathAnalysis.RES_ATTRIBUTES_FREE;
						if(!TextUtils.isEmpty(resAttributesString)) {
							try {
								resAttributes = Integer.parseInt(resAttributesString);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						int postionType = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
						int postionTypeId = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
						if(postionTypeString != null && postionTypeIdString != null){
							try{
								postionType = Integer.parseInt(postionTypeString);
								postionTypeId = Integer.parseInt(postionTypeIdString);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
						if(!StringUtil.isEmpty(resultThemeId)){
							
							downloadInfo.putAdditionInfo(ThemeFileHelper.AdditionKey, resultThemeId);
							DownloadManager.getInstance().modifyAdditionInfo(downloadInfo);
							
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							
							Intent themeListRefreshIntent = new Intent(ThemeGlobal.INTENT_THEME_LIST_REFRESH);
							context.sendBroadcast(themeListRefreshIntent);
							FileUtil.delFile(aptPath);
							if (serThemeId != null && themeSp != null){
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_SUCCESS,postionType,postionTypeId,resAttributes);
							}
							// 重新下载主题后，如果是当前主题则重新应用一次  caizp 2015-4-3
							if(ThemeManagerFactory.getInstance().getCurrentTheme().getThemeId().equals(resultThemeId)) {
								ThreadUtil.execute(new Runnable() {
									@Override
									public void run() {
										ThemeManagerFactory.getInstance().applyThemeWithOutWaitDialog(context, resultThemeId, false, false, false, true);
									}
								});
							}
							//游戏主题迁移游戏主题推荐数据
							if("1".equals(themeAttributesString)){
								String srcPath = CommonGlobal.getBaseDir() + "/caches/gametheme/" + serThemeId;
								String desPath = BaseConfig.THEME_DIR + resultThemeId.replace(" ", "_")+"/gametheme";
								copyGameThemeInfo(srcPath,desPath);
							}
							// 迁移
							transferThemeCampaignInfo(BaseConfig.CACHES_HOME + resultThemeId + ".tcdat", BaseConfig.THEME_DIR + resultThemeId.replace(" ", "_"), "theme_campaign_info");
						}else{
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT_FAIL);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							
							Log.d("ThemeShopV3LauncherExAPI", "主题安装失败");
							
							if (serThemeId != null && themeSp != null){
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void sendInstallModule(final Context context, final String aptPath, final String serverThemeID, final String moduleKey, final BaseDownloadInfo downloadInfo){
		
		if (installIngSet.contains(serverThemeID)){
			return;
		}
		
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				try {
					if (aptPath==null){
						return;
					}
					String diskFile = aptPath;
					if (diskFile.endsWith(".temp")) {
						diskFile = diskFile.substring(0, diskFile.indexOf(".temp"));
					}
					if (diskFile.endsWith(ThemeGlobal.SUFFIX_APT_THEME)) {
						installIngSet.add(serverThemeID);
						String resultThemeId = ThemeManagerFactory.getInstance().installAptThemeModule(diskFile, moduleKey);
						installIngSet.remove(serverThemeID);
						if(!StringUtil.isEmpty(resultThemeId)){
							
							downloadInfo.putAdditionInfo(ThemeFileHelper.AdditionKey, resultThemeId);
							DownloadManager.getInstance().modifyAdditionInfo(downloadInfo);
							
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							
							Intent moduleListRefreshIntent = new Intent(ModuleConstant.INTENT_MODULE_LIST_REFRESH);
							context.sendBroadcast(moduleListRefreshIntent);
							FileUtil.delFile(aptPath);
						}else{
							Intent aptInstallResultIntent = new Intent(THEME_APT_INSTALL_RESULT_FAIL);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_SERVER_THEME_ID, serverThemeID);
							aptInstallResultIntent.putExtra(THEME_PARAMETER_THEME_ID, resultThemeId);
							aptInstallResultIntent.addFlags(32);
							context.sendBroadcast(aptInstallResultIntent);
							
							Log.d("ThemeShopV3LauncherExAPI", "模块安装失败");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 拷贝游戏主题数据
	 */
	private static void copyGameThemeInfo(String srcPath,String desPath){
		File infoFile = new File(srcPath + "/info");
		File imgFile = new File(srcPath + "/icon");
		if (!infoFile.exists() || !imgFile.exists()){
			return;
		}
		File desFile = new File(desPath);
		if (!desFile.exists()){
			desFile.mkdirs();
		}
		//复制数据
		FileUtil.copy(infoFile.getPath(),desPath + "/info");
		FileUtil.copy(imgFile.getPath(), desPath + "/icon");
		FileUtil.delAllFile(srcPath);
	}
	
	/**
	 * <br>Description: 迁移主题活动数据
	 * <br>Author:caizp
	 * <br>Date:2016年1月15日下午4:57:19
	 * @param srcFile
	 * @param desFile
	 */
	private static void transferThemeCampaignInfo(String srcFile, String desPath, String desFileName){
		File infoFile = new File(srcFile);
		if (!infoFile.exists()){
			return;
		}
		File desFile = new File(desPath);
		if (!desFile.exists()){
			desFile.mkdirs();
		}
		FileUtil.copy(srcFile, desPath + "/" + desFileName);
		FileUtil.delFile(srcFile);
	}
	
}
