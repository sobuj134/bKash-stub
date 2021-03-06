package com.symphony.bkashg.data.remote;


import com.symphony.bkashg.data.model.InfoResponse;
import com.symphony.bkashg.data.model.PostResponse;
import com.symphony.bkashg.data.model.PostResponseEdisonBokachoda;
import com.symphony.bkashg.data.model.UpdateResponse;
import com.symphony.bkashg.data.model.PostInfo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;


public interface TokenDataApiService {


    @POST("api/bKashStore")
    Call<PostInfo> postJob(@Body RequestBody params);

    @POST("v1")
    @FormUrlEncoded
    Call<PostResponse> saveInfo(
            @Header ("Authorization") String token,
            @Field("IMEI1") String imei1,
            @Field("IMEI2") String imei2,
            @Field("MAC") String mac,
            @Field("ANDROID_ID") String android_id,
            @Field("sim1") String sim1,
            @Field("sim2") String sim2,
            @Field("Activated") String activated,
            @Field("Model") String model);

    @POST("v1/{id}")
    Call<UpdateResponse> updateInfo(
            @Header ("Authorization") String token,
            @Path("id") long id,
            @Body PostInfo postInfo);

    @POST
    @FormUrlEncoded
    Call<PostResponse> saveInfoLocal(@Url String url,
            @Field("IMEI1") String imei1,
            @Field("IMEI2") String imei2,
            @Field("MAC") String mac,
            @Field("ANDROID_ID") String android_id,
            @Field("sim1") String sim1,
            @Field("sim2") String sim2,
            @Field("Activated") String activated,
            @Field("Model") String model);

    @POST
    Call<UpdateResponse> updateInfoLocal(
	@Url String url,
            @Body PostInfo postInfo);

    @GET("api/{imei}")
    Call<InfoResponse> getInfo(@Path("imei") String imei);


    @POST
    @FormUrlEncoded
    Call<PostResponseEdisonBokachoda> saveInfoEdison(@Url String url,
                                                    @Field("IMEI1") String imei1,
                                                    @Field("IMEI2") String imei2,
                                                    @Field("MAC") String mac,
                                                    @Field("ANDROID_ID") String android_id,
                                                    @Field("sim1") String sim1,
                                                    @Field("sim2") String sim2,
                                                    @Field("Activated") String activated,
                                                    @Field("Model") String model);
}
