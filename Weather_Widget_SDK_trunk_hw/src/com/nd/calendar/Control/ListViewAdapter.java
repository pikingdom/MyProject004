/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : ListViewAdapter
* @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 邱堃
* @n@n<b>创建时间</b>: 2011-7-4 下午07:58:41 
* @n@n<b>文件描述</b>:  
* @version  
*/

package com.nd.calendar.Control;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nd.weather.widget.R;
import com.nd.weather.widget.CommData.SearchInfo;

//城市 搜索、黄历搜索列表
public class ListViewAdapter extends ArrayAdapter<SearchInfo>
{
	public final static int NORMAL      = 0;
	public final static int LAST_RECORD = 1;
	public final static int WITH_NOTE   = 2;	// Note数据在getStrweathJson()中
	
	
	private LayoutInflater mInflater;
	private int mMode = NORMAL;
	private int mTextColor = 0xff323232;
	
	public ListViewAdapter(Context context, List<SearchInfo> objects)
	{

		super(context, 0, objects);
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * @brief 显示模式，只能在设给控件之前设置
	 *
	 * @n<b>函数名称</b>     :setMode
	 * @param mode
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-10-17下午04:38:40      
	*/
	public void setMode(int mode)
	{
		mMode = mode;
	}
	
	public void setTextColor(int iColor)
	{
		mTextColor = iColor;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			// 加载列表项模板
			view = mInflater.inflate(R.layout.weather_seachlist_item, parent, false);

		}

		SearchInfo listInfo = getItem(position);
		TextView textView = (TextView) view.findViewById(R.id.list_item_text);

		if (mTextColor != 0) {
			textView.setTextColor(mTextColor);
		}

		Spanned spanyi = Html.fromHtml(listInfo.getText());
		textView.setText(spanyi);

		if ((mMode & WITH_NOTE) != 0) {
			TextView tvNote = (TextView) view.findViewById(R.id.list_item_note_text);
			tvNote.setText(listInfo.getNote());
			tvNote.setVisibility(View.VISIBLE);
		}

		if ((mMode & LAST_RECORD) != 0) {
			TextView textnearView = (TextView) view.findViewById(R.id.list_item_near_text);
			if (listInfo.isRecent()) {
				textnearView.setVisibility(View.VISIBLE);
				textnearView.setText("最近查询");
				textView.setTextColor(0XffD59029);
			} else {
				textnearView.setVisibility(View.GONE);
				textView.setTextColor(0xffcccccc);
			}
		}

		return view;

	}
}

