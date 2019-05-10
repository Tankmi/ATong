package com.jemer.atong.view.guide.interf;

public interface GIndicator {
    //初始化个数
    void setCount(int var1);
    //设置选中的下表
    void setSelected(int var1);

    /**
     *   当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。
     * @param position  当前页面，即你点击滑动的页面
     * @param positionOffset  当前页面偏移的百分比
     * @param positionOffsetPixels  当前页面偏移的像素位置
     */
    void onIndicatorScroll(int position, float positionOffset, int positionOffsetPixels);

    /**
     * 此方法是在状态改变的时候调用
     * @param state  这个参数有三种状态（0，1，2）
     * arg0 ==1时表示正在滑动，
     * arg0==2时表示滑动完毕了，
     * arg0==0时表示什么都没做。
     */
    void onIndicatorScrollStateChanged(int state);


}
