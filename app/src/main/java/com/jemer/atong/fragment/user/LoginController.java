package com.jemer.atong.fragment.user;

import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.base.BaseView;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.io.File;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:05
 * 描述：动态 mvp
 */
public class LoginController {

    public interface LoginView extends BaseView {

        void getVerifyCodeState(boolean state);
        void loginState(boolean state,String info);

    }

    public interface  LoginPresenter extends BasePresenter<LoginView> {
        String getVerifyCode(String phone);
        boolean login(Map<String, String> map);
    }

    public interface LoginModel{
        void GetVerification(BaseHttpEntity<ResponseBody> entity, String phone, String key);
        void Login(BaseHttpEntity<ResponseBody> entity, Map<String, String> map);
        void updatePhone(BaseHttpEntity<ResponseBody> entity, Map<String, String> map);
    }
}
