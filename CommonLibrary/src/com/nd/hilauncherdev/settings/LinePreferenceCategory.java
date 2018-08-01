package com.nd.hilauncherdev.settings;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

/**
 * 将自定义分隔栏样式提升到CommonLibrary包。
 * <p>Title: PromptPreferenceCategory</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 *
 * @author MaoLinnan
 * @date 2013-11-5
 */
public class LinePreferenceCategory extends PreferenceCategory {
    private Context context = null;

    public LinePreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.v8_settings_color));
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(Color.parseColor("#6d8494"));
            int dip = ScreenUtil.dip2px(view.getContext(), 1);
            tv.setPadding(0 * dip, 5 * dip, 0, 5 * dip);
            tv.setTextSize(0);
        }
    }
}
