package com.nd.hilauncherdev.kitset.fileselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 线性View
 */
public class LineView extends View {
   
    /**paint**/
    private Paint mPaint;

    /**
     * constructor
     * @param context context
     */
    public LineView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     * @param context context
     * @param attr attr
     */
    public LineView(Context context, AttributeSet attr) {
        super(context, attr);
        // TODO Auto-generated constructor stub
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);

    }

    /**
     * ondraw
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        invalidate();

        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        super.onDraw(canvas);
    }

}
