package com.nd.hilauncherdev.framework.view;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.nd.android.pandahome2.R;

/**
 * 指示灯loading页面
 */
public class LoadingView extends View {
	
	/**
	 * 图标缓存
	 */
	private static HashMap<String, SoftReference<Drawable>> drawables;

	/**
	 * loaddingview 实例上下文
	 */
	private Context context;
	
	/**
	 * 依次 
	 * 文字：文字高度:文字宽度
	 */
	private String text;
	private int randomIndex;
	private TextPaint textPaint;
	
	/**
	 * 随机数目@对应string.xml文件
	 */
	private final String textStringResName = "loadingview_text_";
	
	/**
	 * 文字条数
	 */
	private final int textsCount = 15;
	
	/**
	 * 图片：图片间隔@默认值20：图片跟文字间隔@默认30
	 * 图片宽度：图片高度 ：指示灯集合
	 * 指示灯数量
	 */
	private Drawable drawableLight, drawableBlack;
	private int drawablePadding = 20;
	private int textPadding = 30;
	private int drawableWidth;
	private int drawableHeight;
	private int lightCount = 3;

	/**
	 * view宽度：view高度：画笔
	 */
	private float viewWidth;
	private float viewHeight;
	private Paint paint;
	

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public LoadingView(Context context) {
		super(context);
		this.context = context;
	}

	
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setContent(R.drawable.loadingview_point_light, R.drawable.loadingview_point_black, drawablePadding, textPadding);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		/**
		 * 停止动画
		 */
		isProcess = false;
		/**
		 * 清理缓存
		 */
		cleanup();
	}

	
	/**
	 * 是否开始ondraw
	 */
	private boolean isProcess;
	private StaticLayout staticLayout;
	private void setContent(int pointLightResId, int pointBlackResId, int drawablePadding, int textPadding) {

		/**
		 * init content
		 * <text & drawable>
		 */
		initTexts();
		this.drawablePadding = dip2px(context, drawablePadding);
		this.textPadding = dip2px(context, textPadding);
		
		drawableLight = context.getResources().getDrawable(pointLightResId);
		drawableBlack = context.getResources().getDrawable(pointBlackResId);

		drawableWidth = drawableLight.getIntrinsicWidth();
		drawableHeight = drawableLight.getIntrinsicHeight();

		/**
		 * init tools
		 */
		paint = new Paint();
		paint.setAntiAlias(true);

		textPaint = new TextPaint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(dip2px(context,20));
		
		/**
		 * 开始动画
		 */
		isProcess = true;
	}

	
	/**
	 * 获取图片缓存
	 * @return HashMap
	 */
	public static HashMap<String, SoftReference<Drawable>> getIconCache() {
		if (drawables == null) {
			drawables = new HashMap<String, SoftReference<Drawable>>();
		}
		return drawables;
	}
	
	
	/**
	 * 控制指示灯
	 */
	private int index;
	private void setDrawables() {
		for (int i = 0; i < lightCount; i++) {
			getIconCache().put("" + i, new SoftReference<Drawable>(drawableBlack));
		}
		drawables.put("" + index % lightCount, new SoftReference<Drawable>(drawableLight));
		index++;
	}

	/**
	 * 清空图片引用
	 */
	private void cleanup() {
		for (SoftReference<Drawable> icon : drawables.values()) {
			final Drawable drawable = icon.get();
			if (drawable != null) {
				drawable.setCallback(null);
			}
		}
		drawables.clear();
	}

	/**
	 * 初始化文字数组
	 */
	private void initTexts() {
		randomIndex = (int) (Math.random() * textsCount)+1;
		int textId = getResources().getIdentifier(textStringResName + randomIndex, "string", context.getPackageName());
		text = getResources().getString(textId);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (!isProcess) {
			return;
		}

		setDrawables();
		
		if(null == staticLayout){
			staticLayout = new StaticLayout(text, textPaint, (int) (viewWidth - textPadding), Layout.Alignment.ALIGN_CENTER, 0.6F, 0.0F, true);
		}

		int lineCount = staticLayout.getLineCount();
		for (int i = 0; i < lineCount; i++) {
			final String lineText = text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i));
			int x = (int) ((viewWidth  - getTextWidth(textPaint, lineText))/2);
			int y = (int)((viewHeight - drawableHeight) / 2 - textPadding - (lineCount-i)*getTextHeight(textPaint));
			canvas.drawText(lineText, x, y, textPaint);
		}
		
		drawables.get(""+0).get().setBounds((int) (viewWidth - drawableWidth) / 2 - drawablePadding,
				(int) (viewHeight - drawableHeight) / 2, (int) (viewWidth + drawableWidth) / 2 - drawablePadding,
				(int) (viewHeight + drawableHeight) / 2);
		drawables.get(""+0).get().draw(canvas);
		
		drawables.get(""+1).get().setBounds((int) (viewWidth - drawableWidth) / 2, (int) (viewHeight - drawableHeight) / 2,
				(int) (viewWidth + drawableWidth) / 2, (int) (viewHeight + drawableHeight) / 2);
		drawables.get(""+1).get().draw(canvas);
		
		drawables.get(""+2).get().setBounds((int) (viewWidth - drawableWidth) / 2 + drawablePadding,
				(int) (viewHeight - drawableHeight) / 2, (int) (viewWidth + drawableWidth) / 2 + drawablePadding,
				(int) (viewHeight + drawableHeight) / 2);
		drawables.get(""+2).get().draw(canvas);
		
		if (!isRunning) {
			isRunning = true;
			postDelayed(runnable, 1000);
		}
	}

	/**
	 * 更新指示灯线程
	 * lastTime 							：最后刷新时间
	 * delayMillis							：预控制刷新时间
	 * isRunning							：控制线程
	 */
	private long lastTime;
	private final int delayMillis = 1000;
	private boolean isRunning;
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			long dis = System.currentTimeMillis() - lastTime;
			if (dis >= delayMillis) {
				lastTime = System.currentTimeMillis();
				invalidate();
			} else {
				postInvalidateDelayed(dis);
			}
			isRunning = false;
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float w = MeasureSpec.getSize(widthMeasureSpec);
		if (w != viewWidth) {
			viewWidth = w;
			float h = MeasureSpec.getSize(heightMeasureSpec);
			viewHeight = h;
			if (h < w) {
				viewWidth = h;
				viewHeight = w;
			}
		}
	}

	/**
	 * 获取text高度
	 * @param paint
	 * @return float
	 */
	private float getTextHeight(Paint paint) {
		Paint.FontMetrics localFontMetrics = paint.getFontMetrics();
		return (float) Math.ceil(localFontMetrics.descent - localFontMetrics.ascent);
	}

	/**
	 * 获取text宽度
	 * @param paint
	 * @param text
	 * @return float
	 */
	private float getTextWidth(Paint paint, String text) {
		return paint.measureText(text);
	}
	private static float currentDensity = 0;
	public static int dip2px(Context context, float dipValue) {
		if (currentDensity > 0)
			return (int) (dipValue * currentDensity + 0.5f);

		currentDensity = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * currentDensity + 0.5f);
	}
}
