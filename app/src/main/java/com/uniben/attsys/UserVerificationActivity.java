package com.uniben.attsys;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.todddavies.components.progressbar.ProgressWheel;

public class UserVerificationActivity extends AppCompatActivity {

    private ProgressWheel progressWheel;
    private ImageView imageView;
    private Button proceedButton;
    private ConstraintLayout locationVerifiedLayout;
    private ConstraintLayout verifyingLayout;
    private FrameLayout fragContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressWheel = findViewById(R.id.progress_wheel);
        verifyingLayout = findViewById(R.id.rootview_verifying);
        fragContainer = findViewById(R.id.fragment_container);


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressWheel.startSpinning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressWheel.stopSpinning();
    }


    public void dummyTest(View view) {
        fragContainer.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new UserVerifiedFrag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
