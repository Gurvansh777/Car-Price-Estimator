package com.example.carpriceestimator.ui.more;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.FlashScreenActivity;
import com.example.carpriceestimator.R;

public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.more_fragment, container, false);

        TextView tvLogout = root.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(view -> logout());
        return root;
    }

    private void logout() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(getActivity(), FlashScreenActivity.class));
        getActivity().finish();
    }


}