package com.nd.hilauncherdev.drawer.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.app.SerializableAppInfo;
import com.nd.hilauncherdev.app.data.AppDataBase;
import com.nd.hilauncherdev.app.data.AppTable;
import com.nd.hilauncherdev.app.ui.view.util.ApplicationInfoUtil;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.framework.view.commonsliding.datamodel.ICommonDataItem;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.LauncherProviderHelper;
import com.nd.hilauncherdev.launcher.LauncherSettings;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.myphone.data.MyPhoneDataFactory;
import com.nd.hilauncherdev.settings.assit.BackupSetting;
import com.nd.hilauncherdev.theme.data.ThemeData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Anson
 */
public class DrawerDataFactory {

	/**
	 * 更新position
	 */
	private static final String UPDATE_POSITION = "update AppTable set " + AppTable.POS + " = %d where " + AppTable.ID + " = %d";

	/**
	 * 更新container与position
	 */
	private static final String UPDATE_CONTAINER_AND_POSITION = "update AppTable set " + AppTable.CONTAINER + " = %d, " + AppTable.POS
			+ " = %d where " + AppTable.ID + " = %d";

	/**
	 * 更新container
	 */
	private static final String UPDATE_CONTAINER = "update AppTable set " + AppTable.CONTAINER + " = %d where " + AppTable.CONTAINER + " = %d";

	/**
	 * 删除记录
	 */
	private static final String DELETE = "delete from " + AppTable.TABLE_NAME + " where " + AppTable.ID + " = %d";

	/**
	 * 通过包名查询记录
	 */
	private static final String SELECT_APPS_BY_PACKAGE = "select " + AppTable.ID + ", " + AppTable.PCK + ", " + AppTable.CLS + ", " + AppTable.TITLE
			+ ", " + AppTable.CONTAINER + ", " + AppTable.POS + ", " + AppTable.ISHIDDEN + " from " + AppTable.TABLE_NAME + " where " + AppTable.PCK
			+ " = '%s'";

	/**
	 * 通过包名与类名查找程序记录
	 */
	private static final String SELECT_APP_BY_PACKAGE_AND = "select * from " + AppTable.TABLE_NAME + " where " + AppTable.PCK + " = '%s' and "
			+ AppTable.CLS + " = '%s'";

	/**
	 * 查询最大position
	 */
	private static final String SELECT_MAX_POSITION = "select max(" + AppTable.POS + ") from " + AppTable.TABLE_NAME + " where " + AppTable.CONTAINER
			+ " = %d";

	/**
	 * 更新程序title,包括文件夹
	 */
	private static final String UPDATE_TITLE = "update " + AppTable.TABLE_NAME + " set " + AppTable.TITLE + " = '%s' " + " where " + AppTable.ID
			+ " = %d";

    /**
     * 更新程序cls
     */
    private static final String UPDATE_CLS = "update " + AppTable.TABLE_NAME + " set " + AppTable.CLS + " = '%s' " + " where " + AppTable.ID
            + " = %d";

	/**
	 * 查询所有隐藏的程序
	 */
	private final static String SELECT_ALL_HIDDEN_APP = "SELECT * from AppTable where (type = 0 or type =" + LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT +") and ishidden = 1";

	/**
	 * 更新是否隐藏
	 */
	private static final String UPDATE_ISHIDDEN = "update AppTable set " + AppTable.ISHIDDEN + " = %d where " + AppTable.PCK + " = '%s' and "
			+ AppTable.CLS + " = '%s'";
	private static final String UPDATE_ISHIDDEN_FOR_CUSTOM = "update AppTable set " + AppTable.ISHIDDEN + " = %d where " + AppTable.PCK + " = '%s'";

	/**
	 * 所有程序显示
	 */
	private static final String CLEAR_ISHIDDEN = "update AppTable set " + AppTable.ISHIDDEN + " = 0";

	/**
	 * 所有程序显示
	 */
	private static final String CLEAR_ISHIDDEN_EXCEPT = "update AppTable set " + AppTable.ISHIDDEN + " = 0 "+ " where "
			+ AppTable.TYPE + " != " + LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER;
	
	
	/**
	 * 查询是否存在未分类图标
	 */
	private final static String SELECT_ALL_UN_CLASSIFY_APP = "select used from AppTable where con = 0 and type = 0 and ishidden = 0";
	
	/**
	 * 删除表中所有的数据
	 */
	private static final String DELETE_ALL_DATA = "delete from AppTable";

	/**
	 * 插入一条完整的数据
	 */
	private static final String INSERT_DATA = "insert into AppTable(_id, pck, cls, tit, scr, pos, con, type, time, used, cellx, celly, issys, pinyin, ishidden) values(%d, '%s', '%s', '%s', %d, %d, %d, %d, %d, %d, %d, %d, %d, '%s', %d)";

	
	/**
	 * 根据folder的title 查询ID
	 */
	private final static String SELECT_FOLDER_ID = "SELECT _id from AppTable where tit = '%s'";
	
