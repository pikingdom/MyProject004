package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.android.newline.launcher.R;

public class TrashView extends View {
	public TrashView(Context context) {
		super(context);
		init();
	}

	Bitmap trash_hat;
	Bitmap trash;

	public TrashView(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	Paint mPaint = new Paint();
	Rect mSrcRect = new Rect();
	Rect mDestRect = new Rect();
	Camera camera = new Camera();
	Matrix m = new Matrix();

	public void init() {
		trash_hat = BitmapFactory.decodeResource(getResources(), R.drawable.deletezone_trash_hat);
		trash = BitmapFactory.decodeResource(getResources(), R.drawable.deletezone_trash);
		mPaint.setAntiAlias(true);
	}

	boolean hasAdd = false;

	@Override
	public void draw(Canvas canvas) {
		if (!hasAdd) {
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			hasAdd = true;
		}
		super.draw(canvas);

		float interpolatedTime = 0;
		Animation animation = getAnimation();
		int action = 0;
		if (animation != null) {
			openTrashAnimation a = (openTrashAnimation) animation;
			interpolatedTime = a.getInterpolatedTime();
			action = a.getAction();
		}
		int top = (getHeight() - trash.getHeight()) / 2;
		int left = (getWidth() - trash.getWidth()) / 2;

		canvas.save();
		mSrcRect.left = 0;
		mSrcRect.top = 0;
		mSrcRect.right = trash_hat.getWidth();
		mSrcRect.bottom = trash_hat.getHeight() + mSrcRect.top;

		mDestRect.left = left;
		mDestRect.right = mDestRect.left + trash_hat.getWidth();
		mDestRect.top = top;
		mDestRect.bottom = mDestRect.top + trash_hat.getHeight();

		camera.save();

		// 画桶盖
		if (action == 0) {
			camera.rotateZ(interpolatedTime * 20);
			camera.getMatrix(m);
			camera.restore();

			m.preTranslate(-mDestRect.left, -mDestRect.top);
			m.postTranslate(mDestRect.left, mDestRect.top);

			canvas.concat(m);
		} else {
			canvas.translate(0, -trash.getWidth()*interpolatedTime);
		}
		canvas.drawBitmap(trash_hat, mSrcRect, mDestRect, mPaint);

		canvas.restore();
		// 画桶身
		canvas.save();
		mSrcRect.left = 0;
		mSrcRect.top = 0;
		mSrcRect.right = trash.getWidth();
		mSrcRect.bottom = trash.getHeight() + mSrcRect.top;

		mDestRect.left = left;
		mDestRect.top = top;
		mDestRect.right = mDestRect.left + trash.getWidth();
		mDestRect.bottom = mDestRect.top + trash.getHeight();
		canvas.drawBitmap(trash, mSrcRect, mDestRect, mPaint);

		canvas.restore();

	}

	public void openTrash() {
		openTrashAnimation animation = new openTrashAnimation(0, 0);
		// animation.setRepeatCount(1);
		// animation.setRepeatMode(Animation.REVERSE);
		animation.setDuration(300);
		startAnimation(animation);
	}

	public void closeTrash() {
		openTrashAnimation animation = new openTrashAnimation(1, 0);
		// animation.setRepeatCount(1);
		// animation.setRepeatMode(Animation.REVERSE);
		animation.setDuration(300);
		startAnimation(animation);
	}

	public void dropTrashHat() {
		openTrashAnimation animation = new openTrashAnimation(1, 1);
		// animation.setRepeatCount(1);
		// animation.setRepeatMode(Animation.REVERSE);
		animation.setDuration(500);
		startAnimation(animation);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int wMode = MeasureSpec.getMode(widthMeasureSpec);
		int hMode = MeasureSpec.getMode(heightMeasureSpec);

		int w = 0;
		int h = 0;
		if (wMode == MeasureSpec.EXACTLY) {
			w = width;
		} else if (wMode == MeasureSpec.AT_MOST) {
			w = trash.getWidth();
		}

		if (hMode == MeasureSpec.EXACTLY) {
			h = height;
		} else if (hMode == MeasureSpec.AT_MOST) {
			h = trash.getHeight() + trash.getWidth();
		}
		setMeasuredDimension(w, h);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	public class openTrashAnimation extends Animation {
		private float mInterpolatedTime = 0;
		private int mDirect = 0;

		/**
		 * action:0表示打开和合闭盖子，1表示盖子垂直掉落
		 * */
		private int mAction = 0;

		public openTrashAnimation(int direct, int action) {
			mDirect = direct;
			mAction = action;
		}

		public int getAction() {
			return mAction;
		}

		public float getInterpolatedTime() {
			return mInterpolatedTime;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			mInterpolatedTime = mDirect == 0 ? interpolatedTime : 1 - interpolatedTime;
			TrashView.this.invalidate();
			super.applyTransformation(interpolatedTime, t);
		}
	}
}
