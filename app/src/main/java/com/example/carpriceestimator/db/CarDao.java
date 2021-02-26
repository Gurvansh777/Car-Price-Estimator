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

    @Query("SELECT * FROM car ORDER BY rowid DESC")
    LiveData<List<DecodedCar>> getAll();
}
