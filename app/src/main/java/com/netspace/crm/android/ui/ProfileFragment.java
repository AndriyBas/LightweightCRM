package com.netspace.crm.android.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.netspace.crm.android.R;
import com.netspace.crm.android.model.Contact;

import butterknife.Bind;

/**
 * Created by Oleh Kolomiets
 */
public class ProfileFragment extends BaseFragment {

    @Bind(R.id.user_name)
    protected TextView userNameTV;
    @Bind(R.id.user_position)
    protected TextView userPositionTV;

    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Contact userProfile = prefs.getUserProfile();
        userNameTV.setText(getString(R.string.user_name_title, userProfile.getName()));
        String userPosition = userProfile.getPosition();
        if (!TextUtils.isEmpty(userPosition)) {
            userPositionTV.setText(getString(R.string.user_position_title, userPosition));
        } else {
            userPositionTV.setVisibility(View.GONE);
        }
    }
}
