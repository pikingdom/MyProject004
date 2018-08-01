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

import android.content.Context;
import android.database.Cursor;

import com.calendar.CommData.DateInfo;
import com.calendar.CommData.YjcInfo;
import com.nd.calendar.common.ComDataDef.CoustomData;
import com.nd.calendar.common.ComDataDef.DbInfo;

/**
 * @brief 【】
 * @n<b>类名称</b>     :
 * @<b>作者</b>          :  陈希
 * @<b>修改</b>          :
 * @<b>创建时间</b>      :  2013-2-26下午04:49:47
 * @<b>修改时间</b>      :
 */

public class DBCalendarResult
{    
   // 数据库接口
    private IDatabaseRef mDBRef = null;
    private Context mContext = null;
    
    public DBCalendarResult(Context ctx) {
    	mContext = ctx;
    }
    
    /**
     * @brief 【打开数据库】
     * @n<b>函数名称</b>     :open
     * @param sDbName
     * @return
     * @return    boolean   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-5下午05:57:40
     * @<b>修改时间</b>      :
    */
    public boolean open (String sDbName) {
    	
    	mDBRef = new DbSqliteBase(mContext);
        if (!mDBRef.open(sDbName, "", "", DbInfo.R_DB_CALENDAR_VERSION)) {
        	mDBRef = null;
        	return false;
        }
        
        return true;
    }
    
    public void close() {
    	if (mDBRef != null) {
    		mDBRef.close();
		}
    }
    
    /**
     * @brief 【获取宜忌冲数据】
     * @n<b>函数名称</b>     :getYiJiChong
     * @param dateInfo
     * @param yjcInfo
     * @return
     * @return    boolean   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-4下午04:33:25
     * @<b>修改时间</b>      :
    */
    public boolean getYiJiChong(DateInfo dateInfo, YjcInfo yjcInfo) {
    	String sDete = dateInfo.getDateTime(DateInfo.DATE_FORMAT_YYYYMMDD);
    	
        String strsql = "SELECT x.des,y.des,chong FROM calendar " + 
        				"JOIN advices x ON calendar.appropriate=x.id " + 
        				"JOIN advices y ON calendar.avoid=y.id WHERE date='" + sDete + "'";
        
        Cursor cursor = mDBRef.RawQuery(strsql, null);

        if (cursor != null) {
	        try {
	            if (cursor.moveToFirst()) {
	            	yjcInfo.setStrYi(cursor.getString(0)) ;
	            	yjcInfo.setStrJi(cursor.getString(1));
	            	int chong = cursor.getInt(2);
	            	
	            	if (chong >= 0 && chong < CoustomData.TWELVE_CLASH.length) {
	            		yjcInfo.setStrChong(CoustomData.TWELVE_CLASH[chong]);
					}
	            	
	            	return true;
	            }
	        } finally {
	            cursor.close();
	        }
        }
        
        return false;
    }
}
