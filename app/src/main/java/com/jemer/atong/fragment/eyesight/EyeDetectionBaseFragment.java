package com.jemer.atong.fragment.eyesight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.fragment.eyesight.hint.EyeHintDialogFragment;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterPresenter;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterView;

import java.lang.ref.WeakReference;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class EyeDetectionBaseFragment extends BaseFragment implements PersonalCenterView {

    protected PersonalCenterPresenter mPersonPresenter;

    @BindView(R.id.rl_eye_title)  RelativeLayout rl_eye_title;
    @BindView(R.id.tv_eyet_title)  TextView tv_eyet_title;
//    @BindViews({R.id.tv_eye_shortsight, R.id.tv_eye_longsight, R.id.tv_eye_astigmastim, R.id.tv_eye_redblue})
//    List<TextView> eyeSlight;

    public EyeDetectionBaseFragment(int layoutId) {
        super(layoutId);
    }

    @OnClick({R.id.tv_eye_shortsight, R.id.tv_eye_longsight, R.id.tv_eye_astigmastim, R.id.tv_eye_redblue}) void onclick(View view){
        switch (view.getId()){
            case R.id.tv_eye_shortsight:
                LOG("tv_eye_shortsight ");
                eyeSightFragment(1);
                break;
            case R.id.tv_eye_longsight:

                break;
            case R.id.tv_eye_astigmastim:

                break;
            case R.id.tv_eye_redblue:

                break;
        }
    }

    FragmentManager fragmentManager;
    /**
     * 1234
     * 近视，远视，散光，红绿
     */
    private void eyeSightFragment(int state){
        EyeHintDialogFragment eyeHintFragment = EyeHintDialogFragment.getInstance(state);
        if(fragmentManager == null) fragmentManager = getChildFragmentManager();
        eyeHintFragment.show(fragmentManager , "123");
    }

    @Override
    protected void initContent() {

    }


    @Override
    public void error(String msg, int type) {
        super.error(msg, type);

    }


    @Override
    protected void initHead() {

    }

    @Override
    protected void onVisibile() {
    }



    protected final int GETUSERINFO = 10003;




    protected MyHandler mHandler;



    @Override
    public void changeHeaderSuccess(String url) {
        LOG("用户头像上传成功");
    }

    @Override
    public void changeHeaderFailed(String msg) {
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


//    @BindView(R.id.sc_settings)
//    protected ScrollView sc_settings;


    @Override
    protected void initLocation() {
//        mLayoutUtil.drawViewRBLinearLayout(rl_settings_title, 0, 433, 0, 0, 0, 0);
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
