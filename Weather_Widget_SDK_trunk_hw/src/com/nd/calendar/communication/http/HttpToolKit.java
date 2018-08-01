/**   
 *    
 * @file  【网络请求工具类】
 * @brief
 *
 * @<b>文件名</b>      : HttpToolKit
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-28 下午04:19:33 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.communication.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import javax.net.ssl.SSLHandshakeException;

public class HttpToolKit
{
	private static final String	TAG = "HttpToolKit";
	private static final int TIMEOUT_MILLISEC = 20000;
	// server return code
	public static int SERVER_SUCCESS = 200;
	private static final String	GZIP				= "gzip";
	private static final String	ACCEPT_ENCODEING	= "Accept-Encoding";
	
	private Context mContext;
	
	public HttpToolKit(Context context) {
		mContext = context;
	}

	private HttpRequestRetryHandler mReqRetryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			// we will try three times before getting connection
			if (executionCount >= 3) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}

			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;

		}
	};

	/**
	 * @brief 【Get请求】
	 * 
	 * 
	 * @n<b>函数名称</b> :DoGet
	 * @see
	 * 
	 * @param @param strResponse
	 * @param @return
	 * @return int
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午04:15:55
	 */
	public int DoGet(String sURL, StringBuffer strResponse, int timeOut) {
		BufferedReader in = null;
		InputStream is;
		int nRet = 0;
		
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(sURL));
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Content-Encoding", "UTF-8");
			request.addHeader(ACCEPT_ENCODEING, GZIP);	// 增加压缩包头

			// 有代理则使用代理，否则就是网络问题
			HttpHost proxyHost = getProxyHost(mContext);
			if (proxyHost != null) {
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
			}

			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
			client.setHttpRequestRetryHandler(mReqRetryHandler);

			HttpResponse response = client.execute(request);
			nRet = response.getStatusLine().getStatusCode();

			is = response.getEntity().getContent();
			
			// 检查是否是gzip压缩
			Header contentEncoding = response.getFirstHeader(HTTP.CONTENT_ENCODING);
			if (contentEncoding != null
				&& contentEncoding.getValue().equalsIgnoreCase(GZIP))
			{
				//Log.e(TAG, "get is Zip");
				is = new GZIPInputStream(is);
			}
			
			in = new BufferedReader(new InputStreamReader(is));
			
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				strResponse.append(line + NL);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		return nRet;
	}

	public int DoGet(String sURL, StringBuffer strResponse)
	{
		return DoGet(sURL, strResponse, 10000);
	}
	
	/**
	 * @brief【post请求】
	 * 
	 * 
	 * @n<b>函数名称</b> :DoPost
	 * @see
	 * 
	 * @param @param strResponse
	 * @param @return
	 * @return int
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-5-5下午04:15:44
	 */
	public int DoPost(String sURL, StringBuffer strResponse, int timeOut) {
		BufferedReader in = null;
		int nRet = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(sURL);
			
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Content-Encoding", "UTF-8");
			
			// 有代理则使用代理，否则就是网络问题
			HttpHost proxyHost = getProxyHost(mContext);
			if (proxyHost != null) {
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
			}
			
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
			
			HttpResponse response = client.execute(request);
			nRet = response.getStatusLine().getStatusCode();

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				strResponse.append(line + NL);
			}
			in.close();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					Log.e(TAG, ioe.toString());
				}
			}
		}

		return nRet;
	}
	
	public int DoPost(String sURL, StringBuffer strResponse) {
		return DoPost(sURL, strResponse, 10000);
	}

	/**
	 * @brief 【post请求】
	 * 
	 * 
	 * @n<b>函数名称</b> :DoPost
	 * @see
	 * 
	 * @param @param c
	 * @param @param strResponse
	 * @param @return
	 * @return int
	 * @<b>作者</b> : 邱堃
	 * @<b>修改</b> : 陈希
	 * @<b>创建时间</b> : 2011-5-5下午04:15:26
	 */
	public int DoPost(String sURL, JSONObject c, StringBuffer strResponse, int timeOut) {
		BufferedReader in = null;
		InputStream is;
		int nRet = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(sURL);
			request.addHeader(ACCEPT_ENCODEING, GZIP); // 增加压缩包头

			HttpEntity entity;
			String sPost = (c == null ? "" : c.toString());
			StringEntity s = new StringEntity(sPost, "UTF-8");
			s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8));
			entity = s;
			request.setEntity(entity);

			// 有代理则使用代理，否则就是网络问题
			HttpHost proxyHost = getProxyHost(mContext);
			if (proxyHost != null) {
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
			}
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);

			HttpResponse response = client.execute(request);
			nRet = response.getStatusLine().getStatusCode();

			is = response.getEntity().getContent();

			// 检查是否是gzip压缩
			Header contentEncoding = response.getFirstHeader(HTTP.CONTENT_ENCODING);
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase(GZIP)) {
				// Log.e(TAG, "is Zip");
				is = new GZIPInputStream(is);
			}

			in = new BufferedReader(new InputStreamReader(is));

			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				strResponse.append(line + NL);
			}
			in.close();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					Log.e(TAG, ioe.toString());
				}
			}
		}
		return nRet;
	}
	
	
	public int DoPost(String sURL, JSONObject c, StringBuffer strResponse) {
		return DoPost(sURL, c, strResponse, 10000);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	public static boolean saveToFile(Context context, String sUrl, String sPath) {
		boolean bRet = false;

		HttpURLConnection conn = null;
		FileOutputStream fout = null;
		InputStream is = null;
		File file = new File(sPath);

		try {
			fout = new FileOutputStream(file);
			URL url = new URL(sUrl);

			// 设置代理
			Proxy proxy = getProxy(context);
			if (proxy != null) {
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(TIMEOUT_MILLISEC);
			conn.setConnectTimeout(TIMEOUT_MILLISEC);
			conn.connect();
			is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}

			int byteread = 0;
			byte[] buffer = new byte[1024 * 4];

			file.createNewFile();

			while ((byteread = is.read(buffer)) != -1) {
				fout.write(buffer, 0, byteread);
			}

			bRet = true;

		} catch (Exception ex) {
			Log.e(TAG, ex.toString());
			if (file.exists()) {
				file.delete();
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			try {
				if (is != null) {
					is.close();
				}
				if (fout != null) {
					fout.flush();
					fout.close();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}

		return bRet;
	}
	
	public boolean DownloadToFile(String sURL, String path) {
		return saveToFile(mContext, sURL, path);
	}
	
	public byte[] DownloadBytes(String sURL) {
		HttpURLConnection conn = null;
		InputStream is = null;
		byte[] bt = null;
		
		try{
			URL url = new URL(sURL);

        	Proxy proxy = getProxy(mContext);
			if (proxy != null) {
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			
			conn.setRequestMethod("GET");
			conn.setReadTimeout(TIMEOUT_MILLISEC);
			conn.setConnectTimeout(TIMEOUT_MILLISEC);
			conn.connect();

			if (conn.getResponseCode() != SERVER_SUCCESS) {
				Log.d(TAG, "dowload bitmap return: " + conn.getResponseCode());
				return null;
			}else{
				Log.d(TAG, sURL + " response ok");
			}
			
			is = conn.getInputStream();
							
			// get bitmap from input stream
			bt = getBytes(is);

		}catch(Exception ex){
			Log.e(TAG, ex.toString());
			return null;
		}finally{
			if (conn != null){
				conn.disconnect();
			}
			
			try {
				if (is!=null) {
					is.close();
				}
			}catch(Exception e){
				Log.e(TAG, e.toString());
			}
		}

		return bt;
	}
	
	private byte[] getBytes(InputStream is) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;

		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	//////////////////////////////////////// 代理控制 ///////////////////////////////////////
	private static boolean needToProxy(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null) {
			return false;
		}
		
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info == null) {
			return false;
		}
		
		String type = info.getTypeName();

		if(type.equalsIgnoreCase("MOBILE")) {					// GPRS方式
		
			String apnStr = info.getExtraInfo();
			if (apnStr != null) {
				apnStr = apnStr.toLowerCase();
				if (apnStr.contains("wap")) {					// WAP方式
					String proxyHost = android.net.Proxy.getDefaultHost();
					if (proxyHost != null && !proxyHost.equals("")) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @brief 【获取代理】
	 *
	 * @n<b>函数名称</b>     :getProxyHost
	 * @param context
	 * @return
	 * @return    HttpHost   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-3-7下午05:28:32      
	*/
	public static HttpHost getProxyHost(Context context) {
		if (needToProxy(context)) {
			Log.v(TAG, android.net.Proxy.getDefaultHost());
			Log.v(TAG, Integer.toString(android.net.Proxy.getDefaultPort()));
			return new HttpHost(android.net.Proxy.getDefaultHost(),
					android.net.Proxy.getDefaultPort());
		}
		
		return null;
	}
	
	/**
	 * @brief 【获取代理】
	 *
	 * @n<b>函数名称</b>     :getProxy
	 * @param context
	 * @return
	 * @return    Proxy   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-3-7下午05:28:49      
	*/
	public static Proxy getProxy(Context context) {
		if (needToProxy(context)) {
			Log.v(TAG, android.net.Proxy.getDefaultHost());
			Log.v(TAG, Integer.toString(android.net.Proxy.getDefaultPort()));
			
			return new Proxy(java.net.Proxy.Type.HTTP,
					new InetSocketAddress(android.net.Proxy.getDefaultHost(),
							  android.net.Proxy.getDefaultPort()));
		}
		
		return null;
	}

	/**
	 * @brief 【返回当前网络是否有效】
	 * @n<b>函数名称</b>     :isNetworkAvailable
	 * @param context
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-5-18上午10:00:50
	 * @<b>修改时间</b>      :
	*/
	public static boolean isNetworkAvailable(Context context) {
		return TelephoneUtil.isNetworkAvailable(context)/*getActiveNetWorkName(context) != null*/;
	}

	/**
	 * @brief 【返回当前有效网络名】
	 * @n<b>函数名称</b>     :getActiveNetWorkName
	 * @param context
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-5-18上午10:01:40
	 * @<b>修改时间</b>      :
	*/
//	public static String getActiveNetWorkName(Context context) {
//		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		String result = null;
//
//		if (connectivity == null) {
//			return null;
//		}
//		NetworkInfo[] info = connectivity.getAllNetworkInfo();
//		if (info != null) {
//			for (int i = 0; i < info.length; i++) {
//
//				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//					result = info[i].getTypeName();
//				}
//			}
//		}
//		return result;
//	}
	
	/**
	 * @Title: isWifiEnable  
	 * @Description: TODO(判断当前网络是否是WIFI)  
	 * @author yanyy
	 * @date 2012-4-27 下午05:07:46 
	 *
	 * @param context
	 * @return       
	 * @return boolean
	 * @throws
	 */
	public static boolean isWifiEnable(Context context){
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(connectivityManager == null) {
	        return false;
	    }
	                
	    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
	    return ((info != null) && (info.getType() == ConnectivityManager.TYPE_WIFI));
	}


}
