package huitx.libztframework.utils;

import android.os.Build;
//import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout.LayoutParams;

import huitx.libztframework.context.LibApplicationData;
import huitx.libztframework.context.LibPreferenceEntity;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * @author ZhuTao
 * @version V1.0
 * @Title: LayoutUtil.java
 * @Package com.lingjiedian.modou.util
 * @Description: 用于进行主页（没有滑动效果）以及其他页面的控件适配 ，传入的参数包括控件的宽高，
 * 距离父控件上下左右的距离（这几个参数不一定都有，根据使用的方法来定）。大部分视图的父类都是view，所以drawviewLayout基本上都适用；
 * 只有单选框一类的需要单独调用对应的方法
 * @date 2015年6月11日 上午9:34:28
 */
public class LayoutUtil {

    /** 切图宽高比例值  */
    private static float mUIratio;
    /** 设备宽高比 */
    private static float mScreenRatio;
    private static float ScreenWidth;    //屏幕的宽
    private static float ScreenHeight;    //屏幕的高
    /** 虚拟键盘的高 */
    private static float navigationBarHeight;    //虚拟键盘的高
    /** false 显示了，true 没显示  */
    private static boolean hasNavigationBar = false;
    /** 切图宽  */
    private float normalW;
    /**  切图高 */
    private float normalH;
    /** 状态栏的高度 */
    private float StatusBarHeight;
    private static LayoutUtil mLayoutUtilInstance;

    public static LayoutUtil getInstance() {    //双重检验锁，实现线程安全
        if(mLayoutUtilInstance == null){
            synchronized (LayoutUtil.class){
                if(mLayoutUtilInstance == null){
                    mLayoutUtilInstance = new LayoutUtil();
                }
            }
        }
        setIsFullScreen(false);
        return mLayoutUtilInstance;
    }

    private Object readResolve(){
        return getInstance();
    }

    public LayoutUtil() {
        normalW = 750;
        normalH = 1344;
        StatusBarHeight = LibPreferenceEntity.ScreenTop;    //虚拟键盘的高
        navigationBarHeight = LibPreferenceEntity.ScreenTitle_navigationBarHeight;    //虚拟键盘的高
        ScreenWidth = LibPreferenceEntity.screenWidth;    //屏幕的宽
        ScreenHeight = LibPreferenceEntity.screenHeight;    //屏幕的高
//		Log.i("spoort_list","ScreenWidth:" + ScreenWidth + ";ScreenHeight:" + ScreenHeight + ";navigationBarHeight:" +navigationBarHeight);
        //判断是否显示虚拟返回键： true没有显示，false显示了
        if (!LibPreferenceEntity.hasNavigationBar || Build.MANUFACTURER.equals("Meizu")) {
            hasNavigationBar = true;
        } else {
            if (ViewConfiguration.get(LibApplicationData.context).hasPermanentMenuKey()) {
                hasNavigationBar = true;
            } else {
                hasNavigationBar = false;
            }
        }
        mUIratio = normalW / normalH;//UI图的宽高比
        mScreenRatio = ScreenWidth / ScreenHeight;
    }

