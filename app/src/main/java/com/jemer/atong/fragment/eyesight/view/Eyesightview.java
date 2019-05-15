package com.jemer.atong.fragment.eyesight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import huitx.libztframework.utils.LOGUtils;

public class Eyesightview extends RelativeLayout {
    private Context context;
    String TAG = "EyesightView   ";

    float downX,downY;
    float upX,upY;


    public Eyesightview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void initView(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) { // 判斷觸控的動作
            case MotionEvent.ACTION_DOWN: // 按下
                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE: // 拖曳
                return true;
            case MotionEvent.ACTION_UP: // 放開

                upX = event.getX();
                upY = event.getY();
                LOG("up " + downX + " " + upX);
                LOG("up " + downY + " " + upY);

                float x = Math.abs(upX - downX);
                float y = Math.abs(upY - downY);
                if(!(x>=30 || y>=30) ){
                    LOG("不计为滑动" + x + " y " + y);
                    return false;
                }
                double z = Math.sqrt(x * x + y * y);
                int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 角度
                if (upY < downY && jiaodu > 45) {// 上
                    LOG( "角度:" + jiaodu + ", 動作:上");
                } else if (upY > downY && jiaodu > 45) {// 下
                    LOG( "角度:" + jiaodu + ", 動作:下");
                } else if (upX < downX && jiaodu <= 45) {// 左
                    LOG( "角度:" + jiaodu + ", 動作:左");
                    // 原方向不是向右時，方向轉右
                } else if (upX > downX && jiaodu <= 45) {// 右
                    LOG( "角度:" + jiaodu + ", 動作:右");
                    // 原方向不是向左時，方向轉右
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    public interface EyesightViewListener{

    }


    private void LOG(String str){
        LOGUtils.LOG(TAG + str);
    }
}
