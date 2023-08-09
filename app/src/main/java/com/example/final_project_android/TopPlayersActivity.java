package com.example.final_project_android;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying the top players' scores in a ListView.
 */
public class TopPlayersActivity extends AppCompatActivity {

    private ListView listViewTopPlayers;

    /**
     * Called when the activity is first created.
     * Initializes UI elements and fetches top players' data from the database.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_players);

        listViewTopPlayers = findViewById(R.id.listViewTopPlayers);

        new Thread(() -> {
            // Fetch top players' data from the database using your ScoreDao
            ScoreDatabase database = ScoreDatabase.getInstance(this);
            ScoreDao scoreDao = database.scoreDao();
            List<Score> topPlayers = scoreDao.getTopPlayers();

            // Format player names and scores
            List<String> playerNamesAndScores = formatPlayerNamesAndScores(topPlayers);

            // Update the UI on the main thread
            runOnUiThread(() -> {
                // Create an adapter to display the top players' data in the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNamesAndScores);
                listViewTopPlayers.setAdapter(adapter);
            });
        }).start();
    }

    /**
     * Formats the player names and scores for display.
     *
     * @param topPlayers The list of top players' scores.
     * @return A list of formatted player names and scores.
     */
    private List<String> formatPlayerNamesAndScores(List<Score> topPlayers) {
        // Create a list to store formatted player names and scores
        List<String> playerNamesAndScores = new ArrayList<>();

        // Loop through each player and format their name and score
        for (Score player : topPlayers) {
            String playerNameAndScore = player.getPlayerName() + ": " + player.getScore() + " Percentage";
            playerNamesAndScores.add(playerNameAndScore);
        }

        return playerNamesAndScores;
    }
}
