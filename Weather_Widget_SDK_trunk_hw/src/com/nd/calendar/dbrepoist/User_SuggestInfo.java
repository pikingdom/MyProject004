package com.nd.calendar.dbrepoist;

import java.util.List;

import android.database.Cursor;

import com.calendar.CommData.SendSuggestInfo;

public class User_SuggestInfo implements IUser_SuggestInfo
{
	// 数据库接口
	private IDatabaseRef	m_dataBaseRef = null;
	
	@Override
	public void InitDb(IDatabaseRef dataBaseRef)
	{
		m_dataBaseRef = dataBaseRef;	
	}

	@Override
	public boolean InsertSuggestInfo(SendSuggestInfo sendSuggestInfo)
	{
		if (sendSuggestInfo == null)
		{
			return false;
		}
		
		if (!m_dataBaseRef.RunSql(
				"Insert Into SuggestInfo " +
				"([questno],[quest],[flag],[ask_time]) " +
				"values (?,?,?,?)",
				new Object[] {
						sendSuggestInfo.getQuestno(),
						sendSuggestInfo.getQuest(),
						sendSuggestInfo.getFlag(),
						sendSuggestInfo.getAsk_time()
				}
				))
		{
			return false;
		}
		
		return true;
	}

	/**
	 * @brief 【判断是否有没有回答的问题】
	 *
	 *
	 * @n<b>函数名称</b>     :GetIsHasNoAnser
	 * @see
	
	 * @param  @param listSendSuggestInfo
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-11-21下午04:03:54      
	*/
	@Override
    public boolean GetIsHasNoAnser(List<SendSuggestInfo> listSendSuggestInfo) {

        String strsql = "select questno from SuggestInfo where flag = ?";

        Cursor cursor = m_dataBaseRef.RawQuery(strsql, new String[] { "0" });

        if (cursor == null) {
            return false;
        }
        try {
            while (cursor.moveToNext()) {
                SendSuggestInfo itemInfo = new SendSuggestInfo();
                itemInfo.setQuestno(cursor.getString(0));
                listSendSuggestInfo.add(itemInfo);
            }
        } finally {
            cursor.close();
        }
        if (listSendSuggestInfo.size() < 1) {
            return false;
        }
        return true;
    }

	/**
	 * @brief【获取数据库缓存数据】
	 *
	 *
	 * @n<b>函数名称</b>     :GetAllsuggestInfo
	 * @see
	
	 * @param  @param listSendSuggestInfo
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-11-21下午04:35:10      
	*/
	@Override
    public boolean GetAllsuggestInfo(List<SendSuggestInfo> listSendSuggestInfo) {
        String strsql = "select quest, answer, ask_time, answer_time, flag from SuggestInfo order by ask_time desc";

        Cursor cursor = m_dataBaseRef.RawQuery(strsql, null);

        if (cursor == null) {
            return false;
        }
        try {
            while (cursor.moveToNext()) {
                SendSuggestInfo itemInfo = new SendSuggestInfo();
                itemInfo.setQuest(cursor.getString(0));
                itemInfo.setAsk_time(cursor.getString(2));
                int nFlag = cursor.getInt(4);
                itemInfo.setFlag(nFlag);
                if (nFlag == 0) {
                    itemInfo.setAnswer_time("");
                    itemInfo.setAnswer("等待处理");
                } else {
                    itemInfo.setAnswer(cursor.getString(1));
                    itemInfo.setAnswer_time(cursor.getString(3));
                }

                listSendSuggestInfo.add(itemInfo);
            }
        } finally {
            cursor.close();
        }
        if (listSendSuggestInfo.size() < 1) {
            return false;
        }
        return true;
    }

	/**
	 * @brief【更新内容】
	 *
	 *
	 * @n<b>函数名称</b>     :UpdateItemSuggestInfo
	 * @see
	
	 * @param  @param senduggestInfo
	 * @param  @return
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-11-21下午04:39:36      
	*/
	@Override
	public boolean UpdateItemSuggestInfo(SendSuggestInfo senduggestInfo)
	{
		if (senduggestInfo == null)
		{
			return false;
		}
		
		String strsql = "UPDATE SuggestInfo SET flag=?,answer_time=?,answer=? where questno = ?";
		
		if(!m_dataBaseRef.RunSql(strsql, 
				new Object[]{
				senduggestInfo.getFlag(),
				senduggestInfo.getAnswer_time(),
				senduggestInfo.getAnswer(),
				senduggestInfo.getQuestno()
				}))
		{
			return false;
		}			
		
		return true;
	}
	
	
	
    public static boolean createSugessInfo(IDatabaseRef dataBaseRef) {
        String str = "Create TABLE if not exists SuggestInfo("
                + "[IdSuggestInfo] integer PRIMARY KEY ASC AUTOINCREMENT"
                + ",[questno] varchar(50)" + ",[quest] varchar(1000)" + ",[flag] int"
                + ",[ask_time] datetime" + ",[answer_time] datetime" + ",[answer] varchar(1000)"
                + ");";
        if (!dataBaseRef.RunSql(str)) {
            return false;
        }
        return true;
    }
	
	
	
}
