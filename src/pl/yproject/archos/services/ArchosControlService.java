package pl.yproject.archos.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import pl.yproject.archos.common.RilControl;
import pl.yproject.archos.common.ServiceBinder;

import java.io.IOException;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 22.11.13
 * Time: 19:31
 */
public class ArchosControlService extends Service {
    private static final String DEEP_SLEEP_BINARY = "/system/bin/deepsleeper";
    private static final String SYSFS_RFKILL_ENTRY = "/sys/class/rfkill/rfkill0/state";
    public static final String PREFERENCES_NAME = "ARCHOS_CONTROL_PANEL_PREFS";
    public static final int PREFERENCES_MODE = 0;
    public static final String DONGLE_STATUS_PREFS_NAME = "dongleStatus";
    public static final String DEEP_SLEEP_STATUS_PREFS_NAME = "deepSleepStatus";
    public static final String SU_BINARY = "/system/bin/su";

    private IBinder binder;
    private SharedPreferences preferences;
    private Boolean deepSleep = false;
    private Boolean usbDongle = false;
    private RilControl rilControl;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        rilControl = new RilControl(this);
        preferences = getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE);
        reloadPreferences();
        Log.d(getClass().getName(), "ArchosControlService started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        attemptToCreateBinder();
        return binder;
    }

    private void attemptToCreateBinder() {
        if (binder == null) {
            binder = new ServiceBinder<ArchosControlService>(this);
        }
    }

    public void reloadPreferences() {
        attemptToChangeDeepSleepMode();
        attemptToChangeUsbDongleMode();
    }

    private void attemptToChangeUsbDongleMode() {
        if (usbDongle != preferences.getBoolean(DONGLE_STATUS_PREFS_NAME, false)) {
            usbDongle = preferences.getBoolean(DONGLE_STATUS_PREFS_NAME, false);
            setUsbDongleMode();
        }
    }

    private void attemptToChangeDeepSleepMode() {
        if (deepSleep != preferences.getBoolean(DEEP_SLEEP_STATUS_PREFS_NAME, false)) {
            deepSleep = preferences.getBoolean(DEEP_SLEEP_STATUS_PREFS_NAME, false);
            setDeepSleepMode();
        }
    }

    private void setDeepSleepMode() {
        try {
            Runtime.getRuntime().exec(new String[] {SU_BINARY, "-c", DEEP_SLEEP_BINARY + " " + (deepSleep ? "1" : "0") + "\""});
            Toast.makeText(this, "Deep sleep toggled " + (deepSleep ? "on!" : "off!"), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error changing deep sleep status!", Toast.LENGTH_LONG).show();
        }
    }

    private void setUsbDongleMode() {
        rilControl.stopRil();
        try {
            Runtime.getRuntime().exec(new String[] {SU_BINARY, "-c", "echo " + (usbDongle ? "1" : "0") + " >" + SYSFS_RFKILL_ENTRY});
            Toast.makeText(this, "3G dongle toggled " + (usbDongle ? "on!" : "off!"), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error changing 3G dongle status!", Toast.LENGTH_LONG).show();
        }


    }

}
