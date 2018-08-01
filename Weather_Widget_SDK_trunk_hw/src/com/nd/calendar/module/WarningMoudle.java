/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : WarningMoudle
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-1-6 上午10:21:39 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.calendar.module;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.calendar.CommData.WarningInfo;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.util.FileHelp;
import com.nd.calendar.util.StringHelp;

public class WarningMoudle {
    // public final static String WARNING_ICO_DIR = "/WarningIco/";
    public final static int MSG_WARNING_FINISH_SUCCESS = 1;
    public final static int WARNING_FINISH_MSG_FAILURE = 0;

    private static HashMap<String, HashMap<Handler, Integer>> downloadingIco; // 正在下载的图标MAP，防止同时下载出现冲突
    private static WarningMoudle warningMoudle;
    private Context mContext;

    public static WarningMoudle getInstance(Context context) {
        if (warningMoudle == null) {
            warningMoudle = new WarningMoudle();
            warningMoudle.mContext = context;
            downloadingIco = new HashMap<String, HashMap<Handler, Integer>>();
        }

        return warningMoudle;
    }

    /**
     * @brief 【下载预警图标】
     * 
     * @n<b>函数名称</b> :downloadWarningIco
     * @param warningInfo
     * @param finishHandler
     * @param nMsg
     * @return
     * @return boolean
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-1-10下午03:02:06
     */
    public boolean downloadWarningIco(WarningInfo warningInfo, Handler finishHandler, int nMsg) {
        if (warningInfo != null) {
            String sIcoName = StringHelp.getUrlFileName(warningInfo.getImgUrl());
            if (sIcoName != null) {
                synchronized (downloadingIco) {

                    // 检查该图标是否在下载
                    HashMap<Handler, Integer> hmHandlers = downloadingIco.get(sIcoName);
                    if (hmHandlers == null) {
                        hmHandlers = new HashMap<Handler, Integer>();
                        downloadingIco.put(sIcoName, hmHandlers);
                    }

                    // 添加回调消息
                    if (!hmHandlers.containsKey(finishHandler)) {
                        hmHandlers.put(finishHandler, nMsg);

                        new DownloadIcoThread(warningInfo).start();
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * @brief 【发送完成消息】
     * 
     * @n<b>函数名称</b> :sendFinishMsg
     * @param icoKey
     * @param msg
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-1-10下午03:02:34
     */
    void sendFinishMsg(String icoKey, Message msg) {
        if (icoKey == null) {
            return;
        }

        synchronized (downloadingIco) {
            HashMap<Handler, Integer> hmHandlers = downloadingIco.get(icoKey);
            if (hmHandlers == null) {
                return;
            }

            Set<Handler> handlers = hmHandlers.keySet();

            // 向所有的Handler 发送消息
            for (Handler handler : handlers) {
                if (handler != null) {
                    try {
                        handler.sendMessage(handler.obtainMessage(hmHandlers.get(handler),
                                msg.arg1, msg.arg2, msg.obj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 发送完后，删除该队列
            downloadingIco.remove(icoKey);
        }
    }

    // 图标获取线程
    private class DownloadIcoThread extends Thread {
        private WarningInfo warningInfo;

        public DownloadIcoThread(WarningInfo warningInfo) {
            this.warningInfo = warningInfo;
        }

        @Override
        public void run() {
            super.run();

            Message msg = new Message();

            try {
                if (warningInfo != null) {
                    String sWeather = warningInfo.getWeather();
                    if (sWeather != null && sWeather.length() > 0) {
                        String sUrl = warningInfo.getImgUrl();
                        String sIcoName = StringHelp.getUrlFileName(sUrl);
                        if (sIcoName != null) {
                            // 检查 SD 卡
                            String strIcoDir = FileHelp.getCalendarWarningDir();
                            if (TextUtils.isEmpty(strIcoDir)) {
                                msg.arg1 = WARNING_FINISH_MSG_FAILURE;
                                sendFinishMsg(sIcoName, msg);
                                return;
                            }

                            // 存在图标文件，则使用该文件，否则下载
                            String sIcoPath = strIcoDir + File.separator + sIcoName;

                            if (FileHelp.isExist(sIcoPath)) {
                                msg.arg1 = MSG_WARNING_FINISH_SUCCESS;
                                msg.obj = sIcoPath;

                                sendFinishMsg(sIcoName, msg);
                                return;
                            } else {
                                if (HttpToolKit.isNetworkAvailable(mContext)) {
                                    HttpToolKit.saveToFile(mContext, sUrl, sIcoPath);
                                    msg.arg1 = MSG_WARNING_FINISH_SUCCESS;
                                    msg.obj = sIcoPath;
                                    sendFinishMsg(sIcoName, msg);
                                } else {
                                    // 没网络
                                    msg.arg1 = WARNING_FINISH_MSG_FAILURE;
                                    sendFinishMsg(sIcoName, msg);
                                    return;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                msg.arg1 = WARNING_FINISH_MSG_FAILURE;
                sendFinishMsg(null, msg);
                e.printStackTrace();
            }

            return;
        }

    }
}
