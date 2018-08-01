package com.nd.hilauncherdev.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.MyPhoneUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangchao_91 on 2015/7/13.
 */
public class ShareTool {

    public static final String COM_ANDROID_EMAIL_CLASS = "com.android.email.activity.MessageCompose";
    public static final String COM_ANDROID_EMAIL = "com.android.email";
    //	public static final String COM_ANDROID_MMS_CLASS = "com.android.mms.ui.ComposeMessageActivity";
    //	public static final String COM_ANDROID_MMS = "com.android.mms";
    public static final String COM_TENCENT_MM_CLASS2 = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    public static final String COM_TENCENT_MM_CLASS = "com.tencent.mm.ui.tools.ShareImgUI";
    public static final String COM_TENCENT_MM = "com.tencent.mm";
    public static final String COM_QZONE_CLASS = "com.qzone.ui.operation.QZonePublishMoodActivity";
    public static final String COM_QZONE_CLASS1 = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";
    public static final String COM_QZONE = "com.qzone";
    public static final String COM_TENCENT_MOBILEQQ_CLASS = "com.tencent.mobileqq.activity.JumpActivity";
    public static final String COM_TENCENT_MOBILEQQ2 = "com.tencent.qqlite";
    public static final String COM_TENCENT_MOBILEQQ = "com.tencent.mobileqq";
    public static final String COM_SINA_WEIBO_CLASS3 = "com.sina.weibo.composerinde.ComposerDispatchActivity";
    public static final String COM_SINA_WEIBO_CLASS2 = "com.sina.weibog3.EditActivity";
    public static final String COM_SINA_WEIBO_CLASS = "com.sina.weibo.ComposerDispatchActivity";
    public static final String COM_SINA_WEIBO3 = "com.sina.weibo";
    public static final String COM_SINA_WEIBO2 = "com.sina.weibog3";
    public static final String COM_SINA_WEIBO = "com.sina.weibo";

    private Context mContext;
    private List<SharedItem> mSharedDataList = new ArrayList<SharedItem>();
    private Map<String, SharedItem> mSharedDefaultMap = new HashMap<String, SharedItem>();

    public ShareTool(Context ctx) {
        mContext = ctx;
        initEightLegends();
        initOtherApp();
    }

    public List<SharedItem> getSharedDataList() {
        return mSharedDataList;
    }

    private void initEightLegends() {
        initSharedApp(COM_TENCENT_MM + "1", COM_TENCENT_MM, COM_TENCENT_MM_CLASS, R.string.settings_home_apps_shared_wx_name, R.string.settings_home_apps_shared_wx_friend, R.drawable.shared_icon_wechat, true);
        initSharedApp(COM_SINA_WEIBO, COM_SINA_WEIBO, COM_SINA_WEIBO_CLASS, R.string.settings_home_apps_shared_sina_weibo, R.string.settings_home_apps_shared_sina_weibo, R.drawable.shared_icon_weibo, true);
        initSharedApp(COM_SINA_WEIBO, COM_SINA_WEIBO2, COM_SINA_WEIBO_CLASS2, R.string.settings_home_apps_shared_sina_weibo, R.string.settings_home_apps_shared_sina_weibo, R.drawable.shared_icon_weibo, false);
        initSharedApp(COM_SINA_WEIBO, COM_SINA_WEIBO3, COM_SINA_WEIBO_CLASS3, R.string.settings_home_apps_shared_sina_weibo, R.string.settings_home_apps_shared_sina_weibo, R.drawable.shared_icon_weibo, false);
        initSharedApp(COM_TENCENT_MOBILEQQ, COM_TENCENT_MOBILEQQ, COM_TENCENT_MOBILEQQ_CLASS, R.string.settings_home_apps_shared_mobile_qq_name, R.string.settings_home_apps_shared_mobile_qq, R.drawable.shared_icon_qq, true);
        initSharedApp(COM_TENCENT_MOBILEQQ, COM_TENCENT_MOBILEQQ2, COM_TENCENT_MOBILEQQ_CLASS, R.string.settings_home_apps_shared_mobile_qq_name, R.string.settings_home_apps_shared_mobile_qq, R.drawable.shared_icon_qq, false);
        initSharedApp(COM_QZONE, COM_QZONE, COM_QZONE_CLASS, R.string.settings_home_apps_shared_qzone, R.string.settings_home_apps_shared_qzone, R.drawable.shared_icon_qzone, true);
        initSharedApp(COM_QZONE, COM_QZONE, COM_QZONE_CLASS1, R.string.settings_home_apps_shared_qzone, R.string.settings_home_apps_shared_qzone, R.drawable.shared_icon_qzone, false);

        initSharedApp(COM_TENCENT_MM + "2", COM_TENCENT_MM, COM_TENCENT_MM_CLASS2, R.string.settings_home_apps_shared_wx_name, R.string.settings_home_apps_shared_wx_friends, R.drawable.shared_icon_networking, true);
        //		initSharedApp(COM_ANDROID_MMS, COM_ANDROID_MMS_CLASS, R.string.settings_home_apps_shared_sms, R.string.settings_home_apps_shared_sms, R.drawable.shared_icon_sms);
        initSharedApp(COM_ANDROID_EMAIL, COM_ANDROID_EMAIL, COM_ANDROID_EMAIL_CLASS, R.string.settings_home_apps_shared_email, R.string.settings_home_apps_shared_email, R.drawable.shared_icon_email, true);
    }

