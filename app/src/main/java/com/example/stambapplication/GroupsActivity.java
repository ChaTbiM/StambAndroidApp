package com.example.stambapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Intent intent = getIntent();
        int classId = intent.getIntExtra(String.valueOf(MainActivity.CLASS_ID), 0);
        System.out.println("class id is " + classId);

        RecyclerView groupListView = (RecyclerView) findViewById(R.id.groupListView);

        List<GroupModel> groupList = new ArrayList<>();
//        TODO : FETCH GROUP LIST PER CLASS ID AND add to groupLIST
        groupList.add(new GroupModel(1, 1 + classId, "td"));
        groupList.add(new GroupModel(2, 2 + classId, "tp"));
        groupList.add(new GroupModel(3, 3 + classId, "tp"));

        GroupAdapter groupListAdapter = new GroupAdapter(groupList);

        groupListView.setAdapter(groupListAdapter);

        groupListView.setLayoutManager(new LinearLayoutManager(this));


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    public void getAllClassGroups (int classId) {

    }


}
