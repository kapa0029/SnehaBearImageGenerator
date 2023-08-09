package com.example.final_project_android;
import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {

    private static ScoreDatabase scoreDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        scoreDatabase = Room.databaseBuilder(this, ScoreDatabase.class, "score_database")
                .build();
    }

    public static ScoreDatabase getScoreDatabase() {
        return scoreDatabase;
    }
}
