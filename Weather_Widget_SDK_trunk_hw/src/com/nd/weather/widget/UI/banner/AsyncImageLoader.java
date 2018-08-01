package com.nd.weather.widget.UI.banner;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nd.calendar.util.StringHelp;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.theme.data.ThemeFormart;



/**
 * 模式:使用线程池为每个异步加载的图片提供服务
 * @author cfb
 *
 */
public class AsyncImageLoader {

	 private ExecutorService executorService = Executors.newCachedThreadPool(); 

	 public HashMap<String, WeakReference<Drawable>> imageCache;
	  
	     public AsyncImageLoader() {
	    	 imageCache = new HashMap<String, WeakReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
	         if (imageCache.containsKey(imageUrl)) {
	             WeakReference<Drawable> softReference = imageCache.get(imageUrl);
	             Drawable drawable = softReference.get();
	             if (drawable != null) {
	                 return drawable;
	             }
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
	             }
	         };
	         
	         Thread t = new Thread() {
	             @Override
	             public void run() {
	            	 try{
		                 Drawable drawable = loadImageFromUrl(imageUrl);
		                 imageCache.put(imageUrl, new WeakReference<Drawable>(drawable));
		                 Message message = handler.obtainMessage(0, drawable);
		                 handler.sendMessage(message);
	            	 }catch (Exception e) {
						e.printStackTrace();
					 }
	             }
	         };
	         //t.start();
	         executorService.execute(t);	         
	         return null;
	     }

	     /**
	      * <br>Description: 加载本地主题图片，无缩略图时自动生成
	      * <br>Author:caizp
	      * <br>Date:2012-9-12上午11:24:27
	      * @param imagePath
	      * @param themeId
	      * @param imageCallback
	      * @return
	      */
	     public Drawable loadDrawable(final String imagePath, final String themeId, final ImageCallback imageCallback) {
	         if (imageCache.containsKey(imagePath)) {
	             WeakReference<Drawable> softReference = imageCache.get(imagePath);
	             Drawable drawable = softReference.get();
	             if (drawable != null) {
	                 return drawable;
	             }
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((Drawable) message.obj, imagePath);
	             }
	         };
	         
	         Runnable runnable = new Runnable() {
	             @Override
	             public void run() {
	            	 try{
		                 Drawable drawable = loadImageFromPath(imagePath, themeId);
		                 imageCache.put(imagePath, new WeakReference<Drawable>(drawable));
		                 Message message = handler.obtainMessage(0, drawable);
		                 handler.sendMessage(message);
	            	 }catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
	             }
	         };
	         ThreadUtil.executeMore(runnable);         
	         return null;
	     }
	     
		private static Drawable loadImageFromPath(String imagePath, String themeId) {
			File pic = new File(imagePath);
			if (!pic.exists()) {
				ThemeFormart.createThemeThumbnail( BaseConfig.getApplicationContext(), themeId);
			}
			
			Drawable dw = null;
			try {
				dw = Drawable.createFromPath(imagePath);
				//文件存在但是读取出来为空的情况
				if (dw==null) {
					Log.e("AsyncImageLoader", "图片文件被损坏 null");
					//删除图片,等待下次重新下载
					FileUtil.delFile(imagePath);
				}
			} catch (OutOfMemoryError e) {
				Log.e("AsyncImageLoader", "Out of memory",e);
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dw;
		}
			
	     /**
	 	 * 本地化缓存
	 	 * 
	 	 * @param url
	 	 *            链接地址
	 	 */
	 	public static Drawable loadImageFromUrl(String url) {
	 		String path = url;
			if (url != null && url.toLowerCase().startsWith("http")) {
				path = StringHelp.url2path(url, BaseConfig.CACHES_HOME);
				File pic = new File(path);
				if (!pic.exists()) {
		 			if ( !FileUtil.downloadFileByURL(url, path) ) {
		 				return null;
		 			}
		 		}
			}
	 		
	 		Drawable dw = null;
	 		try {
	 			dw = Drawable.createFromPath(path);
	 			//文件存在但是读取出来为空的情况
	 			if (dw==null) {
	 				Log.e("AsyncImageLoader", "图片文件被损坏 null");
	 				//删除图片,等待下次重新下载
	 				FileUtil.delFile(path);
	 			}
	 		} catch (OutOfMemoryError e) {
				Log.e("AsyncImageLoader", "Out of memory",e);
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 		return dw;
	 	}

	  
	    public interface ImageCallback {
	       public void imageLoaded(Drawable imageDrawable, String imageUrl);
	    }	
	    
	    public void releaseImageCache(){
	    	if (imageCache==null) return;
	    	for (WeakReference<Drawable> weakReference : imageCache.values()) {
	             Drawable drawable = weakReference.get();
	             if (drawable != null) {
	            	 drawable.setCallback( null );
	             }
	    	}
	    	
	    	imageCache.clear();	    	 
	    }

}
