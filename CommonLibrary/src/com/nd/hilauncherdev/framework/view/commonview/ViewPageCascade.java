package com.nd.hilauncherdev.framework.view.commonview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 类似照片展示的一个View， 支持设置展示框的尺寸
 *
 * @author zhouhq create date:2015/5/18
 * */
public class ViewPageCascade extends ViewGroup{

	int viewW = 0;
	int viewH = 0;
	private Scroller mScroller;
	float mScale = 0.95f;
	Paint mPaint = new Paint();

	// private final int CASCADE_SAVEFLAGS = Canvas.MATRIX_SAVE_FLAG |
	// Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
	// Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

	/**
	 * 当前正在展示的view
	 * */
	float mCurrPosition;

	/**
	 * 启始的展示位置中屏幕中心的偏移位置
	 *
	 * */
	float mQueueStartOffset = -0;


	float mCurrentScrollX=0;
	/**
	 * 每一个展示的view之间的间隔
	 * */
	float mQueueSpace = 30;
	private boolean isClick = true;
	private float minScale=0.9f;
	private long downTime = 0;
	private ViewGroup mPager;
	/**是否忆发送过取消上滑的请求*/
	private boolean hasReques=false;

	/**
	 * 滑动这个距离后，屏蔽向上滑动的功能
	 * */
	private int slitherDistance=2;
	/**
	 * 非当前页主题页面的透明度
	 * */
	private static final float backAlpha=0.5f;

	/**上一次的选中页*/
	int lastSelect=0;
	public ViewPageCascade(Context context) {
		super(context);
		init(context);
	}
	public ViewPageCascade(Context context,AttributeSet attr,int def) {
		super(context,attr,def);
		init(context);
	}

	public ViewPageCascade(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	/**
	 * 灵敏度，屏幕上滑动N像素，页面跳入前或者后一页
	 *
	 * */
	private float mSensitivity=300;
	/**
	 * 灵敏度，屏幕上滑动N像素，页面跳入前或者后一页
	 * @param sensitivity  像素
	 * */
	public void setSensitivity(int sensitivity) {
		mSensitivity = sensitivity;
	}
	private float mPadding=20;

	public void setPadding(float padding) {

		mPadding = padding;
	}

	private OnPageChange mPageChangeListenr=null;
	public void setPageChangeListenr(OnPageChange listen) {
		mPageChangeListenr = listen;
	}
	public void setPager(ViewGroup pager) {
		mPager = pager;
	}

/*	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}*/

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	public void setViewSize(int w, int h) {
		viewW = w;
		viewH = h;
	}

	public void init(Context context) {
		mScroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		int boder = ((b - t) - viewH) / 2;
		int boderW = ((r - l) - viewW) / 2;
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);

			int left =  boderW;
			int right = left + viewW;
			int top = boder;
			int bottom = top + viewH;
			view.layout(left, top, right, bottom);
		}

		scaleArrar = new float[count];
		leftOffset = new float[count];
		countQueueSpace();
	}

	float mLastMotionX=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final float x = event.getX();
		final int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				if (mPageChangeListenr != null) {
					mPageChangeListenr.onFlashSelect(Math.round(mCurrPosition));
				}
				mScroller.abortAnimation();
			}
			hasReques=false;
			isClick=true;
			downTime = System.currentTimeMillis();
			mLastMotionX=event.getX();
			break;

		case MotionEvent.ACTION_MOVE:


			 int deltaX = (int) (mLastMotionX - x);
			if (deltaX > 5 || deltaX < -5) {
				isClick = false;
			}
			if((deltaX > slitherDistance||deltaX <- slitherDistance)){
				requestDisallowInterceptTouchEvent(true);
				if(!hasReques && mPager != null){
					mPager.requestDisallowInterceptTouchEvent(true);
					hasReques=true;
				}
			}
