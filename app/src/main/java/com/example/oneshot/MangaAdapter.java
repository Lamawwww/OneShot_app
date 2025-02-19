package com.example.oneshot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneshot.model.Manga;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private Context context;
    private List<Manga> mangaList;
    private OnMangaClickListener onMangaClickListener;

    public MangaAdapter(Context context, List<Manga> mangaList, OnMangaClickListener onMangaClickListener) {
        this.context = context;
        this.mangaList = mangaList;
        this.onMangaClickListener = onMangaClickListener;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view, onMangaClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.textViewName.setText(manga.getName());
        Picasso.get().load(manga.getCover()).into(holder.imageViewCover);
        holder.bind(manga, onMangaClickListener);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewCover;

        public MangaViewHolder(@NonNull View itemView, OnMangaClickListener onMangaClickListener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
        }

        public void bind(final Manga manga, final OnMangaClickListener listener) {
            itemView.setOnClickListener(v -> listener.onMangaClick(manga));
        }
    }

    public interface OnMangaClickListener {
        void onMangaClick(Manga manga);
    }
}