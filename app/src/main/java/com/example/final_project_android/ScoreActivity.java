package com.example.final_project_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_android.R;

import java.util.List;

/**
 * Activity for displaying the user's score and handling interactions related to the score.
 */
public class ScoreActivity extends AppCompatActivity {

    private TextView textViewScore;
    private Button buttonSave;
    private Button buttonViewTopPlayers;

    /**
     * Called when the activity is first created.
     * Initializes the UI elements and handles user interactions.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textViewScore = findViewById(R.id.textViewScore);
        buttonSave = findViewById(R.id.buttonSave);
        buttonViewTopPlayers = findViewById(R.id.buttonViewTopPlayers);

        // Get the scorePercentage from the intent
        double scorePercentage = getIntent().getDoubleExtra("scorePercentage", 0.0);

        // Show dialog to get user's name
        showNameInputDialog(scorePercentage);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle saving data logic here
                // For example, you can show a toast message
                Toast.makeText(ScoreActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();

                // Update buttonViewTopPlayers visibility
                buttonViewTopPlayers.setVisibility(View.VISIBLE);
            }
        });

        buttonViewTopPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle opening the TopPlayersActivity
                Intent intent = new Intent(ScoreActivity.this, TopPlayersActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Show a dialog to get the user's name and then display the score.
     *
     * @param scorePercentage The user's score percentage.
     */
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

    /**
     * Display the user's score.
     *
     * @param name            The user's name.
     * @param scorePercentage The user's score percentage.
     */
    private void displayScore(String name, double scorePercentage) {
        String scoreMessage = name + ", you scored: " + scorePercentage + "%";
        textViewScore.setText(scoreMessage);
    }
}
