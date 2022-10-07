package com.ochiengotieno304.qrscan.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IMyService {
    @POST("users")
    @FormUrlEncoded
    Call<ResponseBody> registerUser(@Field("username") String username,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> loginUser(@Field("username") String username,
                                 @Field("password") String password);

    @PUT("check_in")
    Call<ResponseBody> checkIn(@Header("Authorization") String token);

    @PUT("check_out")
    Call<ResponseBody> checkOut(@Header("Authorization") String token);
}
