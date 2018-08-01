/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : UserInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-5-5 下午05:32:44 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.dbrepoist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.CityWeatherJson;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.provider.CalendarDatas;
import com.nd.calendar.provider.CalendarDatas.CityDataColumns;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.kitset.util.ApkTools;

public class UserInfo implements IUserInfo {

    public final static int OPT_SUCCESS = 1;
    public final static int OPT_FAILURE = 0;
    public final static int OPT_UPDATE_ERROR = -1;
    public final static int OPT_IS_EXIST = -2;
    public final static int OPT_QUREY_ERROR = -4;

    public final static int TYPE_WEATHER = 1;
    public final static int TYPE_NOW = 2;
    public final static int TYPE_INDEX = 3;
    public final static int TYPE_WARNING = 4;
    public final static int TYPE_SUN = 5;
    public final static int TYPE_PM = 7;
    
    // 数据库接口
    private IDatabaseRef m_dataBaseRef = null;

    // 建议表接口
    private IUser_SuggestInfo m_User_SuggerstInfoIf = null;

    private static UserInfo _instance;

    private UserInfo(Context ctx, IDatabaseRef dataBaseRef) {
    	CalendarDatas.setAuthority(ctx);

        m_dataBaseRef = dataBaseRef;
        createTable();
        // 到黄历或91智能锁读取天气数据，三合一的黄历插件造成这里卡住，定制版无该需求，去除 caizp 2017-03-16
//        readCityDataFormCalendarOrLock(ctx);
    }

    public synchronized static IUserInfo getInstance(Context ctx, IDatabaseRef dataBaseRef) {
        if (_instance == null) {
            _instance = new UserInfo(ctx, dataBaseRef);
        }
        return _instance;
    }
    
    private void createTable() {
        if (m_dataBaseRef == null) {
            return;
        }

        User_SuggestInfo.createSugessInfo(m_dataBaseRef);
        
    }

    // ///////////////////////////////////////////// 城市管理
    /**
     * @brief 【获取所有城市-主程序】
     * 
     * @n<b>函数名称</b> :GetCityList
     * @param cityList
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-5-10下午06:17:32
     */
    public int GetCityList(Context ctx, List<CityInfo> cityList) {
        int nCount = 0;
        Cursor cursor = null;
        try {
            String[] projection = new String[] { CityDataColumns._ID, CityDataColumns.CITY_NAME,
                    CityDataColumns.CITY_CODE, CityDataColumns.CITY_SORT, CityDataColumns.FLAG };
            cursor = ctx.getContentResolver().query(CityDataColumns.GET_MGR_URI(), projection, null,
                    null, CityDataColumns.CITY_SORT + " ASC");
            if (cursor == null) {
                return 0;
            }

            while (cursor.moveToNext()) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.setId(cursor.getInt(0));
                cityInfo.setName(cursor.getString(1));
                cityInfo.setCode(cursor.getString(2));
                cityInfo.setSort(cursor.getInt(3));
                cityInfo.setFromGps(cursor.getInt(4));

                cityList.add(cityInfo);
            }

            nCount = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nCount;
    }

