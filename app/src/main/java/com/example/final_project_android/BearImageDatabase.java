package com.example.final_project_android;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BearItemEntity.class}, version = 1)
public abstract class BearImageDatabase extends RoomDatabase {
    private static BearImageDatabase instance;

    public abstract BearItemDao bearDao();

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

