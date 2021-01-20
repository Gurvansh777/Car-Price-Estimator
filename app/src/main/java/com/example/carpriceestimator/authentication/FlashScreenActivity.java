package com.example.carpriceestimator.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.carpriceestimator.MainActivity;
import com.example.carpriceestimator.R;

public class FlashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(FlashScreenActivity.this, MainActivity.class);
                startActivity(i); //start new activity
                finish();
            }
        }, 3000);

    }
}