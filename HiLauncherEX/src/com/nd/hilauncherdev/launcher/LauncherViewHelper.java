package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.analysis.AppDistributeStatistics;
import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.framework.view.bubble.LauncherBubbleView;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.ControlledAnalyticsUtil;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.config.LauncherConfig;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;
import com.nd.hilauncherdev.launcher.edit.LauncherEditSlidingView;
import com.nd.hilauncherdev.launcher.edit.LauncherEditView;
import com.nd.hilauncherdev.launcher.edit.LauncherMIUIEditView;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.screens.ScreenViewGroup;
import com.nd.hilauncherdev.launcher.support.BaseLauncherViewHelper;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.IconMaskTextView;
import com.nd.hilauncherdev.push.PushIconTextView;
import com.nd.hilauncherdev.recommend.LauncherRecommendApps;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeData;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展处理显示到桌面的视图
 * <br>Author:ryan
 * <br>Date:2012-7-11下午09:02:43
 */
public class LauncherViewHelper extends BaseLauncherViewHelper{
//    public static final String TAG = LauncherViewHelper.class.getSimpleName();
	/**
	 * 处理AnythingInfo类型的单击事件
	 */
	public static void onAnyClick(final Launcher ctx, final ItemInfo item, final View v) {
		int analysisId = AnalyticsConstant.INVALID ;
		if (!(item instanceof AnythingInfo))
			return;
		
		AnythingInfo anyInfo = (AnythingInfo)item;
		if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT_INSTALLED) {
			analysisId = AnalyticsConstant.FOLDER_RECENT_INSTALL ;
			FolderInfo folder = new FolderInfo();
			List<ApplicationInfo> result = AppDataFactory.getInstance().loadRecentInstalled(ctx, 16);
			if (result != null)
				folder.contents = result;
			
			folder.title = ctx.getText(R.string.folder_recent_installed);
			folder.setProxyView(v);
			ctx.handleFolderClick(folder, v);
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT_RUNNING) {
			analysisId = AnalyticsConstant.FOLDER_RECENT_OPEN ;
			FolderInfo folder = new FolderInfo();
			List<ApplicationInfo> result = AppDataFactory.getInstance().loadRecentAppsList(ctx, 16);
			if (result != null) {
				List<ApplicationInfo> filted = new ArrayList<ApplicationInfo>();
				for (ApplicationInfo app : result) {
					if (app.componentName == null || app.componentName.getPackageName() == null)
						continue;
					if (ctx.getPackageName().equals(app.componentName.getPackageName()))
						continue;
					if ("com.nd.android.smarthome".equals(app.componentName.getPackageName()))
						continue;
					if ("com.nd.android.ilauncher".equals(app.componentName.getPackageName()))
						continue;
					if ("com.nd.android.widget.pandahome.flashlight".equals(app.componentName.getPackageName()))
						continue; // 屏蔽手电筒，一键关屏等等
					
					filted.add(app);
				}
				folder.contents = filted;
				if (filted.size() == 0) {
					MessageUtils.makeLongToast(ctx, R.string.folder_recent_running_empty);
				}
			}
			
			folder.title = ctx.getText(R.string.folder_recent_running);
			folder.setProxyView(v);
			ctx.handleFolderClick(folder, v);
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECOMMEND_APPS) {// 推荐文件夹被点击
			// 设置被点击过标示
			if (!ConfigPreferences.getInstance().getLauncherRecommendAppFolderClick()) {
				ConfigPreferences.getInstance().setLauncherRecommendAppFolderClick(true);
			}
			
			FolderInfo folder = new FolderInfo();
			folder.contents = null;//先文件夹清空数据。
			List<ApplicationInfo> result = LauncherRecommendApps.getInstance(ctx).getLauncherRecommendAppInfos(ctx);
			if (result != null)
				folder.contents = result;
			if(anyInfo.title != null && "".equals(anyInfo.title)){
				folder.title = ctx.getText(R.string.folder_recommend);
			}else{
				folder.title = anyInfo.title;
			}
			folder.setProxyView(v);
			ctx.handleFolderClick(folder, v);
//			HiAnalytics.submitEvent(ctx, AnalyticsConstant.RECOMMEND_FOLDER_ISOPEN);
			ControlledAnalyticsUtil.addCommonAnalytics(ctx,AnalyticsConstant.LAUNCHER_ICON_RECOMMEND);
			AppDistributeStatistics.AppDistributePercentConversionStatistics(ctx, "102");
			UsingIntervalStatistics.stat(ctx, null, null, UsingIntervalStatistics.RECOMMEND_FOLDER_KEY);
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT) { //最近任务
            handleRecentOpen(ctx, v);
        }
		/**
		 * 使用频率统计
		 */
		if (analysisId != AnalyticsConstant.INVALID)
			HiAnalytics.submitEvent(ctx, analysisId);
	}

    /**
     * 处理最近任务打开
     * @param ctx Context
     * @param v View
     */
	public static void handleRecentOpen(Launcher ctx, View v) {
        FolderInfo folder = new FolderInfo();
        int mount = 9;
        if (ctx.getFolderCotroller() != null &&
                ctx.getFolderCotroller().getFolderStyle() != FolderIconTextView.FOLDER_STYLE_FULL_SCREEN) {
            mount = 12;
        }
        List<ApplicationInfo> result = AppDataFactory.getInstance().loadAllRecentApplicationInfo(ctx, mount);
        if (result != null)
            folder.contents = result;

        folder.title = ctx.getText(R.string.folder_recent);
        folder.setProxyView(v);
        ctx.handleFolderClick(folder, v);
        
        ControlledAnalyticsUtil.addCommonAnalytics(ctx,AnalyticsConstant.LAUNCHER_ICON_FASTOPEN);
    }


    /**
	 * Description: 创建Anything类型View，如最近安装、最近打开等
	 * Author: guojy
	 * Date: 2013-9-18 下午6:10:37
	 */
	public static View createAnythingAppView(BaseLauncher launcher, ItemInfo item) {
		if (!(item instanceof AnythingInfo) || item.itemType != LauncherSettings.Favorites.ITEM_TYPE_ANYTHING)
				return null;
		
		IconMaskTextView result = null;
		AnythingInfo anyInfo = (AnythingInfo)item;
		if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT_INSTALLED) {
			result = getViewWithoutIcon(launcher, launcher.getText(R.string.folder_recent_installed), item);
			result.setFolderAvailable(false);
			result.setIconBitmap(BitmapUtils.drawable2Bitmap(ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.LATEST_INSTALL_APP_LIVE_FOLDER)));
			AnythingInfo info = (AnythingInfo)result.getTag();//保存文件夹名称给AnythingInfo
			info.title = launcher.getText(R.string.folder_recent_installed).toString();
			result.setTag(info);
			//view.setIconBgBitmap(bg);
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT_RUNNING) {
			result = getViewWithoutIcon(launcher, launcher.getText(R.string.folder_recent_running), item);
			result.setFolderAvailable(false);
			result.setIconBitmap(BitmapUtils.drawable2Bitmap(ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.OFTEN_USED_LIVE_FOLDER)));
			AnythingInfo info = (AnythingInfo)result.getTag();//保存文件夹名称给AnythingInfo
			info.title = launcher.getText(R.string.folder_recent_running).toString();
			result.setTag(info);
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECOMMEND_APPS) {// 推荐文件夹
			String title = ConfigPreferences.getInstance().getRecommendFolderName();//获取推荐文件夹名称
			if ("".equals(title)){
				title = launcher.getText(R.string.folder_recommend).toString();
			}
			result = getViewWithoutIcon(launcher,title , item);
			AnythingInfo info = (AnythingInfo)result.getTag();//保存文件夹名称给AnythingInfo
			info.title = title;
			result.setTag(info);
			result.setFolderAvailable(false);
			result.setIconBitmap(BitmapUtils.drawable2Bitmap(ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.OFTEN_USED_LIVE_FOLDER)));
			//假如列表为空顺道初始化本地数据给推荐文件夹
			if(LauncherRecommendApps.getInstance(launcher).getRecommendAppInfoList().size() == 0){
				LauncherRecommendApps.getInstance(launcher).initRecommendAppInfoList(launcher);
			}
		} else if (anyInfo.flag == AnythingInfo.FLAG_FOLDER_RECENT) { //最近任务
            result = getRecentView(launcher, item);
        }
		
		return result;
	}

    private static IconMaskTextView getRecentView(BaseLauncher launcher, ItemInfo item) {
        IconMaskTextView result;
        result = getViewWithoutIcon(launcher, launcher.getText(R.string.folder_recent), item);
        result.setFolderAvailable(false);
        result.setIconBitmap(BitmapUtils.drawable2Bitmap(ThemeManagerFactory.getInstance().getThemeDrawable(ThemeData.LATEST_INSTALL_APP_LIVE_FOLDER)));
        AnythingInfo info = (AnythingInfo)result.getTag();//保存文件夹名称给AnythingInfo
        info.title = launcher.getText(R.string.folder_recent).toString();
        result.setTag(info);
        return result;
    }

    private static IconMaskTextView getViewWithoutIcon(final BaseLauncher launcher, CharSequence title, ItemInfo info) {
//		IconMaskTextView favorite = (IconMaskTextView)ctx.getLayoutInflater().inflate(R.layout.iconmasktestview, (ViewGroup) ctx.mWorkspace.getChildAt(ctx.mWorkspace.getCurrentScreen()), false);
		IconMaskTextView favorite = new IconMaskTextView(launcher);
		favorite.setText(title);
		favorite.setTag(info);
		favorite.setOnClickListener(launcher);
		return favorite;
	}
	
	
	/**
	 * 创建小部件View
	 */
	public static View createAppWidgetView(Launcher mLauncher, WidgetInfo itemInfo){
		return LauncherWidgetHelper.createAppWidgetViewWithSetItemType(itemInfo, mLauncher);
	}
	
	
	/**
	 * 生成动画view
	 * author:dingdj
	 * date: 2013-6-21
	 */
