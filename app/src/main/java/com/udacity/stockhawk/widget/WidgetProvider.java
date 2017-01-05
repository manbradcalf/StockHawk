package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.WidgetRemoteViewsService;

/**
 * Created by ben.medcalf on 1/3/17.
 */

public class WidgetProvider extends AppWidgetProvider
{
    // This is called to update the App Widget at intervals defined by the updatePeriodMillis
    // attribute in the AppWidgetProviderInfo.
    // This method is also called when the user adds the App Widget, so it should perform the
    // essential setup, such as define event handlers for Views and start a temporary Service,
    // if necessary. However, if you have declared a configuration Activity, this method is not
    // called when the user adds the App Widget, but is called for the subsequent updates. It is the
    // responsibility of the configuration Activity to perform the first update when configuration
    // is done. (See Creating an App Widget Configuration Activity below.)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {

        CharSequence widgetText = context.getString(R.string.widget_title_string);
        for (int appWidgetId : appWidgetIds) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_info_widget);
        views.setTextViewText(R.id.widget_title, widgetText);
        views.setRemoteAdapter(R.id.widget_listview,
                new Intent(context, WidgetRemoteViewsService.class));

        Intent appIntent = new Intent(context, MyStocksActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_text, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}
