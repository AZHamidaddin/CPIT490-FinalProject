package com.example.aflam.network;

import com.example.aflam.models.Movie;
import com.example.aflam.models.Offer;
import com.example.aflam.models.OffersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    // Fetch all movies
    @GET("movies")
    Call<List<Movie>> getAllMovies();

    // Fetch a single movie by its Mongo _id
    @GET("movies/{id}")
    Call<Movie> getMovieById(@Path("id") String id);

    // Fetch offers, wrapped in { count, offers: [...] }
    @GET("offers")
    Call<OffersResponse> getAllOffers();
}
