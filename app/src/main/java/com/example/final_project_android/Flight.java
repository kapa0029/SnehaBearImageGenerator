package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Flight {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="destination")
    String destination;

    @ColumnInfo(name="terminal")
    String terminal;

    @ColumnInfo(name="gate")
    String gate;

    @ColumnInfo(name="delay")
    int delay;

    public Flight(){}

    public Flight(String dest, String t, String g, int d){
        destination = dest;
        terminal = t;
        gate = g;
        delay = d;
    }

    public String getDestination() {
        return destination;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getGate() {
        return gate;
    }

    public int getDelay() {
        return delay;
    }
}
