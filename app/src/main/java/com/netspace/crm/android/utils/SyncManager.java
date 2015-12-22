package com.netspace.crm.android.utils;

import android.util.Log;

import com.netspace.crm.android.api.ApiService;
import com.netspace.crm.android.api.RetryWithDelay;
import com.netspace.crm.android.callbacks.TaskUploadCallback;
import com.netspace.crm.android.config.AppDatabase;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.events.TaskSyncedEvent;
import com.netspace.crm.android.model.SyncModel;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import taskDB.Task;

/**
 * Created  by Oleh Kolomiets
 */
public class SyncManager {

    private final AppPreferences prefs;
    private final AppDatabase database;
    private final ApiService apiService;

    private final String tag = getClass().getSimpleName();

    public SyncManager(AppDatabase database, ApiService service, AppPreferences prefs) {
        this.database = database;
        this.apiService = service;
        this.prefs = prefs;
    }

    public void syncData() {
        long syncTime = prefs.getLastSync();
        //Upload unSynchronised tasks
        uploadUnSyncedTasks(database.getAllUnSynced());
        //Synchronize api and database, load new task
        if (syncTime != 0) {
            apiService.syncTask(DateUtils.formatDateUTC(new Date(syncTime)))
                    .retryWhen(new RetryWithDelay(3, 2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SyncModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(tag, "", e);
                            EventBus.getDefault().post(new TaskSyncedEvent(false));
                        }

                        @Override
                        public void onNext(SyncModel syncModel) {
                            Log.i(tag, "sync completed");
                            prefs.setTotalResult(syncModel.getTotalCount());
                            if (syncModel.getModifiedCount() != 0) {
                                database.putItems(syncModel.getResult());
                                EventBus.getDefault().post(new TaskSyncedEvent(true));
                            } else {
                                EventBus.getDefault().post(new TaskSyncedEvent(false));
                            }
                            prefs.setLastSync(System.currentTimeMillis());
                        }
                    });
        }
    }

    public void uploadUnSyncedTasks(List<Task> unSynced) {
        if (!unSynced.isEmpty()) {
            for (Task unSyncedTask : unSynced) {
                if (unSyncedTask.getNewTask()) {
                    apiService.postTask(unSyncedTask)
                            .retryWhen(new RetryWithDelay(3, 2000))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new TaskUploadCallback(unSyncedTask, database));
                } else {
                    apiService.updateTask(unSyncedTask)
                            .retryWhen(new RetryWithDelay(3, 2000))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new TaskUploadCallback(unSyncedTask, database));
                }
            }
        }
    }

    public void insertTask(Task task) {
        database.insertOrUpdate(task);
    }

    public void postTask(final Task postTask) {
        insertTask(postTask);
        apiService.postTask(postTask)
                .retryWhen(new RetryWithDelay(3, 2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TaskUploadCallback(postTask, database));
    }

    public void putTask(Task postTask) {
        insertTask(postTask);
        apiService.updateTask(postTask)
                .retryWhen(new RetryWithDelay(3, 2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TaskUploadCallback(postTask, database));
    }
}
