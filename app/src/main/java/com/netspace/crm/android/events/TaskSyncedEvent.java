package com.netspace.crm.android.events;

/**
 * Created by   Andrew Budu
 */
public class TaskSyncedEvent {

    private final boolean synced;

    public TaskSyncedEvent(boolean synced) {
        this.synced = synced;
    }

    public boolean isSynced() {
        return synced;
    }
}
