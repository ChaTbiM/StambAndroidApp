package com.example.stambapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<GroupModel> groupList;


    public GroupAdapter(List<GroupModel> groupList) {
        this.groupList = groupList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupItemText;

        public ViewHolder(View itemView) {
            super(itemView);

            groupItemText = (TextView) itemView.findViewById(R.id.groupItemText);
        }

    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.group_item, parent, false);

        GroupAdapter.ViewHolder viewHolder = new GroupAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        GroupModel groupModel = groupList.get(position);

        TextView textView = holder.groupItemText;
        textView.setText("Group " + groupModel.getType() + "  N: " + groupModel.getGroupNumber());
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }

}
