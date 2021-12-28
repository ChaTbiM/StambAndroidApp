package com.example.stambapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static int CLASS_ID = 0;
    private final boolean isClassesEmpty = true;
    private final String url = "http://10.0.2.2:3000/";
    private RequestQueue queue;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<ClassModel> activeClasses = new ArrayList<ClassModel>();
    private ArrayList<ClassModel> archivedClasses = new ArrayList<ClassModel>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

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


        StringRequest getActiveClasses = new StringRequest(Request.Method.GET, url + "active-classes", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    activeClasses = objectMapper.readValue(response, new TypeReference<List<ClassModel>>() {
                    });
                    createActiveClasses(activeClasses);
                    Log.d("active", activeClasses.toString());
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });


        StringRequest getArchivedClasses = new StringRequest(Request.Method.GET, url + "archived-classes", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    archivedClasses = objectMapper.readValue(response, new TypeReference<List<ClassModel>>() {
                    });
                    createArchivedClasses(archivedClasses);
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(getActiveClasses);
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(getArchivedClasses);
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

    public void createActiveClasses(ArrayList<ClassModel> activeClasses) {
        RecyclerView activeClassListView = findViewById(R.id.activeClassListView);

        ArrayList<ClassModel> activeClassList = activeClasses;

        ClassAdapter activeClassListAdapter = new ClassAdapter(activeClassList, classModel -> {
            accessGroup(classModel.getId());
        }
        );

        activeClassListView.setAdapter(activeClassListAdapter);
        activeClassListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createArchivedClasses(ArrayList<ClassModel> archivedClasses) {
        RecyclerView archivedClassListView = findViewById(R.id.archivedClassListView);

        List<ClassModel> archivedClassList = archivedClasses;

        ClassAdapter archivedClassListAdapter = new ClassAdapter(archivedClassList, classModel -> {
            accessGroup(classModel.getId());
        });

        archivedClassListView.setAdapter(archivedClassListAdapter);
        archivedClassListView.setLayoutManager(new LinearLayoutManager(this));
    }


}