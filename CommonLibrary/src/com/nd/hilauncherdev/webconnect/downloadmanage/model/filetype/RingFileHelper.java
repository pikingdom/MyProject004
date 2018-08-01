package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

public class RingFileHelper implements IFileTypeHelper {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void onClickWhenFinished(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(ctx, "com.nd.hilauncherdev.myphone.myring.online.OnLineRingActivity"));
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("invoke_from_launcher_editor", true);
			intent.putExtra("ring_path", downloadInfo.getFilePath());
			ctx.startActivity(intent);
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		return CommonGlobal.getApplicationContext().getResources().getString(R.string.common_button_set);
	}
	
	@Override
	public void onDownloadCompleted(final Context ctx, final BaseDownloadInfo info, String file) {
		ThreadUtil.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String content = info.getAdditionInfo().get("data");
					String[] arrayOfString = content.split(",");
					String path = info.getSavedDir() + info.getSavedName();
					MediaPlayer player = MediaPlayer.create(ctx, Uri.parse("file://" + path));
					long duration = 0;
					try {
						if(player != null){
							duration = player.getDuration();
						}
					} catch (Exception e) {
						
					}finally{
						if(player != null){
							player.release();
						}
					}
					
					// 后来增加的包含其他信息
					StringBuffer postfix = new StringBuffer("");
					boolean isFromKuyin = false;
					for(int i=6;i<arrayOfString.length;i++){
						postfix.append("," + changeSpliter(arrayOfString[i]));
						if(arrayOfString[i].trim().equals("ringsource=16")){
							isFromKuyin = true;
						}
					}
					StringBuffer sb = new StringBuffer("");
					sb.append(arrayOfString[0] + "," + changeSpliter(arrayOfString[1]) + 
							"," + duration + "," + changeSpliter(arrayOfString[3]) + 
							"," + changeSpliter(arrayOfString[4]) + "," + changeSpliter(path) + postfix.toString() +"\r\n");
					
					append(sb.toString());
					// 为酷音资源的下载，打点
					if(isFromKuyin){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.PLUGIN_COOL_RINGTONES, "xz");
					}
				} catch (Exception e) {
				}
			}
		});
	
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_ring_icon";
	}
	
private static final String BASE_DIR = CommonGlobal.getBaseDir() + "/myphone/myring";
	
	private void append(String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			// FileWriter writer = new FileWriter(fileName, true);
			// writer.write(content);
			// writer.close();
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(BASE_DIR + "/detail.dat" , true), "GBK");
			out.write(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String changeSpliter(String paramString) {
		String str;
		if (paramString != null)
			str = paramString.replaceAll("," , "，");
		else
			str = paramString;
		return str;
	}
	
	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		if (downloadInfo != null) {
			return downloadInfo.fileExists();
		}
		return false;
	}
}
