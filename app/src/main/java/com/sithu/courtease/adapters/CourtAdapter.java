package com.sithu.courtease.adapters;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sithu.courtease.R;
import com.sithu.courtease.models.Court;

import java.util.List;
import java.util.Locale;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {

    public interface OnCourtClickListener {
        void onCourtClick(Court court);
    }

    private List<Court> courtList;
    private final OnCourtClickListener listener;

    public CourtAdapter(
            List<Court> courtList,
            OnCourtClickListener listener
    ) {
        this.courtList = courtList;
        this.listener = listener;
    }

    public void updateList(List<Court> newList) {
        this.courtList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_court, parent, false);

        return new CourtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CourtViewHolder holder,
            int position
    ) {

        Court court = courtList.get(position);

        holder.tvCourtName.setText(court.getName());
        holder.tvSport.setText(court.getSport());
        holder.tvLocation.setText(court.getLocation());

        holder.tvPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "$%.2f / hour",
                        court.getPricePerHour()
                )
        );

        holder.tvRating.setText(
                String.format(
                        Locale.getDefault(),
                        "★ %.1f",
                        court.getRating()
                )
        );

        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
                court.getImageName(),
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        if (imageResource == 0) {
            imageResource = R.drawable.court_indoor;
        }

        Glide.with(holder.itemView.getContext())
                .load(imageResource)
                .centerCrop()
                .into(holder.ivCourt);

        holder.itemView.setOnClickListener(v ->
                listener.onCourtClick(court)
        );
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }

    static class CourtViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCourt;
        TextView tvCourtName;
        TextView tvSport;
        TextView tvLocation;
        TextView tvPrice;
        TextView tvRating;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCourt = itemView.findViewById(R.id.ivCourt);
            tvCourtName = itemView.findViewById(R.id.tvCourtName);
            tvSport = itemView.findViewById(R.id.tvSport);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}