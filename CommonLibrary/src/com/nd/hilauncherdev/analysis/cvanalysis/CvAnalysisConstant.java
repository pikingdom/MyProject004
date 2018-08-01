package com.nd.hilauncherdev.analysis.cvanalysis;
/**
 * CV统计常量
 * 编号和内容找姜维或者卢松攀拿，切勿自行添加
 * <p>Title: CvAnalysisConstant</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年11月23日
 */
public class CvAnalysisConstant {
	/***********************************resType常量*******************************************/
	public static final int RESTYPE_ADS = 1;//广告
	public static final int RESTYPE_APP = 2;//应用
	public static final int RESTYPE_THEME = 3;//主题
	public static final int RESTYPE_RING = 4;//铃声
	public static final int RESTYPE_WALLPAPER = 5;//壁纸
	public static final int RESTYPE_FONT = 6;//字体
	public static final int RESTYPE_THEME_SERIES = 7;//桌面主题系列
	public static final int RESTYPE_PASTER = 8;//桌面贴纸
	public static final int RESTYPE_MITO = 9;//美图作品
	public static final int RESTYPE_POTO = 10;//Po图
	public static final int RESTYPE_THEME_MODULE = 11;//主题模块资源
	public static final int RESTYPE_LINKS = 12;//网址链接
	public static final int RESTYPE_CUSTOM_TIPS = 13;//自定义标签
	public static final int RESTYPE_CARDS = 14;//卡片
	public static final int RESTYPE_OTHER = 100;//其他，不能为空
	
	/***********************************CV点位*****************************************/
	/**
	 * 个性化
	 */
	//个性化loading页
	public static final int THEME_SHOP_LOADING = 91010001;
	//主题详情页
	public static final int THEME_SHOP_THEME_DETAIL = 91010002;
	//个性化首页
	public static final int THEME_SHOP_HOMEPAGE = 91020101;
	//个性化-混搭-锁屏-详情页
	public static final int THEME_SHOP_MODULE_LOCKSCREEN_DETAIL = 91030102;
	//个性化-混搭-壁纸
	public static final int THEME_SHOP_MODULE_WALLPAPER = 91030201;
	//个性化-混搭-壁纸-详情页
	public static final int THEME_SHOP_MODULE_WALLPAPER_DETAIL = 91030202;
	//个性化-混搭-图标-详情页
	public static final int THEME_SHOP_MODULE_ICON_DETAIL = 91030302;
	//个性化-混搭-时钟天气-详情页
	public static final int THEME_SHOP_MODULE_WEATHER_DETAIL = 91030402;
	//个性化-混搭-通讯录-详情页
	public static final int THEME_SHOP_MODULE_SMS_DETAIL = 91030502;
	//个性化-混搭-输入法-详情页
	public static final int THEME_SHOP_MODULE_INPUT_DETAIL = 91030602;
	//个性化-混搭-字体-详情页
	public static final int THEME_SHOP_MODULE_FONT_DETAIL = 91030802;
	
	/**
	 * 应用商店热门游戏
	 */
	//应用商店loading页
	public static final int APP_STORE_LOADING = 92010001;
	//热门游戏loading页
	public static final int HOT_GAMES_LOADING = 93010002;
	
	/**
	 * 导航屏
	 */
	//进入0屏（**0屏统一整理点位**）
	public static final int NAVIGATION_SCREEN_INTO = 94010001;
	//0屏-icon-icon1
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON1 = 94010102;
	//0屏-icon-icon2
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON2 = 94010103;
	//0屏-icon-icon3
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON3 = 94010104;
	//0屏-icon-icon4
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON4 = 94010105;
	//0屏-icon-icon5
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON5 = 94010106;
	//0屏-icon-icon6
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON6 = 94010107;
	//0屏-icon-icon7
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON7 = 94010108;
	//0屏-icon-icon8
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_ICON8 = 94010109;
	//0屏-网址导航-热词
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_NAVIGATION_HOTWORD = 94010202;
	//0屏-网址导航-框
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_NAVIGATION_CLICK = 94010203;
	//0屏-实时热点-热词
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_REALTIME_HOTWORDS  = 94010302;
	//0屏-实时热点-框
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_REALTIME_HOTWORDS_CLICK  = 94010303;
	//0屏-今日头条-框上的热词
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_HEADLINE_HOTWORD = 94010402;
	//0屏-今日头条-链接
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_HEADLINE_HOTWORD_CLICK = 94010403;
	//0屏-购物指南-框上的热词
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_SHOP_GUIDE_HOTWORD = 94010502;
	//0屏-购物指南-链接
	public static final int NAVIGATION_SCREEN_CLASSIFICATION_SHOP_GUIDE_HOTWORD_CLICK = 94010503;
	
