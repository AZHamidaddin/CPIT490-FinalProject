// app/src/main/java/com/example/cpit490project/adapters/ShowtimeAdapter.java
package com.example.cpit490project.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cpit490project.R;
import com.example.cpit490project.models.Showtime;

import java.util.List;

public class ShowtimeAdapter
        extends RecyclerView.Adapter<ShowtimeAdapter.VH> {

    private final List<Showtime> list;
    private final Context ctx;
    private final String parentChain;
    private final String rawShowtimesUrl;

    public ShowtimeAdapter(Context ctx,
                           List<Showtime> list,
                           String parentChain,
                           String rawShowtimesUrl) {
        this.ctx            = ctx;
        this.list           = list;
        this.parentChain    = parentChain.toLowerCase();
        this.rawShowtimesUrl= rawShowtimesUrl;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_showtime, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Showtime s = list.get(i);
        h.tvPlace.setText(s.place);
        h.tvExp.setText(s.experience);

        h.llTimes.removeAllViews();
        for (String t : s.times) {
            Button b = new Button(ctx);
            b.setText(t);
            b.setAllCaps(false);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            p.setMargins(8,0,8,0);
            b.setLayoutParams(p);

            // ON CLICK: build and launch the correct URL
            b.setOnClickListener(v -> {
                String base;
                switch (parentChain) {
                    case "amc":   base = "https://www.amccinemas.com"; break;
                    case "muvi":  base = "https://www.muvicinemas.com"; break;
                    case "vox":   base = "https://ksa.voxcinemas.com"; break;
                    default:      base = ""; break;
                }
                String full = rawShowtimesUrl.startsWith("http")
                        ? rawShowtimesUrl
                        : base + rawShowtimesUrl;

                Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(full));
                ctx.startActivity(web);
            });

            h.llTimes.addView(b);
        }
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView     tvPlace, tvExp;
        LinearLayout llTimes;
        VH(View v) {
            super(v);
            tvPlace = v.findViewById(R.id.tvPlace);
            tvExp   = v.findViewById(R.id.tvExp);
            llTimes = v.findViewById(R.id.llTimes);
        }
    }
}
