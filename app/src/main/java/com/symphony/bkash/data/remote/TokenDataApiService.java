package com.symphony.bkash.data.remote;


import com.symphony.bkash.data.model.PostResponse;
import com.symphony.bkash.data.model.UpdateResponse;
import com.symphony.bkash.data.model.PostInfo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface TokenDataApiService {
    @POST("api/bKashStore")
    Call<PostInfo> postJob(@Body RequestBody params);

    @POST("api/bKashStore")
    @FormUrlEncoded
    Call<PostResponse> saveInfo(@Field("IMEI1") String imei1,
                                @Field("IMEI2") String imei2,
                                @Field("MAC") String mac,
                                @Field("ANDROID_ID") String android_id,
                                @Field("sim1") String sim1,
                                @Field("sim2") String sim2,
                                @Field("Activated") String activated,
                                @Field("Model") String model);

    @POST("api/bKashUpdate/{id}")
    Call<UpdateResponse> updateInfo(@Path("id") long id,
                                    @Body PostInfo postInfo);

    @GET("api/{imei}")
    Call<PostInfo> getInfo(@Path("imei") String imei);
}
