/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : ICommData
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-3 下午03:24:29 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.calendar.CommData;

import org.json.JSONObject;

public interface ICommData
{
	// 日历项目基本类类型
	public static final class DataType
	{
		// DATE_INFO_TYPE:时间类型判断
		public static final int	DATE_INFO_TYPE			= 0xA0000001;
		// LUAN_INFO_TYPE:农历信息类型
		public static final int	LUAN_INFO_TYPE			= 0xA0000002;
		// TDYGOOD_INFO_TYPE:今日吉时类型
		public static final int	TDYGOOD_INFO_TYPE		= 0xA0000003;
		// JIANPING_LUCK_INFO:开运指南、今日简评
		public static final int	JIANPING_LUCK_INFO_TYPE	= 0xA0000004;
		// PEP_INFO_TYPE:人员类型
		public static final int	PEP_INFO_TYPE			= 0xA0000005;
	}
	
	// 类型
	public int GetType();

	// 转化成json
	public JSONObject ToJsonObject();

	// 转化成本地数据
	public void SetJsonString(String strJson);
}
