package com.example.aflam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aflam.R;          // ← add this
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.VH> {
    private final List<String> showtimes;

    public ShowtimeAdapter(List<String> showtimes) {
        this.showtimes = showtimes;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_showtime, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvTime.setText(showtimes.get(position));
    }

    @Override
    public int getItemCount() {
        return showtimes == null ? 0 : showtimes.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTime;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvShowtime);
        }
    }
}
