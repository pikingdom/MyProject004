/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : ICalendarContext
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 上午11:45:49 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.Control;

import android.content.Context;

import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.module.CalendarModule;
import com.nd.calendar.module.ICoustoModule;
import com.nd.calendar.module.IGpsSeverModule;
import com.nd.calendar.module.IHttpModle;
import com.nd.calendar.module.IUserModule;
import com.nd.calendar.module.WeatherModule;

public interface ICalendarContext
{
	public Context	Getcontext();
	//设置软件背景路径
//	public void SetSofeBk(String strPic,boolean bflag);
//	//获取软件背景路径
//	public boolean GetSofeBk(View veiw, Display display);
//	//获取软件背景Drawable资源
//	public Drawable GetSofeBkImage(Display display);
//	//获取当前的风格
//	public int GetSoftType();
	
	//生成发送微博图片到本地
//	public void GenerationImag(View view, String strNewFileName);
	
	// 获取访问哪个数据库接口
	public IDatabaseRef GetDbAccessItf(int nDbType);

	// 获取网络功能接口
	public IHttpModle Get_HttpMdl_Interface();

	// 获取程序数据库接口s
	public ICoustoModule Get_CoustoMdl_Interface();

	// 获取User库功能操作接口
	public IUserModule Get_UserMdl_Interface();
	
	public IGpsSeverModule Get_GpsMdl_Interface();
	
	public WeatherModule Get_WeatherMdl_Interface();
	
	public CalendarModule Get_CalendarMdl_Interface();
}
