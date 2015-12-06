package com.netspace.crm.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.netspace.crm.android.model.AlarmTrackingService;

/**
 *  Created by Andrew Budu
 */
public class TrackerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, AlarmTrackingService.class));
    }
}
