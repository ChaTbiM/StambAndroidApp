package com.example.stambapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ClassAdapter extends
        RecyclerView.Adapter<ClassAdapter.ViewHolder> {



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classItemText;

        public ViewHolder(View itemView) {
            super(itemView);

            classItemText = (TextView) itemView.findViewById(R.id.classItemText);
        }
    }

    private List<ClassModel> classList;

    public ClassAdapter(List<ClassModel> classList) {
        this.classList = classList;
    }

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.class_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ViewHolder holder, int position) {
        ClassModel classModel = classList.get(position);

        TextView textView = holder.classItemText;
        textView.setText(classModel.getName());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

}