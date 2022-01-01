package com.example.stambapplication;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private static Context appContext;
    private final OnClassClickListener clickListener;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String url = "http://10.0.2.2:3000/";
    private List<ClassModel> classList;
    private OnDeleteClassListener deleteClassListener;
    private OnEditClassListener editClassListener;
    private boolean isArchived = false;

    private String updatedModule;
    private String updatedSpecialty;
    private String updatedGrade;


    public ClassAdapter(List<ClassModel> classList, OnClassClickListener clickListener,
                        OnDeleteClassListener deleteClassListener,
                        OnEditClassListener editClassListener,
                        boolean isArchived) {
        this.classList = classList;
        this.clickListener = clickListener;
        this.deleteClassListener = deleteClassListener;
        this.editClassListener = editClassListener;
        this.isArchived = isArchived;
    }

    public ClassAdapter(List<ClassModel> classList, OnClassClickListener clickListener, boolean isArchived) {
        this.classList = classList;
        this.clickListener = clickListener;
        this.isArchived = isArchived;
    }

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        appContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.class_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ViewHolder holder, int position) {
        holder.bind(classList.get(position), clickListener, deleteClassListener, editClassListener);

        ClassModel classModel = classList.get(position);

        TextView textView = holder.classItemText;

        textView.setText(classModel.getName());

        if (isArchived) {
            ImageView deleteClassIcon = holder.deleteClassIcon;
            ImageView editClassIcon = holder.editClassIcon;

            deleteClassIcon.setVisibility(View.GONE);
            editClassIcon.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    public void removeClass(ClassModel classModel) {
        int position = classList.indexOf(classModel);
        this.classList.remove(classModel);
        notifyItemRemoved(position);
    }

    public void updateClass(ClassModel classModel){
        int position = classList.indexOf(classModel);
        classList.get(position).setGrade(Integer.parseInt(updatedGrade));
        classList.get(position).setModule(updatedModule);
        classList.get(position).setSpecialty(updatedSpecialty);

        notifyItemChanged(position);
    }

    public void deleteClassRequest(ClassModel classModel) {
        int classId = classModel.getId();
        StringRequest deleteClassRequest = new StringRequest(Request.Method.DELETE, url + "class/" + classId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ClassModel selectedClassModel = objectMapper.readValue(response, new TypeReference<ClassModel>() {
                    });
                    Log.d("deleted class ", response);
                    showToast(true, "Deleted Class Successfully");
                    removeClass(classModel);
                } catch (Exception e) {
                    showToast(true, "Class was not deleted , please try again");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("delete class error", error.toString());
                showToast(true, "Class was not deleted , please try again");
            }
        }
        );

        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(deleteClassRequest);
    }

    public void updateClassRequest(ClassModel classModel){
        int classId = classModel.getId();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("id", classId);
            jsonBody.put("module", updatedModule);
            jsonBody.put("specialty", updatedSpecialty);
            jsonBody.put("grade", updatedGrade);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updateClassRequest = new JsonObjectRequest(Request.Method.PUT, url + "class/" + classId, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ClassModel updatedClassModel = objectMapper.readValue(response.toString(), new TypeReference<ClassModel>() {
                    });
                    Log.d("updated class ", response.toString());
                    updateClass(classModel);
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showToast(true,"Class was updated !");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Unable to update a class", error.toString());
                showToast(false,"Unable to update a class !");
            }
        });

        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(updateClassRequest);

    }

    public void showToast(boolean isSuccessful, String message) {
        Toast toast = Toast.makeText(appContext,
                message,
                Toast.LENGTH_SHORT);

        toast.show();
    }

    public interface OnClassClickListener {
        void onClassClick(ClassModel classModel);
    }

    public interface OnDeleteClassListener {
        void onClassDeleteClick(PopupWindow popupWindow, ClassModel classModel);
    }

    public interface OnEditClassListener {
        void onClassEditClick(PopupWindow popupWindow, ClassModel classModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classItemText;
        public ImageView deleteClassIcon;
        public ImageView editClassIcon;
        public Button confirmButton;


        public ViewHolder(View itemView) {
            super(itemView);
            classItemText = itemView.findViewById(R.id.classItemText);
            deleteClassIcon = itemView.findViewById(R.id.deleteClassItem);
            editClassIcon = itemView.findViewById(R.id.editClassItem);
        }


        public void bind(ClassModel item,
                         final OnClassClickListener onClassClicklistener,
                         final OnDeleteClassListener onClassDeleteClickListener,
                         final OnEditClassListener onEditClassListener
        ) {
            classItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClassClicklistener.onClassClick(item);
                }
            });

            deleteClassIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_window, null, false);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    onClassDeleteClickListener.onClassDeleteClick(popupWindow, item);

                    Button cancelBtn = popupView.findViewById(R.id.cancelBtn);
                    Button confirmBtn = popupView.findViewById(R.id.confirmBtn);

                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteClassRequest(item);
                            popupWindow.dismiss();
                        }
                    });

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });


                }
            });

            editClassIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_window, null, false);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    onEditClassListener.onClassEditClick(popupWindow,item);

                    Button cancelBtn = popupView.findViewById(R.id.cancelBtn);
                    Button confirmBtn = popupView.findViewById(R.id.confirmBtn);

                    EditText moduleInput = popupView.findViewById(R.id.editModuleInput);
                    EditText specialtyInput = popupView.findViewById(R.id.editSpecialtyInput);
                    EditText gradeInput = popupView.findViewById(R.id.editGradeInput);

                    updatedGrade = gradeInput.getText().toString();
                    updatedModule = moduleInput.getText().toString();
                    updatedSpecialty = specialtyInput.getText().toString();


                    moduleInput.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable module) {
                            updatedModule = module.toString();
                            if (module.toString().length() > 0 && specialtyInput.getText().length() > 0 && gradeInput.getText().toString().trim().length() > 0) {
                                confirmBtn.setEnabled(true);
                            } else {
                                confirmBtn.setEnabled(false);
                            }
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });

                    specialtyInput.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable specialty) {
                            updatedSpecialty = specialty.toString();
                            if (moduleInput.getText().length() > 0 && specialty.toString().length() > 0 && gradeInput.getText().toString().trim().length() > 0) {
                                confirmBtn.setEnabled(true);
                            } else {
                                confirmBtn.setEnabled(false);
                            }
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });

                    gradeInput.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable grade) {
                            updatedGrade = grade.toString();
                            if (moduleInput.getText().length() > 0 && specialtyInput.getText().length() > 0 && grade.toString().trim().length() > 0) {
                                confirmBtn.setEnabled(true);
                            } else {
                                confirmBtn.setEnabled(false);
                            }
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });

                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateClassRequest(item);
                            popupWindow.dismiss();
                        }
                    });

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });

        }

    }



}