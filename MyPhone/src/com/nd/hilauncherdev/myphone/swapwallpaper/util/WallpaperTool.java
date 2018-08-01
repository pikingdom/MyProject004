package com.nd.hilauncherdev.myphone.swapwallpaper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.kitset.util.WallpaperUtil2;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.myphone.swapwallpaper.WallpaperStyleActivity;
import com.nd.hilauncherdev.myphone.swapwallpaper.bean.SwapWallpaperInfo;
import com.nd.hilauncherdev.myphone.swapwallpaper.bean.WallPaperContants;
import com.nd.hilauncherdev.myphone.swapwallpaper.bean.WallpaperItemInfo;
import com.nd.hilauncherdev.myphone.swapwallpaper.db.WallpaperDbHelper;
import com.nd.hilauncherdev.net.ServerResultHeader;
import com.nd.hilauncherdev.net.ThemeHttpCommon;
import com.nd.hilauncherdev.url.URLs;

/**
 * 一键壁纸工具类
 * 
 * @author fmj
 */
public class WallpaperTool {
	private static WallpaperTool instance = null;
	public static final String SWAP_WALLPAPER = "swapWallpaper";
	private static final int MAX_SWAP_FILTER_LIST_SIZE = 1000;

	/**
	 * 可用本地壁纸
	 */
	private List<WallpaperItemInfo> localWallpaperInfos;

	/**
	 * 在线壁纸
	 */
	private List<WallpaperItemInfo> onLineWallpaperInfos;

	/**
	 * 本地所有壁纸
	 */
	private List<WallpaperItemInfo> allWallpaperInfos;

	/**
	 * 当前壁纸
	 */
	private WallpaperItemInfo wallpaperItemInfo;

	/**
	 * 是否更换壁纸中
	 */
	private static boolean setWallPaperIng = false;

	private Handler showHandler = new Handler();

	private static ExecutorService executorService = Executors.newFixedThreadPool(1);

	/**
	 * 当前应用的壁纸路径
	 */
	public static String localPicPath;

	/**
	 * 是否限制壁纸缓存操作
	 */
	private boolean deleteOneKeywPaper = false;

	/**
	 * 一键换壁纸设置工具类
	 */
	private SwapWallpaperSettingUtil settingUtil = null;

	/**
	 * 壁纸缓存操作间隔时间
	 */
	private static final int DELETE_WPAPER_SP_TIME = 60 * 1000;

	/**
	 * 限制缓存操作间隔时间
	 */
	private static final String LIMIT_CACHE_SP_TIME = "limit_cache_sp_time";

	public static final String DELETE_ONE_KEY_WPAPER = "delete_one_key_wpaper";

	private final static String SWAP_LOCAL_WALLPAPER_INDEX = "swapLocalWallpaperIndex";
	private final static String SWAP_LOCAL_WALLPAPER_HINT = "swapLocalWallpaperHint";
	private SharedPreferences sp;

	private boolean changeWallpaperTimeout = false;

	// 6.1一键换壁纸新增
	private Object mGetUnusedLock = new Object();
	private List<WallpaperItemInfo> mUnusedList;
	private boolean mGettingUnused = false;

	public static WallpaperTool getInstance() {
		if (instance == null) {
			instance = new WallpaperTool();
		}
		return instance;
	}

	private WallpaperTool() {
	}

