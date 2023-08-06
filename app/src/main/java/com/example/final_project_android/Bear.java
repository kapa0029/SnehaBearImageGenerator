package com.example.final_project_android;
import android.content.Context;

import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.final_project_android.databinding.ActivityBearBinding;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Bear extends AppCompatActivity {
    ActivityBearBinding binding;
    SharedPreferences preferences;
    RequestQueue bearRequestQueue;
    private BearItemRepository bearItemRepository;

    private List<BearItemEntity> savedImagesList = new ArrayList<>();

    private BearItemDao bearDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bearRequestQueue = Volley.newRequestQueue(this);

        // Get the SharedPreferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load saved values if available
        loadSavedValues();

        // Initialize the BearItemRepository
        bearItemRepository = new BearItemRepository(getApplication());


        binding.generate.setOnClickListener(view -> {
            // Toast notification
            //Toast.makeText(this, "Generate Image Clicked", Toast.LENGTH_SHORT).show();

//            // Snackbar notification
//            Snackbar.make(view, "Image Generated", Snackbar.LENGTH_SHORT).show();
//
//            // AlertDialog notification
//            showAlertDialog();
            // Save the user inputs to SharedPreferences
            saveUserInputs();

            String enteredWidthStr = binding.widthTextField.getText().toString();
            String enteredHeightStr = binding.heightTextField.getText().toString();

            if (enteredWidthStr.isEmpty() || enteredHeightStr.isEmpty()) {
                // Show a toast message if either width or height is empty
                Toast.makeText(this, "Please enter both width and height", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int enteredWidth = Integer.parseInt(enteredWidthStr);
                    int enteredHeight = Integer.parseInt(enteredHeightStr);

                    // Now you have the entered width and height, you can proceed to use them
                    // For example, you can call your fetchBearImage() method here passing enteredWidth and enteredHeight
                    fetchBearImage(enteredWidth, enteredHeight);


                } catch (NumberFormatException e) {
                    // Show a toast message if the user entered non-numeric values for width or height
                    Toast.makeText(this, "Please enter valid numeric values for width and height", Toast.LENGTH_SHORT).show();
                }
            }

            // Your other click handling code goes here...


        });

        binding.viewImageButton.setOnClickListener(view->{
            // Start the SavedImagesActivity
            Intent intent = new Intent(this, BearSavedImagesActivity.class);
            startActivity(intent);

        });


    }

private void fetchBearImage(int width, int height) {
    // Check if the image with the same width and height already exists in the database
    LiveData<BearItemEntity> bearItemLiveData = bearItemRepository.getBearItemBySize(width, height);

    bearItemLiveData.observe(this, new Observer<BearItemEntity>() {
        @Override
        public void onChanged(BearItemEntity bearItem) {
            if (bearItem != null) {
                // If the image exists in the database, display it
                displayBearImage(bearItem);
            } else {
                // If the image does not exist, fetch it from the URL
                String imageUrl = "https://placebear.com/" + width + "/" + height;
                ImageRequest bearImageRequest = new ImageRequest(
                        imageUrl,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // Convert the Bitmap to a byte array
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                response.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();

                                // Create a BearItemEntity with the fetched image and other details
                                BearItemEntity bearItem = new BearItemEntity(byteArray, width, height);

                                // Insert the BearItemEntity into the database through the BearItemRepository
                                bearItemRepository.insert(bearItem);

                                // Display the fetched image
                                displayBearImage(bearItem);
                            }
                        },
                        0, 0,
                        ImageView.ScaleType.CENTER_CROP, // ScaleType for the image.
                        Bitmap.Config.RGB_565, // Bitmap format.
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Error occurred while fetching the image. Handle the error.
                                Toast.makeText(Bear.this, "Error fetching bear image", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                // Add the image request to the RequestQueue
                bearRequestQueue.add(bearImageRequest);
            }

            // Remove the observer to avoid multiple calls
            bearItemLiveData.removeObserver(this);
        }
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
    private void displayBearImage(BearItemEntity bearItem) {
        byte[] byteArray = bearItem.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imageView = findViewById(R.id.bear_image_view);
        imageView.setImageBitmap(bitmap);
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