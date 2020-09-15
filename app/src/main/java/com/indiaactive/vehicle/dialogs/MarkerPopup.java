package com.indiaactive.vehicle.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;
import com.indiaactive.vehicle.R;

public class MarkerPopup extends DialogFragment {
    View view;
    TextView name,madeintv,rpDaytv,kmscompletedtv,rpHourtv;
    String vName,madeIn,kmscompleted,rentPerDay,rentPerHour,plate;
    Button viewInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.marker_popup, container, false);
        Bundle args = getArguments();
        vName = args.getString("name");
        madeIn = args.getString("madein");
        kmscompleted = args.getString("kms");
        rentPerDay = args.getString("rentperday");
        rentPerHour = args.getString("rentperHour");
        plate = args.getString("number");
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        name = view.findViewById(R.id.company_name);
        madeintv = view.findViewById(R.id.made);
        rpDaytv = view.findViewById(R.id.rentperday);
        kmscompletedtv = view.findViewById(R.id.kmscompleted);
        rpHourtv = view.findViewById(R.id.rentperhour);
        viewInfo = view.findViewById(R.id.pviewinfo);
        name.setText(vName);
        madeintv.setText(madeIn);
        kmscompletedtv.setText(kmscompleted);
        rpDaytv.setText(rentPerDay);
        rpHourtv.setText(rentPerHour);

        viewInfo.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_viewDetailsFragment);
            getDialog().dismiss();
        });
        return view;
    }
}
