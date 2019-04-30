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
 * 垂直刻度尺
 *
 * @author ZhuTao
 * @date 2017/3/17
 * @params
 */

public class VerticalRuler extends View {


    private Scroller mScroller;  //滑动
    /** 滑动速率计算工具 */
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;
    public interface OnValueChangeListener { public void onValueChange(int value); }

    public VerticalRuler(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density; //获取屏幕密度
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();  //获得允许fling动作的最小速度值 150

//            setBackgroundResource(R.mipmap.rules_bg);  //设置显示的背景
//        setBackgroundDrawable(createBackground());  //使用下面的画的方法！自定义绘制背景色

    }

    /**
     * 设置用于接收结果的监听器  */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    /** 获取当前刻度值 */
    public int getValue() {
        return mValue;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        Log.i("spoort_list", "left" + left + "top" + top + "right" + right + "bottom" + bottom + "mWidth" + mWidth + "mHeight" + mHeight);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(0xffffccbb);
        drawScaleLine(canvas);  //绘制刻度线
        drawMiddleLine(canvas);  //绘制红色指示线，以及阴影效果

//        canvas.rotate(90);
    }


    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();  //获得VelocityTracker的实例
        }
        mVelocityTracker.addMovement(event);  //将event事件，加入到VelocityTracker实例中

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);  //结束滚动
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:  //后续触屏事件取消，
                countMoveEnd();
                countVelocityTracker(event);  //手势抬起后，需要再滚动一段距离
                return false;
            // break;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    /** 继续滑动一段距离 */
    private void countVelocityTracker(MotionEvent event) {

        mVelocityTracker.computeCurrentVelocity(1000);  //计算一秒（1000毫秒）的速度
        float xVelocity = mVelocityTracker.getXVelocity();  //获取当前的横向的滚动速率
        if (Math.abs(xVelocity) > mMinVelocity) {  //通过横行速率的绝对值与mMinVelocity对比，大于的话就让Scroll进行滚动
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    /** 滑动时，计算出滑动距离和刻度值，并保证在绘制范围内 */
    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
        if (Math.abs(tValue) > 0) {  //计算绝对值
            mValue += tValue;
            mMove -= tValue * mLineDivider * mDensity;
            if (mValue <= mMinValue || mValue > mMaxValue) {    //临界判断
                mValue = mValue <= mMinValue ? mMinValue : mMaxValue;
                mMove = 0;
                mScroller.forceFinished(true);  //强制停止本次滑动
            }
            notifyValueChange();
        }
        postInvalidate();
    }

    /** 滑动结束后，计算出滑动的刻度 */
    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity)); //计算出滑动的刻度，差值不大于1的时候，四舍五入
        mValue = mValue + roundMove;
        mValue = mValue <= mMinValue ? mMinValue : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    /** 回调刻度值 */
    private void notifyValueChange() {
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE) {
//                mListener.onValueChange(mMaxValue - mValue);
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
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
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
    private void drawScaleLine(Canvas canvas) {

        canvas.save();

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.parseColor("#bebebe"));

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textPaint.setColor(Color.parseColor("#bebebe"));

        int width = mWidth, drawCount = 0;
        float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint),unitWidth = Layout.getDesiredWidth("cm", textPaint);


        for (int i = 0; drawCount <= (width - 30*mDensity); i++) {
            int numSize = (String.valueOf(mValue + i)).length();  //获取要显示的内容的长度
//            int numSize = (String.valueOf(mValue + i)).length();  //获取要显示的内容的长度
            //这个是画中间刻度线的右半部分！
            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {
                if ((mValue + i) % mModType == 0) {
                    //ITEM_MAX_HEIGHT  长线的长度！
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MAX_HEIGHT, linePaint);

                    if (mValue + i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_ONE:
                                if ((mMaxValue - (mValue + i) < 3) || ((mValue + i) - mMinValue < 3)) break;    //值与首尾值离的太近时，避免重叠，不绘制
                                //保存画布，正向旋转90度，绘制竖直刻度线  竖着的尺子！数字！
                                canvas.save();
                                canvas.rotate(90);
                                canvas.drawText(String.valueOf((mValue + i)) + "cm", (getHeight()- textWidth * numSize - unitWidth), -(xPosition - (textWidth / 2)), textPaint);
                                canvas.restore();

                                //保存画布，反向旋转90度，绘制竖直刻度线数字！
//                                canvas.save();
//                                canvas.rotate(-90);
//                                canvas.drawText(String.valueOf(mMaxValue - (mValue + i)) + "cm", -(getHeight()- textWidth/4), xPosition + (textWidth / 2), textPaint);
//                                canvas.restore();
                                break;
                            case MOD_TYPE_HALF:
                                canvas.save();
                                canvas.rotate(-90);
                                canvas.drawText(String.valueOf((mValue + i) / 2), -(getHeight() - textWidth), countLeftStart(mValue + i, xPosition, textWidth), textPaint);
                                canvas.restore();
                                break;
                            default:
                                break;
                        }
                    }
                }else if((mValue + i) % mModType == 5){  //绘制中间半长线
                    canvas.drawLine(xPosition,   0, xPosition, ITEM_MEDIUM_HEIGHT, linePaint);
                } else {
                    canvas.drawLine(xPosition,   0, xPosition, ITEM_MIN_HEIGHT, linePaint);
                }

                if((mValue + i) == mMaxValue){  //绘制最大值,最大值有可能不是刻度间隔的整数倍

                    canvas.save();
                    canvas.rotate(90);
                    canvas.drawText(String.valueOf((mValue + i)) + "cm", (getHeight()- textWidth * numSize - unitWidth), -(xPosition - (textWidth / 2)), textPaint);
                    canvas.restore();
                }
            }

            numSize = (String.valueOf(mValue - i)).length();  //获取要显示的内容的长度
            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
