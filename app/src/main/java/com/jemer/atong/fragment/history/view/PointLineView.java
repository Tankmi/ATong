package com.jemer.atong.fragment.history.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.history.PointLineTableBean;

import java.util.ArrayList;
import java.util.List;

import huitx.libztframework.utils.LayoutUtil;

/**
 * 统计， 体重历史记录
 * @author ZhuTao
 * @date 2017/8/22
 * @params
*/

public class PointLineView extends View {

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(0xffbbcc00);
        drawCoordinateLine(canvas);
        if(score == null){
            return;   //列表为空，不用继续绘制下去
        }
        drawDate(canvas);
        drawShader(canvas);
        drawRectf(canvas);
    }

    private Paint initPaint(Paint mPaint){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorLine);
        mPaint.setStrokeWidth(lineWidth);	//画笔宽度1sp
        mPaint.setStyle(Style.FILL);	//实心
        mPaint.setTextSize(textSize);	//字号10sp
        mPaint.setTextAlign(Align.CENTER);
        return mPaint;
    }

    /** 绘制渲染 */
    public void drawShader(Canvas canvas){
        paint_trend_line.setColor(colorTrendLine);
        for (int i = 0; i < score.size(); i++) {
            float data = YCoord(score.get(i).getData() + "");
            Path path = new Path();
            if (i > 0) {
                float dataLast = YCoord(score.get(i - 1).getData() + "");
                path.moveTo(marginLeft + interval_left_right * (i - 1), (getHeight() - marginBottom - lineWidth*1.0f)); //减去画笔宽度，以免覆盖X轴
                path.lineTo(marginLeft + interval_left_right * (i - 1), dataLast);
                path.lineTo(marginLeft + interval_left_right * (i), data);
                path.lineTo(marginLeft + interval_left_right * (i), (getHeight() - marginBottom - lineWidth*1.0f));
                canvas.drawPath(path, paint_trend_line);
            }
        }
    }

    /**
     * 绘制点
     */
    public void drawRectf(Canvas canvas) {

        for (int i = 0; i < score.size(); i++) {
            float data = YCoord(score.get(i).getData() + "");
            LOG("YCoord(score.get(i).score + \"\"):" + YCoord(score.get(i).getData() + ""));
            paint_circle.setColor(0xffffffff);
            paint_circle.setStyle(Style.FILL);
            canvas.drawCircle(marginLeft + interval_left_right * (i), data,circleRadius, paint_circle);
            paint_circle.setStyle(Style.STROKE);
            paint_circle.setColor(colorLine);
            canvas.drawCircle(marginLeft + interval_left_right * (i), data,circleRadius, paint_circle);


            drawDec(canvas,marginLeft + interval_left_right * (i), data,score.get(i).getData() + "");
            drawXCoordinateAxis(canvas,i,score.get(i).getTime());
//            drawXCoordinateAxis(canvas,i,score.get(i).getTime() + "\n" + score.get(i).date1);
        }
    }

    /** 绘制描述 */
    private void drawDec(Canvas canvas, float x, float y, String text){
        float drawY = y - LayoutUtil.getInstance().getWidgetHeight(32);
        canvas.drawText(text,x, drawY,paint_des);
    }

    /**
     *	绘制表中线条
     *
     * @param canvas
     */
    public void drawDate(Canvas canvas) {

        paint_circle.setColor(colorLine);

        for (int i = 0; i < score.size(); i++) {
            if(i>0){
                canvas.drawLine(marginLeft + interval_left_right * (i-1),YCoord(score.get(i-1).getData() + ""),
                        marginLeft + interval_left_right * (i), YCoord(score.get(i).getData() + ""),paint_circle);
            }
        }
    }

    /** 绘制横坐标轴上的刻度 */
    private void drawXCoordinateAxis(Canvas canvas, int i, String state){
        /*paint_circle.setColor(ApplicationData.context.getResources().getColor((R.color.text_color_normal)));
        paint_circle.setStyle(Style.FILL);
        canvas.drawText(state + "\n" + "123",marginLeft + interval_left_right * (i),getHeight() -  LayoutUtil.getInstance().getWidgetHeight(25),paint_circle);*/

        StaticLayout layout = new StaticLayout(state, textPaint, 10000,  Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.5F, true);
        canvas.save();
//        canvas.translate( marginLeft + interval_left_right * (i), getHeight() -  LayoutUtil.getInstance().getWidgetHeight(25));
        canvas.translate( marginLeft + interval_left_right * (i),(getHeight() -  marginBottom + (LayoutUtil.getInstance().getWidgetHeight(20)) ));
//        canvas.drawCircle(0,(getHeight() -  LayoutUtil.getInstance().getWidgetHeight(25)),10,paint_circle);
        layout.draw(canvas);
        canvas.restore();//别忘了restore
    }

    /**
     * 绘制坐标轴
     */
    public void drawCoordinateLine(Canvas canvas) {
        paint_coordinate_line.setColor(colorCoordinateLine);
//        DashPathEffect pathEffect = new DashPathEffect(new float[] { 6,2 }, 1);
        paint_coordinate_line.setStyle(Style.STROKE);
//        paint_coordinate_line.setPathEffect(pathEffect);

        for(int i=0; i<yCoors.size(); i++){ //只画横坐标轴
            Path path = new Path();
             path.moveTo(0, getHeight() - (marginBottom + marginHorizonal * i));
             path.lineTo(getWidth(),getHeight() - (marginBottom + marginHorizonal * i));
            canvas.drawPath(path, paint_coordinate_line);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                float x=event.getX();
                float y = event.getY();
                indexX = x;
                indexY = y;
                System.out.println("距离屏幕边缘：" + event.getRawX() + "," + event.getRawY());
                System.out.println("距离控件边缘：" + event.getX() + "," + event.getY());

//              if( indexY >  getHeight() -marginBottom &&indexY <  getHeight() -marginBottom + textSize + textPadding){
//                invalidate();
//              }
//              if(!isSelect) invalidate();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);

    }

    public int setData(List<PointLineTableBean> score){
        this.score = score;
        if(score != null) XLength = (int) (marginLeft + interval_left_right * score.size());
        invalidate();
        if (XLength > screenWidth) {
            return XLength;
        } else {
            return (int)PreferenceEntity.screenWidth;
        }

    }

    //定义一个接口对象listerner
    private OnColItemSelectListener mListener;

    //获得接口对象的方法。
    public void setOnItemSelectListener(OnColItemSelectListener listener) {
        this.mListener = listener;
    }

    //定义一个接口
    public interface  OnColItemSelectListener{
        public void onColItemSelect(int index, String indexString);
    }

    // 计算绘制时的Y坐标，无数据时返回-999
    private float YCoord(String y0) {
        float y;
        float newValue = 0;
        try {
            y = Float.parseFloat(y0);
        } catch (Exception e) {
            return -999; // 出错则返回-999
        }
        for (int i = 0; i < yCoors.size(); i++) {
            if(i == 0){
                if(y <= yCoors.get(i)){	//比最小值小
//                    newValue = YPoint - 5;	//保证能绘制出来
                    newValue = getHeight() - (marginBottom);
                    return newValue;
                }
            }else if (y >= yCoors.get(i-1) && y <= yCoors.get(i)){
//                newValue = (float) (YPoint-((y-yCoors.get(i-1))/(yCoors.get(i)-yCoors.get(i-1))* marginHorizonal) - (i-1) * marginHorizonal);
                newValue = (float) (getHeight()-((y-yCoors.get(i-1))/(yCoors.get(i)-yCoors.get(i-1))* marginHorizonal) - (marginBottom + marginHorizonal * (i-1)));
                return newValue;
            }else if(y>yCoors.get(yCoors.size()-1)){	//比最大值大
//                newValue = YPoint - (yCoors.size()-1)*marginHorizonal;
                newValue = getHeight() - (marginBottom + marginHorizonal * (yCoors.size()-1));
                return newValue;
            }else{
//    			System.out.println("不满足，重新循环");
            }

        }
        newValue = getHeight() - (marginBottom);	//保证能绘制出来
        return newValue;
    }

    //设置视图的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LOG("onMeasure");
        float width;
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);//获得控件的高度
        if (XLength > screenWidth) {
            width = XLength;
        } else {
            width = screenWidth;
        }
        setMeasuredDimension((int)width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        LOG("onLayout ");
        initView();
    }

    public void initView() {
        if(score == null) score = new ArrayList<>();

        mRectf = new RectF();
        screenWidth = PreferenceEntity.screenWidth;
        screenHeight = LayoutUtil.getInstance().getScreenHeight();
        Resources res = getResources();
        tb = res.getDimension(R.dimen.detection_10);

        textSize = tb * 1.0f;
        lineWidth = LayoutUtil.getInstance().getWidgetHeight(2);
        circleRadius = LayoutUtil.getInstance().getWidgetHeight(8);

//        marginBottom = LayoutUtil.getInstance().getWidgetHeight(103);
//        marginHorizonal = LayoutUtil.getInstance().getWidgetHeight(197)/(yCoors.size() - 1);
        marginBottom = LayoutUtil.getInstance().getWidgetHeight(70);
        marginHorizonal = LayoutUtil.getInstance().getWidgetHeight(208)/(yCoors.size() - 1);

        textMarginPoint = LayoutUtil.getInstance().getWidgetHeight(25);
        marginLeft = LayoutUtil.getInstance().getWidgetWidth(70);
        interval_left_right = LayoutUtil.getInstance().getWidgetWidth(82);
//        XLength = (int) (marginLeft + interval_left_right * percent.size());

        paint_trend_line = initPaint(paint_trend_line);
        paint_coordinate_line = initPaint(paint_coordinate_line);
        paint_circle = initPaint(paint_circle);
        paint_text = initPaint(paint_text);
        paint_text.setColor(ApplicationData.context.getResources().getColor((R.color.text_color_normal)));
        paint_des = initPaint(paint_des);

        paint_coordinate_line.setStrokeWidth(lineWidth * 1.5f);    //设置画笔宽度，宽于正常宽度1.5倍

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(ApplicationData.context.getResources().getColor((R.color.text_color_normal)));
        textPaint.setStrokeWidth(lineWidth);	//画笔宽度1sp
        textPaint.setStyle(Style.FILL);	//实心
        textPaint.setTextSize(textSize);	//字号10sp
        textPaint.setTextAlign(Align.CENTER);
    }

    public PointLineView(Context context) {
        super(context);
//        initView();
    }
    public PointLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDetectionData(context,attrs);
