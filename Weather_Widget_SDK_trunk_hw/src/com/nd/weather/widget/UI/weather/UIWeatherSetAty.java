
package com.nd.weather.widget.UI.weather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.CityStruct;
import com.nd.calendar.Control.DragListAdapter;
import com.nd.calendar.Control.DragListViewListener;
import com.nd.calendar.Control.OnMyDialogClickListener;
import com.nd.calendar.common.ComDataDef.ConfigSet;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.dbrepoist.UserInfo;
import com.nd.calendar.module.GpsSeverModule;
import com.nd.calendar.module.IGpsSeverModule;
import com.nd.calendar.module.IUserModule;
import com.nd.calendar.module.gps.LocManager.Result;
import com.nd.calendar.module.gps.MyLocation;
import com.nd.calendar.module.gps.MyLocation.LocationResult;
import com.nd.calendar.util.ComfunHelp;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.Ctrl.DragListView;
import com.nd.weather.widget.UI.UIBaseAty;
import com.nd.weather.widget.UI.setting.UISettingActivity;

public class UIWeatherSetAty extends UIBaseAty implements OnClickListener,
        android.content.DialogInterface.OnDismissListener, OnMyDialogClickListener,
        LocationListener {

    private Button m_btnBack;
    private TextView m_textPost;
    private CheckBox m_checkGps;
    private DragListView m_dragListView = null;
    private UICitySelectDialog m_citySeachdialog;

    // 管理菜单
    private Button m_btnCityAdd;
    private ViewGroup m_btnCityDel;
    private ViewGroup m_btnCityOrder;

    private ViewGroup m_llSetMenu;
    private ViewGroup m_llFinish;
    private TextView m_tvOperHint;

    private IGpsSeverModule m_gpsSeverMdl = null;
    private List<CityInfo> m_listData = null;
    private DragListAdapter m_listAdapter = null;
    private boolean mDataChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_city_mgr);

        SetCtl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断如果有安装黄历天气就直接启动黄历天气
