package com.jemer.atong.view.dialog.affirm;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.utils.StringUtils;

//调用方式
//    SexDialogFragment playQueueFragment;
//    private FragmentManager fragmentManager;
//    private String MOVEMENT_TIME_TAG = "sharedialog";
//    /**
//     * 显示分享框
//     */
//    private void ShowSexDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class AffirmDialogFragment extends BaseDialogFragment {

    @BindViews({R.id.tv_affirm_dialog_title, R.id.tv_affirm_dialog_content})
    List<TextView> mTVs;
    @BindViews({R.id.bt_affirm_dialog_affirm, R.id.bt_affirm_dialog_cancel})
    protected List<Button> mBTs;


    static AffirmDialogFragment affirmDialogFragment;


    public AffirmDialogFragment() {
        super(R.layout.fra_dialog_affirm);
        TAG = getClass().getSimpleName();
    }


    public static AffirmDialogFragment getInstance(AffirmBean mAffirmBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("affirm_bean", mAffirmBean);
        affirmDialogFragment = new AffirmDialogFragment();
        affirmDialogFragment.setArguments(bundle);
        return affirmDialogFragment;
    }


    @OnClick({R.id.bt_affirm_dialog_affirm, R.id.bt_affirm_dialog_cancel})
    void inputInfo(View view) {
        switch (view.getId()) {
            case R.id.bt_affirm_dialog_affirm:   //确认
                    if (onAffirmListener != null) onAffirmListener.onAffirm(true);
                    dismiss();
                break;
            case R.id.bt_affirm_dialog_cancel:   //取消
                    if (onAffirmListener != null) onAffirmListener.onAffirm(false);
                    dismiss();
                break;
        }
    }

    @Override
    protected void initHead() {
        AffirmBean mAffirmBean = (AffirmBean) getArguments().getSerializable("affirm_bean");
        if(!mAffirmBean.isBackCancel())  setCancelable(false);
        setData(mAffirmBean.getTitle(), mTVs.get(0));
        setData(mAffirmBean.getContent(), mTVs.get(1));
    }

    private void setData(String content, TextView view){
        if(StringUtils.isBlank(content)){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
            view.setText(content);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AlterPhoneDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(677));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

//        //去除阴影
//        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
//        layoutParams.dimAmount = 0.0f;
//        getDialog().getWindow().setAttributes(layoutParams);

        getDialog().setCanceledOnTouchOutside(true);

    }


    @Override
    protected void initLogic() {

    }

    @Override
    public void onResume() {
        super.onResume();
        LOG("onResume");
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected void initLocation() {
    }

    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {
    }


    private onAffirmListener onAffirmListener;

    public interface onAffirmListener {
        void onAffirm(boolean state);
    }

    public void setOnAffirmListener(onAffirmListener listener) {
        this.onAffirmListener = listener;
    }

}
