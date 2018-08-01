/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : CityStruct
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-6-15 下午03:37:20 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.calendar.CommData;


public class CityStruct
{
	// 请求编号
	private String	code;
	// 城市名
	private String	name;

	//所属省名
	private String provName;
	
    /* 是否由GPS定位出的 */
    protected int mGPS = 0;
    
	public int getGPS() {
		return mGPS;
	}

	public void setGPS(int mGPS) {
		this.mGPS = mGPS;
	}

	public CityStruct()
	{
	}
	
	public String getProvName()
	{
		return provName;
	}

	public void setProvName(String provName)
	{
		this.provName = provName;
	}

	public CityStruct(String name, String code)
	{
		this.name = name;
		this.code = code;
	}
	
	public CityStruct(String name, String code, int gps)
	{
		this.name = name;
		this.code = code;
		mGPS = gps;
	}
	
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
