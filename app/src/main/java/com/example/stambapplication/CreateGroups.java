package com.example.stambapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CreateGroups extends AppCompatActivity {
    public int numberOfTdGroups = 0;
    public int numberOfTpGroups = 0;
    public Boolean isTdGroupsChecked;
    public Boolean isTpGroupsChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);

        Intent intent = getIntent();
        isTdGroupsChecked = getIntent().getExtras().getBoolean("IS_TD_GROUPS_CHECKED");
        isTpGroupsChecked = getIntent().getExtras().getBoolean("IS_TP_GROUPS_CHECKED");

        LinearLayout tdGroupsLinearLayout = findViewById(R.id.tdGroupsLinearLayout);
        LinearLayout tpGroupsLinearLayout = findViewById(R.id.tpGroupsLinearLayout);

        if (!isTdGroupsChecked) {
            tdGroupsLinearLayout.setVisibility(View.GONE);
        }

        if (!isTpGroupsChecked) {
            tpGroupsLinearLayout.setVisibility(View.GONE);
        }

        Button finalCreateClass = findViewById(R.id.finalCreateClass);

        finalCreateClass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                TODO : post request to create classes with groups
                goToMainScreen();
            }
        });

        EditText numberOfTdGroupsEditText = (findViewById(R.id.numberOfTdGroups));
        EditText numberOfTpGroupsEditText = findViewById(R.id.numberOfTpGroups);


        numberOfTdGroupsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    numberOfTdGroups = Integer.parseInt(s.toString());
                } else {
                    numberOfTdGroups = 0;
                }
                createGroups(numberOfTdGroups, numberOfTpGroups);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        numberOfTpGroupsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    numberOfTpGroups = Integer.parseInt(s.toString());
                } else {
                    numberOfTpGroups = 0;
                }
                createGroups(numberOfTdGroups, numberOfTpGroups);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        createGroups(0, 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void createGroups(int numberOfTdGroups, int numberOfTpGroups) {
        RecyclerView tdGroupsView = findViewById(R.id.tdGroupsView);
        RecyclerView tpGroupsView = findViewById(R.id.tpGroupsView);


        List<GroupModel> tdGroups = new ArrayList<>();
        String tdGroupType = "td";


        for (int i = 0; i < numberOfTdGroups; i++) {
            tdGroups.add(new GroupModel(tdGroupType));
        }

        List<GroupModel> tpGroups = new ArrayList<>();
        String tpGroupType = "tp";


        for (int i = 0; i < numberOfTpGroups; i++) {
            tpGroups.add(new GroupModel(tdGroupType));
        }

        CreateGroupsAdapter tdGroupsAdapter = new CreateGroupsAdapter(tdGroups);
        CreateGroupsAdapter tpGroupsAdapter = new CreateGroupsAdapter(tpGroups);

        tdGroupsView.setAdapter(tdGroupsAdapter);
        tpGroupsView.setAdapter(tpGroupsAdapter);

        tdGroupsView.setLayoutManager(new LinearLayoutManager(this));
        tpGroupsView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goToMainScreen() {
        Intent mainScreen = new Intent(this, MainActivity.class);

        startActivity(mainScreen);
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
