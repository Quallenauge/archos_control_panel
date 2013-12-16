package pl.yproject.archos.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import pl.yproject.archos.intents.StartIntent;
import pl.yproject.archos.services.ArchosControlService;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 22.11.13
 * Time: 18:02
 */
public class StartupReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(),"ArchosControll Started");
        StartIntent service = new StartIntent(context, ArchosControlService.class);
        service.putExtra(StartIntent.BOOTED_EXTRA, true);
        context.startService(service);
    }
}
