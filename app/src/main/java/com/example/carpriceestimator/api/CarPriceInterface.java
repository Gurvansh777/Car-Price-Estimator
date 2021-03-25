package com.example.carpriceestimator.api;

import com.example.carpriceestimator.entity.PriceResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarPriceInterface {
    @GET("api/price")
    Call<PriceResult> getPrice(@Query("make") String make, @Query("name") String name, @Query("year") int year, @Query("odometer") int odometer);
}
