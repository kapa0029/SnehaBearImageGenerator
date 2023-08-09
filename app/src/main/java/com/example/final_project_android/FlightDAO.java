package com.example.final_project_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with flight data in the database.
 * This interface defines methods to perform database operations related to Flight entities.
 */
@Dao
public interface FlightDAO {

    /**
     * Inserts a flight record into the database.
     *
     * @param f The Flight object to be inserted.
     * @return The ID of the inserted flight.
     */
    @Insert
    public long insertFlight(Flight f);

    /**
     * Updates a flight record in the database.
     *
     * @param f The Flight object with updated information.
     * @return The number of rows affected by the update operation.
     */
    @Update
    public int uptadeFlight(Flight f);

    /**
     * Retrieves a list of all flights stored in the database.
     *
     * @return A list of Flight objects representing all flights in the database.
     */
    @Query("Select * from Flight")
    public List<Flight> getAllFlights();

    /**
     * Deletes a flight record from the database.
     *
     * @param f The Flight object to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    @Delete
    public int deleteFlight(Flight f);
}
