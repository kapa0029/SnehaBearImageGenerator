package com.example.final_project_android;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Represents a question in the quiz, including the question text, options, and correct answer.
 * Implements Parcelable to allow passing Question objects between activities.
 */
public class Question implements Parcelable {

    private String category;
    private String type;
    private String difficulty;
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private List<String> incorrectAnswers;
    private String correctAnswer;

    private int selectedOptionIndex = -1;

    /**
     * Constructor to create a Question instance.
     *
     * @param category        The category of the question.
     * @param type            The type of the question.
     * @param difficulty      The difficulty level of the question.
     * @param questionText    The text of the question.
     * @param correctAnswer   The correct answer of the question.
     * @param incorrectAnswers List of incorrect answers for the question.
     */
    public Question(String category, String type, String difficulty, String questionText, String correctAnswer,
                    List<String> incorrectAnswers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * Get the index of the selected option.
     *
     * @return The index of the selected option.
     */
    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    /**
     * Set the index of the selected option.
     *
     * @param selectedOptionIndex The index of the selected option.
     */
    public void setSelectedOptionIndex(int selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }


    /**
     * Get the category of the question.
     *
     * @return The question's category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get the type of the question.
     *
     * @return The question's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the difficulty level of the question.
     *
     * @return The question's difficulty.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Get the incorrect answers for the question.
     *
     * @return List of incorrect answers.
     */
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    // Parcelable methods

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return A bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object into the specified parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
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

    /**
     * Parcelable.Creator that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * Private constructor that takes a Parcel and creates a Question object from it.
     *
     * @param in The Parcel containing the object's data.
     */
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

    /**
     * Get the text of the question.
     *
     * @return The question's text.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Get all options for the question, including incorrect and correct options.
     *
     * @return List of all options.
     */
    public List<String> getOptions() {
        List<String> allOptions = incorrectAnswers;
        allOptions.add(correctAnswer);
        return allOptions;
    }

    /**
     * Get the index of the correct option.
     *
     * @return The index of the correct option.
     */
    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    /**
     * Get the correct answer of the question.
     *
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}