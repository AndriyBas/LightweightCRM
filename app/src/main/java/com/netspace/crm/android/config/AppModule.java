package com.netspace.crm.android.config;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.netspace.crm.android.BuildConfig;
import com.netspace.crm.android.api.ApiService;
import com.netspace.crm.android.api.AuthInterceptor;
import com.netspace.crm.android.utils.SyncManager;
import com.netspace.crm.android.utils.TaskLoader;
import com.netspace.crm.android.utils.UTCDateAdapter;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created  by Oleh Kolomiets
 */
@Module
@SuppressWarnings("deprecation")
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public AppPreferences providePrefs() {
        return new AppPreferences(context);
    }

    @Provides
    @Singleton
    public ApiService provideService(AppPreferences prefs) {
        GsonConverter converter
                = new GsonConverter(new GsonBuilder().registerTypeAdapter(Date.class, new UTCDateAdapter()).create());
        AuthInterceptor requestInterceptor = new AuthInterceptor(prefs);
        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setClient(new MockClient())
                .setLogLevel((BuildConfig.DEBUG) ? RestAdapter.LogLevel.FULL
                        : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(converter)
                .setEndpoint(BuildConfig.URL)
                .build();

        return restAdapter.create(ApiService.class);
    }

    @Provides
    @Singleton
    public SyncManager provideSyncManager(AppDatabase database, ApiService service, AppPreferences preferences) {
        return new SyncManager(database, service, preferences);
    }

    @Provides
    @Singleton
    public TaskLoader provideTaskLoader(AppDatabase database, AppPreferences prefs, ApiService apiService) {
        return new TaskLoader(database, prefs, apiService);
    }

    @Provides
    @Singleton
    public AppDatabase provideDatabase() {
        return new AppDatabase(context);
    }
}
