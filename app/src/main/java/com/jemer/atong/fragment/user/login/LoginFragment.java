package com.jemer.atong.fragment.user.login;

import android.view.View;


import com.jemer.atong.R;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;

import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;


/**
 * @author ZhuTao
 * @version V1.0
 * @Title: RegisterActivity.java
 * @Package com.huidaifu.liangzi.activity.user.login
 * @Description: TODO(登录页)
 * @date 2015年12月9日 下午3:57:19
 */
public class LoginFragment extends LoginBindBaseFragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
        TAG = this.getClass().getName();
    }

    @Override
    protected void initHead() {
        super.initHead();
//        setTittle("登录");
    }

    @Override
    protected void initLogic() {
        super.initLogic();
        mUserEntity = new UserEntity();
        mTimeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        et_login_account.setText(NewWidgetSetting.getInstance().filtrationStringbuffer(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ACCOUNT),""));
//        et_login_account.setText("18612037398");
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        LOG("close");
        switch (arg0.getId()) {
//            case R.id.btn_login_ok:
//                setOnloginState(false);
//                login(1);
//                break;
            case R.id.btn_login_account:    //发送手机号获取验证码
                getVerification();
                break;
            case R.id.btn_login_close:    //关闭页面
                isCanGoBack();
                break;
            case R.id.tv_login_veri:    //获取验证码
                getVerification();
                break;
            case R.id.btn_login_verifycode:    //提交验证码
                if(view_verifycode_login.isFinish()){
                    login(view_verifycode_login.getContent());
                }
                break;
//            case R.id.tv_login_aggrement:
//                Intent intent_aggrement = new Intent(mContext, AgreementActivity.class);
//                startActivity(intent_aggrement);
//                break;
        }
    }

    /**
     * 当Fragment的显示状态通过FragmentTransition改变时(hide和show),就会回调这个函数,参数hidden将告诉你这个Fragment现在是被隐藏还是显示着.
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    @Override
    protected void pauseClose() {
        super.pauseClose();

        imm.hideSoftInputFromWindow(et_login_account.getWindowToken(), 0); //强制隐藏键盘
    }

}
