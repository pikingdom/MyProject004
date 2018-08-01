package com.nd.hilauncherdev.url;

import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;

/**
 * 桌面URL统一管理类
 * Created by dingdj on 2014/7/30.
 */
public class URLs {
	/**
	 * 百度(2015.10.16 caizp 修改为短链接)
	 */
	public final static String BROWSER_BAIDU_URL = "http://url.ifjing.com/iIBnQz";
	
	/**
	 * 淘宝 (2015.12.8 caizp V7.0.5及V7.0.6使用的短链接)
	 */
	public final static String SEM_BROWSER_BAIDU_URL = "http://url.ifjing.com/Q3E3Mj";
	
	/**
	 * 58同城(2016.1.19 caizp V7.1使用的短链接)
	 */
	public final static String V71_BROWSER_BAIDU_URL = "http://url.ifjing.com/jAVfy2";
	
	public final static String BROWSER_DEFULT_BAIDU_URL = "http://m.baidu.com/s?from=1006401p";
    /**
     * 桌面浏览器默认打开的地址
     */
    public final static String BROWSER_DEFAULT_URL = "http://s.ifjing.com?appid=100002&platform=4";


    //----------------------------------------PANDAHOME_BASE_URL(pandahome.ifjing.com)--------------------------------------------------------

    public static final String PANDAHOME_BASE_URL = "http://pandahome.ifjing.com/";

    /**
     * App分发统计接口
     */
    public static final String APP_DISPATCH_STATISTIC_ANALYSIS = PANDAHOME_BASE_URL + "action.ashx/distributeaction/5001";

    /**
     * 软件的下载地址
     */
    public static final String SOFT_DOWNLOAD_URL = PANDAHOME_BASE_URL + "soft.ashx/SoftUrl?mt=4&redirect=1&fwv=%s&packagename=%s";
    /**
     * 第三方应用程序下载地址 fwv = 手机固件 packagename = 下载apk包名 sjxh = 手机型号 fbl = 手机分辨率 imei
     * = IMEI号 mac = mac地址 vision = 软件版本 network = 上网方式 root = 是否root lan = 语言
     * account = 91通行证
     */
    public static final String THIRD_APPLICATION_DOWNLOAD_URL = PANDAHOME_BASE_URL + "soft.ashx/softurlV2?"
            + "mt=4&redirect=1&fwv=%s&packagename=%s&sjxh=%s&fbl=%s&imei=%s&mac=%s&vision=%s&network=%s&root=%s&lan=%s&account=%s";
    public static final String GET_CONFIG_URL = PANDAHOME_BASE_URL + "android/getdata.aspx?action=1001";

    /**
     * 桌面统一下载应用地址
     */
    public static final String UNIFIED_DOWNLOAD_PATH = PANDAHOME_BASE_URL + "soft/download.aspx?Identifier=%s&sp=%d";

    /**
     * 滤镜
     */
    public static final String STATIC_FILTER = PANDAHOME_BASE_URL + "android/getdata.aspx?action=5&mt=4&tfv=40000";
    /**
     * Dock推荐APP数据URL
     */
    public final static String URL_DOCK_RECOMMEND_APP = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=DockRecommendApp&ver=%s";
    /**
     * 定制版通用万能配置数据URL
     */
    public final static String URL_COMMON_SERVER_CONFIG = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=CommonServerConfigForDZ&ver=%s";
    /**
     * 除力天以外的定制版通用万能配置数据URL
     */
    public final static String URL_COMMON_SERVER_CONFIG_EXCEPT_FOR_LITIAN = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=CommonServerConfigExceptForLitian&ver=%s";
    /**
     * 充电锁屏开关配置信息的URL
     */
    public final static String URL_CHARGING_CONFIG = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=ChargingConfigForCustom&ver=";
    /**
    * 定制版下拉搜索X5开关万能配置数据URL
    */
    public final static String URL_CUSTOM_BRANCH_X5_SWITCH_URL = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=CustomX5Config&ver=%s";
    /**
     * Feed背景图
     */
    public final static String URL_FEED_BG = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=FeedBg";

