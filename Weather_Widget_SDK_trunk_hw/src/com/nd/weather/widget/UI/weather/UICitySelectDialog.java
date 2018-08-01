package com.nd.weather.widget.UI.weather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.calendar.CommData.CityStruct;
import com.nd.calendar.Control.CityListAdapter;
import com.nd.calendar.Control.ListViewAdapter;
import com.nd.calendar.module.ICoustoModule;
import com.nd.weather.widget.R;
import com.nd.weather.widget.CommData.SearchInfo;
import com.nd.weather.widget.UI.BaseDialog;

public class UICitySelectDialog extends BaseDialog implements android.view.View.OnClickListener,
		OnItemClickListener, OnCheckedChangeListener
{
	private String Tag = "CitySelectDialog";

	private static final String POPULAR_CITIES[][] = { { "北京", "101010100" }, { "上海", "101020100" },
			{ "重庆", "101040100" }, { "天津", "101030100" }, { "广州", "101280101" },
			{ "杭州", "101210101" }, { "成都", "101270101" }, { "南京", "101190101" },
			{ "深圳", "101280601" }, { "武汉", "101200101" }, { "西安", "101110101" },
			{ "福州", "101230101" } };

	private static final String POPULAR_SCENIC[][] = { { "丽江", "101291401" },
			{ "九寨沟", "101271906" }, { "杭州西湖", "101210101" }, { "亚龙湾", "101310201" },
			{ "桂林漓江", "101300510" }, { "张家界", "101251101" }, { "黄山", "101221001" },
			{ "布达拉宫", "101140101" } };

	private static final String POPULAR_FOREIGN_CITY[][] = { { "威尼斯", "207800015" },
			{ "波士顿", "213300013" }, { "纽约", "213300024" }, { "伦敦", "213800016" },
			{ "普吉岛", "210900009" }, { "巴黎", "210300010" }, { "东京", "209300030" },
			{ "曼谷", "210900004" }, { "迪拜", "215400003" }, { "首尔", "215500007" } };
	
	//////////////////////////////////////////////////////////////////////////
	Context mContext;
	// view
	private LinearLayout m_layoutNavAll = null; // 1.主界面
	private GridView m_gvHot = null; // 热门城市 热门 景区 热门国外城市
	private GridView m_gvAll = null; // 其余的普通省 外国

	private GridView m_gvCity = null; // 省份下的城市、外国下的城市

	private LinearLayout m_layoutNavSel = null; // 2.根据选择的省份、城市的导航
	private TextView m_itemNav1 = null; // 全国
	private TextView m_itemNav2 = null; // 省
	private TextView m_itemNav3 = null; // 市
	private TextView m_icoNav3 = null; // 市图标

	private EditText m_etSeach = null;

	private RadioGroup m_CityGroup;

	private ScrollView m_ForeignCountryView;

	private GridView m_gvForeignCountry3;
	private GridView m_gvForeignCountry2;
	private GridView m_gvForeignCountry1;

	private int m_nViewType = 0; // 0 省份 1旅游地 2 外国
	private int m_nViewLeve = 00;// 0 = 省选择, 1 = 城市导航选择(地区), 2 = 城市导航选择(市)
	private boolean m_bViewTypeChanged = false;
	private ListView m_listViewSearch = null; // 3.搜索结果
	private ListViewAdapter m_adapterSearch = null;
	private List<SearchInfo> m_searchList;

	private int m_nColorFinalOption = 0;
	private int m_nColorMiddleOption = 0;

	CityStruct m_curProv;
	CityStruct m_curCity;

	// Adapter for grid view
	private CityListAdapter m_adapterHotCity; // 热门城市适配器
	private CityListAdapter m_adapterHotScenic; // 热门景区适配器
	private CityListAdapter m_adapterAllProv; // 省份适配器
	private CityListAdapter m_adapterCity; // 省份下的城市适配器
	private CityListAdapter m_adapterCountry; // 城市下的县适配器
	private CityListAdapter m_adapterScenic; // 省份下的景区适配器

	private CityListAdapter m_adapterHotForeignCity; // 国外热门城市
	private CityListAdapter m_adapterForeignCountry3; // 外国(三列)
	private CityListAdapter m_adapterForeignCountry2; // 外国(两列)
	private CityListAdapter m_adapterForeignCountry1; // 外国(一列)
	private CityListAdapter m_adapterForeignCity; // x国城市

	// 界面级别
	private final int LEVEL_ONE_INTERFACE = 1;
	private final int LEVEL_TWO_INTERFACE = 2;
	private final int LEVEL_THREE_INTERFACE = 3;

	// vector
	private ArrayList<CityStruct> m_vecHotCity; // 热门城市 level_1 m_adapterHotCity
	private ArrayList<CityStruct> m_vecHotScenic; // 热门景区 level_1
	// m_adapterHotScenic
	private ArrayList<CityStruct> m_vecAllProv; // 所有省份 level_1 m_adapterAllProv
	private ArrayList<CityStruct> m_vecCity; // x省份下的城市 level_2 m_adapterCity
	private ArrayList<CityStruct> m_vecScenic; // x省份下的景区 level_2 m_adapterScenic
	private ArrayList<CityStruct> m_vecCountry; // y城市下的县 level_3 m_adapterCountry

	private ArrayList<CityStruct> m_vecHotForeignCity; // 热门国外城市 level_1
	// m_adapterHotForeignCity
	private List<CityStruct> m_vecForeignCountry3; // 国外国家 (国家名称<=四个字)
	private List<CityStruct> m_vecForeignCountry2; // 国外国家 (国家名称>四个字<八个字)
	private List<CityStruct> m_vecForeignCountry1; // 国外国家 (国家名称>=八个字)
	// m_adapterForeignCountry
	private ArrayList<CityStruct> m_vecForeignCity; // 国外城市 level_2
	// m_adapterAllForeignCity

	ArrayList<CityStruct> m_VecCitySearchResult; // 国内城市搜索结果
	ArrayList<CityStruct> m_VecScenicSearchResult; // 国内旅游地搜索结果
	ArrayList<CityStruct> m_VecForeignCitySearchResult; // 国外城市搜索结果

	// 数据接口
	private ICoustoModule m_DbMdl;

	//////////////////////////////////////////////////////////////////////////
	public UICitySelectDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public UICitySelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			if (doBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetCtrl();
		initDataStruct();
		m_nViewLeve = LEVEL_ONE_INTERFACE;
		refreshInterface();

		// 接触对话框外区域时隐藏
		setCanceledOnTouchOutside(true);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	void SetCtrl() {
		setContentView(R.layout.weather_add_city_view);
		m_listViewSearch = (ListView) findViewById(R.id.cityselect_lv_search_city);
		m_layoutNavAll = (LinearLayout) findViewById(R.id.cityselect_ll_all_city);

		m_gvHot = (GridView) findViewById(R.id.cityselect_gv_hot);
		m_gvAll = (GridView) findViewById(R.id.cityselect_gv_all);
		m_gvCity = (GridView) findViewById(R.id.cityselect_gv_city);

		m_layoutNavSel = (LinearLayout) findViewById(R.id.cityselect_ll_navigate);
		m_itemNav1 = (TextView) findViewById(R.id.cityselect_tv_navItem1);
		m_itemNav2 = (TextView) findViewById(R.id.cityselect_tv_navItem2);
		m_itemNav3 = (TextView) findViewById(R.id.cityselect_tv_navItem3);
		m_icoNav3 = (TextView) findViewById(R.id.cityselect_tv_navIco3);
		m_etSeach = (EditText) findViewById(R.id.editText_prompt);

		m_CityGroup = (RadioGroup) findViewById(R.id.rg_city_group);

		m_ForeignCountryView = (ScrollView) findViewById(R.id.scv_foreign_country);

		m_gvForeignCountry3 = (GridView) findViewById(R.id.gv_three_cols);
		m_gvForeignCountry2 = (GridView) findViewById(R.id.gv_two_cols);
		m_gvForeignCountry1 = (GridView) findViewById(R.id.gv_one_cols);

		m_CityGroup.setOnCheckedChangeListener(this);

		m_gvHot.setOnItemClickListener(this);
		m_gvAll.setOnItemClickListener(this);
		m_gvCity.setOnItemClickListener(this);
		m_gvForeignCountry3.setOnItemClickListener(this);
		m_gvForeignCountry2.setOnItemClickListener(this);
		m_gvForeignCountry1.setOnItemClickListener(this);

		// 城市导航面板

		// 全国，切回省面板
		((TextView) findViewById(R.id.cityselect_tv_navItem1)).setOnClickListener(this);

		// 省，切到地区面板
		m_itemNav2.setOnClickListener(this);

		// 返回
		((Button) findViewById(R.id.cityAddBackId)).setOnClickListener(this);

		// 搜索
		m_searchList = new ArrayList<SearchInfo>();
		m_adapterSearch = new ListViewAdapter(m_listViewSearch.getContext(), m_searchList);
		m_adapterSearch.setMode(ListViewAdapter.WITH_NOTE);
		m_adapterSearch.setTextColor(getContext().getResources().getColor(R.color.navy));
		setSearchListViewListener();
	}

	public void resetCityDlg() {
		m_etSeach.setText("");
		m_listViewSearch.setVisibility(View.GONE);
		m_layoutNavSel.setVisibility(View.GONE);
		m_layoutNavAll.setVisibility(View.VISIBLE);
		m_CityGroup.setVisibility(View.VISIBLE);
		m_nViewLeve = LEVEL_ONE_INTERFACE;
	}

	private void initDataStruct() {
		m_nColorFinalOption = getContext().getResources().getColor(R.color.city_hot_color);
		m_nColorMiddleOption = getContext().getResources().getColor(R.color.city_mgr_color);

		m_vecAllProv = new ArrayList<CityStruct>();
		m_vecHotCity = new ArrayList<CityStruct>();
		m_vecHotScenic = new ArrayList<CityStruct>();
		m_vecCity = new ArrayList<CityStruct>();
		m_vecScenic = new ArrayList<CityStruct>();
		m_vecCountry = new ArrayList<CityStruct>();

		m_vecHotForeignCity = new ArrayList<CityStruct>();
		m_vecForeignCountry3 = new ArrayList<CityStruct>();
		m_vecForeignCountry2 = new ArrayList<CityStruct>();
		m_vecForeignCountry1 = new ArrayList<CityStruct>();
		m_vecForeignCity = new ArrayList<CityStruct>();

		m_VecCitySearchResult = new ArrayList<CityStruct>();
		m_VecScenicSearchResult = new ArrayList<CityStruct>();
		m_VecForeignCitySearchResult = new ArrayList<CityStruct>();

		m_curProv = new CityStruct();
		m_curCity = new CityStruct();
		m_curProv.setCode("-1");
		m_curCity.setCode("-1");

		m_DbMdl = m_calendarMgr.Get_CoustoMdl_Interface();
	}

	private void refreshInterface() {
		// TODO Auto-generated method stub
		switch (m_nViewLeve) {
		case LEVEL_ONE_INTERFACE: {
			switch (getViewType()) {
			case 0:
				fillHotCityView();
				fillAllProvView();
				break;
			case 1:
				fillHotScenicView();
				fillAllProvView();
				break;
			case 2:
				fillHotForeignCityView();
				fillForeignCountryView();
				break;
			default:
				fillHotCityView();
				fillAllProvView();
				break;
			}

			if (m_nViewType == 2) {
				// 国外城市是单独一个列表
				m_ForeignCountryView.setVisibility(View.VISIBLE);
				m_gvAll.setVisibility(View.GONE);
			} else {
				m_ForeignCountryView.setVisibility(View.GONE);
				m_gvAll.setVisibility(View.VISIBLE);
			}
			m_gvHot.setVisibility(View.VISIBLE);
			m_layoutNavAll.setVisibility(View.VISIBLE);
			m_layoutNavSel.setVisibility(View.GONE);
			m_listViewSearch.setVisibility(View.GONE);
		}
			break;
		case LEVEL_TWO_INTERFACE: {
			if (m_bViewTypeChanged) {
				m_nViewLeve = LEVEL_ONE_INTERFACE;
				refreshInterface();
			} else {
				switch (getViewType()) {
				case 0:
					fillCityOfProvView();
					m_itemNav1.setText("全国");
					break;
				case 1:
					fillScenicOfProvView();
					m_itemNav1.setText("全国");
					break;
				case 2:
					fillCityOfForeignCountryView();
					m_itemNav1.setText("国外");
					break;
				default:
					fillCityOfProvView();
					m_itemNav1.setText("全国");
					break;
				}
				m_gvCity.setVisibility(View.VISIBLE);

				m_itemNav1.setVisibility(View.VISIBLE);
				m_layoutNavSel.setVisibility(View.VISIBLE);
				m_layoutNavAll.setVisibility(View.GONE);
				m_listViewSearch.setVisibility(View.GONE);
				m_itemNav2.setText(m_curProv.getName());
				m_itemNav2.setVisibility(View.VISIBLE);
				m_itemNav3.setVisibility(View.GONE);
				m_icoNav3.setVisibility(View.GONE);
			}
			break;
		}
		case LEVEL_THREE_INTERFACE: {
			if (0 == getViewType()) {
				fillCountryOfCityView();
				m_gvCity.setVisibility(View.VISIBLE);

				m_layoutNavSel.setVisibility(View.VISIBLE);
				m_layoutNavAll.setVisibility(View.GONE);
				m_listViewSearch.setVisibility(View.GONE);

				m_itemNav2.setText(m_curProv.getName());
				m_itemNav2.setVisibility(View.VISIBLE);
				m_itemNav3.setText(m_curCity.getName());
				m_itemNav3.setVisibility(View.VISIBLE);
				m_icoNav3.setVisibility(View.VISIBLE);
			} else {
				m_nViewLeve = LEVEL_TWO_INTERFACE;
				refreshInterface();
			}
		}
			break;

		default:
			break;
		}
		m_bViewTypeChanged = false;
	}

	/* 设置列表高度 */
	void setGridViewHeight(GridView gv, int numColumns) {
		int height = 0;
		ListAdapter la = gv.getAdapter();
		int count = la.getCount();
		if (count % numColumns > 0) {
			count = count / numColumns + 1;
		} else {
			count = count / numColumns;
		}
		if (count > 0) {
			View view = la.getView(0, null, gv);
			view.measure(0, 0);
			height = view.getMeasuredHeight();
		}
		LayoutParams lp = gv.getLayoutParams();
		int space = mContext.getResources().getDimensionPixelSize(
				R.dimen.gird_view_vertical_spacing);
		lp.height = height * count + space * count;
		gv.setLayoutParams(lp);
	}

	public void fillHotCityView() {
		m_gvHot.setNumColumns(4);
		if (m_adapterHotCity == null) {
			m_adapterHotCity = new CityListAdapter(m_gvHot.getContext());
		}
		if (m_adapterHotCity != null) {
			m_adapterHotCity.setTextColor(m_nColorFinalOption);
			// 如果热门城市容器不为空，即已经读取一遍不再读取
			if (m_vecHotCity.isEmpty()) {
				for (String[] cityInfo : POPULAR_CITIES) {
					m_vecHotCity.add(new CityStruct(cityInfo[0], cityInfo[1]));
				}
				m_adapterHotCity.setData(m_vecHotCity);
			}
			m_gvHot.setAdapter(m_adapterHotCity);
			m_adapterHotCity.notifyDataSetChanged();

			setGridViewHeight(m_gvHot, 4);
		}

	}

	public void fillHotScenicView() {
		m_gvHot.setNumColumns(3);
		if (m_adapterHotScenic == null) {
			m_adapterHotScenic = new CityListAdapter(m_gvHot.getContext());
		}
		if (m_adapterHotScenic != null) {
			m_adapterHotScenic.setTextColor(m_nColorFinalOption);
			// 如果热门城市容器不为空，即已经读取一遍不再读取
			if (m_vecHotScenic.isEmpty()) {
				for (String[] cityInfo : POPULAR_SCENIC) {
					m_vecHotScenic.add(new CityStruct(cityInfo[0], cityInfo[1]));
				}
				m_adapterHotScenic.setData(m_vecHotScenic);
			}
			m_gvHot.setAdapter(m_adapterHotScenic);
			m_adapterHotScenic.notifyDataSetChanged();
			setGridViewHeight(m_gvHot, 3);
		}
	}

	public void fillAllProvView() {
		// 省份
		if (m_adapterAllProv == null) {
			m_adapterAllProv = new CityListAdapter(m_gvAll.getContext());
		}
		if (m_adapterAllProv != null) {
			if (m_adapterAllProv.isEmpty()) {
				m_vecAllProv.clear();
				m_DbMdl.GetAllProvWithSort(m_vecAllProv, null);
				m_adapterAllProv.setTextColor(m_nColorMiddleOption);
				m_adapterAllProv.setData(m_vecAllProv);
			}
			m_gvAll.setAdapter(m_adapterAllProv);
			m_adapterAllProv.notifyDataSetChanged();
		}
	}

	public void fillHotForeignCityView() {
		m_gvHot.setNumColumns(4);
		if (m_adapterHotForeignCity == null) {
			m_adapterHotForeignCity = new CityListAdapter(m_gvHot.getContext());
		}
		if (m_adapterHotForeignCity != null) {
			if (m_adapterHotForeignCity.isEmpty()) {
				for (String[] cityInfo : POPULAR_FOREIGN_CITY) {
					m_vecHotForeignCity.add(new CityStruct(cityInfo[0], cityInfo[1]));
				}
				m_adapterHotForeignCity.setTextColor(m_nColorFinalOption);
				m_adapterHotForeignCity.setData(m_vecHotForeignCity);
			}
			m_gvHot.setAdapter(m_adapterHotForeignCity);
			m_adapterHotForeignCity.notifyDataSetChanged();
			setGridViewHeight(m_gvHot, 4);
		}
	}

	public void fillForeignCountryView() {
		if (m_vecForeignCountry3.size() <= 0) {
			m_DbMdl.GetForeignCountryWithSort(m_vecForeignCountry3, m_vecForeignCountry2,
					m_vecForeignCountry1, null);
		}
		if (m_adapterForeignCountry3 == null) {

			m_adapterForeignCountry3 = new CityListAdapter(m_gvAll.getContext());
			m_adapterForeignCountry3.setTextColor(m_nColorMiddleOption);
			m_adapterForeignCountry3.setData(m_vecForeignCountry3);
			m_gvForeignCountry3.setAdapter(m_adapterForeignCountry3);
			m_adapterForeignCountry3.notifyDataSetChanged();
			setGridViewHeight(m_gvForeignCountry3, 3);
		}
		if (m_adapterForeignCountry2 == null) {
			m_adapterForeignCountry2 = new CityListAdapter(m_gvAll.getContext());
			m_adapterForeignCountry2.setTextColor(m_nColorMiddleOption);
			m_adapterForeignCountry2.setData(m_vecForeignCountry2);
			m_gvForeignCountry2.setAdapter(m_adapterForeignCountry2);
			m_adapterForeignCountry2.notifyDataSetChanged();
			setGridViewHeight(m_gvForeignCountry2, 2);
		}
		if (m_adapterForeignCountry1 == null) {
			m_adapterForeignCountry1 = new CityListAdapter(m_gvAll.getContext());
			m_adapterForeignCountry1.setTextColor(m_nColorMiddleOption);
			m_adapterForeignCountry1.setData(m_vecForeignCountry1);
			m_gvForeignCountry1.setAdapter(m_adapterForeignCountry1);
			m_adapterForeignCountry1.notifyDataSetChanged();
			setGridViewHeight(m_gvForeignCountry1, 1);
		}
	}

	public void fillCityOfForeignCountryView() {
		if (m_adapterForeignCity == null) {
			m_adapterForeignCity = new CityListAdapter(m_gvCity.getContext());
		}
		if (m_adapterForeignCity != null) {
			m_vecForeignCity.clear();
			m_DbMdl.GetForeignCityByAreaCode(m_curProv.getCode(), m_vecForeignCity);
			m_adapterForeignCity.setTextColor(m_nColorFinalOption);
			m_adapterForeignCity.setData(m_vecForeignCity);
			m_gvCity.setAdapter(m_adapterForeignCity);
			m_adapterForeignCity.notifyDataSetChanged();
		}
	}

	public void fillCityOfProvView() {
		if (m_adapterCity == null) {
			m_adapterCity = new CityListAdapter(m_gvCity.getContext());
		}
		if (m_adapterCity != null) {
			m_vecCity.clear();
			m_DbMdl.GetAllAreaByprovcode(m_curProv.getCode(), m_vecCity);
			m_adapterCity.setTextColor(m_nColorMiddleOption);
			m_adapterCity.setData(m_vecCity);
			m_gvCity.setAdapter(m_adapterCity);
			m_adapterCity.notifyDataSetChanged();
		}
	}

	public void fillScenicOfProvView() {
		if (m_adapterScenic == null) {
			m_adapterScenic = new CityListAdapter(m_gvCity.getContext());
		}
		if (m_adapterScenic != null) {
			m_vecScenic.clear();
			m_DbMdl.GetScenicByProvName(m_curProv.getName(), m_vecScenic);
			m_adapterScenic.setTextColor(m_nColorFinalOption);
			m_adapterScenic.setData(m_vecScenic);
			m_gvCity.setAdapter(m_adapterScenic);
			m_adapterScenic.notifyDataSetChanged();
		}
	}

	public void fillCountryOfCityView() {
		if (m_adapterCountry == null) {
			m_adapterCountry = new CityListAdapter(m_gvCity.getContext());
		}
		if (m_adapterCountry != null) {
			m_vecCountry.clear();
			m_DbMdl.GetAllCityByareacode(m_curCity.getCode(), m_vecCountry);
			m_adapterCountry.setTextColor(m_nColorFinalOption);
			m_adapterCountry.setData(m_vecCountry);
			m_gvCity.setAdapter(m_adapterCountry);
			m_adapterCountry.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		CityStruct city = null;
		int id = arg0.getId();
		
		if (id ==  R.id.cityselect_gv_hot) { // 热门城市 热门景区 外国热门城市
			if (0 == m_nViewType) {
				city = m_adapterHotCity.getData().get(arg2);
			} else if (1 == m_nViewType) {
				city = m_adapterHotScenic.getData().get(arg2);
			} else if (2 == m_nViewType) {
				city = m_adapterHotForeignCity.getData().get(arg2);
			}
			CommitResult(city);
		} else if (id ==  R.id.cityselect_gv_all) { // 默认界面的省份
			if (0 == getViewType() || 1 == getViewType()) {
				m_curProv = m_adapterAllProv.getData().get(arg2);
			}
			m_nViewLeve = LEVEL_TWO_INTERFACE;
			refreshInterface();
			
		} else if (id ==  R.id.gv_one_cols) {
			m_curProv = m_adapterForeignCountry1.getData().get(arg2);
			m_nViewLeve = LEVEL_TWO_INTERFACE;
			refreshInterface();
			
		} else if (id ==  R.id.gv_two_cols) {
			m_curProv = m_adapterForeignCountry2.getData().get(arg2);
			m_nViewLeve = LEVEL_TWO_INTERFACE;
			refreshInterface();
			
		} else if (id ==  R.id.gv_three_cols) {
			m_curProv = m_adapterForeignCountry3.getData().get(arg2);
			m_nViewLeve = LEVEL_TWO_INTERFACE;
			refreshInterface();
			

		} else if (id ==  R.id.cityselect_gv_city) { // 导航的界面 包括以下情况：1、x省份下的城市 2、x省份下的景区
			// 3、y城市下的县
			if (getViewType() == 1) {
				city = m_adapterScenic.getData().get(arg2);
				CommitResult(city);
			} else if (getViewType() == 2) {
				city = m_adapterForeignCity.getData().get(arg2);
				CommitResult(city);
			}

			if (getViewType() == 0) {
				if (m_nViewLeve == LEVEL_TWO_INTERFACE) {
					m_curCity = m_adapterCity.getData().get(arg2);
					m_nViewLeve = LEVEL_THREE_INTERFACE;
					refreshInterface();
				} else if (m_nViewLeve == LEVEL_THREE_INTERFACE) {
					city = m_adapterCountry.getData().get(arg2);
					CommitResult(city);
				}
			}
		}
	}

	private void CommitResult(CityStruct citySt) {
		// TODO Auto-generated method stub
		if (m_OnMyDialogClickListener != null) {
			m_OnMyDialogClickListener.SetOnSelectDataItem(citySt);
		}
		UICitySelectDialog.this.dismiss();
	}

	@Override
	public void onClick(View v) {
		
		int id = (v.getId());
		if (id ==  R.id.cityselect_tv_navItem1) { // 默认界面
			m_nViewLeve = LEVEL_ONE_INTERFACE;
			refreshInterface();
			
		} else if (id ==  R.id.cityselect_tv_navItem2) { // 一级导航界面 省份下的城市 或者 省份下的景区
			m_nViewLeve = LEVEL_TWO_INTERFACE;
			refreshInterface();
			
		} else if (id ==  R.id.cityAddBackId) { // 返回键，推出对话框
			if (!doBack()) {
				this.dismiss();
			}
		}
	}

	private void changeViewType() {
		// TODO Auto-generated method stub
		refreshInterface();
		if (m_etSeach.getText().toString() != null) {
			SearchCity(m_etSeach.getText().toString());
		}
	}

	private void setSearchListViewListener() {
		m_etSeach.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.v(Tag, "text change");
				SearchCity(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});

		m_etSeach.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					Log.v(Tag, "key listener");
					SearchCity(m_etSeach.getText().toString());
					return true;
				}
				return false;
			}
		});

		m_listViewSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchInfo info = m_searchList.get(position);
				CommitResult((CityStruct) info.getTag());
			}

		});
	}
	void SearchCity(String sSearch) {
		if (!TextUtils.isEmpty(sSearch)) {
			m_listViewSearch.setVisibility(View.VISIBLE);
			m_CityGroup.setVisibility(View.GONE);
			m_layoutNavSel.setVisibility(View.GONE);
			m_layoutNavAll.setVisibility(View.GONE);

			// 查询数据填充
			m_searchList.clear();

			// if (0 == getViewType()) {
			m_VecCitySearchResult.clear();
			m_DbMdl.GetCityListByMsg(sSearch, m_VecCitySearchResult);
			convert(m_searchList, m_VecCitySearchResult);

			m_VecScenicSearchResult.clear();
			m_DbMdl.GetScenicsBySearch(sSearch, m_VecScenicSearchResult);
			convert(m_searchList, m_VecScenicSearchResult);

			m_VecForeignCitySearchResult.clear();
			m_DbMdl.GetForeignCityBySearch(sSearch, m_VecForeignCitySearchResult);
			convert(m_searchList, m_VecForeignCitySearchResult);
			
			m_listViewSearch.setAdapter(m_adapterSearch);
		} else {
			m_listViewSearch.setVisibility(View.GONE);
			m_CityGroup.setVisibility(View.VISIBLE);
			refreshInterface();
		}
	}

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

	private int getViewType() {
		// TODO Auto-generated method stub
		return m_nViewType;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int id = checkedId;
		
		if (id ==  R.id.rbtn_prov) {
			if (m_nViewLeve != 0) {
				m_nViewType = 0;
				m_bViewTypeChanged = true;
			}
			changeViewType();
			
		} else if (id ==  R.id.rbtn_travel) {
			if (m_nViewType != 1) {
				m_nViewType = 1;
				m_bViewTypeChanged = true;
			}
			changeViewType();
			
		} else if (id ==  R.id.rbtn_foreign) {
			if (m_nViewType != 2) {
				m_nViewType = 2;
				m_bViewTypeChanged = true;
			}
			changeViewType();
		}
	}

	/* 返回处理 */
	private boolean doBack() {
		if (m_listViewSearch.isShown()) {
			resetCityDlg();
			return true;
		} else {
			switch (m_nViewLeve) {
			case LEVEL_TWO_INTERFACE:
				m_nViewLeve = LEVEL_ONE_INTERFACE;
				refreshInterface();
				return true;
			case LEVEL_THREE_INTERFACE:
				m_nViewLeve = LEVEL_TWO_INTERFACE;
				refreshInterface();
				return true;
			default:
				break;
			}
		}
		return false;
	}

}
