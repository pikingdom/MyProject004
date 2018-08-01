package com.nd.hilauncherdev.kitset.util.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

/**
 * 时间选择器
 * 
 * @author youy
 * 
 */
public class TimePickerUtil {
	/**
	 * 时间选择器
	 * 
	 * @param context
	 * @param hour
	 * @param minute
	 */
	public static void showTimePickerDialog(final Context context, final int hour, final int minute, final TimePickerCallBack pickerTime) {
		Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog) {
			private WheelView hoursView;
			private WheelView minsView;

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.myphone_common_time_picker);
				ImageView hourAdd = (ImageView) findViewById(R.id.hour_add);
				ImageView hourMinus = (ImageView) findViewById(R.id.hour_minus);
				ImageView minAdd = (ImageView) findViewById(R.id.min_add);
				ImageView minMinus = (ImageView) findViewById(R.id.min_minus);

				hourAdd.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						hoursView.setCurrentItem(hoursView.getCurrentItem() + 1);
					}
				});
				hourMinus.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						hoursView.setCurrentItem(hoursView.getCurrentItem() - 1);
					}
				});
				minAdd.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						minsView.setCurrentItem(minsView.getCurrentItem() + 1);
					}
				});
				minMinus.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						minsView.setCurrentItem(minsView.getCurrentItem() - 1);
					}
				});

				/**
				 * 1280*800 26 960*540 20 854*480 24 480*320 10
				 */
				int height = ScreenUtil.getCurrentScreenHeight(context); // 屏幕高度（像素）
				int base = 20;
				switch (height) {
				case 1280:
					base = 26;
					break;
				case 960:
					base = 20;
					break;
				case 854:
					base = 24;
					break;
				case 480:
					base = 20;
					break;
				}
				// 时
				hoursView = (WheelView) findViewById(R.id.hour);
				hoursView.setAdapter(new NumericWheelAdapter(0, 23));
				hoursView.setCyclic(true);
				hoursView.setVisibleItems(3);
				hoursView.setItem_height(ScreenUtil.dip2px(context, base));

				// 分
				minsView = (WheelView) findViewById(R.id.mins);
				minsView.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
				minsView.setCyclic(true);
				minsView.setVisibleItems(3);
				minsView.setItem_height(ScreenUtil.dip2px(context, base));

				// 设置默认时间
				hoursView.setCurrentItem(hour);
				minsView.setCurrentItem(minute);

				// 根据屏幕密度来指定选择器字体的大小
				int textSize = 0;
				textSize = ScreenUtil.dip2px(context, 14);
				hoursView.TEXT_SIZE = textSize;
				minsView.TEXT_SIZE = textSize;

				Button btn_sure = (Button) findViewById(R.id.dialog_confirm);
				Button btn_cancel = (Button) findViewById(R.id.dialog_cancel);
				// 确定
				btn_sure.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						pickerTime.getTime(hoursView.getCurrentItem(), minsView.getCurrentItem());
						dismiss();
					}
				});
				// 取消
				btn_cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dismiss();
					}
				});
			}

		};
		dialog.show();
	}

	/**
	 * 选择器的回调
	 * 
	 * @author youy
	 * 
	 */
	public static interface TimePickerCallBack {
		public void getTime(int hour, int minute);
	}

	/**
	 * 时间转换成String格式 22:00
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static String format(int hour, int minute) {
		String hourStr = String.valueOf(hour);
		if (hourStr.length() == 1) {
			hourStr = "0" + hourStr;
		}
		String minuteStr = String.valueOf(minute);
		if (minuteStr.length() == 1) {
			minuteStr = "0" + minuteStr;
		}
		return (hourStr + ":" + minuteStr);
	}

	/**
	 * 字符转换时间
	 * 
	 * @param time
	 * @return int[]{hour,minute}
	 */
	public static int[] parse(String time) {
		int hour = 0;
		int minute = 0;
		if (time == null || time.indexOf(":") < 0) {
			return new int[] { hour, minute };
		}
		String[] array = time.split(":");
		if (array.length != 2) {
			return new int[] { hour, minute };
		}
		if (isIntNumber(array[0])) {
			hour = Integer.valueOf(array[0]);
		}
		if (isIntNumber(array[1])) {
			minute = Integer.valueOf(array[1]);
		}
		return new int[] { hour, minute };

	}

	/**
	 * 是否是整型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIntNumber(String str) {
		try {
			Integer.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
