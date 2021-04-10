package com.example.carpriceestimator.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.entity.DecodedCar;

@Database(entities = {DecodedCar.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CarDao carDao();

    public static AppDatabase INSTANCE = null;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        Constants.DATABASE_NAME
                ).build();
            }
        }
        return INSTANCE;
    }
}
