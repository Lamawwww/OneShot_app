package com.example.oneshot;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.oneshot.fragments.HomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChapterViewActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ChapterImagesAdapter chapterImagesAdapter;
    private List<String> chapterImages;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_view);

        viewPager = findViewById(R.id.viewPager);

        String mangaName = getIntent().getStringExtra("mangaName");
        int chapterIndex = getIntent().getIntExtra("chapterIndex", -1);
        chapterImages = new ArrayList<>();
        chapterImagesAdapter = new ChapterImagesAdapter(this, chapterImages);
        viewPager.setAdapter(chapterImagesAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Manga");
        fetchChapterImages(mangaName, chapterIndex);

        Button homeButton = findViewById(R.id.ChapterViewHomeBtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChapterViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchChapterImages(String mangaName, int chapterIndex) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot mangaSnapshot : snapshot.getChildren()) {
                    if (mangaSnapshot.child("Name").getValue(String.class).equals(mangaName)) {
                        DataSnapshot chapterSnapshot = mangaSnapshot.child("Chapter").child(String.valueOf(chapterIndex));
                        chapterImages.clear();
                        for (DataSnapshot imageSnapshot : chapterSnapshot.child("Chapter_images").getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            chapterImages.add(imageUrl);
                        }
                        chapterImagesAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}