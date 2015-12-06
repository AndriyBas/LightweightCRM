package com.netspace.crm.android.callbacks;

import android.util.Log;

import com.netspace.crm.android.config.AppDatabase;
import com.netspace.crm.android.events.TaskUploadedEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import taskDB.Task;

/**
 * created by Andrew Budu on
 */
public class TaskUploadCallback implements Callback<Task> {

    private final Task postTask;
    private final AppDatabase database;

    private final String tag = getClass().getSimpleName();

    public TaskUploadCallback(Task postTask, AppDatabase database) {
        this.postTask = postTask;
        this.database = database;
    }

    @Override
    public void success(Task task, Response response) {
        if (task != null) {
            database.insertOrUpdate(task);
        }
        Log.i(tag, "Successfully uploaded");
        EventBus.getDefault().post(new TaskUploadedEvent(task, true));
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e(tag, "upload failure", error);
        EventBus.getDefault().post(new TaskUploadedEvent(postTask, false));
    }
}
