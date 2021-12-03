package com.example.stambapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER = "http://10.0.2.2:3000/";
    public static int CLASS_ID = 0;
    private final boolean isClassesEmpty = true;
    private final String url = "https://pokeapi.co/api/v2/pokemon/ditto";
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
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Response", response.toString());
//                        System.out.println("responnse" + response.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                        System.out.println("error");
//                    }
//                }
//        );
//
//        RequestQueueSingleton.getInstance(this).addToRequestQueue(getRequest);

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