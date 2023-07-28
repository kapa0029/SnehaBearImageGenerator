package com.example.final_project_android;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyBearViewHolder extends RecyclerView.ViewHolder {

    ImageView savedImageView;
    TextView savedWidthView;
    TextView savedHeightView;

    public MyBearViewHolder(@NonNull View itemView) {
        super(itemView);
        savedImageView = itemView.findViewById(R.id.savedBearImage);
        savedWidthView = itemView.findViewById(R.id.savedWidth);
        savedHeightView = itemView.findViewById(R.id.savedHeight);
    }

}
