package com.example.final_project_android;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface HistoryDAO {

    @Insert
    void Insert(History messgeToInsert);


    @Query("Select * from History")//SQL Statement
    public List<History> getAllHistory();

    @Query("SELECT * FROM History ORDER BY id DESC LIMIT 1")
    public History getNewestHistory();

    @Query("DELETE FROM History")
    public void deleteAllHistory();

    @Delete
    void Delete(History messgeToDelete);
}
