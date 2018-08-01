package com.nd.android.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import com.nd.weather.widget.R;

public class DownLoadNotification {
    public static void downloadCompletedNotification(Context context, int position, String title, String content, PendingIntent pIntent ){
        NotificationManager nManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        
        //清除之前的通知栏
        nManager.cancel(position);
        
        Notification notif = new Notification(android.R.drawable.stat_sys_download_done, title + " 下载完成！", System.currentTimeMillis());
        notif.flags = Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(context, title, content, pIntent);
        nManager.notify(position, notif);
    }
    
    public static void downloadFailedNotification(Context context, int position, String title, PendingIntent pIntent ){
        NotificationManager nManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notif = new Notification(android.R.drawable.stat_notify_error, "下载失败！", System.currentTimeMillis());
        notif.flags = Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(context, title, "下载失败，请检查网络后重新尝试。", pIntent);
        nManager.notify(position, notif);
    }
    
    public static void downloadRunningNotification(Context context,  int position, String title, PendingIntent pIntent, int progress ){
        try{            
            NotificationManager nManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
            if (progress == 0){
                //第一次要先清理
                nManager.cancel(position);
            }
            RemoteViews view = new RemoteViews(context.getPackageName(),R.layout.notification);     
            view.setProgressBar(
                    R.id.download_notification_down_progress_bar, 100, progress, false);
            view.setTextViewText(
                    R.id.download_notification_down_progress, progress + "%");
            view.setTextViewText(
                    R.id.download_notification_soft, "正在下载" + title);
            view.setViewVisibility(
                    R.id.download_notification_progressblock, View.VISIBLE);
            
            Notification notification=new Notification(android.R.drawable.stat_sys_download, "开始下载" + title, System.currentTimeMillis());
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.icon = android.R.drawable.stat_sys_download;
            notification.contentView = view;
            notification.contentIntent = pIntent;
            nManager.notify(position, notification);
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
