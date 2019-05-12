package com.jemer.atong.fragment.user;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.user.login.net.LoginModelImpl;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.io.File;
import java.util.Map;

import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:28
 * 描述：
 */
public class LoginPresenter implements LoginController.LoginPresenter {

    private LoginController.LoginModel mModel;
    private LoginController.LoginView mView;

    public LoginPresenter() {
        mModel = new LoginModelImpl();
    }


    @Override
    public void attachView(LoginController.LoginView view) {
        this.mView = view;
       if(mModel == null) mModel = new LoginModelImpl();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    @Override
    public String getVerifyCode(String phone) {
        mView.loadingShow();
        mModel.GetVerification(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                Gson gson = new Gson();
                UserEntity mEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    mView.getVerifyCodeState(true);
                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();
            }
        }, phone,  ApplicationData.imei);

        return null;
    }

    @Override
    public boolean login(Map<String, String> map) {
        mView.loadingShow();
        mModel.Login(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {

                Gson gson = new Gson();
                UserEntity mEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    PreferenceEntity.isLogin = true;
                    PreferenceEntity.setUserEntity(mEntity.data);
                    mView.loginState(true,mEntity.data.isall);

                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }

            }

            @Override
            public void onError(String error) {
                mView.loginState(false,error);
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();
            }
        },map);
        return false;
    }

    /** 修改手机号 */
    public boolean UpdatePhone(Map<String, String> map){
        mView.loadingShow();
        mModel.Login(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {

                Gson gson = new Gson();
                UserEntity mEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    ToastUtils.showToast(mEntity.msg);
                    mView.loginState(true, mEntity.msg);

                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }

            }

            @Override
            public void onError(String error) {
                mView.loginState(false,error);
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();
            }
        },map);
        return false;
    }
}
