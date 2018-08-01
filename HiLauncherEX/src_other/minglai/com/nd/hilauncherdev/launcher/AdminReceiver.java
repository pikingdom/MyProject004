package com.nd.hilauncherdev.launcher;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.newline.launcher.R;

/**
 * Created by caizhipeng on 2017/6/30.
 */

public class AdminReceiver extends DeviceAdminReceiver {

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.device_admin_cancel);
    }

}
