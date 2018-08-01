package com.nd.hilauncherdev.kitset.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.ViewFactory;

/**
 * MyPhone工具类
 *
 * @author youy
 *
 */
public class MyPhoneUtil {
	public static String SDCART_PATH = "/mnt/sdcard/";

	/**
	 * 判断SD卡是否存在
	 *
	 * @return true:存在
	 */
	public static boolean isSdExsit() {
		boolean b1 = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
		File file = new File(SDCART_PATH);
		boolean b2 = file.exists();
		return b1 && b2;
	}

	/**
	 * 获取SD卡不存在提示View
	 *
	 * @param context
	 * @param parentView
	 * @return
	 */
	public static View getSdNotExistView(Context context, RelativeLayout parentView) {
		return ViewFactory.getNoDataInfoView(context, parentView, R.string.myphone_sd_isnot_exsit_title, R.string.myphone_sd_isnot_exsit_text);
	}

	/**
	 * 程序大小加载完毕回调接口
	 *
	 * @author Administrator
	 *
	 */
	public static interface ILoadSizeCompleted {
		public void onCompleted(Map<String, Long> map);
	}

	/**
	 * 内存条使用情况颜色背景
	 *
	 * @param view
	 * @param used
	 *            已经使用的
	 * @param total
	 *            总内存
	 */
	public static void setProprotionColor(View view, long used, long total) {
		long pcent = total == 0 ? 0 : (used * 100 / total);
		if (pcent < 50) {
			view.setBackgroundResource(R.drawable.storage_phone_clean_bg);
		} else if (pcent >= 50 && pcent <= 75) {
			view.setBackgroundResource(R.drawable.app_running_clean_bg2);
		} else {
			view.setBackgroundResource(R.drawable.app_running_clean_bg);
		}
	}

	/**
	 * 获取手机内部可用空间大小
	 *
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		StatFs innerStatFs = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = innerStatFs.getBlockSize();
		long availableBlocks = innerStatFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部空间大小
	 *
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		StatFs innerStatFs = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = innerStatFs.getBlockSize();
		long totalBlocks = innerStatFs.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取手机外部可用空间大小
	 *
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (isSdExsit()) {
			StatFs extralStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = extralStatFs.getBlockSize();
			long availableBlocks = extralStatFs.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return -1;
		}
	}

	/**
	 * 获取手机外部空间大小
	 *
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		if (isSdExsit()) {
			StatFs extralStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = extralStatFs.getBlockSize();
			long totalBlocks = extralStatFs.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return -1;
		}
	}

	/**
	 * 实际是否存在外部存储空间 S3手机 手机外部存储和内部存储共用一个空间（实际的外部存储空间为0）(目前只能根据这个差异性判断)
	 * 则应用不能移动到sd
	 *
	 * @author zhenghonglin
	 * @return
	 */
	public static boolean isExistExternalMemorySpace() {
		// 小米2手机 不支持app2sd 直接返回false
		if (TelephoneUtil.isMI2Moble()) {
			return false;
		}
		final long internalUsed = MyPhoneUtil.getTotalInternalMemorySize() - MyPhoneUtil.getAvailableInternalMemorySize();
		final long externalUsed = MyPhoneUtil.getTotalExternalMemorySize() - MyPhoneUtil.getAvailableExternalMemorySize();
		if (internalUsed == externalUsed) {
			return false;
		}
		return true;
	}

	public static Toast toast;