//			if (!hasReques && mPager != null && (deltaX > slitherDistance||deltaX <- slitherDistance)) {
//				mPager.requestDisallowInterceptTouchEvent(true);
//				hasReques=true;
//			}
			 mLastMotionX = x;
			float move = deltaX / mSensitivity*mScrollUnit;
			mCurrentScrollX=mCurrentScrollX+move;
			//mCurrentScrollX=mCurrentScrollX<0?0:mCurrentScrollX;
			mCurrentScrollX=mCurrentScrollX<=-mScrollUnit?-mScrollUnit+1:mCurrentScrollX;

			if (mCurrentScrollX >= (getChildCount()-0.5f) * mScrollUnit) {
				mCurrentScrollX = (getChildCount()-0.5f) * mScrollUnit-1;
			}
			mCurrPosition = mCurrentScrollX / (float) mScrollUnit;
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			long time = System.currentTimeMillis();
			if (isClick &&(time - downTime < 600)) {
					float index;
					index=mCurrPosition<0?0:mCurrPosition;
					index=mCurrPosition>getChildCount()-1?getChildCount()-1:mCurrPosition;
					int clickChild=Math.round(index);
				if (mPageChangeListenr != null) {
					mPageChangeListenr.onPageClick(getChildAt(clickChild));
				}

			}
			snapToScreen();
			if(mPager!=null)mPager.requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}

		return true;
	}

	private float scaleArrar[];
	private float leftOffset[];

	@Override
	protected void dispatchDraw(Canvas canvas) {

		int count = getChildCount();
		float leftTotal = 0;
		for (int i = 0; i < count; i++) {

			float d = i - mCurrPosition;
			if (d < 0) {
				float scale = 1;
				if (count < 2) {
					scale = 1 - (-d) * (1 - minScale);
				} else {
					scale = 1 - (-d / (count - 1) * (1 - minScale));
				}
				leftTotal = mQueueSpace * d;
				scaleArrar[i] = scale;
				leftOffset[i] = leftTotal + mQueueStartOffset;
			} else {
				float scale = 1;
				if (count < 2) {
					scale = 1 - d * (1 - minScale);
				} else {
					scale = 1 - (d / (count - 1) * (1 - minScale));
				}
				leftTotal = mQueueSpace * d;
				scaleArrar[i] = scale;
				leftOffset[i] = leftTotal + mQueueStartOffset;
			}

		}
		float destAlpha=0;
		float halfPage = mCurrPosition - (int) (mCurrPosition);
		int correct = halfPage > 0.5 ? 0 : -1;
		for (int i = 0; i <= mCurrPosition + correct; i++) {
			float d = i - mCurrPosition;

			if (Build.VERSION.SDK_INT >= 14) {
				if (i == count - 1) {
					destAlpha = 1;
				} else if (d < -0.5f && d > -1) {
					destAlpha = (-(d + 0.5f) / 0.5f) * backAlpha;
				} else {
					destAlpha = backAlpha;
				}

			//	getChildAt(i).setAlpha(destAlpha);
			}
			int transferX = getWidth() / 2 + viewW / 2;
			canvas.save();
			canvas.translate(transferX + leftOffset[i], getHeight() / 2);
			canvas.scale(scaleArrar[i], scaleArrar[i]);
			canvas.translate(-transferX, -getHeight() / 2);
			drawChild(canvas, getChildAt(i), getDrawingTime());
			// Log.e("zhou", "m="+canvas.getMatrix());
			canvas.restore();
		}

		for (int i = count - 1; i > mCurrPosition + correct; i--) {
			if(i<0)return;
			float d = i - mCurrPosition;

			if (Build.VERSION.SDK_INT >= 14) {

				if (i == count - 1 && d < 0) {
					destAlpha = 1;
				} else if (i == 0 && d > 0) {
					destAlpha = 1;
				} else if (d < 0 && d >= -0.5f) {
					destAlpha = 1 - ((-d) / 0.5f);
				} else if (d >= 0 && d < 1) {
					destAlpha = 1 - d * backAlpha;
				} else {
					destAlpha = backAlpha;
				}
			//	getChildAt(i).setAlpha(destAlpha);
			}
			int transferX = getWidth() / 2 - viewW / 2;
			canvas.save();
			canvas.translate(transferX + leftOffset[i], getHeight() / 2);
			canvas.scale(scaleArrar[i], scaleArrar[i]);
			canvas.translate(-transferX, -getHeight() / 2);

			drawChild(canvas, getChildAt(i), getDrawingTime());

			// Log.e("zhou", "m="+canvas.getMatrix());
			canvas.restore();

		}
	}

	/**
	 * 滚动的单位
	 * */
	int mScrollUnit = 200;
