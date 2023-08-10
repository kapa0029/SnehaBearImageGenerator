package com.example.final_project_android;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the History table in the database.
 */
@Dao
public interface HistoryDAO {

    /**
     * Insert a new history entry into the database.
     *
     * @param messgeToInsert The history entry to be inserted.
     */
    @Insert
    void Insert(History messgeToInsert);

    /**
     * Retrieve a list of all history entries from the database.
     *
     * @return A list of history entries.
     */
    @Query("Select * from History")//SQL Statement
    public List<History> getAllHistory();

    /**
     * Retrieve the newest history entry from the database.
     *
     * @return The newest history entry.
     */
    @Query("SELECT * FROM History ORDER BY id DESC LIMIT 1")
    public History getNewestHistory();

    /**
     * Delete all history entries from the database.
     */
    @Query("DELETE FROM History")
    public void deleteAllHistory();

    /**
     * Delete a specific history entry from the database.
     *
     * @param messgeToDelete The history entry to be deleted.
     */
    @Delete
    void Delete(History messgeToDelete);
}
