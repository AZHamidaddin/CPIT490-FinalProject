// app/src/main/java/com/example/aflam/ui/OffersActivity.java
package com.example.aflam.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aflam.R;
import com.example.aflam.adapters.OfferAdapter;
import com.example.aflam.models.Offer;
import com.example.aflam.models.OffersResponse;    // ← make sure this is imported
import com.example.aflam.network.ApiClient;
import com.example.aflam.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {
    private RecyclerView rvOffers;
    private OfferAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        rvOffers = findViewById(R.id.rvOffersPage);
        rvOffers.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new OfferAdapter(new ArrayList<>(), offer -> {
            // When an offer is clicked, open its URL
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(offer.getUrl()));
            startActivity(i);
        });
        rvOffers.setAdapter(adapter);

        fetchOffers();
    }

    private void fetchOffers() {
        ApiService svc = ApiClient.getClient().create(ApiService.class);
        svc.getAllOffers().enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    List<Offer> list = resp.body().getOffers();
                    adapter.setData(list);
                    Log.d("OffersActivity", "Loaded " + list.size() + " offers");
                } else {
                    Log.e("OffersActivity", "HTTP " + resp.code());
                }
            }

            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Log.e("OffersActivity", "Network error", t);
            }
        });
    }
}
