/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : IWeatherReslt
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-6-15 下午02:26:12 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.util.List;

import com.calendar.CommData.CityStruct;

public interface IWeatherReslt
{
	// 【初始化】
	public void Init(IDatabaseRef dataBaseRef);

	// 根据拼音、中文获取城市列表
	public int GetCityListByMsg(String strMsg, List<CityStruct> cityStruct);
	
	//获取省
	public int GetAllProv(List<CityStruct> cityStruct);
	public int GetAllProvWithSort(List<CityStruct> cityStruct, String[] ExcludeCitiesName);
	
	//根据省获取地区
	public int GetAllAreaByProvcode(String strCode, List<CityStruct> cityStruct);
	
	//根据地区获取县
	public int GetAllCityByAreacode(String strCode, List<CityStruct> cityStruct);
	
	//根据城市获取citycode
	public boolean GetCityCode(String strCityname, List<String> codeList);

	// 根据城市代码获取省名
	public String GetProvNameByCityCode(String cityCode);
	
	// 根据城市代码
	public String getCityNameByCityCode(String cityCode);
	
	// 根据省份获取旅游地
	public boolean GetScenicByProvName(String sProv, List<CityStruct> cityList);
	
	// 按搜索条件获取旅游地
	public int GetScenicsBySearch(String sSearch, List<CityStruct> cityList);

	//获取国外国家
	public int GetForeignCountryWithSort(List<CityStruct> cityStruct, String[] excludeCitiesName);
	//根据国家获取城市
	public int GetForeignCityByAreaCode(String strCode, List<CityStruct> cityStruct);
	// 根据城市代码获取国家名称
	public String GetCountryNameByCityCode(String cityCode);
	// 根据城市代码获取国家名称
	public int GetForeignCityBySearch(String cityCode, List<CityStruct> cityStruct);

}
