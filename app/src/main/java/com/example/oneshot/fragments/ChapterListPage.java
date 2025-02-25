package com.example.oneshot.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneshot.adapter.ChapterAdapter;
import com.example.oneshot.ChapterViewActivity;
import com.example.oneshot.R;
import com.example.oneshot.model.Chapter;
import com.example.oneshot.model.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Button toggleFavoriteButton;
    private FirebaseAuth firebaseAuth;
    private String mangaUID;

    // Ensure mangaUID is passed correctly in the constructor
    public ChapterListPage(Manga manga, String mangaUID) {
        this.manga = manga;
        this.chapterList = manga.getChapter();
        this.mangaUID = mangaUID;
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
        toggleFavoriteButton = view.findViewById(R.id.toggle_bookmark_button);
        firebaseAuth = FirebaseAuth.getInstance();

        textViewMangaName.setText(manga.getName());
        textViewMangaDescription.setText(manga.getDescription());
        Picasso.get().load(manga.getCover()).into(imageViewMangaCover);

        if (chapterList == null) {
            chapterList = new ArrayList<>();
        }

        chapterAdapter = new ChapterAdapter(chapterList, position -> {
            Intent intent = new Intent(getActivity(), ChapterViewActivity.class);
            intent.putExtra("mangaName", manga.getName());
            intent.putExtra("chapterIndex", position);
            startActivity(intent);
        });
        recyclerViewChapters.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChapters.setAdapter(chapterAdapter);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(currentUser.getUid()).child("Favorites").child(mangaUID);
            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        updateFavoriteButton(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        toggleFavoriteButton.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(getContext(), "Please log in to manage your favorites.", Toast.LENGTH_SHORT).show();
            } else {
                if (mangaUID != null) {
                    toggleFavoriteStatus(mangaUID);
                } else {
                    Toast.makeText(getContext(), "Manga UID is null.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void toggleFavoriteStatus(String mangaUID) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference favoriteRef = userRef.child(firebaseAuth.getUid()).child("Favorites").child(mangaUID);

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    favoriteRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                            updateFavoriteButton(false);
                        }
                    });
                } else {
                    favoriteRef.setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                            updateFavoriteButton(true);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            toggleFavoriteButton.setText("Added To List");
            toggleFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bookmark_added_24px, 0);
            toggleFavoriteButton.setBackgroundColor(Color.parseColor("#6475F7"));
        } else {
            toggleFavoriteButton.setText("Add to List");
            toggleFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bookmark_24px, 0);
            toggleFavoriteButton.setBackgroundColor(Color.parseColor("#374151"));
        }
    }
}