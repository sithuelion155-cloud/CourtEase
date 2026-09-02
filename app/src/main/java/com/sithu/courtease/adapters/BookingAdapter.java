package com.sithu.courtease.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sithu.courtease.R;
import com.sithu.courtease.models.Booking;

import java.util.List;
import java.util.Locale;

public class BookingAdapter
        extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookings;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_booking,
                                parent,
                                false
                        );

        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BookingViewHolder holder,
            int position) {

        Booking booking =
                bookings.get(position);

        holder.tvCourtName.setText(
                booking.getCourtName()
        );

        holder.tvDetails.setText(
                booking.getSport()
                        + " • "
                        + booking.getLocation()
        );

        holder.tvDateTime.setText(
                booking.getDate()
                        + "\n"
                        + booking.getStartTime()
                        + " - "
                        + booking.getEndTime()
        );

        holder.tvPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "$%.2f",
                        booking.getPrice()
                )
        );

        holder.tvStatus.setText(
                booking.getStatus()
        );

        if ("CONFIRMED".equalsIgnoreCase(
                booking.getStatus())) {

            holder.tvStatus.setVisibility(
                    View.VISIBLE
            );

            holder.btnCancel.setVisibility(
                    View.VISIBLE
            );

            holder.btnCancel.setOnClickListener(
                    v -> showCancelDialog(
                            holder.itemView,
                            booking,
                            position
                    )
            );

        } else {

            holder.tvStatus.setVisibility(
                    View.VISIBLE
            );

            holder.btnCancel.setVisibility(
                    View.GONE
            );
        }
    }

    private void showCancelDialog(
            View view,
            Booking booking,
            int position) {

        new AlertDialog.Builder(
                view.getContext()
        )
                .setTitle("Cancel Booking")
                .setMessage(
                        "Are you sure you want to cancel this booking?"
                )
                .setNegativeButton(
                        "Keep",
                        null
                )
                .setPositiveButton(
                        "Cancel Booking",
                        (dialog, which) ->
                                cancelBooking(
                                        view,
                                        booking,
                                        position
                                )
                )
                .show();
    }

    private void cancelBooking(
            View view,
            Booking booking,
            int position) {

        FirebaseFirestore db =
                FirebaseFirestore.getInstance();

        db.collection("bookings")
                .document(booking.getId())
                .update(
                        "status",
                        "CANCELLED"
                )
                .addOnSuccessListener(
                        unused -> {

                            booking.setStatus(
                                    "CANCELLED"
                            );

                            notifyItemChanged(position);

                            Toast.makeText(
                                    view.getContext(),
                                    "Booking cancelled.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .addOnFailureListener(
                        e -> Toast.makeText(
                                view.getContext(),
                                "Unable to cancel booking.",
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvCourtName;
        TextView tvDetails;
        TextView tvDateTime;
        TextView tvPrice;
        TextView tvStatus;
        TextView btnCancel;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourtName =
                    itemView.findViewById(
                            R.id.tvBookingCourtName
                    );

            tvDetails =
                    itemView.findViewById(
                            R.id.tvBookingDetails
                    );

            tvDateTime =
                    itemView.findViewById(
                            R.id.tvBookingDateTime
                    );

            tvPrice =
                    itemView.findViewById(
                            R.id.tvBookingPrice
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvBookingStatus
                    );

            btnCancel =
                    itemView.findViewById(
                            R.id.btnCancelBooking
                    );
        }
    }
}