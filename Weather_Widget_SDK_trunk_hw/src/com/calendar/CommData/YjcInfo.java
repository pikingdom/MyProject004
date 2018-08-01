/**   
 *    
 * @file  【宜忌冲类】
 * @brief
 *
 * @<b>文件名</b>      : YjcInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-27 下午02:51:36 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.calendar.CommData;

public class YjcInfo {
	// strYi:宜
	private String strYi;
	// strJi:忌
	private String strJi;
	// strChong:冲
	private String strChong;

	public YjcInfo() {
		strYi = "";
		// strJi:忌
		strJi = "";
		// strChong:冲
		strChong = "";
	}

	public String getStrYi() {
		return strYi;
	}

	public void setStrYi(String strYi) {
		this.strYi = strYi;
	}

	public String getStrJi() {
		return strJi;
	}

	public void setStrJi(String strJi) {
		this.strJi = strJi;
	}

	public String getStrChong() {
		return strChong;
	}

	public void setStrChong(String strChong) {
		this.strChong = strChong;
	}

}
