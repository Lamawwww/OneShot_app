package com.example.oneshot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneshot.LoginActivity;
import com.example.oneshot.MainActivity;
import com.example.oneshot.R;
import com.example.oneshot.adapter.MangaBookmarkAdapter;
import com.example.oneshot.model.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MyListPage extends Fragment {

    private RecyclerView recyclerViewFavorites;
    private MangaBookmarkAdapter mangaAdapter;
    private List<Manga> favoriteMangaList;
    private FirebaseAuth firebaseAuth;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_my_list_page, container, false);

        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteMangaList = new ArrayList<>();
        mangaAdapter = new MangaBookmarkAdapter(getContext(), favoriteMangaList, this::onMangaClick);
        recyclerViewFavorites.setAdapter(mangaAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        loadFavoriteManga();

        return view;
    }

    private void loadFavoriteManga() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to see your favorites.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Favorites");

        userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteMangaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String mangaUID = dataSnapshot.getKey();
                    if (mangaUID != null) {
                        loadMangaDetails(mangaUID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMangaDetails(String mangaUID) {
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("Manga").child(mangaUID);
        mangaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manga manga = snapshot.getValue(Manga.class);
                if (manga != null) {
                    manga.setUid(mangaUID); // Set the UID for the Manga object
                    favoriteMangaList.add(manga);
                    mangaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load manga details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onMangaClick(Manga manga) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ChapterListPage(manga, manga.getUid()));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}