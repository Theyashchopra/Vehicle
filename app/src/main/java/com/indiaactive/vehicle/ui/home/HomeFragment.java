package com.indiaactive.vehicle.ui.home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.indiaactive.vehicle.GPS.GpsUtils;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.activities.MainActivity;
import com.indiaactive.vehicle.adapters.MasterAdapter;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.adapters.VehicleListAdapter;
import com.indiaactive.vehicle.adapters.VehicleAdapter;
import com.indiaactive.vehicle.datamodels.DriverData;
import com.indiaactive.vehicle.datamodels.ListVehicles;
import com.indiaactive.vehicle.datamodels.MasterRoot;
import com.indiaactive.vehicle.datamodels.MasterVehicle;
import com.indiaactive.vehicle.datamodels.VehicleData;
import com.indiaactive.vehicle.datamodels.VehicleModel;
import com.indiaactive.vehicle.datamodels.VehicleModelRoot;
import com.indiaactive.vehicle.datamodels.VehicleType;
import com.indiaactive.vehicle.datamodels.VehicleTypeRoot;
import com.indiaactive.vehicle.datamodels.Vehicles;
import com.indiaactive.vehicle.dialogs.MarkerPopup;
import com.indiaactive.vehicle.interfaces.API;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, DatePickerDialog.OnDateSetListener {

    View root;
    private ProgressBar homeProgress;
    //for map
    private MapView mapView;
    private GoogleMap map;
    private androidx.appcompat.widget.SearchView searchView;
    boolean isGPS;
    //For Location
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Animation slideDown,slideUp;
    private int bottomState;
    private List<Vehicles> vehiclesList;
    //For First bottom card
    private static LinearLayout bottomll;
    private RelativeLayout[] bottom1cards;

    //For Second bottom card
    private static List<VehicleType> vehiclelist;
    //private static VehicleAdapter vehicleAdapter;
    private static RecyclerView vehicleRecycle,master_vehicle;
    private static ImageView backIV;
    private static ConstraintLayout bottomcl;

    //for Third bottom card
    private RecyclerView driverRecycle;
    private List<DriverData> driverList;
    private VehicleListAdapter vehicleListAdapter;
    private ImageView pin;

    //for filter page
    ImageView fbackiv;
    RangeSeekBar filterrangeseekbar;
    Button filtersearchbutton;
    ConstraintLayout filtercl;
    TextView filterperdayrent;
    TextView fdatefrom,fdateto;
    Spinner filter_spinner;
    boolean flastclickeddate;
    CheckBox filterCheckBox;
    String startcost,endcost;
    String startdate, enddate;
    int checkNow;
    List<String> spinnerList;
    VehicleModelRoot vehicleModelRoot;
    int selectedModelId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //general
    private ProgressBar progressBar, progressBarBottom2, progressBarBottom3,progressBarBottomList,progressBarFilter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        selectedModelId = -1;
        bottomState = 0;
        mapView = root.findViewById(R.id.mapfragment);
        progressBar = root.findViewById(R.id.main_progress);
        master_vehicle = root.findViewById(R.id.master_vehicle);
        pin = root.findViewById(R.id.pin);
        filter_spinner = root.findViewById(R.id.filtermodelspinner);
        searchView = root.findViewById(R.id.homesearch);
        isGPS = false;
        vehicleRecycle = root.findViewById(R.id.homerecycle2);
        driverRecycle = root.findViewById(R.id.driverrecycle);
        backIV = root.findViewById(R.id.backimage);
        bottomll = root.findViewById(R.id.bottom_ll);
        bottomcl = root.findViewById(R.id.bottom_cl1);
        bottom1cards = new RelativeLayout[3];
        homeProgress = root.findViewById(R.id.home_progress);
        /*bottom1cards[0] = root.findViewById(R.id.homerl1);
        bottom1cards[1] = root.findViewById(R.id.homerl2);
        bottom1cards[2] = root.findViewById(R.id.homerl3);*/
        fbackiv = root.findViewById(R.id.backimagefilter);
        filtersearchbutton = root.findViewById(R.id.filtersearch);
        filterrangeseekbar = root.findViewById(R.id.filtercostrange);
        filterperdayrent = root.findViewById(R.id.filterperdayrent);
        filtercl = root.findViewById(R.id.filtercl);
        fdatefrom = root.findViewById(R.id.filterfromdate);
        fdateto = root.findViewById(R.id.filtertodate);
        slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        filterCheckBox = root.findViewById(R.id.filterchecknow);
        progressBarBottom2 = root.findViewById(R.id.progressbottom2);
        progressBarBottom3 = root.findViewById(R.id.progressbottom3);
        progressBarBottomList = root.findViewById(R.id.progressbottomlist);
        progressBarFilter = root.findViewById(R.id.progressfilter);

        vehiclesList = new ArrayList<>();
        vehiclelist = new ArrayList<>();

        mapView.onCreate(savedInstanceState);
        getLocationPermission();
        initMap();
        initFirstBottom();
        resetState();
        placesApi();
        animations(bottom1cards);
        new GpsUtils(getContext()).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
/*                if(root.findViewById(R.id.driverlistrl).getVisibility() == View.VISIBLE && root.findViewById(R.id.bottom_rl3).getVisibility() == View.GONE){
                    root.startAnimation(slideDown);
                    pin.setVisibility(View.VISIBLE);
                    root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
                    root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
                }else {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }*/
                switch(bottomState){
                    case 1:
                        bottomcl.setVisibility(View.GONE);
                        bottomll.setVisibility(View.VISIBLE);
                        bottomState = 0;
                        break;
                    case 2:
                        /*root.findViewById(R.id.filtercl).setVisibility(View.GONE);
                        bottomcl.setVisibility(View.VISIBLE);
                        root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
                        bottomState = 1;*/
                        break;
                    case 3:
                        bottomState = 1;
                        root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
                        bottomcl.setVisibility(View.VISIBLE);
                        map.clear();
                        root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        bottomState = 3;
                        root.startAnimation(slideDown);
                        pin.setVisibility(View.VISIBLE);
                        root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
                        root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
                        break;
                    default:
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        break;
                }
            }
        });
    }

    private void placesApi(){
        double lat = sharedPreferences.getFloat("lat",0);
        double lng = sharedPreferences.getFloat("long",0);
        Places.initialize(root.getContext(),getString(R.string.api));
        PlacesClient placesClient = Places.createClient(getContext());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(lat-0.02, lng-0.02),
                new LatLng(lat+0.02, lng+0.02)));
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "LATLNG"+place.getName()+","+place.getLatLng());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),16));
                searchView.setQuery(place.getAddress().toString(),false);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /*
    * Bottom cards related
    */
    private void initFirstBottom(){
        //getting master vehicle type from here
        progressBar.setVisibility(View.VISIBLE);
        API api = RestAdapter.createAPI();
        Call<MasterRoot> call = api.getMaster();
        call.enqueue(new Callback<MasterRoot>() {
            @Override
            public void onResponse(Call<MasterRoot> call, Response<MasterRoot> response) {
                if (response.isSuccessful()){
                    MasterRoot masterRoot = response.body();
                    master_vehicle.setHasFixedSize(true);
                    master_vehicle.setAdapter(new MasterAdapter(masterRoot.getMasters(),getContext(),HomeFragment.this));
                    master_vehicle.scheduleLayoutAnimation();
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MasterRoot> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        
    }
    public void initSecondBottom(int clicked,String url){
        //getting vehicle types(subcategory from here)
        vehiclelist.clear();
        bottomState = 1;
        progressBarBottom2.setVisibility(View.VISIBLE);
        backIV.setOnClickListener(view -> {
            bottomll.setVisibility(View.VISIBLE);
            bottomcl.setVisibility(View.GONE);
        });
        API api = RestAdapter.createAPI();
        Call<VehicleTypeRoot> call = api.getVtypes(clicked);
        call.enqueue(new Callback<VehicleTypeRoot>() {
            @Override
            public void onResponse(Call<VehicleTypeRoot> call, Response<VehicleTypeRoot> response) {
                if(response.isSuccessful()){
                    VehicleTypeRoot vehicleTypeRoot = response.body();
                    vehiclelist = vehicleTypeRoot.getMasters();
                    vehicleRecycle.setAdapter(new VehicleAdapter(vehiclelist,getContext(),HomeFragment.this,url));
                    vehicleRecycle.scheduleLayoutAnimation();
                    progressBarBottom2.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressBarBottom2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<VehicleTypeRoot> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBarBottom2.setVisibility(View.INVISIBLE);
            }
        });
        bottomll.setVisibility(View.GONE);
        bottomcl.setVisibility(View.VISIBLE);
    }
    //not in use
    /*public void initFilterPage(int id){
        progressBarFilter.setVisibility(View.VISIBLE);
        bottomState = 2;
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fdatefrom.setText(currentDate);
        startdate = currentDate;
        enddate = currentDate;
        fdateto.setText(currentDate);
        checkNow = 0;
        filterCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
              checkNow = 1;
            }
            else {
                checkNow = 0;
            }
        });
        fbackiv.setOnClickListener(v -> {
            root.findViewById(R.id.filtercl).setVisibility(View.GONE);
            bottomcl.setVisibility(View.VISIBLE);
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
            map.clear();
            bottomState = 1;
            progressBarFilter.setVisibility(View.GONE);
        });
        filtersearchbutton.setOnClickListener(v -> {
            if(selectedModelId == -1){
                Toast.makeText(getContext(), "Please Select a model first", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBarFilter.setVisibility(View.GONE);
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
        });
        //filterrangeseekbar.setMinMaxStepSize(100);
        filterrangeseekbar.setStepSize(1000);
        filterrangeseekbar.setMax(10000);
        fdateto.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Calendar.YEAR,Calendar.MONTH, Calendar.DAY_OF_MONTH);
            flastclickeddate = false;
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });
        fdatefrom.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Calendar.YEAR,Calendar.MONTH, Calendar.DAY_OF_MONTH);
            flastclickeddate = true;
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });

        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text =(String) adapterView.getItemAtPosition(i);
                List<VehicleModel> list = vehicleModelRoot.getModels();
                for(VehicleModel vm : list){
                    if(Objects.equals(vm.getModel_name(), text)){
                        selectedModelId = vm.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterrangeseekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                startcost = Integer.toString(i);
                endcost = Integer.toString(i1);
                filterperdayrent.setText("Rs"+i+" - "+"Rs"+i1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

            }
        });
        filtercl.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInLeft)
                .duration(2000)
                .playOn(filtercl);
        bottomcl.setVisibility(View.GONE);
        root.findViewById(R.id.pin).setVisibility(View.GONE);
        progressBarFilter.setVisibility(View.GONE);
    }*/

    public void initThirdBottom(int model_id,int checkNow,String startdate,String startcost,String endcost,String enddate){
        bottomcl.setVisibility(View.GONE);
        //progressBarBottom3.setVisibility(View.VISIBLE);
        bottomState = 3;
        TextView tv = root.findViewById(R.id.typetv3);
        createMarkerList(model_id,checkNow,startdate,startcost,endcost,enddate);
        vehicleListAdapter = new VehicleListAdapter(vehiclesList, getContext(),this);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(vehicleListAdapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);
        driverRecycle.setAdapter(alphaInAnimationAdapter);
        driverRecycle.scheduleLayoutAnimation();
        ImageView up = root.findViewById(R.id.arrowup);
        up.setOnClickListener(v -> {
            bottomState = 4;
            root.startAnimation(slideUp);
            new Handler(Looper.myLooper()).postDelayed(() -> {
                progressBarBottomList.setVisibility(View.GONE);
                pin.setVisibility(View.INVISIBLE);
                root.findViewById(R.id.driverlistrl).setVisibility(View.VISIBLE);
                root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            },300);
        });
        root.findViewById(R.id.downarrow).setOnClickListener(v -> {
            bottomState = 3;
            root.startAnimation(slideDown);
            pin.setVisibility(View.VISIBLE);
            root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
            root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
        });
        root.findViewById(R.id.backimage3).setOnClickListener(v -> {
            progressBarBottom3.setVisibility(View.GONE);
            bottomState = 1;
            root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            bottomcl.setVisibility(View.VISIBLE);
            //map.clear();
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);

        });
       bottomll.setVisibility(View.GONE);
        root.findViewById(R.id.filtercl).setVisibility(View.GONE);
        root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
        progressBarBottom3.setVisibility(View.GONE);
    }

    /*
    * Map related
    */
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
            resetState();
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
                rlp.setMargins(0, 900, 20, 0);
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
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
                            editor.putFloat("lat",(float)currentLocation.getLatitude());
                            editor.apply();
                            editor.putFloat("long",(float)currentLocation.getLongitude());
                            editor.apply();
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
    private void animations(RelativeLayout [] relativeLayout){
        YoYo.with(Techniques.FadeInUp)
                .duration(2000)
                .repeat(0)
                .playOn(searchView);
    }
    public void createMarkerList(int model_id,int checkNow,String startdate,String startcost,String endcost,String enddate){
        MainActivity.vehiclesList.clear();
        vehiclesList.clear();
        homeProgress.setVisibility(View.VISIBLE);
        Call<ListVehicles> call = RestAdapter.createAPI().getVehicleList(model_id, checkNow, startdate, enddate, startcost,endcost);
        call.enqueue(new Callback<ListVehicles>() {
            @Override
            public void onResponse(Call<ListVehicles> call, Response<ListVehicles> response) {
                if(response.isSuccessful()){
                    ListVehicles res = response.body();
                    if(res != null){
                        vehiclesList = res.getVehicles();
                        MainActivity.vehiclesList.addAll(vehiclesList);
                        if(vehiclesList.isEmpty()){
                            Toast.makeText(getContext(), "OOPS!, No vehicles found", Toast.LENGTH_SHORT).show();
                        }
                        vehicleListAdapter = new VehicleListAdapter(vehiclesList, getContext(),HomeFragment.this);
                        driverRecycle.setAdapter(vehicleListAdapter);
                        driverRecycle.scheduleLayoutAnimation();
                        homeProgress.setVisibility(View.INVISIBLE);
                        addMarkersOnMap(vehiclesList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListVehicles> call, Throwable t) {
                Log.e("GET List", t.getMessage());
                homeProgress.setVisibility(View.INVISIBLE);
            }
        });

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("CLICK","MARKER");
        final Marker marker1 = marker;
        for(Vehicles v : vehiclesList){
            Log.i(TAG, "onMarkerClick: "+v.getLat());
            if(((Double)v.getLat()).equals(marker1.getPosition().latitude) && ((Double)v.getLon()).equals(marker1.getPosition().longitude) ){
                MarkerPopup mk = new MarkerPopup();
                Bundle args = new Bundle();
                Log.i(TAG, "onMarkerClick: "+v.getName());
                args.putInt("vid",v.getV_id());
                args.putString("name",v.getName());
                args.putString("madein",v.getYear_of_man());
                args.putString("kms",String.valueOf(v.getRun_km_hr()));
                args.putString("rent",String.valueOf(v.getRent_cost()));
//                args.putString("rentperday",String.valueOf(v.getRent_per_day_with_fuel()));
//                args.putString("rentperHour",String.valueOf(v.getRent_per_hour_with_fuel()));
//                args.putString("number",v.getPlate_no());
                mk.setArguments(args);
                mk.show(getActivity().getSupportFragmentManager(),"marker");
                return true;
            }
        }
        return true;
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
        resetState();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mapView.onDestroy();
        }catch (Exception e){
            e.printStackTrace();
        }
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
    @Override
    public void onMapClick(LatLng latLng) {
        // if required
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (flastclickeddate){
            String df = dayOfMonth+"/"+month+"/"+year;
            fdatefrom.setText(df);
            startdate = df;
            if (dayOfMonth >= Integer.parseInt(fdateto.getText().subSequence(0,2).toString())){
                fdateto.setText(df);
                enddate = df;
            }
        }
        else{
            if (Integer.parseInt(fdatefrom.getText().subSequence(0,2).toString()) <= dayOfMonth){
                fdateto.setText(dayOfMonth+"/"+month+"/"+year);
                enddate = dayOfMonth+"/"+month+"/"+year;
            }
            else {
                Toast.makeText(getContext(), "Starting date is after the ending date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetState(){
        if(MainActivity.vehiclesList.isEmpty()){
            Log.i("LOL_NEW","EMPTY");
            return;
        }
        Log.i("LOG786",MainActivity.vehiclesList.get(0).getName());
        vehiclesList = MainActivity.vehiclesList;
        vehicleListAdapter = new VehicleListAdapter(vehiclesList, getContext(),HomeFragment.this);
        driverRecycle.setAdapter(vehicleListAdapter);
        driverRecycle.scheduleLayoutAnimation();
        homeProgress.setVisibility(View.INVISIBLE);
        addMarkersOnMap(MainActivity.vehiclesList);
        bottomcl.setVisibility(View.GONE);
        //progressBarBottom3.setVisibility(View.VISIBLE);
        bottomState = 3;
        bottomll.setVisibility(View.GONE);
        root.findViewById(R.id.filtercl).setVisibility(View.GONE);
        root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
        ImageView up = root.findViewById(R.id.arrowup);

        up.setOnClickListener(v -> {
            bottomState = 4;
            root.startAnimation(slideUp);
            new Handler(Looper.myLooper()).postDelayed(() -> {
                progressBarBottomList.setVisibility(View.GONE);
                pin.setVisibility(View.INVISIBLE);
                root.findViewById(R.id.driverlistrl).setVisibility(View.VISIBLE);
                root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            },300);
        });
        root.findViewById(R.id.downarrow).setOnClickListener(v -> {
            bottomState = 3;
            root.startAnimation(slideDown);
            pin.setVisibility(View.VISIBLE);
            root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
            root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
        });
        root.findViewById(R.id.backimage3).setOnClickListener(v -> {
            progressBarBottom3.setVisibility(View.GONE);
            bottomState = 1;
            root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            bottomll.setVisibility(View.VISIBLE);
            bottomcl.setVisibility(View.GONE);
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
        });
    }
}