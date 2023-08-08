package com.example.final_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;
import com.example.final_project_android.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Final Project");
        getSupportActionBar().setSubtitle("Group work");


        binding.bearImage.setOnClickListener(click-> {
            // Toast notification
            Toast.makeText(this, "Opening Bear Image Generator", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Bear.class);
            startActivity(intent);
        });


    }
    private void showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog")
                .setMessage("This is an AlertDialog notification!")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Do something when the user clicks the positive button (OK)
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Do something when the user clicks the negative button (Cancel)
                    dialog.dismiss();
                })
                .setCancelable(false) // Prevent dismissing the dialog by tapping outside
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bear_image) {
            startActivity(new Intent(this, Bear.class));
            return true;
        }
        else if (id==R.id.currency_converter) {


        }
        else if (id==R.id.trivia) {


        }
        else if (id==R.id.flight_tracker) {


        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
