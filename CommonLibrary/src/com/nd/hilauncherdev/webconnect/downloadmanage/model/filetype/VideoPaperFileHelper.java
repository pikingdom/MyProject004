package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.felink.videopaper.loader.NativeHelper;
import com.felink.videopaper.model.VideoPaperBean;
import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.analysis.ThemeDownloadPathAnalysis;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.DigestUtils;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

import org.json.JSONObject;

import java.util.HashMap;

public class VideoPaperFileHelper implements IFileTypeHelper {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 视频壁纸安装成功
	 */
	public static final String VIDEO_PAPER_INSTALL_SUCCESS = "com.nd.hilauncherdev.myphone.mytheme.videopaper.install.success";
	/**
	 * 视频壁纸安装失败
	 */
	public static final String VIDEO_PAPER_INSTALL_FAIL = "com.nd.hilauncherdev.myphone.mytheme.videopaper.install.fail";
	
	private final static String CACHES_HOME_MARKET = CommonGlobal.getBaseDir() + "/caches/91space/";
	
	@Override
	public void onClickWhenFinished(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		VideoPaperBean videoPaperBean = getVideoPaper(downloadInfo);
		if(null != videoPaperBean) {// 视频壁纸
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(ctx, "com.nd.hilauncherdev.shop.shop6.videowallpaper.ThemeShopV8VideoPaperDetailActivity"));
			intent.putExtra("video_id", videoPaperBean.vid);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			SystemUtil.startActivitySafely(ctx, intent);
			return;
		}
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		return CommonGlobal.getApplicationContext().getResources().getString(R.string.downloadmanager_preview);
	}
	
	@Override
	public void onDownloadCompleted(final Context ctx, final BaseDownloadInfo downloadInfo, String file) {
		if(downloadInfo.getFinishIndex() != downloadInfo.getTotalSubDownloadListSize()) return;
		final VideoPaperBean videoPaperBean = getVideoPaper(downloadInfo);
		if(null != videoPaperBean) {// 视频壁纸
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					int installResult = ThemeDownloadPathAnalysis.FLAG_INSTALL_FAIL;
					boolean result = NativeHelper.save(videoPaperBean);
					Intent intent = new Intent(VIDEO_PAPER_INSTALL_SUCCESS);
					if(!result) {
						NativeHelper.delete(videoPaperBean.vid, videoPaperBean.identifier);
						intent = new Intent(VIDEO_PAPER_INSTALL_FAIL);
					} else {// 安装成功，刷新本地视频壁纸列表
						installResult = ThemeDownloadPathAnalysis.FLAG_INSTALL_SUCCESS;
						ctx.sendBroadcast(new Intent("com.nd.launcher.internal.refresh.local.videopaper.list"));
					}
					intent.putExtra("video_id_identifier", videoPaperBean.vid+"_"+videoPaperBean.identifier);
					ctx.sendBroadcast(intent);
					// 视频壁纸安装统计上报
					HashMap<String, String> infoMap = downloadInfo.getAdditionInfo();
					String serThemeId = infoMap.get("serThemeId");
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
					// 上报视频壁纸安装结果
					ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(
							serThemeId, ThemeDownloadPathAnalysis.RES_TYPE_VIDEO_PAPER, themeSp,
							installResult,
							postionType,
							postionTypeId,
							resAttributes);
				}
			});
			return;
		}
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_wallpaper_icon";
	}
	
	/**
	 * 
	 * @param url
	 * @param rootpath  结尾带"/"
	 * @return
	 */
	public static String url2path (String url, String rootpath){
		if (url==null)
			url = "";
		String rs = rootpath;
		String picname = getMd5FileName(url);
		rs = rs+picname;
		//TODO 兼容旧版本
		//rs = StringUtil.renameRes(rs);
		return rs;
	}
	
	private static String getMd5FileName(String url){
		if ( url==null || "".equals(url) )
			return "";
		String str = DigestUtils.md5Hex(url)+"";
		return str; 
	}
	
	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		final VideoPaperBean videoPaperBean = getVideoPaper(downloadInfo);
		if(null != videoPaperBean) {// 视频壁纸
			return NativeHelper.hasNativeVideo(videoPaperBean.vid, videoPaperBean.identifier);
		}
		if (downloadInfo != null) {
			return downloadInfo.fileExists();
		}
		return false;
	}

	/**
	 * 获取视频壁纸bean
	 * @param downloadInfo
	 * @return
     */
	private VideoPaperBean getVideoPaper(BaseDownloadInfo downloadInfo) {
		if(null == downloadInfo) return null;
		String videoPaperBeanJson = downloadInfo.getAdditionInfo().get("videoPaperBean");
		if(TextUtils.isEmpty(videoPaperBeanJson)) return null;
		try {
			return NativeHelper.json2NativeBean(new JSONObject(videoPaperBeanJson));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
