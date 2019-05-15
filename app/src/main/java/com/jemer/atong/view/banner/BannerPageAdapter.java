package com.jemer.atong.view.banner;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jemer.atong.R;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BannerPageAdapter<T> extends PagerAdapter {
    protected List<T> mData = new ArrayList();
    //标记是不是只有一张图片
    private boolean once;

    private BannerViewHolder<T> viewHolder;

    public BannerPageAdapter(BannerViewHolder holder) {
        this.viewHolder = holder;
        this.mData = new ArrayList();
    }

    public BannerPageAdapter() {
        this.mData = new ArrayList();
    }


    public void setData(List<T> data){
        this.mData.clear();
        this.mData.addAll(data);
        this.once = data.size() == 1;
        if (!this.once) {
            T first = data.get(0);
            T last = data.get(data.size() - 1);
            this.mData.add(first);
            this.mData.add(0, last);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 由ViewPager中实际的position转换到用户认为的position
     * @param position
     * @return
     */
    public int toRealPosition(int position) {
        if (this.once) {
            return 0;
        } else if (position == this.getCount() - 1) {
            return 0;
        } else {
            return position == 0 ? this.getCount() - 3 : position - 1;
        }
    }


    /**
     * 由用户认为的position转换到ViewPager中对应的position
     * @param realPosition
     * @return
     */
    public int toPosition(int realPosition) {
        return this.once ? 0 : realPosition + 1;
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
//        container.removeView(view);
        ((ViewPager) container).removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {	//初始化条目数据

        View view = this.viewHolder.getView(container.getContext(), this.toRealPosition(position), (T) this.mData.get(position));
        container.addView(view);
        return view;

//        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_home, null);
//        ImageView imageView = view.findViewById(R.id.image);
//
//        Glide.with(imageView)
////                .load(mData.get(toRealPosition(position)))
//                .load(mData.get(position))
////					.apply(options)
//                .into(imageView);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        ((ViewPager) container).addView(view);
//        return view;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) { }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
