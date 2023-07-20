package com.example.final_project_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlightDAO {

    @Insert
    public long insertFlight(Flight f);

    @Update
    public int uptadeFlight(Flight f);

    @Query("Select * from Flight")
    public List<Flight> getAllFlights();

    @Delete
    public int deleteFlight(Flight f);
}
