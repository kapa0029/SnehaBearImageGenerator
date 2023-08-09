package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a player's score and associated information in the app.
 */
@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "player_name")
    public String playerName;

    @ColumnInfo(name = "score")
    public int score;

    /**
     * Get the ID of the score entry.
     *
     * @return The score entry's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the score entry.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the player's name associated with the score.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Set the player's name associated with the score.
     *
     * @param playerName The player's name to set.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Get the score value.
     *
     * @return The score value.
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the score value.
     *
     * @param score The score value to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns a formatted string representation of the score, including player name and percentage.
     *
     * @return The formatted score representation.
     */
    @Override
    public String toString() {
        return getPlayerName() + ": " + getScore() + " percentage";
    }
}
