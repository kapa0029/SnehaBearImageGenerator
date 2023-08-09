package com.example.final_project_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity for selecting a topic and fetching questions related to the selected topic.
 */
public class TopicSelection extends AppCompatActivity implements TopicClickListener {

    private RecyclerView recyclerViewTopics;
    private TopicsAdapter topicsAdapter;
    private RequestQueue requestQueue;
    private List<Question> questionList;

    public TopicSelection() {
    }
    /**
     * Called when the activity is first created.
     * Initializes UI elements and sets up topic selection.
     *
     * @param savedInstanceState The saved instance state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_selection);
        requestQueue = Volley.newRequestQueue(this);

/**
 * Callback when a topic is clicked.
 * Displays a dialog to input the number of questions and fetches questions.
 *
 * @param topic The clicked topic.
 */

        // Initialize RecyclerView
        recyclerViewTopics = findViewById(R.id.recyclerViewTopics);
        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(this));



        // Create a list of topics (you can fetch it from API or local resources)
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new Topic(23, "History", "Test your knowledge of historical events."));
        topicList.add(new Topic(17, "Science & Nature", "Explore questions related to the scientific world."));
        topicList.add(new Topic(21,"Sports","Test Your Knowledge on sports"));
        topicList.add(new Topic(10,"Geography","Study the relationship between people and their environmnet"));
        topicList.add(new Topic(20,"Mythology","Test Your Knowledge of mytho"));


        // Set up the TopicsAdapter
        topicsAdapter = new TopicsAdapter(topicList, this);
        recyclerViewTopics.setAdapter(topicsAdapter);
    }

    /**
     * Callback when a topic is clicked.
     * Displays a dialog to input the number of questions and fetches questions.
     *
     * @param topic The clicked topic.
     */
    @Override
    public void onTopicClicked(Topic topic) {
        // Handle topic selection here, e.g., start the QuestionsActivity
//        int numQuestions = 0;
//        fetchQuestions(topic, numQuestions);
        showNumQuestionDialog(topic);
    }
    /**
     * Show a dialog to input the number of questions and fetch questions based on the topic.
     *
     * @param selectedTopic The selected topic for fetching questions.
     */
    private void showNumQuestionDialog(final Topic selectedTopic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Number of Questions");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numQuestionsStr = input.getText().toString();
                int numQuestions = Integer.parseInt(numQuestionsStr);

                // Fetch questions based on the selected topic and the entered number of questions
                fetchQuestions(selectedTopic, numQuestions);
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
     * Fetch questions from an API based on the selected topic and number of questions.
     *
     * @param selectedTopic The selected topic for fetching questions.
     * @param numQuestions  The number of questions to fetch.
     */
    private void fetchQuestions(Topic selectedTopic, int numQuestions) {
        // Build the URL for fetching questions based on the selected topic's category ID
        String url = "https://opentdb.com/api.php?amount=" +numQuestions +"&category=" + selectedTopic.getId() + "&type=multiple";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray results = jsonObject.getJSONArray("results");
                            List<Question> questionList = new ArrayList<>();

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject questionObj = results.getJSONObject(i);
                                String category = questionObj.getString("category");
                                String type = questionObj.getString("type");
                                String difficulty = questionObj.getString("difficulty");
                                String questionText = questionObj.getString("question");
                                String correctAnswer = questionObj.getString("correct_answer");
                                JSONArray incorrectAnswersArray = questionObj.getJSONArray("incorrect_answers");
                                List<String> incorrectAnswers = new ArrayList<>();

                                for (int j = 0; j < incorrectAnswersArray.length(); j++) {
                                    incorrectAnswers.add(incorrectAnswersArray.getString(j));
                                }



                                Question q = new Question(category, type, difficulty, questionText, correctAnswer, incorrectAnswers);
                                questionList.add(q);
                            }
                            startQuestionsActivity(selectedTopic, questionList);

                            // Start the QuestionsActivity and pass the list of questions as an extra
                            Intent intent = new Intent(TopicSelection.this, fetchQuestion.class);
                            intent.putParcelableArrayListExtra("questions", new ArrayList<>(questionList));
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TopicSelection.this, "Failed to fetch questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(TopicSelection.this, "Failed to fetch questions.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue

        requestQueue.add(stringRequest);
    }
    /**
     * Start the QuestionsActivity to display fetched questions.
     *
     * @param topic        The selected topic.
     * @param questionList The list of fetched questions.
     */
    private void startQuestionsActivity(Topic topic, List<Question> questionList) {
        Intent intent = new Intent(TopicSelection.this, fetchQuestion.class);
        intent.putExtra("selected_topic", topic);
        intent.putParcelableArrayListExtra("questions", new ArrayList<>(questionList));
        startActivity(intent);
    }



}
