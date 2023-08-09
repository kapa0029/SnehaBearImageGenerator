package com.example.final_project_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity for displaying and answering a series of questions.
 */
public class fetchQuestion extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonNext;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;

    private List<Integer> userAnswers;

    @Override
    /**
     * Method called when the activity is created. Initializes UI elements, sets up question display, and handles user input.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_look);

        // Get the questionList from the intent
        questionList = getIntent().getParcelableArrayListExtra("questions");
        userAnswers = new ArrayList<>();

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        buttonNext = findViewById(R.id.buttonNext);

        // Display the first question
        showQuestion(currentQuestionIndex);

        buttonNext.setOnClickListener(view -> {

            int checkedRadioButtonId = radioGroupAnswers.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                // No radio button is selected, show an error message or handle the situation as needed
                // For example, you can display a Toast message
                Toast.makeText(this, "Please select an answer before proceeding.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the selected option for the current question
            int selectedOptionIndex = radioGroupAnswers.indexOfChild(findViewById(checkedRadioButtonId));
            questionList.get(currentQuestionIndex).setSelectedOptionIndex(selectedOptionIndex);
            userAnswers.add(selectedOptionIndex);

            if (currentQuestionIndex == questionList.size() - 2) {
                // Change the button text to "Submit" when the user is on the second-to-last question
                buttonNext.setText("Submit");
            }

            // Handle the Next button click
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            } else {
                showNameInputDialog();
            }
        });
    }


    /**
     * Displays an input dialog for the user to enter their name, calculates the score, and stores it in the database.
     */
    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name That you want in the database");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.input_name, null);
        final EditText inputName = viewInflated.findViewById(R.id.inputName);
        builder.setView(viewInflated);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playerName = inputName.getText().toString().trim();
                int correctAnswers = calculateCorrectAnswers();
                double scorePercentage = ((double) correctAnswers / questionList.size()) * 100;

                Score score = new Score();
                score.setPlayerName(playerName);
                score.setScore((int) scorePercentage);

                // Perform database insertion on a background thread
                new Thread(() -> {
                    ScoreDatabase database = ScoreDatabase.getInstance(fetchQuestion.this);
                    ScoreDao scoreDao = database.scoreDao();
                    scoreDao.insertScore(score);


                }).start();

                // All questions answered, navigate to score/activity result
                Intent intent = new Intent(fetchQuestion.this, ScoreActivity.class);
                intent.putExtra("questions", (ArrayList<? extends Parcelable>) questionList);
                intent.putExtra("userAnswers", (ArrayList<Integer>) userAnswers);
                intent.putExtra("scorePercentage", scorePercentage);
                intent.putExtra("playerName", playerName);
                intent.putExtra("correctAnswer", correctAnswers);
                startActivity(intent);
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
     * Calculates the number of correct answers based on user responses to the questions.
     *
     * @return The number of correct answers.
     */
    private int calculateCorrectAnswers() {
        int correctAnswers = 0;

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            int selectedOptionIndex = question.getSelectedOptionIndex();
            int userAnswerIndex = userAnswers.get(i);

            // Check if an option is selected
            if (selectedOptionIndex != -1) {
                String selectedOption = question.getOptions().get(selectedOptionIndex);

                if (selectedOption.equals(question.getCorrectAnswer())) {
                    correctAnswers++;
                }
            } else if (userAnswerIndex != -1 && question.getOptions().get(userAnswerIndex).equals(question.getCorrectAnswer())) {
                // If user had previously answered correctly, count it as correct
                correctAnswers++;
            }
        }

        return correctAnswers;
    }




    /**
     * Displays the specified question on the screen, allowing the user to select an answer.
     *
     * @param questionIndex The index of the question to be displayed.
     */

    private void showQuestion(int questionIndex) {
        Question question = questionList.get(questionIndex);

        // Set question text
        textViewQuestion.setText(question.getQuestionText());

        // Clear previous options
        radioGroupAnswers.clearCheck();
        radioGroupAnswers.removeAllViews();

        // Add options to RadioGroup
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroupAnswers.addView(radioButton);
        }

        // Enable or disable Next button based on user's selection
        buttonNext.setEnabled(false);
        radioGroupAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            buttonNext.setEnabled(true);

            // Update the selected option for the current question
            int selectedOptionIndex = radioGroupAnswers.indexOfChild(findViewById(checkedId));
            question.setSelectedOptionIndex(selectedOptionIndex);
         //   Snackbar.make(findViewById(android.R.id.content), "Option selected: " + question.getOptions().get(selectedOptionIndex), Snackbar.LENGTH_SHORT).show();
        });

    }
    /**
     * Save the player's name to shared preferences.
     *
     * @param playerName The player's name to be saved.
     */
    private void savePlayerName(String playerName) {
        SharedPreferences preferences = getSharedPreferences("PlayerPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("playerName", playerName);
        editor.apply();
    }


}