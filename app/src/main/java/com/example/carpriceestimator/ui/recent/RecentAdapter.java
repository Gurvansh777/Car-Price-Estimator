package com.example.carpriceestimator.ui.recent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpriceestimator.R;
import com.example.carpriceestimator.entity.DecodedCar;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentRecycler> {

    List<DecodedCar> carList;

    public RecentAdapter(List<DecodedCar> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public RecentRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_layout, parent, false);
        RecentRecycler viewHolder = new RecentRecycler(itemView);
        viewHolder.vin = itemView.findViewById(R.id.carVIN);
        viewHolder.bodyClass = itemView.findViewById(R.id.textViewCarBodyClassData);
        viewHolder.make = itemView.findViewById(R.id.textViewCarMakeData);
        viewHolder.model = itemView.findViewById(R.id.textViewCarModelData);
        viewHolder.modelYear = itemView.findViewById(R.id.textViewCarModelYearData);
        viewHolder.doors = itemView.findViewById(R.id.textViewDoorsData);
        viewHolder.carMake = itemView.findViewById(R.id.carName);
        viewHolder.manufacturer = itemView.findViewById(R.id.textViewCarManufactureNameData);
        viewHolder.price = itemView.findViewById(R.id.textViewPrice);
        viewHolder.shimmerLayout = itemView.findViewById(R.id.facebookShimmerLayoutPrice);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRecycler holder, int position) {
        holder.manufacturer.setText(carList.get(position).getManufactureName());
        holder.doors.setText(String.valueOf(carList.get(position).getDoors()));
        holder.modelYear.setText(String.valueOf(carList.get(position).getModelYear()));
        holder.bodyClass.setText(carList.get(position).getBodyClass());
        holder.make.setText(carList.get(position).getMake());
        holder.vin.setText(carList.get(position).getVin());
        holder.model.setText(carList.get(position).getModel());
        holder.carMake.setText(carList.get(position).getMake() + " " + carList.get(position).getModel());
        holder.price.setText("Estimated price: $" + carList.get(position).getPrice());
        holder.shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class RecentRecycler extends RecyclerView.ViewHolder {
        TextView make, model, modelYear, bodyClass, vin, doors, manufacturer, price, carMake;
        ShimmerFrameLayout shimmerLayout;

        public RecentRecycler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
