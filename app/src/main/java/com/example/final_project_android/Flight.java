package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing flight information.
 * This class defines the structure of flight data to be stored in the database using Room persistence library.
 */
@Entity
public class Flight {


    /**
     * Primary key of the flight entity, auto-generated.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    /**
     * Destination of the flight.
     */
    @ColumnInfo(name="destination")
    String destination;

    /**
     * Terminal information for the flight.
     */
    @ColumnInfo(name="terminal")
    String terminal;


    /**
     * Gate information for the flight.
     */
    @ColumnInfo(name="gate")
    String gate;

    /**
     * Delay in minutes for the flight.
     */
    @ColumnInfo(name="delay")
    int delay;

    /**
     * Default constructor for Flight class.
     * Creates an empty Flight object.
     */
    public Flight(){}

    /**
     * Parameterized constructor for Flight class.
     * Initializes a Flight object with provided details.
     *
     * @param dest The destination of the flight.
     * @param t The terminal of the flight.
     * @param g The gate of the flight.
     * @param d The delay of the flight in minutes.
     */
    public Flight(String dest, String t, String g, int d){
        destination = dest;
        terminal = t;
        gate = g;
        delay = d;
    }

    /**
     * Retrieves the destination of the flight.
     *
     * @return The destination of the flight.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Retrieves the terminal of the flight.
     *
     * @return The terminal of the flight.
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * Retrieves the gate of the flight.
     *
     * @return The gate of the flight.
     */
    public String getGate() {
        return gate;
    }

    /**
     * Retrieves the delay of the flight in minutes.
     *
     * @return The delay of the flight.
     */
    public int getDelay() {
        return delay;
    }
}
