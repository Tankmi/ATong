package com.jemer.atong.fragment.eyesight.net;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.eyesight.EyesightEntity;
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
public class EyesightPresenter implements BasePresenter<EyesightView> {

    private EyesightModel mModel;
    private EyesightView mView;

    public EyesightPresenter() {
        mModel = new EyesightModel();
    }

    @Override
    public void attachView(EyesightView view) {
        this.mView = view;
        if(mModel == null) mModel = new EyesightModel();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    public void putEyesightData(int state, String lefteye,String righteye){
        mView.loadingShow();

        String userId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY_USERID,"");
        if(StringUtils.isBlank(userId)){
            userId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ID,"");
        }

        Map<String,String> mMap = new HashMap<>();
        mMap.put("lefteye", lefteye);
        mMap.put("righteye", righteye);
        mMap.put("customerFamilyId", userId);

        mModel.putEyesightData(state,new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                EyesightEntity mUserEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mUserEntity = gson.fromJson(str, EyesightEntity.class);
                } catch (Exception e) {
                    return;
                }
                if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                    ToastUtils.showToast(mUserEntity.msg);
                    mView.putEyesightSuccess(mUserEntity.data);
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
