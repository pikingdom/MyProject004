/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nd.hilauncherdev.kitset.crop;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;


class HighlightView {
    private static final String TAG = "HighlightView";
    View mContext;  // The View displaying the image.

    public static final int GROW_NONE        = (1 << 0);
    public static final int GROW_LEFT_EDGE   = (1 << 1);
    public static final int GROW_RIGHT_EDGE  = (1 << 2);
    public static final int GROW_TOP_EDGE    = (1 << 3);
    public static final int GROW_BOTTOM_EDGE = (1 << 4);
    public static final int MOVE             = (1 << 5);

    enum ModifyMode { None, Move, Grow }

    private ModifyMode mMode = ModifyMode.None;

    private RectF mImageRect;  // 壁纸图片大小， 如960*800的图片：RectF(0.0, 0.0, 960, 800)
    
    RectF mCropRect;  // 截取壁纸大小：如RectF(160.0, 0.0, 800, 800)
    Rect mDrawRect;  // 截取壁纸的边框在屏幕大小： 如Rect(80, 200 - 400, 600)
    
    Matrix mMatrix;

    private boolean mMaintainAspectRatio = false;
    private float mInitialAspectRatio;

    private Drawable mResizeDrawableWidth;
    private Drawable mResizeDrawableHeight;

    private final Paint mFocusPaint = new Paint();
    private final Paint mNoFocusPaint = new Paint();
    private final Paint mOutlinePaint = new Paint();
    private final static long mOutlinePaintSize = 1;
    
    private int screenWidth;
    private int screenHeight;
    
    boolean mIsFocused;
    boolean mHidden;

    private boolean isShowResize = false;//是否显示边框的缩放图标
    private float bitmapScale = 1.0f;//大图片缩放比例
    private boolean isWallpaperRolling = true; //是否允许壁纸滚动
    /**
     * 是否开启支持自定义头像
     * */
    private boolean mIsCustomIcon=false;
    /**自定义头像最小宽度
     * */
    private float mMinCustomW=100;
    /**自定义头像最小高度
     * */
    private float mMinCustomH=100;
    float matrixArrar[] = new float[9];
    public HighlightView(View ctx) {
        mContext = ctx;
    }

    private void init() {
        android.content.res.Resources resources = mContext.getResources();
        mResizeDrawableWidth =
                resources.getDrawable(R.drawable.camera_crop_width);
        mResizeDrawableHeight =
                resources.getDrawable(R.drawable.camera_crop_height);
    }
    
    public void clean(){
    	mResizeDrawableWidth = null;
    	mResizeDrawableHeight = null;
    }
    
	public boolean hasFocus() {
        return mIsFocused;
    }

    public void setFocus(boolean f) {
        mIsFocused = f;
    }

    public void setHidden(boolean hidden) {
        mHidden = hidden;
    }

    public void setShowResize(boolean isShowResize) {
		this.isShowResize = isShowResize;
	}
    
