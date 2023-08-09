package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bear_table")
public class BearItemEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
            private int id;
    @ColumnInfo(name = "image")
    byte[] image;
    @ColumnInfo(name = "width")
    int width;
    @ColumnInfo(name = "height")
    int height;


    public BearItemEntity(byte[] image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[] getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
