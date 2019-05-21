package com.jemer.atong.net.service;

import com.jemer.atong.context.UrlConstant;
import com.jemer.atong.context.UrlConstantRx;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/20 : 11:09
 * 描述：进行Retrofit网络请求接口的实例——发布动态
 */
public interface HomeService {

    //首页
    @POST(UrlConstantRx.API_HOMEDATAS)
    @FormUrlEncoded
    Observable<ResponseBody> getHomeDatas(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_BANNERDATAS)
    Observable<ResponseBody> getBannerDatas();

    @POST(UrlConstantRx.API_VERIFICATION)
    @FormUrlEncoded
    Observable<ResponseBody> getVerifyCode(@Field("phone") String phone, @Field("imei") String imei);

    @POST(UrlConstantRx.API_LOGIN)
    @FormUrlEncoded
    Observable<ResponseBody> login(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_CHANGE_PHONE)
    @FormUrlEncoded
    Observable<ResponseBody> changePhone(@FieldMap Map<String, String> map);


    @POST(UrlConstantRx.API_SYSISALL)
    @FormUrlEncoded
    Observable<ResponseBody> perfectInfo(@FieldMap Map<String, String> map);

    @GET(UrlConstantRx.GET_PERSONAL_CENTER)
    Observable<ResponseBody> getUserInfo();

    @POST(UrlConstantRx.API_UPDATEPERSONAL)
    @FormUrlEncoded
    Observable<ResponseBody> updateUserInfo(@FieldMap Map<String, String> map);

    //修改用户头像
    @Multipart
    @POST(UrlConstantRx.API_USER_CHANGEHEADER)
    Observable<ResponseBody> uploadingPicture(@Part MultipartBody.Part  file);

    @POST(UrlConstantRx.API_ADDFAMILY)
    @FormUrlEncoded
    Observable<ResponseBody> addFamily(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_DELFAMILY)
    @FormUrlEncoded
    Observable<ResponseBody> delFamily(@Field("id") int comment_id);

    @POST(UrlConstantRx.API_PUT_EYESIGHT_SHORT)
    @FormUrlEncoded
    Observable<ResponseBody> putEyesightShort(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_PUT_EYESIGHT_LONG)
    @FormUrlEncoded
    Observable<ResponseBody> putEyesightLong(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_PUT_EYESIGHT_HISTORY_SHORT)
    @FormUrlEncoded
    Observable<ResponseBody> getEyesightHisShort(@FieldMap Map<String, String> map);

    @POST(UrlConstantRx.API_PUT_EYESIGHT_HISTORY_LONG)
    @FormUrlEncoded
    Observable<ResponseBody> getEyesightHisLong(@FieldMap Map<String, String> map);


}
