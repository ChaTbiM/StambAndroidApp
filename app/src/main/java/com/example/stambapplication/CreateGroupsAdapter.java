package com.example.stambapplication;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CreateGroupsAdapter extends RecyclerView.Adapter<CreateGroupsAdapter.ViewHolder> {
    private List<GroupModel> groupList;


    public CreateGroupsAdapter(List<GroupModel> groupList) {
        this.groupList = groupList;
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
        holder.bind(groupList.get(position));

        GroupModel groupModel = groupList.get(position);

        TextView textView = holder.groupNumberText;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText groupNumberText;

        public ViewHolder(View itemView) {
            super(itemView);

            groupNumberText = (EditText) itemView.findViewById(R.id.groupNumberText);
        }

        public void bind(GroupModel groupModel) {
            groupNumberText.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString().trim())) {
                        groupModel.setGroupNumber(-1);
                    } else {
                        groupModel.setGroupNumber(Integer.parseInt(s.toString()));
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });

        }

    }

}
