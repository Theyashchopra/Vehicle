package com.indiaactive.vehicle.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ViewDetailsFragment extends Fragment {
    
    ProgressBar progressBar,ownerProgress;
    RecyclerView recyclerView;
    Button call;
    View root;
    ImageView backbutton;
    RequestPopup rpop;
    int vid;
    List<Bitmap> bitmapList;
    VehicleImageAdapter vehicleImageAdapter;
    TextView name,plate,avail,model,kms,yom,owner,rent_per_day,rent_per_hour,owner_name,owner_mobile1,owner_mobile2,owner_email;
    String mobile,mobile2;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_view_details, container, false);
        vid = -1;
        vid = getArguments().getInt("vid");
        init();
        getCallPermission();
        backbutton.setOnClickListener(view -> Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigateUp());
        return root;
    }

    private void init(){
        sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        owner_email = root.findViewById(R.id.owner_email);
        ownerProgress = root.findViewById(R.id.card_progress);
        owner_name = root.findViewById(R.id.owner_name);
        owner_mobile1 = root.findViewById(R.id.owner_mobile1);
        owner_mobile2 = root.findViewById(R.id.owner_mobile2);
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
        call = root.findViewById(R.id.owner_call);
        backbutton = root.findViewById(R.id.viewback);
        try {
            getData();
            getVback();
            getVside();
            getVfront();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            rent_per_day.setText(v.getRent_cost());
                            rent_per_hour.setText(v.getBusy_end());
                            //Log.i("YEAR OF MAN",v.getYear_of_man());
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
                }else{
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_error_404);
                    addImage(largeIcon);
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
                }else{
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_error_404);
                    addImage(largeIcon);
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
                }else{
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_error_404);
                    addImage(largeIcon);
                    notifyData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getOwnerData(int id){
        ownerProgress.setVisibility(View.VISIBLE);
        API api = RestAdapter.createAPI();
        Call<ProfileOwnerData> call = api.getOwnerData(id);
        call.enqueue(new Callback<ProfileOwnerData>() {
            @Override
            public void onResponse(Call<ProfileOwnerData> call, Response<ProfileOwnerData> response) {
                if(response.isSuccessful()){
                    ProfileOwnerData p = response.body();
                    if(p != null){
                        ownerProgress.setVisibility(View.INVISIBLE);
                        owner_email.setText(p.getEmail());
                        owner.setText(p.getName());
                        owner_name.setText(p.getName());
                        owner_mobile1.setText(p.getMobile());
                        owner_mobile2.setText(p.getMobile2());
                        setMobile(p.getMobile());
                        setMobile2(p.getMobile2());
                        sendEnquiry(vid,p.getId());
                        listeners();
                    }
                    ownerProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProfileOwnerData> call, Throwable t) {
                ownerProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void listeners(){
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile.isEmpty()){
                    getCallPermission();
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

    private void sendEnquiry(int vid,int oid){
        Map<String,Object> map = new HashMap<>();
        int uid = sharedPreferences.getInt("id",0);
        if(uid == 0){
            return;
        }
        float lat = sharedPreferences.getFloat("lat",0.0f);
        float lon = sharedPreferences.getFloat("long",0.0f);
        map.put("uid",uid);
        map.put("owner_id",oid);
        map.put("v_id",vid);
        map.put("lat",lat);
        map.put("lon",lon);
        API api = RestAdapter.createAPI();
        Call<Map> call = api.callEnquiry(map);
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {

            }
        });
    }

    private void getCallPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.CALL_PHONE};
        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(getActivity(), permissions, 12);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");

        switch(requestCode){
            case 12:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    //initialize our map
                }
            }
        }
    }
}