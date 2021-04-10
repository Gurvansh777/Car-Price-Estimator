package com.example.carpriceestimator.ui.recent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carpriceestimator.db.CarsRepository;
import com.example.carpriceestimator.entity.DecodedCar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RecentViewModel extends AndroidViewModel {
    private LiveData<List<DecodedCar>> decodedCarsList;
    private CarsRepository carsRepository;

    public RecentViewModel(@NonNull Application application) {
        super(application);
        carsRepository = new CarsRepository(application, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        decodedCarsList = carsRepository.getAllDecodedCars();
    }

    LiveData<List<DecodedCar>> getDecodedCarsList() {
        return this.decodedCarsList;
    }
}