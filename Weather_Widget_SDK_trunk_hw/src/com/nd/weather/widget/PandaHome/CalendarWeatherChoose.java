package com.nd.weather.widget.PandaHome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.nd.weather.widget.R;

public class CalendarWeatherChoose extends Activity{
        
    /**
     * Intent widget名称
     */
    private static final String WIDGET_NAME = "widget_name"; 
    
    /**
     * Intent 包名
     */
    private static final String WIDGET_PACKAGE = "widget_package";
    
    /**
     * Intent Widget布局资源
     */
    private static final String WIDGET_LAYOUT_RESOURCE = "widget_layout_resource";
    
    /**
     * Intent widget预览图资源
     */
    private static final String WIDGET_PREVIEW_RESOURCE = "widget_preview_resource";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent data = new Intent();
        String packageName = CalendarWeatherChoose.this.getApplication().getPackageName();
        
        data.putExtra( WIDGET_NAME, this.getString( R.string.sdk_app_name ) );
        data.putExtra( WIDGET_PACKAGE, packageName );
        data.putExtra( WIDGET_LAYOUT_RESOURCE, new int[]{R.xml.weather_widget_4x2, R.xml.weather_widget_4x1 } );
        data.putExtra( WIDGET_PREVIEW_RESOURCE, new int[]{R.drawable.widget_weather_preview_4x2, R.drawable.widget_weather_preview_4x1 } );

        setResult(RESULT_OK, data);
        finish();
    }
}
