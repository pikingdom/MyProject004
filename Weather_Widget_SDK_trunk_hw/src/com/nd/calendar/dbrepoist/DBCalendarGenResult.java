/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : DBCalendarResult
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2013-2-26 下午04:49:47 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.calendar.dbrepoist;


/**
 * @brief 【】
 * @n<b>类名称</b>     :
 * @<b>作者</b>          :  陈希
 * @<b>修改</b>          :
 * @<b>创建时间</b>      :  2013-2-26下午04:49:47
 * @<b>修改时间</b>      :
 */

public class DBCalendarGenResult
{
//    private static DBCalendarGenResult _instance = null;
//    
//   // 数据库接口
//    private IDatabaseRef m_dataBaseRef = null;
//     
//    private DBCalendarGenResult(Context ctx, IDatabaseRef dataBaseRef) {
//        m_dataBaseRef = dataBaseRef;
//        createTable();
//    }
//
//    public synchronized static DBCalendarGenResult getInstance(Context ctx, IDatabaseRef dataBaseRef) {
//        if (_instance == null) {
//            _instance = new DBCalendarGenResult(ctx, dataBaseRef);
//        }
//        
//        return _instance;
//    }
//    
//    private void createTable() {
//    	if (m_dataBaseRef == null) {
//			return;
//		}
//
//        if (!m_dataBaseRef.TableExist("advices")) {
//        	createAdvicesTable();
//        }
//
//        if (!m_dataBaseRef.TableExist("calendar")) {
//        	createCalendarTable();
//        }
//    }
//    
//    private boolean createAdvicesTable() {
//        String str = "CREATE TABLE [advices] ([id] INTEGER PRIMARY KEY AUTOINCREMENT,[des] nvarchar);";
//        return m_dataBaseRef.RunSql(str);
//    }
//    
//    private boolean createCalendarTable() {
//        String str = "CREATE TABLE [calendar] ([date] DATETIME,[appropriate] integer,[avoid] integer,[chong] integer);";
//        if (!m_dataBaseRef.RunSql(str)) {
//            return false;
//        }
//
//        return true;
//    }
//    
//    public void start() {
//    	m_dataBaseRef.beginTransaction();
//    }
//    
//    public void setSuccess() {
//    	m_dataBaseRef.setTransactionSuccessful();
//    }
//    
//    public void end() {
//    	m_dataBaseRef.endTransaction();
//    }
//    
//    public int saveYiJi(String sDes) {
//    	if (m_dataBaseRef.RunSql("INSERT INTO advices ([des]) VALUES (?)",
//				new Object[] {sDes})) {
//    		
//            String strsql = "SELECT last_insert_rowid()";
//            Cursor cursor = m_dataBaseRef.RawQuery(strsql, null);
//
//            if (cursor != null) {
//    	        try {
//    	            if (cursor.moveToFirst()) {
//    	                return cursor.getInt(0);
//    	            }
//    	        } finally {
//    	            cursor.close();
//    	        }
//            }
//		}
//    	
//        return -1;
//    }
//    
//    public int findYiJi(String sDes) {
//        String strsql = "select id from advices where des=?";
//        Cursor cursor = m_dataBaseRef.RawQuery(strsql, new String[] {sDes});
//
//        if (cursor != null) {
//	        try {
//	            if (cursor.moveToNext()) {
//	                return cursor.getInt(0);
//	            }
//	        } finally {
//	            cursor.close();
//	        }
//        }
//
//        return -1;
//    }
//    
//    public boolean saveCalendar(String sDate, int appropriate, int avoid, int iChong) {
//    	return m_dataBaseRef.RunSql("Insert Into calendar ([date],[appropriate],[avoid],[chong]) values (?,?,?,?)",
//				new Object[] {sDate, appropriate, avoid, iChong});
//    }
}
