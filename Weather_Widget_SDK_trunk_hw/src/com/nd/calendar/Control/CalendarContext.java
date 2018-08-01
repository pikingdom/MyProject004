/**   
 *    
 * @file 【接口管理类】
 * @brief
 *
 * @<b>文件名</b>      : CalendarContext
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 上午11:52:43 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.Control;

import android.content.Context;
import android.content.res.Resources;

import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.dbrepoist.DbSqliteBase;
import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.module.CalendarModule;
import com.nd.calendar.module.CoustoModule;
import com.nd.calendar.module.GpsSeverModule;
import com.nd.calendar.module.HttpModle;
import com.nd.calendar.module.ICoustoModule;
import com.nd.calendar.module.IGpsSeverModule;
import com.nd.calendar.module.IHttpModle;
import com.nd.calendar.module.IUserModule;
import com.nd.calendar.module.UserModule;
import com.nd.calendar.module.WeatherModule;
import com.nd.weather.widget.R;

public class CalendarContext implements ICalendarContext {
    
    public CalendarContext(Context context) {
        m_context = context.getApplicationContext();
        if (m_context == null) {
			m_context = context;
		}
    }
	
    /**
     * @brief 【创建单体实例】
     * 
     * 
     * @n<b>函数名称</b> :getInstance
     * @see
     * 
     * @param @return
     * @return Calendar ：【返回单体实例对象】
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-4-12下午04:40:53
     */
    public static synchronized CalendarContext getInstance(Context context) {
        if (g_Instance == null) {
            g_Instance = new CalendarContext(context);
        }
        return g_Instance;
    }

    /**
     * @brief 【获取宜忌冲等功能的接口】
     * 
     * 
     * @n<b>函数名称</b> :Get_CoustoMdl_Interface
     * @see
     * 
     * @param @return ICoustoModule：获取宜忌冲等功能的接口
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-4-28下午02:00:11
     */
    @Override
    public synchronized ICoustoModule Get_CoustoMdl_Interface() {
        if (m_CounstoModule == null) {
            m_CounstoModule = new CoustoModule();
            m_CounstoModule.Init(GetDbAccessItf(ComDataDef.DbInfo.R_DB_CUSTOM_TYPE));
        }
        
        return m_CounstoModule;
    }

    /**
     * @brief 【天气模块接口】
     *
     * @n<b>函数名称</b>     :Get_WeatherMdl_Interface
     * @return
     * @<b>作者</b>          :  陈希
     * @<b>创建时间</b>      :  2012-11-23下午03:21:50      
    */
    public synchronized WeatherModule Get_WeatherMdl_Interface() {
        if (mWeatherModule == null) {
            mWeatherModule = new WeatherModule(m_context, GetDbAccessItf(ComDataDef.DbInfo.R_DB_USER_TYPE));
        }

        return mWeatherModule;
    }

    /**
     * @brief 【用户数据库接口】
     *
     * @n<b>函数名称</b>     :Get_UserMdl_Interface
     * @return
     * @<b>作者</b>          :  陈希
     * @<b>创建时间</b>      :  2012-11-23下午03:47:36      
    */
    @Override
    public synchronized IUserModule Get_UserMdl_Interface() {
        if (m_UserModule == null) {
            m_UserModule = new UserModule(m_context, GetDbAccessItf(ComDataDef.DbInfo.R_DB_USER_TYPE));
        }

        return m_UserModule;
    }

    public synchronized CalendarModule Get_CalendarMdl_Interface() {
        if (mCalendarModule == null) {
        	mCalendarModule = new CalendarModule(m_context);
        }

        return mCalendarModule;
    }
    /**
     * @brief 【 获取访问哪个数据库接口】
     * 
     * 
     * @n<b>函数名称</b> :GetDbAccessItf
     * @see
     * 
     * @param @param nDbType ：要获取哪个库接口的类型
     * @param @return
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-4-28下午01:58:16
     */
    @Override
    public synchronized IDatabaseRef GetDbAccessItf(int nDbType) {

    	if (nDbType == ComDataDef.DbInfo.R_DB_CUSTOM_TYPE) {
    		if (dbRefCustom == null) {
                Resources res = m_context.getResources();
                dbRefCustom = new DbSqliteBase(m_context);
                
                if (!dbRefCustom.open(
                		res.openRawResource(R.raw.calendar),
                		ComDataDef.DbInfo.R_DB_CUSTOM,
                        ComDataDef.DbInfo.R_DB_CUSTOM_VERSION)) {
                	dbRefCustom = null;
                }
			}

            return dbRefCustom;
            
		} else if (nDbType == ComDataDef.DbInfo.R_DB_USER_TYPE) {
			if (dbRefUser == null) {
				dbRefUser = new DbSqliteBase(m_context);
	            if (!dbRefUser.open(ComDataDef.DbInfo.R_DB_USER, "", "",
	                    ComDataDef.DbInfo.R_DB_USER_VERSION)) {
	            	dbRefUser = null;
	            }
			}

			return dbRefUser;
		}
    	
    	return null;
    }

    /**
     * @brief 【 获取网络功能接口】
     * 
     * 
     * @n<b>函数名称</b> :Get_HttpMdl_Interface
     * @see
     * 
     * @param @return
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-5-3上午11:29:44
     */
    @Override
    public synchronized IHttpModle Get_HttpMdl_Interface() {
        if (m_HttpModle == null) {
            m_HttpModle = new HttpModle(m_context, GetDbAccessItf(ComDataDef.DbInfo.R_DB_USER_TYPE));
        }
        
        return m_HttpModle;
    }

    /**
     * @brief 【GPS接口】
     *
     * @n<b>函数名称</b>     :Get_GpsMdl_Interface
     * @return
     * @<b>作者</b>          :  陈希
     * @<b>创建时间</b>      :  2012-11-23下午03:22:12      
    */
    public synchronized IGpsSeverModule Get_GpsMdl_Interface() {
        if (m_GpsSeverModule == null) {
            m_GpsSeverModule = new GpsSeverModule(m_context);
        }

        return m_GpsSeverModule;
    }

    
    @Override
    public Context Getcontext() {
        return m_context;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // android上下文
    private Context m_context = null;
    
    // g_Instance:单体实例对象
    private static CalendarContext g_Instance = null;
    // 宜忌冲等功能接口
    private ICoustoModule m_CounstoModule = null;
    // 网络请求接口
    private IHttpModle m_HttpModle = null;
    
    // 用户操作接口
    private IUserModule m_UserModule = null;

    private IGpsSeverModule m_GpsSeverModule = null;
    
    private WeatherModule mWeatherModule = null;;

    private CalendarModule mCalendarModule = null;
    
    private IDatabaseRef dbRefUser = null;
    private IDatabaseRef dbRefCustom = null;

}
