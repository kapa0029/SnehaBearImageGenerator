package com.example.final_project_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

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
        Toolbar triviaToolbar = findViewById(R.id.triviaToolbar);
        setSupportActionBar(triviaToolbar);
        getSupportActionBar().setTitle("Trivia Question Database");
        getSupportActionBar().setSubtitle("Topics");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.helpTrivia) {
            String helpMessage = "This page displays the top players";
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
}
