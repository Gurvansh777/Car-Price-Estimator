package com.example.carpriceestimator.ui.recent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpriceestimator.R;

public class RecentFragment extends Fragment {

    private RecentViewModel recentViewModel;

    //private List<DecodedCar> decodedCarList = new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recent_fragment, container, false);
//        decodedCarList.add(new DecodedCar("JHLRD78834C802452","HONDA", "HONDA", "CRV", "2020", "SUV", 4, 0));
//        decodedCarList.add(new DecodedCar("JHLRD78834C802452","TOYOTA", "TOYOTA", "RAV4", "2020", "SUV", 4, 0));
//        decodedCarList.add(new DecodedCar("JHLRD78834C802452","CHEVY", "CHEVY", "CRUZE", "2020", "SUV", 4, 0));
//        decodedCarList.add(new DecodedCar("JHLRD78834C802452","TESLA", "TESLA", "MODEL 3", "2020", "SUV", 4, 0));
        recentViewModel = new ViewModelProvider(this).get(RecentViewModel.class);
        recentViewModel.getDecodedCarsList().observe(getViewLifecycleOwner(), decodedCars -> {
            RecentAdapter recentAdapter = new RecentAdapter(decodedCars);
            RecyclerView recent_recycler_view = root.findViewById(R.id.recyclerViewRecentScans);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            recent_recycler_view.setLayoutManager(linearLayoutManager);
            recent_recycler_view.setAdapter(recentAdapter);
        });

        return root;
    }

}