package com.nd.hilauncherdev.kitset.Analytics;

/**
 * 功能统计事件ID集合
 */
public class AnalyticsConstant {

	public static final int INVALID = -1;	
	/**
	 * 2014.1.13变更统计ID
	 * maolinnan_350804
	 * 格式：14年+01月+两位系列编号+两位编号
	 */
	/**
	 * 00 一键系列
	 */
	//一键清理(1-1x1；2-2x1) V5.6版本去掉
//	public static final int WIDGET_ONE_KEY_CLEAN = 14010001;
	//深度清理  V5.6版本去掉，V5.7又继续统计 6.1.1废弃
//	public static final int WIDGET_ONE_KEY_CLEAN_DEPTH = 14010002;
	//一键换壁纸
	public static final int WIDGET_WALLPAPER_ONE_CLICK_CHANGE = 14010003;
	
	/**
	 * 01 开关系列
	 */
	//一键关屏-（1-下拉菜单；2-插件；3-详情；4-91快捷）
	public static final int SWITCH_ONE_KEY_LOCK_SCREEN = 14010101;
	//手电筒-（1-下拉菜单；2-插件；3-详情；4-91快捷）
	public static final int SWITCH_FLASHLIGHT = 14010102;
	//快捷开关-wifi-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_WIFI = 14010103;
	//快捷开关-亮度（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_BRIGHT = 14010104;
	//快捷开关-飞行模式（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_FLY_MODE = 14010105;
	//快捷开关-响铃模式-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_RING_MODE = 14010106;
	//快捷开关-更多-（1-下拉菜单；2-插件）
	public static final int SWITCH_MORE = 14010107;
	//快捷开关-自动转屏-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_AUTO_TURN = 14010108;
	//快捷开关-蓝牙-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_BLUETOOTH = 14010109;
	//快捷开关-GPS-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_GPS = 14010110;
	//快捷开关-自动锁屏-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_AUTO_LOCK = 14010111;
	//快捷开关-同步账户-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_SYNC = 14010112;
	//快捷开关-USB模式-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_USB = 14010113;
	//快捷开关-触感反应-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_TOUCH_VIBRATE = 14010114;
	//快捷开关-屏幕超时-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_SCREEN_LOCK = 14010115;
	//快捷开关-振动-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_VIBRATE = 14010116;
	//快捷开关-二维码-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_QR_CODE = 14010117;
	//快捷开关-移动网络-（1-下拉菜单；2-插件；3-详情）
	public static final int SWITCH_GPRS = 14010118;
	
	/**
	 * 02 设置系列
	 */
	//下拉菜单-快捷设置-（1-开网络；2-定闹钟；3-设时间；4-应用管理；5-设铃声；6-快点）
	public static final int SET_QUICK_SETTINGS = 14010201;
	//系统设置使用情况-（1-WIFI；2-移动数据；3-网络共享；4-蓝牙；5-屏幕安全；6-亮度；7-铃声；8-语言输入；9-应用；10-存储；11-电池；12-USB调试；13-更多系统设置）
	public static final int SET_SYS_SETTINGS_USE_SITUATION = 14010202;
	//桌面设置
	public static final int SET_LAUNCHER_SETTINGS = 14010203;
	
	/**
	 * 03 菜单系列
	 */
	//下拉菜单（1-桌面；2-匣子）6.5屏蔽
//	public static final int MENU_DROP_DOWN_MENU = 14010301;
	//桌面菜单（1-menu；2-上滑手势）V5.7屏蔽
//	public static final int MENU_LAUNCHER_MENU = 14010302;
	//桌面菜单使用情况（1-14：各选项）
	public static final int MENU_LAUNCHER_MENU_USAGE = 14010303;
	//匣子菜单（1-图标；2-menu键）
	public static final int MENU_DRAWER_MENU = 14010304;
	//长按快捷菜单（1-换图标；2-重命名；3-详情；4-缩放; 5-删除;6-换风格;7-深度清理）
	public static final int MENU_LAUNCHER_SCREEN_ICON_LONGPRESS = 14010305;
	
	/**
	 * 04 动作
	 */
	//捏屏幕-屏幕预览
	public static final int ACTION_LAUNCHER_MENU_NIE_SCREEN_PREVIEW = 14010401;
	//长按图标（1-移动位置；2-交换位置；3-删除；4-卸载；5-跨屏移动）V5.7屏蔽
//	public static final int ACTION_ICON_LONGPRESS = 14010402;
	//长按插件（1-移动位置；2-交换位置；3-删除；4-跨屏移动）V5.7屏蔽
//	public static final int ACTION_WEIGHT_LONGPRESS = 14010403;
	//长按桌面
//	public static final int LAUNCHER_SCREEN_EDIT_MODE = 14010404;
	//删除桌面默认图标
//	public static final int ACTION_REMOVE_DEFAULT_ICON = 14010405;
	
	/**
	 * 05 编辑模式
	 */
//	//编辑模式-小部件（1-系统小部件；2-更多小部件；3-添加小部件）
//	public static final int LAUNCHER_SCREEN_EDIT_WEIGHT = 14010501;
//	//编辑模式-应用（1-10：各选项）
//	public static final int LAUNCHER_SCREEN_EDIT_APP = 14010502;
//	//编辑模式-个性化（1-主题；2-壁纸；3-字体；4-铃声）
//	public static final int LAUNCHER_SCREEN_EDIT_INDIVIDUATION = 14010503;
//	//编辑模式-特效(1-指尖特效；2-滑屏特效)
//	public static final int LAUNCHER_SCREEN_EDIT_EFFECT = 14010504;
//	//编辑模式-个性化-主题-本地主题
//	public static final int LAUNCHER_SCREEN_EDIT_LOCAL_THEME = 14020505;
//	//编辑模式-个性化-主题-在线主题
//	public static final int LAUNCHER_SCREEN_EDIT_ONLINE_THEME = 14020506;
//	//编辑模式-壁纸（jj-壁纸滚动，gd-更多壁纸，xg-壁纸效果，xc-相册，yy-直接应用壁纸）
//	public static final int LAUNCHER_SCREEN_EDIT_INDIVIDUATION_WALLPAPER = 14042507;
	
	/**
	 * 06 DOCK栏
	 */
	//左右滑动V5.7屏蔽
//	public static final int DOCK_BAR_SCROLL_SCREEN = 14010601;
	//图标替换（具体位置）
	public static final int DOCK_BAR_ICON_EXCHANGE = 14010602;
	//默认图标使用情况（1-15：具体位置，除了匣子按钮）
//	public static final int DOCK_BAR_ICON_USAGE = 14010603;
	//自定义图标使用情况（1-15：具体位置）
//	public static final int DOCK_BAR_CUSTOM_ICON_USAGE = 14010604;
	
	/**
	 * 07 搜索 
	 */
	//进入搜索（1-0屏；2-百度插件）
//	public static final int SEARCH_INTO_SEARCH = 14010701;
	//热词刷新
//	public static final int SEARCH_HOT_WORD_REFRESH = 14010702;
	//搜索请求
	public static final int  SEARCH_REQUEST= 14010703;
	//语音搜索（1-0屏；2-详情）
	public static final int  SEARCH_VOICE_SEARCH= 14010704;
	//二维码（1-0屏；2-百度插件）
	public static final int  SEARCH_QR_CODE = 14010705;
	//搜索-点击搜索按钮(db-百度，sm-神马)
	public static final int SEARCH_CLICK_SEARCH_BUTTON = 14060706;
	//搜索-点击联想词
	//public static final int SEARCH_CLICK_RELATIONLIST = 14060707;
	//搜索-点击历史记录(db-百度，sm-神马)
	public static final int SEARCH_CLICK_HISTORY = 14060708;
	//以下两个给了5.7.1版本的统计，计数值要记得越过。
//	//本地搜索(lxr-联系人，dx-短信，app-应用,yy-音乐,tp-图片,sp-视频，sz-桌面设置项，ss-在网络上搜索，dw-应用定位)
//	public static final int NAVIGATION_SEARCH_LOCAL = 14070709;
//	//应用搜索(app-应用,dw-应用定位,xz-应用下载,zxapp-点击在线应用弹出详情)
//	public static final int NAVIGATION_SEARCH_APP = 14070710;
	
	/**
	 * 08 热词 
	 */
	//热词点击（1-0屏；2-百度插件；3-搜索界面）V5.7删除
//	public static final int HOT_WORD_CLICK = 14010801;
	//热词点击0屏(wf-wifi;3g-移动网络;no-无网络)
//	public static final int HOT_WORD_CLICK_ZERO_SCREEN = 14060802;
	//热词点击百度插件(wf-wifi;3g-移动网络;no-无网络)
	public static final int HOT_WORD_CLICK_BAIDU_WIDGET = 14060803;
	//热词点击搜索界面(wf-wifi;3g-移动网络;no-无网络)
	public static final int HOT_WORD_CLICK_SEARCH = 14060804;
	
	
	/**
	 * 09 0屏
	 */
	//导航图标（1-N：各位置）原有：label: 0-淘宝,1-爱奇艺,2-去哪儿,30-新浪,31-美团,32-安卓网
	//public static final int EVENT_FIRST_SCREEN_ICON_CLICK = 14010901;
	//导航链接（1-N：各位置）1~4 5为用户添加 6为自动生成
	//public static final int EVENT_FIRST_SCREEN_LINK_CLICK = 14010902;
	//设置
	//public static final int ZERO_SCREEN_SETTING = 14010903;
	
	/**
	 * 10 匣子 
	 */
	//匣子选项点击情况（1-应用程序；2-小部件；3-我的手机）//2014-4-25号屏蔽该点
//	public static final int DRAWER_CHOSE_USAGE = 14011001;		
	//匣子滑屏情况（1-进入应用程序；2-进入小部件；3-进入我的手机）//2014-4-17号屏蔽该点。
//	public static final int DRAWER_EFFECT_USAGE = 14011002;
	//匣子搜索 6.1.1废弃
//	public static final int DRAWER_SEARCH = 14011003;
	//匣子搜索热词（1-点击；2-刷新）
	public static final int DRAWER_SEARCH_HOT_WORD = 14011004;
	//匣子搜索请求（1-本地；2-应用中心）
	public static final int DRAWER_SEARCH_REQUEST = 14011005;
	//匣子搜索结果（1-定位；2-打开）
	public static final int DRAWER_SEARCH_RESULT = 14011006;
	//匣子搜索语音
	public static final int DRAWER_SEARCH_VOICE = 14011007;
	//匣子菜单
//	public static final int DRAWER_MENU = 14011008;
	//匣子菜单使用情况（1-图标排序；2-新建文件夹；3-隐藏程序；4-应用列表设置）
	public static final int DRAWER_MENU_USAGE = 14011009;
	
	/**
	 * 11 我的手机
	 */
	//我的手机各功能使用情况（1-N：我的手机各模块） 6.1.1废弃
//	public static final int MYPHONE_FUNCTION_USAGE = 14011101;
	
