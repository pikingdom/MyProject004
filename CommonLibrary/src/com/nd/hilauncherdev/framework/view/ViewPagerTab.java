package com.nd.hilauncherdev.framework.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.ColorUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

/**
 * 与ViewPager联动的tab
 */
public class ViewPagerTab extends View {

	public static int DEFAULT_THEME = 0, RED_THEME = 1;
	// 主题
	private int theme = DEFAULT_THEME;

	private int selectColor, defaultColor;
	int selectShadowColor, defaultShadowColor;
	private float maxWidth = 0, distanceScale, touchedTab = -1;
	private int popWidth, selectTab = CommonGlobal.NO_DATA;
	/**
	 * logo图标与文字的间距
	 */
	private int drawablePadding;
	private boolean showMore;
	private int hlLineLeft, lineTop, textMargin, textHeight, textTop, hlLineWidth, hlLineInitCenter;

	private Context ctx;
	private PopupWindow popup;
	private ViewPager viewpager;
	private Drawable line, hlLine, more;
	private ListView layout;
	private ArrayAdapter<String> adapter;
	private Paint textPaint = new Paint();
	private List<String> popupString = new ArrayList<String>();
	private Map<String, String> showingTitle = new HashMap<String, String>();
	private List<TabInfo> tabs = new ArrayList<TabInfo>();

	//tab 之间的分割线
	private Drawable tabDivideImg;
	public ViewPagerTab(Context context) {
		super(context);
		init(context);
	}

	public ViewPagerTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ViewPagerTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		Resources res = context.getResources();
		selectColor = res.getColor(R.color.myphone_common_dialog_text_color);
		selectShadowColor = ColorUtil.antiColorAlpha(255, selectColor);
		defaultColor = res.getColor(R.color.myphone_common_little_text_color);
		defaultShadowColor = ColorUtil.antiColorAlpha(255, defaultColor);

