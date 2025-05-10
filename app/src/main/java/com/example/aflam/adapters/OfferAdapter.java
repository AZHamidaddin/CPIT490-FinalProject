package com.example.aflam.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aflam.R;
import com.example.aflam.models.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(Offer offer);
    }

    private List<Offer> offers;
    private final OnItemClickListener listener;

    public OfferAdapter(List<Offer> offers, OnItemClickListener listener) {
        this.offers = offers;
        this.listener = listener;
    }

    public void setData(List<Offer> newOffers) {
        this.offers = newOffers;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Offer o = offers.get(position);

        // Log for debugging
        Log.d("OfferAdapter", "Offer[" + position + "] title=" + o.getTitle()
                + " imageUrl=" + o.getImageUrl()
                + " url=" + o.getUrl()
                + " parent=" + o.getParent());

        holder.title.setText(o.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(o.getImageUrl())
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(o));
    }

    @Override
    public int getItemCount() {
        return offers == null ? 0 : offers.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.offer_image);
            title = itemView.findViewById(R.id.offer_title);
        }
    }
}
