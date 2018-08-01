package com.nd.hilauncherdev.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.nd.android.pandahome2.shop.R;
import com.nd.hilauncherdev.framework.view.CustomProgressDialog;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;

/**
 * 显示应用详情的PopWindwo Title: DrawerSearchAppDetailPopWindow Description: Company:
 * ND
 * 
 * @author MaoLinnan
 * @date 2014年12月15日
 */
public class ShopRecommendAppDetailPopupWindow {
	private final int SHOW_DRAWER_SEARCH_APPDETAIL_POPUPWINDOW = 1;// 显示详情PopupWindow
	private final int DISMISS_PROGRESSDIALOG = 2;// 关闭加载框的显示
	private final int NETWORK_ERROR = 3;// 网络连接错误
	public static String packageName = "";
	private Context context;
	private PopupWindow popupWindow;
	private ProgressDialog progressDialog;
	private ShopRecommendAppDetailView drawerSearchAppDetailView;
	private View parentView;
	private SoftDetialBean softDetialBean;

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DRAWER_SEARCH_APPDETAIL_POPUPWINDOW:
				drawerSearchAppDetailView.requestUpdateDrawerSearchAppDetailView(softDetialBean);
				// 透明通知栏要下移高度
				int statusBarHeight = ScreenUtil.dip2px(context, ScreenUtil.getNotificationHeight());
				popupWindow.showAsDropDown(parentView, 0, statusBarHeight - ScreenUtil.getCurrentScreenHeight(context));
//				popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, statusBarHeight);
				popupWindow.update();
				break;
			case DISMISS_PROGRESSDIALOG:
				progressDialog.dismiss();
				break;
			case NETWORK_ERROR:
				progressDialog.dismiss();
				MessageUtils.showOnlyToast(context, R.string.frame_viewfacotry_net_slowly_text);
				break;
			}
			return false;
		}
	});

	public ShopRecommendAppDetailPopupWindow(Context context) {
		this.context = context;
		drawerSearchAppDetailView = new ShopRecommendAppDetailView(context, this);
		popupWindow = new PopupWindow(drawerSearchAppDetailView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		progressDialog = new CustomProgressDialog(context);
		progressDialog.setMessage(context.getString(R.string.searchbox_hotword_detail_info_loading_content));
		progressDialog.show();
	}

	/**
	 * 显示应用详情PopupWindow Title: showDrawerSearchAppDetailPopupWindow
	 * Description:
	 * 
	 * @param context
	 * @param packageName
	 *            包名
	 * @param view
	 *            父视图（对齐PopupWindow用）
	 * @author maolinnan_350804
	 */
	public void showDrawerSearchAppDetailPopupWindow(final String packageName, View view) {
		if(packageName == null || "".equals(packageName) || view == null){
			return;
		}
		this.parentView = view;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		ShopRecommendAppDetailPopupWindow.packageName = packageName;
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				softDetialBean = SoftDetialBean.getSoftDetialBean(context, packageName);// 这个操作要放到线程中，切记。
				if (softDetialBean == null) {
					handler.sendEmptyMessage(NETWORK_ERROR);
					return;
				}
				if (ShopRecommendAppDetailPopupWindow.packageName.equals(packageName)) {
					handler.sendEmptyMessage(DISMISS_PROGRESSDIALOG);
					handler.sendEmptyMessage(SHOW_DRAWER_SEARCH_APPDETAIL_POPUPWINDOW);
				}
			}
		});
	}
	/**
	 * 自带APK下载地址的详情
	 * <p>Title: showDrawerSearchAppDetailPopupWindow</p>
	 * <p>Description: </p>
	 * @param packageName
	 * @param apkDownloadUrl
	 * @param view
	 * @author maolinnan_350804
	 */
	public void showDrawerSearchAppDetailPopupWindow(final String packageName,final String apkDownloadUrl,View view){
		if(packageName == null || "".equals(packageName) || view == null){
			return;
		}
		this.parentView = view;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		ShopRecommendAppDetailPopupWindow.packageName = packageName;
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				softDetialBean = SoftDetialBean.getSoftDetialBean(context, packageName);// 这个操作要放到线程中，切记。
				if (softDetialBean == null) {
					handler.sendEmptyMessage(NETWORK_ERROR);
					return;
				}
				if (!"".equals(apkDownloadUrl) && apkDownloadUrl != null){
					softDetialBean.downloadUrl = apkDownloadUrl;
				}
				if (ShopRecommendAppDetailPopupWindow.packageName.equals(packageName)) {
					handler.sendEmptyMessage(DISMISS_PROGRESSDIALOG);
					handler.sendEmptyMessage(SHOW_DRAWER_SEARCH_APPDETAIL_POPUPWINDOW);
				}
			}
		});
	}
	
	/**
	 * 关闭应用详情 Title: cancelDrawerSearchAppDetailPopupWindow Description:
	 * 
	 * @author maolinnan_350804
	 */
	public void cancelDrawerSearchAppDetailPopupWindow() {
		handler.removeMessages(DISMISS_PROGRESSDIALOG);
		handler.removeMessages(SHOW_DRAWER_SEARCH_APPDETAIL_POPUPWINDOW);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	public PopupWindow getAppDetailPopupWindow() {
		return popupWindow;
	}
}
