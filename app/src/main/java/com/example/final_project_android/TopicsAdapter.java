package com.example.final_project_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class for displaying a list of topics in a RecyclerView.
 */
public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {

    private List<Topic> topics;
    private TopicClickListener listener;
    private int[] topicColors = {
            Color.parseColor("#FFC0CB"), // Color for Topic 1
            Color.parseColor("#FFDAB9"), // Color for Topic 2
            Color.parseColor("#F37445")  // Color For Topic 3
    };

    /**
     * Constructor for creating a TopicsAdapter instance.
     *
     * @param topics   The list of topics to be displayed.
     * @param listener The listener for topic click events.
     */
    public TopicsAdapter(List<Topic> topics, TopicClickListener listener) {
        this.topics = topics;
        this.listener = listener;
    }

    /**
     * Called when creating a new ViewHolder instance.
     *
     * @param parent   The parent view group.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called when binding data to a ViewHolder instance.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.textTitle.setText(topic.getTitle());
        holder.textDescription.setText(topic.getDescription());
        int colorIndex = position % topicColors.length;
        holder.itemView.setBackgroundColor(topicColors[colorIndex]);

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTopicClicked(topic);
            }
        });
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return topics.size();
    }

    /**
     * ViewHolder class for holding the views of a single topic item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDescription;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
