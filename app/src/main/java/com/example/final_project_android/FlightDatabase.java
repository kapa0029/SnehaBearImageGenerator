package com.example.final_project_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for managing flight data using the Room persistence library.
 * This class defines the database configuration and provides access to the FlightDAO interface.
 */
@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    /**
     * Retrieves an instance of the FlightDAO interface for database operations.
     *
     * @return An instance of the FlightDAO interface.
     */
    public abstract FlightDAO fDAO();
}
