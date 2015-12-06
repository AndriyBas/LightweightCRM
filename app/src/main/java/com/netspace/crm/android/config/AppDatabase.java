package com.netspace.crm.android.config;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taskDB.DaoMaster;
import taskDB.Task;
import taskDB.TaskDao;

/**
 * created by Andrew Budu
 */
public class AppDatabase {

    private final TaskDao daoTask;

    public AppDatabase(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "appDatabase", null);
        daoTask = new DaoMaster(helper.getWritableDatabase()).newSession().getTaskDao();
    }

    public void insertOrUpdate(Task task) {
        daoTask.insertOrReplace(task);
    }

    public void clearDatabase() {
        daoTask.deleteAll();
    }

    public List<Task> getAllTasks() {
        return daoTask.queryBuilder().orderDesc(TaskDao.Properties.FinishDate).list();
    }

    public Task getByUUID(String uuid) {
        return daoTask.queryBuilder().where(TaskDao.Properties.Id.eq(uuid)).unique();
    }

    public List<String> getAllUniqueTitles() {
        List<String> unique = new ArrayList<>();
        List<Task> all = getAllTasks();
        for (Task a : all) {
            if (!unique.contains(a.getTitle())) {
                unique.add(a.getTitle());
            }
        }
        return unique;
    }

    public List<Task> getPageOfTasks(int offset, int count) {
        return daoTask.queryBuilder().orderDesc(TaskDao.Properties.CreatedOn).
                offset(offset).limit(count).list();
    }

    public List<Task> getAllUnSynced() {
        return daoTask.queryBuilder().where(TaskDao.Properties.Syncronized.eq(false)).list();
    }

    /**
     * @param responseModel Tasks that came from API
     */

    public void putItems(List<Task> responseModel) {
        for (Task item : responseModel) {
            Task dbTask = getByUUID(item.getId());
            if (dbTask != null) {
                if (dbTask.getModifiedOn().getTime() < item.getModifiedOn().getTime()) {
                    insertOrUpdate(item);
                }
            } else {
                insertOrUpdate(item);
            }
        }
    }

    public void putItems(Task... model) {
        putItems(Arrays.asList(model));
    }

    public int getCount() {
        return (int) daoTask.count();
    }
}
