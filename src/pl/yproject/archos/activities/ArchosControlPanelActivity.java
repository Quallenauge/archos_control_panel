package pl.yproject.archos.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import pl.yproject.archos.common.StatusSwitchChangedListener;
import pl.yproject.archos.services.ArchosControlService;
import pl.yproject.archos.services.ArchosControlServiceConnection;
import pl.yproject.archos.services.Connectable;
import pl.yproject.archos.widget.StatusWidget;
import pl.yproject.archos_controll.R;

public class ArchosControlPanelActivity extends Activity implements Connectable<ArchosControlService> {
    public static final String ANDROID_UPDATE_ACTION_NAME = "android.appwidget.action.APPWIDGET_UPDATE";
    private Boolean serviceBinded;
    private ArchosControlService service;
    private ServiceConnection archosServiceConnection;
    private SharedPreferences preferences;
    private Switch deepSleepSwitch;
    private Switch usbDongleSwicth;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        Log.d(getLocalClassName(), "ArchosControlPanel Activity");
        preferences = getSharedPreferences(ArchosControlService.PREFERENCES_NAME, ArchosControlService.PREFERENCES_MODE);
        bindService();
        initViews();
    }

    private void initViews() {
        deepSleepSwitch = (Switch)findViewById(R.id.deepSleepStatus);
        usbDongleSwicth = (Switch)findViewById(R.id.usbDongleStatus);
        deepSleepSwitch.setChecked(preferences.getBoolean(ArchosControlService.DEEP_SLEEP_STATUS_PREFS_NAME, false));
        usbDongleSwicth.setChecked(preferences.getBoolean(ArchosControlService.DONGLE_STATUS_PREFS_NAME, false));
        usbDongleSwicth.setOnCheckedChangeListener(new StatusSwitchChangedListener(this));
        deepSleepSwitch.setOnCheckedChangeListener(new StatusSwitchChangedListener(this));
    }

    private void bindService() {
        archosServiceConnection = new ArchosControlServiceConnection(this);
        Intent intent = new Intent(this, ArchosControlService.class);
        bindService(intent, archosServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void connect(ArchosControlService service) {
        this.service = service;
        serviceBinded = true;
        getStatusField().setText(getString(R.string.service_connected));
    }

    @Override
    public void disconnect() {
        this.service = null;
        serviceBinded = false;
        getStatusField().setText(getString(R.string.service_not_connected));
    }

    private TextView getStatusField() {
        return ((TextView)findViewById(R.id.serviceConnectionStatus));
    }

    public void setPrefs() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ArchosControlService.DEEP_SLEEP_STATUS_PREFS_NAME, deepSleepSwitch.isChecked());
        editor.putBoolean(ArchosControlService.DONGLE_STATUS_PREFS_NAME, usbDongleSwicth.isChecked());
        editor.commit();
        refreshWidget();
        if (serviceBinded) {
            service.reloadPreferences();
        }
    }

    private void refreshWidget() {
        Intent refreshWidgetIntent = new Intent(this, StatusWidget.class);
        refreshWidgetIntent.setAction(ANDROID_UPDATE_ACTION_NAME);
        refreshWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAllWidgetIds());
        sendBroadcast(refreshWidgetIntent);
    }

    private int[] getAllWidgetIds() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        return appWidgetManager.getAppWidgetIds(new ComponentName(this, StatusWidget.class));
    }
}
