/**   
 *    
 * @file 【熊猫主题管理】
 * @brief
 *
 * @<b>文件名</b>      : PandaThemeManager
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-7-14 下午06:07:44 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.weather.widget.PandaHome;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.nd.calendar.util.FileHelp;
import com.nd.weather.widget.R;

public class PandaThemeManager
{
	private static final String THEME_SKIN_CONFIG = "theme_skin.dat";
	
	/**
	 * @brief 【导入主题，将其保存到 theme_skin.dat】
	 * @n<b>函数名称</b>     :importTheme
	 * @param themeId
	 * @param sSkinPath
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-16上午10:20:46
	 * @<b>修改时间</b>      :
	*/
//	public static boolean importTheme(String themeId, String sSkinPath) {
//		
//		// 0 为默认主题
//		if ("0".equals(themeId)) {
//			return false;
//		}
//		
//		String WIDGET_SKIN_PATH = FileHelp.getCalendarSkinDir() + File.separator;
//
//		// 无文件夹时创建
//		File skinDir = new File(WIDGET_SKIN_PATH);
//		if (!skinDir.exists()) {
//			skinDir.mkdir();
//		}
//
//		String sThemePath = WIDGET_SKIN_PATH + THEME_SKIN_CONFIG;
//		File file = new File(sThemePath);
//		JSONObject json = null;
//		
//		if (file.exists()) {
//			json = getThemeJson(file);
//		}
//
//		if (json != null) {
//			// remove ID
//			json.remove(themeId);
//		} else {
//			json = new JSONObject();
//		}
//		
//		// save
//		try {
//			json.put(themeId, sSkinPath);
//			
//			saveThemeJson(file, json);
//			return true;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}
//
//	/**
//	 * @brief 【根据ID获取皮肤路径】
//	 * @n<b>函数名称</b>     :getThemeSkin
//	 * @param themeId
//	 * @return
//	 * @return    String   
//	 * @<b>作者</b>          :  陈希
//	 * @<b>修改</b>          :
//	 * @<b>创建时间</b>      :  2012-7-16上午10:21:30
//	 * @<b>修改时间</b>      :
//	*/
//	public static String getThemeSkin(String themeId) {
//		if ("0".equals(themeId)) {
//			return null;
//		}
//		
//		File file = new File(FileHelp.getCalendarSkinDir() + File.separator + THEME_SKIN_CONFIG);
//		String sPath = "";
//		
//		try {
//			if (file.exists()) {
//				JSONObject json = getThemeJson(file);
//				if (json != null) {
//					sPath = json.optString(themeId);
//				}
//			}
//		
//			file = new File(sPath);
//			if (!file.exists()) {
//				sPath = getSkinPathFromId(themeId);
//			}
//
//			return sPath;
//		} catch (Exception e) {
//		}
//		
//		return null;
//	}
	
	public static String getSkinPathFromId(Context context, String themeId) {
		String sPath = context.getString(R.string.skin_path);
		if (TextUtils.isEmpty(sPath)) {
			sPath = FileHelp.getCalendarSkinDir() + File.separator;
		} else {
			sPath = getSkinDir(sPath);
		}
		
		String tId = sPath + themeId.replace(" ", "_") + File.separator;

		File file = new File(tId);
		if (file.exists()) {
			return tId;
		}
		
		return null;
	}
	
    private static String getSkinDir(String sPath) {
        String path = "";
        String sdPath = "";
        
        try {
        	sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		} catch (Exception e) {
		}
		
        if (!TextUtils.isEmpty(sdPath)) {
            File f = new File(sdPath, sPath);
            if (!f.exists()){
                f.mkdirs();
            }
            
            path = f.getAbsolutePath();
        }
        
        return path;
    }
    
	/**
	 * @brief 【删除主题】
	 * @n<b>函数名称</b>     :removeThemeSkin
	 * @param themeId
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-31下午05:57:58
	 * @<b>修改时间</b>      :
	*/
//	public static String removeThemeSkin(String themeId) {
//		if ("0".equals(themeId)) {
//			return null;
//		}
//		
//		String sThemePath = FileHelp.getCalendarSkinDir() + File.separator + THEME_SKIN_CONFIG;
//		File file = new File(sThemePath);
//		JSONObject json = null;
//		
//		if (file.exists()) {
//			json = getThemeJson(file);
//			if (json != null) {
//				try {
//					Object oPath = json.remove(themeId);
//					if ( oPath != null
//						&& saveThemeJson(file, json)) {					
//						
//						return oPath.toString();
//					}
//					
//				} catch (Exception e) {
//				}
//			}
//		}
//		
//		return null;
//	}

	/**
	 * @brief 【保存主题json】
	 * @n<b>函数名称</b>     :saveThemeJson
	 * @param file
	 * @param jsonObject
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-16上午10:23:14
	 * @<b>修改时间</b>      :
	*/
//	static final boolean saveThemeJson(File file, JSONObject jsonObject) {
//		try {
//			FileWriter fileWriter = new FileWriter(file);
//			fileWriter.write(jsonObject.toString());
//			fileWriter.close();
//			return true;
//		} catch (Exception e) {
//		}
//		
//		return false;
//	}
//	
//
//	/**
//	 * @brief 【从配置文件获取主题json】
//	 * @n<b>函数名称</b>     :getThemeJson
//	 * @param file
//	 * @return
//	 * @return    JSONObject   
//	 * @<b>作者</b>          :  陈希
//	 * @<b>修改</b>          :
//	 * @<b>创建时间</b>      :  2012-7-16上午10:23:36
//	 * @<b>修改时间</b>      :
//	*/
//	static JSONObject getThemeJson(File file) {
//		try {
//			String sLine;
//			FileReader fileReader = new FileReader(file);
//			BufferedReader reader = new BufferedReader(fileReader);
//			StringBuilder sBuilder = new StringBuilder();
//			
//			while ((sLine = reader.readLine()) != null) {
//				sBuilder.append(sLine);
//			}
//			
//			reader.close();
//			fileReader.close();
//			return StringHelp.getJSONObject(sBuilder.toString()) ;
//		} catch (Exception e) {
//		}
//		
//		return null;
//	}
}
