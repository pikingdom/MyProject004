package com.nd.hilauncherdev.launcher;

import android.net.Uri;

import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;

/**
 * Settings related utilities.
 */
public class LauncherSettings {
	
	public static interface BaseLauncherColumns extends BaseLauncherSettings.BaseLauncherColumns {
		/**
		 * 新加一个字段 用来存储系统快捷方式等默认图标   add by zhenghonglin
		 */
		static final String DEFAULT_ICON = "defaultIcon";
	}

	/**
	 * ID值2,3,4,1012,10000,2015,-100,-101已被父类使用，新ID值不能与这些值重复！！！
	 * Favorites.
	 */
	public static final class Favorites extends BaseLauncherSettings.Favorites implements BaseLauncherColumns {
		/**
		 * 情景桌面ID
		 */
		public static final String SCENE_ID = "scene_id";
		
		/**
		 * 绑定桌面与打开的时候再决定如何处理
		 */
		public static final int ITEM_TYPE_ANYTHING = 5;
        
		/**
         * 模拟时钟widget
         */
        public static final int ITEM_TYPE_WIDGET_ANALOG_CLOCK = 1000;

		/**
         * The favorite is a search widget
         */
        public static final int ITEM_TYPE_WIDGET_SEARCH = 1001;

        /**
         * The favorite is a photo frame
         */
        public static final int ITEM_TYPE_WIDGET_PHOTO_FRAME = 1002;
        
        /**
         * 用户反馈(兼容熊猫桌面2.X小插件) edit by caizp 2012-8-28
         */
        public static final int ITEM_TYPE_WIDGET_FEEDBACK = 1011;
        
        /**
         * 我的手机应用程序
         */
        public static final int ITEM_TYPE_HI_APPLICATION = 2012;
        
        /**
         * 应用程序图标
         */
        public static final int ITEM_TYPE_WIDGET_ICONAPP = 2014;
        
        /**
         * 桌面匣子app栏新增的推荐快捷方式
         */
        public static final int ITEM_TYPE_DRAWER_SHORTCUT = 2016;
        
        /**
         * 匣子中的系统快捷方式
         */
        public static final int ITEM_TYPE_SYSTEM_SHORTCUT = 2017;
        
        
        /**
         * 一键清理
         */
        public static final int ITEM_TYPE_WIDGET_CLEANER = 2018;
        
 
        /**
         * 省电4x1
         */
        public static final int ITEM_TYPE_WIDGET_POWER = 2019;
        
        /**
         * 百度搜索widget
         */
        public static final int ITEM_TYPE_WIDGET_BAIDU = 2020;
        /**
         * 省电4x2
         */
        public static final int ITEM_TYPE_WIDGET_POWER_4X2 = 2021;
        
        /**
         * 淘宝widget
         */
        public static final int ITEM_TYPE_WIDGET_TAOBAO = 2022;
        
        /**
         * 在匣子中是widget, 拖放到桌面后, 它是一个1x1的Shortcut 
         * 例如: 联系人1x1 小部件就是此类型, 
         */
        public static final int ITEM_TYPE_WIDGET_SHORTCUT = 2023;
        
        /**
         * 1X1 一键清理  
         */
        public static final int ITEM_TYPE_WIDGET_CLEANER_1X1 = 2024;
        
        /**
         * 1X1 换壁纸
         */
        public static final int ITEM_TYPE_WIDGET_WALLPAPER_1X1 = 2025;
        
        /**
         * 快捷开关widget(兼容熊猫桌面2.X小插件) edit by caizp 2012-8-28
         */
        public static final int ITEM_TYPE_WIDGET_TOGGLE = 1007;
        
        /**
         * 独立应用类型，该类应用类型不能被删除,卸载;<br>
         * 现在仅用于底部托盘中的匣子图标
         */
        public static final int ITEM_TYPE_INDEPENDENCE = 2026;
        
        /**
         * 禽流感疫情widget
         */
        public static final int ITEM_TYPE_WIDGET_H7N9 = 2027;
        
        /**
         * 流量监控widget
         */
        public static final int ITEM_TYPE_WIDGET_FLOW = 2028;
        
        /**
         * 展示主题中推荐应用的文件夹
         */
        public static final int ITEM_TYPE_THEME_APP_FOLDER = 2029;
        
        /**
         * 展示桌面默认推荐应用的文件夹
         */
        public static final int ITEM_TYPE_LAUNCHER_RECOMMEND_FOLDER = 2030;
        
        /**
         * 抢票插件widget
         */
        public static final int ITEM_TYPE_WIDGET_GRAB_TICKET = 2031;
        
        /**
    	 * 百变桌面插件
    	 */
    	public final static int ITEM_TYPE_WIDGET_VARIETY = 2032;

        /**
         * 应用升级文件夹
         */
        public final static int ITEM_TYPE_UPGRADE_FOLDER = 2033;
        
        /**
         * 应用升级文件夹
         */
        public final static int ITEM_TYPE_MYPHONE_FOLDER = 2034;
        
        /**
        * 游戏主题推荐应用
        */
         public final static int ITEM_TYPE_GAME_THEME_RECOMMEND_APP = 2035;
         
         /** 贴纸视图类型  */
        public final static int ITEM_TYPE_TAG_VIEW = 2036;
        
        /**
         * 自定义入口搜索插件 edit by caizp 2012-8-28
         */
        public static final int ITEM_TYPE_WIDGET_CUSTOM_SEARCH = 2037;

        /**
         * 时钟插件
         */
        public static final int ITEM_TYPE_WIDGET_CLOCK = 2038;

        /**
         * 谷歌搜索条
         */
        public static final int ITEM_TYPE_WIDGET_GOOGLE_SEARCH_BAR= 2039;

        /**
         * 熊猫桌面2.x最近安装和实时文件夹URI add by caizp 2012-8-28
         */
        public final static String LIVEFOLDER_AUTHORITY = "com.nd.android.pandahome2.livefolder.CustomLiveFolderProvider";
        public static final Uri LATEST_INSTALL_APP_URI = 
			Uri.parse("content://"+LIVEFOLDER_AUTHORITY+"/latest_install_app");
        public static final Uri OFTEN_USED_APP_URI = 
			Uri.parse("content://"+LIVEFOLDER_AUTHORITY+"/often_used_app");

	}
}
