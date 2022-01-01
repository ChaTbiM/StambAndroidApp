package com.example.stambapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends AppCompatActivity {
    public static int CLASS_ID = 0;
    private final boolean isClassesEmpty = true;
    private final String url = "http://10.0.2.2:3000/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RequestQueue queue;
    private ArrayList<ClassModel> activeClasses = new ArrayList<ClassModel>();
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        ClassModel createdClass = (ClassModel) intent.getSerializableExtra("CreatedClass");
                        activeClasses.add(createdClass);
                        createActiveClasses(activeClasses);
                    }
                }
            });
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

        mStartForResult.launch(createClassIntent);
    }


    public void accessGroup(int classId) {
        Intent intent = new Intent(this, GroupsActivity.class);

        intent.putExtra(String.valueOf(CLASS_ID), classId);


        startActivity(intent);
    }


    public void populateDeletePopupContent(View popupView, ClassModel classModel) {
        TextView popupTitle = popupView.findViewById(R.id.popupTitle);
        popupTitle.setText("Delete Class");

        TextView popupContent = popupView.findViewById(R.id.popupContent);
        popupContent.setText("Are you sure you want to delete class with name  " + classModel.getName());

        popupView.findViewById(R.id.popupContent).setVisibility(View.VISIBLE);
        popupView.findViewById(R.id.popupEditContentContainer).setVisibility(View.GONE);

        findViewById(R.id.main).setBackgroundColor(Color.parseColor("#989696"));
        popupView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    public void populateEditPopupContent(View popupView, ClassModel classModel) {
        TextView popupTitle = popupView.findViewById(R.id.popupTitle);
        popupTitle.setText("Edit Class");

        TextView popupContent = popupView.findViewById(R.id.popupContent);
        popupContent.setText("Are you sure you want to delete class with name  " + classModel.getName());

        popupView.findViewById(R.id.popupContent).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupEditContentContainer).setVisibility(View.VISIBLE);

        EditText moduleInput = popupView.findViewById(R.id.editModuleInput);
        EditText specialtyInput = popupView.findViewById(R.id.editSpecialtyInput);
        EditText gradeInput = popupView.findViewById(R.id.editGradeInput);

        moduleInput.setText(classModel.getModule());
        specialtyInput.setText(classModel.getSpecialty());
        gradeInput.setText(String.valueOf(classModel.getGrade()));
        gradeInput.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5")});

        findViewById(R.id.main).setBackgroundColor(Color.parseColor("#989696"));
        popupView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }


    public void createActiveClasses(ArrayList<ClassModel> activeClasses) {
        RecyclerView activeClassListView = findViewById(R.id.activeClassListView);

        ArrayList<ClassModel> activeClassList = activeClasses;

        ClassAdapter activeClassListAdapter = new ClassAdapter(activeClassList, classModel -> {
            accessGroup(classModel.getId());
        }, (popupWindow, classModel) -> {
            View popupView = popupWindow.getContentView();

            populateDeletePopupContent(popupView, classModel);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.main).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });

            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

        }, (popupWindow, classModel) -> {

            View popupView = popupWindow.getContentView();

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupView.findViewById(R.id.main).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });
            populateEditPopupContent(popupView, classModel);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.main).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });

            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

        }, false
        );

        activeClassListView.setAdapter(activeClassListAdapter);
        activeClassListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createArchivedClasses(ArrayList<ClassModel> archivedClasses) {
        RecyclerView archivedClassListView = findViewById(R.id.archivedClassListView);

        List<ClassModel> archivedClassList = archivedClasses;

        ClassAdapter archivedClassListAdapter = new ClassAdapter(archivedClassList, classModel -> {
            accessGroup(classModel.getId());
        }, true);

        archivedClassListView.setAdapter(archivedClassListAdapter);
        archivedClassListView.setLayoutManager(new LinearLayoutManager(this));
    }

}