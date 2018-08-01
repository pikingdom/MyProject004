/**
 * Create Date:2011-7-15下午04:26:24
 */
package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 总是显示跑马灯效果的TextView
 * 使用时请添加android:singleLine="true"和 android:ellipsize="marquee"两个属性
 * 永久滚动请添加android:marqueeRepeatLimit="marquee_forever"属性
 */
public class AlwaysMarqueeTextView extends TextView{  
    public AlwaysMarqueeTextView(Context context) {  
        super(context);  
    }  
  
    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    @Override  
    public boolean isFocused() {  
        return true;  
    }  
}  
