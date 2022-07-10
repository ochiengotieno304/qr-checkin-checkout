package com.example.qrscan.Retrofit;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {
    @POST("/users")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("username") String username,
                                    @Field("password") String password);

    @POST("/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("username") String username,
                                 @Field("password") String password);
}
