/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : CityItem
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-4-24 下午05:38:11 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.calendar.CommData;

public class CityInfo extends BaseCityInfo
{
	private boolean bDeleteState = false;

	public CityInfo() {
		super();
	}
	
	public CityInfo(String name, String code) {
		super(name, code);
	}
	
	public boolean isDeleteState() {
		return bDeleteState;
	}

	public void setDeleteState(boolean bDeleteState) {
		this.bDeleteState = bDeleteState;
	}
}
