package com.example.madasignment.read;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class ReadOnlyAdapter extends RecyclerView.Adapter<ReadOnlyAdapter.ViewHolder> {

    private final List<ReadOnlyTranscription> transcriptionList;
    private boolean showTranslation;

    public ReadOnlyAdapter(List<ReadOnlyTranscription> transcriptionList, boolean showTranslation) {
        this.transcriptionList = transcriptionList;
        this.showTranslation = showTranslation;
    }

    public void setShowTranslation(boolean showTranslation) {
        this.showTranslation = showTranslation;
        notifyDataSetChanged();
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
        ReadOnlyTranscription transcription = transcriptionList.get(position);
        holder.chineseText.setText(transcription.getChinese());

        if (showTranslation) {
            holder.englishText.setVisibility(View.VISIBLE);
            holder.englishText.setText(transcription.getEnglish());
        } else {
            holder.englishText.setVisibility(View.GONE);
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