    /**
     * 设置是否全屏,默认false。建议初始化界面的时候调用一次
     * 全屏意味大于切图，缩放宽;小于切图，缩放高;
     * 默认保证宽填满全屏，对高进行换算,即支持上下滑动预览
     */
    public static void setIsFullScreen(boolean fullScreen) {
        if (fullScreen) {
            ScreenHeight = LibPreferenceEntity.screenHeight;
            ScreenWidth = LibPreferenceEntity.screenWidth;
            if (mUIratio > 1) {
                if (mScreenRatio > mUIratio) {    //大于切图，缩放高
                    ScreenHeight = ScreenWidth / mUIratio;

                } else if (mScreenRatio < mUIratio) {    //小于切图，缩放宽，没遇到过
                    if (!hasNavigationBar) {    //计算出实际绘制时的屏幕高度
                        ScreenHeight = ScreenHeight - navigationBarHeight;
                    }
                    ScreenWidth = ScreenHeight * mUIratio;
                }
            } else {
                if (mScreenRatio > mUIratio) {    //大于切图，缩放宽
                    if (!hasNavigationBar) {    //计算出实际绘制时的屏幕高度
                        ScreenHeight = ScreenHeight - navigationBarHeight;
                    }
                    ScreenWidth = ScreenHeight * mUIratio;
                } else if (mScreenRatio < mUIratio) {    //小于切图，缩放高，还没遇到过
                    ScreenHeight = ScreenWidth / mUIratio;
                }
            }
        } else {
            ScreenWidth = LibPreferenceEntity.screenWidth;
            ScreenHeight = LibPreferenceEntity.screenHeight;
            if (mScreenRatio != mUIratio) {    //默认保证宽填满全屏，对高进行换算
                ScreenHeight = ScreenWidth / mUIratio;
            }
        }
    }

    public float getScreenHeight() {
        return ScreenHeight;
    }

    public float getScreenWidth() {
        return ScreenWidth;
    }

    /** 获取控件实际的高度 */
    public int getWidgetHeight(float height) {
        return (int) (ScreenHeight * (height / normalH));
    }

    /** 获取控件实际的宽度 */
    public int getWidgetWidth(float width) {
        return getWidgetWidth(width, false);
    }

    /** 获取控件实际的宽度 */
    public int getWidgetWidth(float width, boolean isFullScreen) {
        int ScreenWidth;
        if (isFullScreen && mScreenRatio > mUIratio) {
            ScreenWidth = (int) (ScreenHeight * mUIratio);
        } else {
            ScreenWidth = LibPreferenceEntity.screenWidth;
        }
        return (int) (ScreenWidth * (width / normalW));
    }

    /** 获取切图的宽高比  */
    public float getUIRatio() {
        return mUIratio;
    }

    /**  获取屏幕的宽高比   */
    public float getScreenRatio() {
        return mScreenRatio;
    }

    public void drawRadiobutton(RadioButton view, float width, float height, float marginleft, float marginTop) {
        view.setLayoutParams(getLayoutParams((LinearLayout.LayoutParams) view.getLayoutParams(), width, height, marginleft, -1, marginTop, -1));
    }

    public void drawViewLayouts(View view, float width, float height, float marginleft, float marginTop) {
        view.setLayoutParams(getLayoutParams((LayoutParams) view.getLayoutParams(), width, height, marginleft, -1, marginTop, -1));
    }

//    public void drawCheckedTextView(CheckedTextView view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
//        GridLayoutManager.LayoutParams params = getLayoutParam((GridLayoutManager.LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom);
//        view.setLayoutParams(params);
//    }

    public void drawViewlLayouts(View view, float width, float height, float marginright, float marginTop) {
        view.setLayoutParams(getLayoutParams((LayoutParams) view.getLayoutParams(), width, height, -1, marginright, marginTop, -1));
    }

