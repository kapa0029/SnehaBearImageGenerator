package com.example.final_project_android;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Topic implements Parcelable {
    private int id;
    private String title;
    private String description;

    public Topic(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);

    }
    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    // Constructor that takes a Parcel and creates a Topic object from it
    private Topic(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
    }


    public String getDescription() {
        return  description;
    }
}
