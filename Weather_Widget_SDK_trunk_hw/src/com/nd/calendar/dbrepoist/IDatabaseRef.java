/**   
 *    
 * @file  【数据基类接口】
 * @brief
 *
 * @<b>文件名</b>      : IDatabaseRef
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 下午09:16:05 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.io.InputStream;

import android.database.Cursor;

public interface IDatabaseRef
{
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
	public boolean open(String strDbName, String user, String password,
			int nVersion);

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
	public boolean open(InputStream inputSteam, String strDbFile, int nVersion);

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
	public boolean RunSql(String strSql);

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
	public boolean RunSql(String strSql, Object[] bindArgs);

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
	public Cursor RawQuery(String strSql, String[] selectionArgs);
	public Object RawQuery(String strSql);

	/**
	 * @brief 【判断这个表是否存在】
	 * 
	 * 
	 * @n<b>函数名称</b> :TableExist
	 * @see
	 * 
	 * @param @param strTabale
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午05:56:00
	 */
	public boolean TableExist(String strTabale);

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
	public void beginTransaction();

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
	public void endTransaction();

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
	public void setTransactionSuccessful();

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
	public void close();
	
	/**
	 * @Title: getDataBasePath  
	 * @Description: TODO(获取数据库路径)  
	 * @author yanyy
	 * @date 2012-5-15 上午09:38:46 
	 *
	 * @param fileName
	 * @return       
	 * @return String
	 * @throws
	 */
	public String getDataBasePath(String fileName);
}
