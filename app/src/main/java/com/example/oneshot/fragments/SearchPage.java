package com.example.oneshot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneshot.adapter.MangaAdapter;
import com.example.oneshot.R;
import com.example.oneshot.model.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends Fragment {
    private RecyclerView recyclerViewManga;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList;
    private DatabaseReference databaseReference;

    private SearchView searchView;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_search_page, container, false);
        //SEARCH BAR INITIALIZE
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        searchView = view.findViewById(R.id.categoryBar);
        searchView.clearFocus();
        TextView errorText = view.findViewById(R.id.errorText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        recyclerViewManga = view.findViewById(R.id.recyclerViewManga);
        recyclerViewManga.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mangaList = new ArrayList<>();
        mangaAdapter = new MangaAdapter(getContext(), mangaList, this::onMangaClick);
        recyclerViewManga.setAdapter(mangaAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Manga");
        fetchMangas();

        return view;

    }

    private void filterList(String text) {
        TextView errorText = view.findViewById(R.id.errorText);
        List<Manga> filteredList = new ArrayList<>();
        for (Manga manga: mangaList) {
            if (manga.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(manga);
            }
        }

        if (filteredList.isEmpty()) {
            errorText.setText("No search results for " + text);
            errorText.setVisibility(ViewGroup.VISIBLE);
            recyclerViewManga.setVisibility(ViewGroup.INVISIBLE);
        }
        else {
            mangaAdapter.setFilteredList(filteredList);
            recyclerViewManga.setVisibility(ViewGroup.VISIBLE);
            errorText.setVisibility(ViewGroup.INVISIBLE);
        }
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
        transaction.replace(R.id.fragment_container, new ChapterListPage(manga, manga.getIndex()));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


