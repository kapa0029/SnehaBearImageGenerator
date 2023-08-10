package com.example.final_project_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A fragment that displays details of a bear image, including width, height, and the image itself.
 */
public class BearImageDetailsFragment extends Fragment {
    /**
     * The key for the width argument when creating a new instance of {@link BearImageDetailsFragment}.
     */
    private static final String ARG_WIDTH = "width";

    /**
     * The key for the height argument when creating a new instance of {@link BearImageDetailsFragment}.
     */
    private static final String ARG_HEIGHT = "height";

    /**
     * The key for the image argument when creating a new instance of {@link BearImageDetailsFragment}.
     */
    private static final String ARG_IMAGE = "image";


    /**
     * Creates a new instance of the BearImageDetailsFragment with the provided width, height, and image.
     *
     * @param width  The width of the bear image.
     * @param height The height of the bear image.
     * @param image  The byte array representing the image.
     * @return A new instance of BearImageDetailsFragment.
     */
    public static BearImageDetailsFragment newInstance(int width, int height, byte[] image) {
        BearImageDetailsFragment fragment = new BearImageDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH, width);
        args.putInt(ARG_HEIGHT, height);
        args.putByteArray(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bear_details, container, false);

        // Get references to views
        ImageView imageView = rootView.findViewById(R.id.imageViewBear);
        TextView widthTextView = rootView.findViewById(R.id.textViewWidth);
        TextView heightTextView = rootView.findViewById(R.id.textViewHeight);

        // Retrieve arguments
        if (getArguments() != null) {
            int width = getArguments().getInt(ARG_WIDTH);
            int height = getArguments().getInt(ARG_HEIGHT);
            byte[] imageByteArray = getArguments().getByteArray(ARG_IMAGE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

            // Set image and text views
            imageView.setImageBitmap(bitmap);
            String widthText = getResources().getString(R.string.width_header_text);
            String heightText = getResources().getString(R.string.height_header_text);
            widthTextView.setText(widthText + ": " + width);
            heightTextView.setText(heightText + ": " + height);

            // Log details for debugging
            Log.d("BearImageDetails", "Width: " + width);
            Log.d("BearImageDetails", "Height: " + height);
            Log.d("BearImageDetails", "Image byte array length: " + imageByteArray.length);
        }

        return rootView;
    }
}
