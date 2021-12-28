package com.example.stambapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateClass extends AppCompatActivity {
    private final String url = "http://10.0.2.2:3000/";
    public String module = "";
    public String specialty = "";
    public int grade = 0;
    private RequestQueue queue;
    private JsonObjectRequest createClassRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();


        Button createClassFinalBtn = findViewById(R.id.createClassFinalBtn);

        EditText moduleInput = findViewById(R.id.moduleInput);
        EditText specialtyInput = findViewById(R.id.specialtyInput);
        EditText gradeInput = findViewById(R.id.gradeInput);
        gradeInput.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "5")});

        moduleInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                module = s.toString();
                enableCreateClassBtn(createClassFinalBtn);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        specialtyInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                specialty = s.toString();
                enableCreateClassBtn(createClassFinalBtn);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        gradeInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    grade = Integer.parseInt(s.toString());
                } else {
                    grade = 0;
                }
                enableCreateClassBtn(createClassFinalBtn);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        createClassFinalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateClassHandler();
                goToMainView();
            }
        });

        createClassFinalBtn.setEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onCreateClassHandler() {
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("module", module);
            jsonBody.put("specialty", specialty);
            jsonBody.put("grade", grade);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest createClassRequest = new JsonObjectRequest(Request.Method.POST, url + "active-classes", jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("post response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("post response", error.toString());
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(createClassRequest);
    }

    public void enableCreateClassBtn(Button createClassButton) {
        if (module.length() > 0 && specialty.length() > 0 && grade > 0) {
            createClassButton.setEnabled(true);
        } else {
            createClassButton.setEnabled(false);
        }
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

    public void goToMainView() {
        Intent mainViewIntent = new Intent(this, MainActivity.class);

        startActivity(mainViewIntent);
    }

}