	/**
	 * 12小部件和快捷等桌面图标
	 */
	//从匣子中添加小部件到桌面（标签区别具体小部件）6.1.1废弃
//	public static final int WIDGET_ADD_TO_LAUNCHER_FROM_DRAWER = 14011201;
	//从编辑模式中添加小部件到桌面（标签区别具体小部件）
//	public static final int WIDGET_ADD_TO_LAUNCHER_FROM_LAUNCHER_EDTOR = 14011202;
	//小部件 91快捷 每日新鲜事点击【增加主题商场入口进入标记[ztjr]】
	public static final int LAUNCHER_ND_SHORTCUT_DAILY_FRESH_NEWS = 14011203;
	//小部件 91快捷 每日新鲜事 条目点击
	//public static final int LAUNCHER_ND_SHORTCUT_DAILY_FRESH_NEWS_ITEM = 14011204;
	//小部件 91快捷 具体点击项	6.1.1废弃
//	public static final int LAUNCHER_ND_SHORTCUT_DEATAIL = 14011205;
	//小部件 桌面搬家（1-桌面图标；2-桌面设置）
	public static final int LAUNCHER_MOVE_HOUSE = 14011206;
	//整理 应用分类 最近安装	
	public static final int FOLDER_RECENT_INSTALL = 14011207;
	//整理 应用分类 最近打开
	public static final int FOLDER_RECENT_OPEN = 14011208;
    //整理 应用分类 应用商店
	public static final int APP_STORE_ENTRY = 14021209;
	//执行桌面搬家成功统计
	public static final int EVENT_MOVE_DESK_ITEM_MOVE_SUCCESS = 14021210;
	/**
	 * 13省电widget
	 */
	//省电按钮 6.1.1废弃
//	public static final int BATTERY_BUTTON = 14011301;
	//进入省电 6.1.1废弃
//	public static final int BATTERY_INTO_BATTERY = 14011302;
	
	
	/**
	 * 14桌面主屏推荐应用
	 * 2014.7.8日作废5.7以前的推荐。
	 */
//	//桌面推荐应用 - 91手机助手
//	public static final int RECOMMEND_APP_ND_ASSISTANCE = 14011401;
//	//桌面推荐应用 - 机锋市场
//	public static final int RECOMMEND_APP_GFAN = 14011402;
//	//桌面推荐应用 - 安卓市场
//	public static final int RECOMMEND_APP_HIAPK = 14011403;
//	//桌面推荐应用 - QQ浏览器
//	public static final int RECOMMEND_APP_QQMTT = 14011404;
//	//桌面推荐应用 - 今日新闻
//	public static final int RECOMMEND_APP_TODAY = 14011405;
//	//桌面推荐应用 - 凤凰新闻
//	public static final int RECOMMEND_APP_FHXW = 14011406;
//	//桌面推荐应用 - 百度地图
//	public static final int RECOMMEND_APP_BDDT = 14011407;
//	//桌面推荐应用 - 豌豆荚
//	public static final int RECOMMEND_APP_WDJ = 14011408;
//	//桌面推荐应用 - 土豆视频
//	public static final int RECOMMEND_APP_TDSP = 14011409;
//	//桌面推荐应用 - 百度手机助手
//	public static final int RECOMMEND_APP_BDSJZS = 14011410;
//	//桌面推荐应用 - 安卓优化大师
//	public static final int RECOMMEND_APP_AZYHDS = 14011411;
//	//桌面推荐应用 - 百度音乐
//	public static final int RECOMMEND_APP_BDYY = 14011412;
//	//桌面推荐应用 - 高德地图
//	public static final int RECOMMEND_APP_GDDT = 14011413;
//	//桌面推荐应用 - PPS
//	public static final int RECOMMEND_APP_PPS = 14011414;
//	//桌面推荐应用 - 爱奇艺
//	public static final int RECOMMEND_APP_AQY = 14011415;
//	//桌面推荐应用 - 大众点评
//	public static final int RECOMMEND_APP_DZDP = 14011416;
//	//桌面推荐应用 - 金山词霸
//	public static final int RECOMMEND_APP_JSCB = 14011417;
//	//桌面推荐应用 - 我查查
//	public static final int RECOMMEND_APP_WCC = 14011418;
//	//桌面推荐应用 - 91熊猫看书
//	public static final int RECOMMEND_APP_XMKS = 14011419;
//	//桌面推荐应用 - 墨迹天气
//	public static final int RECOMMEND_APP_MJTQ = 14031420;
//	//桌面推荐应用 - 书旗小说
//	public static final int RECOMMEND_APP_SQXS = 14031421;
//	//桌面推荐应用 - 百度浏览器
//	public static final int RECOMMEND_APP_BDLLQ = 14031422;

	//点开推荐文件夹 6.1.1废弃
//	public static final int RECOMMEND_FOLDER_ISOPEN = 14071423;
	//完全下载完推荐（zm-桌面推荐下载完全，tj-主题推荐下载完全）
	public static final int RECOMMEND_FOLDER_DOWNLOAD_ALL_LAUNCHER = 14071424;
	//下载桌面推荐应用（label为应用名）
	public static final int RECOMMEND_FOLDER_DOWNLOAD_LAUNCHER = 14071425;
	//下载主题推荐应用（label为应用名）
	public static final int RECOMMEND_FOLDER_DOWNLOAD_THEME = 14071426;
	//刷新推荐文件夹内容
//	public static final int RECOMMEND_FOLDER_FRESH_CONTENT = 14071427;
	//发起推荐请求(zm-桌面请求，tj-推荐请求)
	public static final int RECOMMEND_FOLDER_REQUEST_THEME_RECOMMEND = 14071428;

	
	/**
	 * 15其他
	 */
	//桌面异常次数	
	public static final int LAUNCHER_EXCEPTION = 14011501;
	//进入情境桌面次数
	public static final int SCENE_LOAD_EVENT = 14011502;
	//匣子多选操作(这个ID谁写的呀。。。已经用不能改了呀。。= =！继续用这个ID吧。)
	public static final int DRAWER_MULTI_SELECT = 14041501;
	//进入情境桌面次数英文环境
	public static final int SCENE_LOAD_EVENT_EN = 14041504;
	//安卓智能升级（xz-开始下载安卓市场，az-成功安装安卓市场，sj-跳转至安卓市场详情界面升级）
	//public static final int SMART_UPDATE_HIMARKET = 14051505;
	//桌面自升级发送通知栏提醒
	public static final int LAUNCHER_UPDATE_NOTIFI = 14051506;
	//桌面自升级窗口（az-安卓市场智能升级，zs-91助手智能升级，pt-普通升级，zd-我知道了,bd-百度手助智能升级）
	public static final int LAUNCHER_UPDATE_DIALOG = 14051507;
	//DOCK栏推荐APP下载(标签为应用包名末尾串)
	public static final int DOCK_RECOMMEND_APP = 14051508;
	//通知中心推送
	public static final int NOTIFICATION_MESSAGE_PUSH = 14051509;
	//通知中心点击量
	public static final int NOTIFICATION_MESSAGE_PUSH_CLICK = 14051510;
	//通知中心点击量成功打开页面(V5.6开始在每日新鲜事动态加载中用到)下面的id要跳过11
//	public static final int NOTIFICATION_MESSAGE_PUSH_CLICK_SUCCESS = 14051511;
	//桌面自升级点击通知栏提醒
	public static final int LAUNCHER_UPDATE_NOTIFI_CLICK = 14051512;
	//DOCK栏推荐APP成功安装(标签为应用包名末尾串)
	public static final int DOCK_RECOMMEND_APP_INSTALLED = 14051513;
	//引导热词和连接打开百度浏览器次数
	//public static final int CLICK_LINKED_TO_BAIDU_BROWSER = 14051514;
	//DOCK栏有推荐APP时打开次数(dh-电话，dx-短信，txl-通讯录，llq-浏览器)
//	public static final int DOCK_RECOMMEND_APP_DIALOG = 14061515;
	//百度mo+服务开关
	public static final int BAIDU_MOPLUS_SERVICE_CLOSE = 14061516;
	//wifi自动下载-开始下载（标签-包名，其他）从6.0.1后版本不用 不准确
    //@Deprecated
	//public static final int WIFI_AUTO_DOWNLOAD_APK_START = 14061517;
	//wifi自动下载-下载完成（标签-包名，其他）从6.0.1后版本不用 不准确
   // @Deprecated
	//public static final int WIFI_AUTO_DOWNLOAD_APK_END = 14061518;
	//异常关闭桌面宠物
	public static final int CLOSE_PETFLOAT = 14061519;
    //wifi自动下载-开始下载（标签-包名，其他）
    public static final int WIFI_AUTO_DOWNLOAD_APK_START2 = 14061520;
    //wifi自动下载-下载完成（标签-包名，其他）
    public static final int WIFI_AUTO_DOWNLOAD_APK_END2 = 14061521;
	
	/**
	 * 16小部件统计
	 */
	//匣子小部件开始下载小部件(小部件详情页中点击开始下载，且小部件来自于匣子小部件时记录)	
	public static final int WIDGET_DRAWER_DOWNLOAD = 14021601;
	//在线商城开始下载某个小部件(小部件详情页中点击开始下载，且小部件来自于在线商城时记录)	
	public static final int WIDGET_SHOP_DOWNLOAD = 14021602;
	//在线商城小部件升级开始下载(小部件详情页中点击开始下载，且小部件来自于在线商城的升级时)
	public static final int WIDGET_SHOP_UPDATE_DOWNLOAD = 14021603;
	//匣子小部件成功安装小部件 (小部件详情页中安装成功，且小部件来自于匣子小部件时记录)	
	public static final int WIDGET_DRAWER_INSTALL = 14021604;
	//在线商城成功安装某个小部件(小部件详情页中成功安装，且小部件来自于在线商城时记录)	
	public static final int WIDGET_SHOP_INSTALL = 14021605;
	//升级已安装小部件 (成功升级某个已安装小部件时记录)
	public static final int WIDGET_SHOP_UPDATE_INSTALL = 14021606;
	//统计91桌面某个小部件带入的对应的客户端的成功安装量
	public static final int WIDGET_APP_INSTALL_FROM_WIDGET = 14021607;
	
	/**
	 * 17主题
	 */
	//我的主题(wf-wifi;3g-移动网络;no-无网络)
//	public static final int MYPHONE_THEME_SHOP = 14031701;
	//下载主题(mf-免费；ff-付费; ch-彩虹;gm-点击购买;sy-试用主题)
	public static final int MYPHONE_THEME_SHOP_DOWNLOAD = 14031702;
	//应用主题(bj-编辑模式；bd-本地主题；sc-主题商城)
	public static final int MYPHONE_THEME_SHOP_USER = 14031703;
	//主题专辑-下载统计(label代表专辑名称)
//	public static final int MYPHONE_THEME_SHOP_FROM_SPECIAL_SUBJECT = 14071704;
	//个人中心点击项（cx-积金查询，sc-积金商城）V5.7.2版本使用。其他版本无用。
//	public static final int MYPHONE_THEME_PERSONAL_CENTER = 14031705;
	/**
	 * 18 新用户引导
	 */
	//弹出新用户引导界面
//	public static final int OPEN_NEW_USER_GUIDE = 14061801;
	//引导界面点击添加世界杯插件（5.7.1去掉）
//	public static final int OPEN_NEW_USER_GUIDE_ADD_WORLDCUP_WIDGET = 14061802;
	//引导界面点击马上体验
//	public static final int OPEN_NEW_USER_GUIDE_IMMEDIATELY_EXPERIENCE = 14061803;
	//点击开始下载天天动听  5.7.1添加占位置
//	public static final int OPEN_NEW_USER_GUIDE_DOWNLOAD_TTPOD = 14061804;
	
	/**
	 * 19深度清理系列
	 */
	//卫士-下载次数
	//public static final int CLEAN_DEPTH_BAIDU_MOBILESAFE_DOWNLOAD = 14061901;
	//卫士-安装次数
	//public static final int CLEAN_DEPTH_BAIDU_MOBILESAFE_INSTALL = 14061902;
	//卫士-进入手机加速
	//public static final int CLEAN_DEPTH_BAIDU_MOBILESAFE_ENTER_EXPEDITE = 14061903;
	//卫士-推荐安装成功
	public static final int CLEAN_DEPTH_BAIDU_MOBILESAFE_INSTALLED = 14061904;
	
