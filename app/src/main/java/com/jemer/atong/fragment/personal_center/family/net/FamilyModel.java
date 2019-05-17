package com.jemer.atong.fragment.personal_center.family.net;

import com.jemer.atong.net.DefaultObserver;
import com.jemer.atong.net.RetrofitHelper;
import com.jemer.atong.net.model.BaseHttpEntity;
import com.jemer.atong.net.service.HomeService;

import java.io.File;
import java.util.Map;

import huitx.libztframework.utils.LOGUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:21
 * 描述：
 */
public class FamilyModel {
    HomeService service;

    public FamilyModel() {
        service = RetrofitHelper.getService().getApi();
    }

    public void getUserInfo(BaseHttpEntity<ResponseBody> entity) {
        service.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {
                        LOGUtils.LOG("getModel  onSuccess");
                        entity.onSuccess(data);
                        entity.onFinish();
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
                        entity.onFinish();
                        entity.onError(error);
                    }

                    @Override
                    public void onFinish() {
                        LOGUtils.LOG("getModel  onFinish");
                        entity.onFinish();
                    }
                });

    }

    public void addFamily(BaseHttpEntity<ResponseBody> entity, Map<String, String> map) {
        service.addFamily(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {
                        LOGUtils.LOG("getModel  onSuccess");
                        entity.onSuccess(data);
                        entity.onFinish();
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
                        entity.onFinish();
                        entity.onError(error);
                    }

                    @Override
                    public void onFinish() {
                        LOGUtils.LOG("getModel  onFinish");
                        entity.onFinish();
                    }
                });

    }

    public void delFamily(BaseHttpEntity<ResponseBody> entity, int id) {
        service.delFamily(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {
                        LOGUtils.LOG("getModel  onSuccess");
                        entity.onSuccess(data);
                        entity.onFinish();
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
                        entity.onFinish();
                        entity.onError(error);
                    }

                    @Override
                    public void onFinish() {
                        LOGUtils.LOG("getModel  onFinish");
                        entity.onFinish();
                    }
                });

    }

}

