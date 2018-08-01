/**   
 *    
 * @file 【网络请求接口】
 * @brief
 *
 * @<b>文件名</b>      : IHttpModle
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-3 上午11:07:25 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.Date;

import com.calendar.CommData.DateInfo;
import com.calendar.CommData.ICommData;

public interface IHttpModle
{
	//public void Init(IDatabaseRef dataBaseRef);
	
	//获取版本信息
	public boolean DownverInfo(int appid);
	
	//提交意见
	public boolean DownSuggestanswer(int appid, String strContactinfo, String strSuggest);
	//根据内容从服务端获取回答
	public boolean GetListAnswer(int appid);
	
//	//下载软件推荐信息
//	public boolean DownSoftAdInfo(int nAppId, int nAppVersion, int nSoftVersoin);

	// 网络调用函数
	public boolean InvokeNetWorkAppFun(String sid, int nFunId, ICommData param,
			ICommData result, Date stdDate);

	// 网络获取主人信息
//	public boolean GetHostPeopInfo(ICommData result);
	
	public int DownWidgetSkinListInfo(ICommData widgetSkinListOut, int nPage, int nPageSize);

	// 下载电商列表
//	public boolean getECommerceInfo(int appid, String maxVer, ArrayList<ElecSupplierInfo> outList, StringBuilder sOutMaxVer);
	
	// 获取系统时间
	public String GetServerDate();
//	
//	public boolean UploadPeopleList(String sid, ICommData result);
//	
//	public boolean GetPeopleList(String sid, ICommData result, int iVersion);
//	
//	public int initPeopleList(String strUserName, String sid);
	
	// 获取系统时间
	public void GetLoginServerDate(DateInfo date);
	
	//获取软件版本
	public String getM_sVerCode();
}
