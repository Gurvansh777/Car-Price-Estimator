package com.example.carpriceestimator.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.carpriceestimator.entity.DecodedCar;

import java.util.List;

/**
 * Cars repository which helps to call room database methods
 */
public class CarsRepository {
    private CarDao carDao;
    private LiveData<List<DecodedCar>> allDecodedCars;

    public CarsRepository(Application application, String userEmailAddress) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        carDao = appDatabase.carDao();
        allDecodedCars = carDao.getAll(userEmailAddress);
    }

    /**
     * This method gets all the decoded cars
     * @return - Live data of decoded cars
     */
    public LiveData<List<DecodedCar>> getAllDecodedCars() {
        return allDecodedCars;
    }

    /**
     * Method to insert a new car
     * @param car
     */
    public void insert(DecodedCar car) {
        new insertAsyncTask(carDao).execute(car);
    }

    /**
     * Method to delete cars based on user settings
     * @param recentRecords
     */
    public void deleteNotRecentCars(int recentRecords) {
        new deleteAsyncTask(carDao).execute(recentRecords);
    }

    /**
     * Inner class for multi threading
     */
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

    /**
     * Inner class for multi threading
     */
    private class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CarDao carDao;

        public deleteAsyncTask(CarDao carDao) {
            this.carDao = carDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            this.carDao.deleteNotRecentCars(integers[0]);
            return null;
        }
    }

}
