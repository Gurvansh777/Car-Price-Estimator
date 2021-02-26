package com.example.carpriceestimator.api;

import com.example.carpriceestimator.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static Retrofit INSTANCE = null;
    private RetrofitBuilder(){}

    public static Retrofit getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}
