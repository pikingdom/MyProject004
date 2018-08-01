package com.nd.hilauncherdev.wifi;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.framework.view.commonview.MyphoneContainer;

public class WifiSetMainActivity extends HiActivity {
	protected static final int REFRESH_VIEW = 1;
	protected static final int WIFI_ENABLED = 2;
	protected static final int REQUEST_CODE = 1;
	private WifiManager mWifiManager;
	/** Called when the activity is first created. */
	private Context ctx;
	private ListView wifi_list;
	private WifiUtil mWifiUtil;
	private WifiListAdapter adapter;
	private List<WifiBean> list;
	private View wifi_static_set_layout;
	//	private ImageView wifi_static_icon;
	//	private ImageView wifi_static_input;
	private View wifiStaticBtn;
	private TextView wifiStaticText;
	private View wifiSetBtn;
	private boolean showline = false;
	private boolean isStatic = false;
	private Timer t;
	private MyphoneContainer container;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		container = new MyphoneContainer(this);
		setContentView(container);
		container.initContainer(this.getString(R.string.wifi_set), LayoutInflater.from(this).inflate(R.layout.wifi_list_main, null), MyphoneContainer.DEFALUT_THEME);
		container.setGoBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		setContentView(container);

		this.ctx = this;
		if (Build.VERSION.SDK_INT > 10) {
			showline = false;
		} else {
			showline = true;
		}
		init();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_VIEW:
				mWifiUtil.startScan();
				break;
			case WIFI_ENABLED:
				mWifiUtil.startScan();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		registerReceiver(receiver, filter);
		registerReceiver(scanCompletereceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
		unregisterReceiver(scanCompletereceiver);
	}

	public void init() {
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi_list = (ListView) findViewById(R.id.wifi_list);
		mWifiUtil = WifiUtil.getInstance(ctx);
		
		wifi_static_set_layout = (View) findViewById(R.id.wifi_static_set_layout);
		wifi_static_set_layout.setVisibility(showline ? View.VISIBLE : View.GONE);
		
		if (showline) {
			isStatic = isStaticIP();
			wifiStaticBtn = findViewById(R.id.wifi_staic_btn);
			wifiStaticText = (TextView) findViewById(R.id.wifi_staic_btn_text);
			wifiSetBtn = findViewById(R.id.wifi_set_btn);
			initStaticText();

			wifiStaticBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isStatic = !isStatic;
					Settings.System.putInt(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP, isStatic ? 1 : 0);
					isStatic = isStaticIP();
					initStaticText();
				}
			});
			wifiSetBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ctx, WifiStaticIpDialogActivity.class);
					WifiSetMainActivity.this.startActivityForResult(intent, REQUEST_CODE);
				}
			});
		}
	}
	
	private void initStaticText() {
		if (!isStatic) {
			wifiStaticText.setText(R.string.wifi_dyn_ip_text);
			wifiStaticText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wifi_dymn_icon, 0);
		} else {
			wifiStaticText.setText(R.string.wifi_static_ip_text);
			wifiStaticText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wifi_static_icon, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(ctx, ctx.getString(R.string.wifi_info_edit_suc), Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean isStaticIP() {
		boolean flag = false;
		try {
			flag = Settings.System.getInt(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP) == 1 ? true : false;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkWifiState();
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(REFRESH_VIEW);
			}
		}, 5000, 6000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != t) {
			t.cancel();
		}
	}

	public void checkWifiState() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiUtil.openWifi();
		}
	}

	//	public void refresh() {
	//		if (adapter != null) {
	//		    mWifiUtil.startScan();
	//			list = mWifiUtil.getWifiData();
	//			adapter.setData(list);
	//			adapter.notifyDataSetChanged();
	//		}
	//	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			int state = mWifiUtil.checkState();
			switch (state) {
			case WifiManager.WIFI_STATE_ENABLED:
				handler.sendEmptyMessage(WIFI_ENABLED);
				break;
			default:
				break;
			}
		}
	};

	private BroadcastReceiver scanCompletereceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			mWifiUtil.dualWithScanResult();
			list = mWifiUtil.getWifiData();
			if (adapter != null) {
				adapter.setData(list);
				adapter.notifyDataSetChanged();
			} else {
				adapter = new WifiListAdapter(ctx, list);
				wifi_list.setAdapter(adapter);
			}
		}
	};
}
