package com.anushka.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.anushka.quizapp.MainActivity;
import com.anushka.quizapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
