package com.example.final_project_android;

import android.content.Context;
import android.sax.RootElement;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Score.class}, version=1)
public abstract class ScoreDatabase extends RoomDatabase {

    public abstract ScoreDao scoreDao();

    private static ScoreDatabase instance;

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
