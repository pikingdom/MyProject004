/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : CalendarWeatherWidgetView_4x2
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-10 下午06:51:08 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.PandaHome;

import android.content.Context;
import android.util.AttributeSet;

import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetTask;
import com.nd.weather.widget.skin.WidgetLoadedSkinInfo;


public class CalendarWeatherWidgetView_4x2 extends PandaWidgetView 
{
//	private static final Class<?> WIDGET_PROVIDER = WidgetPandaProvider_4x2.class;
	private WidgetTask				mWidgetTask = null;
	private WidgetLoadedSkinInfo	mSkinInfo = null;

	//
	// 注意：该 context 是不完整、无权限的，无法使用数据库，除了广播外，不可使用!!!
	//
	public CalendarWeatherWidgetView_4x2(final Context context, AttributeSet attrs) {
		super(context, attrs);

		mWidgetType = WIDGET_4X2;
	}

	public void setHomePackage(String homePackage) {
		
	}

	/**
	 * @brief 【】
	 *
	 * @n<b>函数名称</b>     :onDestory
	 * @param arg0
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-7-10下午06:53:32      
	*/
	@Override
	public void onDestory(int widgetId) {
		super.onDestory(widgetId);
	}

	/**
	 * @brief 【】
	 *
	 * @n<b>函数名称</b>     :onLoad
	 * @param arg0
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-7-10下午06:53:32      
	*/
	@Override
	public void onLoad(int widgetId) {
		super.onLoad(widgetId, CALENDAR_WIDGET_LAYOUT_4x2, R.layout.weather_widget_panda_4x2_content);
	}

//	/**
//	 * @brief 【】
//	 *
//	 * @n<b>函数名称</b>     :sendEventToApp
//	 * @param action
//	 * @return
//	 * @<b>作者</b>          :  陈希
//	 * @<b>创建时间</b>      :  2012-8-9下午05:20:46      
//	*/
//	
//	@Override
//	protected void sendEventToApp(int action, int type) {
//		sendEventToApp(mContext, WIDGET_PROVIDER, mWidgetId, action, type);
//	}
//	
//	protected void sendClickEventToApp(int x, int y) {
//		Bundle bundle = new Bundle();
//		bundle.putInt(WidgetPandaIntent.UPDATE_ACTION_TYPE, WidgetPandaIntent.WIDGET_ACTION_FREE_CLICK);
//		bundle.putInt(WidgetPandaIntent.UPDATE_WIDGET_ID, mWidgetId);
//		bundle.putInt(WidgetPandaIntent.UPDATE_FREE_CLICK_X, x);
//		bundle.putInt(WidgetPandaIntent.UPDATE_FREE_CLICK_Y, y);
//		
//		sendEventToApp(mContext, WIDGET_PROVIDER, bundle);
//	}


	@Override
	protected WidgetLoadedSkinInfo getWidgetSkinInfo() {
		if (mSkinInfo == null) {
			mSkinInfo = new WidgetLoadedSkinInfo();
		}
		
		return mSkinInfo;
	}

	@Override
	protected void setWidgetSkinInfo(WidgetLoadedSkinInfo skinInfo) {
		mSkinInfo = skinInfo;
	}

	@Override
	protected void setWidgetBuilder(WidgetTask widgetBuilder) {
		mWidgetTask = widgetBuilder;
	}

	@Override
	protected WidgetTask getWidgetBuilder() {
		return mWidgetTask;
	}

	
	@Override
	protected int getWidgetLayout() {
		return R.id.panda_widget_4x2;
	}

	@Override
	protected int getWidgetLoadingBackround() {
		return R.drawable.loading_bg4x2;
	}
}
