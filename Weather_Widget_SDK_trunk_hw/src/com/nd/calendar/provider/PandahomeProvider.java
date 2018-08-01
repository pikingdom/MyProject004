package com.nd.calendar.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nd.calendar.common.ComDataDef.DbInfo;
import com.nd.calendar.provider.CalendarDatas.CityDataColumns;

/**
 * @ClassName: CalendarProvider
 * @Description: TODO(共享数据类)
 * @author yanyy
 * @date 2012-11-19 下午02:42:05
 * 
 */
public class PandahomeProvider extends ContentProvider {
    static final String TAG = "CalendarProvider";
    private static final int CITYS = 1;
    private static final int CITY_ID = 2;
    private static UriMatcher sUriMatcher;

    private DatabaseHelper mOpenHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        /* 构造函数-创建一个数据库 */
        DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, null, version);
        }

        /* 创建一个表 */
        @Override
        public void onCreate(SQLiteDatabase db) {
            createlistweathInfo(db);
        }

        /* 升级数据库 */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            createlistweathInfo(db);
        }
        
        
    }
    
    /*创建表*/
    private static boolean createlistweathInfo(SQLiteDatabase db) {
        String str = "Create TABLE if not exists ListWeathInfo("
                + "[listInfoId]  integer PRIMARY KEY ASC AUTOINCREMENT" + ",[strText] nvarchar"
                + ",[nSort] integer" + ",[strweathJson] nvarchar"
                + ",[strNowweathJson] nvarchar"
                + ",[strIndexJson] nvarchar" // 指数，2012.1.4 add by chenx
                + ",[strWarningJson] nvarchar" // 预警，2012.1.4 add by chenx
                + ",[strSunJson] nvarchar" // 日出日落，2012.3.5 add by chenx
                + ",[strPMJson] nvarchar" // pm
                + ",[strCode] nvarchar " + ",[nFlag] int" + ",[strSaveTime] datetime"
                + ",[strNowRefTime] datetime" + ",[strIndexTime] datetime" + // 指数获取时间，2012.1.16 add
                                                                             // by chenx
                ",[strWarnTime] datetime" + // 预警获取时间，2012.1.16 add by chenx
                ",[strSunTime] datetime" + // 日出日落时间，2012.3.5 add by chenx
                ",[strPMTime] datetime" + // pm
                ");";
        try {
            db.execSQL(str);

            // 创建索引
            db.execSQL("CREATE INDEX  if not exists INDEX_ListWeathInfo_CODE_TEXT On [ListWeathInfo] ([strText] ,[strCode] ) ;");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri))
        {
          case CITYS:
            count = db.delete(CityDataColumns.TABLE_NAME, where, whereArgs);
            break;

          case CITY_ID:
            String cityId = uri.getPathSegments().get(1);
            count = db.delete(CityDataColumns.TABLE_NAME, CityDataColumns._ID + "=" + cityId + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

          default:
            Log.v(TAG, "Unknown URI " + uri);
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case CITYS:
            return CityDataColumns.CONTENT_TYPE;

        case CITY_ID:
            return CityDataColumns.CONTENT_ITEM_TYPE;

        default:
            Log.v(TAG, "Unknown URI " + uri);
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != CITYS) {
            Log.v(TAG, "Unknown URI " + uri);
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // 城市代码不能为空
        if (!values.containsKey(CityDataColumns.CITY_CODE)
                || TextUtils.isEmpty(values.getAsString(CityDataColumns.CITY_CODE))) {
            Log.v(TAG, "citycode is null !");
            throw new IllegalArgumentException("citycode is not allow null");
        }
        // 城市名称不能为空
        if (!values.containsKey(CityDataColumns.CITY_NAME)
                || TextUtils.isEmpty(values.getAsString(CityDataColumns.CITY_NAME))) {
            Log.v(TAG, "cityname is null !");
            throw new IllegalArgumentException("cityname is not allow null");
        }
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(CityDataColumns.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri appUri = ContentUris.withAppendedId(CityDataColumns.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(appUri, null);
            return appUri;
        } else {
            Log.v(TAG, "Failed to insert row into " + uri);
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public boolean onCreate() {
    	Context context = getContext();
    	CalendarDatas.setAuthority(context);
    	
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CalendarDatas.AUTHORITY_PANDAHOME, "ListWeathInfos", CITYS);
        sUriMatcher.addURI(CalendarDatas.AUTHORITY_PANDAHOME, "ListWeathInfos/#", CITY_ID);
        
        mOpenHelper = new DatabaseHelper(context, DbInfo.R_DB_USER, null, DbInfo.R_DB_USER_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderby = null;
        switch (sUriMatcher.match(uri)) {
        case CITYS:
            qb.setTables(CityDataColumns.TABLE_NAME);
            if (TextUtils.isEmpty(sortOrder)) {
                orderby = CityDataColumns.DEFAULT_SORT_ORDER;
            } else {
                orderby = sortOrder;
            }
            break;
        case CITY_ID:
            qb.setTables(CityDataColumns.TABLE_NAME);
            qb.appendWhere(CityDataColumns._ID + "=" + uri.getPathSegments().get(1));
            break;
        default:
            Log.v(TAG, "Unknown URI " + uri);
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = null;
        try {
        	c = qb.query(db, projection, selection, selectionArgs, null, null, orderby);
        } catch(Exception e) {
        	e.printStackTrace();
        	//修复城市天气表丢失，造成无法添加城市的问题 caizp 2014-11-06 
            createlistweathInfo(db);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri))
        {
          case CITYS:
            count = db.update(CityDataColumns.TABLE_NAME, values, where, whereArgs);
            break;

          case CITY_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(CityDataColumns.TABLE_NAME, values, CityDataColumns._ID + "=" + noteId + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

          default:
            Log.v(TAG, "Unknown URI " + uri);
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
