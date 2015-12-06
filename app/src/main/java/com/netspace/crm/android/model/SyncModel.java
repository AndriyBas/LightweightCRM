package com.netspace.crm.android.model;

import java.util.Date;
import java.util.List;

import taskDB.Task;

/**
 *  Created by Andrew Budu
 */
public class SyncModel {

    private Date lastSyncTime;
    private int modifiedCount;
    private int totalCount;
    private List<Task> result;

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public int getModifiedCount() {
        return modifiedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<Task> getResult() {
        return result;
    }
}
