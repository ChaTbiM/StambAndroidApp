package com.example.stambapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GroupsActivity extends AppCompatActivity {
    private final String url = "http://10.0.2.2:3000/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File copiedGroupExcelFile;
    private InputStream openedGroupInputStream;

    private List<GroupModel> groupList = new ArrayList<>();

    private final ActivityResultLauncher<Intent> importGroupsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        Uri fileUri = Uri.parse(intent.getDataString());


                    }
                }
            });


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


        Button importGroupsBtn = findViewById(R.id.importGroupBtn);

        importGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromExcelFile();
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


    public void fetchDataFromExcelFile (){
        Intent pickFileIntent = new Intent(Intent.ACTION_GET_CONTENT);

        pickFileIntent.setType("*/*");
        pickFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickFileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        importGroupsLauncher.launch(pickFileIntent);
    }

//    public void importFile(Uri uri) throws IOException {
//        String fileName = getFileName(uri);
//
//        // The temp file could be whatever you want
////        File fileCopy = copyToTempFile(uri, dynamic);
//        File fileCopy = copyToTempFile(uri ,openedGroupExcelFile);
//
//
//    }

    public InputStream getDataFromExcel (Uri uri) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return inputStream;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Obtains the file name for a URI using content resolvers. Taken from the following link
     * https://developer.android.com/training/secure-file-sharing/retrieve-info.html#RetrieveFileInfo
     *
     * @param uri a uri to query
     * @return the file name with no path
     * @throws IllegalArgumentException if the query is null, empty, or the column doesn't exist
     */
    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }

    /**
     * Copies a uri reference to a temporary file
     *
     * @param uri      the uri used as the input stream
     * @param tempFile the file used as an output stream
     * @return the input tempFile for convenience
     * @throws IOException if an error occurs
     */
    private File copyToTempFile(Uri uri, File tempFile) throws IOException {
        // Obtain an input stream from the uri
        InputStream inputStream = getContentResolver().openInputStream(uri);

        if (inputStream == null) {
            throw new IOException("Unable to obtain input stream from URI");
        }

        // Copy the stream to the temp file
        FileUtils.copyInputStreamToFile(inputStream, tempFile);

        return tempFile;
    }

    private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(DateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(DateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.d("error output" ,e.toString());
        }
        return value;
    }



}
