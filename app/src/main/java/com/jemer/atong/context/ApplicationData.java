package com.jemer.atong.context;

import android.telephony.TelephonyManager;

import huitx.libztframework.context.LibApplicationData;
import huitx.libztframework.utils.LOGUtils;

public class ApplicationData extends LibApplicationData {


    /**
     * 获取imei号
     *
     */
    public static void getDatas() {
        // 获取IMEI号
        TelephonyManager tele = (TelephonyManager) context .getSystemService(TELEPHONY_SERVICE);
        imei = tele.getDeviceId();
        LOGUtils.LOG("LibApplicationData 获取IMEI号   " + imei);
    }
}
