package com.example.aflam.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aflam.R;
import com.example.aflam.adapters.MovieAdapter;
import com.example.aflam.adapters.OfferAdapter;
import com.example.aflam.models.Movie;
import com.example.aflam.models.Offer;
import com.example.aflam.models.OffersResponse;
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvMovies, rvOffers;
    private MovieAdapter movieAdapter;
    private OfferAdapter offerAdapter;
    private FloatingActionButton fabRecommend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvMovies     = findViewById(R.id.rvMovies);
        rvOffers     = findViewById(R.id.rvOffers);
        fabRecommend = findViewById(R.id.fabRecommend);

        // 1) Movies grid: 2-column vertical scroll
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> {
            Intent i = new Intent(this, MovieDetailActivity.class);
            i.putExtra("movieId", movie.getId());
            startActivity(i);
        });
        rvMovies.setAdapter(movieAdapter);

        // 2) Offers slider: horizontal LinearLayoutManager
        rvOffers.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        );
        offerAdapter = new OfferAdapter(new ArrayList<>(), offer -> {
            // Open offer URL in browser
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(offer.getUrl()));
            startActivity(i);
        });
        rvOffers.setAdapter(offerAdapter);

        // 3) Optional FAB action
        fabRecommend.setOnClickListener(v -> {
            // TODO: your recommendation logic
        });

        // Fetch data
        fetchMovies();
        fetchOffers();
    }

    private void fetchMovies() {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getAllMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    movieAdapter.setData(resp.body());
                } else {
                    Log.e("HomeActivity", "Movies HTTP " + resp.code());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("HomeActivity", "Movies network failure", t);
            }
        });
    }

    private void fetchOffers() {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getAllOffers().enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    List<Offer> list = resp.body().getOffers();
                    offerAdapter.setData(list);
                    Log.d("HomeActivity", "Loaded " + list.size() + " offers");
                } else {
                    Log.e("HomeActivity", "Offers HTTP " + resp.code());
                }
            }
            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Log.e("HomeActivity", "Offers network failure", t);
            }
        });
    }
}
