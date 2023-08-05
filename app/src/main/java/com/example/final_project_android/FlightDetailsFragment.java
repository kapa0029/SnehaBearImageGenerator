package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.final_project_android.databinding.DetailsLayoutBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FlightDetailsFragment extends Fragment {

    Flight selected;
    DetailsLayoutBinding binding;
    FlightDatabase myDB;
    FlightDAO myDAO;

    public FlightDetailsFragment(Flight f, FlightDatabase fDB) {
        selected = f;
        myDB = fDB;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        myDB = Room.databaseBuilder(getContext(), FlightDatabase.class, "database-name").build();
        myDAO = myDB.fDAO();

        binding = DetailsLayoutBinding.inflate(inflater, container, false);
        binding.destinationText.setText(selected.destination);
        binding.terminalText.setText(selected.terminal);
        binding.gateText.setText(selected.gate);
        binding.delayText.setText(selected.delay);

        binding.saveButton.setOnClickListener( click -> {
            if (selected.id == 0) {
                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    selected.id = myDAO.insertFlight(selected);
                });

            }
        });

        binding.deleteButton.setOnClickListener( click -> {


        });

        return binding.getRoot();
    }
}
