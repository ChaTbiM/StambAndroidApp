package com.example.stambapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    public interface OnClassClickListener {
        void onClassClick(ClassModel classModel);
    }

    private  List<ClassModel> classList;
    private final OnClassClickListener clickListener;


    public ClassAdapter(List<ClassModel> classList,OnClassClickListener clickListener) {
        this.classList = classList;
        this.clickListener = clickListener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classItemText;

        public ViewHolder(View itemView) {
            super(itemView);

            classItemText = (TextView) itemView.findViewById(R.id.classItemText);
        }

        public void bind(final ClassModel item, final OnClassClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onClassClick(item);
                }
            });
        }

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
        holder.bind(classList.get(position), clickListener);

        ClassModel classModel = classList.get(position);

        TextView textView = holder.classItemText;
        textView.setText(classModel.getName());
    }



    @Override
    public int getItemCount() {
        return classList.size();
    }

}