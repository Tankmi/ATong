package com.jemer.atong.fragment.home.net;

import com.google.gson.Gson;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.home.BannerEntity;
import com.jemer.atong.entity.home.HomeEntity;
import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.io.IOException;
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
public class HomePresenter implements BasePresenter<HomeView> {

    private HomeModel mModel;
    private HomeView mView;

    private int current = 1,rowSize = 8;

    public HomePresenter() {
        mModel = new HomeModel();
    }

    @Override
    public void attachView(HomeView view) {
        this.mView = view;
        if(mModel == null) mModel = new HomeModel();
    }

    @Override
    public void detachView() {
        if(mView != null) mView = null;
        if(mModel != null) mModel = null;
    }

    @Override
    public void getNetData() {

    }

    /**
     *
     * @param searchData
     * @param state 1,下拉刷新，2，加载更多
     * @return
     */
    public String getListData(String searchData,int state) {
        if(state == 1){
            current = 1;
        }else{
            current++;
        }
        Map<String,String> mMap = new HashMap<>();
        mMap.put("current", current + "");
        mMap.put("rowSize", rowSize + "");
      if(!StringUtils.isBlank(searchData))   mMap.put("keywords", searchData);

        mView.loadingShow();
        mModel.getListData(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                HomeEntity mEntity;
                String str;
                try {
                    str = StringUtils.replaceJson(data.string());
                    mEntity = gson.fromJson(str, HomeEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    if(state == 1){
                        PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_CACHE_HOME,str);
                    }
                    mView.getListDataSuccess(state,mEntity.data.list);
                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    if(state == 2) current--;
                    else if(state ==1 && !StringUtils.isBlank(searchData)) ToastUtils.showToast("暂无与  “" + searchData +"”  相关的数据");
                    mView.getListDataSuccess(state, null);
//                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {

                String str = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_BANNER);
                if(!StringUtils.isBlank(str)){
                    Gson gson = new Gson();
                    BannerEntity mEntity;
                    try {
                        mEntity = gson.fromJson(str, BannerEntity.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                        mView.getListDataSuccess(state,mEntity.data.list);
                    }
                }else {
                    mView.loadingDissmis();
                    mView.getListDataFailed(state, searchData);
                }
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        }, mMap);
        return null;
    }

    public String getBannerData() {

        mView.loadingShow();
        mModel.getBannerData(new BaseHttpEntity<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mView.loadingDissmis();
                Gson gson = new Gson();
                BannerEntity mEntity;
                String str;
                try {
                    str = StringUtils.replaceJson(data.string());
                    mEntity = gson.fromJson(str, BannerEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_CACHE_BANNER, str);
                    mView.getBannerData(true,mEntity.data.list);
                } else if (mEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                    mView.loginOut();
                } else {
                    mView.getBannerData(false,null);
//                    ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mEntity.msg, "接口信息异常！"));
                }
            }

            @Override
            public void onError(String error) {
                mView.loadingDissmis();
                String str = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_BANNER);

                if(!StringUtils.isBlank(str)){
                    Gson gson = new Gson();
                    BannerEntity mEntity;
                    try {
                        mEntity = gson.fromJson(str, BannerEntity.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                        mView.getBannerData(true,mEntity.data.list);
                    }
                }else mView.getBannerData(false,null);
            }

            @Override
            public void onFinish() {
                mView.loadingDissmis();

            }
        });
        return null;
    }

}
