package com.nd.hilauncherdev.launcher;

import java.util.List;

import android.content.Context;

import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.model.load.Callbacks;
import com.nd.hilauncherdev.launcher.model.load.DeferredHandler;


public class AppsLoaderAndBinder {
	private DeferredHandler handler;
	private Callbacks callbacks;
	
	public AppsLoaderAndBinder(Callbacks callback, DeferredHandler handler) {
		this.callbacks = callback;
		this.handler = handler;
	}
	
	public void loadAndBindOnLauncher(final Context ctx) {
		if (callbacks == null)
			return;
		
		AppDataFactory factory = AppDataFactory.getInstance();
		if (factory != null) {
			final List<ApplicationInfo> result = factory.loadAppsOnLauncher(ctx);
			handler.post(new Runnable() {
				public void run() {
					callbacks.bindAllApplications(result);
				}
			});
		}
		
	}
}
