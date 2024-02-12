package com.anushka.quizapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.anushka.quizapp.Adapters.SetAdapter;
import com.anushka.quizapp.Models.SetModel;
import com.anushka.quizapp.R;
import com.anushka.quizapp.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity extends AppCompatActivity {

    ActivitySetsBinding binding;
    ArrayList<SetModel> list;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if ActionBar is available
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // Hide the ActionBar if available
        } else {
            // Handle case where ActionBar is null (if needed)
        }

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("SET-1"));
        list.add(new SetModel("SET-2"));
        list.add(new SetModel("SET-3"));
        list.add(new SetModel("SET-4"));
        list.add(new SetModel("SET-5"));
        list.add(new SetModel("SET-6"));
        list.add(new SetModel("SET-7"));
        list.add(new SetModel("SET-8"));
        list.add(new SetModel("SET-9"));
        list.add(new SetModel("SET-10"));

        SetAdapter adapter = new SetAdapter(this, list);
        binding.setsRecy.setAdapter(adapter);

    }
}