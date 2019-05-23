package com.jemer.atong.net;

import java.net.ConnectException;

import huitx.libztframework.utils.LayoutUtil;
import huitx.libztframework.utils.NetUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DefaultObserver<T> implements Observer<T> {
    private Disposable mDisposable;
    private String ERROR_TOAST ="";

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        ClearDisposable.getInstance().getCompositeDisposable().add(d);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        if(!NetUtils.isAPNType()){
            onError("");
            onFinish();
        } else if (e instanceof ConnectException){
            ERROR_TOAST="连接超时，请稍后重试！";
            onError(ERROR_TOAST);
            onFinish();
        }else {
            onError(e + e.getMessage());
            onFinish();
        }
    }

    @Override
    public void onComplete() {
        if (!mDisposable.isDisposed()) mDisposable.dispose();  //取消订阅
    }


    public abstract void onSuccess(T data);

    public abstract void onError(String error);

    public abstract void onFinish();
}
