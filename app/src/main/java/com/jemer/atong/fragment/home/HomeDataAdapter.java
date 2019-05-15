package com.jemer.atong.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jemer.atong.R;
import com.jemer.atong.entity.home.HomeEntity;
import com.jemer.atong.net.select_photo.SimpleItemTouchHelperCallback;
import com.jemer.atong.web.activity.WebViewActivity;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.LayoutUtil;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.MyViewHolder> implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<HomeEntity.Data.HomeData> mList;
    private View headerView;

    public HomeDataAdapter(Context context) {
//        hasHeaderView = true;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HomeDataAdapter(Context context, List<HomeEntity.Data.HomeData> data) {
//        hasHeaderView = true;
        this.mContext = context;
        this.mList = data;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addDataToFirst();
    }

    public void setListData(List<HomeEntity.Data.HomeData> data) {
        this.mList = data;
        LOGUtils.LOG("setListData mList.size():  " + mList.size());
        addDataToFirst();
    }

    public void setLoadNextData(List<HomeEntity.Data.HomeData> data) {
        this.mList.addAll(data);
        notifyDataSetChanged();
    }

    private void addDataToFirst() {
//        HomeEntity.Data.HomeData list = null;
//        mList.add(0, list);   //position == 1 时显示暂无数据提示
        notifyDataSetChanged();
    }

    @Override
    public HomeDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.rc_item_home, parent, false), viewType);
//        if(viewType == TYPE_CONTENT){
//            return new MyViewHolder(inflater.inflate(R.layout.item_food_calorie_normal, parent, false),viewType);
//        }
//        else if(viewType == TYPE_NODATA){
//            return new MyViewHolder(inflater.inflate(R.layout.item_recycleview_nodata, parent, false),viewType);
//        }
//        else{
//            return new MyViewHolder(inflater.inflate(R.layout.item_food_calorie_head, parent, false),viewType);
//        }
    }

    @Override
    public void onBindViewHolder(HomeDataAdapter.MyViewHolder holder, final int position) {
        //        holder.setIsRecyclable(false);  //禁止复用
        final HomeEntity.Data.HomeData mData = mList.get(position);
//        if(getItemViewType(position) == TYPE_NODATA){
//            holder.mTVhint.setText("暂无数据");
//        }else
//        if(getItemViewType(position) == TYPE_HEAD){
//            holder.mTVtitle.setText("" +(position+1) + "." + mData.planName);
//            Glide.with(mContext).load(mData.image).into(holder.mIVimage);
//            holder.mTVhint.setText(mData.restTime);
//        }
//        else{
        holder.mTVtitle.setText("" + mData.title);
//            holder.mTVhint.setText(mData.restTime);
        holder.mTVcontent.setText(mData.content);
        RequestOptions options;
        options = new RequestOptions()
                .placeholder(R.drawable.icon_loading)
                .circleCrop();
        Glide.with(holder.imageView).load(mData.image).apply(options).transforms(new CenterCrop(), new RoundedCorners(dp2px(5))).disallowHardwareConfig().into(holder.imageView);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(mContext, WebViewActivity.class);
                intent_home.putExtra("url", mData.url);
                intent_home.putExtra("title_name", mData.title);
                intent_home.putExtra("is_refresh", false);
                mContext.startActivity(intent_home);
            }
        });
//        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        int count = (mList == null ? 0 : mList.size());
        if (headerView != null) {
            count++;
        }
        return count;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mainView;
        private TextView mTVtitle;
        private TextView mTVcontent;
        private ImageView imageView;
//        private TextView mTVhint;

        public MyViewHolder(View view, int viewType) {
            super(view);
//            if(viewType == TYPE_CONTENT){
            mainView = view.findViewById(R.id.ll_item_home_main);
            mTVtitle = view.findViewById(R.id.tv_ihc_title);
            mTVcontent = view.findViewById(R.id.tv_ihc_content);
            imageView = view.findViewById(R.id.iv_item_home);

//                mainView.setMinimumHeight(LayoutUtil.getInstance().getWidgetHeight(124));
//            }
//            else if(viewType == TYPE_NODATA){
//                mTVhint = view.findViewById(R.id.tv_item_nodata);
//            }
//            else{
//                mainView = view.findViewById(R.id.lin_food_calorie_head_main);
//                mTVtitle = view.findViewById(R.id.tv_food_calorie_head_title);
//                mTVcontent = view.findViewById(R.id.tv_food_calorie_head_content);
//
//                mainView.setMinimumHeight(LayoutUtil.getInstance().getWidgetHeight(70));
//            }
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(mList, from, to);
        notifyItemMoved(from, to);
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        headerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private final int TYPE_HEAD = 0;
    private final int TYPE_CONTENT = 1;
//    /** 暂无数据 */
//    private final int TYPE_NODATA = 2;

    @Override
    public int getItemViewType(int position) {
//        final HomeEntity.Data.HomeData mData = mList.get(position);
//
//        if(mData == null) return TYPE_NODATA;

//        if(position == 0)
//            return TYPE_HEAD;
//        else
        return TYPE_CONTENT;
    }

    public int dp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, mContext.getResources().getDisplayMetrics());
    }
}
