package com.indiaactive.vehicle.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.adapters.SearchVehicleListAdapter;
import com.indiaactive.vehicle.datamodels.ListVehicles;
import com.indiaactive.vehicle.datamodels.Vehicles;
import com.indiaactive.vehicle.dialogs.MarkerPopup;
import com.indiaactive.vehicle.dialogs.SearchMarkerPopup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ViewOnMapFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener{

    private MapView mapView;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    List<Vehicles> vehiclesList;
    ImageView imageView;
    View root;
    String selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_on_map, container, false);
        mapView = root.findViewById(R.id.vommap);
        imageView = root.findViewById(R.id.vomback);
        vehiclesList = new ArrayList<>();
        mapView.onCreate(savedInstanceState);
        if (getArguments() != null){
            selected = getArguments().getString("name");
        }
        imageView.setOnClickListener(view -> NavHostFragment.findNavController(ViewOnMapFragment.this).navigateUp());
        initMap();
        getLocationPermission();
        getTheMarkers();
        return root;
    }
    private void initMap(){
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(mMap -> {
            map = mMap;
            map.setOnMarkerClickListener(this);
/*                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(25.6714, -100.309));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLocationPermission();
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.moveCamera(center);
            map.animateCamera(zoom);*/
            Log.d(TAG, "onMapReady: map is ready");
            if (mLocationPermissionsGranted) {
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                // position the mylocation button
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rlp.setMargins(0, 300, 20, 0);
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
    }
    private void getTheMarkers(){
        vehiclesList.clear();

        Call<ListVehicles> call = RestAdapter.createAPI().getVehiclesByName(selected);
        call.enqueue(new Callback<ListVehicles>() {
            @Override
            public void onResponse(Call<ListVehicles> call, Response<ListVehicles> response) {
                if(response.isSuccessful()){
                    ListVehicles res = response.body();
                    if(res != null){

                        vehiclesList = res.getVehicles();
                        if(vehiclesList.isEmpty()){
                            Toast.makeText(getContext(), "OOPS!, No vehicles found", Toast.LENGTH_SHORT).show();
                        }
                        addMarkersOnMap(vehiclesList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListVehicles> call, Throwable t) {
                Log.e("GET List", t.getMessage());
                //   homeProgress.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();
                        try {
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
/*                            editor.putFloat("lat",(float)currentLocation.getLatitude());
                            editor.apply();
                            editor.putFloat("long",(float)currentLocation.getLongitude());
                            editor.apply();*/
                        }catch (Exception e){
                            //21.1610282,78.9324241

                        }

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Secur" +
                    "ityException: " + e.getMessage() );
        }
    }
    private void moveCamera(LatLng latLng){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    public void addMarkersOnMap(List<Vehicles> vehiclesList){
        for(Vehicles vehicle : vehiclesList){
            try {
                LatLng latLng = new LatLng(vehicle.getLat(),vehicle.getLon());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Truck").icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_car_top_view));
                map.addMarker(markerOptions);
                Log.e("Marker Position", vehicle.getLat()+" ," +vehicle.getLon());
                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.146002, 79.089984),13));
            }
            catch (Exception e){
                Log.e("Exception ",e.getMessage());
            }
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(context,vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("CLICK","MARKER");
        final Marker marker1 = marker;
        for(Vehicles v : vehiclesList){
            Log.i(TAG, "onMarkerClick: "+v.getLat());
            if(((Double)v.getLat()).equals(marker1.getPosition().latitude) && ((Double)v.getLon()).equals(marker1.getPosition().longitude) ){
                SearchMarkerPopup mk = new SearchMarkerPopup();
                Bundle args = new Bundle();
                Log.i(TAG, "onMarkerClick: "+v.getName());
                args.putInt("vid",v.getV_id());
                args.putString("name",v.getName());
                args.putString("madein",v.getYear_of_man());
                args.putString("kms",String.valueOf(v.getRun_km_hr()));
                args.putString("rentperday",String.valueOf(v.getRent_per_day_with_fuel()));
                args.putString("rentperHour",String.valueOf(v.getRent_per_hour_with_fuel()));
                args.putString("number",v.getPlate_no());
                mk.setArguments(args);
                mk.show(getActivity().getSupportFragmentManager(),"marker");
                return true;
            }
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}