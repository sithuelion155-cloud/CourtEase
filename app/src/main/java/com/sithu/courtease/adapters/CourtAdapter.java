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

        int imageResource = getImageResource(court.getImageName());

        if (imageResource != 0) {
            Glide.with(holder.itemView.getContext())
                    .load(imageResource)
                    .centerCrop()
                    .into(holder.ivCourt);
        }

        holder.itemView.setOnClickListener(v ->
                listener.onCourtClick(court)
        );
    }

    private int getImageResource(String imageName) {

        if (imageName == null) {
            return 0;
        }

        switch (imageName) {

            case "court_badminton":
                return R.drawable.court_indoor;

            case "court_basketball":
                return R.drawable.court_multisport;

            case "court_pickleball":
                return R.drawable.court_pickleball;

            case "court_multisport":
                return R.drawable.court_basketball;

            case "court_indoor":
                return R.drawable.court_badminton;

            default:
                return R.drawable.court_indoor;
        }
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