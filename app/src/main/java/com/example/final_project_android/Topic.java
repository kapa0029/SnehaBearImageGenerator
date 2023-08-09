package com.example.final_project_android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a topic that can be displayed in the app.
 * Implements Parcelable to allow passing Topic objects between activities.
 */
public class Topic implements Parcelable {

    private int id;
    private String title;
    private String description;

    /**
     * Constructor to create a Topic instance.
     *
     * @param id          The unique identifier for the topic.
     * @param title       The title of the topic.
     * @param description The description of the topic.
     */
    public Topic(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    /**
     * Get the unique identifier of the topic.
     *
     * @return The topic's identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the title of the topic.
     *
     * @return The topic's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the description of the topic.
     *
     * @return The topic's description.
     */
    public String getDescription() {
        return description;
    }

    // Parcelable methods

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return A bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object into the specified parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
    }

    /**
     * Parcelable.Creator that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    /**
     * Private constructor that takes a Parcel and creates a Topic object from it.
     *
     * @param in The Parcel containing the object's data.
     */
    private Topic(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
    }
}
