package com.netspace.crm.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.netspace.crm.android.R;
import com.netspace.crm.android.ui.views.WrappingViewPager;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Oleh Kolomiets
 */
public class DateDialogFragment extends DialogFragment implements TimePicker.OnTimeChangedListener {

    public static final int TIME_PICKER_ID = 0;
    public static final int DATE_PICKER_ID = 1;
    public static final int PAGE_COUNT = 2;

    @Bind(R.id.view_pager)
    protected WrappingViewPager viewPager;
    @Bind(R.id.dialog_ok_btn)
    protected Button okButton;
    @Bind(R.id.dialog_cancel_btn)
    protected Button cancelButton;
    @Bind(R.id.dialog_title)
    protected TextView dialogTitleTV;

    private Date date;
    private Calendar calendar;
    private UpdateDate updateDate;
    private TimePickerFragment timePagerFragment;
    private DatePickerFragment datePagerFragment;

    @OnClick(R.id.dialog_ok_btn)
    public void onOkButtonClick() {
        this.dismiss();
        collectData();
        updateDate.update(calendar.getTime());
    }

    @OnClick(R.id.dialog_cancel_btn)
    public void onCancelButtonClick() {
        this.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_dialog_fragment, container, false);
        ButterKnife.bind(this, rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        calendar = Calendar.getInstance(DateDialogFragment.this.getResources().getConfiguration().locale);
        calendar.setTime(date);
        viewPager.setAdapter(new DatePagerAdapter(getChildFragmentManager()));
        dialogTitleTV.setText(R.string.dialog_set_time_title);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        updateDate = (UpdateDate) activity;
    }

    private void collectData() {
        calendar.set(
                datePagerFragment.datePicker.getYear(),
                datePagerFragment.datePicker.getMonth(),
                datePagerFragment.datePicker.getDayOfMonth(),
                timePagerFragment.timePicker.getCurrentHour(),
                timePagerFragment.timePicker.getCurrentMinute()
        );
    }

    public void init(Date date) {
        this.date = date;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Handler handler = new Handler();
        if (calendar.get(Calendar.MINUTE) != minute) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(DATE_PICKER_ID);
                    dialogTitleTV.setText(R.string.dialog_set_date_title);
                }
            }, 600);
        }
    }

    class DatePagerAdapter extends FragmentPagerAdapter {

        public DatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == TIME_PICKER_ID) {
                timePagerFragment = new TimePickerFragment();
                timePagerFragment.init(calendar, DateDialogFragment.this);
                return timePagerFragment;
            } else {
                datePagerFragment = new DatePickerFragment();
                datePagerFragment.init(calendar);
                return datePagerFragment;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    interface UpdateDate {
        void update(Date date);
    }
}
