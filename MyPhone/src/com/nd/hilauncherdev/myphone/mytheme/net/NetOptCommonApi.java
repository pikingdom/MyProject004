package com.nd.hilauncherdev.myphone.mytheme.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.util.Log;

import com.nd.hilauncherdev.kitset.DigestUtils;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

public class NetOptCommonApi {

	public static final int CONNECTION_TIMEOUT = 10*1000;
	
	public static final String TAG = "NetOptCommonApi"; 
	
	public static boolean downloadImageByURL(String imgurl, String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				URL url = new URL(imgurl);
				URLConnection con = url.openConnection();
				//连接超时时间设置为10秒
				con.setConnectTimeout(CONNECTION_TIMEOUT);
				con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
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
				} finally {
					try {
						os.close();
					} catch (Exception ex) {
					}
					try {
						is.close();
					} catch (Exception ex) {
					}
					os = null;
					is = null;
					con = null;
					url = null;
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			
		}

	}
	
	/**
	 * 
	 * @param url
	 * @param rootpath  结尾带"/"
	 * @return
	 */
	public static String url2path (String url, String rootpath){
		if (url==null)
			url = "";
		String rs = rootpath;
		String picname = getMd5FileName(url);
		rs = rs+picname;
		//TODO 兼容旧版本
		//rs = StringUtil.renameRes(rs);
		return rs;
	}
	
	private static String getMd5FileName(String url){
		if ( url==null || "".equals(url) )
			return "";
		String str = DigestUtils.md5Hex(url)+"";
		return str; 
	}
	
	public static String getURLContent(String surl) {	
		return getURLContent(surl, "utf-8");
	}
	
	public static String getURLContent(String surl, String encode) {		
		InputStream is = null;
		try {
			URL url = new URL( surl );
			HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.setConnectTimeout(CONNECTION_TIMEOUT);
			is = httpUrl.getInputStream();			
			BufferedReader br = new BufferedReader(new InputStreamReader(is,encode));
			StringBuffer sb = new StringBuffer();
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "getURLContent:" + surl);
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}	
	
	
	/**
	 * 如果原URL带有参数则补&key=value,否则补上?key=value
	 * 
	 * @param sb
	 *            现有的URL的StringBuffer
	 * @param key
	 *            参数
	 * @param value
	 *            参数
	 */
	public static void appendAttrValue(StringBuffer sb, String key, String... values) {
		if (sb.indexOf("?" + key + "=") != -1 || sb.indexOf("&" + key + "=") != -1) {
			return;
		}

		for (String value : values) {
			if (sb.indexOf("?") == -1) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
	}
	
	/**
	 * 添加全局请求参数 
	 * @param ctx
	 * @param sb
	 */
	public static void addGlobalRequestValue(Context ctx, StringBuffer sb){
		
		if (sb==null || ctx==null)
			return ;
		
		String imsiNumber = TelephoneUtil.getIMSI(ctx);
		if( null == imsiNumber ) {
			imsiNumber = "91";
		}
		String imeiNumber = TelephoneUtil.getIMEI(ctx);
		if( null == imeiNumber ) {
			imeiNumber = "91";
		}
		appendAttrValue(sb, "mt", "4");
		appendAttrValue(sb, "tfv", "40000");
		appendAttrValue(sb, "imei", imeiNumber);
		appendAttrValue(sb, "imsi", imsiNumber);
		appendAttrValue(sb, "Pid", "6");
		appendAttrValue(sb, "DivideVersion", com.nd.hilauncherdev.kitset.util.TelephoneUtil.getVersionName(ctx, ctx.getPackageName()) );
		appendAttrValue(sb, "supfirm", com.nd.hilauncherdev.kitset.util.TelephoneUtil.getFirmWareVersion());
		appendAttrValue(sb, "nt", com.nd.hilauncherdev.kitset.util.TelephoneUtil.getNT(ctx));
		appendAttrValue(sb, "chl", ChannelUtil.getChannel(ctx));
	}
}
