package com.example.final_project_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.final_project_android.databinding.InformationBinding;
import com.example.final_project_android.databinding.ActivityHistoryRoomBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class HistoryRoom extends AppCompatActivity {



    static RecyclerView.Adapter myAdapter;
    ActivityHistoryRoomBinding HRbinding;

    ArrayList<History> allstuff = new ArrayList<>();

    int currentPosition = 0;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // HistoryDatabase db = Room.databaseBuilder(getApplicationContext(), HistoryDatabase.class, "database-name").build();
        // myDAO = myDB.HDAO();
        // myDB = db; // Assign the created database instance to myDB
        //  myDAO = myDB.HDAO();



        HRbinding = ActivityHistoryRoomBinding.inflate(getLayoutInflater());
        setContentView(HRbinding.getRoot());

        /*HRbinding.button.setOnClickListener(c -> {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {

                // Fetch all data from the database and populate the allstuff list
                List<History> histories = MainActivity.myDAO.getAllHistory();
               // History histories = MainActivity.myDAO.getNewestHistory();
                allstuff.clear();
                allstuff.addAll(histories);*/



        // Update the RecyclerView on the main thread
        // runOnUiThread(() -> {
        //HRbinding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
        RecyclerView.Adapter<MyRowHolder> myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                InformationBinding Ibinding = InformationBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(Ibinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                History obj = allstuff.get(position);
                holder.OriNumText.setText(obj.OriginalNumber);
                holder.OriCurrencyText.setText(obj.OriginalCurrency);
                holder.ConNumText.setText(obj.ConvertedNumber);
                holder.ConCurrencyText.setText(obj.ConvertedCurrency);
            }

            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getItemCount() {
                return allstuff.size();
            }

        };


        HRbinding.recyclerview.setAdapter(myAdapter);

        HRbinding.button.setOnClickListener(c -> {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                // Fetch all data from the database and populate the allstuff list
                List<History> histories = MainActivity.myDAO.getAllHistory();
                allstuff.clear();
                allstuff.addAll(histories);

                // Update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    // Notify the adapter that the data has changed
                    myAdapter.notifyDataSetChanged();
                });
            });



            // myAdapter.notifyDataSetChanged();
            //});
        });
        // });


        //myAdapter.notifyDataSetChanged();




        // HRbinding.recyclerview.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
        //   });
        HRbinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }


}



class MyRowHolder extends RecyclerView.ViewHolder {
    TextView OriNumText;
    TextView OriCurrencyText;
    TextView ConNumText;
    TextView ConCurrencyText;

    public MyRowHolder(@NonNull View itemView) {
        super(itemView);
        OriNumText=itemView.findViewById(R.id.OriNum);
        OriCurrencyText=itemView.findViewById(R.id.OriCurrency);
        ConNumText=itemView.findViewById(R.id.ConNum);
        ConCurrencyText=itemView.findViewById(R.id.ConCurrency);
    }
}
