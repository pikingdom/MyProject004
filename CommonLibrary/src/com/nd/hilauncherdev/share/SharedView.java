package com.nd.hilauncherdev.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonSlidingView;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.CommonSlidingViewData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonData;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.launcher.config.IconAndTextSizeConfig;

public class SharedView extends CommonSlidingView {


	private Context mContext;
	private CommonSlidingViewData data;
	private List<ICommonDataItem> commonDataItems = new ArrayList<ICommonDataItem>();
	private LayoutInflater layoutInflater;
//	private List<SharedItem> mSharedDataList = new ArrayList<SharedItem>();
//	private Map<String, SharedItem> mSharedDefaultMap = new HashMap<String, SharedItem>();
	private int iconSize;
	private static final int launcher_edit_cell_col = 3;
	private static final int launcher_edit_cell_row = 2;
	private LinearLayout.LayoutParams mLayoutParams;
	private static int SHARED_APP_ICON_SIZE = 60;

	public SharedView(Context context) {
		super(context);
		init(context);
	}

	public SharedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SharedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		SHARED_APP_ICON_SIZE = ScreenUtil.dip2px(mContext, 48);
		mLayoutParams = new LinearLayout.LayoutParams(SHARED_APP_ICON_SIZE, SHARED_APP_ICON_SIZE);
		mLayoutParams.gravity = Gravity.CENTER;
		initDefaultView();
		showShareDataView();
	}

	private void initDefaultView() {
		List<ICommonData> list = new ArrayList<ICommonData>();
		this.setList(list);
		iconSize = IconAndTextSizeConfig.getSmallIconSize(getContext());
		data = new CommonSlidingViewData(iconSize, iconSize, launcher_edit_cell_col, launcher_edit_cell_row, commonDataItems);
	}

	private ICommonData assembleSharedData() {
		data.setChildViewHeight((int) (2 * iconSize));
		data.setChildViewWidth((int) (2 * iconSize));
		data.setColumnNum(launcher_edit_cell_col);
		data.setRowNum(launcher_edit_cell_row);
		data.getDataList().clear();

        ShareTool tool = new ShareTool(mContext);

		data.getDataList().addAll(tool.getSharedDataList());
		return data;
	}

	private void showShareDataView() {
		this.getList().clear();
		ICommonData data = assembleSharedData();
		this.getList().add(data);
	}

	@Override
	protected void initSelf(Context context) {
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View onGetItemView(ICommonData data, int position) {
		View v = layoutInflater.inflate(R.layout.shared_pop_item, null);
		final SharedItem sharedItem = (SharedItem) data.getDataList().get(position);
		TextView itemTxt = (TextView) v.findViewById(R.id.shared_item_txt);
		ImageView itemIcon = (ImageView) v.findViewById(R.id.shared_item_icon);
		itemIcon.setLayoutParams(mLayoutParams);
		itemTxt.setText(sharedItem.getDesc());
		itemIcon.setImageDrawable(sharedItem.getIcon());
		return v;
	}
}