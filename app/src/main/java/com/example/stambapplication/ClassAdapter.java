package com.example.stambapplication;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

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
    private OnPopupConfirmClickListener onPopupConfirmClickLister;
    private OnPopupCancelCancelListener onPopupCancelCancelListener;
    private boolean isArchived = false;


    public ClassAdapter(List<ClassModel> classList, OnClassClickListener clickListener,
                        OnDeleteClassListener deleteClassListener,
                        OnEditClassListener editClassListener,
                        OnPopupConfirmClickListener onPopupConfirmClickListener,
                        OnPopupCancelCancelListener onPopupCancelCancelListener,
                        boolean isArchived) {
        this.classList = classList;
        this.clickListener = clickListener;
        this.deleteClassListener = deleteClassListener;
        this.editClassListener = editClassListener;
        this.onPopupConfirmClickLister = onPopupConfirmClickListener;
        this.onPopupCancelCancelListener = onPopupCancelCancelListener;
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
        holder.bind(classList.get(position), clickListener, deleteClassListener, editClassListener, onPopupConfirmClickLister, onPopupCancelCancelListener);

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
                    System.out.println("errooor");
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


    public interface OnPopupConfirmClickListener {
        void onPopupConfirmClickListener(ClassModel classModel);
    }

    public interface OnPopupCancelCancelListener {
        void onPopupCancelClickListener(ClassModel classModel);
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
                         final OnEditClassListener onEditClassListener,
                         final OnPopupConfirmClickListener onPopupConfirmClickListener,
                         final OnPopupCancelCancelListener onPopupCancelCancelListener
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
                            onPopupConfirmClickListener.onPopupConfirmClickListener(item);
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
                    onEditClassListener.onClassEditClick(popupWindow, item);
                }
            });

        }

    }


}