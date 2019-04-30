package com.jemer.atong.net;

import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.net.service.HomeService;

/**
 * 3、RetrofitHelper
 */
public class RetrofitHelper {
    private static RetrofitHelper mHelper;
    //http://v.juhe.cn/weather/index?cityname=%E5%8C%97%E4%BA%AC&dtype=&format=&key=69edcefe693173e720380d6334d15e1f
    private String BASE_URL = UrlConstant.API_BASE;

    private RetrofitHelper() {
//        BASE_URL = constans.baseUrl;
    }

    public static RetrofitHelper getService() {
        if (mHelper == null) mHelper = new RetrofitHelper();
        return mHelper;
    }

    public HomeService getApi() {
        return RetrofitApi.getApiService(HomeService.class, BASE_URL);
    }

//    public CreateDynamicService getDynamicServiceApi() {
//        return RetrofitApi.getApiService(CreateDynamicService.class, BASE_URL);
//    }
}
