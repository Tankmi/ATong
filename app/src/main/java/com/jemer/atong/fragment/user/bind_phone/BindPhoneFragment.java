package com.jemer.atong.fragment.user.bind_phone;

import android.view.View;


import com.jemer.atong.R;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.user.login.LoginBindBaseFragment;

import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;


/**
 * 绑定手机号
 * @author ZhuTao
 * @date 2018/8/20 * @params
*/

public class BindPhoneFragment extends LoginBindBaseFragment {
    /**
     * 记录页面退出时间
     */
    private long exitTime = 0;

    public BindPhoneFragment() {
//        super(R.layout.fragment_bind_phone);
        super(R.layout.fragment_login);
        TAG = this.getClass().getName();
    }

    @Override
    protected void initHead() {
        super.initHead();
        state = 1;
    }

    @Override
    protected void initLogic() {
        super.initLogic();
        mUserEntity = new UserEntity();
        mTimeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        et_login_account.setText(NewWidgetSetting.getInstance().filtrationStringbuffer(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ACCOUNT),""));
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        LOG("close");
        switch (arg0.getId()) {
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
                    bindPhoneNumber(view_verifycode_login.getContent());
                }
                break;
        }
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    protected void pauseClose() {
        super.pauseClose();
        imm.hideSoftInputFromWindow(et_login_account.getWindowToken(), 0); //强制隐藏键盘
    }

}
