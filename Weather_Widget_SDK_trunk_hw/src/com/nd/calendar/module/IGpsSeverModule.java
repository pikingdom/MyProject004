/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : IGpsSeverModule
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2011-11-4 上午11:50:13 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.calendar.module;

import com.calendar.CommData.CityStruct;

public interface IGpsSeverModule
{


	// 【获得GPS定位信息】
	public abstract int GetGpsInfo(CityStruct cityInfo);

	//【获得 GPS 状态】
	public abstract boolean GetGpsOpenState();

	public abstract void SetGpsOpenState(boolean bOPen);
	
	// 根据经度和纬度获取城市代码
	public boolean GetGpsInfoFromServer(double latitude, double longitude, CityStruct cityInfo);

}