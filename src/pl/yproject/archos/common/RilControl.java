package pl.yproject.archos.common;

import android.content.Context;
import dalvik.system.DexFile;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 23.11.13
 * Time: 16:28
 */
public class RilControl {

    public static final String RIL_DAEMON = "ril-daemon";
    public static final String STOP = "ctl.stop";

    private Context context;

    public RilControl(Context context) {
        this.context = context;
    }

    public void stopRil() {
        set(RIL_DAEMON, STOP);
    }

    private void set(String key, String mode) throws IllegalArgumentException {
        try{
            @SuppressWarnings("unused")
            DexFile df = new DexFile(new File("/system/app/Settings.apk"));
            @SuppressWarnings("unused")
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            @SuppressWarnings("rawtypes")
            Class[] paramTypes= new Class[2];
            paramTypes[0]= String.class;
            paramTypes[1]= String.class;
            Method set = SystemProperties.getMethod("set", paramTypes);
            Object[] params= new Object[2];
            params[0]= new String(mode);
            params[1]= new String(key);
            set.invoke(SystemProperties, params);
        }catch( IllegalArgumentException iAE ){
            throw iAE;
        }catch( Exception e ){
        }

    }
}
