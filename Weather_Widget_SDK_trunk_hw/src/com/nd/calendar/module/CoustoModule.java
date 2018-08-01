/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : CoustoModule
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 上午10:31:52 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.ArrayList;
import java.util.List;

import com.calendar.CommData.CityStruct;
import com.nd.calendar.dbrepoist.DbCoustomReslt;
import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.dbrepoist.IWeatherReslt;
import com.nd.calendar.dbrepoist.WeatherReslt;

public class CoustoModule implements ICoustoModule
{
	// 数据库层接口
	private DbCoustomReslt	m_dbCouReslt		= null;
	// 天气城市信息数据接口
	private IWeatherReslt	m_dbWeahterReslt	= null;

	/**
	 * @brief 【获取宜忌数据】
	 *
	 *
	 * @n<b>函数名称</b>     :GetYiAndJiInfo
	 * @see
	
	 * @param  @param date
	 * @param  @param yjcInfo
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-10-21上午10:24:20      
	*/
//	@Override
//	public boolean GetYiAndJiInfo(DateInfo date, YjcInfo yjcInfo)
//	{
//		return m_dbCouReslt.GetYiAndJiInfo(date, yjcInfo);	
//	}
	
	
	/**
	 * @brief 【获取宜忌冲接口】
	 * @n<b>函数名称</b>     :getYJCInfo
	 * @param date
	 * @param yjcInfo
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-6-13下午05:38:50
	 * @<b>修改时间</b>      :
	*/
//	public boolean getYJCInfo(DateInfo date, YjcInfo yjcInfo) {
//		CalendarInfo cInfo = CalendarInfo.getInstance();
//		String strTianGanDiZhi = cInfo.GetLlGZDay(date);
//		String strDayDiZhi = strTianGanDiZhi.substring(1);
//		
//		int iIdx = cInfo.GetDiZhiIndex(strDayDiZhi);
//		if (iIdx == -1) {
//			return false;
//		}
//		
//		//冲
//		String strChong = ComDataDef.CoustomData.TWELVE_CLASH[(iIdx + 18) % 12];
//		yjcInfo.setStrChong(strChong);
//		
//		return m_dbCouReslt.GetYiAndJiInfo(date, yjcInfo);
//	}
	
	/**
	 * @brief 【初始化】
	 * 
	 * 
	 * @n<b>函数名称</b> :Init
	 * @see
	 * 
	 * @param @param dataBaseRef
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-28上午11:20:02
	 */
	@Override
	public void Init(IDatabaseRef dataBaseRef) {
		if (m_dbCouReslt == null) {
			m_dbCouReslt = new DbCoustomReslt();
		}
		
		if (m_dbWeahterReslt == null) {
			m_dbWeahterReslt = new WeatherReslt();
		}

		m_dbCouReslt.Init(dataBaseRef);
		m_dbWeahterReslt.Init(dataBaseRef);
	}

	////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【根据拼音、中文获取城市列表】
	 * 
	 * 
	 * @n<b>函数名称</b> :GetCityListByMsg
	 * @see
	 * 
	 * @param @param strMsg
	 * @param @param cityStruct
	 * @param @return
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-6-16上午11:01:46
	 */
	@Override
	public int GetCityListByMsg(String strMsg, List<CityStruct> cityStruct)
	{
		return m_dbWeahterReslt.GetCityListByMsg(strMsg, cityStruct);
	}

	/**
	 * @brief【根据省获取所有的区域】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllAreaByprovcode
	 * @see
	
	 * @param  @param strCode
	 * @param  @param cityStruct
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-5下午01:48:43      
	*/
	@Override
	public int GetAllAreaByprovcode(String strCode, List<CityStruct> cityStruct)
	{
		return m_dbWeahterReslt.GetAllAreaByProvcode(strCode, cityStruct);
	}

