package com.example.stambapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String url = "http://10.0.2.2:3000/";
    private List<GroupModel> groupList;
    private Context appContext;
    private OnDeleteGroupListener onDeleteGroupListener;

    public GroupAdapter(List<GroupModel> groupList) {
        this.groupList = groupList;
    }
    public GroupAdapter(List<GroupModel> groupList, OnDeleteGroupListener onDeleteGroupListener) {
        this.groupList = groupList;
        this.onDeleteGroupListener = onDeleteGroupListener;
    }


    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        appContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.group_item, parent, false);

        GroupAdapter.ViewHolder viewHolder = new GroupAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        holder.bind(groupList.get(position), onDeleteGroupListener);

        GroupModel groupModel = groupList.get(position);

        TextView textView = holder.groupItemText;
        textView.setText("Group " + groupModel.getType() + "  N: " + groupModel.getNumber());

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void removeGroup(GroupModel groupModel) {
        int position = groupList.indexOf(groupModel);
        this.groupList.remove(groupModel);
        notifyItemRemoved(position);
    }

    public void deleteGroupRequest(GroupModel groupModel) {
        int groupId = groupModel.getId();
        StringRequest deleteGroupRequest = new StringRequest(Request.Method.DELETE, url + "groups/" + groupId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ClassModel selectedClassModel = objectMapper.readValue(response, new TypeReference<ClassModel>() {
                    });
                    Log.d("deleted class ", response);
                    showToast(true, "Deleted Class Successfully");
                    removeGroup(groupModel);
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

        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(deleteGroupRequest);
    }

    public void showToast(boolean isSuccessful, String message) {
        Toast toast = Toast.makeText(appContext,
                message,
                Toast.LENGTH_SHORT);

        toast.show();
    }

    public interface OnDeleteGroupListener {
        void onGroupDeleteClick(PopupWindow popupWindow, GroupModel groupModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupItemText;
        public ImageView deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteGroupIcon);
            groupItemText = (TextView) itemView.findViewById(R.id.groupItemText);

        }

        public void bind (GroupModel item , OnDeleteGroupListener onDeleteGroupListener){
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_window, null, false);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    onDeleteGroupListener.onGroupDeleteClick(popupWindow, item);

                    Button cancelBtn = popupView.findViewById(R.id.cancelBtn);
                    Button confirmBtn = popupView.findViewById(R.id.confirmBtn);

                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteGroupRequest(item);
                            removeGroup(item);
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
