package com.example.madasignment.lessons.Module.module;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.lessons.Module.read.ReadOnlyActivity;
import com.example.madasignment.lessons.Module.video.video;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    private final List<ModuleData> moduleList;

    public ModuleAdapter(List<ModuleData> moduleList) {
        this.moduleList = new ArrayList<>(moduleList); // Use a copy of the list
    }

    public void updateData(List<ModuleData> newModules) {
        moduleList.clear();
        moduleList.addAll(newModules);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModuleData module = moduleList.get(position);
        holder.moduleTitle.setText(module.getTitle());
        holder.moduleImage.setImageResource(module.getImageResource());

        // Navigate to the "Read" page
        holder.readButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ReadOnlyActivity.class);
            intent.putExtra("readContent", module.getReadContent());
            context.startActivity(intent);
        });

        // Navigate to the "Listen" page
        holder.listenButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, video.class);
            intent.putExtra("videoUrl", module.getVideoUrl());
            intent.putExtra("transcriptionFile", module.getReadContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView moduleTitle;
        ImageView moduleImage;
        View readButton, listenButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleTitle = itemView.findViewById(R.id.moduleTitle);
            moduleImage = itemView.findViewById(R.id.moduleImage);
            readButton = itemView.findViewById(R.id.readIcon);
            listenButton = itemView.findViewById(R.id.listenIcon);
        }
    }
}
