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

public class DeleteFragment extends Fragment {
    RecyclerView.Adapter myAdapter;
    Flight selected;
    ArrayList<Flight> theFlights;
    DetailsDeleteBinding binding;
    FlightDAO myDAO;
    int position;

    public DeleteFragment(Flight f, ArrayList<Flight> theFlights, int position, RecyclerView.Adapter myAdapter, FlightDAO myDAO) {
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

        binding = DetailsDeleteBinding.inflate(inflater, container, false);

        binding.destinationText.setText("Destination: " + selected.destination);
        binding.terminalText.setText("Terminal: " + selected.terminal);
        binding.gateText.setText("Gate: " + selected.gate);
        binding.delayText.setText("Delay: " + selected.delay );

        binding.deleteButton.setOnClickListener( click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
            builder.setMessage( "Do you want to delete the flight?")
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, cl) -> { } )
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        Flight toDelete = theFlights.get(position);
                        theFlights.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(() -> {
                            myDAO.deleteFlight(selected);
                        });

                        Snackbar.make(binding.deleteButton, "You deleted flight #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", clk -> {
                                    theFlights.add(position, toDelete);
                                    myAdapter.notifyItemInserted(position);
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
