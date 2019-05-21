package com.jemer.atong.fragment.history.net;

import com.jemer.atong.net.DefaultObserver;
import com.jemer.atong.net.RetrofitHelper;
import com.jemer.atong.net.model.BaseHttpEntity;
import com.jemer.atong.net.service.HomeService;

import java.util.Map;

import huitx.libztframework.utils.LOGUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:21
 * 描述：
 */
public class HistoryModel {
    HomeService service;

    public HistoryModel() {
        service = RetrofitHelper.getService().getApi();
    }

    public void getHistoryData(int state, BaseHttpEntity<ResponseBody> entity, Map<String, String> map) {
        if (state == 1) {   //近视
            service.getEyesightHisShort(map)
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
        } else {
            service.getEyesightHisLong(map)
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

}

