package com.jemer.atong.fragment.personal_center.dialog;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.share.WechatShareUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

//调用方式
//    SexDialogFragment playQueueFragment;
//    private FragmentManager fragmentManager;
//    private String MOVEMENT_TIME_TAG = "sharedialog";
//    /**
//     * 显示分享框
//     */
//    private void ShowMovementDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class SexDialogFragment extends BaseDialogFragment{

    public SexDialogFragment() {
        super(R.layout.fragment_dialog_sex);
    }

    @Override
    protected void initHead() {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
//        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(350));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(450));
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
//        mLayoutUtil.drawViewRBLayout(tv_share_title, -1, -1, -1, -1, 42, -1);
//        mLayoutUtil.drawViewRBLayout(rel_share_content, -1, 166, -1, -1, -1, -1);
//        mLayoutUtil.drawViewRBLinearLayout(iv_share_wechat, 100, 100, -1, -1, -1, -1);
//        mLayoutUtil.drawViewRBLinearLayout(iv_share_moments, 100, 100, 138, -1, -1, -1);
//        tv_share_dismiss.setMinimumHeight(mLayoutUtil.getWidgetHeight(80));
    }

    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {

    }

    @OnClick ({R.id.iv_sex_dialog_man, R.id.iv_sex_dialog_woman})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sex_dialog_man:
                LOG("男");
                if(mSexListener != null){
                    mSexListener.onSex(1);
                }

                break;
            case R.id.iv_sex_dialog_woman:
                LOG("女");
                if(mSexListener != null){
                    mSexListener.onSex(2);
                }
                break;
        }
        dismiss();
    }

    private onSexListener mSexListener;

    public interface onSexListener {
        /** 1男2女 */
        void onSex(int state);
    }

    public void setSexListener(onSexListener listener) {
        mSexListener = listener;
    }

    @BindView(R.id.ll_sex_dialog)
    protected LinearLayout ll_sex_dialog;
    @BindViews({R.id.iv_sex_dialog_man, R.id.iv_sex_dialog_woman})
    protected List<ImageView> ivSex;


}
