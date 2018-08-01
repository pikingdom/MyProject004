/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : FileHelp
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 下午05:46:40 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;

public class FileHelp {
    /*软件在sd卡下的主目录*/
    public static final String CALENDAR_DIR = ".91Calendar";
    /*旧的目录*/
    public static final String OLD_CALENDAR_DIR = "91Calendar";
    /*插件缓存*/
    public static final String THUMB_DIR = "thumb";
    /*插件皮肤*/
    public static final String SKIN_DIR = "skin";
    /*软件推荐图标*/
    public static final String ICON_DIR = "Icon";
    /*预警图标目录*/
    public static final String WARNING_DIR = "WarningIco";
    /*背景图片目录*/
    public static final String UIBK_DIR = "UiBk";
    
    /*临时目录*/
    public static final String TEMP_DIR = "temp";
    
    /*引导图片目录*/
    public static final String GUIDE_IMAGE_DIR = "guideImage";
    
    /*天气图标目录*/
    public static final String WIP_ICON_DIR = "wipIcon";
    
    /*天气图标目录*/
    public static final String WIP_NEW_ICON_DIR = "wipIcon2";
    
    /** 91桌面天气插件皮肤存放目录 */
	private static String PANDAHOME_CALENDAR_DIR = Environment.getExternalStorageDirectory() + "/PandaHome2/91clockandweather/";
    
    public static String GetPhoneDatabase(Context context, String strFile) {
        File dbFile = context.getDatabasePath(strFile);
        
        File f = new File(dbFile.getParent());
        if (!f.exists()) {
            f.mkdirs();
        }
        
        return dbFile.getAbsolutePath();
    }

    public static String GetDBNameWithVer(String strFile, String sVer) {
        int p = strFile.lastIndexOf('.');

        if (p > -1) {
            StringBuffer yBuffer = new StringBuffer(strFile);
            return yBuffer.insert(p, sVer).toString();
        }

        return null;
    }

