package com.example.final_project_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class FlightTracker extends AppCompatActivity {

    protected FlightTrackerBinding binding;
    FlightTrackerViewModel flightModel;
    FlightDatabase myDB;
    FlightDAO myDAO;
    ArrayList<Flight> theFlights;

    RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    protected String airportCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        binding = FlightTrackerBinding.inflate(getLayoutInflater());

        binding.searchButton.setOnClickListener(click -> {
            int type = 1;
            airportCode = binding.airportCodeField.getText().toString();

            String searchURL = "http://api.aviationstack.com/v1/flights?access_key=2436dee7ec6af21ed899c8b07ad871cd&dep_iata=" + airportCode;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchURL, null,
                    (response) -> {
                        try {
                            //Destination, Terminal, Gate, and Delay
                            JSONArray data = response.getJSONArray("data");
                            int len = data.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject thisObj = data.getJSONObject(i);
                                //String status = thisObj.getString("flight_status");
                                JSONObject arrival = thisObj.getJSONObject("arrival");
                                String destination = arrival.getString("airport");
                                JSONObject dept = thisObj.getJSONObject("depature");
                                String terminal = dept.getString("terminal");
                                String gate = dept.getString("gate");
                                int delay = dept.getInt("delay");

//                            }
//                            runOnUiThread( ( ) -> {

                                Flight newFlight = new Flight(destination, terminal, gate, delay);
                                theFlights.add(newFlight);

                                myAdapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    },
                    (error) -> {

                    });
            queue.add(request);

            binding.airportCodeField.setText("");
        });

        setContentView(binding.getRoot());

        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        theFlights = flightModel.theFlights.getValue();
        if (theFlights == null) {
            flightModel.theFlights.postValue( theFlights = new ArrayList<Flight>());
        }

        binding.myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyViewHolder>() {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if (viewType == 1 ) {
                    ItemsLayoutBinding binding = ItemsLayoutBinding.inflate(getLayoutInflater(), parent, false);

                }
                return new MyViewHolder( binding.getRoot() );
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
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
