package com.example.madasignment.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class TestPerformanceAdapter extends RecyclerView.Adapter<TestPerformanceAdapter.PerformanceViewHolder> {

    private Context context;
    private List<TestPerformanceItem> performanceItems; // A list to hold performance data

    public TestPerformanceAdapter(Context context, List<TestPerformanceItem> performanceItems) {
        this.context = context;
        this.performanceItems = performanceItems;
    }

    @NonNull
    @Override
    public PerformanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_item_performance_breakup, parent, false);
        return new PerformanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerformanceViewHolder holder, int position) {
        TestPerformanceItem item = performanceItems.get(position);

        // Set data in the view
        holder.questionText.setText(item.getQuestionText());
        holder.userAnswer.setText("Your Answer: " + item.getUserAnswer());
        holder.correctAnswer.setText("Correct Answer: " + item.getCorrectAnswer());

        // Set the result icon
        if (item.isCorrect()) {
            holder.resultIcon.setImageResource(R.drawable.quiz_correct); // Replace with your correct icon
        } else {
            holder.resultIcon.setImageResource(R.drawable.quiz_wrong); // Replace with your incorrect icon
        }
    }

    @Override
    public int getItemCount() {
        return performanceItems.size();
    }

    static class PerformanceViewHolder extends RecyclerView.ViewHolder {

        ImageView resultIcon;
        TextView questionText, userAnswer, correctAnswer;

        public PerformanceViewHolder(@NonNull View itemView) {
            super(itemView);

            resultIcon = itemView.findViewById(R.id.resultIcon);
            questionText = itemView.findViewById(R.id.questionText);
            userAnswer = itemView.findViewById(R.id.userAnswer);
            correctAnswer = itemView.findViewById(R.id.correctAnswer);
        }
    }
}
