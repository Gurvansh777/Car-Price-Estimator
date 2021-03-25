package com.example.carpriceestimator.api;

import com.example.carpriceestimator.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static Retrofit INSTANCE = null;
    public static Retrofit INSTANCE_PRICE = null;
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

    public static Retrofit getPriceInstance(){
        if (INSTANCE_PRICE == null) {
            INSTANCE_PRICE = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE_PRICE;
    }
}
