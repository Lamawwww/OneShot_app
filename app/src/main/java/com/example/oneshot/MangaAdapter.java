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

    public MangaAdapter(Context context, List<Manga> mangaList) {
        this.context = context;
        this.mangaList = mangaList;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.textViewName.setText(manga.getName());
        Picasso.get().load(manga.getCover()).into(holder.imageViewCover);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewCover;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
        }
    }
}
