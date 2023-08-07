package com.example.final_project_android.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project_android.Flight;

import java.util.ArrayList;

public class FlightTrackerViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Flight>> theFlights = new MutableLiveData<>();
    public MutableLiveData<Flight> selectedFlight = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Flight>> savedFlights = new MutableLiveData<>();
}
