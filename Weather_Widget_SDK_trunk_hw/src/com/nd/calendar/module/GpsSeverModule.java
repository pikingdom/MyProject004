/**   
*    
* @file
* @brief 【GPS服务模块】
*
* @<b>文件名</b>      : GpsSeverModule
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2011-9-29 下午06:51:08 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.calendar.module;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.calendar.CommData.CityStruct;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.communication.http.HttpAppFunClient;
import com.nd.calendar.util.StringHelp;

public class GpsSeverModule implements IGpsSeverModule
{
	public final static int GPS_TYPE_FROM_NAME = 1;		// 需要通过城市名称解析
	public final static int GPS_TYPE_FINAL = 2;			// 最终坐标
	public final static int NO_GPS_SERVICE = -2;		// 未开启GPS服务
	
	private Context mContext = null;
	private LocationManager m_gpsMgr = null;
	
	// 应用层网络访问对象
	private HttpAppFunClient m_AppFunClient	= null;

	public GpsSeverModule(Context c)
	{
		mContext = c.getApplicationContext();
		if (mContext == null) {
			mContext = c;
		}

		m_AppFunClient = new HttpAppFunClient(mContext);
	}
	
	/**
	 * @brief
	 *
	 *
	 * @n<b>函数名称</b>     :GetGpsInfo
	 * @see
	
	 * @param  @param cityInfo
	 * @param  @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-11-4上午11:50:14      
	*/
	@Override
	public  synchronized int GetGpsInfo(CityStruct cityInfo)
	{
		if (cityInfo == null)
		{
			return -1;
		}
				
		GetGpsManager();
		
		try
		{
			if (!m_gpsMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !m_gpsMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			{
				Log.i("GpsSeverModule", "NO_GPS_SERVICE");
				return NO_GPS_SERVICE;
			}
			
			Location location = updateStat(false);
			
			if (location == null)
			{
				return 0;
			}
			
			
			// 1.从服务器读取解析数据
			//if (GetGpsInfoFromServer(39.921111, 116.461111, cityInfo))
			Log.i("GpsSeverModule", "Latitude:" + location.getLatitude()
					+ ",Longitude:" + location.getLongitude());
			
			if (GetGpsInfoFromServer(location.getLatitude(), location.getLongitude(), cityInfo))
			{
				return GPS_TYPE_FINAL;
			}
			
			// 2.从Google
			Geocoder gc = new Geocoder(mContext, Locale.getDefault());	
			if (gc != null)
			{
				List<Address> add = gc.getFromLocation(
										location.getLatitude(),
										location.getLongitude(), 1);
				
				if (add != null && add.size() > 0)
				{
					GetCityInfoFromAddress(add.get(0).getLocality(), cityInfo);
					return GPS_TYPE_FROM_NAME;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	private LocationManager GetGpsManager()
	{
		if (m_gpsMgr == null && mContext != null)
		{
			m_gpsMgr = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		}
		
		return m_gpsMgr;
	}
	
	private void GetCityInfoFromAddress(String address, CityStruct cityInfo)
	{
		if(address != null && cityInfo != null)
		{
			String sCity = address;
			
			//过滤字符串
			int npost = sCity.indexOf("市", 0);
			if( npost != -1)
			{
				sCity = sCity.substring(0, npost);
			}
			else
			{
				npost = address.indexOf("县", 0);
				if(npost != -1)
				{
					sCity = sCity.substring(0, npost);
				}
			}
			
			cityInfo.setName(sCity);
		}
	}

	/**
	 * @brief 【获取定位信息】
	 *
	 * @n<b>函数名称</b>     :updateStat
	 * @return
	 * @return    Location   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-9-30下午01:51:30      
	*/
	private Location updateStat(boolean bNetworkProvider)
	{
		LocationManager gpsMgr = GetGpsManager();

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);       
		criteria.setBearingRequired(false);        
		criteria.setCostAllowed(true); 
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
		
		String bestGps;
		if (bNetworkProvider)
		{
			bestGps = LocationManager.NETWORK_PROVIDER;
		}
		else
		{
			bestGps = gpsMgr.getBestProvider(criteria, true);			
		}
		
		Location location = null;
		if (bestGps != null)
		{
			//location = getLocation(gpsMgr, bestGps);
			location = gpsMgr.getLastKnownLocation(bestGps);
		}
		
		if(location == null && !bNetworkProvider)
		{
			//location = getLocation(gpsMgr, LocationManager.NETWORK_PROVIDER);
			location = gpsMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		return location;
	}
	
	/**
	 * @brief 【通过回调获得经纬度】
	 *
	 * @n<b>函数名称</b>     :getLocation
	 * @param gpsMgr
	 * @param bestGps
	 * @return
	 * @return    Location   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-3-27上午11:57:49      
	*/
	Location getLocation(LocationManager gpsMgr, String bestGps) {
		// 注册gps变化监听
		GpsListener gpsListener = new GpsListener();
		gpsMgr.requestLocationUpdates(bestGps, 0L, 0.0f, gpsListener);
		Location location = null;
		
		// 循环5次监听
		int k = 0;
		while (k < 5) {
			
			location = gpsListener.getLocation();
			if (location != null) {
				break;
			}

			try {
				Thread.sleep(500L);
			} catch (Exception Exception) {
			}

			k++;
		}

		gpsMgr.removeUpdates(gpsListener);
		
		return location;
	}
	
	/**
	 * @brief 【获得 GPS 定位解析数据】
	 *
	 * @n<b>函数名称</b>     :DownloadGpsInfo
	 * @param latitude
	 * @param longitude
	 * @param cityInfo
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-11-1下午05:01:28      
	*/
	@Override
	public boolean GetGpsInfoFromServer(double latitude, double longitude, CityStruct cityInfo) {
		if (cityInfo == null) {
			return false;
		}
		
		StringBuffer strResOut = new StringBuffer("");
		
		if (m_AppFunClient == null) {
			m_AppFunClient = new HttpAppFunClient(mContext);
		}
		
		if(m_AppFunClient.DownloadGpsInfo(latitude, longitude, strResOut, null)) {
			try {
				final String s = strResOut.toString();
				Log.d("gps", s);
				JSONObject jsonOut = StringHelp.getJSONObject(s);
				
				String code = jsonOut.getString("citycode");
				if (code.equals("error")) {
				    return false;
				}
				
				cityInfo.setCode(code);
				cityInfo.setName(jsonOut.getString("cityname"));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;		
			}
		}
		
		return false;
	}
	
	/**
	 * @brief
	 *
	 *
	 * @n<b>函数名称</b>     :GetGpsOpenState
	 * @see
	
	 * @param  @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-11-4上午11:50:14      
	*/
	
	@Override
	public boolean GetGpsOpenState()
	{
		SharedPreferences set = mContext.getSharedPreferences(ConfigsetData.CONFIG_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return set.getBoolean("gps", true);
	}
	
	/**
	 * @brief
	 *
	 *
	 * @n<b>函数名称</b>     :SetGpsOpenState
	 * @see
	
	 * @param  @param bOPen
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-11-4上午11:50:14      
	*/
	
	@Override
	public void SetGpsOpenState(boolean bOPen)
	{
		SharedPreferences set = mContext.getSharedPreferences(ConfigsetData.CONFIG_NAME, 
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		Editor editor = set.edit();
		editor.putBoolean("gps", bOPen).commit();
	}
	
	
	// GPS 变动监听
	final class GpsListener implements LocationListener
	{
		private Location location = null;

		public final Location getLocation() {
			return this.location;
		}

		public final void onLocationChanged(Location paramLocation) {
			
			// 获得位置时，赋值
			if ((paramLocation != null) 
				&& (paramLocation.getLatitude() != 0)
				&& (paramLocation.getLongitude() != 0)) {
				
				Log.d("GpsModule", "get location");
				this.location = paramLocation;
			}
		}

		public final void onProviderDisabled(String paramString) {
		}

		public final void onProviderEnabled(String paramString) {
		}

		public final void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {
		}
	}
}
