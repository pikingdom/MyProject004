package com.nd.hilauncherdev.launcher;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.util.XmlUtils;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.widget.DynamicWidgetManager;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetInfoManager;
import com.nd.hilauncherdev.dynamic.clientparser.Client;
import com.nd.hilauncherdev.kitset.invoke.ForeignPackage;
import com.nd.hilauncherdev.kitset.invoke.ThirdWidgetAccessor;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.HiLauncherEXUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.info.PandaWidgetInfo;
import com.nd.hilauncherdev.launcher.info.PandaWidgetPreviewInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.screens.ScreenViewGroup;
import com.nd.hilauncherdev.launcher.view.PandaWidgetViewContainer;
import com.nd.hilauncherdev.myphone.common.ITransfer.PluginState;
import com.nd.hilauncherdev.myphone.common.PluginTransferUtil;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppWrapper;
import com.nd.hilauncherdev.scene.Scene;
import com.nd.hilauncherdev.scene.SceneManager;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.widget.pandawidget.PandaWidgetPreviewImageView;

/**
 * 91小部件 Launcher.java 中所有关于91小部件的代码请在此修改 <br>
 * Author:ryan <br>
 * Date:2012-7-3下午05:03:56
 */
public class LauncherPandaWidgetHelper {
	private static final String pandaWidget = "pandaWidget";

	public static View createPandaWidget(Launcher ctx, WidgetInfo item) {
		if (item.itemType == LauncherSettings.Favorites.ITEM_TYPE_PANDA_WIDGET)
			return createPandaWidgetView(ctx, item);
		else
			return createPandaPreviewWidget(ctx, item);
	}