    protected void draw(Canvas canvas) {
        if (mHidden) {
            return;
        }
        canvas.save();
        Path path = new Path();
        float fix = mOutlinePaintSize;
        if (!hasFocus()) {
            mOutlinePaint.setColor(0xFF000000);
            canvas.drawRect(mDrawRect.left + fix, mDrawRect.top + fix, 
            		mDrawRect.right - fix, mDrawRect.bottom - fix, mOutlinePaint);
        } else {
            Rect viewDrawingRect = new Rect();
            mContext.getDrawingRect(viewDrawingRect);
            path.addRect(mDrawRect.left + fix, mDrawRect.top + fix,
            		mDrawRect.right - fix, mDrawRect.bottom - fix, Path.Direction.CW);
        	mOutlinePaint.setColor(0xFFFFFFFF);
        	
        	ImageViewTouchBase view=(ImageViewTouchBase)mContext;
        	Point point=new Point();
        	Bitmap bitmap=view.getBitmapDisplayed();
        	CountPoint(view.getmDisplayMatrix(),0,0,point);
        	viewDrawingRect.left=point.x;
        	viewDrawingRect.top=point.y;
        	
        	CountPoint(view.getmDisplayMatrix(),bitmap.getWidth(),bitmap.getHeight(),point);
        	viewDrawingRect.right=point.x;
        	viewDrawingRect.bottom=point.y;
        	viewDrawingRect.offset(mContext.getPaddingLeft(), mContext.getPaddingTop());
            try{
                //有些机子GPU下可能不支持该方法，需要捕捉异常(目前已关闭GPU)
            	canvas.clipPath(path, Region.Op.DIFFERENCE);
            	canvas.drawRect(viewDrawingRect,
                        hasFocus() ? mFocusPaint : mNoFocusPaint);
            }catch(Exception e){
            	Log.e(TAG, e.toString());
            }

            canvas.restore();
            canvas.drawPath(path, mOutlinePaint);
            
            

            if (isShowResize && mMode != ModifyMode.Move) {
                int left    = mDrawRect.left   + 1;
                int right   = mDrawRect.right  + 1;
                int top     = mDrawRect.top    + 4;
                int bottom  = mDrawRect.bottom + 3;

                int widthWidth   =
                        mResizeDrawableWidth.getIntrinsicWidth() / 2;
                int widthHeight  =
                        mResizeDrawableWidth.getIntrinsicHeight() / 2;
                int heightHeight =
                        mResizeDrawableHeight.getIntrinsicHeight() / 2;
                int heightWidth  =
                        mResizeDrawableHeight.getIntrinsicWidth() / 2;

                int xMiddle = mDrawRect.left
                        + ((mDrawRect.right  - mDrawRect.left) / 2);
                int yMiddle = mDrawRect.top
                        + ((mDrawRect.bottom - mDrawRect.top) / 2);

                mResizeDrawableWidth.setBounds(left - widthWidth,
                                               yMiddle - widthHeight,
                                               left + widthWidth,
                                               yMiddle + widthHeight);
                mResizeDrawableWidth.draw(canvas);

                mResizeDrawableWidth.setBounds(right - widthWidth,
                                               yMiddle - widthHeight,
                                               right + widthWidth,
                                               yMiddle + widthHeight);
                mResizeDrawableWidth.draw(canvas);

                mResizeDrawableHeight.setBounds(xMiddle - heightWidth,
                                                top - heightHeight,
                                                xMiddle + heightWidth,
                                                top + heightHeight);
                mResizeDrawableHeight.draw(canvas);

                mResizeDrawableHeight.setBounds(xMiddle - heightWidth,
                                                bottom - heightHeight,
                                                xMiddle + heightWidth,
                                                bottom + heightHeight);
                mResizeDrawableHeight.draw(canvas);
            }
        }
    }

