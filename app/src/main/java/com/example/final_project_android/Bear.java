package com.example.final_project_android;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.final_project_android.databinding.ActivityBearBinding;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

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

        setSupportActionBar(binding.bearToolbar);
        getSupportActionBar().setTitle("Bear Image Generator");
        getSupportActionBar().setSubtitle("Generate Images");

        bearRequestQueue = Volley.newRequestQueue(this);

        // Get the SharedPreferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load saved values if available
        loadSavedValues();

        // Initialize the BearItemRepository
        bearItemRepository = new BearItemRepository(getApplication());


        binding.generate.setOnClickListener(view -> {

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.helpBear) {
            showHelpSnackbar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bear, menu);
        return true;
    }
    private void showHelpSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Enter width and height to generate customized bear images", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.toolbarTheme));
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }
}