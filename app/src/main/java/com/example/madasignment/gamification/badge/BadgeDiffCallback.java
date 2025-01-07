package com.example.madasignment.gamification.badge;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

public class BadgeDiffCallback extends DiffUtil.Callback {
    private final List<Badge> oldList;
    private final List<Badge> newList;

    public BadgeDiffCallback(List<Badge> oldList, List<Badge> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
