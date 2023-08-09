package com.example.final_project_android.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project_android.History;


import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
  //public static MutableLiveData<ChatMessage> selectedMessage= new MutableLiveData< >();
  public static ArrayList<History> histotry = new ArrayList<>();
  public static MutableLiveData<History> selectedMessage = new MutableLiveData<>();
  //public static ArrayList<ChatMessage> messages = new ArrayList<>();
 // public static MutableLiveData<List<History>> messages = new MutableLiveData<>();
 // public static MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData<>();
  //public MutableLiveData<ArrayList<String>> messages = new MutableLiveData< >();
}
