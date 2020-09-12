package com.lifecapable.vehicle.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.lifecapable.vehicle.R;

public class RequestPopup extends DialogFragment {
    View view;
    Button donebt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.request_popup,container,false);

        donebt = view.findViewById(R.id.donebt);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        donebt.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_viewDetailsFragment_to_navigation_home);
        super.onDismiss(dialog);
    }
}
