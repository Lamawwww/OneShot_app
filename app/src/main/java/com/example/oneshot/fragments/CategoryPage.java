package com.example.oneshot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneshot.MangaAdapter;
import com.example.oneshot.R;
import com.example.oneshot.model.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryPage extends Fragment {

    private RecyclerView recyclerViewManga;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList;
    private DatabaseReference databaseReference;

    private SearchView categoryView;

    View view1;
    Button actionButton = view1.findViewById(R.id.actionButton);
    Button romanceButton = view1.findViewById(R.id.romanceButton);
    Button comedyButton = view1.findViewById(R.id.comedyButton);
    Button dramaButton = view1.findViewById(R.id.dramaButton);
    TextView categoryTitle = view1.findViewById(R.id.categoryTitle);

    String setSearchText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view1 = inflater.inflate(R.layout.fragment_category_page, container, false);
        //SEARCH BAR INITIALIZE
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        categoryView = view1.findViewById(R.id.categoryBar);
        categoryView.clearFocus();
        categoryView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        recyclerViewManga = view1.findViewById(R.id.recyclerViewManga);
        recyclerViewManga.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mangaList = new ArrayList<>();
        mangaAdapter = new MangaAdapter(getContext(), mangaList, this::onMangaClick);
        recyclerViewManga.setAdapter(mangaAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Manga");
        fetchMangas();


        //BUTTON INITIALIZATIONS
        Button actionButton = view1.findViewById(R.id.actionButton);
        Button romanceButton = view1.findViewById(R.id.romanceButton);
        Button comedyButton = view1.findViewById(R.id.comedyButton);
        Button dramaButton = view1.findViewById(R.id.dramaButton);
        TextView categoryTitle = view1.findViewById(R.id.categoryTitle);

        //ACTION BUTTON FUNCTIONS
        actionButton.setOnClickListener(view -> {
            setSearchText += "Action";
            categoryView.setQuery(setSearchText, false);
            categoryView.clearFocus();
            recyclerViewManga.setVisibility(ViewGroup.VISIBLE);
            romanceButton.setVisibility(ViewGroup.GONE);
            actionButton.setVisibility(ViewGroup.GONE);
            comedyButton.setVisibility(ViewGroup.GONE);
            dramaButton.setVisibility(ViewGroup.GONE);
            categoryTitle.setText(setSearchText);
            categoryTitle.setVisibility(ViewGroup.VISIBLE);

        });

        //ROMANCE BUTTON FUNCTIONS
        romanceButton.setOnClickListener(view -> {
            String setSearchText = "";
            setSearchText += "Romance";
            categoryView.setQuery(setSearchText, false);
            categoryView.clearFocus();
            recyclerViewManga.setVisibility(ViewGroup.VISIBLE);
            romanceButton.setVisibility(ViewGroup.GONE);
            actionButton.setVisibility(ViewGroup.GONE);
            comedyButton.setVisibility(ViewGroup.GONE);
            dramaButton.setVisibility(ViewGroup.GONE);
            categoryTitle.setText(setSearchText);
            categoryTitle.setVisibility(ViewGroup.VISIBLE);
        });

        //COMEDY BUTTON FUNCTIONS
        comedyButton.setOnClickListener(view -> {
            String setSearchText = "";
            setSearchText += "Comedy";
            categoryView.setQuery(setSearchText, false);
            categoryView.clearFocus();
            recyclerViewManga.setVisibility(ViewGroup.VISIBLE);
            romanceButton.setVisibility(ViewGroup.GONE);
            actionButton.setVisibility(ViewGroup.GONE);
            comedyButton.setVisibility(ViewGroup.GONE);
            dramaButton.setVisibility(ViewGroup.GONE);
            categoryTitle.setText(setSearchText);
            categoryTitle.setVisibility(ViewGroup.VISIBLE);
        });

        //DRAMA BUTTON FUNCTIONS
        dramaButton.setOnClickListener(view -> {
            String setSearchText = "";
            setSearchText += "Drama";
            categoryView.setQuery(setSearchText, false);
            categoryView.clearFocus();
            recyclerViewManga.setVisibility(ViewGroup.VISIBLE);
            removeButtons();
        });

        return view1;


    }

    private void removeButtons() {
        romanceButton.setVisibility(ViewGroup.GONE);
        actionButton.setVisibility(ViewGroup.GONE);
        comedyButton.setVisibility(ViewGroup.GONE);
        dramaButton.setVisibility(ViewGroup.GONE);
        categoryTitle.setText(setSearchText);
        categoryTitle.setVisibility(ViewGroup.VISIBLE);
    }


    private void filterList(String text) {
        List<Manga> filteredList = new ArrayList<>();
        for (Manga manga: mangaList) {
            if (manga.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(manga);
            }
        }

        if (filteredList.isEmpty()) {

        }
        else {
            mangaAdapter.setFilteredList(filteredList);
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
        transaction.replace(R.id.fragment_container, new ChapterListPage(manga));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}