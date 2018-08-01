package com.nd.hilauncherdev.webconnect.downloadmanage.model.state;

import android.content.Context;

import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

/** 
 * 状态机接口
 * 
 * @author pdw 
 * @date 2012-9-19 下午03:52:10 
 */
public interface IDownloadStateHelper {
	
	/**
	 * 功能开关
	 * 为了保持良好的扩展性和可用性，改由在接口中实时的传入上下文 At 2012-11-22
	 * 
	 */
	public boolean action(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) ;
	
	/**
	 * 初始化视图
	 */
	public void initView(ViewHolder viewHolder, BaseDownloadInfo downloadInfo) ;
	
	/**
	 * 获取状态
	 */
	public int getState() ;
	
}
