package com.nd.hilauncherdev.launcher;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.kitset.resolver.CenterControl;
import com.nd.hilauncherdev.settings.HomeSettingsActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultHomeTipActivity extends Activity implements View.OnClickListener{
	public final  static String INTENT_PARAM_FROM_WHICH="intent_param_from_which";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_btn_dialog_layout_full);
		Intent localIntent = new Intent(this,CallHomeService.class);
		findViewById(R.id.common_dialog_layout).setOnClickListener(this);
		findViewById(R.id.common_dialog_button).setOnClickListener(this);
		stopService(localIntent);
		Intent i=getIntent();
		//改变提示的文字
		if (i != null) {
			String r = i.getStringExtra(INTENT_PARAM_FROM_WHICH);
			if ("1".equals(r)) {
				View v=findViewById(R.id.common_dialog_top_relativelayout);
				if (Build.VERSION.SDK_INT >= 14){
					v.setBackgroundResource(R.drawable.set_default_home_mostfacescore);
				}
			}
		}
	}



	public static List getAllHomes(Context context) {
		ArrayList<String> list = new ArrayList();
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		Iterator iterator = pm.queryIntentActivities(intent, 0).iterator();
		while (iterator.hasNext()) {
			list.add(((ResolveInfo) iterator.next()).activityInfo.packageName);
		}
		return list;
	}

	private static  String getProcessOld(Context context) {
		List list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
		if (list != null)
			return ((ActivityManager.RunningTaskInfo) list.get(0)).topActivity.getPackageName();
		return null;
	}

	public static String getProcessPkg(Context context)
	{
		if (Build.VERSION.SDK_INT >= 21)
			return getProcessNew1(context);
		return getProcessOld(context);
	}

	private static  String getProcessNew1(Context context)
	{

		ActivityManager.RunningAppProcessInfo processInfo;
		Iterator iterator;
		Field processStateField;
		try {
			processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");

		} catch (NoSuchFieldException e) {
			return null;
		}
		if(null == ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses()) return "";
		iterator = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses().iterator();
		while (iterator.hasNext()) {
			//cond_1
			processInfo = (ActivityManager.RunningAppProcessInfo)iterator.next();
			if((processInfo.importance == 100) && processInfo.importanceReasonCode == 0)
			{

				try {
					Integer state = Integer.valueOf(processStateField.getInt(processInfo));
					if ((state != null) && (state.intValue() == 2)) ;
					{

						return processInfo.pkgList[0];
					}

				}catch ( Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	return null;
	}

	@Override
	public void onClick(View view) {
		if (!CenterControl.startGuideSurface(this)) {
			HomeSettingsActivity.startSetDefaultHome(this);
		}
		finish();
	}
}