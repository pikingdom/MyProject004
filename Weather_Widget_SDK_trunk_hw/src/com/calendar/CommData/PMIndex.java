/**   
*    
* @file 【PM指数】
* @brief
*
* @<b>文件名</b>      : PMIndex
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-5-23 下午02:22:44 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.calendar.CommData;

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.calendar.util.StringHelp;

public class PMIndex implements Serializable
{
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个字段表示什么) 
     */
    private static final long serialVersionUID = 1L;

    public final static int PM_SOURCE_NULL = 0;		// 无源
	public final static int PM_SOURCE_ONLY_US = 1;	// 仅有美使馆
	public final static int PM_SOURCE_ONLY_GOV = 2;	// 仅有环保部
	public final static int PM_SOURCE_ALL = PM_SOURCE_ONLY_US | PM_SOURCE_ONLY_GOV;		// 都有效
	
	public final static String PM_SOURCE_NAME_US = "美使馆";
	public final static String PM_SOURCE_NAME_GOV = "环保部";
	
	private int id;
	private String code;
	private PMIndexInfo USSource;
	private PMIndexInfo GovSource;
	private int pmSourceInfo = PM_SOURCE_NULL;
	
	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public int getSourceInfo() {
		return pmSourceInfo;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public PMIndexInfo getUSSource() {
		return USSource;
	}

	public PMIndexInfo getGovSource() {
		return GovSource;
	}

	public void setUSSource(PMIndexInfo uSSource) {
		USSource = uSSource;
	}

	public void setGovSource(PMIndexInfo govSource) {
		GovSource = govSource;
	}
	
	
	//////////////////////////////////////////////////////////
    public class PMIndexInfo implements Serializable {
		/**
         * @Fields serialVersionUID : TODO(用一句话描述这个字段表示什么) 
         */
        private static final long serialVersionUID = -253354801164167187L;
        private String airGrd;	// 空气质量
		private String hint;	// 空气质量等级
		private String advice;	// 提示
		private String other;	// 空气质量提示
		private String pm25;	// pm2.5
		private String pm10;	// pm10
		private String so2;		// SO2
		private String no2;		// NO2
		private String rank;	// 未用
		private String sourceName;
		private int    airLevel;
		
		public int getAirLevel() {
			return airLevel;
		}
		
		public String getSourceName() {
			return sourceName;
		}
		public void setSourceName(String sourceName) {
			this.sourceName = sourceName;
		}
		
		public String getAirGrd() {
			return airGrd;
		}
		public String getHint() {
			return hint;
		}
		public String getAdvice() {
			return advice;
		}
		public String getOther() {
			return other;
		}
		public String getPM25() {
			return pm25;
		}
		public String getPM10() {
			return pm10;
		}
		public String getSO2() {
			return so2;
		}
		public String getNO2() {
			return no2;
		}
		public String getRank() {
			return rank;
		}
		public void setAirGrd(String airGrd) {
			this.airGrd = airGrd;
		}
		public void setHint(String hint) {
			this.hint = hint;
		}
		public void setAdvice(String advice) {
			this.advice = advice;
		}
		public void setOther(String other) {
			this.other = other;
		}
		public void setPM25(String pm25) {
			this.pm25 = pm25;
		}
		public void setPM10(String pm10) {
			this.pm10 = pm10;
		}
		public void setSO2(String so2) {
			this.so2 = so2;
		}
		public void setNO2(String no2) {
			this.no2 = no2;
		}
		public void setRank(String rank) {
			this.rank = rank;
		}
		
		
		/**
		 * @brief 【返回PM10的等级名称】
		 * @n<b>函数名称</b>     :getPM10Level
		 * @return
		 * @return    String   
		 * @<b>作者</b>          :  陈希
		 * @<b>修改</b>          :
		 * @<b>创建时间</b>      :  2012-5-31下午04:45:56
		 * @<b>修改时间</b>      :
		*/
		public final String getPM10Level(){
			int pmValue = 0;
			try {
				pmValue = Integer.valueOf(pm10);
			} catch (Exception e) {
				return "";
			}
			
			String sPM10Level = "";

			if (pmValue <= 0) {
				
			} else if (pmValue <50) {
		    	sPM10Level = "优";
		    }else if(pmValue <100){
		    	sPM10Level = "良";
		    }else if(pmValue <200){
		    	sPM10Level = "轻";
		    }else if(pmValue <300){
		    	sPM10Level = "中";
		    }else if(pmValue <=500){
		    	sPM10Level = "重";
		    }
		    
		    return sPM10Level;
		}
		
		/**
		 * @brief 【返回PM2.5的等级名称】
		 * @n<b>函数名称</b>     :getPM25Level
		 * @return
		 * @return    String   
		 * @<b>作者</b>          :  陈希
		 * @<b>修改</b>          :
		 * @<b>创建时间</b>      :  2012-5-31下午04:47:53
		 * @<b>修改时间</b>      :
		*/
		public final String getPM25Level() {
			int pmValue = 0;
			try {
				pmValue = Integer.valueOf(pm25);
			} catch (Exception e) {
				return "";
			}
			
			String sPM25Level = "";
			
		    if (pmValue <= 35 && pmValue >0) {
		    	sPM25Level  = "优";
		    }else if(pmValue > 35 && pmValue <= 75){
		    	sPM25Level = "良";
		    }else if(pmValue >75){
		    	sPM25Level = "差";
		    }
		    return sPM25Level;
		}
		
		/**
		 * @brief 【源是否有效】
		 * @n<b>函数名称</b>     :isAvailable
		 * @return
		 * @return    boolean   
		 * @<b>作者</b>          :  陈希
		 * @<b>修改</b>          :
		 * @<b>创建时间</b>      :  2012-6-4下午06:01:45
		 * @<b>修改时间</b>      :
		*/
		public final boolean isAvailable() {
			return 	!TextUtils.isEmpty(airGrd) &&
					!TextUtils.isEmpty(hint);
		}
		
		public boolean setJsonObject(JSONObject jo) {
			try {
				airGrd = getCleanValue(jo.getString("airgrd"));
				hint = getCleanValue(jo.getString("hint"));
				pm25 = getCleanValue(jo.getString("pm25"));
				pm10 = getCleanValue(jo.getString("pm10"));
				so2 = getCleanValue(jo.getString("SO2"));
				no2 = getCleanValue(jo.getString("NO2"));
				advice = jo.getString("advice");
				other = (jo.getString("other"));
				rank = getCleanValue(jo.getString("rank"));
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		public boolean setJsonString(String sJson) {
            try {
                JSONObject jo = StringHelp.getJSONObject(sJson);
                return setJsonObject(jo);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
		}
	}
	
    private static String getCleanValue(String sV) {
    	int i = sV.indexOf('\n');
    	if (i != -1) {
    		sV = sV.substring(0, i);
		}
    	return sV;
    }
    
    ////////////////////////////////////////////////////////////
	/**
	 * @brief 【返回环保部空气质量等级数】
	 * @n<b>函数名称</b>     :getGovAirGrdLevel
	 * @return
	 * @return    int   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-5-31下午07:35:35
	 * @<b>修改时间</b>      :
	*/
	public int getGovAirGrdLevel() {
		// 0-50: 优
		// 50-100: 良
		// 100-150: 轻微污染       
		// 150-200: 轻度污染
		// 200- 250: 中度污染      
		// 250-300: 中度重污染      
		// 300-500: 重污染   
		
		int airIndex = 0;
		try {
			airIndex = Integer.valueOf(GovSource.airGrd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    if (airIndex <50) {
	        return 1;
	    }else if(airIndex <100){
	        return 2;
	    }else if(airIndex <150){
	        return 3;
	    }else if(airIndex <200){
	        return 4;
	    }else if(airIndex <250){
	        return 5;
	    }else if(airIndex <300){
	        return 6;
	    }else if(airIndex <500){
	        return 7;
	    }else {
	        return 7;
	    }
	}
	
	/**
	 * @brief 【返回美使馆空气质量等级数】
	 * @n<b>函数名称</b>     :getUSAirGrdLevel
	 * @return
	 * @return    int   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-5-31下午07:36:13
	 * @<b>修改时间</b>      :
	*/
	int getUSAirGrdLevel() {
	    
		int airIndex = 0;
		try {
			airIndex = Integer.valueOf(USSource.airGrd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (airIndex <50) {
	        return 1;
	    }else if(airIndex <100){
	        return 2;
	    }else if(airIndex <150){
	        return 4;
	    }else if(airIndex <200){
	        return 5;
	    }else if(airIndex <300){
	        return 6;
	    }else if(airIndex <500){
	        return 7;
	    }else {
	        return 7;
	    }
	}
	
	public boolean setJsonString(String sJson) {
		try {
		    JSONObject jo = StringHelp.getJSONObject(sJson);
			JSONObject joUS = jo.getJSONObject("us");
			JSONObject joGov = jo.getJSONObject("gov");
			
			USSource = new PMIndexInfo();
			GovSource = new PMIndexInfo();
			USSource.setSourceName(PM_SOURCE_NAME_US);
			GovSource.setSourceName(PM_SOURCE_NAME_GOV);
			
			boolean bUS = USSource.setJsonObject(joUS);
			boolean bGov = GovSource.setJsonObject(joGov);
			
			if (bUS || bGov) {
				
				if (USSource.isAvailable()) {
					pmSourceInfo = PM_SOURCE_ONLY_US;
					USSource.airLevel = getUSAirGrdLevel();
				}
				
				if (GovSource.isAvailable()) {
					
					if (pmSourceInfo == PM_SOURCE_ONLY_US) {
						pmSourceInfo = PM_SOURCE_ALL;
					} else {
						pmSourceInfo = PM_SOURCE_ONLY_GOV;
					}
					
					GovSource.airLevel = getGovAirGrdLevel();
				}
				
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
