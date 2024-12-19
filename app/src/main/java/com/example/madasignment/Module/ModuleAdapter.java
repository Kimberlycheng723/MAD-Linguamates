package com.example.madasignment.Module;

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
import com.example.madasignment.read.ReadOnlyActivity;
import com.example.madasignment.video.video;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    private final List<ModuleData> moduleList;
    private final Runnable updateProgressCallback;

    public ModuleAdapter(List<ModuleData> moduleList, Runnable updateProgressCallback) {
        this.moduleList = moduleList;
        this.updateProgressCallback = updateProgressCallback;
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

        // Check if module is completed
        if (module.isCompleted()) {
            holder.completedIcon.setVisibility(View.VISIBLE); // Show blue tick
        } else {
            holder.completedIcon.setVisibility(View.GONE); // Hide blue tick
        }

        // Navigate to the "Read" page
        holder.readButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ReadOnlyActivity.class);
            intent.putExtra("readContent", module.getReadContent());
            context.startActivity(intent);

            // Mark the module as read
            module.setReadCompleted(true);
            checkCompletionAndUpdate(holder.getAdapterPosition());
        });

        // Navigate to the "Listen" page
        holder.listenButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, video.class);
            intent.putExtra("videoUrl", module.getVideoUrl());
            intent.putExtra("transcriptionFile", module.getReadContent());
            context.startActivity(intent);

            // Mark the module as listened
            module.setListenCompleted(true);
            checkCompletionAndUpdate(holder.getAdapterPosition());
        });
    }

    private void checkCompletionAndUpdate(int position) {
        ModuleData module = moduleList.get(position);

        // Update blue tick
        if (module.isCompleted()) {
            notifyItemChanged(position); // Update blue tick visibility

            // Update progress bar only when both read and listen are completed
            updateProgressCallback.run();
        }
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView moduleTitle;
        ImageView moduleImage;
        ImageView completedIcon; // Blue tick icon
        View readButton, listenButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleTitle = itemView.findViewById(R.id.moduleTitle);
            moduleImage = itemView.findViewById(R.id.moduleImage);
            completedIcon = itemView.findViewById(R.id.completedIcon); // Blue tick icon
            readButton = itemView.findViewById(R.id.readIcon);
            listenButton = itemView.findViewById(R.id.listenIcon);
        }
    }
}
