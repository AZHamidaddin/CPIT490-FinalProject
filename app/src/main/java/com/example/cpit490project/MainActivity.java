// app/src/main/java/com/example/cpit490project/MainActivity.java
package com.example.cpit490project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cpit490project.adapters.OfferAdapter;
import com.example.cpit490project.models.Offer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvOffers;
    private OfferAdapter offerAdapter;
    private List<Offer> offers = new ArrayList<>();
    private RequestQueue queue;
    private final String BASE = "http://10.0.2.2:5000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btnVox).setOnClickListener(v -> openMovies("vox"));
        findViewById(R.id.btnAMC).setOnClickListener(v -> openMovies("amc"));
        findViewById(R.id.btnMuvi).setOnClickListener(v -> openMovies("muvi"));
        findViewById(R.id.btnEmpire).setOnClickListener(v -> openMovies("empire"));

        rvOffers = findViewById(R.id.rvOffers);
        rvOffers.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        offerAdapter = new OfferAdapter(this, offers);
        rvOffers.setAdapter(offerAdapter);

        fetchOffers();
    }

    private void fetchOffers() {
        String url = BASE + "/offers";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, null,
                resp -> {
                    try {
                        JSONArray arr = resp.getJSONArray("offers");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            String img   = o.getString("offer_image");
                            String title = o.getString("offer title");
                            String link  = o.getString("offer URL");
                            offers.add(new Offer(img, title, link));
                        }
                        offerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> err.printStackTrace()
        );
        queue.add(req);
    }

    private void openMovies(String cinema) {
        Intent i = new Intent(this, MovieListActivity.class);
        i.putExtra("cinema", cinema);
        startActivity(i);
    }
}