	/**
	 * 兼容原来保留的
	 */
//	// 1000008	超过半天再次进入桌面	
//	public static final int LAUNCHER_HALFDAY = 1000008;
//	// 1000009	超过一天再次进入桌面	
//	public static final int LAUNCHER_ONEDAY = 1000009;
//	// 1000010	超过三天再次进入桌面	
//	public static final int LAUNCHER_THREEDAY = 1000010;
	// 1000020	关闭第0屏
	//public static final int CLOSE_ZERO_SCREEN = 1231520;
	// 1000021	在应用其它主题	
	public static final int APPLY_OTHER_THEME = 1231521;
	// 1000022	在使用其它滑屏特效	
	//public static final int APPLY_OTHER_SCREEN_EFFECT = 1231522;
	// 1000023	设置为默认桌面	
	public static final int SET_AS_DEFAULT_LAUNCHER = 1231523;
	// 1000024	使用多个桌面	2014年5月22日废弃
//	public static final int INSTALL_MULTI_LAUNCHER = 1231524;
	// 1000025	使用流量监控	
	//public static final int OPEN_NET_TRAFFIC_MONITOR = 1231525;
	// 1000026	使用隐私空间
	//public static final int OPEN_PRIVACY_SPACE = 1231526;
	// 1000027	没下载过主题	
	//public static final int NEVER_DOWNLOAD_THEME = 1231527;
	// 1000028	没下载过壁纸	
	//public static final int NEVER_DOWNLOAD_WALLPAPER = 1231528;
	// 1000029	没下载过铃声
	//public static final int NEVER_DOWNLOAD_RING = 1231529;
	// 1000030	没下载过字体	
	//public static final int NEVER_DOWNLOAD_FONT = 1231530;
	// 1000031	没使用黄历天气小部件
	//public static final int NOT_USE_WEATHER_WIDGET = 1231531;
	// 1000032	没使用百度小部件
	//public static final int NOT_USE_BAIDU_WIDGET = 1231532;
	// 1000033	没使用省电小部件
	//public static final int NOT_USE_POWER_WIDGET = 1231533;
	// 1000034	没使用开关小部件
	//public static final int NOT_USE_SWITCH_WIDGET = 1231534;
	// 1231535	桌面V5.2.1版本的ABTest两类用户总量
	public static final int ABTEST_COUNT_VSERSION_5210 = 1231535;
	// 1231536  安装360手机卫士
	//public static final int INSTALL_360_SAFE = 1231536;
	// 1231537  桌面上的图标数
	//public static final int APP_ON_WORKSPACE_COUNT = 1231537;
	// 1231538   微信在第几屏
	//public static final int WECHAT_ON_WORKSPACE_SCREEN = 1231538;
	// 1231539 qq在第几屏
	//public static final int QQ_ON_WORKSPACE_SCREEN = 1231539;
	// 1231540 推荐文件夹
	//public static final int NOT_USE_RECOMMEND_FOLDER = 1231540;
	// 1231541 默认屏的图标数
	//public static final int APP_ON_DEFAULT_SCREEN_COUNT = 1231541;
	// 1231542 没使用一键清理
	//public static final int NOT_USE_CLEANER_WIDGET = 1231542;
	// 1231543  没使用一键换壁纸
	//public static final int NOT_USE_WALLPAPER_WIDGET = 1231543;
	// 1231544  使用触屏特效
	//public static final int USE_TOUCH_EFFECT = 1231544;
	//1231545 是否安装快点(V5.3.1版本)
	//public static final int INSTALL_SHORTCUTSLOT_531 = 1231545;
	//1231546 统计用户下载主题套数(V5.3.5版本,V6.8重新恢复打点)
	public static final int THEME_DOWNLOAD_COUNT = 1231546;
	//1231547 统计用户安装过的桌面(360-360桌面，go-go桌面,bd-百度桌面,dx-点心桌面,mx-魔秀桌面,qu-Q立方,mi-MIUI)
	public static final int INSTALLED_OTHER_LAUNCHER = 1231547;
	//1231548统计用户是否使用世界杯小部件
	//public static final int USE_WORLDCUP_WIDGET = 1231548;
	//1231549统计用户是否安装了百度手机卫士
	//public static final int USE_BAIDU_MOBILESAFE = 1231549;
	//1231550统计用户是否使用天天动听小部件 5.7.1添加，占位置
	//public static final int USE_BAIDU_TTOPD = 1231550;
	//1231551统计用户是否使用魔力球
	public static final int USE_MOLIQIU = 1231551;
	//1231552统计是否开启mini通知栏
	public static final int USE_ADVANCED_NOTIFICATION= 1231552;
	//1231553统计文件夹打开风格  0 ios风格, 1 android风格,  2  全屏
	//public static final int USE_FOLDER_STYLE= 1231553;
	//监测0屏卡片的使用情况
	//public static final int  NAVIGATION_SCREEN_CARD_SERVICE_CONDITION = 1231554;
	
	
	/*******************************************************************************/
	/**************************			V6打点新规则		****************************/
	/*******************	          桌面版本号4位+系列号2位+内部序号2位                    *********************/
	/*******************************************************************************/
	/**
	 * 00每日统计系列
	 */
	//app市场安装统计(91-91助手,hi-安卓市场,bd-百度手机助手,360-360手机助手,yyb-应用宝,wdj-豌豆荚,anzhi-安智市场)
	public static final int INSTALLED_MARKET = 61000001;
	//桌面安装时是否进行自动搬家(yes-有, no-无)
	//public static final int MOVE_HOME_ON_INSTALLED = 61000002;
	//手电筒增强插件是否安装(yes-有, no-无)
	public static final int INSTALLED_FLASH_LIGHT = 61000003;
	//匣子里是否存在爱淘宝图标
	public static final int HAS_AITAOBAO_ICON = 63000001;
	//匣子里是否存在升级文件夹
	public static final int HAS_DRAWER_UPDATE_FOLDER = 63010001;
	//是否安装91桌面(百度桌面使用)
	public static final int HAS_INSTALL_91LAUNCHER = 63300001;
	//桌面是否存在应用商店
	public static final int HAS_APPSHOP_ICON = 65000001;
	//桌面是否存在热门游戏
	public static final int HAS_GAME_CENTER_ICON = 65000002;
	//是否使用天天系列主题
	public static final int USE_THEME_SERIES = 65000003;
	//桌面用户使用第三方锁屏情况(91-91智能锁;v-微锁屏;mi-小米百变锁屏;wz-纹字锁屏;hui-惠锁屏;bz-安卓壁纸;zm-最美锁屏)
//	public static final int USE_OTHER_LOCK_SCREEN = 67000001;
	//桌面是否存在淘宝插件
	public static final int HAS_TAOBAO_WIDGET = 68000001;
	//桌面是否存在默认浏览器图标
	public static final int HAS_LAUNCHER_DEFAULT_BROWSER_ICON = 70000001;
	//桌面是否存在爱淘宝图标
	public static final int HAS_LAUNCHER_AITAOBAO_ICON = 70000002;
	//桌面是否存在百度一下图标
	public static final int HAS_LAUNCHER_BAIDU_ICON = 70300001;
	//桌面是否存在唯品会图标
	public static final int HAS_LAUNCHER_WEIPINHUI_ICON = 75100003;
	//桌面是否存在京东图标
	public static final int HAS_LAUNCHER_JINGDONG_ICON = 75180004;
	//桌面是否安装指定app(标签为指定应用的名称)
	public static final int TRACK_APP_INSTALL = 75200001;
	//桌面无匣子模式每日使用人数标签为机型的名称，xm-小米 hw-华为 mz-魅族 vivo-Vivo oppo—Oppo sam—三星 letv-乐视 lenovo-联想 other-其它)
	public static final int USE_NO_DRAWER = 76000001;
	//桌面零屏资讯类型(sh-搜狐，wy-网易)
	public static final int LAUNCHER_INFORMATION_TYPE =  76000002;
	//桌面是否存在天气入口图标
	public static final int HAS_LAUNCHER_WEATHER_ICON = 81980005;
	//每日统计一键返回桌面开关打开
	public static final int ONE_KEY_RETURN_LAUNCHER_SWITCH_OPEN = 83000006;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-力天定制版
	public static final int LAUNCHER_INFORMATION_TYPE_LITIAN =  76000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-天湃定制版
	public static final int LAUNCHER_INFORMATION_TYPE_TIANPAI =  80000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-帆悦定制版
	public static final int LAUNCHER_INFORMATION_TYPE_FANYUE =  81000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-刷机精灵定制版
	public static final int LAUNCHER_INFORMATION_TYPE_SHUAJIJINGLING =  82000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-椒盐定制版
	public static final int LAUNCHER_INFORMATION_TYPE_JIAOYAN =  83000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-新时空定制版
	public static final int LAUNCHER_INFORMATION_TYPE_XINSHIKONG =  84000002;
	//桌面零屏资讯类型(0p-零屏)-力天印尼定制版
	public static final int LAUNCHER_INFORMATION_TYPE_LITIAN_YINNI =  85000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-鼎开定制版
	public static final int LAUNCHER_INFORMATION_TYPE_DINGKAI =  86000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-天湃-智桌面定制版
	public static final int LAUNCHER_INFORMATION_TYPE_TIANPAI_SMARTHOME = 87000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-神龙定制版
	public static final int LAUNCHER_INFORMATION_TYPE_SHENLONG =  88000002;
	//桌面零屏资讯类型(sh-搜狐，wy-网易 0p-零屏)-铭来定制版
	public static final int LAUNCHER_INFORMATION_TYPE_MINGLAI =  89000002;

	/**
	 * 01菜单系列
	 */
	//桌面上滑菜单（yy-添加应用，xbj-添加小部件，xtsz-系统设置，gxh-个性化,grzx-个人中心,zmsz-桌面设置,pmyl-屏幕预览，bzsc-帮助手册） 6.1.1废弃
//	public static final int MENU_LAUNCHER_UPGLIDE_MENU = 60000101;
	//桌面长按菜单（zt-换主题，yy-添加应用，bz-换壁纸，xbj-添加小部件，tx-换特效） 6.1.1废弃
//	public static final int MENU_LAUNCHER_LONG_PRESS_MENU = 60000102;
	//上滑菜单打开方式（an-菜单按钮,sh-上滑打开,ca-长按打开）
	public static final int MENU_LAUNCHER_MENU = 60000103;
	//下滑菜单 6.1.1废弃
//	public static final int MENU_DOWN_GLIDE_MENU = 60000104;
	
	/**
	 * 02个性化
	 */
	//进入个性化(wf-wifi;3g-移动网络;no-无网络)
	public static final int INDIVIDUATION_INTO_INDIVIDUATION = 60000201;
	//混搭点击具体项(sp-锁屏，bz-壁纸,tb-图标，tq-时钟天气,txl-通讯录，srf-输入法，ls-铃声,zt-字体) 6.1.1去除 7.5.1重新统计
	public static final int INDIVIDUATION_MODULE = 60000202;
	//点击搜索
	public static final int INDIVIDUATION_SEARCH = 60000203;
	//点击我的
	public static final int INDIVIDUATION_PERSONAL_CENTER = 60000204;
	// 进入主题购买，记录登录状态(t-有登录, f-没登录)
	public static final int MYPHONE_THEME_SHOP_BUY_LOGIN_STATUS = 61000205;
	// 下载付费主题/字体（ 1-点击购买主题, 2-购买主题成功, 3-点击购买字体, 4-购买字体成功)
	public static final int MYPHONE_THEME_SHOP_BUY = 61000206;
	// 充值（ 1-成功, 2-失败)
	public static final int MYPHONE_THEME_SHOP_RECHARGE = 63000207;
	// 登录失败
	public static final int MYPHONE_LOGIN_FAIL = 71000208;
	
	/**
	 * 03其他点
	 */
	//桌面推荐91桌面HD
//	public static final int RECOMMEND_91LAUNCHER_HD = 60000301;
	//APK下载成功，弹出安装界面
	public static final int DOWNLOAD_APK_SUCCEED = 61000302;
	//inapp搜索，打开视频页次数
//	public static final int SEARCH_INAPP_VIDEO_SHOW = 61000303;
	//魔力球界面热门游戏入口
	//public static final int DYNAMIC_PETFLOAT_INTO_HOTGAME = 61000304;
    //隐藏应用使用人数（匣子设置中进入+手势打开）
    public static final int APP_HIDE_ENTER = 61100305;
    //使用密码人数（使用设置密保人数+成功找回密码人数）
    public static final int APP_HIDE_ENCRIPT = 61100306;
    //小米手机弹出默认桌面引导框
    public static final int XIAOMI_DEFAULT_LAUNCHER_POPWINDOW = 61100307;
    //打开多个浏览器选择界面(from_dock-DOCK栏打开浏览器, from_widget-搜索widget打开浏览器，from_nav-导航屏打开浏览器，from_other-其他位置打开浏览器)
    public static final int CUSTOM_MULTI_BROWSER_ENTER = 61100308;
    //打开多个浏览器选择界面后，用户点击返回键退出(from_dock-DOCK栏打开浏览器, from_widget-搜索widget打开浏览器，from_nav-导航屏打开浏览器，from_other-其他位置打开浏览器)
    //该打点在V7.0.2正式版本去除
    public static final int CUSTOM_MULTI_BROWSER_RETURN = 61100309;
    //在静态广播中上报活跃
   // public static final int UPLOAD_ACTIVITY_USER_ONRECEIVE=61100310;
    
    //三种机型关屏跳转打点
    public static final int JUMP_91_ON3PHONE=61100311;
    //编辑菜单界面设置默认桌面展示次数
    public static final int EDIT_MENU_SETTING_DEFAULT_LAUNCHER_SHOW=62000312;
    //编辑菜单界面设置默认桌面（ok-设置默认桌面；no-取消）
    public static final int EDIT_MENU_SETTING_DEFAULT_LAUNCHER=62000313;
    //调用桌面通用协议访问桌面资源/功能统计
    public static final int CALL_91URI_STATUS=62000314;
    
    //酷我铃声（xz-下载铃声;sz-设置铃声）
    public static final int PLUGIN_COOL_RINGTONES=63100315;
    //统计动态插件版本
    public static final int DYN_PLUGIN_VERSION=65000316;
	//统计动态插件启动出错(个性化-themeshop)
	public static final int START_PLUGIN_ERROR=65000317;
    
