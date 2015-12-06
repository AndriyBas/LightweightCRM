package com.netspace.crm.android.model;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.netspace.crm.android.R;
import com.netspace.crm.android.ui.DetailActivity;
import com.netspace.crm.android.utils.DateUtils;

import java.util.Random;

/**
 * Created by Andrew Budu
 */
public class AlarmTrackingService extends IntentService {

    private final Random random = new Random();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmTrackingService() {
        super("AlarmTrackingService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(DetailActivity.PARAM_IS_TASK_NEW, true);
        long currentTime = System.currentTimeMillis();
        i.putExtra(DetailActivity.PARAM_TASK_FINISH_DATE, currentTime);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent pendingIntent
                = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_message))
                .setContentText(DateUtils.formatTime(currentTime))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        notification.flags |= (Notification.FLAG_NO_CLEAR);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(random.nextInt(), notification);
    }
}
