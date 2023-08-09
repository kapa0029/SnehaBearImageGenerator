package com.example.final_project_android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.databinding.ActivityBearBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BearSavedImagesActivity extends AppCompatActivity implements MyBearAdapter.OnItemClickListener, MyBearAdapter.OnItemLongClickListener {
    ActivityBearBinding binding;
    private RecyclerView recyclerView;
    private MyBearAdapter adapter;
    private List<BearItemEntity> bearItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_bear_saved_images);

        recyclerView = findViewById(R.id.recyclerViewSavedImages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String actionBarSubTitle = getResources().getString(R.string.actionbar_subtitle_saved_images);
        String actionBarTitle = getResources().getString(R.string.actionbar_title_generator);

        Toolbar toolbar = findViewById(R.id.bearToolbar); // Find the Toolbar by its ID
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setSubtitle(actionBarSubTitle);

        // Retrieve bearItems from the database using the BearItemRepository
        BearItemRepository bearItemRepository = new BearItemRepository(getApplication());
        bearItemRepository.getAllBearItems().observe(this, bearItems -> {
            this.bearItems = bearItems; // Update the local list of bearItems

            // Initialize the adapter with the retrieved bearItems and bearItemRepository
            adapter = new MyBearAdapter(getApplicationContext(), bearItems, bearItemRepository);
            adapter.setOnItemClickListener(this); // Set the click listener for RecyclerView items
            adapter.setOnItemLongClickListener(this); // Set the long click listener for RecyclerView items
            recyclerView.setAdapter(adapter); // Set the adapter to the RecyclerView
        });
    }

    @Override
    public void onItemClick(int position) {
        // Check if the bearItems list is null or empty
        if (bearItems == null || bearItems.isEmpty()) {
            return; // Return early to avoid a crash
        }
        // Get the selected bear item from the list
        BearItemEntity selectedBearItem = bearItems.get(position);

        // Load the BearImageDetailsFragment with the selected bear item data
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BearImageDetailsFragment fragment = BearImageDetailsFragment.newInstance(
                selectedBearItem.getWidth(),
                selectedBearItem.getHeight(),
                selectedBearItem.getImage()
        );

        // Replace the current fragment with the BearImageDetailsFragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add to the back stack for back navigation
        fragmentTransaction.commit();
    }

    @Override
    public void onItemLongClick(int position) {
        String alertTitle = getResources().getString(R.string.alert_title_delete_image);
        String alertSubTitle = getResources().getString(R.string.alert_subtitle_delete_image);
        String imageDeletedText = getResources().getString(R.string.image_deleted_text);
        // Show the AlertDialog for confirmation
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(alertSubTitle)
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the item from the database and update the RecyclerView
                    adapter.deleteItem(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, imageDeletedText, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.helpBear) {
            showAlertDialog();
            return true;
        }
        // Handle other menu items if needed
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bear, menu);
        return true;
    }
    private void showAlertDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.how_to_use))
                .setMessage(getResources().getString(R.string.how_to_use_help))
                .setPositiveButton("OK", (dialog, which) -> {
                    // Do something when the user clicks the positive button (OK)
                    dialog.dismiss();
                })
                .setCancelable(false) // Prevent dismissing the dialog by tapping outside
                .show();
    }
}