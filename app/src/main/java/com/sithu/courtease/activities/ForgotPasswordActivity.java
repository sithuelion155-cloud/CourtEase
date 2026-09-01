package com.sithu.courtease.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sithu.courtease.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnResetPassword.setOnClickListener(v -> sendResetEmail());

        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void sendResetEmail() {

        String email = etEmail.getText().toString().trim();

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

        btnResetPassword.setEnabled(false);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    btnResetPassword.setEnabled(true);

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Unable to send reset email. Please check the email address.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}