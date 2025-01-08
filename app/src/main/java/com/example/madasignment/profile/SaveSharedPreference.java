package com.example.madasignment.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String PREF_NAME = "UserPreferences";
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_IS_LOGGED_IN = "isLoggedIn";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setLoggedIn(Context ctx, boolean isLoggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public static boolean isLoggedIn(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_LOGGED_IN, false);
    }

    public static void clearUserData(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }
}
