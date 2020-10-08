package com.indiaactive.vehicle.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.adapters.SearchVehicleListAdapter;
import com.indiaactive.vehicle.adapters.VehicleListAdapter;
import com.indiaactive.vehicle.datamodels.ListOfVehicle;
import com.indiaactive.vehicle.datamodels.ListVehicles;
import com.indiaactive.vehicle.datamodels.Vehicles;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    View root;
    AutoCompleteTextView autoCompleteTextView;
    List<String> searchList;
    ArrayAdapter<String> searchAdapter;
    String selected;

    RecyclerView vehicleRecycle;
    SearchVehicleListAdapter vehicleListAdapter;
    List<Vehicles> vehiclesList;
    TextView searchText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        autoCompleteTextView = root.findViewById(R.id.searchvehicle);
        vehicleRecycle = root.findViewById(R.id.vehiclerecycle);
        searchText = root.findViewById(R.id.searchtext);
        vehiclesList = new ArrayList<>();
        getSearchData();
        root.findViewById(R.id.vehicleshowmap).setOnClickListener(view -> {
            if(selected != null){
                Bundle args = new Bundle();
                args.putString("name",selected);
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_navigation_dashboard_to_viewOnMapFragment,args);
            }
        });
        return root;
    }
    public void getSearchData(){
        searchList = new ArrayList<>();
        searchList.clear();
        Call<ListOfVehicle> call = RestAdapter.createAPI().vehicleSearch("list");
        call.enqueue(new Callback<ListOfVehicle>() {
            @Override
            public void onResponse(Call<ListOfVehicle> call, Response<ListOfVehicle> response) {
                if(!response.isSuccessful()){
                    return;
                }
                ListOfVehicle res = response.body();
                if(res != null){
                    if(res.getNames() != null){
                        searchList.addAll(res.getNames());
                        if(searchAdapter != null){
                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfVehicle> call, Throwable t) {

            }
        });
        setupSearch();
    }
    public void setupSearch(){
        searchAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,searchList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(searchAdapter);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            selected = searchAdapter.getItem(i);
            Log.e("yo", selected);
            createList(selected);
            searchText.setVisibility(View.GONE);
            autoCompleteTextView.clearFocus();
            vehicleRecycle.requestFocus();
        });
        searchAdapter.notifyDataSetChanged();
    }
    public void createList(String name)
    {
        vehiclesList.clear();

        Call<ListVehicles> call = RestAdapter.createAPI().getVehiclesByName(name);
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
                        vehicleListAdapter = new SearchVehicleListAdapter(vehiclesList, getContext(), DashboardFragment.this);
                        vehicleRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        vehicleRecycle.setAdapter(vehicleListAdapter);
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

    @Override
    public void onResume() {
        super.onResume();
        getSearchData();
    }
}