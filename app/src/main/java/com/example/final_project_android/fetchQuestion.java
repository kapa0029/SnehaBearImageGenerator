package com.example.final_project_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class fetchQuestion extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonNext;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_look);

        // Get the questionList from the intent
        questionList = getIntent().getParcelableArrayListExtra("questions");

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

            // Handle the Next button click
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            } else {
                int correctAnswers = calculateCorrectAnswers();
                int totalQuestions = questionList.size();
                double scorePercentage = ((double) correctAnswers / totalQuestions) * 100;

                // All questions answered, navigate to score/activity result
                Intent intent = new Intent(fetchQuestion.this, ScoreActivity.class);
                intent.putExtra("questions", (ArrayList<? extends Parcelable>) questionList);
                intent.putExtra("correctAnswer", correctAnswers);

                intent.putExtra("scorePercentage", scorePercentage);
                startActivity(intent);
            }
        });
    }

    private int calculateCorrectAnswers() {
        int correctAnswers = 0;
        for (Question question : questionList) {
            int selectedOptionIndex = question.getSelectedOptionIndex();

            // Check if an option is selected
            if (selectedOptionIndex != -1) {
                String selectedOption = question.getOptions().get(selectedOptionIndex);

                if (selectedOption.equals(question.getCorrectAnswer())) {
                    correctAnswers++;
                }
            }
        }
        return correctAnswers;
    }


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
        });
    }

}