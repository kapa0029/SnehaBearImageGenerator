package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.final_project_android.databinding.DetailsSaveBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for displaying flight details and allowing the user to save the flight.
 * This fragment provides information about a selected flight and enables users to save the flight to the database.
 */
public class FlightDetailsFragment extends Fragment {

    /**
     * The selected flight for which details are displayed.
     */
    Flight selected;

    /**
     * Binding instance for accessing layout elements of the fragment.
     */
    DetailsSaveBinding binding;

    /**
     * Data Access Object (DAO) for performing database operations.
     */
    FlightDAO myDAO;

    /**
     * Constructs a new FlightDetailsFragment instance.
     *
     * @param f The Flight object for which details will be displayed.
     * @param myDAO The Data Access Object (DAO) used to interact with flight data in the database.
     */
    public FlightDetailsFragment(Flight f, FlightDAO myDAO) {
        selected = f;
        this.myDAO = myDAO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using the binding
        binding = DetailsSaveBinding.inflate(inflater, container, false);

        // Populate UI elements with flight details
        binding.destinationText.setText("Destination: " + selected.destination);
        binding.terminalText.setText("Terminal: " + selected.terminal);
        binding.gateText.setText("Gate: " + selected.gate);
        binding.delayText.setText("Delay: " + selected.delay + " minutes" );

        // Set up the save button click listener
        binding.saveButton.setOnClickListener( click -> {
            // Show a toast indicating that the flight has been saved
            Toast.makeText(getActivity(), "You saved the flight!",
                    Toast.LENGTH_LONG).show();

            // Insert the selected flight into the database using a background thread
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {

                    selected.id = myDAO.insertFlight(selected);
            });
        });
        return binding.getRoot();
    }
}
