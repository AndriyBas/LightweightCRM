package com.netspace.crm.android.events;

/**
 *  Created by   Andrew Budu
 */
public class PageLoadedEvent {
    private final int offset;
    private final int count;
    private final boolean updateList;

    public PageLoadedEvent(int offset, int count, boolean updateList) {
        this.offset = offset;
        this.count = count;
        this.updateList = updateList;
    }

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }

    public boolean isUpdateList() {
        return updateList;
    }
}
