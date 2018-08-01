package com.nd.hilauncherdev.kitset.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.xmlparser.ElementX;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;

/**
 * 渠道工具类
 */
public class ChannelUtil {
	//=======================海外渠道类型==================//
	/**
	 * GP渠道类型
	 */
	public static final int GP_CHANNEL = 1;

	/**
	 * MOBOMARKET渠道类型
	 */
	public static final int MOBOMARKET_CHANNEL = 2;
	/**
	 * ML国内渠道类型 (v6.1.6.1新增，暂不使用)
	 */
	public static final int ML_CHANNEL=3;
	/**
	 * GP定制渠道类型
	 */
	public static final int CUSTOMIZE_GP_CHANNEL = 4;
	/**
	 * 渠道类型
	 */
	public static final int CHANNEL_TYPE = MOBOMARKET_CHANNEL;

	/**
	 * 跳转方式下载 打开网页的方式
	 */
	public static final int JUMP_DOWOLOAD = 1;
	/**
	 * 直接下载的方式 走下载管理
	 */
	public static final int DIRECT_DOWOLOAD = 2;
	//=======================海外渠道类型==================//

	/**
	 * 是否是海外版.改海外版时，需要把该值改为true
	 */
	public static final boolean isGooglePlayVersion = false;
    /**
     * 安卓市场渠道ID
     */
    public static final String HIAPK_CHANNEL_ID = "1010311d";
    /**
     * 91助手渠道ID
     */
    public static final String ASSIST_91_CHANNEL_ID = "1010311b";
    /**
     * 百度渠道ID
     */
    public static final String BAIDU_CHANNEL_ID = "1010312c";

	
	/**
	 * -1 未初始化
	 * 0正常包
	 */
	static int isChannelPck = -1;
	
	/**
	 * -1 未初始化
	 * 0正常包
	 */
	static int isPayPck = -1;
	
	public static final String ND_CHANNELID_XML = "NdChannelId.xml";
	/**
	 * 是否是渠道包的标识，只要assets目录下有这个文件名的文件就表示这是个渠道包
	 */
	public static final String IS_CHANNEL_PKG_FLAG_FILE = "helloplay";
	/**
	 * 是否是渠道包的标识，只要assets目录下有这个文件名的文件就表示这是个渠道包
	 */
	public static final String IS_PAY = "hellotheme";
	
	private static boolean isNoPay = false;
	
	
	/**
	 * 
	 * 是否为定制版
	 * isCustomVersions 为true时 其他定制版控制参数才有效
	 */
	private static boolean isCustomVersions = false;
	// 是否三无包
	public static boolean isChannel = false;
	// 是否阿里云包（需在三无包基础上去除应用商店和热门游戏）
	public static boolean isAliYun = false;
	
	//兼容安智，默认关闭，需要三无
	private static boolean isAnzhi = false;
	
	//中国移动MM
	private static boolean isYiDongMM = false;
	
	//是否为定制应用推荐
	private static boolean isRecommendAPP = false;
	
	//vivo定制
	private static boolean isCustomVivo = false;

	//是否安装ROM
	private static boolean isInstallROM = false;
	
	/**
	 * 是否关闭智能升级
	 */
	public static final boolean closeSmartUpdate = false;
	
	
	
	/**
	 * 各种定制版的类型标识
	 */
	
	/**
	 * 三无包
	 */
	public static final int CUSTOM_CHANNEL = 0;
	/**
	 * 机锋市场
	 */
	public static final int CUSTOM_GFAN = 1;
	/**
	 * 安智
	 */
	public static final int CUSTOM_ANZHI = 2;
	/**
	 * 豌豆荚
	 */
	public static final int CUSTOM_WANDOUJIA = 3;
	/**
	 * 应用推荐
	 */
	public static final int CUSTOM_RECOMMENDAPP = 4;
	/**
	 * VIVO
	 */
	public static final int CUSTOM_VIVO = 5;
	/**
	 * rom版本 统计渠道
	 */
	public static final int CUSTOM_ROM = 6;
	/**
	 *  渠道包
	 */
	public static final int CUSTOM_CHANNELPACKAGE = 7;
	/**
	 * 采用特殊统计的定制版
	 */
	public static final int CUSTOM_SEPECIALCHANNEL = 8;
	/**
	 * 关闭智能升级包
	 */
	public static final int CUSTOM_SMART_UPDATE = 9;

	
	/**
	 * 解决某些定制版,在升级到主版本后,不采用主版本中的渠道标识进行相关统计,
	 * 而是采用原先定制版本中所配置的渠道标识
	 * isSepecialChannelPackage 是否为某些采用特殊统计的定制版
	 * sepecialChannelPackageIdSpKey 用于保存某些采用特殊统计的定制版本的渠道标识
	 * 
	 * @author wangguomei,2013.11.22
	 */
	private static boolean isSepecialChannelPackage = false;
	public static String  sepecialChannelPackageIdSpKey = "sepecial_channel_id";

	
	public static final int SANWUCHANNEL = 0;
	
