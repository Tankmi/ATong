package com.jemer.atong.view.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import huitx.libztframework.utils.LayoutUtil;


/**
 * 水平刻度尺
 *
 * @author ZhuTao
 * @date 2017/3/17
 * @params
 */

public class RadioHorizonalRuler extends View {

    public interface OnValueChangeListener {
        public void onValueChange(int value);
    }

    private Scroller mScroller;  //滑动
    private VelocityTracker mVelocityTracker;    //滑动速率的控制

    private OnValueChangeListener mListener;

    @SuppressWarnings("deprecation")
    public RadioHorizonalRuler(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Log.i("RadioRuler", "RadioRuler 构造方法 获得允许fling动作的最小速度值" + mMinVelocity);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();  //获得允许fling动作的最小速度值 150
//          setBackgroundResource(R.mipmap.rules_bg);  //设置显示的背景
//        setBackgroundDrawable(createBackground());  //使用下面的画的方法！自定义绘制背景色

    }

    /**
     * 初始值，最大值，最小值，间隔
     */
    public void initViewParam(int defaultValue, int maxValue, int minValue, int model)
    {
        Log.i("RadioRuler", "initViewParam");
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                break;
            case MOD_TYPE_ONE:
                mModType = MOD_TYPE_ONE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                mMinValue = minValue;
                break;

            default:
                break;
        }
        invalidate();  //执行computeScroll方法,实现滚动效果

