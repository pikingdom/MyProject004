package com.nd.hilauncherdev.launcher.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.framework.OnKeyDownListenner;
import com.nd.hilauncherdev.launcher.Launcher;

public abstract class LauncherBaseMenu extends RelativeLayout implements OnKeyDownListenner {

	public LauncherBaseMenu(Context context) {
		this(context, null);
	}

	public LauncherBaseMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public abstract void show();
	public abstract void hide();
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		Launcher launcher = Global.getLauncher();
		if (launcher != null) {
			launcher.addOnKeyDownListener(this);
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		Launcher launcher = Global.getLauncher();
		if (launcher != null) {
			launcher.removeOnKeyDownListener(this);
		}
	}
	
	protected boolean processKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	@Override
	public boolean onKeyDownProcess(int keyCode, KeyEvent event) {
		if (processKeyDown(keyCode, event)) {
			return true;
		}
		
		if (getVisibility() == View.VISIBLE 
			&& (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
			hide();
			return true;
		}
		
		return false;
	}
}
