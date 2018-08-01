package com.nd.hilauncherdev.launcher;

import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.view.DragView;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FolderAnimation extends Animation {
	/**
	 * 目标的位置,即刚添加入文件夹的图标的缩略图在屏幕中的绝对位置
	 * */
	private RectF mDestRect = new RectF();
	/**
	 * 被拖动的图标的位置
	 * */
	private RectF mSrcRect =new RectF();
	private float mScale = 0;
	/**
	 * 
	 * 被拖动的图标的上padding
	 * */
	float mTopPadding;
	/**
	 * 被拖动的图标的左padding
	 * */
	float mLetfPadding;
	/***
	 * 被手动的图标要加入到的目标文件夹
	 */
	private FolderIconTextView mFolderIconView;

	private DragView mDragView;
	public DragController mDragController;
	/**
	 * 从源位置的目标位置的X轴差值
	 */
	private float mDeltaX = 0;
	/**
	 * 从源位置的目标位置的Y轴差值
	 */
	private float mDeltaY = 0;
	/**
	 * 匣子中 FolderIconTextView对象是变改变，而FolderInfo是不变的，
	 * 所以要保存它，通过它得到FolderIconTextView
	 * */
	private FolderInfo mFolderInfo;
	/**
	 * 文件夹的位置
	 * */
	int mFolderPosition[] = { -1, -1 };

	public FolderAnimation(FolderIconTextView view, int topPadding, int leftPadding, float scale, DragView dragView, DragController dragController, FolderInfo folderInfo) {
		mTopPadding = topPadding;
		mLetfPadding = leftPadding;
		mFolderIconView = view;
		mDragView = dragView;
		mDragController = dragController;
		mFolderInfo = folderInfo;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		if (mFolderPosition[0] == -1) {
			// 匣子中的文件夹
			if (mFolderInfo != null) {
				mFolderIconView = mFolderInfo.mFolderIcon;
				if (mFolderIconView.mInfo.contents.size() == 2) {
					mFolderIconView.setNoDrawIndex(0);
				} else {
					mFolderIconView.setNoDrawIndex(mFolderIconView.mInfo.contents.size());
				}

			}
			int temp[] = new int[2];
		    mDragView.getLocationOnScreen(temp);
		    mSrcRect.left = temp[0];
		    mSrcRect.top = temp[1];
		    mSrcRect.right = mSrcRect.left + mDragView.getWidth();
		    mSrcRect.bottom = mSrcRect.bottom + mDragView.getHeight();
			mFolderIconView.getLocationOnScreen(mFolderPosition);
			float position[] = mFolderIconView.getThumbsInfo();
			mDestRect.left = mFolderPosition[0] + position[0];
			mDestRect.top = mFolderPosition[1] + position[1];

			mDestRect.right = mDestRect.left + position[2];
			mDestRect.bottom = mDestRect.top + position[2];
			mScale = mDestRect.width() / (float) (mSrcRect.width() - mLetfPadding * 2);
			mDeltaX = mDestRect.left - (mSrcRect.left + mLetfPadding);
			mDeltaY = mDestRect.top - (mSrcRect.top + mTopPadding);
		}
		Matrix matrix = t.getMatrix();
		matrix.preTranslate(-mLetfPadding, -mTopPadding);
		matrix.postScale(1 - (1 - mScale) * interpolatedTime, 1 - (1 - mScale) * interpolatedTime);
		matrix.postTranslate(mLetfPadding, mTopPadding);
		matrix.postTranslate(mDeltaX * interpolatedTime, mDeltaY * interpolatedTime);
	}

	public FolderIconTextView getFolderIconView() {
		return mFolderIconView;
	}

	public DragView getDragView() {
		return mDragView;
	}

}
