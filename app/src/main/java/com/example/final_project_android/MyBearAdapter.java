package com.example.final_project_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyBearAdapter extends RecyclerView.Adapter<MyBearViewHolder> {

    Context context;
    List<BearItems> bearItems;

    public MyBearAdapter(Context context, List<BearItems> bearItems) {
        this.context = context;
        this.bearItems = bearItems;
    }

    @NonNull
    @Override
    public MyBearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBearViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bear_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBearViewHolder holder, int position) {
        holder.savedImageView.setImageResource(bearItems.get(position).getImage());
        holder.savedHeightView.setText("Height"+ String.valueOf(bearItems.get(position).getHeight()));
        holder.savedWidthView.setText("Width: "+String.valueOf(bearItems.get(position).getWidth()));
    }

    @Override
    public int getItemCount() {
        return bearItems.size();
    }
    public void clearData() {
        bearItems.clear();
        notifyDataSetChanged();
    }
}
