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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.datamodels.Vehicles;

import java.util.List;

public class SearchVehicleListAdapter extends RecyclerView.Adapter<SearchVehicleListAdapter.SearchVehicleViewHolder>{
    List<Vehicles> mList;
    Context mContext;
    Fragment mFragment;

    public SearchVehicleListAdapter(List<Vehicles> mList, Context mContext, Fragment mFragment) {
        this.mList = mList;
        this.mContext = mContext;
        this.mFragment = mFragment;
    }

    @NonNull
    @Override
    public SearchVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_driver,parent,false);
        return new SearchVehicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVehicleViewHolder holder, int position) {
        Vehicles curr = mList.get(position);
        holder.oname.setText(curr.getName());
        holder.dname.setText(curr.getPlate_no().toUpperCase());
        holder.cardcl.setOnClickListener(v ->{
            Bundle args = new Bundle();
            args.putInt("vid",mList.get(position).getV_id());
            /*Navigation.findNavController(mFragment.getActivity(),R.id.nav_host_fragment)
                    .navigate(R.id.action_viewDetailsFragment_to_navigation_home,args);*/
            NavHostFragment.findNavController(mFragment).navigate(R.id.action_viewDetailsFragment_to_navigation_home,args);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class SearchVehicleViewHolder extends RecyclerView.ViewHolder{
        TextView dname,oname;
        ConstraintLayout cardcl;
        public SearchVehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            dname = itemView.findViewById(R.id.drivername);
            oname = itemView.findViewById(R.id.organizationname);
            cardcl = itemView.findViewById(R.id.drivercardcl);
        }
    }
}
