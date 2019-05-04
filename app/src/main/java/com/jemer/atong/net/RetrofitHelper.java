package com.jemer.atong.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.net.service.HomeService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 3、RetrofitHelper
 */
public class RetrofitHelper {
    private static RetrofitHelper mHelper;
    //http://v.juhe.cn/weather/index?cityname=%E5%8C%97%E4%BA%AC&dtype=&format=&key=69edcefe693173e720380d6334d15e1f
    private static String BASE_URL = UrlConstant.API_BASE;
    private static Retrofit retrofit;

    private RetrofitHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkhttpHelper.getOkHttpHelper().getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitHelper getService() {
        if (mHelper == null) mHelper = new RetrofitHelper();
        return mHelper;
    }

    public HomeService getApi() {
        return getApiService(HomeService.class);
    }


    public static <T> T getApiService(Class<T> tClass) {
        return retrofit.create(tClass);
    }
}
