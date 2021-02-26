package com.example.carpriceestimator.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.carpriceestimator.entity.DecodedCar;

import java.util.List;

public class CarsRepository {
    private CarDao carDao;
    private LiveData<List<DecodedCar>> allDecodedCars;

    public CarsRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        carDao = appDatabase.carDao();
        allDecodedCars = carDao.getAll();
    }

    public LiveData<List<DecodedCar>> getAllDecodedCars() {
        return allDecodedCars;
    }

    public void insert (DecodedCar car) {
        new insertAsyncTask(carDao).execute(car);
    }

    private class insertAsyncTask extends AsyncTask<DecodedCar, Void, Void> {
        private CarDao carDao;

        public insertAsyncTask(CarDao carDao) {
            this.carDao = carDao;
        }

        @Override
        protected Void doInBackground(DecodedCar... decodedCars) {
            this.carDao.insertCar(decodedCars[0]);
            return null;
        }
    }
}
