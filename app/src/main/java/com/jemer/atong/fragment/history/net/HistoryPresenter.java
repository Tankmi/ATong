package com.jemer.atong.fragment.history.net;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.eyesight.EyesightEntity;
import com.jemer.atong.entity.history.HistoryEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.model.BaseHttpEntity;

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
public class HistoryPresenter implements BasePresenter<HistoryView> {

    private HistoryModel mModel;
    private HistoryView mView;

    public HistoryPresenter() {
        mModel = new HistoryModel();
    }

    @Override
    public void attachView(HistoryView view) {
        this.mView = view;
        if(mModel == null) mModel = new HistoryModel();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    public void getHistoryData(int state,String userId){
        mView.loadingShow();

        if(StringUtils.isBlank(userId)){
            userId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ID,"");
            userId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY_USERID,"");
        }

        Map<String,String> mMap = new HashMap<>();
        mMap.put("current", "");
        mMap.put("rowSize", 10000 + "");
        mMap.put("customerFamilyId", userId);

        mModel.getHistoryData(state,new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                HistoryEntity mUserEntity;
                try {
                    String str = StringUtils.replaceJson(data.string());
                    mUserEntity = gson.fromJson(str, HistoryEntity.class);
                } catch (Exception e) {
                    return;
                }
                if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                    ToastUtils.showToast(mUserEntity.msg);
                    mView.getHisSuccess(mUserEntity.data,state);
                } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    mView.getHisSuccess(null,state);
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
