package com.example.aflam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.aflam.R;
import com.example.aflam.models.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.VH> {
    private List<Movie> movies;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Movie m);
    }

    public MovieAdapter(List<Movie> movies, OnItemClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    public void setData(List<Movie> data) {
        this.movies = data;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Movie m = movies.get(i);
        h.title.setText(m.getTitle());
        h.lang.setText(m.getLanguage());
        Glide.with(h.itemView).load(m.getImageUrl()).into(h.image);
        h.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override public int getItemCount() { return movies==null?0:movies.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, lang;
        VH(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.movie_image);
            title = v.findViewById(R.id.movie_title);
            lang  = v.findViewById(R.id.movie_lang);
        }
    }
}
