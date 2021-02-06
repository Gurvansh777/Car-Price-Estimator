package com.example.carpriceestimator.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.R;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);

        final TextView textView = root.findViewById(R.id.tvHome);
        textView.setText(String.format("User: %s", sharedPreferences.getString(Constants.USER_EMAIL, "")));

        Button cameraButton = root.findViewById(R.id.btnCamera);
        TextView carName = root.findViewById(R.id.carName);
        TextView carVIN = root.findViewById(R.id.carVIN);

        //Dummy data to represent ui//
        carName.setText("Porsche Carrera 991");
        carVIN.setText("VIN:1HGBH41JXMN109186");

        cameraButton.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 123);
        });
        return root;
    }
}