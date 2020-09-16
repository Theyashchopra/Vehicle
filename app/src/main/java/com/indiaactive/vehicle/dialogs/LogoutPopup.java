package com.indiaactive.vehicle.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.indiaactive.vehicle.activities.LoginActivity;

public class LogoutPopup extends DialogFragment {

    View root;
    boolean isLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button yesbt,nobt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.logout_popup, container, false);
        yesbt = root.findViewById(R.id.yes);
        nobt = root.findViewById(R.id.no);
        sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLogin = sharedPreferences.getBoolean("login",false);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        yesbt.setOnClickListener(v ->{
            logout();
            dialog.dismiss();
            editor.putBoolean("login",false);
            editor.apply();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
        nobt.setOnClickListener(v ->{

            dialog.dismiss();

        });
        return root;
    }

    public void logout(){

    }
}
