package com.netspace.crm.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.netspace.crm.android.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * created by Oleh Kolomiets
 */
public class DatePickerFragment extends Fragment {

    @Bind(R.id.date_picker)
    protected DatePicker datePicker;

    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        ButterKnife.bind(this, rootView);
        datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void init(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