    public static final String TEST_QUERY_URL = PANDAHOME_BASE_URL + "other.ashx/testquery?";
    public final static String REQ_DOWNLOAD_URL = PANDAHOME_BASE_URL + "Activity.ashx/Download";
    /**
     * 获取活动中心hot标识url
     */
    public final static String ACTIVITY_HOT_URL = PANDAHOME_BASE_URL + "Activity.ashx/HasEffectiveActivity?mt=4&imei=%s&divideversion=%s&supfirm=%s&supphone=%s&ProtocolVersion=%s";

    /**
     * 获取转盘资源URL
     */
    public final static String CONFIG_URL = PANDAHOME_BASE_URL + "Activity.ashx/WheelConfig?ProtocolVersion=%s&Mt=4&Themetype=40000&SupPhone=%s&SupFirm=%s&DivideVersion=0&SessionId=%s&Imei=%s&Imsi=%s";
    /**
     * 转盘结果URL
     */
    public final static String CIRCLE_URL = PANDAHOME_BASE_URL + "Activity.ashx/WheelResult?ProtocolVersion=%s&Mt=4&Themetype=40000&SupPhone=%s&SupFirm=%s&DivideVersion=0&SessionId=%s&Imei=%s&Imsi=%s&Ts=%s&Sign=%s";
    /**
     * 中了神秘礼物手机号上传URL
     */
    public final static String PHONE_NUM_URL = PANDAHOME_BASE_URL + "Activity.ashx/WheelNumber?ProtocolVersion=%s&Mt=4&Themetype=40000&DivideVersion=0&SupPhone=%s&SupFirm=%s&SessionId=%s&Imei=%s&Imsi=%s&Number=%s&Ts=%s&Sign=%s";
    /**
     * 分享取内容URL
     */
    public final static String SHARE_URL = PANDAHOME_BASE_URL + "Activity.ashx/ShareToWeibo?ProtocolVersion=%s&Mt=4&Themetype=40000&DivideVersion=0&SessionId=%s&Imei=%s&Imsi=%s&ShareId=%s&ShareType=%s&Msg=%s&Coin=%s&Unit=%s";
    /**
     * 主页面的分享
     */
    public final static String SHARE_MAIN_URL = PANDAHOME_BASE_URL + "Activity.ashx/ShareToWeibo?ProtocolVersion=%s&Mt=4&Themetype=40000&DivideVersion=0&SessionId=%s&Imei=%s&Imsi=%s&ShareId=%s&ShareType=%s";
    /**
     * 活动规则URL
     */
    public final static String RULE_URL = PANDAHOME_BASE_URL +"activity/WheelRule.html";
    /**
     * 成绩查看Url
     */
    public final static String SCORE_URL = PANDAHOME_BASE_URL +"activity/WheelHistory.aspx?ProtocolVersion=2.0&SessionId=%s&ActivityId=%s";
    /**
     * 神秘礼物URL
     */
    public final static String GIFT_URL = PANDAHOME_BASE_URL + "activity/WheelBigReward.aspx";

    /**
     * 上传服务器地址
     */
    public final static String SVR_URL = PANDAHOME_BASE_URL + "lightapp/tmpdata.aspx";

    public final static String PID = "20000036";
    /**
     * 91 事件热词, (通常用于桌面插件 上的热词展示)
     */
    public static final String URL_HOTWORD_91_EVENT = PANDAHOME_BASE_URL + "Soft.ashx/HotKey?mt=4&fwv=%s&imei=%s";
    public static final String EXP_CFG_URL = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=ExceptionCfg&ver=%s";
    public final static String NAVIGATION_URL = PANDAHOME_BASE_URL + "Soft.ashx/navv6?mt=4&pid="+PID+"&fwv=%s&imei=%s&pc=%s&iconpc=%s&vc=%s";
    public static final String PHONE_MANAGER_DOWNLOAD_URL = PANDAHOME_BASE_URL + "Soft.ashx/PhotoManagerUrl?mt=4&fwv=%s";

