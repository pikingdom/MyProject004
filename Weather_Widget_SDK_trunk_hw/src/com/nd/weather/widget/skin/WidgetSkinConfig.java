/**   
 *    
 * @file 【读取配置文件，同时填充绘制信息】
 * @brief
 *
 * @<b>文件名</b>      : WidgetSkinConfig
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>修  改</b>  : 陈希
 * @n@n<b>创建时间</b>: 2011-9-26 下午04:26:27 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget.skin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.nd.calendar.util.ComfunHelp;

public class WidgetSkinConfig
{
	// 引擎版本号
	public static final int SKIN_ENGINE_VERSION = 2;

	public interface Skin {
		
		public static final String ROOT_NAME = "Skins";
		
		public static final String SUPPORT_RESOLUTION = "SupportResolution";
		
		/**
		 * Tag 名称
		 */
		public static final String NODE_NAME = "Skin";
		
		/**
		 * 使用官方数字图标
		 */
		public static final String USE_ORG_NUMBER_ICON = "useOrgNumberIcon";
		
		/**
		 * 使用官方背景图标
		 */
		public static final String USE_ORG_BACKGROUND = "useOrgBackground";
		
		/**
		 * 使用官方天气图标
		 */
		public static final String USE_ORG_WEATHER_ICON = "useOrgWeahterIcon";
		
		/**
		 * 天气图标版本
		 */
		public static final String WEATHER_ICON_VER = "WeahterIconVer";
		
		/**
		 * 天气图标版本
		 */
		public static final String NUMBER_ICON_VER = "NumberIconVer";
		
		/**
		 * widget类型
		 */
		public static final String WIDGET_TYPE = "WidgetType";
		
		/**
		 * widget方向
		 */
		public static final String ORIENTATION = "Orientation";
		
		/**
		 * 分辨率
		 */
		public static final String RESOLUTION = "Resolution";
		
		/**
		 * 热区
		 */		
		public static final String HOT_AREA = "HotArea";
		
		/**
		 * 文本
		 * @author 章军飞
		 * date 2011-1-24
		 */
		public interface SkinText {
			/**
			 * Tag 名称
			 */
			public static final String NODE_NAME = "SkinText";
			
			/**
			 * 应用标识
			 */
			public static final String TEXTKEY = "TextKey";

			public static final String TEXTKEY_VER = "TextKey_";

			/**
			 * x坐标
			 */
			public static final String X = "X";
			
			/**
			 * y坐标
			 */
			public static final String Y = "Y";
			
			/**
			 * 字体大小
			 */
			public static final String SIZE = "Size";
			
			/**
			 * 字体
			 */
			public static final String FONT = "Font";
			
			/**
			 * 颜色
			 */
			public static final String COLOR = "Color";
			
			/**
			 * 对齐方向
			 */
			public static final String ALIGN = "Align";
			
			/**
			 * 使用阴影
			 */
			public static final String USE_SHADOW = "UseShadow";
			
			/**
			 * 阴影大小
			 */
			public static final String SHADOW_BLUR_SIZE = "ShadowBlurSize";
			
			/**
			 * 阴影颜色
			 */
			public static final String SHADOW_COLOR = "ShadowColor";
			
			/**
			 * 阴影X偏移量
			 */
			public static final String SHADOW_OFFSET_X = "ShadowOffsetX";
			
			/**
			 * 阴影Y偏移量
			 */
			public static final String SHADOW_OFFSET_Y = "ShadowOffsetY";
			
			/**
			 * 类别
			 */
			public static final String TYPE = "Type";
			
			/**
			 * 文字
			 */
			public static final String MAX_LENGTH = "Lenth";
		}
		
		/**
		 * 图片
		 * @author 章军飞
		 * date 2011-1-24
		 */
		public interface SkinDraw {
			
			/**
			 * Tag 名称
			 */
			public static final String NODE_NAME = "SkinDraw";
			
			/**
			 * 文件名称
			 */
			public static final String FILENAME = "FileName";
			
			/**
			 * 图片标识
			 */
			public static final String TYPE = "Type";
			
			/**
			 * x坐标
			 */
			public static final String X = "X";
			
			/**
			 * y坐标
			 */
			public static final String Y = "Y";
			
			/**
			 * 宽度
			 */
			public static final String WIDTH = "Width";
			
			/**
			 * 高度
			 */
			public static final String HEIGHT = "Height";
			
			/**
			 * 是否需要缩放
			 */
			public static final String NEED_SCALE = "NeedScale";
		}
	}
	
	public interface SkinHotArea {
		public static final int HOT_AREA_NULL = 0;
		public static final int HOT_AREA_CITY = 1;
		public static final int HOT_AREA_WEATHER = 2;
		public static final int HOT_AREA_TIME = 3;
		public static final int HOT_AREA_DATE = 4;
	}
	
	public static final String DEFAULT_RESOLUTION = "320*480";
	
	private List<DrawTextInfo> mListTextItemInfo = null;
	private List<DrawImageInfo> mListDrawImageInfo = null;
	private List<DrawInfoBase> mListDraw = null;
	
	private int mHeight;
	private int mWidth;
	private boolean mbOutOfResolution = false;
	private boolean mUseOrgNumberIcon = false;
	private boolean mUseOrgWeahterIcon = false;
	private boolean mUseOrgBackgroud = false;
	public int mWeahterIconVer = 0;	// 使用的天气图标版本
	public int mNumberIconVer = 0;
	
	DrawImageInfo mDrawPictureBkInfo = null;

	public List<DrawTextInfo> getDrawTextInfoList() {
		return mListTextItemInfo;
	}

	public List<DrawImageInfo> getDrawImageInfoList() {
		return mListDrawImageInfo;
	}

	public int getWidgetHeight() {
		return mHeight;
	}

	public int getWidgetWidth() {
		return mWidth;
	}

	public void setCustomWidgetSize(int width, int height) {
		mHeight = height;
		mWidth = width;
	}
	
	public void setOutOfResolution() {
		mbOutOfResolution = true;
	}
	
	public boolean isUseOrgNumberIcon() {
		return mUseOrgNumberIcon;
	}

	public boolean isUseOrgWeahterIcon() {
		return mUseOrgWeahterIcon;
	}

	public boolean isUseOrgBackgroud() {
		return mUseOrgBackgroud;
	}
	
	public int getHotElementFromPos(int x, int y) {
		Log.i("WidgetClick", "x="+x+",y="+y);
		/* 优先级:
		 * 1.城市 
		 * 		CITY_NAME_QUOTE
		 * 2.日期 
		 * 		ND_DATE_YEAR、ND_DATE_MONTH、ND_DATE_DAY、ND_DATE_NL_QUOTE、
		 * 		ND_WEEK_QUOTE、ND_DATE_YEAR_NL、ND_DATE_SHORT_TYPE_1_QUOTE、
		 * 		ND_DATE_SHORT_TYPE_2_QUOTE、ND_DATE_SHORT_TYPE_3_QUOTE
		 * 3.时间
		 * 		ND_TIME_HOUR_POSITION_1_ID、ND_TIME_HOUR_POSITION_2_ID、
		 * 		ND_TIME_MINUTE_POSITION_1_ID、ND_TIME_MINUTE_POSITION_2_ID、
		 * 		ND_TIME_COLON_ID
		 * 4.天气图标
		 * 		ND_WEATHER_ICON_CURRENT_ID、ND_WEATHER_ICON_FORECAST_DAY_1_ID、
		 * 		ND_WEATHER_ICON_FORECAST_DAY_2_ID、ND_WEATHER_ICON_FORECAST_DAY_3_ID、
		 * 		ND_WEATHER_ICON_FORECAST_DAY_4_ID、ND_WEATHER_ICON_FORECAST_DAY_5_ID
		 */
		
		if (mListDraw == null) {
			return SkinHotArea.HOT_AREA_NULL;
		}
		
		boolean [] hotIndex = new boolean[3];
		
		for (DrawInfoBase drawItem : mListDraw) {
			if (drawItem.rectHot != null && drawItem.rectHot.contains(x, y)) {
				if (drawItem.emtType == SkinHotArea.HOT_AREA_CITY) {
					// 城市等级最高，遇到直接返回
					return SkinHotArea.HOT_AREA_CITY;
					
				} else if (drawItem.emtType == SkinHotArea.HOT_AREA_DATE) {
					hotIndex[0] = true;
				} else if (drawItem.emtType == SkinHotArea.HOT_AREA_TIME) {
					hotIndex[1] = true;
				} else if (drawItem.emtType == SkinHotArea.HOT_AREA_WEATHER) {
					hotIndex[2] = true;
				}
			}
		}
	
		// 按优先级返回
		if (hotIndex[0]) {
			return SkinHotArea.HOT_AREA_DATE;
		} else if (hotIndex[1]) {
			return SkinHotArea.HOT_AREA_TIME;
		} else if (hotIndex[2]) {
			return SkinHotArea.HOT_AREA_WEATHER;
		}
		
		return SkinHotArea.HOT_AREA_NULL;
	}
	
	/**
	 * @brief 【读取配置文件】
	 *
	 * @n<b>函数名称</b>     :readXML
	 * @param inStream
	 * @param sResolution
	 * @param strWidgetType
	 * @return    void   
	 * @<b>修改</b>          :  陈希
	 * @<b>修改时间</b>      :  2012-4-18上午11:35:41      
	*/
	public boolean readXML(InputStream inStream, String sResolution, String sWidgetType) {
		Log.d("widget", "sResolution: " + sResolution);
		mListTextItemInfo = null;
		mListDrawImageInfo = null;

		mbOutOfResolution = false;
		mUseOrgNumberIcon = false;
		mUseOrgWeahterIcon = false;
		mUseOrgBackgroud = false;
		mDrawPictureBkInfo = null;


		if (mListTextItemInfo == null) {
			mListTextItemInfo = new ArrayList<DrawTextInfo>();
		}
		if (mListDrawImageInfo == null) {
			mListDrawImageInfo = new ArrayList<DrawImageInfo>();
		}
		if (mListDraw == null) {
			mListDraw = new ArrayList<DrawInfoBase>();
		}
		
		String sFixResolution = getCanvaSize(sResolution, sWidgetType, false);
		
		return parserWidgetSkin(inStream, sWidgetType, sFixResolution, sResolution);
	}

	/**
	 * @brief 【获得画布尺寸】
	 * @n<b>函数名称</b>     :getCanvaSize
	 * @param sResolution
	 * @param sWidgetType
	 * @param bAllResolution
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :  蔡志鹏
	 * @<b>创建时间</b>      :  2013-8-21下午6:33:33
	 * @<b>修改时间</b>      :  2014-10-10下午4:56:10
	 * @<b>修改内容</b>      :  插件图片修改为按屏幕宽比例生成
	 * 
	*/
	String getCanvaSize(String sResolution, String sWidgetType, boolean bAllResolution) {
		String sFixResolution = sResolution;
		
		mWidth = 0;
		mHeight = 0;
		mbOutOfResolution = false;
		
		// 配对分辨率
		if (sWidgetType.equalsIgnoreCase("4x1")) {
			if ("240*320".equals(sResolution)) {
				mWidth = 240;
				mHeight = 75;
			} else if ("320*480".equals(sResolution)) {
				mWidth = 320;
				mHeight = 100;
			} else if ("480*800".equals(sResolution)) {
				mWidth = 480;
				mHeight = 147;
			} else if ("480*854".equals(sResolution)) {
				mWidth = 480;
				mHeight = 150;
			} else if ("540*960".equals(sResolution)) {
				mWidth = 540;
				mHeight = 135;
			} else if ("640*960".equals(sResolution)) {
				mWidth = 640;
				mHeight = 170;
			} else {
				if (bAllResolution) {
					if ("240*400".equals(sResolution)) {
						mWidth = 240;
						mHeight = 80;
					} else if ("800*1280".equals(sResolution)) {
						mWidth = 800;
						mHeight = 230;
					} else if ("600*1024".equals(sResolution)) {
						mWidth = 600;
						mHeight = 150;
					} else if ("1080*1776".equals(sResolution)) {
						mWidth = 1080;
						mHeight = 271;
					} else if ("1080*1794".equals(sResolution)) {
						mWidth = 1080;
						mHeight = 271;
					} else if (sResolution.startsWith("480*")) {
						mWidth = 480;
						mHeight = 114;
					} else if (sResolution.startsWith("1080*")) {
						mWidth = 1080;
						mHeight = 274;
					} else if (sResolution.startsWith("540*")) {
						mWidth = 540;
						mHeight = 135;
					} else if (sResolution.startsWith("720*")) {
						mWidth = 720;
						mHeight = 180;
					} else if (sResolution.startsWith("1200*")) {
						mWidth = 1200;
						mHeight = 270;
					} else if (sResolution.startsWith("1440*")) {
						mWidth = 1440;
						mHeight = 360;
					}
				}
				
				if (mWidth == 0) {
					mWidth = ComfunHelp.convertSize(320);
					mHeight = ComfunHelp.convertSize(100);
					Log.d("skin", "default: " + mWidth + ", " + mHeight);
					mbOutOfResolution = true;
					sFixResolution = DEFAULT_RESOLUTION;
				}
			}
		} else {
			
			if ("240*320".equals(sResolution)) {
				mWidth = 240;
				mHeight = 133;
			} else if ("320*480".equals(sResolution)) {
				mWidth = 320;
				mHeight = 198;
			} else if ("480*800".equals(sResolution)) {
				mWidth = 480;
				mHeight = 294;
			} else if ("480*854".equals(sResolution)) {
				mWidth = 480;
				mHeight = 322;
			} else if ("540*960".equals(sResolution)) {
				mWidth = 540;
				mHeight = 337;
			} else if ("640*960".equals(sResolution)) {
				mWidth = 640;
				mHeight = 370;
			} 
			else {
				if ("240*400".equals(sResolution)) {
					mWidth = 240;
					mHeight = 160;
				} else if ("800*1280".equals(sResolution)) {
					mWidth = 800;
					mHeight = 420;
				} else if ("720*1280".equals(sResolution)) {
					mWidth = 720;
					mHeight = 400;
				} else if ("720*1184".equals(sResolution)) {
					mWidth = 720;
					mHeight = 400;
				} else if ("600*1024".equals(sResolution)) {
					mWidth = 600;
					mHeight = 320;
				} else if ("1080*1776".equals(sResolution)) {
					mWidth = 1080;
					mHeight = 600;
				}
				
				if (mWidth == 0) {
					mWidth = ComfunHelp.convertSize(320);
					mHeight = ComfunHelp.convertSize(198);
					Log.d("skin", "default: " + mWidth + ", " + mHeight);
					mbOutOfResolution = true;
					sFixResolution = DEFAULT_RESOLUTION;
				}
			}
		}
		
		return sFixResolution;
	}
	
	/**
	 * @brief 【解析对应皮肤】
	 * @n<b>函数名称</b>     :parserWidgetSkin
	 * @param inStream
	 * @param sWidgetType
	 * @param sResolution
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  
	 * @<b>修改</b>          : 陈希
	 * @<b>创建时间</b>      :  2012-7-23下午04:09:08
	 * @<b>修改时间</b>      :
	*/
	final boolean parserWidgetSkin(InputStream inStream, String sWidgetType, String sResolution, String sOrgResolution) {
		boolean bMatched = false;
		
		mListTextItemInfo.clear();
		mListDrawImageInfo.clear();
		mListDraw.clear();
		mWeahterIconVer = 0;
		mNumberIconVer = 0;
		
		try {
			XmlPullParser parser = Xml.newPullParser();
			
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
		
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if (eventType == XmlPullParser.START_TAG) {		// 开始元素事件
					String name = parser.getName();

					if (name.equalsIgnoreCase(Skin.NODE_NAME)) {
						String sSkinResolution = parser.getAttributeValue(null, Skin.RESOLUTION);
						String sSkinWidgetType = parser.getAttributeValue(null, Skin.WIDGET_TYPE);

						if (sSkinWidgetType.equalsIgnoreCase(sWidgetType) && 
								(sSkinResolution.equals(sResolution) || (sSkinResolution.startsWith("1080*") && (sResolution.startsWith("1080*")))
										|| (sSkinResolution.startsWith("480*") && (sResolution.startsWith("480*")))
										 || (sSkinResolution.startsWith("540*") && (sResolution.startsWith("540*")))
												 || (sSkinResolution.startsWith("720*") && (sResolution.startsWith("720*")))
										|| (sSkinResolution.startsWith("1440*") && (sResolution.startsWith("1440*")))
														 || (sSkinResolution.startsWith("1200*") && (sResolution.startsWith("1200*"))))) {
							Log.e("skinTest", "sSkinResolution="+sSkinResolution+",sResolution="+sResolution);
							bMatched = true;
							mListTextItemInfo.clear();
							mListDrawImageInfo.clear();
							mListDraw.clear();
							
							mUseOrgNumberIcon = Boolean.parseBoolean(parser.getAttributeValue(null, Skin.USE_ORG_NUMBER_ICON));
							mUseOrgWeahterIcon = Boolean.parseBoolean(parser.getAttributeValue(null, Skin.USE_ORG_WEATHER_ICON));
							mUseOrgBackgroud = Boolean.parseBoolean(parser.getAttributeValue(null, Skin.USE_ORG_BACKGROUND));
						}
						
					} else if (bMatched) {
						
						// 根据属性绘制文本
						if (name.equalsIgnoreCase(Skin.SkinText.NODE_NAME)) {
							DrawTextInfo drawText = new DrawTextInfo();
							int attrCount = parser.getAttributeCount();
							int maxVer = 0;
							String sTextKey = null;
							
							for (int i = 0; i < attrCount; i++) {
								String sName = parser.getAttributeName(i);
								if (sName == null) {
									continue;
								}
								
								int pos = sName.indexOf(Skin.SkinText.TEXTKEY_VER);
								if (pos == -1) {
									continue;
								}
								
								int ver = 0;
								try {
									ver = Integer.parseInt(sName.substring(Skin.SkinText.TEXTKEY_VER.length()));
								} catch (Exception e) {
								}
								
								if (ver > maxVer && ver <= SKIN_ENGINE_VERSION) {
									maxVer = ver;
									sTextKey = parser.getAttributeValue(i);
								}
							}
							
							if (sTextKey == null) {
								sTextKey = parser.getAttributeValue(null, Skin.SkinText.TEXTKEY);
							}
							
							drawText.TextKey = sTextKey;
							drawText.Type = parser.getAttributeValue(null, Skin.SkinText.TYPE);
							
							// 字体位置
							drawText.X = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinText.X));
							drawText.Y = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinText.Y));
							
							drawText.Size = parseInt(parser.getAttributeValue(null, Skin.SkinText.SIZE));
							drawText.Color = parseInt(parser.getAttributeValue(null, Skin.SkinText.COLOR), 16);
							drawText.Align = parser.getAttributeValue(null, Skin.SkinText.ALIGN);	
							drawText.UseShadow = Boolean.parseBoolean(parser.getAttributeValue(null,
														Skin.SkinText.USE_SHADOW));
							
							// 超出模板的使用默认
							if (mbOutOfResolution) {
								drawText.X = ComfunHelp.convertSize(drawText.X);
								drawText.Y = ComfunHelp.convertSize(drawText.Y);
								drawText.Size = ComfunHelp.convertSize(drawText.Size);
							}
							
							String strLenth = parser.getAttributeValue(null, "Lenth");
							int nLenth = 0;
							if (!TextUtils.isEmpty(strLenth)) {
								nLenth = Integer.parseInt(strLenth);
								if (nLenth < 0) {
									nLenth = 0;
								}
							}

							drawText.nLenth = nLenth;
							drawText.ShadowColor = parseInt(parser.getAttributeValue(null, Skin.SkinText.SHADOW_COLOR), 16);
							drawText.ShadowBlurSize = parseInt(parser.getAttributeValue(null, Skin.SkinText.SHADOW_BLUR_SIZE));
							drawText.ShadowOffsetX = parseInt(parser.getAttributeValue(null, Skin.SkinText.SHADOW_OFFSET_X));
							drawText.ShadowOffsetY = parseInt(parser.getAttributeValue(null, Skin.SkinText.SHADOW_OFFSET_Y));
							drawText.Type = parser.getAttributeValue(null, Skin.SkinText.TYPE);
							
							mListTextItemInfo.add(drawText);
							
							String sHotArea = parser.getAttributeValue(null, Skin.HOT_AREA);
							
							// 热区
							if (!TextUtils.isEmpty(drawText.TextKey)) {
								if (drawText.TextKey.contains(WidgetRuleId.CITY_NAME_QUOTE)
									|| WidgetRuleId.ND_HOT_CITY_ID.equals(sHotArea)) {
									
									drawText.emtType = SkinHotArea.HOT_AREA_CITY;
									mListDraw.add(drawText);
									
								}// 天气插件点日期也进入天气详情 2016-5-24 V7.5.2改动 定制版加回来2017-02-24 caizp
								else if (drawText.TextKey.contains(WidgetRuleId.ND_DATE_YEAR) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_MONTH) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_DAY) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_NL_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEEK_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_1_QUOTE) || 
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_2_QUOTE) || 
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_3_QUOTE)) {
									
									drawText.emtType = SkinHotArea.HOT_AREA_DATE;
									mListDraw.add(drawText);
								} else if (drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_TEMP_NOW_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_TEMP_DESP_NOW_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_DESP_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TEMP_LOWEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TEMP_HEIGHEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_DESP_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TEMP_LOWEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TEMP_HEIGHEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_DESP_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TEMP_LOWEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TEMP_HEIGHEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_DESP_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TEMP_LOWEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TEMP_HEIGHEST_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEATHER_FORECAST_DATE_5_DESP_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.W_FD5_NIGHT_TEMP) ||
										drawText.TextKey.contains(WidgetRuleId.W_FD5_DAY_TEMP) ||
										drawText.TextKey.contains(WidgetRuleId.W_DAY_TEMP) || 
										drawText.TextKey.contains(WidgetRuleId.W_CD_YI_TEXT) ||
										drawText.TextKey.contains(WidgetRuleId.W_CD_JI_TEXT) ||
										drawText.TextKey.contains(WidgetRuleId.W_CD_CHONG_TEXT) || 
										drawText.TextKey.contains(WidgetRuleId.ND_WARN) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_YEAR) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_MONTH) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_DAY) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_NL_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_WEEK_QUOTE) ||
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_1_QUOTE) || 
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_2_QUOTE) || 
										drawText.TextKey.contains(WidgetRuleId.ND_DATE_SHORT_TYPE_3_QUOTE) ||
										WidgetRuleId.ND_HOT_WEATHER_ID.equals(sHotArea)) {
									drawText.emtType = SkinHotArea.HOT_AREA_WEATHER;
									mListDraw.add(drawText);
								}
							}
						}

						// 根据属性绘制图片
						else if (name.equalsIgnoreCase(Skin.SkinDraw.NODE_NAME)) {
							DrawImageInfo drawImage = new DrawImageInfo();
							
							drawImage.Type = parser.getAttributeValue(null, Skin.SkinDraw.TYPE);
							drawImage.FileName = parser.getAttributeValue(null, Skin.SkinDraw.FILENAME);
							
							int X = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinDraw.X));
							int Y = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinDraw.Y));
							int width = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinDraw.WIDTH));
							int height = Integer.parseInt(parser.getAttributeValue(null, Skin.SkinDraw.HEIGHT));
							
							drawImage.NeedScale = Boolean.parseBoolean(parser.getAttributeValue(null, Skin.SkinDraw.NEED_SCALE));

							// 超出模板的使用默认
							if (mbOutOfResolution) {
								X = ComfunHelp.convertSize(X);
								Y = ComfunHelp.convertSize(Y);
								width = ComfunHelp.convertSize(width);
								height = ComfunHelp.convertSize(height);
							}
							
							drawImage.rect = new Rect(X, Y, X + width, Y + height);
							int extH = 0, extW = 0;
							if (height < WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE) {
								extH = (WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE - height) / 2;
							}

							if (width < WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE) {
								extW = (WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE - width) / 2;
							}
							
							drawImage.rectHot = new Rect(X - extW, Y - extH, X + width + extW, Y + height + extH);

							if (drawImage.Type.equalsIgnoreCase("ND_BACKGROUND")) {
								mDrawPictureBkInfo = drawImage;
							} else {
								mListDrawImageInfo.add(drawImage);
								
								// 热区
								String sHotArea = parser.getAttributeValue(null, Skin.HOT_AREA);
								if (sHotArea != null && WidgetRuleId.ND_HOT_CITY_ID.equals(sHotArea)) {
									drawImage.emtType = SkinHotArea.HOT_AREA_CITY;
									mListDraw.add(drawImage);
									
								} else if (WidgetRuleId.ND_TIME_HOUR_POSITION_1_ID.equals(drawImage.Type) ||
									WidgetRuleId.ND_TIME_HOUR_POSITION_2_ID.equals(drawImage.Type) || 
									WidgetRuleId.ND_TIME_MINUTE_POSITION_1_ID.equals(drawImage.Type) ||
									WidgetRuleId.ND_TIME_MINUTE_POSITION_2_ID.equals(drawImage.Type) ||
									WidgetRuleId.ND_TIME_COLON_ID.equals(drawImage.Type)) {
									
									drawImage.emtType = SkinHotArea.HOT_AREA_TIME;
									mListDraw.add(drawImage);
									
								} else if (WidgetRuleId.ND_WEATHER_ICON_CURRENT_ID.equals(drawImage.Type) ||
										WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_1_ID.equals(drawImage.Type) || 
										WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_2_ID.equals(drawImage.Type) ||
										WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_3_ID.equals(drawImage.Type) ||
										WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_4_ID.equals(drawImage.Type) ||
										WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_5_ID.equals(drawImage.Type) ||
										WidgetRuleId.ND_WARNING.equals(drawImage.Type)
										) {
									
									drawImage.emtType = SkinHotArea.HOT_AREA_WEATHER;
									mListDraw.add(drawImage);
								}
							}
						}
					} else if (name.equalsIgnoreCase(Skin.ROOT_NAME)) {
						// 检查支持的分辨率
						String sSupport = parser.getAttributeValue(null, Skin.SUPPORT_RESOLUTION);
						if (!TextUtils.isEmpty(sSupport) && (sSupport.contains(sOrgResolution) || sSupport.contains("1080*")
								|| sSupport.contains("480*") || sSupport.contains("540*") || sSupport.contains("720*") || sSupport.contains("1200*")
								|| sSupport.contains("1440*"))) {
							sResolution = getCanvaSize(sOrgResolution, sWidgetType, true);
						}
						
						String sIcoVer = parser.getAttributeValue(null, Skin.WEATHER_ICON_VER);
						if (!TextUtils.isEmpty(sIcoVer)) {
							mWeahterIconVer = Integer.parseInt(sIcoVer);
						}
						
						sIcoVer = parser.getAttributeValue(null, Skin.NUMBER_ICON_VER);
						if (!TextUtils.isEmpty(sIcoVer)) {
							mNumberIconVer = Integer.parseInt(sIcoVer);
						}
					}
					
				} else if (eventType == XmlPullParser.END_TAG) {// 结束元素事件
					if (parser.getName().equalsIgnoreCase(Skin.NODE_NAME) && bMatched) {
						break;
					}
				}

				eventType = parser.next();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			inStream.close();
		} catch (Exception e) {
		}
		
		return bMatched;
	}
	
	static final int parseInt(String s) {
		if (s != null) {
			return Integer.parseInt(s);
		}
		
		return 0;
	}
	
	static final int parseInt(String s, int radix) {
		if (s != null) {
			return Integer.parseInt(s, radix);
		}
		
		return 0;
	}
}
