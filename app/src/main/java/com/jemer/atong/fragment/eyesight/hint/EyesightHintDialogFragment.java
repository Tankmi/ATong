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
import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.entity.eyesight.EyesightHintBean;
import com.jemer.atong.entity.eyesight.EyesightHintStepBean;
import com.jemer.atong.view.guide.GuideViewPager;
import com.jemer.atong.view.guide.indicator.DotIndicator;
import com.jemer.atong.view.guide.interf.GuideViewHolder;
import com.jemer.atong.view.guide.interf.GuideViewPagerPageListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试时提醒左右眼切换
 * state 1，近视，2，远视
 * step 1，左眼，2，右眼
 */
public class EyesightHintDialogFragment extends BaseDialogFragment {

    static EyesightHintDialogFragment eyeHintFragment;

    private int state, step;

    @BindView(R.id.iv_eyesight_hint)
    ImageView mImgView;
    @BindView(R.id.tv_eyesight_hint)
    TextView mHintView;
    @BindView(R.id.bt_eyesight_hint)
    Button mBtn;

    public EyesightHintDialogFragment() {
        super(R.layout.fragment_dialog_eyesight_hint);
        TAG = getClass().getSimpleName() + "     ";
    }

    public static EyesightHintDialogFragment getInstance(int state, int step) {
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        bundle.putInt("step", step);
//        if(eyeHintFragment == null)
        eyeHintFragment = new EyesightHintDialogFragment();
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
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        setCancelable(false);

    }

    @OnClick(R.id.bt_eyesight_hint)
    void next() {
        dismiss();
    }

    @Override
    protected void initContent() {
        initData();
    }

    private void initData() {
        state = getArguments().getInt("state");
        step = getArguments().getInt("step");
        if (step == 1) {
            mHintView.setText("用手遮住或闭上左眼");
        } else {
            mHintView.setText("用手遮住或闭上右眼");
        }
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

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {
        LOG("destroyClose");
    }

}
