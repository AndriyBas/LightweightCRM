package com.netspace.crm.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.utils.SyncManager;
import com.netspace.crm.android.utils.TaskLoader;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Oleh Kolomiets
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String tag = getClass().getSimpleName();

    @Inject
    protected AppPreferences prefs;
    @Inject
    protected TaskLoader taskLoader;
    @Inject
    protected SyncManager syncManager;

    protected abstract int getContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        CRMApp.getComponent().inject(this);
        ButterKnife.bind(this);
    }

    protected void showToast(int stringResource) {
        Toast.makeText(this, stringResource, Toast.LENGTH_SHORT).show();
    }
}
