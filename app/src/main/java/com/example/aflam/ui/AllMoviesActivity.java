package com.example.aflam.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aflam.R;
import com.example.aflam.adapters.MovieAdapter;
import com.example.aflam.models.Movie;
import com.example.aflam.models.Timing;
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMoviesActivity extends AppCompatActivity {
    private RecyclerView rvAllMovies;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        rvAllMovies = findViewById(R.id.rvAllMovies);
        rvAllMovies.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MovieAdapter(new ArrayList<>(), movie -> {
            Intent i = new Intent(this, MovieDetailActivity.class);
            i.putExtra("movieId", movie.getId());
            startActivity(i);
        });
        rvAllMovies.setAdapter(adapter);

        fetchAndMergeMovies();
    }

    private void fetchAndMergeMovies() {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getAllMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> resp) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    Log.e("AllMovies", "Empty or error response: " + resp.code());
                    return;
                }

                List<Movie> raw = resp.body();

                // 1) Group similar titles
                List<List<Movie>> groups = mergeMoviesByTitle(raw);

                // 2) Flatten each group (merge showtimes + pick best image)
                List<Movie> flat = flattenGroups(groups);

                // 3) Feed to adapter
                adapter.setData(flat);
                Log.d("AllMovies", "Displaying " + flat.size() + " movies");
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("AllMovies", "Network failure", t);
            }
        });
    }

    // ————————————————
    // 1) mergeMoviesByTitle (exactly your React logic) :contentReference[oaicite:0]{index=0}:contentReference[oaicite:1]{index=1}
    private List<List<Movie>> mergeMoviesByTitle(List<Movie> movies) {
        List<List<Movie>> groups = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (int i = 0; i < movies.size(); i++) {
            if (visited.contains(i)) continue;
            Movie mi = movies.get(i);
            String normA = normalize(mi.getTitle());
            List<Movie> group = new ArrayList<>();
            group.add(mi);
            visited.add(i);

            for (int j = i + 1; j < movies.size(); j++) {
                if (visited.contains(j)) continue;
                Movie mj = movies.get(j);
                String normB = normalize(mj.getTitle());
                if (normA.contains(normB) || normB.contains(normA)) {
                    group.add(mj);
                    visited.add(j);
                }
            }
            groups.add(group);
        }

        Collections.sort(groups, (a, b) ->
                a.get(0).getTitle()
                        .compareToIgnoreCase(b.get(0).getTitle())
        );
        return groups;
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]+", "")
                .trim();
    }

    // ————————————————
    // 2) flattenGroups: merge timings & pick best poster
    private List<Movie> flattenGroups(List<List<Movie>> groups) {
        List<Movie> flat = new ArrayList<>();
        List<String> priority = List.of("VOX", "Muvi", "Empire", "AMC");

        for (List<Movie> grp : groups) {
            Movie main = grp.get(0);

            // pick best image
            String bestImg = getBestImage(grp, priority);
            main.setImageUrl(bestImg);

            // merge all showtimes
            Set<Timing> merged = new HashSet<>();
            for (Movie m : grp) {
                if (m.getTimings() != null) {
                    merged.addAll(m.getTimings());
                }
            }
            main.setTimings(new ArrayList<>(merged));

            flat.add(main);
        }
        return flat;
    }

    private String getBestImage(List<Movie> grp, List<String> priority) {
        for (String p : priority) {
            for (Movie m : grp) {
                if (m.getParent() != null
                        && m.getParent().equalsIgnoreCase(p)
                        && m.getImageUrl() != null) {
                    return m.getImageUrl();
                }
            }
        }
        return grp.get(0).getImageUrl();
    }
}
