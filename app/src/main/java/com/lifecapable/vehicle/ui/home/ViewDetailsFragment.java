package com.lifecapable.vehicle.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.lifecapable.vehicle.R;
import com.lifecapable.vehicle.dialogs.RequestPopup;

public class ViewDetailsFragment extends Fragment {

    Button requestbt;
    View root;
    RequestPopup rpop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_details, container, false);
        requestbt = root.findViewById(R.id.requestbt);
        rpop = new RequestPopup();

        requestbt.setOnClickListener(v -> {
            rpop.show(getActivity().getSupportFragmentManager(),"request");
        });
        return root;
    }
}