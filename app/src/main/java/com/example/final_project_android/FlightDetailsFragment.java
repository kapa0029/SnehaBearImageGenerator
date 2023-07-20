package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.final_project_android.databinding.DetailsLayoutBinding;

public class FlightDetailsFragment extends Fragment {

    Flight selected;
    DetailsLayoutBinding binding;

    public FlightDetailsFragment(Flight f) {
        selected = f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DetailsLayoutBinding.inflate(inflater, container, false);


        binding.destinationText.setText( selected.destination );
        binding.terminalText.setText(selected.terminal);
        binding.gateText.setText(selected.gate);
        binding.delayText.setText(selected.delay);

        return binding.getRoot();
    }
}
