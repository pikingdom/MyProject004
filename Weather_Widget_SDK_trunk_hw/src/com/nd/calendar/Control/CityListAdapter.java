/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : CityListAdapter
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2011-9-22 下午06:47:38 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.calendar.Control;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calendar.CommData.CityStruct;
import com.nd.weather.widget.R;

public class CityListAdapter extends BaseAdapter
{
	private Context mContext = null;
	private List<CityStruct> cityList = null; // 城市数据
	private int mTextColor = 0;
	private int mTextSize = 0;
	LayoutInflater mInflater;
	
	Drawable mGpsDrawable = null;
	
	public CityListAdapter(Context c) {
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setData(List<CityStruct> cityList) {
		this.cityList = cityList;
	}

	public List<CityStruct> getData() {
		return this.cityList;
	}

	public void setTextColor(int color) {
		this.mTextColor = color;
	}

	public void setTextDipSize(int textSize) {
		this.mTextSize = textSize;
	}
	
	// 获取个数
	@Override
	public int getCount() {
		return (cityList) != null ? cityList.size() : 0;
	}

	// 获取在库中的位置
	@Override
	public Object getItem(int position) {
		return position;
	}

	// 获取ID
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			View vieItem = mInflater.inflate(R.layout.weather_city_grid_item, parent, false);

			textView = (TextView) vieItem.findViewById(R.id.CityTextId);
		} else {
			textView = (TextView) convertView;
		}

		if (mTextSize != 0) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
		}
		
		CityStruct cs = cityList.get(position);
		if (cs.getGPS() == 2) {
			textView.setTextColor(0xff5abfff);
		} else {
			textView.setTextColor(mTextColor);
		}
		
		textView.setText(cs.getName());

		return textView;
	}
}
