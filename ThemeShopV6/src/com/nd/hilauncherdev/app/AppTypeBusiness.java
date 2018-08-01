package com.nd.hilauncherdev.app;

import android.view.View;
import android.widget.ImageView;

import com.nd.android.pandahome2.shop.R;

/**
 * 
 * 应用类型突出业务，首发，特约，官方等
 * @author 		linchen
 * @date		2012-12-10
 */

public class AppTypeBusiness {
	/**应用类型*/
	public static final	int APPTYPE_NORMAL					= 0;
	public static final int APPTYPE_PUBLISHING_FIRST 		= 2;//首发
	public static final int APPTYPE_PUBLISHING_EXCLUSIVE	= 4;//独家
	public static final int APPTYPE_OFFICIAL				= 32768;//官方
	public static final int APPTYPE_RECOMMEND				= 8;//推荐
	public static final int APPTYPE_CONTRIBUTING			= 16;//特约
	public static final int APPTYPE_FRESHNEW 				= 32;//抢先
	/**
	 * 列表中显示应用类型
	 * @param view		显示应用类型的ImageView
	 * @param typeID    应用类型字符串，可以是多种类型，如"2,4"，取最小值显示相应的图标
	 */
	public static void showType(ImageView view, String typeID) {
		
		int showID = getShowID(typeID);
		
		if (showID == APPTYPE_NORMAL) {
			view.setVisibility(View.GONE);
			return;
		}
		
		showView(view, showID);
	}
	
	private static void showView(ImageView view, int showID) {
		
		view.setVisibility(View.VISIBLE);
		switch (showID) {
		case APPTYPE_PUBLISHING_FIRST:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_first);
		}
		break;
		
		case APPTYPE_PUBLISHING_EXCLUSIVE:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_exclusive);
		}
		break;
		
		case APPTYPE_OFFICIAL:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_offical);
		}
		break;
		
		case APPTYPE_RECOMMEND:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_recommend);
		}
		break;
		
		case APPTYPE_CONTRIBUTING:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_contributing);
		}
		break;
		
		case APPTYPE_FRESHNEW:
		{
			view.setBackgroundResource(R.drawable.market_ic_app_freshnew);
		}
		break;
		
		default:
		{
			view.setVisibility(View.GONE);
		}
		break;
		}
	}

	private static int getShowID(String typeID) {
		if (null == typeID || 0 == typeID.length()) {
			return APPTYPE_NORMAL;
		}
		
		int ret = APPTYPE_NORMAL;
		
		String[] strs = typeID.split(",");
		ret = Integer.valueOf(strs[0].trim()).intValue();;
		return ret;
	}
}
