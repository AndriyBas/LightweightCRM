package com.netspace.crm.android.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.netspace.crm.android.BuildConfig;
import com.netspace.crm.android.R;

import butterknife.Bind;

/**
 * Created on 12/6/15.
 */
public class VersionActivity extends BaseActivity {

    @Bind(R.id.activity_version_text)
    protected TextView versionText;

    @Override
    protected int getContentView() {
        return R.layout.activity_version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        versionText.setText(BuildConfig.VERSION_NAME);
    }
}