    public void drawViewRBLayouts(View view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        view.setLayoutParams(getLayoutParams((LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }
    public void drawViewRBLinearLayouts(View view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        view.setLayoutParams(getLayoutParams((LinearLayout.LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }

    public void drawViewRBLayout(View view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        view.setLayoutParams(getLayoutParam((LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }

    /**
     * 顶部距离是否去除状态栏的高度（全屏的页面状态栏的高度需要去除）
     */
    public void drawViewRBLayout(View view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom, boolean isAddStatusBar) {    view.setLayoutParams(getLayoutParam((LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
        // 设置距离顶部的距离，涉及到全屏需要抛出状态栏的高度
        if(isAddStatusBar) view.setLayoutParams(setMTopAddStatusBar((LayoutParams) view.getLayoutParams(), marginTop));
    }

    /** 用于设置不需要根据屏幕宽高比进行缩放的图标，按钮等的尺寸与相对位置 */
    public void drawViewDefaultLayout(View view, int width, int height, int marginleft, int marginright, int marginTop, int marginBottom) {
        view.setLayoutParams(getDefaultLayoutParam((LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }

    /** 用于设置不需要根据屏幕宽高比进行缩放的图标，按钮等的尺寸与相对位置 */
    public void drawViewDefaultLinearLayout(View view, int width, int height, int marginleft, int marginright, int marginTop, int marginBottom) {
        view.setLayoutParams(getDefaultLayoutParam((LinearLayout.LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }
    public void drawViewDefaultLinearLayout(View view, int width) {
        view.setLayoutParams(getDefaultLayoutParam((LinearLayout.LayoutParams) view.getLayoutParams(), width, -1, -1, -1, -1, -1));
    }
    public void drawViewDefaultLinearLayout(View view, int width, int height ) {
        view.setLayoutParams(getDefaultLayoutParam((LinearLayout.LayoutParams) view.getLayoutParams(), width, height, -1, -1, -1, -1));
    }

    public void drawViewRBLinearLayout(View view, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        view.setLayoutParams(getLayoutParam((LinearLayout.LayoutParams) view.getLayoutParams(), width, height, marginleft, marginright, marginTop, marginBottom));
    }

    private LayoutParams getLayoutParams(LayoutParams params, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        if (width != -1 && width != 0)  params.width = (int) (ScreenWidth * width);
        if (height != -1 && height != 0)  params.height = (int) (ScreenHeight * height);
        if (marginleft != -1 && marginleft != 0)  params.leftMargin = (int) (ScreenWidth * marginleft);
        if (marginright != -1 && marginright != 0) params.rightMargin = (int) (ScreenWidth * marginright);

        if (marginTop != -1) {
            //判断是否显示虚拟返回键： true没有显示，false显示了
            if (hasNavigationBar) {
                params.topMargin = (int) ((ScreenHeight) * (marginTop));
            } else {
                params.topMargin = (int) ((ScreenHeight - navigationBarHeight) * (marginTop));
            }
        }
        if (marginBottom != -1)  params.bottomMargin = (int) (ScreenHeight * marginBottom);
        return params;
    }

    private LinearLayout.LayoutParams getLayoutParams(LinearLayout.LayoutParams params, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        if (width != -1 && width != 0)  params.width = (int) (ScreenWidth * width);
        if (height != -1 && height != 0)  params.height = (int) (ScreenHeight * height);
        if (marginleft != -1 && marginleft != 0)  params.leftMargin = (int) (ScreenWidth * marginleft);
        if (marginright != -1 && marginright != 0) params.rightMargin = (int) (ScreenWidth * marginright);

        if (marginTop != -1) {
            //判断是否显示虚拟返回键： true没有显示，false显示了
            if (hasNavigationBar) {
                params.topMargin = (int) ((ScreenHeight) * (marginTop));
            } else {
                params.topMargin = (int) ((ScreenHeight - navigationBarHeight) * (marginTop));
            }
        }
        if (marginBottom != -1)  params.bottomMargin = (int) (ScreenHeight * marginBottom);
        return params;
    }

    private LinearLayout.LayoutParams getLayoutParam(LinearLayout.LayoutParams params, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {

        if (width != 0.0f && width != -1) params.width = (int) (ScreenWidth * (width / normalW));

        if (height != 0.0f && height != -1) params.height = (int) (ScreenHeight * (height / normalH));

        if (marginleft != -1) params.leftMargin = (int) (ScreenWidth * (marginleft / normalW));
        if (marginright != -1) params.rightMargin = (int) (ScreenWidth * (marginright / normalW));

        if (marginTop != -1) {
            //判断是否显示虚拟返回键： true没有显示，false显示了
            if (hasNavigationBar) params.topMargin = (int) ((ScreenHeight) * (marginTop / normalH));
            else
                params.topMargin = (int) ((ScreenHeight - navigationBarHeight) * (marginTop / normalH));
        }
        if (marginBottom != -1)
            params.bottomMargin = (int) (ScreenHeight * (marginBottom / normalH));

        return params;
    }

//    private GridLayoutManager.LayoutParams getLayoutParam(GridLayoutManager.LayoutParams params, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
//        if (width != -1 && width != 0) params.width = (int) (ScreenWidth * (width / normalW));
//
//        if (height != -1 && height != 0) params.height = (int) (ScreenHeight * (height / normalH));
//
//        if (marginleft != -1) params.leftMargin = (int) (ScreenWidth * (marginleft / normalW));
//        if (marginright != -1) params.rightMargin = (int) (ScreenWidth * (marginright / normalW));
//
//        if (marginTop != -1) {
//            //判断是否显示虚拟返回键： true没有显示，false显示了
//            if (hasNavigationBar) params.topMargin = (int) ((ScreenHeight) * (marginTop / normalH));
//            else
//                params.topMargin = (int) ((ScreenHeight - navigationBarHeight) * (marginTop / normalH));
//        }
//        if (marginBottom != -1)
//            params.bottomMargin = (int) (ScreenHeight * (marginBottom / normalH));
//
//        return params;
//    }

    private LayoutParams getLayoutParam(LayoutParams params, float width, float height, float marginleft, float marginright, float marginTop, float marginBottom) {
        if (width != -1 && width != 0) params.width = (int) (ScreenWidth * (width / normalW));

        if (height != -1 && height != 0) params.height = (int) (ScreenHeight * (height / normalH));

        if (marginleft != -1 && marginleft != 0)
            params.leftMargin = (int) (ScreenWidth * (marginleft / normalW));
        if (marginright != -1 && marginright != 0)
            params.rightMargin = (int) (ScreenWidth * (marginright / normalW));
        if (marginTop != -1) {
            //判断是否显示虚拟返回键： true没有显示，false显示了
            if (hasNavigationBar) params.topMargin = (int) ((ScreenHeight) * (marginTop / normalH));
            else
                params.topMargin = (int) ((ScreenHeight - navigationBarHeight) * (marginTop / normalH));
        }
        if (marginBottom != -1)
            params.bottomMargin = (int) (ScreenHeight * (marginBottom / normalH));

        return params;
    }

    /** 设置控件顶部距离屏幕顶端的高度（加上状态栏的高度） */
    private LayoutParams setMTopAddStatusBar(LayoutParams params, float marginTop) {
        //判断是否显示虚拟返回键： true没有显示，false显示了
        if (hasNavigationBar)
            params.topMargin = (int) (StatusBarHeight + (ScreenHeight) * (marginTop / normalH));
        else
            params.topMargin = (int) (StatusBarHeight + (ScreenHeight - navigationBarHeight) * (marginTop / normalH));

        return params;
    }

    private LayoutParams getDefaultLayoutParam(LayoutParams params, int width, int height, int marginleft, int marginright, int marginTop, int marginBottom) {
        if (width != -1) params.width = width;
        if (height != -1) params.height = height;
        if (marginleft != -1) params.leftMargin = marginleft;
        if (marginright != -1) params.rightMargin = marginright;
        if (marginTop != -1) params.topMargin = marginTop;
        if (marginBottom != -1) params.bottomMargin = marginBottom;
        return params;
    }

    private LinearLayout.LayoutParams getDefaultLayoutParam(LinearLayout.LayoutParams params, int width, int height, int marginleft, int marginright, int marginTop, int marginBottom) {
        if (width != -1) params.width = width;
        if (height != -1) params.height = height;
        if (marginleft != -1) params.leftMargin = marginleft;
        if (marginright != -1) params.rightMargin = marginright;
        if (marginTop != -1) params.topMargin = marginTop;
        if (marginBottom != -1) params.bottomMargin = marginBottom;
        return params;
    }

}
