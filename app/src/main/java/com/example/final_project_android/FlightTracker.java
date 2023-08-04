package com.example.final_project_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.example.final_project_android.data.FlightTrackerViewModel;
import com.example.final_project_android.databinding.DetailsLayoutBinding;
import com.example.final_project_android.databinding.FlightTrackerBinding;
import com.example.final_project_android.databinding.ItemsLayoutBinding;

import java.util.ArrayList;

public class FlightTracker extends AppCompatActivity {

    protected FlightTrackerBinding binding;
    protected ItemsLayoutBinding binding1;
    protected DetailsLayoutBinding binding2;
    FlightDatabase myDB;
    FlightDAO myDAO;
    FlightTrackerViewModel flightModel;
    ArrayList<Flight> theFlights;

    RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    protected String airportCode;
    String searchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "database-name").build();
        myDAO = myDB.fDAO();

        binding = FlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FrameLayout fragmentLocation = findViewById( R.id.fragmentLocation );

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        flightModel.selectedFlight.observe(this, (newFlightValue) -> {
            if (newFlightValue != null) {
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                FlightDetailsFragment flightFragment = new FlightDetailsFragment( newFlightValue );
                tx.replace(R.id.fragmentLocation, flightFragment);
                tx.addToBackStack("Doesn't matter");
                tx.commit();
            }
        });
        binding2.SaveButton.setOnClickListener(click -> {

        });

        binding.searchButton.setOnClickListener(click -> {
            int type = 1;
            airportCode = binding.airportCodeField.getText().toString();
            searchURL = "http://api.aviationstack.com/v1/flights?access_key=2436dee7ec6af21ed899c8b07ad871cd&dep_iata=" + airportCode;

//            queue = Volley.newRequestQueue(this);
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchURL, null,
//                    (response) -> {
//                        try {
//                            //Destination, Terminal, Gate, and Delay
//                            JSONArray data = response.getJSONArray("data");
//                            int len = data.length();
//                            for (int i = 0; i < len; i++) {
//                                JSONObject thisObj = data.getJSONObject(i);
//                                //String status = thisObj.getString("flight_status");
//                                JSONObject arrival = thisObj.getJSONObject("arrival");
//                                String destination = arrival.getString("airport");
//                                JSONObject departure = thisObj.getJSONObject("departure");
//                                String terminal = departure.getString("terminal");
//                                String gate = departure.getString("gate");
//                                int delay = departure.getInt("delay");
//                                JSONObject flight = thisObj.getJSONObject("flight");
//                                String flightNumber = flight.getString("number");
//                                JSONObject airline = thisObj.getJSONObject("airline");
////                              String airlineName = airline.getString("name");
//
//                            runOnUiThread( ( ) -> {
//                                binding1.flightNumberText.setText("Flight Number: " + flightNumber);
//                                binding1.airlineNameText.setText("Airline Name: " + airlineName);
//                                binding2.destinationText.setText("Destination: " + destination);
//                                binding2.terminalText.setText("Terminal: " + terminal);
//                                binding2.gateText.setText("Gate: " + gate);
//                                binding2.delayText.setText("Delay: " + delay);
////
//                                Flight newFlight = new Flight(destination, terminal, gate, delay);
//
//                                theFlights.add(newFlight);
//
//                                myAdapter.notifyDataSetChanged();
//
//                            }
//                        } catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    },
//                    (error) -> {
//                        int = 0;
//                    });
//            queue.add(request);

            binding.airportCodeField.setText("");
        });

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        theFlights = flightModel.theFlights.getValue();
        if (theFlights == null) {
            flightModel.theFlights.postValue( theFlights = new ArrayList<Flight>());
        }

        binding.myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemsLayoutBinding binding = ItemsLayoutBinding.inflate(getLayoutInflater(), parent, false);
                return new MyViewHolder( binding.getRoot() );
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                Flight inThisHolder = theFlights.get(position);
                holder.destText.setText(inThisHolder.destination);
                holder.terminalText.setText(inThisHolder.terminal);
                holder.gateText.setText(inThisHolder.gate);
                holder.delayText.setText(inThisHolder.delay);
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
        TextView terminalText;
        TextView gateText;
        TextView delayText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destinationText);
            terminalText = itemView.findViewById(R.id.terminalText);
            gateText = itemView.findViewById(R.id.gateText);
            delayText = itemView.findViewById(R.id.delayText);
        }
    }
}
