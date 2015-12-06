package com.netspace.crm.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.R;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.model.AlarmService;
import com.netspace.crm.android.utils.TaskLoader;

import javax.inject.Inject;

/**
 * created by Oleh Kolomiets
 */
public class SettingsFragment extends PreferenceFragment {

    protected Preference logoutPreference;
    @Inject
    protected AppPreferences prefs;
    @Inject
    protected TaskLoader taskLoader;

    private ListPreference listPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CRMApp.getComponent().inject(this);
        addPreferencesFromResource(R.xml.settings_preference);
        listPreference = (ListPreference) findPreference(AppPreferences.PARAM_LOAD_PAGE);
        listPreference.setSummary(listPreference.getValue());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listPreference.setSummary(newValue.toString());
                listPreference.setValue(newValue.toString());
                return false;
            }
        });
        logoutPreference = findPreference(getString(R.string.logout_pref_key));
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage(R.string.logout_dialog_message)
                        .setPositiveButton(R.string.profile_logout, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (prefs.getStartTime() != 0) {
                                    getActivity().stopService(new Intent(getActivity(), AlarmService.class));
                                }
                                prefs.logout();
                                taskLoader.clearDatabase();
                                startActivity(new Intent(getActivity(), LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                getActivity().finish();
                            }
                        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });
    }
}
