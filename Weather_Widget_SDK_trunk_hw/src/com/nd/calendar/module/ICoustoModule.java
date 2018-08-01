/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : ICoustoModule
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 上午10:35:49 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.List;

import com.calendar.CommData.CityStruct;
import com.nd.calendar.dbrepoist.IDatabaseRef;

public interface ICoustoModule
{
	public void Init(IDatabaseRef dataBaseRef);

//	public void orderYJ();
	///////////////////////////////////////////////////////////////////////////
	// 根据拼音、中文获取城市列表
	public int GetCityListByMsg(String strMsg, List<CityStruct> cityStruct);
	
	//获取省
	public int GetAllProv(List<CityStruct> cityStruct);
	
	// 获取省，按拼音排列，ExcludeCitiesName 需排除的省份名称
	public int GetAllProvWithSort(List<CityStruct> cityStruct, String[] ExcludeCitiesName);

	//根据省获取地区
	public int GetAllAreaByprovcode(String strCode, List<CityStruct> cityStruct);
	
	//根据地区获取县
	public int GetAllCityByareacode(String strCode, List<CityStruct> cityStruct);
	
	//根据城市获取代号
	public boolean GetCityCode(String strCityname, List<String> codeList);
	
	// 根据城市代码获取省名
	public String GetProvNameByCityCode(String cityCode);
	
	/*根据城市代码获取城市名称*/
	public String getCityNameByCityCode(String cityCode);
	
	// 根据省份获取旅游地
	public boolean GetScenicByProvName(String sProv, List<CityStruct> cityList);
	
	// 按搜索条件获取旅游地
	public int GetScenicsBySearch(String sSearch, List<CityStruct> cityList);
	
	//获取国外国家
	public int GetForeignCountryWithSort(List<CityStruct> threeStructs, List<CityStruct> twoStructs, List<CityStruct> oneStructs, String[] excludeCitiesName);
	//根据国家获取城市
	public int GetForeignCityByAreaCode(String strCode, List<CityStruct> cityStruct);
	// 根据城市代码获取国家名称
	public String GetCountryNameByCityCode(String cityCode);
	
	public int GetForeignCityBySearch(String info,List<CityStruct> cityStruct);
	////////////////////////////////////////////////////////////////////////////////
	
	// 获取宜忌冲
//	public boolean GetCYJResult(DateInfo date, YjcInfo yjcInfo);

//	public boolean getYJCInfo(DateInfo date, YjcInfo yjcInfo);
	
	// 获取今日吉时
	//public boolean GetTdyGdTimeResult(DateInfo date, TdyGoodTime tdGoodTime);

	//【获取宜忌常用数据】
//	public int GetConstantYuJiData(Vector<String> vectString);
	
	//【获取宜忌数据】
//	public boolean GetYiAndJiInfo(DateInfo date, YjcInfo yjcInfo);

//	//【获取某个时间范围的宜忌】
//	public int GetFromTimeToAnthoTimeYuJiInfo(DateInfo dateBegin, DateInfo dateEnd, String strCon,boolean bFlag, 
//			Vector<YjcInfo> vectYjInfo, Vector<DateInfo> vectDateInfo);
//
//	
//	//【获取结婚吉日】
//	public int GetMakeMatchResult(DateInfo dateBegin, DateInfo dateEnd, String strShenXiaoMen, String strShenXiaoWomen,
//			Vector<YjcInfo> vectYjInfores, Vector<DateInfo> vectDateInfo);
	
//	// 【获取黄历名词解释】
//	public boolean GetHuangLiExplanation(int iType, String formatNouns, Vector<HuangLiExplainInfo> explanList);
//	
//	public String checkExpData();
//	
//	// 获取吉凶数据
//	public ArrayList<String> getTimeJX(DateInfo d);
	
}
