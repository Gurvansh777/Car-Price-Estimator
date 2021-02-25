package com.example.carpriceestimator.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carpriceestimator.db.CarsRepository;
import com.example.carpriceestimator.entity.DecodedCar;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<DecodedCar>> decodedCarsList;
    private CarsRepository carsRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        carsRepository = new CarsRepository(application);
        decodedCarsList = carsRepository.getAllDecodedCars();
    }

    LiveData<List<DecodedCar>> getDecodedCarsList() { return this.decodedCarsList; }

    public void insert(DecodedCar decodedCar) { carsRepository.insert(decodedCar); }

}