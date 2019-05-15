package com.jemer.atong.view.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.jemer.atong.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.viewpager.widget.ViewPager;


public class DiscoverSlideShowView extends RelativeLayout {
	private Context mContext;
	// 自定义轮播图的资源
	private List<String> imageValues;
	// 放轮播图片的ImageView 的list
//	private List<View> imageViewsList;
	

	private BannerViewPager mViewPager;
	private LinearLayout slideview_dot;
	/** 占位条 */
	private ImageView slideview_iv_ph;
	private ScorllListener mScorllListener;
	
	Timer mTimer;
	TimerTask mTask;
	// 当前轮播页
	private int currentItems = 0;
	// 定时任务
//	public static ScheduledExecutorService scheduledExecutorService;
	// 自动轮播启用开关
	private static boolean isAutoPlay = true;


	public DiscoverSlideShowView(Context context) {
		this(context, null);
	}

	public DiscoverSlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void scrollListenter(ScorllListener listenter){
		this.mScorllListener = listenter;
	}
	
	public DiscoverSlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		imageValues = new ArrayList<String>();


	}
	
	public void setImageValues(List<String> imageUrls){
		if(imageValues!= null){
			imageValues.clear();
		}
		this.imageValues = imageUrls;
		initData();
	}

	/**
	 * 开启定时任务
	 * @param state 0,第一次不延时
	 */
	public void startTask(int state) {
		// TODO Auto-generated method stub
		if(imageValues == null || imageValues.size()<=0) return;
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
		mTimer.schedule(mTask,state * 5 * 1000, 5 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
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

	public void delete(){
		if(imageValues != null){
			imageValues.clear();
			imageValues = null;
		}
	}

	// 处理EmptyMessage(0)
		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				mViewPager.setCurrentItem(currentItems, true);
			}
		};

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		//获取资源
		initUI(mContext);
		startTask(0);
	}

//	private MyPagerAdapter mAdapter;
	private BannerPageAdapter mAdapter;
	int pointMargin = 0;

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		if (imageValues == null || imageValues.size() == 0)
			return;

		if (mAdapter ==null){
			mAdapter = new BannerPageAdapter<String>();
		}

		View viwPagerLayout = LayoutInflater.from(context).inflate(R.layout.layout_discover_first_slideshow, this, true);

		slideview_dot = viwPagerLayout.findViewById(R.id.slideview_dot);
		slideview_iv_ph = viwPagerLayout.findViewById(R.id.slideview_iv_ph);
		
//		initDot(slideview_dot,imageValues.size()-2,0);
		initDot(slideview_dot,imageValues.size(),0);
		
		mViewPager = viwPagerLayout.findViewById(R.id.viewPager);
//		mViewPager.setPageTransformer();	//设置切换动画
		mViewPager.setFocusable(true);
//		mViewPager.setBannerAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setData(imageValues);




		if(slideview_dot.getChildCount() > 2) {
            mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //获得两个圆点之间的距离
                    pointMargin = slideview_dot.getChildAt(1).getLeft() - slideview_dot.getChildAt(0).getLeft();
                    LOG("小圆点间距：" + pointMargin);
                    mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
		
	}

	/**
	 * 设置选中的点的位置
	 * @param size : 总共的点,列表两边各加了一个缓冲区，所以总数要-2
	 * @param position : 选中的位置，下标0开始计算，viewpager是从1开始的，所以要-1
	 *  */
	private void initDot(LinearLayout ll_dot , int size, int position){
//		if(ll_dot != null)ll_dot.removeAllViews();
			selectPostion = 0;
			for (int i = 0; i < size; i++) {
				if (i == position) {
					ImageView imageview = new ImageView(mContext);

					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(dp2px(15), dp2px(15));
					ll.setMargins(dp2px(30), 0, 0, 0);
					imageview.setLayoutParams(ll);
					imageview.setImageResource(R.drawable.iv_slideview_dot_sel);
					ll_dot.addView(imageview);
				} else {
					ImageView imageview = new ImageView(mContext);
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(dp2px(15), dp2px(15));
					ll.setMargins(dp2px(30), 0, 0, 0);
					imageview.setLayoutParams(ll);
					imageview.setImageResource(R.drawable.iv_slideview_dot_unsel);
					ll_dot.addView(imageview);
				}
			}
	}

	private int selectPostion;

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		//0,什么都没做。1正在滑动。2滑动完毕
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_DRAGGING) {	//空闲或者滑动中
				if (mViewPager.getCurrentItem() == mAdapter.getCount() - 1) {
					mViewPager.setCurrentItem(1,false);
				} else if (mViewPager.getCurrentItem() == 0) {
					mViewPager.setCurrentItem(mAdapter.getCount()-2,false);
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
						startTask(1);
					}

					break;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
//            float leftMargin = pointMargin * (position + positionOffset);
            float leftMargin = pointMargin * (positionOffset);
//            LOG("页面偏移值" + leftMargin);

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) slideview_dot.getLayoutParams();
//            params.leftMargin = (int) leftMargin;
//            slideview_dot.setLayoutParams(params);
		}

		@Override
		public void onPageSelected(int pos) {
			int lastPosition = selectPostion;
			selectPostion = mAdapter.toRealPosition(pos);
			currentItems = pos;

			LOG(  " pos   " + pos + "   selectPostion   " + selectPostion + " lastPosition   " + lastPosition);

			ImageView imageview = (ImageView) slideview_dot.getChildAt(selectPostion);
			imageview.setImageResource(R.drawable.iv_slideview_dot_sel);

			if(selectPostion != lastPosition){
				ImageView imageviewUnSel = (ImageView) slideview_dot.getChildAt(lastPosition);
				imageviewUnSel.setImageResource(R.drawable.iv_slideview_dot_unsel);
			}

		}
	}



	/**
	 * 滑动  回调
	 */
	public interface ScorllListener{
		public void setOnClick(int position);
	}

	private void LOG(String data){
		Log.i("spoort_list","轮播图控件： " + data);
	}

	public int dp2px(float f) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getResources().getDisplayMetrics());
	}
}
