package com.nd.hilauncherdev.kitset.fileselector;

import java.util.ArrayList;
import java.util.List;

/**
 * 图标模型
 */
public class IconModel {

	/**
	 * 图标包
	 */
	public static class IconPack {
		/** 图标包名称 */
		public String name = "";

		/** 图标apk包名称 com.nd... */
		public String packName = "";

		/** from */
		public String from = "";

		/** iconList */
		public final List<String> iconList = new ArrayList<String>();
	}

	/**
	 * 图标
	 */
	public static class Icon {
		/** pack */
		public IconPack pack;

		/** path */
		public String path;
	}
}