    /* 获取城市数量 */
    public int GetCityCount(Context ctx) {
        int nCount = 0;
        Cursor cursor = null;
        try {
            String[] projection = new String[] { "count(*)" };
            cursor = ctx.getContentResolver().query(CityDataColumns.GET_READ_ONLY_URI(), projection, null,
                    null, null);
            // Cursor cursor = m_dataBaseRef.RawQuery("select count(*) from ListWeathInfo", null);
            if (cursor == null) {
                return 0;
            }

            if (cursor.moveToPosition(0)) {
                nCount = cursor.getInt(0);
            }
            // Log.e("GetCityCount", "nCount=" + nCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nCount;
    }

    /* 根据城市名称和城市代码获取城市ID */
    private int getCityListID(Context ctx, CityInfo cityInfo) {
        int id = 0;
        try {
            String where = CityDataColumns.CITY_NAME + " = '" + cityInfo.getName() + "' and "
                    + CityDataColumns.CITY_CODE + " = '" + cityInfo.getCode() + "'";
            Cursor cursor = ctx.getContentResolver().query(CityDataColumns.GET_MGR_URI(),
                    new String[] { CityDataColumns._ID }, where, null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        id = cursor.getInt(0);
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * @brief 【添加城市信息】
     * 
     * @n<b>函数名称</b> :InsertCityInfo
     * @param cityInfo
     * @param bAutoUpdate
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-5-10下午06:17:52
     */
    public int InsertCityInfo(Context ctx, CityInfo cityInfo, boolean bAutoUpdate) {
        try {
        	
        	Log.d("tag", "uri: " + CityDataColumns.GET_MGR_URI());
            // 查询是否已经存在
            int id = getCityListID(ctx, cityInfo);
            if (id > 0) {
                // 已存在
            	cityInfo.setId(id);
                if (bAutoUpdate) {
                    if (UpdateCityInfo(ctx, cityInfo)) {
                        return OPT_SUCCESS;
                    } else {
                        return OPT_UPDATE_ERROR;
                    }
                }

                return OPT_IS_EXIST;
            } else {
                ContentValues values = new ContentValues();
                values.put(CityDataColumns.CITY_NAME, cityInfo.getName());
                values.put(CityDataColumns.CITY_CODE, cityInfo.getCode());
                values.put(CityDataColumns.CITY_SORT, cityInfo.getSort());
                values.put(CityDataColumns.FLAG, cityInfo.getFromGps());
                Uri uri = ctx.getContentResolver().insert(CityDataColumns.GET_MGR_URI(), values);
                // Log.e("InsertCityInfo", uri.getPathSegments().get(1));

                int iret = OPT_QUREY_ERROR;

                if (uri != null) {
                    cityInfo.setId(Integer.parseInt(uri.getPathSegments().get(1)));
                    iret = OPT_SUCCESS;
                }

                return iret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return OPT_QUREY_ERROR;
        }
    }

    /**
     * @brief 【更新城市信息】
     * 
     * @n<b>函数名称</b> :UpdateCityInfo
     * @param cityInfo
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-5-10下午06:19:24
     */
    @Override
    public boolean UpdateCityInfo(Context ctx, CityInfo cityInfo) {
        int count = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CityDataColumns.CITY_NAME, cityInfo.getName());
            values.put(CityDataColumns.CITY_SORT, cityInfo.getSort());
            values.put(CityDataColumns.FLAG, cityInfo.getFromGps());
            Uri uri = ContentUris.withAppendedId(CityDataColumns.GET_MGR_URI(), cityInfo.getId());
            count = ctx.getContentResolver().update(uri, values, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (count > 0);
    }

    // 删除城市信息
    @Override
    public boolean DeleteCityInfo(Context ctx, CityInfo cityInfo) {
        int row = 0;
        try {
        	// 删除城市24小时天气数据及天气背景地址 caizp 2016-05-05
        	ConfigHelper.getInstance(ctx).removeKey(cityInfo.getCode());
        	ConfigHelper.getInstance(ctx).removeKey(cityInfo.getCode()+"_bg");
			ConfigHelper.getInstance(ctx).commit();
            Uri uri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, cityInfo.getId());
            row = ctx.getContentResolver().delete(uri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    // /////////////////////////////////////// 天气 /////////////////////////////////////////////
    @Override
    public boolean getCityWeatherJsonById(Context ctx, String sId, CityWeatherJson city) {
        boolean bRet = false;
        try {
            String[] projections = new String[] { CityDataColumns._ID, CityDataColumns.CITY_NAME,
                    CityDataColumns.CITY_CODE, CityDataColumns.DAY_WEATHER_JSON,
                    CityDataColumns.NOW_WEATHER_JSON, CityDataColumns.LIVE_INDEX_JSON,
                    CityDataColumns.WARNING_JSON, CityDataColumns.SUN_JSON,
                    CityDataColumns.CITY_SORT, CityDataColumns.FLAG, CityDataColumns.DAY_SAVE_TIME,
                    CityDataColumns.NOW_REF_SAVE_TIME, CityDataColumns.INDEX_SAVE_TIME,
                    CityDataColumns.WARN_SAVE_TIME, CityDataColumns.SUN_SAVE_TIME,
                    CityDataColumns.PM_JSON, CityDataColumns.PM_SAVE_TIME };
            Uri uri = ContentUris
                    .withAppendedId(CityDataColumns.GET_READ_ONLY_URI(), Integer.parseInt(sId));
            Cursor cursor = ctx.getContentResolver().query(uri, projections, null, null, null);

            if (cursor == null) {
                return false;
            }

            try {

                if (cursor.moveToNext()) {
                    city.setId(cursor.getInt(0));
                    city.setName(cursor.getString(1));
                    city.setCode(cursor.getString(2));
                    city.setDayWeatherJson(cursor.getString(3));
                    city.setNowWeatherJson(cursor.getString(4));
                    city.setIndexJson(cursor.getString(5));
                    city.setWarningJson(cursor.getString(6));
                    city.setSunJson(cursor.getString(7));
                    city.setSort(cursor.getInt(8));
                    city.setFromGps(cursor.getInt(9));
                    city.setDayWeatherTime(cursor.getString(10));
                    city.setNowWeatherTime(cursor.getString(11));
                    city.setIndexTime(cursor.getString(12));
                    city.setWarnTime(cursor.getString(13));
                    city.setSunTime(cursor.getString(14));
                    city.setPMJson(cursor.getString(15));
                    city.setPMTime(cursor.getString(16));
                    bRet = true;
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRet;
    }

    /**
     * @brief 【获取天气列表-主程序】
     * 
     * @n<b>函数名称</b> :getCityWeatherList
     * @param citys
     * @param size
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-5-11上午09:42:37
     */
    @Override
    public boolean getCityWeatherList(Context ctx, ArrayList<CityWeatherJson> citys) {
        return getCityWeatherList(ctx, CityDataColumns.CONTENT_URI_PANDAHOME, citys);
    }

    private boolean getCityWeatherList(Context ctx, Uri uri, ArrayList<CityWeatherJson> citys) {
        boolean bret = false;
        try {
            String[] projections = new String[] { CityDataColumns._ID, CityDataColumns.CITY_NAME,
                    CityDataColumns.CITY_CODE, CityDataColumns.DAY_WEATHER_JSON,
                    CityDataColumns.NOW_WEATHER_JSON, CityDataColumns.LIVE_INDEX_JSON,
                    CityDataColumns.WARNING_JSON, CityDataColumns.SUN_JSON,
                    CityDataColumns.CITY_SORT, CityDataColumns.FLAG, CityDataColumns.DAY_SAVE_TIME,
                    CityDataColumns.NOW_REF_SAVE_TIME, CityDataColumns.INDEX_SAVE_TIME,
                    CityDataColumns.WARN_SAVE_TIME, CityDataColumns.SUN_SAVE_TIME,
                    CityDataColumns.PM_JSON, CityDataColumns.PM_SAVE_TIME };

            Cursor cursor = ctx.getContentResolver().query(uri, projections, null, null, null);

            if (cursor == null) {
                return false;
            }
            try {
                while (cursor.moveToNext()) {
                    CityWeatherJson info = new CityWeatherJson();
                    info.setId(cursor.getInt(0));
                    info.setName(cursor.getString(1));
                    info.setCode(cursor.getString(2));
                    info.setDayWeatherJson(cursor.getString(3));
                    info.setNowWeatherJson(cursor.getString(4));
                    info.setIndexJson(cursor.getString(5));
                    info.setWarningJson(cursor.getString(6));
                    info.setSunJson(cursor.getString(7));
                    info.setSort(cursor.getInt(8));
                    info.setFromGps(cursor.getInt(9));
                    info.setDayWeatherTime(cursor.getString(10));
                    info.setNowWeatherTime(cursor.getString(11));
                    info.setIndexTime(cursor.getString(12));
                    info.setWarnTime(cursor.getString(13));
                    info.setSunTime(cursor.getString(14));
                    info.setPMJson(cursor.getString(15));
                    info.setPMTime(cursor.getString(16));
                    citys.add(info);
                }

                bret = (cursor.getCount() > 0);
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bret;
    }

    @Override
    public boolean updateWeatherInfo(Context ctx, CityWeatherJson city) {
        try {
            ContentValues values = new ContentValues();
            values.put(CityDataColumns.DAY_WEATHER_JSON, city.getDayWeatherJson());
            values.put(CityDataColumns.NOW_WEATHER_JSON, city.getNowWeatherJson());
            values.put(CityDataColumns.LIVE_INDEX_JSON, city.getIndexJson());
            values.put(CityDataColumns.WARNING_JSON, city.getWarningJson());
            values.put(CityDataColumns.SUN_JSON, city.getSunJson());
            values.put(CityDataColumns.PM_JSON, city.getPMJson());
            values.put(CityDataColumns.DAY_SAVE_TIME, city.getDayWeatherTime());
            values.put(CityDataColumns.NOW_REF_SAVE_TIME, city.getNowWeatherTime());
            values.put(CityDataColumns.INDEX_SAVE_TIME, city.getIndexTime());
            values.put(CityDataColumns.WARN_SAVE_TIME, city.getWarnTime());
            values.put(CityDataColumns.SUN_SAVE_TIME, city.getSunTime());
            values.put(CityDataColumns.PM_SAVE_TIME, city.getPMTime());

            Uri uri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, city.getId());

            int row = ctx.getContentResolver().update(uri, values, null, null);

            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getLastTimeByType(Context ctx, String id, int type) {
        if (id == null) {
            return null;
        }

        String field = CityDataColumns.NOW_REF_SAVE_TIME;
        switch (type) {
        case TYPE_WEATHER:
            field = CityDataColumns.DAY_SAVE_TIME;
            break;
        case TYPE_WARNING:
            field = CityDataColumns.WARN_SAVE_TIME;
            break;
        case TYPE_SUN:
            field = CityDataColumns.SUN_SAVE_TIME;
            break;
        case TYPE_INDEX:
            field = CityDataColumns.INDEX_SAVE_TIME;
        case TYPE_PM:
            field = CityDataColumns.PM_SAVE_TIME;
            break;
        default:
            break;
        }
        String[] projection = new String[] { field };
        Uri uri = ContentUris.withAppendedId(CityDataColumns.GET_READ_ONLY_URI(), Integer.parseInt(id));
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @brief 【根据字段名，自动更新对应数据，但json 和 time要配对】
     * @n<b>函数名称</b> :updateWeatherAuto
     * @param city
     * @return
     * @return boolean
     * @<b>作者</b> : 陈希
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-5-10下午05:57:27
     * @<b>修改时间</b> :
     */
    public boolean updateWeatherAuto(Context ctx, CityWeatherJson city) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(city.getDayWeatherJson())) {
            values.put(CityDataColumns.DAY_WEATHER_JSON, city.getDayWeatherJson());
        }
        if (!TextUtils.isEmpty(city.getNowWeatherJson())) {
            values.put(CityDataColumns.NOW_WEATHER_JSON, city.getNowWeatherJson());
        }
        if (!TextUtils.isEmpty(city.getIndexJson())) {
            values.put(CityDataColumns.LIVE_INDEX_JSON, city.getIndexJson());
        }
        if (!TextUtils.isEmpty(city.getWarningJson())) {
            values.put(CityDataColumns.WARNING_JSON, city.getWarningJson());
        }
        if (!TextUtils.isEmpty(city.getSunJson())) {
            values.put(CityDataColumns.SUN_JSON, city.getSunJson());
        }
        if (!TextUtils.isEmpty(city.getPMJson())) {
            values.put(CityDataColumns.PM_JSON, city.getPMJson());
        }
        if (!TextUtils.isEmpty(city.getDayWeatherTime())) {
            values.put(CityDataColumns.DAY_SAVE_TIME, city.getDayWeatherTime());
        }
        if (!TextUtils.isEmpty(city.getNowWeatherTime())) {
            values.put(CityDataColumns.NOW_REF_SAVE_TIME, city.getNowWeatherTime());
        }
        if (!TextUtils.isEmpty(city.getIndexTime())) {
            values.put(CityDataColumns.INDEX_SAVE_TIME, city.getIndexTime());
        }
        if (!TextUtils.isEmpty(city.getWarnTime())) {
            values.put(CityDataColumns.WARN_SAVE_TIME, city.getWarnTime());
        }
        if (!TextUtils.isEmpty(city.getSunTime())) {
            values.put(CityDataColumns.SUN_SAVE_TIME, city.getSunTime());
        }
        if (!TextUtils.isEmpty(city.getPMTime())) {
            values.put(CityDataColumns.PM_SAVE_TIME, city.getPMTime());
        }

        if (values.size() > 0) {
            try {
                Uri uri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, city.getId());
                int row = ctx.getContentResolver().update(uri, values, null, null);
                return row > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @brief 【下一个城市ID，循环】
     * 
     * @n<b>函数名称</b> :getNextCityID
     * @param id
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-6-14上午11:25:39
     */
    public int getNextCityID(Context ctx, int id) {
        Cursor cursor = null;
        int nSort = -1;
        try {
        	final Uri uriRead = CityDataColumns.GET_READ_ONLY_URI();
        	
            // 1、id 有效时，判断城市id是否存在
            if (id > 0) {
                Uri uri = ContentUris.withAppendedId(CityDataColumns.GET_READ_ONLY_URI(), id);
                cursor = ctx.getContentResolver().query(uri,
                        new String[] { CityDataColumns.CITY_SORT }, null, null, null);
                if (cursor != null) {
                    try {
                        if (cursor.moveToNext()) {
                            nSort = cursor.getInt(0);
                        }
                    } finally {
                        cursor.close();
                    }
                }

                // nSort > 0，说明该id有城市
                if (nSort > -1) {
                    String where = CityDataColumns.CITY_SORT + ">" + nSort;
                    String orderby = CityDataColumns.CITY_SORT + " asc limit 1 ";
                    cursor = ctx.getContentResolver().query(uriRead,
                            new String[] { CityDataColumns._ID }, where, null, orderby);
                }
            }

            // 2、第1步无效(id == 0，或者 城市找不到) ，则nSort < 0，
            // getCityWeatherJsonWidget 返回首城市，因此要取第二个城市id
            //
            if (nSort < 0) {
                String orderby = CityDataColumns.CITY_SORT + " asc limit 1, 1 ";
                cursor = ctx.getContentResolver().query(uriRead,
                        new String[] { CityDataColumns._ID }, null, null, orderby);
            }

            // 3、都无结果时，取首个
            if (cursor == null || cursor.getCount() <= 0) {
                if (cursor != null) {
                    cursor.close();
                }
                String orderby = CityDataColumns.CITY_SORT + " asc limit 1 ";
                cursor = ctx.getContentResolver().query(uriRead,
                        new String[] { CityDataColumns._ID }, null, null, orderby);
            }

            if ((cursor != null) && (cursor.moveToNext())) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((cursor != null) && (!cursor.isClosed())) {
                cursor.close();
            }
        }
        return -1;
    }

    /**
     * @brief 【获取城市天气，仅限桌面插件】
     * 
     * @n<b>函数名称</b> :getCityWeatherJsonWidget
     * @param id
     *            0，代表第一个城市
     * @param city
     * @return
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-6-13下午12:26:24
     */
    @Override
    public boolean getCityWeatherJsonWidget(Context ctx, int id, CityWeatherJson city) {
        boolean bRet = false;
        Cursor cursor = null;
        try {
            String[] projections = new String[] { CityDataColumns._ID, CityDataColumns.CITY_NAME,
                    CityDataColumns.CITY_CODE, CityDataColumns.CITY_SORT,
                    CityDataColumns.DAY_WEATHER_JSON, CityDataColumns.NOW_WEATHER_JSON,
                    CityDataColumns.SUN_JSON, CityDataColumns.DAY_SAVE_TIME,
                    CityDataColumns.NOW_REF_SAVE_TIME, CityDataColumns.SUN_SAVE_TIME,
                    CityDataColumns.PM_JSON, CityDataColumns.PM_SAVE_TIME, CityDataColumns.WARNING_JSON};
            Log.e("WeatherWidget", "GET_READ_ONLY_URI="+CityDataColumns.GET_READ_ONLY_URI());
            if (id > 0) {
                Uri uri = ContentUris.withAppendedId(CityDataColumns.GET_READ_ONLY_URI(), id);
                cursor = ctx.getContentResolver().query(uri, projections, null, null, null);
            }

            // 没取到数据，默认为首个
            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null) {
                    cursor.close();
                }
                String orderby = CityDataColumns.CITY_SORT + " asc limit 1";
                cursor = ctx.getContentResolver().query(CityDataColumns.GET_READ_ONLY_URI(), projections,
                        null, null, orderby);
            }

            if ((cursor != null) && (cursor.moveToNext())) {
                city.setId(cursor.getInt(0));
                city.setName(cursor.getString(1));
                city.setCode(cursor.getString(2));
                city.setSort(cursor.getInt(3));

                city.setDayWeatherJson(cursor.getString(4));
                city.setNowWeatherJson(cursor.getString(5));
                city.setSunJson(cursor.getString(6));

                city.setDayWeatherTime(cursor.getString(7));
                city.setNowWeatherTime(cursor.getString(8));
                city.setSunTime(cursor.getString(9));
                
                city.setPMJson(cursor.getString(10));
                city.setPMTime(cursor.getString(11));
                city.setWarningJson(cursor.getString(12));
                
                bRet = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((cursor != null) && (!cursor.isClosed())) {
                cursor.close();
            }
        }
        return bRet;
    }

    public String getCityCodeByID(Context ctx, int id) {
        Cursor cursor = null;
        String code = null;
        try {
            Uri uri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, id);
            cursor = ctx.getContentResolver().query(uri,
                    new String[] { CityDataColumns.CITY_CODE }, null, null, null);
            if ((cursor != null) && (cursor.moveToNext())) {
                code = cursor.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return code;
    }

    /**
     * @brief 【从id获得城市名】
     * @n<b>函数名称</b>     :getCityNameByID
     * @param ctx
     * @param id
     * @return
     * @return    String   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-11上午11:27:01
     * @<b>修改时间</b>      :
    */
    public String getCityNameByID(Context ctx, int id) {
        Cursor cursor = null;
        String sName = null;
        try {
            Uri uri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, id);
            cursor = ctx.getContentResolver().query(uri,
                    new String[] { CityDataColumns.CITY_NAME }, null, null, null);
            if ((cursor != null) && (cursor.moveToNext())) {
            	sName = cursor.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sName;
    }
    
    /* 设置 数据库访问地方(91桌面/黄历天气) */
    /*
     * private void setProviderAuthority(Context ctx) {
     * CalendarDatas.setAuthority(CalendarDatas.AUTHORITY_PANDAHOME); //先查找黄历天气是否有数据 if
     * (getCalendarCityCount(ctx) <= 0) { //黄历天气没有数据或者没有安装
     * CalendarDatas.setAuthority(CalendarDatas.AUTHORITY_PANDAHOME); }
     * //CalendarDatas.setAuthority(CalendarDatas.AUTHORITY_PANDAHOME); }
     */

    private boolean readCityDataFormCalendarOrLock(Context ctx) {
        // 从黄历天气读数据到91桌面
        try {
            if (GetCityCount(ctx) <= 0) {
            	Uri contentUri = null;
                // 判断是否安装黄历天气2.2以上的版本
                if (ComfunHelp.checkApkExist(ctx, ComDataDef.CALENDAR_PACKAGE, ComDataDef.CALENDAR_VAR_CODE_2_3_0)) {
                	contentUri = CityDataColumns.CONTENT_URI_CALENDAR;
                } else {// 判断是否安装91桌面
                	if(ApkTools.isApkInstalled(ctx, ComDataDef.LOCK_PACKAGE)) {
                		contentUri = CityDataColumns.CONTENT_URI_LOCK;
                	}
                }
                if(null == contentUri) return false;
                // 到黄历天气或91智能锁读取数据
                ArrayList<CityWeatherJson> citys = new ArrayList<CityWeatherJson>();
                boolean bret = getCityWeatherList(ctx, contentUri,
                        citys);
                if ((bret) && (citys.size() > 0)) {
                    for (CityWeatherJson city : citys) {
                        insertCityData(ctx, city);
                        city = null;
                    }
                }
                citys.clear();
                citys = null;
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    /* 写入数据 */
    private int insertCityData(Context ctx, CityWeatherJson city) {
        try {
            ContentValues values = new ContentValues();
            values.put(CityDataColumns._ID, city.getId());
            values.put(CityDataColumns.CITY_NAME, city.getName());
            values.put(CityDataColumns.CITY_CODE, city.getCode());
            values.put(CityDataColumns.CITY_SORT, city.getSort());
            values.put(CityDataColumns.FLAG, city.getFromGps());
            values.put(CityDataColumns.DAY_WEATHER_JSON, city.getDayWeatherJson());
            values.put(CityDataColumns.NOW_WEATHER_JSON, city.getNowWeatherJson());
            values.put(CityDataColumns.LIVE_INDEX_JSON, city.getIndexJson());
            values.put(CityDataColumns.WARNING_JSON, city.getWarningJson());
            values.put(CityDataColumns.SUN_JSON, city.getSunJson());
            values.put(CityDataColumns.PM_JSON, city.getPMJson());
            values.put(CityDataColumns.DAY_SAVE_TIME, city.getDayWeatherTime());
            values.put(CityDataColumns.NOW_REF_SAVE_TIME, city.getNowWeatherTime());
            values.put(CityDataColumns.INDEX_SAVE_TIME, city.getIndexTime());
            values.put(CityDataColumns.WARN_SAVE_TIME, city.getWarnTime());
            values.put(CityDataColumns.SUN_SAVE_TIME, city.getSunTime());
            values.put(CityDataColumns.PM_SAVE_TIME, city.getPMTime());
            Uri uri = ctx.getContentResolver()
                    .insert(CityDataColumns.CONTENT_URI_PANDAHOME, values);
            if (uri != null) {
                return OPT_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OPT_QUREY_ERROR;
    }
    
    @Override
    public boolean setLocationCity(Context ctx, CityInfo cityInfo) {
        boolean bRet = false;
        try {
            ContentValues values = new ContentValues();
            values.put(CityDataColumns.CITY_NAME, cityInfo.getName());
            values.put(CityDataColumns.CITY_CODE, cityInfo.getCode());
            values.put(CityDataColumns.CITY_SORT, cityInfo.getSort());
            values.put(CityDataColumns.FLAG, 2);
            
            final Uri uriMgr = CityDataColumns.GET_MGR_URI();
            final ContentResolver cr = ctx.getContentResolver();
            
            String where = CityDataColumns.FLAG + "=2";
            int count = cr.update(uriMgr, values, where, null);
            if (count <= 0) {
                //数据没有，就插入
                Uri uri = cr.insert(uriMgr, values);
                if (uri != null) {
                    cityInfo.setId(Integer.parseInt(uri.getPathSegments().get(1)));
                    bRet = true;
                }
            }else{
                bRet = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bRet = false;
        }
        return bRet;
    }
    
    @Override
    public CityInfo getLocationCity(Context ctx, int id) {
        Cursor cursor = null;
        try {
            String[] projections = new String[] {
                    CityDataColumns._ID,
                    CityDataColumns.CITY_NAME,
                    CityDataColumns.CITY_CODE,
                    CityDataColumns.CITY_SORT
            };

            String where = CityDataColumns.FLAG + "=2 ";
            if (id > 0) {
                where += " and " + CityDataColumns._ID + "=" + id;
            }
            
            cursor = ctx.getContentResolver().query(CityDataColumns.GET_MGR_URI(), projections,
                    where, null, null);
            if ((cursor != null) && (cursor.moveToNext())) {
                CityInfo c = new CityInfo();
                c.setId(cursor.getInt(0));
                c.setName(cursor.getString(1));
                c.setCode(cursor.getString(2));
                c.setSort(cursor.getInt(3));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    
    /**
     * @brief 【获取建议接口】
     * @n<b>函数名称</b> :GetUserSuggestIf
     * @see
     * @param @return
     * @<b>作者</b> : 邱堃
     * @<b>创建时间</b> : 2011-11-21下午03:25:09
     */
    @Override
    public IUser_SuggestInfo GetUserSuggestIf() {
        if (m_User_SuggerstInfoIf == null) {
            m_User_SuggerstInfoIf = new User_SuggestInfo();
            m_User_SuggerstInfoIf.InitDb(m_dataBaseRef);
        }
        return m_User_SuggerstInfoIf;

    }

    /* 获取黄历天气城市数量 */
    /*
     * private int getCalendarCityCount(Context ctx) {
     * CalendarDatas.setAuthority(CalendarDatas.AUTHORITY_CALENDAR); int nCount = 0; try { String[]
     * projection = new String[] { "count(*)" }; Cursor cursor =
     * ctx.getContentResolver().query(CityDataColumns.CONTENT_URI, projection, null, null, null); if
     * (cursor == null) { return 0; }
     * 
     * if (cursor.moveToPosition(0)) { nCount = cursor.getInt(0); } cursor.close(); //
     * Log.e("GetCityCount", "nCount=" + nCount); } catch (Exception e) { //e.printStackTrace(); }
     * return nCount; }
     */
}
