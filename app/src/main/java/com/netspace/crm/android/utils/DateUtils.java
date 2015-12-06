package com.netspace.crm.android.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew Budu
 */
public final class DateUtils {

    private static final String SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String DATE_PATTERN = "dd MMM";

    private static final ThreadLocal<DateFormat> SDF = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_DATE_PATTERN, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf;
        }
    };

    private static final ThreadLocal<DateFormat> SDF_TIME = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        }
    };
    private static final ThreadLocal<DateFormat> SDF_DATE = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        }
    };

    private DateUtils() {
    }

    public static String formatTime(long time) {
        return SDF_TIME.get().format(time);
    }

    public static String formatDate(long time) {
        return SDF_DATE.get().format(time);
    }

    public static String formatDateUTC(Date date) {
        return SDF.get().format(date);
    }

    public static Date parseDateUTC(String src) throws ParseException {
        return SDF.get().parse(src);
    }

    public static String formatTimerTime(long time) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }
}
