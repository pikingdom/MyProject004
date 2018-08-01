package com.nd.hilauncherdev.framework.view.commonview;

import android.content.Context;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.MyPhoneUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

public class BottomEditBoxLayout extends LinearLayout implements OnClickListener {

	public TextView sdCardTextView;
	// 编辑Btn
	private TextView myFileEditBtn;

	private CleanAnimationImageView sdMemoryImage;

	public LinearLayout myFileShowStatus;

	public LinearLayout myFileEditStatus;

	private LinearLayout selectAllBtn;

	private ImageView checkbox;

	private LinearLayout deleteBtn;

	private LinearLayout backBtn;

	private long unUsedSize;

	private Context context;

	private boolean isSelectAll = false;

	private BottomEditListener bottomEditListener;

	private View myFileLayoutProportion;

	public BottomEditBoxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BottomEditBoxLayout(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public Handler handler = new Handler();

	public void init() {
		sdCardTextView = (TextView) findViewById(R.id.sd_card_show);
		myFileEditBtn = (TextView) findViewById(R.id.myfile_edit_btn);
		myFileLayoutProportion = (View) findViewById(R.id.storage_layout_proportion);
		myFileShowStatus = (LinearLayout) findViewById(R.id.myfile_show_status);
		myFileEditStatus = (LinearLayout) findViewById(R.id.myfile_edit_status);
		sdMemoryImage = (CleanAnimationImageView) findViewById(R.id.sd_card_cleanimg);
		selectAllBtn = (LinearLayout) findViewById(R.id.myfile_edit_mode_select_all_btn);
		checkbox = (ImageView) findViewById(R.id.checkbox);

		deleteBtn = (LinearLayout) findViewById(R.id.myfile_edit_mode_delete_btn);
		backBtn = (LinearLayout) findViewById(R.id.myfile_edit_back_btn);

		this.refreshCapacityView();
		sdCardTextView.setOnClickListener(this);
		myFileEditBtn.setOnClickListener(this);
		selectAllBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	/**
	 * <p>说明: 更新底部显示sd卡容量视图的数据</p>
	 * @author Yu.F, 2013-9-12
	 */
	public void refreshCapacityView() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				String text = getResources().getText(R.string.storage_come_sdcard).toString();
				unUsedSize = MyPhoneUtil.getAvailableExternalMemorySize();
				long memoryTotal = MyPhoneUtil.getTotalExternalMemorySize();
				if (unUsedSize == -1 || memoryTotal == -1) {
					Toast.makeText(context, R.string.storage_sd_no_found_err, Toast.LENGTH_LONG).show();
					memoryTotal = 1;
					unUsedSize = 0;
				}
				sdCardTextView.setText(text + ":" + Formatter.formatFileSize(context, memoryTotal - unUsedSize) + " / " + Formatter.formatFileSize(context, memoryTotal));
				int totalWidth = sdCardTextView.getWidth();
				if (0 == totalWidth) {
					totalWidth = ScreenUtil.getCurrentScreenWidth(context);
				}
				int maskWidth = (int) (unUsedSize * totalWidth * 1.0 / memoryTotal);
				MyPhoneUtil.setProprotionColor(myFileLayoutProportion, memoryTotal - unUsedSize, memoryTotal);
				sdMemoryImage.setOffset(maskWidth);
				sdMemoryImage.setStartAnimation(false);
			}
		}, 500);
	}


	@Override
	public void onClick(View v) {
		if (bottomEditListener == null)
			return;
		if (v == sdCardTextView) {
			if (unUsedSize == 0) {
				Toast.makeText(context, R.string.storage_sd_no_found_err, Toast.LENGTH_LONG).show();
			} else {
//				MyFileUtil.showFileManager(context);
			}
		} else if (v == myFileEditBtn) {
//			HiAnalytics.submitEvent(context, AnalyticsConstant.MYPHONE_MYFILE_EDIT);
			bottomEditListener.editBtnStatus();
			editStatus();
			unSelectAll(true);
		} else if (v == selectAllBtn) {
			if (isSelectAll) {
				bottomEditListener.unselectAll();
				setChecked(false);
			} else {
				bottomEditListener.selectAll();
				setChecked(true);
			}

		} else if (v == deleteBtn) {
			bottomEditListener.deleteFile();
		} else if (v == backBtn) {
			bottomEditListener.back();
		}
	}

	/**
	 * <p>说明: 是否反选全部(含义不直接, 历史遗留代码)</p>
	 * @author Yu.F, 2013-8-14
	 * @param flag
	 */
	public void unSelectAll(boolean flag) {
		setChecked(!flag);
	}

	/**
	 * <p>说明: 返回全选checkbox的选中状态</p>
	 * @author Yu.F, 2013-9-12
	 * @return
	 */
	/*public boolean isSelectAll() {
		return isSelectAll;
	}
*/
	/**
	 * <p>说明: 不会产生全选/反选事件, 仅仅是UI更新</p>
	 * @author Yu.F, 2013-9-12
	 * @param isChecked
	 */
	/*public void setSelectAllChecked(boolean isChecked) {
		this.setChecked(isChecked);
	}*/

	/**
	 * 编辑状态
	 *
	 * @author Jimmy <br>
	 *         create at 2012-8-2 下午07:20:31 <br>
	 *         modify at 2012-8-2 下午07:20:31
	 */
	public void editStatus() {
		myFileEditStatus.setVisibility(View.VISIBLE);
		myFileShowStatus.setVisibility(View.GONE);
	}

	private void setChecked(boolean isChecked) {
		if (isChecked) {
			isSelectAll = true;
			checkbox.setImageResource(R.drawable.common_checkbox_checked);
		} else {
			isSelectAll = false;
			checkbox.setImageResource(R.drawable.common_checkbox_uncheck);
		}
	}

	/**
	 * SD卡显示状态
	 *
	 * @author Jimmy <br>
	 *         create at 2012-8-2 下午07:20:53 <br>
	 *         modify at 2012-8-2 下午07:20:53
	 */
/*	public void showStatus() {
		myFileEditStatus.setVisibility(View.GONE);
		myFileShowStatus.setVisibility(View.VISIBLE);
	}*/

/*	public void setBottomEditListener(BottomEditListener bottomEditListener) {
		this.bottomEditListener = bottomEditListener;
	}*/

	public interface BottomEditListener {
		public void unselectAll();

		public void selectAll();

		public void editBtnStatus();

		public void deleteFile();

		public void back();
	}
}
