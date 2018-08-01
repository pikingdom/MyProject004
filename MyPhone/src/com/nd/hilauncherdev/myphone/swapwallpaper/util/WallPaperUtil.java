package com.nd.hilauncherdev.myphone.swapwallpaper.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.bean.WallPaperContants;
import com.nd.hilauncherdev.url.URLs;

/**
 * 描述:壁纸管理的工具类
 * 
 * @author linqiang(866116)
 * @Since 2012-5-29
 */
public class WallPaperUtil {

	/**
	 * 获取已存在的缩略图片列表
	 * 
	 * @return
	 */
	public static List<String> getExistsThumbWallpapers() {
		String path = WallPaperUtil.getWPThumbCachePath();
		return FileUtil.getExistsFileNames(path, FileUtil.imagefileFilter, true);
	}

	/**
	 * 获取已存在的壁纸分类图标列表
	 * 
	 * @return
	 */
	public static List<String> getExistsClassifyIcon() {
		String path = WallPaperUtil.getWPClassifyCachePath();
		return FileUtil.getExistsFileNames(path, FileUtil.imagefileFilter, true);
	}

	/**
	 * 获取下载的本地原图列表
	 * 
	 * @return
	 */
	public static List<String> getExistsWallpapers() {
		String path = WallPaperUtil.getPicturesHome();
		return getExistsFileNames(path, FileUtil.imagefileFilter, true);
	}

	/**
	 * 获取本地壁纸缩略图缓存列表
	 * 
	 * @return
	 */
	public static List<String> getExistsCacheWallpapers() {
		String path = WallPaperUtil.getWPRealCachePath();
		return FileUtil.getExistsFileNames(path, FileUtil.imagefileFilter, true);
	}

	/**
	 * 获取壁纸缩略图 缓存目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPThumbCachePath() {
		return FileUtil.getPath(WallPaperContants.WP_THUMB_CACHE_DIR);
	}

	/**
	 * 获取壁纸分类图标缓存目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPClassifyCachePath() {
		return FileUtil.getPath(WallPaperContants.CLASSIFY_CACHE_DIR);
	}

	/**
	 * 获取壁纸原图 缓存目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPRealCachePath() {
		return FileUtil.getPath(WallPaperContants.WP_REAL_CACHE_DIR);
	}

	/**
	 * 获取默认壁纸/在线下载壁纸原图目录（全路径）
	 * 
	 * @return
	 */
	public static String getPicturesHome() {
		return FileUtil.getPath(WallPaperContants.PICTURES_HOME);
	}

	/**
	 * 获取一键换壁纸原图目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPPicOneKeyHome(){
		return FileUtil.getPath(WallPaperContants.PICTURES_ONEKEY_HOME);
	}
	
	/**
	 * 获取一键换壁纸临时目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPPicOneKeyTmpHome(){
		return FileUtil.getPath(WallPaperContants.PICTURES_ONEKEY_TMP);
	}
	
	/**
	 * 获取一键壁纸类型风格原图（全路径）
	 */
	public static String getWPPicOneKeyStyleHome(){
		return FileUtil.getPath(WallPaperContants.PICTURES_ONEKEY_STYLE);
	}
	
	/**
	 * 获取本地壁纸缩略图目录（全路径）
	 * 
	 * @return
	 */
	public static String getWPPicHomeCachePath() {
		return FileUtil.getPath(WallPaperContants.WP_LOCAL_THUMB_CACHE_DIR);
	}

	/**
	 * 
	 * <br>
	 * Description: 获取在线壁纸InputStream <br>
	 * Author:caizp <br>
	 * Date:2011-8-10上午11:54:18
	 * 
	 * @param isZh
	 * @param navType
	 *            导航标识
	 * @return
	 */
	public static InputStream getWallpaperStream(int type, int pageIndex) {
		String strUrl = null;
		String frontUrl = URLs.STATIC_WALLPAPER_FETCH_URL;

		int navType = WallPaperContants.TAB_HOTEST_TAG;

		String base_server = null;
		if (CommonGlobal.isZh()) {// 中国
			base_server = WallPaperContants.BASE_SERVER_ZH;
		} else {
			// 国外
			base_server = WallPaperContants.BASE_SERVER_EN;
		}

		if (type == WallPaperContants.CALSSIFY_WALLPAPER) {
			strUrl = String.format(URLs.STATIC_WALLPAPER_CLASSIFY_URL, base_server);
		} else {
			if (type == WallPaperContants.NEWEST_WALLPAPER) {
				navType = WallPaperContants.TAB_NEWEST_TAG;
			}
			strUrl = String.format(frontUrl, base_server, "" + navType, "" + pageIndex, WallPaperContants.SHOP_WALLPAPER_PAGE_SIZE);
		}

		return openURL(strUrl);// 返回输入流
	}

