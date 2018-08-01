/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : SearchInfo
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-4-23 下午03:11:31 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.CommData;


public class SearchInfo
{
	private String text;		// 标题
	private String note;		// 提示
	private boolean recent;		// 是否是最近搜索
	private Object tag;

	public String getText() {
		return text;
	}

	public String getNote() {
		return note;
	}

	public boolean isRecent() {
		return recent;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setRecent(boolean recent) {
		this.recent = recent;
	}
	
	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}
