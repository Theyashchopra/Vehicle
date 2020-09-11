package com.lifecapable.vehicle.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lifecapable.vehicle.R;

public class MarkerPopup extends DialogFragment {
    View view;
    TextView name,madein,rent,hoursused;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.marker_popup, container, false);
        Bundle args = getArguments();
        String n = args.getString("name");
        String m = args.getString("mobile");
        String p = args.getString("plate");
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        name = view.findViewById(R.id.company_name);
        madein = view.findViewById(R.id.made);
        rent = view.findViewById(R.id.rentperday);
        hoursused = view.findViewById(R.id.noofhours);

        name.setText(n);
        return view;
    }
}
