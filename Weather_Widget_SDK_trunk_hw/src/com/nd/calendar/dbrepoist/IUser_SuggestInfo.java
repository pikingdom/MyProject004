package com.nd.calendar.dbrepoist;

import java.util.List;

import com.calendar.CommData.SendSuggestInfo;

public interface IUser_SuggestInfo
{
	public void InitDb(IDatabaseRef dataBaseRef);
	//保持提问数据
	public boolean InsertSuggestInfo(SendSuggestInfo sendSuggestInfo);
	//判断是否有没有回答的数据
	public boolean GetIsHasNoAnser(List<SendSuggestInfo> listSendSuggestInfo);
	//更新数据缓存数据
	public boolean UpdateItemSuggestInfo(SendSuggestInfo senduggestInfo);
	//获取数据库缓存数据
	public boolean GetAllsuggestInfo(List<SendSuggestInfo> listSendSuggestInfo);
	
}
