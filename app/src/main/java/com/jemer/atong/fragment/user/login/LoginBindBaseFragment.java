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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.activity.user.perfect_info.PerfectInfoActivity;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.view.verify_code.VerificationCodeView;

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
public class LoginBindBaseFragment extends BaseFragment implements OnClickListener, VerificationCodeView.VerifyCodeInterface {

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

    }

    @Override
    protected void initContent() {
        findView();
        if(state == 0) tv_login_hint.setText("请输入手机号");
        else tv_login_hint.setText("绑定手机号");

//        initVerifyCodeView();
    }

//    @Override
    public void paddingDatas(String mData, int type) {
        setLoading(false,"");
        setOnloginState(true);
        Gson gson = new Gson();
        try {
            mUserEntity = gson.fromJson(mData, UserEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (!NetUtils.isAPNType(mContext)) {
            } else {
                String str;
                if(type==2) str = "验证码发送失败，请稍候重试！";
                else str = ((state==0)?"登录":(state==1)?"绑定手机号":"") + "失败，请稍候重试！";
                ToastUtils.showToast(str);
            }
            setOnloginState(true);
            return;
        }
        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
            if (type == 1 || type == 3) {    //登录 || 绑定手机号
                PreferenceEntity.isLogin = true;
                PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, phone);
                PreferenceEntity.setUserEntity(mUserEntity.data);
                Intent intent_home;
                if (mUserEntity.data.isall.equals("0")) {
                    intent_home = new Intent(mContext, PerfectInfoActivity.class);
                } else {
                    intent_home = new Intent(mContext, HomeActivity.class);
                }
                startActivity(intent_home);
                getActivity().finish();
            } else if (type == 2) {
                ToastUtils.showToast("验证码已发送至手机");
                mTimeCount.start();
                isShowVerifyCode(true);
            }
        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
            reLoading();
        } else {
            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
        }
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

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//			UserEntity user_Entity = (UserEntity) msg.obj;
            switch (msg.what) {

            }
        }

    };

    /**
     * 登录 1
     */
    public void login(String verifyCode) {
        phone = tv_login_veri_account.getText().toString();
        String token = 	XGPushConfig.getToken(mContext);

        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phone);
