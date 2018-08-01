/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : WeatherReslt
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-6-16 上午09:37:27 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.util.List;

import android.database.Cursor;

import com.calendar.CommData.CityStruct;

public class WeatherReslt implements IWeatherReslt
{
	// 数据库接口
	private IDatabaseRef m_dataBaseRef	= null;
	
	@Override
	public void Init(IDatabaseRef dataBaseRef)
	{
		m_dataBaseRef = dataBaseRef;
	}
	
	/**
	 * @brief 【城市搜索】
	 *
	 * @n<b>函数名称</b>     :GetCityListByMsg
	 * @param strMsg
	 * @param cityStruct
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-26上午11:17:36      
	*/
	@Override
	public int GetCityListByMsg(String strMsg, List<CityStruct> cityStruct) {
		String strTempPingYing = strMsg + "%";
		strMsg = "%" + strMsg + "%";

		Cursor cursor = m_dataBaseRef.RawQuery(
				"select w.citycode,w.cityname,weather_prov.provname from "
				+ "(select distinct * from weather_city where py like ? or pinyin like ? "
				+ "or cityname like ? ORDER BY cityname) w "
				+ "join weather_area on weather_area.areacode = w.areacode "
				+ "join weather_prov on weather_prov.provcode = weather_area.provcode;",
				new String[] { strMsg, strTempPingYing, strMsg });

		if (cursor == null) {
			return 0;
		}

		while (cursor.moveToNext()) {
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(decodeCityCode(cursor.getString(0)));
			cityStructInfo.setName(cursor.getString(1));
			cityStructInfo.setProvName(cursor.getString(2));
			
			cityStruct.add(cityStructInfo);
		}

		int nCount = cursor.getCount();

		cursor.close();

		return nCount;
	}

	/**
	 * @brief【获取地区】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllAreaByprovcode
	 * @see
	
	 * @param  @param strCode
	 * @param  @param cityStruct
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-5下午01:41:11      
	*/
	@Override
	public int GetAllAreaByProvcode(String strCode, List<CityStruct> cityStruct) {
		String sql = String.format("select a.areacode, a.areaname from "
				+ "weather_area as a inner join weather_prov as b "
				+ "on a.provcode = b.provcode where b.provcode = '%s'", strCode);

		Cursor cursor = m_dataBaseRef.RawQuery(sql, null);

		if (cursor == null) {
			return 0;
		}

		while (cursor.moveToNext()) {
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(cursor.getString(0));
			cityStructInfo.setName(cursor.getString(1));
			cityStruct.add(cityStructInfo);
		}

		int nCount = cursor.getCount();

		cursor.close();

		return nCount;
	}

	/**
	 * @brief 【根据省获取地区】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllProv
	 * @see
	
	 * @param  @param cityStruct
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-5下午01:41:57      
	*/
	@Override
	public int GetAllProv(List<CityStruct> cityStruct)
	{
		Cursor cursor = m_dataBaseRef.RawQuery("select provcode,provname from weather_prov;", null);
		
		if (cursor == null)
		{
			return 0;
		}
		
		while (cursor.moveToNext())
		{
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(cursor.getString(0));
			cityStructInfo.setName(cursor.getString(1));
			cityStruct.add(cityStructInfo);
		}
		
		int nCount = cursor.getCount();
		
		return nCount;
		
	}

	/**
	 * @brief 【获取省份，按拼音排序】
	 *
	 * @n<b>函数名称</b>     :GetAllProvWithSort
	 * @param cityStruct
	 * @param excludeCitiesName
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-5-14下午03:28:10      
	*/
	@Override
	public int GetAllProvWithSort(List<CityStruct> cityStruct, String[] excludeCitiesName) {
		Cursor cursor = m_dataBaseRef.RawQuery(
				"select provcode,provname from weather_prov;", null);

		if (cursor == null) {
			return 0;
		}

		boolean bExist = false;
		while (cursor.moveToNext()) {
			if (excludeCitiesName != null) {
				bExist = false;
				for (String name : excludeCitiesName) {
					if (cursor.getString(1).equals(name)) {
						bExist = true;
						break;
					}
				}

				if (bExist) {
					continue;
				}
			}

			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(cursor.getString(0));
			cityStructInfo.setName(cursor.getString(1));
			cityStruct.add(cityStructInfo);
		}

		int nCount = cursor.getCount();

		cursor.close();

		return nCount;

	}
	