    //百度手助有奖栏目活动参与情况-用户进入活动页面（1-欢迎页面进入；2-个人中心进入; 3-个性化Loading）
    public static final int  AWARD_COLUMN_ACTIVITY_INTO_ACTIVITY = 66000317;
    //百度手助有奖栏目活动参与情况-活动页面按钮点击情况（1-去登入；2-登入成功;3-去下载主题;）
    //public static final int  AWARD_COLUMN_ACTIVITY_BUTTON_CLICK = 66000318;
    //统计壁纸被更换时是否正在使用默认主题(y-使用默认主题，n-使用其他主题)
    public static final int USE_THEME_WHEN_WALLPAPER_CHANGE = 68000301;
    //统计是否切换至默认主题(y-使用默认主题(从v7版本开始该位置标签换为v7)，n-使用其他主题，c-当前主题未变化，v6-使用V6主题，p-使用原生主题)
    public static final int IS_USE_DEFAULT_WHEN_THEME_CHANGE = 68000302;
    //统计多个相机选择界面(dk-打开选择界面，xz-下载)
    public static final int CUSTOM_MULTI_CAMERA_ENTER = 68000318;
    //统计应用管家-进入谁是卧底
    public static final int APP_MANAGER_INTO_WHO_IS_UNDERCOVER = 68980319;
    //桌面贴纸-进入在线贴纸专区
    public static final int LAUNCHER_TAG_ENTER_TAG_SHOP = 68980320;
    //桌面贴纸-添加贴纸到桌面
    public static final int LAUNCHER_TAG_ADD_TAG = 68980321;
    //桌面贴纸-点击桌面上的贴纸
    public static final int LAUNCHER_TAG_CLICK_TAG = 68980322;
    //进入情景桌面主页列表
    public static final int ENTER_SCENE_SHOP_MAIN_ACTIVITY = 70000323;
    //访问网页时QQX5的调起逻辑（qq-QQ浏览器;qb-简版QB;yx-优选框）
    public static final int VISIT_WEB_QQX5_CALLLOGIC = 71000324;
    //DOCK浏览器选择框打开浏览器应用(标签为打开应用的名称)
  	public static final int DOCK_BROWSER_SELECT_APP = 71000325;
    //百度push dex分包异常(label-安卓版本号)
  	public static final int DEX_SUBPACKAGE_EXCEPTION_FOR_BAIDU_PUSH = 71000327;
  	//简版QQ浏览器 dex分包异常(label-安卓版本号)
  	public static final int DEX_SUBPACKAGE_EXCEPTION_FOR_SIMPLE_QQBROWSER = 71000328;
  	//加载框架 dex分包异常(label-安卓版本号)
  	public static final int DEX_SUBPACKAGE_EXCEPTION_FOR_LOAD_FRAME= 71000329;
  	//手机管家-病毒查杀(dj-点击病毒查杀;jr-进入病毒查杀;sy-使用腾讯手机管家)
  	public static final int MOBILE_BUTLER_VIRUS_SCANNER = 71000330;
  	//手机管家-一键清理(dj-点击一键清理;jr-进入一键清理;sy-使用腾讯手机管家)
  	public static final int MOBILE_BUTLER_AKEY_CLEAN = 71000331;
  	//桌面贴纸-点击桌面上的主题贴纸
    public static final int LAUNCHER_TAG_CLICK_TAG_FROM_THEME = 72000332;
    //统计91锁屏需唤醒量
    public static final int STAT_91_LOCKED_AWAKEN = 75200333;
    // 广告banner图高度超过手机半屏(标签为手机分辨率)
    public static final int AD_BANNER_HEIGHT_EXCEED_HALF_SCREEN = 75300334;
    // 弹出应用主题结果页时统计91锁屏需唤醒量(V7.6.1版本添加)
    public static final int STAT_91_LOCKED_AWAKEN_V761 = 76100335;
    // 流量监控使用情况(校准流量-jzll，日流量排行-mr，流量排行-llph，网络防火墙-wlfhq，个人中心-grzx)，流量插件中使用
    public static final int TRAFFIC_MONITOR_USE = 76200336;
    // 流量监控变现效果(点击流量产品第1个图标-cp1，点击流量产品第2个图标-cp2，点击流量产品第3个图标-cp3，进入流量充值-llcz，桌面91快捷进入流量充值-zmllcz)，流量插件中使用
    public static final int TRAFFIC_MONITOR_CASH = 76200337;
	//新旧匣子切换设置（x-切换到新匣子；j-切换到旧匣子）
    public static final int NEW_DRAWER_SETTING = 76300338;
	//桌面悬浮按钮使用情况(dj-单击次数;ql-一键清理次数;gp-一键关屏次数)
	public static final int LAUNCHER_FLOAT_BTN_USER_INFO = 82000339;
	//推荐视频App- (dlapp-下载app ; dlvdo-下载视频)
	public static final int RECOMMEND_VIDEO_DETAIL = 84980341;
	//视频壁纸功能打点
	public static final int RECOMMEND_VIDEO_CLICK = 84980342;

	/**
	 * 04桌面操作
	 */
	//用户添加应用到桌面的方式(0 按menu>添加应用  1长按>添加应用  2进入匣子>添加应用)
	public static final int ADD_APP_PATH = 60100401;
	//用户添加小部件到桌面的方式(0 按menu>添加小部件  1长按>添加小部件  2进入匣子>添加小部件)
	public static final int ADD_WIDGET_PATH = 60100402;
	//用户点击桌面推送图标
	public static final int CLICK_PUSH_ICON = 61100401;
	//打开文件夹(z-桌面打开,x-匣子打开,y-有获取到推荐app信息,n-没有获取到推荐app信息)
	public static final int LAUNCHER_OPEN_FOLDER = 71000404;
	//文件夹操作（t-添加；j-加密）
	public static final int LAUNCHER_FOLDER_OPERATE = 71000405;
	//无匣子风格-应用列表使用情况(打开应用列表-dk, 发送到桌面-fs, 应用详情-xq, 定位-dw, 隐藏应用程序-yc, 搜索-ss)
	public static final int OPEN_APP_LIST_FOR_NO_DRAWER = 76000406;
	//无匣子风格-最HOT应用使用情况（dj-推荐应用点击；zk-展开最hot应用;sx-刷新最hot应用;xz-下载推荐应用）
	public static final int FOLDER_V8_HOT_APPS = 77000407;
	//无匣子风格-文件夹菜单使用情况（mc-按名称排序；sy-按使用情况排序;cmm-文件夹重命名）
	public static final int FOLDER_V8_MENU = 77000408;
	//无匣子风格-文件夹底部的点击情况（hot-点击最HOT应用；tj-点击推荐文件夹刷新;sj-点击升级文件夹升级）
	public static final int FOLDER_V8_BOTTOM_CLICK = 77000409;
	//无匣子风格-文件夹更多应用点击（yy-进入应用商店；yx-进入游戏中心）
	public static final int FOLDER_V8_MORE_APPS = 77000410;
	//无匣子风格-打开文件夹
	public static final int FOLDER_V8_SHOW_FOLDER = 78000411;
	//桌面-打开推荐视频ICON （app-直接打开app ; tjxq-进入推荐详情界面）
	public static final int LAUNCHER_OPEN_RECOMMEND_VIDEO_ICON = 84980412;
	
	/**
	 * 05桌面自升级及智能升级
	 */
	//91助手智能升级（xz-开始下载91助手，az-成功安装91助手，sj-跳转至91助手详情界面升级）
  	//public static final int SMART_UPDATE_SJ91 = 61000501;
  	//软件自升级界面传参错误  
  	public static final int SMART_UPDATE_UI_ARGUMENT_ERROR = 62000502;
  	//软件自升级界面智能升级信息传参错误
  	//public static final int SMART_UPDATE_INFORMATION_ARGUMENT_ERROR = 62000503;
  	//百度手机助手智能升级V7.3.1开始（点击省流量按钮-dj，下载百手客户端-xz，百手客户端安装成功-az）
  	public static final int SMART_UPDATE_BD_V731 = 73080504;

    /**
     * 06 app分发
     */

    //下载成功
    public static final int APP_DISTRIBUTE_DOWNLOAD_SUC = 61000600;
    //下载失败
    public static final int APP_DISTRIBUTE_DOWNLOAD_FAIL = 61000601;
    //安装
    public static final int APP_DISTRIBUTE_INSTALL = 61000602;
    //激活
    public static final int APP_DISTRIBUTE_ACTIVE = 61000603;
    //监测app分发产品端的转化率统一打点（具体可查相关文档）
    public static final int APP_DISTRIBUTE_PERCENT_CONVERSION_STATISTICS = 63100604;
    //匣子应用升级文件夹点击升级文件夹
    public static final int DRAWER_UPDATE_CLICK_FOLDER = 63100605;
    //匣子应用升级文件夹点击文件夹中的应用
    public static final int DRAWER_UPDATE_CLICK_FOLDER_APP = 63100606;
    //匣子应用升级文件夹点击一键省流量升级
    public static final int DRAWER_UPDATE_CLICK_ONE_KEY_PROVINCE_TRAFFIC_UPDATE = 63100607;
    //匣子应用升级文件夹点击下载安卓市场
    public static final int DRAWER_UPDATE_CLICK_DOWNLOAD_HIAPK = 63100608;
    //匣子应用升级异常未找到类名
    public static final int DRAWER_UPDATE_CLICK_UNFIND_CLASSNAME = 63100609;
    //搜索点击百度手机助手（dk-打开百度手助,xz-下载百度手助）
    public static final int SEARCH_CLICK_BAIDUHELPER_ITEM = 66000610;
    //游戏主题推荐游戏app详情点击量（zm-来自桌面图标点击，xq-来自主题详情点击）
    public static final int APP_DISTRIBUTE_SHOW_APP_DETAIL_CLICK = 67000611;
    //sem渠道包，桌面推荐软件点击情况(w-wifi;g-移动网络)
    public static final int SEM_CHANNEL_LAUNCHER_RECOMMEND_APP_CLICK = 68980612;
    //下载管理器apk下载（ks-开始下载;cg-成功;sb-失败;az-提示安装）
    public static final int APP_DOWNLOAD_MANAGE_APP_DOWNLOAD = 68980613;
    //下载管理器apk下载操作（zt-暂停;qx-取消）
    //public static final int APP_DOWNLOAD_MANAGE_APP_DOWNLOAD_OPERATE = 68980614;
    //桌面自升级包RSA验证失败
    public static final int PANDAHOME_VERSION_UPGRADE_RSA_CHECK_FAIL = 70580615;
    //文件夹应用推荐(zk-展开应用推荐,dj-点击应用推荐,xq-打开应用详情,xz-应用详情页开始下载)
    public static final int APP_DISTRIBUTE_FOLDER = 73100616;
    //无匣子风格-最HOT应用使用情况(zk-展开应用推荐,dj-点击应用推荐,hdj-最HOT应用页面点击应用推荐,xz-应用详情页开始下载)
    public static final int APP_DISTRIBUTE_FOLDER_NO_DRAWER = 76000617;
    //SP下载中本地无空间的下载失败
    public static final int APP_DISTRIBUTE_NO_SPACE_DOWNLOAD_FAIL = 80000618;
	//一键ROOT点击量(xz-点击下载;az-点击安装)
	public static final int ONE_KEY_ROOT_CLICK = 80980619;
	//一键ROOT的操作情况(cz-已操作过;ccg-已操作成功)
	public static final int ONE_KEY_ROOT_DOWNLOAD_INFO = 80980620;

    /**
     * 07 双11活动
     */
    //双11活动页面访问量（1-欢迎页面>点击抢红包；2-个性化banner>点击活动banner；3-个性化悬浮窗>点击悬浮窗；4-个性化付费页面>点击免费获取猫爪；5-每日新鲜事>点击活动banner；6-通知栏推送>点击活动介绍信息）
    public static final int NOVEMBER_ELEVEN_ACTIVITY_OPEN = 61000701;
    //双11活动页面下应用送红包使用情况（1-领红包>点击领红包按钮）
    //public static final int NOVEMBER_ELEVEN_ACTIVITY_REDPACKET_USER= 61000702;
    //双11活动软件下载量(label-软件名)
   // public static final int NOVEMBER_ELEVEN_ACTIVITY_SOFTWARE_DOWNLOAD= 61000703;
    //双11活动软件激活情况(label-软件名)
    //public static final int NOVEMBER_ELEVEN_ACTIVITY_SOFTWARE_ACTIVE= 61000704;
    //双11活动红包使用情况(只关注是否打开红包，不重复记录打开次数,1-批量打开，2-单个打开)
  //  public static final int NOVEMBER_ELEVEN_ACTIVITY_REDPACKET_OPEN= 61000705;
    
