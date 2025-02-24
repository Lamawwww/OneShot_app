package com.example.oneshot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneshot.model.Chapter;
import com.example.oneshot.R;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> chapterList;
    private OnChapterClickListener onChapterClickListener;

    public ChapterAdapter(List<Chapter> chapterList, OnChapterClickListener onChapterClickListener) {
        this.chapterList = chapterList != null ? chapterList : new ArrayList<>();
        this.onChapterClickListener = onChapterClickListener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.textViewChapterName.setText(chapter.getChapter_name());
        holder.itemView.setOnClickListener(v -> onChapterClickListener.onChapterClick(position));
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewChapterName;

        ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChapterName = itemView.findViewById(R.id.textViewChapterName);
        }
    }

    public interface OnChapterClickListener {
        void onChapterClick(int position);
    }
}