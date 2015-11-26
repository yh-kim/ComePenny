package com.enterpaper.comepenny.util;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.enterpaper.comepenny.R;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidgetMain extends AppWidgetProvider {
    static final String ACTION_CLICK = "CLICK";
    static int rgbGet = Color.rgb(168,168,168);

    /**
     * 브로드 캐스트를 수신할 때, overrid된 콜백 메소드가 호출되기 직전에 호출됨
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        // 해당 버튼이 클릭 되었는지 판별하고 로직 수행
        if(action != null && action.equals(ACTION_CLICK)){
            int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            rgbGet = intent.getIntExtra("rgb", Color.rgb(0,0,0));

            updateAppWidget(context, AppWidgetManager.getInstance(context), id);   // 버튼이 클릭되면 새로고침 수행

        }

        super.onReceive(context, intent);
    }



    /**
     * 위젯을 갱신할 때 호출됨
     * 주의 : configure activity를 정의했을 때에는 위젯 등록시 처음 한번은 호출이 되지 않음
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }



    /**
     * 위젯이 처음 생성될 때 호출됨
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }



    /**
     * 위젯의 마지막 인스턴스가 제거될 때 호출됨
     * onEnabled()에서 정의한 리소스 정리할 때
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



    /**
     * 위젯이 사용자에 의해 제거될 때 호출됨
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }


    /**
     * 위젯 업데이트
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String widgetText = "아무것도 못 먹었다. 배가 고프다.";
        // RemoteViews를 이용해 Text 설정
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_main);

        views.setTextViewText(R.id.main_widget_tv_content, widgetText + rgbGet);
        views.setTextColor(R.id.main_widget_tv_content, rgbGet);

        // intent 달기
        Intent intent = new Intent(context, WidgetConfigureActivity.class);
        intent.setAction(ACTION_CLICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.main_widget_tv_content, pendingIntent);

        // 위젯 업데이트
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
}

