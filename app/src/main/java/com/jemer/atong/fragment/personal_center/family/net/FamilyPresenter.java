package com.jemer.atong.fragment.personal_center.family.net;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.io.File;
import java.util.HashMap;
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
public class FamilyPresenter implements BasePresenter<FamilyView> {

    private FamilyModel mModel;
    private FamilyView mView;

    public FamilyPresenter() {
        mModel = new FamilyModel();
    }

    @Override
    public void attachView(FamilyView view) {
        this.mView = view;
        if(mModel == null) mModel = new FamilyModel();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    public String getUserInfo() {

        mView.loadingShow();
        mModel.getUserInfo(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                UserEntity mUserEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY,str);
                    mUserEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    return;
                }
                if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                    PreferenceEntity.setUserInfoEntity(mUserEntity.data);
                    mView.getUserInfoSuccess(mUserEntity.data);
                } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {
                mView.loadingDissmis();
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        });
        return null;
    }

    public String delFamily(int id,int position) {

        mView.loadingShow();
        mModel.delFamily(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                UserEntity mUserEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mUserEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    return;
                }
                if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                    mView.delUserInfoState(true,position);
                } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {
                mView.loadingDissmis();
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        },id);
        return null;
    }

    public void addFamily(String name, String bir, String sex){
        mView.loadingShow();

//        mMap.put("birthday", PreferenceEntity.perfectInfoBirthday + " 00:00:00");
//        mMap.put("sex", mSex);
//        mMap.put("name", et_perinfo_name.getText().toString() );

        Map<String,String> mMap = new HashMap<>();
        mMap.put("name", name);
        mMap.put("birthday", bir);
        mMap.put("sex", sex);

        mModel.addFamily(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                UserEntity mUserEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mUserEntity = gson.fromJson(str, UserEntity.class);
                } catch (Exception e) {
                    return;
                }
                if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                    ToastUtils.showToast(mUserEntity.msg);
                    mView.addUserInfoState(true, "");
                } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {
                mView.loadingDissmis();
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        },mMap);
    }
}