//        initView();
    }
    public PointLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDetectionData(context,attrs);
//        initView();
    }

    /** 绘制高度 */
    private float drawHight;
    private int line_color;

    /**
     * 初始化数据
     * @param context
     * @param attrs
     */
    public void setDetectionData(Context context, AttributeSet attrs) {

        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.chart_ycoords);
        String ycoordinate = attribute.getString(R.styleable.chart_ycoords_ycoordinate);
//        cutIndex = attribute.getInteger(R.styleable.chart_ycoords_ycoordcut,2);
        marginBottom = attribute.getFloat(R.styleable.chart_ycoords_ycoormargin_bottom,100);
        drawHight = attribute.getFloat(R.styleable.chart_ycoords_ycoordraw_height,1000);
        line_color = attribute.getColor(R.styleable.chart_ycoords_line_color,0x096);
        colorLine = line_color;
        colorTrendLine = attribute.getColor(R.styleable.chart_ycoords_trend_line_color,0x20009966);


        initYcoorData((ycoordinate==null||ycoordinate.equals(""))?"0.1, 1.0, 1.5":ycoordinate);

        initView();
    }

    /**
     * 初始化Y坐标的范围
     */
    public void initYcoorData(String ycoordinate){
        String[] yCorrdinateArray;
        yCoors = new ArrayList<>();
        yCorrdinateArray = ycoordinate.split(",");
        for (int i = 0; i < yCorrdinateArray.length; i++) {
            yCoors.add(Float.parseFloat(yCorrdinateArray[i]));
        }

        invalidate();
    }

    /** 屏幕的宽 */
    private float screenWidth;
    /** 屏幕的高 */
    private float screenHeight;

    List<PointLineTableBean> score;

    /** 圆点 */
    private Paint paint_text;
    private Paint paint_circle;
    /** 坐标值描述 */
    private Paint paint_des;
    /** 实线*/
    private Paint paint_trend_line;
    /** 表中横轴刻度 */
    private Paint paint_coordinate_line;
    private TextPaint textPaint;

    private int colorLine = 0xff49dec7;
    private int colorTrendLine = 0xffbcf3ea;
    private int colorCoordinateLine = 0xffEBEBEB;


    private float tb;
    RectF mRectf;
    /** 点击的坐标x */
    public float indexX = 0;
    /** 点击的坐标y */
    public float indexY = 0;

    /** 左右间距 两个圆柱的间距，加上圆柱的直径 */
    private float interval_left_right;
    /**  X轴的长度 */
    public int XLength = 0;
    /** 第一个圆柱中心距离画布左边的距离 */
    private float marginLeft;
    /** 圆柱圆点距离画布底部的距离 */
    private float marginBottom;
    /** 纵坐标轴的纵向间距 以6个区间为标准制定的 */
    private float marginHorizonal;
    /** Y坐标轴原点的位置 */
//    public float YPoint = 0;

    /** 圆点半径 */
    public float circleRadius = 0;
    /** 字体大小 */
    public float textSize = 0;
    /** 画笔宽度 */
    public float lineWidth = 0;
    /** 横坐标轴文字距离原点的纵向距离（文字左下角开始算） */
    public float textMarginPoint = 0;

    /** y轴坐标值 */
    public List<Float> yCoors;
//    public List<String> xCoors;

    private void LOG(String data){
        Log.i("spoort_list", "PointLineView   " + data);
    }

}
