package com.example.aflam.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aflam.R;
import com.example.aflam.adapters.ShowtimeAdapter;
import com.example.aflam.models.Movie;
import com.example.aflam.models.Timing;
import com.example.aflam.models.Showtime;
import com.example.aflam.models.Experience;
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvDesc, tvGenre, tvLanguage;
    private ImageView ivPoster;
    private RecyclerView rvShowtimes;
    private ShowtimeAdapter showtimeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // 1) Wire views
        ivPoster    = findViewById(R.id.ivPoster);
        tvTitle     = findViewById(R.id.tvTitle);
        tvDesc      = findViewById(R.id.tvDesc);
        tvLanguage  = findViewById(R.id.tvLanguage);
        tvGenre     = findViewById(R.id.tvGenre);
        rvShowtimes = findViewById(R.id.rvShowtimes);

        // 2) Setup showtimes RecyclerView
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        showtimeAdapter = new ShowtimeAdapter(new ArrayList<>());
        rvShowtimes.setAdapter(showtimeAdapter);

        // 3) Fetch movieId from intent
        String movieId = getIntent().getStringExtra("movieId");
        if (movieId != null) {
            fetchMovieDetail(movieId);
        }
    }

    private void fetchMovieDetail(String id) {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getMovieById(id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> resp) {
                if (!resp.isSuccessful() || resp.body() == null) return;
                Movie m = resp.body();

                // Populate text fields
                tvTitle.setText(m.getTitle());
                tvDesc.setText(m.getDescription());
                tvLanguage.setText("Language: " + m.getLanguage());

                List<String> genres = m.getGenre();
                tvGenre.setText("Genre: " + (genres == null ? "" : String.join(", ", genres)));

                // Load poster
                Glide.with(MovieDetailActivity.this)
                        .load(m.getImageUrl())
                        .into(ivPoster);

                // Flatten times:
                List<String> timesList = new ArrayList<>();
                if (m.getTimings() != null) {
                    for (Timing t : m.getTimings()) {
                        if (t.getShowtimes() != null) {
                            for (Showtime s : t.getShowtimes()) {
                                if (s.getExperiences() != null) {
                                    for (Experience e : s.getExperiences()) {
                                        if (e.getTimes() != null) {
                                            timesList.addAll(e.getTimes());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Update the adapter
                showtimeAdapter.setData(timesList);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // TODO: handle failure
            }
        });
    }
}
