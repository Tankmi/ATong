package com.jemer.atong.fragment.eyesight.hint;


import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.entity.eyesight.EyesightHintBean;
import com.jemer.atong.view.guide.GuideViewPager;
import com.jemer.atong.view.guide.indicator.DotIndicator;
import com.jemer.atong.view.guide.interf.GuideViewHolder;
import com.jemer.atong.view.guide.interf.GuideViewPagerPageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1，近视，2，远视
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
//        if(eyeHintFragment == null)
            eyeHintFragment = new EyeHintDialogFragment();
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

        initData();


    }

    private void initData(){
        state = getArguments().getInt("state");
        LOG("state  " + state);
        List<EyesightHintBean> lists = new ArrayList<>();
        if(state == 1){
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_01,"第一步：测试者手持手机距被测试者眼睛2.5米远"));
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_02,"第二步：手机与被测试者眼睛置同一水平位"));
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_03,"第三步：被测试者指出缺口方向，测试者正确滑动"));
        }else{
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_01,"远视第一步：手机距被测试者眼睛25cm远"));
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_02,"第二步：手机与被测试者眼睛置同一水平位"));
            lists.add(new EyesightHintBean(R.drawable.bg_eyesight_guide_03,"第三步：按缺口方向正确滑动"));
        }
        mViewpager.setData(lists);
//        mViewpager.setData(Arrays.asList(lists));


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

    public class BannerViewHolder implements GuideViewHolder<EyesightHintBean> {
        private boolean isRoundedCorner = true;

        public BannerViewHolder() {

        }

        public BannerViewHolder(boolean roundedCorner) {
            isRoundedCorner = roundedCorner;
        }

        @Override
        public View getView(Context context, final int position, EyesightHintBean data) {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.item_guide_eyesight_hint, null);
            ImageView imageView = inflate.findViewById(R.id.iv_item_eyesight);
            TextView textView = inflate.findViewById(R.id.tv_item_eyesight);
//            if (isRoundedCorner) {
//                Glide.with(imageView).load(data).transforms(new CenterCrop(), new RoundedCorners(dp2px(5))).disallowHardwareConfig().into(imageView);
//            } else {
                Glide.with(imageView).load(data.getImgId()).disallowHardwareConfig().into(imageView);
//            }

            textView.setText(data.getContent());

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
        LOG("destroyClose");
    }

    public int dp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getResources().getDisplayMetrics());
    }

}