    /**
     * 服务端封装百度热词地址
     * 3.6.1之后全部切换到91地址
     */
    public static final String URL_HOTWORD_BAIDU_TO91 = PANDAHOME_BASE_URL + "theme.ashx/ResContent?act=15&Fid=15&Format=xml&mt=4&pid="+PID+"&lbl=pandahome2"+CUIDUtil.getCUIDPART();
    /**
     * 5.0.2后百度热词接口，增加启动桌面界面热词
     */
    public static final String URL_HOTWORD_BAIDU_TO91_FOR_V502 = PANDAHOME_BASE_URL + "Theme.ashx/BDHotKeyv3?mt=4&pid="+PID+"&tfv=40000"+CUIDUtil.getCUIDPART();
    /**
     * 推送接口
     */
    public static final String URL_PUSH_TO91 = PANDAHOME_BASE_URL + "android/getdata.aspx?mt=4&tfv=40000&action=2"+CUIDUtil.getCUIDPART()+"&pid=" + BaseConfig.PID;

    /**
     * 插件商城地址
     */
    public final static String widgetUrl = PANDAHOME_BASE_URL + "soft/widget.aspx?action=1&pid="+PID+"&mt=4&tfv=40000"+CUIDUtil.getCUIDPART();
    public final static String categoryUrl = PANDAHOME_BASE_URL + "soft/widget.aspx?action=2&pid="+PID+"&mt=4&tfv=40000"+CUIDUtil.getCUIDPART();
    public final static String newestWidgetUrl = PANDAHOME_BASE_URL + "soft/widget.aspx?action=3&pid=5&mt=4&tfv=40000"+CUIDUtil.getCUIDPART();// &identifier=com.sds.android.ttpod|com.yoka.magazine;
    public static final String downloadUrl = PANDAHOME_BASE_URL + "soft/widget.aspx?action=4"+"&mt=4&tfv=40000&pid="+PID+"&identifier=";// =包名
    /**
     * 请求的Url 192.168.254.69:803
     */
    public static String url = PANDAHOME_BASE_URL + "other.ashx/resUrl?mt=4&fwv=40000&type=%s&pc=%s&imei=%s&imsi=%s";
    /**
     * dataType--0下发Icon推荐跟主题推荐；1下发icon推荐；2下发主题推荐；3下发游戏主题推荐；默认0 iconPage--Icon请求页，默认1
     * themePage--主题请求页，默认1 identifier--主题名称，不传下发默认推荐
     */
    public static String launcherRecommendRequestPath = PANDAHOME_BASE_URL +"Android/GetData.aspx?action="+PID+"&sp=9&dataType=1&iconPage=%d";
    public static String themeRecommendRequestPath = PANDAHOME_BASE_URL +"Android/GetData.aspx?action="+PID+"&dataType=2&themePage=%d&identifier=%s";
    public static String themeRecommendRequestPathSeries = PANDAHOME_BASE_URL +"Android/GetData.aspx?action="+PID+"&dataType=2&themePage=%d&themeid=%s";
    public static String gameThemeRecommendAppRequestPath = PANDAHOME_BASE_URL +"Android/GetData.aspx?action="+PID+"&dataType=3&sp=31&themeid=%s";


    public final static String URL_H7N9_DATA_SRC = PANDAHOME_BASE_URL + "Other.ashx/DiseaseMsg?mt=4&tfv=40000&imei=%s&DivideVersion=3.6&SupFirm=%s&SupPhone=%s";
    //	public final static String URL_H7N9_DATA_SRC = "http://192.168.254.69:803/Other.ashx/DiseaseMsg?mt=4&tfv=40000&imei=%s&DivideVersion=3.6&SupFirm=%s&SupPhone=%s";



    //----------------------------------------百宝箱 BBX2_BASE_URL------------------------------------------------------


    public final static String BBX2_BASE_URL = "http://bbx2.ifjing.com/";

    /**
     * 铃声热词
     */
    public final static String HOTKEYWORD_URL = BBX2_BASE_URL + "ring.ashx?act=104&mt=4&iv=5&pid="+PID+"";

    /**
     * 壁纸热词
     */
    public static final String STATIC_WALLPAPER_HOTWORD_URL = BBX2_BASE_URL + "pic.ashx?act=104&mt=4&iv=5";
    /**
     * 壁纸搜索
     */
    public static final String STATIC_WALLPAPER_SEARCH_URL = BBX2_BASE_URL + "pic/pandahome/list.aspx?act=414&iv=5&dst=0&mt=4&keyword=%s";


    /**
     * 铃声相关
     */
    public final static String BBX2_RING_BASE_URL = BBX2_BASE_URL + "ring/pandahome/";

