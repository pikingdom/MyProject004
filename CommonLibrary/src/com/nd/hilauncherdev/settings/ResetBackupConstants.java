package com.nd.hilauncherdev.settings;

/**
 * @ProjectName 							CommonLibrary
 * @Author 									C.xt
 * @Version 								1.0.0
 * @CreateDate： 							2013-4-25 下午9:01:05
 * @JDK 									<JDK1.6>
 * Description:								循环奔溃数据备份标识，此文件异常重置桌面不能删除！
 */


public class ResetBackupConstants {

	/**
	 * Preference配置文件名称
	 */
	public final static String CONST_SP_FILE_NAME = "resetflag";

	/**
	 * 循环奔溃数据备份 launcher.db
	 */
	public final static String EXCEPTION_PREPARE_RESET_DATABASE_LAUNCHER = "exception_prepare_reset_database_launcher";

	/**
	 * 循环奔溃数据备份 app.db
	 */
	public final static String EXCEPTION_PREPARE_RESET_DATABASE_APP = "exception_prepare_reset_database_app";
	
	 /**
     * 手动备份
     * 还原重启
     */
	public final static String SETTINGS_PREPARE_RESET_DATA= "settings_prepare_reset_data";
}
