package com.example.madasignment.home.lesson_unit.lesson_unit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.levels.LevelsActivity;

import java.util.List;

public class LessonUnitsAdapter extends RecyclerView.Adapter<LessonUnitsAdapter.UnitViewHolder> {

    private List<LessonUnitData> unitList;

    public LessonUnitsAdapter(List<LessonUnitData> unitList) {
        this.unitList = unitList;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_unit_item_card, parent, false);
        return new UnitViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        LessonUnitData unit = unitList.get(position);

        // Set data for each unit
        holder.unitTitle.setText(unit.getTitle());
        holder.unitProgressBar.setProgress(unit.getProgress());
        holder.progressText.setText(unit.getProgress() + "%");
        holder.unitImage.setImageResource(unit.getImageResId());
        holder.unitActionButton.setText(unit.getAction());


        // Dynamically set card background color while retaining rounded corners
        GradientDrawable backgroundDrawable = (GradientDrawable) holder.itemView.getBackground();
        backgroundDrawable.setColor(Color.parseColor(unit.getBackgroundColor())); // Set color dynamically


        // Dynamically set button color
        holder.unitActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(unit.getButtonColor())));

        // Handle button click
        holder.unitActionButton.setOnClickListener(view -> {
            if (unit.getTitle().equals("Common Greetings and Phrases") && unit.getAction().equalsIgnoreCase("Replay")) {
                Intent intent = new Intent(view.getContext(), LevelsActivity.class);
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), unit.getAction() + " " + unit.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return unitList.size();
    }

    static class UnitViewHolder extends RecyclerView.ViewHolder {
        TextView unitTitle;
        ProgressBar unitProgressBar;
        TextView progressText; // Added progress text
        Button unitActionButton;
        ImageView unitImage;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            unitTitle = itemView.findViewById(R.id.unitTitle);
            unitProgressBar = itemView.findViewById(R.id.unitProgressBar);
            progressText = itemView.findViewById(R.id.progressText); // Bind progress text
            unitActionButton = itemView.findViewById(R.id.unitActionButton);
            unitImage = itemView.findViewById(R.id.unitImage);
        }
    }

}
