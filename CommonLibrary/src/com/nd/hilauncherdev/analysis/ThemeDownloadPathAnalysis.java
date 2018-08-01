package com.nd.hilauncherdev.analysis;

import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.net.ThemeHttpCommon;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 主题下载路径统计
 * <p>Title: ThemeDownloadPathAnalysis</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2014年12月22日
 */
public class ThemeDownloadPathAnalysis {
	public static int SP_UNKNOWN = 99;//未知来源
	public static int SP_UPDATE = 1;//更新
	public static int SP_TOP_BANNER = 2;//顶部banner
	//public static int SP_CHOICE_COLLECTION = 3;//精选合集
	public static int SP_91_KIBUM = 4;//91人气王
	public static int SP_NEW = 5;//最新
	public static int SP_RANKING_FREE = 6;//排行-免费
	public static int SP_RANKING_HITS = 7;//排行-人气
	public static int SP_RANKING_HOT = 8;//排行-热卖
	public static int SP_HOBBY = 9;//爱好
	public static int SP_TRADITION = 10;//传统
	public static int SP_STYLE = 11;//风格
	public static int SP_MOOD = 12;//心情
	public static int SP_COLOUR = 13;//颜色
	public static int SP_SEARCH = 14;//搜索
	public static int SP_HOT_WORD = 15;//热词
	public static int SP_IMAGE = 16;//形象
	public static int SP_COLLECT = 17;//收藏
	public static int SP_RESOURCE_LOSE_RELOAD = 18;//SD卡主题资源丢失，重新下载
	public static int SP_GUIDE_PAGE_RECOMMEND_DOWNLOAD = 19;//欢迎页面推荐下载
	//public static int SP_SHOP_GUIDE_PAGE_RECOMMEND_DOWNLOAD = 20;//商城欢迎页推荐爱下载(由服务端下发action统计参数)
	public static int SP_THEME_PROBATION_BUY = 21;//主题试用后购买
	public static int SP_COMPAIGN_DIRECT_DOWNLOAD_THEME = 22;//运营活动直接下载主题
	public static int SP_THEME_SERIES = 23;//下载主题系列时的主题
	//public static int SP_THEME_CAMPUS_ACTIVITIES_THEME_DOWNLOAD = 24;//校园活动主题下载
	public static int SP_V67_TOP_BANNER = 25;//V6.7首页改版后顶部Banner主题下载
	public static int SP_V67_DIALY_BANNER = 26;//V6.7首页改版后每日推荐下方Banner主题下载
	//V7 个性化-主题分类 start
	public static int SP_INDIVIDUATION = 27;//v7.0 主题分类-个性炫酷
	public static int SP_CARTOONS = 28;//v7.0 主题分类-卡通萌物
	public static int SP_ROMANCES = 29;//v7.0 主题分类-浪漫爱情
	public static int SP_CHARMING = 30;//v7.0 主题分类-男神女神
	public static int SP_ANIANDGAME = 31;//v7.0 主题分类-动漫游戏
	public static int SP_ARTS = 32;//v7.0 主题分类-手绘艺术
	public static int SP_SIMPLICITY = 33;//v7.0 主题分类-清新简约
	public static int SP_NATURE = 34;//v7.0 主题分类-风景静物
	public static int SP_CARSSPORTS = 35;//v7.0 主题分类-名车运动
	public static int SP_ANTIQUE = 36;//v7.0 主题分类-中国风/古风古韵
	public static int SP_DIY = 37;//v7.0 主题分类-主题DIY
	public static int SP_ONE_HOUR_CARD = 38;//V7.1.1 一小时卡片入口下载主题
	public static int SP_DOWNLOADING_CARD = 39;//V7.1.1 正在下载卡片入口下载主题
	public static int SP_TOP_MENU_BOUTIQUE_THEME = 40;//v7.5下滑发现精品主题
//	public static int SP_YOUBAO_THEME_AREA = 41;//v7.5.2有宝主题专区
	public static int SP_RECOMMEND_CUSTOM_THEME_SERIES = 42;//v7.5.5下载天天荐主题系列
	//V7 个性化-主题分类 end

	/**
	 * 统计SP全局变量
	 * 每次进入一个路径开端的时候赋值
	 */
	private static int SP = SP_UNKNOWN;
	public static void setThemeDownloadPathAnalysisSP(int AnalysisSP){
		ThemeDownloadPathAnalysis.SP = AnalysisSP;
	}
	public static int getThemeDownloadPathAnalysisSP(){
		return SP;
	}

