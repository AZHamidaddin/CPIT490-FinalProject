// app/src/main/java/com/example/cpit490project/MovieListActivity.java
package com.example.cpit490project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cpit490project.adapters.MovieAdapter;
import com.example.cpit490project.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {
    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private List<Movie> movies = new ArrayList<>();
    private RequestQueue queue;
    private final String BASE = "http://10.0.2.2:5000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        queue = Volley.newRequestQueue(this);
        String cinema = getIntent().getStringExtra("cinema");

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));

        // Pass the cinema chain into the adapter so it can forward it to the detail screen
        adapter = new MovieAdapter(this, movies, cinema);
        rvMovies.setAdapter(adapter);

        fetchMovies(cinema);
    }

    private void fetchMovies(String cinema) {
        String url = BASE + "/movies/parent/" + cinema;
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, null,
                resp -> {
                    try {
                        JSONArray arr = resp.getJSONArray("movies");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            String title        = o.getString("Title");
                            String img          = o.getString("Image URL");
                            String lang         = o.getString("Language");
                            String showtimesUrl = o.getString("Showtimes URL"); // NEW
                            JSONArray tims      = o.getJSONArray("Timings");
                            movies.add(new Movie(
                                    title, img, lang, showtimesUrl, tims
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> err.printStackTrace()
        );
        queue.add(req);
    }
}
