package com.nd.hilauncherdev.kitset.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;

import com.nd.hilauncherdev.datamodel.db.MyPhoneDB;
import com.nd.hilauncherdev.framework.constants.MyphoneConstants.AppRunningLockSql;

public class AppRunningDbUtil {
	private static AppRunningDbUtil instance;
	private Context context;

	public static AppRunningDbUtil getInstance(Context context) {
		if (instance == null)
			instance = new AppRunningDbUtil(context.getApplicationContext());
		return instance;
	}

	public AppRunningDbUtil(Context context) {
		this.context = context;
	}

	/**
	 * 获取锁定的进程
	 * 
	 * @return
	 */
	public Map<String, Boolean> getLockInfo() {
		MyPhoneDB db = null;
		Cursor c = null;
		Map<String, Boolean> resMap = new HashMap<String, Boolean>();
		try {
			db = MyPhoneDB.getInstance(context);
			c = db.query(AppRunningLockSql.SELECTLOCK);
			final int pkgIndex = c.getColumnIndexOrThrow(AppRunningLockSql.PKG);
			final int lockIndex = c.getColumnIndexOrThrow(AppRunningLockSql.LOCK);
			while (c.moveToNext()) {
				String pkg = c.getString(pkgIndex);
				if (pkg == null)
					continue;
				int lock = c.getInt(lockIndex);
				if (!isAppUninstall(pkg))
					resMap.put(pkg, lock == 1 ? true : false);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (c != null && !c.isClosed())
				c.close();
			if (db != null)
				db.close();

		}
		return resMap;
	}

	/**
	 * 应用是否已经卸载
	 * 
	 * @param pkgName
	 * @return
	 */
	private boolean isAppUninstall(String pkgName) {
		try {
			PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
			if (pkgInfo != null)
				return false;
		} catch (NameNotFoundException e) {
		}
		return true;
	}

	/**
	 * 插入数据
	 * 
	 * @param pkg
	 * @return
	 */
	/*public boolean insert(String pkg) {
		MyPhoneDB db = null;
		boolean res = false;
		try {
			db = MyPhoneDB.getInstance(context);
			res = db.execSQL(String.format(AppRunningLockSql.INSERT, pkg, 1));
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}*/

	/**
	 * 批量插入
	 * 
	 * @param pkgs
	 * @return
	 */
	public boolean insert(List<String> pkgs) {
		MyPhoneDB db = null;
		boolean res = false;
		try {
			if (pkgs != null && pkgs.size() > 0) {
				db = MyPhoneDB.getInstance(context);
				String[] sqls = new String[pkgs.size()];
				for (int i = 0; i < pkgs.size(); i++) {
					sqls[i] = String.format(AppRunningLockSql.INSERT, pkgs.get(i), 1);
				}
				res = db.execBatchSQL(sqls, true);
			}
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}

	/**
	 * 删除
	 * 
	 * @param pkg
	 * @return
	 */
	/*public boolean deleteByPkg(String pkg) {
		MyPhoneDB db = null;
		boolean res = false;
		try {
			db = MyPhoneDB.getInstance(context);
			res = db.execSQL(String.format(AppRunningLockSql.DELETE, pkg));
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}
*/
	/**
	 * 批量删除
	 * 
	 * @param pkgs
	 * @return
	 */
	/*public boolean deleteByPkg(List<String> pkgs) {
		MyPhoneDB db = null;
		boolean res = false;
		try {
			if (pkgs != null && pkgs.size() > 0) {
				db = MyPhoneDB.getInstance(context);
				String[] sqls = new String[pkgs.size()];
				for (int i = 0; i < pkgs.size(); i++) {
					sqls[i] = String.format(AppRunningLockSql.DELETE, pkgs.get(i));
				}
				res = db.execBatchSQL(sqls, true);
			}
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}*/

	/**
	 * 批量删除
	 * 
	 * @param pkgs
	 * @return
	 */
	public boolean deleteAll() {
		MyPhoneDB db = null;
		boolean res = false;
		try {
			db = MyPhoneDB.getInstance(context);
			res = db.execSQL(AppRunningLockSql.DELETE_ALL);
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}
}
