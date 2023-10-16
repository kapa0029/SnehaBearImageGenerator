package com.example.final_project_android;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.final_project_android.databinding.ActivityMainBinding;
/**
 * The main entry point of the application that displays the main menu options.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar with title and subtitle
        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Sneha's Bear Image Generator");

        binding.bearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the opening message from resources
                String startingMessage = getResources().getString(R.string.bear_opening_message);

                // Create an intent to launch the Bear Image Generator activity
                Intent intent = new Intent(String.valueOf(Bear.class));
                startActivity(intent); // Start the Bear activity
            }
        });



    }


//         binding.bearImage.setOnClickListener(click-> {
//             // Toast notification
//             Toast.makeText(this, "Opening Bear Image Generator", Toast.LENGTH_SHORT).show();
//             Intent intent = new Intent(MainActivity.this, Bear.class);
//             startActivity(intent);
//         });



    
//   @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//  }
////    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.bear_image) {
//            startActivity(new Intent(this, Bear.class));
//            return true;
//        }
//        else if (id==R.id.currency_converter) {
//
//
//        }
//        else if (id==R.id.trivia) {
//
//
//        }
//        else if (id==R.id.flight_tracker) {
//            startActivity(new Intent(this, FlightTracker.class));
//            return true;
//        }
//        else{
//            return super.onOptionsItemSelected(item);
//        }
//        return false;
//    }
}


