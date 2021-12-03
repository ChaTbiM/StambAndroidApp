package com.example.stambapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreateClass extends AppCompatActivity {
    public static boolean IS_TD_GROUPS_CHECKED = false;
    public static boolean IS_TP_GROUPS_CHECKED = false;
    public boolean isTdGroupsChecked = false;
    public boolean isTpGroupsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        CheckBox hasTdGroups = findViewById(R.id.tdCheckbox);
        CheckBox hasTpGroups = findViewById(R.id.tpCheckbox);
        Button createClassNextBtn = findViewById(R.id.createClassNextBtn);

        hasTdGroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTdGroupsChecked = isChecked;
                if (isChecked || isTpGroupsChecked) {
                    changeButtonText("Next", createClassNextBtn);
                } else {
                    changeButtonText("Create Class", createClassNextBtn);
                }

            }
        });

        hasTpGroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTpGroupsChecked = isChecked;
                if (isChecked || isTdGroupsChecked) {
                    changeButtonText("Next", createClassNextBtn);
                } else {
                    changeButtonText("Create Class", createClassNextBtn);
                }
            }
        });

        createClassNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTdGroupsChecked || isTpGroupsChecked) {
                    onCreateClassNextClickHandler(false);
                } else {
                    onCreateClassNextClickHandler(true);
                }
            }
        });


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

    public void changeButtonText(String text, Button btn) {
        btn.setText(text);
    }

    public void onCreateClassNextClickHandler(boolean isFinalStep) {
        if (isFinalStep) {
//            TODO : SEND POST REQUEST TO CREATE A CLASS WITHOUT GROUPS

        } else {
            Intent intent = new Intent(this, CreateGroups.class);

            intent.putExtra(String.valueOf(IS_TD_GROUPS_CHECKED), isTdGroupsChecked);
            intent.putExtra(String.valueOf(IS_TP_GROUPS_CHECKED), isTpGroupsChecked);
            startActivity(intent);
        }
    }
}
