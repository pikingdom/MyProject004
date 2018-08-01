package com.nd.hilauncherdev.myphone.mytheme.net;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author cfb
 *
 * @param <T>
 */
public class ServerResult<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	/**无法访问到服务端的数据*/
	public boolean bNetworkProblem = false;
	
	/**服务端的响应编码*/
	public int resultCode;  
	
	/** 分页信息*/
	public PageInfo pageInfo = new PageInfo();
	
	/**返回结果集*/
	public ArrayList<T> itemList = new ArrayList<T>();
	
	public boolean atLastPage = false;
}
