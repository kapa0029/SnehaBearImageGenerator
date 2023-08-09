package com.example.final_project_android;

/**
 * Interface for handling clicks on topics.
 */
public interface TopicClickListener {
    /**
     * Called when a topic is clicked.
     *
     * @param topic The clicked topic.
     */
    void onTopicClicked(Topic topic);
}

