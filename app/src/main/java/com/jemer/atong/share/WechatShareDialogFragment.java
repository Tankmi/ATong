package com.jemer.atong.share;

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
public class WechatShareDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public WechatShareDialogFragment()
    {
        super(R.layout.dialog_share);
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
    public void onStart()
    {
        super.onStart();
        //设置fragment高度 、宽度
//        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(350));
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(true);
    }


    @Override
    protected void initLogic()  {

    }


    @Override
    public void onResume()
    {
        super.onResume();
        LOG("onResume");
    }

    private String shareUrl;
    private int shareType;

    public void setShareInfo(String shareUrl)
    {
        this.shareUrl = shareUrl;

    }


    @Override
    protected void initContent()
    {
        rel_share_main = findViewByIds(R.id.rel_share_main);
        tv_share_title = findViewByIds(R.id.tv_share_title);
        rel_share_content = findViewByIds(R.id.rel_share_content);
        iv_share_wechat = findViewByIds(R.id.iv_share_wechat);
        iv_share_moments = findViewByIds(R.id.iv_share_moments);
        tv_share_dismiss = findViewByIds(R.id.tv_share_dismiss);

        iv_share_wechat.setOnClickListener(this);
        iv_share_moments.setOnClickListener(this);
        tv_share_dismiss.setOnClickListener(this);
    }

    @Override
    protected void initLocation()
    {
        mLayoutUtil.drawViewRBLayout(tv_share_title, -1, -1, -1, -1, 42, -1);
        mLayoutUtil.drawViewRBLayout(rel_share_content, -1, 166, -1, -1, -1, -1);
        mLayoutUtil.drawViewRBLinearLayout(iv_share_wechat, 100, 100, -1, -1, -1, -1);
        mLayoutUtil.drawViewRBLinearLayout(iv_share_moments, 100, 100, 138, -1, -1, -1);
        tv_share_dismiss.setMinimumHeight(mLayoutUtil.getWidgetHeight(80));
    }

    @Override
    protected void pauseClose()
    {

    }

    @Override
    protected void destroyClose()
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.iv_share_wechat:
                if(shareUrl!=null && !shareUrl.equals("")){
                    if(WechatShareUtil.wechatShare(mContext,shareUrl,1)){
                        dismiss();
                    }
                }
                break;
            case R.id.iv_share_moments:
                if(shareUrl!=null && !shareUrl.equals("")){
                    if(WechatShareUtil.wechatShare(mContext,shareUrl,2)){
                        dismiss();
                    }
                }
                break;
            case R.id.tv_share_dismiss:
                dismiss();
                break;
//            case R.id.tv_share_dismiss:
//                int value = view_guidance_weight.getValue();
//                float equalCalorie = mUnit * (value / 60.0f);
//                equalCalorie = NumberConversion.preciseNumber(equalCalorie, 0);
//                if (mMovementListener != null)  mMovementListener.onSelMovementTime(value,(int)equalCalorie);
//                else LOG("回调事件为空！");
//                dismiss();
//                break;
        }
    }

    private OnShareListener mShareListener;

    public interface OnShareListener {
        void onShare(int state);
    }

    public void setQueueListener(OnShareListener listener)
    {
        mShareListener = listener;
    }

    private RelativeLayout rel_share_main;
    private LinearLayout rel_share_content;
    protected ImageView iv_share_wechat,iv_share_moments;
    private TextView tv_share_title,tv_share_dismiss;


}
