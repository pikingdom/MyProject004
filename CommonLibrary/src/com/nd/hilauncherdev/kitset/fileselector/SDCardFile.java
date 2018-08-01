package com.nd.hilauncherdev.kitset.fileselector;

import java.io.File;

import android.graphics.drawable.Drawable;

/**
 * sd卡文件数据描述
 */
public class SDCardFile {
	public static final int ICON_FILE_DIR = 9;
	public static final int ICON_FILE_PACK = 8;
	public static final int ICON_FILE = 7;
	private File file;

	private Drawable icon;

	private String title;

	public boolean filtered;

	public int type = 0;
	private Object tag;

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public SDCardFile(File file) {
		setFile(file);
	}

	public SDCardFile(int type) {
		this.type = type;
	}

	public boolean isFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isNULLFile() {
		return null == file ? true : false;
	}
}
