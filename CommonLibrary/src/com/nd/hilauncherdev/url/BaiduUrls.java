package com.nd.hilauncherdev.url;

import com.nd.hilauncherdev.kitset.AppDistributeUtil;

/**
 *	百度渠道号服务端配置
 * <p>Title: BaiduUrls</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年1月26日
 */
public class BaiduUrls {
	/**
     * 百度搜索自动完成提示源地址
     * 联想词
     */
    private static final String URL_BAIDU_SUGGUESTION = "http://m.baidu.com/su?from=%s&ie=utf-8&wd=%s&action=opensearch";
    /**
     * 百度搜索
     * @version 2.0    新百度搜索渠道地址由yuf@2013.08.05更正为: http://m.baidu.com/s?from=1000925i&word=%s
     * @version 1.0, 原渠道地址: http://m.baidu.com/s?from=1429e&word=%s
     * 1、默认程序选择框，点击浏览器打开百度页面。
     * 2、请求获取搜索地址（除热词其他都用这个）
     * 3、在线搜索
     */
    private static final String BAIDU_SEARCH_URL = "http://m.baidu.com/s?from=%s&word=%s";
    /**
     * 百度热词
     * 除宜搜外, 其他热词默认都用百度
     */
    private static final String BAIDU_HOTWORD_URL = "http://m.baidu.com/s?from=%s&word=%s";
    /**
     * 获取百度联想词地址
     * <p>Title: getBaiduSugguestion</p>
     * <p>Description: </p>
     * @return
     * @author maolinnan_350804
     */
    public static String getBaiduSugguestion(String word){
    	String channel = AppDistributeUtil.AppDistributePreference.getInstance().getSugguestionChannel();//渠道号
    	String result = String.format(URL_BAIDU_SUGGUESTION, channel,word);
    	return result;
    }
	/**
	 * 获取百度搜索地址
	 * <p>Title: getBaiduSearch</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	public static String getBaiduSearch(String word){
		String channel = AppDistributeUtil.AppDistributePreference.getInstance().getSearchChannel();//渠道号
    	String result = String.format(BAIDU_SEARCH_URL, channel,word);
    	return result;
	}
	/**
	 * 获取百度热词地址
	 * <p>Title: getBaiduHotword</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	public static String getBaiduHotword(String word){
		String channel = AppDistributeUtil.AppDistributePreference.getInstance().getHotwordChannel();//渠道号
    	String result = String.format(BAIDU_HOTWORD_URL, channel,word);
    	return result;
	}
}
