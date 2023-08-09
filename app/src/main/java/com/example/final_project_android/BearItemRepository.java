package com.example.final_project_android;import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BearItemRepository {
    private BearItemDao bearItemDao;
    private LiveData<List<BearItemEntity>> allBearItems;

    public BearItemRepository(Application application) {
        BearImageDatabase database = BearImageDatabase.getInstance(application);
        bearItemDao = database.bearDao();
        allBearItems = bearItemDao.getAllBearItems();
    }

    public LiveData<List<BearItemEntity>> getAllBearItems() {
        return allBearItems;
    }

    public void insert(BearItemEntity bearItem) {
        // Run the insertion on a background thread using an Executor or AsyncTask
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Insert the bearItem into the database through the BearItemDao
            bearItemDao.insert(bearItem);
        });
    }
    public LiveData<BearItemEntity> getBearItemBySize(int width, int height) {
        return bearItemDao.getBearItemBySize(width, height);
    }
    public void deleteBearImage(BearItemEntity bearItem) {
        // Similar to the insert method, you can perform the delete operation in a background thread
        bearItemDao.deleteBearImage(bearItem);
    }
}
