package com.sithu.courtease.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUserManager {

    private final FirebaseFirestore db;

    public FirestoreUserManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void createOrUpdateUser(
            FirebaseUser firebaseUser,
            String name,
            String phone,
            String provider,
            OnUserSavedListener listener) {

        if (firebaseUser == null) {

            listener.onFailure(
                    new IllegalArgumentException(
                            "Firebase user is null."
                    )
            );

            return;
        }

        String uid =
                firebaseUser.getUid();

        String email =
                firebaseUser.getEmail();

        /*
         * If the supplied name is empty, use the
         * Firebase display name.
         */
        if (name == null || name.trim().isEmpty()) {

            name =
                    firebaseUser.getDisplayName();
        }

        if (name == null || name.trim().isEmpty()) {

            name = "CourtEase User";
        }

        if (phone == null) {
            phone = "";
        }

        if (provider == null
                || provider.trim().isEmpty()) {

            provider = "email";
        }

        Map<String, Object> userData =
                new HashMap<>();

        userData.put(
                "uid",
                uid
        );

        userData.put(
                "name",
                name.trim()
        );

        userData.put(
                "email",
                email != null ? email : ""
        );

        userData.put(
                "phone",
                phone.trim()
        );

        userData.put(
                "provider",
                provider
        );

        userData.put(
                "updatedAt",
                FieldValue.serverTimestamp()
        );

        DocumentReference userReference =
                db.collection("users")
                        .document(uid);

        /*
         * merge() prevents us from unnecessarily
         * deleting existing profile fields.
         */
        userReference.set(
                        userData,
                        SetOptions.merge()
                )
                .addOnSuccessListener(
                        unused -> {

                            listener.onSuccess();
                        }
                )
                .addOnFailureListener(
                        listener::onFailure
                );
    }

    public interface OnUserSavedListener {

        void onSuccess();

        void onFailure(@NonNull Exception e);
    }
}