    /**
     * 分类
     */
    public final static String CLASSIFY_URL = BBX2_RING_BASE_URL + "cat.aspx?mt=4&act=513&iv=5&pid="+PID+"";
    /**
     * 精品
     */
    public final static String MONTH_HOT_URL = BBX2_RING_BASE_URL + "list.aspx?mt=4&act=512&iv=5&pid="+PID+"";
    /**
     * 最新
     */
    public final static String NEWEST_URL = BBX2_RING_BASE_URL + "list.aspx?mt=4&act=510&iv=5&pid="+PID+"";

    /**
     * 铃声搜索
     */
    public final static String SEARCH_URL = BBX2_RING_BASE_URL + "list.aspx?act=514&iv=5&keyword=%s&mt=4";

    /**
     * 铃声排行
     */
    public final static String RINGRANK_URL = BBX2_RING_BASE_URL + "list.aspx?act=511&iv=5&mt=4&pid="+PID+"";

    /**
     * 专题主界面的地址
     */
    // public static final String
    // SUBJECT_TEMP_DNS="http://192.168.254.47:9292/";
    // public static final String SUBJECT_TEMP_DNS="http://bbx2.sj.91.com/";
    public static final String SUBJECT_MAIN_URL = BBX2_BASE_URL + "soft/DeskTop/tag.aspx?" + "act=%1$s" + // 操作标识
            "&iv=%2$s" + // 3:91桌面专题列表
            "&mt=%3$s" + // 平台标识android ,iphone
            "&pid=%4$s" + // 产品ID，桌面是6
            "&imei=%5$s" + // IME号
            "&sv=%6$s" + // 客户端版本号
            "&osv=%7$s" + // Android版本号
            "&nt=%8$s" + // 网络类型
            "&chl=%9$s" + // 渠道ID
            "&take=1";// 单专题标识

    /**
     * 一键装机软件列表地址
     */
    public static final String BASE_APP_LIST_URL = BBX2_BASE_URL + "softs.ashx?act=%1$s" + // 操作标识
            "&mt=%2$s" + // 平台标识android ,iphone
            "&osv=%3$s" + // Android版本号
            "&resType=%4$s" + // 资源类别
            "&pid=%5$s" + // 产品ID，桌面是6
            "&imei=%6$s" + // IME号
            "&sv=%7$s" + // 客户端版本号
            "&nt=%8$s" + // 网络类型
            "&iv=%9$s" + // 接口版本
            "&chl=%10$s"; // 渠道ID


    /**
     * 91热门应用、游戏   热词源地址 （通常用于热词云视图）
     * 因为应用要能够显示图标，所以iv从3转换到5 2015-3-4
     */
    public static final String URL_HOTWORD_91 = BBX2_BASE_URL + "softs.ashx?act=104&osv=2.3.3&iv=5&start=%s";
    /**
     * 百度热词接口推荐APP详情界面
     */
    public static final String URL_HOTWORD_APP_DETAIL = BBX2_BASE_URL + "soft/phone/detail.aspx?act=226&iv=2&pid="+PID+"&identifier=%s&mt=4&osv=%s";


    //----------------------------------------百宝箱 BBX_DATA_BASE_URL--------------------------------------------------------------

    public static final String BBX_DATA_BASE_URL = "http://bbxdata.ifjing.com/";
    /**
     * 下载成功反馈的接口地址
     */
    public static final String DOWNLOAD_SUCCESS_FEEDBACK_URL = BBX_DATA_BASE_URL + "stat.ashx?act=%1$s" + "&resId=%2$s" + "&resType=%3$s"
            + "&mt=%4$s" + "&statType=%5$s" + "&chl=%6$s" + "&pid=%7$s" + "&imei=%8$s" + "&sv=%9$s" + "&nt=%10$s";

    /**
     * 下载成功后统计
     */
    public final static String DOWNLOAD_SUCCESS_URL = BBX_DATA_BASE_URL + "stat.ashx?mt=4&act=101&resid=%s&restype=3&pid="+PID+"&stattype=4";


    /**
     * 国内主机
     */
    public static final String HOST_ZH = "pandahome.ifjing.com";
    /**
     * 国外主机
     */
    public static final String HOST_EN = "bbx.pandaapp.com";

