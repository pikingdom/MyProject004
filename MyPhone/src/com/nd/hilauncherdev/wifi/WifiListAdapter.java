package com.nd.hilauncherdev.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;

public class WifiListAdapter extends BaseAdapter {
	private final Context context;
	private LayoutInflater inflater;
	private WifiUtil mWifiUtil;
	private WifiManager wifiManager;
	private WifiConnect wc;
	private List<WifiBean> data = new ArrayList<WifiBean>();
	private List<WifiConfiguration> configurations;
	private WifiConfiguration wifiConfiguration;
	private String current_use_ssid;
	private String current_speed;
	private String current_ip;
	/**
	 * 连接过的热点
	 */
	private Map<String, WifiConfiguration> usedPoint = new HashMap<String, WifiConfiguration>();
	/**
	 * 从未连接过的热点
	 */
	private Map<String, WifiBean> neverUsedPoint = new HashMap<String, WifiBean>();
	private static String NOT_IN_SCAPE = "";
	private static String DISCONNECT = "";
	private static String NOT_SAVE = "";
	private static String CONNECT = "";
	private static String RETURN = "";
	private static String TIP_TYPE0 = "";
	private static String TIP_TYPE1 = "";
	private static String TIP_TYPE2 = "";
	private static String TIP_TYPE3 = "";
	private static String TIP_TYPE4 = "";

	public WifiListAdapter(Context context, List<WifiBean> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
		mWifiUtil = WifiUtil.getInstance(context);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wc = new WifiConnect(wifiManager);
		NOT_IN_SCAPE = context.getString(R.string.wifi_not_in_scape);
		DISCONNECT = context.getString(R.string.wifi_disconnect);
		NOT_SAVE = context.getString(R.string.wifi_forget_point);
		CONNECT = context.getString(R.string.wifi_connect);
		RETURN = context.getString(R.string.common_button_return);
		TIP_TYPE0 = context.getString(R.string.wifi_point_type0);
		TIP_TYPE1 = context.getString(R.string.wifi_point_type1);
		TIP_TYPE2 = context.getString(R.string.wifi_point_type2);
		TIP_TYPE3 = context.getString(R.string.wifi_point_type3);
		TIP_TYPE4 = context.getString(R.string.wifi_point_type4);
		refreshWifiData();
	}

	public void setData(List<WifiBean> data) {
		this.data = data;
		refreshWifiData();
	}

	private void refreshWifiData() {
		current_use_ssid = WifiHelper.dealWithSSID(mWifiUtil.getSSID());
		current_speed = mWifiUtil.getLinkSpeed();
		current_ip = WifiHelper.intIpToStringIp(mWifiUtil.getIpAddress());
		configurations = mWifiUtil.getConfiguration();
		//		Log.e("asd", "current_use_ssid=" + current_use_ssid);
		usedPoint.clear();
		neverUsedPoint.clear();
		for (Iterator<WifiConfiguration> iterator = configurations.iterator(); iterator.hasNext();) {
			wifiConfiguration = iterator.next();
			usedPoint.put(WifiHelper.dealWithSSID(wifiConfiguration.SSID), wifiConfiguration);
			// Log.e("asd", "Config ssid=" + wifiConfiguration.SSID + ",status="
			// + wifiConfiguration.status + ",networkid=" +
			// wifiConfiguration.networkId);
			// + ",allowedAuthAlgorithms="
			// + wifiConfiguration.allowedAuthAlgorithms
			// + ",allowedGroupCiphers="
			// + wifiConfiguration.allowedGroupCiphers
			// + ",allowedKeyManagement="
			// + wifiConfiguration.allowedKeyManagement
			// + ",allowedPairwiseCiphers="
			// + wifiConfiguration.allowedPairwiseCiphers
			// + ",allowedProtocols=" + wifiConfiguration.allowedProtocols);
		}
		for (Iterator<WifiBean> iterator = data.iterator(); iterator.hasNext();) {
			WifiBean bean = iterator.next();
			// Log.e("asd", "sc ssid=" + bean.getSsid());
			if (!usedPoint.containsKey(bean.ssid)) {
				// Log.e("asd", "not login = " + bean);
				neverUsedPoint.put(bean.ssid, bean);
			}
		}
	}

	@Override
	public int getCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public Object getItem(int paramInt) {
		return null;
	}

	@Override
	public long getItemId(int paramInt) {
		return 0;
	}