	/**
	 * @brief 【用地区代码获得城市】
	 *
	 * @n<b>函数名称</b>     :GetAllCityByareacode
	 * @param strCode
	 * @param cityStruct
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午04:35:04      
	*/
	@Override
	public int GetAllCityByAreacode(String strCode, List<CityStruct> cityStruct) {
		Cursor cursor = m_dataBaseRef.RawQuery(String.format(
				"select citycode,cityname from weather_city where areacode = '%s'", strCode), null);

		if (cursor == null) {
			return 0;
		}

		while (cursor.moveToNext()) {
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(decodeCityCode(cursor.getString(0)));
			cityStructInfo.setName(cursor.getString(1));
			cityStruct.add(cityStructInfo);
		}

		int nCount = cursor.getCount();
		cursor.close();
		return nCount;

	}

	/**
	 * @brief 【以城市名获得城市码】
	 *
	 * @n<b>函数名称</b>     :GetCityCode
	 * @param strCityname
	 * @param codeList
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午04:36:56      
	*/
	@Override
	public boolean GetCityCode(String strCityname, List<String> codeList) {
		codeList.clear();
		Cursor cursor = m_dataBaseRef.RawQuery(String.format(
				"select citycode from weather_city where cityname='%s'", strCityname),
				null);

		if (cursor == null) {
			return false;
		}

		while (cursor.moveToNext()) {
			codeList.add(decodeCityCode(cursor.getString(0)));
		}

		int count = cursor.getCount();
		cursor.close();

		return (count > 0);
	}

	/**
	 * @brief 【】
	 *
	 * @n<b>函数名称</b>     :GetProvNameByCityCode
	 * @param cityCode
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午04:37:14      
	*/
	@Override
	public String GetProvNameByCityCode(String cityCode) {
		String sql = "select weather_prov.provname from "
				+ "(select areacode from weather_city where citycode=?) w "
				+ "join weather_area on weather_area.areacode=w.areacode "
				+ "join weather_prov on weather_prov.provcode=weather_area.provcode;";

		Cursor cursor = m_dataBaseRef.RawQuery(sql, new String[] {encodeCityCode(cityCode)});

		if (cursor == null) {
			return null;
		}

		String provName = null;

		if (cursor.moveToNext()) {
			provName = cursor.getString(0);
		}

		cursor.close();

		return provName;
	}

	/**
	 * @brief 【根据省份获取旅游地】
	 *
	 * @n<b>函数名称</b>     :GetScenicByProvName
	 * @param sProv
	 * @param cityStruct
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-4-20上午09:47:12      
	*/
	public boolean GetScenicByProvName(String sProv, List<CityStruct> cityStruct) {
		
		String sql = "select sProv,CityCode,sScenicName from scenic where sProv='%s';";
		Cursor cursor = m_dataBaseRef.RawQuery(String.format(sql, sProv), null);

		if (cursor == null) {
			return false;
		}
		
		while (cursor.moveToNext()) {
			CityStruct c = new CityStruct(cursor.getString(2), cursor.getString(1));
			c.setProvName(cursor.getString(0));
			
			cityStruct.add(c);
		}

		cursor.close();
		return true;
	}
	
	
	/**
	 * @brief 【按搜索条件获取旅游地】
	 *
	 * @n<b>函数名称</b>     :GetScenicsBySearch
	 * @param sSearch
	 * @param cityStruct
	 * @return
	 * @return    int   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-4-20上午11:38:51      
	*/
	public int GetScenicsBySearch(String sSearch, List<CityStruct> cityStruct) {
		String sPinYin = sSearch + "%";
		sSearch = "%" + sSearch + "%";

		String sSql = "select distinct sProv,CityCode,sScenicName"
				+ " from scenic where sPyAbb like ? or sPy like ?"
				+ " or sScenicName like ? ORDER BY sScenicName";

		Cursor cursor = m_dataBaseRef.RawQuery(sSql, new String[] {sSearch, sPinYin, sSearch});

		if (cursor == null) {
			return 0;
		}

		while (cursor.moveToNext()) {
			CityStruct c = new CityStruct(cursor.getString(2), decodeCityCode(cursor.getString(1)));
			c.setProvName(cursor.getString(0));
			cityStruct.add(c);
		}

		int nCount = cursor.getCount();

		cursor.close();
		return nCount;
	}