	/**
	 * 条件满足将重启桌面，保持最后执行
	 */
	public static void updatePandaWidget(List<PandaWidgetViewContainer> widgetList, String packagename, final Context ctx, Handler handler) {
		if (StringUtil.isEmpty(packagename) || widgetList == null)
			return;

		for (PandaWidgetViewContainer container : widgetList) {
			if (!packagename.equals(container.getWidgetPackage()))
				continue;
			String widgetName = LauncherWidgetHelper.getWidgetName(packagename);
			if (!"".equals(widgetName)) {
				Toast.makeText(ctx, ctx.getString(R.string.widget_update_desktop_restart_hint, widgetName), Toast.LENGTH_LONG).show();
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.e("LauncherPandaWidgetHelper", "widget updated restartDesktop...");
					HiLauncherEXUtil.restartDesktop(ctx);
				}
			}, 3000);
			break;
		}
	}

	/**
	 * 删除非标widget
	 */
	public static void removePandaWidget(List<PandaWidgetViewContainer> widgetList, String packagename, ScreenViewGroup ws, Context ctx) {
		if (StringUtil.isEmpty(packagename) || widgetList == null)
			return;

		for (int i = widgetList.size() - 1; i >= 0; i--) {
			PandaWidgetViewContainer container = widgetList.get(i);
			if (!packagename.equals(container.getWidgetPackage()))
				continue;

			if (container.getTag() == null)
				continue;

			Object object = container.getTag();
			if (!(object instanceof PandaWidgetInfo))
				continue;

			PandaWidgetInfo info = (PandaWidgetInfo) object;
			LauncherModel.deleteItemFromDatabase(ctx, info);
			ThirdWidgetAccessor.invokeThirdWidgetOnDestoryMethod(container.getWidgetView(), info.appWidgetId);
			ws.removeChildLayoutView(info.screen, container);
			widgetList.remove(i);
		}
	}

	private static View createPandaPreviewWidget(Launcher ctx, WidgetInfo item) {
		if (!(item instanceof PandaWidgetPreviewInfo))
			return null;

		PandaWidgetPreviewInfo info = (PandaWidgetPreviewInfo) item;
		final PandaWidgetPreviewImageView preview = (PandaWidgetPreviewImageView) LayoutInflater.from(ctx).inflate(R.layout.widget_preview, null);
		preview.setTag(item);
		preview.setImageResource(info.iconRes);
		if (Global.isOnScene() && info.iconRes < 0) {
			Scene scene = SceneManager.getInstance(ctx).getCurrentScene();
			String icon = scene.getRecommendMeta(info.pandaWidgetPackage).applicationIcon;
			preview.setImageDrawable(ThemeManagerFactory.getInstance().getCurrentTheme().getDrawableByKey(icon, false, false));
		}
		preview.setLauncher(ctx);
		return preview;
	}

	private static View createPandaWidgetView(Launcher ctx, WidgetInfo item) {
		if (!(item instanceof PandaWidgetInfo))
			return null;

		PandaWidgetInfo info = (PandaWidgetInfo) item;
		String pluginJarName = info.pandaWidgetPackage + ".jar";
		/**
		 * 插件存放位置修改,兼容上一个版本启用过的插件
		 */
		File lastVersionJar = new File(Global.DOWNLOAD_DIR + pluginJarName);
		if (null != lastVersionJar && lastVersionJar.exists()) {
			String assetFullName = "";
			try {
				for (String name : ctx.getAssets().list("plugin/dynamic")) {
					if (name.contains(info.pandaWidgetPackage)) {
						assetFullName = name;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			PluginState state = PluginTransferUtil.getPluginStateDefaultImpl(ctx, "plugin/dynamic", Global.WIDGET_PLUGIN_DIR, info.pandaWidgetPackage, pluginJarName);
			if (state != PluginState.NORMAL) {
				// 文件异常重新拷贝
				FileUtil.copyAssetsFile(ctx, "plugin/dynamic/" + assetFullName, CommonGlobal.WIDGET_PLUGIN_DIR, pluginJarName);
			}
			FileUtil.delFile(Global.DOWNLOAD_DIR + pluginJarName);
		}
		Client client = DynamicWidgetManager.getClient(Global.WIDGET_PLUGIN_DIR + pluginJarName);
		if (LauncherWidgetInfoManager.DEBUG) {
			client = DynamicWidgetManager.getClient(Global.DOWNLOAD_DIR + info.pandaWidgetPackage + ".apk");
			if (client != null) {
				int[] spanxy = new int[]{info.spanX, info.spanY};
				View dview = null;
				dview = DynamicWidgetManager.createdynamicWidget(ctx, info.pandaWidgetPackage + ".apk", spanxy[0], spanxy[1], info.appWidgetId, Global.DOWNLOAD_DIR);
				if (dview != null)
					dview.setTag(info);
				return dview;
			}
		}
		if (client != null) {
			return createDynamicWidgetView(ctx, client, pluginJarName, info);
		}else{
			if (StringUtil.isEmpty(info.layoutResString) || StringUtil.isEmpty(info.pandaWidgetPackage)){
				return null;
			}
			if (!AndroidPackageUtils.isPkgInstalled(ctx, info.pandaWidgetPackage)) {
				LauncherModel.deleteItemFromDatabase(ctx, info);
				return null;
			}
		}
		ForeignPackage fp = null;
		try {// 增加非标小插件在构造函数、onAttachedToWindow、onFinishInflate异常捕获 caizp
				// 2013-1-15
			fp = new ForeignPackage(ctx, info.pandaWidgetPackage, true);
			final View remoteView = fp.getLayout(info.layoutResString);
			if (remoteView == null) {
				return null;
			}
			PandaWidgetViewContainer container = new PandaWidgetViewContainer(ctx, remoteView) {
				@Override
				protected void onAttachedToWindow() {
					super.onAttachedToWindow();
					Launcher mLauncher = Global.getLauncher();
					if (mLauncher != null && mLauncher.isOnSpringMode()) {
						if (Build.VERSION.SDK_INT >= 16) {// android4.1以上特殊处理
							mLauncher.mWorkspace.destoryCurrentChildHardwareLayer();
						}
						mLauncher.delayRefreshWorkspaceSpringScreen(300);
					}
				}
			};
			container.setTag(info);
			container.setWidgetPackage(info.pandaWidgetPackage);
			ThirdWidgetAccessor.invokeThirdWidgetAttatchToWindowAndOnLoadMethod(remoteView, info);
			return container;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static View transferPreviewInfoToRemoteView(Launcher ctx, PandaWidgetPreviewInfo item) {
		if (item == null)
			return null;

		ForeignPackage fp;
		try {
			fp = new ForeignPackage(ctx, item.pandaWidgetPackage, true);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		String layoutString = null;
		if (Global.isOnScene()) {
			if (item.layoutXml.startsWith("layout/")) {
				layoutString = item.layoutXml.substring(7);
			} else {
				String lyXml = item.layoutXml.startsWith("xml/") ? item.layoutXml.substring(4) : item.layoutXml;
				XmlResourceParser xmlResParser = fp.getXML(lyXml);
				if (null == xmlResParser)
					return null;
				layoutString = parser(fp, xmlResParser);
			}
		} else {
			XmlResourceParser xmlResParser = fp.getXML(item.layoutXml);
			if (null != xmlResParser) {
				layoutString = parser(fp, xmlResParser);
			} else {
				layoutString = item.layoutXml;
			}
		}

		if (StringUtil.isEmpty(layoutString))
			return null;

		PandaWidgetInfo result = new PandaWidgetInfo(item);
		result.layoutResString = layoutString;
		final View remoteView = fp.getLayout(layoutString);
		if (remoteView == null) {
			return null;
		}

		PandaWidgetViewContainer container = new PandaWidgetViewContainer(ctx, remoteView);
		container.setTag(result);
		container.setWidgetPackage(result.pandaWidgetPackage);
		ThirdWidgetAccessor.invokeThirdWidgetOnLoadMethod(remoteView, item.appWidgetId);

		return container;
	}

/*	public static View getRemoteView(Context ctx, PandaWidgetInfo item) {
		if (item == null)
			return null;

		ForeignPackage fp;
		try {
			fp = new ForeignPackage(ctx, item.pandaWidgetPackage, true);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		final View remoteView = fp.getLayout(item.layoutResString);
		if (remoteView == null) {
			return null;
		}
		ThirdWidgetAccessor.invokeThirdWidgetAttatchToWindowAndOnLoadMethod(remoteView, item);
		return remoteView;
	}*/

	private static String parser(ForeignPackage foreignPackage, XmlResourceParser xrp) {
		String result = null;
		XmlResourceParser parser = xrp;
		try {
			XmlUtils.beginDocument(parser, pandaWidget);

			final int depth = parser.getDepth();

			for (int j = 0; j < depth; j++) {
				int attrCount = parser.getAttributeCount();
				for (int k = 0; k < attrCount; k++) {
					String attrName = parser.getAttributeName(k);
					String attrValue = parser.getAttributeValue(k);
					if ("layoutRes".equalsIgnoreCase(attrName)) {
						String layoutResStr = attrValue.substring(1);
						try {
							int id = Integer.parseInt(layoutResStr);
							result = foreignPackage.getContext().getResources().getResourceEntryName(id);
						} catch (Exception e) {
							if (layoutResStr.startsWith("layout/")) {
								result = layoutResStr.substring(7);
							}
						}
						break;
					}
				}
			}
			parser.close();
		} catch (XmlPullParserException e) {
			parser.close();
			Log.w("parser", "Got exception parsing pandaWidget.", e);
		} catch (IOException e) {
			parser.close();
			Log.w("parser", "Got exception parsing pandaWidget.", e);
		}
		parser.close();

		return result;
	}

	public static boolean addPandaWidgetPreview(Context ctx, SQLiteDatabase db, ContentValues values, TypedArray a) {
		setContentVales(ctx, values, a);
		db.insert(LauncherProvider.TABLE_FAVORITES, null, values);
		return true;
	}

	/**
	 * 大分辨率下将4*1天气小部件改成4*2
	 * 
	 * @author dingdj
	 * @createtime 2013-8-16
	 * @param ctx
	 * @param db
	 * @param values
	 * @param a
	 * @return
	 */
/*	public static boolean addPandaWidgetPreview4BigWidget(Context ctx, SQLiteDatabase db, ContentValues values, TypedArray a, String bigWidgetUri) {
		setContentVales(ctx, values, a);
		values.put(LauncherSettings.Favorites.ICON_RESOURCE, bigWidgetUri);
		db.insert(LauncherProvider.TABLE_FAVORITES, null, values);
		return true;
	}*/

	private static void setContentVales(Context ctx, ContentValues values, TypedArray a) {
		String uri = a.getString(R.styleable.Favorite_uri);
		int iconResId = a.getResourceId(R.styleable.Favorite_icon, 0);
		if((LauncherBranchController.isLiTian() || LauncherBranchController.isMingLai() || LauncherBranchController.isDingKai() || LauncherBranchController.isLiTian_Yinni() || LauncherBranchController.isTiapPai_SmartHome())
				&& "weather_widget_4x1".equals(uri) && CustomRecommendAppWrapper.LITIAN_WEATHER_WIDGET_STYLE == 1 ){//适配力天定制版天气样式
			uri = "weather_widget_4x2";
			iconResId = R.drawable.loading_bg4x2;
			values.put(Favorites.SPANY, 2);
		}
		final int titleResId = a.getResourceId(R.styleable.Favorite_title, 0);

		values.put(LauncherSettings.Favorites.ICON_TYPE, iconResId);
		values.put(LauncherSettings.Favorites.TITLE, ctx.getString(titleResId));

		values.put(LauncherSettings.Favorites.ICON_PACKAGE, a.getString(R.styleable.Favorite_packageName));
		values.put(LauncherSettings.Favorites.ICON_RESOURCE, uri);
		values.put(Favorites.ITEM_TYPE, LauncherSettings.Favorites.ITEM_TYPE_PANDA_PREVIEW_WIDGET);

	}
	
	/**
	 * 增加动态插件
	 * @author dingdj
	 * Date:2014-7-8下午3:27:21
	 *  @param ctx
	 *  @param client
	 *  @param pluginJarName
	 *  @param info
	 *  @return
	 */
	private static View createDynamicWidgetView(final Launcher ctx, Client client, final String pluginJarName, final PandaWidgetInfo info){
		Client newClient = DynamicWidgetManager.getClient(Global.WIFI_DOWNLOAD_PATH + pluginJarName);
		if (newClient != null && newClient.getPluginVersion() > client.getPluginVersion()) {// 如果有新版本
			int[] spanxy = new int[] { info.spanX, info.spanY };
			View dview = DynamicWidgetManager.createdynamicWidget(ctx, pluginJarName, spanxy[0], spanxy[1], info.appWidgetId, Global.WIDGET_PLUGIN_DIR);
			if (dview == null)
				return null;
			dview.setTag(info);
			
			ViewGroup widgetLayout = (ViewGroup) ctx.getLayoutInflater().inflate(R.layout.plugin_widget_wrapper_update, null);
			//需要返回一个PandaWidgetViewContainer对象，所以将widgetLayout包在里面
			final PandaWidgetViewContainer pandaWidgetViewContainer = new PandaWidgetViewContainer(ctx, widgetLayout);
			pandaWidgetViewContainer.setTag(info);
			
			final LinearLayout content = (LinearLayout) widgetLayout.findViewById(R.id.view_wrapper);
			content.addView(dview); //将生成的插件包在content中
			
			TextView widgetUpdateTip = (TextView) widgetLayout.findViewById(R.id.update_desc);
			widgetUpdateTip.setText(Global.getApplicationContext().getString(R.string.plugin_update_tip));
			final LinearLayout updateContentLayout = (LinearLayout) widgetLayout.findViewById(R.id.update_layout);
			
			
			Button updateButton = (Button) updateContentLayout.findViewById(R.id.ok);
			Button cancelButton = (Button) updateContentLayout.findViewById(R.id.cancel);
			updateButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updateContentLayout.setVisibility(View.GONE);
					if(!FileUtil.isFileExits(Global.WIFI_DOWNLOAD_PATH + pluginJarName)){
						return;
					}
					FileUtil.delFile(Global.WIDGET_PLUGIN_DIR + pluginJarName);
					// 将wifidownload下的插件拷贝到目标目录下 然后删除
					FileUtil.moveFile(Global.WIFI_DOWNLOAD_PATH + pluginJarName, Global.WIDGET_PLUGIN_DIR + pluginJarName);
					Client client = DynamicWidgetManager.getClient(Global.WIDGET_PLUGIN_DIR + pluginJarName);
					if (client != null) {
						Launcher mLauncher = (Launcher) Global.getLauncher();
						if (mLauncher == null)
							return;
						
						//删除原来的动态插件
						ctx.getWorkspace().getCurrentCellLayout().removeView(pandaWidgetViewContainer);
						mLauncher.ifNeedClearCache(pandaWidgetViewContainer);
						int[] new_spanxy = new int[] { info.spanX, info.spanY };
						View view = null;
						view = DynamicWidgetManager.createdynamicWidget(ctx, pluginJarName, new_spanxy[0], new_spanxy[1], info.appWidgetId,
								Global.WIDGET_PLUGIN_DIR);
						if (view != null){
							view.setTag(info);
							
							mLauncher.getScreenViewGroup().addInScreen(view, info.screen, info.cellX, info.cellY, info.spanX, info.spanY);
							mLauncher.ifNeedCache(info, view);
						}
					}
				}
			});
			cancelButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					updateContentLayout.setVisibility(View.GONE);
				}
			});
			
			
			return pandaWidgetViewContainer;
		} else {
			int[] new_spanxy = new int[] { info.spanX, info.spanY };
			View view = null;
			view = DynamicWidgetManager.createdynamicWidget(ctx, pluginJarName, new_spanxy[0], new_spanxy[1], info.appWidgetId,
					Global.WIDGET_PLUGIN_DIR);
			if (view != null)
				view.setTag(info);
			
			return view;
		}
	
	}
}
