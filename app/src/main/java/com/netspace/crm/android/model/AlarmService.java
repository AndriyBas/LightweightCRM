package com.netspace.crm.android.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateUtils;

import com.netspace.crm.android.CRMApp;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.receivers.TrackerReceiver;

import javax.inject.Inject;

/**
 * Created by Andrew Budu
 */
public class AlarmService extends Service {

    @Inject
    protected AppPreferences prefs;

    private AlarmManager manager;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        CRMApp.getComponent().inject(this);

        if (manager == null) {
            manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long startTime = prefs.getStartTime();
        if (startTime == 0) {
            stopSelf();
            return 0;
        }
        createPendingIntent();

        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                getNotifyStartTime(startTime),
                DateUtils.HOUR_IN_MILLIS,
                pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
        super.onDestroy();
    }

    private void createPendingIntent() {
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,
                    new Intent(this, TrackerReceiver.class), 0);
        }
    }

    private long getNotifyStartTime(long time) {
        return time + ((System.currentTimeMillis() - time)
                / DateUtils.HOUR_IN_MILLIS + 1) * DateUtils.HOUR_IN_MILLIS;
    }
}
