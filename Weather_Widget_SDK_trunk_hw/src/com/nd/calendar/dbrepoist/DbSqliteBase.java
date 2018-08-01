/**   
 *    
 * @file  【数据库基类】
 * @brief
 *
 * @<b>文件名</b>      : DbSqliteBase
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-27 上午11:16:59 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nd.calendar.util.FileHelp;

public class DbSqliteBase implements IDatabaseRef
{
	// 用于打印log
	private static final String TAG = "DbSqliteBase";

	private final int BUFFER_SIZE = 1024*4;

	// 本地Context对象
	private Context m_Context = null;

	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase m_SQLiteDatabase = null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper m_DatabaseHelper = null;

	// 是否打数据库
	boolean m_IsOpen = false;
	int mVer = -1;
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, null, version);
		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			mVer = oldVersion;
			
			onCreate(db);
		}
	}

	/* 构造函数-取得Context */
	public DbSqliteBase(Context context) {
		m_Context = context.getApplicationContext();
		if (m_Context == null) {
			m_Context = context;
		}
	}

	/**
	 * @brief 【默认创建数据库并且打开】
	 * 
	 * 
	 * @n<b>函数名称</b> :open
	 * @see
	 * 
	 * @param @param strDbName
	 * @param @param nVersion
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-28下午08:55:10
	 */
	@Override
	public boolean open(String strDbName, String user, String password, int nVersion) {
		try {
			m_DatabaseHelper = new DatabaseHelper(m_Context, strDbName, null, nVersion);
			m_SQLiteDatabase = m_DatabaseHelper.getWritableDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
			m_IsOpen = false;
			return false;
		}

		m_IsOpen = true;
		//RunSql("VACUUM");

		return true;
	}
	
	/**
	 * @brief 【】
	 * @n<b>函数名称</b>     :copyStreamToFile
	 * @param inputSteam
	 * @param strfilePath
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-11-23上午11:19:14
	 * @<b>修改时间</b>      :
	*/
	int copyStreamToFile(InputStream inputSteam, File fileDB) {
		try {
			if (!fileDB.exists()) {
				FileOutputStream fos = new FileOutputStream(fileDB);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = inputSteam.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				
				return 2;
			}
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, "open database error, File not found");
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			Log.e(TAG, "open database error, IO exception");
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}

	
	/**
	 * @brief 【通过资源打开导入数据库】
	 * 
	 * 
	 * @n<b>函数名称</b> :open
	 * @see
	 * 
	 * @param @param inputSteam
	 * @param @param strDbFile
	 * @param @param nVersion
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-28下午08:54:22
	 */
	@Override
	public boolean open(InputStream inputSteam, String strDbFile, int nVersion) {
		//strDbFile = FileHelp.GetDBNameWithVer(strDbFile, nVersion);
		String strfilePath = FileHelp.GetPhoneDatabase(m_Context, strDbFile);
		File file = new File(strfilePath);

		// 复制数据库
		if (copyStreamToFile(inputSteam, file) == 1) {
			// 打开数据库，并检查数据库版本
			if (open(strDbFile, "", "", nVersion) && mVer != -1 && mVer < (nVersion)) {
				// 新版本数据库，关闭已打开的，删除数据库文件
				close();
				m_IsOpen = false;
				file.delete();
				
				copyStreamToFile(inputSteam, file);
			}
		}
		
		try {
			inputSteam.close();
		} catch (Exception e) {
		}
		
		if (!m_IsOpen) {
			open(strDbFile, "", "", nVersion);
			m_IsOpen = true;
		}

		return true;
	}

	/**
	 * @brief【执行ＳＱＬ语句】
	 * 
	 * 
	 * @n<b>函数名称</b> :RunSql
	 * @see
	 * 
	 * @param @param strSql
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午01:53:26
	 */
	@Override
	public boolean RunSql(String strSql) {
		if (m_IsOpen) {
			try {
				m_SQLiteDatabase = m_DatabaseHelper.getWritableDatabase();
				m_SQLiteDatabase.execSQL(strSql);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			Log.e(TAG, "You don't open database ,so Calling RunSql error ");
			return false;
		}
	}

	/**
	 * @brief【执行ＳＱＬ语句，带参数】
	 * 
	 * 
	 * @n<b>函数名称</b> :RunSql
	 * @see
	 * 
	 * @param @param strSql: sql语句
	 * @param @param bindArgs ： 参数
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午01:54:30
	 */
	@Override
	public boolean RunSql(String strSql, Object[] bindArgs) {
		if (m_IsOpen) {
			try {
				m_SQLiteDatabase = m_DatabaseHelper.getWritableDatabase();
				m_SQLiteDatabase.execSQL(strSql, bindArgs);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			Log.e(TAG, "You don't open database ,so Calling RunSql error ");
			return false;
		}
	}

	/**
	 * @brief 【更加sq语句获取数据】
	 * 
	 * 
	 * @n<b>函数名称</b> :RawQuery
	 * @see
	 * 
	 * @param @param strSql :sql语句
	 * @param @param selectionArgs:参数
	 * @param @return
	 * @return Cursor
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午02:06:34
	 */
	@Override
	public Cursor RawQuery(String strSql, String[] selectionArgs) {
		if (m_IsOpen) {
			Cursor cursor = null;
			try {
				m_SQLiteDatabase = m_DatabaseHelper.getReadableDatabase();
				cursor = m_SQLiteDatabase.rawQuery(strSql, selectionArgs);
			} catch (SQLException e) {
				e.printStackTrace();
				return cursor;
			}
			return cursor;
		} else {
			Log.e(TAG, "You don't open database ,so Calling RawQuery error ");
			return null;
		}
	}

	/**
	 * @brief 【began affaris】
	 * 
	 * 
	 * @n<b>函数名称</b> :beginTransaction
	 * @see
	 * 
	 * @param
	 * @return void
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午02:30:06
	 */
	@Override
	public void beginTransaction() {
		if (m_IsOpen) {
			if (!m_SQLiteDatabase.inTransaction()) {
				m_SQLiteDatabase.beginTransaction();
			}
		} else {
			Log.e(TAG, "You don't open database ,so Calling beginTransaction error ");
		}
	}

	/**
	 * @brief 【Submit affairs】
	 * 
	 * 
	 * @n<b>函数名称</b> :endTransaction
	 * @see
	 * 
	 * @param
	 * @return void
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午02:31:56
	 */
	@Override
	public void endTransaction() {
		if (m_IsOpen) {
			if (m_SQLiteDatabase.inTransaction()) {
				m_SQLiteDatabase.endTransaction();
			}
		} else {
			Log.e(TAG, "You don't open database ,so Calling endTransaction error ");
		}
	}

	/**
	 * @brief 【Notice to submit affairs success】
	 * 
	 * 
	 * @n<b>函数名称</b> :setTransactionSuccessful
	 * @see
	 * 
	 * @param
	 * @return void
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午02:35:07
	 */
	@Override
	public void setTransactionSuccessful() {
		if (m_IsOpen) {
			if (m_SQLiteDatabase.inTransaction()) {
				m_SQLiteDatabase.setTransactionSuccessful();
			}
		} else {
			Log.e(TAG, "You don't open database ,so Calling setTransactionSuccessful error ");
		}
	}

	/**
	 * @brief 【close database】
	 * 
	 * 
	 * @n<b>函数名称</b> :close
	 * @see
	 * 
	 * @param
	 * @return void
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-27下午02:37:38
	 */
	@Override
	public void close() {
		if (m_IsOpen) {
			m_SQLiteDatabase.close();
		} else {
			Log.e(TAG, "You don't open database ,so Calling close error ");
		}

	}

	/**
	 * @brief 【判断表是否存在】
	 * 
	 * 
	 * @n<b>函数名称</b> :TableExist
	 * @see
	 * 
	 * @param @param strTabale
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午05:58:43
	 */
	@Override
	public boolean TableExist(String strTabale) {
		Cursor cursor = RawQuery(
				"select count(*) as number from sqlite_master where type='table' and name=? ",
				new String[] { strTabale });
		int number = 0;
		while (cursor != null && cursor.moveToNext()) {
			number = cursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
		}

		cursor.close();

		if (number == 0) {
			return false;
		}
		return true;

	}

	@Override
	public Object RawQuery(String strSql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataBasePath(String fileName) {
		return FileHelp.GetPhoneDatabase(m_Context, fileName);
	}

}
