package com.example.carpriceestimator.ui.recent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpriceestimator.R;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentRecycler> {
    @NonNull
    @Override
    public RecentRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_details_card,parent,false);
        return new RecentRecycler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRecycler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class RecentRecycler extends RecyclerView.ViewHolder {
        public RecentRecycler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