    @Override
    public String getCityNameByCityCode(String cityCode) {
        String sql = "select cityname from weather_city where citycode=" + encodeCityCode(cityCode);

        Cursor cursor = m_dataBaseRef.RawQuery(sql, null);

        if (cursor == null) {
            return null;
        }
        String cityName = null;
        try {
            if (cursor.moveToNext()) {
                cityName = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return cityName;
    }

	/**
	 * @brief 【获得国外国家】
	 *
	 * @n<b>函数名称</b>     :GetForeignCountryWithSort
	 * @param cityStruct
	 * @param excludeCitiesName
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午05:45:56      
	*/
	@Override
    public int GetForeignCountryWithSort(List<CityStruct> cityStruct, String[] excludeCitiesName) {
        cityStruct.clear();
        Cursor cursor = m_dataBaseRef
                .RawQuery("select areacode,areaname from foreign_countries;", null);

        if (cursor == null) {
            return 0;
        }

        boolean bExist = false;
        try {
            while (cursor.moveToNext()) {
                if (excludeCitiesName != null) {
                    bExist = false;
                    for (String name : excludeCitiesName) {
                        if (cursor.getString(1).equals(name)) {
                            bExist = true;
                            break;
                        }
                    }

                    if (bExist) {
                        continue;
                    }
                }

                CityStruct cityStructInfo = new CityStruct();
                cityStructInfo.setCode(cursor.getString(0));
                cityStructInfo.setName(cursor.getString(1));
                cityStruct.add(cityStructInfo);
            }
        } finally {
            cursor.close();
        }
        return cityStruct.size();
    }

	/**
	 * @brief 【按地区获取国外城市】
	 *
	 * @n<b>函数名称</b>     :GetForeignCityByAreaCode
	 * @param strCode
	 * @param cityStruct
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-26上午11:36:58      
	*/
	@Override
	public int GetForeignCityByAreaCode(String strCode, List<CityStruct> cityStruct) {
		// TODO Auto-generated method stub
		
		Cursor cursor = m_dataBaseRef
				.RawQuery(String.format("select w.citycode,w.cityname,foreign_countries.areaname from " + 
						"(select distinct * from foreign_cities " +
						"where areacode = '%s' ORDER BY cityname) w " +
						"join foreign_countries on foreign_countries.areacode = w.areacode " ,
						strCode ), null);
				
		if (cursor == null)
		{
			return 0;
		}
		while (cursor.moveToNext())
		{
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(cursor.getString(0));
			cityStructInfo.setName(cursor.getString(1));
			cityStructInfo.setProvName(cursor.getString(2));
			cityStruct.add(cityStructInfo);
		}
		
		int nCount = cursor.getCount();
		
		cursor.close();

		return nCount;
	}

	/**
	 * @brief 【以城市代码获得国家名】
	 *
	 * @n<b>函数名称</b>     :GetCountryNameByCityCode
	 * @param cityCode
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午05:08:04      
	*/
	@Override
	public String GetCountryNameByCityCode(String cityCode) {
		// TODO Auto-generated method stub
		
		String sql = "select areaname from foreign_countries " +
				"where areacode = (select areacode from foreign_cities where foreign_cities.citycode = '%s')";

		Cursor cursor = m_dataBaseRef.RawQuery(String.format(sql, cityCode), null);

		if (cursor == null) {
			return null;
		}

		String provName = null;

		if (cursor.moveToNext()) {
			provName = cursor.getString(0);
		}

		cursor.close();

		return provName;
		
	}

	/**
	 * @brief 【搜索国外城市】
	 *
	 * @n<b>函数名称</b>     :GetForeignCityBySearch
	 * @param strMsg
	 * @param cityStruct
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-11-23下午04:51:33      
	*/
	@Override
	public int GetForeignCityBySearch(String strMsg, List<CityStruct> cityStruct) {
		// TODO Auto-generated method stub
		String strTempPingYing = strMsg + "%";
		strMsg = "%" + strMsg + "%";

		Cursor cursor = m_dataBaseRef
				.RawQuery(
						"select w.citycode,w.cityname,foreign_countries.areaname from "
						+ "(select distinct * from foreign_cities "
						+ "where py like ? or pinyin like ? or cityname like ? ORDER BY cityname) w "
						+ "join foreign_countries on foreign_countries.areacode = w.areacode ",
						new String[] { strMsg, strTempPingYing, strMsg });

		if (cursor == null) {
			return 0;
		}

		while (cursor.moveToNext()) {
			CityStruct cityStructInfo = new CityStruct();
			cityStructInfo.setCode(cursor.getString(0));
			cityStructInfo.setName(cursor.getString(1));
			cityStructInfo.setProvName(cursor.getString(2));
			cityStruct.add(cityStructInfo);
		}

		int nCount = cursor.getCount();

		cursor.close();

		return nCount;

	}
	
    //国内城市和旅游地城市代码解密
    public String decodeCityCode(String en){
//        String code = mCalTools.decode(en);
//        long icode = Long.parseLong(code) + 101000000;
//        return Long.toString(icode);
        return en;
    }
    
    public String encodeCityCode(String code){
//        long icode = Long.parseLong(code) - 101000000;
//        return mCalTools.encode(Long.toString(icode));
    	
    	return code;
    }
}
