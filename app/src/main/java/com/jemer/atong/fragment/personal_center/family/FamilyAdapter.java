package com.jemer.atong.fragment.personal_center.family;

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
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.home.HomeEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.net.select_photo.SimpleItemTouchHelperCallback;
import com.jemer.atong.web.activity.WebViewActivity;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<UserEntity.Data.FamilyData> mList;
    private View headerView;
    private String userId;

    public FamilyAdapter(Context context) {
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userId = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ID,"");
    }

    public FamilyAdapter(Context context, List<UserEntity.Data.FamilyData> data) {
//        hasHeaderView = true;
        this.mContext = context;
        this.mList = data;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    public void setListData(List<UserEntity.Data.FamilyData> data) {
        this.mList = data;
        LOGUtils.LOG("setListData mList.size():  " + mList.size());
        notifyDataSetChanged();
    }

    public void setLoadNextData(List<UserEntity.Data.FamilyData> data) {
        this.mList.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public FamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.rc_item_family, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(FamilyAdapter.MyViewHolder holder, final int position) {
        final UserEntity.Data.FamilyData mData = mList.get(position);
        holder.mTVtitle.setText("" + mData.name);
       LOGUtils.LOG("userId: " + userId + "  mData.id: " + mData.id + userId.equals(mData.id));
        if(userId.equals(mData.id)){
            holder.mIvBtn.setVisibility(View.GONE);
            return;
        }
        holder.mIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null) mOnItemClickListener.onRemoveListener(mData.id, position);
            }
        });
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
        private TextView mTVtitle;
        private ImageView mIvBtn;

        public MyViewHolder(View view, int viewType) {
            super(view);
            mTVtitle = view.findViewById(R.id.tv_rcitem_family);
            mIvBtn = view.findViewById(R.id.iv_rcitem_family);
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


    private OnFamilyRemoveListener mOnItemClickListener;

    public interface OnFamilyRemoveListener {
        void onRemoveListener(int id, int position);
    }

    public void setOnItemClickListener(OnFamilyRemoveListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private final int TYPE_HEAD = 0;
    private final int TYPE_CONTENT = 1;
//    /** 暂无数据 */
//    private final int TYPE_NODATA = 2;

    @Override
    public int getItemViewType(int position) {
//        final UserEntity.Data.FamilyData mData = mList.get(position);
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
