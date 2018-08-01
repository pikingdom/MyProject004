
package com.nd.weather.widget.UI.weather;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.calendar.CommData.CityWeatherInfo;
import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.nd.weather.widget.R;

public class CityViewFlipper extends ViewFlipper implements OnGestureListener, AnimationListener {

    public interface IOnFlipperCity {
        public void onFilpperCityEnd(CityWeatherInfo c);
    }

    private final int swipe_threshold_veloicty = 200;

    private final int swipe_min_distance = 80;

    private final static int VIEW_CACHE_COUNT = 2;

    private Animation m_AnimLeft_in = null;

    private Animation m_AnimLeft_out = null;

    private Animation m_AnimRight_in = null;

    private Animation m_AnimRight_out = null;

    private GestureDetector m_gestureDetector;

    private Context mContext;

    private CityWeatherView[] mCityViews = new CityWeatherView[VIEW_CACHE_COUNT];

    private List<CityWeatherInfo> m_arrListInfo;

    private int mCityItem = 0;

    private IOnFlipperCity mOnFlipperCity;

    public CityViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
    	initView();
	}

	private void initView() {
        m_gestureDetector = new GestureDetector(this);
        for (int i = 0; i < VIEW_CACHE_COUNT; i++) {
            mCityViews[i] = new CityWeatherView(mContext);
            mCityViews[i].setParentPager(this);
            addView(mCityViews[i].getView());
        }
    }

    /* 设置城市数据 */
    public void setData(List<CityWeatherInfo> citys) {
        m_arrListInfo = citys;
    }

    /* 滑动回调 */
    public void setOnFlipperCity(IOnFlipperCity onFlipper) {
        mOnFlipperCity = onFlipper;
    }

    /* 设置显示第几个城市 */
    public void setCurrentItem(int item) {
        try {
            mCityViews[getDisplayedChild()].setData(m_arrListInfo.get(item), item, m_arrListInfo
                    .size());
            mCityItem = item;
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public void refreshCurrentWeather() {
        try {
            mCityViews[getDisplayedChild()].refreshWeather();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }

        float fXs = e2.getX() - e1.getX();
        float fys = e2.getY() - e1.getY();

        // 左右划屏
        if (Math.abs(fys) <= Math.abs(fXs)) {

            if (Math.abs(velocityX) > swipe_threshold_veloicty) {
                if (fXs > swipe_min_distance) { // 从左至右
                    // Log.e("onFling", "showPrevious");
                    showPreviousView();
                    return true;

                } else if ((-fXs) > swipe_min_distance) {
                    // Log.e("onFling", "showNext");
                    showNextView();
                    return true;
                }

            }
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    /*@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if (!m_gestureDetector.onTouchEvent(ev)) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
	}*/

    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!m_gestureDetector.onTouchEvent(ev)) {
            return super.dispatchTouchEvent(ev);
        } else {
            return true;
        }
	}

	private int getNextCityItem() {
        if (mCityItem >= m_arrListInfo.size() - 1) {
            mCityItem = 0;
        } else {
            mCityItem++;
        }
        return mCityItem;
    }

    private int getPreviousCityItem() {
        if (mCityItem <= 0) {
            mCityItem = m_arrListInfo.size() - 1;
        } else {
            mCityItem--;
        }
        return mCityItem;
    }

    private void showNextView() {
        try {
            if (m_AnimLeft_in == null) {
                m_AnimLeft_in = AnimationUtils.loadAnimation(mContext, R.anim.left_in);
            }
            if (m_AnimLeft_out == null) {
                m_AnimLeft_out = AnimationUtils.loadAnimation(mContext, R.anim.left_out);
                m_AnimLeft_out.setAnimationListener(this);
            }
            setInAnimation(m_AnimLeft_in);
            setOutAnimation(m_AnimLeft_out);

            // 先设置数据
            int nCount = getDisplayedChild();
            int iItem = (nCount + 1) % VIEW_CACHE_COUNT;
            int pos = getNextCityItem();
            mCityViews[iItem].setData(m_arrListInfo.get(pos), pos, m_arrListInfo.size());
            showNext();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    private void showPreviousView() {
        try {
            if (m_AnimRight_in == null) {
                m_AnimRight_in = AnimationUtils.loadAnimation(mContext, R.anim.right_in);
            }
            if (m_AnimRight_out == null) {
                m_AnimRight_out = AnimationUtils.loadAnimation(mContext, R.anim.right_out);
                m_AnimRight_out.setAnimationListener(this);
            }
            setInAnimation(m_AnimRight_in);
            setOutAnimation(m_AnimRight_out);

            int nCount = getDisplayedChild();
            int iItem = (nCount - 1 + 2) % VIEW_CACHE_COUNT;
            int pos = getPreviousCityItem();
            mCityViews[iItem].setData(m_arrListInfo.get(pos), pos, m_arrListInfo.size());
            showPrevious();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        try {
            // 刷新四天天气
            if (mOnFlipperCity != null) {
                mOnFlipperCity.onFilpperCityEnd(m_arrListInfo.get(mCityItem));
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    public CityWeatherInfo getDisplayCityInfo() {
        try {
            return mCityViews[getDisplayedChild()].getCityWeatherInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void refreshDisplayCity() {
        try {
            mCityViews[getDisplayedChild()].setData(m_arrListInfo.get(mCityItem), mCityItem,
                    m_arrListInfo.size());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public void refreshWeatherImgWithSun() {
        mCityViews[getDisplayedChild()].refreshWeatherImgWithSun();
    }

    public void refreshState(CityWeatherInfo cityInfo) {
        try {
            if (cityInfo != null) {
                if (m_arrListInfo.get(mCityItem).getId() == cityInfo.getId()) {
                    mCityViews[getDisplayedChild()].refreshState();
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public void refreshAdImage(String pos, AdvertInfo advertInfo) {
    	if(null == advertInfo) return;
        try {
        	for (CityWeatherView v : mCityViews) {
                if(null != v.getAdsImageView()) {
                	v.getAdsImageView().setAdvertInfo(pos, advertInfo);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /* 刷新预警 */
    public void refreshWarning(CityWeatherInfo cityInfo) {
        try {
            if (cityInfo != null) {
                if (m_arrListInfo.get(mCityItem).getId() == cityInfo.getId()) {
                    mCityViews[getDisplayedChild()].refreshWarning();
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public void refreshPMIndex() {
        try {
            mCityViews[getDisplayedChild()].refreshPMParam();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public void clearBitmap() {
        try {
            for (CityWeatherView v : mCityViews) {
                v.clearBitmap();
            }
        } catch (Exception e) {

        }
    }
}
