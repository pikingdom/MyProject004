/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : IUserModule
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-5 下午08:01:41 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.List;

import android.content.Context;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.SendSuggestInfo;

public interface IUserModule
{

	///////////////////////////////// 城市管理 //////////////////////////////////
	// add by chenx, 2012.4.24
	//
	// 获取城市列表
	public int GetCityList(List<CityInfo> cityList);
	// 更新城市信息
	public boolean UpdateCityInfo(CityInfo cityInfo);
	//添加城市信息
	public int SetCityInfo(CityInfo cityInfo, boolean bAutoUpdate);
	//删除城市信息
	public boolean DeleteCityInfo(CityInfo cityInfo);
	
	//设置自动定位城市信息
	public boolean setLocationCity(Context ctx, CityInfo city);
	
	/*获取自动定位城市*/
	public CityInfo getLocationCity(Context ctx, int id);
	
	/////////////////////////////////////////////////////////////////////////
//	//添加城市信息
//	public int InsertWeathInfo(ListInfo listInfo, boolean bFlag);
	
	//获取所有保存的天气信息
//	public int GetWeahInfoList(List<ListInfo> listweathInfo);
//	public int GetWeahInfoList(List<ListInfo> listweathInfo, int size);

//	//更新城市信息
//	public boolean UpdateWeathInfo(ListInfo listInfo);
	//删除城市信息
//	public boolean DeleteWeathInfo(ListInfo listInfo);
	
//	// 获取软件推送信息
//	public int GetSoftInfo(Vector<SoftTypeInfo> softTypeInfos);
//	//获取所有操作信息
//	public int GetSoftInfoExt(String strType, List<SoftInfoExt> softInfoExts);
//	//获取推荐版本号
//	public int GetSoftAdAppInfoId();
//	//获取类别数据
//	public int GetSoftTypeName(Vector<String> vectType);
//	public int UpdateSoftInfoExt(SoftInfoExt softInfoExt);
	
	
	/////////////////////////////////////////////////////////////////////////
	
	//获取意见反馈缓存数据
	public boolean GetAllsuggestInfo(List<SendSuggestInfo> listSendSuggestInfo);
//	
//	// 获得电商列表
//	public boolean getECommerceInfos(ArrayList<Object> esList);
//
//	// 更新电商列表
//	public boolean updateECommerceInfos(ArrayList<ElecSupplierInfo> esList);
//	
//	// 获取指定电商
//	public ElecSupplierInfo getECommerceInfo(String sID);

}
