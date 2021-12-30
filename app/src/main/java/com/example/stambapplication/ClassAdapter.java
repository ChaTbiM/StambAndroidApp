package com.example.stambapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private final OnClassClickListener clickListener;
    private List<ClassModel> classList;
    private OnDeleteClassListener deleteClassListener;
    private OnEditClassListener editClassListener;
    private boolean isArchived = false;

    public ClassAdapter(List<ClassModel> classList, OnClassClickListener clickListener, OnDeleteClassListener deleteClassListener, OnEditClassListener editClassListener, boolean isArchived) {
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


    public interface OnClassClickListener {
        void onClassClick(ClassModel classModel);
    }


    public interface OnDeleteClassListener {
        void onClassDeleteClick(View view,ClassModel classModel);
    }

    public interface OnEditClassListener {
        void onClassEditClick(View view, ClassModel classModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classItemText;
        public ImageView deleteClassIcon;
        public ImageView editClassIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            classItemText = itemView.findViewById(R.id.classItemText);
            deleteClassIcon = itemView.findViewById(R.id.deleteClassItem);
            editClassIcon = itemView.findViewById(R.id.editClassItem);

        }

        public void bind(final ClassModel item, final OnClassClickListener onClassClicklistener, final OnDeleteClassListener onClassDeleteClickListener, final OnEditClassListener onEditClassListener) {
            classItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClassClicklistener.onClassClick(item);
                }
            });

            deleteClassIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClassDeleteClickListener.onClassDeleteClick(view,item);
                }
            });

            editClassIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditClassListener.onClassEditClick(view, item );
                }
            });
        }

    }

}