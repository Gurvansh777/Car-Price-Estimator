package com.example.carpriceestimator.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.carpriceestimator.entity.DecodedCar;

import java.util.List;

/**
 * Data access object to interact with SQLite database
 */
@Dao
public interface CarDao {
    /**
     * Insert a new car
     * @param decodedCar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCar(DecodedCar decodedCar);

    /**
     * Get all cars based on email address
     * @param emailAddress
     * @return List of decoded car
     */
    @Query("SELECT * FROM car WHERE userEmailAddress = :emailAddress ORDER BY rowid DESC")
    LiveData<List<DecodedCar>> getAll(String emailAddress);

    /**
     * Delete extra cars based on user settings
     * @param recentRecords
     */
    @Query("DELETE FROM car WHERE rowid NOT IN (SELECT rowid FROM car ORDER BY rowid DESC LIMIT :recentRecords)")
    void deleteNotRecentCars(int recentRecords);
}
