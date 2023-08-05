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
    private MyBearAdapter.OnItemClickListener onItemClickListener; // Add this line

    public MyBearViewHolder(@NonNull View itemView) {
        super(itemView);
        savedImageView = itemView.findViewById(R.id.savedBearImage);
        savedWidthView = itemView.findViewById(R.id.savedWidth);
        savedHeightView = itemView.findViewById(R.id.savedHeight);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(MyBearAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