	/**
	 * 08 异常
	 */
	//daemon进程启动异常
	public static final int ERROR_DAEMON_START = 61000801;
    //由于安全问题
    public static final int SECURITYEXCEPTION_INTNET_START = 61100802;
    //activity未找到
    public static final int ACTIVITYNOTFOUNDEXCEPTION_INTENT_START = 61100803;
    //其它异常
    public static final int OTHEREXCEPTION_INTENT_START = 61100804;
    //机型适配失败（label-机型,OS版本）
    public static final int PHONE_ADAPTER_FAIL = 80000805;
	//处理推送异常（receive-接收 action-处理跳转 download-下载）
    public static final int PUSH_URL_SCHEME_HANDLER_FAIL = 80000806;
	// FAQ使用情况(1-搜索框热词标签值,2-文件夹icon标签值,3-桌面设置标签值)
	public static final int FAQ_USE_CASE=80000807;
	// 利用pm list package 命令获取已安装应用列表时，出错（hq,cx hq为调用pm命令获取包名失败，cx为调用queryIntentActivities查询相关信息）
	public static final int PM_GET_INSTALL_LIST_ERROR = 80980808;

	
	/***************应UED需求，对操作路径进行完整打点打点从6110版本开始生效start 以下点都是可控点，可根据服务端配置来控制收集与否的*************************/
	/**
	 * 09 91桌面图标
	 */
	//推荐文件夹
	public static final int LAUNCHER_ICON_RECOMMEND = 61100901;
	//桌面工具箱
	public static final int LAUNCHER_ICON_TOOLCASE = 61100902;
	//快速打开
	public static final int LAUNCHER_ICON_FASTOPEN = 61100903;
	//个性化
	public static final int LAUNCHER_ICON_INDIVIDUATION = 61100904;
	//桌面活动图标(添加到桌面-tj   点击图标打开-dk  点击红包-dkhb)
	public static final int LAUNCHER_CAMPAIGN_ICON = 69100901;
	
	
	/**
	 * 10 桌面操作
	 */
	//手势操作（sh-上滑;xh-下滑;sz-双指下滑）
	public static final int LAUNCHER_MANIPULATE_GESTURE = 61101001;
	//桌面编辑菜单（yy-添加应用，xbj-添加小部件，xtsz-系统设置，gxh-个性化,grzx-个人中心,zmsz-桌面设置,pmyl-屏幕预览，bzsc-帮助手册）
	public static final int LAUNCHER_MANIPULATE_EDIT_MENU = 61101002;
	//长按编辑菜单（zt-换主题，yy-添加应用，bz-换壁纸，xbj-添加小部件，tx-换特效）
	public static final int LAUNCHER_MANIPULATE_LONG_PRESS_MENU = 61101003;
	//特效-指尖特效
	public static final int LAUNCHER_EFFECT_FINGERTIP = 61101004;
	//特效-桌面特效
	public static final int LAUNCHER_EFFECT_LAUNCHER = 61101005;
	//特效-列表特效
	public static final int LAUNCHER_EFFECT_LIST = 61101006;
	//特效-列表进出特效
	public static final int LAUNCHER_EFFECT_LIST_INANDOUT = 61101007;
	//文件夹风格(0-iphone风格；1-android4.0风格;2-全屏风格)
	public static final int LAUNCHER_MANIPULATE_FOLDER_STYLE = 61101008;
	
	/**
	 * 11 应用列表
	 */
	//详情
	public static final int LAUNCHER_DRAWER_DETAIL = 61101101;
	//备份 6.5屏蔽
//	public static final int LAUNCHER_DRAWER_BACKUP = 61101102;
	//图标排序（label 0~4代表顺序）
	public static final int LAUNCHER_DRAWER_ICONSORT = 61101103;
	//智能分类
	public static final int LAUNCHER_DRAWER_INTELLIGENT_CLASSIFICATION = 61101104;
	//隐藏程序
	public static final int LAUNCHER_DRAWER_HIDING_PROGRAM = 61101105;
	//批量添加
	public static final int LAUNCHER_DRAWER_BATCH_ADD = 61101106;
	//小部件(添加成功的标签)
	public static final int LAUNCHER_DRAWER_WIDGET = 61101107;
	//我的手机  6.5屏蔽
//	public static final int LAUNCHER_DRAWER_MYPHONE = 61101108;
	//我的手机各功能使用情况（1-N：我的手机各模块）--该点不是可控点V6.6去除
//	public static final int LAUNCHER_DRAWER_MYPHONE_DETAILED = 61101109;
	//匣子搜索--该点不是可控点
	public static final int LAUNCHER_DRAWER_SEARCH = 61101110;
	
	/**
	 * 12 小部件
	 */
	//91天气（fg-换风格;sf-缩放）
	public static final int LAUNCHER_WIDGET_WEATHER = 61101201;
	//百度搜索（ss-搜索;ewm-二维码;rc-热词;sf-缩放）
	public static final int LAUNCHER_WIDGET_BAIDU_SEARCH = 61101202;
	//91省电（ql-省电清理;gl-省电管理;sf-缩放）
	public static final int LAUNCHER_WIDGET_SAVEPOWER = 61101203;
	//91快捷（使用数字对应，对应关系单独列）
	public static final int LAUNCHER_WIDGET_SHORTCUT = 61101204;
	//一键清理
	public static final int LAUNCHER_WIDGET_CLEAN = 61101205;
	//一键清理-深度清理(ca-长按图标的菜单；dhk-对话框)
	public static final int LAUNCHER_WIDGET_CLEAN_DEEPCLEAN = 61101206;
	//一键换壁纸（fg-壁纸风格；sz-设置；bzk-壁纸库）
	public static final int LAUNCHER_WIDGET_CHANGE_WALLPAPER = 61101207;
	//快速搜索屏(label代表开关)
	public static final int LAUNCHER_NAVIGATION = 61101208;
	//快速搜索屏-搜索热词(label代表开关)
	public static final int LAUNCHER_NAVIGATION_HOTWORD = 61101209;
	//快速搜索屏-返回键切换(label代表开关)
	public static final int LAUNCHER_NAVIGATION_BACKKEY_CHANGE = 61101210;
	//下拉通知栏菜单(ss-搜索;dk=最近打开;kg-快捷开关;tzl-系统通知栏) 6.5屏蔽
//	public static final int LAUNCHER_GLIDE_NOTIFICATION_BAR = 61101211;
	//热门游戏页面访问量--该点是不可控打点
	public static final int LAUNCHER_OPEN_HOT_GAME = 61101212;
	//91天气点击分布（1-时间；2-日历；3-天气简版；4-天气通版；5-城市；6-点击下载天气通版；7-进入推荐黄历天气界面）
	//public static final int LAUNCHER_WIDGET_WEATHER_CLICK_DISTRIBUTE = 62001213;
	//天气SDK内下载黄历天气客户端点击位置（1-关于界面；2-主界面天气指数；3-主界面宜忌；4-主界面农历；5-主界面多日天气图表；6-主界面主界面右下角更多图标；7-主界面多日温度图表;8-主界面主界面右下角更多图标直接下载）
	//public static final int LAUNCHER_WIDGET_WEATHER_DOWNLOAD_HUANGLI = 63001214;
	//天气SDK内下载黄历天气客户端安装成功打点
	//public static final int LAUNCHER_WIDGET_WEATHER_INSTALL_HUANGLI = 63101215;
	//点击淘宝插件进入内容页
	public static final int LAUNCHER_WIDGET_TAOBAO_INTO_CONTENT = 68001216;
	//91快捷病毒查杀（dj-点击病毒查杀；jr-进入推荐页；dk-打开腾讯手机管家病毒查杀；dt-点击推荐页推荐按钮）
	public static final int LAUNCHER_WIDGET_SHORTCUT_VIRUS_SCANNER = 71101217;
	//一键换壁纸V7.2改版（hbz-点击一键换壁纸次数，hhq-点击换回去次数，bc-点击保存壁纸的次数，gd-点击更多壁纸的次数，ck-弹出框的展开次数，yy-壁纸选择窗内点击应用壁纸的次数，gg-点击广告banner的次数）
	public static final int LAUNCHER_WIDGET_CHANGE_WALLPAPER_V72 = 71201218;
	//下滑发现(ss-搜索框;yy-推荐应用;bz-试用壁纸;zt-主题详情;xw-新闻;jp-精品应用资源;gg-广告;gx-无网络卡片点击进个性化;tq-进入天气详情)
	public static final int LAUNCHER_TOP_MENU = 73101219;
	//下滑发现打开情况(xh-下滑手势打开;ls-通过点击拉绳打开下滑发现;yy-下滑发现展示是应用推荐;kg-下滑发现展示是快捷开关;)
	public static final int LAUNCHER_TOP_MENU_OPEN_INFO = 73101220;
	//下滑发现快捷开关使用情况(wi-wifi;ly-蓝牙;ld-亮度;xl-响铃模式;zd-震动;sd-手电筒;gp-一键关屏;nz-闹钟;sz-系统设置;gd-更多)
	public static final int LAUNCHER_TOP_MENU_SWITCH_USERINFO = 73101221;
	//下滑发现拉绳使用情况(ca-长按形象，弹出隐藏选项框;yc1-通过弹框点击隐藏后隐藏拉绳+形象;yc2-通过设置界面选择隐藏拉绳+形象;kq-通过设置界面选择开启拉绳+形象)
	public static final int LAUNCHER_TOP_MENU_DRAWSTRING_USER = 75081222;
	//天气SDK内下载黄历天气客户端点击位置（1-关于界面；2-主界面天气指数；3-主界面左上角农历；4-主界面未来6小时天气图表；5-主界面多日天气图表），在天气插件中勿删
	public static final int LAUNCHER_WIDGET_WEATHER_ENTER_RECOMMEND_HUANGLI = 70181223;
	//活动形象卡片（zs-活动形象展示；dj-活动形象点击；kpdj-活动卡片点击）
	public static final int LAUNCHER_TOP_MENU_DRAWSTRING_CAMPAIGN = 75201224;
	//天气插件自定义城市的使用情况(tj-添加城市；gdcs-更多城市；gdjd-更多景点)天气插件中使用勿删
	public static final int LAUNCHER_WIDGET_WEATHER_ADD_CITY = 76081225;
	//一键清理推荐应用分发效率（应用弹框展示量；应用弹框下载量；深度清理页面应用下载量）
	public static final int LAUNCHER_WIDGET_CLEAN_APP_DISTRIBUTE = 76081226;
	//91快捷流量充值（dj-点击流量充值）
	public static final int LAUNCHER_WIDGET_SHORTCUT_FLOW_CHARGE = 76181227;
	//天气今日运势（jr-从天气入口进入今日运势次数;sz-今日运势详情设置信息改变次数）
	public static final int LAUNCHER_WIDGET_WEATHER_TODAY_LUCK = 80001228;
	//下滑发现搜狐新闻SDK打开量
	public static final int LAUNCHER_TOP_MENU_NEWS_SDK_OPEN = 80981229;
	//SEM渠道推荐抢红包应用下载点击 dj-点击下载  az-安装完成
	public static final int SEM_RECOMMEND_GRAB_REDPACKAGE_APP = 80981230;
	//91快捷天气（dj-点击天气图标，jrhl进入黄历天气，jrcj进入插件天气详情）
	public static final int LAUNCHER_WIDGET_SHORTCUT_WEATHER = 81981231;

	/**
	 * 13 商城我的
	 */
	//我的收藏
	public static final int LAUNCHER_PERSONAL_CENTER_COLLECT = 61101301;
	//我的下载
	public static final int LAUNCHER_PERSONAL_CENTER_DOWNLOAD_MANAGER = 61101302;
	//消费记录
	public static final int LAUNCHER_PERSONAL_CENTER_CONSUMPTION_RECORD = 61101303;
	
