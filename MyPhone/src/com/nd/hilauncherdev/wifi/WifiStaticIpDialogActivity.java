package com.nd.hilauncherdev.wifi;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.kitset.util.MessageUtils;

public class WifiStaticIpDialogActivity extends HiActivity implements OnClickListener {

	private EditText wifi_ip;
	private EditText wifi_gateway;
	private EditText wifi_netmask;
	private EditText wifi_dns1;
	private EditText wifi_dns2;
	private String ip;
	private String gateway;
	private String netmask;
	private String dns1;
	private String dns2;
	private TextView cancle_btn;
	private TextView save_btn;
	private final static String DEFAULT_DNS = "8.8.8.8";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wifi_high_info_input);
		initView();
	}

	private void initView() {
		wifi_ip = (EditText) findViewById(R.id.wifi_ip);
		wifi_gateway = (EditText) findViewById(R.id.wifi_gateway);
		wifi_netmask = (EditText) findViewById(R.id.wifi_netmask);
		wifi_dns1 = (EditText) findViewById(R.id.wifi_dns1);
		wifi_dns2 = (EditText) findViewById(R.id.wifi_dns2);
		cancle_btn = (TextView) findViewById(R.id.wifi_button_cancle);
		cancle_btn.setOnClickListener(this);
		save_btn = (TextView) findViewById(R.id.wifi_button_save);
		save_btn.setOnClickListener(this);
		initIpData();
	}

	private void initIpData() {
		ip = Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_IP);
		gateway = Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_GATEWAY);
		netmask = Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_NETMASK);
		dns1 = Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_DNS1);
		dns2 = Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_DNS2);
		if (null==dns2 || DEFAULT_DNS.equals(dns2)) {
			dns2 = "";
		}
		wifi_ip.setText(ip);
		wifi_gateway.setText(gateway);
		wifi_netmask.setText(netmask);
		wifi_dns1.setText(dns1);
		wifi_dns2.setText(dns2);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.wifi_button_save) {
			ip = wifi_ip.getText().toString();
			gateway = wifi_gateway.getText().toString();
			netmask = wifi_netmask.getText().toString();
			dns1 = wifi_dns1.getText().toString();
			dns2 = wifi_dns2.getText().toString();
			if (dns2 == null || "".equals(dns2.trim())) {
				dns2 = DEFAULT_DNS;
			}
			boolean res = invalidIp();
			if (!res) {
				return;
			}

			Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_IP, ip);
			Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_GATEWAY, gateway);
			Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_NETMASK, netmask);
			Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_DNS1, dns1);
			Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_DNS2, dns2);
			setResult(Activity.RESULT_OK);
			finish();
		} else if (id == R.id.wifi_button_cancle) {
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	}

	/**
	 * 验证输入是否有效
	 * @Title: invIp 
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean invalidIp() {
		if (!WifiHelper.isValidIp(ip)) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_ip_address)));
			return false;
		}
		if (!WifiHelper.isValidIp(gateway)) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_gateway_address)));
			return false;
		}
		if (!WifiHelper.isValidIp(netmask)) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_netmask_address)));
			return false;
		}
		if (!WifiHelper.isValidIp(dns1)) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_dns1_address)));
			return false;
		}
		if (!WifiHelper.isValidIp(dns2)) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_dns2_address)));
			return false;
		}
		return true;
	}
}
