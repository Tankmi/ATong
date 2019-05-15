package com.jemer.atong.fragment.personal_center.net;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.user.LoginController;
import com.jemer.atong.fragment.user.login.net.LoginModelImpl;
import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.io.File;
import java.util.Map;

import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:28
 * 描述：
 */
public class PersonalCenterPresenter implements BasePresenter<PersonalCenterView> {

    private PersonalCenterModel mModel;
    private PersonalCenterView mView;

    public PersonalCenterPresenter() {
        mModel = new PersonalCenterModel();
    }

    @Override
    public void attachView(PersonalCenterView view) {
        this.mView = view;
        if(mModel == null) mModel = new PersonalCenterModel();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    public String uploadingPic( File file) {
        mView.loadingShow();
        mModel.executePicture(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
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
                    mView.changeHeaderSuccess(mEntity.data.head);
                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {
                mView.loadingDissmis();
                mView.changeHeaderFailed(error);
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        }, file);
        return null;
    }

}