    /**
     * 静态壁纸列表的加载地址(推荐、最新、最热)(mo=0 不过滤 mo=1 为收费 mo=2 免费)
     */
    public static final String STATIC_WALLPAPER_FETCH_URL = "http://%s/Pic.ashx?mt=4&Resolution=960*800&st=%s&mo=2&pi=%s&pagesize=%s";
    /**
     * 分类列表地址
     */
    public static final String STATIC_WALLPAPER_CLASSIFY_URL = "http://%s/pic.ashx/CateListInfo?mt=4&resolution=960*800&imei=xxxx&projectoption=2000";
    /**
     * 分类下的壁纸地址
     */
    public static final String STATIC_WALLPAPER_CLASSIFY_DETAIL_URL = "http://%s/pic.ashx?mt=4&resolution=960*800&projectsource=2000&imei=xxxx&cateid=%s&pi=%s&pagesize=%s";



    //----------------------------------------------其它链接-----------------------------------------------------------------

    /**
     * 分享图片背景图的网络地址
     */
//    public final static String URL_SHARE_PICTURE_BG = "http://da.91rb.com/android/soft/2012/tmp/launcher_menu_share_thumb_bg.9.png";

// --Commented out by Inspection START (2015/12/10 14:08):
//    /**
//     * 双十二活动地址(通知栏消息点击)
//     */
//    public final static String URL_DOWBLE_TWELVE_COMPAIGN_NOTIFI = "http://url.91.com/iEn2Eb?mt=4&v=%s&pid="+PID+"";
// --Commented out by Inspection STOP (2015/12/10 14:08)
// --Commented out by Inspection START (2015/12/10 14:08):
//    /**
//     * 双十二活动地址(桌面图标点击)
//     */
//    public final static String URL_DOWBLE_TWELVE_COMPAIGN_ICON = "http://url.91.com/fuauIj?mt=4&v=%s&pid="+PID+"";
// --Commented out by Inspection STOP (2015/12/10 14:08)

    /**
     * 宜搜热词源地址
     */
    public static final String URL_HOTWORD_EASOU = "http://ad2.easou.com:8080/j10ad/hot.jsp?cid=hif1140_10741_001&size=10000";

    /**
     * 91搜索地址信息, 其参数信息详见: <br/>
     * <a href='http://ressearch.sj.91.com/t.aspx'>http://ressearch.sj.91.com/t.aspx</a>
     */
    public static final String URL_91_SEARCH = "http://ressearch.ifjing.com/service.ashx?platform=android&proj=1900&act=203&fee=2&fw=%s&keyword=%s&imei=%s";
    /**
     * 91搜索地址信息(英文接口)
     */
    public static final String ONLINE_SEARCH_EN_URL = "http://ressearch.moborobo.com/service.ashx?platform=android&proj=1900&act=203&fee=2&fw=%s&keyword=%s&imei=%s";

    //	private final static String UPLOAD_URL = "http://192.168.9.183:8011/search.ashx"; // 测试地址
    public final static String UPLOAD_URL = "http://dataservice.ifjing.com/search.ashx";

// --Commented out by Inspection START (2015/12/10 14:08):
//    //	private final static String NAVIGATION_URL = "http://192.168.254.69:803/Soft.ashx/navv5?mt=4&pid="+PID+"&fwv=%s&imei=%s&pc=%s&iconpc=%s";
//    public final static String NAVIGATION_URL_EN = "http://bbx.pandaapp.com/soft.ashx/nav?mt=4&tfv=40000&pc=%s&vc=%s";// 海外版
// --Commented out by Inspection STOP (2015/12/10 14:08)

    // --Commented out by Inspection (2015/12/10 14:08):public static final String ASSIT_APP_DOWNLOAD_URL = "http://dl.sj.91.com/business/91soft/91assistant_Andphone167.apk";
    public static final String LAUNCHER_NAMESPACE = "http://schemas.android.com/apk/res/com.nd.android.pandahome2";

    /**
     * 新评论接口服务器
     */
    public static final String HOST_Comment_Inland_Server = "http://comment.ifjing.com";
    public static final String HOST_Comment_Foreign_Server = "http://comment.moborobo.com";
// --Commented out by Inspection START (2015/12/10 14:08):
//    /**
//     * 百度热词源地址
//     */
//    public static final String URL_HOTWORD_BAIDU = "http://top.baidu.com/none/top10.xml?from=1429e";
// --Commented out by Inspection STOP (2015/12/10 14:08)