	/**
	 * 根据folder的title 查询ID
	 */
	private final static String SELECT_APP_NUM_BY_FOLDER_ID = "SELECT COUNT(*) as appcount from AppTable where con = %d";
	
	
	
	public final static String CACHE_DIR = BaseConfig.getBaseDir() + "/.cache/";
	public final static String ZIP_FILE_NAME = "app.zip";
	public final static String DATA_FILE_NAME = "AppTable.json";
	/**
	 * 爱淘宝
	 */
	public static int AITAOBAO_DRAWABLE = R.drawable.drawer_custom_itaobao;
	public static int AITAOBAO_TITLE = R.string.drawer_app_custom_taobao_title;
	public static String AITAOBAO_RES_NAME = Global.PKG_NAME +":drawable/drawer_custom_itaobao";
	/**
	 * 天猫精选
	 */
//	public static int TMALL_DRAWABLE = R.drawable.drawer_custom_tmall;
//	public static int TMALL_TITLE = R.string.drawer_app_custom_tmall_title;
//	public static String TMALL_RES_NAME = Global.PKG_NAME +":drawable/drawer_custom_tmall";
//	public static String TMALL_INTENT = "#Intent;action=android.intent.action.VIEW;component="
//			+ Global.PKG_NAME + "/com.nd.hilauncherdev.uri.UriDeliveryActivity;S.url=http%3A%2F%2Furl.ifjing.com%2FQNB3A3;end";
	
	/**
	 * 匣子内app推荐图标“爱淘宝”,6.5-6.8更换为"天猫精选"图标
	 */
	public static int CUSTOM_AITAOBAO_DRAWABLE = AITAOBAO_DRAWABLE;
	public static int CUSTOM_AITAOBAO_TITLE = AITAOBAO_TITLE;
	public static String CUSTOM_AITAOBAO_PCK_NAME = AITAOBAO_RES_NAME;
	public static String CUSTOM_AITAOBAO_CLS_NAME = LauncherProviderHelper.AITAOBAO_INTENT;
	/**
	 * 匣子内app推荐图标“个性化”
	 */
	public final static int CUSTOM_INDIVIDUAL_DRAWABLE = R.drawable.com_nd_android_pandahome2_manage_shop_themeshopmainactivity;
	public final static int CUSTOM_INDIVIDUAL_TITLE = R.string.theme_shop_v6_app_name;
	public final static String CUSTOM_INDIVIDUAL_PCK_NAME = Global.PKG_NAME +":drawable/com_nd_android_pandahome2_manage_shop_themeshopmainactivity";
	public final static String CUSTOM_INDIVIDUAL_CLS_NAME = MyPhoneDataFactory.INTENT_MY_THEME;

	/**
	 * Description: 匣子内添加"爱淘宝"图标
	 * Author: guojianyun_91 
	 * Date: 2015年5月15日 下午2:27:35
	 */
//	public static void updateAiTaobaoConfig(){
//		CUSTOM_AITAOBAO_DRAWABLE = AITAOBAO_DRAWABLE;
//		CUSTOM_AITAOBAO_TITLE = AITAOBAO_TITLE;
//		CUSTOM_AITAOBAO_PCK_NAME = AITAOBAO_RES_NAME;
//		CUSTOM_AITAOBAO_CLS_NAME = LauncherProviderHelper.AITAOBAO_INTENT;
//	}

