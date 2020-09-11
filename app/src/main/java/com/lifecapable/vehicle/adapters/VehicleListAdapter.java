package com.lifecapable.vehicle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lifecapable.vehicle.R;
import com.lifecapable.vehicle.datamodels.Vehicles;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.DriverViewHolder> {
    List<Vehicles> mList;
    Context mContext;
    Fragment mFragment;

    public VehicleListAdapter(List<Vehicles> mList, Context mContext, Fragment mFragment) {
        this.mList = mList;
        this.mContext = mContext;
        this.mFragment = mFragment;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_driver,parent,false);
        return new DriverViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Vehicles curr = mList.get(position);
        holder.oname.setText(curr.getName());
        holder.dname.setText(curr.getPlateNumber());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder{
        TextView dname,oname;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            dname = itemView.findViewById(R.id.drivername);
            oname = itemView.findViewById(R.id.organizationname);
        }
    }
}
