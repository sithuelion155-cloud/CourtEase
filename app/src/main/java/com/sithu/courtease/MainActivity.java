package com.sithu.courtease;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sithu.courtease.activities.LoginActivity;
import com.sithu.courtease.fragments.BookingsFragment;
import com.sithu.courtease.fragments.HomeFragment;
import com.sithu.courtease.fragments.NotificationsFragment;
import com.sithu.courtease.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private final BottomNavigationView.OnItemSelectedListener navigationListener =
            item -> {

                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();

                } else if (itemId == R.id.nav_bookings) {
                    selectedFragment = new BookingsFragment();

                } else if (itemId == R.id.nav_notifications) {
                    selectedFragment = new NotificationsFragment();

                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragmentContainer,
                                    selectedFragment
                            )
                            .commit();

                    return true;
                }

                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Make sure only authenticated users can access the application shell.
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            openLogin();
            return;
        }

        bottomNavigationView =
                findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(
                navigationListener
        );

        // Display Home when the application starts.
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(
                    R.id.nav_home
            );
        }
    }

    private void openLogin() {

        android.content.Intent intent =
                new android.content.Intent(
                        MainActivity.this,
                        LoginActivity.class
                );

        intent.setFlags(
                android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}