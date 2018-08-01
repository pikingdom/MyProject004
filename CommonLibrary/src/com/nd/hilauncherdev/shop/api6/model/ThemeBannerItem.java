package com.nd.hilauncherdev.shop.api6.model;

import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;

public class ThemeBannerItem implements ICommonDataItem{

	/**ID(专辑id、主题id、活动id、广告id)*/
	public int bannerId;
	
	/**banner名称*/
	public String bannerName;
	
	/**活动链接（专题和主题走对应接口） 、广告类型对应动作参数*/
	public String bannerLinkUrl;
	
	/**图标链接*/
	public String bannerIconUrl;
	
	/**Banner类型: 1活动 2 专辑 3 主题 4主题web专题  888广告*/
	public int bannerType;
	
	/** Banner显示位置id **/
	public int placeId;

	/**专题活动*/
	public static final int BANNER_FLAG_PLANEVENT = 1; 
	
	/**主题专辑*/
	public static final int BANNER_FLAG_THEME_TAG = 2; 
	
	/**单个主题*/
	public static final int BANNER_FLAG_THEME_SINGLE = 3; 
	
	/**主题专题*/
	public static final int BANNER_FLAG_THEME_TOPIC = 4;
	
	/**广告*/
	public static final int BANNER_FLAG_THEME_AD = 888;
	
	@Override
	public int getPosition() {
		return 0;
	}

	@Override
	public void setPosition(int position) {
	}

	@Override
	public boolean isFolder() {
		return false;
	}
}
