package com.symphony.bkash.data.remote;


import com.symphony.bkash.data.model.BkashResponse;
import com.symphony.bkash.data.model.PostInfo;
import com.symphony.bkash.data.model.PostUserInfo;
import com.symphony.bkash.data.model.UpdateUserInfo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface TokenDataApiService {
    @POST("api/bKashStore")
    Call<PostInfo> postJob(@Body RequestBody params);

    @POST("api/bKashStore")
    @FormUrlEncoded
    Call<PostUserInfo> saveInfo(@Field("IMEI1") String imei1,
                                @Field("IMEI2") String imei2,
                                @Field("MAC") String mac,
                                @Field("ANDROID_ID") String android_id,
                                @Field("SIM_Number") String sim_number,
                                @Field("Activated") String activated,
                                @Field("Model") String model);

    @POST("api/bKashUpdate/{id}")
    Call<BkashResponse> updateInfo(@Path("id") long id,
                                   @Body PostInfo postInfo);
}
