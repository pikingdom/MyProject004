/**   
 *    
 * @file  【customresult数据库操作类】
 * @brief
 *
 * @<b>文件名</b>      : DbYjcInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 下午09:04:38 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;


public class DbCoustomReslt
{
	// 数据库接口
	private IDatabaseRef	m_dataBaseRef;
//	private CalendarInfo	mCalTools;

	/**
	 * @brief【初始化】
	 * 
	 * 
	 * @n<b>函数名称</b> :Init
	 * @see
	 * 
	 * @param @param dataBaseRef
	 * @return void
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-28上午10:04:13
	 */
	//@Override
	public void Init(IDatabaseRef dataBaseRef)
	{
		m_dataBaseRef = dataBaseRef;
//		mCalTools = CalendarInfo.getInstance();
	}
	
	public IDatabaseRef getM_dataBaseRef()
	{
		return m_dataBaseRef;
	}
	
	/**
	 * @brief 【获取宜忌信息解释】
	 *
	 *
	 * @n<b>函数名称</b>     :GetYiAndJiInfo
	 * @see
	
	 * @param  @param date
	 * @param  @param yjcInfo
	 * @param  @return
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2011-10-21上午10:22:43      
	*/
	//@Override
//	public boolean GetYiAndJiInfo(DateInfo date, YjcInfo yjcInfo)
//	{
//	    // 如果有传时间进来就要判断如果是到晚上11点就说明是新的一天
//        DateInfo d = new DateInfo(date);
//        if (d.getHour() >= 23) {
//            d = ComfunHelp.SetDateDeiff(1, d);
//            d.setHour(0);
//        }
//	        
//		CalendarInfo calInfo = CalendarInfo.getInstance();
//		String sMonth = calInfo.GetLlGZMonthGl(date).substring(1);
//		
//		Cursor cursor = m_dataBaseRef
//		.RawQuery("select appropriate,avoid from advices as a inner "
//				+ "join advice as b on a.Code = b.Code where ( month = ? and dayGZ = ?);",
//						new String[] {sMonth, calInfo.GetLlGZDay(d)});
//
//		if(cursor == null) {
//			return false;
//		}
//		
//		if (cursor.moveToNext())
//		{
//			//宜
//			yjcInfo.setStrYi(mCalTools.decode(cursor.getString(0)));
//			//忌
//			yjcInfo.setStrJi(mCalTools.decode(cursor.getString(1)));
//		}
//		
//		cursor.close();
//		return true;
//		
//	}
	
