package com.example.final_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.final_project_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.bearImage.setOnClickListener(click->
//                startActivity(new Intent(this, Bear.class)));
//
//        binding.flightTracker.setOnClickListener(click->
//                startActivity(new Intent(this, FlightTracker.class)));
//
//        binding.trivia.setOnClickListener(click->
//                startActivity(new Intent(this, Trivia.class)));
//
//        binding.currencyConverter.setOnClickListener(click->
//                startActivity(new Intent(this, CurrencyConverter.class)));
    }
}