/*	public static View createAnimationView(Launcher mLauncher, Drawable drawable, int[] locations, View v, int[] cellParams) {
		
		ImageView favorite;
		if(LauncherEditView.animationViewCache.size() > 0){
			SoftReference<ImageView> sr = LauncherEditView.animationViewCache.get(0);
			favorite =  sr.get();
			if(favorite == null){
				favorite =  new ImageView(mLauncher);
				LauncherEditView.animationViewCache.add(new SoftReference<ImageView>(favorite));
			}
		}else{
			favorite = new ImageView(mLauncher);
			LauncherEditView.animationViewCache.add(new SoftReference<ImageView>(favorite));
		}
		
		int width = 0;
		int height = 0;
		final ScreenViewGroup mWorkspace = mLauncher.mWorkspace;
		if(cellParams != null){
			width = (int)(cellParams[0] * mWorkspace.getSpringScale());
			height =(int)(cellParams[1]* mWorkspace.getSpringScale());
		}else{
			int iconSize = IconAndTextSizeConfig.getSmallIconSize(mLauncher);
			width = height = (int) (iconSize * mWorkspace.getSpringScale());
		}
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(width, height);
		p.gravity = Gravity.TOP;
		float orginalCenterx = locations[0] + v.getWidth()/ 2;
		float orginalCentery = locations[1] + v.getHeight()/ 2;

		float scalex = orginalCenterx - (p.width / 2);
		float scaley = orginalCentery - (p.height / 2);

		p.setMargins((int) scalex, (int) scaley, 0, 0);
		locations[0] = (int) scalex;
		locations[1] = (int) scaley;
		favorite.setLayoutParams(p);
		favorite.setImageDrawable(drawable);
		mLauncher.mDragLayer.addView(favorite, p);
		return favorite;
	}*/
	
	
	/**
	 * 创建冒泡View
	 * @param hint
	 * @param screen
	 * @param hostView
	 * @return
	 */
	public static LauncherBubbleView createBubbleViewInWorkspace(String hint, int screen, View hostView) {
		BaseLauncher mLauncher = BaseConfig.getBaseLauncher();
		if(mLauncher == null)
			return null;
		
		boolean isOnBottom = false;//是否显示于目标View的底部
		CellLayout.LayoutParams hostLP = (CellLayout.LayoutParams)hostView.getLayoutParams();
		if(hostLP.cellY == 0){
			isOnBottom = true;
		}
		
		CellLayout cl = mLauncher.mWorkspace.getCellLayoutAt(screen);
		int screenWidth = ScreenUtil.getScreenWH()[0];
		int cellWidth = cl.getCellWidth();
		int cellHeight = cl.getCellHeight();
		if(cellWidth == 0 || cellHeight == 0){
			cellWidth = BaseConfigPreferences.getInstance().getCellLayoutCellWidth();
			cellHeight = BaseConfigPreferences.getInstance().getCellLayoutCellHeight();
		}
		if(screenWidth == 0 || cellWidth == 0 || cellHeight == 0){
			Log.e("createBubbleView", "createBubbleView fail");
			return null;
		}
		int[] xy = new int[2];
		if(hostLP.x != 0 || hostLP.y != 0){
			xy[0] = hostLP.x;
			xy[1] = hostLP.y;
		}else{
			hostView.getLocationOnScreen(xy);
	        if(xy[0] > screenWidth){
	        	xy[0] = xy[0] % screenWidth;
	        }
	        if(xy[0] < 0){
	        	xy[0] = screenWidth - (Math.abs(xy[0]) % screenWidth);
	        }
		}
        
        //内容View
		final int fontSize = 14;
        Paint p = new Paint();
        final float densityMultiplier = ScreenUtil.getDensity();
        p.setTextSize(fontSize * densityMultiplier);
        Rect bounds = new Rect();
        p.getTextBounds(hint, 0, hint.length(), bounds);
        
		LauncherBubbleView favorite = new LauncherBubbleView(mLauncher);
		favorite.setOnClickListener(mLauncher);
		CellLayout.LayoutParams lp = new CellLayout.LayoutParams(new ViewGroup.LayoutParams(bounds.width()+ScreenUtil.dip2px(mLauncher, 30), 
				bounds.height() +ScreenUtil.dip2px(mLauncher, 30)));
		int line = lp.width / (cellWidth * 2);
		lp.width = Math.min(lp.width, cellWidth * 2);//冒泡view最宽不超过2个View
		lp.height += bounds.height() * line;
        lp.x = Math.max(0, xy[0] + cellWidth/2 - bounds.width()/2);
        if(lp.x + lp.width > screenWidth){
        	lp.x = screenWidth - lp.width;
        }
        if(isOnBottom){
        	lp.y = xy[1] - CellLayoutConfig.getMarginTop() + cellHeight;
        }else{        	
        	lp.y = xy[1] - CellLayoutConfig.getMarginTop() - lp.height;
        }
        lp.isOnXYAndWHMode = true;
		favorite.setGravity(Gravity.CENTER);
		favorite.setBackgroundResource(R.drawable.cleaner_widget_bg);
		favorite.setText(hint);
		favorite.setTextSize(fontSize);
		favorite.setLayoutParams(lp);
        //角标View
        ImageView iv = new ImageView(mLauncher);
        CellLayout.LayoutParams lp2 = new CellLayout.LayoutParams(new ViewGroup.LayoutParams(ScreenUtil.dip2px(mLauncher, 20), 
				ScreenUtil.dip2px(mLauncher, 22)));
        if(isOnBottom){
        	iv.setImageResource(R.drawable.launcher_notify_angle_down);
            lp2.x = xy[0] + cellWidth/2;
            lp2.y = lp.y - lp2.height;
            lp2.isOnXYAndWHMode = true;
        }else{
        	iv.setImageResource(R.drawable.launcher_notify_angle_up);
            lp2.x = xy[0] + cellWidth/2;
            lp2.y = lp.y + lp.height - ScreenUtil.dip2px(mLauncher, 8.5f);
            lp2.isOnXYAndWHMode = true;
        }
        iv.setLayoutParams(lp2);
        
        favorite.setAngleView(iv);
        return favorite;
	}
	
	public static LauncherBubbleView createBubbleView(Context ctx, String hint, View hostView) {
		int screenWidth = ScreenUtil.getScreenWH()[0];
		int maxWidth = screenWidth / 2;
		
		
		int[] xy = new int[2];
		hostView.getLocationOnScreen(xy);
        if(xy[0] > screenWidth){
        	xy[0] = xy[0] % screenWidth;
        }
        if(xy[0] < 0){
        	xy[0] = screenWidth - (Math.abs(xy[0]) % screenWidth);
        }
        
		final int fontSize = 14;
        Paint p = new Paint();
        final float densityMultiplier = ScreenUtil.getDensity();
        p.setTextSize(fontSize * densityMultiplier);
        Rect bounds = new Rect();
        p.getTextBounds(hint, 0, hint.length(), bounds);
        
        int width = bounds.width()+ScreenUtil.dip2px(ctx, 30);
        int height = bounds.height() +ScreenUtil.dip2px(ctx, 30);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        int line = lp.width / maxWidth;
		lp.width = Math.min(lp.width, maxWidth);
		lp.height += bounds.height() * line;
		int left = Math.max(0, xy[0] + hostView.getWidth()/2 - bounds.width()/2);
        if(left + lp.width > screenWidth){
            left = screenWidth - lp.width;
        }
        int top = xy[1] - ScreenUtil.dip2px(ctx, 8) - lp.height;
        lp.setMargins(left, top, left+lp.width, top+lp.height);
		
		LauncherBubbleView favorite = new LauncherBubbleView(Global.getApplicationContext());
		favorite.setGravity(Gravity.CENTER);
		favorite.setBackgroundResource(R.drawable.cleaner_widget_bg);
		favorite.setText(hint);
		favorite.setTextSize(fontSize);
		favorite.setLayoutParams(lp);
		
		//角标View
        ImageView iv = new ImageView(ctx);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(ctx, 20),
				ScreenUtil.dip2px(ctx, 22));
        iv.setImageResource(R.drawable.launcher_notify_angle_up);
        int iv_Left = xy[0] + hostView.getWidth()/2;
        int iv_Top = top + lp.height - ScreenUtil.dip2px(ctx, 8.5f);
        lp2.setMargins(iv_Left, iv_Top, iv_Left+lp2.width, iv_Top+lp2.height);
        iv.setLayoutParams(lp2);
        
        favorite.setAngleView(iv);
        return favorite;
	}


    public static LauncherBubbleView createBubbleViewForAppHide(Context ctx, String hint, View hostView) {
        int screenWidth = ScreenUtil.getScreenWH()[0];
        int maxWidth = screenWidth / 2;


        int[] xy = new int[2];
        hostView.getLocationOnScreen(xy);
        if(xy[0] > screenWidth){
            xy[0] = xy[0] % screenWidth;
        }
        if(xy[0] < 0){
            xy[0] = screenWidth - (Math.abs(xy[0]) % screenWidth);
        }

        final int fontSize = 14;
        Paint p = new Paint();
        final float densityMultiplier = ScreenUtil.getDensity();
        p.setTextSize(fontSize * densityMultiplier);
        Rect bounds = new Rect();
        p.getTextBounds(hint, 0, hint.length(), bounds);

        int width = bounds.width()+ScreenUtil.dip2px(ctx, 30);
        int height = bounds.height() +ScreenUtil.dip2px(ctx, 30);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        int line = lp.width / maxWidth;
        lp.width = Math.min(lp.width, maxWidth);
        lp.height += bounds.height() * line;
        int left = Math.max(0, xy[0] + hostView.getWidth()/2 - bounds.width()/2);
        if(left + lp.width > screenWidth){
            left = screenWidth - lp.width;
        }
        int top = xy[1] - ScreenUtil.dip2px(ctx, 8) - lp.height;
        //Log.e(TAG, "left:"+left +"|" +"top:"+top +"|" + "right:"+left+lp.width+"|bottom:"+top+lp.height);
        lp.setMargins(left, top, left+lp.width, top+lp.height);
        lp.gravity = Gravity.TOP;

        LauncherBubbleView favorite = new LauncherBubbleView(Global.getApplicationContext());
        favorite.setGravity(Gravity.CENTER);
        favorite.setBackgroundResource(R.drawable.cleaner_widget_bg);
        favorite.setText(hint);
        favorite.setTextSize(fontSize);
        favorite.setLayoutParams(lp);

        //角标View
        ImageView iv = new ImageView(ctx);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ScreenUtil.dip2px(ctx, 20),
                ScreenUtil.dip2px(ctx, 22));
        iv.setImageResource(R.drawable.launcher_notify_angle_up);
        int iv_Left = xy[0] + hostView.getWidth()/2;
        int iv_Top = top + lp.height - ScreenUtil.dip2px(ctx, 8.5f);
        lp2.setMargins(iv_Left, iv_Top, iv_Left+lp2.width, iv_Top+lp2.height);
        lp2.gravity = Gravity.TOP;
        iv.setLayoutParams(lp2);

        favorite.setAngleView(iv);
        return favorite;
    }

	/**
	 * 创建主题标签冒泡view
	 * @param hint
	 * @param screen
	 * @param hostView
	 * @return
	 */
	public static View createThemeTagNotifyViewInWorkspace(View hostView, int screen) {
		BaseLauncher mLauncher = BaseConfig.getBaseLauncher();
		if(mLauncher == null)
			return null;


		CellLayout cl = mLauncher.mWorkspace.getCellLayoutAt(screen);
		int screenWidth = ScreenUtil.getScreenWH()[0];
		int cellWidth = cl.getCellWidth();
		int cellHeight = cl.getCellHeight();
		if(cellWidth == 0 || cellHeight == 0){
			cellWidth = BaseConfigPreferences.getInstance().getCellLayoutCellWidth();
			cellHeight = BaseConfigPreferences.getInstance().getCellLayoutCellHeight();
		}
		if(screenWidth == 0 || cellWidth == 0 || cellHeight == 0){
			Log.e("createBubbleView", "createBubbleView fail");
			return null;
		}
		int[] xy = new int[2];
		hostView.getLocationOnScreen(xy);

		int hostView_width = cl.getWidth();
		int hostView_height = cl.getHeight();

		//内容View
		final int fontSize = 14;
		Paint p = new Paint();
		final float densityMultiplier = ScreenUtil.getDensity();
		p.setTextSize(fontSize * densityMultiplier);
		Rect bounds = new Rect();
		String styledText = "<font color='#405d6c'>该主题带有专属贴纸点击有惊喜~</font><font color='#1d91ce'>长按可拖动</font>.";
		CharSequence hint = Html.fromHtml(styledText);
		p.getTextBounds(hint.toString(), 0, hint.toString().length(), bounds);

		View favorite = mLauncher.getLayoutInflater().inflate(R.layout.theme_tag_notify, null);

		TextView textView = (TextView) favorite.findViewById(R.id.tvContent);
		textView.setText(hint);
		favorite.setOnClickListener(mLauncher);
		CellLayout.LayoutParams lp = new CellLayout.LayoutParams(new ViewGroup.LayoutParams(
				bounds.width()+ScreenUtil.dip2px(mLauncher, 30),
				bounds.height() +ScreenUtil.dip2px(mLauncher, 60)));
		int line = lp.width / (cellWidth * 2);
		//lp.width = Math.min(lp.width, cellWidth * 2);//冒泡view最宽不超过2个View
		lp.height += bounds.height() * line;
		xy[0] = xy[0]+hostView_width/2 + ScreenUtil.dip2px(mLauncher, 80) - Math.min(lp.width, cellWidth * 2)
		+ cellWidth/2;
		xy[1] =hostView_height/2 - lp.height/2 - ScreenUtil.dip2px(mLauncher, 60);
		if(xy[0] > screenWidth){
			xy[0] = xy[0] % screenWidth;
		}
		if(xy[0] < 0){
			xy[0] = screenWidth - (Math.abs(xy[0]) % screenWidth);
		}
		lp.x = xy[0];
		lp.y = xy[1];
		lp.isOnXYAndWHMode = true;
		favorite.setLayoutParams(lp);
		return favorite;
	}
	
	
	/**
	 * Description: 创建桌面上普通app、“我的手机”里的app、系统快捷、91快捷、自定义app(热门软件、热门游戏等)的View
	 * Author: guojy
	 * Date: 2013-9-18 下午4:36:34
	 */
	public static View createPushIconTextView(ApplicationInfo info) {
		Launcher mLauncher = Global.getLauncher();
		if(mLauncher == null)
			return null;
		
		ScreenViewGroup mWorkspace = mLauncher.getScreenViewGroup();
		ViewGroup parent = (ViewGroup) mWorkspace.getChildAt(mWorkspace.getCurrentScreen());
		if (parent == null)
			parent = (ViewGroup) mWorkspace.getChildAt(0);

		PushIconTextView favorite = new PushIconTextView(mLauncher);
		ViewGroup.MarginLayoutParams vm = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, 
				ViewGroup.MarginLayoutParams.MATCH_PARENT);
		CellLayout.LayoutParams cl = LauncherConfig.getLauncherHelper().newCellLayoutLayoutParams(vm);
		cl.setMargins(ScreenUtil.dip2px(mLauncher, 3), ScreenUtil.dip2px(mLauncher, 5), 
				ScreenUtil.dip2px(mLauncher, 3), ScreenUtil.dip2px(mLauncher, 5));
		favorite.setLayoutParams(cl);
		favorite.setPadding(ScreenUtil.dip2px(mLauncher, 2), 0, ScreenUtil.dip2px(mLauncher, 2), 0);
		favorite.setText(info.title);
		favorite.setTag(info);
		favorite.setOnClickListener(mLauncher);
		favorite.setIconBitmap(info.iconBitmap);
		if(mLauncher.isWorkspaceVisable() && info.screen == mLauncher.getWorkspace().getCurrentScreen()){
			favorite.callOnCurrentScreen();
		}
		return favorite;
	}
	
	/**
	 * 生成动画view
	 * author:dingdj
	 * date: 2013-6-21
	 */
	public static View createAnimationView(Launcher mLauncher, Drawable drawable, int[] locations, View v, int[] cellParams) {
		
		ImageView favorite;
		if(LauncherMIUIEditView.animationViewCache.size() > 0){
			SoftReference<ImageView> sr = LauncherMIUIEditView.animationViewCache.get(0);
			favorite =  sr.get();
			if(favorite == null){
				favorite =  new ImageView(mLauncher);
				LauncherMIUIEditView.animationViewCache.add(new SoftReference<ImageView>(favorite));
			}
		}else{
			favorite = new ImageView(mLauncher);
			LauncherMIUIEditView.animationViewCache.add(new SoftReference<ImageView>(favorite));
		}
		
		int width = 0;
		int height = 0;
		final ScreenViewGroup mWorkspace = mLauncher.mWorkspace;
		if(cellParams != null){
//			width = (int)(cellParams[0] * mWorkspace.getSpringScale());
//			height =(int)(cellParams[1]* mWorkspace.getSpringScale());
			width = (int)((v.getWidth() - ScreenUtil.dip2px(mLauncher, LauncherEditSlidingView.widgetPaddingDp))* mWorkspace.getSpringScale());
			height =(int)((v.getHeight() - ScreenUtil.dip2px(mLauncher, LauncherEditSlidingView.widgetPaddingDp))* mWorkspace.getSpringScale());
		}else{
			int iconSize = mLauncher.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
			width = height = (int) (iconSize * mWorkspace.getSpringScale());
		}
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(width, height);
		p.gravity = Gravity.TOP;
		float orginalCenterx = locations[0] + v.getWidth()/ 2;
		float orginalCentery = locations[1] + v.getHeight()/ 2;

		float scalex = orginalCenterx - (p.width / 2);
		float scaley = orginalCentery - (p.height / 2);

		p.setMargins((int) scalex, (int) scaley, 0, 0);
		locations[0] = (int) scalex;
		locations[1] = (int) scaley;
		favorite.setLayoutParams(p);
		favorite.setImageDrawable(drawable);
		mLauncher.mDragLayer.addView(favorite, p);
		return favorite;
	}
	
}
