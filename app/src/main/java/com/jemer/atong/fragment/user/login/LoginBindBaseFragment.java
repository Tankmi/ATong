package com.jemer.atong.fragment.user.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.activity.HomeActivity;
import com.jemer.atong.activity.user.perfect_info.PerfectInfoActivity;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.user.LoginController;
import com.jemer.atong.fragment.user.LoginPresenter;
import com.jemer.atong.view.EditTextNumberView;
import com.jemer.atong.view.verify_code.VerificationCodeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NetUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * @author ZhuTao
 * @version V1.0
 * @Title: RegisterBaseActivity.java
 * @Package com.huidaifu.liangzi.activity.user.login
 * @Description: TODO(登录父类)
 * @date 2015年12月9日 下午3:57:39
 */
@SuppressLint("ValidFragment")
public class LoginBindBaseFragment extends BaseFragment implements OnClickListener,
        LoginController.LoginView {

    LoginPresenter mPresenter;

    protected UserEntity mUserEntity;
    String phone;

    /** 0,登录，1，绑定手机号 */
    protected int state = 0;

    /** 标记是微信2还是QQ3绑定手机号 */
    protected int type;

    public LoginBindBaseFragment(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void initHead() {
        if(mPresenter == null){
            mPresenter = new LoginPresenter();
        }
        mPresenter.attachView(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void destroyClose() {
        mPresenter.detachView();
    }

    @Override
    protected void initContent() {
        findView();


//        initVerifyCodeView();
    }

//    @Override
    public void paddingDatas(String mData, int type) {
//        setLoading(false,"");
//        setOnloginState(true);
//        Gson gson = new Gson();
//        try {
//            mUserEntity = gson.fromJson(mData, UserEntity.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (!NetUtils.isAPNType(mContext)) {
//            } else {
//                String str;
//                if(type==2) str = "验证码发送失败，请稍候重试！";
//                else str = ((state==0)?"登录":(state==1)?"绑定手机号":"") + "失败，请稍候重试！";
//                ToastUtils.showToast(str);
//            }
//            setOnloginState(true);
//            return;
//        }
//        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
//            if (type == 1 || type == 3) {    //登录 || 绑定手机号
//                PreferenceEntity.isLogin = true;
//                PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, phone);
//                PreferenceEntity.setUserEntity(mUserEntity.list);
//                Intent intent_home;
//                if (mUserEntity.list.isall.equals("0")) {
//                    intent_home = new Intent(mContext, PerfectInfoActivity.class);
//                } else {
//                    intent_home = new Intent(mContext, HomeActivity.class);
//                }
//                startActivity(intent_home);
//                getActivity().finish();
//            } else if (type == 2) {
//                ToastUtils.showToast("验证码已发送至手机");
//                mTimeCount.start();
//                isShowVerifyCode(true);
//            }
//        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
//            reLoading();
//        } else {
//            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
//        }
    }

    @Override
    public void error(String msg, int type) {
        super.error(msg,type);
        setLoading(false,"");
        setOnloginState(true);
//        if (type == 1) {
//            ToastUtils.showToast("登录失败，请稍候重试！");
//        }else if (type == 2) {
//            ToastUtils.showToast("验证码发送失败，请稍候重试！");
//        }else if (type == 3) {
//            ToastUtils.showToast("手机号绑定失败，请稍候重试！");
//        }

    }


    /**
     * 登录 1
     */
    public void login(String verifyCode) {
        phone = et_login_account.getRealNumber();
        LOG("phone:" + phone);
//        String token = 	XGPushConfig.getToken(mContext);
        Map<String,String> mMap = new HashMap<>();
        mMap.put("phone", phone);
        mMap.put("vd", verifyCode);
        mMap.put("imei", ApplicationData.imei);
        mMap.put("outerToken", "a12312313123123123" );

        mPresenter.login(mMap);
    }

    /**
     * 获取验证码 2
     * 32592659
     */
    public void getVerification() {
        String phone = et_login_account.getRealNumber();
        mPresenter.getVerifyCode(phone);
    }



    @Override
    protected void initLocation() {
        mLayoutUtil.setIsFullScreen(true);
        mLayoutUtil.drawViewDefaultLayout(btn_login_close, 120, 56, -1, 0, -1, 0);
        mLayoutUtil.drawViewRBLinearLayout(tv_login_hint, 0, 0, -1, 0, 180, 0);
//        mLayoutUtil.drawViewRBLinearLayout(rel_login_account, 0, 0, -1, -1, -1, 0);
//        mLayoutUtil.drawViewDefaultLayout(tv_login_veri, -1, -1, 0, -1, 0, 0);
        tv_login_veri.setMinimumWidth(mLayoutUtil.getWidgetWidth(166));


    }

    public void findView() {
        btn_login_close = findViewByIds(R.id.btn_login_close);
        sc_login_main = findViewByIds(R.id.sc_login_main);

        rel_login_account = findViewByIds(R.id.rel_login_account);
        et_login_account = findViewByIds(R.id.et_login_account);
        rel_login_verification = findViewByIds(R.id.rel_login_verification);
        tv_login_veri = findViewByIds(R.id.tv_login_veri);

        btn_login_close.setOnClickListener(this);
        tv_login_veri.setOnClickListener(this);
        addTextchange();
    }



    /** 返回false，本级消化
     * 返回true,本级没有操作，交给下一级做处理 */
    public boolean fragementIsGoBack(){
//        return isCanGoBack();
        return  true;
    }


    protected Button btn_login_close;
    @BindView(R.id.tv_login_hint) protected TextView tv_login_hint;
    /** 全局父包裹 */
    protected ScrollView sc_login_main;
    /** 帐号父包裹 */
    protected RelativeLayout rel_login_account;
    /**  帐号  */
    protected EditTextNumberView et_login_account;
    /** 验证码父包裹  */
    @BindView(R.id.rel_login_verification) protected RelativeLayout rel_login_verification;
    @BindView(R.id.et_login_veri) protected EditText et_login_veri;
    /** 验证码按钮 */
    protected TextView tv_login_veri;
    @BindView(R.id.lin_phone_login) protected LinearLayout lin_phone_login;
    @BindView(R.id.btn_phone_login) protected Button btn_phone_login;

    @OnClick({R.id.lin_phone_login,R.id.btn_phone_login}) void login(){
        LOG("登录");
        if(canLogin())
        login(et_login_veri.getText().toString());
        else
            ToastUtils.showToast("请检查输入内容是否正确！");


    }

    /**
     * 输入框添加监听
     */
    public void addTextchange() {

        et_login_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {    //有内容
                    canLogin();
                    if (StringUtils.isMobileNO(et_login_account.getRealNumber()))
                        tv_login_veri.setEnabled(true);
                    else tv_login_veri.setEnabled(false);
                } else {    //无内容
                    tv_login_veri.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public boolean canLogin() {
        if (et_login_veri.getText().length() > 0 && StringUtils.isMobileNO(et_login_account.getRealNumber())) {
            return true;
        }
        return false;
    }

    @Override
    protected void initLogic() {

    }


    @Override
    protected void pauseClose() {
    }

    @Override
    public void onClick(View arg0) {
    }

    protected TimeCount mTimeCount;
    @Override
    public void getVerifyCodeState(boolean state) {
        if(!state){
            LOG("验证码获取失败，错误信息：");
            return;
        }
        if(state){
            ToastUtils.showToast("验证码已发送至手机");
            mTimeCount.start();
        }
    }

    @Override
    public void loginState(boolean state,String isall) {
        if(!state){
            LOG("登录失败，错误信息：" + isall);
            return;
        }
        PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, phone);
        Intent intent_home;
        if (isall.equals("0")) {
            intent_home = new Intent(mContext, PerfectInfoActivity.class);
        } else {
            intent_home = new Intent(mContext, HomeActivity.class);
        }
        startActivity(intent_home);
        getActivity().finish();
    }

    @Override
    public void loadingShow() {
        setLoading(true);
    }

    @Override
    public void loadingDissmis() {
        setLoading(false);
    }

    @Override
    public void loginOut() {
        loginOut();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    /* 定义一个倒计时的内部类 */
    protected class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            tv_login_veri.setText("获取验证码");
//			btn_register_veri.setClickable(true);
            tv_login_veri.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_login_veri.setEnabled(false);
            tv_login_veri.setText("" + millisUntilFinished / 1000 + "s");
        }

    }

    /**
     * 网络请求时等情况时，设置页面的控件是否可以点击
     *
     * @param state
     */
    public void setOnloginState(boolean state) {
        if (state) {
            tv_login_veri.setEnabled(true);
        } else {
            tv_login_veri.setEnabled(false);
        }
    }
}
