package com.ochiengotieno304.qrscan.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl("https://yulu.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return instance;
    }
}
