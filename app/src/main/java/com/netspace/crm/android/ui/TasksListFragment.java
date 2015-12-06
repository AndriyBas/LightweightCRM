package com.netspace.crm.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.netspace.crm.android.R;
import com.netspace.crm.android.events.PageLoadedEvent;
import com.netspace.crm.android.events.TaskSyncedEvent;
import com.netspace.crm.android.events.TaskUploadedEvent;
import com.netspace.crm.android.utils.NetworkUtils;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import taskDB.Task;

/**
 * created by Oleh Kolomiets
 */
public class TasksListFragment extends BaseFragment {

    public static final int REQUEST_INDEX = 1;
    public static final String PARAM_INDEX = "Index";

    @Bind(R.id.swiped_task_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.list)
    protected ListView tasksListView;
    @Bind(R.id.empty)
    protected ProgressBar emptyProgressBar;
    protected ProgressBar loadDataPB;

    private int totalTaskCount;
    private boolean isLoadingFinished = true;
    private TasksAdapter listAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_tasks_list;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View footerView = getActivity().getLayoutInflater().inflate(R.layout.list_footer, null, false);
        loadDataPB = (ProgressBar) footerView.findViewById(R.id.progress_bar);
        totalTaskCount = prefs.getTotalResult();
        List<Task> temp = taskLoader.getPage(0, prefs.getItemsPerPage());
        listAdapter = new TasksAdapter(this.getActivity(), R.layout.list_item, temp);
        tasksListView.addFooterView(footerView);
        tasksListView.setAdapter(listAdapter);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(DetailActivity.PARAM_TASK, listAdapter.getItem(position));
                i.putExtra(PARAM_INDEX, position);
                i.putExtra(DetailActivity.PARAM_IS_TASK_NEW, false);
                startActivityForResult(i, REQUEST_INDEX);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        tasksListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int currentVisibleItemCount;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d(tag, " onScrollChanged");
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d(tag, " " + firstVisibleItem);
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE
                        && totalTaskCount >= totalItem - tasksListView.getFooterViewsCount()
                        && isLoadingFinished
                        && totalItem - tasksListView.getFooterViewsCount() != prefs.getTotalResult()) {
                    loadData(totalItem - tasksListView.getFooterViewsCount());
                    Log.d(tag, " " + "load data");
                }
            }
        });
        tasksListView.setEmptyView(emptyProgressBar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INDEX && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.d(tag, " OnActivity result Intent is null");
                return;
            }
            Task task = data.getParcelableExtra(DetailActivity.PARAM_TASK);
            int pos = data.getIntExtra(PARAM_INDEX, -1);
            listAdapter.replace(pos, task);
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        swipeRefreshLayout.setEnabled(true);
        EventBus.getDefault().register(this);
        int itemsPerPage = prefs.getItemsPerPage();
        if (listAdapter.getCount() < itemsPerPage || totalTaskCount == 0) {
            loadData(0, itemsPerPage);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.destroyDrawingCache();
        swipeRefreshLayout.clearAnimation();
        EventBus.getDefault().unregister(this);
    }

    private void loadData(int offset) {
        loadData(offset, prefs.getItemsPerPage());
    }

    public void loadData(int offset, int count) {
        if (NetworkUtils.checkInternetConnection(getActivity())) {
            if (!swipeRefreshLayout.isRefreshing()) {
                loadDataPB.setVisibility(View.VISIBLE);

            }
            isLoadingFinished = false;
            taskLoader.loadDataPage(offset, count);
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            getBaseActivity().showToast(R.string.user_offline_message);
            isLoadingFinished = true;
            updateAdapter(offset, count, true);
        }
    }

    public void updateAdapter(int offset, int count, boolean updateList) {
        if (updateList) {
            totalTaskCount = prefs.getTotalResult();
            new GetItems(offset, count).execute();
        }
        isLoadingFinished = true;
        loadDataPB.setVisibility(View.GONE);
        listAdapter.notifyDataSetChanged();
        Log.d(tag, "load tasks offset: " + offset + "count: " + count);
        Log.d(tag, "updateAdapter: adapter size: " + listAdapter.getCount() + "total count: " + prefs.getTotalResult());
    }

    public void rebuildAdapter(boolean isSynced) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.clearAnimation();
        swipeRefreshLayout.destroyDrawingCache();
        isLoadingFinished = true;
        if (isSynced) {
            totalTaskCount = prefs.getTotalResult();
            int currentCount = listAdapter.getCount();
            new GetItems(0, currentCount).execute();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(PageLoadedEvent event) {
        updateAdapter(event.getOffset(), event.getCount(), event.isUpdateList());
    }

    @SuppressWarnings("unused")
    public void onEvent(TaskUploadedEvent event) {
        rebuildAdapter(event.isUploaded());
    }

    @SuppressLint("unused")
    public void onEvent(TaskSyncedEvent event) {
        rebuildAdapter(event.isSynced());
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            if (NetworkUtils.checkInternetConnection(getActivity())) {
                swipeRefreshLayout.setRefreshing(true);
                syncManager.syncData();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.clearAnimation();
                swipeRefreshLayout.destroyDrawingCache();
                getBaseActivity().showToast(R.string.user_offline_message);
            }
        }
    }

    private class GetItems extends AsyncTask<Void, Void, List<Task>> {

        private final int offset;
        private final int count;

        protected GetItems(int offset, int count) {
            this.offset = offset;
            this.count = count;
        }

        @Override
        protected List<Task> doInBackground(Void... params) {
            return taskLoader.getPage(offset, count);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);

            if (offset == 0) {
                listAdapter.clear();
            }
            listAdapter.addAll(tasks);
            listAdapter.notifyDataSetChanged();
        }
    }
}
