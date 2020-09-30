package com.indiaactive.vehicle.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.datamodels.VehicleData;
import com.indiaactive.vehicle.datamodels.VehicleType;
import com.indiaactive.vehicle.dialogs.FilterPopup;
import com.indiaactive.vehicle.ui.home.HomeFragment;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewModel> {
    List<VehicleType> mList;
    Context mContext;
    HomeFragment mFragment;
    String url;
    public VehicleAdapter(List<VehicleType> mList, Context mContext, HomeFragment mFragment,String url) {
        this.mList = mList;
        this.mContext = mContext;
        this.mFragment = mFragment;
        this.url = url;
    }

    @NonNull
    @Override
    public VehicleViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewModel(LayoutInflater.from(mContext).inflate(R.layout.card_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewModel holder, int position) {
        VehicleType vehicleType = mList.get(position);
        try{
            holder.cardText.setText(vehicleType.getVehicle_type_name());
            GlideToVectorYou.init()
                    .with(mContext)
                    .setPlaceHolder(R.drawable.ic_master_loading,R.drawable.ic_no_camera)
                    .load(Uri.parse(url),holder.cardImage);
        }catch (Exception e){ /*eat exception */}
        holder.cardLayout.setOnClickListener(v -> {
            try {
                //mFragment.initFilterPage(mList.get(position).getId());
                FilterPopup fp = new FilterPopup(mFragment,mList.get(position).getId());
                AppCompatActivity activity = (AppCompatActivity) mContext;
                fp.show(activity.getSupportFragmentManager(),"filter");
            }catch (Exception e) { }
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
