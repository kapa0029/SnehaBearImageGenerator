package com.example.final_project_android;

import android.app.Application;

import androidx.room.Room;

import com.example.final_project_android.ScoreDatabase;

/**
 * Custom Application class for initializing the ScoreDatabase instance.
 */
public class MyApplication extends Application {

    private static ScoreDatabase scoreDatabase;

    /**
     * Called when the application is first created. Initializes the ScoreDatabase instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the ScoreDatabase using Room's databaseBuilder
        scoreDatabase = Room.databaseBuilder(this, ScoreDatabase.class, "score_database")
                .build();
    }

    /**
     * Get the instance of the ScoreDatabase.
     *
     * @return The ScoreDatabase instance.
     */
    public static ScoreDatabase getScoreDatabase() {
        return scoreDatabase;
    }
}