	public static final int ASSISTCHANNEL_91 = 1;
	
	public static final int HIAPKCHANNEL = 2;
	
	public static final int OTHERCHANNEL = 3;

	/*
	 * SEM渠道ID
	 */
	public static boolean isSemChannel = false;
	private static final String SEM_THEME_CHN_ID = "1010596s";
    private static final String SEM_WALLPAPER_CHN_ID = "1010596p";
    private static final String SEM_RING_CHN_ID = "1010596y";
    private static final String SEM_LOCK_CHN_ID = "1010596t";
    private static String SEM_CHN_ID = null;
    //是否QQ定制渠道
    public static boolean isQQChannel = false;
	//应用宝渠道ID
	private static final String YINGYONGBAO_CHN_ID = "1010631a";
    
	/**
	 * 是否定制版，升级用户都不会被当做定制版用户
	 * @return
	 */
    private static boolean isCustomVersion(){
		if(!isCustomVersions)
			return false;
		
		int installedVersion = BaseConfigPreferences.getInstance().getVersionCodeFrom();
		int curVersion = TelephoneUtil.getVersionCode(BaseConfig.getApplicationContext());
		if(installedVersion == curVersion){//新用户
			return isCustomVersions;
		}else{
			return false;
		}
	}
	
	/**
	 * 所有定制版都通过此函数来判断
	 * @param customtype 定制版类型
	 * @return
	 */
	public static boolean getIsCustomByType(int customtype){
		// isCustomVersion 为是否定制版的总开关
		boolean isCustomVersion = isCustomVersion();
		if (isCustomVersion){
			switch(customtype){
			
			case CUSTOM_CHANNEL:
				return isChannel;
			case CUSTOM_ANZHI:
				return isAnzhi;
			case CUSTOM_RECOMMENDAPP:
				return isRecommendAPP;
			case CUSTOM_VIVO:
				return isCustomVivo;
			case CUSTOM_ROM:
				return isInstallROM;
			case CUSTOM_CHANNELPACKAGE:
				if (isAnzhi) {
					return true;
				}
				return isChannel;
			case CUSTOM_SEPECIALCHANNEL:
				return isSepecialChannelPackage;
			case CUSTOM_SMART_UPDATE:
				return closeSmartUpdate;
			default :
				return false;
			}
		}
		return isCustomVersion;
		
	}
	
	
	/**
	 * 获取渠道ID
	 * @param context
	 * @return String
	 */
	public static String getChannel(Context context) {
		return HiAnalytics.getChannel(context);
	}
	