    // 获取sd卡的路径
    public static String getSDPath() {
        File sdDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        } else {
            // 返回到自己的默认安装路径
            return "";
        }

    }

    /**
     * @Title: getCalendarDir
     * @Description: TODO(获取黄历sd卡目录)
     * @author yanyy
     * @date 2012-5-23 下午02:42:32
     * 
     * @return
     * @return String
     * @throws
     */
    public static String getCalendarDir(String dir) {
        String path = "";
        String sdPath = getSDPath();
        if (!TextUtils.isEmpty(sdPath)) {
            File f = new File(sdPath, CALENDAR_DIR);
            if (!f.exists()) {
                // 判断旧的文件是否存在
                File oldf = new File(sdPath, OLD_CALENDAR_DIR);
                if (oldf.exists()) {
                    // 存在就挪到新的
                    oldf.renameTo(f);
                }
                if (!f.exists()) {
                    f.mkdir();
                }
            }
            if (!TextUtils.isEmpty(dir)){
                File dirf = new File(f.getAbsolutePath(), dir);
                if (!dirf.exists()){
                    dirf.mkdir();
                }
                path = dirf.getAbsolutePath();
            }else{
                path = f.getAbsolutePath();
            }
        }
        return path;
    }
    
    /**
     * @brief 【返回SD卡常量路径】
     * @n<b>函数名称</b>     :getCalendarDir2
     * @param dir
     * @return
     * @return    String   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-11-2上午10:19:45
     * @<b>修改时间</b>      :
    */
    private static String getCalendarDir2(String dir) {
        String path = "";
        String sdPath ="";
        
        try {
        	sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		} catch (Exception e) {
		}
		
        if (!TextUtils.isEmpty(sdPath)) {
            File f = new File(sdPath, CALENDAR_DIR);
            if (!f.exists()) {
                // 判断旧的文件是否存在
                File oldf = new File(sdPath, OLD_CALENDAR_DIR);
                if (oldf.exists()) {
                    // 存在就挪到新的
                    oldf.renameTo(f);
                }
                if (!f.exists()) {
                    f.mkdir();
                }
            }
            
            if (!TextUtils.isEmpty(dir)){
                File dirf = new File(f.getAbsolutePath(), dir);
                if (!dirf.exists()){
                    dirf.mkdirs();
                }
                path = dirf.getAbsolutePath();
            }else{
                path = f.getAbsolutePath();
            }
        }
        
        return path;
    }
    public static String getCalendarThumbDir(){
        return getCalendarDir2(THUMB_DIR);
    }
    
    public static String getCalendarSkinDir(){
        return getCalendarDir2(SKIN_DIR);
    }
    
    public static String getCalendarIconDir(){
        return getCalendarDir(ICON_DIR);
    }
    
    public static String getCalendarUiBkDir(){
    	String sPath = getCalendarDir(UIBK_DIR);
    	if (sPath.indexOf(UIBK_DIR) == -1) {
			return "";
		}
    	
        return sPath;
    }
    
    public static String getCalendarWarningDir(){
        return getCalendarDir(WARNING_DIR);
    }
    
    public static String getCalendarTempDir(){
        return getCalendarDir(TEMP_DIR);
    }
    
    /**
     * @Title: getCalendarGuideImageDir  
     * @Description: TODO(引导图存放目录)  
     * @author yanyy
     * @date 2012-12-4 下午03:56:15 
     *
     * @param c
     * @return       
     * @return File
     * @throws
     */
    public static File getCalendarGuideImageDir(Context c){
        // SD卡不存在，返回data目录
    	if(!SysHelpFun.isSdcardExist()) {
    		return c.getDir(GUIDE_IMAGE_DIR, Context.MODE_PRIVATE);
    	}
    	File f = new File(PANDAHOME_CALENDAR_DIR + GUIDE_IMAGE_DIR + "/");
    	if(!f.exists()) {
    		f.mkdirs();
    	}
    	return f;
    }
    
    /**
     * @Title: getCalendarWipIconDir  
     * @Description: TODO(天气图标目录)  
     * @author yanyy
     * @date 2012-12-9 上午10:28:44 
     *
     * @param c
     * @return       
     * @return File
     * @throws
     */
    public static File getCalendarWipIconDir(Context c){
    	// SD卡不存在，返回data目录
    	if(!SysHelpFun.isSdcardExist()) {
    		return c.getDir(WIP_ICON_DIR, Context.MODE_PRIVATE);
    	}
    	File f = new File(PANDAHOME_CALENDAR_DIR + WIP_ICON_DIR + "/");
    	if(!f.exists()) {
    		f.mkdirs();
    	}
    	return f;
    }
    
    public static File getCalendarNewWipIconDir(Context c){
    	if(!SysHelpFun.isSdcardExist()) {
    		return c.getDir(WIP_NEW_ICON_DIR, Context.MODE_PRIVATE);
    	}
    	File f = new File(PANDAHOME_CALENDAR_DIR + WIP_NEW_ICON_DIR + "/");
    	if(!f.exists()) {
    		f.mkdirs();
    	}
    	return f;
    }

    // 获取默认程序安装路径
    public static String GetPhoneData(Context c, String strFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getFilesDir().getParent());
        sb.append(File.separator).append(strFileName);
        return sb.toString();
    }

    // 创建文件夹
    public static boolean CreatePathFile(String strPath) {

        File destDir = new File(strPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return true;
    }

    public static boolean isExist(String path) {
        File file = new File(path);
        // 判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    // 复制文件
    public static boolean copyFile(String sourceFile, String targetFile) {
        try {
            if (sourceFile.equalsIgnoreCase(targetFile)) {
                return false;
            }
            // 新建文件输入流并对它进行缓冲
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            // 关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // 复制文件
    public static boolean copyFile(File sourceFile, String targetFile) {
        try {
            // 新建文件输入流并对它进行缓冲
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            // 关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // 删除文件
    public static boolean DeleteFile(String targetFile) {
        File destDir = new File(targetFile);
        return DeleteFile(destDir);
    }
    
    public static boolean DeleteFile(File f) {
        if (f.exists()) {
            return f.delete();
        }
        return true;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static Bitmap getImageFile(String filePath, Display display) {
        return getImageFile(filePath, display.getWidth(), display.getHeight());
    }

    public static Bitmap getImageFile(String filePath, int sWidth, int sHeight) {
        File file = new File(filePath);
        if (!file.exists())// 文件不存在
            return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        // 先测量图片的尺寸
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);
        int imWidth = opts.outWidth; // 图片宽
        int imHeight = opts.outHeight; // 图片高
        
        if (bmp != null) {
			bmp.recycle();
			bmp = null;
		}
        
        int scale = 1;
        if (imWidth > imHeight)
            scale = Math.round((float) imWidth / sWidth);
        else
            scale = Math.round((float) imHeight / sHeight);
        scale = scale == 0 ? 1 : scale;

        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        FileInputStream fis = null;
        Bitmap tmpBmp = null;
        try {
            fis = new FileInputStream(new File(filePath));
            tmpBmp = BitmapFactory.decodeStream(fis, null, opts);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }

        return tmpBmp;

    }

    /**
     * @brief 【下载文件并保存到文件】
     * 
     * 
     * @n<b>函数名称</b> :DownSaveToFile
     * @see
     * 
     * @param @param url
     * @param @param file
     * @param @return
     * @return boolean
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2012-1-4下午03:40:20
     */
    public static boolean DownSaveToFile(String url, File file) {
        boolean bFlag = false;
        InputStream is = null;
        OutputStream os = null;
        try {
            // 下载函数
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();

            conn.connect();
            is = conn.getInputStream();
            if (is == null)
                throw new RuntimeException("stream is null");

            // 把文件存到path
            DeleteFile(file);
            os = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int byteread = 0;
            while ((byteread = is.read(buf)) != -1) {
                os.write(buf, 0, byteread);
            }

            bFlag = true;

        } catch (Exception e) {
            // Log.v("PubFun.DownSaveToFile", PubFun.getErrorMessage(e));
            e.printStackTrace();
            DeleteFile(file);
        } finally {

            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                // Log.v("PubFun.DownSaveToFile", PubFun.getErrorMessage(e));
                e.printStackTrace();
            }

        }
        return bFlag;
    }

    /**
     * @brief 【修改文件夹下的图标文件后缀名】
     * 
     * @n<b>函数名称</b> :changeImgsExtension
     * @param FilePath
     * @param sExtension
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-6下午03:15:32
     */
    public static void changeImgsExtension(String FilePath, String sExtension) {
        File dir = new File(FilePath);

        final FilenameFilter fnf = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return (filename.toLowerCase().matches("\\b.*\\.(gif|png)\\b"));
            }
        };

        File[] imgFiles = dir.listFiles(fnf);

        for (File file : imgFiles) {
            file.renameTo(new File(FilePath, file.getName() + sExtension));
        }
    }
    
    /**
     * @Title: readStrAssetsFile  
     * @Description: TODO(从资产中读取文件)  
     * @author yanyy
     * @date 2012-10-9 下午02:28:56 
     *
     * @param ctx
     * @param assetpath
     * @return       
     * @return String
     * @throws
     */
    public static String readStrAssetsFile(Context ctx, String assetpath) {
        InputStream fis = null;
        ByteArrayOutputStream bout = null;
        try {
            fis = ctx.getAssets().open(assetpath);// 获取输入流
            if (fis == null) {
                return null;
            }
            bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                bout.write(buffer, 0, len);
            }

            return new String(bout.toByteArray());
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
