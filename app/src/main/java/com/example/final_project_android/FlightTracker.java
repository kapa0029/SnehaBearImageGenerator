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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project_android.data.FlightTrackerViewModel;
import com.example.final_project_android.databinding.DetailsLayoutBinding;
import com.example.final_project_android.databinding.FlightTrackerBinding;
import com.example.final_project_android.databinding.ItemsLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlightTracker extends AppCompatActivity {

    protected FlightTrackerBinding binding;
    protected ItemsLayoutBinding binding1;
    protected DetailsLayoutBinding binding2;
    FlightDatabase myDB;
    FlightDAO myDAO;
    FlightTrackerViewModel flightModel;
    ArrayList<Flight> theFlights;
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
                            "6. Use the 'Saved Flights' button to view the list of flights saved in the database.\n" +
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

        setSupportActionBar(binding.myToolbar);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        airportCode = prefs.getString("airportCode", "");
        binding.airportCodeField.setText(airportCode);

        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "database-name").build();
        myDAO = myDB.fDAO();

        binding = FlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        flightModel.selectedFlight.observe(this, (newFlightValue) -> {
            if (newFlightValue != null) {
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                FlightDetailsFragment flightFragment = new FlightDetailsFragment(newFlightValue, myDB);
                tx.replace(R.id.fragmentLocation, flightFragment);
                tx.addToBackStack("Doesn't matter");
                tx.commit();
            }
        });

        binding.searchButton.setOnClickListener(click -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", binding.airportCodeField.getText().toString());
            editor.apply();

            airportCode = binding.airportCodeField.getText().toString();
            searchURL = "http://api.aviationstack.com/v1/flights?access_key=2436dee7ec6af21ed899c8b07ad871cd&dep_iata=" + airportCode;

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
                                int delay = departure.getInt("delay");
                                runOnUiThread(() -> {
                                    Flight newFlight = new Flight(destination, terminal, gate, delay);
                                    theFlights.add(newFlight);
                                    myAdapter.notifyDataSetChanged();
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        int i = 0;
                    });

            queue.add(request);
            binding.airportCodeField.setText("");
        });

//        binding.listSavedButton.setOnClickListener(click -> {
//
//        });

        binding.myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //this inflates the row layout
                DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(getLayoutInflater(), parent, false);
                return new MyViewHolder( binding.getRoot() );
            }

            @Override
            public int getItemViewType(int position) {
               return 0;
            }

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
        });
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

     protected class MyViewHolder extends RecyclerView.ViewHolder {
        TextView destText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //add click listener to select
            itemView.setOnClickListener( click -> {
                int position = getAdapterPosition();
                Flight selected = theFlights.get(position);
                flightModel.selectedFlight.postValue(selected);
            });
            destText = itemView.findViewById(R.id.destinationText);
        }
    }
}
