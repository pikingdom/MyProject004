package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2016/10/27.
 */
public class RotateImageView extends View {
    Bitmap bitmap = null;
    ValueAnimator ani = null;

    public RotateImageView(Context context) {
        super(context);
        init();
    }

    public RotateImageView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    private float scale = 1;
    private float angel = 0;
    private Paint paint = new Paint();

    private void init() {
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(scale, scale);
        canvas.rotate(angel);
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }

    public void startAni() {
        stopAni();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float f, Object s, Object e) {
                float rate = 1;
                float state1 = 0.5f;
                if (f < state1) {
                    rate = f / state1;
                    scale = 0.5f + 0.5f * (1 - rate);
                    angel = 90 * rate;
                } else {
                    rate = (f - state1) / (1 - state1);
                    scale = 0.5f + 0.5f * rate;
                    angel = 90 + 90 * rate;


                }
                RotateImageView.this.postInvalidate();
                return f;
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(Animation.RESTART);
        animator.start();
        ani = animator;

    }

    public void stopAni() {
        if (ani != null) {
            ani.end();
            ani = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = 0;
        int h = 0;
        if (bitmap != null) {
            w = bitmap.getWidth() + getPaddingBottom() + getPaddingRight();
            h = bitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        } else {
            w = getPaddingBottom() + getPaddingRight();
            h = getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(w, h);
    }
}
