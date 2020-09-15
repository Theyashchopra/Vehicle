package com.indiaactive.vehicle.dialogs;

import android.app.Dialog;
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
import com.indiaactive.vehicle.R;

public class LogoutPopup extends DialogFragment {

    View root;
    Button yesbt,nobt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.logout_popup, container, false);
        yesbt = root.findViewById(R.id.yes);
        nobt = root.findViewById(R.id.no);

        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        yesbt.setOnClickListener(v ->{
            logout();
            dialog.dismiss();

        });
        nobt.setOnClickListener(v ->{

            dialog.dismiss();

        });
        return root;
    }

    public void logout(){

    }
}
