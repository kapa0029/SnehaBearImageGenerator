package com.example.final_project_android;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) for interacting with the bear item entity in the database.
 */
@Dao
public interface BearItemDao {
    /**
     * Insert a new bear item into the database.
     *
     * @param bearItem The bear item to insert.
     */
    @Insert
    void insert(BearItemEntity bearItem);

    /**
     * Retrieve all bear items from the database.
     *
     * @return A LiveData list of all bear items.
     */
    @Query("SELECT * FROM bear_table")
    LiveData<List<BearItemEntity>> getAllBearItems();

    /**
     * Delete a bear image from the database.
     *
     * @param bearItem The bear item to delete.
     */
    @Delete
    void deleteBearImage(BearItemEntity bearItem);

    /**
     * Retrieve a bear item from the database based on the provided width and height.
     *
     * @param width  The width of the bear image.
     * @param height The height of the bear image.
     * @return A LiveData containing the bear item with the specified dimensions.
     */
    @Query("SELECT * FROM bear_table WHERE width = :width AND height = :height LIMIT 1")
    LiveData<BearItemEntity> getBearItemBySize(int width, int height);
}

