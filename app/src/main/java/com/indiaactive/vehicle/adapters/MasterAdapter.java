package com.indiaactive.vehicle.adapters;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.datamodels.MasterVehicle;
import com.indiaactive.vehicle.ui.home.HomeFragment;

import java.io.InputStream;
import java.util.List;

import okhttp3.RequestBody;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MasterModel>{
    List<MasterVehicle> masterModelList;
    Context context;
    RequestBuilder<PictureDrawable> requestBuilder;
    HomeFragment fragment;
    public MasterAdapter(List<MasterVehicle> masterModelList, Context context, HomeFragment fragment) {
        this.masterModelList = masterModelList;
        this.context = context;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public MasterModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MasterModel(LayoutInflater.from(context).inflate(R.layout.master_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MasterModel holder, int position) {
        MasterVehicle masterVehicle = masterModelList.get(position);
        try {
            holder.masterText.setText(masterVehicle.getName());
            GlideToVectorYou.init()
                    .with(context)
                    .setPlaceHolder(R.drawable.ic_master_loading,R.drawable.ic_no_camera)
                    .load(Uri.parse(masterVehicle.getImage()),holder.masterImage);

        }catch (Exception e){
            e.printStackTrace();
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.initSecondBottom(masterModelList.get(position).getId(),masterModelList.get(position).getImage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return masterModelList.size();
    }

    class MasterModel extends RecyclerView.ViewHolder{
        ImageView masterImage;
        TextView masterText;
        LinearLayout linearLayout;
        public MasterModel(@NonNull View itemView) {
            super(itemView);
            masterImage = itemView.findViewById(R.id.master_image);
            masterText = itemView.findViewById(R.id.master_name);
            linearLayout = itemView.findViewById(R.id.master_layout);
        }
    }
}
