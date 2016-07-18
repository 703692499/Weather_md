package com.willchen.weather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.willchen.weather.service.UpdateTimeService;


public class AppWidget extends AppWidgetProvider {
    private Intent intent;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        intent = new Intent(context, UpdateTimeService.class);
        context.startService(intent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        intent = new Intent(context, UpdateTimeService.class);
        context.stopService(intent);
        super.onDeleted(context, appWidgetIds);
    }

}
