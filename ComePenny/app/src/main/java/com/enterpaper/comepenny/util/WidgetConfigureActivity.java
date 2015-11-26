package com.enterpaper.comepenny.util;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.enterpaper.comepenny.R;

/**
 * Created by Kim on 2015-11-26.
 */
public class WidgetConfigureActivity extends Activity {
    private int mAppWidgetId;
    private AppWidgetManager appWidgetManager;
    private RemoteViews remoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);

        Intent intent = getIntent();
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.e("ttttt", mAppWidgetId+"");

        ColorPickerDialog dialog = new ColorPickerDialog(this,mAppWidgetId);
        dialog.show();
    }
}
