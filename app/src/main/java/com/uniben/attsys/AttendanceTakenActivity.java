package com.uniben.attsys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uniben.attsys.utils.Constants;

public class AttendanceTakenActivity extends AppCompatActivity {

    private Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_taken_layout);
        homeButton = findViewById(R.id.home_btn);
//        intent.putExtra(Constants.USER_KEY, user);
        homeButton.setOnClickListener(v -> finish());
    }
}
