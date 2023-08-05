package com.example.final_project_android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textViewScore = findViewById(R.id.textViewScore);

        // Get the questionList and userAnswers from the intent
        List<Question> questionList = getIntent().getParcelableArrayListExtra("questions");
        List<Integer> userAnswers = getIntent().getIntegerArrayListExtra("userAnswers");
        double scorePercentage = getIntent().getDoubleExtra("scorePercentage", 0.0);
        TextView textViewScore = findViewById(R.id.textViewScore);
        textViewScore.setText("Score: " + scorePercentage + "%");


    }
}


