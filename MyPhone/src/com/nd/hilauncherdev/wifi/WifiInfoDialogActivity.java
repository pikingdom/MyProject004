package com.nd.hilauncherdev.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.wifi.WifiHelper.PskType;

/**
 * 
 * @ClassName: WifiInfoDialogActivity
 * @Description: 输入wifi连接信息对话框
 * @author linyt
 * @date 2013-3-7 下午5:31:45
 * 
 */
public class WifiInfoDialogActivity extends HiActivity implements OnClickListener {

	private EditText wifi_passwd;
	/**
	 * 切换显示明文密码
	 */
	private CheckBox wifi_showpasswd_checkbox;
	private View wifi_ip_assignment_line;
	private TextView wifi_ip_assignment_txt;
	private EditText wifi_ip;
	private EditText wifi_gateway;
	private EditText wifi_prefix;
	private EditText wifi_dns1;
	private EditText wifi_dns2;
	private TextView cancle_btn;
	private TextView connect_btn;
	private ImageView wifi_ip_assignment;
	private View wifi_ip_static_layout;
	private boolean isStaticIp = false;
	private boolean isZh = true;
	private WifiManager mWifiManager;
	private String ssid;
	private String capabilities;
	private PskType pskType;
	/**
	 * 根据android版本是否高于2.3，决定是否显示切换静态动态ip的layout
	 */
	private boolean showline = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_info_input);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		isZh = TelephoneUtil.isZh(this);
		if (Build.VERSION.SDK_INT > 10) {
			showline = true;
		} else {
			showline = false;
		}
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		ssid = intent.getStringExtra("ssid");
		setTitle(ssid);
		capabilities = intent.getStringExtra("capabilities");
		pskType = WifiHelper.getSecurityType(capabilities);
		wifi_ip = (EditText) findViewById(R.id.wifi_ip);
		wifi_passwd = (EditText) findViewById(R.id.wifi_passwd);
		wifi_showpasswd_checkbox = (CheckBox) findViewById(R.id.wifi_showpasswd_checkbox);
		wifi_ip_assignment_line = findViewById(R.id.wifi_ip_assignment_line);
		wifi_ip_assignment_line.setVisibility(showline ? View.VISIBLE : View.GONE);
		wifi_gateway = (EditText) findViewById(R.id.wifi_gateway);
		wifi_prefix = (EditText) findViewById(R.id.wifi_prefix);
		wifi_dns1 = (EditText) findViewById(R.id.wifi_dns1);
		wifi_dns2 = (EditText) findViewById(R.id.wifi_dns2);

		cancle_btn = (TextView) findViewById(R.id.wifi_button_cancle);
		cancle_btn.setOnClickListener(this);
		connect_btn = (TextView) findViewById(R.id.wifi_button_connect);
		connect_btn.setOnClickListener(this);

		wifi_ip_assignment_txt = (TextView) findViewById(R.id.wifi_ip_assignment_txt);
		wifi_ip_assignment = (ImageView) findViewById(R.id.wifi_ip_assignment);
		wifi_ip_assignment.setImageResource(isZh ? R.drawable.wifi_dynamic_zh : R.drawable.wifi_dynamic_en);
		wifi_ip_static_layout = findViewById(R.id.wifi_ip_static_layout);
		wifi_ip_assignment.setOnClickListener(this);
		wifi_ip_assignment_line.setOnClickListener(this);
		wifi_showpasswd_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				wifi_passwd.setInputType(InputType.TYPE_CLASS_TEXT | (isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.wifi_ip_assignment || id == R.id.wifi_ip_assignment_line) {
			if (!wifi_ip_static_layout.isShown()) {
				wifi_ip_static_layout.setVisibility(View.VISIBLE);
				isStaticIp = true;
				wifi_ip_assignment.setImageResource(isZh ? R.drawable.wifi_static_zh : R.drawable.wifi_static_en);
			} else {
				wifi_ip_static_layout.setVisibility(View.GONE);
				isStaticIp = false;
				wifi_ip_assignment.setImageResource(isZh ? R.drawable.wifi_dynamic_zh : R.drawable.wifi_dynamic_en);
			}
			wifi_ip_assignment_txt.setText(isStaticIp ? R.string.wifi_static : R.string.wifi_dynamics);
		} else if (id == R.id.wifi_button_cancle) {
			finish();
		} else if (id == R.id.wifi_button_connect) {
			WifiConnect wc = new WifiConnect(mWifiManager);
			WifiBean bean = new WifiBean();
			bean.ssid=ssid;
			bean.capabilities=capabilities;
			bean.passwd=wifi_passwd.getText().toString();
			bean.pskType=pskType;
			bean.isStatic = isStaticIp;
			if (isStaticIp) {
				if (!invalidIp()) {
					return;
				}
				bean.ip = wifi_ip.getText().toString();
				bean.gateway = wifi_gateway.getText().toString();
				bean.networkPrefix= transformNetworkPrefix(wifi_prefix.getText().toString());
				bean.dns1 = wifi_dns1.getText().toString();
				bean.dns2 = wifi_dns2.getText().toString();
			}
			wc.connect(bean);
			finish();
		}
	}

	private int transformNetworkPrefix(String len) {
		int prefix_length = 24;
		if (null == len || "".equals(len)) {
			return prefix_length;
		}
		try {
			prefix_length = Integer.valueOf(len);
		} catch (Exception e) {
		}
		return prefix_length;
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
		if (!WifiHelper.isValidIp(wifi_ip.getText().toString())) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_ip_address)));
			return false;
		}
		if (!WifiHelper.isValidIp(wifi_gateway.getText().toString())) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_gateway_address)));
			return false;
		}
		int len = transformNetworkPrefix(wifi_prefix.getText().toString());
		if (len < 0 || len > 32) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_prefix_length)));
			return false;
		}
		if (!WifiHelper.isValidIp(wifi_dns1.getText().toString())) {
			MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_dns1_address)));
			return false;
		}
		String dns2 = wifi_dns2.getText().toString();
		if (!"".equals(dns2)) {
			if (!WifiHelper.isValidIp(dns2)) {
				MessageUtils.makeShortToast(this, String.format(getString(R.string.wifi_ip_faild), getString(R.string.wifi_dns2_address)));
				return false;
			}
		}
		return true;
	}
}