	/**
	 * 数值和4041接口有对应关系
	 */
	public static int FLAG_DOWNLOAD_THEME = 1;//开始下载
	public static int FLAG_DOWNLOAD_SUCCESS = 4;//下载成功
	public static int FLAG_INSTALL_SUCCESS = 5;//安装成功
	public static int FLAG_INSTALL_FAIL = 6;//安装失败
	public static int FLAG_DOWNLOAD_FAIL = 8;//下载失败
	public static int FLAG_CLICK_PAY = 10;//点击付费
	public static int FLAG_PAY_SUCCESS = 14;//付费成功
	public static int FLAG_PAY_FAIL = 15;//付费失败
	
	/**
	 * 资源类型(主题=2)
	 */
	public final static int RES_TYPE_THEME = 2;
	/**
	 * 资源类型(编辑推荐主题系列=36)
	 */
	public final static int RES_TYPE_THEME_SERIES = 36;
	/**
	 * 资源类型(用户推荐主题系列=61)
	 */
	public final static int RES_TYPE_RECOMMEND_CUSTOM_THEME_SERIES = 61;
	/**
	 * 资源类型(风格=62)
	 */
	public final static int RES_TYPE_STYLE = 62;
	/**
	 * 资源类型(单个祠壁纸=71)
	 */
	public final static int RES_TYPE_VIDEO_PAPER = 71;
	
	/**
	 * 4041统计主题属性(免费主题不传，服务端默认为0)
	 */
	public static int RES_ATTRIBUTES_FREE = 0;
	/**
	 * 4041统计主题属性(彩虹主题)
	 */
	public static int RES_ATTRIBUTES_RAINBOW = 1;
	/**
	 * 4041统计主题属性(付费主题)
	 */
	public static int RES_ATTRIBUTES_CHARGE = 2;
	/**
	 * 4041统计主题属性(试用主题)
	 */
	public static int RES_ATTRIBUTES_TRIAL = 4;

	private static String THEME_DISTRIBUTE_ANALYSIS_PATH = "http://pandahome.ifjing.com/action.ashx/ThemeAction/";

	/**
	 * 统计的PostionType
	 */
	public static int POSTION_TYPE_DEFAULT = 0;//默认值
	public static int POSTION_TYPE_SPECIAL = 1;//专辑
	/*public static int POSTION_TYPE_CLASSIFY = 2;//分类
	public static int POSTION_TYPE_RECOMMENDED_TOPICS = 3;//推荐专题*/
	public static int POSTION_TYPE_V8_PERSONAL_TAG = 4;//V8个性化标签
	private static int postionType = POSTION_TYPE_DEFAULT;
	public static void setThemeDownloadPathAnalysisPostionType(int postionType){
		ThemeDownloadPathAnalysis.postionType = postionType;
	}
	public static int getThemeDownloadPathAnalysisPostionType(){
		return postionType;
	}
	/**
	 * 统计的PostionTypeId,用来传递专辑/分类/推荐专题的id
	 */
	private static int postionTypeId = POSTION_TYPE_DEFAULT;
	public static void setThemeDownloadPathAnalysisPostionTypeId(int postionTypeId){
		ThemeDownloadPathAnalysis.postionTypeId = postionTypeId;
	}
	public static int getThemeDownloadPathAnalysisPostionTypeId(){
		return postionTypeId;
	}
	/**
	 * 发送主题下载统计sp
	 * <p>Title: sendThemeDownloadPathAnalysisSP</p>
	 * <p>Description: </p>
	 * serThemeId 服务端下发的原始id
	 * StatType 当前状态类型
	 * resAttributes 主题属性 0-免费主题(客户端不传，服务端默认为0); 1-彩虹主题; 2-付费主题; 4试用主题
	 * @author maolinnan_350804
	 */
	public static void sendThemeDownloadPathAnalysisSP(final String serThemeId,final String themeSp,final int StatType,final int postionType,final int postionTypeId, final int resAttributes){
		sendThemeDownloadPathAnalysisSP(serThemeId, ThemeDownloadPathAnalysis.RES_TYPE_THEME, themeSp, StatType, postionType, postionTypeId, resAttributes);
	}

