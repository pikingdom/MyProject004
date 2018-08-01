package com.nd.hilauncherdev.kitset.uconfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.url.URLs;

/**
 * Description: 万能配置 helper</br>
 * Author: cxy
 * Date: 2017/2/20.
 */

public class UConfigHelper {

    private static final String SP_NAME = "universal";
    private static final String BASE_URL = URLs.PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?cname=%s&ver=%s";

    private Context ctx;
    private SharedPreferences preferences;

    UConfigHelper(Context context) {
        ctx = context;
        preferences = context.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS);
    }

    private void waitForLoad() {
        if (preferences != null) {
            return;
        }

        preferences = ctx.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS);
    }

    public String getBaseUrl(String configName, boolean verifyVer) {
        if (TextUtils.isEmpty(configName)) {
            return "";
        }
        int ver = verifyVer ? getVersion(configName) : 0;
        String url = String.format(BASE_URL, configName, ver);
        url = url + CUIDUtil.getCUIDPART() + "&pid=6&mt=" + 4 + "&DivideVersion=" + TelephoneUtil.getVersionName(ctx, ctx.getPackageName());
        return url;
    }

    public int getVersion(String configName) {
        waitForLoad();

        final String keyVer = configName + "_ver";

        if (preferences != null) {
            return preferences.getInt(keyVer, 0);
        }
        return 0;
    }

    public boolean setVersion(String configName, int ver) {
        waitForLoad();
        final String keyVer = configName + "_ver";

        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(keyVer, ver);
            return editor.commit();
        }
        return false;
    }

    public String getContent(String configName) {
        waitForLoad();

        final String keyCon = configName + "_con";

        if (preferences != null) {
            return preferences.getString(keyCon, null);
        }
        return null;
    }

    public boolean setContent(String configName, String content) {
        waitForLoad();

        final String keyCon = configName + "_con";

        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(keyCon, content);
            return editor.commit();
        }

        return false;
    }
}
