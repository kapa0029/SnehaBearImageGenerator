package com.example.final_project_android;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

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



        //Toolbar for Trivia main activity
        Toolbar triviaToolbar = findViewById(R.id.triviaToolbar);
        setSupportActionBar(triviaToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.trivia_actionbar_title));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.trivia_subtitle));


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
        topicList.add(new Topic(23, getResources().getString(R.string.history_title), getResources().getString(R.string.history_subtitle)));
        topicList.add(new Topic(17, getResources().getString(R.string.science_title), getResources().getString(R.string.science_subtitle)));
        topicList.add(new Topic(21,getResources().getString(R.string.sports_title),getResources().getString(R.string.sports_subtitle)));
        topicList.add(new Topic(10,getResources().getString(R.string.geography_title),getResources().getString(R.string.geography_subtitle)));
        topicList.add(new Topic(20,getResources().getString(R.string.mythology_title),getResources().getString(R.string.mythology_subtitle)));


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

        builder.setTitle(getResources().getString(R.string.enter_number_of_ques));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Retrieve the stored number of questions and set it in the EditText
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int storedNumQuestions = preferences.getInt("numQuestions", 0);
        input.setText(String.valueOf(storedNumQuestions));

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numQuestionsStr = input.getText().toString();
                int numQuestions = Integer.parseInt(numQuestionsStr);

                // Store the entered number of questions in SharedPreferences
                saveNumQuestions(numQuestions);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.helpTrivia) {
            String helpMessage = getResources().getString(R.string.topicSelection_help);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    helpMessage, Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.toolbarTheme));
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
            snackbar.show();
            return true;
        }
        else if (id == R.id.item_currency) {
            startActivity(new Intent(this, CurrencyMainActivity.class));
        }
        else if (id == R.id.item_flight) {
            startActivity(new Intent(this, FlightTracker.class));
        }
        else if (item.getItemId() == R.id.item_bear) {
            startActivity(new Intent(this, Bear.class));
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Inflates the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trivia, menu);
        return true;
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
                            String fetchQuestionError = getResources().getString(R.string.fetch_question_error);
                            Toast.makeText(TopicSelection.this, fetchQuestionError, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String fetchQuestionError = getResources().getString(R.string.fetch_question_error);
                        Toast.makeText(TopicSelection.this, fetchQuestionError, Toast.LENGTH_SHORT).show();
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
    private void saveNumQuestions(int numQuestions) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("numQuestions", numQuestions);
        editor.apply();
    }




}