	/***********************************应UED需求，对操作路径进行完整打点打点从6110版本开始生效end*****************************************/
	/**
	 * 14 主题商城（统一打点，详细请查看桌面个性化商城各个位置点击量文档）
	 */
	public static final int LAUNCHER_THEME_SHOP = 61101401;
	//广告下载成功(tag 提交文件名)
	//public static final int LAUNCHER_THEME_SHOP_AD_DOWNLOAD_SUCCESS = 61101402;
	//广告展示(tag 提交文件名)
	public static final int LAUNCHER_THEME_SHOP_AD_SHOW = 61101403;
	//搜索过来自动下载
	//public static final int LAUNCHER_THEME_SHOP_SEARCHED_AUTO_DOWNLOAD = 61101404;
	//主题推荐页面点击下载应用
	public static final int LAUNCHER_THEME_SHOP_RECOMMEND_APP_DOWNLOAD = 61101405;
	//进入主题商城loading页（xz-下载软件，wb-外部打开网页，nb-内部打开网页）
	//public static final int LAUNCHER_THEME_SHOP_LOADING_CLICK = 61101406;
	//评论功能使用情况-点击评论tab
	public static final int LAUNCHER_THEME_SHOP_COMMENT_CLICK_TAB = 63201407;
	//评论功能使用情况-点击我要评论（1-未撰写评论，2-撰写评论）
	public static final int LAUNCHER_THEME_SHOP_COMMENT_MY_COMMENT = 63201408;
	//评论功能使用情况-点击发布评论
	public static final int LAUNCHER_THEME_SHOP_COMMENT_POST_COMMENT = 63201409;
	//本地主题详情页面（gm-购买；sy-试用）
	public static final int LAUNCHER_THEME_SHOP_LOCAL_THEME_DETAIL = 63201410;
	//在线主题详情页面（xz-试用下载；yy-试用应用）
	public static final int LAUNCHER_THEME_SHOP_ONLINE_THEME_DETAIL = 63201411;
	//主题试用到期的对话框(gm-购买；yy-应用回原主题)
	public static final int LAUNCHER_THEME_SHOP_TRYOUT_EXPIRE_DIALOG = 63201412;
	//主题试用通知栏点击跳转到详情页
	public static final int LAUNCHER_THEME_SHOP_TRYOUT_NOTIFICATION__CLICK = 63201413;
	//作者专区使用（1-进入作者专区；2-进入作者专区主题推荐）
	public static final int LAUNCHER_THEME_SHOP_AUTHOR_AREA = 65001414;
	//主题详情应用推荐
	public static final int LAUNCHER_THEME_SHOP_THEME_DETAIL_APP_RECOMMEND = 65001415;
	//天天系列主题详情
	//(2-在线详情一键下载；3-在线详情全套购买；4-在线详情滑动切换主题预览图；5-在线详情点击主题预览图；6-在线、本地点击应用全套)
	public static final int LAUNCHER_THEME_SHOP_THEME_SERIES_DETAIL = 65001416;
	//天天系列自动更换结束后的弹框(1-在弹框展示；2-再来一遍；3-换种口味试试)
	//public static final int LAUNCHER_THEME_SHOP_THEME_SERIES_END_HINT = 65001417;
	//进入搜索界面的时间（<=1;<=5;<=10;>10 分钟）
	public static final int LAUNCHER_THEME_SHOP_INTO_THEME_SEARCH = 65001418;
	//反馈提示出现情况（xyh-新用户首次退出出；cx-出现搜索结果后推出；no-无搜索内容时）
	public static final int LAUNCHER_THEME_SHOP_FEEDBACK_TIPS = 65001419;
	//用户点击提示框情况（fs-发送;tc-退出）
	public static final int LAUNCHER_THEME_SHOP_FEEDBACK_CLICK = 65001420;
	//壁纸统一打点(详见文档)
	//public static final int LAUNCHER_THEME_SHOP_WALLPAPER = 66001421;
	//个性化-美图功能使用情况(jr-进入美图页面;ck-查看美图;xz-下载美图)
	public static final int LAUNCHER_THEME_SHOP_MITO_USE = 67001421;
	//个性化-美图-我要创作入口(xq-详情页入口;sy-美图首页入口)
	public static final int LAUNCHER_THEME_SHOP_MITO_CREATION = 67001422;
	//个性化-美图-我要创作详细功能使用情况(tp-图片;jq-剪切;lj-滤镜;tz-贴纸;tm-涂抹;wz-文字;bc-保存/分享;fq-放弃编辑)
	//public static final int LAUNCHER_THEME_SHOP_MITO_CREATION_FUNCTION_USE = 67001423;
	//个性化-美图-分享至第三方功能使用情况(pyq-分享到朋友圈;wx-分享到微信;wb-分享到微博;qq-分享到QQ空间)
	//public static final int LAUNCHER_THEME_SHOP_MITO_SHARE = 67001424;
	//个性化-Loading页广告点击
	public static final int LAUNCHER_THEME_SHOP_AD_CLICK = 69101425;
	//个性化-主题详情页广告点击
	public static final int LAUNCHER_THEME_DETAIL_AD_CLICK = 69101426;
    //个性化美图功能使用情况（tg-投稿;bq-选择标签;ht-选择话题）
    //public static final int LAUNCHER_THEME_SHOP_MITO_FUNCTION_USE= 70001427;
    //个性化DIY主题功能使用情况(jr-进入DIY功能;sc-生成主题;qx-取消制作主题;yy-应用生成的主题)
   // public static final int LAUNCHER_THEME_SHOP_DIY_THEME_FUNCTION_USE = 70001428;
    //个性化-美图-美图位置展示（5,10xN -展示位置）
    public static final int LAUNCHER_THEME_SHOP_MITO_SHOW_POSITION = 71001429;
    //个性化-主题分享（diy-DIY主题分享;bz-壁纸分享;zt-主题分享）
    public static final int LAUNCHER_THEME_SHOP_THEME_SHARE = 71001430;
	//个性化-1小时人气主题(dj-主题点击量;gd-进入更多)
    public static final int LAUNCHER_THEME_SHOP_ONE_HOUR_HOT_THEME = 71001431;
    //个性化-1小时人气主题正在下载主题点击量
    public static final int LAUNCHER_THEME_SHOP_ONE_HOUR_HOT_THEME_DOWNLOADING_CLICK = 71001432;
    //个性化-主题活动(点击banner进入活动页面-djlq+主题名称，应用主题后添加贴纸-tjtz+主题名称)
    public static final int LAUNCHER_THEME_SHOP_CAMPAIGN_THEME = 71001433;
    //个性化-美图-进入话题界面
    public static final int LAUNCHER_THEME_SHOP_MITO_INTO_TOPIC = 71101434;
    //进入主题DIY（升级用户进入动态插件-sj，打开DIY主题大师-dk，未安装三个市场进入动态插件-nomarket，打开应用宝下载-xz_yyb，打开百度手助下载-xz_bd，打开360手助下载-xz_360）
  	public static final int LAUNCHER_DIY_THEME_ENTRANCE = 73081435;
  	//应用主题结果页功能使用情况(弹出框的展示次数-zs，点击“换回去”的次数-hhq，点击“就这套”的次数-jzt，点击“更多主题”的次数-gd，
  	// 主题推荐的展示次数-tjzs，主题推荐的点击次数-tjdj，广告banner的展示次数-ggzs，广告banner的点击次数-gg)
  	public static final int LAUNCHER_THEME_MENU = 73081436;
  	// 个性化天天漫画功能使用情况(jr-进入天天漫画模块; ksyd-点击开始阅读; jxyd-点击继续阅读; hs-点击漫画章节)，在美图动态插件中勿删
    public static final int COMIC_FUNCTION_USE = 75181437;
    // 个性化天天漫画阅读情况(标签为漫画名称前四个字)，在美图动态插件中勿删
    public static final int COMIC_READ_DETAIL = 75181438;
    // 个性化大家天天荐(点击个性化首页“大家天天荐”上的更多时记录-gd; 点击性化首页” 大家天天荐”及二级页面中的天天主题时记录-zt;点击 ”大家天天荐”二级页面中的“一键全套下载”按钮时记录-xz)
    public static final int RECOMMEND_CUSTOM_THEME_SERIES = 75981439;
    // 个性化天天漫画banner及分类点击情况(标签banner为dttj，分类-对应的分类名称)，在美图动态插件中勿删
    public static final int COMIC_BANNER_CATEGORY_CLICK = 75281440;
    // V7.7改版个性化个人壁纸功能使用情况(bzxq-壁纸详情查看按钮的点击量，bzxz-壁纸详情点击下载，hjdj-合辑的点击量，bzyl-壁纸预览按钮的点击量，swbz-设置为桌面壁纸按钮的点击量)，在个人壁纸动态插件中勿删
    public static final int WALLPAPER_V77_CLICK = 76981441;
    // V7.7改版个性化个人壁纸应用分发效果情况(壁纸预览模式中每一位置的某一推荐应用的点击量 标签: dj01、dj02、dj03...)，在个人壁纸动态插件中勿删
    public static final int WALLPAPER_V77_PREVIEW_APP_CLICK = 76981442;
    // V8个性化-发现顶部入口点击量(主题广场-ztgc，漫画-mh，美图-mt，订阅-dy，贴纸-tz)
    public static final int V8_DISCOVERY_ENTRANCE_CLICK = 79981443;
    // V8个性化-发现推荐模块点击情况(点击大家都在秀模块展示的用户-djdzx，点击天天漫画模块中的漫画-ttmh，点击主题广场模块中的主题-ztgc，点击我的订阅模块中的资讯-wddy
    // 大家都在秀-更多-gdmt，天天漫画-更多-gdmh，主题广场-更多-gdzt，我的订阅-更多-gddy)
    public static final int V8_DISCOVERY_RECOMMEND_CLICK = 79981444;
    // V8.1 风格(桌面设置进入风格-fg-zmsz，个性化首页进入风格-fg-gxh，双指下滑进入风格-fg-szxh，进入在线风格详情-zxfgxq，
    // 进入本地风格详情-bdfgxq，个性化我的进入我的风格-wdfg，双指下滑页面应用风格次数fgyy-szxh，详情界面应用风格fgyy-wdfg，
    // 风格撤回-fg-ch)
    public static final int V81_STYLE_CLICK = 80981445;
    
	/**
	 * 15 下载管理
	 */
	//下载管理使用情况（tzl-通知栏进入，glzx-个人中心进入；wd-个性化我的进入）
	public static final int  DOWNLOAD_MANAGER_USE = 61101501;
	//下载管理各类型使用情况（label-类型）
	public static final int  DOWNLOAD_MANAGER_TYPE = 61101502;
	//下载管理器下载状态（sd-手动暂停;zd-自定暂停;sc-未下载完成被删除）
	public static final int  DOWNLOAD_MANAGER_DOWNLOAD_STATE = 63101503;
	
	/**
	 * 16 游戏中心与应用商店
	 */
	//游戏中心-进入游戏中心
	//public static final int  GAME_CENTER_ENTER = 62001601;
	//游戏中心-顶部banner
	//public static final int  GAME_CENTER_TOP_BANNER = 62001602;
	//游戏中心-tab点击数（AS1-推荐；AS2-榜单；AS3-榜单热门；AS4-分类；AS5-搜索）
	//public static final int  GAME_CENTER_TAB_CLICK = 62001603;
	//游戏中心-搜索框点击次数
	//public static final int  GAME_CENTER_SEARCH = 62001604;
	//应用商店-进入应用商店
	//public static final int  APP_SHOP_ENTER = 62001605;
	//应用商店-tab点击数（AS1-推荐；AS2-热门；AS3-分类；AS4-搜索）
	//public static final int  APP_SHOP_TAB_CLICK = 62001606;
	//应用商店-顶部banner
	//public static final int  APP_SHOP_TOP_BANNER = 62001607;
	//应用商店-搜索框点击次数
	//public static final int  APP_SHOP_SEARCH = 62001608;
	//应用商店loading显示点击
	public static final int  APP_SHOP_LOADING_CLICK = 69001609;
	//应用商店推荐列表点击
	public static final int  APP_SHOP_RECOMMEND_LIST_CLICK = 69001610;
	//应用商店热词点击
	public static final int  APP_SHOP_HOTWORD_CLICK = 69001611;
	//游戏中心loading显示点击
	public static final int  GAME_CENTER_LOADING_CLICK = 69001612;
	//游戏中心推荐列表点击
	public static final int  GAME_CENTER_RECOMMEND_LIST_CLICK = 69001613;
	//游戏中心热词点击	
	public static final int  GAME_CENTER_HOTWORD_CLICK = 69001614;
	//游戏中心礼包显示
	public static final int  GAME_CENTER_GIFT_SHOW = 69001615;
	//游戏中心礼包领取状态：waz:应用未安装  lqsb:领取失败  th:淘号  lh:领号
	public static final int  GAME_CENTER_GIFT_GETTING = 69001616;
	//应用商店搜索界面下载完成(pxt:品效通广告 qt:其他)
	public static final int  APP_SHOP_SEARCH_DOWNLOAD_SUCCESS = 71001617;
	//应用商店游戏中心必备应用推荐展示(yzs-推荐应用展示；ydj-推荐应用一键安装点击； rzs-热门游戏展示；rdj-热门游记一键安装点击)
	public static final int APP_SHOP_AND_GAME_CENTER_NECESSARY_APP = 71001618;
	//应用商店推荐应用一键安装的应用(label-个数)
	public static final int APP_SHOP_NECESSARY_APP_COUNT = 71001619;
	//游戏中心推荐应用一键安装的应用(label-个数)
	public static final int GAME_CENTER_NECESSARY_APP_COUNT = 71001620;
	/**
	 * 17积分墙
	 */
	//进入积分墙(ZX1-个人中心入口；FK2-主题详情页入口；GM3-主题购买页面入口)
	public static final int  INTEGRAL_WALL_INTO = 62001701;
	//成功登入91账号
	//public static final int  INTEGRAL_WALL_LOGIN_SUCCESS = 62001702;
	//APP下载量
	//public static final int  INTEGRAL_WALL_APP_DOWNLOAD = 62001703;
	//app激活量（JH1-当日激活量；JH2-次日日激活量；JH3-一周激活量；JH4-一月激活量）
	//public static final int  INTEGRAL_WALL_APP_ACTIVATION = 62001704;
	//猫爪劵分发数（DR1-登入；DW2-下载后；JH3-激活后；JH4-刺入激活；JH5-一周激活后；JH6-一月激活）
	//public static final int  INTEGRAL_WALL_CATPAD_DISTRIBUTE = 62001705;
	//从banner进入积分墙的点击次数
	public static final int  INTEGRAL_WALL_BANNER_CLICK_ENTER = 63301706;
	//积分墙功能触发（xz-点击一键下载;gl-点击任务管理）
	public static final int INTEGRAL_WALL_CLICK_FUNCTION = 71001707;
	
