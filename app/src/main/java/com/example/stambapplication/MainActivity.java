package com.example.stambapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static int CLASS_ID = 0;
    private final boolean isClassesEmpty = true;
    private final String url = "http://10.0.2.2:3000/";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        Button createClassButton = findViewById(R.id.createClassBtn);

        createClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToCreateClassView();
            }
        });

//        TODO : FETCH CLASSES DATA AND ADD THEM TO ACTIVE CLASS OR ARCHIVED CLASS
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(getRequest);

        createClassLists();


    }

    public void goToCreateClassView() {
        Intent createClassIntent = new Intent(this, CreateClass.class);

        startActivity(createClassIntent);
    }


    public void accessGroup(int classId) {
        Intent intent = new Intent(this, GroupsActivity.class);

        intent.putExtra(String.valueOf(CLASS_ID), classId);
        startActivity(intent);
    }

    public void createClassLists() {
        RecyclerView activeClassListView = findViewById(R.id.activeClassListView);

        List<ClassModel> activeClassList = new ArrayList<>();
        activeClassList.add(new ClassModel(1, "isi2_master_2020_2021"));
        activeClassList.add(new ClassModel(2, "web_master_2020_2021"));
        activeClassList.add(new ClassModel(3, "resaux_master_2020_2021"));

        ClassAdapter activeClassListAdapter = new ClassAdapter(activeClassList, classModel -> {
            accessGroup(classModel.getId());
        }
        );

        RecyclerView archivedClassListView = findViewById(R.id.archivedClassListView);

        List<ClassModel> archivedClassList = new ArrayList<>();
        archivedClassList.add(new ClassModel(1, "isi2_master_2019_2020"));


        ClassAdapter archivedClassListAdapter = new ClassAdapter(archivedClassList, classModel -> {
            accessGroup(classModel.getId());
        });

        activeClassListView.setAdapter(activeClassListAdapter);
        archivedClassListView.setAdapter(archivedClassListAdapter);

        activeClassListView.setLayoutManager(new LinearLayoutManager(this));
        archivedClassListView.setLayoutManager(new LinearLayoutManager(this));
    }


}