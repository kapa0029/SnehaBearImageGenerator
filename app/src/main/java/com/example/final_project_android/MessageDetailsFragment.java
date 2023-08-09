package com.example.final_project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.final_project_android.databinding.DetailsLayoutBinding;



public class MessageDetailsFragment extends Fragment{


    History selected;

    public MessageDetailsFragment(History m){
        selected=m;
    }
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
