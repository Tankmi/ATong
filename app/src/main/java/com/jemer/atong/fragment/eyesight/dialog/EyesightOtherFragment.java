package com.jemer.atong.fragment.eyesight.dialog;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.entity.eyesight.EyesightEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试时提醒左右眼切换
 * leftEyesight
 * rightEyesight
 */
public class EyesightOtherFragment extends BaseDialogFragment {

    //1，散光，2，红绿
    protected int state;

    static EyesightOtherFragment eyeHintFragment;

    private EyesightEntity.Data mEyeEntity;

    @BindView(R.id.bt_eyeso_left)
    Button btnBack;
    @BindView(R.id.tv_eyeso_title)
    TextView tvTitle;
    @BindView(R.id.iv_eyesight_other)
    ImageView ivOther;
    @BindView(R.id.ll_eyesight_other_rb)
    LinearLayout llRB;
    @BindView(R.id.iv_eyesight_other_hint)
    ImageView ivHint;

    public EyesightOtherFragment() {
        super(R.layout.fragment_dialog_eyesight_other);
        TAG = getClass().getSimpleName() + "     ";
    }

    public static EyesightOtherFragment getInstance(int state) {
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        eyeHintFragment = new EyesightOtherFragment();
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
//        setCancelable(false);
    }

    @OnClick({R.id.bt_eyeso_left })
    void next(View view) {
        switch (view.getId()) {
            case R.id.bt_eyeso_left:
                dismiss();
                break;
        }

    }

    @Override
    protected void initContent() {
        initData();
    }

    private void initData() {
        state = getArguments().getInt("state");
        if(state == 1){
            ivOther.setImageResource(R.drawable.iv_eyesight_astigmatism);
            ivOther.setVisibility(View.VISIBLE);
            llRB.setVisibility(View.GONE);
            ivHint.setImageResource(R.drawable.bg_eyesight_astigmatism_hint);
        }else{
            ivOther.setImageResource(R.drawable.iv_eyesight_rb);
            ivOther.setVisibility(View.VISIBLE);
            llRB.setVisibility(View.GONE);
            ivHint.setImageResource(R.drawable.bg_eyesight_redblue_hint);
        }
    }


    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewDefaultLayout(btnBack, 170, 86, -1, -1, -1, -1);
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
