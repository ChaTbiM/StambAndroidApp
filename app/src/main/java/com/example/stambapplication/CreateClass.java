package com.example.stambapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class CreateClass extends AppCompatActivity {

    public static boolean IS_TD_GROUPS_CHECKED = false;
    public static boolean IS_TP_GROUPS_CHECKED = false;

    public boolean isTdGroupsChecked = false;
    public boolean isTpGroupsChecked = false;

    public String specialty;
    public int grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        CheckBox hasTdGroups = findViewById(R.id.tdCheckbox);
        CheckBox hasTpGroups = findViewById(R.id.tpCheckbox);
        Button createClassNextBtn = findViewById(R.id.createClassNextBtn);

        EditText specialtyInput = findViewById(R.id.specialtyInput);
        EditText gradeInput = findViewById(R.id.gradeInput);

        specialtyInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                specialty = s.toString();

                if(specialty.length() > 0 && grade > 0 ){
                    createClassNextBtn.setEnabled(true);
                }else if(specialty.length() == 0 || grade <= 0) {
                    createClassNextBtn.setEnabled(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        gradeInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() > 0){
                    grade = Integer.parseInt(s.toString());
                }else {
                    grade = 0;
                }
                System.out.println("resutls   "+grade);
                if (!TextUtils.isEmpty( s.toString()) && !TextUtils.isEmpty( specialty) ) {
                    createClassNextBtn.setEnabled(true);
                }else {
                    createClassNextBtn.setEnabled(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });





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

        createClassNextBtn.setEnabled(false);
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

            intent.putExtra("IS_TD_GROUPS_CHECKED", isTdGroupsChecked);
            intent.putExtra("IS_TP_GROUPS_CHECKED", isTpGroupsChecked);
            startActivity(intent);
        }
    }
}