    public ModifyMode getMode() {
        return mMode;
    }

    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }

    // Determines which edges are hit by touching at (x, y).
    public int getHit(float x, float y) {
        Rect r = computeLayout();
        final float hysteresis = 50F;//是否点击到缩放框的宁敏度
        int retval = GROW_NONE;

        // verticalCheck makes sure the position is between the top and
        // the bottom edge (with some tolerance). Similar for horizCheck.
        boolean verticalCheck = isShowResize && (y >= r.top - hysteresis)
                && (y < r.bottom + hysteresis);
        boolean horizCheck = isShowResize && (x >= r.left - hysteresis)
                && (x < r.right + hysteresis);

        // Check whether the position is near some edge(s).
        if ((Math.abs(r.left - x)     < hysteresis)  &&  verticalCheck) {
            retval |= GROW_LEFT_EDGE;
        }
        if ((Math.abs(r.right - x)    < hysteresis)  &&  verticalCheck) {
            retval |= GROW_RIGHT_EDGE;
        }
        if ((Math.abs(r.top - y)      < hysteresis)  &&  horizCheck) {
            retval |= GROW_TOP_EDGE;
        }
        if ((Math.abs(r.bottom - y)   < hysteresis)  &&  horizCheck) {
            retval |= GROW_BOTTOM_EDGE;
        }

        // Not near any edge but inside the rectangle: move.
        if (retval == GROW_NONE && r.contains((int) x, (int) y)) {
    		retval = MOVE;
        }
        return retval;
    }

    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    void handleMotion(int edge, float dx, float dy) {
        Rect r = computeLayout();
        if (edge == GROW_NONE) {
            return;
        } else if (edge == MOVE) {//移动裁减区位置
        	moveBy(dx * (mCropRect.width() / r.width()), dy * (mCropRect.height() / r.height()));
        }else {//缩放区域大小
            if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & edge) == 0) {
                dx = 0;
            }

            if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & edge) == 0) {
                dy = 0;
            }

            // Convert to image space before sending to growBy().
            float xDelta = dx * (mCropRect.width() / r.width());
            float yDelta = dy * (mCropRect.height() / r.height());
            
            float growX = (((edge & GROW_LEFT_EDGE) != 0) ? -1 : 1) * xDelta;
            float growY = (((edge & GROW_TOP_EDGE) != 0) ? -1 : 1) * yDelta;
            
            growBy(growX, growY);
        }
    }

    //移动壁纸截取区
    void moveBy(float dx, float dy) {
        Rect invalRect = new Rect(mDrawRect);

        mCropRect.offset(dx, dy);

        // Put the cropping rectangle inside image rectangle.
        mCropRect.offset(
                Math.max(0, mImageRect.left - mCropRect.left),
                Math.max(0, mImageRect.top  - mCropRect.top));

        mCropRect.offset(
                Math.min(0, mImageRect.right  - mCropRect.right),
                Math.min(0, mImageRect.bottom - mCropRect.bottom));

        mDrawRect = computeLayout();
        invalRect.union(mDrawRect);
        invalRect.inset(-10, -10);
        mContext.invalidate(invalRect);
        
    }

    
    // Grows the cropping rectange by (dx, dy) in image space.
    void growBy(float dx, float dy) {
        if(dx == 0f && dy == 0f)
        	return;
       
		float limitWidth = 0;
		float limitHeight = 0;
		if (mIsCustomIcon) {
			limitWidth = mMinCustomW;
			limitHeight = mMinCustomH;
		} else {
			limitWidth = isWallpaperRolling ? screenWidth * bitmapScale * 2 : screenWidth * bitmapScale;
			limitHeight = screenHeight * bitmapScale;
		}
        
         
    	
    	
    	//小于屏幕不缩放
		if((dx < 0 || dy < 0) && 
			(Math.round(mCropRect.width() / bitmapScale) <= limitWidth 
			|| Math.round(mCropRect.height() / bitmapScale) <= limitHeight)){
        	return;
        }
		
        if (mMaintainAspectRatio) {
            if (dx != 0) {
                dy = dx / mInitialAspectRatio;
            } else if (dy != 0) {
                dx = dy * mInitialAspectRatio;
            }
        }

        // Don't let the cropping rectangle grow too fast.
        // Grow at most half of the difference between the image rectangle and
        // the cropping rectangle.
        RectF r = new RectF(mCropRect);
        //缩小时不能小于屏幕
        if (dx < 0F && r.width() + 2 * dx < limitWidth) {
            float adjustment = (limitWidth - r.width()) / 2F;
            dx = adjustment;
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio;
            }
        }
        if (dy < 0F && r.height() + 2 * dy < limitHeight) {
            float adjustment = (limitHeight - r.height()) / 2F;
            dy = adjustment;
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio;
            }
        }
        //放大时不能大于图片
        if (dx > 0F && r.width() + 2 * dx > mImageRect.width()) {
            float adjustment = (mImageRect.width() - r.width()) / 2F;
            dx = adjustment;
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio;
            }
        }
        if (dy > 0F && r.height() + 2 * dy > mImageRect.height()) {
            float adjustment = (mImageRect.height() - r.height()) / 2F;
            dy = adjustment;
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio;
            }
        }
        

        
        r.inset(-dx, -dy);

        // Don't let the cropping rectangle shrink too fast.
        final float widthCap = 25F;
        if (r.width() < widthCap) {
            r.inset(-(widthCap - r.width()) / 2F, 0F);
        }
        float heightCap = mMaintainAspectRatio
                ? (widthCap / mInitialAspectRatio)
                : widthCap;
        if (r.height() < heightCap) {
            r.inset(0F, -(heightCap - r.height()) / 2F);
        }

        // Put the cropping rectangle inside the image rectangle.
        if (r.left < mImageRect.left) {
            r.offset(mImageRect.left - r.left, 0F);
        } else if (r.right > mImageRect.right) {
            r.offset(-(r.right - mImageRect.right), 0);
        }
        if (r.top < mImageRect.top) {
            r.offset(0F, mImageRect.top - r.top);
        } else if (r.bottom > mImageRect.bottom) {
            r.offset(0F, -(r.bottom - mImageRect.bottom));
        }

        mCropRect.set(r);
        mDrawRect = computeLayout();
        
        mContext.invalidate();
    }
    
    // Returns the cropping rectangle in image space.
    public Rect getCropRect() {
    	return new Rect(Math.round(mCropRect.left/bitmapScale), Math.round(mCropRect.top/bitmapScale),
    			Math.round(mCropRect.right/bitmapScale), Math.round(mCropRect.bottom/bitmapScale));
    }
    

    // Maps the cropping rectangle from image space to screen space.
    private Rect computeLayout() {
        RectF r = new RectF(mCropRect.left, mCropRect.top,
                            mCropRect.right, mCropRect.bottom);
        mMatrix.mapRect(r);
        return new Rect(Math.round(r.left), Math.round(r.top),
                        Math.round(r.right), Math.round(r.bottom));
    }
    

    public void invalidate() {
        mDrawRect = computeLayout();
    }

    public void setup(Matrix m, Rect imageRect, RectF cropRect, boolean maintainAspectRatio) {
        mMatrix = new Matrix(m);

        mCropRect = cropRect;
        
        mImageRect = new RectF(imageRect);
        mMaintainAspectRatio = maintainAspectRatio;

        mInitialAspectRatio = mCropRect.width() / mCropRect.height();
        mDrawRect = computeLayout();

        mFocusPaint.setARGB(80, 0, 0, 0);
        mNoFocusPaint.setARGB(80, 0, 0, 0);
        mOutlinePaint.setStrokeWidth(mOutlinePaintSize);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
        
        final int screen[] = ScreenUtil.getScreenWH();
		screenWidth = screen[0];
		screenHeight = screen[1];
        init();
    }
    
    /*public int[] getCropImageSize(){
    	int width = Math.round(mCropRect.width()/bitmapScale);
    	int height = Math.round(mCropRect.height()/bitmapScale);
    	int fix = 3;
    	//大小和屏幕相近时，校正数值
    	if(width > screenWidth - fix && width <= screenWidth + fix){
    		width = screenWidth;
    	}
    	if(height > screenHeight - fix && height <= screenHeight + fix){
    		height = screenHeight;
    	}
    	return new int[]{width, height};
    }*/
    
	public void setWallpaperRolling(boolean isWallpaperRolling) {
		this.isWallpaperRolling = isWallpaperRolling;
	}

	public void setBitmapScale(float bitmapScale) {
		this.bitmapScale = bitmapScale;
	}
	/**
	 * 获取当前显示的裁切区，即不是真实尺寸的裁切区
	 * 真实尺寸的为 类中的getCropRect()
	 * */
	public RectF getOriCropRect() {
		return mCropRect;
	}

	public void setCustomIcon(boolean isCustomIcon,float minW,float minH) {
		mIsCustomIcon=isCustomIcon;
		mMinCustomW=minW;
		mMinCustomH=minH;
	}
	
	/* 原点乘以矩阵得到目标点 */
	 void CountPoint(Matrix matrix, float x, float y, Point destPoint) {
		
		float z = 0;
		matrix.getValues(matrixArrar);
		destPoint.x = (int) (matrixArrar[0] * x + matrixArrar[1] * y + matrixArrar[2]);
		destPoint.y = (int) (matrixArrar[3] * x + matrixArrar[4] * y + matrixArrar[5]);
		z = (matrixArrar[6] * x + matrixArrar[7] * y + matrixArrar[8]);
		destPoint.x = (int) (destPoint.x / z);
		destPoint.y = (int) (destPoint.y / z);
	}
}
