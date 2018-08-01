package com.nd.hilauncherdev.launcher;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;

import java.util.List;

public class CallHomeService extends Service {
    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                startShowSetDefaultLauncherActivity();
            }
        }).start();
        super.onCreate();
       // startShowSetDefaultLauncherActivity();
    }

    private void startShowSetDefaultLauncherActivity()
    {

       List<String> home= DefaultHomeTipActivity.getAllHomes(this);
        String process = DefaultHomeTipActivity.getProcessPkg(this);
        String pkn=this.getPackageName();
        if(process!=null && home.contains(process) && !process.equals(pkn)) {

            Intent localIntent = new Intent(this, DefaultHomeTipActivity.class);
            localIntent.setFlags(268435456);
            localIntent.setAction("android.intent.action.MAIN");
            startActivity(localIntent);
            ConfigPreferences.getInstance().setShowSetDefaultLauncherPopupWindow(false);
        }else {
            this.stopSelf();
        }
    }

    /**
     * Called when the activity is first created.
     */


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
