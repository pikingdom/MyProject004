/**   
*    
* @file
* @brief 【建议列表】
*
* @<b>文件名</b>      : SuggestListAdapter
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2011-11-17 下午04:57:16 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.calendar.Control;

import java.util.AbstractList;
import java.util.Vector;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calendar.CommData.SuggestInfo;
import com.nd.weather.widget.R;


public class SuggestListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Vector<SuggestInfo> mSuggestInfos;
	
	public SuggestListAdapter(Context context, AbstractList<SuggestInfo> suggestList)
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSuggestInfos = (Vector<SuggestInfo>)suggestList;
	}
	
	@Override
	public int getCount()
	{
		return mSuggestInfos.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return arg0;
	}
	
	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder vHolder;
		
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.weather_suggest_item, parent, false);
			vHolder = new ViewHolder();
			vHolder.tvSuggest = (TextView)convertView.findViewById(R.id.suggestText);
			vHolder.tvRespond = (TextView)convertView.findViewById(R.id.respondText);
			
			convertView.setTag(vHolder);
		}
		else
		{
			vHolder = (ViewHolder)convertView.getTag();
		}
		
		SuggestInfo suggestInfo = mSuggestInfos.get(position);
		String tString = suggestInfo.getSuggestDate();
		String displaysStr;
		
		// 建议
		if (tString.length() > 0)
		{
			displaysStr = replaceWebSymbo(suggestInfo.getSuggest())
						+ " <font color='#888888'>[" + tString + "]</font>";
		}
		else
		{
			displaysStr = replaceWebSymbo(suggestInfo.getSuggest());
		}
		
		Spanned spanSuggest = Html.fromHtml(displaysStr);
		vHolder.tvSuggest.setText(spanSuggest);
		
		// 回复
		tString = suggestInfo.getRespondDate();
		if (tString.length() > 0)
		{
			displaysStr = replaceWebSymbo(suggestInfo.getRespond())
						+ " <font color='#888888'>[" + tString + "]</font>";
		}
		else
		{
			displaysStr = replaceWebSymbo(suggestInfo.getRespond());
		}
		
		Spanned spanRespond = Html.fromHtml(displaysStr);
		vHolder.tvRespond.setText(spanRespond);

		return convertView;
	}
	
	final String replaceWebSymbo(String s) {
		return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	
	class ViewHolder
	{
		TextView tvSuggest;
		TextView tvRespond;
	}
}
