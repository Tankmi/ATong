package huitx.libztframework.utils;


import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HandlerUtils extends Handler {

    private static HandlerUtils instance = null;
    WeakReference<Context> mActivityReference;

    public static HandlerUtils getInstance(Context context) {
        if (instance == null) {
            instance = new HandlerUtils(context.getApplicationContext());
        }
        return instance;
    }

    HandlerUtils(Context context) {
        mActivityReference = new WeakReference<>(context);
    }
}
