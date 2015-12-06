package com.netspace.crm.android.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.netspace.crm.android.model.Contact;

/**
 * Created by Oleh Kolomiets
 */
public class AppPreferences {

    public static final String PARAM_LOAD_PAGE = "keyLoadPerPage";

    private static final String TAG_UUID = "userId";
    private static final String TAG_START_TIME = "startTime";
    private static final String TAG_TOTAL_RESULT = "totalResult";
    private static final String TAG_LAST_SYNC = "syncTime";
    private static final String TAG_USER_PROFILE = "profile";

    private final SharedPreferences prefs;
    private final Gson gson;

    @Deprecated
    public AppPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(prefs.getString(TAG_UUID, ""));
    }

    public void saveUserId(String id) {
        prefs.edit().putString(TAG_UUID, id).apply();
    }

    public String getUserId() {
        return prefs.getString(TAG_UUID, "");
    }

    public long getStartTime() {
        return prefs.getLong(TAG_START_TIME, 0);
    }

    public void saveStartTime(long time) {
        prefs.edit().putLong(TAG_START_TIME, time).apply();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    public int getItemsPerPage() {
        return Integer.parseInt(prefs.getString("keyLoadPerPage", "25"));
    }

    public int getTotalResult() {
        return prefs.getInt(TAG_TOTAL_RESULT, 0);
    }

    public void setTotalResult(int result) {
        prefs.edit().putInt(TAG_TOTAL_RESULT, result).apply();
    }

    public void saveUserProfile(Contact contact) {
        prefs.edit().putString(TAG_USER_PROFILE, gson.toJson(contact)).apply();
    }

    public Contact getUserProfile() {
        String json = prefs.getString(TAG_USER_PROFILE, "");
        return gson.fromJson(json, Contact.class);
    }

    public void setLastSync(long time) {
        prefs.edit().putLong(TAG_LAST_SYNC, time).apply();
    }

    public long getLastSync() {
        return prefs.getLong(TAG_LAST_SYNC, 0);
    }
}
