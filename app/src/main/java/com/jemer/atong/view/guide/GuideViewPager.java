package com.jemer.atong.view.guide;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;


import com.jemer.atong.view.guide.interf.GIndicator;
import com.jemer.atong.view.guide.interf.GuideViewHolder;
import com.jemer.atong.view.guide.interf.GuideViewPagerPageListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

public class GuideViewPager extends ViewPager {

    GuidePageAdapter mAdapter;
    private GuideViewPagerPageListener mPagerListener;
    //添加的指示器
    private List<GIndicator> mGIndicators;
    //最大值的下标记
    private int  maxPosition;
    //最大长度
    private int  maxNum;

    public GuideViewPager(Context context) {
        super(context);
    }

    public GuideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGIndicators = new ArrayList<>();

        this.addOnPageChangeListener(new OnPageChangeListener() {
            /**
             *   当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。
             * @param position  当前页面，即你点击滑动的页面
             * @param positionOffset  当前页面偏移的百分比
             * @param positionOffsetPixels  当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Iterator var2 = GuideViewPager.this.mGIndicators.iterator();
                while(var2.hasNext()) {
                    GIndicator in = (GIndicator)var2.next();
                    in.onIndicatorScroll(position,positionOffset,positionOffsetPixels);
                }

            }

            /**
             *  此方法是页面跳转完后得到调用
             * @param position 是你当前选中的页面的Position（位置编号,从0开始）。
             */
            @Override
            public void onPageSelected(int position) {
                if(mPagerListener != null){
                    mPagerListener.currentItem(maxNum,position,maxPosition == position);
                }

                Iterator var2 = GuideViewPager.this.mGIndicators.iterator();
                while(var2.hasNext()) {
                    GIndicator in = (GIndicator)var2.next();
                    in.setSelected(position);
                }
            }

            /**
             * 此方法是在状态改变的时候调用
             * @param state  这个参数有三种状态（0，1，2）
             * arg0 ==1时表示正在滑动，
             * arg0==2时表示滑动完毕了，
             * arg0==0时表示什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setOnPageChangeListener(GuideViewPagerPageListener listener){
        if(mPagerListener != null){
            LOG("已经设置过监听，不要重复设置");
            return;
        }
        this.mPagerListener = listener;
    }

    public  <T>  void setViewHolder(GuideViewHolder<T> holder) {
        mAdapter = new GuidePageAdapter(holder);
        super.setAdapter(this.mAdapter);
    }

    public <T> void setData(List<T> data) {

        if (this.mAdapter == null) {
            throw new IllegalStateException("setViewHolder must be called first");
        } else if (data != null && data.size() != 0) {
            Iterator var2 = mGIndicators.iterator();

            while(var2.hasNext()) {
                GIndicator indicator = (GIndicator)var2.next();
                indicator.setCount(data.size());
            }

            this.mAdapter.setData(data);

            maxNum = data.size();
            maxPosition = maxNum - 1;
            mAdapter.setData(data);

            setCurrentItem(0);
        }
    }

    public void addIndicator(GIndicator indicator){
        mGIndicators.add(indicator);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        GuideViewPager.super.setCurrentItem(item,smoothScroll);
    }



    private void LOG(String data){
        Log.i("spoort_list","GuideViewPager： " + data);
    }
}
