package com.nd.hilauncherdev.launcher;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 锁屏调用类
 * ！！！如果更改包名或类名，请与锁屏同步！！！
 * Created by guojianyun on 2017/3/17.
 */

public class ActiveService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CustomChannelController.onUserPresentOn(this);
        return super.onStartCommand(intent, flags, startId);
    }

}
