package com.example.final_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.final_project_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button buttonTrivia = findViewById(R.id.trivia);
        buttonTrivia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopicSelection.class);
            startActivity(intent);
        });

    }
}