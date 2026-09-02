package com.sithu.courtease.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sithu.courtease.R;
import com.sithu.courtease.adapters.BookingAdapter;
import com.sithu.courtease.models.Booking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookingsFragment extends Fragment {

    private RecyclerView recyclerViewBookings;
    private ProgressBar progressBar;
    private TextView tvEmptyBookings;
    private TextView tvErrorBookings;

    private BookingAdapter adapter;
    private final List<Booking> bookingList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public BookingsFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_bookings,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(
                view,
                savedInstanceState
        );

        recyclerViewBookings =
                view.findViewById(
                        R.id.recyclerViewBookings
                );

        progressBar =
                view.findViewById(
                        R.id.progressBarBookings
                );

        tvEmptyBookings =
                view.findViewById(
                        R.id.tvEmptyBookings
                );

        tvErrorBookings =
                view.findViewById(
                        R.id.tvErrorBookings
                );

        db =
                FirebaseFirestore.getInstance();

        auth =
                FirebaseAuth.getInstance();

        recyclerViewBookings.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        adapter =
                new BookingAdapter(
                        bookingList
                );

        recyclerViewBookings.setAdapter(
                adapter
        );

        loadBookings();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (db != null && auth != null) {
            loadBookings();
        }
    }

    private void loadBookings() {

        FirebaseUser user =
                auth.getCurrentUser();

        if (user == null) {

            showError(
                    "Please log in to view your bookings."
            );

            return;
        }

        showLoading();

        db.collection("bookings")
                .whereEqualTo(
                        "userId",
                        user.getUid()
                )
                .get()
                .addOnSuccessListener(
                        querySnapshot -> {

                            bookingList.clear();

                            for (QueryDocumentSnapshot document
                                    : querySnapshot) {

                                Booking booking =
                                        document.toObject(
                                                Booking.class
                                        );

                                if (booking.getId() == null
                                        || booking.getId().isEmpty()) {

                                    booking.setId(
                                            document.getId()
                                    );
                                }

                                bookingList.add(
                                        booking
                                );
                            }

                            // Sort locally to avoid requiring
                            // a compound Firestore index.
                            bookingList.sort(
                                    Comparator
                                            .comparing(
                                                    Booking::getDate,
                                                    Comparator.nullsLast(
                                                            String::compareTo
                                                    )
                                            )
                                            .thenComparing(
                                                    Booking::getStartTime,
                                                    Comparator.nullsLast(
                                                            String::compareTo
                                                    )
                                            )
                            );

                            adapter.notifyDataSetChanged();

                            showContentState();
                        }
                )
                .addOnFailureListener(
                        e -> {

                            String message =
                                    e.getMessage();

                            if (message == null
                                    || message.trim().isEmpty()) {

                                message =
                                        "Unable to load your bookings. Please try again.";
                            }

                            showError(
                                    message
                            );
                        }
                );
    }

    private void showLoading() {

        progressBar.setVisibility(
                View.VISIBLE
        );

        recyclerViewBookings.setVisibility(
                View.GONE
        );

        tvEmptyBookings.setVisibility(
                View.GONE
        );

        tvErrorBookings.setVisibility(
                View.GONE
        );
    }

    private void showContentState() {

        progressBar.setVisibility(
                View.GONE
        );

        tvErrorBookings.setVisibility(
                View.GONE
        );

        if (bookingList.isEmpty()) {

            recyclerViewBookings.setVisibility(
                    View.GONE
            );

            tvEmptyBookings.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerViewBookings.setVisibility(
                    View.VISIBLE
            );

            tvEmptyBookings.setVisibility(
                    View.GONE
            );
        }
    }

    private void showError(String message) {

        progressBar.setVisibility(
                View.GONE
        );

        recyclerViewBookings.setVisibility(
                View.GONE
        );

        tvEmptyBookings.setVisibility(
                View.GONE
        );

        tvErrorBookings.setVisibility(
                View.VISIBLE
        );

        tvErrorBookings.setText(
                message
        );
    }
}