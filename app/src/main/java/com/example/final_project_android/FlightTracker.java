package com.example.final_project_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class FlightTracker extends AppCompatActivity {

    protected FlightTrackerBinding binding;
    FlightDatabase myDB;
    FlightDAO myDAO;
    FlightTrackerViewModel flightModel;
    ArrayList<Flight> theFlights;
    ArrayList<Flight> savedFlights;
    int position;
    SharedPreferences prefs;
    RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    protected String airportCode;
    String searchURL;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flight_menu, menu);
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
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        airportCode = prefs.getString("airportCode", "");
        binding.airportCodeField.setText(airportCode);

        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "database-name").build();
        myDAO = myDB.fDAO();

        myAdapter = new RecyclerView.Adapter<MyViewHolder>() {
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
                //puts the string in position at theWords TextView
                holder.destText.setText(atThisRow.destination);
            }

            @Override
            public int getItemCount() {
                return theFlights.size();
            }
        };

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);

        savedFlights = flightModel.savedFlights.getValue();
        if (savedFlights == null) {
            flightModel.savedFlights.postValue(savedFlights = new ArrayList<>());
        }
        binding.listSavedButton.setOnClickListener( click -> {

            savedFlights.clear();
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                savedFlights.addAll( myDAO.getAllFlights() ); //Once you get the data from database
                runOnUiThread( () ->
                        binding.myRecyclerView.setAdapter( myAdapter )); //You can then load the RecyclerView
                        myAdapter.notifyDataSetChanged();
            });
        });

        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);
        flightModel.selectedFlight.observe(this, newFlightValue -> {
            if (newFlightValue.id == 0) {
                FlightDetailsFragment flightFragment = new FlightDetailsFragment(newFlightValue, theFlights, position, myAdapter, myDAO);
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, flightFragment)
                    .addToBackStack("Doesn't matter")
                    .commit();
            } else {
                DeleteFragment deleteFragment = new DeleteFragment(newFlightValue, theFlights, position, myAdapter, myDAO);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, deleteFragment)
                        .addToBackStack("Doesn't matter")
                        .commit();
            }
        });
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.searchButton.setOnClickListener(click -> {
            theFlights = new ArrayList<>();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("airportCode", binding.airportCodeField.getText().toString());
            editor.apply();

            airportCode = binding.airportCodeField.getText().toString();
            searchURL = "http://api.aviationstack.com/v1/flights?access_key=ecb6338d1a834182eb7f8cee251bc267&dep_iata=" + airportCode;

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
                                //String status = thisObj.getString("flight_status");
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
                                binding.myRecyclerView.setAdapter(myAdapter));
                                myAdapter.notifyDataSetChanged();
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Connection Failed",
                                Toast.LENGTH_LONG).show();
                    });
            queue.add(request);
        });

        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        TextView destText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destinationText);
            //add click listener to select
            itemView.setOnClickListener( click -> {
                position = getAdapterPosition();
                Flight selected = theFlights.get(position);
                flightModel.selectedFlight.postValue(selected);
            });
        }
    }
}
