package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.data.FlightTrackerViewModel;
import com.example.final_project_android.databinding.DetailsSaveBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FlightDetailsFragment extends Fragment {

    RecyclerView.Adapter myAdapter;
    Flight selected;
    ArrayList<Flight> theFlights;
    ArrayList<Flight> savedFlights;
    DetailsSaveBinding binding;
    FlightDAO myDAO;
    int position;
    FlightTrackerViewModel flightModel;

    public FlightDetailsFragment(Flight f, ArrayList<Flight> theFlights, int position, RecyclerView.Adapter myAdapter, FlightDAO myDAO) {
        selected = f;
        this.theFlights = theFlights;
        this.position = position;
        this.myAdapter = myAdapter;
        this.myDAO = myDAO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DetailsSaveBinding.inflate(inflater, container, false);

        binding.destinationText.setText("Destination: " + selected.destination);
        binding.terminalText.setText("Terminal: " + selected.terminal);
        binding.gateText.setText("Gate: " + selected.gate);
        binding.delayText.setText("Delay: " + selected.delay );

        binding.saveButton.setOnClickListener( click -> {
            Toast.makeText(getActivity(), "You just clicked the save button!",
                    Toast.LENGTH_LONG).show();
//            selected.setSearched(false);
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                myDAO.insertFlight(selected);
            });
        });

        return binding.getRoot();
    }
}
