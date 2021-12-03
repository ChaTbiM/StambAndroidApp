package com.example.stambapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CreateGroups extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);

        createGroups();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void createGroups() {
        RecyclerView tdGroupsView = findViewById(R.id.tdGroupsView);

        List<GroupModel> tdGroups = new ArrayList<>();
        tdGroups.add(new GroupModel(2, "web_master_2020_2021"));
        tdGroups.add(new GroupModel(3, "resaux_master_2020_2021"));


        GroupAdapter tdGroupsAdapter = new GroupAdapter(tdGroups);

        tdGroupsView.setAdapter(tdGroupsAdapter);

        tdGroupsView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
