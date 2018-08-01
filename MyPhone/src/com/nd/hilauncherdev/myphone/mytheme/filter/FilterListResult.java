package com.nd.hilauncherdev.myphone.mytheme.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FilterListResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public List<FilterItem> itemList = new ArrayList<FilterItem>();
}
