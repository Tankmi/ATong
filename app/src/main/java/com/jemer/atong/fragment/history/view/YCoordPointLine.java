package com.jemer.atong.fragment.history.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;

import java.util.ArrayList;
import java.util.List;

import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.LayoutUtil;

/**
 * 体重历史记录Y轴坐标
 * @author ZhuTao
 * @date 2018/12/15
 * @params
*/


public class YCoordPointLine extends View {

    private float tb;
    private Paint paint_text;

    private int colorText = ApplicationData.context.getResources().getColor(R.color.text_color_hint);

    /** 换行数 */
    private int cutIndex;
    /** 圆柱圆点距离画布底部的距离 */
    private float marginBottom;
    /** 纵坐标轴的纵向间距 */
    private float marginVertical;
    /** 绘制高度 */
    private float drawHight;
    /** 字体大小 */
    public float textSize = 0;

    /** Y坐标轴原点的位置 */
    public float YPoint = 0;
    public List<String> yCoords;

    public YCoordPointLine(Context context) {
        super(context);
    }
    public YCoordPointLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDetectionData(context,attrs);
    }
    public YCoordPointLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDetectionData(context,attrs);
    }

    /**
     * 初始化数据
     * @param context
     * @param attrs
     */
    public void setDetectionData(Context context, AttributeSet attrs) {

        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.chart_ycoords);
        String ycoordinate = attribute.getString(R.styleable.chart_ycoords_ycoordinate);
        cutIndex = attribute.getInteger(R.styleable.chart_ycoords_ycoordcut,2);
        marginBottom = attribute.getFloat(R.styleable.chart_ycoords_ycoormargin_bottom,80);
        drawHight = attribute.getFloat(R.styleable.chart_ycoords_ycoordraw_height,1000);

        marginBottom = LayoutUtil.getInstance().getWidgetHeight(marginBottom);
        drawHight = LayoutUtil.getInstance().getWidgetHeight(drawHight);

        initYcoorData((ycoordinate==null||ycoordinate.equals(""))?"30,60,100":ycoordinate);
    }

    /**
     * 初始化Y坐标的范围
     */
    public void initYcoorData(String ycoordinate){
        if(yCoords == null) yCoords = new ArrayList<String>();
        String[] yCorrdinateArray =  ycoordinate.split(",");
        for (int i = 0; i < yCorrdinateArray.length; i++) {
            yCoords.add(yCorrdinateArray[i]);
        }

        marginVertical = drawHight/(yCoords.size()-1);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        init();
    }

    public void init() {
        Resources res = getResources();
        tb = res.getDimension(R.dimen.detection_10);
        textSize = tb * 1f;	//10sp

        paint_text = new Paint();
        paint_text.setStrokeWidth(textSize);
        paint_text.setTextSize(textSize);
        paint_text.setColor(colorText);
        paint_text.setTextAlign(Paint.Align.CENTER);
        paint_text.setAntiAlias(true);
        paint_text.setStyle(Paint.Style.FILL);	//实心

    }

    protected void onDraw(Canvas canvas) {
//		canvas.drawColor(0xffee03fd);
        drawDate(canvas);
    }


    /**
     *	x轴 y轴文字描述信息
     * @param canvas
     */
    public void drawDate(Canvas canvas) {
        LOGUtils.LOG("getHeight()): " + getHeight());
        LOGUtils.LOG("yCoords.size(): " + yCoords.size());
        for (int i = 0; i < yCoords.size(); i++) {
            String text;
            if(yCoords.get(i).length()>cutIndex){
                TextPaint textPaint = new TextPaint();
                textPaint.setAntiAlias(true);
                textPaint.setColor(ApplicationData.context.getResources().getColor(R.color.text_color_hint));
                textPaint.setStrokeWidth(tb*0.1f);	//画笔宽度1sp
                textPaint.setStyle(Paint.Style.FILL);	//实心
                textPaint.setTextSize(textSize);	//字号10sp
                textPaint.setTextAlign(Paint.Align.CENTER);

                String t1 = yCoords.get(i);
                text = t1.substring(0,cutIndex) + "\n" + t1.substring(cutIndex);

                StaticLayout layout = new StaticLayout(text, textPaint, 10000, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

                canvas.save();
                canvas.translate( getWidth()/2, getHeight() - marginBottom - i* marginVertical - layout.getHeight()/2);
                layout.draw(canvas);
                canvas.restore();//别忘了restore

            }else{
                text = yCoords.get(i);
                canvas.drawText(text, getWidth()/2, getHeight() - marginBottom - i* marginVertical + textSize/2,paint_text);
                LOGUtils.LOG("getHeight() - marginBottom - i* marginVertical + textSize/2: " + (getHeight() - marginBottom - i* marginVertical + textSize/2));
            }
        }
    }

//    //设置视图的大小
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////		setMeasuredDimension(XLength, (int) (0.352f  *screenHeight));
//        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);//获得控件的高度
//        setMeasuredDimension(width, height);
//        YPoint = height - marginBottom;
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
