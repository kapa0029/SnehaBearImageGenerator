package com.example.final_project_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

public class fetchQuestion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_look);


        List<Question> questionList = getIntent().getParcelableArrayListExtra("questions");


    }
}
