package com.jemer.atong.fragment.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jemer.atong.context.PreferenceEntity;

import androidx.annotation.Nullable;

/**
 * NestedScroll
 */
public class NestedScrollParentView extends LinearLayout {
    private View mHeader;
    private int mHeaderHeight;

    public NestedScrollParentView(Context context) {
        super(context);
    }

    public NestedScrollParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollParentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 是否接受嵌套滚动,只有它返回true,后面的其他方法才会被调用
     * @param child  包裹 target 的父布局的直接子View(该直接子View不一定是发生滑动嵌套的view）
     * @param target  触发嵌套滑动的 view
     * @param nestedScrollAxes  表示滚动的方向：
     *                                    ViewCompat.SCROLL_AXIS_VERTICAL(垂直方向滚动)
     *                                    ViewCompat.SCROLL_AXIS_HORIZONTAL(水平方向滚动)
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    /**
     *  返回true时，该方法被回调, 父控件针可以在该方法中对嵌套滑动做一些前期工作。覆写该方法时，记得要调用父类实现：super.onNestedScrollAccepted，如果存在父类的话
     * @param child
     * @param target
     * @param axes
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    /**
     * 在内层view处理滚动事件前先被调用,可以让外层view先消耗部分滚动
     * @param target  	触发嵌套滑动的 view
     * @param dx  表示 view 本次 x 方向的滚动的总距离，单位：像素
     * @param dy  表示 view 本次 y 方向的滚动的总距离，单位：像素
     * @param consumed  输出：表示父布局消费的水平和垂直距离。
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);

        boolean headerScrollUp = dy > 0 && getScrollY() < mHeaderHeight;
        boolean headerScrollDown = dy < 0 && getScrollY() > 0 && !target.canScrollVertically(-1);
        if (headerScrollUp || headerScrollDown) {
            scrollBy(0, dy);
            consumed[1] = dy;

        }
    }

    /**
     * 接收子View处理完滑动后的滑动距离信息, 在这里父控件可以选择是否处理剩余的滑动距离。
     * 如果想要该方法得到回调，先前的onStartNestedScroll(View, View, int, int)必须返回true。
     * @param target 触发嵌套滑动的 view
     * @param dxConsumed 表示 view 消费了 x 方向的距离
     * @param dyConsumed 	表示 view 消费了 y 方向的距离
     * @param dxUnconsumed 表示 view 剩余未消费 x 方向距离
     * @param dyUnconsumed 表示 view 剩余未消费 y 方向距离
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

    }



    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            mHeader = getChildAt(0);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderHeight = mHeader.getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeader.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec) + mHeader.getMeasuredHeight();
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(totalHeight, mode));
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        } else if (y > mHeaderHeight) {
            y = mHeaderHeight;
        }

        super.scrollTo(x, y);
    }

}
