package com.example.carpriceestimator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carpriceestimator.authentication.LoginActivity;

public class FlashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        new Handler().postDelayed(() -> {
            String userEmail = sharedPreferences.getString(Constants.USER_EMAIL, "");
            boolean invalidSession = userEmail.equals("");

            Intent i;
            if (invalidSession) {
                i = new Intent(FlashScreenActivity.this, LoginActivity.class);
            } else {
                i = new Intent(FlashScreenActivity.this, MainActivity.class);
            }
            startActivity(i); //start new activity
            finish();
        }, 3000);

    }
}