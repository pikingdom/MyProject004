/**   
 *   
 * @file  
 * @brief 【一些定义类】
 *
 * @<b>文件名</b>      : ComDataDef
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-27 上午11:16:59 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.calendar.common;


public final class ComDataDef
{
	public static final String VERSION_NAME = "1.2";
	public static final String CALENDAR_PACKAGE = "com.calendar.UI";
	public static final String LOCK_PACKAGE = "cn.com.nd.s";
	/**
	 * 黄历天气客户端下载地址 caizp 2014-10-24
	 */
	public static final String CALENDAR_DOWN_URL = "http://url.ifjing.com/QF3aYb";//"http://zy.down.91.com/download/rj/weather_panda_android.apk";
	public static final int CALENDAR_VAR_CODE_2_2_0 = 21;
	public static final int CALENDAR_VAR_CODE_2_3_0 = 23;
	public static final int CALENDAR_VAR_CODE_2_4_0 = 25;
	public static final int CALENDAR_VAR_CODE_2_5_0 = 27;
	public static final int CALENDAR_VAR_CODE_2_7_0 = 32;
	public static final int CALENDAR_VAR_CODE_3_3_2 = 43;
	public static final int CALENDAR_VAR_CODE_3_6_1 = 361;
	/**
	 * 从该版本增加BrightnessActivity，用于解决5.0以上固件黄历天气被强制停止后无法访问provider的问题
	 */
	public static final int CALENDAR_VAR_CODE_3_14_1 = 31410;
	
	// 日历项目基本类类型
	public static final class CalendarData
	{
		// 产品Id
		private static int APPID = 8054;
//		
		public static final int GET_APPID() {
			return APPID;
		}

		// DATE_INFO_TYPE:时间类型判断
		public static final int	DATE_INFO_TYPE			= 0xA0000001;
		// LUAN_INFO_TYPE:农历信息类型
		public static final int	LUAN_INFO_TYPE			= 0xA0000002;
		// TDYGOOD_INFO_TYPE:今日吉时类型
		public static final int	TDYGOOD_INFO_TYPE		= 0xA0000003;
		// JIANPING_LUCK_INFO:开运指南、今日简评
		public static final int	JIANPING_LUCK_INFO_TYPE	= 0xA0000004;
		// PEP_INFO_TYPE:人员类型
		public static final int	PEP_INFO_TYPE			= 0xA0000005;
	 }
	
	public static final class ConfigSet {
		/*插件显示城市*/
		public static final String CONFIG_KEY_WIDGET_CITY_ID = "Widget_CityID";
		
		/*是否是第一次进入天气界面(true/false)默认true*/
		public static final String CONFIG_KEY_FIRST_TO_WEATHER = "first_to_weather";
		
	    // 通讯密钥
        public static final String CONFIG_NAME_KEY_COMM_KEY = "comm_key";
	}
	
	// 数据库宏定义数据
	public static final class DbInfo
	{
		// R_DB_CUSTOM:数据库CustomResult.db
		public static final String	R_DB_CUSTOM			= "CustomResult.db";
		// 只读数据库CustomResult.db版本号
		// 只读数据库版本更新 2012.11.28 = 1(1.0), 增加精简库, by chenxi
		public static final int		R_DB_CUSTOM_VERSION	= 2;
		// CustomResult.db数据库类别
		public static final int		R_DB_CUSTOM_TYPE	= 1;

		// R_DB_USER:用户数据存储db
		public static final String	R_DB_USER			= "User.db";
		// 只读数据库User.db版本号
		public static final int		R_DB_USER_VERSION	= 1;	// 数据库版本更新(版本比对) 2012.10.12 = 17(2.1),  by yyy
		// User.db数据库类别
		public static final int		R_DB_USER_TYPE		= 2;
		
		public static final String	R_DB_CALENDAR		= "Calendar.db";
		public static final int		R_DB_CALENDAR_VERSION = 1;
	}

	public static final class NetWorkAppFunId
	{
		// 今日吉时
		public static final int	ASTRO_CUSTOM_GET_TDYGOODTIME_RESULT	= 4004;
		// 下载主人信息
		public static final int	ASTRO_UAP_HOSTPEOPLEINFO_RESULT		= 0xB0000005;
	}

	// 预测模块基本宏定义数据
	public static final class CoustomData
	{
		// 相冲信息
		public static final String TWELVE_CLASH[] = {
			"冲鼠(子午相冲)", "冲牛(丑未相冲)", "冲虎(寅申相冲)",
			"冲兔(卯酉相冲)", "冲龙(辰戌相冲)", "冲蛇(巳亥相冲)",
			"冲马(子午相冲)", "冲羊(丑未相冲)", "冲猴(寅申相冲)",
			"冲鸡(卯酉相冲)", "冲狗(辰戌相冲)", "冲猪(巳亥相冲)"
		};

		public static final String	TOOL_NAME[]			= { "天气", "万年历", "黄历", "更多" };															
	}

	// 网络请求网址宏定义数据
	public static final class HttpURLData
	{
		// HttpLoginUap
		// 内：http://192.168.94.19/uaps/
		// 外：http://uap.91.com/
//		public static final String	LOGINUAP_OUT	= "http://uap.91.com/";
//		public static final String	LOGINUAP_IN		= "http://192.168.94.19/uaps/";

		public static final boolean SERVER_TEST_W	= false;		// 天气，测试服务器(发布时要改为 false)
		// HttpAppFunClient
		public static final String	APPFUN_OUT		= "http://api.divine.rj.91.com/";
//		public static final String	APPFUN_IN		= "http://192.168.9.128:8080/divine/";
//		public static final String  HOST_WEATHER_IN	= "http://192.168.9.128:8080/divine/";

		//悬赏反馈
		public static final String  APPFUN_SUGGEST =  "http://hltq.91.com/";//"http://api.fk.rj.91.com/";
		//天气
		public static final String  HOST_WEATHER_OUT=  "http://hltq.91.com/";//"http://api.weather.rj.91.com/";//"http://121.207.250.119/";//
		//广告推荐
//		public static final String  HOST_ADAPI_OUT  = "http://api.ad.rj.91.com/";
		
		public static final String  APPFUN_GPS      = HOST_WEATHER_OUT + "getweatherjson";
		public static final String  APPFUN_WEATHER  = "http://tq.91.com/api/?act=215";//HOST_WEATHER_OUT + "getweatherdatapro";//"getweatherdata";//"getweatherinfo";
		public static final String 	WEATHER_MAIN_PAGE_BASE_URL = "http://tq.91.com/api/?act=210"; //天气主界面接口地址
		public static final String  APPFUN_SKINLIST = HOST_WEATHER_OUT + "getPluginSkinList";
		public static final String  APPFUN_SKIN_CHK = HOST_WEATHER_OUT + "pluginskinquery";
		public static final String  APPFUN_SOFTAD   = HOST_WEATHER_OUT + "getsoftinfo";
		public static final String  APPFUN_VERINFO  = HOST_WEATHER_OUT + "getverinfo";
		public static final String  APPFUN_TOOLS    = HOST_WEATHER_OUT + "othertools";
		public static final String  APPFUN_TIME     = HOST_WEATHER_OUT + "getServerDateTime";
		public static final String  APPFUN_E_COMM   = HOST_WEATHER_OUT + "ebsquery";	// 电商推荐接口
		public static final String  APPFUN_GETKEY   = HOST_WEATHER_OUT + "getkey"; // 获取密钥

		// 获取天气
		public static final String	WEATHER_OUT		= "http://m.weather.com.cn/data/";
		public static final String	WEATHER_NOW_OUT	= "http://www.weather.com.cn/data/sk/";

		// 天气刷新时间
		public static final long	REF_TIME		= 1000 * 60 * 60;
	}

	public static final class ConfigsetData
	{
		public static final String	CONFIG_NAME					= "calendarWidgetSet";
//		public static final String	CONFIG_NAME_KEY_BEGIN		= "Begin";
//		public static final String	CONFIG_NAME_KEY_SUNDAY_FIRST= "Week";				// 周首日
//		public static final String	CONFIG_NAME_KEY_SHENXIAO	= "ShengXiao";
//		public static final String  CONFIG_NAME_KEY_JIERI_PRIORITY = "jieriSingle";		// 节日优先级
//		public static final String  CONFIG_NAME_KEY_DISPLAY_NL	= "jieriYouXianNL";
//		public static final String  CONFIG_NAME_KEY_DISPLAY_JQ 	= "jieriYouXianJQ";
//		public static final String  CONFIG_NAME_KEY_DISPLAY_GL	= "jieriYouXianXL";
//		public static final String  CONFIG_NAME_KEY_UPDATE_DATE	= "updateDate";
//		public static final String  CONFIG_NAME_KEY_BK_TYPE		= "bkType";
		public static final String  CONFIG_NAME_KEY_SETSHOW_TYPE = "setShowType";
		public static final String  CONFIG_NAME_KEY_SKIN_TAB	= "WidgetSkinTab";		// 插件最后一次选择
		
		//热区插件配置
		//4x1
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_1_1	= "HotArea_4_1_1";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_1_2	= "HotArea_4_1_2";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_1_3	= "HotArea_4_1_3";
//		//4x2
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_2_1	= "HotArea_4_2_1";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_2_2	= "HotArea_4_2_2";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_2_3	= "HotArea_4_2_3";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_2_4	= "HotArea_4_2_4";
//		public static final String  CONFIG_NAME_KEY_HOTAREA_4_2_5	= "HotArea_4_2_5";
		
		//是否开启自动更新(boolean)默认true
		public static final String  CONFIG_NAME_KEY_AUTOUPDATE = "weatherAutoUpdate";
		//更新的时间间隔(int 0~24)默认1小时
		public static final String  CONFIG_NAME_KEY_TIEMUPDATE = "weatherTimeUpdate";
		//开始时间时(int 0~24)默认8点
		public static final String  CONFIG_NAME_KEY_BEGINGTIEMEH = "weatherBeginTimeH";
		//开始时间分(int 0~60)默认0分
		public static final String  CONFIG_NAME_KEY_BEGINGTIEMEM = "weatherBeginTimeM";
		//结束时间时(int 0~24)默认22点
		public static final String  CONFIG_NAME_KEY_ENDTIEMEH = "weatherEngTimeH";
		//结束时间时(int 0~60)默认0分
		public static final String  CONFIG_NAME_KEY_ENDTIEMEM = "weatherEngTimeM";
		
		// 是否更新全部城市 配置(默认true)
		public static final String CONFIG_NAME_KEY_UPDATE_ALL = "weatherUpdateAll";
		
		// PM 默认源(默认true)
		public static final String CONFIG_NAME_KEY_PM_SOURCE = "weatherPMSource";
		
		// 软件推荐更新时间
		//public static final String CONFIG_NAME_KEY_SOFT_RECOMMEND_DATE = "softRecommendDate";
			
		// 数据升级修复失败提示信息
		//public static final String CONFIG_NAME_KEY_FIX_FAIL = "fixFailHint"; 
		
		// 版本履历提示
		public static final String CONFIG_NAME_KEY_NEW_VERSION = "version";
		
		public static final String CONFIG_NAME_KEY_HUANGLI_LAST = "huang_li_last_error_time";

		//////////////////////////////////////////////////////////////////
		public static final String CONFIG_VALUE_PM_SOURCE_US = "us";
		public static final String CONFIG_VALUE_PM_SOURCE_GOV = "gov";
		
		//////////////////////////////////////////////////////////////////
//		public static final int		CONFIG_DEFAULT_SKIN_TAB		= 1;	// 插件最后一次选择，0:排行版; 1,已安装; 2,最新
//		
//		public static final boolean	CONFIG_DEFAULT_WEEK			= false;	// 周首日，true: 周日; false, 周一
//		public static final boolean	CONFIG_DEFAULT_SHENXIAO		= true;		// 万年历背景，true: 生肖; false, 数字
//		public static final int		CONFIG_DEFAULT_JIERI_PRIORITY = 0;		// 节日优先级，0:节气; 1,农历; 2,公历
//		public static final int     CONFIG_DEFAULT_SHOWSET_TYPE = 0;        //0：软件推荐   1：91软件  2：设置
//		public static final boolean	CONFIG_DEFAULT_DISPLAY_NL   = true;
//		public static final boolean	CONFIG_DEFAULT_DISPLAY_JQ	= true;
//		public static final boolean	CONFIG_DEFAULT_DISPLAY_GL	= true;
		
//		public static final int		CONFIG_DEFAULT_BK_TYPE      = ConfigHelper.VALUE_DEFALUT_STYLE;
		
		public static final boolean	CONFIG_DEFAULT_AUTOUPDATE	= true;
		public static final float	CONFIG_DEFAULT_TIEMUPDATE	= 1.0f;
		public static final int		CONFIG_DEFAULT_BEGINGTIEMEH	= 6;
		public static final int		CONFIG_DEFAULT_BEGINGTIEMEM	= 0;
		public static final int		CONFIG_DEFAULT_ENDTIEMEH	= 22;
		public static final int		CONFIG_DEFAULT_ENDTIEMEM	= 0;
		
		public static final String	CONFIG_DEFAULT_PM_SOURCE	= "us";
		
	}
	
	public static final class ProgramState
	{
		public static final String	STATE_NAME					= "ProgramState";
		public static final String  STATE_NAME_KEY_UPDATE_DATE	= "updateDate";
	}
	
//	public static final class IntentReqCode
//	{
//		public static final int SHOW_SUBACTIVITY  = 1;
//		public static final int SHOW_RESHACTIVITY = 2;
//		public static final int SELCET_IMAGE = 3;
//		public static final int INIT_WEATHER_DAY = 4;
//		public static final int INIT_CALENDAR_ATY = 5;
//		public static final int INIT_WEATHER_NORMAL = 6;	// 正常启动(带城市检查与帮助)
//	}	
}
