package com.example.stambapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private boolean isClassesEmpty = true;
    private static final String SERVER = "http://10.0.2.2:3000/";
    private String url = "https://pokeapi.co/api/v2/pokemon/ditto";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        System.out.println("responnse" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        System.out.println("error");
                    }
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(getRequest);


    }
}