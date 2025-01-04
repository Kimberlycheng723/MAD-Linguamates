package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class LessonMatchingAdapter extends RecyclerView.Adapter<LessonMatchingAdapter.ViewHolder> {

private final List<String> leftOptions;
private final List<String> rightOptions;
private final List<String> userMatches;

public LessonMatchingAdapter(List<String> leftOptions, List<String> rightOptions, List<String> userMatches) {
    this.leftOptions = leftOptions;
    this.rightOptions = rightOptions;
    this.userMatches = userMatches;
}

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_matching_item_matching_pair, parent, false);
    return new ViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String leftOption = leftOptions.get(position / 2); // Map positions for left and right options
    String rightOption = rightOptions.get(position % 2);

    if (position % 2 == 0) {
        // Left options
        holder.optionText.setText(leftOption);
    } else {
        // Right options
        holder.optionText.setText(rightOption);
    }

    holder.itemView.setOnClickListener(v -> {
        // Handle drag/drop matching here (or future implementation)
    });
}

@Override
public int getItemCount() {
    return leftOptions.size() * 2; // For left and right
}

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView optionText;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        optionText = itemView.findViewById(R.id.optionText);
    }
}
}
