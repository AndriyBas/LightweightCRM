package com.netspace.crm.android.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.netspace.crm.android.api.ApiService;
import com.netspace.crm.android.api.RetryWithDelay;
import com.netspace.crm.android.config.AppDatabase;
import com.netspace.crm.android.config.AppPreferences;
import com.netspace.crm.android.events.PageLoadedEvent;
import com.netspace.crm.android.model.ResponseModel;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import taskDB.Task;


/**
 * created by Oleh Kolomiets
 */
public class TaskLoader {

    private AppDatabase database;
    private AppPreferences prefs;
    private ApiService apiService;

    private final String tag = getClass().getSimpleName();

    public TaskLoader(AppDatabase database, AppPreferences prefs, ApiService apiService) {
        this.database = database;
        this.prefs = prefs;
        this.apiService = apiService;
    }

    public List<Task> getPage(int offset, int count) {
        return database.getPageOfTasks(offset, count);
    }

    public void clearDatabase() {
        database.clearDatabase();
    }

    public List<String> getUniqueTitles() {
        return database.getAllUniqueTitles();
    }

    public void loadDataPage(final int offset, final int count) {
        //download items page if needed
        if (offset <= prefs.getTotalResult() && offset + count > database.getCount()) {
            apiService.loadTasks(count, offset)
                    .retryWhen(new RetryWithDelay(3, 2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(tag, e.toString());
                            EventBus.getDefault().post(new PageLoadedEvent(offset, count, false));
                        }

                        @Override
                        public void onNext(ResponseModel responseModel) {
                            Log.i(tag, "successfully loaded from API");
                            List<Task> tasks = responseModel.getTasks();
                            new PutTasks(offset, count).execute(tasks.toArray(new Task[tasks.size()]));
                            prefs.setLastSync(System.currentTimeMillis());
                            prefs.setTotalResult(responseModel.getTotalResult());
                        }
                    });
        } else {
            EventBus.getDefault().post(new PageLoadedEvent(offset, count, true));
        }
    }

    private class PutTasks extends AsyncTask<Task, Void, Void> {

        private final int offset;
        private final int count;

        protected PutTasks(int offset, int count) {
            this.offset = offset;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Task... params) {
            database.putItems(params);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new PageLoadedEvent(offset, count, true));
        }
    }
}
