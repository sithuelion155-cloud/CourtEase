package com.sithu.courtease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sithu.courtease.MainActivity;
import com.sithu.courtease.R;

import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.auth.FirebaseUser;
import com.sithu.courtease.utils.FirestoreUserManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btnRegister;
    private Button tvLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Connect XML components
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Register button
        btnRegister.setOnClickListener(v -> registerUser());

        // Return to Login
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword =
                etConfirmPassword.getText().toString().trim();

        // -----------------------------
        // Defensive Input Validation
        // -----------------------------

        if (TextUtils.isEmpty(name)) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError(
                    "Password must contain at least 6 characters"
            );
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError(
                    "Please confirm your password"
            );
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(
                    "Passwords do not match"
            );
            etConfirmPassword.requestFocus();
            return;
        }

        // -----------------------------
        // Create Firebase Account
        // -----------------------------

        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    btnRegister.setEnabled(true);

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                RegisterActivity.this,
                                "Account created successfully!",
                                Toast.LENGTH_LONG
                        ).show();

                        FirebaseUser user = task.getResult().getUser();

                        if (user != null) {

                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(
                                            profileTask -> {

                                                FirestoreUserManager userManager =
                                                        new FirestoreUserManager();

                                                userManager.createOrUpdateUser(
                                                        user,
                                                        name,
                                                        phone,
                                                        "email",
                                                        new FirestoreUserManager.OnUserSavedListener() {

                                                            @Override
                                                            public void onSuccess() {

                                                                Toast.makeText(
                                                                        RegisterActivity.this,
                                                                        "Account created successfully.",
                                                                        Toast.LENGTH_SHORT
                                                                ).show();

                                                                Intent intent =
                                                                        new Intent(
                                                                                RegisterActivity.this,
                                                                                MainActivity.class
                                                                        );

                                                                intent.setFlags(
                                                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                );

                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onFailure(
                                                                    @NonNull Exception e) {

                                                                Toast.makeText(
                                                                        RegisterActivity.this,
                                                                        "Account created, but profile could not be saved. Please try again.",
                                                                        Toast.LENGTH_LONG
                                                                ).show();

                                                                // Allow access even if Firestore failed.
                                                                Intent intent =
                                                                        new Intent(
                                                                                RegisterActivity.this,
                                                                                MainActivity.class
                                                                        );
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                );
                                            }
                                    );
                        }

                    } else {

                        String errorMessage;

                        if (task.getException() != null) {
                            errorMessage =
                                    task.getException().getMessage();
                        } else {
                            errorMessage =
                                    "Registration failed.";
                        }

                        Toast.makeText(
                                RegisterActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}