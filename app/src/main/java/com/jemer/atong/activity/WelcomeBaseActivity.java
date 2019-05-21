package com.jemer.atong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

import com.jemer.atong.R;
import com.jemer.atong.activity.user.SelLoginActivity;
import com.jemer.atong.activity.user.perfect_info.PerfectInfoActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.web.activity.WebViewActivity;

import java.lang.ref.WeakReference;

import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StatusBarCompat;

import static huitx.libztframework.utils.LOGUtils.LOG;


public class WelcomeBaseActivity extends Activity {
    protected boolean setscr = false;
    /**
     * 网络请求 回调
     */
//    protected GetNetData mgetNetData;
    protected UserEntity mUserEntity;
    protected Context mContext;    //上下文
    protected int intent_state = -1;

    protected MyHandler mHandler;

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Activity> mActivity;

        protected MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent_home = null;
            switch (msg.what) {
                case 0:    //没有登录成功
                    intent_home = new Intent(mContext, SelLoginActivity.class);
                    break;
                case 1:    //登录成功
                    String isall = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ISALL, "");
                    if (isall.equals("0")) {
                        intent_home = new Intent(mContext, PerfectInfoActivity.class);
                    } else {
//                        if (intent_state == -1)
                        intent_home = new Intent(mContext, HomeActivity.class);
//                        else if (intent_state == 100)
//                            intent_home = new Intent(mContext, EarActivity.class);    //耳穴检测
                    }
                    break;
                default:
                    break;
            }
//            intent_home = new Intent(mContext, SelLoginActivity.class);
//            intent_home = new Intent(mContext, HomeActivity.class);
//            intent_home = new Intent(mContext, PerfectInfoActivity.class);

            startActivity(intent_home);
            toFinish();
        }
    }

    private synchronized void finalTest() {  //判断所有任务是否执行完成

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
//		super.onWindowFocusChanged(hasFocus);

        if (setscr) return;
        if (hasFocus) {
            boolean isSave = PreferencesUtils.getBoolean(this, PreferenceEntity.KEY_SCREEN_ISSAVE, false);
            if (!isSave) {
//            if (isSave) {
                LOGUtils.LOG("初始化屏幕信息");
                //获取屏幕尺寸，不包括虚拟功能高度<br><br>
                int widowHeight = getWindowManager().getDefaultDisplay().getHeight();
//                Rect rect = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                int top = rect.top; // 状态栏的高度
//                LOGUtils.LOG("状态栏的高度" + top);

                Resources resources = WelcomeBaseActivity.this.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                View view = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int top2 = view.getTop(); // 状态栏与标题栏的高度
                int width = view.getWidth(); // 视图的宽度
                int height = view.getHeight(); // 视图的高度
                int navigationBarHeight = resources.getDimensionPixelSize(resourceId);    //虚拟按键的高度

//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int mewidth = metric.widthPixels;  // 屏幕宽度（像素）
//        int meheight = metric.heightPixels;  // 屏幕高度（像素）
//        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
//        LOGUtils.LOG("屏幕密度: " + density + "; 屏幕密度DPI: " + densityDpi);

                float status_bar_height = 0;
                int resourceId_status = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId_status > 0) {
                    status_bar_height = this.getResources().getDimensionPixelSize(resourceId_status);
                    LOGUtils.LOG("状态栏高度: " + status_bar_height);
                }

                PreferenceEntity.ScreenTop = status_bar_height;
                PreferenceEntity.ScreenTitle_navigationBarHeight = navigationBarHeight;
                PreferenceEntity.screenWidth = width;
                PreferenceEntity.screenHeight = height + top2;


                boolean hasNavigationBar = false;
                if ((height + top2) - widowHeight > 0) hasNavigationBar = true;
                else hasNavigationBar = false;
                LOG("  虚拟键盘 hasNavigationBar =  " + (!hasNavigationBar ? "没显示" : "显示了"));
                PreferenceEntity.hasNavigationBar = hasNavigationBar;

                LOGUtils.LOG("状态栏的高度：" + status_bar_height + ",标题栏与状态栏的高度:" + top2 + ",标题栏与状态栏的高度占比:"
                        + PreferencesUtils.getFloat(this, "ScreenTitle")
                        + ",视图的宽度:" + width + ",视图的高度(不包括状态栏的高度):" + height + "屏幕高度（不包括虚拟功能高度）： " + widowHeight + ",屏幕的宽度:"
                        + PreferenceEntity.screenWidth + ",屏幕的高度:"
                        + PreferenceEntity.screenHeight + "虚拟键盘的高度：" + navigationBarHeight + "手机厂商：" + Build.MANUFACTURER);
                PreferenceEntity.saveScreenView();
                setscr = true;
            } else {
                LOG("屏幕信息已保存，初始化");
                PreferenceEntity.initScreenView();
                setscr = true;
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 设置状态栏颜色，默认透明
     *
     * @param isFit: 是否是全屏显示	5.0以上的系统需要此属性判断状态栏的属性设置
     * @param isSet: 是否设置特殊颜色，否的话，第三个参数可以设置为0
     */
    public void setStatusBarColor(boolean isFit, boolean isSet, int color) {
        if (isSet) StatusBarCompat.compat(this, color, isFit);
        else
            StatusBarCompat.compat(this, mContext.getResources().getColor(R.color.status_bar_default_bg), isFit);
    }

    protected void toFinish() {
        mUserEntity = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        setContentView(R.layout.view_null);
        finish();
        System.gc();
    }

}
