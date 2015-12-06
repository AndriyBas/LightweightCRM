package com.netspace.crm.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.netspace.crm.android.R;
import com.netspace.crm.android.model.AlarmService;
import com.netspace.crm.android.receivers.TrackerReceiver;
import com.netspace.crm.android.utils.DateUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * created by Andrew Budu
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.start_time_textView)
    protected TextView startTimeTextView;
    @Bind(R.id.main_fragment_imageButton)
    protected ImageButton imageButton;
    @Bind(R.id.start_stop_textView)
    protected TextView startStopTextView;
    @Bind(R.id.main_fragment_chronometer)
    protected Chronometer workedTimer;

    private boolean workStarted;
    private long startTime;

    private Animation rotateButtonAnimation;

    @Override
    protected int getContentView() {
        return R.layout.main_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workedTimer.setOnChronometerTickListener(new TickListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        startTime = prefs.getStartTime();
        workStarted = (startTime != 0);
        setUpFragment();
        rotateButtonAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getResources().getBoolean(R.bool.add_menu)) {
            inflater.inflate(R.menu.main_fragment_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                makeOneNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.main_fragment_imageButton)
    public void imageButtonClick() {
        if (!workStarted) {
            imageButton.startAnimation(rotateButtonAnimation);
            workStarted = true;
            startTime = System.currentTimeMillis();
            prefs.saveStartTime(startTime);
            getActivity().startService(new Intent(getActivity(), AlarmService.class));
            setUpFragment();
        } else {
            workStarted = false;
            startTime = 0;
            prefs.saveStartTime(startTime);
            getActivity().stopService(new Intent(getActivity(), AlarmService.class));
            Intent i = new Intent(getActivity(), DetailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(DetailActivity.PARAM_IS_TASK_NEW, true);
            startActivity(i);
        }
    }

    private void makeOneNotification() {
        getActivity().sendBroadcast(new Intent(getActivity(), TrackerReceiver.class));
    }

    private void setUpFragment() {
        if (!workStarted) {
            imageButton.setImageResource(R.drawable.start_button_normal);
            startStopTextView.setText(R.string.start_tracking);
            startTimeTextView.setText(" ");
            workedTimer.setVisibility(View.INVISIBLE);
            workedTimer.stop();
        } else {
            workedTimer.setBase(startTime);
            workedTimer.setVisibility(View.VISIBLE);
            workedTimer.start();
            imageButton.setImageResource(R.drawable.stop_button_normal);
            startStopTextView.setText(R.string.stop_tracking);
            startTimeTextView.setText(getString(R.string.start_time,
                    DateUtils.formatTime(startTime)));
        }
    }

    private class TickListener implements Chronometer.OnChronometerTickListener {

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            workedTimer.setFormat(getString(R.string.worked_time,
                    DateUtils.formatTimerTime((System.currentTimeMillis() - chronometer.getBase()))));
        }
    }
}
