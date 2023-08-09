package com.example.final_project_android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert
    void insertScore(Score score);

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    List<Score> getTopPlayers();
}