//
//	public void snapToScreen(int direct) {
//		//int currscroll = mScroller.getCurrX();
//
//		int currscroll=(int)mCurrentScrollX;
//		int nextScroll = 0;
//		// 向左
//		int dest = 0;
//		if (direct == 0) {
//			nextScroll = currscroll - mScrollUnit;
//			if (nextScroll < 0) {
//				dest = 0;
//			} else {
//				dest = nextScroll / mScrollUnit * mScrollUnit;
//			}
//		} else {
//			nextScroll = currscroll + mScrollUnit;
//			if (nextScroll > (getChildCount()-1) * mScrollUnit) {
//				dest = (getChildCount()-1) * mScrollUnit;
//			} else {
//				int remainder = nextScroll % mScrollUnit;
//				dest = nextScroll / mScrollUnit * mScrollUnit;
//				dest = remainder > 0 ? (dest + mScrollUnit) : dest;
//			}
//		}
//		Log.e("zhou", "currscroll="+currscroll+" dest="+dest);
//		mScroller.startScroll(currscroll, 0, -(currscroll - dest), 0,100000);
//		invalidate();
//	}


	public void snapToScreen() {
		int currscroll = (int)mCurrentScrollX;
		int mod=(int)mCurrentScrollX%mScrollUnit;
		float  destScroll;
		float dest=0;

		if (mod < 0) {
			dest=0;
		} else if (mod > mScrollUnit / 2) {
			dest = (int) Math.floor(mCurrPosition) + 1;
		} else {
			dest = (int) Math.floor(mCurrPosition);
		}
		if(dest>getChildCount()-1)dest=getChildCount()-1;
		destScroll=dest*mScrollUnit;
		mScroller.startScroll(currscroll, 0, -(currscroll - (int)destScroll), 0);
		invalidate();
	}

	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {

			mCurrPosition = mScroller.getCurrX() / (float) mScrollUnit;
			mCurrentScrollX=mScroller.getCurrX();
			//Log.e("zhou", "mCurrPosition="+mCurrPosition+"scroll="+mScroller.getCurrX());
			postInvalidate();
			// tab.scrollHighLight(mScroller.getCurrX());
			// 判断滑动是否停止
			if (mScroller.isFinished()) {
				if (mPageChangeListenr != null && !isClick) {
					int select = (int) mCurrPosition;
					if (lastSelect != select) {
						lastSelect = select;
						mPageChangeListenr.onPageSelected((int) mCurrPosition);
					}
				}
			}
		}
	}
	/**
	 * 页面改变
	 */
	public interface OnPageChange {
		public void onPageClick(View view);
		public void onPageSelected(int select);
		public void onFlashSelect(int select);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int cCount=getChildCount();

        int childW=MeasureSpec.makeMeasureSpec(viewW,MeasureSpec.EXACTLY );
        int childH=MeasureSpec.makeMeasureSpec(viewH,MeasureSpec.EXACTLY );
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			measureChild(child, childW, childH);
		}
		setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth:sizeWidth,viewH);
	}

	private void countQueueSpace() {
		int count = getChildCount();
		if (count < 2)
			return;
		float totalspace=getWidth()-viewW*minScale-mPadding;

		float space=totalspace-(getWidth()/2-viewW/2);
		mQueueSpace=space/(count-1);
	}
}
