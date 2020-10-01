package com.indiaactive.vehicle.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiaactive.vehicle.R;

import java.util.List;

public class VehicleImageAdapter extends RecyclerView.Adapter<VehicleImageAdapter.ImageHolder> {

    Context context;
    List<Bitmap> list;

    public VehicleImageAdapter(Context context, List<Bitmap> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Bitmap bitmap = list.get(position);
        if (bitmap == null){
            holder.imageView.setImageResource(R.drawable.ic_error_404);
        }else{
            holder.imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.vimage);
        }
    }
}
