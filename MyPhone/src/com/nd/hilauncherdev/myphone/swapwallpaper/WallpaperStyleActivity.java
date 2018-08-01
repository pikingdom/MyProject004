package com.nd.hilauncherdev.myphone.swapwallpaper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.view.commonview.HeaderView;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.MyPhoneUtil;
import com.nd.hilauncherdev.kitset.util.TabContainerUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.SwapWallpaperSettingUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.WallPaperUtil;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.SwapWallpaperSettingUtil.WallpaperTypeBean;
import com.nd.hilauncherdev.myphone.swapwallpaper.util.WallpaperTool;
import com.nd.hilauncherdev.myphone.swapwallpaper.wpaperinterface.GetWallpaperTypeListener;

/**
 * 壁纸风格
 * @author fmj
 */
public class WallpaperStyleActivity extends Activity implements GetWallpaperTypeListener{

	private Context context;
	private HeaderView mHeaderView;
	private ListView wallpaperList;
	private WallpaperTypeAdapter typeAdapter;
	private static final String SP_TIME = "sp_time";
    private SwapWallpaperSettingUtil settingUtil = null;
    private WallpaperTool wallpaperTool = null;
    private List<WallpaperTypeBean> typeList = new ArrayList<WallpaperTypeBean>();
    private List<String> isCheckList = new ArrayList<String>();
    private List<WallpaperTypeBean> tempList = null;
    private static final int TIMING = 12 * 60 * 60 * 1000;
//    private static final int TIMING = 20 * 1000;
    private ProgressDialog progressDialog;
    public static final String SPLIT_STR = ",";
	
	private ViewGroup mainView;
	private View contentView;
	private View netBreakView;// 无网络提示view
    
