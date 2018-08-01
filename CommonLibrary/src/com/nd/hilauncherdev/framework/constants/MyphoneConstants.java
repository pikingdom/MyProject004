package com.nd.hilauncherdev.framework.constants;

public class MyphoneConstants {
	/**
	 * 正在运行表
	 * 
	 * @author youy
	 * 
	 */
	public static class AppRunningLockSql {
		// pkg,lock 1,锁定，0未锁定
		public static String CREATE_LOCK_TABLE = "CREATE TABLE IF NOT EXISTS 'AppLockTable' ('pkg' varchar(150) NOT NULL,'lock'INTEGER default 0)";
		public static String SELECTLOCK = "select * from 'AppLockTable' where lock=1";
		public static String INSERT = "insert into AppLockTable values('%s','%d')";
		public static String DELETE = "delete from AppLockTable where pkg='%s'";
		public static String DELETE_ALL = "delete from AppLockTable";
		public static String PKG = "pkg";
		public static String LOCK = "lock";
	}
}
