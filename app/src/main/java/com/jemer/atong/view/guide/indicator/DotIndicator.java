package com.jemer.atong.view.guide.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jemer.atong.R;
import com.jemer.atong.view.guide.interf.GIndicator;


public class DotIndicator extends LinearLayout implements GIndicator {
    private int dotRes;
    private int dotSize;
    private int dotMargin;
    private int selectedPosition;

    private ImageView selDotImagView;

    public DotIndicator(Context context) {
        this(context, (AttributeSet)null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.selectedPosition = 0;
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(17);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GuideIndicator);
        this.dotRes = ta.getResourceId(R.styleable.GuideIndicator_dot, R.drawable.default_bg_dot_view);
        this.dotSize = (int) ta.getDimension(R.styleable.GuideIndicator_dotSize, this.dp2px(5.0F));
        this.dotMargin = (int) ta.getDimension(R.styleable.GuideIndicator_dotMargin, this.dp2px(12.0F));
        ta.recycle();
    }

    private int dp2px(float f) {
        int i = (int) TypedValue.applyDimension(1, f, this.getResources().getDisplayMetrics());
        return i;
    }

    public void setCount(int count) {
        if (count > 0) {
            if (count <= 1) {
                this.setVisibility(INVISIBLE);
            } else {
                this.setVisibility(VISIBLE);
            }

            this.removeAllViews();
            this.selectedPosition = 0;

            for(int i = 0; i < count; ++i) {
                final ImageView imageView = new ImageView(this.getContext());
                LayoutParams lp = new LayoutParams(dotSize, dotSize);
                lp.rightMargin = i == count ? 0 : this.dotMargin;
                imageView.setLayoutParams(lp);
                imageView.setImageResource(this.dotRes);
                this.addView(imageView);
                if (i == this.selectedPosition) {
                    imageView.post(new Runnable() {
                        public void run() {
                            imageView.setSelected(true);
                        }
                    });
                }
            }


        }
    }

    public void setSelected(int position) {
        LOG("" + position);

        int last = this.selectedPosition;
        this.selectedPosition = position;
        this.getChildAt(position).setSelected(true);
        this.getChildAt(last).setSelected(false);
    }

    @Override
    public void onIndicatorScroll(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onIndicatorScrollStateChanged(int state) {

    }

    private void LOG(String data){
        Log.i("spoort_list","DotIndicator： " + data);
    }
}