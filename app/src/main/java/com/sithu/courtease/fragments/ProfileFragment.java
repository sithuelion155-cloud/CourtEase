package com.sithu.courtease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sithu.courtease.R;
import com.sithu.courtease.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvProfilePhone;
    private TextView tvProfileAvatar;

    private ProgressBar progressBarProfile;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ProfileFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_profile,
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

        tvProfileName =
                view.findViewById(
                        R.id.tvProfileName
                );

        tvProfileEmail =
                view.findViewById(
                        R.id.tvProfileEmail
                );

        tvProfilePhone =
                view.findViewById(
                        R.id.tvProfilePhone
                );

        tvProfileAvatar =
                view.findViewById(
                        R.id.tvProfileAvatar
                );

        progressBarProfile =
                view.findViewById(
                        R.id.progressBarProfile
                );

        View btnLogout =
                view.findViewById(
                        R.id.btnLogout
                );

        auth =
                FirebaseAuth.getInstance();

        db =
                FirebaseFirestore.getInstance();

        btnLogout.setOnClickListener(
                v -> logout()
        );

        loadUserProfile();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (auth != null) {
            loadUserProfile();
        }
    }

    private void loadUserProfile() {

        FirebaseUser firebaseUser =
                auth.getCurrentUser();

        if (firebaseUser == null) {
            logout();
            return;
        }

        progressBarProfile.setVisibility(
                View.VISIBLE
        );

        /*
         * First use Firebase Authentication data
         * because it is immediately available.
         */
        String authName =
                firebaseUser.getDisplayName();

        String email =
                firebaseUser.getEmail();

        tvProfileName.setText(
                authName != null
                        && !authName.trim().isEmpty()
                        ? authName
                        : "CourtEase User"
        );

        String firstLetter =
                tvProfileName
                        .getText()
                        .toString()
                        .trim()
                        .substring(0, 1)
                        .toUpperCase();

        tvProfileAvatar.setText(
                firstLetter
        );

        tvProfileEmail.setText(
                email != null
                        ? email
                        : ""
        );

        /*
         * Then load our richer application profile
         * from Firestore.
         */
        db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {

                            progressBarProfile
                                    .setVisibility(
                                            View.GONE
                                    );

                            if (documentSnapshot.exists()) {

                                String name =
                                        documentSnapshot.getString(
                                                "name"
                                        );

                                String phone =
                                        documentSnapshot.getString(
                                                "phone"
                                        );

                                if (name != null
                                        && !name.trim().isEmpty()) {

                                    tvProfileName.setText(
                                            name
                                    );

                                    String initial =
                                            name.trim()
                                                    .substring(0, 1)
                                                    .toUpperCase();

                                    tvProfileAvatar.setText(
                                            initial
                                    );
                                }

                                if (phone != null
                                        && !phone.trim().isEmpty()) {

                                    tvProfilePhone.setText(
                                            phone
                                    );

                                } else {

                                    tvProfilePhone.setText(
                                            "Phone number not provided"
                                    );
                                }

                            } else {

                                tvProfilePhone.setText(
                                        "Phone number not provided"
                                );
                            }
                        }
                )
                .addOnFailureListener(
                        e -> {

                            progressBarProfile
                                    .setVisibility(
                                            View.GONE
                                    );

                            /*
                             * Authentication information
                             * is still displayed even if
                             * Firestore temporarily fails.
                             */
                            tvProfilePhone.setText(
                                    "Phone number not available"
                            );
                        }
                );
    }

    private void logout() {

        auth.signOut();

        Intent intent =
                new Intent(
                        requireActivity(),
                        LoginActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        requireActivity().finish();
    }
}