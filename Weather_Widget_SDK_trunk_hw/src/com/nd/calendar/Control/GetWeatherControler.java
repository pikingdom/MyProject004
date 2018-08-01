package com.nd.calendar.Control;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.calendar.CommData.CityWeatherInfo;
import com.nd.calendar.module.WeatherModule;

/**
 * @ClassName: GetWeatherControler
 * @Description: TODO(获取天气控制类)
 * @author Administrator
 * @date 2012-4-13 上午09:30:30
 */
public class GetWeatherControler {
    private final String TAG = "GetWeatherControler";

    private Context mContext;

    private Handler mHandler = null;

    private GetWeatherThread mGetWeatherThread = null;

    private WeatherModule weatherModle = null;

    /* 状态反馈 */
    private int mMsgInProcess = -1;

    /* 错误反馈 */
    private int mMsgErrorTime = -1;

    /* 完成反馈 */
    private int mMsgFinish = 2;

    /* 预警更新反馈 */
    private int mMsgWarningFinish = -1;

    private static GetWeatherControler _instance = null;

    private GetWeatherControler(Context c) {
        mContext = c.getApplicationContext();
        weatherModle = CalendarContext.getInstance(mContext).Get_WeatherMdl_Interface();
    }

    public static GetWeatherControler getInstance(Context c) {
        if (_instance == null) {
            _instance = new GetWeatherControler(c);
        }
        return _instance;
    }

    public void setHandler(Handler h) {
        mHandler = h;
    }

    public void setFinishMsg(int iMsg) {
        mMsgFinish = iMsg;
    }

    public void setRefreshInProcessMsg(int iMsg) {
        mMsgInProcess = iMsg;
    }

    public void setWarnFinishMsg(int iMsg) {
        mMsgWarningFinish = iMsg;
    }

    public void setErrorTimeMsg(int iMsg) {
        mMsgErrorTime = iMsg;
    }

    public void addTask(CityWeatherInfo city) {
        if (!weatherThreadIsAlive()) {
            mGetWeatherThread = new GetWeatherThread();
        }
        mGetWeatherThread.addTask(city);

        // 开启线程
        startWeatherThread();
    }

    /**
     * @Title: addTopTask
     * @Description: TODO(把任务添加到第一个)
     * @author yanyy
     * @date 2012-4-18 上午10:26:30
     * 
     * @param city
     * @return void
     * @throws
     */
    public void addTopTask(CityWeatherInfo city) {
        if (!weatherThreadIsAlive()) {
            mGetWeatherThread = new GetWeatherThread();
        }
        mGetWeatherThread.addTopTask(city);

        // 开启线程
        startWeatherThread();
    }

    private boolean weatherThreadIsAlive() {
        return ((mGetWeatherThread != null) && (mGetWeatherThread.isAlive()) && (!mGetWeatherThread
                .isInterrupted()));
    }

    private void startWeatherThread() {
        if (mGetWeatherThread != null) {
            // 开启线程
            if (!mGetWeatherThread.isAlive()) {
                // Log.e(TAG, "start");
                mGetWeatherThread.start();
            } else if (mGetWeatherThread.isPause()) {
                mGetWeatherThread.nofityThread();
                // Log.e(TAG, "nofityThread");
            }
        }
    }

    public void addTasks(List<CityWeatherInfo> citys) {
        // Log.e(TAG, "addTasks");
        if (!weatherThreadIsAlive()) {
            // Log.e(TAG, "mGetWeatherThread.isAlive() = false");
            mGetWeatherThread = new GetWeatherThread();
        } else {
            // Log.e(TAG, "mGetWeatherThread.isAlive()");
        }
        for (final CityWeatherInfo c : citys) {
            mGetWeatherThread.addTask(c);
        }

        // 开启线程
        startWeatherThread();
    }

    public void removeTask(CityWeatherInfo city) {
        if (mGetWeatherThread != null) {
            mGetWeatherThread.removeTask(city);
        }
    }

    public boolean hasTask(CityWeatherInfo city) {
        if (mGetWeatherThread != null) {
            return mGetWeatherThread.hasTask(city);
        }
        return false;
    }

    /**
     * @Title: clearTask
     * @Description: TODO(清除任务)
     * @author yanyy
     * @date 2012-4-13 上午11:21:25
     * @return void
     * @throws
     */
    public void clearTask() {
        if (mGetWeatherThread != null) {
            if (!mGetWeatherThread.isPause()) {
                mGetWeatherThread.interrupt();
                mGetWeatherThread.clearTask();
            }
        }
    }

    /**
     * @ClassName: GetWeatherThread
     * @Description: TODO(获取天气线程)
     * @author Administrator
     * @date 2012-4-13 上午09:57:55
     */
    protected class GetWeatherThread extends Thread {
        private boolean mPause = false;

        // 栈
        private Stack<CityWeatherInfo> mStack;

        public GetWeatherThread() {
            mPause = false;
            mStack = new Stack<CityWeatherInfo>();
        }

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                mPause = false;
                if (!mStack.isEmpty()) {
                    CityWeatherInfo c = mStack.firstElement();
                    try {
                        if (c != null) {
                            // 判断是否是需要更新
                            if (isNeedUpdate(c)) {
                                // 获取城市天气信息
                                getCityWeatherFromServer(c);
                            } else {
                                if (isNeedReadCache(c)) {
                                    // 需要从数据库读取
                                    getCityWeatherFromLocal(c);
                                }
                                // 预警数据已和天气数据一起获取 caizp 2017-02-14
//                                if (isNeedUpdateWarning(c)) {
//                                    // 需要更新预警
//                                    getCityWarningFromServer(c);
//                                }
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.setIsForceUpdate(false);
                            c.setUpdating(false);
                        }
                        if (mStack.contains(c)) {
                            mStack.remove(c);
                        }
                    }
                } else {
                    pause();
                }
            }
        }

