package com.example.stambapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CreateGroupsAdapter extends RecyclerView.Adapter<CreateGroupsAdapter.ViewHolder> {
    private List<GroupModel> groupList;


    public CreateGroupsAdapter(List<GroupModel> groupList) {
        this.groupList = groupList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupNumberText;

        public ViewHolder(View itemView) {
            super(itemView);

            groupNumberText = (TextView) itemView.findViewById(R.id.groupNumberText);
        }

    }

    @Override
    public CreateGroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.group_number_item, parent, false);

        CreateGroupsAdapter.ViewHolder viewHolder = new CreateGroupsAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CreateGroupsAdapter.ViewHolder holder, int position) {
//        GroupModel groupModel = groupList.get(position);
//
//        TextView textView = holder.groupNumberText;
//            textView.setText("Group " + groupModel.getType() + "  N: " + groupModel.getGroupNumber());
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }

}
