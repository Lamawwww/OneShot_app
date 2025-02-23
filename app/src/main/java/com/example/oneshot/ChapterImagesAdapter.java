package com.example.oneshot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChapterImagesAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls;

    public ChapterImagesAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter_image, container, false);
        PhotoView photoView = view.findViewById(R.id.photoViewChapterImage);
        TextView pageNumberTextView = view.findViewById(R.id.pageNumberTextView);

        // Load the image using Picasso
        Picasso.get()
                .load(imageUrls.get(position))
                .into(photoView);

        // Set the page number text
        pageNumberTextView.setText("Page " + (position + 1) + " of " + getCount());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}