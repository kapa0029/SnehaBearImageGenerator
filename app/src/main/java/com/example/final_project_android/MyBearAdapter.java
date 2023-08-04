package com.example.final_project_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyBearAdapter extends RecyclerView.Adapter<MyBearViewHolder> {

    Context context;
    List<BearItemEntity> bearItems;

    public MyBearAdapter(Context context, List<BearItemEntity> bearItems) {
        this.context = context;
        this.bearItems = bearItems;
    }

    @NonNull
    @Override
    public MyBearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBearViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bear_image, parent, false));
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
}
