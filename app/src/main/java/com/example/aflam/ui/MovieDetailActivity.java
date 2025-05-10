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
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    ImageView ivPoster;
    TextView tvTitle, tvDesc, tvLang, tvGenre;
    RecyclerView rvShowtimes;

    @Override protected void onCreate(@Nullable Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_movie_detail);

        ivPoster    = findViewById(R.id.ivPoster);
        tvTitle     = findViewById(R.id.tvTitle);
        tvDesc      = findViewById(R.id.tvDesc);
        tvLang      = findViewById(R.id.tvLang);
        tvGenre     = findViewById(R.id.tvGenre);
        rvShowtimes = findViewById(R.id.rvShowtimes);

        rvShowtimes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        String movieId = getIntent().getStringExtra("movieId");
        fetchDetails(movieId);
    }

    private void fetchDetails(String id) {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getMovieById(id).enqueue(new Callback<Movie>() {
            @Override public void onResponse(Call<Movie> c, Response<Movie> r) {
                if (!r.isSuccessful() || r.body()==null) return;
                Movie m = r.body();
                tvTitle.setText(m.getTitle());
                tvDesc.setText(m.getDescription());
                tvLang.setText("Language: " + m.getLanguage());
                tvGenre.setText("Genre: " + String.join(", ", m.getGenre()));
                Glide.with(MovieDetailActivity.this).load(m.getImageUrl()).into(ivPoster);

                // assume m.getTimings() returns List<String>
                ShowtimeAdapter showAdapter = new ShowtimeAdapter(m.getTimings());
                rvShowtimes.setAdapter(showAdapter);
            }
            @Override public void onFailure(Call<Movie> c, Throwable t) { /* TODO */ }
        });
    }
}
