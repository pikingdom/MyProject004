
package com.nd.android.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.util.FileHelp;
import com.nd.weather.widget.WidgetBaseService;
import com.nd.weather.widget.UI.weather.UIWeatherFragmentAty;

/**
 * @author Administrator
 */
public class DownloadService extends WidgetBaseService {
    // 新增下载任务
    private static final int ADD_DOWNLOAD_TASK = 1;

    // 暂停下载任务
    private static final int PAUSE_DOWNLOAD_TASK = 2;
    
    private static final int CONTINUE_DOWNLOAD_TASK = 3;

    private static final int DOWNLOAD_COMPLETE = 0;

    private static final int DOWNLOAD_FAIL = 1;

    private static final String TAG = "DownloadService";

    private Context mContext;
    
    private int UPDATE_GAP = 1000;// 更新进度间隔时间为1秒

    // 通知栏
    private NotificationManager notificationManager = null;

    private PendingIntent homePendingIntent = null;

    private DownThread downThread = null;

    private boolean running = false;

    /** downList 未开始下载皮肤列表 */
    private ConcurrentLinkedQueue<String> downList = new ConcurrentLinkedQueue<String>();

    /** DOWN_DIR */
    // private final static String DOWN_DIR = "";

    /** itemMap 未下载完成的主题列表(包含未开始下载和下载中的主题) */
    private ConcurrentHashMap<String, DowningTaskItem> itemMap = new ConcurrentHashMap<String, DowningTaskItem>();
    
