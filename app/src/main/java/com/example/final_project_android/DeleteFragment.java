package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.databinding.DetailsDeleteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A fragment for deleting flight information from the application.
 * This fragment allows the user to delete a selected flight, providing options for confirmation and undoing the deletion.
 */
public class DeleteFragment extends Fragment {
    /**
     * Adapter for the list of flights.
     */
    RecyclerView.Adapter listAdapter;

    /**
     * The selected flight to be deleted.
     */
    Flight selected;

    /**
     * List of saved flights.
     */
    ArrayList<Flight> savedFlights;


    /**
     * Binding instance for accessing layout elements of the delete fragment.
     */
    DetailsDeleteBinding binding;

    /**
     * Data Access Object (DAO) for performing database operations.
     */
    FlightDAO myDAO;

    /**
     * Position of the selected flight in the list.
     */
    int position1;

    /**
     * Constructs a new DeleteFragment instance.
     *
     * @param f The Flight object to be deleted.
     * @param savedFlights The list of saved flights from which the flight will be deleted.
     * @param position1 The position of the flight to be deleted in the saved flights list.
     * @param listAdapter The RecyclerView adapter for displaying the list of flights.
     * @param myDAO The Data Access Object (DAO) used to interact with flight data in the database.
     */
    public DeleteFragment(Flight f, ArrayList<Flight> savedFlights, int position1, RecyclerView.Adapter listAdapter, FlightDAO myDAO) {
        selected = f;
        this.savedFlights = savedFlights;
        this.position1 = position1;
        this.listAdapter = listAdapter;
        this.myDAO = myDAO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using the binding
        binding = DetailsDeleteBinding.inflate(inflater, container, false);

        // Populate UI elements with flight details
        binding.destinationText.setText("Destination: " + selected.destination);
        binding.terminalText.setText("Terminal: " + selected.terminal);
        binding.gateText.setText("Gate: " + selected.gate);
        binding.delayText.setText("Delay: " + selected.delay + " minutes" );

        // Set up the delete button click listener
        binding.deleteButton.setOnClickListener( click -> {
            // Build a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
            builder.setMessage( "Do you want to delete the flight?")
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, cl) -> { } )
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        // Perform flight deletion
                        Flight toDelete = savedFlights.get(position1);
                        savedFlights.remove(position1);
                        listAdapter.notifyItemRemoved(position1);

                        // Delete the flight from the database using a background thread
                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(() -> {
                            myDAO.deleteFlight(selected);
                        });

                        // Show a Snackbar with the option to undo the deletion
                        Snackbar.make(binding.deleteButton, "You deleted flight #" + position1, Snackbar.LENGTH_LONG)
                                .setAction("Undo", clk -> {
                                    savedFlights.add(position1, toDelete);
                                    listAdapter.notifyItemInserted(position1);
                                    Executor myThread = Executors.newSingleThreadExecutor();
                                    myThread.execute( () -> {
                                        myDAO.insertFlight(toDelete);
                                    });
                                })
                                .show();
                    })
                    .create().show();
        });
        return binding.getRoot();
    }
}
