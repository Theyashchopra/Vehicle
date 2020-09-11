package com.lifecapable.vehicle.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lifecapable.vehicle.R;
import com.lifecapable.vehicle.datamodels.VehicleData;
import com.lifecapable.vehicle.ui.home.HomeFragment;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewModel> {
    List<VehicleData> mList;
    Context mContext;
    HomeFragment mFragment;

    public VehicleAdapter(List<VehicleData> mList, Context mContext, HomeFragment mFragment) {
        this.mList = mList;
        this.mContext = mContext;
        this.mFragment = mFragment;
    }

    @NonNull
    @Override
    public VehicleViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewModel(LayoutInflater.from(mContext).inflate(R.layout.card_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewModel holder, int position) {
        final VehicleData currData = mList.get(position);
        holder.cardText.setText(currData.getVehicleName());
        holder.cardImage.setImageResource(currData.getVehicleImage());
        holder.cardLayout.setOnClickListener(v -> {
            mFragment.initFilterPage("TAta");
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class VehicleViewModel extends RecyclerView.ViewHolder{
        ImageView cardImage;
        TextView cardText;
        ConstraintLayout cardLayout;
        public VehicleViewModel(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardimage);
            cardText = itemView.findViewById(R.id.cardtext);
            cardLayout = itemView.findViewById(R.id.cardlayout);
        }
    }
}
