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
    private MyBearAdapter.OnItemLongClickListener onItemLongClickListener; // Add this line
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
        // Add the long click listener to the itemView
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(getAdapterPosition());
                }
                return true; // Return true to indicate that the long click event is consumed
            }
        });
    }
    // Add a setter for the long click listener
    public void setOnItemLongClickListener(MyBearAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public void setOnItemClickListener(MyBearAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
