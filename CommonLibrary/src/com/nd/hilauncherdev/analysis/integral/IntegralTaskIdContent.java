package com.nd.hilauncherdev.analysis.integral;

import android.content.Context;

import com.nd.android.pandahome2.R;

/**
 * 积分任务id常量列表
 * <p>Title: IntegralTaskIdContent</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年10月21日
 */
public class IntegralTaskIdContent {
	/**
	 * 获取任务对应的弹出信息文字
	 * <p>Title: gettaskDescribe</p>
	 * <p>Description: </p>
	 * @param context
	 * @param taskType
	 * @return
	 * @author maolinnan_350804
	 */
	public static String gettaskDescribe(Context context,int taskType){
		String result = "";
		switch (taskType) {
			case 1:result = context.getString(R.string.obtain_integral_remind01);break;
			case 2:result = context.getString(R.string.obtain_integral_remind02);break;
			case 3:result = context.getString(R.string.obtain_integral_remind03);break;
			case 4:result = context.getString(R.string.obtain_integral_remind04);break;
			case 5:result = context.getString(R.string.obtain_integral_remind05);break;
			case 6:result = context.getString(R.string.obtain_integral_remind06);break;
			case 7:result = context.getString(R.string.obtain_integral_remind07);break;
			case 8:result = context.getString(R.string.obtain_integral_remind08);break;
			case 9:result = context.getString(R.string.obtain_integral_remind09);break;
			case 10:result = context.getString(R.string.obtain_integral_remind10);break;
			case 11:result = context.getString(R.string.obtain_integral_remind11);break;
			case 12:result = context.getString(R.string.obtain_integral_remind12);break;
			case 13:result = "";break;//默认内容
		}
		return result;
	}
	
	
	
	
	/*******************************************************************************/
	/****************** 		    V7积分任务ID号赋予规则		   *********************/
	/****************** 	          桌面版本号4位+系列号2位+内部序号2位                    *********************/
	/******************  最终需要在cosimple后台对应位置绑定taskid才会生效    *********************/
	/*******************************************************************************/
	/**
	 * 01、搜索一下
	 */
	//使用0屏搜索\上滑菜单的搜索打开百度搜索
	public static final int SEARCH_USE_NAVIGATION_OR_LAUNCHER_MENU_SEARCH = 70000101;
	//个性化-搜索页
	public static final int SEARCH_THEME_SHOP_SEARCH = 70000102;
	//热门游戏-游戏搜索
	public static final int SEARCH_HOT_GAME_GAME_SEARCH = 70000103;
	//应用商店-应用搜索
	public static final int SEARCH_APP_STORE_APP_SEARCH = 70000104;
	//推荐文件夹中的应用搜索
	public static final int SEARCH_RECOMMEND_FOLDER_APP_SEARCH = 70000105;
	//匣子搜索
	public static final int SEARCH_DRAWER_SEARCH = 70000106;
	
	/**
	 * 02、下载推荐游戏
	 */
	//SP=31下载的游戏被激活
	public static final int DOWNLOAD_RECOMMEND_GAME_THEME_RECOMMEND_GAME = 70000201;
	//热门游戏-推荐banner被点击
	public static final int DOWNLOAD_RECOMMEND_GAME_HOT_GAME_RECOMMEND_BANNER = 70000202;
	//热门游戏-推荐点击下载按钮
	public static final int DOWNLOAD_RECOMMEND_GAME_HOT_GAME_RECOMMEND_DOWNLOAD = 70000203;
	//热门游戏-榜单点击下载按钮
	public static final int DOWNLOAD_RECOMMEND_GAME_HOT_GAME_RANK_DOWNLOAD = 70000204;
	//热门游戏-游戏详情点击下载
	public static final int DOWNLOAD_RECOMMEND_GAME_HOT_GAME_DETAIL_DOWNLOAD = 70000205;
	
	/**
	 * 03、点击搜索热门图标
	 */
	//0屏图标icon
	public static final int NAVIGATION_ICON = 70000301;
	//0网址导航
	public static final int NAVIGATION_RECOMMEND_WEB = 70000302;
	
	/**
	 * 04、点击浏览热点
	 */
	//百度搜索小部件的热词
	public static final int BAIDU_WIDGET_HOTWORD = 70000401;
	//0屏实时热点
	public static final int NAVIGATION_REALTIME_HOTWORD = 70000402;
	//0屏小说阅读
	public static final int NAVIGATION_STORY = 70000403;
	//0屏购物指南
	public static final int NAVIGATION_SHOPPING_GUIDE = 70000404;
	//0屏品牌特卖
	public static final int NAVIGATION_SHOPPING_BRAND_SALE = 70000405;
	//0屏热门游戏
	public static final int NAVIGATION_HOT_GAMES = 70000406;
	
	
	/**
	 * 05、欣赏广告
	 */
	//点击广点通或淘宝广告条
	public static final int ADMIRE_ADS_TENCENT_OR_TAOBAO_BANNER = 70000501;
	//个性化-推荐页点击4个banner
	public static final int ADMIRE_ADS_THEME_SHOP_RECOMMEND_BANNER = 70000502;
	
