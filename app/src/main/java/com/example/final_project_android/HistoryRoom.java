package com.example.final_project_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project_android.data.MainActivityViewModel;
import com.example.final_project_android.databinding.InformationBinding;
import com.example.final_project_android.databinding.ActivityHistoryRoomBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HistoryRoom extends AppCompatActivity {

    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private ActivityHistoryRoomBinding HRbinding;

    private ArrayList<History> allstuff = new ArrayList<>();

    private int currentPosition = 0;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HRbinding = ActivityHistoryRoomBinding.inflate(getLayoutInflater());
        setContentView(HRbinding.getRoot());

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
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
                List<History> histories = CurrencyMainActivity.myDAO.getAllHistory();
                allstuff.clear();
                allstuff.addAll(histories);

                // Update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    // Notify the adapter that the data has changed
                    myAdapter.notifyDataSetChanged();
                });
            });
        });

        HRbinding.recyclerview.setLayoutManager(new LinearLayoutManager(HistoryRoom.this));

        MainActivityViewModel.selectedMessage.observe(this, (newMessageValue) -> {
            if (newMessageValue != null) {
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.replace(R.id.frameLayout, chatFragment);
                tx.addToBackStack("");
                tx.commit();
            }
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView OriNumText;
        TextView OriCurrencyText;
        TextView ConNumText;
        TextView ConCurrencyText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            OriNumText = itemView.findViewById(R.id.OriNum);
            OriCurrencyText = itemView.findViewById(R.id.OriCurrency);
            ConNumText = itemView.findViewById(R.id.ConNum);
            ConCurrencyText = itemView.findViewById(R.id.ConCurrency);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                History selected = allstuff.get(position);
                MainActivityViewModel.selectedMessage.postValue(selected);
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryRoom.this);
            builder.setMessage("Do you want to delete the message")
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        int position = getAbsoluteAdapterPosition();
                        History removedMessage = allstuff.get(position);
                        allstuff.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        Snackbar.make(OriNumText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", ck -> {
                                    allstuff.add(position, removedMessage);
                                    myAdapter.notifyItemInserted(position);
                                }).show();
                    }).create().show();
        }
    }
}