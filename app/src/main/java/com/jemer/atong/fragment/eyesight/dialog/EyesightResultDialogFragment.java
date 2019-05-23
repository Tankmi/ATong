package com.jemer.atong.fragment.eyesight.dialog;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.entity.eyesight.EyesightEntity;
import com.jemer.atong.entity.eyesight.EyesightHintStepBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.utils.NewWidgetSetting;

/**
 * 测试时提醒左右眼切换
 * leftEyesight
 * rightEyesight
 */
public class EyesightResultDialogFragment extends BaseDialogFragment {

//    protected float leftEyesight, rightEyesight;

    static EyesightResultDialogFragment eyeHintFragment;

    private EyesightEntity.Data mEyeEntity;

    @BindView(R.id.tv_esr_left)
    TextView tv_esr_left;
    @BindView(R.id.tv_esr_right)
    TextView tv_esr_right;

    public EyesightResultDialogFragment() {
        super(R.layout.fragment_dialog_eyesight_result);
        TAG = getClass().getSimpleName() + "     ";
    }

    public static EyesightResultDialogFragment getInstance(EyesightEntity.Data data) {
//    public static EyesightResultDialogFragment getInstance(float leftEyesight, float rightEyesight) {
        Bundle bundle = new Bundle();
//        bundle.putFloat("leftEyesight", leftEyesight);
//        bundle.putFloat("rightEyesight", rightEyesight);
        bundle.putSerializable("data", data);
        eyeHintFragment = new EyesightResultDialogFragment();
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

    @OnClick({R.id.bt_esr_again, R.id.bt_esr_finish})
    void next(View view) {
        switch (view.getId()) {
            case R.id.bt_esr_again:
                LOG("再来一遍");
                EyesightHintStepBean bean = new EyesightHintStepBean(true);
                EventBus.getDefault().post(bean);
                dismiss();
                break;
            case R.id.bt_esr_finish:
//                EyesightHintStepBean bean = new EyesightHintStepBean(state, step);
//                EventBus.getDefault().post(bean);
                getActivity().finish();
                dismiss();
                break;
        }

    }

    @Override
    protected void initContent() {
        initData();
    }

    private void initData() {
//        leftEyesight = getArguments().getFloat("leftEyesight");
//        rightEyesight = getArguments().getFloat("rightEyesight");
        mEyeEntity = (EyesightEntity.Data) getArguments().getSerializable("data");

        tv_esr_left.setText("");
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_left, mContext.getResources().getColor(R.color.bg_color_main), 2f, mEyeEntity.lefteye + "", false);
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_left, -999, 1, "左眼", true);
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_left, mContext.getResources().getColor(R.color.text_color_eye_resulr_e), 1.25f, mEyeEntity.leftresult, true);
        tv_esr_right.setText("");
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_right, mContext.getResources().getColor(R.color.bg_color_main), 2f, mEyeEntity.righteye + "", false);
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_right, -999, 1, "右眼", true);
        NewWidgetSetting.setIdenticalLineTvColor(tv_esr_right, mContext.getResources().getColor(R.color.text_color_eye_resulr_e), 1.25f, mEyeEntity.rightresult, true);
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
