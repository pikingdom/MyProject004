package com.nd.hilauncherdev.myphone.swapwallpaper.bean;


public class WallpaperItemInfo extends WallPaperLocalItem{
	public String wallPaperurl; 
	public int isUsed;//(0：已使用过，1：未使用过)
	public String typeId;
	public String wPaperType;
	public String fileName;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((wallPaperurl == null) ? 0 : wallPaperurl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WallpaperItemInfo other = (WallpaperItemInfo) obj;
		if (wallPaperurl == null) {
			if (other.wallPaperurl != null)
				return false;
		} else if (!wallPaperurl.equals(other.wallPaperurl))
			return false;
		return true;
	}
	
	
	
}