//            Log.i("DrawLine", "绘制前i" + i + "          xPosition:" + xPosition + "   getPaddingLeft" + getPaddingLeft());
            //这个是画中间刻度线的左半部分！
            if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % mModType == 0) {  //绘制长线，长线下绘制刻度数
                    canvas.drawLine(xPosition, 0, xPosition, ITEM_MAX_HEIGHT, linePaint);
                    if (mValue - i > mMinValue) {
                        switch (mModType) {
                            case MOD_TYPE_ONE:
                                if ((mMaxValue - (mValue - i) < 3) || ((mValue - i) - mMinValue < 3)) break;    //值与首尾值离的太近时，避免重叠，不绘制

                                //保存画布，反向旋转90度，绘制竖直刻度线  竖着的尺子！数字！
//                                canvas.save();
//                                canvas.rotate(-90);
//                                canvas.drawText(String.valueOf(mMaxValue - (mValue - i)) + "cm", -(getHeight()- textWidth/4), xPosition + (textWidth / 2), textPaint);
//                                canvas.restore();
                                //保存画布，正向旋转90度，绘制竖直刻度线  竖着的尺子！数字！
                                canvas.save();
                                canvas.rotate(90);
                                canvas.drawText(String.valueOf((mValue - i)) + "cm", (getHeight()- textWidth * numSize - unitWidth), -(xPosition - (textWidth / 2)), textPaint);
                                canvas.restore();
                                break;
                            case MOD_TYPE_HALF:
                                canvas.save();
                                canvas.rotate(-90);
                                canvas.drawText(String.valueOf(((mValue - i)) / 2), -(getHeight() - textWidth), countLeftStart(mValue - i, xPosition, textWidth), textPaint);
//                            canvas.drawText(String.valueOf((mValue - i) / 2), countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                canvas.restore();
                                break;
                            default:
                                break;
                        }
                    }
                }   else if((mValue - i) % mModType == 5){  //绘制中间半长线
                    canvas.drawLine(xPosition,   0, xPosition,  ITEM_MEDIUM_HEIGHT, linePaint);
                }
                else{  //绘制短线
                    canvas.drawLine(xPosition,   0, xPosition, ITEM_MIN_HEIGHT, linePaint);
                }

                if((mValue - i) == mMinValue){  //绘制最小值,最小值有可能不是刻度间隔的整数倍

                    canvas.save();
                    canvas.rotate(90);
                    canvas.drawText(String.valueOf((mValue - i)) + "cm", (getHeight()- textWidth * numSize - unitWidth), -(xPosition - (textWidth / 2)), textPaint);
                    canvas.restore();
                }
            }

            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 画中间的红色指示线、阴影等。指示线两端简单的用了两个矩形代替
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {

        int indexWidth = LayoutUtil.getInstance().getWidgetWidth(6);
        int color = 0xff22d7bb;

        canvas.save();

        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(color);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, ITEM_SELECT_HEIGHT, redPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        setMeasuredDimension(LayoutUtil.getInstance().getWidgetWidth(686,false),LayoutUtil.getInstance().getWidgetHeight(160));

    }


    /**
     * 被调用的方法，传进来三个值初始值，最大值，间隔
     *
     * @param defaultValue 初始值
     * @param maxValue     最大值
     * @param model        刻度盘精度：<br>
     */
    public void initViewParam(int defaultValue, int maxValue,int minValue, int model) {
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                break;
            case MOD_TYPE_ONE:
                mLineDivider = ITEM_ONE_DIVIDER;
                mMaxValue = maxValue;
                mModType = model;
                mValue = defaultValue;  //与下面这行话作对比
//                mValue = mMaxValue - defaultValue;  //1、将水平画布旋转呈垂直画布；2、绘制顺序也与原来的做了倒置；所以，实际上的值与绘制的值是相反的。即：150 = 230-80；
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

    /**  屏幕密度  */
    private float mDensity;
    private int mLastX, mMove;  //猜测，结束时的X坐标，滑动距离
    private int mWidth, mHeight;	//视图的宽高
    private static final int TEXT_SIZE = 12;  //字体大小 15

    /** 允许fling动作的最小速度值 */
    private int mMinVelocity;

    //两个默认的间隔数
    /** 2个间隔 ，没怎么用过，也没做具体适配 */
    private static final int MOD_TYPE_HALF = 2;
    /** 10个间隔 */
    private static final int MOD_TYPE_ONE = 10;

    private static final int ITEM_HALF_DIVIDER = 40;  //设置每个间隔的宽度（像素值）  40
    private static final int ITEM_ONE_DIVIDER = 10;  //设置每个间隔的宽度（像素值）  5

    private int mValue = 50, mMaxValue = 100, mMinValue = 0,
            mModType = MOD_TYPE_HALF,mLineDivider = ITEM_HALF_DIVIDER;  //给这两位加了默认值 2 40

    /** 选中刻度线的长度（中间的标识线） */
    private static final int ITEM_SELECT_HEIGHT =  LayoutUtil.getInstance().getWidgetHeight(65);
    /** 长刻度线的长度（中间的标识线） */
    private static final int ITEM_MAX_HEIGHT =  LayoutUtil.getInstance().getWidgetHeight(35);
    /** 中等长度刻度线的长度 */
    private static final int ITEM_MEDIUM_HEIGHT =  LayoutUtil.getInstance().getWidgetHeight(20);   //35
    /** 短刻度线的长度 */
    private static final int ITEM_MIN_HEIGHT =  LayoutUtil.getInstance().getWidgetHeight(13);   //35

}