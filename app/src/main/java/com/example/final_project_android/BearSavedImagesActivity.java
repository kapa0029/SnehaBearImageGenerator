package com.example.final_project_android;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BearSavedImagesActivity extends AppCompatActivity implements MyBearAdapter.OnItemClickListener, MyBearAdapter.OnItemLongClickListener {

    private RecyclerView recyclerView;
    private MyBearAdapter adapter;
    private List<BearItemEntity> bearItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear_saved_images);

        recyclerView = findViewById(R.id.recyclerViewSavedImages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        // Show the AlertDialog for confirmation
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the item from the database and update the RecyclerView
                    adapter.deleteItem(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
