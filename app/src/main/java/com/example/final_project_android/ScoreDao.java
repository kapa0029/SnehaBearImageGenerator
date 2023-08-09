package com.example.final_project_android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
/**
 * Data Access Object (DAO) interface for performing database operations related to scores.
 */
public interface ScoreDao {

    @Insert
    /**
     * Insert a score into the database.
     *
     * @param score The Score object to be inserted.
     */
    void insertScore(Score score);

    /**
     * Retrieve a list of top players' scores from the database.
     *
     * @return A list of top players' scores, ordered by score in descending order.
     */
    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    List<Score> getTopPlayers();
}
