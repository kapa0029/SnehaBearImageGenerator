package com.example.final_project_android;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Repository class that handles interactions with the bear image database and provides data to the ViewModel.
 */
public class BearItemRepository {
    /** The DAO (Data Access Object) for bear image database operations. */
    private BearItemDao bearItemDao;

    /** LiveData containing a list of all bear items from the database. */
    private LiveData<List<BearItemEntity>> allBearItems;

    /**
     * Constructs a new BearItemRepository instance.
     *
     * @param application The Application context.
     */
    public BearItemRepository(Application application) {
        BearImageDatabase database = BearImageDatabase.getInstance(application);
        bearItemDao = database.bearDao();
        allBearItems = bearItemDao.getAllBearItems();
    }

    /**
     * Retrieves LiveData containing a list of all bear items.
     *
     * @return LiveData containing a list of all bear items.
     */
    public LiveData<List<BearItemEntity>> getAllBearItems() {
        return allBearItems;
    }

    /**
     * Inserts a bear item into the database.
     *
     * @param bearItem The BearItemEntity to be inserted.
     */
    public void insert(BearItemEntity bearItem) {
        // Run the insertion on a background thread using an Executor
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Insert the bearItem into the database through the BearItemDao
            bearItemDao.insert(bearItem);
        });
    }

    /**
     * Retrieves LiveData containing a bear item based on the provided width and height.
     *
     * @param width  The width of the bear item.
     * @param height The height of the bear item.
     * @return LiveData containing the bear item with the specified width and height.
     */
    public LiveData<BearItemEntity> getBearItemBySize(int width, int height) {
        return bearItemDao.getBearItemBySize(width, height);
    }

    /**
     * Deletes a bear item from the database.
     *
     * @param bearItem The BearItemEntity to be deleted.
     */
    public void deleteBearImage(BearItemEntity bearItem) {
        // Perform the delete operation in a background thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            bearItemDao.deleteBearImage(bearItem);
        });
    }
}
