package com.jemer.atong.view.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.viewpager.widget.ViewPager;

public class BannerViewPager extends ViewPager {

    private int selectPostion;
    private int currentItems = 0;
    Timer mTimer;
    TimerTask mTask;
    // 自动轮播启用开关
    private static boolean isAutoPlay = true;

    BannerPageAdapter mAdapter;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new MyPageChangeListener());
    }

//    public void setBannerAdapter(BannerPageAdapter adapter) {
//        mAdapter = adapter;
//        super.setAdapter(mAdapter);
//    }

    public <T> void setViewHolder(BannerViewHolder<T> holder){
        mAdapter = new BannerPageAdapter(holder);
        super.setAdapter(mAdapter);
    }

    public <T> void setData(List<T> data) {
        mAdapter.setData(data);
        setCurrentItem(1);
        startTask();
    }

    @Override
    public void setCurrentItem(int item) {
        LOG("setCurrentItem" + item + "   mAdapter.toPosition(item)  " + mAdapter.toPosition(item));
//        super.setCurrentItem(mAdapter.toPosition(item));
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        LOG("setCurrentItem boolean   " + item + " to " + mAdapter.toPosition(item));
//        BannerViewPager.super.setCurrentItem(mAdapter.toPosition(item),smoothScroll);
        BannerViewPager.super.setCurrentItem(item,smoothScroll);
    }

    private void LOG(String data){
        Log.i("spoort_list","BannerViewPager： " + data);
    }


    /**
     * 开启定时任务
     */
    public void startTask() {
        // TODO Auto-generated method stub
        if(mAdapter.getCount() <= 1) return;
        isAutoPlay = true;
        if(mTimer!=null){
            mTimer.cancel();
            mTask.cancel();
            System.gc();
        }
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
//				LOG("执行定时切换");
                currentItems ++;
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTask,3 * 1000, 3 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
    }

    /**
     * 停止定时任务
     */
    public void stopTask() {
        // TODO Auto-generated method stub
        try {
            isAutoPlay = false;
            if(mTimer!=null){
                mTimer.cancel();
                mTask.cancel();
                System.gc();
            }
            LOG("广告轮播控件 关闭定时任务");
        } catch (Exception e) {

        }
    }

    // 处理EmptyMessage(0)
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setCurrentItem(currentItems, true);
        }
    };




    /**
     * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        //0,什么都没做。1正在滑动。2滑动完毕
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_DRAGGING) {	//空闲或者滑动中
                if (getCurrentItem() == mAdapter.getCount() - 1) {
                    setCurrentItem(1,false);
                } else if (getCurrentItem() == 0) {
                    setCurrentItem(mAdapter.getCount()-2,false);
                }
            }

        switch (state) {
            case 1:// 手势滑动中
                isAutoPlay = false;
                stopTask();
                break;
            case 2:// 滑动完毕
                break;
            case 0:// 空闲
                if(!isAutoPlay){
                    isAutoPlay = true;
                    startTask();
                }

                break;
        }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int pos) {
            int lastPosition = selectPostion;
            selectPostion = mAdapter.toRealPosition(pos);
            currentItems = pos;

            LOG(  " pos   " + pos + "   selectPostion   " + selectPostion + " lastPosition   " + lastPosition);
        }
    }

    public void close(){
        stopTask();
        if(mHandler != null ){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