	/**
	 * @brief【获取所有的省】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllProv
	 * @see
	
	 * @param  @param cityStruct
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-5下午01:48:47      
	*/
	@Override
	public int GetAllProv(List<CityStruct> cityStruct)
	{
		return m_dbWeahterReslt.GetAllProv(cityStruct);
	}

	/**
	 * @brief 获取省，按拼音排列，ExcludeCitiesName 需排除的省份名称
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllProvWithSort
	 * @see
	
	 * @param  @param cityStruct
	 * @param  @param ExcludeCitiesName
	 * @param  @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-9-23下午02:29:20      
	*/
	public int GetAllProvWithSort(List<CityStruct> cityStruct, String[] ExcludeCitiesName)
	{
		return m_dbWeahterReslt.GetAllProvWithSort(cityStruct, ExcludeCitiesName);
	}

	/**
	 * @brief 【根据地区获取县】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllCityByareacode
	 * @see
	
	 * @param  @param strCode
	 * @param  @param cityStruct
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-5下午06:02:53      
	*/
	@Override
	public int GetAllCityByareacode(String strCode, List<CityStruct> cityStruct)
	{
		return m_dbWeahterReslt.GetAllCityByAreacode(strCode, cityStruct);
		
	}

	/**
	 * @brief【根据城市获取天气代号】
	 *
	 *
	 * @n<b>函数名称</b>     :GetCityCode
	 * @see
	
	 * @param  @param strCityname
	 * @param  @param strCode
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-7下午03:25:40      
	*/
	@Override
	public boolean GetCityCode(String strCityname, List<String> codeList)
	{
		return m_dbWeahterReslt.GetCityCode(strCityname, codeList);
	}

	@Override
	public String GetProvNameByCityCode(String cityCode)
	{
		return m_dbWeahterReslt.GetProvNameByCityCode(cityCode);
	}
	
	// 根据省份获取旅游地
	public boolean GetScenicByProvName(String sProv, List<CityStruct> cityList)
	{
		return m_dbWeahterReslt.GetScenicByProvName(sProv, cityList);
	}
	
	// 按搜索条件获取旅游地
	public int GetScenicsBySearch(String sSearch, List<CityStruct> cityList) {
		return m_dbWeahterReslt.GetScenicsBySearch(sSearch, cityList);
	}

    @Override
    public String getCityNameByCityCode(String cityCode) {
        return m_dbWeahterReslt.getCityNameByCityCode(cityCode);
    }
    
	@Override
	public int GetForeignCountryWithSort(List<CityStruct> threeStructs, List<CityStruct> twoStructs, List<CityStruct> oneStructs, 
			String[] excludeCitiesName) {
	    threeStructs.clear();
	    twoStructs.clear();
	    oneStructs.clear();
		ArrayList<CityStruct> cs = new ArrayList<CityStruct>();
		int ireturn = m_dbWeahterReslt.GetForeignCountryWithSort(cs, excludeCitiesName);
		for (CityStruct c : cs) {
		    int len = c.getName().length();
            if (len <= 4){
                threeStructs.add(c);
            }else if ((len > 4 ) && (len <8)){
                twoStructs.add(c);
            }else{
                oneStructs.add(c);
            }
        }
		return ireturn;
	}

	@Override
	public int GetForeignCityByAreaCode(String strCode, List<CityStruct> cityStruct) {
		// TODO Auto-generated method stub
		return m_dbWeahterReslt.GetForeignCityByAreaCode(strCode, cityStruct);
	}

	@Override
	public String GetCountryNameByCityCode(String cityCode) {
		// TODO Auto-generated method stub
		return m_dbWeahterReslt.GetCountryNameByCityCode(cityCode);
	}

	@Override
	public int GetForeignCityBySearch(String cityName, List<CityStruct> cityStruct) {
		// TODO Auto-generated method stub
		return m_dbWeahterReslt.GetForeignCityBySearch(cityName, cityStruct);
	}
    
}
