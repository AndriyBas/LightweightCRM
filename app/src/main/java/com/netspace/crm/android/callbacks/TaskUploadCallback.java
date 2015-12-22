package com.netspace.crm.android.callbacks;

import android.util.Log;

import com.netspace.crm.android.config.AppDatabase;
import com.netspace.crm.android.events.TaskUploadedEvent;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
import taskDB.Task;

/**
 * created by Andrew Budu on
 */
public class TaskUploadCallback extends Subscriber<Task> {

    private final Task postTask;
    private final AppDatabase database;

    private final String tag = getClass().getSimpleName();

    public TaskUploadCallback(Task postTask, AppDatabase database) {
        this.postTask = postTask;
        this.database = database;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(tag, "upload failure", e);
        EventBus.getDefault().post(new TaskUploadedEvent(postTask, false));
    }

    @Override
    public void onNext(Task task) {
        if (task != null) {
            database.insertOrUpdate(task);
        }
        Log.i(tag, "Successfully uploaded");
        EventBus.getDefault().post(new TaskUploadedEvent(task, true));
    }
}
