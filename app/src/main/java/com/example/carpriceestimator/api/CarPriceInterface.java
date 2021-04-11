package com.example.carpriceestimator.api;

import com.example.carpriceestimator.entity.PriceResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/***
 * Interface to use car price estimator api
 */
public interface CarPriceInterface {
    /**
     * This method will call the car price estimator api to get the price
     * @param make - Car make
     * @param name - Car name/model
     * @param year - Car year
     * @param odometer - Odometer value
     * @return - Call<PriceResult>
     */
    @GET("api/price")
    Call<PriceResult> getPrice(@Query("make") String make, @Query("name") String name, @Query("year") int year, @Query("odometer") int odometer);
}
