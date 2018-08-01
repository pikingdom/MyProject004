package com.nd.hilauncherdev.push.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nd.hilauncherdev.kitset.util.StringUtil;

public class NotifyPushInfo implements Parcelable {
	private int id;//推送的ID
	private String title;
	private String content;
	private String intent;
	private boolean persist = false;
	private boolean target = false;
	private String notifyIcon;//通知的图标
	private String notifyIconPath;//通知的图标的本地路径
	private String pos;//代表推送的位置，无值表示推送到通知栏，1推送的menu视图
	private int style;//代表推送样式
	public int sourceID = 0;//用来表示推送的电商广告源
	public boolean submitECShowUrl = false;//是否上报电商广告的曝光url
	
	/**
	 * 下发普通通知
	 */
	public static final int PUSH_COMMON_NOTIFICATION = 1 ;
	/**
	 * 下发大图通知
	 */
	public static final int PUSH_BIG_PIC_NOTIFICATION = 1 << 1;
	/**
	 * 下发通知到Menu
	 */
	public static final int PUSH_MENU_NOTIFICATION = 1 << 2;
	
	/**
	 * 下发通知到桌面顶部弹窗
	 */
	public static final int PUSH_POPUPWINDOW_NOTIFICATION = 1 << 3;

	/**
	 * 下发美图通知到桌面
	 */
	public static final int PUSH_FEED_MEITU_NOTIFICATION = 1 << 4;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isTarget() {
		return target;
	}
	public void setTarget(boolean target) {
		this.target = target;
	}
	public boolean isPersist() {
		return persist;
	}
	public void setPersist(boolean persist) {
		this.persist = persist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getNotifyIcon() {
		return notifyIcon;
	}
	public void setNotifyIcon(String notifyIcon) {
		this.notifyIcon = notifyIcon;
	}
	public String getNotifyIconPath() {
		return notifyIconPath;
	}
	public void setNotifyIconPath(String notifyIconPath) {
		this.notifyIconPath = notifyIconPath;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	
	/**
	 * 获取推送类型
	 * @return
	 */
	public int getType(){
		int type = PUSH_COMMON_NOTIFICATION;
		if(!StringUtil.isEmpty(getPos())){
			if(String.valueOf(PUSH_MENU_NOTIFICATION).equals(getPos())){
				type = PUSH_MENU_NOTIFICATION;
			}else if(String.valueOf(PUSH_POPUPWINDOW_NOTIFICATION).equals(getPos())){
				type = PUSH_POPUPWINDOW_NOTIFICATION;
			}else if(String.valueOf(PUSH_FEED_MEITU_NOTIFICATION).equals(getPos())){
				type = PUSH_FEED_MEITU_NOTIFICATION;
			}
		}
		return type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.title);
		dest.writeString(this.content);
		dest.writeString(this.intent);
		dest.writeByte(persist ? (byte) 1 : (byte) 0);
		dest.writeByte(target ? (byte) 1 : (byte) 0);
		dest.writeString(this.notifyIcon);
		dest.writeString(this.notifyIconPath);
		dest.writeString(this.pos);
		dest.writeInt(this.style);
	}

	public NotifyPushInfo() {
	}

	protected NotifyPushInfo(Parcel in) {
		this.id = in.readInt();
		this.title = in.readString();
		this.content = in.readString();
		this.intent = in.readString();
		this.persist = in.readByte() != 0;
		this.target = in.readByte() != 0;
		this.notifyIcon = in.readString();
		this.notifyIconPath = in.readString();
		this.pos = in.readString();
		this.style = in.readInt();
	}

	public static final Creator<NotifyPushInfo> CREATOR = new Creator<NotifyPushInfo>() {
		public NotifyPushInfo createFromParcel(Parcel source) {
			return new NotifyPushInfo(source);
		}

		public NotifyPushInfo[] newArray(int size) {
			return new NotifyPushInfo[size];
		}
	};
}
