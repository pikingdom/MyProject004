/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : WidgetLoadedSkinInfo
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-6 下午06:27:40 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.skin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.WidgetGlobal;

public class WidgetLoadedSkinInfo
{
	public final static int WSI_LOAD_FAILURE = 0;
	public final static int WSI_LOAD_SUCCESS = 1;
	public final static int WSI_SWITCH_TO_DEFAULT = 2;
	public final static int WSI_NEW_SKIN = 4;
	final static String SKIN_XML = "skin.xml";
	
	public WidgetSkinConfig skinConfig = new WidgetSkinConfig();
	public String sLoadedSkinPath = null;
	public boolean bUserSkin = false;
	public AssetManager assetMgr = null;
	
	public int loadWidgetSkin(Context context, String sSkinPath, String sWidgetType, String sDefaultSkin) {
		int iRet = WSI_LOAD_FAILURE;
		InputStream is = null;
		
		// Log.d("skin", sSkinPath);

		try {
			assetMgr = null;
			
			// 主版皮肤切换
			if (WeatherLinkTools.WIDGET_PANDA_DEFAULT_SKIN.equals(sSkinPath)) {
				sSkinPath = WidgetGlobal.getDefaultSkinPath(context);
			} else if(WeatherLinkTools.WIDGET_CALENDAR_SKIN.equals(sSkinPath)) {
				assetMgr = WeatherLinkTools.getInstance(context).getLinkAssetManager();
			}

			if (assetMgr == null) {
				assetMgr = context.getAssets();
			}
			
			String sSkinFile = sSkinPath + SKIN_XML;

			// 1.测试打开指定的皮肤配置(检测皮肤图片是否可用)
			try {
				bUserSkin = sSkinPath.startsWith(File.separator);
				if (bUserSkin) {
					is = new FileInputStream(sSkinFile);
				} else {
					is = assetMgr.open(sSkinFile);
				}
			} catch (Exception e) {		// 如果没有找到，则暂置为内置
				bUserSkin = false;
				if (sDefaultSkin == null) {
					sDefaultSkin = WidgetGlobal.getDefaultSkinPath(context);
				}
				
				sSkinPath = sDefaultSkin;
				
				sSkinFile = sSkinPath + SKIN_XML;
				is = assetMgr.open(sSkinFile);
				
				iRet |= WSI_SWITCH_TO_DEFAULT;
			}
			
			// 2.如果皮肤配置发生变化，则重新加载配置
			if (sLoadedSkinPath == null || !sLoadedSkinPath.equals(sSkinPath)) {
				
				sLoadedSkinPath = sSkinPath;
				if (!skinConfig.readXML(is, WidgetGlobal.GetResolution(context), sWidgetType)) {
					is = null;
					
					if (bUserSkin) {
						is = new FileInputStream(sSkinFile);
					} else {
						is = assetMgr.open(sSkinFile);
					}
					
					skinConfig.readXML(is, "", sWidgetType);
				}

				Log.d("skin", "loaded skin");
				iRet |= WSI_NEW_SKIN;
			} else {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			
			iRet |= WSI_LOAD_SUCCESS;
		} catch (Exception e) {
			
			iRet = WSI_LOAD_FAILURE;
			e.printStackTrace();
		}	

		return iRet;
	}
}
