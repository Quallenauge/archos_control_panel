package pl.yproject.archos.intents;

import android.content.Context;
import android.content.Intent;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 12.12.13
 * Time: 19:48
 */
public class StartIntent extends Intent {
    public static final String BOOTED_EXTRA = "BOOTED";
    public StartIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }
}
