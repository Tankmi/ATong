package com.jemer.atong.net.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/20 : 11:09
 * 描述：进行Retrofit网络请求接口的实例——发布动态
 */
public interface HomeService {

    //获取首页内容
    @GET("main/mainPage.do")
    Observable<ResponseBody> getHomeChoiceness();
//    Observable<ResponseBody> getHomeChoiceness(@HeaderMap Map<String, String> map);

    //@path 用于匹配大括号中的缺省值
//    @GET("weather/{city_name}")
//    Call<ResponseBody> getWeather(@Path("city_name") String cityName);

    //@Query  用于在域名后面添加请求值（key-map形式）
//    @GET("weather")
//    Call<ResponseBody> getWeather(@Query("city") String cityName);

//    @GET("weather")
//    Call<ResponseBody> getWeather(@QueryMap() Map<String,String> map);

    //情形 http://192.168.0.1/comment
    //body参数：{"comment_id":"1","content":"我是评论","user_id":"1001"}
//    @FormUrlEncoded
//    @POST("comment")
//    Observable<ResponseBody> createDynamic(@Field("comment_id") String comment_id, @Field("content") String content, @Field("user_id") String user_id);

//    @FormUrlEncoded
//    @POST("comment")
//    Observable<ResponseBody> createDynamic(@FieldMap() Map<String,String> map);

//    @POST("comment")
//    Observable<ResponseBody> createDynamic(@Body Object requestBean);
//
//    @POST("comment")
//    Observable<ResponseBody> createDynamic(@Body List<Object> requestBean);

//    @POST("upload/")
//    Observable<ResponseBody> createDynamic(@Body RequestBody requestBody);
}
