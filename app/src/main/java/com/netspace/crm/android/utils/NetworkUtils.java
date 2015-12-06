package com.netspace.crm.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * Created by Oleh Kolomiets
 */
public final class NetworkUtils {

    private NetworkUtils() {
        // Utility classes should not have a public or default constructor
    }

    public static boolean checkInternetConnection(@Nullable Context context) {
        boolean result = false;
        if (context != null) {
            ConnectivityManager cm
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            result = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return result;
    }
}