//        if (ComfunHelp.checkApkExist(this, ComDataDef.CALENDAR_PACKAGE, ComDataDef.CALENDAR_VAR_CODE_2_3_0)
//        		&& (android.os.Build.VERSION.SDK_INT < 21  || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA)){
//            finish();
//        }
    }

    /**
     * @brief 【初始化设置控件】
     * @n<b>函数名称</b> :SetCtl
     * @see
     * @param
     * @return void
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-7-22上午11:01:42
     */
    void SetCtl() {

        m_btnBack = (Button) findViewById(R.id.weathersetbackId);
        m_textPost = (TextView) this.findViewById(R.id.list_item_textId);
        m_btnBack.setOnClickListener(this);

        m_btnCityAdd = (Button) findViewById(R.id.city_set_add);
        m_btnCityDel = (ViewGroup) findViewById(R.id.city_set_delete);
        m_btnCityOrder = (ViewGroup) findViewById(R.id.city_set_order);
        m_llSetMenu = (ViewGroup) findViewById(R.id.city_set_menu);
        m_llFinish = (ViewGroup) findViewById(R.id.llOperFinish);
        m_checkGps = (CheckBox) findViewById(R.id.gps_item_imageId);
        m_tvOperHint = (TextView) findViewById(R.id.tvOperHint);

        findViewById(R.id.btnFinish).setOnClickListener(this);
        m_btnCityAdd.setOnClickListener(this);
        m_btnCityDel.setOnClickListener(this);
        m_btnCityOrder.setOnClickListener(this);

        m_checkGps.setOnClickListener(this);
        m_gpsSeverMdl = new GpsSeverModule(this);

        m_btnBack.postDelayed(new Runnable() {
            public void run() {
                initData();
            }
        }, 30);

        IsGetGpsInfo();
    }

    /**
     * @brief 【初始化数据】
     * @n<b>函数名称</b> :initData
     * @see
     * @param
     * @return void
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-7-22上午11:00:35
     */
    void initData() {
        // 数据结果
        m_listData = new ArrayList<CityInfo>();
        m_calendarMgr.Get_UserMdl_Interface().GetCityList(m_listData);

        for (int i = 0; i < m_listData.size(); i++) {
            CityInfo cityInfo = m_listData.get(i);
            if (cityInfo.getCode().startsWith("1")) {
            	cityInfo.setProvName(m_calendarMgr.Get_CoustoMdl_Interface().GetProvNameByCityCode(
                        cityInfo.getCode()));
			}
            else if (cityInfo.getCode().startsWith("2")) {
            	cityInfo.setProvName(m_calendarMgr.Get_CoustoMdl_Interface().GetCountryNameByCityCode(
                        cityInfo.getCode()));
			} 
        }

        m_dragListView = (DragListView) findViewById(R.id.drag_list);
        m_listAdapter = new DragListAdapter(m_dragListView.getContext(), m_listData);
        m_dragListView.setDragListViewListener(new MyListViewListener());
        m_dragListView.setOnItemClickListener(onItemClick);
        m_dragListView.setAdapter(m_listAdapter);
        mDataChanged = false;
        
        // 保存配置(第一次进入天气界面，到这里设置界面，以后就可以不进来了)
        ConfigHelper c = ConfigHelper.getInstance(getApplicationContext());
        c.saveBooleanKey(ConfigSet.CONFIG_KEY_FIRST_TO_WEATHER, false);
        c.commit();
    }
    
    private OnItemClickListener onItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                switch (m_listAdapter.getMode()) {
                case DragListAdapter.MODE_DELETE:
                    m_listAdapter.setDeleteCheck(view);
                    break;
                case DragListAdapter.MODE_NORMAL:
                    // 进入城市天气
                    UIWeatherFragmentAty.setCityIndex(position);
                    UISettingActivity.setNeedClose();
                    finish();
                    break;
                default:
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * @brief 【插入列表项】
     * @n<b>函数名称</b> :AddItemData
     * @see
     * @param @param listInfonew
     * @return void
     * @<b>作者</b> : 邱堃
     * @<b>修改</b> : 陈希
     * @<b>创建时间</b> : 2011-7-22上午11:01:02
     */
    void AddItemData(CityInfo cityInfo) {

        if(null == cityInfo) return;
        if(null == m_listData) return;
        // 找到最大的序号，将其赋值给新城市
        int nSort = m_listData.size();

        for (int i = 0; i < m_listData.size(); i++) {
            CityInfo cInfo = m_listData.get(i);
            int n = cInfo.getSort();

            if (n >= nSort) {
                nSort = n + 1;
            }
        }

        cityInfo.setSort(nSort);

        boolean bExist = false;
        int iRet = m_calendarMgr.Get_UserMdl_Interface().SetCityInfo(cityInfo, false);
        if ((iRet == UserInfo.OPT_SUCCESS) || (iRet == UserInfo.OPT_IS_EXIST)) {
        	// 有可能插件那边定位存在了
            for (CityInfo lfInfo : m_listData) {
                if (lfInfo.getCode().equals(cityInfo.getCode())
                        && lfInfo.getName().equals(cityInfo.getName())) {
                    bExist = true;
                }
            }

            if (!bExist) {
                iRet = 1;
            }
        }

        if (iRet == 1) {
            String sProv = cityInfo.getProvName();
            if (sProv == null || sProv.equals("")) {                
                if (cityInfo.getCode().startsWith("1")) {
                	cityInfo.setProvName(m_calendarMgr.Get_CoustoMdl_Interface().GetProvNameByCityCode(
                            cityInfo.getCode()));
				}
                else if (cityInfo.getCode().startsWith("2")) {
                	cityInfo.setProvName(m_calendarMgr.Get_CoustoMdl_Interface().GetCountryNameByCityCode(
                            cityInfo.getCode()));
				}                
            }

            m_listData.add(cityInfo);
            m_listAdapter.notifyDataSetChanged();
        }

        mDataChanged = true;
        // 天气界面城市列表刷新
       UIWeatherFragmentAty.setRefreshRetrunFromSubAty(true);
    }

    /**
     * @brief【是否GPS开启，如果是执行GPS定位】
     * @n<b>函数名称</b> :IsGetGpsInfo
     * @see
     * @param
     * @return void
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-7-22上午11:02:00
     */
    void IsGetGpsInfo() {
        // 是否GPS开启
        if ((m_gpsSeverMdl.GetGpsOpenState()) && (HttpToolKit.isNetworkAvailable(this))) {
            m_btnBack.postDelayed(new Runnable() {
                public void run() {
                    getCityLocation();
                }
            }, 300);
        } else {
            m_checkGps.setChecked(false);
            m_textPost.setText("点击开关定位获取城市");
        }
    }

    // 列表事件监听
    class MyListViewListener implements DragListViewListener {

        @Override
        public void OnDrop(int iSrcPos, int iDstPos) {
        	CityInfo dragItem = m_listData.get(iSrcPos);
        	m_listData.remove(iSrcPos);
        	m_listData.add(iDstPos, dragItem);
        	
        	for (int i = 0; i < m_listData.size(); i++) {
				dragItem = m_listData.get(i);
				dragItem.setSort(i);
				m_calendarMgr.Get_UserMdl_Interface().SetCityInfo(dragItem, true);
			}
        	
        	m_listAdapter.notifyDataSetChanged();
            mDataChanged = true;
            // 天气界面城市列表刷新
            UIWeatherFragmentAty.setRefreshRetrunFromSubAty(true);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        
        if (id ==  R.id.weathersetbackId) { // 返回按钮
            doBack();
        } else if (id ==  R.id.city_set_add) {
            ShowCityAddDialog();
            
        } else if (id ==  R.id.city_set_delete) {
            setDeleteState();
            
        } else if (id ==  R.id.city_set_order) {
            setOrderState();
            
        } else if (id ==  R.id.btnFinish) {
            switch (m_listAdapter.getMode()) {
            case DragListAdapter.MODE_ORDER:
                break;
            case DragListAdapter.MODE_DELETE:
                deleteCities();
                break;

            default:
                break;
            };
            showFinishPanel(false, DragListAdapter.MODE_NORMAL);
           
        } else if (id ==  R.id.gps_item_imageId) {

            if (m_checkGps.isChecked()) {
                // GetGpsInfo ();
                //new ProgressTask().execute();
                if (checkLocationService()){
                    getCityLocation();
                }else{
                    // 弹出设置界面
                    // 代码中直接设置 Settings.Secure.putString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "network"); 
                    try {
						startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					} catch (Exception e) {
						try {
							startActivity(new Intent(Settings.ACTION_SETTINGS));
						} catch (Exception e1) {
							Toast.makeText(this, "无法打开GPS设置页面，请手动设置!", Toast.LENGTH_SHORT).show();
						}
					}
                    m_checkGps.setChecked(false);
                }
            } else {
                // 保存GPS开启关闭信息
                m_gpsSeverMdl.SetGpsOpenState(false);
                m_textPost.setText("点击开关定位获取城市");
            }
        }
    }
    
    void setDeleteState() {
        try {
            if (m_listAdapter.getCount() > 0) {
                showFinishPanel(true, DragListAdapter.MODE_DELETE);
            } else {
                Toast.makeText(this, "没有城市信息!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setOrderState() {
        try {
            if (m_listAdapter.getCount() > 0) {
                showFinishPanel(true, DragListAdapter.MODE_ORDER);
            } else {
                Toast.makeText(this, "没有城市信息!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief 【删除城市】
     * 
     * @n<b>函数名称</b> :deleteCities
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-25上午11:05:17
     */
    void deleteCities() {

        boolean bDelete = false;
        IUserModule userMdl = m_calendarMgr.Get_UserMdl_Interface();
        int i = 0;
        while (i < m_listData.size()) {

            CityInfo info = m_listData.get(i);

            if (info.isDeleteState()) {
                if (userMdl.DeleteCityInfo(info)) {
                    m_listData.remove(info);

                    bDelete = true;
                }
            } else {
                i++;
            }
        }

        if (bDelete) {
            mDataChanged = true;
            // 天气界面城市列表刷新
            UIWeatherFragmentAty.setRefreshRetrunFromSubAty(true);
        }

    }

    /**
     * @brief 【显示删除面板】
     * @n<b>函数名称</b> :showDeletePanel
     * @param bShow
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-5-14下午02:03:37
     * @<b>修改时间</b> :
     */
    void showFinishPanel(boolean bShow, int mode) {

        if (bShow) {
            m_llFinish.setVisibility(View.VISIBLE);
            m_llSetMenu.setVisibility(View.GONE);
            m_listAdapter.setMode(mode);
            switch (mode) {
            case DragListAdapter.MODE_DELETE:
                m_tvOperHint.setText(R.string.delete_city_hint);
                m_dragListView.setbEnableDrag(false);
                for (int i = 0; i < m_listData.size(); i++) {
                    m_listData.get(i).setDeleteState(false);
                }
                break;
            case DragListAdapter.MODE_ORDER:
                m_tvOperHint.setText(R.string.sort_city_hint);
                m_dragListView.setbEnableDrag(true);
                break;
            default:
                break;
            }
        } else {
            m_dragListView.setbEnableDrag(false);
            m_llFinish.setVisibility(View.GONE);
            m_llSetMenu.setVisibility(View.VISIBLE);

            m_listAdapter.setMode(DragListAdapter.MODE_NORMAL);
        }

        m_listAdapter.notifyDataSetChanged();
    }

    /**
     * @brief 【显示城市添加界面】
     * @n<b>函数名称</b> :ShowCityAddDialog
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2011-12-9下午03:44:14
     */
    void ShowCityAddDialog() {
        if (m_citySeachdialog == null) {
            m_citySeachdialog = new UICitySelectDialog(this, R.style.dialog);
            m_citySeachdialog.setContentView(R.layout.weather_add_city_view);
            m_citySeachdialog.setOnDismissListener(this);
            m_citySeachdialog.setOnMyDialogClickListener(this);
            m_citySeachdialog.setCanceledOnTouchOutside(false);

            m_citySeachdialog.getWindow().setSoftInputMode(
            		WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            		| WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);

            if (metrics != null && metrics.heightPixels < 480) {
                LinearLayout ll = (LinearLayout) m_citySeachdialog
                        .findViewById(R.id.ll_add_city_dlg);
                LayoutParams lp = ll.getLayoutParams();
                lp.height = ComfunHelp.dip2px(this, 380);
                ll.setLayoutParams(lp);
            }
        }

        m_citySeachdialog.show();
        m_citySeachdialog.resetCityDlg();
    }
    
    private boolean checkLocationService() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        boolean network_enabled = false;
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        return (gps_enabled || network_enabled);
    }

    private void getCityLocation(){
        m_checkGps.setChecked(true);
        m_textPost.setText("正在定位当前位置...");
        MyLocation l = new MyLocation();
        l.getLocation(this, new LocationResult() {
            
            @Override
            public void gotLocation(final Result location) {
                // 定位回来了
                if ((location != null) && (!UIWeatherSetAty.this.isFinishing())){
                    // 去服务器取城市代码
                    new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                            final CityStruct m_cityInfo = new CityStruct();
                            final boolean result = m_gpsSeverMdl.GetGpsInfoFromServer(location.latitude, location.longitude, m_cityInfo);
                            UIWeatherSetAty.this.runOnUiThread(new Runnable() {   
                                @Override
                                public void run() {
                                    if (!UIWeatherSetAty.this.isFinishing()) {
                                        if ((result) && (!TextUtils.isEmpty(m_cityInfo.getCode()))
                                                && (!TextUtils.isEmpty(m_cityInfo.getName()))) {
                                            // 成功
                                            CityInfo listInfonew = new CityInfo();
                                            listInfonew.setCode(m_cityInfo.getCode());
                                            listInfonew.setName(m_cityInfo.getName());
                                            listInfonew.setFromGps(1);
                                            AddItemData(listInfonew);
                                            m_gpsSeverMdl.SetGpsOpenState(false);
                                            m_textPost.setText(m_cityInfo.getName());
                                            m_checkGps.setChecked(false);
                                        } else {
                                            // 失败
                                            doGpsFail();
                                        }
                                    }
                                }
                            });
                        }
                    }).start();

                } else {
                    // 失败
                    if (!UIWeatherSetAty.this.isFinishing()) {
                        UIWeatherSetAty.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                doGpsFail();
                            }
                        });
                    }
                }
            }
        });
    }
    
    

    private void doGpsFail(){
        m_textPost.setText("定位当前位置失败,请重试");
        // 保存GPS开启关闭信息
        m_gpsSeverMdl.SetGpsOpenState(false);
        m_checkGps.setChecked(false);
    }

    private boolean doBack() {
        if (m_llFinish.getVisibility() == View.VISIBLE) {
            //如果是在删除确认面板则关闭它
            showFinishPanel(false, DragListAdapter.MODE_NORMAL);
            return false;
        }
        finish();
        return true;
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((KeyEvent.KEYCODE_BACK == keyCode) && (event.getRepeatCount() == 0)) {
            if (!doBack()){
                return true;
            }
        } 
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void SetOnSelectDataItem(CityStruct citySut) {
        CityInfo cityInfo = new CityInfo();
        cityInfo.setCode(citySut.getCode());
        cityInfo.setName(citySut.getName());
        cityInfo.setFromGps(0);
        cityInfo.setProvName(citySut.getProvName());

        AddItemData(cityInfo);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }    
    
    private void refreshNotify(){
        // 数据有变化，通知刷新
        if (mDataChanged){
            // 桌面插件刷新
        	WidgetUtils.notifyWidgetTimeChanged(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshNotify();
    }
    
    
    
}