    private Handler downloadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            DowningTaskItem taskItem = (DowningTaskItem)msg.obj;
            switch (msg.arg1) {
            case DOWNLOAD_COMPLETE:
                // 点击安装PendingIntent
                try {
                    Uri uri = Uri.fromFile(taskItem.getDownloadFile());
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setDataAndType(uri,
                            "application/vnd.android.package-archive");

                    PendingIntent installPendingIntent = PendingIntent.getActivity(mContext, 0,
                            installIntent, 0);
                    CompleteDownload(true, taskItem, installPendingIntent);
                    mContext.startActivity(installIntent);
                } catch (Exception e) {

                }
                ClearNotification(taskItem.getUid());
                itemMap.remove(taskItem.getSoftUrl());
                break;
            case DOWNLOAD_FAIL:
                // 下载失败
                CompleteDownload(false, taskItem, null);

                //ClearNotification(taskItem.getUid());
                if (itemMap.contains(taskItem.getSoftUrl())) {
                    itemMap.remove(taskItem.getSoftUrl());
                }
                break;

            default:
                super.handleMessage(msg);
                break;
            }
            // super.handleMessage(msg);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 提高service优先级（数值越小证明优先级越高，被kill掉的时间越晚）
        doForeground();
        mContext = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForegroundCompat(1);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        doCmd(intent);
    }
    
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    	doCmd(intent);
		return START_STICKY;
	}
    
    private void doCmd(Intent intent) {
        if (intent == null) {
            return;
        }
        int type = intent.getIntExtra("type", 0);
        switch (type) {
        case ADD_DOWNLOAD_TASK:
            //Log.e(TAG, "ADD_DOWNLOAD_TASK");
            /* 新增下载任务 */
            DowningTaskItem taskItem = (DowningTaskItem)intent
                    .getSerializableExtra("taskItem");
            if (taskItem == null) {
                return;
            }
            addDownTaskQueue(taskItem);
            break;
        case PAUSE_DOWNLOAD_TASK:
            /* 暂停下载任务 */
            String pauseUrl = intent.getStringExtra("url");
            if (TextUtils.isEmpty(pauseUrl)) {
                return;
            }
            if (downList.contains(pauseUrl)) {
                downList.remove(pauseUrl);
            }
            itemMap.remove(pauseUrl);
            break;
        case CONTINUE_DOWNLOAD_TASK:
            //Log.e(TAG, "CONTINUE");
            DowningTaskItem task = new DowningTaskItem();
            task.setSoftName(intent.getStringExtra("softname"));
            task.setDownloadDirPath(intent.getStringExtra("dir"));
            task.setDownloadFileName(intent.getStringExtra("filename"));
            task.setSoftUrl(intent.getStringExtra("url"));
            task.setDownloadIco(intent.getIntExtra("icon", 0));
            task.setSoftUid(intent.getIntExtra("uid", 0));
            addDownTask(this, task);
            break;     
        default:
            break;
        }
    }
    /**
     * @Title: addDownTaskQueue  
     * @Description: 添加下载任务到下载队列  
     * @author yanyy
     * @date 2013-2-18 上午9:27:06 
     *
     * @param taskItem       
     * @return void
     * @throws
     */
    private void addDownTaskQueue(DowningTaskItem taskItem){
        String url = taskItem.getSoftUrl();
        if ((url == null) || inDownList(url)) {
            return;
        }
        if (InitDownload(taskItem)) {
            downList.add(url);
            itemMap.put(url, taskItem);
            try {
                if (downThread == null || !running) {
                    downThread = new DownThread();
                    downThread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private boolean inDownList(String url) {
        return itemMap.containsKey(url);
    }

    private class DownThread extends Thread {
        public DownThread() {

        }

        @Override
        public void run() {
            if (!running) {
                try {
                    // 下载函数
                    // 增加权限<uses-permission
                    // android:name="android.permission.INTERNET">;
                    running = true;
                    String mUrl = downList.poll();
                    while (mUrl != null) {
                        DowningTaskItem taskItem = itemMap.get(mUrl);
                        Message message = downloadHandler.obtainMessage();
                        message.what = taskItem.getUid();
                        message.obj = taskItem;
                        try {
                            if (InitDownload(taskItem) && PreDownload(taskItem)) {
                                boolean isSuccess = downloadFile(mUrl);
                                if (isSuccess) {
                                    // 下载成功
                                    taskItem.state = DowningTaskItem.DownState_Finish;
                                    message.arg1 = DOWNLOAD_COMPLETE;
                                    downloadHandler.sendMessage(message);
                                    // return;
                                } else {
                                    message.arg1 = DOWNLOAD_FAIL;
                                    downloadHandler.sendMessage(message);
                                }
                            }
                        } catch (Exception e) {
                            message.arg1 = DOWNLOAD_FAIL;
                            downloadHandler.sendMessage(message);
                            e.printStackTrace();
                        }
                        mUrl = downList.poll();
                    }
                    running = false;
                    //停止服务(没有下载任务)
                    stopSelf();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    /**
     * @brief 【初始化下载】
     * @return
     */
    private boolean InitDownload(DowningTaskItem taskItem) {
        // 创建文件
        String strSdPath = FileHelp.getSDPath();
        if (strSdPath.length() > 0) {
            File downloadDir = new File(strSdPath,
                    taskItem.getDownLoadDirPath());
            // File downloadFile = new File(downloadDir.getPath(),
            // taskItem.getDownloadFileName());
            taskItem.setDownloadDir(downloadDir);
            // taskItem.setDownloadFile(downloadFile);
        } else {
            Toast.makeText(mContext, "未检测到SD存储卡，无法下载", Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        // 设置下载过程中，点击通知栏，回到主界面
        if (homePendingIntent == null) {
            Intent intent = new Intent(mContext, UIWeatherFragmentAty.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            homePendingIntent = PendingIntent.getActivity(mContext, 1,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        
        UpdateNotificationView(taskItem, 0);

        return true;
    }

    /**
     * @brief【判断软件存储路径是否存在】
     * @param taskItem
     * @return
     */
    private boolean PreDownload(DowningTaskItem taskItem) {
        try {
            // 增加权限<uses-permission
            // android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
            if (!taskItem.getDownloadDir().exists()) {
                taskItem.getDownloadDir().mkdirs();
            }

            // if (!taskItem.getDownloadFile().exists()) {
            // taskItem.getDownloadFile().createNewFile();
            // }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * @brief【下载完成后的处理】
     * @param bSuccess
     * @param taskItem
     * @param intentOnComplete
     */
    private void CompleteDownload(boolean bSuccess, DowningTaskItem taskItem,
            PendingIntent intentOnComplete) {
        if (bSuccess) {
            DownLoadNotification.downloadCompletedNotification(this, taskItem.getUid(), taskItem.getSoftName(), "下载完成，点击安装。", intentOnComplete);
        } else {
            // 下载失败
            //Log.e(TAG, "fail id=" + taskItem.getUid() + " url=" + taskItem.getSoftUrl());
            // 下载失败
            Intent i = new Intent(this, DownloadService.class);
            i.putExtra("type", CONTINUE_DOWNLOAD_TASK);
            i.putExtra("softname", taskItem.getSoftName());
            i.putExtra("dir", taskItem.getDownLoadDirPath());
            i.putExtra("filename", taskItem.getDownloadFileName());
            i.putExtra("url", taskItem.getSoftUrl());
            i.putExtra("icon", taskItem.getDownLoadIcon());
            i.putExtra("uid", taskItem.getUid());
            PendingIntent pi = PendingIntent.getService(this, taskItem.getUid(), i, PendingIntent.FLAG_UPDATE_CURRENT);
            DownLoadNotification.downloadFailedNotification(this, taskItem.getUid(), taskItem.getSoftName(), pi);
            itemMap.remove(taskItem.getSoftUrl());
        }
    }

    /**
     * @Title: addDownTask
     * @Description: 添加下载任务
     * @author yanyy
     * @date 2013-2-16 上午10:01:43
     * @param ctx
     * @param task
     * @return void
     * @throws
     */
    public static void addDownTask(Context ctx, DowningTaskItem task) {
        Intent i = new Intent(ctx, DownloadService.class);
        i.putExtra("type", ADD_DOWNLOAD_TASK);
        i.putExtra("taskItem", task);
        ctx.startService(i);
    }

    /**
     * 暂停 pauseUrl下载
     * 
     * @param pauseUrl
     */
    public static void pauseDownTask(Context ctx, String pauseUrl) {
        Intent i = new Intent(ctx, DownloadService.class);
        i.putExtra("type", PAUSE_DOWNLOAD_TASK);
        i.putExtra("url", pauseUrl);
        ctx.startService(i);
    }

    /**
     * 更新通知
     */
    private void UpdateNotificationView(DowningTaskItem taskItem, int progress) {
        DownLoadNotification.downloadRunningNotification(this, taskItem.getUid(), taskItem.getSoftName(), homePendingIntent, progress);
    }

    public void ClearNotification(int notificationId) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager)this
                    .getSystemService(Service.NOTIFICATION_SERVICE);
        }
        notificationManager.cancel(notificationId);
    }

    public void ClearAllNotification() {
        if (itemMap != null && itemMap.size() > 0) {
            for (String sUrl : itemMap.keySet()) {
                DowningTaskItem taskItem = itemMap.get(sUrl);
                ClearNotification(taskItem.getUid());
                itemMap.remove(sUrl);
            }
        }
    }

    /**
     * @brief 【下载文件】
     * @n<b>函数名称</b> :downloadFile
     * @param downloadUrl
     * @param saveFile
     * @return
     * @throws Exception
     * @return long
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2011-12-7下午05:23:33
     */
    private boolean downloadFile(String downloadUrl)
            throws Exception {
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;
        long lastUpdatedTime = 0;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        RandomAccessFile fos = null;
        DowningTaskItem taskItem = itemMap.get(downloadUrl);

        try {
            // 建立文件
            String tempFilePath = taskItem.getDownloadDir() + "/" + taskItem.getDownloadFileName();
            File temp = new File(tempFilePath + ".temp");
            if (!temp.exists()) {
                temp.createNewFile();
            }

            httpConnection = getConnection(downloadUrl);
            
            currentSize = (int)getCurrentSize(temp);

            if (currentSize > 0) {
                httpConnection.setRequestProperty("Range", "bytes="
                    + currentSize + "-");
            }
            httpConnection.connect();

            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            
            updateTotalSize = httpConnection.getContentLength();
            if (updateTotalSize == -1){
                //取不到总长度(不支持短点续传)(只能弄个假的进度)
                if (currentSize > 0){
                    currentSize = 0;
                    temp.delete();
                    temp.createNewFile();
                }
            }
            
            updateTotalSize = updateTotalSize + currentSize;

            is = httpConnection.getInputStream();
            fos = new RandomAccessFile(temp, "rw");
            byte buffer[] = new byte[4096];
            int readsize = 0;
            fos.seek(currentSize);
            totalSize = currentSize;
            int iProgress = 0;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                currentSize += readsize;
                //取不到总长度(不支持短点续传)(只能弄个假的进度)
                iProgress = Math.abs((int)totalSize * 100
                        / (updateTotalSize == -1 ? 5000000 : updateTotalSize));
                if (iProgress > 100) {
                    iProgress = 100;
                }

                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ( System.currentTimeMillis() - lastUpdatedTime > UPDATE_GAP || iProgress==100 ) {
                    //更新进度条
                    UpdateNotificationView(taskItem, iProgress);
                    lastUpdatedTime = System.currentTimeMillis();
                }   
                if (currentSize == updateTotalSize){
                    // File tempFile = new File(tempFilePath);
                    File saveFile = new File(tempFilePath);
                    temp.renameTo(saveFile);
                    taskItem.setDownloadFile(saveFile);
                    return true;
                }
                // 线程被干扰掉，一般是人为取消下载时触发的
                if (!Thread.currentThread().isAlive()
                        || Thread.currentThread().isInterrupted()) {
                    Log.d(TAG, "run.......InterruptedException.");
                    throw new InterruptedException();
                }
            }
            if (updateTotalSize == -1){
                // 取不到总长度的
                if (iProgress < 100){
                    // 进度没到100(因为有的连接取不到总长度，进度是假的)
                    UpdateNotificationView(taskItem, 100);
                }
                // File tempFile = new File(tempFilePath);
                File saveFile = new File(tempFilePath);
                temp.renameTo(saveFile);
                taskItem.setDownloadFile(saveFile);
                return true;
            }
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                // fos.flush();
                fos.close();
            }
        }
        return false;
    }

    private long getCurrentSize(File path) {

        // File file = FileUtil.createFile(path);
        File file = path;
        long size = 0;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            size = file.length();
        }
        return size;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private HttpURLConnection getConnection(String urlString) throws Exception
    {
        URL url=null;
        HttpURLConnection conn=null;
        try {
            url=new URL(urlString);
            // 有代理则使用代理，否则就是网络问题
            Proxy proxy = HttpToolKit.getProxy(mContext);
            if (proxy != null)
            {
                conn = (HttpURLConnection)url.openConnection(proxy);
            }
            else
            {
                conn = (HttpURLConnection)url.openConnection();
            }
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);//超时30秒钟
            conn.setReadTimeout(30000);//连接之后30秒钟的时间内没有读到数据，即超时
        } catch (Exception e) {
            if(conn!=null)
                conn.disconnect();
        }
        return conn;
    }
}
