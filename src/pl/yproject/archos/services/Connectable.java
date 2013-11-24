package pl.yproject.archos.services;

import android.app.Service;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 22.11.13
 * Time: 19:38
 */
public interface Connectable<T extends Service> {
    public void connect(T service);
    public void disconnect();
}