	/**
	 * 描述:
	 *
	 * @author linqiang(866116)
	 * @Since 2012-10-23
	 * @param text
	 */
	public static void showOnlyToast(Context mContext, final String text) {
		if (toast == null) {
			toast = Toast.makeText(mContext.getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	/**
	 * 描述:
	 *
	 * @author linqiang(866116)
	 * @Since 2012-10-23
	 * @param text
	 */
	public static void showOnlyToast(Context mContext, final int text) {
		if (toast == null) {
			toast = Toast.makeText(mContext.getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}


	/**
	 * 获取提示框(我知道了)
	 *
	 * @param context
	 * @param text
	 *            内容
	 * @return
	 */
	public static Dialog getIKnowDialog(Context context, final String text) {

		Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog) {
			public TextView textView;
			public Button okBtn;

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.myphone_iknow_dialog_view);
				textView = (TextView) findViewById(R.id.app_running_dialog_text);
				textView.setText(text);
				okBtn = (Button) findViewById(R.id.app_running_dialog_btn);
				okBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View paramView) {
						dismiss();
					}
				});
			}
		};
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = (int) (ScreenUtil.getCurrentScreenWidth(context) * 0.9);
		params.height = ScreenUtil.dip2px(context, 200);
		params.gravity = 1;
		params.horizontalMargin = 0;
		dialog.getWindow().setAttributes(params);
		return dialog;
	}

	/**
	 * 数据加载中ProgressDialog
	 *
	 * @param context
	 * @param isCancel
	 *            按下返回，是否取消
	 * @return
	 */
	public static ProgressDialog getComProDialog(Context context, boolean isCancel) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getString(R.string.myphone_hint_loading));
		progressDialog.setCancelable(isCancel);
		return progressDialog;
	}

	public static ProgressDialog getComProDialog(Context context, String text, boolean isCancel) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(text);
		progressDialog.setCancelable(isCancel);
		return progressDialog;
	}

	/**
	 * 获取重新挂载/system的路径
	 *
	 * @Title: getMountPath
	 * @Description: TODO
	 * @param @return
	 */
	public static String getMountCode() {
		Process process = null;
		BufferedReader reader = null;
		String result = "";
		try {
			process = Runtime.getRuntime().exec("mount");
			process.waitFor();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/system")) {
					String[] str = line.split("\\s");
					if (null != str && str.length > 0) {
						for (int i = 0; i < str.length; i++) {
							if (str[i].contains("/dev")) {
								result = "mount -oremount,rw " + str[i] + " /system\n";
								break;
							}
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (process != null)
					process.destroy();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		Log.e("asd", "MountPath=" + result);
		return result;
	}

	/**
     * 用来判断服务是否运行.
     * @param context
     * @param pkgName 判断的应用的包名
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
	public static boolean isServiceRunning(Context mContext, String pkgName, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
		if ( serviceList.size()<=0 ) {
			return false;
		}
		String serPkgName = "";
		String serClassName = "";
		for (int i = 0; i < serviceList.size(); i++) {
			serPkgName = serviceList.get(i).service.getPackageName();
			serClassName = serviceList.get(i).service.getClassName();
			//Log.i("isServiceRunning", i+"**"+serClassName);
			if ( serPkgName.equals(pkgName) && serClassName.equals(className) ) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 获取当前用户手机的具体状态
	 * 包括：no,wifi,2g,3g,4g,unknow
	 * <p>Title: getTelephoneConcreteNetworkState</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	private static final int NETWORK_CLASS_UNKNOWN = 0;
	private static final int NETWORK_CLASS_YD_2G = 1;
	private static final int NETWORK_CLASS_LT_2G = 2;
	private static final int NETWORK_CLASS_DX_2G = 3;
	private static final int NETWORK_CLASS_2G = 4;
	private static final int NETWORK_CLASS_YD_3G = 5;
	private static final int NETWORK_CLASS_LT_3G = 6;
	private static final int NETWORK_CLASS_DX_3G = 7;
	private static final int NETWORK_CLASS_3G = 8;
	private static final int NETWORK_CLASS_4G = 9;
	private static final int NETWORK_CLASS_WIFI = 10;
	private static final int NETWORK_CLASS_NO = 11;

	public static String getTelephoneConcreteNetworkStateString(Context context){
		switch (getTelephoneConcreteNetworkState(context)) {
		case NETWORK_CLASS_YD_2G:return "yd2g";
		case NETWORK_CLASS_LT_2G:return "lt2g";
		case NETWORK_CLASS_DX_2G:return "dx2g";
		case NETWORK_CLASS_2G:return "2g";
		case NETWORK_CLASS_YD_3G:return "yd3g";
		case NETWORK_CLASS_LT_3G:return "lt3g";
		case NETWORK_CLASS_DX_3G:return "dx3g";
		case NETWORK_CLASS_3G:return "3g";
		case NETWORK_CLASS_4G:return "4g";
		case NETWORK_CLASS_WIFI:return "wifi";
		case NETWORK_CLASS_NO:return "no";
		default:
			return "unknow";
		}
	}

	public static int getTelephoneConcreteNetworkState(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null){
			return NETWORK_CLASS_UNKNOWN;
		}
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {// 没网络
			return NETWORK_CLASS_NO;
		}
		String type = networkInfo.getTypeName();
		if ("WIFI".equals(type) || "wifi".equals(type)) {
			return NETWORK_CLASS_WIFI;
		} else if ("MOBILE".equals(type) || "mobile".equals(type)) {
			return getNetworkClass(networkInfo.getType());
		} else {
			return NETWORK_CLASS_UNKNOWN;
		}
	}

	private static int getNetworkClass(int networkType) {
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:// 联通2g
			return NETWORK_CLASS_LT_2G;
		case TelephonyManager.NETWORK_TYPE_EDGE:// 移动2g
			return NETWORK_CLASS_YD_2G;
		case TelephonyManager.NETWORK_TYPE_CDMA:// 电信2g
			return NETWORK_CLASS_DX_2G;
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NETWORK_CLASS_2G;
		case TelephonyManager.NETWORK_TYPE_UMTS:// 联通3g
		case TelephonyManager.NETWORK_TYPE_HSDPA:// 联通3g
			return NETWORK_CLASS_LT_3G;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信3g
		case TelephonyManager.NETWORK_TYPE_EVDO_A:// 电信3g
		case TelephonyManager.NETWORK_TYPE_EVDO_B:// 电信3g
			return NETWORK_CLASS_DX_3G;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:

		case 14:// TelephonyManager.NETWORK_TYPE_EHRPD
		case 15:// TelephonyManager.NETWORK_TYPE_HSPAP
			return NETWORK_CLASS_3G;
		case 13:// TelephonyManager.NETWORK_TYPE_LTE	LTE标准的4g
			return NETWORK_CLASS_4G;
		default:
			return NETWORK_CLASS_UNKNOWN;
		}
	}

	/**
	 * 自定义activity会出异常的手机
	 * <p>Title: isNoCustomActivityTitleExceptionTelephone</p>
	 * <p>Description: </p>
	 * @param context
	 * @return
	 * @author maolinnan_350804
	 */
	public static boolean isNoCustomActivityTitleExceptionTelephone(){
		String machineName = TelephoneUtil.getMachineName();
		if (machineName == null){
			return true;
		}
		if (machineName.contains("HOOW") ||				//宏为
		    machineName.contains("Doeasy") || machineName.contains("BIRD") || machineName.contains("Bird")	||//波导
		    machineName.contains("MOFUT")  ||		//美富通
		    machineName.contains("KliTON") ||	machineName.contains("KLITON")	||	machineName.contains("I868 LZ") ||//凯利通
		    machineName.contains("GXQ X5") ||		//高新奇
			machineName.contains("HISIKI G2") ||	//华世基
			machineName.contains("GETEK-A3") ||		//高科
			machineName.contains("BOHP") ||			//铂瓷
			machineName.contains("T200") ||			//糖葫芦
			machineName.contains("VIMOO M9") ||		//为美
			machineName.contains("3GNET_T2") ||		//庆邦
			machineName.contains("Tensent S8800")){	//腾信
			return false;
		}
		return true;
	}
	
	/**
	 * <br>Description: 合并数组
	 * <br>Author:caizp
	 * <br>Date:2016年4月19日上午11:19:54
	 * @param first
	 * @param second
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] first, T[] second) {
		if(null == first && null != second) {
			return second;
		} else if (null != first && null == second) {
			return first;
		} else if (null == first && null == second) {
			return null;
		}
		final int alen = first.length;  
	    final int blen = second.length;  
	    if (alen == 0) {  
	        return second;  
	    }  
	    if (blen == 0) {  
	        return first;  
	    }  
	    final T[] result = (T[]) java.lang.reflect.Array.  
	            newInstance(first.getClass().getComponentType(), alen + blen);  
	    System.arraycopy(first, 0, result, 0, alen);  
	    System.arraycopy(second, 0, result, alen, blen);  
	    return result;
	}
}
