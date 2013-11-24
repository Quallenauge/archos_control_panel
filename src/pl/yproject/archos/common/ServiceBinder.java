package pl.yproject.archos.common;

import android.app.Service;
import android.os.Binder;

/**
 * Created with notepad.exe.
 * Author: mordesku
 * Date: 13.10.2013
 * Time: 10:59
 */
public class ServiceBinder<T extends Service> extends Binder {
    private T service;

    public ServiceBinder(T service) {
        this.service = service;
    }

    public T getService() {
            return service;
    }

}