	/**
	 * 只体供给: 特殊定制分支包在渠道统计初始化时获取渠道ID,除此之外,
	 * 桌面所有获取渠道ID统一调用ChannelUtil.getChannel(Context context)
	 * @author wangguomei
	 * @param context
	 * @return String
	 */
	public static String getChannelOnlyForSpecialLauncherDoAnalyticInit(Context context) {
		String channelid = "";
		InputStream is = null;
		try {
			is = context.getAssets().open(ND_CHANNELID_XML);
			byte[] b = new byte[256];
			int lenght = is.read(b);
			String xmlContent = new String(b, 0, lenght);
			ElementX element = ElementX.parse(xmlContent, null);
			ElementX ndData = element.getChild("nddata");
			channelid = ndData.getChildText("chl", "");
		} catch (Exception e) {
			if (null != e) {
				e.printStackTrace();
			}
			channelid = context.getString(R.string.app_chl);
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
		return channelid;
	}


	/**
	 * 是否渠道包
	 * @return boolean
	 */
//	public static boolean isChannelPackage() {
//		/**
//		 * add By C.xt
//		 * 如果是机锋版本不显示，
//		 * 如果不是，维持原来
//		 */
//		if (getIsGfan()) {
//			return true;
//		}
//		if (getIsAnzhi()) {
//			return true;
//		}
//		return isChannel;
//	}
	
	/**
	 * 是否是91助手渠道
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isAssiantPackage(Context ctx) {
		String channelId = HiAnalytics.getChannel(ctx);
		if (StringUtil.isEmpty(channelId))
			return false;
		
		if (ASSIST_91_CHANNEL_ID.equals(channelId))
			return true;
		
		return false;
	}
	
	/**
	 * 判断是否允许付费主题
	 * @param context
	 * @return boolean
	 */
	public static boolean isNoPayPackage(Context context) {
		return isNoPay;
		
//		if (isPayPck != -1) {
//			return isPayPck == 0 ? false : true;
//		}
//		
//		isPayPck = 0;
//		try {
//			String[] files = context.getAssets().list("");
//			for (String str : files) {
//				if (IS_PAY.equals(str)) {
//					isPayPck = 1;
//					break;
//				} 
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return isPayPck == 0 ? false : true;
	}
	
	/**
	 * 是否中国移动MM
	 * @return
	 */
	public static boolean isYiDongMM() {
		return isCustomVersion() && isYiDongMM;
	}
	
	/**
	 * 是否是安智
	 * @return boolean
	 */
	public static boolean getIsAnzhi() {
		return isAnzhi;
	}

	/**
	 * 是否百度渠道
	 * @return
	 */
	public static boolean isBaiduAssistChannel(Context ctx) {
		String channelId = HiAnalytics.getChannel(ctx);
		return BAIDU_CHANNEL_ID.equals(channelId);
	}

	/*public static boolean isBaiduAssistChannel630(Context ctx) {
		if(!isBaiduAssistChannel(ctx))
			return false;
		return false;//修改这里
	}*/
	
	/**
	 * 获取渠道类型
	 * @param ctx
	 * @return int
	 */
	public static int getChannelType(Context ctx) {
		if (getIsCustomByType(CUSTOM_CHANNEL)) {// 三无包
			return SANWUCHANNEL;
		}
		String channelId = HiAnalytics.getChannel(ctx);
		if (!StringUtil.isEmpty(channelId)) {
			if (ASSIST_91_CHANNEL_ID.equals(channelId)) {
				return ASSISTCHANNEL_91;
			} else if (HIAPK_CHANNEL_ID.equals(channelId)) {
				return HIAPKCHANNEL;
			}
		}
		return OTHERCHANNEL;
	}

    public static String getBaiduChannelId() {
        return BAIDU_CHANNEL_ID;
    }
    
    
    public static boolean isSEMThemeChannel(){
    	return isSEMChannel(SEM_THEME_CHN_ID);
    }
    
    public static boolean isSEMWallpaperChannel(){
    	return isSEMChannel(SEM_WALLPAPER_CHN_ID);
    }
    
    public static boolean isSEMRingChannel(){
    	return isSEMChannel(SEM_RING_CHN_ID);
    }
    
    public static boolean isSEMLockChannel(){
    	return isSEMChannel(SEM_LOCK_CHN_ID);
    }
    
    private static boolean isSEMChannel(String id){
    	if(!isSemChannel)
    		return false;
    	if(SEM_CHN_ID == null){
    		SEM_CHN_ID = getChannelOnlyForSpecialLauncherDoAnalyticInit(BaseConfig.getApplicationContext());
    	}
    	return SEM_CHN_ID != null && SEM_CHN_ID.equals(id);
    }
    
    /**
     * 是否是应用宝渠道
     * @param context
     * @return
     */
	public static boolean isYYBChannel(Context context){
		String channelId = getChannel(context);
		if(YINGYONGBAO_CHN_ID.equals(channelId)){
			return true;
		}
		return false;
	}
}