        mLastX = 0;
        mMove = 0;
        notifyValueChange();
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener)
    {
        Log.i("RadioRuler", "setValueChangeListener");
        mListener = listener;
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public int getValue()
    {
        Log.i("RadioRuler", "getValue");
        return mValue;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        mWidth = getWidth();
        mHeight = getHeight();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//         canvas.drawColor(0xffffccbb);
        drawScaleLine(canvas);  //绘制刻度线
        drawMiddleLine(canvas);  //绘制红色指示线，以及阴影效果
    }


    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth)
    {
        Log.i("RadioRuler", "countLeftStart 计算没有数字显示位置的辅助方法");
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return xp;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();  //获得VelocityTracker的实例
        }
        mVelocityTracker.addMovement(event);  //将event事件，加入到VelocityTracker实例中

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("RadioRuler", "onTouchEvent ACTION_DOWN");
                mScroller.forceFinished(true);

                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("RadioRuler", "onTouchEvent ACTION_MOVE");
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("RadioRuler", "onTouchEvent ACTION_UP");
            case MotionEvent.ACTION_CANCEL:  //后续触屏事件取消，
                Log.i("RadioRuler", "onTouchEvent ACTION_CANCEL");
                countMoveEnd();
                countVelocityTracker(event);  //手势抬起后，判断时候需要再滚动一段距离
                return false;
            // break;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event)
    {

        mVelocityTracker.computeCurrentVelocity(1000);  //计算一秒（1000毫秒）的速度
        float xVelocity = mVelocityTracker.getXVelocity();  //获取当前的横向的滚动速率
        Log.i("RadioRuler", "countVelocityTracker 滑动速率计算，判断是否滚动 横向滚动速率xVelocity" + xVelocity);
        if (Math.abs(xVelocity) > mMinVelocity) {  //通过横行速率的绝对值与mMinVelocity对比，大于的话就让Scroll进行滚动
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private void changeMoveAndValue()
    {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
        LOG("tValue: " + tValue);
        if (Math.abs(tValue) > 0) {  //计算绝对值
            mValue += tValue;
            mMove -= tValue * mLineDivider * mDensity;
            if (mValue <= mMinValue || mValue > mMaxValue) {
                mValue = mValue <= mMinValue ? mMinValue : mMaxValue;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
        Log.i("RadioRuler", "changeMoveAndValue mValue:   " + mValue);
        postInvalidate();
    }

    private void countMoveEnd()
    {

        int roundMove = Math.round(mMove / (mLineDivider * mDensity)); //差值不大于1的时候，四舍五入
        Log.i("RadioRuler", "countMoveEnd roundMove:" + roundMove);
        Log.i("RadioRuler", "countMoveEnd mValue:" + mValue);
        mValue = mValue + roundMove;
        mValue = mValue <= mMinValue ? mMinValue : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange()
    {
        Log.i("RadioRuler", "notifyValueChange");
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE) {
                mListener.onValueChange(mValue);
            }
            if (mModType == MOD_TYPE_HALF) {
                mListener.onValueChange(mValue / 2);
            }
        }
    }

    /**
     * 刷新界面，实现滚动效果
     * 界面有变化的时候就会执行此方法
     */
    @Override
    public void computeScroll()
    {
        Log.i("RadioRuler", "computeScroll");
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            Log.i("RadioRuler", "mScroller.computeScrollOffset()");
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over  滚动方向X的偏移，滚动结束时的位置（仅对fling手势有效）

                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }

    /**
     * 从中间往两边开始画刻度线
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas)
    {

        canvas.save();

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.parseColor("#dcdcdc"));

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#999999"));
        textPaint.setTextSize(TEXT_SIZE * mDensity);

        int width = mWidth, drawCount = 0;
        float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint);

        for (int i = 0; drawCount <= (width - 30 * mDensity); i++) {
//        	 Log.i("DrawLine", "绘制前i" + i + "          drawCount:" + drawCount + "width:" + width + "mMove:" + mMove);
            int numSize = String.valueOf(mValue + i).length();  //获取文本的字数

            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
//            Log.i("DrawLine", "绘制前i" + i + "          xPosition:" + xPosition + "   getPaddingRight" + getPaddingRight());
            if (xPosition + getPaddingRight() < mWidth) {
                if ((mValue + i) % mModType == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MAX_HEIGHT, linePaint);

                    if (mValue + i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue + i) / 2), countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                if ((mMaxValue - (mValue + i) < 3) || ((mValue + i) - mMinValue < 3))
                                    break;//值与首尾值离的太近时，避免重叠，不绘制
                                    canvas.drawText(String.valueOf(mValue + i), xPosition - (textWidth * numSize / 2), getHeight() - textWidth / 4, textPaint);

                                break;

                            default:
                                break;
                        }
                    }
                } else if ((mValue + i) % mModType == 5) {  //绘制中间半长线
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MEDIUM_HEIGHT, linePaint);
                } else {
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MIN_HEIGHT, linePaint);
                }
                if ((mValue + i) == mMaxValue) {  //绘制最大值,最大值有可能不是刻度间隔的整数倍
                    canvas.drawText(String.valueOf(mValue + i), xPosition - (textWidth * numSize / 2), getHeight() - textWidth / 4, textPaint);
                }
            }

            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
//            Log.i("DrawLine", "绘制前i" + i + "          xPosition:" + xPosition + "   getPaddingLeft" + getPaddingLeft());
            if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % mModType == 0) {  //绘制长线，长线下绘制刻度数
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MAX_HEIGHT, linePaint);
                    if (mValue - i >= mMinValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue - i) / 2), countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                if ((mMaxValue - (mValue - i) < 3) || ((mValue - i) - mMinValue < 3))
                                    break;//值与首尾值离的太近时，避免重叠，不绘制
                                canvas.drawText(String.valueOf(mValue - i), xPosition - (textWidth * numSize / 2), getHeight() - textWidth / 4, textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else if ((mValue - i) % mModType == 5) {  //绘制中间半长线
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MEDIUM_HEIGHT, linePaint);
                } else {  //绘制短线
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MIN_HEIGHT, linePaint);
                }
                if ((mValue - i) == mMinValue) {  //绘制最小值,最小值有可能不是刻度间隔的整数倍
                    canvas.drawText(String.valueOf(mValue - i), xPosition - (textWidth * numSize / 2), getHeight() - textWidth / 4, textPaint);
                }

            }

            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 画中间的指示线、阴影等。
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas)
    {
        Log.i("RadioRuler", "drawMiddleLine 绘制红线");

        // TOOD 常量太多，暂时放这，最终会放在类的开始，放远了怕很快忘记
        int gap = 12, indexWidth = LayoutUtil.getInstance().getWidgetWidth(6), indexTitleWidth = 24, indexTitleHight = 10, shadow = 6;
        int color = 0xff22d7bb;

        canvas.save();

        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(color);
//         canvas.drawLine(mWidth / 2,  6 * mDensity, mWidth / 2, ITEM_MAX_HEIGHT, redPaint);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, ITEM_SELECT_HEIGHT, redPaint);

//        Paint ovalPaint = new Paint();
//        ovalPaint.setColor(Color.RED);
//        ovalPaint.setStrokeWidth(indexTitleWidth);
//        canvas.drawLine(mWidth / 2, 0, mWidth / 2, indexTitleHight, ovalPaint);
//        canvas.drawLine(mWidth / 2, mHeight - indexTitleHight, mWidth / 2, mHeight, ovalPaint);

//绘制投影  
//        Paint shadowPaint = new Paint();
//        shadowPaint.setStrokeWidth(shadow);
//        shadowPaint.setColor(Color.parseColor(color));
//        canvas.drawLine(mWidth / 2 + gap, 0, mWidth / 2 + gap, mHeight, shadowPaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//         setMeasuredDimension(LayoutUtil.getInstance().getWidgetWidth(644,false),LayoutUtil.getInstance().getWidgetHeight(110));

    }


    /**
     * 屏幕密度
     */
    private float mDensity;
    private int mLastX, mMove;  //猜测，结束时的X坐标，滑动距离
    private int mWidth, mHeight;    //视图的宽高
    private static final int TEXT_SIZE = 12;  //字体大小 15

    /**
     * 允许fling动作的最小速度值
     */
    private int mMinVelocity;

    //两个默认的间隔数
    /**
     * 2个间隔 ，没怎么用过，也没做具体适配
     */
    private static final int MOD_TYPE_HALF = 2;
    /**
     * 10个间隔
     */
    private static final int MOD_TYPE_ONE = 10;

    private static final int ITEM_HALF_DIVIDER = 40;  //设置每个间隔的宽度（像素值）  40
    private static final int ITEM_ONE_DIVIDER = 10;  //设置每个间隔的宽度（像素值）  5

    private int mValue = 50, mMaxValue = 100, mMinValue = 0,
            mModType = MOD_TYPE_HALF, mLineDivider = ITEM_HALF_DIVIDER;  //给这两位加了默认值 2 40

    /**
     * 选中刻度线的长度（中间的标识线）
     */
    private static final int ITEM_SELECT_HEIGHT = LayoutUtil.getInstance().getWidgetHeight(65);
    /**
     * 长刻度线的长度（中间的标识线）
     */
    private static final int ITEM_MAX_HEIGHT = LayoutUtil.getInstance().getWidgetHeight(35);
    /**
     * 中等长度刻度线的长度
     */
    private static final int ITEM_MEDIUM_HEIGHT = LayoutUtil.getInstance().getWidgetHeight(20);   //35
    /**
     * 短刻度线的长度
     */
    private static final int ITEM_MIN_HEIGHT = LayoutUtil.getInstance().getWidgetHeight(13);


    /**
     * 打印日志
     *
     * @param data 需要打印的内容
     */
    public void LOG(String data)
    {
        Log.i("spoort_list", "RadioRuler:" + data + "");
    }
}