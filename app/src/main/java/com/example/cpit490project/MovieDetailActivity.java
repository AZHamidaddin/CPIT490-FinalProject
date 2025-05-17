// app/src/main/java/com/example/cpit490project/MovieDetailActivity.java
package com.example.cpit490project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpit490project.adapters.ShowtimeAdapter;
import com.example.cpit490project.models.Showtime;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MovieDetailActivity extends AppCompatActivity {
    private ChipGroup chipGroupCities, chipGroupDates;
    private RecyclerView rvShowtimes;
    private JSONArray timingsJson;
    private List<Showtime> allShows = new ArrayList<>();
    private ShowtimeAdapter showAdapter;

    private String rawShowtimesUrl;  // full or partial URL from JSON
    private String parentChain;      // "amc", "muvi", "vox", or "empire"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_movie_detail);

            // Read extras
            rawShowtimesUrl = getIntent().getStringExtra("movie_showtimes_url");
            parentChain     = getIntent().getStringExtra("movie_parent");

            // Bind views
            chipGroupCities = findViewById(R.id.chipGroupCities);
            chipGroupDates  = findViewById(R.id.chipGroupDates);
            rvShowtimes     = findViewById(R.id.rvShowtimes);

            // Recycler setup
            rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
            showAdapter = new ShowtimeAdapter(
                    this,
                    allShows,
                    parentChain,
                    rawShowtimesUrl
            );
            rvShowtimes.setAdapter(showAdapter);

            // Parse timings JSON
            String raw = getIntent().getStringExtra("movie_timings");
            timingsJson = new JSONArray(raw);

            // Build the UI
            setupCityChips();

        } catch (Exception e) {
            Log.e("MovieDetailActivity", "Error in onCreate", e);
            Toast.makeText(this,
                    "Detail init error: " + e.getClass().getSimpleName() +
                            " – " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
    }

    private void setupCityChips() {
        Set<String> cities = new LinkedHashSet<>();
        for (int i = 0; i < timingsJson.length(); i++) {
            JSONObject dateBlock = timingsJson.optJSONObject(i);
            JSONArray shows = dateBlock.optJSONArray("Showtimes");
            for (int j = 0; j < shows.length(); j++) {
                JSONObject s = shows.optJSONObject(j);
                cities.add(s.optString("City"));
            }
        }
        for (String city : cities) {
            Chip c = new Chip(this);
            c.setText(city);
            c.setCheckable(true);
            chipGroupCities.addView(c);
        }

        // pick first by default
        if (chipGroupCities.getChildCount() > 0) {
            Chip first = (Chip) chipGroupCities.getChildAt(0);
            first.setChecked(true);
            updateDateChips(first.getText().toString());
        }

        chipGroupCities.setOnCheckedChangeListener((group, checkedId) -> {
            Chip ch = findViewById(checkedId);
            if (ch != null) {
                updateDateChips(ch.getText().toString());
            }
        });
    }

    private void updateDateChips(String city) {
        chipGroupDates.removeAllViews();

        Set<String> dates = new LinkedHashSet<>();
        for (int i = 0; i < timingsJson.length(); i++) {
            JSONObject dateBlock = timingsJson.optJSONObject(i);
            JSONArray shows = dateBlock.optJSONArray("Showtimes");
            for (int j = 0; j < shows.length(); j++) {
                if (shows.optJSONObject(j).optString("City").equals(city)) {
                    dates.add(dateBlock.optString("Date"));
                    break;
                }
            }
        }
        for (String date : dates) {
            Chip c = new Chip(this);
            c.setText(date);
            c.setCheckable(true);
            chipGroupDates.addView(c);
        }

        if (chipGroupDates.getChildCount() > 0) {
            Chip firstDate = (Chip) chipGroupDates.getChildAt(0);
            firstDate.setChecked(true);
            loadShowtimes(city, firstDate.getText().toString());
        }

        chipGroupDates.setOnCheckedChangeListener((group, checkedId) -> {
            Chip ch = findViewById(checkedId);
            if (ch != null) {
                loadShowtimes(city, ch.getText().toString());
            }
        });
    }

    private void loadShowtimes(String city, String date) {
        allShows.clear();
        try {
            for (int i = 0; i < timingsJson.length(); i++) {
                JSONObject dateBlock = timingsJson.optJSONObject(i);
                if (!dateBlock.optString("Date").equals(date)) continue;

                JSONArray shows = dateBlock.optJSONArray("Showtimes");
                for (int j = 0; j < shows.length(); j++) {
                    JSONObject s = shows.optJSONObject(j);
                    if (!s.optString("City").equals(city)) continue;

                    String place = s.optString("Place");
                    JSONArray exps = s.optJSONArray("Experiences");
                    for (int k = 0; k < exps.length(); k++) {
                        JSONObject e = exps.optJSONObject(k);
                        String expName = e.optString("Experience");
                        JSONArray timesArr = e.optJSONArray("Times");

                        List<String> times = new ArrayList<>();
                        for (int t = 0; t < timesArr.length(); t++) {
                            times.add(timesArr.optString(t));
                        }
                        allShows.add(new Showtime(place, expName, times));
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("MovieDetailActivity", "Error parsing showtimes", ex);
        }
        showAdapter.notifyDataSetChanged();
    }
}
