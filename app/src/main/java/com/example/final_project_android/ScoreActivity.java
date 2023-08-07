package com.example.final_project_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
        double scorePercentage = getIntent().getDoubleExtra("scorePercentage", 0.0);

        // Show dialog to get user's name
        showNameInputDialog(scorePercentage);
    }

    private void showNameInputDialog(double scorePercentage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.input_name, null);
        final EditText inputName = viewInflated.findViewById(R.id.inputName);
        builder.setView(viewInflated);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString().trim();
                displayScore(name, scorePercentage);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void displayScore(String name, double scorePercentage) {
        String scoreMessage = name + ", you scored: " + scorePercentage + "%";
        textViewScore.setText(scoreMessage);
    }


}
