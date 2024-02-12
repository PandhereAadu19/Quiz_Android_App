package com.anushka.quizapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anushka.quizapp.R;
import com.anushka.quizapp.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {
    ActionBar actionBar;

    ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if ActionBar is available
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // Hide the ActionBar if available
        } else {
            // Handle case where ActionBar is null (if needed)
        }

        int totalScore = getIntent().getIntExtra("total", 0);
        int correctAns = getIntent().getIntExtra("score", 0);

        int wrong = totalScore - correctAns;

        binding.totalQuestions.setText(String.valueOf(totalScore));
        binding.rightAns.setText(String.valueOf(correctAns));
        binding.wrongAns.setText(String.valueOf(wrong));

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, SetsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}