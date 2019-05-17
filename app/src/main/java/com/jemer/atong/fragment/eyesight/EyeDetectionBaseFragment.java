package com.jemer.atong.fragment.eyesight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.fragment.eyesight.hint.EyeGuideHintDialogFragment;
import com.jemer.atong.fragment.eyesight.window.EyesightActivity;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterPresenter;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterView;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.utils.PreferencesUtils;

@SuppressLint("ValidFragment")
public class EyeDetectionBaseFragment extends BaseFragment implements PersonalCenterView {

    protected MyHandler mHandler;
    protected PersonalCenterPresenter mPersonPresenter;
    FragmentManager fragmentManager;

    @BindView(R.id.rl_eye_title)  RelativeLayout rl_eye_title;
    @BindView(R.id.tv_eyet_title)  TextView tv_eyet_title;
    @BindViews({R.id.tv_eye_shortsight, R.id.tv_eye_longsight, R.id.tv_eye_astigmastim, R.id.tv_eye_redblue})
    List<TextView> eyeSlight;

    public EyeDetectionBaseFragment(int layoutId) {
        super(layoutId);
    }

    @OnClick({R.id.tv_eye_shortsight, R.id.tv_eye_longsight, R.id.tv_eye_astigmastim, R.id.tv_eye_redblue}) void onclick(View view){
        switch (view.getId()){
            case R.id.tv_eye_shortsight:
                LOG("tv_eye_shortsight ");
                getEyeSight(1);
                break;
            case R.id.tv_eye_longsight:
                getEyeSight(2);
                break;
            case R.id.tv_eye_astigmastim:

                break;
            case R.id.tv_eye_redblue:

                break;
        }
    }


    /**
     * 1234
     * 近视，远视，散光，红绿
     */
    private void eyeSightFragment(int state){
        EyeGuideHintDialogFragment eyeHintFragment = EyeGuideHintDialogFragment.getInstance(state);
        if(fragmentManager == null) fragmentManager = getChildFragmentManager();
        eyeHintFragment.show(fragmentManager , "123");
    }


    /**
     * 1,近视，2，远视
     * @param state
     */
    protected void getEyeSight(int state){
        boolean isHint = PreferencesUtils.getBoolean(mContext, state==1? PreferenceEntity.KEY_EYE_HINT_GUIDE_SHORT:PreferenceEntity.KEY_EYE_HINT_GUIDE_LONG, false);
        if(isHint){
            Intent intent = new Intent(getActivity(), EyesightActivity.class);
            intent.putExtra("state", state);
            getActivity().startActivity(intent);
        }else{
            eyeSightFragment(state);
        }
    }

    @Override
    protected void initContent() {

    }

    @Override
    protected void initHead() {

    }

    @Override
    protected void onVisibile() {
        LOG("onVisibile");
    }


    @Override
    public void changeHeaderSuccess(String url) {
        LOG("用户头像上传成功");
    }

    @Override
    public void changeHeaderFailed(String msg) {
    }

    @Override
    public void getUserInfoSuccess(Object data) {

    }

    @Override
    public void modificationUserInfoSuccess(String name, String value) {

    }

    @Override
    public void loadingShow() {
        setLoading(true, "");
    }

    @Override
    public void loadingDissmis() {
        setLoading(false, "");
    }

    @Override
    public void loginOut() {
        reLoading();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Context> mActivity;

        protected MyHandler(Context activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 0: // 获取版本号

                    break;
            }
        }

    }

    @Override
    protected void initLocation() {
//        mLayoutUtil.drawViewRBLinearLayout(rl_settings_title, 0, 433, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLinearLayout(rl_eye_title, -1, -1, 0, 0, PreferenceEntity.ScreenTop, -1);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initLogic() {
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {

    }

}
