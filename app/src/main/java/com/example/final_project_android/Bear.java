package com.example.final_project_android;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.databinding.ActivityBearBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Bear extends AppCompatActivity {
    ActivityBearBinding binding;
    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the SharedPreferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load saved values if available
        loadSavedValues();


        binding.generate.setOnClickListener(view -> {
            // Toast notification
            Toast.makeText(this, "Generate Image Clicked", Toast.LENGTH_SHORT).show();

//            // Snackbar notification
//            Snackbar.make(view, "Image Generated", Snackbar.LENGTH_SHORT).show();
//
//            // AlertDialog notification
//            showAlertDialog();
            // Save the user inputs to SharedPreferences
            saveUserInputs();

            // Your other click handling code goes here...
        });

        binding.viewImageButton.setOnClickListener(view->{
            List<BearItems> bearItemsList = new ArrayList<>();
            if (binding.recyclerViewSavedBearImages.getVisibility() == View.VISIBLE) {
                // If the RecyclerView is visible, hide it and show other views
                hideRecyclerView();
            } else {
                // If the RecyclerView is not visible, show it and load bearItems if needed
                showRecyclerView();
                if (bearItemsList.isEmpty()) {
                    bearItemsList.add(new BearItems(R.drawable.bear_image,50,50));
                    bearItemsList.add(new BearItems(R.drawable.bear_image,50,50));
                    bearItemsList.add(new BearItems(R.drawable.bear_image,50,50));
                }
            }
            RecyclerView recyclerView = findViewById(R.id.recyclerViewSavedBearImages);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyBearAdapter(getApplicationContext(),bearItemsList));

        });
    }


    private void showRecyclerView() {
        // Hide other views except the RecyclerView
        binding.widthTextField.setVisibility(View.GONE);
        binding.heightTextField.setVisibility(View.GONE);
        binding.generate.setVisibility(View.GONE);
        binding.heightHeader.setVisibility(View.GONE);
        binding.widthHeader.setVisibility(View.GONE);
        binding.viewImageButton.setVisibility(View.GONE);
        binding.recyclerViewSavedBearImages.setVisibility(View.VISIBLE);
    }
    private void hideRecyclerView() {
        // Show other views and hide the RecyclerView
        binding.widthTextField.setVisibility(View.VISIBLE);
        binding.heightTextField.setVisibility(View.VISIBLE);
        binding.generate.setVisibility(View.VISIBLE);
        binding.heightHeader.setVisibility(View.VISIBLE);
        binding.widthHeader.setVisibility(View.VISIBLE);
        binding.viewImageButton.setVisibility(View.VISIBLE);
        binding.recyclerViewSavedBearImages.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        // Check if the RecyclerView is visible
        if (binding.recyclerViewSavedBearImages.getVisibility() == View.VISIBLE) {
            // If RecyclerView is visible, hide it and show other views
            hideRecyclerView();
        } else {
            // If RecyclerView is not visible, navigate back to the MainActivity
            super.onBackPressed();
        }
    }


    private void loadSavedValues() {
        // Retrieve the saved values from SharedPreferences
        String savedWidth = preferences.getString("width", "");
        String savedHeight = preferences.getString("height", "");

        // Set the retrieved values to the EditText fields
        binding.widthTextField.setText(savedWidth);
        binding.heightTextField.setText(savedHeight);
    }
    private void saveUserInputs() {
        // Get the user inputs from the EditText fields
        String widthInput = binding.widthTextField.getText().toString();
        String heightInput = binding.heightTextField.getText().toString();

        // Save the user inputs to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("width", widthInput);
        editor.putString("height", heightInput);
        editor.apply();
    }
//    private void showAlertDialog() {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Alert Dialog")
//                .setMessage("This is an AlertDialog notification!")
//                .setPositiveButton("OK", (dialog, which) -> {
//                    // Do something when the user clicks the positive button (OK)
//                    dialog.dismiss();
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> {
//                    // Do something when the user clicks the negative button (Cancel)
//                    dialog.dismiss();
//                })
//                .setCancelable(false) // Prevent dismissing the dialog by tapping outside
//                .show();
//    }


}