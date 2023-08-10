package com.example.final_project_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project_android.data.FlightTrackerViewModel;
import com.example.final_project_android.databinding.FlightTrackerBinding;
import com.example.final_project_android.databinding.ItemsLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * MainActivity class for the Flight Tracker app.
 * This activity manages the user interface, data retrieval, and navigation within the app.
 */
public class FlightTracker extends AppCompatActivity {

    /**
     * The binding instance to inflate and access the layout elements of the activity.
     */
    protected FlightTrackerBinding binding;

    /**
     * The Room database instance to interact with flight data.
     */
    FlightDatabase myDB;

    /**
     * The Data Access Object (DAO) instance for performing database operations.
     */
    FlightDAO myDAO;

    /**
     * SharedPreferences instance for managing persistent data storage.
     */
    SharedPreferences prefs;

    /**
     * ViewModel instance for handling UI-related data and operations.
     */
    FlightTrackerViewModel flightModel;

    /**
     * List to store retrieved flight data from API search.
     */
    ArrayList<Flight> theFlights;

    /**
     * List to store saved flight data from the database.
     */
    ArrayList<Flight> savedFlights;

    /**
     * Position of the selected item in the search results list.
     */
    int position;

    /**
     * Position of the selected item in the saved flights list.
     */
    int position1;

    /**
     * Adapter for displaying search result items in the RecyclerView.
     */
    RecyclerView.Adapter searchAdapter;

    /**
     * Adapter for displaying saved flight items in the RecyclerView.
     */
    RecyclerView.Adapter listAdapter;

    /**
     * RequestQueue instance for managing network requests.
     */
    RequestQueue queue = null;

    /**
     * The airport code used for flight searches and data retrieval.
     */
    protected String airportCode;

