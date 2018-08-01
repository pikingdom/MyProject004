
package com.calendar.CommData;

/** 
 * @ClassName: BaseCityInfo 
 * @Description: TODO(城市信息) 
 * @author yanyy
 * @date 2012-4-19 上午09:16:32 
 *
 */
public class BaseCityInfo {
	
	protected int id;

	/* 城市名称 */
    protected String mStrName;

    /* 城市代码 */
    protected String mStrCode;

    /* 省份 */
    protected String mStrProvName;

    /* 顺序 */
    protected int mSort;

    /* 是否由GPS定位出的 */
    protected int mFlag;

	public BaseCityInfo() {
	}
	
	public BaseCityInfo(String name, String code) {
		mStrName = name;
		mStrCode = code;
	}
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
    public void setName(String sName) {
        mStrName = sName;
    }

    public String getName() {
        return mStrName;
    }

    public void setCode(String code) {
        mStrCode = code;
    }

    public String getCode() {
        return mStrCode;
    }

    public void setProvName(String prov) {
        mStrProvName = prov;
    }

    public String getProvName() {
        return mStrProvName;
    }

    public void setSort(int sort) {
        mSort = sort;
    }

    public int getSort() {
        return mSort;
    }

    public void setFromGps(int iFromGps) {
        mFlag = iFromGps;
    }

    public int getFromGps() {
        return mFlag;
    }
}
