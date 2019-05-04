package huitx.libztframework.context;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.List;

import huitx.libztframework.utils.CrashHandler;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;

public class LibApplicationData extends Application {

    public static Context context;
    /**
     * imei号
     */
    public static String imei;


    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象  
    private static LibApplicationData ApplicationDatainstance;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());//初始化抓取异常的工具类！

        imei = PreferencesUtils.getString(context, "app_imei", "");
//        getDatas(this);
        getInstance();

    }

    /**
     * 获取imei号
     *
     */
    public static void getDatas() {
        // 获取IMEI号
        TelephonyManager tele = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        imei = tele.getDeviceId();
        PreferencesUtils.putString(context,"app_imei", imei);
        LOGUtils.LOG("LibApplicationData 获取IMEI号   " + imei);
    }


    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    private static int getMemoryCacheSize(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
            // limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }
        return memoryCacheSize;
    }

    //实例化一次  
    public synchronized static LibApplicationData getInstance() {
        if (null == ApplicationDatainstance) {
            ApplicationDatainstance = new LibApplicationData();
        }
        return ApplicationDatainstance;
    }

    // add Activity    
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //关闭每一个list内的activity  
    public void exit() {
        if (mList.size() == 0) {
            return;
        }
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.exit(0);   
        }
    }

    public void finishApp(){
        exit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //杀进程  
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
