package com.jemer.atong.fragment.eyesight.hint;


import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.view.guide.GuideViewPager;
import com.jemer.atong.view.guide.indicator.DotIndicator;
import com.jemer.atong.view.guide.interf.GuideViewHolder;
import com.jemer.atong.view.guide.interf.GuideViewPagerPageListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心
 *
 * @author ZhuTao
 * @date 2018/11/28
 * @params
 */

public class EyeHintDialogFragment extends BaseDialogFragment implements GuideViewPagerPageListener {

    static EyeHintDialogFragment eyeHintFragment;

    private int state;
    private boolean isEnd;

    @BindView(R.id.guide_eyesight_hint)
    GuideViewPager mViewpager;
    @BindView(R.id.dot_eyesight_hint)
    DotIndicator mDotView;
    @BindView(R.id.btn_eyesight_hint)
    Button mBtn;

    private String[] images = {
            "https://images.unsplash.com/photo-1531256379416-9f000e90aacc?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1513757378314-e46255f6ed16?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1506269085878-5c33839927e9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1543095414-e0660f5a1a5c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
    };

    public EyeHintDialogFragment() {
        super(R.layout.fragment_eye_sight_hint);
        TAG = getClass().getSimpleName() + "     ";
    }

    public static EyeHintDialogFragment getInstance(int state){
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        if(eyeHintFragment == null) eyeHintFragment = new EyeHintDialogFragment();
        eyeHintFragment.setArguments(bundle);
        return eyeHintFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initHead() {

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(677));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @OnClick(R.id.btn_eyesight_hint)void next(){
        mViewpager.setCurrentItem(mViewpager.getCurrentItem()+1);
        LOG("isEnd:  " + isEnd);
    }

    @Override
    public void currentItem(int sum, int position, boolean isEnd) {
        LOG(sum + " " + position + " " + isEnd);
        this.isEnd = isEnd;
        if(isEnd) mBtn.setText("开始测试");
        else mBtn.setText("下一步");
    }

    @Override
    protected void initContent() {
        mViewpager.setViewHolder(new BannerViewHolder());
        mViewpager.addIndicator(mDotView);
        mViewpager.setOnPageChangeListener(this);
        mViewpager.setData(Arrays.asList(images));

        state = getArguments().getInt("state");
        LOG("state  " + state);
    }

    @Override
    protected void initLocation() {

    }

    @Override
    protected void initLogic() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class BannerViewHolder implements GuideViewHolder<String> {
        private boolean isRoundedCorner = true;

        public BannerViewHolder() {

        }

        public BannerViewHolder(boolean roundedCorner) {
            isRoundedCorner = roundedCorner;
        }

        @Override
        public View getView(Context context, final int position, String data) {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.item_banner_home, null);
            ImageView imageView = inflate.findViewById(R.id.image);
            if (isRoundedCorner) {
                Glide.with(imageView).load(data).transforms(new CenterCrop(), new RoundedCorners(dp2px(5))).disallowHardwareConfig().into(imageView);
            } else {
                Glide.with(imageView).load(data).transforms(new CenterCrop()).disallowHardwareConfig().into(imageView);
            }

            imageView.setOnClickListener(view -> {
//                Intent intent_home = new Intent(mContext, WebViewActivity.class);
//                intent_home.putExtra("url", data.url);
//                intent_home.putExtra("title_name", data.title);
//                intent_home.putExtra("is_refresh", false);
//                mContext.startActivity(intent_home);
            });
            return inflate;
        }
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {
    }

    public int dp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getResources().getDisplayMetrics());
    }

}
