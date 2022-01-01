package com.example.stambapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {
    private final String url = "http://10.0.2.2:3000/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<GroupModel> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Intent intent = getIntent();
        int classId = intent.getIntExtra(String.valueOf(MainActivity.CLASS_ID), 0);

        StringRequest getGroups = new StringRequest(Request.Method.GET, url + "class/" + classId + "/groups", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    groupList = objectMapper.readValue(response, new TypeReference<List<GroupModel>>() {
                    });
                    showGroups(groupList);
                    Log.d("groups", response.toString());
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


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(getGroups);

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

    public void showGroups (List<GroupModel> groups){
        RecyclerView groupListView = (RecyclerView) findViewById(R.id.groupListView);

        GroupAdapter groupListAdapter = new GroupAdapter(groupList, (popupWindow, groupModel)  ->{
            View popupView = popupWindow.getContentView();

            populateDeletePopupContent(popupView, groupModel);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.groupsActivity).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });

            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);
        });

        groupListView.setAdapter(groupListAdapter);

        groupListView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void populateDeletePopupContent(View popupView, GroupModel groupModel) {
        TextView popupTitle = popupView.findViewById(R.id.popupTitle);
        popupTitle.setText("Delete Group");

        TextView popupContent = popupView.findViewById(R.id.popupContent);
        popupContent.setText("Are you sure you want to delete group with number  " + groupModel.getNumber());

        popupView.findViewById(R.id.popupContent).setVisibility(View.VISIBLE);
        popupView.findViewById(R.id.popupEditContentContainer).setVisibility(View.GONE);

        findViewById(R.id.groupsActivity).setBackgroundColor(Color.parseColor("#989696"));
        popupView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }




}
