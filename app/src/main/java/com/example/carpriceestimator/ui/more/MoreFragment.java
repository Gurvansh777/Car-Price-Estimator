package com.example.carpriceestimator.ui.more;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.FlashScreenActivity;
import com.example.carpriceestimator.R;
import com.example.carpriceestimator.ui.home.HomeViewModel;
/**
 * This fragment set up the more tab
 */
public class MoreFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SharedPreferences sharedPreferences;
    SeekBar sbRecentRecords;
    int recentRecords = 5;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.more_fragment, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        recentRecords = sharedPreferences.getInt(Constants.RECENT_RECORDS, 5);

        Button tvLogout = root.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(view -> logout());

        TextView tvRecentRecords = root.findViewById(R.id.tvRecentRecords);
        tvRecentRecords.setText("Recent Records : " + recentRecords);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        sbRecentRecords = root.findViewById(R.id.sbRecentRecords);
        sbRecentRecords.setProgress(recentRecords);

        sbRecentRecords.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                recentRecords = i;
                tvRecentRecords.setText("Recent Records : " + recentRecords);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putInt(Constants.RECENT_RECORDS, recentRecords);
                spEditor.apply();
                homeViewModel.deleteNotRecentCars(recentRecords);
            }
        });
        return root;
    }

    /**
     * Method to process logout of user
     */
    private void logout() {
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(getActivity(), FlashScreenActivity.class));
        getActivity().finish();
    }


}