	/**
	 * 18面搜索产品端的转化率
	 */
	//桌搜索的入口分布(1-0屏；2-百度插件；3-常驻通知栏；4-桌面手势下滑；5-桌面手势上滑)
	public static final int  SEARCH_PERCENT_CONVERSION_SEARCH_INLET = 63101801;
	//搜索的使用分布(1-热词；2-本地信息；3-本地联系人；4-本地应用；5-本地图片文件；6-本地音频文件；7-在线网页；8-本地视频)
	public static final int  SEARCH_PERCENT_CONVERSION_SEARCH_USE = 63101802;
	//其他搜索(1-0屏热词；2-百度搜索热词；3-浏览器打开百度首页)
	public static final int  SEARCH_PERCENT_CONVERSION_SEARCH_OTHER = 63101803;
	//0屏网址导航卡片的流量分发效果
	//public static final int  NAVIGATION_SCREEN_CARD_DISTRIBUTE_EFFECT = 63301804;
	//实时热点卡片的流量分发效果
	//public static final int  NAVIGATION_SCREEN_ACTUALTIME_CARD_DISTRIBUTE_EFFECT = 63301805;
	//监测实今日头条卡片的流量分发效果(t1-天天快报第一条;t2-天天快报第二条;t3-天天快报第二条)
	public static final int  NAVIGATION_SCREEN_TODAY_HEADLINES_CARD_DISTRIBUTE_EFFECT = 63301806;
	//监测0屏卡片的分享
	//public static final int  NAVIGATION_SCREEN_CARD_SHARE = 63301807;
	//桌面icon应用搜索(jr-进入应用搜索人数；sy-使用搜索框搜索应用；rc-点击热词人数)
	public static final int LAUNCHER_ICON_APP_SEARHC = 66001808;
	//0屏推荐词点击(点击位置)
	//public static final int NAVIGATION_SCREEN_REOMMEND_WORD_CLICK = 66001809;
	//小说阅读卡片的流量分发效果（0-更多网址；1-阅读卡片1；2-阅读卡片2；3-阅读卡片3）
	//public static final int NAVIGATION_SCREEN_STORY_CARD_DISTRIBUTE_EFFECT = 66001810;
	//小说阅读——我的栏目的流量分发效果(label:小说id)
	//public static final int NAVIGATION_SCREEN_STORY_CARD_MY_COLUMN_DISTRIBUTE_EFFECT = 66001811;
	//小说阅读——推荐栏目的流量分发效果(label:小说id)
	//public static final int NAVIGATION_SCREEN_STORY_CARD_RECOMMEND_COLUMN_DISTRIBUTE_EFFECT = 66001812;
	//小说阅读——加入入书架
	//public static final int NAVIGATION_SCREEN_STORY_CARD_ADD_BOOKRACK = 66001813;
	//小说阅读——在线阅读
	//public static final int NAVIGATION_SCREEN_STORY_CARD_ONLINE_READ = 66001814;
	//显示内置浏览器(0p-0屏搜索,xh-下滑搜索)
	public static final int  SEARCH_SHOW_INLAY_BROWSER = 68081815;
	//0屏卡片-开心一刻流量汇总 (hyp-换一批;gd-更多段子;xq-详情)
	public static final int NAVIGATION_SCREEN_HAPPY_MOMENT = 69101816;
	//进入0屏(hd-滑动进入,fh-返回键进入)
	public static final int NAVIGATION_SCREEN_INTO = 69101817;
	//0屏游戏卡片（hyp-换一批;gd-更多游戏;rm1~4-热门游戏1~4点击;h51~4-H5游戏1~4点击;zx1~2-咨询1~2点击）
	public static final int NAVIGATION_SCREEN_GAME_CARD = 70001818;
	//0屏唯品会卡片(gd-更多精品;ad1~5-广告1~5点击)
	public static final int NAVIGATION_SCREEN_VIPSHOP_CARD = 70001819;
	//0屏卡片-卡片推荐（mtmt-默认推美图;msmt-默认删美图;mtxz-默认推星座;msxz-默认删星座;gtxs-女生推小说;gsxs-女生删小说;
	//			   gtgw-女生推购物;gsgw-女生删购物;btmt-男生推美图;bsmt-男生删美图;btyx-男生推游戏;bsyx男生删游戏）
	public static final int NAVIGATION_SCREEN_CARDS_RECOMMEND = 71001820;
	//0屏卡片—下拉刷新(xl-下拉刷新；tj-添加卡片；gl-管理卡片)
	public static final int NAVIGATION_SCREEN_CARDS_PULL_REFRESH = 71001821;
	//进入中间页(jr-进入；dj-点击)
	public static final int INTO_MIDDLE_PAGE = 73001822;
	//中间页点击(wz-推荐网址;ss-搜索;rc-热词;xw-新闻;wg-文字链广告;dg-大图广告;xg-小图广告;ll-浏览器)
	public static final int INTO_MIDDLE_PAGE_CLICK = 73001823;
	//0屏购物卡片显示页数（label代表1-10页及10页以上）
	public static final int NAVIGATION_SCREEN_SHOPPING_CARD_SHOW_PAGE = 73101824;
	//0屏全屏动画（db-大动画播放;dd-大动画点击;xb-小动画播放;xd 小动画点击）
	public static final int NAVIGATION_SCREEN_FULL_SCREEN_ANIMATION = 75201825;
	//0屏趣发现(dtdj-大图资讯点击;x1dj-小图资讯1点击;x2dj-小图资讯2点击;hyp-换一批点击;gd-更多发现点击;zxlb-资讯列表内的点击;zxfx-资讯分享点击;zxxq-资讯详情页面内点击)
	public static final int NAVIGATION_SCREEN_INTERESTING_DISCOVERY_CARDS = 75301826;
	//零屏-搜狐新闻(hr-成功滑入;hk-成功滑开)
	public static final int NAVIGATION_SCREEN_SOUHU_NEWS = 75301827;
	//零屏趣发现卡片查看详情页面(label-咨询id)
	public static final int NAVIGATION_SCREEN_INTERESTING_DISCOVERY_DETAIL = 75301828;
	//京东SDK打开链接(un-未知;0p-零屏;ld-LOADING;ts-推送;tb-图标;yy-应用)
	public static final int NAVIGATION_SCREEN_JD_SDK_OPEN_URL = 76101829;
	//零屏-搜狐新闻引导卡片点击
	public static final int NAVIGATION_SCREEN_SOUHU_NEWS_GUIDE_CARDS_CLICK = 76101830;
	//零屏微传阅卡片统计一级界面(hyp-换一批;gd-更多订阅;wzc-卡片文章点击;)
	public static final int NAVIGATION_SCREEN_MICRO_CIRCULATION_FIRST_SCREEN = 76101831;
	//零屏微传阅卡片统计二级界面(jr-进入订阅号详情;wzc-二级页面的文章点击;tj-订阅号添加)
	public static final int NAVIGATION_SCREEN_MICRO_CIRCULATION_SECOND_SCREEN = 76101832;
	//搜索二级页面搜索源的选择情况(bd-选择百度搜索;sm-选择神马搜索)
	public static final int SEARCH_WIDGET_ENGINE_SELECT = 76201833;
	//零屏-搜狐新闻引导  kz-卡片展示 kd-卡片点击 dz-动画展示 
	public static final int NAVIGATION_SCREEN_SOUHU_NEWS_ANIM_GUIDE = 76201834;
	//桌面搜索媒体使用情况（yy-点击【搜索音乐】;sp-点击【搜索视频】;tp-点击【搜索图片】）
	public static final int LAUNCHER_SEARCH_MEDIA_INFO = 80001835;

	/**
	 * 24、定制版打点
	 */
	//猎鹰定制版点击浏览器次数
	public static final int LIEYING_CLICK_BROWSER = 84002401;
	//猎鹰定制版下载浏览器插件(ks-开始下载；cg-下载成功;sb-下载失败)
	public static final int LIEYING_DOWNLOAD_BROWSER_PLUGIN = 84002402;
	//猎鹰定制版打开猎鹰浏览器
	public static final int LIEYING_OPEN_BROWSER_PLUGIN = 84002403;
	//定制版桌面启动
	public static final int LAUNCHER_ONCREATE = 84002404;
	//黄历天气定制版黄历天气异常
	public static final int CALENDAR_WEATHER_EXCEPTION = 84102407;
	//启动锁屏服务
	public static final int LOCKSCREEN_START_SERVICE_COUNT = 84102408;
	
	/**
	 * 19自定义壁纸
	 */
	//启动相机情况(sys-打开系统相机；app-打开第三方相机)
	//public static final int  CUSTOM_WALLPAPER_START_CAMERA= 63101901;
	//相机应用关联功能统计(cx-出现魔力球；dj-单击魔力球；sj-双击魔力球；ca-长按魔力球)
	//public static final int  CUSTOM_WALLPAPER_CAMERA_APP_CORRELATION_FUNCTION = 63101902;
	
	/**
	 * 20新手引导
	 */
	//新手引导界面用户选择主题情况(1--以91桌面默认主题进入桌面, 2--以下载主题1进入桌面, 3--以下载主题2进入桌面, 4--点击下载推荐主题1, 5--点击下载推荐主题2)
	// 该打点于V6.3.3版本废弃 caizp 2015-4-24
	//public static final int README_GALLERY_THEME = 63102001;
	//升级用户更新日志界面推荐APP选择情况(y--下载推荐APP n--不下载推荐APP)
	public static final int README_UPGRADE_RECOMMEND_APP = 70282001;
	//设置默认说明引导的分发效率（1-用户首次滑动屏幕再次回到主屏时，设置默认桌面引导弹窗的点击量;2-用户点击HOME键由系统返回91桌面时，设置默认桌面引导弹窗的点击量）
	public static final int SETTING_DEFAULT_LAUNCHER_GUIDE_CLICK = 76202002;
	//主题商城引导设置默认桌面活动(jr-进入活动页；sz-跳转设置默认桌面;cg-设置成功;sb-设置失败)
	public static final int THEME_SHOP_GUIDE_SET_DEFAULT_LAUNCHER = 80002003;
	//OPPO/VIVO/阿里云辅助功能设置情况（tc-辅助功能弹窗次数；tck-点击马上开启次数；tcg-点击返回退出次数；fwk-顺利开启辅助服务次数;fwg-没开启辅助服务次数
	// dqtc-进入当前弹窗次数;tz-开启通知访问权限次数;bmd-开启白名单次数;tg-点击跳过次数；fhk-一键返回桌面开关手动打开次数；fhg-一键返回桌面开关手动关闭次数）
	public static final int SETTING_DEFAULT_LAUNCHER_GUIDE_ACCESSIBILITY_WINDOW = 82102004;
	//一键返回桌面的次数
	public static final int SETTING_DEFAULT_LAUNCHER_GUIDE_ONE_KEY_RETURN_LAUNCHER_COUNT = 83002005;
	/**
	 * 21使用间隔打点
	 */
	//爱淘宝 天猫精选使用间隔打点
//    public static final int INTERVAL_AITAOBAO = 65002101;
//    //应用商店 游戏中心使用间隔打点
//    public static final int INTERVAL_APPSTORE = 65002102;
//    //个性化使用间隔打点
//    public static final int INTERVAL_INDIVIDUAL = 65002103;
//    //推荐文件夹使用间隔打点
//    public static final int INTERVAL_RECOMMEND_FOLDER = 65002104;
    //爱淘宝 天猫精选使用时间分布打点
    public static final int DISTRIBUTION_AITAOBAO = 66002105;
    //应用商店 游戏中心使用时间分布打点
    public static final int DISTRIBUTION_APPSTORE = 66002106;
    //个性化使用时间分布打点
    public static final int DISTRIBUTION_INDIVIDUAL = 66002107;
    //推荐文件夹使用时间分布打点
    public static final int DISTRIBUTION_RECOMMEND_FOLDER = 66002108;
    //搜索使用时间分布打点
    public static final int DISTRIBUTION_SEARCH = 66002109;
    //活动使用时间分布打点
    public static final int DISTRIBUTION_COMPAIGN = 66002110;

