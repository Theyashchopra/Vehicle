package com.indiaactive.vehicle.ui.home;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.dialogs.RequestPopup;

public class ViewDetailsFragment extends Fragment {

    Button requestbt;
    View root;
    ImageView backbutton;
    RequestPopup rpop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_details, container, false);
        requestbt = root.findViewById(R.id.requestbt);
        backbutton = root.findViewById(R.id.viewback);
        rpop = new RequestPopup();

        requestbt.setOnClickListener(v -> {
            rpop.show(requireActivity().getSupportFragmentManager(),"request");
        });
        backbutton.setOnClickListener(view -> Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigateUp());
        return root;
    }
}