//	/**
//	 * @brief【获取宜忌常用数据】
//	 *
//	 *
//	 * @n<b>函数名称</b>     :GetConstantYuJiData
//	 * @see
//	
//	 * @param  @param vectString
//	 * @param  @return
//	 * @<b>作者</b>          :  邱堃
//	 * @<b>创建时间</b>      :  2011-6-24上午09:56:09      
//	*/
//	//@Override
//	public int GetConstantYuJiData(Vector<String> vectString)
//	{
//		Cursor cursor = m_dataBaseRef.RawQuery("select sDetailModernName from huangdaojiri order by  iCommonUse desc;", null);
//		while (cursor.moveToNext())
//		{
//			vectString.add(cursor.getString(0));
//		}
//		int nCount = cursor.getCount();
//		if(cursor != null)
//		{
//			cursor.close();
//		}
//		return nCount;
//	}
//	
//	//@Override
//	public boolean IsOldYujiName(String strCon , StringBuffer strOut)
//	{
//		Cursor cursor = m_dataBaseRef
//		.RawQuery(String.format("select sDetailModernName ,sOrigName from huangdaojiri where sOrigName = '%s'", strCon), null);
//		while (cursor.moveToNext())
//		{
//			strOut.append(cursor.getString(0));
//			break;
//		}
//		if(cursor.getCount()<1)
//		{
//			if(cursor != null)
//			{
//				cursor.close();
//			}
//			return false;
//		}
//		if(cursor != null)
//		{
//			cursor.close();
//		}
//		return true;
//	}
//	
//
//	/**
//	 * @brief【根据判断是否存该字符串】
//	 *
//	 *
//	 * @n<b>函数名称</b>     :IsHasYiAndJiResult
//	 * @see
//	
//	 * @param  @param date
//	 * @param  @param yjcInfo
//	 * @param  @param strCon
//	 * @param  @param bFlag
//	 * @param  @return
//	 * @<b>作者</b>          :  邱堃
//	 * @<b>创建时间</b>      :  2011-6-23下午05:35:43      
//	*/
//	//@Override
//	public boolean IsHasYiAndJiResult(DateInfo date, YjcInfo yjcInfo,String strCon,
//			boolean bFlag)
//	{
//
//		CalendarInfo calInfo = CalendarInfo.getInstance();
//		
//		String strMonth = calInfo.GetLlGZMonthGl(calInfo.Lunar(date)).substring(1);
//		String strDayGz = calInfo.GetLlGZDay(date);
//		String stradvices = "%"+strCon+"%";
//		
//		String strSql;
//		if(bFlag)
//		{
//			strSql = String.format("select appropriate,avoid ,favonian, terrible,fetus from advices as a inner "
//				+ "join advice as b on a.Code = b.Code where ( month = '%s' and dayGZ = '%s' and appropriate like '%s');",strMonth,strDayGz, stradvices);
//		}
//		else
//		{
//			strSql = String.format("select appropriate,avoid ,favonian, terrible,fetus from advices as a inner "
//				+ "join advice as b on a.Code = b.Code where ( month = '%s' and dayGZ = '%s' and avoid like '%s');",strMonth,strDayGz, stradvices);
//		}
//
//		
//		Cursor cursor = m_dataBaseRef.RawQuery(strSql, null);
//		while (cursor.moveToNext())
//		{
//			if(bFlag)
//			{
//				// 宜 
//				String strTemp = cursor.getString(0);
//				if (strTemp != null)
//				{
//					strTemp = strTemp.replace(strCon, "<font color=\"#5b9e13\">"+strCon+"</font>");
//					yjcInfo.setStrYi(strTemp);					
//				}
//			
//				yjcInfo.setStrJi(cursor.getString(1));
//			}
//			else
//			{
//				yjcInfo.setStrYi(cursor.getString(0));
//				// 忌
//				String strTemp = cursor.getString(1);
//				if (strTemp != null)
//				{
//					strTemp = strTemp.replace(strCon, "<font color=\"#5b9e13\">"+strCon+"</font>");
//					yjcInfo.setStrJi(strTemp);					
//				}
//			}
//			// 吉神
//			yjcInfo.setStrJiSheng(cursor.getString(2));
//			// 凶神
//			yjcInfo.setStrXiongSheng(cursor.getString(3));
//			// 胎神
//			yjcInfo.setStrTaiSheng(cursor.getString(4));
//			break;
//		}
//		if(cursor.getCount()>0)
//		{
//			if(cursor != null)
//			{
//				cursor.close();
//			}
//			return true;
//		}
//		if(cursor != null)
//		{
//			cursor.close();
//		}
//		return false;
//	}
//	
//	/**
//	 * @brief 【获取黄历名词解释】
//	 *
//	 *
//	 * @n<b>函数名称</b>     :GetHuangLiExplanation
//	 * @see
//	
//	 * @param  @param iType
//	 * @param  @param vecNouns
//	 * @param  @param explanList
//	 * @param  @param userNouns  自定义名词
//	 * @param  @return
//	 * @<b>作者</b>          :  陈希
//	 * @<b>创建时间</b>      :  2011-10-25下午06:41:01      
//	*/
//	//@Override
//	public boolean GetHuangLiExplanation(int iType, String[] vecNouns, String[] userNouns, Vector<HuangLiExplainInfo> explanList)
//	{
//		if (iType <0 || iType > MAX_EXPLAIN_INDEX || vecNouns == null
//				|| explanList == null || vecNouns.length < 1
//				|| userNouns == null || userNouns.length < vecNouns.length)
//		{
//			return false;
//		}
//		
//		switch (iType)
//		{
//		case YI_JI_EXPLAIN:
//			{
//				m_dataBaseRef.beginTransaction();
//	
//				for (int i=0;i<vecNouns.length; i++)
//				{
//					String sLike = "%" + vecNouns[i] + "%";
//					Cursor cursor = m_dataBaseRef.RawQuery(String.format("select sDetailModernName," +
//							"sOrigName,sDescribe from huangdaojiri where sDetailModernName like '%s'"
//							//+ " or sOrigName like '%s';"
//							,sLike, sLike));
//					
//					if (cursor != null)
//					{
//						if (cursor.moveToNext()) {
//							explanList.add(new HuangLiExplainInfo(cursor.getString(0), cursor.getString(1),
//									cursor.getString(2), userNouns[i]));							
//						}
//
//						cursor.close();
//					}
//				}
//				
//				m_dataBaseRef.endTransaction();
//				
//				break;
//			}
//		case PRO_NOUN_EXPLAIN:
//		case XIONG_SHEN_EXPLAIN:
//		case JI_SHEN_EXPLAIN:
//		case CHONG_EXPLAIN:
//		case PENG_ZU_EXPLAIN:
//		case SHENG_XIAO_EXPLAIN:
//		case SOLAR_TERMS_EXPLAIN:
//		case TAI_SHEN_EXPLAIN:
//		case ZHU_SHEN_EXPLAIN:
//		{
//			String strSQL = "select sDescribe from " + EXP_TABLE[iType] + " where name='%s';";
//		
//			m_dataBaseRef.beginTransaction();
//			
//			for (int i=0; i< vecNouns.length; i++)
//			{
//				Cursor cursor = m_dataBaseRef.RawQuery(String.format(strSQL, vecNouns[i]));
//				
//				if (cursor != null)
//				{
//					if (cursor.moveToNext()) {
//						explanList.add(new HuangLiExplainInfo(null, null, cursor.getString(0), userNouns[i]));
//					}
//					cursor.close();
//				}
//			}
//			
//			m_dataBaseRef.endTransaction();
//		}
//			break;
//		case JIE_RI_EXPLAIN:
//		{
//			String strSQL = "select sDescribe from " + EXP_TABLE[JIE_RI_EXPLAIN] + " where name='%s'" +
//					" union select sDescribe from "+ EXP_TABLE[SOLAR_TERMS_EXPLAIN] + " where name='%s'";
//			
//			m_dataBaseRef.beginTransaction();
//			ArrayList<String> keyLst = new ArrayList<String>();
//            for (int i = 0; i < vecNouns.length; i++) {
//                String sKey = vecNouns[i].trim();
//                // 有可能出现重复，过滤重复的
//                if (!keyLst.contains(sKey)) {
//                    Cursor cursor = m_dataBaseRef.RawQuery(String.format(strSQL, sKey,
//                            sKey));
//
//                    if (cursor != null) {
//                        if (cursor.moveToNext()) {
//                            explanList.add(new HuangLiExplainInfo(null, null, cursor.getString(0),
//                                    userNouns[i]));
//                        } else {
//                            // 仿真读取广告解释
//                            HuangLiExplainInfo explainInfo = CalendarModule.getADExplian(sKey);
//                            if (explainInfo != null) {
//                                explanList.add(explainInfo);
//                            }
//                        }
//
//                        cursor.close();
//                    }
//                    keyLst.add(sKey);
//                }
//            }
//            keyLst.clear();
//			m_dataBaseRef.endTransaction();
//		}
//			break;
//		default:
//			break;
//		}
//			
//		return true;
//	}
//	
//
//	/**
//	 * @brief 【检查解释数据】
//	 * @n<b>函数名称</b>     :checkData
//	 * @return
//	 * @return    String   
//	 * @<b>作者</b>          :  陈希
//	 * @<b>修改</b>          :
//	 * @<b>创建时间</b>      :  2012-5-14下午03:31:09
//	 * @<b>修改时间</b>      :
//	*/
//	public String checkData() {
//	
//		String strSql = String.format("select favonian,terrible,appropriate,avoid from advices;");
//		Cursor cursor = m_dataBaseRef.RawQuery(strSql);
//		
//		HashSet<String> favonian = new HashSet<String>();		// 吉神
//		HashSet<String> terrible = new HashSet<String>();		// 凶神
//		HashSet<String> yiji = new HashSet<String>();			// 宜 忌
//				
//		if (cursor != null) {
//			// 1.获取所有关键字
//			while (cursor.moveToNext()) {
//				// 吉神
//				String sText = cursor.getString(0);
//				if (!TextUtils.isEmpty(sText)) {
//					String [] su = sText.split(" ");
//					for (String sr : su) {
//						favonian.add(sr);
//					}
//				}
//				
//				sText = cursor.getString(1);
//				if (!TextUtils.isEmpty(sText)) {
//					String [] su = sText.split(" ");
//					for (String sr : su) {
//						terrible.add(sr);
//					}
//				}				
//				
//				sText = cursor.getString(2);
//				if (!TextUtils.isEmpty(sText)) {
//					String [] su = sText.split(" ");
//					for (String sr : su) {
//						yiji.add(sr);
//					}
//				}
//				
//				sText = cursor.getString(3);
//				if (!TextUtils.isEmpty(sText)) {
//					String [] su = sText.split(" ");
//					for (String sr : su) {
//						yiji.add(sr);
//					}
//				}	
//			}
//			
//			cursor.close();
//			
//			// 2.搜索解释
//			fd(favonian, "JiShenExp", "name");
//			fd(terrible, "XiongShenExp", "name");
//			fd(yiji, "HuangDaoJiRi", "sDetailModernName");
//		}	
//		
//		return null;
//	}
//
//	void fd(HashSet<String> setObj, String sTable, String sName) {
//		if (setObj.size() > 0) {
//			StringBuilder sB = new StringBuilder();
//			sB.append("select ").append(sName).append(" from ")
//			.append(sTable);
//			
//			Cursor cursor = m_dataBaseRef.RawQuery(sB.toString());
//			if (cursor != null) {
//				sB = new StringBuilder(sTable);
//				sB.append(": ");
//				
//				ArrayList<String> sExps = new ArrayList<String>();
//				while (cursor.moveToNext()) {
//					sExps.add(cursor.getString(0)) ;
//				}
//				cursor.close();
//				
//				for (String sKey : setObj) {
//					if (!sExps.contains(sKey)) {
//						sB.append(sKey).append(",");
//					}
//				}
//				
//				Log.d("exps", sB.toString());
//			}
//		}
//	}

}
