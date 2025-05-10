package com.example.aflam.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aflam.R;
import com.example.aflam.adapters.MovieAdapter;
import com.example.aflam.models.Movie;
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMoviesActivity extends AppCompatActivity {
    private RecyclerView rvAllMovies;
    private MovieAdapter adapter;
    private EditText etSearch;
    private ImageButton btnSearch;

    // Final list for adapter
    private List<Movie> displayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        rvAllMovies = findViewById(R.id.rvAllMovies);
        etSearch    = findViewById(R.id.etSearch);
        btnSearch   = findViewById(R.id.btnSearch);

        rvAllMovies.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter(new ArrayList<>(), m -> {
            Intent i = new Intent(this, MovieDetailActivity.class);
            i.putExtra("movieId", m.getId());
            startActivity(i);
        });
        rvAllMovies.setAdapter(adapter);

        fetchAndGroupMovies();

        btnSearch.setOnClickListener(v -> {
            String q = etSearch.getText().toString().trim().toLowerCase();
            List<Movie> filtered = new ArrayList<>();
            for (Movie m : displayList) {
                if (m.getTitle().toLowerCase().contains(q)) {
                    filtered.add(m);
                }
            }
            adapter.setData(filtered);
        });
    }

    private void fetchAndGroupMovies() {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getAllMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    List<Movie> raw = resp.body();

                    // 1) group
                    List<List<Movie>> groups = groupByNormalizedTitle(raw);

                    // 2) flatten & prioritize
                    displayList = flattenAndPrioritize(groups);

                    // 3) display
                    adapter.setData(displayList);
                    Log.d("AllMoviesActivity", "Unique movies: " + displayList.size());
                } else {
                    Log.e("AllMoviesActivity", "Fetch failed: HTTP " + resp.code());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("AllMoviesActivity", "Network failure", t);
            }
        });
    }

    /**
     * Creates groups via a LinkedHashMap keyed by the normalized title.
     * Ensures each movie only belongs to one group.
     */
    private List<List<Movie>> groupByNormalizedTitle(List<Movie> movies) {
        Map<String, List<Movie>> map = new LinkedHashMap<>();
        for (Movie m : movies) {
            String norm = normalize(m.getTitle());
            String foundKey = null;
            // find an existing key that matches/includes this norm
            for (String key : map.keySet()) {
                if (key.contains(norm) || norm.contains(key)) {
                    foundKey = key;
                    break;
                }
            }
            if (foundKey != null) {
                map.get(foundKey).add(m);
            } else {
                List<Movie> newGroup = new ArrayList<>();
                newGroup.add(m);
                map.put(norm, newGroup);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * From each group pick the first movie, override its imageUrl by priority,
     * and return the flat list.
     */
    private List<Movie> flattenAndPrioritize(List<List<Movie>> groups) {
        List<Movie> flat = new ArrayList<>();
        List<String> priority = Arrays.asList("VOX", "Muvi", "Empire", "AMC");
        for (List<Movie> grp : groups) {
            Movie main = grp.get(0);
            String best = pickBestImageUrl(grp, priority);
            main.setImageUrl(best);
            flat.add(main);
        }
        return flat;
    }

    /** Normalize exactly like your React code */
    private String normalize(String s) {
        return s == null
                ? ""
                : s.toLowerCase()
                .replaceAll("[^a-z0-9]+", "")
                .trim();
    }

    /** Mirror getPrioritizedImageUrl from React */
    private String pickBestImageUrl(List<Movie> group, List<String> priority) {
        for (String src : priority) {
            for (Movie m : group) {
                if (m.getParent() != null
                        && m.getParent().equalsIgnoreCase(src)
                        && m.getImageUrl() != null
                        && !m.getImageUrl().isEmpty()) {
                    return m.getImageUrl();
                }
            }
        }
        // fallback to first one's URL
        return group.get(0).getImageUrl();
    }
}
