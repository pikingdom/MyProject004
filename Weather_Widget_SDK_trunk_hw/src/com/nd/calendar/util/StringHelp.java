package com.nd.calendar.util;

import java.util.Date;

import org.json.JSONObject;

import android.text.TextUtils;

import com.calendar.CommData.DateInfo;

public class StringHelp
{
	//private static String TAG = "StringHelp";
	
    public static JSONObject getJSONObject(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return new JSONObject(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/*static public String getMD5Str(String str)
	{
		MessageDigest messageDigest = null;
		
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			
			messageDigest.reset();
			
			messageDigest.update(str.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		byte[] byteArray = messageDigest.digest();
		
		StringBuffer md5StrBuff = new StringBuffer();
		
		for (int i = 0; i < byteArray.length; i++)
		{
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) md5StrBuff
					.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
			else md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		
		return md5StrBuff.toString();
	}*/
	
	public static String ToDBC(String input) {
		if (input == null)
		{
			return null;
		}
		
		   char[] c = input.toCharArray();
		   for (int i = 0; i< c.length; i++) {
		       if (c[i] == 32) {
		         c[i] = (char) 12288;
		         continue;
		       }

		       if (c[i] < 127)
		          c[i] = (char) (c[i] + 65248);
		       }
		   return new String(c);
		}
	
	
	/**
	 * @brief 【获得图标名】
	 *
	 * @n<b>函数名称</b>     :getUrlFileName
	 * @param sUrl
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-4-6上午11:23:55      
	*/
	public static String getUrlFileName(String sUrl) {
		
		if (sUrl != null && sUrl.length() > 0)
		{
			int ipos = sUrl.lastIndexOf('/');
			if (ipos != -1)
			{
				return sUrl.substring(ipos + 1);
			}
		}
		
		return null;
	}
	
	
	/**
	 * @brief 【计算节气】
	 *
	 * @n<b>函数名称</b>     :getJieQiFormat
	 * @param string
	 * @param dateInfo
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-3-13下午05:18:51      
	*/
    public static String getJieQiFormat(String string, DateInfo dateInfo, boolean bInterval) {
        try {
            String[] strGroup = string.split(" ");

            if (strGroup != null && strGroup.length > 1) {
                String[] str = strGroup[1].split("/");

                if (str != null) {
                    int year = dateInfo.getYear();
                    int month = dateInfo.getMonth();
                    int day = dateInfo.getDay();

                    if (str.length == 3) {
                        year = Integer.valueOf(str[0]);
                        month = Integer.valueOf(str[1]);
                        day = Integer.valueOf(str[2]);
                    } else if (str.length == 2) {
                        month = Integer.valueOf(str[0]);
                        day = Integer.valueOf(str[1]);
                    }

                    if (year == dateInfo.getYear() && month == dateInfo.getMonth()
                            && day == dateInfo.getDay()) {

                        if (strGroup.length > 2) {
                            return String.format("%s 今日 %s", strGroup[0], strGroup[2]);
                        }

                    } else if (bInterval) {
                        Date dateJieQi = new Date(year, month - 1, day);
                        Date dateNow = new Date(dateInfo.getYear(), dateInfo.getMonth() - 1,
                                dateInfo.getDay());

                        long diffTime = Math.abs(dateJieQi.getTime() - dateNow.getTime());
                        int nDay = (int) (diffTime / (24 * 60 * 60 * 1000));

                        return String.format("距 %s 还有%d天", strGroup[0], nDay);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }
    
    public static String url2path (String url, String rootpath){
		if (url==null)
			url = "";
		String rs = rootpath;
		String picname = getPicNameFromUrlWithSuff(url);
		rs = rs+picname;
		rs = renameRes(rs);
		return rs;
	}	
    
    /**
	 * @param path
	 * @return
	 */
	public static String renameRes(String path) {
		if (path == null) {
			return null;
		}
		return path.replace(".png", ".a").replace(".jpg", ".b");
	}
	
	/**
	 * 从图片url中获得图片名
	 * @param url
	 * @return
	 */
	public static String getPicNameFromUrlWithSuff(String url){
		String str = "";
		try {
			if ( url==null || "".equals(url) )
				return "";
			str = url;
			String [] s = str.split("\\/");
			str = s[s.length-1];			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str; 
	}
}
