package pl.yproject.archos.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import pl.yproject.archos.activities.ArchosControlPanelActivity;
import pl.yproject.archos.services.ArchosControlService;
import pl.yproject.archos_controll.R;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 24.11.13
 * Time: 11:51
 */
public class StatusWidget extends AppWidgetProvider {
    private SharedPreferences preferences;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(getClass().getName(), "Updating widget");
        attemptToSetPreferences(context);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = initializeWidgetClickListeners(context);
            refreshStatuses(views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void attemptToSetPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(ArchosControlService.PREFERENCES_NAME, ArchosControlService.PREFERENCES_MODE);
        }
    }

    private void refreshStatuses(RemoteViews views) {
        Boolean deepSleep = preferences.getBoolean(ArchosControlService.DEEP_SLEEP_STATUS_PREFS_NAME, false);
        Boolean dongle = preferences.getBoolean(ArchosControlService.DONGLE_STATUS_PREFS_NAME, false);
        views.setTextViewText(R.id.deepSLeepStatusView, "Deep sleep is " + (deepSleep ? "enabled" : "disabled"));
        views.setTextViewText(R.id.usbDongleStatusView, "3G stick is " + (dongle ? "enabled" : "disabled"));
    }

    private RemoteViews initializeWidgetClickListeners(Context context) {
        Intent intent = new Intent(context, ArchosControlPanelActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.status_widget);
        views.setOnClickPendingIntent(R.id.deepSLeepStatusView, pendingIntent);
        views.setOnClickPendingIntent(R.id.usbDongleStatusView, pendingIntent);
        return views;
    }
}
