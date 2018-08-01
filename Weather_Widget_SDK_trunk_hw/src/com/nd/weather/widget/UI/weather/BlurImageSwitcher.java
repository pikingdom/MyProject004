package com.nd.weather.widget.UI.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BlurImageSwitcher extends View {
	
	protected Bitmap bitmap_old;
	protected Bitmap bitmap_now;

	private boolean isAnimating = false;

	private static final float TIME = 500f;
	private long switchStartTime = 0;
	Paint mPaint;

	private RectF rectDst;

	public void switchTo(Bitmap bmp) {
		if (bmp == null) {
			return;
		}
		if (bitmap_now != null) {
			bitmap_old = bitmap_now;
		}
		bitmap_now = bmp;
		isAnimating = true;
		switchStartTime = System.currentTimeMillis();
		this.invalidate();
	}

	public void setCurrentBitmap(Bitmap bmp) {
		if (bmp == null) {
			return;
		}
		bitmap_now = bmp;
		this.invalidate();
	}

	public BlurImageSwitcher(Context context) {
		super(context);
		init();
	}

	public BlurImageSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BlurImageSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (rectDst == null && this.getWidth() != 0 && this.getHeight() != 0) {
			rectDst = new RectF(0, 0, this.getWidth(), this.getHeight());
		}
		if (bitmap_now != null && !bitmap_now.isRecycled() && rectDst != null) {
			mPaint.setAlpha(255);
			canvas.drawBitmap(bitmap_now, null, rectDst, mPaint);
		}
		if (isAnimating && bitmap_old != null && !bitmap_old.isRecycled()
				&& rectDst != null) {
			int sw = (int) (255 * (1 - (System.currentTimeMillis() - switchStartTime)
					/ TIME));
			if (sw < 0) {

				sw = 0;
				isAnimating = false;
				// bitmap_old.clear();
				bitmap_old = null;
				return;
			}
			mPaint.setAlpha(sw);
			canvas.drawBitmap(bitmap_old, null, rectDst, mPaint);
			this.invalidate();
		}

	}

}
