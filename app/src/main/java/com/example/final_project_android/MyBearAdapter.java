package com.example.final_project_android;

import android.content.Context;
import android.content.res.Resources;
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

    /**
     * Constructor for MyBearAdapter.
     *
     * @param context            The context of the application.
     * @param bearItems          The list of BearItemEntity objects to display.
     * @param bearItemRepository The repository for database operations.
     */
    public MyBearAdapter(Context context, List<BearItemEntity> bearItems, BearItemRepository bearItemRepository) {
        this.context = context;
        this.bearItems = bearItems;
        this.bearItemRepository = bearItemRepository;
    }
    /**
     * Sets the click listener for item clicks.
     *
     * @param listener The click listener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    /**
     * Sets the long click listener for item long clicks.
     *
     * @param listener The long click listener to set.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * This is myBearViewHolder.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public MyBearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bear_image, parent, false);
        MyBearViewHolder viewHolder = new MyBearViewHolder(itemView);
        viewHolder.setOnItemLongClickListener(onItemLongClickListener);
        viewHolder.setOnItemClickListener(onItemClickListener); // Set the OnItemClickListener for the ViewHolder
        return viewHolder;
    }

    /**
     * Interface for on item click listner
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    /**
     * This is method to delete Item
     * @param position refers to the position
     */
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

    /**
     * This onBindViewHolder.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyBearViewHolder holder, int position) {
        // Convert the byte array back to a Bitmap
        byte[] imageByteArray = bearItems.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        holder.savedImageView.setImageBitmap(bitmap);
        Resources resources = context.getResources();
        holder.savedHeightView.setText(resources.getString(R.string.height_header_text) +": "+ String.valueOf(bearItems.get(position).getHeight()));
        holder.savedWidthView.setText(resources.getString(R.string.width_header_text) +": "+ String.valueOf(bearItems.get(position).getWidth()));
    }

    /**
     * Interface for handling item long click events.
     */
    @Override
    public int getItemCount() {
        return bearItems.size();
    }


    /**
     * Interface for handling item click events.
     */

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
