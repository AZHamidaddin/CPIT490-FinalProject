// app/src/main/java/com/example/cpit490project/adapters/OfferAdapter.java
package com.example.cpit490project.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cpit490project.R;
import com.example.cpit490project.models.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.VH> {
    private final List<Offer> list;
    private final Context ctx;

    public OfferAdapter(Context ctx, List<Offer> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_offer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Offer o = list.get(i);
        // Load image
        Glide.with(ctx)
                .load(o.imageUrl)
                .into(h.img);

        // Open offer URL on click
        h.itemView.setOnClickListener(v -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(o.url));
            ctx.startActivity(browser);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        VH(View v) {
            super(v);
            img = v.findViewById(R.id.imgOffer);
        }
    }
}
