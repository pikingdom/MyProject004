package com.nd.hilauncherdev.launcher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.theme.data.BaseThemeData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */
public class RetrievalAppInfoHelper {

    private static final String[] CAMERA_LABELS = {"相机", "照像机", "摄像机"};
    private static final String[] GALLERYPICKER_LABELS = {"相册", "图库", "照片"};

    public Map<String, List<String>> mLabelDictionary = new HashMap<String, List<String>>();

    private static RetrievalAppInfoHelper instance = new RetrievalAppInfoHelper();

    private RetrievalAppInfoHelper(){
        mLabelDictionary.put(BaseThemeData.ICON_CAMERA, Arrays.asList(CAMERA_LABELS));
        mLabelDictionary.put(BaseThemeData.ICON_GALLERYPICKER, Arrays.asList(GALLERYPICKER_LABELS));
    }

    public static RetrievalAppInfoHelper getInstance(){
        return instance;
    }

    /**
     * Description: 获取容错后的ComponentName<br>
     * Author: chenxy
     * Date: 2016/9/21 18:28
     *
     * @param targetLabels 待匹配的应用名称列表
     */
    public ComponentName getComponentNameCompat(PackageManager pm, List<String> targetLabels, boolean isFullMatch) {
        ComponentName target = null;
        ResolveInfo info = getResolveInfoCompat(pm, targetLabels, isFullMatch);
        if(info != null){
            target = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
        }
        return target;
    }

    public ResolveInfo getResolveInfoCompat(PackageManager pm, List<String> targetLabels, boolean isFullMatch){
        if (targetLabels == null || targetLabels.size() < 1 || pm == null)
            return null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        if (list == null || list.isEmpty())
            return null;

        ResolveInfo target = null;

        for (ResolveInfo info : list) {
            if (info == null) continue;
            try {
                CharSequence label = info.loadLabel(pm);
                if (label != null) {
                    String covLabel = label.toString();
                    for (int i = 0, len = targetLabels.size(); i < len; i++) {
                        String targetLabel = targetLabels.get(i);
                        if (!StringUtil.isEmpty(targetLabel)) {
                            if(isFullMatch && targetLabel.equals(covLabel)){//全匹配
                                target = info;
                                break;
                            } else if(!isFullMatch && covLabel.contains(targetLabel)) {//模糊匹配
                                target = info;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (target != null) {
                break;
            }
        }
        return target;
    }

    /**
     * Description: 获取标签对应的待匹配label列表<br>
     * Author: chenxy
     * Date: 2016/9/21 18:25
     *
     * @param icon {@link BaseThemeData#ICON_CAMERA} .etc
     */
    public List<String> resolveAppLabels(String icon) {
        if(TextUtils.isEmpty(icon))
            return null;
        List<String> res = mLabelDictionary.get(icon);
        if(res == null){
            if(icon.contains("/")){
                String[] s = icon.split("/");
                icon = s[0];
                res = mLabelDictionary.get(icon);
            }
        }
        return res;
    }
}
