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

public class BearImageDetailsFragment extends Fragment {
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_IMAGE = "image";

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
        View rootView = inflater.inflate(R.layout.fragment_bear_details, container, false);

        ImageView imageView = rootView.findViewById(R.id.imageViewBear);
        TextView widthTextView = rootView.findViewById(R.id.textViewWidth);
        TextView heightTextView = rootView.findViewById(R.id.textViewHeight);

        if (getArguments() != null) {
            int width = getArguments().getInt(ARG_WIDTH);
            int height = getArguments().getInt(ARG_HEIGHT);
            byte[] imageByteArray = getArguments().getByteArray(ARG_IMAGE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

            imageView.setImageBitmap(bitmap);
            String widthText = getResources().getString(R.string.width_header_text);
            String heightText = getResources().getString(R.string.height_header_text);
            widthTextView.setText(widthText+": " + width);
            heightTextView.setText(heightText + ": "+ height);

            Log.d("BearImageDetails", "Width: " + width);
            Log.d("BearImageDetails", "Height: " + height);
            Log.d("BearImageDetails", "Image byte array length: " + imageByteArray.length);
        }

        return rootView;
    }
}
