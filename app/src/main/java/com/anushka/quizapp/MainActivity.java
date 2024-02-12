package com.anushka.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anushka.quizapp.Activities.SetsActivity;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    CardView history, science;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if ActionBar is available
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // Hide the ActionBar if available
        } else {
            // Handle case where ActionBar is null (if needed)
        }

        history = findViewById(R.id.history);
        science = findViewById(R.id.science);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetsActivity.class);
                startActivity(intent);
            }
        });
    }
}
