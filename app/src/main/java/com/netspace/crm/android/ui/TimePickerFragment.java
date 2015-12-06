package com.netspace.crm.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.netspace.crm.android.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * created by Oleh Kolomiets
 */
public class TimePickerFragment extends Fragment {

    @Bind(R.id.time_picker)
    protected TimePicker timePicker;

    private Calendar calendar;
    private TimePicker.OnTimeChangedListener onTimeChangedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        ButterKnife.bind(this, rootView);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(onTimeChangedListener);
    }

    public void init(Calendar calendar, DateDialogFragment dateDialogFragment) {
        this.calendar = calendar;
        onTimeChangedListener = dateDialogFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
