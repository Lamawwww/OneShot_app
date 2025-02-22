package com.example.oneshot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.oneshot.model.Manga;
import com.example.oneshot.MangaAdapter;
import com.example.oneshot.R;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {

    private RecyclerView recyclerViewManga;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList;
    private DatabaseReference databaseReference;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerViewManga = view.findViewById(R.id.recyclerViewManga);
        recyclerViewManga.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mangaList = new ArrayList<>();
        mangaAdapter = new MangaAdapter(getContext(), mangaList, this::onMangaClick);
        recyclerViewManga.setAdapter(mangaAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Manga");
        fetchMangas();

        return view;
    }

    private void fetchMangas() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Manga manga = dataSnapshot.getValue(Manga.class);
                    mangaList.add(manga);
                }
                mangaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void onMangaClick(Manga manga) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ChapterListPage(manga));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}