	/**
	 * 更换本地壁纸
	 * 
	 * @param mContext
	 */
	public void swapLocalWallPaper(final Context mContext, final Handler mHandler, final int message) {
		// 更换壁纸中
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				swapLocalWallPaperInner(mContext, mHandler, message);
			}
		});
	}

	private SharedPreferences getSharedPreferences() {
		if (sp == null) {
			sp = BaseConfig.getApplicationContext().getSharedPreferences(SWAP_WALLPAPER, Context.MODE_PRIVATE);
		}
		return sp;
	}

	public void setSwapLocalWallpaperIndex(int value) {
		getSharedPreferences().edit().putInt(SWAP_LOCAL_WALLPAPER_INDEX, value).commit();
	}

	public int getSwapLocalWallpaperIndex() {
		return getSharedPreferences().getInt(SWAP_LOCAL_WALLPAPER_INDEX, 0);
	}

	public void setSwapLocalWallpaperHint(boolean value) {
		getSharedPreferences().edit().putBoolean(SWAP_LOCAL_WALLPAPER_HINT, value).commit();
	}

	public boolean isSwapLocalWallpaperHint() {
		return getSharedPreferences().getBoolean(SWAP_LOCAL_WALLPAPER_HINT, true);
	}

	public List<WallpaperItemInfo> getLocalWallpapers(Context mContext) {
		List<WallpaperItemInfo> allWallpaperInfos = new ArrayList<WallpaperItemInfo>();
		String wPaperTypeId = getSharedPreferences().getString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID);
		String str[] = wPaperTypeId.split(WallpaperStyleActivity.SPLIT_STR);
		if (!wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
			// 本地一键分类壁纸
			String query_sql_2 = WallpaperDbHelper.QUERY_SQL + " where typeId in(" + buildTigid(str) + ") order by id";
			allWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, query_sql_2, str);
		}
		if (allWallpaperInfos == null || allWallpaperInfos.size() < 1) {
			// 本地全部 壁纸
			allWallpaperInfos = getAllWallpaperList();
			// if
			// (!wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID))
			// {
			// if (System.currentTimeMillis() - curTime > DELETE_WPAPER_SP_TIME)
			// {
			// curTime = System.currentTimeMillis();
			// showToast(mContext,mContext.getString(R.string.swap_wallpaper_no_wallpaper_type));
			// }
			// }
		}
		return allWallpaperInfos;
	}

	private void swapLocalWallPaperInner(Context mContext, Handler mHandler, int message) {
		boolean toNext = true;
		WallpaperItemInfo result = null;
		boolean toastOrSendMsg = true;
		if (mHandler == null)
			toastOrSendMsg = false;
		List<WallpaperItemInfo> allWallpaperInfos = getLocalWallpapers(mContext);
		if (allWallpaperInfos != null && allWallpaperInfos.size() > 0) {
			int wallpaperIndex = getSwapLocalWallpaperIndex();
			int size = allWallpaperInfos.size();
			int index = 0;
			while (toNext && index <= size) {
				wallpaperIndex++;
				wallpaperIndex %= allWallpaperInfos.size();
				result = allWallpaperInfos.get(wallpaperIndex);
				if (result != null && new File(result.localPath).exists())
					toNext = false;
				if (result != null && !new File(result.localPath).exists()) {
					WallpaperDbHelper.execSQL(mContext, String.format(WallpaperDbHelper.DELETE_SQL, result.wallPaperurl));
				}
				index++;
			}

			if (result != null && applyWallpaper(mContext, result.localPath)) {
				moveTmpWallpaper(mContext, result);

				setSwapLocalWallpaperIndex(wallpaperIndex);
				Message msg = new Message();
				msg.what = message;
				msg.obj = result.localPath;
				if (toastOrSendMsg) {
					mHandler.sendMessage(msg);
				}
			}
			if (toastOrSendMsg) {
				if (allWallpaperInfos.size() == 1) {
					showToast(mContext, mContext.getString(R.string.swap_wallpaper_open_gprs_swap_wallpaper));
				} else if (isSwapLocalWallpaperHint()) {
					showToast(mContext, mContext.getString(R.string.swap_wallpaper_msg3));
					setSwapLocalWallpaperHint(false);
				}
			}
		} else {
			if (toastOrSendMsg) {
				showToast(mContext, mContext.getString(R.string.swap_wallpaper_open_gprs_swap_wallpaper));
			}
			return;
		}
	}

	private String buildTigid(String str[]) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			buffer.append("?");
			if (i < str.length - 1) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}

	/**
	 * 一键换壁纸
	 */
	public synchronized void swapWallPaper(final Context mContext, final Handler mHandler, final int message) {
		if (settingUtil == null) {
			settingUtil = new SwapWallpaperSettingUtil(mContext);
		}
		final SharedPreferences sp = getSharedPreferences();
		// 更换壁纸中
		if (setWallPaperIng) {
			return;
		}
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				boolean getAllWpapers = false;// 是否读取过数据库，获取全部壁纸
				String lQuerySql = "";
				String aQuerySql = "";
				String tagId = "";
				String str[] = null;
				String lSelectionArgs[] = null;
				String aSelectionArgs[] = null;
				setWallPaperIng = true;
				// 当前壁纸分类（类型id。默认:全部，id:10000）
				String wPaperTypeId = sp.getString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID);
				// 全部情况
				if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
					lQuerySql = WallpaperDbHelper.QUERY_SQL_BY_ISUSED;
					lSelectionArgs = new String[] { "0" };
					aQuerySql = WallpaperDbHelper.QUERY_SQL_1;
					aSelectionArgs = new String[] {};
				} else {
					// 分类情况
					tagId = "&Tagid=" + wPaperTypeId.replace(",", "|");
					str = wPaperTypeId.split(WallpaperStyleActivity.SPLIT_STR);
					lSelectionArgs = str;
					aSelectionArgs = str;
					lQuerySql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")order by id";
					aQuerySql = WallpaperDbHelper.QUERY_SQL + " where typeId in(" + buildTigid(str) + ")";
				}
				localWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, lQuerySql, lSelectionArgs);				

				// 限制壁纸缓存数量
				deleteOneKeywPaper = sp.getBoolean(DELETE_ONE_KEY_WPAPER, false);
				long time = settingUtil.getLong(LIMIT_CACHE_SP_TIME, -1);
				if (time == -1 || System.currentTimeMillis() - time > DELETE_WPAPER_SP_TIME || deleteOneKeywPaper) {
					String sizeStr = sp.getString(SwapWallpaperSettingUtil.WALLPAPER_CACHE_SIZE, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_CACHE_SIZE);
					if (!sizeStr.equals(mContext.getString(R.string.swap_wallpaper_unlimited))) {
						final int cacheSize = Integer.parseInt(sizeStr);

						List<WallpaperItemInfo> tempList = WallpaperDbHelper.queryWallpaperFromDB(mContext, WallpaperDbHelper.QUERY_SQL_BY_ISUSED, new String[] { "1" });
						if (tempList != null && tempList.size() - cacheSize > 0) {
							int toDeleteSize = tempList.size() - cacheSize;
							final List<WallpaperItemInfo> toDeleteList = new ArrayList<WallpaperItemInfo>();
							for (int i = 0; i < toDeleteSize; i++) {
								toDeleteList.add(tempList.get(i));
							}

							WallpaperDbHelper.deleteUsedWallpaper(mContext, toDeleteList);
							ThreadUtil.executeMore(new Runnable() {
								@Override
								public void run() {
									deleteUsedWallpaper(mContext, toDeleteList);

									settingUtil.setLong(LIMIT_CACHE_SP_TIME, System.currentTimeMillis());
									deleteOneKeywPaper = false;
									sp.edit().putBoolean(DELETE_ONE_KEY_WPAPER, deleteOneKeywPaper).commit();
								}
							});
						}

						// List<WallpaperItemInfo> tempList =
						// WallpaperDbHelper.queryWallpaperFromDB(mContext,
						// WallpaperDbHelper.QUERY_SQL_1, new String[]{});
						// if (tempList != null && tempList.size() - cacheSize >
						// 0)
						// WallpaperDbHelper.execSQL(mContext,
						// String.format(WallpaperDbHelper.DELETE_SQL_LOCALPATH,
						// tempList.size() - cacheSize));
						// ThreadUtil.executeMore(new Runnable() {
						// @Override
						// public void run() {
						// delwPaperCache(mContext, getWPPicOneKeyPath(),
						// cacheSize);
						// settingUtil.setLong(LIMIT_CACHE_SP_TIME,
						// System.currentTimeMillis());
						// deleteOneKeywPaper = false;
						// sp.edit().putBoolean(DELETE_ONE_KEY_WPAPER,deleteOneKeywPaper).commit();
						// }
						// });
					}
				}

				// 如果本地壁纸都已经被设置过了，首先获取推荐壁纸
				if (localWallpaperInfos == null || localWallpaperInfos.size() < 1) {
					if (WallPaperUtil.isNetworkAvailable(mContext)) {
						SwapWallpaperInfo swapWallpaperInfo = getRecommendWp();
						if (swapWallpaperInfo == null) {
							swapWallpaperInfo = new SwapWallpaperInfo();
							swapWallpaperInfo.code = -1;
						}
						int code = swapWallpaperInfo.code;
						if (code == 0) {
							String lSql = "";
							onLineWallpaperInfos = swapWallpaperInfo.WallpaperItemInfos;
							if (onLineWallpaperInfos != null && onLineWallpaperInfos.size() > 0) {
								if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
									lSql = String.format(WallpaperDbHelper.QUERY_SQL_BY_ISUSED_1, 0);
								} else {
									lSql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")";
								}
								if(!getAllWpapers){
									allWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, aQuerySql, aSelectionArgs);
									getAllWpapers = true;
								}
								WallpaperDbHelper.insertWallpaperInfo(mContext, filterSameWallpaper(onLineWallpaperInfos, allWallpaperInfos), WallPaperUtil.getWPPicOneKeyHome());
								// 获取数据库中未被设置过的壁纸(isUsed = 0)
								localWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, lSql, str);

								clearFilterListIfNeed(mContext);
							}
						}
					}
				}

				// 如果本地壁纸都已经被设置过了，向服务器请求壁纸
				if (localWallpaperInfos == null || localWallpaperInfos.size() < 1) {
					if (WallPaperUtil.isNetworkAvailable(mContext)) {
						SwapWallpaperInfo swapWallpaperInfo = sendRequestWallpaper(tagId);
						if (swapWallpaperInfo == null) {
							swapWallpaperInfo = new SwapWallpaperInfo();
							swapWallpaperInfo.code = -1;
						}
						int code = swapWallpaperInfo.code;
						if (code == 0) {
							String lSql = "";
							onLineWallpaperInfos = swapWallpaperInfo.WallpaperItemInfos;
							if (onLineWallpaperInfos != null && onLineWallpaperInfos.size() > 0) {
								if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
									lSql = String.format(WallpaperDbHelper.QUERY_SQL_BY_ISUSED_1, 0);
								} else {
									lSql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")";
								}
								if(!getAllWpapers){
									allWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, aQuerySql, aSelectionArgs);
									getAllWpapers = true;
								}
								WallpaperDbHelper.insertWallpaperInfo(mContext, filterSameWallpaper(onLineWallpaperInfos, allWallpaperInfos), WallPaperUtil.getWPPicOneKeyHome());
								// 获取数据库中未被设置过的壁纸(isUsed = 0)
								localWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, lSql, str);

								clearFilterListIfNeed(mContext);
							} else {
								// 返回code=0,当无数据时
								swapLocalWallPaperInner(mContext, mHandler, message);
								setWallPaperIng = false;
								return;
							}
						} else {
							swapLocalWallPaperInner(mContext, mHandler, message);
							setWallPaperIng = false;
							return;
						}
					}
				}

				if (allWallpaperInfos != null) {
					allWallpaperInfos.clear();
				}

				if (localWallpaperInfos != null && localWallpaperInfos.size() > 0) {
					wallpaperItemInfo = localWallpaperInfos.get(0);
				} else {
					swapLocalWallPaperInner(mContext, mHandler, message);
					setWallPaperIng = false;
					return;
				}
				if (!isChangeWallpaperTimeout()) {
					RedirectionLoadWallPaper(mContext, mHandler, message, wallpaperItemInfo);
				} else {
					swapLocalWallPaperInner(mContext, null, -1);
					setChangeWallpaperTimeout(false);
					setWallPaperIng = false;
				}
			}
		});
	}

	/**
	 * 向服务器请求推荐壁纸
	 */
	private SwapWallpaperInfo getRecommendWp() {
		String url = "http://pandahome.ifjing.com/action.ashx/wallpaperaction/7001";
		
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		List<WallpaperItemInfo> wallpaperItemInfos;
		WallpaperItemInfo wallpaperItemInfo;
		SwapWallpaperInfo swapWallpaperInfo = new SwapWallpaperInfo();
		try {
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			ThemeHttpCommon.addGlobalRequestValue(paramsMap, CommonGlobal.getApplicationContext(), "");
			ThemeHttpCommon httpCommon = new ThemeHttpCommon(url);
			ServerResultHeader csResult = httpCommon.getResponseAsCsResultPost(paramsMap, "");
			if (csResult != null) {
				jsonObject = new JSONObject(csResult.getResponseJson());
			}
			
			if (jsonObject == null)
				return null;

			if (jsonObject.has("PicList")) {
				jsonArray = jsonObject.getJSONArray("PicList");
				if (jsonArray != null) {
					swapWallpaperInfo.code = 0;
					wallpaperItemInfos = new ArrayList<WallpaperItemInfo>(jsonArray.length());
					for (int i = 0; null != jsonArray && i < jsonArray.length(); i++) {
						wallpaperItemInfo = new WallpaperItemInfo();
						JSONObject object = jsonArray.getJSONObject(i);
						if(object.has("ID")){
							wallpaperItemInfo.typeId = object.getString("ID");
						}
						if(object.has("Url")){
							wallpaperItemInfo.wallPaperurl = object.getString("Url");
						}
						if (wallpaperItemInfo.wallPaperurl != null && !wallpaperItemInfo.wallPaperurl.equals("")) {
							int length = wallpaperItemInfo.wallPaperurl.lastIndexOf("/");
							wallpaperItemInfo.fileName = wallpaperItemInfo.wallPaperurl.substring(length, wallpaperItemInfo.wallPaperurl.length());
						}
						wallpaperItemInfos.add(wallpaperItemInfo);
					}
					swapWallpaperInfo.WallpaperItemInfos = wallpaperItemInfos;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return swapWallpaperInfo;
	}

	/**
	 * （6.1版本一键换壁纸改造新增）
	 */
	public void beforeGetUnusedWallpaper(Context context) {
		getSharedPreferences();
		if (settingUtil == null) {
			settingUtil = new SwapWallpaperSettingUtil(context);
		}
	}

	/**
	 * 删除超过最大缓存限制的使用过的壁纸（6.1版本一键换壁纸改造新增）
	 */
	public void deleteUsedWallpaper(Context context, List<WallpaperItemInfo> toDeleteInfos) {
		if (toDeleteInfos == null || toDeleteInfos.size() <= 0) {
			return;
		}

		for (WallpaperItemInfo info : toDeleteInfos) {
			if (info == null || StringUtil.isEmpty(info.localPath)) {
				continue;
			}

			File file = new File(info.localPath);
			if (file.exists()) {
				FileUtil.delFile(info.localPath);
			}
		}
	}

	/**
	 * （6.1版本一键换壁纸改造新增）
	 */
	public void afterGetUnusedList() {
		if (mUnusedList != null) {
			mUnusedList.clear();
			mUnusedList = null;
		}
		mGettingUnused = false;
	}

	/**
	 * （6.1版本一键换壁纸改造新增）
	 */
	public void selectUnusedWallpaper(Context context, WallpaperItemInfo info) {
		moveTmpWallpaper(context, info);
	}

	/**
	 * 将壁纸从临时目录移到正式目录（6.1版本一键换壁纸改造新增）
	 */
	private boolean moveTmpWallpaper(Context context, WallpaperItemInfo info) {
		if (info == null || StringUtil.isEmpty(info.localPath) || !info.localPath.contains(WallPaperContants.PICTURES_ONEKEY_TMP)) {
			return false;
		}

		try {
			String oldPath = info.localPath;
			info.localPath = info.localPath.replace(WallPaperContants.PICTURES_ONEKEY_TMP, WallPaperContants.PICTURES_ONEKEY_HOME);
			FileUtil.moveFile(oldPath, info.localPath);
			String sql = String.format(WallpaperDbHelper.UPDATE_SQL_ISUSED, 1, info.id);
			WallpaperDbHelper.execSQL(context, sql);
			sql = String.format(WallpaperDbHelper.UPDATE_SQL_LOCAL_PATH, info.localPath, info.id);
			WallpaperDbHelper.execSQL(context, sql);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 将未使用过的且浏览过的壁纸移到过滤名单，之后不再向用户显示该壁纸（6.1版本一键换壁纸改造新增）
	 * 
	 * @return 壁纸信息
	 */
	public void moveToFilterList(Context context, List<WallpaperItemInfo> toMoveInfos) {
		if (toMoveInfos == null || toMoveInfos.size() <= 0) {
			return;
		}

		for (WallpaperItemInfo info : toMoveInfos) {
			if (info == null || StringUtil.isEmpty(info.localPath)) {
				continue;
			}

			File file = new File(info.localPath);
			if (file.exists()) {
				FileUtil.delFile(info.localPath);
			}
		}

		WallpaperDbHelper.moveToFilterList(context, toMoveInfos);
	}

	/**
	 * 当被过滤的壁纸达到上限时，重置过滤列表（6.1版本一键换壁纸改造新增）
	 */
	private void clearFilterListIfNeed(Context context) {
		int filterSize = WallpaperDbHelper.getFilterListSize(context);
		if (filterSize >= MAX_SWAP_FILTER_LIST_SIZE) {
			WallpaperDbHelper.clearFilterList(context);
		}
	}

	/**
	 * 获取一张未使用过的壁纸，如果本地没有，则从服务端获取（6.1版本一键换壁纸改造新增）
	 * 
	 * @return 壁纸信息
	 */
	public WallpaperItemInfo getUnusedWallpaper(final Context mContext) {
		if (sp == null) {
			return null;
		}

		WallpaperItemInfo unusedInfo = null;
		synchronized (mGetUnusedLock) {
			String lQuerySql = "";
			String aQuerySql = "";
			String tagId = "";
			String str[] = null;
			String lSelectionArgs[] = null;
			String aSelectionArgs[] = null;
			setWallPaperIng = true;
			// 当前壁纸分类（类型id。默认:全部，id:10000）
			String wPaperTypeId = sp.getString(SwapWallpaperSettingUtil.WALLPAPER_TYPE_ID, SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID);
			// 全部情况
			if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
				lQuerySql = WallpaperDbHelper.QUERY_SQL_BY_ISUSED;
				lSelectionArgs = new String[] { "0" };
				aQuerySql = WallpaperDbHelper.QUERY_SQL_1;
				aSelectionArgs = new String[] {};
			} else {
				// 分类情况
				tagId = "&Tagid=" + wPaperTypeId.replace(",", "|");
				str = wPaperTypeId.split(WallpaperStyleActivity.SPLIT_STR);
				lSelectionArgs = str;
				aSelectionArgs = str;
				lQuerySql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")order by id";
				aQuerySql = WallpaperDbHelper.QUERY_SQL + " where typeId in(" + buildTigid(str) + ")";
			}
			if (mUnusedList == null) {
				mUnusedList = WallpaperDbHelper.queryWallpaperFromDB(mContext, lQuerySql, lSelectionArgs);
			}
			allWallpaperInfos = WallpaperDbHelper.queryWallpaperFromDB(mContext, aQuerySql, aSelectionArgs);

			// 如果本地壁纸都已经被设置过了，首先获取推荐壁纸
			if (mUnusedList == null || mUnusedList.size() < 1) {
				if (WallPaperUtil.isNetworkAvailable(mContext)) {
					SwapWallpaperInfo swapWallpaperInfo = getRecommendWp();
					if (swapWallpaperInfo == null) {
						swapWallpaperInfo = new SwapWallpaperInfo();
						swapWallpaperInfo.code = -1;
					}
					int code = swapWallpaperInfo.code;
					if (code == 0) {
						String lSql = "";
						onLineWallpaperInfos = swapWallpaperInfo.WallpaperItemInfos;
						if (onLineWallpaperInfos != null && onLineWallpaperInfos.size() > 0) {
							if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
								lSql = String.format(WallpaperDbHelper.QUERY_SQL_BY_ISUSED_1, 0);
							} else {
								lSql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")";
							}
							List<WallpaperItemInfo> newList = filterSameWallpaper(onLineWallpaperInfos, allWallpaperInfos);
							WallpaperDbHelper.insertWallpaperInfo(mContext, newList, WallPaperUtil.getWPPicOneKeyTmpHome());

							clearFilterListIfNeed(mContext);

							if (newList != null && newList.size() > 0) {
								if (mUnusedList == null) {
									mUnusedList = new ArrayList<WallpaperItemInfo>();
								}
								mUnusedList.addAll(newList);
							};
						}
					}
				}
			}
			
			// 如果本地壁纸都已经被设置过了，向服务器请求壁纸
			if (mUnusedList == null || mUnusedList.size() < 1) {
				if (WallPaperUtil.isNetworkAvailable(mContext)) {
					SwapWallpaperInfo swapWallpaperInfo = sendRequestWallpaper(tagId);
					if (swapWallpaperInfo == null) {
						swapWallpaperInfo = new SwapWallpaperInfo();
						swapWallpaperInfo.code = -1;
					}
					int code = swapWallpaperInfo.code;
					if (code == 0) {
						String lSql = "";
						onLineWallpaperInfos = swapWallpaperInfo.WallpaperItemInfos;
						if (onLineWallpaperInfos != null && onLineWallpaperInfos.size() > 0) {
							if (wPaperTypeId.equals(SwapWallpaperSettingUtil.ONLINE_WALLPAPER_TYPE_ID)) {
								lSql = String.format(WallpaperDbHelper.QUERY_SQL_BY_ISUSED_1, 0);
							} else {
								lSql = WallpaperDbHelper.QUERY_SQL + " where isUsed='0' and typeId in(" + buildTigid(str) + ")";
							}
							List<WallpaperItemInfo> newList = filterSameWallpaper(onLineWallpaperInfos, allWallpaperInfos);
							WallpaperDbHelper.insertWallpaperInfo(mContext, newList, WallPaperUtil.getWPPicOneKeyTmpHome());

							clearFilterListIfNeed(mContext);

							if (newList != null && newList.size() > 0) {
								if (mUnusedList == null) {
									mUnusedList = new ArrayList<WallpaperItemInfo>();
								}
								mUnusedList.addAll(newList);
							}
						} else {
							// 返回code=0,当无数据时
							setWallPaperIng = false;
							return null;
						}
					} else {
						setWallPaperIng = false;
						return null;
					}
				}
			}

			if (allWallpaperInfos != null) {
				allWallpaperInfos.clear();
			}

			if (mUnusedList != null && mUnusedList.size() > 0) {
				unusedInfo = mUnusedList.remove(0);
			} else {
				setWallPaperIng = false;
				return null;
			}
		}

		mGettingUnused = true;
		return (unusedInfo != null ? getWallpaperFromServer(mContext, unusedInfo) : null);
	}

	/**
	 * 从服务端下载壁纸（6.1版本一键换壁纸改造新增）
	 * 
	 * @return 壁纸路径
	 */
	private WallpaperItemInfo getWallpaperFromServer(Context context, WallpaperItemInfo info) {
		if (info.localPath != null) {
			File f = new File(info.localPath);
			if (f.exists()) {
				return info;
			}
		}

		if (!TelephoneUtil.isNetworkAvailable(context)) {
			return null;
		}

		String url320 = HttpCommon.getRedirectionURL(info.wallPaperurl.toString());
		String fileName = FileUtil.getFileName(url320, true);
		String path = WallPaperUtil.getWPPicOneKeyTmpHome() + "/" + fileName;
		info.localPath = path;
		try {
			String sql = String.format(WallpaperDbHelper.UPDATE_SQL_LOCAL_PATH, info.localPath, info.id);
			WallpaperDbHelper.execSQL(context, sql);

			URL url = new URL(url320);
			if (loadWallpaper(url, path, true)) {
				return info;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<String> getWPPicOneKeyPath() {
		String wPPicOneKeyHome = WallPaperUtil.getWPPicOneKeyHome();
		File file = new File(wPPicOneKeyHome);
		File[] wPaperFile = file.listFiles(FileUtil.imagefileFilter);
		List<String> filePathList = new ArrayList<String>();
		if (wPaperFile != null) {
			Arrays.sort(wPaperFile, new CompratorByLastModified());
			for (File tmpFile : wPaperFile) {
				String tmppath = tmpFile.getAbsolutePath();
				filePathList.add(tmppath);
			}
		}
		return filePathList;
	}

	private void delwPaperCache(Context mContext, List<String> fileNameList, int cacheSize) {
		if (fileNameList == null || fileNameList.size() < 1)
			return;
		if (fileNameList.size() > cacheSize) {
			for (int i = 0; i < fileNameList.size() - cacheSize + 1; i++) {
				File file = new File(fileNameList.get(i));
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	static class CompratorByLastModified implements Comparator<File> {
		public int compare(File f1, File f2) {
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}

		public boolean equals(Object obj) {
			return true;
		}
	}

	/**
	 * 重定向并下载壁纸
	 */
	private void RedirectionLoadWallPaper(Context mContext, Handler mHandler, int message, final WallpaperItemInfo wallpaperItemInfo) {
		try {
			boolean downSucc = false;

			// 如果已经下载到临时目录，则不再重新下载
			if (wallpaperItemInfo.localPath != null && wallpaperItemInfo.localPath.contains(WallPaperContants.PICTURES_ONEKEY_TMP)) {
				File f = new File(wallpaperItemInfo.localPath);
				if (f.exists()) {
					localPicPath = wallpaperItemInfo.localPath;
					downSucc = true;
				}
			}

			if (!downSucc) {
				ExecutorService threadPool = Executors.newSingleThreadExecutor();
				Future<String> future = threadPool.submit(new Callable<String>() {
					public String call() {
						final String url320 = HttpCommon.getRedirectionURL(wallpaperItemInfo.wallPaperurl.toString());
						return url320;
					};
				});
				String url320 = future.get();
				String fileName = FileUtil.getFileName(url320, true);
				localPicPath = WallPaperUtil.getWPPicOneKeyHome() + "/" + fileName;
				URL url = new URL(url320);
				// 下载壁纸
				downSucc = loadWallpaper(url, localPicPath);
			}

			// 应用壁纸
			if (!downSucc) {
				File f = new File(localPicPath);
				if (f.exists()) {
					FileUtil.delFile(f.getAbsolutePath());
				}
			}
			if (downSucc && applyWallpaper(mContext, localPicPath)) {
				if (moveTmpWallpaper(mContext, wallpaperItemInfo)) {
					localPicPath = localPicPath.replace(WallPaperContants.PICTURES_ONEKEY_TMP, WallPaperContants.PICTURES_ONEKEY_HOME);
				}

				Message msg = new Message();
				msg.what = message;
				msg.obj = localPicPath;
				mHandler.sendMessage(msg);
			} else {
				swapLocalWallPaperInner(mContext, mHandler, message);
			}
			// 已用的壁纸标志位(isUsed)改为1
			String sql = String.format(WallpaperDbHelper.UPDATE_SQL_ISUSED, 1, wallpaperItemInfo.id);
			WallpaperDbHelper.execSQL(mContext, sql);
			setWallPaperIng = false;
		} catch (Exception e) {
			setWallPaperIng = false;
			e.printStackTrace();
			swapLocalWallPaperInner(mContext, mHandler, message);
		}
	}

	/**
	 * 描述:应用壁纸(指定文件路径)[参考安卓桌面的代码]
	 * 
	 * @param ctx
	 * @param wallpaperFileFullName
	 */
	private boolean applyWallpaper(final Context ctx, final String wallpaperFileFullName) {
		if (null == wallpaperFileFullName || wallpaperFileFullName.equals(""))
			return false;
		try {
			// 获取宽高
			if (ScreenUtil.isLargeScreen()) {// 大分辨率使用壁纸流加快速度
				if (ScreenUtil.isLargeScreen()) {
					//Log.e("zhou", "走自定义壁纸 流");
					WallpaperUtil2.setWallPaper(ctx, null, new FileInputStream(wallpaperFileFullName), 1, true);
					return true;
				}
			}
			int[] wh = BitmapUtils.getImageWH(wallpaperFileFullName);
			BitmapFactory.Options options = new BitmapFactory.Options();
			int[] wallpaperWH = ScreenUtil.getWallpaperWH();
			int inSampleSize = Math.max(wh[0] / wallpaperWH[0], wh[1] / wallpaperWH[1]);
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			if (inSampleSize < 1) {
				inSampleSize = 1;
			}
			options.inSampleSize = inSampleSize;
			Bitmap bitmap = BitmapFactory.decodeFile(wallpaperFileFullName, options);
			if (null != bitmap) {
				bitmap = BitmapUtils.toSizeBitmap(bitmap, wallpaperWH[0], wallpaperWH[1]);
				//Log.e("zhou", "走自定义壁纸 图片");
				WallpaperUtil2.setWallPaper(ctx, bitmap, null, 0, true);
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 过滤本地壁纸和在线壁纸的交集部分
	 * 
	 * @return
	 */
	public List<WallpaperItemInfo> filterSameWallpaper(List<WallpaperItemInfo> onLineWallpaperInfos, List<WallpaperItemInfo> localWallpaperInfos) {
		if (localWallpaperInfos == null || localWallpaperInfos.size() < 1)
			return onLineWallpaperInfos;
		List<WallpaperItemInfo> temp = new ArrayList<WallpaperItemInfo>();
		if (onLineWallpaperInfos != null && onLineWallpaperInfos.size() > 0) {
			for (WallpaperItemInfo info : onLineWallpaperInfos) {
				if (!localWallpaperInfos.contains(info)) {
					temp.add(info);
				}
			}
		}
		return temp;
	}

	/**
	 * 下载壁纸
	 */
	public boolean loadWallpaper(URL url, String localPicPath) {
		return loadWallpaper(url, localPicPath, false);
	}

	/**
	 * 下载壁纸
	 */
	public boolean loadWallpaper(URL url, String localPicPath, boolean forGetUnused) {
		File f = null;
		File tmpFile = null;
		String tmpPath = localPicPath + "tmp";
		InputStream is = null;
		OutputStream os = null;
		// 下载被中断则删除文件
		boolean needClean = false;
		try {
			f = new File(localPicPath);
			if (!f.exists()) {
				tmpFile = new File(tmpPath);
				if (tmpFile.exists()) {
					FileUtil.delFile(tmpFile.getAbsolutePath());
				}

				URLConnection con = url.openConnection();
				con.setConnectTimeout(8 * 1000);
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
				is = con.getInputStream();
				if (con.getContentEncoding() != null && con.getContentEncoding().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(con.getInputStream());
				}
				byte[] bs = new byte[1024];
				int len = -1;
				os = new FileOutputStream(tmpFile);
				while ((len = is.read(bs)) != -1) {
					if (forGetUnused && !mGettingUnused) {
						needClean = true;
						break;
					}
					os.write(bs, 0, len);
				}

				FileUtil.moveFile(tmpPath, localPicPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (f != null && f.exists()) {
				FileUtil.delFile(f.getAbsolutePath());
			}
			if (tmpFile != null && tmpFile.exists()) {
				FileUtil.delFile(tmpFile.getAbsolutePath());
			}
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();

				}

				if (needClean) {
					FileUtil.delFile(localPicPath);
					FileUtil.delFile(tmpPath);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 请求壁纸地址
	 * 
	 * @param pc
	 * @return
	 */
	@SuppressWarnings("unused")
	public SwapWallpaperInfo sendRequestWallpaper(String tagId) {
		String url = URLs.PANDAHOME_BASE_URL + "android/pic.aspx?mt=4&tfv=40000&pc=1&Action=2" + tagId;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		List<WallpaperItemInfo> wallpaperItemInfos;
		WallpaperItemInfo wallpaperItemInfo;
		SwapWallpaperInfo swapWallpaperInfo = new SwapWallpaperInfo();
		int code = -1;
		try {
			String visitaResult = getURLContent(url, "utf-8");
			if (StringUtil.isAnyEmpty(visitaResult))
				return null;
			jsonObject = new JSONObject(visitaResult);
			if (jsonObject == null)
				return null;
			if (jsonObject.has("Code")) {
				code = jsonObject.getInt("Code");
				swapWallpaperInfo.code = code;
			}
			if (code != 0 && code != 9) {
				return null;
			}
			if (jsonObject.has("Msg")) {
				swapWallpaperInfo.msg = jsonObject.getString("Msg");
			}
			if (jsonObject.has("Result")) {
				jsonObject = jsonObject.getJSONObject("Result");
				if (jsonObject.has("latestPc")) {
					swapWallpaperInfo.latestPage = jsonObject.getInt("latestPc");
				}
				if (jsonObject.has("items")) {
					jsonArray = jsonObject.getJSONArray("items");
					if (jsonArray != null) {

						wallpaperItemInfos = new ArrayList<WallpaperItemInfo>(jsonArray.length());
						for (int i = 0; null != jsonArray && i < jsonArray.length(); i++) {
							wallpaperItemInfo = new WallpaperItemInfo();
							JSONObject object = jsonArray.getJSONObject(i);
							if (object.has("id")) {
								wallpaperItemInfo.typeId = object.getString("id");
							}
							if (object.has("url")) {
								wallpaperItemInfo.wallPaperurl = object.getString("url");
							}
							if (wallpaperItemInfo.wallPaperurl != null && !wallpaperItemInfo.wallPaperurl.equals("")) {
								int length = wallpaperItemInfo.wallPaperurl.lastIndexOf("/");
								wallpaperItemInfo.fileName = wallpaperItemInfo.wallPaperurl.substring(length, wallpaperItemInfo.wallPaperurl.length());
								wallpaperItemInfos.add(wallpaperItemInfo);
							}
						}
						swapWallpaperInfo.WallpaperItemInfos = wallpaperItemInfos;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return swapWallpaperInfo;
	}

	public void showToast(final Context context, final String message) {
		showHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * 获取本地壁纸文件夹内的所有壁纸列表 -- （一键换壁纸）
	 * 
	 * @return 本地壁纸文件夹内的所有壁纸列表
	 */
	public List<WallpaperItemInfo> getOneKeyWallpaperList() {
		List<WallpaperItemInfo> allListItem = new ArrayList<WallpaperItemInfo>();
		// 一键壁纸的网络壁纸部分的文件夹读取
		String path = WallPaperUtil.getWPPicOneKeyHome();
		List<String> existsWallpapers = WallPaperUtil.getExistsOnekeyWallpapers();
		for (String fileName : existsWallpapers) {
			String filePath = path + "/" + fileName;
			File file = new File(filePath);
			// 删除为0的壁纸
			if (file.length() == 0) {
				FileUtil.delFile(filePath);
				FileUtil.delFile(path + "/" + fileName);
				continue;
			}
			WallpaperItemInfo wp = new WallpaperItemInfo();
			wp.localPath = filePath;
			wp.fileName = fileName;
			wp.size = FileUtil.getMemorySizeString(new File(wp.localPath).length());
			allListItem.add(wp);
		}

		return allListItem;
	}

	/**
	 * 获取本地壁纸文件夹内的所有壁纸列表 -- （一键换壁纸）
	 * 
	 * @return 本地壁纸文件夹内的所有壁纸列表
	 */
	public List<WallpaperItemInfo> getAllWallpaperList() {
		List<WallpaperItemInfo> allListItem = new ArrayList<WallpaperItemInfo>();
		// 本地壁纸文件夹
		String path = WallPaperUtil.getWPPicHomeCachePath();
		List<String> existsWallpapers = WallPaperUtil.getExistsWallpapers();
		for (String fileName : existsWallpapers) {
			String filePath = WallPaperUtil.getPicturesHome() + "/" + fileName;
			File file = new File(filePath);
			// 删除为0的壁纸
			if (file.length() == 0) {
				FileUtil.delFile(filePath);
				FileUtil.delFile(path + "/" + fileName);
				continue;
			}
			WallpaperItemInfo wp = new WallpaperItemInfo();
			wp.localPath = filePath;
			wp.fileName = fileName;
			wp.size = FileUtil.getMemorySizeString(new File(wp.localPath).length());
			allListItem.add(wp);
		}

		// 一键壁纸的网络壁纸部分的文件夹读取
		path = WallPaperUtil.getWPPicOneKeyHome();
		existsWallpapers = WallPaperUtil.getExistsOnekeyWallpapers();
		for (String fileName : existsWallpapers) {
			String filePath = path + "/" + fileName;
			File file = new File(filePath);
			// 删除为0的壁纸
			if (file.length() == 0) {
				FileUtil.delFile(filePath);
				FileUtil.delFile(path + "/" + fileName);
				continue;
			}
			WallpaperItemInfo wp = new WallpaperItemInfo();
			wp.localPath = filePath;
			wp.fileName = fileName;
			wp.size = FileUtil.getMemorySizeString(new File(wp.localPath).length());
			allListItem.add(wp);
		}

		return allListItem;
	}

	public static final int CONNECTION_TIMEOUT = 10000;

	/**
	 * 处理xml的时候需要加上"utf-8"
	 * 
	 * @param surl
	 * @param encode
	 * @return
	 */
	public static String getURLContent(String surl, String encode) {
		InputStream is = null;
		try {
			URL url = new URL(Utf8URLencode(surl));
			HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.setConnectTimeout(CONNECTION_TIMEOUT);
			is = httpUrl.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
			StringBuffer sb = new StringBuffer();
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if ((c >= 0) && (c <= 255) && (c != 32)) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

	public boolean isChangeWallpaperTimeout() {
		return changeWallpaperTimeout;
	}

	public void setChangeWallpaperTimeout(boolean changeWallpaperTimeout) {
		this.changeWallpaperTimeout = changeWallpaperTimeout;
	}
}
