package com.indiaactive.vehicle.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.activities.LoginActivity;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.datamodels.VehicleModel;
import com.indiaactive.vehicle.datamodels.VehicleModelRoot;
import com.indiaactive.vehicle.interfaces.API;
import com.indiaactive.vehicle.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterPopup extends DialogFragment implements  DatePickerDialog.OnDateSetListener  {

    Button search;
    VehicleModelRoot vehicleModelRoot;
    View view;
    int id;
    CheckBox checkBox;
    ProgressBar progressBar;
    TextView startDate,endDate,range;
    Spinner spinner;
    HomeFragment homeFragment;
    RangeSeekBar rangeSeekBar;
    Button filtersearchbutton;
    boolean flastclickeddate;
    String startcost,endcost;
    String startdate, enddate;
    int checkNow,selectedModelId;
    List<VehicleModel> list;
    List<String> spinnerList;

    public FilterPopup(HomeFragment homeFragment, int id){
        this.id = id;
        this.homeFragment = homeFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.filter_popup, container, false);
        list = new ArrayList<>();
        init();
        getModelsfromApi(id);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return view;
    }

    private void init(){
        selectedModelId = -1;
        startcost = "";
        endcost = "";
        enddate = "";
        startdate = "";
        search = view.findViewById(R.id.search);
        progressBar = view.findViewById(R.id.progress);
        filtersearchbutton = view.findViewById(R.id.filtersearch);
        rangeSeekBar = view.findViewById(R.id.filtercostrange);
        range = view.findViewById(R.id.filterperdayrent);
        startDate = view.findViewById(R.id.filterfromdate);
        endDate = view.findViewById(R.id.filtertodate);
        checkBox = view.findViewById(R.id.filterchecknow);
        spinner = view.findViewById(R.id.filtermodelspinner);
        listeners();
    }
    private void listeners(){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        startDate.setText(currentDate);
        startdate = currentDate;
        enddate = currentDate;
        endDate.setText(currentDate);
        checkNow = 0;
        rangeSeekBar.setStepSize(1000);
        rangeSeekBar.setMax(10000);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                checkNow = 1;
            }
            else {
                checkNow = 0;
            }
        });
        startDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Calendar.YEAR,Calendar.MONTH, Calendar.DAY_OF_MONTH);
            flastclickeddate = false;
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });
        endDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, Calendar.YEAR,Calendar.MONTH, Calendar.DAY_OF_MONTH);
            flastclickeddate = true;
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = (String) adapterView.getItemAtPosition(i);
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

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                startcost = Integer.toString(i);
                endcost = Integer.toString(i1);
                range.setText("Rs"+i+" - "+"Rs"+i1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startdate.isEmpty() || enddate.isEmpty()){
                    Toast.makeText(getContext(), "Please add dates", Toast.LENGTH_SHORT).show();
                    return;
                }else if(startcost.isEmpty() || endcost.isEmpty()){
                    Toast.makeText(getContext(), "Please add Price Range", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedModelId != -1){
                    homeFragment.initThirdBottom(selectedModelId,checkNow,startdate,startcost,endcost,enddate);
                    getDialog().dismiss();
                }else{
                    Toast.makeText(getContext(), "Please select a model first", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (flastclickeddate){
            String df = dayOfMonth+"/"+month+"/"+year;
            endDate.setText(df);
            startdate = df;
            if (dayOfMonth >= Integer.parseInt(startDate.getText().subSequence(0,2).toString())){
                startDate.setText(df);
                enddate = df;
            }
        }
        else{
            if (Integer.parseInt(endDate.getText().subSequence(0,2).toString()) <= dayOfMonth){
                startDate.setText(dayOfMonth+"/"+month+"/"+year);
                enddate = dayOfMonth+"/"+month+"/"+year;
            }
            else {
                Toast.makeText(getContext(), "Starting date is after the ending date", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getModelsfromApi(int id){
        progressBar.setVisibility(View.VISIBLE);
        API api = RestAdapter.createAPI();
        Call<VehicleModelRoot> call = api.getVModels(id);
        spinnerList = new ArrayList<>();
        call.enqueue(new Callback<VehicleModelRoot>() {
            @Override
            public void onResponse(Call<VehicleModelRoot> call, Response<VehicleModelRoot> response) {
                if(response.isSuccessful()){
                    vehicleModelRoot = response.body();
                    if(vehicleModelRoot != null){
                        for(VehicleModel v : vehicleModelRoot.getModels()){
                            spinnerList.add(v.getModel_name());
                        }
                        if(spinnerList.isEmpty()){
                            Toast.makeText(getContext(), "No Models registered for this type yet", Toast.LENGTH_SHORT).show();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerList);
                        spinner.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<VehicleModelRoot> call, Throwable t) {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
