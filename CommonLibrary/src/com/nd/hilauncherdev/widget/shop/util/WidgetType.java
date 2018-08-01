package com.nd.hilauncherdev.widget.shop.util;

import com.nd.hilauncherdev.datamodel.CommonGlobal;

public enum WidgetType {
	/**
	 * 桌面小插件
	 */
	LAUNCHER_TYPE {
		@Override
		public String getBaseDir() {
			return CommonGlobal.WIDGET_PLUGIN_DIR;
		}

		@Override
		public String getAssetsDir() {
			return "plugin/dynamic/";
		}
	},
	/**
	 * Myphone功能插件
	 */
	MYPHONE_TYPE {
		@Override
		public String getBaseDir() {
			return CommonGlobal.PLUGIN_DIR;
		}

		@Override
		public String getAssetsDir() {
			return "plugin/";
		}
	},
	/**
	 * 未知类型插件
	 */
	UNKNOWN_TYPE {
		@Override
		public String getBaseDir() {
			return "";
		}

		@Override
		public String getAssetsDir() {
			return "";
		}
	};
	/**
	 * 获取该类型插件使用时的所在目录
	 * 
	 * @Title: getBaseDir
	 * @author lytjackson@gmail.com
	 * @date 2014-1-14
	 * @return
	 */
	public abstract String getBaseDir();

	/**
	 * 获取该类型插件存放在assets下的目录
	 * 
	 * @Title: getAssetsDir
	 * @author lytjackson@gmail.com
	 * @date 2014-1-14
	 * @return
	 */
	public abstract String getAssetsDir();

	public static int getIntValue(WidgetType t) {
		int ret = -1;
		switch (t) {
		case LAUNCHER_TYPE:
			ret = 0;
			break;
		case MYPHONE_TYPE:
			ret = 1;
			break;
		case UNKNOWN_TYPE:
			ret = -1;
			break;
		default:
			break;
		}
		return ret;
	}

	public static WidgetType getEnumValue(int v) {
		WidgetType widgetType = UNKNOWN_TYPE;
		switch (v) {
		case 0:
			widgetType = LAUNCHER_TYPE;
			break;
		case 1:
			widgetType = MYPHONE_TYPE;
			break;
		case -1:
			widgetType = UNKNOWN_TYPE;
			break;
		default:
			break;
		}
		return widgetType;
	}
}