package com.sithu.courtease.activities;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sithu.courtease.R;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;
    private Button btnGoogle;

    private Button tvForgotPassword;
    private Button tvRegister;

    private FirebaseAuth mAuth;

    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            openMainActivity();
            return;
        }

        // Initialize Credential Manager
        credentialManager = CredentialManager.create(this);

        // Connect XML components
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        // Email/password login
        btnLogin.setOnClickListener(v -> loginUser());

        // Register
        tvRegister.setOnClickListener(v -> {
            android.content.Intent intent =
                    new android.content.Intent(
                            LoginActivity.this,
                            RegisterActivity.class
                    );

            startActivity(intent);
        });

        // Forgot password
        tvForgotPassword.setOnClickListener(v -> {
            android.content.Intent intent =
                    new android.content.Intent(
                            LoginActivity.this,
                            ForgotPasswordActivity.class
                    );

            startActivity(intent);
        });

        // Google Sign-In
        btnGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    /**
     * Email/password authentication.
     */
    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Firebase email/password authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                LoginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        openMainActivity();

                    } else {

                        String errorMessage =
                                "Login failed. Please check your details.";

                        if (task.getException() != null) {
                            errorMessage = task.getException().getMessage();
                        }

                        Toast.makeText(
                                LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    /**
     * Starts the Google Sign-In flow using Credential Manager.
     */
    private void signInWithGoogle() {

        // Prevent repeated clicks while authentication is running
        btnGoogle.setEnabled(false);

        Toast.makeText(
                LoginActivity.this,
                "Connecting to Google...",
                Toast.LENGTH_SHORT
        ).show();

        /*
         * Google requires the Web OAuth client ID here.
         *
         * This value is generated automatically from
         * google-services.json as:
         *
         * R.string.default_web_client_id
         */
        GetGoogleIdOption googleIdOption =
                new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(
                                getString(R.string.default_web_client_id)
                        )
                        .build();

        /*
         * Create the Credential Manager request.
         */
        GetCredentialRequest request =
                new GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build();

        /*
         * Ask Credential Manager to obtain a Google credential.
         */
        credentialManager.getCredentialAsync(
                LoginActivity.this,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse,
                        GetCredentialException>() {

                    @Override
                    public void onResult(
                            @NonNull GetCredentialResponse result) {

                        runOnUiThread(() -> {
                            handleGoogleCredential(result);
                        });
                    }

                    @Override
                    public void onError(
                            @NonNull GetCredentialException e) {

                        runOnUiThread(() -> {

                            btnGoogle.setEnabled(true);

                            Log.e(
                                    TAG,
                                    "Google Credential Manager error",
                                    e
                            );

                            Toast.makeText(
                                    LoginActivity.this,
                                    getGoogleErrorMessage(e),
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    /**
     * Processes the credential returned by Credential Manager.
     */
    private void handleGoogleCredential(
            GetCredentialResponse result) {

        btnGoogle.setEnabled(true);

        Credential credential = result.getCredential();

        /*
         * Check that the returned credential is a
         * Google ID token credential.
         */
        if (credential instanceof CustomCredential) {

            CustomCredential customCredential =
                    (CustomCredential) credential;

            if (GoogleIdTokenCredential
                    .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    .equals(customCredential.getType())) {

                /*
                 * Convert the returned credential data
                 * into a GoogleIdTokenCredential.
                 */
                GoogleIdTokenCredential googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(
                                customCredential.getData()
                        );

                String idToken =
                        googleIdTokenCredential.getIdToken();

                /*
                 * Send the Google ID token to Firebase.
                 */
                firebaseAuthWithGoogle(idToken);

            } else {

                Log.e(
                        TAG,
                        "Unexpected credential type: "
                                + customCredential.getType()
                );

                Toast.makeText(
                        LoginActivity.this,
                        "Unexpected Google credential.",
                        Toast.LENGTH_LONG
                ).show();
            }

        } else {

            Log.e(
                    TAG,
                    "Credential returned was not a CustomCredential."
            );

            Toast.makeText(
                    LoginActivity.this,
                    "Unable to process Google account.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    /**
     * Authenticate the Google ID token with Firebase.
     */
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        idToken,
                        null
                );

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    btnGoogle.setEnabled(true);

                    if (task.isSuccessful()) {

                        Log.d(
                                TAG,
                                "Google sign-in successful"
                        );

                        FirebaseUser user =
                                mAuth.getCurrentUser();

                        if (user != null) {

                            String displayName =
                                    user.getDisplayName();

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Welcome "
                                            + (displayName != null
                                            ? displayName
                                            : "to CourtEase"),
                                    Toast.LENGTH_SHORT
                            ).show();

                            openMainActivity();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Authentication succeeded, but user information could not be loaded.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    } else {

                        Log.e(
                                TAG,
                                "Firebase Google authentication failed",
                                task.getException()
                        );

                        String errorMessage =
                                "Google sign-in failed. Please try again.";

                        if (task.getException() != null) {
                            errorMessage =
                                    task.getException().getMessage();
                        }

                        Toast.makeText(
                                LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    /**
     * Converts Credential Manager failures into
     * understandable user-facing messages.
     */
    private String getGoogleErrorMessage(
            GetCredentialException exception) {

        if (exception instanceof androidx.credentials.exceptions.NoCredentialException) {
            return "No Google accounts found. Please make sure you are signed into Google on this device.";
        }

        String message = exception.getMessage();

        if (message == null || message.trim().isEmpty()) {
            return "Google sign-in was cancelled or could not be completed.";
        }

        return "Google sign-in failed: " + message;
    }

    /**
     * Navigate to the authenticated application shell.
     */
    private void openMainActivity() {

        android.content.Intent intent =
                new android.content.Intent(
                        LoginActivity.this,
                        com.sithu.courtease.MainActivity.class
                );

        /*
         * Prevent the user from pressing Back and returning
         * to the authentication screen after successful login.
         */
        intent.addFlags(
                android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}