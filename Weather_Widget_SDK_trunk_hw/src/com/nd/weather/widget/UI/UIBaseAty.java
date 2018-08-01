/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : UIBaseAty
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-7-1 下午03:29:04 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.ICalendarContext;

public class UIBaseAty extends Activity
{
	public ICalendarContext m_calendarMgr = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		m_calendarMgr = CalendarContext.getInstance(getBaseContext());
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}
	
    @Override
    public void finish() {
    	hindSoftInput(null);
        super.finish();
    }
    
    protected void hindSoftInput(IBinder windowToken) {
        try {
        	if (windowToken == null) {
        		windowToken = getCurrentFocus().getWindowToken();
			}
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }
}
