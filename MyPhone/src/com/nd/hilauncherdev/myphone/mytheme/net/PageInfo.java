package com.nd.hilauncherdev.myphone.mytheme.net;


/**
 * 
 * @author cfb
 */
public class PageInfo {

    /**当前页码*/
    public int currentPageNum = 1;

    /**总页数*/
    public int totalPages = 1;

    /**总记录数  搜索结果需要用到*/
    public int totalRecordNums = 0;
    
    public PageInfo() {
    }

	public int getNextPageIndex() {
		if (currentPageNum < totalPages)
			return (++currentPageNum);
		else
			return -1;
	}

	public boolean isLastPage(){
		
		return currentPageNum>=totalPages;
	}
	
	@Override
	public String toString(){
		return "currentPageNum="+currentPageNum+", totalPages="+totalPages+", totalRecordNums="+totalRecordNums;
	}
}