		textPaint.setAntiAlias(true);
		textPaint.setColor(defaultColor);
		// textPaint.setShadowLayer(1, 1, 1, defaultShadowColor);
		textPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.frame_viewpager_tab_textsize));
		textHeight = textPaint.getFontMetricsInt(null);
		textMargin = textHeight / 2;

		line = context.getResources().getDrawable(R.drawable.myphone_common_tab_unfocused);
		hlLine = context.getResources().getDrawable(R.drawable.myphone_common_tab_focused);
		more = context.getResources().getDrawable(R.drawable.myphone_common_tab_more);
		ctx = context;
		drawablePadding = ScreenUtil.dip2px(ctx, 10);
	}

	/**
	 * 默认显示的TAB
	 * @param logo 图标资源ID
	 * @param title
	 */
	public void addIconAndTitile(int logo[], String title[]) {
		final int N = title.length;
		for (int i = 0; i < N; i++) {
			showingTitle.put(title[i], "");
			TabInfo tab = new TabInfo();
			tab.logo = ctx.getResources().getDrawable(logo[i]);
			tab.title = title[i];
			tab.width = textPaint.measureText(tab.title);
			tabs.add(tab);
			if (tab.width > maxWidth)
				maxWidth = tab.width;
		}

		if (selectTab == CommonGlobal.NO_DATA)
			selectTab = (N - 1) / 2;
	}

	/**
	 * 默认显示的TAB
	 * @param title
	 */
	public void addTitle(String title[]) {
		final int N = title.length;
		for (int i = 0; i < N; i++) {
			showingTitle.put(title[i], "");
			TabInfo tab = new TabInfo();
			tab.title = title[i];
			tab.width = textPaint.measureText(tab.title);
			tabs.add(tab);
			if (tab.width > maxWidth)
				maxWidth = tab.width;
		}

		if (selectTab == CommonGlobal.NO_DATA)
			selectTab = (N - 1) / 2;

		// hlLineWidth = (int) (maxWidth * 3 / 2);
	}

	/**
	 * 所有的TAB
	 * @param titles
	 */
	public void addAllTitle(List<String> titles) {
		if (titles == null)
			return;

		for (String ti : titles) {
			int wid = (int) textPaint.measureText(ti);
			if (wid > popWidth) {
				popWidth = wid;
			}
			if (showingTitle.get(ti) == null)
				popupString.add(ti);
		}

		showMore = true;
		// layout = (ListView) View.inflate(ctx, R.layout.list_7, null);
		layout = new ListView(ctx);
		layout.setCacheColorHint(Color.TRANSPARENT);
		layout.setDivider(getContext().getResources().getDrawable(R.drawable.file_manager_address_separator));
		adapter = new TitleItemAdapter(getContext(), popupString);
		layout.setAdapter(adapter);
		layout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectTab < 0 || selectTab >= tabs.size()) {
					return;
				}

				if (!(viewpager instanceof LazyViewPager))
					return;

				String title = adapter.getItem(position);
				TabInfo info = tabs.get(selectTab);
				popupString.remove(title);
				popupString.add(info.title);
				adapter.notifyDataSetChanged();
				showingTitle.remove(info.title);
				showingTitle.put(title, "");
				info.title = title;
				info.left = info.center - textPaint.measureText(title) / 2;
				invalidate();
				popup.dismiss();

				((LazyViewPager) viewpager).refreshView(selectTab, title);
			}
		});

		popup = new PopupWindow(layout, (int) (popWidth * 1.5f), LayoutParams.WRAP_CONTENT);
		popup.setOutsideTouchable(true);
	}

	public void setTextSize(int size) {
		textPaint.setTextSize(size);
	}

	class TabInfo {
		Drawable logo;
		String title;
		float width;
		float left, top, center;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int N = tabs.size();
		if (N <= 1)
			return;

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		textTop = (height - (textHeight * 2 + textMargin)) / 2 + textHeight + textMargin;
		lineTop = textTop + textMargin;
		float margin = (width - N * maxWidth) / N;
		float startLeft = margin / 2 + (maxWidth - tabs.get(0).width) / 2;
		for (int i = 0; i < N; i++) {
			TabInfo info = tabs.get(i);
			info.left = startLeft + (maxWidth + margin) * i + (maxWidth - info.width) / 2;
			info.center = info.left + info.width / 2;
			info.top = textTop;
		}
		hlLineWidth = width / tabs.size();
		hlLineLeft = (int) (tabs.get(0).center - hlLineWidth / 2);
		distanceScale = (tabs.get(1).center - tabs.get(0).center) / width;
		line.setBounds(0, lineTop, width, lineTop + line.getIntrinsicHeight());
		hlLineInitCenter = (int) (tabs.get(selectTab).center - hlLineWidth / 2);
		hlLine.setBounds(hlLineInitCenter, lineTop, hlLineInitCenter + hlLineWidth, lineTop + hlLine.getIntrinsicHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int N = tabs.size();
		if (N <= 1)
			return;

		for (int i = 0; i < N; i++) {
			TabInfo info = tabs.get(i);
			if (i == selectTab || i == touchedTab) {
				textPaint.setColor(selectColor);
				// textPaint.setShadowLayer(1, 1, 1, selectShadowColor);
			} else {
				textPaint.setColor(defaultColor);
				// textPaint.setShadowLayer(1, 1, 1, defaultShadowColor);
			}
			if (null != info.logo) {
				final int left = (int) info.left - drawablePadding - ctx.getResources().getDimensionPixelSize(R.dimen.frame_viewpager_tab_textsize);
				int top = textTop - info.logo.getIntrinsicHeight() / 2 - 8;
				info.logo.setBounds(left, top, left + info.logo.getIntrinsicWidth(), top + info.logo.getIntrinsicHeight());
				info.logo.draw(canvas);
			}
			canvas.drawText(info.title, info.left, info.top, textPaint);

			if (showMore && i == selectTab) {
				final int left = (int) info.left - ctx.getResources().getDimensionPixelSize(R.dimen.frame_viewpager_tab_textsize);
				int top = textTop - more.getIntrinsicHeight() / 2 - 8;
				more.setBounds(left, top, left + more.getIntrinsicWidth(), top + more.getIntrinsicHeight());
				more.draw(canvas);
			}

		}

		line.draw(canvas);
		hlLine.draw(canvas);
		//画tab之间的分割线
		if (null != tabDivideImg) {
			int divideWidth = ScreenUtil.dip2px(ctx, 2);
			int divideHeight = textHeight;
			int divideTop = textTop - textHeight;
			for (int i = 0; i < N - 1; i++) {
				int divideLeft = (i + 1) * hlLineWidth - divideWidth / 2;
				tabDivideImg.setBounds(divideLeft, divideTop, divideLeft + divideWidth, divideTop + divideHeight);
				tabDivideImg.draw(canvas);
			}
		}
	}

	public void scrollHighLight(int scrollX) {
		hlLineInitCenter = hlLineLeft + (int) (scrollX * distanceScale);
		hlLine.setBounds(hlLineInitCenter, lineTop, hlLineInitCenter + hlLineWidth, lineTop + hlLine.getIntrinsicHeight());
		invalidate();
	}

	public void updateSelected() {
		Rect bound = hlLine.getBounds();
		final int y = bound.top + (bound.bottom - bound.top) / 2;
		for (int i = 0; i < tabs.size(); i++) {
			if (!bound.contains((int) tabs.get(i).center, y))
				continue;

			selectTab = i;
			invalidate();
			break;
		}
	}

	public int getSelectedTab() {
		return selectTab;
	}

	public void setViewpager(ViewPager viewpager) {
		this.viewpager = viewpager;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			TabInfo hintInfo = getHitRangeInfo(event);
			if (hintInfo != null) {
				final int touchedTab = tabs.indexOf(hintInfo);
				if (touchedTab == selectTab)
					showMore(hintInfo);
				else
					viewpager.snapToScreen(tabs.indexOf(hintInfo));
			}
			touchedTab = -1;
			invalidate();
		} else if (action == MotionEvent.ACTION_DOWN) {
			TabInfo hintInfo = getHitRangeInfo(event);
			if (hintInfo != null) {
				touchedTab = tabs.indexOf(hintInfo);
				invalidate();
			}
		}
		return true;
	}

	/**
	 * 弹出未被显示的TAB
	 */
	private void showMore(TabInfo hintInfo) {
		if (popup == null)
			return;

		popup.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.frame_viewpager_tab_popup_bg));
		popup.setFocusable(true);
		int xoff = (int) (hlLineInitCenter + hlLineWidth / 2 - popup.getWidth() / 2);
		xoff = xoff < 0 ? 0 : xoff;
		popup.showAsDropDown(this, xoff, (int) this.getTop());
		popup.update();
		// popup.setAnimationStyle(R.style.ShortCutMenuGrowFromTop);
	}

	private TabInfo getHitRangeInfo(MotionEvent e) {
		int x = Math.round(e.getX());
		int y = Math.round(e.getY());
		Rect hitRect;
		for (int index = 0; index < tabs.size(); index++) {
			TabInfo info = tabs.get(index);
			hitRect = new Rect((int) (info.center - info.width), (int) (info.top - textHeight - textMargin), (int) (info.center + info.width), (int) (info.top + textMargin));
			if (hitRect.contains(x, y)) {
				return info;
			}
		}

		return null;
	}

	/**
	 * 指定默认显示的TAB
	 * @param tab
	 */
	public void setInitTab(int tab) {
		this.selectTab = tab;
	}

	/**
	 * 未显示TAB列表
	 */
	class TitleItemAdapter extends ArrayAdapter<String> {
		private LayoutInflater mInflater;

		public TitleItemAdapter(Context context, List<String> addressList) {
			super(context, 0, addressList);
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String title = getItem(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.framework_viewpager_tab_title_item, parent, false);
			}

			TextView titleview = (TextView) convertView.findViewById(R.id.frame_viewpager_tab_title_item_text);
			titleview.setText(title);
			return convertView;
		}
	}

	/****************************************************************
	 * add by linqiang(866116) 添加排序功能TAB 和 addAllTitle不能同时实现
	 ****************************************************************/
	private List<List<String>> popupStr4sort;
	private List<ArrayAdapter<String>> adapter4sort;

	/**
	 * 实现排序功能
	 * @param titles
	 */
	public void addSortTitle(String[]... titles) {
		if (titles == null)
			return;
		// 初始化
		popupStr4sort = new ArrayList<List<String>>();
		adapter4sort = new ArrayList<ArrayAdapter<String>>();
		showMore = true;
		for (int i = 0; i < titles.length; i++) {
			List<String> list = new ArrayList<String>();
			for (String ti : titles[i]) {
				int wid = (int) textPaint.measureText(ti);
				if (wid > popWidth) {
					popWidth = wid;
				}
				list.add(ti);
			}
			popupStr4sort.add(list);
			ArrayAdapter<String> adapterTemp = new TitleItemAdapter(getContext(), list);
			adapter4sort.add(adapterTemp);
		}

		// layout = (ListView) View.inflate(ctx, R.layout.list_7, null);
		layout = new ListView(ctx);
		layout.setCacheColorHint(Color.TRANSPARENT);
		layout.setDivider(getContext().getResources().getDrawable(R.drawable.file_manager_address_separator));
		layout.setAdapter(adapter4sort.get(selectTab));
		layout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectTab < 0 || selectTab >= tabs.size()) {
					return;
				}

				if (!(viewpager instanceof LazyViewPager))
					return;
				ArrayAdapter<String> adapter = adapter4sort.get(selectTab);
				String title = adapter.getItem(position);
				adapter.notifyDataSetChanged();
				invalidate();
				popup.dismiss();
				((LazyViewPager) viewpager).refreshView(selectTab, title);
			}
		});
		popup = new PopupWindow(layout, (int) (maxWidth * 1.5f), LayoutParams.WRAP_CONTENT);
		popup.setOutsideTouchable(true);
	}

	/**
	 * 
	 * @param selectTab
	 */
	public void setSortTitle(int selectTab) {
		if (selectTab < 0 || selectTab >= tabs.size()) {
			return;
		}
		ArrayAdapter<String> adapter = adapter4sort.get(selectTab);
		layout.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}

	/****************************************************************
	 * end add by linqiang(866116) 添加排序功能TAB
	 ****************************************************************/
		
	public void setTabDivideImg(Drawable drawable){
		tabDivideImg = drawable;
	}
	
	public void setLineImg(Drawable drawable){
		line = drawable;
	}
	
	public void setHlLineImg(Drawable drawable){
		hlLine = drawable;
	}
	
	public void setSelectColor(int color){
		selectColor = color;
	}
}
