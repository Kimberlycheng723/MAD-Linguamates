package com.example.madasignment.gamification;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BadgeItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public BadgeItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.bottom = spacing;

        // Add top margin only for the first row
        if (parent.getChildAdapterPosition(view) < ((GridLayoutManager) parent.getLayoutManager()).getSpanCount()) {
            outRect.top = spacing;
        }
    }
}
