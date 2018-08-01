package com.nd.hilauncherdev.myphone.mytheme.filter;

import java.io.Serializable;
import java.net.URL;
import android.net.Uri;

/**
 * 壁纸列表单项信息
 * 
 * @author caizp date 2011-8-9
 */
public class FilterItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 壁纸标识
	 */
	public String id;

	/**
	 * 壁纸名称
	 */
	public String name;

	/**
	 * 壁纸价格
	 */
	public String price;

	/**
	 * 壁纸大小
	 */
	public String size;

	/**
	 * 壁纸作者
	 */
	public String author;

	/**
	 * 壁纸下载次数
	 */
	public String downloads;

	/**
	 * 壁纸缩略图下载地址
	 */
	public URL thumbUrl;

	/**
	 * 壁纸原图下载地址
	 */
	public URL downloadUrl;

	/**
	 * 壁纸缩略图本地路径
	 */
	public Uri localThumbURI;

	/**
	 * 壁纸原图本地路径
	 */
	public Uri localURI;

	/**壁纸详情界面预览图*/
	public String detailUrl;
	
	public String localThumbPath;
	public String localWallPicPath;
	
	@Override
	public boolean equals(Object o) {
		if( o instanceof FilterItem ) {
			FilterItem item = (FilterItem)o;
			return this.id.equals( item.id );	
		}		
		return false;
	}

}
