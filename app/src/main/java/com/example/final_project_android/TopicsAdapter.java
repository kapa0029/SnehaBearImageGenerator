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

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder>{
    private List<Topic> topics;
    private TopicClickListener listener;
    private int[] topicColors = {
            Color.parseColor("#FFC0CB"), // Color for Topic 1
            Color.parseColor("#FFDAB9"), // Color for Topic 2
            Color.parseColor("#F37445"), // Color For Topic 3
    };


    public TopicsAdapter(List<Topic> topics, TopicClickListener listener) {
        this.topics = topics;
        this.listener = listener;
    }

    public TopicsAdapter(List<Question> questionList){
    }


    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return topics.size();
    }

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
