package com.sithu.courtease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sithu.courtease.R;
import com.sithu.courtease.activities.BookingActivity;
import com.sithu.courtease.models.Court;
import com.sithu.courtease.utils.Constants;

import java.util.Locale;

public class CourtDetailsFragment extends Fragment {

    private static final String ARG_COURT = "court";

    private Court court;

    private ImageView ivCourtDetails;
    private TextView tvCourtNameDetails;
    private TextView tvSportDetails;
    private TextView tvLocationDetails;
    private TextView tvAddressDetails;
    private TextView tvRatingDetails;
    private TextView tvPriceDetails;
    private TextView tvDescriptionDetails;

    private Button btnBookNow;

    public static CourtDetailsFragment newInstance(Court court) {

        CourtDetailsFragment fragment =
                new CourtDetailsFragment();

        Bundle args = new Bundle();

        args.putSerializable(ARG_COURT, court);

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_court_details,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            court = (Court)
                    getArguments()
                            .getSerializable(ARG_COURT);
        }

        initialiseViews(view);

        if (court != null) {

            displayCourtDetails();

            if (!court.isAvailable()) {

                btnBookNow.setEnabled(false);
                btnBookNow.setText(R.string.currently_unavailable);
                btnBookNow.setAlpha(0.6f);

            } else {

                btnBookNow.setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    requireContext(),
                                    BookingActivity.class
                            );

                    intent.putExtra(
                            "courtId",
                            court.getId()
                    );

                    intent.putExtra(
                            "courtName",
                            court.getName()
                    );

                    intent.putExtra(
                            "sport",
                            court.getSport()
                    );

                    intent.putExtra(
                            "location",
                            court.getLocation()
                    );

                    intent.putExtra(
                            "pricePerHour",
                            court.getPricePerHour()
                    );

                    startActivity(intent);
                });
            }
        }
    }

    private void initialiseViews(View view) {

        ivCourtDetails =
                view.findViewById(R.id.ivCourtDetails);

        tvCourtNameDetails =
                view.findViewById(R.id.tvCourtNameDetails);

        tvSportDetails =
                view.findViewById(R.id.tvSportDetails);

        tvLocationDetails =
                view.findViewById(R.id.tvLocationDetails);

        tvAddressDetails =
                view.findViewById(R.id.tvAddressDetails);

        tvRatingDetails =
                view.findViewById(R.id.tvRatingDetails);

        tvPriceDetails =
                view.findViewById(R.id.tvPriceDetails);

        tvDescriptionDetails =
                view.findViewById(R.id.tvDescriptionDetails);

        btnBookNow =
                view.findViewById(R.id.btnBookNow);
    }

    private void displayCourtDetails() {

        tvCourtNameDetails.setText(
                court.getName()
        );

        tvSportDetails.setText(
                court.getSport()
        );

        tvLocationDetails.setText(
                court.getLocation()
        );

        tvAddressDetails.setText(
                court.getAddress()
        );

        tvRatingDetails.setText(
                String.format(
                        Locale.getDefault(),
                        "★ %.1f",
                        court.getRating()
                )
        );

        tvPriceDetails.setText(
                String.format(
                        Locale.getDefault(),
                        "$%.2f / hour",
                        court.getPricePerHour()
                )
        );

        tvDescriptionDetails.setText(
                court.getDescription()
        );

        setCourtImage();
    }

    private void setCourtImage() {

        int imageResource = requireContext().getResources().getIdentifier(
                court.getImageName(),
                "drawable",
                requireContext().getPackageName()
        );

        if (imageResource == 0) {
            imageResource = R.drawable.court_indoor;
        }

        Glide.with(this)
                .load(imageResource)
                .centerCrop()
                .into(ivCourtDetails);
    }
}