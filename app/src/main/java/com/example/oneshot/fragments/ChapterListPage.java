package com.example.oneshot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneshot.ChapterAdapter;
import com.example.oneshot.ChapterViewActivity;
import com.example.oneshot.R;
import com.example.oneshot.model.Chapter;
import com.example.oneshot.model.Manga;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ChapterListPage extends Fragment {
    private TextView textViewMangaName, textViewMangaDescription;
    private ImageView imageViewMangaCover;
    private RecyclerView recyclerViewChapters;
    private ChapterAdapter chapterAdapter;
    private List<Chapter> chapterList;
    private Manga manga;

    public ChapterListPage(Manga manga) {
        this.manga = manga;
        this.chapterList = manga.getChapter();
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_chapter_list_page, container, false);

        textViewMangaName = view.findViewById(R.id.textViewMangaName);
        textViewMangaDescription = view.findViewById(R.id.textViewMangaDescription);
        imageViewMangaCover = view.findViewById(R.id.imageViewMangaCover);
        recyclerViewChapters = view.findViewById(R.id.recyclerViewChapters);

        textViewMangaName.setText(manga.getName());
        textViewMangaDescription.setText(manga.getDescription());
        Picasso.get().load(manga.getCover()).into(imageViewMangaCover);

        //test pag null
        if (chapterList == null) {
            chapterList = new ArrayList<>();
        }

        chapterAdapter = new ChapterAdapter(chapterList, new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(int position) {
                Intent intent = new Intent(getActivity(), ChapterViewActivity.class);
                intent.putExtra("mangaName", manga.getName());
                intent.putExtra("chapterIndex", position);
                startActivity(intent);
            }
        });
        recyclerViewChapters.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChapters.setAdapter(chapterAdapter);

        return view;
    }
}