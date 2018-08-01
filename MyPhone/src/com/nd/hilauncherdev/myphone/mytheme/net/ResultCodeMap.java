package com.nd.hilauncherdev.myphone.mytheme.net;


import java.util.HashMap;

public class ResultCodeMap {

	public static final int APP_SUCCESS_CODE = 0;
	public static final int GET_LIST_FAIL_CODE = 9;
		
	private static HashMap<Integer,String> resultCodeMap = new HashMap<Integer,String>();
	
	static {
		resultCodeMap.put(APP_SUCCESS_CODE, "成功");
		resultCodeMap.put(GET_LIST_FAIL_CODE, "失败，未知异常");
	}
	
	public static String getCodeDesc(int resultCode){
		String resultStr = resultCodeMap.get(resultCode);
		if ( resultStr!=null )
			return resultStr;
		return "访问服务发生未知异常!";
	}
}
