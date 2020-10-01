package com.indiaactive.vehicle.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.adapters.VehicleAdapter;
import com.indiaactive.vehicle.adapters.VehicleImageAdapter;
import com.indiaactive.vehicle.datamodels.ProfileOwnerData;
import com.indiaactive.vehicle.datamodels.VehicleDetailsData;
import com.indiaactive.vehicle.dialogs.RequestPopup;
import com.indiaactive.vehicle.interfaces.API;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDetailsFragment extends Fragment {
    
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Button call;
    View root;
    ImageView backbutton;
    RequestPopup rpop;
    int vid;
    List<Bitmap> bitmapList;
    VehicleImageAdapter vehicleImageAdapter;
    TextView name,plate,avail,model,kms,yom,owner,rent_per_day,rent_per_hour;
    String mobile,mobile2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_view_details, container, false);
        vid = -1;
        vid = getArguments().getInt("vid");
        init();
        backbutton.setOnClickListener(view -> Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigateUp());
        return root;
    }

    private void init(){
        progressBar = root.findViewById(R.id.progress);
        name = root.findViewById(R.id.viewmodel);
        plate = root.findViewById(R.id.platenumber);
        avail = root.findViewById(R.id.viewavailability);
        model = root.findViewById(R.id.viewcategory);
        kms = root.findViewById(R.id.totalkmscomp);
        owner = root.findViewById(R.id.organization);
        rent_per_day = root.findViewById(R.id.viewrentperday);
        rent_per_hour = root.findViewById(R.id.viewrentperhour);
        yom = root.findViewById(R.id.manufacturedin);
        bitmapList = new ArrayList<>();
        vehicleImageAdapter = new VehicleImageAdapter(getContext(),bitmapList);
        recyclerView = root.findViewById(R.id.viewvehimage);
        recyclerView.setAdapter(vehicleImageAdapter);
        recyclerView.scheduleLayoutAnimation();
        call = root.findViewById(R.id.call_owner);
        backbutton = root.findViewById(R.id.viewback);
        try {
            getData();
            getVback();
            getVside();
            getVfront();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listeners();
    }

    private void getData() throws Exception{
        if(vid == -1){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        API api = RestAdapter.createAPI();
        Call<VehicleDetailsData> call = api.getVehicleDetails(vid);
        call.enqueue(new Callback<VehicleDetailsData>() {
            @Override
            public void onResponse(Call<VehicleDetailsData> call, Response<VehicleDetailsData> response) {
                if(response.isSuccessful()){
                    VehicleDetailsData v = response.body();
                    if(v != null){
                        try{
                            progressBar.setVisibility(View.INVISIBLE);
                            name.setText(v.getName());
                            plate.setText(v.getPlate_no());
                            if(v.isAvailibility()){
                                avail.setText("Available");
                            }else{
                                avail.setText("Not Available");
                            }
                            getOwnerData(v.getOwner_id());
                            model.setText(v.getModel_name());
                            kms.setText(String.valueOf(v.getRun_km_hr()));
                            rent_per_day.setText(String.valueOf(v.getRent_per_day_with_fuel()));
                            rent_per_hour.setText(String.valueOf(v.getRent_per_hour_with_fuel()));
                            Log.i("YEAR OF MAN",v.getYear_of_man());
                            yom.setText(v.getYear_of_man());
                        }catch (Exception e){
                            progressBar.setVisibility(View.INVISIBLE);/*eat the exceptions for now eh!*/ }
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleDetailsData> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "No internet access", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVfront(){
        API api = RestAdapter.createAPI();
        Call<ResponseBody> call = api.getVfront(vid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                if(bmp != null) {
                    addImage(bmp);
                    notifyData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getVside(){
        API api = RestAdapter.createAPI();
        Call<ResponseBody> call = api.getVside(vid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                if(bmp != null) {
                    addImage(bmp);
                    notifyData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getVback(){
        API api = RestAdapter.createAPI();
        Call<ResponseBody> call = api.getVback(vid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                if(bmp != null) {
                    addImage(bmp);
                    notifyData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getOwnerData(int id){
        API api = RestAdapter.createAPI();
        Call<ProfileOwnerData> call = api.getOwnerData(id);
        call.enqueue(new Callback<ProfileOwnerData>() {
            @Override
            public void onResponse(Call<ProfileOwnerData> call, Response<ProfileOwnerData> response) {
                if(response.isSuccessful()){
                    ProfileOwnerData p = response.body();
                    if(p != null){
                        owner.setText(p.getName());
                        setMobile(p.getMobile());
                        setMobile2(p.getMobile2());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileOwnerData> call, Throwable t) {

            }
        });
    }

    private void listeners(){
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile.isEmpty()){
                    Toast.makeText(getContext(), "Please wait", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+getMobile()));
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getContext().startActivity(phoneIntent);
            }
        });
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile() {
        return mobile;
    }

    private void addImage(Bitmap bitmap){
        bitmapList.add(bitmap);
    }

    private void notifyData(){
        vehicleImageAdapter.notifyDataSetChanged();
    }
}