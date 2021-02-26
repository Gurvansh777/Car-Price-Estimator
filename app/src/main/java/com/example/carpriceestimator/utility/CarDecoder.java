package com.example.carpriceestimator.utility;

import android.widget.Toast;

import com.example.carpriceestimator.entity.Car;
import com.example.carpriceestimator.entity.CarResult;
import com.example.carpriceestimator.entity.DecodedCar;
import com.example.carpriceestimator.ui.home.HomeFragment;

import java.util.List;

public class CarDecoder {
    private static DecodedCar decodedCar;

    public static DecodedCar decode(Car car) {
        decodedCar = new DecodedCar();
        List<CarResult> carResults = car.getResults();
        decodedCar.setVin(car.getSearchCriteria());
        for (CarResult carResult : carResults) {
            switch (carResult.getVariable()) {
                case "Make":
                    decodedCar.setMake(carResult.getValue().toString());
                    break;

                case "Body Class":
                    decodedCar.setBodyClass(carResult.getValue().toString());
                    break;

                case "Manufacturer Name":
                    decodedCar.setManufactureName(carResult.getValue().toString());
                    break;

                case "Model":
                    decodedCar.setModel(carResult.getValue().toString());
                    break;

                case "Model Year":
                    decodedCar.setModelYear(carResult.getValue().toString());
                    break;

                case "Doors":
                    decodedCar.setDoors(Integer.parseInt(carResult.getValue().toString()));
                    break;
            }
        }


        return decodedCar;
    }
}
