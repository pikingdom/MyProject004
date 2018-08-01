package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BlurView extends View {
	/**
	 * 当前桌面的模糊背景
	 * */
	private Bitmap mBlurBitmap = null;
	private Rect mSrcRect = new Rect();
	private Rect mDestRect = new Rect();
	private Paint mPaint = new Paint();
	private int mColor=0;
	public BlurView(Context paramContext) {
		this(paramContext, null);
	}

	public BlurView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public BlurView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public void setBlurBitmap(Bitmap bitmap) {
		mBlurBitmap = bitmap;
		mSrcRect.left = 0;
		mSrcRect.top = 0;
		mSrcRect.right = bitmap.getWidth();
		mSrcRect.bottom = bitmap.getHeight();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if (mBlurBitmap != null) {
			mDestRect.left = 0;
			mDestRect.top = 0;
			mDestRect.right = getWidth();
			mDestRect.bottom = getHeight();

			canvas.drawBitmap(mBlurBitmap, mSrcRect, mDestRect, mPaint);
			if (mColor != 0) {
				canvas.drawColor(mColor);
			}
			super.onDraw(canvas);
		}
	}

	public void setShadeColor(int color) {
		mColor = color;
	}
}