	/**
	 * 更新程序的位置信息
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
	public static void updateAppInfosPosition(Context ctx, List<ICommonDataItem> listAppInfos) {
		String[] sqls = new String[listAppInfos.size()];
		int i = 0;
		for (ICommonDataItem appInfo : listAppInfos) {
			long id = -1;
			if (appInfo instanceof ApplicationInfo) {
				id = ((ApplicationInfo) appInfo).id;
			} else if (appInfo instanceof FolderInfo) {
				id = ((FolderInfo) appInfo).id;
			}
			sqls[i] = String.format(UPDATE_POSITION, i, id);
			i++;
		}
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			dbUtil.execBatchSQL(sqls, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 级联更新app位置 <br>
	 * create at 2012-6-20 上午11:08:41 <br>
	 * modify at 2012-6-20 上午11:08:41
	 * 
	 * @param app
	 */
	public static void updateAppPositionCascade(Context ctx, ApplicationInfo app) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String[] sqls = new String[2];
			sqls[0] = "update " + AppTable.TABLE_NAME + " set " + AppTable.POS + " = " + AppTable.POS + " + 1 where " + AppTable.CONTAINER + " = "
					+ ApplicationInfo.CONTAINER_DRAWER + " and " + AppTable.POS + " >= " + app.pos;
			sqls[1] = String.format(UPDATE_CONTAINER_AND_POSITION, app.drawerContainer, app.pos, (int) app.id);
			dbUtil.execBatchSQL(sqls, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 更新程序的container与position信息
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
	public static void updateAppInfosContainerAndPos(Context ctx, long containerId, List<ApplicationInfo> listAppInfos) {
		String[] sqls = new String[listAppInfos.size()];
		int i = 0;
		for (ApplicationInfo appInfo : listAppInfos) {
			sqls[i++] = String.format(UPDATE_CONTAINER_AND_POSITION, containerId, appInfo.pos, appInfo.id);
		}
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			dbUtil.execBatchSQL(sqls, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 更新程序的container与position信息
	 * 
	 * @author pdw
	 * @param ctx
	 * @param appList
	 */
	public static void updateAppInfosContainerAndPos(Context ctx, List<ICommonDataItem> appList) {
		ArrayList<String> sqls = new ArrayList<String>(appList.size());
		String sql;
		for (ICommonDataItem item : appList) {
			if (item instanceof ApplicationInfo) {
				final ApplicationInfo app = (ApplicationInfo) item;
				sql = String.format(UPDATE_CONTAINER_AND_POSITION, app.drawerContainer, app.pos, app.id);
				sqls.add(sql);
			} else if (item instanceof FolderInfo) {
				final FolderInfo folderInfo = (FolderInfo) item;
				sql = String.format(UPDATE_CONTAINER_AND_POSITION, folderInfo.drawerContainer, folderInfo.pos, folderInfo.id);
				sqls.add(sql);
				for (ApplicationInfo info : folderInfo.contents) {
					sql = String.format(UPDATE_CONTAINER_AND_POSITION, info.drawerContainer, info.pos, info.id);
					sqls.add(sql);
				}
			}
		}
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			final String[] sqlsArray = new String[sqls.size()];
			sqls.toArray(sqlsArray);
			dbUtil.execBatchSQL(sqlsArray, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 新建文件夹记录
	 * 
	 * @param ctx
	 * @param folderInfo
	 */
	public static long createFolder(Context ctx, FolderInfo folderInfo) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			ContentValues values = new ContentValues();
			values.put(AppTable.PCK, "");
			values.put(AppTable.CLS, "");
			values.put(AppTable.TITLE, folderInfo.title == null ? "" : folderInfo.title.toString());
			values.put(AppTable.POS, folderInfo.pos);
			values.put(AppTable.TYPE, ApplicationInfo.APP_TYPE_FOLDER);
			folderInfo.id = dbUtil.add(AppTable.TABLE_NAME, values);
			if (folderInfo.id == -1) {
				return -1;
			}

			List<ApplicationInfo> itemList = folderInfo.contents;
			if (itemList == null || itemList.size() == 0)
				return folderInfo.id;

			updateAppInfosContainerAndPos(ctx, folderInfo.id, itemList);

			return folderInfo.id;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}

		return -1;
	}

	/**
	 * 删除文件夹记录
	 * 
	 * @param ctx
	 * @param folderInfo
	 */
	public static void deleteFolder(Context ctx, FolderInfo folderInfo) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			int contentSize = folderInfo.contents.size();
			String sql = "update " + AppTable.TABLE_NAME + " set " + AppTable.POS + " = " + AppTable.POS + " + " + (contentSize - 1) + " where "
					+ AppTable.CONTAINER + " = " + folderInfo.drawerContainer + " and " + AppTable.POS + " > " + folderInfo.pos;
			dbUtil.execSQL(sql);

			int folderPosition = folderInfo.pos;
			List<ApplicationInfo> itemList = folderInfo.contents;
			String[] sqls = new String[itemList.size() + 1];
			int i = 0;
			for (ApplicationInfo appInfo : itemList) {
				sqls[i++] = String.format(UPDATE_CONTAINER_AND_POSITION, ItemInfo.CONTAINER_DRAWER, folderPosition++, appInfo.id);
			}
			sqls[i] = String.format(UPDATE_CONTAINER, ItemInfo.CONTAINER_DRAWER, folderInfo.id);
			dbUtil.execBatchSQL(sqls, true);
			sql = String.format(DELETE, folderInfo.id);
			dbUtil.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}


    /**
     * 隐藏文件夹记录
     *
     * @param ctx
     * @param folderInfo
     */
    public static void hideFolder(Context ctx, FolderInfo folderInfo) {
        AppDataBase dbUtil = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            String sql = "update " + AppTable.TABLE_NAME + " set " + AppTable.ISHIDDEN + " = 1 " + " where "
                    + AppTable.ID + " = " + folderInfo.id;
            dbUtil.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }


    /**
     *显示应用升级文件夹
     *
     * @param ctx
     */
    public static void showUpgradeFolder(Context ctx) {
        AppDataBase dbUtil = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            String sql = "update " + AppTable.TABLE_NAME + " set " + AppTable.ISHIDDEN + " = 0 " + " where "
                    + AppTable.TYPE + " = " + LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER;
            dbUtil.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }

    /**
     *隐藏应用升级文件夹
     *
     * @param ctx
     */
    public static void hideUpgradeFolder(Context ctx) {
        AppDataBase dbUtil = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            String sql = "update " + AppTable.TABLE_NAME + " set " + AppTable.ISHIDDEN + " = 1 " + " where "
                    + AppTable.TYPE + " = " + LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER;
            dbUtil.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }


	/**
	 * 查询匣子中最大position
	 */
/*	public static int getMaxPositionInDrawer(Context ctx) {
		AppDataBase dbUtil = AppDataBase.getInstance(ctx);
		Cursor cursor = null;
		try {
			cursor = dbUtil.query(String.format(SELECT_MAX_POSITION, ItemInfo.CONTAINER_DRAWER));
			cursor.moveToNext();
			int position = cursor.getInt(0);
			return position;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}*/

	/**
	 * 添加程序记录
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
	public static boolean addApps(Context ctx, List<ApplicationInfo> listAppInfos) {
		if (listAppInfos == null || listAppInfos.size() == 0) {
			return false;
		}

		AppDataBase dbUtil = AppDataBase.getInstance(ctx);
		Cursor cursor = null;
		try {
			cursor = dbUtil.query(String.format(SELECT_MAX_POSITION, ItemInfo.CONTAINER_DRAWER));
			cursor.moveToNext();
			int position = cursor.getInt(0);
			for (ApplicationInfo info : listAppInfos) {
				ContentValues values = new ContentValues();
				values.put(AppTable.PCK, info.componentName.getPackageName());
				values.put(AppTable.CLS, info.componentName.getClassName());
				CharSequence title = info.title;
				if (title == null || "".equals(title.toString())) {
					ResolveInfo resolve = AndroidPackageUtils.getResolveInfo(info.intent, ctx.getPackageManager());
					if (resolve != null) {
						title = resolve.loadLabel(ctx.getPackageManager());
					}
				}
				values.put(AppTable.TITLE, title == null ? "" : title.toString());
				values.put(AppTable.POS, ++position);
				values.put(AppTable.INSTALL_TIME, info.installTime);
				values.put(AppTable.ISHIDDEN, info.isHidden);
				info.id = dbUtil.add(AppTable.TABLE_NAME, values);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 删除程序记录，同时更新position
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
	public static void deleteApps(Context ctx, List<ApplicationInfo> listAppInfos) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			for (ApplicationInfo appInfo : listAppInfos) {
				String sql = "update " + AppTable.TABLE_NAME + " set " + AppTable.POS + " = " + AppTable.POS + " - 1 where " + AppTable.CONTAINER
						+ " = " + appInfo.drawerContainer + " and " + AppTable.POS + " > " + appInfo.pos;
				dbUtil.execSQL(sql);
				sql = String.format(DELETE, appInfo.id);
				dbUtil.execSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 更新程序记录, 未变化则不作任何操作, 否则删除程序记录后将新记录加至最后
	 * 
	 * @param packageName
	 * @param listAppInfos
	 *            - listAppInfos为同一packageName的程序集合
	 */
	public static void updateApps(Context ctx, String packageName, List<ApplicationInfo> listAppInfos) {
		boolean isNeedUpdate = false;
		AppDataBase dbUtil = null;
		Cursor cursor = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String sql = String.format(SELECT_APPS_BY_PACKAGE, packageName);
			cursor = dbUtil.query(sql);

			if (cursor == null) {
				return;
			}

			/**
			 * 检测是否完全一致
			 */
			if (cursor.getCount() != listAppInfos.size()) {
				/**
				 * 个数不同
				 */
				isNeedUpdate = true;
			} else {
				/**
				 * 个数相同，则检查入口是否一致
				 */
				while (cursor.moveToNext() && !isNeedUpdate) {
					isNeedUpdate = true;
					String className = cursor.getString(2);
					for (ApplicationInfo info : listAppInfos) {
						info.isHidden = cursor.getInt(6);
						if (info.componentName.getPackageName().equals(packageName) && info.componentName.getClassName().equals(className)) {
							isNeedUpdate = false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}

		if (!isNeedUpdate)
			return;

		deleteAppsByPackage(ctx, packageName);
		addApps(ctx, listAppInfos);
	}

	/**
	 * 通过包名删除应用程序记录，同时更新position信息
	 * 
	 * @param ctx
	 * @param packageName
	 */
	public static void deleteAppsByPackage(Context ctx, String packageName) {
		List<ApplicationInfo> listAppInfos = new ArrayList<ApplicationInfo>();
		AppDataBase dbUtil = null;
		Cursor cursor = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String sql = String.format(SELECT_APPS_BY_PACKAGE, packageName);
			cursor = dbUtil.query(sql);

			if (cursor == null) {
				return;
			}

			while (cursor.moveToNext()) {
				ApplicationInfo info = new ApplicationInfo();
				info.id = cursor.getLong(0);
				info.drawerContainer = cursor.getLong(4);
				info.pos = cursor.getInt(5);
				listAppInfos.add(info);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
		deleteApps(ctx, listAppInfos);
	}

	public static long selectAppId(Context ctx, ApplicationInfo info) {
		AppDataBase dbUtil = null;
		Cursor cursor = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String sql = String.format(SELECT_APP_BY_PACKAGE_AND, info.componentName.getPackageName(), info.componentName.getClassName());
			cursor = dbUtil.query(sql);

			if (cursor == null) {
				return -1;
			}

			cursor.moveToNext();
			return cursor.getLong(AppTable.INDEX_ID);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}

		return -1;
	}

	public static boolean isAppHidden(Context ctx, ApplicationInfo info) {
		AppDataBase dbUtil = null;
		Cursor cursor = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String sql = String.format(SELECT_APP_BY_PACKAGE_AND, info.componentName.getPackageName(), info.componentName.getClassName());
			cursor = dbUtil.query(sql);

			if (cursor == null) {
				return false;
			}

			cursor.moveToNext();
			return cursor.getLong(AppTable.INDEX_ISHIDDEN) == 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}

		return false;
	}

	public static void updateTitle(Context ctx, String title, int id) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			String sql = String.format(UPDATE_TITLE, StringUtil.filtrateInsertParam(title), id);
			dbUtil.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

    public static void updateClassName(Context ctx, long id, String cls) {
        AppDataBase dbUtil = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            String sql = String.format(UPDATE_CLS, cls, id);
            dbUtil.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }

	/**
	 * 加载隐藏程序列表
	 */
	public static List<ApplicationInfo> getHiddenAppsList(Context ctx) {
		AppDataBase dbUtil = null;
		Cursor cursor = null;
		try {
			Bitmap defIcon = BitmapUtils.getDefaultAppIcon(ctx.getResources());
			dbUtil = AppDataBase.getInstance(ctx);
			cursor = dbUtil.query(SELECT_ALL_HIDDEN_APP);
			if (cursor.getCount() == 0) {
				cursor.close();
				dbUtil.close();
				return null;
			}

			List<ApplicationInfo> result = new ArrayList<ApplicationInfo>(cursor.getCount());
			while (cursor.moveToNext()) {
				ApplicationInfo info = new ApplicationInfo();
				info.id = cursor.getInt(AppTable.INDEX_ID);
				String pck = cursor.getString(AppTable.INDEX_PCK);
				String cls = cursor.getString(AppTable.INDEX_CLS);
				info.used_time = cursor.getInt(AppTable.INDEX_USED_TIME);
				info.installTime = cursor.getLong(AppTable.INDEX_INSTALL_TIME);
				ComponentName cn = new ComponentName(pck, cls);
				info.componentName = cn;
				info.setActivity(cn);
                info.itemType = cursor.getInt(AppTable.INDEX_TYPE);
				String title = cursor.getString(AppTable.INDEX_TITLE);
				if (title == null)
					title = "";
				info.title = title;
				info.iconBitmap = defIcon;
				info.pos = cursor.getInt(AppTable.INDEX_POS);
				info.drawerContainer = cursor.getInt(AppTable.INDEX_CONTAINER);
				info.pinyin = cursor.getString(AppTable.INDEX_PINYIN);
				info.isSystem = cursor.getInt(AppTable.INDEX_ISSYSTEM);
				info.isHidden = cursor.getInt(AppTable.INDEX_ISHIDDEN);
                if(info.itemType == LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT){
                    try{
                    	Bitmap icon = null;
            			try{//"个性化"尝试拿主题的图片
            				if(info.intent.getComponent().equals(Intent.parseUri(MyPhoneDataFactory.INTENT_MY_THEME, 0).getComponent())){
            					icon = ApplicationInfoUtil.getIconFromThemeAndAdjustDrawMask(info,  ThemeData.MYPHONE_THEME_SHOP);
            				 }
            			}catch(Exception e){
            				e.printStackTrace();
            			}
            				 
            			int drawableId = ctx.getResources().getIdentifier(pck, null, null);
            			if(icon == null){
                            Drawable d = ctx.getResources().getDrawable(drawableId);
                            icon = BitmapUtils.createIconBitmapFor91Icon(((BitmapDrawable)d).getBitmap(), ctx);
            			}
                        info.iconBitmap = icon;
                        info.intent = Intent.parseUri(cls, 0);
                        info.itemType = LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT;
                        info.iconResource = Intent.ShortcutIconResource.fromContext(ctx, drawableId);
                        info.componentName = cn;
                    } catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }
                }
				result.add(info);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

    /**
     * 加载隐藏程序列表
     */
    public static HashSet<ComponentName> getHiddenApps(Context ctx) {
        HashSet<ComponentName> set = new HashSet<ComponentName>();
        AppDataBase dbUtil = null;
        Cursor cursor = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            cursor = dbUtil.query(SELECT_ALL_HIDDEN_APP);
            if (cursor.getCount() == 0) {
                cursor.close();
                dbUtil.close();
                return set;
            }
            while (cursor.moveToNext()) {
                String pck = cursor.getString(AppTable.INDEX_PCK);
                String cls = cursor.getString(AppTable.INDEX_CLS);
                ComponentName cn = new ComponentName(pck, cls);
                set.add(cn);
            }

            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }

	/**
	 * 所有程序显示
	 * 
	 * @param ctx
	 */
	public static void clearAppInfosHiddenExceptUpgradeFolder(Context ctx) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			dbUtil.execSQL(CLEAR_ISHIDDEN_EXCEPT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 所有程序显示
	 *
	 * @param ctx
	 */
	public static void clearAppInfosHidden(Context ctx) {
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			dbUtil.execSQL(CLEAR_ISHIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 更新程序的隐藏信息
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
	public static void hideAppInfos(Context ctx, List<ApplicationInfo> listAppInfos) {
		String[] sqls = new String[listAppInfos.size()];
		int i = 0;
		for (ApplicationInfo appInfo : listAppInfos) {
			ComponentName component = appInfo.intent.getComponent();
			if(appInfo.itemType == LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT) {
				if(ctx.getString(DrawerDataFactory.CUSTOM_INDIVIDUAL_TITLE).equals(appInfo.title)){
					sqls[i++] = String.format(UPDATE_ISHIDDEN_FOR_CUSTOM, 1, CUSTOM_INDIVIDUAL_PCK_NAME);
				}else if (ctx.getString(DrawerDataFactory.CUSTOM_AITAOBAO_TITLE).equals(appInfo.title)){
					sqls[i++] = String.format(UPDATE_ISHIDDEN_FOR_CUSTOM, 1, CUSTOM_AITAOBAO_PCK_NAME);
				}
            }else{
                sqls[i++] = String.format(UPDATE_ISHIDDEN, 1, component.getPackageName(), component.getClassName());
            }
		}
		AppDataBase dbUtil = null;
		try {
			dbUtil = AppDataBase.getInstance(ctx);
			dbUtil.execBatchSQL(sqls, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
	}

	/**
	 * 加载匣子内所有文件夹内或已被隐藏的程序，并且该程序不在指定文件内。 <br>
	 * 如果指定的文件夹为空，则为匣子内所有文件夹中的程序。
	 * 
	 * @author pdw <br>
	 *         创建于 2012-7-16 上午10:41:12
	 * @param ctx
	 * @param mFolderInfo
	 *            指定文件夹
	 */
	public static void loadAppsInOtherFoldersOrIsHidden(Context ctx, FolderInfo mFolderInfo, List<SerializableAppInfo> outList) {

		long folderId = -1;

		if (outList == null)
			outList = new ArrayList<SerializableAppInfo>();

		if (mFolderInfo != null)
			folderId = mFolderInfo.id;

		String sql = "select %s,%s,%s from %s where " + AppTable.CONTAINER + " not in (%d, %d) or " + AppTable.ISHIDDEN + " = 1";
		sql = String.format(sql, AppTable.TITLE, AppTable.PCK, AppTable.CLS, AppTable.TABLE_NAME, folderId, ItemInfo.CONTAINER_DRAWER);
		AppDataBase db = null;
		Cursor cursor = null;
		try {
			db = AppDataBase.getInstance(ctx);
			cursor = db.query(sql);
			if (cursor != null) {
				boolean flag = cursor.moveToFirst();
				while (flag) {
					ApplicationInfo app = new ApplicationInfo();
					app.title = cursor.getString(0);
					ComponentName com = new ComponentName(cursor.getString(1), cursor.getString(2));
					app.setActivity(com);
					outList.add(app.makeSerializable());
					flag = cursor.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

	}

	/**
	 * 删除分类文件夹
	 * @author dingdj
	 * Date:2014-6-3下午3:46:40
	 *  @param ctx
	 *  @return
	 */
	public static boolean delClassifyAppFolder(Context ctx) {

		boolean result = false;
		AppDataBase db = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(AppTable.TABLE_NAME).append(" where ").append(AppTable.TYPE).append("=")
					.append(ApplicationInfo.APP_TYPE_FOLDER);
			db = AppDataBase.getInstance(ctx);
			result = db.execSQL(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return result;
	}
	
	/**
	 * 是否存在未分类图标
	 * @author dingdj
	 * Date:2014-6-3下午3:46:40
	 *  @param ctx
	 *  @return
	 */
	public static boolean hasUnClassifiedIcon(Context ctx) {
		AppDataBase db = null;
		try {
			db = AppDataBase.getInstance(ctx);
			Cursor cursor = db.query(SELECT_ALL_UN_CLASSIFY_APP);
			if(cursor.moveToNext()){
				cursor.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return false;
	}
	
	
	/**
	 * 删除除了隐藏程序外所有的记录
	 * 
	 * @param ctx
	 * @param listAppInfos
	 */
/*	public static void deleteAllRecordExceptHidden(Context ctx) {
		AppDataBase db = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(AppTable.TABLE_NAME).append(" where ").append(AppTable.ISHIDDEN).append("=")
					.append("0");
			db = AppDataBase.getInstance(ctx);
			db.execSQL(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}*/
	
	
	/**
	 * 根据分组的组名来获取该组下有多少的App
	 * @author dingdj
	 * Date:2014-6-22上午9:32:25
	 *  @param title
	 *  @return
	 */
/*	public static int getOriginalNumByCategoryTitle(Context ctx, String title){
		AppDataBase dbUtil = AppDataBase.getInstance(ctx);
		Cursor cursor = null;
		Cursor appCursor = null;
		try {
			cursor = dbUtil.query(String.format(SELECT_FOLDER_ID, title));
			if(cursor.moveToNext()){
				int folderId = cursor.getInt(0);
				appCursor = dbUtil.query(String.format(SELECT_APP_NUM_BY_FOLDER_ID, folderId));
				if(appCursor.moveToNext()){
					int appCount = appCursor.getInt(0);
					return appCount;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (appCursor != null) {
				appCursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
		}
		return 0;
	}*/
	
	/**
	 * 备份 AppTable 到 /mnt/sdcard/pandahome2/.cache/app.zip
	 */
	public static void backupAppTable(Context ctx) {

		FileUtil.delFile(CACHE_DIR + ZIP_FILE_NAME);// 删除之前可能已经存在的备份文件

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		AppDataBase dbUtil = null;
		Cursor cursor = null;

		try {
			dbUtil = AppDataBase.getInstance(ctx);
			cursor = dbUtil.query(AppTable.SELECT);
			boolean flag = cursor.moveToNext();

			if (!flag || cursor.getCount() == 0)
				return;

			JSONArray total = new JSONArray();

			while (flag) {
				JSONObject obj = new JSONObject();

				obj.put(AppTable.ID, cursor.getInt(AppTable.INDEX_ID));
				obj.put(AppTable.PCK, cursor.getString(AppTable.INDEX_PCK));
				obj.put(AppTable.CLS, cursor.getString(AppTable.INDEX_CLS));
				obj.put(AppTable.TITLE, cursor.getString(AppTable.INDEX_TITLE));
				obj.put(AppTable.SCREEN, cursor.getInt(AppTable.INDEX_SCREEN));
				obj.put(AppTable.POS, cursor.getInt(AppTable.INDEX_POS));
				obj.put(AppTable.CONTAINER, cursor.getInt(AppTable.INDEX_CONTAINER));
				obj.put(AppTable.TYPE, cursor.getInt(AppTable.INDEX_TYPE));
				obj.put(AppTable.INSTALL_TIME, cursor.getInt(AppTable.INDEX_INSTALL_TIME));
				obj.put(AppTable.USED_TIME, cursor.getInt(AppTable.INDEX_USED_TIME));
				obj.put(AppTable.CELLX, cursor.getInt(AppTable.INDEX_CELL_X));
				obj.put(AppTable.CELLY, cursor.getInt(AppTable.INDEX_CELL_Y));
				obj.put(AppTable.ISSYSTEM, cursor.getInt(AppTable.INDEX_ISSYSTEM));
				obj.put(AppTable.PINYIN, cursor.getString(AppTable.INDEX_PINYIN));
				obj.put(AppTable.ISHIDDEN, cursor.getInt(AppTable.INDEX_ISHIDDEN));

				total.put(obj);

				flag = cursor.moveToNext();
			}

			if (0 == total.length())
				return;

			String jsonString = total.toString();
			FileUtil.writeFile(CACHE_DIR + DATA_FILE_NAME, jsonString, false);

			java.io.File zipFile = new java.io.File(CACHE_DIR + ZIP_FILE_NAME);
			java.io.File dataFile = new java.io.File(CACHE_DIR + DATA_FILE_NAME);

			if (dataFile.exists()) {
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				ZipEntry zipEntry = new ZipEntry(DATA_FILE_NAME);
				zos.putNextEntry(zipEntry);

				fis = new FileInputStream(CACHE_DIR + DATA_FILE_NAME);
				bis = new BufferedInputStream(fis, 1024 * 10);
				int read = 0;
				byte[] bufs = new byte[1024 * 10];
				while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
					zos.write(bufs, 0, read);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtil.delFile(CACHE_DIR + DATA_FILE_NAME); // 删除暂时的JSON文件
			if (cursor != null) {
				cursor.close();
			}
			if (dbUtil != null) {
				dbUtil.close();
			}
			// 关闭流
			try {
				if (null != zos) {
					zos.flush();
					zos.close();
				}
				if (null != bis)
					bis.close();
				if (null != fis)
					fis.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 恢复 /mnt/sdcard/pandahome2/.cache/app.zip 数据到 AppTable
	 * 
	 * @param ctx
	 */
	public static void restoreAppTable(Context ctx) {
		FileUtil.delFile(CACHE_DIR + DATA_FILE_NAME);
		if (!FileUtil.isFileExits(CACHE_DIR + ZIP_FILE_NAME))
			return;
		OutputStream os = null;
		InputStream is = null;
		try {
			ZipFile zip = new ZipFile(CACHE_DIR + ZIP_FILE_NAME);
			ZipEntry entry = zip.getEntry(DATA_FILE_NAME);
			if (null == entry)
				return;
			os = new BufferedOutputStream(new FileOutputStream(CACHE_DIR + DATA_FILE_NAME));
			is = new BufferedInputStream(zip.getInputStream(entry));
			int readLen = 0;
			byte[] buf = new byte[1024 * 10];
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
				if (null != os) {
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 解析json
		String jsonString = FileUtil.readFileContent(CACHE_DIR + DATA_FILE_NAME);
		if (null == jsonString) {
			FileUtil.delFile(CACHE_DIR + DATA_FILE_NAME);// 放在最后
			return;
		}
		AppDataBase dbUtil = null;
		try {
			JSONTokener jsonParser = new JSONTokener(jsonString);
			JSONArray array = (JSONArray) jsonParser.nextValue();

			if (null == array || array.length() == 0)
				return;
			dbUtil = AppDataBase.getInstance(ctx);
			
			String[] sqls = new String[array.length()+1];
			// 清空AppTable表的数据 delete from AppTable
			sqls[0] = DELETE_ALL_DATA;
			//long tim = System.currentTimeMillis();
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				int ID = obj.getInt(AppTable.ID);
				String PCK = obj.getString(AppTable.PCK);
				String CLS = obj.getString(AppTable.CLS);
				String TITLE = StringUtil.filtrateInsertParam(obj.getString(AppTable.TITLE));
				int SCREEN = obj.getInt(AppTable.SCREEN);
				int POS = obj.getInt(AppTable.POS);
				int CONTAINER = obj.getInt(AppTable.CONTAINER);
				int TYPE = obj.getInt(AppTable.TYPE);
				int INSTALL_TIME = obj.getInt(AppTable.INSTALL_TIME);
				int USED_TIME = obj.getInt(AppTable.USED_TIME);
				int CELLX = obj.getInt(AppTable.CELLX);
				int CELLY = obj.getInt(AppTable.CELLY);
				int ISSYSTEM = obj.getInt(AppTable.ISSYSTEM);
				String PINYIN = "";
				if(!obj.isNull(AppTable.PINYIN)){
					PINYIN = obj.getString(AppTable.PINYIN);
				}
				int ISHIDDEN = obj.getInt(AppTable.ISHIDDEN);

				sqls[i+1] = String.format(INSERT_DATA, ID, PCK, CLS, TITLE, SCREEN, POS, CONTAINER, TYPE, INSTALL_TIME, USED_TIME, CELLX, CELLY,
						ISSYSTEM, PINYIN, ISHIDDEN);
			}
			// 插入数据库
			dbUtil.execBatchSQL(sqls, true);
		} catch (Exception e) {
			FileUtil.delFile(CACHE_DIR + ZIP_FILE_NAME);// 删除失效的备份文件
			e.printStackTrace();
		} finally {
			if (null != dbUtil)
				dbUtil.close();
			FileUtil.delFile(CACHE_DIR + DATA_FILE_NAME);// 放在最后
			// 对匣子进行维护，删除已卸载的app图标，增加新安装的app图标
			SQLiteDatabase dataAppDb = null;
			try {
				List<ApplicationInfo> resultFromPckManager = AppDataFactory.loadApplicationInfosFromPackageManager(ctx, null);
				String namespace = ctx.getPackageName();
				dataAppDb = SQLiteDatabase.openDatabase(Environment.getDataDirectory() + BackupSetting.DATA_DIR + namespace
						+ BackupSetting.DATABASE_DIR + BackupSetting.FILENAME_DRAWERDB, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				AppDataFactory.loadDrawerAppFromDB(ctx, dataAppDb, resultFromPckManager);// 方法中已经关闭了dataAppDb
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除备份文件
	 * @author dingdj
	 * Date:2014-6-27上午9:35:25
	 */
	public static void clearAppClassifyBackupFile(){
		FileUtil.delFile(CACHE_DIR + ZIP_FILE_NAME);// 删除备份文件
	}

	/**
	 * 是否为匣子内的"爱淘宝"图标
	 * @param str
	 * @return
	 */
	public static boolean isAiTaobao(String str){
		return !StringUtil.isEmpty(str) && str.contains(CUSTOM_AITAOBAO_PCK_NAME);
	}
	
	/**
	 * 获取"爱淘宝"的ComponentName
	 * @return
	 */
	public static ComponentName getAiTaobaoComponentName(){
		return new ComponentName(CUSTOM_AITAOBAO_PCK_NAME, CUSTOM_AITAOBAO_CLS_NAME);
	}
	
	/**
	 * 是否为匣子内的“个性化”图标
	 */
	public static boolean isGeXingHua(String str){
		return !StringUtil.isEmpty(str) && str.contains(CUSTOM_INDIVIDUAL_PCK_NAME);
	}
	
	/**
	 * 获取"个性化"的ComponentName
	 * @return
	 */
	public static ComponentName getGeXingHuaComponentName(){
		ComponentName c = null;
		try {
			c = new ComponentName(CUSTOM_INDIVIDUAL_PCK_NAME, Intent.parseUri(CUSTOM_INDIVIDUAL_CLS_NAME, 0).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	 /**
     * 直接删除文件夹文件夹记录
     *
     * @param ctx
     * @param folderInfo
     */
    public static void deleteFolderDirect(Context ctx, FolderInfo folderInfo) {
        AppDataBase dbUtil = null;
        try {
            dbUtil = AppDataBase.getInstance(ctx);
            String sql =null;
            sql = String.format(DELETE, folderInfo.id);
            dbUtil.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbUtil != null) {
                dbUtil.close();
            }
        }
    }
}
