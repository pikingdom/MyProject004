package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.launcher.config.ApplicationContextConfig;
import com.nd.hilauncherdev.launcher.config.LauncherConfig;
import com.nd.hilauncherdev.launcher.model.BaseLauncherProvider;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppWrapper;
import com.nd.hilauncherdev.settings.assit.movedesk.newmove.NewMoveDesk;

public class LauncherProvider extends BaseLauncherProvider {
	/**
	 * 数据库版本 -- 桌面版本
	 * 10 -- 3.0 
	 * 11 -- 3.0.1 
	 * 12 -- 3.1 
	 * 13 -- 3.2 
	 * 14 -- 3.5
	 * 15 -- 3.5.2 
	 * 16 -- 3.5.5 
	 * 17 -- 3.5.6 
	 * 18 -- 5.0
	 * 19 -- 5.1
	 * 20 -- 5.3.1
	 * 21 -- 5.3.2
	 */
	public static int DATABASE_VERSION = 21;

	/**
	 * favorites建表sql
	 */
	public static final String FAV_TABLE_NAME = "favorites";
	public static String CREATE_TABLE_FAVORITES = String.format(CREATE_TABLE_FAVORITES_MODEL, FAV_TABLE_NAME, "");
	
	@Override
	public boolean onCreate() {
		Log.e("LauncherProvider", "onCreate");
//		super.onCreate();
		ApplicationContextConfig.setApplicationContext(getContext().getApplicationContext());
		LauncherConfig.init(getContext());
		initOthers();
		mOpenHelper = getSQLiteHelperInstance();
		return true;
	}
	
	private void initOthers(){
		Context mContext = getContext();
		if(Global.getApplicationContext()==null){
			Global.setApplicationContext(mContext.getApplicationContext());
			Global.initDain91SDK(mContext);
		}
		//初始化数据库辅助类
		LauncherProviderHelper.initDBHelper();
		//桌面风格设置
		LauncherSwitchController.onSwitchDefaultLauncher(mContext);
	}

	@Override
	public SQLiteHelper getSQLiteHelperInstance(){
		return new DatabaseHelper(getContext());
	}
	
	public static class DatabaseHelper extends SQLiteHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			setCreateTableSql(CREATE_TABLE_FAVORITES);
			setLauncherUpgrader(new LauncherProviderUpgrader(context, this));
		}

		@Override
		public void loadDefaultData(SQLiteDatabase db) {
			//处理循环崩溃数据恢复
			int converted = LauncherProviderHelper.exceptionResetDataFromSdcard(db);
			if (!Global.isOnScene() && converted <= 0) {
				loadFavorites(db);
			}
		}
		
		public void loadFavorites(SQLiteDatabase db){
			//搬家原生桌面内容	
//			NewMoveDesk.readyForAutoMove(db);
			LauncherProviderHelper.loadFavorites(db, getAppWidgetHost(), Global.getApplicationContext());
		}
	}
}
