package com.jemer.atong.net.service;

import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.context.UrlConstantRx;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/20 : 11:09
 * 描述：进行Retrofit网络请求接口的实例——发布动态
 */
public interface HomeService {

    //获取首页内容
    @GET("main/mainPage.do")
    Observable<ResponseBody> getHomeChoiceness();


//    @GET(UrlConstantRx.API_VERIFICATION)
//    Observable<ResponseBody> getVerifyCode(@Query("phone") String phone,@Query("imei") String imei);

    @POST(UrlConstantRx.API_VERIFICATION)
    @FormUrlEncoded
    Observable<ResponseBody> getVerifyCode(@Field("phone") String phone, @Field("imei") String imei);

    @POST(UrlConstantRx.API_LOGIN)
    @FormUrlEncoded
    Observable<ResponseBody> login(@FieldMap Map<String, String> map);


    @POST(UrlConstantRx.API_SYSISALL)
    @FormUrlEncoded
    Observable<ResponseBody> perfectInfo(@FieldMap Map<String, String> map);

    @GET(UrlConstantRx.GET_PERSONAL_CENTER)
    Observable<ResponseBody> getUserInfo();

    @POST(UrlConstantRx.API_UPDATEPERSONAL)
    @FormUrlEncoded
    Observable<ResponseBody> updateUserInfo(@FieldMap Map<String, String> map);


}
