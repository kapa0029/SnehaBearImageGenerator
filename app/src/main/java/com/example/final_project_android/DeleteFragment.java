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
    RecyclerView.Adapter myAdapter1;
    Flight selected;
    ArrayList<Flight> savedFlights;
    DetailsDeleteBinding binding;
    FlightDAO myDAO;
    int position1;

    public DeleteFragment(Flight f, ArrayList<Flight> savedFlights, int position1, RecyclerView.Adapter myAdapter1, FlightDAO myDAO) {
        selected = f;
        this.savedFlights = savedFlights;
        this.position1 = position1;
        this.myAdapter1 = myAdapter1;
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
                        Flight toDelete = savedFlights.get(position1);
                        savedFlights.remove(position1);
                        myAdapter1.notifyItemRemoved(position1);

                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(() -> {
                            myDAO.deleteFlight(selected);
                        });

                        Snackbar.make(binding.deleteButton, "You deleted flight #" + position1, Snackbar.LENGTH_LONG)
                                .setAction("Undo", clk -> {
                                    savedFlights.add(position1, toDelete);
                                    myAdapter1.notifyItemInserted(position1);
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
