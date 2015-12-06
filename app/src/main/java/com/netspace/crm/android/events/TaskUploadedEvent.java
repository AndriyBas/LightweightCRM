package com.netspace.crm.android.events;

import taskDB.Task;

/**
 * Created by   Andrew Budu
 */
public class TaskUploadedEvent {
    private final Task task;
    private final boolean uploaded;

    public TaskUploadedEvent(Task task, boolean uploaded) {
        this.task = task;
        this.uploaded = uploaded;
    }

    public Task getTask() {
        return task;
    }

    public boolean isUploaded() {
        return uploaded;
    }
}
