
package com.nd.weather.widget.UI.weather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.CityStruct;
import com.nd.calendar.Control.CityListAdapter;
import com.nd.calendar.Control.ListViewAdapter;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.dbrepoist.UserInfo;
import com.nd.calendar.module.GpsSeverModule;
import com.nd.calendar.module.ICoustoModule;
import com.nd.calendar.module.IGpsSeverModule;
import com.nd.calendar.module.IUserModule;
import com.nd.calendar.module.gps.LocManager.Result;
import com.nd.calendar.module.gps.MyLocation;
import com.nd.calendar.module.gps.MyLocation.LocationResult;
import com.nd.weather.widget.R;
import com.nd.weather.widget.TimeService;
import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.CommData.SearchInfo;
import com.nd.weather.widget.UI.UIBaseAty;

public class UIWidgetCityMgrAty extends UIBaseAty implements OnClickListener, OnItemClickListener {

	private GridView m_gvHotCity = null; 
	private CityListAdapter m_adapterHotCity; // 热门城市适配器
	private ListView mlvSreach;
	private EditText metSreach;
	private ICoustoModule m_DbMdl;
	private IUserModule  m_userMdl;
	private ArrayList<SearchInfo> mSearchList;
	private ArrayList<CityStruct> mCitylist = new ArrayList<CityStruct>();
	static final String LOCATION_CITY = "000000000";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_widget_city_mgr);
        
        m_gvHotCity = (GridView) findViewById(R.id.widget_city_mgr_hot);
        mlvSreach = (ListView) findViewById(R.id.widget_city_mgr_search_list);
        metSreach = (EditText) findViewById(R.id.widget_city_mgr_search_edit);
		
        View viewBk = findViewById(R.id.widget_city_mgr);
        viewBk.setBackgroundDrawable(null);
        viewBk.setOnClickListener(this);
        
        mSearchList = new ArrayList<SearchInfo>();
        m_adapterHotCity = new CityListAdapter(this);

        metSreach.requestFocus();
        
		findViewById(R.id.widget_city_mgr_gps).setOnClickListener(this);;
        (findViewById(R.id.widget_city_mgr_back_btn)).setOnClickListener(this);
        metSreach.post(new Runnable() {
			
			@Override
			public void run() {
				initData();
			}
		});
        
        setSearchListViewListener();
   }

    /////////////////////////////////////////////////////////////////
	void initData() {
		ArrayList<CityStruct> hotlist = loadHotCity();
		List<CityInfo> cityList = new ArrayList<CityInfo>();
		
		WeatherLinkTools.getInstance(getApplicationContext()).getCalendarContext(false);
		m_userMdl = m_calendarMgr.Get_UserMdl_Interface();
		m_userMdl.GetCityList(cityList);
		
		int size = cityList.size();
		
		if (size > 0) {
			int len = (size > 4)? (size - 4): 0;

			for (int i = size - 1; i >= len; i--) {
				CityInfo cityInfo = cityList.get(i);
				mCitylist.add(new CityStruct(cityInfo.getName(), cityInfo.getCode(), cityInfo.getFromGps()));
				
				delCityInList(hotlist, cityInfo);
			}
		}
		
		m_adapterHotCity.setTextDipSize(16);
		
		m_adapterHotCity.setTextColor(getResources().getColor(R.color.navy));
		mCitylist.addAll(hotlist);
		m_adapterHotCity.setData(mCitylist);
		m_gvHotCity.setAdapter(m_adapterHotCity);
	}
    
    /**
     * @brief 【从城市列表中删除】
     * @n<b>函数名称</b>     :delCityInList
     * @param hotlist
     * @param exculdeCity
     * @return    void   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-1-16下午03:27:26
     * @<b>修改时间</b>      :
    */
    void delCityInList(List<CityStruct> hotlist, CityInfo exculdeCity) {
    	int size = hotlist.size();
    	for (int i = 0; i < size; i++) {
    		CityStruct cs = hotlist.get(i);
			if (cs.getName().equals(exculdeCity.getName()) && 
				cs.getCode().equals(exculdeCity.getCode())) {
				hotlist.remove(i);
				break;
			}
		}
    }
    
	/**
	 * @brief 【加载热门城市数据】
	 * @n<b>函数名称</b>     :loadHotCity
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-16下午02:35:09
	 * @<b>修改时间</b>      :
	*/
    ArrayList<CityStruct> loadHotCity() {
		Resources res = getResources();
		String[] names = res.getStringArray(R.array.hot_city_names);
		String[] codes = res.getStringArray(R.array.hot_city_codes);
		
		if (names != null && codes != null && names.length > 0) {
			ArrayList<CityStruct> hotCityList = new ArrayList<CityStruct>();
			
			for (int i = 0; i < codes.length && i < names.length; i++) {
				hotCityList.add(new CityStruct(names[i], codes[i]));
			}
			
			return hotCityList;
		}
		
		return null;
	}

    Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 0:
				Toast.makeText(UIWidgetCityMgrAty.this, "定位失败", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(UIWidgetCityMgrAty.this, "切换城市中...", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				
				break;
			}
			super.handleMessage(msg);
		}
    };
    
    /**
     * @brief 【gps 定位】
     * @n<b>函数名称</b>     :getCityLocation
     * @return    void   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-1-17下午03:26:58
     * @<b>修改时间</b>      :
    */
    private void getCityLocation() {
    	
    	// 当天气接口可链接时切换到主版定位
    	WeatherLinkTools wlt = WeatherLinkTools.getInstance(getApplicationContext());
    	if (wlt.canLink()) {
    		setLocalCity();
		} else {
	        MyLocation l = new MyLocation();
	        l.getLocation(this, new LocationResult() {
	            @Override
	            public void gotLocation(Result location) {
	            	try {
						if (isFinishing()) {
							return;
						}
						
						if (location == null) {
							// 失败
							mHandler.sendEmptyMessage(0);
							return;
						}
						
						// 定位回来 去服务器取城市代码
						GPSThread gThread = new GPSThread();
						gThread.location = location;
						gThread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        });
		}
    }
    
    /**
     * @brief 【主版定位功能】
     * @n<b>函数名称</b>     :setLocalCity
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-8-19下午4:59:12
     * @<b>修改时间</b>      :
    */
    private void setLocalCity() {
    	new Thread() {
			@Override
			public void run() {
				super.run();
		    	Context context = getApplicationContext();
		    	CityInfo cityInfo = m_userMdl.getLocationCity(context, 0);
		    	int newid = -1;
		    	
		    	if (cityInfo == null) {
		            // 添加自动定位
		        	cityInfo = new CityInfo();
		            cityInfo.setCode(LOCATION_CITY);
		            cityInfo.setName("自动定位");
		            cityInfo.setFromGps(2);
		            
		    		if (m_userMdl.setLocationCity(context, cityInfo)) {
		    			cityInfo = m_userMdl.getLocationCity(context, 0);
		    			if (cityInfo != null) {
		    				newid = cityInfo.getId();
						}
					}
				} else {
					newid = cityInfo.getId();
				}
		    	
				if (newid != -1) {
			       	WeatherLinkTools.getInstance(context).setFirstCityID(newid);
			       	finish();
				} else {
			    	// 失败
			    	mHandler.sendEmptyMessage(0);
				}
			}
			
    	}.start();
    }
    
    class GPSThread extends Thread {
    	public Result location = null;
    	
        @Override
        public void run() {
            try {
				if (location == null) {
					// 失败
					mHandler.sendEmptyMessage(0);
					return;
				}
				
				CityStruct gpsCity = new CityStruct();
				IGpsSeverModule gpsSeverMdl = new GpsSeverModule(getBaseContext());
				boolean result = gpsSeverMdl.GetGpsInfoFromServer(
						location.latitude,
						location.longitude,
						gpsCity);
				
				if (!isFinishing()) {
				    if (result && !TextUtils.isEmpty(gpsCity.getCode())
				            && !TextUtils.isEmpty(gpsCity.getName())) {
				        // 成功
				        AddCity(gpsCity, 1);
				        finish();
				    } else {
				    	// 失败
				    	mHandler.sendEmptyMessage(0);
				    }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    /**
     * @brief 【插入列表项】
     * @n<b>函数名称</b> :AddItemData
     * @see
     * @param @param listInfonew
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>修改</b> : 陈希
     * @<b>创建时间</b> : 2013-1-10上午11:01:02
     */
    void AddCity(CityStruct citySut, int iGPS) {
    	ArrayList<CityInfo> listData = new ArrayList<CityInfo>();
    	m_userMdl.GetCityList(listData);

        // 找到最大的序号，将其赋值给新城市
        int nSort = listData.size();

        for (int i = 0; i < listData.size(); i++) {
            CityInfo cInfo = listData.get(i);
            int n = cInfo.getSort();

            if (n >= nSort) {
                nSort = n + 1;
            }
        }
        
        CityInfo cityInfo = new CityInfo();
        cityInfo.setCode(citySut.getCode());
        cityInfo.setName(citySut.getName());
        cityInfo.setFromGps(iGPS);
        cityInfo.setProvName(citySut.getProvName());
        cityInfo.setDeleteState(false);
        cityInfo.setSort(nSort);

        int iRet = m_userMdl.SetCityInfo(cityInfo, true);
        if ((iRet == UserInfo.OPT_SUCCESS) || (iRet == UserInfo.OPT_IS_EXIST)) {
        	Context context = getApplicationContext();
			int newid = cityInfo.getId();
			if (newid != -1) {
				if (!WeatherLinkTools.getInstance(context).setFirstCityID(newid)) {
					// 切换城市
					TimeService.autoUpdateWeather(context, newid, "", false);
				}
			}
        }
    }

	/**
	 * @brief 【搜索城市】
	 * @n<b>函数名称</b>     :SearchCity
	 * @param sSearch
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-16下午06:13:34
	 * @<b>修改时间</b>      :
	*/
	void SearchCity(String sSearch) {
		if (!TextUtils.isEmpty(sSearch)) {
			if (m_DbMdl == null) {
				m_DbMdl = m_calendarMgr.Get_CoustoMdl_Interface();
			}
			
			mlvSreach.setVisibility(View.VISIBLE);
			m_gvHotCity.setVisibility(View.GONE);
			ArrayList<CityStruct> searchResult = new ArrayList<CityStruct>();
			
			// 查询数据填充
			mSearchList.clear();

			searchResult.clear();
			m_DbMdl.GetCityListByMsg(sSearch, searchResult);
			convert(mSearchList, searchResult);

			searchResult.clear();
			m_DbMdl.GetScenicsBySearch(sSearch, searchResult);
			convert(mSearchList, searchResult);

			searchResult.clear();
			m_DbMdl.GetForeignCityBySearch(sSearch, searchResult);
			convert(mSearchList, searchResult);
			
			mlvSreach.setAdapter(new ListViewAdapter(this, mSearchList));
		} else {
			mlvSreach.setVisibility(View.GONE);
			m_gvHotCity.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @brief 【转换数据】
	 * @n<b>函数名称</b>     :convert
	 * @param listDest
	 * @param listSrc
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-16下午06:44:47
	 * @<b>修改时间</b>      :
	*/ 
	void  convert (List<SearchInfo> listDest, List<CityStruct> listSrc) {
		if (!listSrc.isEmpty()) {
			int size = listSrc.size();
			for (int i = 0; i < size; i++) {
				CityStruct cityInfo = listSrc.get(i);
				SearchInfo searchInfo = new SearchInfo();
				searchInfo.setRecent(false);

				searchInfo.setText(cityInfo.getName());
				searchInfo.setNote("(" + cityInfo.getProvName() + ")"); // note
				searchInfo.setTag(cityInfo);
				listDest.add(searchInfo);
			}
		}
	}
	
	private void setSearchListViewListener() {
		metSreach.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				SearchCity(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});

		metSreach.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					SearchCity(metSreach.getText().toString());
					return true;
				}
				return false;
			}
		});

		mlvSreach.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchInfo info = mSearchList.get(position);
				CommitResult((CityStruct) info.getTag());
			}
		});
		
		m_gvHotCity.setOnItemClickListener(this);
	}
	
	private void CommitResult(CityStruct citySt) {
		AddCity(citySt, citySt.getGPS());
		finish();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long tid) {
		int id = parent.getId();
		
		if (id == R.id.widget_city_mgr_search_list) {
			SearchInfo info = mSearchList.get(position);
			CommitResult((CityStruct) info.getTag());
			
		} else if (id ==  R.id.widget_city_mgr_hot) {
			CommitResult(mCitylist.get(position));		
		} 
	}
	
	public static Toast toast;
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.widget_city_mgr_back_btn) {
			finish();
		} else if (id == R.id.widget_city_mgr_gps) {
	        if (HttpToolKit.isNetworkAvailable(this)) {
	        	getCityLocation();
	        	// 增加点击定位按钮提示 caizp 2014-10-10
	        	String str = getString(R.string.localing_text);
	            if (toast == null) {
	            	toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
	            } else {
	            	toast.setText(str);
	            }
	            toast.show();
	        } else {
	        	Toast.makeText(this, getString(R.string.no_net_to_local_text), Toast.LENGTH_SHORT).show();
	        }
		} else if (id == R.id.widget_city_mgr) {
			finish();
		}
	}

}