    /**
     * The URL for making flight search API requests.
     */
    String searchURL;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_flight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_help){
            AlertDialog.Builder builder = new AlertDialog.Builder( FlightTracker.this );
            builder.setTitle("Instructions:")
                    .setMessage("Welcome to the Aviation Stack Flight Tracker app!\n\n" +
                            "1. Enter the 3-letter airport code in the search box.\n" +
                            "2. Click the 'Search' button to retrieve the list of flights departing from that airport.\n" +
                            "3. The list of flights will be shown below in a scrollable list.\n" +
                            "4. Click on a flight from the list to view its details (Destination, Terminal, Gate, and Delay).\n" +
                            "5. On the details page, you can click the 'Save to Database' button to save the flight details.\n" +
                            "6. Use the 'List' button to view the list of flights saved in the database.\n" +
                            "7. In the 'Saved Flights' list, click on a flight to view its details.\n" +
                            "8. On the detailed page of a saved flight, click the 'Delete from Database' button to remove the flight from the database.\n\n")
                    .setPositiveButton("OK", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .create().show();
        }
        else if (item.getItemId() == R.id.item_bear) {
            startActivity(new Intent(this, Bear.class));
        }
        else if (item.getItemId() == R.id.item_currency) {
            startActivity(new Intent(this, CurrencyMainActivity.class));
        }
        else if (item.getItemId() == R.id.item_trivia) {
            startActivity(new Intent(this, TopicSelection.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("Flight Tracker");

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        airportCode = prefs.getString("airportCode", "");
        binding.airportCodeField.setText(airportCode);

        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "database-name").build();
        myDAO = myDB.fDAO();

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        savedFlights = flightModel.savedFlights.getValue();
        theFlights = flightModel.theFlights.getValue();

        // Initialize RecyclerView adapters for search results and saved flights
        searchAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemsLayoutBinding binding = ItemsLayoutBinding.inflate(getLayoutInflater(), parent, false);
                return new MyViewHolder( binding.getRoot() );
            }

            @Override
            public int getItemViewType(int position) { return 0; }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                //updates the widgets
                Flight atThisRow = theFlights.get(position);
                holder.destText.setText(atThisRow.destination);
            }

            @Override
            public int getItemCount() {
                return theFlights.size();
            }
        };

        listAdapter = new RecyclerView.Adapter<MyViewHolder1>() {
            @NonNull
            @Override
            public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemsLayoutBinding binding = ItemsLayoutBinding.inflate(getLayoutInflater(), parent, false);
                return new MyViewHolder1( binding.getRoot() );
            }

            @Override
            public int getItemViewType(int position1) { return 0; }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position1) {
                Flight atThisRow = savedFlights.get(position1);
                holder.destText.setText(atThisRow.destination);
            }

            @Override
            public int getItemCount() {
                return savedFlights.size();
            }
        };

        // Set up button click listener to retrieve saved flights from the database
        binding.listSavedButton.setOnClickListener( click -> {
            if (savedFlights == null) {
                savedFlights = new ArrayList<>();
            } else {
                savedFlights.clear();
            }
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                savedFlights.addAll( myDAO.getAllFlights() );
                runOnUiThread( () ->{
                        binding.myRecyclerView.setAdapter( listAdapter );
                        listAdapter.notifyDataSetChanged();
                });
            });
        });

        // Observe changes in the selectedFlight LiveData and navigate to appropriate fragments
        flightModel.selectedFlight.observe(this, newFlightValue -> {
            if (newFlightValue.id == 0) {
                FlightDetailsFragment flightFragment = new FlightDetailsFragment(newFlightValue, myDAO);
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, flightFragment)
                    .addToBackStack("Doesn't matter")
                    .commit();
            } else {
                DeleteFragment deleteFragment = new DeleteFragment(newFlightValue, savedFlights, position1, listAdapter, myDAO);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, deleteFragment)
                        .addToBackStack("Doesn't matter")
                        .commit();
            }
        });

        // Set up the search button click listener to retrieve flight data from API
        binding.searchButton.setOnClickListener(click -> {
            theFlights = new ArrayList<>();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("airportCode", binding.airportCodeField.getText().toString());
            editor.apply();

            airportCode = binding.airportCodeField.getText().toString();
            searchURL = "http://api.aviationstack.com/v1/flights?access_key=e848b2c7bdcabb06f8166cc6ea4d84ee&dep_iata=" + airportCode;

            queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    searchURL,
                    null,
                    response -> {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            int len = data.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject thisObj = data.getJSONObject(i);
                                JSONObject arrival = thisObj.getJSONObject("arrival");
                                String destination = arrival.getString("airport");
                                JSONObject departure = thisObj.getJSONObject("departure");
                                String terminal = departure.getString("terminal");
                                String gate = departure.getString("gate");
                                int delay = departure.optInt("delay", 0);

                                Flight newFlight = new Flight(destination, terminal, gate, delay);
                                theFlights.add(newFlight);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(() ->
                                binding.myRecyclerView.setAdapter(searchAdapter));
                        searchAdapter.notifyDataSetChanged();
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Connection Failed",
                                Toast.LENGTH_LONG).show();
                    });
            queue.add(request);
        });
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Inner ViewHolder class for displaying search result items in the RecyclerView.
     * Binds the layout widgets and handles click events on the search result items.
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {
        TextView destText;

        /**
         * Constructor for the MyViewHolder class.
         *
         * @param itemView The view representing a single search result item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destinationText);

            // Set a click listener for the search result item
            itemView.setOnClickListener( click -> {
                position = getAbsoluteAdapterPosition();
                Flight selected = theFlights.get(position);
                flightModel.selectedFlight.postValue(selected);
            });
        }
    }

    /**
     * Inner ViewHolder class for displaying saved flight items in the RecyclerView.
     * Binds the layout widgets and handles click events on the saved flight items.
     */
    protected class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView destText;

        /**
         * Constructor for the MyViewHolder1 class.
         *
         * @param itemView The view representing a single saved flight item.
         */
        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destinationText);

            // Set a click listener for the saved flight item
            itemView.setOnClickListener( click -> {
                position1 = getAbsoluteAdapterPosition();
                Flight selected = savedFlights.get(position1);
                flightModel.selectedFlight.postValue(selected);
            });
        }
    }
}
