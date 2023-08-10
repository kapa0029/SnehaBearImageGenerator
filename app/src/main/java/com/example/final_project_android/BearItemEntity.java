package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity class representing a bear item in the database.
 */
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

    /**
     * Constructor to create a new BearItemEntity.
     *
     * @param image  The byte array representing the bear image.
     * @param width  The width of the bear image.
     * @param height The height of the bear image.
     */
    public BearItemEntity(byte[] image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the byte array representing the bear image.
     *
     * @param image The byte array representing the bear image.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Sets the width of the bear image.
     *
     * @param width The width of the bear image.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height of the bear image.
     *
     * @param height The height of the bear image.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Retrieves the byte array representing the bear image.
     *
     * @return The byte array representing the bear image.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Retrieves the width of the bear image.
     *
     * @return The width of the bear image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the bear image.
     *
     * @return The height of the bear image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the ID of the bear item.
     *
     * @return The ID of the bear item.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the bear item.
     *
     * @param id The ID of the bear item.
     */
    public void setId(int id) {
        this.id = id;
    }

}
