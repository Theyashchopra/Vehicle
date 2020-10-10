package com.indiaactive.vehicle.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.datamodels.Vehicles;

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
        holder.dname.setText(curr.getPlate_no().toUpperCase());
        holder.cardcl.setOnClickListener(v ->{
            Bundle args = new Bundle();
            args.putInt("vid",mList.get(position).getV_id());
            Navigation.findNavController(mFragment.getActivity(),R.id.nav_host_fragment)
                    .navigate(R.id.action_navigation_home_to_viewDetailsFragment,args);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder{
        TextView dname,oname;
        ConstraintLayout cardcl;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            dname = itemView.findViewById(R.id.drivername);
            oname = itemView.findViewById(R.id.organizationname);
            cardcl = itemView.findViewById(R.id.drivercardcl);
        }
    }
}
