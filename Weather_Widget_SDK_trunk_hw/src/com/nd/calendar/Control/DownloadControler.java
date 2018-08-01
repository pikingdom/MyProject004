package com.nd.calendar.Control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import android.content.Context;
import android.text.TextUtils;

import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.FileHelp;

/**
 * @ClassName: DownHighPicControler
 * @Description: 下载高清天气图标
 * @author yanyy
 * @date 2012-12-9 上午10:57:56
 * 
 */
public class DownloadControler {
	protected Context mContext;
	protected File mDir;
	protected String mUrl = "";
	protected boolean mUseCalendarIco = false;
	protected String mZipFile = null;
	
	protected Stack<String> mStack = new Stack<String>();
	protected ArrayList<String> mTopTask = new ArrayList<String>();

	protected downWipThread mDownWipThread = null;

	public DownloadControler(Context ctx) {
		mContext = ctx.getApplicationContext();
		if (mContext == null) {
			mContext = ctx;
		}
		//mDir = getCacheDir();
	}

	protected int getCalendarVer() {
		return -1;
	}
	
	protected void finishDownload(String sPic, File file) {
		
	}
	
	
	public synchronized void startDown() {
		startThread();
	}

	public synchronized void addTopTask(String pic) {
		if (!TextUtils.isEmpty(pic)) {
			if (!mStack.contains(pic)) {
				mStack.add(0, pic);
			} else if (mStack.indexOf(pic) > 0) {
				mStack.remove(pic);
				mStack.add(0, pic);
			}
			if (!mTopTask.contains(pic)) {
				mTopTask.add(pic);
			}
			startThread();
		}
	}

	private void startThread() {
		if ((mDownWipThread != null) && (mDownWipThread.isAlive())
				&& (!mDownWipThread.isInterrupted())) {
			// 开启线程
			if (mDownWipThread.isPause()) {
				mDownWipThread.nofityThread();
			}
		} else {
			mDownWipThread = new downWipThread();
			mDownWipThread.start();
		}
	}

	private class downWipThread extends Thread {
		private boolean mIsPause = false;

		@Override
		public void run() {
			super.run();
			final boolean netAvailable = HttpToolKit.isNetworkAvailable(mContext);
			boolean hasCalendar = false;
			
			// 没网络并且没安装黄历天气
			if (!netAvailable
				&& !(mUseCalendarIco 
					/*&& (hasCalendar = ComfunHelp.checkApkExist(mContext, ComDataDef.CALENDAR_PACKAGE, getCalendarVer()))*/)) {
				pause();
				return;
			}
			
			while (!isInterrupted()) {
				mIsPause = false;
				if (!mStack.isEmpty()) {
					String pic = mStack.firstElement();
					try {
						if (pic != null) {
							File f = new File(mDir, pic + ".a");
							if (!f.exists()) {
								// 先到黄历天气取，没去到就去下载
								if (!mUseCalendarIco || !hasCalendar || !getImageFromCalendar(pic, f)) {
									if (netAvailable) { // 有网络
										downImageFromServer(pic, f);
									}
								}

								if ((f.exists()) && (mTopTask.contains(pic))) {
									finishDownload(pic, f);
								}
							}
						}
					} finally {
						if (mStack.contains(pic)) {
							mStack.remove(pic);
						}
					}
				} else {
					pause();
				}
			}
		}

		public void pause() {
			synchronized (this) {
				// 休眠等待 下次唤醒
				mIsPause = true;
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void nofityThread() {
			synchronized (this) {
				mIsPause = false;
				this.notify();
			}
		}

		public boolean isPause() {
			return mIsPause;
		}

		/**
		 * 从服务器下载
		 * 
		 * @param pic
		 * @param saveFile
		 */
		private void downImageFromServer(String pic, File saveFile) {
			// 下载(放临时文件)
			File downf = new File(mDir, pic + "." + UUID.randomUUID().toString());
			try {
				FileHelp.DeleteFile(downf);
				StringBuilder sb = new StringBuilder();
				sb.append(mUrl).append(pic).append(".png");
				boolean bRet = HttpToolKit.saveToFile(mContext, sb.toString(), downf.getAbsolutePath());
				if ((bRet) && (downf.exists())) {
					// 下载完成文件名改正正式文件
					downf.renameTo(saveFile);
				} else {
					// 下载失败删除残留
					FileHelp.DeleteFile(downf);
				}
			} catch (Exception e) {
				FileHelp.DeleteFile(downf);
			}
		}

		/**
		 * 从黄历天气读取图片
		 * 
		 * @param resName
		 * @param saveFile
		 * @return
		 */
		private boolean getImageFromCalendar(String resName, File saveFile) {
			InputStream is = null;
			OutputStream out = null;
			try {
				Context ctx = mContext.createPackageContext(
						ComDataDef.CALENDAR_PACKAGE,
						Context.CONTEXT_IGNORE_SECURITY);
				if (ctx != null) {
					int resId = ComfunHelp.getResIdByName(ctx, resName);
					if (resId > 0) {
						is = ctx.getResources().openRawResource(resId);
						if (saveFile.exists()) {
							saveFile.delete();
						}
						out = new FileOutputStream(saveFile);
						byte buf[] = new byte[1024];
						int len;
						while ((len = is.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						return true;
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
			}
			return false;
		}

	}

}
