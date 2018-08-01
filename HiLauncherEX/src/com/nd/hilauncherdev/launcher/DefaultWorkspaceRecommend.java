package com.nd.hilauncherdev.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;

import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.theme.data.ThemeData;

/**
 * description: 默认桌面布局推荐处理<br/>
 * author: dingdj<br/>
 * data: 2014/8/28<br/>
 */
public class DefaultWorkspaceRecommend {

    //所有推荐 数组的大小需于优选匹配
    public static String[][] allRecommends = {
    	{"com.tencent.mobileqq"},//QQ
    	{"com.tencent.mm"},//微信
    	{"com.meitu.meiyancamera"},//美颜相机
    	{"com.qiyi.video"},//爱奇艺
    	{"com.qzone"},//QQ空间
    	{"com.youku.phone"},//优酷
    	{"com.kugou.android"},//酷狗音乐
    	{"com.sina.weibo"},//新浪微博
    	{"com.tencent.qqmusic"},//QQ音乐
    	{"com.tencent.qqlive"},//腾讯视频
    	{"com.baidu.BaiduMap"},//百度地图
    	{"com.autonavi.minimap"},//高德地图
    	{"com.qq.reader"},//QQ阅读
    	{"com.mt.mtxx.mtxx"},//美图秀秀
    	{"com.dianping.v1"},//大众点评
    	{"com.xiachufang"},//下厨房
    	{"com.yixia.videoeditor"},//秒拍
    	{"com.ss.android.article.news"},//今日头条
    	{"com.tencent.news"},//腾讯新闻
    	{"com.netease.newsreader.activity"}//网易新闻
    };

    //优选推荐 数组的大小需于所有推荐匹配
    public static String[][] optRecommends = {
    	{"com.tencent.mobileqq"},//QQ
    	{"com.tencent.mm"},//微信
    	{"com.meitu.meiyancamera"},//美颜相机
    	{"com.qiyi.video"},//爱奇艺
    	{"com.qzone"},//QQ空间
    	{"com.youku.phone"},//优酷
    	{"com.kugou.android"},//酷狗音乐
    	{"com.sina.weibo"},//新浪微博
    	{"com.tencent.qqmusic"},//QQ音乐
    	{"com.tencent.qqlive"},//腾讯视频
    	{"com.baidu.BaiduMap"},//百度地图
    	{"com.autonavi.minimap"},//高德地图
    	{"com.qq.reader"},//QQ阅读
    	{"com.mt.mtxx.mtxx"},//美图秀秀
    	{"com.dianping.v1"},//大众点评
    	{"com.xiachufang"},//下厨房
    	{"com.yixia.videoeditor"},//秒拍
    	{"com.ss.android.article.news"},//今日头条
    	{"com.tencent.news"},//腾讯新闻
    	{"com.netease.newsreader.activity"}//网易新闻
    };


    /**
     * 默认推荐的系统应用
     */
    public static final String[] DEFAULT_RECOMMEND_APPS = {
            ThemeData.ICON_ALARMCLOCK,//时钟
            ThemeData.ICON_CALENDAR,//日历
            ThemeData.ICON_SETTINGS,//设置
            ThemeData.ICON_CAMERA//相机
    };

    public static void main(String[] args) {
        filterRecommend(Global.getApplicationContext(), 10);
    }

    public static TreeSet<RecommendInfo> filterRecommend(Context context, int upNum) {
        TreeSet<RecommendInfo> set = createRecomendInfo(context, upNum);
        /*for (RecommendInfo recommendInfo : set) {
            System.out.println(recommendInfo.getName());
        }*/
        return set;
    }

    public static boolean isInstall(Context context,  String packageName) {
        return AndroidPackageUtils.isPkgInstalled(context, packageName);
    }

    /**
     * 返回全部的推荐图标 按优先级分类 priority数值越低位置越前
     */
    private static TreeSet<RecommendInfo> createRecomendInfo(Context context, int upNum) {
        TreeSet<RecommendInfo> set = new TreeSet<RecommendInfo>();
        List<String> all = new ArrayList<String>();
        List<String> opt = new ArrayList<String>();
        for (int i = 0; i < Math.min(allRecommends.length, optRecommends.length); i++) {
            all.clear();
            opt.clear();
            all.addAll(Arrays.asList(allRecommends[i]));
            opt.addAll(Arrays.asList(optRecommends[i]));
            int yuzhi = Math.min(2, opt.size());
            int count = 0;
            int j = 0;
            for (String packageName : opt) {
                if (isInstall(context, packageName)) { // 已安装
                    set.add(new RecommendInfo(packageName, i * 100 + j));
                    count++;
                }
                all.remove(packageName);
                j++;
            }

            for (String packageName : all) {
                if (isInstall(context, packageName)) { // 已安装
                    if (count < yuzhi) {
                        set.add(new RecommendInfo(packageName, i * 100 + j));
                    } else {// 超过了阈值 提权10000
                        set.add(new RecommendInfo(packageName, i * 100 + j + 10000));
                    }
                    count++;
                }
                j++;
            }
            if(upNum != -1 && set.size() >= upNum) {
                break;
            }
        }
        return set;
    }

    static class RecommendInfo implements Comparable<RecommendInfo> {

        public RecommendInfo(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        private String name;

        private int priority;

        @Override
        public int compareTo(RecommendInfo o) {
            if (o == null) {
                return this.getPriority();
            }
            return this.getPriority() - o.getPriority();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPriority() {
            return priority;
        }

    }
}