//        params.addBodyParameter("psw", MD5Utils.md5(psw)); //md5加密
        params.addBodyParameter("vd", verifyCode);
        params.addBodyParameter("imei", ApplicationData.imei);
        params.addBodyParameter("outerToken", "a" + token);
        mgetNetData.GetData(this, UrlConstant.API_LOGIN, 1, params);
        setLoading(true,"");
    }

    /**
     * 获取验证码 2
     * 32592659
     */
    public void getVerification() {

        String phone = et_login_account.getRealNumber().toString();

        final StringBuilder url = new StringBuilder();
        url.append(UrlConstant.API_VERIFICATION);
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("imei", ApplicationData.imei);
        mgetNetData.GetData(this, url.toString(), 2, params);
        setLoading(true,"");
    }

    /**
     * 绑定手机号 3
     */
    public void bindPhoneNumber(String ver) {
        phone = tv_login_veri_account.getText().toString();
        String sex = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_SEX, "");
        String name = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_NICK, "");
        String head = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_HEADER, "");
        String unionId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_APP_WX_QQ_UNIONID, "");

        LOG(sex + "," + name + "," + head + "," + unionId);
        String token = XGPushConfig.getToken(mContext);

        RequestParams params = new RequestParams();
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("vd", ver);
        params.addBodyParameter("imei", ApplicationData.imei);
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("name", name);
        params.addBodyParameter("head", head);
        params.addBodyParameter("unionId", unionId);
        params.addBodyParameter("outerToken", "a" + token);
        mgetNetData.GetData(this, UrlConstant.API_WX_BIND, 3, params);
        setLoading(true,"");
    }


    @Override
    protected void initLocation() {
        mLayoutUtil.setIsFullScreen(true);
        mLayoutUtil.drawViewDefaultLayout(btn_login_close, 120, 56, -1, 0, -1, 0);

        mLayoutUtil.drawViewRBLinearLayout(tv_login_hint, 0, 0, -1, 0, 232, 0);

        mLayoutUtil.drawViewRBLinearLayout(rel_login_account, 0, 0, -1, -1, -1, 0);
        mLayoutUtil.drawViewDefaultLayout(btn_login_account, 46, 46, -1, -1, -1, -1);

        mLayoutUtil.drawViewDefaultLayout(tv_login_veri, -1, -1, 0, -1, 0, 0);
        tv_login_veri.setMinimumWidth(mLayoutUtil.getWidgetWidth(148));

        mLayoutUtil.drawViewRBLinearLayout(views_login_verifycode, 0, 0, 0, 0, 84, 0);

    }

    public void findView() {
        btn_login_close = findViewByIds(R.id.btn_login_close);
        sc_login_main = findViewByIds(R.id.sc_login_main);
        rel_login_account = findViewByIds(R.id.rel_login_account);
        tv_login_hint = findViewByIds(R.id.tv_login_hint);
        et_login_account = findViewByIds(R.id.et_login_account);
        btn_login_account = findViewByIds(R.id.btn_login_account);
        rel_login_verification = findViewByIds(R.id.rel_login_verification);
        tv_login_veri_account = findViewByIds(R.id.tv_login_veri_account);
        tv_login_veri = findViewByIds(R.id.tv_login_veri);
        views_login_verifycode = findViewByIds(R.id.views_login_verifycode);

        btn_login_account.setOnClickListener(this);
        btn_login_close.setOnClickListener(this);
        tv_login_veri.setOnClickListener(this);
        addTextchange();
    }

    /**
     * 展示/隐藏 验证码输入框
     */
    protected void initVerifyCodeView() {
        if (view_verifycode_login == null) {
            views_login_verifycode.inflate();
            view_verifycode_login = findViewByIds(R.id.view_verifycode_login);
            btn_login_verifycode = findViewByIds(R.id.btn_login_verifycode);
            view_verifycode_login.setVerifyCodeListener(this);
            btn_login_verifycode.setOnClickListener(this);
            btn_login_verifycode.setEnabled(false);
            mLayoutUtil.drawViewRBLayout(view_verifycode_login, 532, 0, 0, 0, 0, 0);
            mLayoutUtil.drawViewRBLayout(btn_login_verifycode, 46, 46, 8, -1, -1, -1);
            view_verifycode_login.isShowCursor(false);

            views_login_verifycode.setVisibility(View.VISIBLE);
            view_verifycode_login.setVisibility(View.VISIBLE);

        } else {
            views_login_verifycode.setVisibility(View.VISIBLE);
            view_verifycode_login.setVisibility(View.VISIBLE);
        }
        view_verifycode_login.clear();
        view_verifycode_login.isShowkeyBoard(true,imm);
    }

    /** 点击返回键后的操作
     *
     * */
    protected boolean isCanGoBack(){
        if(view_verifycode_login != null && view_verifycode_login.getVisibility() == View.VISIBLE){
            isShowVerifyCode(false);
            return false;
        }else{
            et_login_account.setText("");
            view_verifycode_login = null;
            getFragmentManager().popBackStack();
            return true;
        }
    }

    /** 返回false，本级消化
     * 返回true,本级没有操作，交给下一级做处理 */
    public boolean fragementIsGoBack(){
        return isCanGoBack();
    }

    protected void isShowVerifyCode(boolean isShow){
        if(isShow){
            tv_login_hint.setText("验证码码已发送至");
            tv_login_veri_account.setText(et_login_account.getRealNumber().toString());
            rel_login_account.setVisibility(View.GONE);
            rel_login_verification.setVisibility(View.VISIBLE);
            initVerifyCodeView();
        }else{
            if(state == 0) tv_login_hint.setText("请输入手机号");
            else tv_login_hint.setText("绑定手机号");

            tv_login_veri_account.setText(et_login_account.getRealNumber().toString());
            rel_login_account.setVisibility(View.VISIBLE);
            rel_login_verification.setVisibility(View.GONE);
            views_login_verifycode.setVisibility(View.GONE);
            view_verifycode_login.setVisibility(View.GONE);
        }

    }

    protected Button btn_login_close;
    /** 全局父包裹 */
    protected ScrollView sc_login_main;
    protected TextView tv_login_hint;
    /** 帐号父包裹 */
    protected RelativeLayout rel_login_account;
    /**  帐号  */
    protected EditTextNumberView et_login_account;
    protected Button btn_login_account;
    /** 验证码父包裹  */
    protected RelativeLayout rel_login_verification;
    protected TextView tv_login_veri_account;
    /** 验证码按钮 */
    protected TextView tv_login_veri;

    protected ViewStub views_login_verifycode;
    protected VerificationCodeView view_verifycode_login;
    protected Button btn_login_verifycode;

    /** 进度条 */
//    protected ProgressBar login_pb;

    /**
     * 输入框添加监听
     */
    public void addTextchange() {

        et_login_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {    //有内容
                    LOG("有内容：" + (et_login_account.getRealNumber().toString()));
                    if (StringUtils.isMobileNO(et_login_account.getRealNumber().toString()))
//                    if (StringUtils.isMobileNO(et_login_account.getText().toString()))
                        btn_login_account.setEnabled(true);
                    else btn_login_account.setEnabled(false);
                } else {    //无内容
                    btn_login_account.setEnabled(false);
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

    @Override
    protected void initLogic() {

    }

    @Override
    protected void destroyClose() {
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    public void onClick(View arg0) {
    }



    @Override
    public void onVerifyCodeListener(boolean isEnter, String data)
    {
        if(isEnter){
            btn_login_verifycode.setEnabled(true);
        }else btn_login_verifycode.setEnabled(false);
    }

    protected TimeCount mTimeCount;

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
