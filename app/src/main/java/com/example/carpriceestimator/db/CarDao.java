package com.example.carpriceestimator.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.carpriceestimator.entity.DecodedCar;

import java.util.List;

@Dao
public interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCar(DecodedCar decodedCar);

    @Query("SELECT * FROM car WHERE userEmailAddress = :emailAddress ORDER BY rowid DESC")
    LiveData<List<DecodedCar>> getAll(String emailAddress);

    @Query("DELETE FROM car WHERE rowid NOT IN (SELECT rowid FROM car ORDER BY rowid DESC LIMIT :recentRecords)")
    void deleteNotRecentCars(int recentRecords);
}
