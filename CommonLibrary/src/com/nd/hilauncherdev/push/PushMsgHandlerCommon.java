package com.nd.hilauncherdev.push;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.db.ConfigDataBase;
import com.nd.hilauncherdev.push.model.NotifyPushInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingdongjin_dian91 on 2016/1/8.
 */
public class PushMsgHandlerCommon {


    public static final String EXTRA_INTENT = "extra_intent";
    public static final String EXTRA_PUSH_ID = "extra_push_id";
    public static final String EXTRA_PUSH_TITLE = "extra_push_title";
    public static final String EXTRA_PUSH_SOURCE_ID = "extra_push_source_id";
    public static final String EXTRA_PUSH_NOTIFY_ICON = "extra_push_notify_icon";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";
    // 推送图标到桌面类型
    public static final String EXTRA_TYPE_ADD_ICON = "extra_add_icon";
    public static final String EXTRA_ADD_ICON_TITLE = "extra_add_icon_title";
    public static final String EXTRA_ADD_ICON_INTENT = "extra_add_icon_intent";
    public static final String EXTRA_ADD_ICON_PATH = "extra_add_icon_path";

    /**
     * 获取跳转Intent
     *
     * @param ctx
     * @param intent
     * @return
     */
    public static Intent getIntent(Context ctx, String intent, int pushID, String path) {
        Intent rIntent = new Intent();
        rIntent.setClassName(ctx, "com.nd.hilauncherdev.push.PushMsgRedirectActivity");
        rIntent.putExtra(EXTRA_INTENT, intent);
        rIntent.putExtra(EXTRA_PUSH_ID, "" + pushID);
        if(!StringUtil.isEmpty(path)){
            rIntent.putExtra(EXTRA_PUSH_NOTIFY_ICON, path);
        }
        return rIntent;
    }

    public static List<NotifyPushInfo> getNotReadMsg(int size, int type) {
        List<NotifyPushInfo> list = null;
        ConfigDataBase db = null;
        Cursor c = null;
        try {
            db = ConfigDataBase.getInstance(BaseConfig.getApplicationContext());
            c = db.query("select * from launcher_push where isRead = 0 and type ='" + "notify" + "' and value like '%pos%"
                    + type + "%' order by _id desc ");
            if (c != null) {
                list = new ArrayList<NotifyPushInfo>();
                int idIndex = c.getColumnIndexOrThrow("_id");
                int valueIndex = c.getColumnIndexOrThrow("value");
                size = (size > 0) ? size : Integer.MAX_VALUE;
                int count = 0;
                while (c.moveToNext()) {
                    NotifyPushInfo info = parseNotifyPushInfo(c.getString(valueIndex));
                    if(StringUtil.isEmpty(info.getPos()) || !info.getPos().equals("" + type))
                        continue;
                    info.setId(c.getInt(idIndex));
                    info.setNotifyIconPath("");
                    list.add(info);
                    count ++;
                    if(count >= size)
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }
        return list;
    }


    /**
     * 设置冒泡提示推送已点击
     * @param id
     */
    public static void setPushMsgRead(long id){
        ConfigDataBase db = null;
        try {
            db = ConfigDataBase.getInstance(BaseConfig.getApplicationContext());
            db.execSQL("update launcher_push set isRead = 1 where _id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }


    /**
     * 解析普通通知，数据格式：title=通知1&content=通知1内容&intent=ii&persist=0&target=com.nd.android.pandahome2&
     * @param str
     * @return
     * @throws JSONException
     */
    public static NotifyPushInfo parseNotifyPushInfo(String str) throws JSONException{
        if(str.startsWith("{")){//解析json格式
            return parseNotifyPushInfoJson(new JSONObject(str));
        }
        String[] array = str.split("&");
        if(array.length > 10){//大图通知
            return null; //parseNotifyBigPicPushInfo(str);
        }
        NotifyPushInfo obj = new NotifyPushInfo();
        obj.setTitle(array[0].substring(6));//title=
        obj.setContent(array[1].substring(8));//content=
        obj.setIntent(array[2].substring(7));//intent=
        obj.setPersist(!StringUtil.isEmpty(array[3].substring(8)) && array[3].substring(8).equals("1"));//persist=
        obj.setNotifyIcon(array[5].substring(12));//notify_icon=
        if(array.length > 6){
            obj.setPos(array[6].substring(4));//pos=
        }
        if(array.length > 7 && !StringUtil.isEmpty(array[7].substring(6))){
            obj.setStyle(Integer.valueOf(array[7].substring(6)));//style=
        }
        return obj;
    }

    private static NotifyPushInfo parseNotifyPushInfoJson(JSONObject object){
        NotifyPushInfo obj = new NotifyPushInfo();
        obj.setTitle(object.optString("title"));
        obj.setContent(object.optString("content"));
        obj.setIntent(object.optString("intent"));
        obj.setPersist(!StringUtil.isEmpty(object.optString("persist")) && object.optString("persist").equals("1"));
        obj.setNotifyIcon(object.optString("notify_icon"));
        obj.setPos(object.optString("pos"));
        obj.setStyle(object.optInt("style"));
        return obj;
    }
}
