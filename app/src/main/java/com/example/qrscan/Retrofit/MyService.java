package com.example.qrscan.Retrofit;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {
    @POST("users")
    @FormUrlEncoded
    Call<ResponseBody> registerUser(@Field("username") String username,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> loginUser(@Field("username") String username,
                                 @Field("password") String password);
}