	/**
	 * 发送主题下载统计sp
	 * <p>Title: sendThemeDownloadPathAnalysisSP</p>
	 * <p>Description: </p>
	 * serThemeId 服务端下发的原始id
	 * StatType 当前状态类型
	 * resAttributes 主题属性 0-免费主题(客户端不传，服务端默认为0); 1-彩虹主题; 2-付费主题; 4试用主题
	 * @author maolinnan_350804
	 */
	public static void sendThemeDownloadPathAnalysisSP(final String serThemeId, final int resType,final String themeSp, final int StatType,final int postionType,final int postionTypeId, final int resAttributes){
//		Log.e("个性化资源统计", "当前统计主题id="+serThemeId+";统计SP="+themeSp+";统计类型="+StatType);
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				int acitonCode = 4041;
				String jsonParams = "";
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("ResId", Long.parseLong(serThemeId));
					jsonObject.put("ResType", resType);
					jsonObject.put("Position", Integer.parseInt(themeSp));
					jsonObject.put("StatType", StatType);
					if(RES_ATTRIBUTES_FREE != resAttributes) {
						jsonObject.put("ResAttributes", resAttributes);
					}
					if(postionType != POSTION_TYPE_DEFAULT && postionTypeId != POSTION_TYPE_DEFAULT){
						jsonObject.put("PostionType", postionType);
						jsonObject.put("PostionTypeID", postionTypeId);
					}
					jsonParams = jsonObject.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				ThemeHttpCommon.addGlobalRequestValue(paramsMap, CommonGlobal.getApplicationContext(), jsonParams);
				ThemeHttpCommon httpCommon = new ThemeHttpCommon(THEME_DISTRIBUTE_ANALYSIS_PATH + acitonCode);
				httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
//				if(csResult.getResultCode() == 0){
//					Log.e("个性化资源统计", "该统计已经向服务端发送");
//				}
			}
		});
	}

	/**
	 * 发送主题下载统计sp,专门用来统计下载失败的
	 * <p>Title: sendThemeDownloadPathAnalysisSP</p>
	 * <p>Description: </p>
	 * @param serThemeId
	 * @param themeSp
	 * @param downloadFaileUrl		下载地址
	 * @param networkState			网络状态
	 * @author maolinnan_350804
	 */
	public static void sendThemeDownloadPathAnalysisSP(final String serThemeId,final String themeSp,final String downloadFaileUrl,final String netinfo,final int postionType,final int postionTypeId, final int resAttributes){
		sendThemeDownloadPathAnalysisSP(serThemeId, 2, themeSp, downloadFaileUrl, netinfo, postionType, postionTypeId, resAttributes);
	}

	/**
	 * 发送主题下载统计sp,专门用来统计下载失败的
	 * <p>Title: sendThemeDownloadPathAnalysisSP</p>
	 * <p>Description: </p>
	 * @param resType 资源类型，普通主题为2，主题系列为36
	 * @param serThemeId
	 * @param themeSp
	 * @param downloadFaileUrl		下载地址
	 * @param networkState			网络状态
	 * @author maolinnan_350804
	 */
	public static void sendThemeDownloadPathAnalysisSP(final String serThemeId, final int resType, final String themeSp,final String downloadFaileUrl,final String netinfo,final int postionType,final int postionTypeId, final int resAttributes){
//		Log.e("个性化资源统计", "当前统计主题id="+serThemeId+";统计SP="+themeSp+";统计类型="+StatType);
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				int acitonCode = 4041;
				String jsonParams = "";
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("ResId", Long.parseLong(serThemeId));
					jsonObject.put("ResType", resType);
					jsonObject.put("Position", Integer.parseInt(themeSp));
					jsonObject.put("StatType", FLAG_DOWNLOAD_FAIL);
					jsonObject.put("downloadurl", encodeAttrValue(downloadFaileUrl));
					jsonObject.put("netinfo", encodeAttrValue(netinfo));
					if(RES_ATTRIBUTES_FREE != resAttributes) {
						jsonObject.put("ResAttributes", resAttributes);
					}
					if(postionType != POSTION_TYPE_DEFAULT && postionTypeId != POSTION_TYPE_DEFAULT){
						jsonObject.put("PostionType", postionType);
						jsonObject.put("PostionTypeID", postionTypeId);
					}
					jsonParams = jsonObject.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				ThemeHttpCommon.addGlobalRequestValue(paramsMap, CommonGlobal.getApplicationContext(), jsonParams);
				ThemeHttpCommon httpCommon = new ThemeHttpCommon(THEME_DISTRIBUTE_ANALYSIS_PATH + acitonCode);
				httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
//				if(csResult.getResultCode() == 0){
//					Log.e("个性化资源统计", "该统计已经向服务端发送");
//				}
			}
		});
	}

	/**
	 * 对参数进行转码
	 * <p>Title: encodeAttrValue</p>
	 * <p>Description: </p>
	 * @param value
	 * @return
	 * @author maolinnan_350804
	 */
	private static String encodeAttrValue(String value){
		String returnValue = "";
		try {
			value = URLEncoder.encode(value+"", "UTF-8");
			returnValue = value.replaceAll("\\+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
}
