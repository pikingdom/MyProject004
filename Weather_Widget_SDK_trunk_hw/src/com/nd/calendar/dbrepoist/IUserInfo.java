/**   
 *    
 * @file 【User.db数据库操作接口】
 * @brief
 *
 * @<b>文件名</b>      : IUserInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-5 下午05:35:05 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.CityWeatherJson;

public interface IUserInfo
{	
	/////////////////////////////////////////////////////////////////////	
    //获取所有城市天气信息
    public boolean getCityWeatherList(Context ctx, ArrayList<CityWeatherJson> citys);
    
    //根据ID获取城市信息
    public boolean getCityWeatherJsonById(Context ctx, String id, CityWeatherJson city);
    
    // 下一个城市ID，循环
    public int getNextCityID(Context ctx, int id);
    
    // 按条件获取城市，仅限桌面插件(id == 0，代表第一个城市)
    public boolean getCityWeatherJsonWidget(Context ctx, int id, CityWeatherJson city);
    
    //更新城市信息
    public boolean updateWeatherInfo(Context ctx, CityWeatherJson city);
    
    //获取更新时间
    public String getLastTimeByType(Context ctx, String id, int type);
    
    public String getCityCodeByID(Context ctx, int id);
    
    public String getCityNameByID(Context ctx, int id);
    ///////////////////////////////////////////////////////////////////
    // 获取城市列表
    public int GetCityList(Context ctx, List<CityInfo> cityList);
    
    public int GetCityCount(Context ctx);
    // 更新城市信息
    public boolean UpdateCityInfo(Context ctx, CityInfo cityInfo);
    //添加城市信息
    public int InsertCityInfo(Context ctx, CityInfo cityInfo, boolean bAutoUpdate);
    //删除城市信息
    public boolean DeleteCityInfo(Context ctx, CityInfo cityInfo);
    
	//设置自动定位城市信息(bClearWeatherInfo 是否清理天气数据)
	public boolean setLocationCity(Context ctx, CityInfo city);
	
	/*获取自动定位城市*/
	public CityInfo getLocationCity(Context ctx, int id);
    ////////////////////////////////////////////////////////////////////
    
    // 根据字段名，自动更新对应数据，但json 和 time要配对
    public boolean updateWeatherAuto(Context ctx, CityWeatherJson city);
    
    //获取提交意见接口
    public IUser_SuggestInfo GetUserSuggestIf();
	
}
