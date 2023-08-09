package com.example.final_project_android;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * Database class that defines the Room database for storing scores.
 */
@Database(entities = {Score.class}, version = 1)
public abstract class ScoreDatabase extends RoomDatabase {

    /**
     * Abstract method to get the ScoreDao interface for database operations.
     *
     * @return The ScoreDao interface.
     */
    public abstract ScoreDao scoreDao();

    private static ScoreDatabase instance;

    /**
     * Get a singleton instance of the ScoreDatabase.
     *
     * @param context The application context.
     * @return The singleton instance of the ScoreDatabase.
     */
    public static synchronized ScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ScoreDatabase.class, "score_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
