package com.lifecapable.vehicle.ui.home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.lifecapable.vehicle.GPS.GpsUtils;
import com.lifecapable.vehicle.R;
import com.lifecapable.vehicle.adapters.DriverAdapter;
import com.lifecapable.vehicle.adapters.VehicleAdapter;
import com.lifecapable.vehicle.datamodels.DriverData;
import com.lifecapable.vehicle.datamodels.VehicleData;
import com.lifecapable.vehicle.datamodels.Vehicles;
import com.lifecapable.vehicle.dialogs.MarkerPopup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, DatePickerDialog.OnDateSetListener {

    View root;

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
    private int bottomState = 0;
    private List<Vehicles> vehiclesList;
    //For First bottom card
    private LinearLayout bottomll;
    private RelativeLayout[] bottom1cards;

    //For Second bottom card
    private List<VehicleData> vehiclelist;
    private VehicleAdapter vehicleAdapter;
    private RecyclerView vehicleRecycle;
    private ImageView backIV;
    private ConstraintLayout bottomcl;

    //for Third bottom card
    private RecyclerView driverRecycle;
    private List<DriverData> driverList;
    private DriverAdapter driverAdapter;
    private ImageView pin;

    //for filter page
    ImageView fbackiv;
    RangeSeekBar filterrangeseekbar;
    Button filtersearchbutton;
    ConstraintLayout filtercl;
    TextView filterperdayrent;
    TextView fdatefrom,fdateto;
    boolean flastclickeddate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = root.findViewById(R.id.mapfragment);
        pin = root.findViewById(R.id.pin);
        searchView = root.findViewById(R.id.homesearch);
        isGPS = false;
        vehicleRecycle = root.findViewById(R.id.homerecycle2);
        backIV = root.findViewById(R.id.backimage);
        bottomll = root.findViewById(R.id.bottom_ll);
        bottomcl = root.findViewById(R.id.bottom_cl1);
        bottom1cards = new RelativeLayout[3];
        bottom1cards[0] = root.findViewById(R.id.homerl1);
        bottom1cards[1] = root.findViewById(R.id.homerl2);
        bottom1cards[2] = root.findViewById(R.id.homerl3);
        fbackiv = root.findViewById(R.id.backimagefilter);
        filtersearchbutton = root.findViewById(R.id.filtersearch);
        filterrangeseekbar = root.findViewById(R.id.filtercostrange);
        filterperdayrent = root.findViewById(R.id.filterperdayrent);
        filtercl = root.findViewById(R.id.filtercl);
        fdatefrom = root.findViewById(R.id.filterfromdate);
        fdateto = root.findViewById(R.id.filtertodate);
        slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        vehiclesList = new ArrayList<>();

        //second bottom layout back button
        backIV.setOnClickListener(v -> {
            bottomcl.setVisibility(View.GONE);
            bottomll.setVisibility(View.VISIBLE);
        });

        mapView.onCreate(savedInstanceState);
        getLocationPermission();
        initMap();
        initFirstBottom();
        animations(bottom1cards);
        createTestMarkerList();
        new GpsUtils(getContext()).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(root.findViewById(R.id.driverlistrl).getVisibility() == View.VISIBLE && root.findViewById(R.id.bottom_rl3).getVisibility() == View.GONE){
                    root.startAnimation(slideDown);
                    pin.setVisibility(View.VISIBLE);
                    root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
                    root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
                }else {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            }
        });
    }
    /*
    * Bottom cards related
    */
    private void initFirstBottom(){
        bottom1cards[0].setOnClickListener(v -> {initSecondBottom(1);});
        bottom1cards[1].setOnClickListener(v -> {initSecondBottom(2);});
        bottom1cards[2].setOnClickListener(v -> {initSecondBottom(3);});
    }
    private void initSecondBottom(int clicked){
        vehiclelist = new ArrayList<>();
        switch (clicked){
            case 1:
                vehiclelist.clear();
                vehiclelist.add(new VehicleData(R.drawable.ic_excavator,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_excavator,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_excavator,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_excavator,"TATA"));
                vehicleAdapter = new VehicleAdapter(vehiclelist, getContext(), HomeFragment.this);
                break;
            case 2:
                vehiclelist.clear();
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_truck,"TATA"));
                vehicleAdapter = new VehicleAdapter(vehiclelist, getContext(), HomeFragment.this);
                break;
            case 3:
                vehiclelist.clear();
                vehiclelist.add(new VehicleData(R.drawable.ic_mini_truck,"TATA"));
                vehiclelist.add(new VehicleData(R.drawable.ic_mini_truck,"TATA"));
                vehicleAdapter = new VehicleAdapter(vehiclelist, getContext(), HomeFragment.this);
                break;
            default:
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        vehicleRecycle.setAdapter(vehicleAdapter);
        vehicleRecycle.scheduleLayoutAnimation();
        bottomll.setVisibility(View.GONE);
        bottomcl.setVisibility(View.VISIBLE);
    }
    public void initFilterPage(String type){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fdatefrom.setText(currentDate);
        fdateto.setText(currentDate);
        fbackiv.setOnClickListener(v -> {
            root.findViewById(R.id.filtercl).setVisibility(View.GONE);
            bottomcl.setVisibility(View.VISIBLE);
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
            map.clear();

        });
        filtersearchbutton.setOnClickListener(v -> {
            initThirdBottom("Tata");
            addMarkersOnMap();
            root.findViewById(R.id.pin).setVisibility(View.VISIBLE);
        });
//        filterrangeseekbar.setMinMaxStepSize(100);
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

        filterrangeseekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
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
        bottomcl.setVisibility(View.GONE);
        root.findViewById(R.id.pin).setVisibility(View.GONE);

    }
    public void initThirdBottom(String veh){
        TextView tv = root.findViewById(R.id.typetv3);
        tv.setText(veh);
        /*driverList = new ArrayList<>();
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));
        driverList.add(new DriverData("YOYO","YEAH"));*/
        createTestMarkerList();
        driverAdapter = new DriverAdapter(vehiclesList, getContext(),this);
        driverRecycle = root.findViewById(R.id.driverrecycle);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(driverAdapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);
        driverRecycle.setAdapter(alphaInAnimationAdapter);
        driverRecycle.scheduleLayoutAnimation();
        ImageView up = root.findViewById(R.id.arrowup);
        up.setOnClickListener(v -> {
            root.startAnimation(slideUp);
            new Handler().postDelayed(() -> {
                pin.setVisibility(View.INVISIBLE);
                root.findViewById(R.id.driverlistrl).setVisibility(View.VISIBLE);
                root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            },300);
        });
        root.findViewById(R.id.downarrow).setOnClickListener(v -> {
            root.startAnimation(slideDown);
            pin.setVisibility(View.VISIBLE);
            root.findViewById(R.id.driverlistrl).setVisibility(View.GONE);
            root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
        });
        root.findViewById(R.id.backimage3).setOnClickListener(v -> {
            root.findViewById(R.id.bottom_rl3).setVisibility(View.GONE);
            root.findViewById(R.id.filtercl).setVisibility(View.VISIBLE);
            root.findViewById(R.id.pin).setVisibility(View.GONE);

        });
        bottomll.setVisibility(View.GONE);
        root.findViewById(R.id.filtercl).setVisibility(View.GONE);
        root.findViewById(R.id.bottom_rl3).setVisibility(View.VISIBLE);
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
            //Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
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
                rlp.setMargins(0, 300, 180, 0);

                //map.setMyLocationEnabled(true);
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
                        }catch (Exception e){
                            //21.1610282,78.9324241
                            moveCamera(new LatLng(21.1610282,78.9324241));
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
        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .repeat(0)
                .playOn(root.findViewById(R.id.homerl1));
        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .repeat(0)
                .playOn(root.findViewById(R.id.homerl2));
        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .repeat(0)
                .playOn(root.findViewById(R.id.homerl3));
        YoYo.with(Techniques.Bounce)
                .duration(3000)
                .repeat(0)
                .playOn(pin);
    }

    private void createTestMarkerList(){
        vehiclesList.clear();
        vehiclesList.add(new Vehicles("SBC TRAVELS","MH 31 EX 1234","9234324XX45",21.159369, 79.062351));
        vehiclesList.add(new Vehicles("ABC TRAVELS","MH 31 EA 1235","9234324XX45",21.167452, 79.075419));
        vehiclesList.add(new Vehicles("DBC TRAVELS","MH 31 EB 1236","9234324XX45",21.160786, 79.093762));
        vehiclesList.add(new Vehicles("CBC TRAVELS","MH 31 EC 1237","9234324XX45",21.140029, 79.085561));
        vehiclesList.add(new Vehicles("BBC TRAVELS","MH 31 EZ 1238","9234324XX45",21.130568, 79.099805));
        vehiclesList.add(new Vehicles("XBC TRAVELS","MH 31 EV 1239","9234324XX45",21.185917, 79.083619));
        vehiclesList.add(new Vehicles("ZBC TRAVELS","MH 31 EB 1240","9234324XX45",21.141036, 79.109948));
        vehiclesList.add(new Vehicles("VBC TRAVELS","MH 31 EN 1231","9234324XX45",21.133185, 79.047577));
        vehiclesList.add(new Vehicles("MBC TRAVELS","MH 31 EM 1232","9234324XX45",21.122473, 79.062218));
        vehiclesList.add(new Vehicles("NBC TRAVELS","MH 31 ES 1233","9234324XX45",21.118241, 79.016849));
        //wardha
        vehiclesList.add(new Vehicles("QBC TRAVELS","MH 31 ES 1233","9234324XX45",20.751936, 78.596291));
        vehiclesList.add(new Vehicles("WBC TRAVELS","MH 31 ES 1233","9234324XX45",20.750812, 78.613458));
        vehiclesList.add(new Vehicles("EBC TRAVELS","MH 31 ES 1233","9234324XX45",20.759239, 78.606849));
        vehiclesList.add(new Vehicles("RBC TRAVELS","MH 31 ES 1233","9234324XX45",20.754424, 78.625131));
        vehiclesList.add(new Vehicles("TBC TRAVELS","MH 31 ES 1233","9234324XX45",20.761968, 78.585992));
        vehiclesList.add(new Vehicles("YBC TRAVELS","MH 31 ES 1233","9234324XX45",20.751760, 78.646405));
        vehiclesList.add(new Vehicles("UBC TRAVELS","MH 31 ES 1233","9234324XX45",20.754030, 78.580616));
        vehiclesList.add(new Vehicles("IBC TRAVELS","MH 31 ES 1233","9234324XX45",20.735726, 78.630914));
        vehiclesList.add(new Vehicles("OBC TRAVELS","MH 31 ES 1233","9234324XX45",20.766843, 78.598762));
        vehiclesList.add(new Vehicles("PBC TRAVELS","MH 31 ES 1233","9234324XX45",20.713900, 78.605072));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("CLICK","MARKER");
        final Marker marker1 = marker;
        createTestMarkerList();
        for(Vehicles v : vehiclesList){
            Log.i(TAG, "onMarkerClick: "+v.getLatitude());
            if(((Double)v.getLatitude()).equals(marker1.getPosition().latitude) && ((Double)v.getLongitude()).equals(marker1.getPosition().longitude) ){
                MarkerPopup mk = new MarkerPopup();
                Bundle args = new Bundle();
                Log.i(TAG, "onMarkerClick: "+v.getName());
                args.putString("name",v.getName());
                args.putString("mobile",v.getContact());
                args.putString("plate",v.getNumber());
                mk.setArguments(args);
                mk.show(getActivity().getSupportFragmentManager(),"marker");
                return true;
            }
        }
        return true;
    }
    public void addMarkersOnMap(){
        for(Vehicles vehicles : vehiclesList){
            LatLng latLng = new LatLng(vehicles.getLatitude(),vehicles.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Truck").icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_car_top_view));
            map.addMarker(markerOptions);
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.146002, 79.089984),13));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.146002, 79.089984),13));
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
    @Override
    public void onMapClick(LatLng latLng) {
        // if required
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (flastclickeddate){
            String df = dayOfMonth+"/"+month+"/"+year;
            fdatefrom.setText(df);
            if (dayOfMonth >= Integer.parseInt(fdateto.getText().subSequence(0,2).toString())){
                fdateto.setText(df);
            }
        }
        else{
            if (Integer.parseInt(fdatefrom.getText().subSequence(0,2).toString()) <= dayOfMonth){
                fdateto.setText(dayOfMonth+"/"+month+"/"+year);
            }
            else {
                Toast.makeText(getContext(), "Starting date is after the ending date", Toast.LENGTH_SHORT).show();
            }
        }
    }
}