        public void pause() {
            synchronized (this) {
                // 休眠等待 下次唤醒
                mPause = true;
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void nofityThread() {
            synchronized (this) {
                mPause = false;
                this.notify();
            }
        }

        public void addTask(CityWeatherInfo c) {
            if ((!TextUtils.isEmpty(c.getCityCode())) && (!mStack.contains(c))) {
                mStack.add(c);
            }
        }

        public void addTopTask(CityWeatherInfo c) {
            if (!TextUtils.isEmpty(c.getCityCode())) {
                if (!mStack.contains(c)) {
                    mStack.add(0, c);
                } else {
                    mStack.remove(c);
                    mStack.add(0, c);
                }
            }
        }

        public void removeTask(CityWeatherInfo c) {
            if (mStack.contains(c)) {
                mStack.remove(c);
            }
        }

        public boolean hasTask(CityWeatherInfo c) {
            return mStack.contains(c);
        }

        public boolean isPause() {
            return mPause;
        }

        public void clearTask() {
            mStack.clear();
        }

        private boolean isNeedUpdate(CityWeatherInfo c) {
            if (c.isForceUpdate()) {
                // 强制更新
                return true;
            } else {
                Date sysdate = new Date(System.currentTimeMillis());
                boolean bNeed = weatherModle.isToRefreshWeather(c.getId(), sysdate,
                        WeatherModule.TYPE_WEATHER);
                if (bNeed) {
                    return true;
                }
                bNeed = weatherModle.isToRefreshWeather(c.getId(), sysdate, WeatherModule.TYPE_NOW);

                if (bNeed) {
                    return true;
                }
                bNeed = weatherModle.isToRefreshWeather(c.getId(), sysdate,
                        WeatherModule.TYPE_INDEX);
                if (bNeed) {
                    return true;
                }
            }
            return false;
        }

        /* 判断预警是否需要更新 */
        private boolean isNeedUpdateWarning(CityWeatherInfo c) {
        	Date sysdate = new Date(System.currentTimeMillis());
            return weatherModle.isToRefreshWeather(c.getId(), sysdate, WeatherModule.TYPE_WARNING);
        }

        /**
         * @Title: isNeedReadCache
         * @Description: TODO(判断是否需要从数据库读取)
         * @author yanyy
         * @date 2012-4-23 上午10:00:05
         * 
         * @param c
         * @return
         * @return boolean
         * @throws
         */
        private boolean isNeedReadCache(CityWeatherInfo c) {
            return weatherModle.isNeedReadCache(c.getId(), c.getNowWeatherTime(),
                    WeatherModule.TYPE_NOW);
        }

        /* 更新天气信息 */
        private void getCityWeatherFromServer(CityWeatherInfo c) {
            try {
                int iRet = 0;
                // 获取单个天气
                c.setUpdating(true);
                c.setFromBackup(false);

                /* 通知界面正在更新 */
                if (mHandler != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(mMsgInProcess, 0, 0, c));
                }

                // 获取天气信息
                iRet = weatherModle.GetWeatherInfo(c, true);

                // 获取预警
                int iRet2 = weatherModle.GetWarningInfo(c, false);

                // 完成更新
                c.setNetSuccess(WeatherModule.isSuccess(iRet));

                c.setUpdating(false);

                boolean bNoRefresh = (iRet == WeatherModule.WM_NO_REFRESH);

                // send finish message
                if (mHandler != null) {
                    // 有一个成功就更新
                    if (!bNoRefresh) {
                        mHandler.sendMessage(mHandler.obtainMessage(mMsgFinish, 1, bNoRefresh ? 0
                                : 1, c));
                    }

                    // 有错误可以发送
                    if (mMsgErrorTime != -1) {

                        // 天气信息更新错误
                        boolean bErr1 = (iRet == WeatherModule.WM_WEATHER_CHECKCODE_ERROR);

                        // 预警信息更新错误
                        boolean bErr2 = (iRet2 == WeatherModule.WM_WEATHER_CHECKCODE_ERROR);

                        if (bErr1 || bErr2) {
                            mHandler.sendMessage(mHandler.obtainMessage(mMsgErrorTime, bErr1 ? 1
                                    : 0, bErr2 ? 1 : 0, c));
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "getCityWeatherFromServer error");
            }
        }

        /* 本地读取 */
        private void getCityWeatherFromLocal(CityWeatherInfo c) {
            boolean breturn = weatherModle.getCityWeatherById(c.getId(), c);
            if (breturn) {
                // 通知界面刷新
                if (mHandler != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(mMsgFinish, 1, 0, c));
                }
            }
        }

        /* 更新预警 */
        private void getCityWarningFromServer(CityWeatherInfo c) {
            // 预备警后台更新不要体现到界面，在队列中删除，这样用户可以点手动更新
            removeTask(c);
            // 从网络请求数据
            int iRet = weatherModle.GetWarningInfo(c, true);
            if (WeatherModule.isSuccess(iRet)) {
                // 通知界面刷新
                if (mHandler != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(mMsgWarningFinish, 0, 0, c));
                }
            }
        }
    }
}
