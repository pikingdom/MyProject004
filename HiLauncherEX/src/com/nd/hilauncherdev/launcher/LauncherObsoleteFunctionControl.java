package com.nd.hilauncherdev.launcher;

import java.util.ArrayList;

import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

/**
 * 桌面废弃功能管理 要废弃的功能都请先通过此路径判断是否需要做版本控制废弃
 * <p>
 * Title: LauncherObsoleteFunctionControl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: ND
 * </p>
 * 
 * @author MaoLinnan
 * @date 2015年5月8日
 */
public class LauncherObsoleteFunctionControl {
	private static final int MAX_FIRST_INSTALL_VERSION = 999999;
	
	public static final int FUNCTION_MARKING_SCENE_DESK = 1;// 情景桌面
	public static final int FUNCTION_MARKING_SHORTCUTLOG = 2;// 魔力球
	public static final int FUNCTION_MARKING_MYPHONELOG = 3;// 91快捷和我的手机
	public static final int FUNCTION_MARKING_GESTURE_CONTROL = 4;//手势设置
	
	public static final int FUNCTION_MARKING_MOBILE_NETWORK = 5;//移动网络功能再5.0上不能使用所以屏蔽
	
	public static final int FUNCTION_MARKING_TOP_MENU = 6;//控制下滑发现的快捷菜单是否需要屏蔽

	public static boolean isObsoleteFunction(int functionMarking) {
		int firstInstallVersion = MAX_FIRST_INSTALL_VERSION;
		ArrayList<Integer> allInstallVersions = ConfigPreferences.getInstance().getAllInstalledVersionCode();
		for (Integer version : allInstallVersions) {
			firstInstallVersion = version < firstInstallVersion ? version : firstInstallVersion;
		}
		if (firstInstallVersion >= 6498) {// 第一次安装是V6.5
			switch (functionMarking) {
			case FUNCTION_MARKING_SHORTCUTLOG://屏蔽魔力球
				return true;
			case FUNCTION_MARKING_MYPHONELOG://屏蔽我的手机
				return true;
			case FUNCTION_MARKING_GESTURE_CONTROL://调整手势设置
				return true;
			}
		}
		
		if (firstInstallVersion >= 6808){//第一次安装V6.8.1
			switch (functionMarking) {
			case FUNCTION_MARKING_MOBILE_NETWORK:
				int apiLevel = TelephoneUtil.getApiLevel();
				if (apiLevel >=21){//大于5.0需要屏蔽移动网络的功能，替换成蓝牙
					return true;
				}
				break;
			}
		}
		
		if (firstInstallVersion >= 7300){//大于7.3的用户显示下滑发现的推荐应用
			switch (functionMarking) {
			case FUNCTION_MARKING_TOP_MENU:
				return true;
			}
		}
		return false;
	}
}
