package com.example.madasignment.lessons.Module.video;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class videoTranscriptionAdapter extends RecyclerView.Adapter<videoTranscriptionAdapter.ViewHolder> {

    private final List<videoTranscription> transcriptionList;
    private int activeIndex = -1;

    public videoTranscriptionAdapter(List<videoTranscription> transcriptionList) {
        this.transcriptionList = transcriptionList;
    }

    public void setActiveIndex(int index) {
        if (index != activeIndex) { // Only refresh if the active index changes
            activeIndex = index;
            notifyDataSetChanged(); // Notify RecyclerView to refresh
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transcription_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        videoTranscription transcription = transcriptionList.get(position);
        holder.chineseText.setText(transcription.chinese);
        holder.englishText.setText(transcription.english);

        // Highlight the active transcription
        if (position == activeIndex) {
            holder.chineseText.setTextColor(Color.BLUE); // Active Chinese text
            holder.englishText.setTextColor(Color.BLUE); // Active English text
            holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD")); // Light blue background
        } else {
            holder.chineseText.setTextColor(Color.BLACK); // Default Chinese text color
            holder.englishText.setTextColor(Color.DKGRAY); // Default English text color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); // Default background
        }
    }


    @Override
    public int getItemCount() {
        return transcriptionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chineseText;
        TextView englishText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chineseText = itemView.findViewById(R.id.chinese_text);
            englishText = itemView.findViewById(R.id.english_text);
        }


    }


}
