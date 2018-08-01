package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.nd.hilauncherdev.analysis.ThemeDownloadPathAnalysis;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.theme.data.BasePandaTheme;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.exception.PandaDesktopException;
import com.nd.hilauncherdev.theme.parse.apt.ThemeLoader;
import com.nd.hilauncherdev.theme.pref.ThemeSeriesSharePref;
import com.nd.hilauncherdev.theme.series.ThemeSeriesAssit;
import com.nd.hilauncherdev.theme.series.ThemeSeriesInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

/**
 * <br>Description: 个性化商城主题系列安装
 * <br>Author:caizp
 * <br>Date:2015年5月12日下午5:21:28
 */
public class ThemeShopV6SeriesInstallAssit {
	
	private static class InstallThemeInfo {
		public ConcurrentHashMap<String, String> needInstallThemeServerIds;
		public String[] intsalledThemeIds;
	}
	
	/**
	 * 主题系列安装集合
	 */
	private static final Map<String, InstallThemeInfo> installIngSeriesMap = new ConcurrentHashMap<String, InstallThemeInfo>();

	/**
	 * <br>Description: 安装主题系列（主题下载完成后立即安装）
	 * <br>Author:caizp
	 * <br>Date:2015年5月13日上午11:22:19
	 * @param seriesId
	 * @param needInstallThemeServerId
	 * @param downloadIndex
	 * @param seriesDownloadInfo
	 */
	public static void installSeriesTheme(final Context context, final String seriesId, final BaseDownloadInfo seriesDownloadInfo) {
		if(TextUtils.isEmpty(seriesId) || null == seriesDownloadInfo) return;
		final List<BaseDownloadInfo> subDownloadInfos = seriesDownloadInfo.getInnerSubBaseDownloadInfoS();
		if(null == subDownloadInfos || subDownloadInfos.size() <= 0) return;
		final int downloadIndex = seriesDownloadInfo.getFinishIndex()-1;
		if(downloadIndex >= subDownloadInfos.size()) return;
		final String needInstallThemeServerId = subDownloadInfos.get(downloadIndex).getIdentification();
		final String needInstallThemeAptPath = subDownloadInfos.get(downloadIndex).getSavedDir() + subDownloadInfos.get(downloadIndex).getSavedName();
		if(TextUtils.isEmpty(needInstallThemeServerId)) return;
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				InstallThemeInfo installThemeInfo = installIngSeriesMap.get(seriesId);
				if(null == installThemeInfo) {//主题系列安装任务不存在则创建
					installThemeInfo = new InstallThemeInfo();
					installThemeInfo.needInstallThemeServerIds = new ConcurrentHashMap<String, String>();
					installThemeInfo.intsalledThemeIds = new String[subDownloadInfos.size()];
					for(int i=0; i<subDownloadInfos.size(); i++) {
						BaseDownloadInfo subDownloadInfo = subDownloadInfos.get(i);
						if(null == subDownloadInfo) continue;
						String themeServerId = subDownloadInfo.getIdentification();
						String fileDir = subDownloadInfo.getSavedDir();
						String fileName = subDownloadInfo.getSavedName();
						String installTheme = ThemeSeriesSharePref.getStringValueByKey(context, seriesId+"#"+themeServerId);
						if(!TextUtils.isEmpty(installTheme)) {// 初始化已安装过的主题
							try {
								String[] installInfo = installTheme.split("\\|");
								if(null != installInfo && 2 == installInfo.length) {
									int index = Integer.parseInt(installInfo[1]);
									installThemeInfo.intsalledThemeIds[index] = installInfo[0];
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {// 添加到需安装列表中
							installThemeInfo.needInstallThemeServerIds.put(themeServerId, fileDir + fileName);
						}
					}
					installIngSeriesMap.put(seriesId, installThemeInfo);
				}
				if(TextUtils.isEmpty(needInstallThemeAptPath))return;
				try {
					// 提交主题系列安装统计数据
					HashMap<String, String> infoMap = seriesDownloadInfo.getAdditionInfo();
					String themeSp = infoMap.get("themeSp");
					String postionTypeString = infoMap.get("postionType");
					String postionTypeIdString = infoMap.get("postionTypeId");
					String resAttributesString = infoMap.get("ResAttributes");
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
					// 主题的SP来源固定为主题系列
					String themeSeriesSp = String.valueOf(ThemeDownloadPathAnalysis.SP_THEME_SERIES);
					BasePandaTheme theme = ThemeLoader.getInstance().loaderThemeZip(needInstallThemeAptPath, seriesId);
					String resultThemeId = theme.getThemeId();
					if(StringUtil.isEmpty(resultThemeId)) {
						//主题安装失败
						ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(needInstallThemeServerId, themeSeriesSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
						return;
					}
					//主题安装成功
					ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(needInstallThemeServerId, themeSeriesSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_SUCCESS,postionType,postionTypeId,resAttributes);
					// 保存主题系列中已安装成功的主题信息
					ThemeSeriesSharePref.setStringValueByKey(context, seriesId+"#"+needInstallThemeServerId, resultThemeId+"|"+downloadIndex);
					installThemeInfo.needInstallThemeServerIds.remove(needInstallThemeServerId);
					installThemeInfo.intsalledThemeIds[downloadIndex] = resultThemeId;
					Intent intent = null;
					if(installThemeInfo.needInstallThemeServerIds.isEmpty()) {//主题系列安装
						installIngSeriesMap.remove(seriesId);
						String allThemeIds = "";
						//拼接主题系列中所有主题ID，以"|"分隔
						for(int i=0; i<installThemeInfo.intsalledThemeIds.length; i++) {
							if(0 != i) {//主题ID以“|”分隔
								allThemeIds += "|";
							}
							allThemeIds += installThemeInfo.intsalledThemeIds[i];
						}
						if(TextUtils.isEmpty(allThemeIds)) {//安装失败
							//主题系列安装失败
							ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(seriesId, 36, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
							intent = new Intent(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_FAIL);
							intent.putExtra("seriesId", seriesId);
							context.sendBroadcast(intent);
							return;
						}
						final String seriesId = seriesDownloadInfo.getAdditionInfo().get("serThemeId");
						final String seriesName = seriesDownloadInfo.getAdditionInfo().get("seriesName");
						final String seriesMarkName = seriesDownloadInfo.getAdditionInfo().get("seriesMarkName");
						final String seriesDesc = seriesDownloadInfo.getAdditionInfo().get("seriesDesc");
						ThemeSeriesInfo seriesInfo = new ThemeSeriesInfo();
						seriesInfo.seriesId = seriesId;
						seriesInfo.seriesName = seriesName;
						seriesInfo.seriesMark = seriesMarkName;
						seriesInfo.seriesDesc = seriesDesc;
						seriesInfo.themeIds = allThemeIds;
						// 把主题系列信息写入SD卡
						if(!FileUtil.writeObjectToFile(BaseConfig.THEME_SERIES_DIR + seriesInfo.seriesId + "/" + ThemeGlobal.SERIES_INFO_FILE, seriesInfo)) {
							//主题系列安装失败
							ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(seriesId, 36, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
							intent = new Intent(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_FAIL);
							intent.putExtra("seriesId", seriesId);
							context.sendBroadcast(intent);
							return;
						}
						// 把主题系列写入数据库并更新所包含主题记录的父ID
						if(ThemeSeriesAssit.saveThemeSeriesInfo2Db(seriesInfo)) {
							context.sendBroadcast(new Intent(ThemeGlobal.INTENT_THEME_LIST_REFRESH));
							intent = new Intent(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_SUCCESS);
							intent.putExtra("seriesId", seriesId);
							context.sendBroadcast(intent);
							for(int i=0; i<subDownloadInfos.size(); i++) {
								BaseDownloadInfo subDownloadInfo = subDownloadInfos.get(i);
								if(null == subDownloadInfo) continue;
								String themeServerId = subDownloadInfo.getIdentification();
								String fileDir = subDownloadInfo.getSavedDir();
								String fileName = subDownloadInfo.getSavedName();
								// 安装主题系列主题成功后删除所有apt包
								FileUtil.delFile(fileDir + fileName);
								// 清除主题系列中已安装成功的主题信息
								ThemeSeriesSharePref.removeByKey(context, seriesId+"#"+themeServerId);
							}
							//主题系列安装成功
							ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(seriesId, 36, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_SUCCESS,postionType,postionTypeId,resAttributes);
						} else {
							//主题系列安装失败
							ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(seriesId, 36, themeSp, ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL,postionType,postionTypeId,resAttributes);
							intent = new Intent(ThemeGlobal.ACTION_INSTALL_THEME_SERIES_FAIL);
							intent.putExtra("seriesId", seriesId);
							context.sendBroadcast(intent);
						}
					}
				} catch (PandaDesktopException e) {
					e.printStackTrace();
				}
				
			}
		});

	}
	
}
