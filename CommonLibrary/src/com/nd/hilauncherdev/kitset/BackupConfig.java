package com.nd.hilauncherdev.kitset;

import android.content.Context;
import android.database.Cursor;

import com.nd.hilauncherdev.datamodel.db.MyPhoneDB;
import com.nd.hilauncherdev.launcher.config.preference.SettingsConstants;

public class BackupConfig {
	
	public static final String BACKUP_BEFORE_UNINSTALL_SELECT = "select value from Config where id = '" + SettingsConstants.SETTINGS_BACKUP_BEFORE_UNINSTALL + "'";
	public static final String BACKUP_BEFORE_UNINSTALL_INSERT = "insert into Config values('" + SettingsConstants.SETTINGS_BACKUP_BEFORE_UNINSTALL + "', 'Y')";
	public static final String BACKUP_BEFORE_UNINSTALL_DELETE = "delete from Config where id='" + SettingsConstants.SETTINGS_BACKUP_BEFORE_UNINSTALL + "'";
	
	/**
	 * 是否开启卸载前备份
	 * 
	 * @param context
	 * @return
	 */
	/*public static boolean isBackupBeforeUninstall(Context context) {
		try {
			MyPhoneDB db = MyPhoneDB.getInstance(context);
			Cursor c = db.query(BACKUP_BEFORE_UNINSTALL_SELECT);
			boolean hadInit = c.moveToNext();
			c.close();
			db.close();
			return hadInit;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}*/

	/**
	 * 开启/关闭开启卸载前备份
	 * 
	 * @param context
	 * @param silentInstallable
	 */
	/*public static void setBackupBeforeUninstall(Context context, boolean backupBeforeUninstall) {
		try {
			MyPhoneDB db = MyPhoneDB.getInstance(context);
			if (backupBeforeUninstall)
				db.execSQL(BACKUP_BEFORE_UNINSTALL_INSERT);
			else
				db.execSQL(BACKUP_BEFORE_UNINSTALL_DELETE);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
