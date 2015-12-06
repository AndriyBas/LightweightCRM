package com.netspace.crm.android.api;

import com.netspace.crm.android.config.AppPreferences;

import retrofit.RequestInterceptor;

/**
 * Created  by Andrew Budu on
 */
public class AuthInterceptor implements RequestInterceptor {

    private final AppPreferences prefs;

    public AuthInterceptor(AppPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void intercept(RequestFacade request) {
        String authorization = "Basic " + prefs.getUserId();
        if (prefs.isLoggedIn()) {
            request.addHeader("Authorization", authorization);
        }
    }
}
