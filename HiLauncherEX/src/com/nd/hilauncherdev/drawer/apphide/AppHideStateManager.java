package com.nd.hilauncherdev.drawer.apphide;


import com.nd.hilauncherdev.drawer.data.DrawerPreference;

/**
 * description: 隐藏程序状态管理<br/>
 * author: dingdj<br/>
 * date: 2014/10/13<br/>
 */
public class AppHideStateManager {

    /**
     * 未使用隐藏App功能
     */
    public static final int UN_USE_APP_HIDE_FUNC = 0;
    /**
     * 使用隐藏App功能并且锁定了
     */
    public static final int USED_APP_HIDE_FUN_AND_LOCK = 1;

    /**
     * 使用隐藏App功能且未锁定了
     */
    public static final int USED_APP_HIDE_FUN_AND_UNLOCK = 2;

    /**
     * 获取当前状态
     *
     * @return 当前所处于的状态
     */
    public static int getAppHideState() {
        if(DrawerPreference.getInstance().isUsedHideAppFunc()) {
              if(DrawerPreference.getInstance().isHideAppEncrypt()) {
                    return USED_APP_HIDE_FUN_AND_LOCK;
              }else{
                  return USED_APP_HIDE_FUN_AND_UNLOCK;
              }
        }else{
            return UN_USE_APP_HIDE_FUNC;
        }
    }

}
