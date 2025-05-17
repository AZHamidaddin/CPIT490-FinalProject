// app/src/main/java/com/example/cpit490project/adapters/MovieAdapter.java
package com.example.cpit490project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cpit490project.MovieDetailActivity;
import com.example.cpit490project.R;
import com.example.cpit490project.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.VH> {
    private final List<Movie> list;
    private final Context ctx;
    private final String parentChain;     // NEW

    public MovieAdapter(Context ctx, List<Movie> list, String parentChain) {
        this.ctx         = ctx;
        this.list        = list;
        this.parentChain = parentChain;   // e.g. "amc", "muvi", "vox", "empire"
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_movie, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Movie m = list.get(i);
        h.tvTitle.setText(m.title);
        h.tvLang.setText(m.language);
        Glide.with(ctx).load(m.imageUrl).into(h.imgPoster);

        // OPEN DETAIL, passing both the raw showtimes URL and the chain
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, MovieDetailActivity.class);
            intent.putExtra("movie_title",       m.title);
            intent.putExtra("movie_timings",     m.timings.toString());
            intent.putExtra("movie_showtimes_url", m.showtimesUrl);
            intent.putExtra("movie_parent",      parentChain);
            ctx.startActivity(intent);
        });
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView  tvTitle, tvLang;
        VH(View v) {
            super(v);
            imgPoster = v.findViewById(R.id.imgPoster);
            tvTitle   = v.findViewById(R.id.tvTitle);
            tvLang    = v.findViewById(R.id.tvLang);
        }
    }
}