    private final Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch (msg.what) {
			case 0:
				refreshAdapter();
				break;
			default:
				break;
			}
    	}
    };
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabContainerUtil.fullscreen(this);
		setContentView(R.layout.wallpaper_style);
		context = WallpaperStyleActivity.this;
		createManager();
		initData();
		initView();
		settingUtil.setGetWallpaperTypeListener(this);
		sendWallPaperTypeRequest();
	}
	
	
	private void createManager(){
		if(settingUtil == null){
			settingUtil = new SwapWallpaperSettingUtil(context);
		}
		if(wallpaperTool == null){
			wallpaperTool = WallpaperTool.getInstance();
		}
	}
	
	private void structData(){
		WallpaperTypeBean typeBean =  settingUtil.new WallpaperTypeBean();
		typeBean.typeId = SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID;
		typeBean.typeValue = context.getString(R.string.swap_wallpaper_type_random);
		typeList.add(typeBean);
	}
	
	private void sendWallPaperTypeRequest() {
		long time = settingUtil.getLong(SP_TIME, -1);
		if (time != -1 && System.currentTimeMillis() - time < TIMING)
			return;
		if (!WallPaperUtil.isNetworkAvailable(context)){
			return;
		}
		progressDialog.show();
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				settingUtil.sendWallpaperTypeRequest();
			}
		});
	}

	private void initData(){//0,2618,4574
		tempList = settingUtil.getOnLineWallpaperType();
		String str = settingUtil.getString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID);
		if(str.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)){
			isCheckList.add(str);
		}else{
			String typeId[] = str.split(SPLIT_STR);
			if(typeId.length > 0){
				for(String temp : typeId){
					isCheckList.add(temp);	
				}
			}
		}
		typeAdapter = new WallpaperTypeAdapter(context);
		typeAdapter.setData(tempList);
	}
	
	private void initView() {
		mainView = (ViewGroup) findViewById(R.id.ring_main_layout);
		contentView = findViewById(R.id.wallpaperList);
		if (!TelephoneUtil.isNetworkAvailable(context)) {
			// 无网络显示View
			netBreakView = ViewFactory.getNomalErrInfoView(context, null, ViewFactory.NET_BREAK_VIEW);
			Button btn = (Button)netBreakView.findViewById(R.id.framework_viewfactory_err_btn);
			btn.setTextColor(Color.BLACK);
			netBreakView.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.MATCH_PARENT);
			netBreakView.setLayoutParams(params);
			mainView.addView(netBreakView);
			contentView.setVisibility(View.GONE);
		}
				
		progressDialog = MyPhoneUtil.getComProDialog(context, true);
		mHeaderView = (HeaderView)findViewById(R.id.headerView); 
		mHeaderView.setTitle(getString(R.string.swap_wallpaper_style));
		mHeaderView.setTitleGravity(Gravity.CENTER);
		mHeaderView.setGoBackListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mHeaderView.setMenuVisibility(View.VISIBLE);
		mHeaderView.setMenuImageResource(R.drawable.common_setting);
		mHeaderView.setMenuListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WallpaperStyleActivity.this,SwapWallpaperSettingActivity.class);
				startActivity(intent);
				finish();
			}
		});
		wallpaperList = (ListView) findViewById(R.id.wallpaperList);
		wallpaperList.setAdapter(typeAdapter);
	}

	private void refreshAdapter(){
		typeAdapter.setData(typeList);
		typeAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StringBuffer typeIdBuffer = new StringBuffer();;
		if(isCheckList.size() > 0){
			for(int i = 0; i < isCheckList.size(); i++){
				typeIdBuffer.append(isCheckList.get(i));
				if(i != isCheckList.size() - 1){
					typeIdBuffer.append(SPLIT_STR);
				}
			}
		}
		settingUtil.setString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID,typeIdBuffer.toString());
	}
	

	/**
	 * 请求分类壁纸成功
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onGetWallPaperItemSuccess(Object object) {
		progressDialog.dismiss();
		settingUtil.setLong(SP_TIME, System.currentTimeMillis());
		if(typeList.size() > 0){
			typeList.clear();
		}
		typeList.addAll((List<WallpaperTypeBean>)object);
//		structData();
		settingUtil.savaOnLineWallpaperType(typeList);
		if(tempList == null || tempList.size() < 1){
			if(typeList != null && typeList.size() > 0)
		    //返回分类数据第一条为默认分类（编辑精选）
//			isCheckList.add(typeList.get(0).typeId);
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					loadStyleImage(handler);
				}
			});
		}
	}
	
	/**
	 * 下载壁纸类型logo
	 * @param handler
	 */
	private void loadStyleImage(Handler handler) {
		try {
			if(typeList.size() < 1)
				return;
			for (WallpaperTypeBean item : typeList) {
				String fileName = FileUtil.getFileName(item.url, true);
				String path = WallPaperUtil.getWPPicOneKeyStyleHome() + "/" + fileName;
				item.localPath = path;
				URL url = new URL(item.url);
				if(wallpaperTool.loadWallpaper(url, path)){
					handler.sendEmptyMessage(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 请求分类壁纸失败
	 */
	@Override
	public void onGetWallPaperItemFailed() {
		progressDialog.dismiss();
		if (tempList == null || tempList.size() < 1) {
			SwapWallpaperSettingUtil.showToast(context, getString(R.string.swap_wallpaper_request_wallpaper_type_fail));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (progressDialog != null && progressDialog.isShowing()){
				progressDialog.dismiss();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public class WallpaperTypeAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<WallpaperTypeBean> wallpaperTypeList;
		private Context ctx;

		public WallpaperTypeAdapter(Context context) {
			this.ctx = context;
			inflater = LayoutInflater.from(ctx);
			if(wallpaperTool == null){
				wallpaperTool = WallpaperTool.getInstance();
			}
		}
		
		public void setData(List<WallpaperTypeBean> list){
			this.wallpaperTypeList = list;
		}

		@Override
		public int getCount() {
			if (wallpaperTypeList != null && wallpaperTypeList.size() > 0) {
				return wallpaperTypeList.size();
			}
			return 0;
		}

		@Override
		public WallpaperTypeBean getItem(int position) {
			if (wallpaperTypeList != null && wallpaperTypeList.size() > 0) {
				return wallpaperTypeList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.wallpaper_style_item, null);
				holder = new ViewHolder();
				holder.itemLayout = (RelativeLayout)convertView.findViewById(R.id.itemLayout);
				holder.style_img = (ImageView)convertView.findViewById(R.id.style_img);
				holder.typeTitle = (TextView)convertView.findViewById(R.id.dialog_select_item_desc);
				holder.checkIv = (CheckBox)convertView.findViewById(R.id.isCheck);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final WallpaperTypeBean item = wallpaperTypeList.get(position);
			if(item.localPath == null){
				String fileName = FileUtil.getFileName(item.url, true);
				item.localPath = WallPaperUtil.getWPPicOneKeyStyleHome() + "/" + fileName;
			}
			Bitmap bm = BitmapFactory.decodeFile(item.localPath);  
			if(bm != null){
				holder.style_img.setImageBitmap(bm);
			}
			holder.typeTitle.setText(item.typeValue);
			if (isCheckList.contains(item.typeId)) {
				holder.checkIv.setChecked(true);
			} else {
				holder.checkIv.setChecked(false);
			}
			holder.itemLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isCheckList.contains(item.typeId)) {
						isCheckList.remove(item.typeId);
					} else {
						isCheckList.add(item.typeId);
					}
					notifyDataSetChanged();
				}
			});
			return convertView;
		}
	}
	
	public class ViewHolder {
		public RelativeLayout itemLayout;
		public ImageView style_img;
		public TextView typeTitle;
		public CheckBox checkIv;
	}
}