    /**
     * 22、会员积分
     */
    //主题购买页面会员购买主题情况（kt-立即开通会员,dh-主题券兑换主题,jf-点击赚积分按钮）
    public static final int MEMBER_INTEGRAL_THEME_BUY_INFO = 70002201;
    //开通会员情况(kt-立即开通;jf-点击赚取积分按钮;p3-花99猫爪购买p3;p4-花199猫爪购买p4)
    public static final int MEMBER_INTEGRAL_OPEN_MEMBER = 70002202;
    //进入会员等级说明页(kt-开通会员页面进入;wd-从我的账号进入;hb-从我的红包页面进入)
    public static final int MEMBER_INTEGRAL_INTO_LEVEL_INFO = 70002203;
    //进入我的积分(wd-个性化我的进入;gr1-个人中心icon进入;gr2-个人中心我的积分)
    public static final int MEMBER_INTEGRAL_INTO_MY_INTEGRAL = 70002204;
    //进入我的红包 (wd-个性化我的进入;gr-个人中心进入)
    public static final int MEMBER_INTEGRAL_INTO_MY_REDPACKET = 70002205;
    //会员升级按钮被点击
    public static final int MEMBER_INTEGRAL_MEMBER_UPGRADE = 70002206;
    //进入每日任务页面(wd-个性化我的进入;gr-个人中心进入;jf-我的积分点击得积分进入)
    public static final int MEMBER_INTEGRAL_INTO_EVERYDAY_TASK = 70002207;
    //进入会员开通界面(wd-个性化我的进入;mr-每日任务进入;zh-我的账号进入)
    public static final int MEMBER_INTEGRAL_INTO_OPEN_MEMBER = 71002208;
    //新版会员开通界面(jr-进入;kt-点击立即开通;rw-做任务赚积分;p3-点击p3的购买;p4-点击p4的购买)
    public static final int MEMBER_INTEGRAL_INTO_NEW_OPEN_MEMBER = 71002209;
    //桌面菜单通知中心-开通菜单通知中心（k-开通，g-关闭）
    public static final int LAUNCHER_HOME_MENU_OPEN_LAUNCHER_NOTIFICATION = 71102210;
    //桌面菜单通知中心-通知点击（type请查阅文档）
    public static final int LAUNCHER_HOME_MENU_LAUNCHER_NOTIFICATION_CLICK = 71102211;
    //积分激励弹框(zs-弹框展示量;ck-查看详情并领取积分)
    public static final int MEMBER_INTEGRAL_ENCOURAGE = 72002212;
    //我的会员(wd-我的进入;mr-每日任务进入;dl-点击登录;jf-点击赚积分;gm-直接购买;sj-点击升级)
    public static final int MEMBER_INTEGRAL_MY_MEMBER = 72002213;
	//积分商城访问量（wd-我的积分商城;mr-每日任务顶部banner;xq-积分详情兑换积分）
	public static final int MEMBER_INTEGRAL_INTO_INTEGRAL_SHOP = 80002214;
	//我的账号页面使用统计（jr-进入我的帐号页面;sj-点击升级奖励;cz-点击充值记录;gm-点击购买记录）
	public static final int MEMBER_INTEGRAL_MY_ACCOUNT = 80002215;
	//我的会员页面使用统计（jr-进入我的会员页面;kt-点击马上开通;xf-点击续费会员;zf-点击支付）
	public static final int MEMBER_INTEGRAL_MY_V8_MEMBER = 80002216;

    /**
	 * 24 动态APK插件打点
	 */
	//SEM黄历天气定制版安装黄历天气
	public static final int CALENDAR_WEATHER_INSTALL = 80092401;
	//SEM黄历天气定制版打开黄历天气插件(cj-打开插件;app-打开黄历天气正式app)
	public static final int CALENDAR_WEATHER_OPEN_PLUGIN = 80092402;
	//SEM启动锁屏服务
	public static final int SCREEN_LOCK_START_LOCK_SERVER = 80092403;


    // 进入制作自定义主题 进入zr,完成wc
    // 点击本地主题底部 “制作天天主题”按钮时记录-jr1，点击“大家天天荐”二级页面标题栏icon时记录-jr2，点击本地自定义天天主题详情页 “全套应用”按钮时记录-yy
	public static final int CREATE_CUSTOM_SERIES = 68010001;


	//============================================================================================//
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!======海外定制版代码迁移的打点id====!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!===============Start=============!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
	//============================================================================================//


	/**
	 * 01：0屏
	 */
	/**
	 * 进入零屏 （1：进入零屏)
	 */
	public static final int NAVIGATION_ENTER = 61700101;
	/**
	 * 零屏mobovee广告详情（1：展示上报 2：广告绑定视图 3：点击广告）
	 */
	public static final int NAVIGATION_AD_MOBOVEE_DETAIL=63200109;
	/**
	 * 零屏du广告获取详情（1：获取成功 2：获取失败）
	 */
	public static final int NAVIGATION_AD_DU_OBTAIN_SITUATION = 66000110;

	/**
	 * 02：搜索页面功能
	 */
	/**
	 * 进入搜索页面方式（1：0屏 2：搜索插件 3：匣子搜索按钮 4：桌面上滑）
	 */
	public static final int SEARCH_PAGE_ENTER = 61700201;
	/**
	 * 搜索页面的点击情况（1：广告点击 2：常用应用点击 3：网页搜索点击 4：本机搜索 5：热词点击）
	 */
	public static final int SEARCH_PAGE_CLICKS = 61700202;

	/**
	 * 热词的点击（1、免费热词 2、付费热词）
	 */
	public static final int SEARCH_HOTWORD_CLICKS = 66700203;

	/**
	 * 桌面异常
	 */
	public static final int LAUNCHER_EXCEPTION_FLAG = 61709901;

	/**
	 * 下载异常统计
	 * 1=连接异常（有网络情况下）
	 * 2.sdcard已满
	 */
	public static final int DM_EXCEPTION = 66709903;

	/**
	 * oom记录
	 */
	public static final int OOM_EXCEPTION = 66709904;


	// 13 天气模块使用统计  wcz
	/**
	 * 天气初次启动桌面时是否定位成功
	 *
	 * 定位成功  标签值：1 （初次启动桌面时城市定位成功，天气小部件有数据时记录）
	 * 定位失败  标签值：0 （初次启动桌面时城市定位失败，天气小部件无数据时记录）
	 */
	public static final int LAUNCHER_WEATHER_FIRST_LOCATION = 62001301;
	public static final String LAUNCHER_WEATHER_FIRST_LOCATION_SUCCESS = "1";
	public static final String LAUNCHER_WEATHER_FIRST_LOCATION_FAILURE = "0";

	/**
	 * 海外天气小部件使用情况
	 *
	 * 进入小部件二级界面  标签值：1 （用户进入小部件二级界面时记录）
	 * 添加天气城市  标签值：2 （用户添加天气城市时记录）
	 * 删除默认天气城市  标签值： 3（用户删除默认天气城市时记录）
	 * 无法定位所在城市  标签值：4 （无法定位所在城市时记录）
	 * 手动定位成功城市  标签值：5 （手动定位成功城市时记录）
	 */

	public static final int LAUNCHER_WEATHER_COMPONENT_USE_CONDITION = 62001302;
	public static final String LAUNCHER_WEATHER_COMPONENT_USE_CONDITION_INTO = "1";
	public static final String LAUNCHER_WEATHER_COMPONENT_USE_CONDITION_INSERT = "2";
	public static final String LAUNCHER_WEATHER_COMPONENT_USE_CONDITION_REMOVE = "3";
	public static final String LAUNCHER_WEATHER_COMPONENT_USE_CONDITION_FAIL_LOCATION = "4";
	public static final String LAUNCHER_WEATHER_COMPONENT_USE_CONDITION_SUCC_LOCATION = "5";

	/**
	 * 用户添加的天气城市个数
	 *
	 * 0  标签值：0
	 * 1  标签值： 1
	 * 2  标签值： 2
	 * 3  标签值： 3
	 * 大于3 标签值： 4
	 */
	public static final int LAUNCHER_WEATHER_CITIES_NUMBER = 62001303;
	public static final String LAUNCHER_WEATHER_CITIES_NUMBER_ZERO = "0";
	public static final String LAUNCHER_WEATHER_CITIES_NUMBER_ONE = "1";
	public static final String LAUNCHER_WEATHER_CITIES_NUMBER_TWO = "2";
	public static final String LAUNCHER_WEATHER_CITIES_NUMBER_THREE = "3";
	public static final String LAUNCHER_WEATHER_CITIES_NUMBER_MORE = "4";

	/**
	 * 用户安装桌面定位后系统GPS开关状态。
	 * 0 处于打开状态
	 * 1 处于关闭状态
	 */
	public static final int LAUNCHER_WEATHER_GPS_STATUS = 62001304;
	public static final String LAUNCHER_WEATHER_GPS_STATUS_OPEN = "0";
	public static final String LAUNCHER_WEATHER_GPS_STATUS_CLOSE = "1";

	/**
	 * 因重写定位服务，于1.0.1版本废弃<br/>
	 * 天气定位方法成功率打点统计<br/>
	 * 0: 无网络连接导致的定位失败<br/>
	 * 1: google基站定位<br/>
	 * 2: wifi定位<br/>
	 * 3: gps定位<br/>
	 * 4： CDMA定位<br/>
	 * 5: 所有定位失败<br/>
	 */
	@Deprecated
	public static final int LAUNCHER_WEATHER_LOCATION_METHOD = 62001305;
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_NO_NETWORK = "0";
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_BASE_STATION = "1";
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_NETWORK = "2";
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_GPS = "3";
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_CDMA = "4";
	@Deprecated
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_FAILER = "5";

	/**
	 * 天气定位方法成功率打点统计<br/>
	 * 0: 无网络连接导致的定位失败<br/>
	 * 1: gps定位<br/>
	 * 2: wifi定位<br/>
	 * 3： IP定位<br/>
	 * 4: 所有定位失败<br/>
	 */
	public static final int LAUNCHER_WEATHER_LOCATION_METHOD_EX = 62001306;
	public static final String LAUNCHER_WEATHER_LOCATION_NO_NETWORK_EX = "0";
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_GPS_EX = "1";
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_NETWORK_EX = "2";
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_IP_EX = "3";
	public static final String LAUNCHER_WEATHER_LOCATION_METHOD_FAILER_EX = "4";

	/**
	 * 21：充电锁屏=============================================
	 */
	/**
	 * 充电锁屏展示(1:展示)
	 */
	public static final int LAUNCHER_CHARGING_SAVER_SHOW = 63302101;
	/**
	 * 充电锁屏广告情况（1：请求 2：展示  3：点击）
	 */
	public static final int LAUNCHER_CHARGING_SAVER_AD_DETAIL = 63302102;
	/**
	 * 22：广告相关=============================================
	 */
	/**
	 * 广告工程获取到广告策略失败（位置）
	 */
	public static final int LAUNCHER_AD_OBTAIN_STRATEGY_FILE_FAILED = 66002201;
	/**
	 * 广告工程获取到广告策略成功位置）
	 */
	public static final int LAUNCHER_AD_OBTAIN_STRATEGY_FILE_SUCCESSED = 66002202;

	/**
	 * 通用web页面跳转异常
	 */
	public static final int WEB_PAGE_CRASH = 66609902;

	/**
	 *浏览器推荐位
	 */
	public static final int LAUNCHER_RECOMMEND_APP_DOCKBAR_BROWER= 86002410;

	/**
	 *零屏推荐应用
	 */
	public static final int LAUNCHER_RECOMMEND_APP_ZERO_GRID= 86002411;

	/**
	 *搜索屏推荐应用
	 */
	public static final int LAUNCHER_RECOMMEND_APP_SEARCH_GRID= 86002412;

	/**
	 *推送的应用
	 */
	public static final int LAUNCHER_RECOMMEND_APP_PUSH= 86002413;

	//============================================================================================//
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!======海外定制版代码迁移的打点id====!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!===============End===============!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
	//============================================================================================//
}
