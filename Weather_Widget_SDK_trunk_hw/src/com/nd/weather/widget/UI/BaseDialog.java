/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : BaseDialog
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-7-5 下午07:00:27 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.Control.OnMyDialogClickListener;

public class BaseDialog extends Dialog
{
	protected OnMyDialogClickListener m_OnMyDialogClickListener = null;
	protected ICalendarContext m_calendarMgr = null;
	
	protected Context mCtx;

	public BaseDialog(Context context) {

		super(context);
		m_calendarMgr = CalendarContext.getInstance(getContext());
		mCtx = context;
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		m_calendarMgr = CalendarContext.getInstance(getContext());
		mCtx = context;
	}

	/**
	 * Creates a new instance of BaseDialog.
	 * 
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		m_calendarMgr = CalendarContext.getInstance(getContext());
	}

	public void setOnMyDialogClickListener(OnMyDialogClickListener mOnMyDialogClickListener) {
		m_OnMyDialogClickListener = mOnMyDialogClickListener;
	}

    @Override
    public void dismiss() {
        try {
            // 先关闭键盘
            ((InputMethodManager) mCtx.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }

}
