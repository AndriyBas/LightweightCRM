package com.netspace.crm.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.netspace.crm.android.config.AppComponent;
import com.netspace.crm.android.config.AppModule;
import com.netspace.crm.android.config.DaggerAppComponent;
import com.netspace.crm.android.utils.SyncManager;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 *  Created by Oleh Kolomiets
 */
public class CRMApp extends Application {

    private static AppComponent component;

    @Inject
    protected SyncManager syncManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);
        syncManager.syncData();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