	@Override
	public View getView(int pos, View converView, ViewGroup paramViewGroup) {
		final ViewHolder holder;
		if (converView == null) {
			converView = inflater.inflate(R.layout.wifi_list_item, null);
			holder = new ViewHolder();
			holder.wifi_item = (View) converView.findViewById(R.id.wifi_item);
			holder.wifi_flag_icon = (ImageView) converView.findViewById(R.id.wifi_flag_icon);
			holder.wifi_key_lock = (ImageView) converView.findViewById(R.id.wifi_key_lock);
			holder.wifi_ssid = (TextView) converView.findViewById(R.id.wifi_ssid);
			holder.wifi_info = (TextView) converView.findViewById(R.id.wifi_info);
			holder.wifi_connect_img = (ImageView) converView.findViewById(R.id.wifi_connect_img);
			holder.wifi_connect_txt = (TextView) converView.findViewById(R.id.wifi_connect_txt);
			holder.wifi_state_layout = converView.findViewById(R.id.wifi_state_layout);

			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		final WifiBean info = data.get(pos);
		holder.wifi_ssid.setText(info.ssid);
		//在范围中
		if (info.type == WifiBean.TYPE_SCANRESULT) {
			//正在使用
			if (info.ssid.equals(current_use_ssid)) {
				holder.wifi_info.setText(String.format(TIP_TYPE1, current_speed));
				holder.wifi_state_layout.setVisibility(View.VISIBLE);
				holder.wifi_connect_img.setVisibility(View.VISIBLE);
				holder.wifi_connect_txt.setVisibility(View.GONE);
				holder.wifi_key_lock.setVisibility(View.GONE);
			} else {
				//连接过
				if (usedPoint.containsKey(info.ssid)) {
					holder.wifi_info.setText(TIP_TYPE0 + String.format(TIP_TYPE2, WifiHelper.changeName(context, WifiHelper.getKeyMgmt(info.capabilities)), mWifiUtil.getRssi(info.level).getName(context)));
					holder.wifi_state_layout.setVisibility(View.VISIBLE);
					holder.wifi_connect_img.setVisibility(View.GONE);
					holder.wifi_connect_txt.setVisibility(View.VISIBLE);
					holder.wifi_key_lock.setVisibility(View.GONE);
				}
				//未连接过
				else {
					holder.wifi_info.setText(String.format(TIP_TYPE2, WifiHelper.changeName(context, WifiHelper.getKeyMgmt(info.capabilities)), mWifiUtil.getRssi(info.level).getName(context)));
					holder.wifi_state_layout.setVisibility(View.GONE);
					holder.wifi_key_lock.setVisibility(View.VISIBLE);
				}
			}
		}
		//不在范围中
		else {
			holder.wifi_info.setText(NOT_IN_SCAPE);
			holder.wifi_state_layout.setVisibility(View.GONE);
		}
		holder.wifi_flag_icon.setImageResource(mWifiUtil.getRssi(info.level).getIconRes(context));
		holder.wifi_item.setTag(R.id.wifi_item, info);
		holder.wifi_state_layout.setTag(info);
		holder.wifi_state_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final WifiBean bean = (WifiBean) v.getTag();
				final String ssid = bean.ssid;
				mWifiUtil.startScan();
				configurations = mWifiUtil.getConfiguration();
				for (Iterator<WifiConfiguration> iterator = configurations.iterator(); iterator.hasNext();) {
					wifiConfiguration = iterator.next();
					String w_ssid = WifiHelper.dealWithSSID(wifiConfiguration.SSID);
					if (ssid.equals(w_ssid)) {
						// mWifiUtil.connetionConfiguration(wifiConfiguration.networkId);
						WifiConfiguration cc = wc.IsExsits(w_ssid);
						wc.connect(cc);
					}
				}
			}
		});
		holder.wifi_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final WifiBean bean = (WifiBean) v.getTag(R.id.wifi_item);
				final String ssid = bean.ssid;
				Dialog.OnClickListener no_save_listen = new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						wifiManager.removeNetwork(bean.networkId);
					}
				};

				// 从来没使用过的热点，弹出输入对话框
				if (neverUsedPoint.containsKey(ssid)) {
					Intent intent = new Intent(context, WifiInfoDialogActivity.class);
					intent.putExtra("ssid", ssid);
					intent.putExtra("capabilities", info.capabilities);
					intent.putExtra("level", info.level);
					context.startActivity(intent);
				} else {
					// 点击正在使用的连接
					// 返回 不保存 断开
					if (ssid.equals(current_use_ssid)) {
						WifiHelper.showActionDialog(context, 1, android.R.drawable.btn_star, ssid, String.format(TIP_TYPE3, WifiHelper.changeName(context, WifiHelper.getKeyMgmt(info.capabilities)), current_speed, mWifiUtil.getRssi(info.level).getName(context), current_ip), RETURN, new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}, DISCONNECT, new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								wifiManager.disableNetwork(bean.networkId);
							}
						}, NOT_SAVE, no_save_listen);
						return;
					}
					// 点击不在使用范围的连接
					// 返回 不保存
					// ....修改

					if (bean.type == WifiBean.TYPE_WIFICONFIGURATION) {
						WifiHelper.showActionDialog(context, 1, android.R.drawable.btn_star, ssid, context.getString(R.string.wifi_not_in_scape_tip), RETURN, new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}, NOT_SAVE, no_save_listen);
						return;
					}
					// 点击不在使用的连接，但是可用的
					// 返回 不保存 连接

					WifiHelper.showActionDialog(context, 1, android.R.drawable.btn_star, ssid, String.format(TIP_TYPE4, WifiHelper.changeName(context, WifiHelper.getKeyMgmt(info.capabilities)), mWifiUtil.getRssi(info.level).getName(context)), RETURN, new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, CONNECT, new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mWifiUtil.startScan();
							configurations = mWifiUtil.getConfiguration();
							for (Iterator<WifiConfiguration> iterator = configurations.iterator(); iterator.hasNext();) {
								wifiConfiguration = iterator.next();
								String w_ssid = WifiHelper.dealWithSSID(wifiConfiguration.SSID);
								if (ssid.equals(w_ssid)) {
									// mWifiUtil.connetionConfiguration(wifiConfiguration.networkId);
									WifiConfiguration cc = wc.IsExsits(w_ssid);
									wc.connect(cc);
								}
							}
						}
					}, NOT_SAVE, no_save_listen);
				}
			}
		});

		return converView;
	}

	class ViewHolder {
		public View wifi_item;
		public ImageView wifi_flag_icon;
		public ImageView wifi_key_lock;
		public TextView wifi_ssid;
		public TextView wifi_info;
		public View wifi_state_layout;
		public ImageView wifi_connect_img;
		public TextView wifi_connect_txt;
	}

}
