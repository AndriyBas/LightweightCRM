package com.netspace.crm.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.R;
import com.netspace.crm.android.api.ApiService;
import com.netspace.crm.android.api.RetryWithDelay;
import com.netspace.crm.android.model.Contact;
import com.netspace.crm.android.utils.NetworkUtils;
import com.netspace.crm.android.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * created by Oleh Kolomiets
 */
public class LoginActivity extends BaseActivity {

    public static final String DEBUG_USER_ID = "662DD0C8-9AAE-45AF-BCA5-88CF0BBFB754";

    @Bind(R.id.loginEditText)
    protected EditText userId;
    @Bind(R.id.loginButton)
    protected Button loginButton;
    @Bind(R.id.login_activity_progress_bar)
    protected ProgressBar loginProgressBar;

    @Inject
    protected ApiService apiService;

    @OnClick(R.id.loginButton)
    public void click() {
        final String id = userId.getText().toString();
        if (isValid(id)) {
            if (NetworkUtils.checkInternetConnection(this)) {
                Log.d(tag, " Request sent");
                loginButton.setEnabled(false);
                final String authorization = "Basic " + id;
                loginProgressBar.setVisibility(View.VISIBLE);
                apiService.logIn(authorization)
                        .retryWhen(new RetryWithDelay(3, 2000))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Contact>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable error) {
                                Log.d(tag, "login failed", error);
                                if (LoginActivity.this != null) {
                                    loginProgressBar.setVisibility(View.GONE);
                                    showToast(R.string.wrong_uuid);
                                }
                                loginButton.setEnabled(true);
                            }

                            @Override
                            public void onNext(Contact contact) {
                                prefs.saveUserId(id);
                                prefs.saveUserProfile(contact);
                                loginButton.setEnabled(true);
                                loginProgressBar.setVisibility(View.GONE);
                                Log.d(tag, " Response received, name and idTextView saved");
                                startMainActivity();
                            }
                        });
            } else {
                showToast(R.string.user_offline_message);
            }
        } else {
            showToast(R.string.wrong_uuid);
        }
    }

    public static boolean isValid(String id) {
        return Pattern.matches(Utils.UUID_PATTERN, id);
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}"
                + "\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CRMApp.getComponent().inject(this);
        if (getResources().getBoolean(R.bool.mock_id)) {
            userId.setText(DEBUG_USER_ID);
        }
        if (prefs.isLoggedIn()) {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        loginButton.setEnabled(true);
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
