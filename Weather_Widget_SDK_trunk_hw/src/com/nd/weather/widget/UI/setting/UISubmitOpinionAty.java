/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : UISubmitOpinionAty
* @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 邱堃
* @n@n<b>创建时间</b>: 2011-10-21 下午05:20:42 
* @n@n<b>文件描述</b>:  
* @version  
*/

package com.nd.weather.widget.UI.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.CommData.SendSuggestInfo;
import com.calendar.CommData.SuggestInfo;
import com.nd.calendar.Control.SuggestListAdapter;
import com.nd.calendar.common.ComDataDef.CalendarData;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.UIBaseAty;

public class UISubmitOpinionAty extends UIBaseAty implements OnClickListener
{
	private static final int	BLOG_MAX_LEN	= 140;
	private static final int    DIALOG_KEY      = 100;
	Button m_btnback;
	Button m_btnSubmin;
	EditText m_editSubMessage;
	//EditText m_editConti;
	TextView m_textViewHit;
	ListView m_SuggestListView;
	List<SendSuggestInfo> m_listSendSuggestInfo = null;
	Vector<SuggestInfo> m_suggestInfos = null;
	SuggestListAdapter m_suggestListAdapter = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_submitopinion);
		SetCtrl();
		InitData();
	}
	void SetCtrl()
	{
		//m_calendarMgr.GetSofeBk(findViewById(R.id.viewbkId), getWindowManager().getDefaultDisplay());
		m_btnback = (Button)findViewById(R.id.submintInfobackId);
		m_btnSubmin = (Button)findViewById(R.id.idSubmit);
		m_textViewHit = (TextView)findViewById(R.id.textViewHit);
		m_editSubMessage = (EditText)findViewById(R.id.edtBlogId);
		m_editSubMessage.addTextChangedListener(textwatcher);
		//m_editConti = (EditText)findViewById(R.id.contactId);
		m_SuggestListView = (ListView)findViewById(R.id.SuggestList);
		
		m_textViewHit.setText("您还可以输入140字");
		m_btnback.setOnClickListener(this);
		m_btnSubmin.setOnClickListener(this);
		
		m_editSubMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(BLOG_MAX_LEN)});
	}
	
	void InitData()
	{
		if(m_suggestInfos == null)
		{
			m_suggestInfos = new Vector<SuggestInfo>();
		}
		m_suggestInfos.clear();
	
		if(m_suggestListAdapter == null)
		{
			m_suggestListAdapter = new SuggestListAdapter(this, m_suggestInfos);
		}
		m_SuggestListView.setAdapter(m_suggestListAdapter);
		
		//初始化数据
		new ShowSuggestTask().execute();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.submintInfobackId) {
			finish();
		} else if (id == R.id.idSubmit) {
			SubmitOpint();
		}
	}
	
	private TextWatcher		textwatcher	= new TextWatcher()
	{

		@Override
		public void onTextChanged(
				CharSequence s, int start,
				int before, int count)
		{
		}

		@Override
		public void beforeTextChanged(
				CharSequence s, int start,
				int count, int after)
		{
		}

		@Override
		public void afterTextChanged(
				Editable s)
		{
			int ilen = BLOG_MAX_LEN - m_editSubMessage.getText().toString().length();
			String desc = String.format("%d字",ilen);
			m_textViewHit.setText(desc);
		}
	};
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_KEY: 
            {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("上传信息中..........");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }
        }
        return null;
    }
    
    private void ReshData()
    {
    	m_suggestInfos.clear();
		for(int i=0; i<m_listSendSuggestInfo.size(); i++)
		{
			
			SuggestInfo nTemp = new SuggestInfo();
			nTemp.setSuggest(m_listSendSuggestInfo.get(i).getQuest());
			nTemp.setSuggestDate(m_listSendSuggestInfo.get(i).getAsk_time());
			if(1 == m_listSendSuggestInfo.get(i).getFlag())
			{
				nTemp.setRespond(m_listSendSuggestInfo.get(i).getAnswer());
				nTemp.setRespondDate(m_listSendSuggestInfo.get(i).getAnswer_time());
			}
			else
			{
				nTemp.setRespond("等待处理");
				nTemp.setRespondDate("");
			}

			m_suggestInfos.add(nTemp);
		}
		m_suggestListAdapter.notifyDataSetChanged();
    }
    
    //获取内容
    private class ShowSuggestTask extends AsyncTask<Void, Integer, Integer>
    {

		protected void onPreExecute() 
		{
			//progress_largeId
			findViewById(R.id.progress_largeId).setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params)
		{
			//从本地获取数据
			if(m_listSendSuggestInfo == null)
			{
				m_listSendSuggestInfo = new ArrayList<SendSuggestInfo>();
			}
			m_listSendSuggestInfo.clear();
			
			//先从本地读取数据
			if(m_calendarMgr.Get_UserMdl_Interface().GetAllsuggestInfo(m_listSendSuggestInfo))
			{
				 publishProgress(0);
			}
			
			//从服务获取回答数据
			if(m_calendarMgr.Get_HttpMdl_Interface().GetListAnswer(CalendarData.GET_APPID()))
			{
				m_listSendSuggestInfo.clear();
				//有回答则刷新
				if(m_calendarMgr.Get_UserMdl_Interface().
						GetAllsuggestInfo(m_listSendSuggestInfo))
				{
					publishProgress(0); 
				}
			}
			
			return 1;
		}
		
	    protected void onProgressUpdate(Integer... values) 
	    {
	    	int nType = values[0];
	    	
	    	switch (nType)
			{
				case 0:
				{
					ReshData();
				}
				break;
				case 1:
					break;
				default:
					break;
			}
	    }
		
		protected void onPostExecute(Integer result)
		{
			findViewById(R.id.progress_largeId).setVisibility(View.GONE);
		}
    
    }
    
	private class ProgressTask extends AsyncTask<Void, Void, Integer>
	{
		String strContactinfo = null;
		String strSuggest = null;
		@Override 
		protected void onPreExecute() 
		{
			strContactinfo = "";//m_editConti.getText().toString().trim();
			strSuggest = m_editSubMessage.getText().toString().trim();
		}

		
		@Override
		protected Integer doInBackground(Void... params)
		{
			if(m_calendarMgr.Get_HttpMdl_Interface()
				.DownSuggestanswer(CalendarData.GET_APPID(), strContactinfo, strSuggest))
			{
				return 1;
			}
			return 0;
		}
		
		@Override 
		protected void onPostExecute(Integer result)
		{
			int nRes = (int)result;
			if(nRes == 1)
			{
				Toast.makeText(UISubmitOpinionAty.this, "提交意见成功", Toast.LENGTH_LONG).show();
				m_editSubMessage.setText("");
				//m_editConti.setText("");
				
				//先从本地读取数据
				m_listSendSuggestInfo.clear();
				if(m_calendarMgr.Get_UserMdl_Interface().
						GetAllsuggestInfo(m_listSendSuggestInfo))
				{
					ReshData();
				}
			
			}
			else
			{
				Toast.makeText(UISubmitOpinionAty.this, "提交意见失败", Toast.LENGTH_LONG).show();
			}
			dismissDialog(DIALOG_KEY);
		}
	}
	
	private boolean SubmitOpint()
	{
		String strSuggest = m_editSubMessage.getText().toString().trim();
		
		if(strSuggest.length()<1)
		{
			Toast.makeText(UISubmitOpinionAty.this, "文字不可为空", Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(strSuggest.length()>BLOG_MAX_LEN)
		{
			Toast.makeText(UISubmitOpinionAty.this, "输入建议的文字太长", Toast.LENGTH_LONG).show();
		}
		else
		{
			showDialog(DIALOG_KEY);
			new ProgressTask().execute();
		}
		
		return true;
	}

}

