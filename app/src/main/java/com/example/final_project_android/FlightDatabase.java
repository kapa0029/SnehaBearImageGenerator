package com.example.final_project_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    public abstract FlightDAO fDAO();
}
