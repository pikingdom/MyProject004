package com.nd.hilauncherdev.analysis.integral;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.shop.api6.net.ServerDetailResult;

/**
 * 积分提交工具
 * 有提供给CommonLibraryLimited反射调用，不要随便改位置和名称。
 * <p>
 * Title: IntegralSubmitUtil
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: ND
 * </p>
 * 
 * @author MaoLinnan
 * @date 2015年10月26日
 */
public class IntegralSubmitUtil {
	private static final int WM_REMOVE_VIEW = 0;
	
	private WindowManager wm;
	private IntegralSubmitAnimationLayout animationLayout;
	private Handler handler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == WM_REMOVE_VIEW){
				if (wm != null && animationLayout != null && animationLayout.getParent() != null){
					wm.removeView(animationLayout);
				}
			}
			return false;
		}
	});;
	/**
	 * 单例
	 */
	private static IntegralSubmitUtil integralSubmitUtil;

	public static IntegralSubmitUtil getInstance(Context context) {
		if (integralSubmitUtil == null) {
			integralSubmitUtil = new IntegralSubmitUtil(context);
		}
		return integralSubmitUtil;
	}

	private IntegralSubmitUtil(Context context) {
		wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	}

	public void submit(final Context context, final int taskId) {
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				ServerDetailResult<IntegralInfoBean> sdr = MemberintegralNetOptApiBase.getTaskInformationList_2(context, taskId + "");
				if (sdr != null && sdr.detailItem != null) {
					final IntegralInfoBean bean = sdr.detailItem;
					handler.post(new Runnable() {
						@Override
						public void run() {
							String describe = IntegralTaskIdContent.gettaskDescribe(context,bean.taskType);
							showIntegralToast(context, describe + context.getString(R.string.obtain_integral_remind), bean.getIntegrals);
						}
					});
				}else{//达到上限提醒
					switch (sdr.getCsResult().getResultCode()) {
					case 2005://已到达未登录设备的获取总积分上限
					case 2006://已到达每天未登录设备的获取总积分上限
						handler.post(new Runnable() {
							@Override
							public void run() {
								MessageUtils.showOnlyToast(context, R.string.obtain_integral_fail_tips1);
							}
						});
						break;
					case 2007://已到达每天账号的获取总积分上限
						handler.post(new Runnable() {
							@Override
							public void run() {
								MessageUtils.showOnlyToast(context, R.string.obtain_integral_fail_tips2);
							}
						});
						break;
					}
				}
			}
		});
	}
	/**
	 * 显示获得积分动画
	 * <p>Title: showOnlyToast</p>
	 * <p>Description: </p>
	 * @param context
	 * @param taskDescribe 积分显示文字
	 * @param integral 获得积分数
	 * @author maolinnan_350804
	 */
	public void showIntegralToast(Context context, String taskDescribe,int integral) {
		if (animationLayout == null){
			animationLayout = new IntegralSubmitAnimationLayout(context);
		}
		if (wm == null){
			return;
		}else{
			handler.removeMessages(WM_REMOVE_VIEW);
			if (animationLayout.getParent() != null){
				wm.removeView(animationLayout);
			}
		}
		//显示到界面
		LayoutParams wmParams = new LayoutParams();
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_TOUCHABLE | 0x01000000;
		wmParams.type = LayoutParams.TYPE_TOAST;
		wmParams.gravity = Gravity.BOTTOM; // 调整悬浮窗口在底部
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = LayoutParams.MATCH_PARENT;
		wmParams.height = ScreenUtil.dip2px(context, 150);
		wmParams.format = PixelFormat.RGBA_8888;
		wm.addView(animationLayout, wmParams);
		//启动toast
		animationLayout.showIntegralToast(taskDescribe, integral);
		//3秒后移除该toast
		handler.sendEmptyMessageDelayed(WM_REMOVE_VIEW, 3000);
	}
}
