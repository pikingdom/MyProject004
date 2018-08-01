package com.nd.android.update;

import java.io.File;
import java.io.Serializable;

import android.app.Notification;

public class DowningTaskItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String softName = null;
	private String softUrl;
	private String downloadDirPath = null;
	private String downloadFileName;
	private int    downloadIco = 0;
	private int    notificationId = 0;
	private Notification notification = null;
	// 文件存储
	private File downloadDir = null;
	private File downloadFile = null;
	
	//下载状态
	public int state;
	
	/**下载中*/
	public static final int DownState_Downing = 1;
	
	/**暂停*/
	public static final int DownState_Pause   = 2;
	
	/**下载完成*/
	public static final int DownState_Finish  = 3;
	
	/**下载失败*/
	public static final int DownState_Fail    = -1;
	
	public void setSoftName(String softName)
	{
		this.softName = softName;
	}

	public void setSoftUrl(String softUrl)
	{
		this.softUrl = softUrl;
	}

	public void setDownloadDirPath(String downloadDirPath)
	{
		this.downloadDirPath = downloadDirPath;
	}

	public void setDownloadFileName(String downloadFileName)
	{
		this.downloadFileName = downloadFileName;
	}

	public void setDownloadIco(int downloadIco)
	{
		this.downloadIco = downloadIco;
	}

	public void setSoftUid(int notificationId)
	{
		this.notificationId = notificationId;
	}
	
	public void setNotification(Notification notification){
		this.notification = notification;
	}
	
	public void setDownloadDir(File downloadDir){
		this.downloadDir=downloadDir;
	}
	
	public void setDownloadFile(File downloadFile){
		this.downloadFile=downloadFile;
	}
	
	public String getSoftUrl(){
		return softUrl;
	}
	
	public String getSoftName(){
		return softName;
	}
	
	public String getDownLoadDirPath(){
		return downloadDirPath;
	}
	
	public int getDownLoadIcon(){
		return downloadIco;
	}
	
	public int getUid(){
		return notificationId;
	}
	
	public Notification getNotification(){
		return notification;
	}
	
	public String getDownloadFileName(){
		return downloadFileName;
	}
	
	public File getDownloadDir(){
		return downloadDir;
	}
	
	public File getDownloadFile(){
		return downloadFile;
	}

}
