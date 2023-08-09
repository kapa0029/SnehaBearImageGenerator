package com.example.final_project_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {History.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDAO HDAO();
}
