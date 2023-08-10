package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.final_project_android.databinding.DetailsLayoutBinding;

/**
 * A fragment to display details of a selected message entry.
 */

public class MessageDetailsFragment extends Fragment{


    History selected;
    /**
     * Constructs a new instance of the MessageDetailsFragment with the selected history entry.
     *
     * @param m The selected history entry.
     */
    public MessageDetailsFragment(History m){
        selected=m;
    }

    /**
     * Creates and inflates the view for the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.OrinalCurrency.setText(selected.OriginalCurrency);
        binding.OrinialNum.setText(selected.OriginalNumber);
        binding.ConvertedNum.setText(selected.ConvertedNumber);
        binding.ConvertedCurrency.setText(selected.ConvertedCurrency);
        binding.id.setText("Id="+selected.id);
        return binding.getRoot();

    }
}
