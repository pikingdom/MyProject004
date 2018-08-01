package com.calendar.CommData;

import org.json.JSONException;
import org.json.JSONObject;

import com.nd.calendar.util.ComfunHelp;

public class SendSuggestInfo implements ICommData
{
	
    private final String KEY="736datyh378j4f63ws9bd0c8e54278bv";
	// 产品标识
	private String productid;
	// 应用名称
	private String Product;
	// 操作系统名称
	private String OS;
	// 手机系统版本号
	private String Os_version;
	// 应用版本号
	private String App_version;
	// 对方机型
	private String Type;
	// 代表机身号或其他唯一标识
	private String localid;
	// 建议或问题的GUID字符串
	private String questno;
	// 用户反馈的内容
	private String quest;
	//回答内容
	private String answer;
	//提问时间
	private String ask_time;
	//回答时间
	private String answer_time;
	//是否回答
	private int flag;
	//验证码
	private String chkcode;
	//当前日期，格式(yyyy-mm-dd)
	private String cudate;
	
	
	
	public String getProductid()
	{
		return productid;
	}
	public void setProductid(String productid)
	{
		this.productid = productid;
	}
	public String getProduct()
	{
		return Product;
	}
	public void setProduct(String product)
	{
		Product = product;
	}
	public String getOS()
	{
		return OS;
	}
	public void setOS(String oS)
	{
		OS = oS;
	}
	public String getOs_version()
	{
		return Os_version;
	}
	public void setOs_version(String osVersion)
	{
		Os_version = osVersion;
	}
	public String getApp_version()
	{
		return App_version;
	}
	public void setApp_version(String appVersion)
	{
		App_version = appVersion;
	}
	public String getType()
	{
		return Type;
	}
	public void setType(String type)
	{
		Type = type;
	}
	public String getLocalid()
	{
		return localid;
	}
	public void setLocalid(String localid)
	{
		this.localid = localid;
	}
	public String getQuestno()
	{
		return questno;
	}
	public void setQuestno(String questno)
	{
		this.questno = questno;
	}
	public String getQuest()
	{
		return quest;
	}
	public void setQuest(String quest)
	{
		this.quest = quest;
	}
	@Override
	public int GetType()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	public String getAsk_time()
	{
		return ask_time;
	}
	public void setAsk_time(String askTime)
	{
		ask_time = askTime;
	}
	public String getAnswer_time()
	{
		return answer_time;
	}
	public void setAnswer_time(String answerTime)
	{
		answer_time = answerTime;
	}
	public int getFlag()
	{
		return flag;
	}
	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	public String getChkcode() {
        return chkcode;
    }
	public void setChkcode(String chkcode) {
        this.chkcode = chkcode;
    }
	public String getCudate() {
        return cudate;
    }
	public void setCudate(String cudate) {
        this.cudate = cudate;
    }
	
	@Override
	public JSONObject ToJsonObject()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			
			jsonObject.put("productid", productid);
			jsonObject.put("product", Product);
			jsonObject.put("os", OS);
			jsonObject.put("os_version", Os_version);
			jsonObject.put("app_version", App_version);
			jsonObject.put("type", Type);
			jsonObject.put("localid", localid);
			jsonObject.put("questno", questno);
			jsonObject.put("quest", quest);
			jsonObject.put("chkcode", ComfunHelp.md5(productid+'|'+localid+'|'+cudate+'|'+KEY));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;

		}
		return jsonObject;
	}
	@Override
	public void SetJsonString(String strJson)
	{
		// TODO Auto-generated method stub
		
	}
	
}
