package com.example.final_project_android;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Question implements Parcelable {
    private String category;
    private String type;
    private String difficulty;
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private List<String> incorrectAnswers;
    private String correctAnswer;




    public Question(String category, String type, String difficulty, String questionText, String correctAnswer, List<String> incorrectAnswers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }


    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }
    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(difficulty);
        dest.writeString(questionText);
        dest.writeStringList(options);
        dest.writeInt(correctOptionIndex);
        dest.writeStringList(incorrectAnswers);
        dest.writeString(correctAnswer);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private Question(Parcel in) {
        category = in.readString();
        type = in.readString();
        difficulty = in.readString();
        questionText = in.readString();
        options = in.createStringArrayList();
        correctOptionIndex = in.readInt();
        incorrectAnswers = in.createStringArrayList();
        correctAnswer = in.readString();
    }

}
