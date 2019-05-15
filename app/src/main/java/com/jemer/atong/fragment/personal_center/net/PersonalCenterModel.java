package com.jemer.atong.fragment.personal_center.net;

import com.jemer.atong.fragment.user.LoginController;
import com.jemer.atong.net.DefaultObserver;
import com.jemer.atong.net.RetrofitHelper;
import com.jemer.atong.net.model.BaseHttpEntity;
import com.jemer.atong.net.service.HomeService;

import java.io.File;
import java.util.Map;

import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;
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
public class PersonalCenterModel {
    HomeService service;

    public PersonalCenterModel() {
        service = RetrofitHelper.getService().getApi();
    }

    public void executePicture(final BaseHttpEntity<ResponseBody> entity, File file) {

//        String userId = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ID, "");
//
//        MediaType textType = MediaType.parse("text/plain");
//        RequestBody uId = RequestBody.create(textType, userId);

        //构建requestbody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-list;charset=UTF-8"), file);
        //将resquestbody封装为MultipartBody.Part对象
        MultipartBody.Part body = MultipartBody.Part.createFormData("header", file.getName(), requestFile);

        service.uploadingPicture( body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {
                        LOGUtils.LOG("getModel  onSuccess");
                        entity.onSuccess(data);
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
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

