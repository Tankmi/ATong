package com.jemer.atong.fragment.eyesight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.eyesight.dialog.EyeGuideHintDialogFragment;
import com.jemer.atong.fragment.eyesight.dialog.EyeSightSelUserDialogFragment;
import com.jemer.atong.fragment.eyesight.dialog.EyesightOtherFragment;
import com.jemer.atong.fragment.eyesight.window.EyesightActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;

@SuppressLint("ValidFragment")
public class EyeDetectionBaseFragment extends BaseFragment implements EyeSightSelUserDialogFragment.OnSelUserListener {

    protected MyHandler mHandler;
//    protected PersonalCenterPresenter mPersonPresenter;
    FragmentManager fragmentManager;

    /** 1,近视，2，远视 */
    private int eyeState;

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
                getEyeSight(1,true);
                break;
            case R.id.tv_eye_longsight:
                getEyeSight(2,true);
                break;
            case R.id.tv_eye_astigmastim:
                eyeSightOtherFragment(1);
                break;
            case R.id.tv_eye_redblue:
                eyeSightOtherFragment(2);
                break;
        }
    }


    /**
     * 12
     * 近视，远视
     */
    private void eyeSightFragment(int state){
        EyeGuideHintDialogFragment eyeHintFragment = EyeGuideHintDialogFragment.getInstance(state);
        if(fragmentManager == null) fragmentManager = getChildFragmentManager();
        eyeHintFragment.show(fragmentManager , "123");
    }

    /**
     * 12
     * 散光，红绿
     */
    private void eyeSightOtherFragment(int state){
        EyesightOtherFragment eyeHintFragment = EyesightOtherFragment.getInstance(state);
        if(fragmentManager == null) fragmentManager = getChildFragmentManager();
        eyeHintFragment.show(fragmentManager , "1234");
    }


    EyeSightSelUserDialogFragment selUserFragment;
    /**
     * 选择用户
     */
    private void selUserFragment(){
        if(selUserFragment == null){
            selUserFragment = new EyeSightSelUserDialogFragment();
            selUserFragment.setSelUserListener(this);
        }
        if(fragmentManager == null) fragmentManager = getChildFragmentManager();
        selUserFragment.show(fragmentManager , "123");
    }


    @Override
    public void onSelUser(String userid) {
        LOG("选中的用户ID：" + userid + "  测试类型：" + eyeState);
        PreferencesUtils.putString(mContext, PreferenceEntity.KEY_CACHE_FAMILY_USERID,userid);

        Intent intent = new Intent(getActivity(), EyesightActivity.class);
        intent.putExtra("state", eyeState);
        getActivity().startActivity(intent);
    }

    /**
     * 1,近视，2，远视
     * @param state
     * @param isShowHint  是否显示引导页
     */
    protected void getEyeSight(int state,boolean isShowHint){
        this.eyeState = state;
        if(isShowHint){
            eyeSightFragment(state);
        }else{
            if(hasFamily()){
                selUserFragment();
            }else{
                Intent intent = new Intent(getActivity(), EyesightActivity.class);
                intent.putExtra("state", eyeState);
                getActivity().startActivity(intent);
            }
        }
    }

    private boolean hasFamily(){
        String FamilyData = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_CACHE_FAMILY);
        if(!StringUtils.isBlank(FamilyData)){
            Gson gson = new Gson();
            UserEntity mUserEntity;
            try {
                mUserEntity = gson.fromJson(FamilyData, UserEntity.class);
            } catch (Exception e) {
                return false;
            }
            if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                if(mUserEntity.data.list!=null && mUserEntity.data.list.size()>1){  //有家庭用户
                    return true;
                }
            }
        }
        return false;
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
        mLayoutUtil.drawViewDefaultLinearLayout(rl_eye_title, -1, -1, 0, 0, (int) PreferenceEntity.ScreenTop, -1);
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
