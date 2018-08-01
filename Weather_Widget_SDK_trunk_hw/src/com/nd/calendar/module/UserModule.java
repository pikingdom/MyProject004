/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : UserModule
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-5 下午07:59:08 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.module;

import java.util.List;

import android.content.Context;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.SendSuggestInfo;
import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.dbrepoist.IUserInfo;
import com.nd.calendar.dbrepoist.UserInfo;

public class UserModule implements IUserModule
{

	// 数据库层接口
	private IUserInfo	m_dbUserInfo	= null;
	
	private Context mContext;
	
	public UserModule(Context ctx, IDatabaseRef dataBaseRef) {
		mContext = ctx.getApplicationContext();
		if (mContext == null) {
			mContext = ctx;
		}
	    m_dbUserInfo = UserInfo.getInstance(mContext, dataBaseRef);
    }
	
	//////////////////////////////////////////////////////////////////////
	@Override
	public int GetCityList(List<CityInfo> cityList) {
		return  m_dbUserInfo.GetCityList(mContext, cityList);
	}

	@Override
	public boolean UpdateCityInfo(CityInfo cityInfo) {
		return m_dbUserInfo.UpdateCityInfo(mContext, cityInfo);
	}

	@Override
	public int SetCityInfo(CityInfo cityInfo, boolean bAutoUpdate) {
		return m_dbUserInfo.InsertCityInfo(mContext, cityInfo, bAutoUpdate);
	}

	@Override
	public boolean DeleteCityInfo(CityInfo cityInfo) {
		return m_dbUserInfo.DeleteCityInfo(mContext, cityInfo);
	}
	
    @Override
    public boolean setLocationCity(Context ctx, CityInfo city) {
        return m_dbUserInfo.setLocationCity(ctx, city);
    }

    @Override
    public CityInfo getLocationCity(Context ctx, int id) {
        return m_dbUserInfo.getLocationCity(ctx, id);
    }
    
	   //获取意见反馈缓存数据
    public boolean GetAllsuggestInfo(List<SendSuggestInfo> listSendSuggestInfo)
    {
        return m_dbUserInfo.GetUserSuggestIf().GetAllsuggestInfo(listSendSuggestInfo);
    }

}
