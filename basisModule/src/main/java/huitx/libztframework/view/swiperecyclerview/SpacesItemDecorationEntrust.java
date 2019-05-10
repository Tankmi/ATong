package huitx.libztframework.view.swiperecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者：请叫我百米冲刺 on 2016/12/6 上午11:37
 * 邮箱：mail@hezhilin.cc
 */

public abstract class SpacesItemDecorationEntrust {

    //color的传入方式是resouce.getcolor
    protected Drawable mDivider;

    protected int leftRight;

    protected int topBottom;

    public SpacesItemDecorationEntrust(int leftRight, int topBottom, int mColor) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        if (mColor != 0) {
            mDivider = new ColorDrawable(mColor);
        }
    }


    public abstract void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state);

    public abstract void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state);

}