	/**
	 * 桌面搜索
	 */
	//桌面搜索
	public static final int LAUNCHER_SEARCH= 96010101;
	//桌面-百度插件-搜索-搜索框搜索
	public static final int LAUNCHER_SEARCH_BAIDU_WIDGET_SEARCH_FIELD= 96010103;
	//桌面-百度插件-搜索-热词
	public static final int LAUNCHER_SEARCH_BAIDU_WIDGET_HOTWORD= 96010104;
	//桌面-百度插件-搜索-历史记录
	public static final int LAUNCHER_SEARCH_BAIDU_WIDGET_HISTORY= 96010105;
	//桌面-百度插件-搜索-联想词搜索
	public static final int LAUNCHER_SEARCH_BAIDU_WIDGET_GUESSING= 96010106;
	//桌面搜索(0屏)
	public static final int LAUNCHER_SEARCH_NAVIGATION = 94010001;
	//0屏-搜索
	public static final int LAUNCHER_SEARCH_NAVIGATION_SCREEN_SEARCH= 94012001;
	//0屏-搜索-搜索框搜索
	public static final int LAUNCHER_SEARCH_NAVIGATION_SEARCH_FIELD= 94012002;
	//0屏-搜索-热词
	public static final int LAUNCHER_SEARCH_NAVIGATION_HOTWORD= 94012003;
	//0屏-搜索-历史记录
	public static final int LAUNCHER_SEARCH_NAVIGATION_HISTORY= 94012004;
	//0屏-搜索-联想词搜索
	public static final int LAUNCHER_SEARCH_NAVIGATION_GUESSING= 94012005;
	
	//一键换壁纸V7.2
	public static final int WALLPAPER_WIDGET_DRAWER = 96030001;
	//应用主题结果页面V7.3.1
	public static final int THEME_MENU_DRAWER = 96040001;
	
	//天气插件详情广告页面ID V7.5.2(天气插件中有引用该变量，勿删 caizp)
	public static final int WEATHER_DETAIL_AD = 96090001;
	//天气插件详情悬浮图标广告位置ID(天气插件中有引用该变量，勿删 caizp)
	public static final int WEATHER_DETAIL_AD_POS_ID1 = 96090101;
	//天气插件详情底部banner广告位置ID(天气插件中有引用该变量，勿删 caizp)
	public static final int WEATHER_DETAIL_AD_POS_ID2 = 96090201;
	
	//V8个性化推荐精选页广告banner
	public static final int THEME_SHOP_HOMEPAGE_AD_POS_RECOMMEND = 91020102;
	//V8个性化发现页顶部广告banner
	public static final int THEME_SHOP_HOMEPAGE_AD_POS_DISCOVERY_TOP = 91020202;
	
	//下滑发现
	public static final int TOP_MENU_PAGEID = 96050001;
	//下滑发现广告
	public static final int TOP_MENU_ADS = 96050101;
	//下滑发现新闻
	public static final int TOP_MENU_NEWS = 96050201;
	//下滑发现搜索
	public static final int TOP_MENU_SEARCH = 96050301;
	//下滑发现搜索-搜索框
	public static final int TOP_MENU_SEARCH_SEARCH_FIELD = 96050302;
	//下滑发现搜索-热词
	public static final int TOP_MENU_SEARCH_HOTWORD = 96050303;
	//下滑发现搜索-历史记录
	public static final int TOP_MENU_SEARCH_HISTORY = 96050304;
	//下滑发现搜索-联想词搜素
	public static final int TOP_MENU_SEARCH_GUESSING = 96050305;
	//桌面拉绳广告曝光
	public static final int TOP_MENU_MASCOT_AD_EXPOSURE = 96120001;
	//桌面上滑菜单广告曝光
	public static final int LAUNCHER_MENU_AD_EXPOSURE = 96130001;

	//通知栏推送
	public static final int PUSH_NOTIFICATIONS = 96060001;
	//冒泡推送
	public static final int PUSH_POPUP = 96070001;
	//图标推送
	public static final int PUSH_ICON = 96080001;
	//桌面一键清理
	public static final int CLEANER_ONE_KEY_PAGEID = 96100001;

	//充电屏保
	public static final int CHARGING_ACTIVITY = 96110001;
	
}
