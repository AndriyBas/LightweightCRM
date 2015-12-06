package com.netspace.crm.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.netspace.crm.android.model.AlarmService;

/**
 *  Created by Andrew Budu
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("TAG", "BootReceived");
            context.startService(new Intent(context, AlarmService.class));
        }
    }
}