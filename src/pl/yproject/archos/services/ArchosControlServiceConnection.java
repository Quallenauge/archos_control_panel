package pl.yproject.archos.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import pl.yproject.archos.common.ServiceBinder;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 22.11.13
 * Time: 19:37
 */
public class ArchosControlServiceConnection implements ServiceConnection {


    private Connectable connectable;
    public ArchosControlServiceConnection(Connectable connectable) {
        this.connectable = connectable;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ServiceBinder<ArchosControlService> binder = (ServiceBinder<ArchosControlService>) service;
        connectable.connect(binder.getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        connectable.disconnect();
    }
}
