package com.example.final_project_android.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project_android.Flight;

import java.util.ArrayList;

/**
 * ViewModel class for managing flight tracking data in the application.
 * This class extends the Android ViewModel class and provides LiveData instances to hold and observe
 * flight-related data such as the list of all flights, the currently selected flight, and a list of
 * saved flights.
 */
public class FlightTrackerViewModel extends ViewModel {

    /**
     * LiveData holding the list of all flights currently available.
     */
    public MutableLiveData<ArrayList<Flight>> theFlights = new MutableLiveData<>();

    /**
     * LiveData holding the currently selected flight.
     */
    public MutableLiveData<Flight> selectedFlight = new MutableLiveData<>();

    /**
     * LiveData holding the list of saved flights.
     */
    public MutableLiveData<ArrayList<Flight>> savedFlights = new MutableLiveData<>();
}
