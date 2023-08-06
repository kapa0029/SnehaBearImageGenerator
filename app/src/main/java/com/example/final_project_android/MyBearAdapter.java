package com.example.final_project_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyBearAdapter extends RecyclerView.Adapter<MyBearViewHolder> {

    Context context;
    List<BearItemEntity> bearItems;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private BearItemRepository bearItemRepository;

    public MyBearAdapter(Context context, List<BearItemEntity> bearItems, BearItemRepository bearItemRepository) {
        this.context = context;
        this.bearItems = bearItems;
        this.bearItemRepository = bearItemRepository;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    @NonNull
    @Override
    public MyBearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bear_image, parent, false);
        MyBearViewHolder viewHolder = new MyBearViewHolder(itemView);
        viewHolder.setOnItemLongClickListener(onItemLongClickListener);
        viewHolder.setOnItemClickListener(onItemClickListener); // Set the OnItemClickListener for the ViewHolder
        return viewHolder;
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    // Method to delete a bear item from the database
    public void deleteItem(int position) {
        if (position >= 0 && position < bearItems.size()) {
            final BearItemEntity bearItem = bearItems.get(position);

            // Execute the database operation on a background thread
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    bearItemRepository.deleteBearImage(bearItem);
                }
            });
            thread.start();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyBearViewHolder holder, int position) {
        // Convert the byte array back to a Bitmap
        byte[] imageByteArray = bearItems.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        holder.savedImageView.setImageBitmap(bitmap);

        holder.savedHeightView.setText("Height: " + String.valueOf(bearItems.get(position).getHeight()));
        holder.savedWidthView.setText("Width: " + String.valueOf(bearItems.get(position).getWidth()));
    }

    @Override
    public int getItemCount() {
        return bearItems.size();
    }

    public void clearData() {
        bearItems.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
