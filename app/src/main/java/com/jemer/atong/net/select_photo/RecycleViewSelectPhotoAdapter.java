package com.jemer.atong.net.select_photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jemer.atong.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.LayoutUtil;


public class RecycleViewSelectPhotoAdapter extends RecyclerView.Adapter<RecycleViewSelectPhotoAdapter.MyViewHolder> implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private LinkedList<PhotoEntity> mList;
    private getPhoto getPhoto;
    private List<String> selectPhoto = new ArrayList<>();
    private getSelectPhoto getSelectPhoto;
    private View headerView;
    /**
     * 集合选择的最大条目数，如果为1，默认为单选
     */
    private int maxNum;
    /**
     * 单选模式下，标记选中的下标
     */
    private int perPos = -1;
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_NOMAL = 0;

    public RecycleViewSelectPhotoAdapter(Context context, LinkedList<PhotoEntity> data, int maxNum, boolean isAddFirst)
    {
        this.mContext = context;
        this.mList = data;
        this.maxNum = maxNum;
        LOGUtils.LOG("mList.size():" + mList.size());
        if (isAddFirst) {
            //第一个是填充位置，预留填充位置
            PhotoEntity mData;
            if (mList == null || mList.size() == 0) {
                mData = new PhotoEntity("", "", 0, false);
                mList.add(mData);
            } else {
                mData = mList.get(0);
                mList.addFirst(mData);
            }
        }
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectPhoto.clear();
    }

    public void getPhoto(getPhoto getPhoto)
    {
        this.getPhoto = getPhoto;
    }

    public void getSelectPhoto(getSelectPhoto getSelectPhoto)
    {
        this.getSelectPhoto = getSelectPhoto;
    }

    @Override
    public RecycleViewSelectPhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
//        View view;
//        if(viewType == TYPE_HEAD && headerView != null){
//            view = headerView;
//        }else {
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_recycleview_select_photo, parent, false);
//        }
//        return new MyViewHolder(view);

//        if (viewType == 0) {//显示的第一张图片的布局 ， 就是调用摄像头拍照的布局。
//            return new MyViewHolder(inflater.inflate(R.layout.item_recycleview_select_photo, parent, false));
//        }//之后的图片显示的布局。
        return new MyViewHolder(inflater.inflate(R.layout.item_recycleview_select_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecycleViewSelectPhotoAdapter.MyViewHolder holder, final int position)
    {

        holder.setIsRecyclable(false);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = (LayoutUtil.getInstance().getWidgetWidth(244));
        params.width = (LayoutUtil.getInstance().getWidgetWidth(244));
//        params.height = (int) (LayoutUtil.getInstance().getScreenWidth() / 3);
//        params.width = (int) (LayoutUtil.getInstance().getScreenWidth() / 3);
        holder.itemView.setLayoutParams(params);//重设item大小

        if (getItemViewType(position) == 0) {
            holder.checkBox.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.iv_take_photo).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //因为拍照要返回参数，所以接口回调方式将事件处理交给activity处理
                    getPhoto.getTakeCreame(v);
                }
            });
            return;
        }

        final int nPosition = position;
        final MyViewHolder myViewHolder = holder;
        final PhotoEntity photoInfo = mList.get(nPosition);

        try {
            Glide.with(mContext).load(photoInfo.path).into(myViewHolder.image);
            if (photoInfo.getChecked()) { //标记选中
                myViewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.checkBox.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }

        myViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (maxNum == 1) {    //单选模式
                    LOGUtils.LOG("perPos: " + perPos + " ;position: " + nPosition);
                    if (perPos == nPosition) {
                        return;
                    }
                    if (perPos != -1) {   //标记上一个值为未选中状态
                        mList.get(perPos).setChecked(false);
//                        notifyItemChanged(perPos);
                    }
                    mList.get(nPosition).setChecked(true);
//                    notifyItemChanged(position);

                    selectPhoto.clear();
                    selectPhoto.add(photoInfo.path);
                    perPos = nPosition;

                    notifyDataSetChanged();
                    //回调选中的图片地址
                    getSelectPhoto.getListPhoto(selectPhoto);
                    return;
                }

                //多选模式
                if (selectPhoto.size() < maxNum) {
                    //selectPhoto为选中的集合。
                    if (selectPhoto.contains(photoInfo.path)) {
                        selectPhoto.remove(photoInfo.path);
                        photoInfo.setChecked(false);
                        myViewHolder.checkBox.setVisibility(View.GONE);

                    } else {
                        selectPhoto.add(photoInfo.path);
                        photoInfo.setChecked(true);
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);
                    }
                    getSelectPhoto.getListPhoto(selectPhoto);
                } else {
                    if (selectPhoto.contains(photoInfo.path)) {
                        selectPhoto.remove(photoInfo.path);
                        photoInfo.setChecked(false);
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(mContext, "最多选择9张", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (photoInfo.getChecked()) {
            myViewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.checkBox.setVisibility(View.GONE);
        }


//        if(getItemViewType(position) == TYPE_HEAD)
//            return;
//        int pos = getRealPosition(holder);
//        holder.tv.setText(list.get(pos).getName());
//        if (mOnItemClickListener != null) {
//            holder.tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(v,position);
//                }
//            });
//            holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(v, position);
//                    return true;
//                }
//            });
//        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder)
    {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }

    @Override
    public int getItemCount()
    {
        int count = (mList == null ? 0 : mList.size());
        if (headerView != null) {
            count++;
        }
        return count;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView checkBox;

        public MyViewHolder(View view)
        {
            super(view);
            if (view == headerView)
                return;
            image = view.findViewById(R.id.iv_item_sel_photo);
            checkBox = view.findViewById(R.id.iv_item_sel_photo_check);

            LayoutUtil.getInstance().drawViewRBLayout(image, 244, 244, -1, -1, -1, -1);
            LayoutUtil.getInstance().drawViewRBLayout(checkBox, 50, 50, -1, -1, -1, -1);
        }
    }

    @Override
    public void onItemDismiss(int position)
    {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to)
    {
        Collections.swap(mList, from, to);
        notifyItemMoved(from, to);
    }

    public void setHeaderView(View headerView)
    {
        this.headerView = headerView;
        headerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        notifyItemInserted(0);
    }

    public View getHeaderView()
    {
        return headerView;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public int getItemViewType(int position)
    {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(headerView == null)
//            return TYPE_NOMAL;
//        if(position == 0)
//            return TYPE_HEAD;
//        return TYPE_NOMAL;
//    }

    //获取拍照后的照片
    public interface getPhoto {
        void getTakeCreame(View v);
    }

    public interface getSelectPhoto {
        void getListPhoto(List<String> photoPath);
    }

}
