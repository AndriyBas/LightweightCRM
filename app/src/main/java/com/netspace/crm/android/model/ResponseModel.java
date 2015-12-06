package com.netspace.crm.android.model;

import java.util.List;

import taskDB.Task;

/**
 * Created by Oleh Kolomiets
 */
public class ResponseModel {

    private int offset;
    private int count;
    private int totalResult;
    private List<Task> result;

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public List<Task> getTasks() {
        return result;
    }
}
