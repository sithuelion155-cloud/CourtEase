package com.sithu.courtease.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtils {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    public static String getUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public static boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
}