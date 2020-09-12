package com.lifecapable.vehicle.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lifecapable.vehicle.R;
import com.lifecapable.vehicle.dialogs.LogoutPopup;

public class NotificationsFragment extends Fragment {
    View root;
    Button editbt, donebt, logoutbt;
    LogoutPopup logoutPopup;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        editbt = root.findViewById(R.id.edit);
        donebt = root.findViewById(R.id.done);
        logoutbt = root.findViewById(R.id.logout);
        logoutPopup = new LogoutPopup();

        editbt.setOnClickListener(v -> {
            donebt.setVisibility(View.VISIBLE);
            editbt.setVisibility(View.GONE);


        });
        donebt.setOnClickListener(v -> {
            donebt.setVisibility(View.GONE);
            editbt.setVisibility(View.VISIBLE);

        });

        logoutbt.setOnClickListener(v -> {
            logoutPopup.show(getActivity().getSupportFragmentManager(),"logout");

        });

        return root;
    }
}