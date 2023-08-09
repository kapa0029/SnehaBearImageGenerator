package com.example.final_project_android;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BearItemDao {
    @Insert
    public void insert(BearItemEntity bearItem);

    @Query("SELECT * FROM bear_table")
    public LiveData<List<BearItemEntity>> getAllBearItems();

    @Delete
    public void deleteBearImage(BearItemEntity bearItem);

    @Query("SELECT * FROM bear_table WHERE width = :width AND height = :height LIMIT 1")
    LiveData<BearItemEntity> getBearItemBySize(int width, int height);

}

