package com.uniben.attsys;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {


    private Button button;
    private FrameLayout frameLayout;
    public AttendanceFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        button = view.findViewById(R.id.take_attendance_btn);
        frameLayout = view.findViewById(R.id.attendance_indicator_card);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserVerificationActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createAnimation();
    }

    private void createAnimation(){

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                frameLayout,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

    }

}