	/**
	 * 
	 * @Title: getWallpaperSearchStream
	 * @Description:搜索壁纸
	 * @param url
	 * @param pageIndex
	 * @param searchKW
	 * @return
	 * @return InputStream
	 * @throws
	 * @date 2012-11-7 上午10:15:13
	 */
	public static InputStream getWallpaperSearchStream(String url, int pageIndex, String searchKW) {

		String strUrl = "";
		String base_server = null;
		if (CommonGlobal.isZh()) {
			base_server = WallPaperContants.BASE_SERVER_ZH;
		} else {
			// 国外
			base_server = WallPaperContants.BASE_SERVER_EN;
		}
		strUrl = String.format(url, base_server, "" + pageIndex, searchKW);
		return openURL(strUrl);
	}

	/**
	 * 描述:获取分类在线壁纸InputStream
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-6-1
	 * @param cateid
	 *            分类ID
	 * @param pageIndex
	 *            当前页
	 * @return
	 */
	public static InputStream getWallpaperDetailStream(String cateid, int pageIndex) {
		String strUrl = null;
		String frontUrl = URLs.STATIC_WALLPAPER_CLASSIFY_DETAIL_URL;
		strUrl = String.format(frontUrl, WallPaperContants.BASE_CLASSIFY_SERVER_ZH, cateid, "" + pageIndex, WallPaperContants.SHOP_WALLPAPER_PAGE_SIZE);
		return openURL(strUrl);// 返回输入流
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (null != connectivityManager) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
			if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected()) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 
	 * <br>
	 * Description: 获取网络输入流 <br>
	 * Author:caizp <br>
	 * Date:2011-8-10上午11:57:19
	 * 
	 * @param strUrl
	 * @return
	 */
	public static InputStream openURL(String strUrl) {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			// 连接超时时间设置为8秒
			conn.setConnectTimeout(8 * 1000);
			conn.setDoInput(true);
			conn.connect();
			in = conn.getInputStream();
			return in;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 描述:下载网络图片
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-5-23
	 * @param url
	 *            //图片的URL
	 * @param fileName
	 *            //要保存到本地的文件名
	 * @param savePath
	 *            //要保存的目录全路径
	 * @return
	 */
	public static boolean downLoadImageOnline(URL url, String fileName, String savePath) {
		String tempFileName = "";
		if (url != null && savePath != null) {
			try {

				fileName = fileName == null ? FileUtil.getFileName(url.toString(), true) : fileName;
				tempFileName = fileName + ".temp";
				File f = new File(savePath + "/" + tempFileName);
				if (!f.exists()) {
					URLConnection con = url.openConnection();
					// 连接超时时间设置为8秒
					con.setConnectTimeout(8 * 1000);
					con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
					// HttpURLConnection conn = (HttpURLConnection) con;
					// if (conn.getResponseCode() ==
					// java.net.HttpURLConnection.HTTP_NOT_FOUND) {
					// return java.net.HttpURLConnection.HTTP_NOT_FOUND;
					// }
					InputStream is = con.getInputStream();
					if (con.getContentEncoding() != null && con.getContentEncoding().equalsIgnoreCase("gzip")) {
						is = new GZIPInputStream(con.getInputStream());
					}
					byte[] bs = new byte[256];
					int len = -1;
					OutputStream os = new FileOutputStream(f);
					try {
						while ((len = is.read(bs)) != -1) {
							os.write(bs, 0, len);
						}
						// os.flush();
					} finally {
						try {
							os.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						try {
							is.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						os = null;
						is = null;
						con = null;
						url = null;
						FileUtil.renameFile(savePath + "/" + tempFileName, savePath + "/" + fileName);
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				File file = new File(savePath + "/" + tempFileName);
				if (file.exists()) {
					FileUtil.delFile(file.getAbsolutePath());
				}
				return false;
			}
		} else {
			return false;
		}
	}// end downLoadImageOnline

	/**
	 * 描述:下载生成缩略图
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-5-25
	 * @param url
	 * @param fileName
	 * @param savePath
	 * @return
	 */
	public static boolean downLoadImageAsThumb(URL url, int thumbWidth, int thumbHeight, String fileName, String savePath) {
		String tempFileName = "";
		if (url != null && savePath != null) {
			try {

				fileName = fileName == null ? FileUtil.getFileName(url.toString(), true) : fileName;
				tempFileName = fileName + ".temp";

				String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
				if (ext.equals("JPG")) {
					ext = "JPEG";
				}

				File f = new File(savePath + "/" + tempFileName);
				if (f.exists()) {
					f.delete();
				}
				URLConnection con = url.openConnection();
				// 连接超时时间设置为8秒
				con.setConnectTimeout(8 * 1000);
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
				InputStream is = con.getInputStream();
				if (con.getContentEncoding() != null && con.getContentEncoding().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(con.getInputStream());
				}
				OutputStream os = new FileOutputStream(f);
				Bitmap bitmap = null;
				try {
					bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(is), thumbWidth, thumbHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
					bitmap.compress(Bitmap.CompressFormat.valueOf(ext), 100, os);
					os.flush();
				} finally {
					try {
						os.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					try {
						is.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					os = null;
					is = null;
					con = null;
					url = null;
					FileUtil.renameFile(savePath + "/" + tempFileName, savePath + "/" + fileName);
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				File file = new File(savePath + "/" + tempFileName);
				if (file.exists()) {
					FileUtil.delFile(file.getAbsolutePath());
				}
				return false;
			}
		} else {
			return false;
		}
	}// end downLoadImageOnline

	/**
	 * 描述:下载生成缩略图
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-5-25
	 * @param url
	 * @param fileName
	 * @param savePath
	 * @return
	 */
	public static boolean downLoadImageAsThumb(URL url, String fileName, String savePath) {
		return downLoadImageAsThumb(url, 142, 118, fileName, savePath);
	}

	/**
	 * 描述:通过原图生成缩略图片
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-6-6
	 * @param filePath
	 * @param thumbPath
	 * @return
	 */
	public static boolean createThumb(String sourcePath, String thumbPath) {
		if (!StringUtil.isEmpty(sourcePath)) {
			try {
				String ext = sourcePath.substring(sourcePath.lastIndexOf(".") + 1).toUpperCase();
				if (ext.equals("JPG")) {
					ext = "JPEG";
				}

				// 如果目标地址为空,则在原目录下建 一个
				if (StringUtil.isEmpty(thumbPath)) {
					String path = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
					String fileName = FileUtil.getFileName(sourcePath, true);
					thumbPath = path + "/.thumb/" + fileName;
				}

				File f = new File(thumbPath);

				if (!f.exists()) {
					InputStream is = new FileInputStream(sourcePath);
					OutputStream os = new FileOutputStream(f);
					Bitmap bitmap = null;
					try {
						bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(is), 178, 118);
						bitmap.compress(Bitmap.CompressFormat.valueOf(ext), 100, os);
						os.flush();
					} finally {
						try {
							os.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						try {
							is.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						os = null;
						is = null;
						if (bitmap != null && !bitmap.isRecycled()) {
							bitmap.recycle();
						}
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}// end downLoadImageOnline
	
	/**
	 * 获取下载的一键换壁纸本地原图列表
	 * 
	 * @return
	 */
	public static List<String> getExistsOnekeyWallpapers() {
		String path = WallPaperUtil.getWPPicOneKeyHome();
		return getExistsFileNames(path, FileUtil.imagefileFilter, true);
	}
	
	private static List<String> getExistsFileNames(String dir, FileFilter fileFilter, boolean hasSuffix) {
		String path = dir;
		File file = new File(path);
		File[] files = file.listFiles(fileFilter);
		List<String> fileNameList = new ArrayList<String>();
		if (null != files) {
			Arrays.sort(files, new CompratorByLastModified());
			for (File tmpFile : files) {
				String tmppath = tmpFile.getAbsolutePath();
				String fileName = FileUtil.getFileName(tmppath, hasSuffix);
				fileNameList.add(fileName);
			}
		}
		return fileNameList;
	}

	static class CompratorByLastModified implements Comparator<File> {
		public int compare(File f1, File f2) {
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}

		public boolean equals(Object obj) {
			return true;
		}
	}
}
