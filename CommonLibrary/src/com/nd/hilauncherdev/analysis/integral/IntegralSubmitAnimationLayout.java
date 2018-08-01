package com.nd.hilauncherdev.analysis.integral;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

/**
 * 积分动画布局
 * <p>Title: IntegralSubmitAnimationLayout</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年11月4日
 */
public class IntegralSubmitAnimationLayout extends LinearLayout{
	private LayoutInflater inflater;
	private ViewCache viewCache;
	private View mRoot;
	private Context context;
	private boolean isLoadSuccess;
	
	public IntegralSubmitAnimationLayout(Context context) {
		super(context);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initView();
	}

	private void initView(){
		mRoot = inflater.inflate(R.layout.integral_submit_layout, null);
		viewCache = new ViewCache();
		viewCache.integralIcon = (ImageView) mRoot.findViewById(R.id.integral_submit_integral_big_icon);
		viewCache.integralTextLayout = (LinearLayout) mRoot.findViewById(R.id.integral_submit_layout);
		viewCache.describe = (TextView) mRoot.findViewById(R.id.integral_details);
		viewCache.integral = (TextView) mRoot.findViewById(R.id.integral_amount);
		addView(mRoot, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!isLoadSuccess){
			startIntegralAnimation(context);
			isLoadSuccess = true;
		}
	}
	
	public void showIntegralToast(String taskDescribe,int integral){
		if (viewCache == null){
			return;
		}
		//赋值文字
		viewCache.describe.setText(taskDescribe);
		viewCache.integral.setText(integral + "");
		isLoadSuccess = false;
	}
	
	private void startIntegralAnimation(final Context context){
		if (viewCache == null){
			return;
		}
		viewCache.integralIcon.setVisibility(View.VISIBLE);
		viewCache.integralTextLayout.setVisibility(View.INVISIBLE);
		//获得积分图标所在坐标
		final int[] iconLocation = new int[2];
		viewCache.integralIcon.getLocationOnScreen(iconLocation);
		/**积分图标动画*/
		int integralIconWidth = viewCache.integralIcon.getWidth();
		int integralTextLayoutWidth=  viewCache.integralTextLayout.getWidth();
		int iconMiddlePosition = integralTextLayoutWidth/2;
		//积分跳出
		AnimationSet animationSet1 = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(0f, 1.1f, 0.3f, 1);
		sa.setDuration(300);
		TranslateAnimation ta=new TranslateAnimation(iconMiddlePosition, iconMiddlePosition - integralIconWidth/2, 
				ScreenUtil.dip2px(context, 40) + integralIconWidth,
				-ScreenUtil.dip2px(context, 120) + integralIconWidth);
		ta.setDuration(300);
		animationSet1.addAnimation(sa);
		animationSet1.addAnimation(ta);
		//积分落下
		final AnimationSet animationSet2 = new AnimationSet(true);
		ScaleAnimation sa1 = new ScaleAnimation(1.1f, 1f, 1, 0.9f);
		sa1.setDuration(100);
		TranslateAnimation ta1=new TranslateAnimation(iconMiddlePosition - integralIconWidth/2, 
				iconMiddlePosition - integralIconWidth/2, 
				-ScreenUtil.dip2px(context, 120) + integralIconWidth,0 - ScreenUtil.dip2px(context, 10));
		ta1.setDuration(100);
		animationSet2.addAnimation(sa1);
		animationSet2.addAnimation(ta1);
		
		//积分落超过
		final AnimationSet animationSet21 = new AnimationSet(true);
		ScaleAnimation sa21 = new ScaleAnimation(1f, 1.1f, 0.9f, 0.9f);
		sa21.setDuration(10);
		TranslateAnimation ta21=new TranslateAnimation(iconMiddlePosition - integralIconWidth/2, 
				iconMiddlePosition - integralIconWidth/2, 
				ScreenUtil.dip2px(context, 10),0);
		ta21.setDuration(10);
		animationSet21.addAnimation(sa21);
		animationSet21.addAnimation(ta21);
		
		//积分落超过回来
		final AnimationSet animationSet22 = new AnimationSet(true);
		ScaleAnimation sa22 = new ScaleAnimation(1.1f, 1f, 0.9f, 1);
		sa22.setDuration(100);
		TranslateAnimation ta22=new TranslateAnimation(iconMiddlePosition - integralIconWidth/2, 
				iconMiddlePosition - integralIconWidth/2, 
				0,0);
		ta22.setDuration(100);
		animationSet22.addAnimation(sa22);
		animationSet22.addAnimation(ta22);
		
		//积分滚动
		final AnimationSet animationSet3 = new AnimationSet(true);
		RotateAnimation ra1 = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra1.setDuration(300);
		TranslateAnimation ta2=new TranslateAnimation(iconMiddlePosition - integralIconWidth/2, 0, 0, 0);
		ta2.setDuration(300);
		animationSet3.addAnimation(ra1);
		animationSet3.addAnimation(ta2);
		
		//文字展开动画
		//获得文字所在坐标
//		final int[] textLocation = new int[2];
//		viewCache.integralTextLayout.getLocationOnScreen(textLocation);
		//文字动画
		final AnimationSet animationSet4 = new AnimationSet(true);
		ScaleAnimation textSa = new ScaleAnimation(0f, 1, 1, 1);
		textSa.setDuration(300);
		TranslateAnimation textTa=new TranslateAnimation(integralTextLayoutWidth/2, 0, 0, 0);
		textTa.setDuration(300);
		animationSet4.addAnimation(textSa);
		animationSet4.addAnimation(textTa);
		
		animationSet1.setAnimationListener(new AnimationListenerAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				viewCache.integralIcon.startAnimation(animationSet2);
			}
		});
		
		animationSet2.setAnimationListener(new AnimationListenerAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				viewCache.integralIcon.startAnimation(animationSet21);
			}
		});	
		animationSet21.setAnimationListener(new AnimationListenerAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				viewCache.integralIcon.startAnimation(animationSet22);
			}
		});
		animationSet22.setAnimationListener(new AnimationListenerAdapter(){
			@Override
			public void onAnimationEnd(Animation animation) {
				viewCache.integralIcon.startAnimation(animationSet3);
				viewCache.integralTextLayout.setVisibility(View.VISIBLE);
				viewCache.integralTextLayout.startAnimation(animationSet4);
			}
		});
		
		viewCache.integralIcon.startAnimation(animationSet1);
	}
	
	
	
	
	private class AnimationListenerAdapter implements AnimationListener{
		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	private class ViewCache{
		public ImageView integralIcon;//积分图标
		public LinearLayout integralTextLayout;//积分文字布局
		public TextView describe;//任务描述
		public TextView integral;//积分值
	}
}