    /**
     * Google搜索(国内)
     */
    public static final String GOOGLE_ZH_SEARCH_URL = "http://www.google.com.hk/search?q=%s";
    /**
     * Google搜索(国外)
     */
    public static final String GOOGLE_EN_SEARCH_URL = "http://www.google.com/search?q=%s";
    /**
     * 淘宝搜索
     */
    public static final String TAOBAO_SEARCH_URL = "http://r.m.taobao.com/s?p=mm_32510800_3292991_10741815&q=%s";
    /**
     * 宜搜搜索
     */
    public static final String EASOU_SEARCH_URL = "http://ad2.easou.com:8080/j10ad/ea2.jsp?channel=11&cid=hif1140_10741_001&q=%s";


    public final static String URL_MORE_DETAIL = "http://www.chinacdc.cn/jkzt/crb/rgrgzbxqlg_5295/rgrqlgyp/";
    /**
     * 搜索
     */
    public final static String URL_TAOBAO_SEARCH = "http://pandahome.ifjing.com/android/TbSearchPlugin.aspx?searchKey=%s";
    /**
     * 冲值
     */
    public final static String URL_TAOBAO_CHARGE = "http://r.m.taobao.com/zc?p=mm_32510800_3412911_11019770";
    /**
     * 彩票
     */
    public final static String URL_TAOBAO_LOTTERY = "http://caipiao.m.taobao.com/?ttid=52zsh003";


    // --Commented out by Inspection (2015/12/10 14:08):public final static String NAVIGATION_SITE_ICON_URL = "http://dl.sj.91.com/business/91soft/";

    /**
     * 桌面下载地址
     */
//    public static String URL_LAUNCHER_DOWNLOAD = "http://da.91rb.com/pandahome/91pandahome_android.apk";
    // 提交的接口
    public static String URL_USER_FEED_BACK = "http://feedback.ifjing.com/GetResourceData.aspx";
    // 查看客服反馈的接口
    public static String URL_USER_FEED_REPLY_BACK = "http://feedback.ifjing.com/FeedBackList.aspx";

// --Commented out by Inspection START (2015/12/10 14:08):
//    /**
//     * 获取第三方合作app列表接口
//     */
//    public static final String API_GET_THIRDPART_APP = "http://%s/data/recommendsofts.xml";
// --Commented out by Inspection STOP (2015/12/10 14:08)

    /**
     * 卸载桌面打开推荐应用网页
     */
    public static final String UNINSTALL_PANDHOME_OPEN_PAGE = "http://pandahome.ifjing.com/action.ashx/unload?mt=4&pid="+PID+"&cuid=%s&cycleinfo=%s&productinfo=%s";
    
    /**
     * 异常上报接口
     */
    public static final String EXCEPTION_UPLOAD_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/4";

    /**
     * 动态插件升级配置接口
     */
    public static final String DYN_PLUGIN_UPGRADE_URL = PANDAHOME_BASE_URL + "action.ashx/distributeaction/5016";

    /**
     * 定制版动态插件升级配置接口
     */
    public final static String DYN_PLUGIN_UPGRADE_URL_FOR_DZ = PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=DynamicPluginInfoForDZ&ver=";


    /**
     * 用户档案上报接口
     */
    public static final String USER_ARCHIVES_UPLOAD_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/5";
    
    /**
     * 获取后台配置的参数
     */
    public static final String GET_BACKSTAGE_PARAM_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/6";

    /**###########################################  Mobo海外接口  ##################################################**************/

    public static final String MOBO_BASE_URL = "http://pandahome.sj.91launcher.com/";
    /*************Mobo随天气工程移植过来 by wcz 2016年3月11日 14:51:34    开始**************/
    /**
     * otheraction接口,Mobo天气
     */
    public static final String 	OTHER_ACTION_URL =   MOBO_BASE_URL+ "action.ashx/otheraction/";

    /**
     * distributeaction请求接口
     */
    public static final String DISTRIBUTE_ACTION_URL = MOBO_BASE_URL+ "action.ashx/distributeaction/";

    /*************Mobo随天气工程移植过来 by wcz 2016年3月11日 14:51:34   结束**************/

}