    private void initSharedApp(String id, String pkg, String clazz, int installNameId, int descId, int iconId, boolean addToDefault) {
        SharedItem sharedItem = new SharedItem();
        sharedItem.setId(id);
        sharedItem.setPkg(pkg);
        sharedItem.setDesc(mContext.getString(descId));
        sharedItem.setInstallName(mContext.getString(installNameId));
        sharedItem.setClassName(clazz);
        sharedItem.setIcon(mContext.getResources().getDrawable(iconId));
        mSharedDefaultMap.put(pkg + "/" + clazz, sharedItem);
        if (addToDefault) {
            mSharedDataList.add(sharedItem);
        }
    }

    private void initOtherApp() {
        // 新浪微博com.sina.weibo/com.sina.weibo.ComposerDispatchActivity
        // qq好友com.tencent.mobileqq/com.tencent.mobileqq.activity.JumpActivity--/com.tencent.mobileqq.activity.qfileJumpActivity
        // qq空间com.qzone/com.qzone.ui.operation.QZonePublishMoodActivity
        // 微信com.tencent.mm/com.tencent.mm.ui.tools.ShareImgUI--/com.tencent.mm.ui.tools.ShareToTimeLineUI
        // 、复制链接、邮件分享、
        // 短信com.android.mms/com.android.mms.ui.ComposeMessageActivity

        SharedItem si = null;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setType("image/*");

        PackageManager mPackageManager = mContext.getPackageManager();
        List<ResolveInfo> a = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : a) {

            String packageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;
            String key = packageName + "/" + className;
            if (!mSharedDefaultMap.containsKey(key)) {
                si = new SharedItem();
                si.setId(packageName);
                si.setPkg(packageName);
                si.setDesc(resolveInfo.loadLabel(mPackageManager).toString());
                si.setIcon(resolveInfo.activityInfo.loadIcon(mPackageManager));
                si.setClassName(className);
                si.setInstalled(true);
                mSharedDataList.add(si);
            } else {
                /**
                 * 适配一个应用多个包名的情况
                 * 找到系统安装的包名替换默认放的包名
                 */
                String id = mSharedDefaultMap.get(key).getId();
                int pos = -1;
                SharedItem tmp = null;
                for (int i = 0; i < mSharedDataList.size(); i++) {
                    tmp = mSharedDataList.get(i);
                    if (tmp.getId().equals(id)) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1) {
                    tmp = mSharedDataList.remove(pos);
                    tmp.setPkg(packageName);
                    tmp.setClassName(className);
                    tmp.setDesc(resolveInfo.loadLabel(mPackageManager).toString());
                    // si.setIcon(resolveInfo.activityInfo.loadIcon(mPackageManager));
                    tmp.setInstalled(true);
                    mSharedDataList.add(pos, tmp);
                }
            }
        }
    }

    /**
     * 根据id来选择分享的方式
     *
     * @param id
     * @param mContent
     * @param mUri
     */
    private void shareOneWay(String id, String mContent, ArrayList<Uri> mUri) {
        if (null == mUri) {
            MyPhoneUtil.showOnlyToast(mContext, R.string.settings_home_apps_shared_pic_not_exist);
            return;
        }

        SharedItem sharedItem = null;
        for (int i = 0; i < mSharedDataList.size(); i++) {
            if (mSharedDataList.get(i).getId().equals(id)) {
                sharedItem = mSharedDataList.get(i);
                break;
            }
        }
        if (null == sharedItem)
            return;

        if (null == mContent)
            mContent = "";
        if (sharedItem.isInstalled()) {
//          Intent intent = new Intent(Intent.ACTION_SEND);// 单张图片
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, mContent);
            intent.putExtra("sms_body", mContent); // 适配短信
            intent.putExtra("Kdescription", mContent);// 适配微信

            intent.setType("image/*");
//          intent.putExtra(Intent.EXTRA_STREAM, mUri);// 单张图片
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mUri);

            intent.setComponent(new ComponentName(sharedItem.getPkg(), sharedItem.getClassName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                MyPhoneUtil.showOnlyToast(mContext, String.format(mContext.getString(R.string.settings_home_apps_shared_not_install), sharedItem.getInstallName()));
            }
        } else {
            MyPhoneUtil.showOnlyToast(mContext, String.format(mContext.getString(R.string.settings_home_apps_shared_not_install), sharedItem.getInstallName()));
        }
    }
    /**
     * 分享：微信朋友圈
     *
     * @param mContent
     * @param uri
     */
/*
    public void shareWechatMoments(String mContent, ArrayList<Uri> uri) {
        shareOneWay(COM_TENCENT_MM + "2", mContent, uri);
    }
*/

    /**
     * 分享：微信好友
     *
     * @param mContent
     * @param uri
     */
/*
    public void shareWechatFriend(String mContent, ArrayList<Uri> uri) {
        shareOneWay(COM_TENCENT_MM + "1", mContent, uri);
    }
*/

    /**
     * 分享：微博
     *
     * @param mContent
     * @param uri
     */
/*    public void shareWeibo(String mContent, ArrayList<Uri> uri) {
        shareOneWay(COM_SINA_WEIBO, mContent, uri);
    }*/

    /**
     * 分享：qq空间
     *
     * @param mContent
     * @param uri
     */
/*    public void shareQZone(String mContent, ArrayList<Uri> uri) {
        shareOneWay(COM_QZONE, mContent, uri);
    }*/

}
