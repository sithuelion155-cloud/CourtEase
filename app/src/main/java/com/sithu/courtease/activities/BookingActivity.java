package com.sithu.courtease.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Transaction;
import com.sithu.courtease.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private TextView tvCourtName;
    private TextView tvSport;
    private TextView tvLocation;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private TextView tvTotalPrice;

    private MaterialButton btnSelectDate;
    private MaterialButton btnConfirmBooking;

    private Spinner spinnerTime;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String courtId;
    private String courtName;
    private String sport;
    private String location;
    private double pricePerHour;

    private String selectedDate = "";
    private String selectedStartTime = "";
    private String selectedEndTime = "";

    private boolean bookingInProgress = false;

    private final String[] timeSlots = {
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(
                    this,
                    "Please log in before making a booking.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        readCourtDetailsFromIntent();

        initializeViews();

        setupTimeSpinner();

        btnSelectDate.setOnClickListener(v -> showDatePicker());

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void readCourtDetailsFromIntent() {

        courtId = getIntent().getStringExtra("courtId");
        courtName = getIntent().getStringExtra("courtName");
        sport = getIntent().getStringExtra("sport");
        location = getIntent().getStringExtra("location");

        pricePerHour =
                getIntent().getDoubleExtra(
                        "pricePerHour",
                        0.0
                );

        if (TextUtils.isEmpty(courtId)
                || TextUtils.isEmpty(courtName)) {

            Toast.makeText(
                    this,
                    "Court information is missing.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
        }
    }

    private void initializeViews() {

        tvCourtName = findViewById(R.id.tvCourtName);
        tvSport = findViewById(R.id.tvSport);
        tvLocation = findViewById(R.id.tvLocation);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        spinnerTime = findViewById(R.id.spinnerTime);

        tvCourtName.setText(courtName);
        tvSport.setText(sport);
        tvLocation.setText(location);

        tvTotalPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "$%.2f / hour",
                        pricePerHour
                )
        );

        tvSelectedDate.setText("Select a date");
        tvSelectedTime.setText("Select a time");
    }

    private void setupTimeSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        timeSlots
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerTime.setAdapter(adapter);

        spinnerTime.setSelection(0);

        spinnerTime.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        selectedStartTime =
                                timeSlots[position];

                        selectedEndTime =
                                calculateEndTime(
                                        selectedStartTime
                                );

                        tvSelectedTime.setText(
                                selectedStartTime
                                        + " - "
                                        + selectedEndTime
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {

                        selectedStartTime = "";
                        selectedEndTime = "";
                        tvSelectedTime.setText(
                                "Select a time"
                        );
                    }
                }
        );
    }

    private String calculateEndTime(String startTime) {

        try {

            String[] parts = startTime.split(":");

            int hour =
                    Integer.parseInt(parts[0]);

            int minute =
                    Integer.parseInt(parts[1]);

            Calendar calendar =
                    Calendar.getInstance();

            calendar.set(
                    Calendar.HOUR_OF_DAY,
                    hour
            );

            calendar.set(
                    Calendar.MINUTE,
                    minute
            );

            calendar.add(
                    Calendar.HOUR_OF_DAY,
                    1
            );

            return String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            );

        } catch (Exception e) {

            return "";
        }
    }

    private void showDatePicker() {

        Calendar today =
                Calendar.getInstance();

        Calendar minimumDate =
                Calendar.getInstance();

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {

                            Calendar selected =
                                    Calendar.getInstance();

                            selected.set(
                                    year,
                                    month,
                                    dayOfMonth
                            );

                            SimpleDateFormat formatter =
                                    new SimpleDateFormat(
                                            "yyyy-MM-dd",
                                            Locale.getDefault()
                                    );

                            selectedDate =
                                    formatter.format(selected.getTime());

                            tvSelectedDate.setText(
                                    formatReadableDate(selected)
                            );
                        },
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)
                );

        // Prevent selecting dates in the past.
        dialog.getDatePicker()
                .setMinDate(
                        minimumDate.getTimeInMillis()
                );

        dialog.show();
    }

    private String formatReadableDate(
            Calendar calendar) {

        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "EEE, d MMM yyyy",
                        Locale.getDefault()
                );

        return formatter.format(
                calendar.getTime()
        );
    }

    private void confirmBooking() {

        if (bookingInProgress) {
            return;
        }

        if (TextUtils.isEmpty(selectedDate)) {

            Toast.makeText(
                    this,
                    "Please select a date.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (TextUtils.isEmpty(selectedStartTime)
                || TextUtils.isEmpty(selectedEndTime)) {

            Toast.makeText(
                    this,
                    "Please select a time.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        FirebaseUser user =
                auth.getCurrentUser();

        if (user == null) {

            Toast.makeText(
                    this,
                    "Your session has expired. Please log in again.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        bookingInProgress = true;

        btnConfirmBooking.setEnabled(false);
        btnConfirmBooking.setText("Checking availability...");

        String bookingId =
                buildBookingId();

        DocumentReference bookingReference =
                db.collection("bookings")
                        .document(bookingId);

        db.runTransaction(
                transaction -> {

                    com.google.firebase.firestore.DocumentSnapshot snapshot =
                            transaction.get(bookingReference);

                    /*
                     * Reject only an active/confirmed booking.
                     * A cancelled booking can be reused.
                     */
                    if (snapshot.exists()) {

                        String existingStatus =
                                snapshot.getString("status");

                        if ("CONFIRMED".equalsIgnoreCase(
                                existingStatus)) {

                            throw new FirebaseFirestoreException(
                                    "This time slot is already booked.",
                                    FirebaseFirestoreException.Code.ALREADY_EXISTS
                            );
                        }
                    }

                    Map<String, Object> booking =
                            new HashMap<>();

                    booking.put(
                            "id",
                            bookingId
                    );

                    booking.put(
                            "userId",
                            user.getUid()
                    );

                    booking.put(
                            "courtId",
                            courtId
                    );

                    booking.put(
                            "courtName",
                            courtName
                    );

                    booking.put(
                            "sport",
                            sport
                    );

                    booking.put(
                            "location",
                            location
                    );

                    booking.put(
                            "date",
                            selectedDate
                    );

                    booking.put(
                            "startTime",
                            selectedStartTime
                    );

                    booking.put(
                            "endTime",
                            selectedEndTime
                    );

                    booking.put(
                            "price",
                            pricePerHour
                    );

                    booking.put(
                            "status",
                            "CONFIRMED"
                    );

                    booking.put(
                            "createdAt",
                            FieldValue.serverTimestamp()
                    );

                    transaction.set(
                            bookingReference,
                            booking
                    );

                    return null;
                }
        ).addOnSuccessListener(
                unused -> {

                    bookingInProgress = false;

                    btnConfirmBooking.setEnabled(true);

                    btnConfirmBooking.setText(
                            "Booking Confirmed"
                    );

                    Toast.makeText(
                            BookingActivity.this,
                            "Booking confirmed successfully.",
                            Toast.LENGTH_LONG
                    ).show();

                    showConfirmationAndFinish(
                            bookingId
                    );
                }
        ).addOnFailureListener(
                e -> {

                    bookingInProgress = false;

                    btnConfirmBooking.setEnabled(true);
                    btnConfirmBooking.setText(
                            "Confirm Booking"
                    );

                    String message =
                            e.getMessage();

                    if (e instanceof FirebaseFirestoreException) {

                        FirebaseFirestoreException firestoreException =
                                (FirebaseFirestoreException) e;

                        switch (firestoreException.getCode()) {

                            case ALREADY_EXISTS:

                                message =
                                        "This time slot is already booked. "
                                                + "Please choose another time.";

                                break;

                            case PERMISSION_DENIED:

                                message =
                                        "You do not have permission to make "
                                                + "this booking.";

                                break;

                            case UNAVAILABLE:

                                message =
                                        "The booking service is temporarily "
                                                + "unavailable. Please try again.";

                                break;

                            default:

                                message =
                                        "Unable to complete booking. "
                                                + "Please try again.";
                        }
                    }

                    if (message == null
                            || message.trim().isEmpty()) {

                        message =
                                "Unable to complete booking. "
                                        + "Please try again.";
                    }

                    Toast.makeText(
                            BookingActivity.this,
                            message,
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    private String buildBookingId() {

        String safeStartTime =
                selectedStartTime.replace(
                        ":",
                        ""
                );

        return courtId
                + "_"
                + selectedDate
                + "_"
                + safeStartTime;
    }

    private void showConfirmationAndFinish(
            String bookingId) {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Booking Confirmed")
                .setMessage(
                        "Your booking has been confirmed.\n\n"
                                + courtName
                                + "\n"
                                + selectedDate
                                + "\n"
                                + selectedStartTime
                                + " - "
                                + selectedEndTime
                )
                .setPositiveButton(
                        "View My Bookings",
                        (dialog, which) -> {

                            finish();
                        }
                )
                .setOnDismissListener(
                        dialog -> finish()
                )
                .show();
    }
}