package com.nd.hilauncherdev.kitset.fileselector;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.nd.hilauncherdev.kitset.fileselector.IconModel.IconPack;
import com.nd.hilauncherdev.kitset.util.IconUtils;
import com.nd.hilauncherdev.theme.data.ResParser;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;

/**
 * 图标工厂
 * @author cjg
 *
 */
public class IconFactory {

    /**
     * 获取第三方图标包
     * @param ctx Context
     * @return List
     */
    public static List<IconPack> getIconPacks(Context ctx) {
        List<IconPack> list = new ArrayList<IconPack>();
        final PackageManager pm = ctx.getPackageManager();
        //1.openHome icons...
        Intent queryIntent = new Intent(ThemeGlobal.INTENT_OPENHOME_ICON);
        List<ResolveInfo> apps = pm.queryIntentActivities(queryIntent, 0);
        if (apps != null && apps.size() > 0) {
            for (ResolveInfo app : apps) {
                String packName = app.activityInfo.packageName;
                try {
                    IconPack ip = new IconPack();
                    ip.name = "" + app.loadLabel(pm);
                    ip.packName = packName;
                    ip.from = "OpenHome";
                    list.add(ip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 获取图标包里面的图标
     * @param ctx Context
     * @param ip IconPack
     * @return List
     */
    public static List<String> getPackIcons(Context ctx, IconPack ip) {
        ip.iconList.clear();
        try {
            Context packCtx = ctx.createPackageContext(ip.packName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            String[] icons = packCtx.getAssets().list("icons");
            for (String sIcon : icons) {
                int flag = sIcon.lastIndexOf(".");
                String str = sIcon.substring(flag);
                if (".png".equalsIgnoreCase(str)) {
                    ip.iconList.add("icons/" + sIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip.iconList;
    }

    /**
     * 读取外部的图标
     * @param ctx Context
     * @param tag 如: com.xxx.xxx|icons/xx.png格式
     * @return Drawable
     */
    public static Drawable getIconDrawable(Context ctx, String tag) {
        Drawable d = ResParser.getImageDrawable(ctx, null, tag, 0, false);
        if(d != null){
            return IconUtils.createIconDrawable(d, ctx);
        }
        return null;
    }
}
