package com.netspace.crm.android.config;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.model.AlarmService;
import com.netspace.crm.android.ui.BaseActivity;
import com.netspace.crm.android.ui.BaseFragment;
import com.netspace.crm.android.ui.LoginActivity;
import com.netspace.crm.android.ui.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * created by Oleh Kolomiets
 */

@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(AlarmService s);

    void inject(BaseFragment f);

    void inject(BaseActivity a);

    void inject(LoginActivity loginActivity);

    void inject(SettingsFragment settingsFragment);

    void inject(CRMApp app);
}
