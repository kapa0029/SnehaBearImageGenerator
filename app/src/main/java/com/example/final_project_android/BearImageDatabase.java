package com.example.final_project_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * The Room database class that represents the bear image database.
 */
@Database(entities = {BearItemEntity.class}, version = 1)
public abstract class BearImageDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static BearImageDatabase instance;

    /**
     * Returns the DAO (Data Access Object) for bear image database operations.
     *
     * @return The BearItemDao instance.
     */
    public abstract BearItemDao bearDao();

    /**
     * Returns the singleton instance of the database. If the instance doesn't exist, it creates one.
     *
     * @param context The application context.
     * @return The singleton instance of BearImageDatabase.
     */
    public static synchronized BearImageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BearImageDatabase.class, "bear_image_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
