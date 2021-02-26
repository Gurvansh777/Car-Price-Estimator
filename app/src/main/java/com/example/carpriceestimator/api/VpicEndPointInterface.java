package com.example.carpriceestimator.api;

import com.example.carpriceestimator.entity.Car;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VpicEndPointInterface {

    @GET("api/vehicles/decodevin/{vin}?format=json")
    Call<Car> getCar(@Path("vin") String vin);
}