	/**
	 * 06、下载推荐应用
	 */
	//SP=27下载的应用被激活（积分墙）
	public static final int DOWNLOAD_RECOMMEND_APP_JIFENQIANG = 70000601;
	//SP=1~35除31、27之外的应用被激活
	public static final int DOWNLOAD_RECOMMEND_APP_OTHER_APP = 70000602;
	//应用商店-推荐页banner点击
	public static final int DOWNLOAD_RECOMMEND_APP_APP_STORE_RECOMMEND_BANNER = 70000603;
	//应用商店-推荐页下载按钮点击
	public static final int DOWNLOAD_RECOMMEND_APP_APP_STORE_RECOMMEND_DOWNLOAD = 70000604;
	//应用商店-热门页点击下载
	public static final int DOWNLOAD_RECOMMEND_APP_APP_STORE_HOT_DOWNLOAD = 70000605;
	//应用商店-应用详情页点击下载
	public static final int DOWNLOAD_RECOMMEND_APP_APP_STORE_APP_DETAIL_DOWNLOAD = 70000606;
	
	/**
	 * 07、欣赏开机大屏页面
	 */
	//个性化、应用商店、热门游戏loading界面
	public static final int ADMIRE_LOADING = 70000701;
	
	/**
	 * 08、分享
	 */
	//主题分享
	public static final int SHARE_THEME_SHARE = 70000801;
	//美图制作并分享
	public static final int SHARE_THEME_MITO_SHARE = 70000802;
	//DIY主题制作并分享
	public static final int SHARE_THEME_DIY_THEME_SHARE = 70000803;
	
	/**
	 * 09、设置默认桌面
	 */
	//设置默认桌面成功
	public static final int SETTING_DEFAULT_LAUNCHER = 70000901;
	
	/**
	 * 10、个性化资源下载
	 */
	//主题下载成功
	public static final int THEME_SHOP_THEME_DOWNLOAD_SUCCESS = 70001001;
	//锁屏、图标、时钟天气、通讯录、输入法下载成功
	public static final int THEME_SHOP_LOCK_ICON_WEATHER_ADDRESSLIST_INPUTMETHOD_DOWNLOAD_SUCCESS = 70001002;
	//铃声、字体下载成功
	public static final int THEME_SHOP_RING_FONT_DOWNLOAD_SUCCESS = 70001003;
	//壁纸下载成功
	public static final int THEME_SHOP_WALLPAPER_DOWNLOAD_SUCCESS = 70001004;
	
	/**
	 * 11、桌面美化
	 */
	//长按添加小插件成功
	public static final int LAUNCHER_GLORIFY_LONGTOUCH_ADD_WIDGET = 70001101;
	//长按添加应用成功
	public static final int LAUNCHER_GLORIFY_LONGTOUCH_ADD_APP = 70001102;
	//更换主题成功
	public static final int LAUNCHER_GLORIFY_CHANGE_THEME = 70001103;
	
	/**
	 * 12、参与活动
	 */
	//参与每日新鲜活动点击
	public static final int PARTAKE_ACTIVITY_EVERYDAY_NEW_ACTIVITY = 70001201;
	//点击幸运转盘
	public static final int PARTAKE_ACTIVITY_LELELUCKY = 70001202;
	//用户进入主题商城积分福利
	public static final int USER_INTO_THEME_SHOP_INTEGRAL_WEAL = 72001203;
	
	/**
	 * 默认
	 */
	//开通桌面菜单通知中心
	public static final int DEFAULT_OPEN_LAUNCHER_MENU_NOTIFICATION = 71101301;
	//桌面菜单1积分任务
	public static final int DEFAULT_LAUNCHER_MENU_NOTIFICATION_ONE_INTEGRAL_TASK = 71101302;
	public static final int DEFAULT_LAUNCHER_MENU_NOTIFICATION_TEN_INTEGRAL_TASK = 71101303;
	public static final int DEFAULT_LAUNCHER_MENU_NOTIFICATION_FIFTEEN_INTEGRAL_TASK = 71101304;
	public static final int DEFAULT_LAUNCHER_MENU_NOTIFICATION_TWENTY_INTEGRAL_TASK = 71101305;
	//用户注册送积分
	public static final int USER_REGIST_INTEGRAL_TASK = 72001306;
	/**
	 * 14 用户等级任务
	 */
	//用户在91桌面登录
	public static final int USER_LEVEL_LOGIN_TASK = 80001401;
	//用户下载个性化资源（主题/壁纸/锁屏/字体/输入法等）超过3个
	public static final int USER_LEVEL_DOWNLOAD_TASK = 80001402;
	
	/**
	 * 15 送红包任务
	 */
	//主题商城设置默认桌面引导任务
	public static final int THEME_SHOP_LAUNCHER_GUIDE_CAMPAIGN = 80001501;
}
