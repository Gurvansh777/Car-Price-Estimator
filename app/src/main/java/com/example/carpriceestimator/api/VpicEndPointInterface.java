package com.example.carpriceestimator.api;

import com.example.carpriceestimator.entity.Car;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
/***
 * Interface to use vpic api
 */
public interface VpicEndPointInterface {

    /**
     * Method to get car details through vpic api
     * @param vin - Car VIN
     * @return = Call<Car>
     */
    @GET("api/vehicles/decodevin/{vin}?format=json")
    Call<Car> getCar(@Path("vin") String vin);
}
