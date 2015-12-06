package com.netspace.crm.android.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.utils.SyncManager;
import com.netspace.crm.android.utils.TaskLoader;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * created by Oleh Kolomiets
 */
public abstract class BaseFragment extends Fragment {

    protected final String tag = getClass().getSimpleName();

    @Inject
    protected AppPreferences prefs;
    @Inject
    protected TaskLoader taskLoader;
    @Inject
    protected SyncManager syncManager;

    protected abstract int getContentView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CRMApp.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
