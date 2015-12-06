package com.netspace.crm.android.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.netspace.crm.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * created by Andrew Budu
 */

public class MainActivity extends BaseActivity {

    public static final String PARAM_INDEX = "activity_state";

    @Bind(R.id.drawer_list)
    protected ListView drawerList;
    protected DrawerAdapter adapter;
    protected ActionBarDrawerToggle drawerToggle;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.drawer_linear_layout)
    protected LinearLayout drawerLinearLayout;
    private DrawerItems drawerItem;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (prefs.isLoggedIn()) {
            if (savedInstanceState != null) {
                drawerItem = (DrawerItems) savedInstanceState.getSerializable(PARAM_INDEX);
            } else {
                if (getIntent().getBooleanExtra(DetailActivity.PARAM_IS_TASK_NEW, false)) {
                    drawerItem = DrawerItems.TASKS;
                } else {
                    drawerItem = DrawerItems.TRACKING;
                }
            }

            addToDrawer();
            setupDrawer();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            startFragment(drawerItem);
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return drawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PARAM_INDEX, drawerItem);
        super.onSaveInstanceState(outState);
    }

    private void addToDrawer() {
        adapter = new DrawerAdapter(this);
        drawerList.setAdapter(adapter);
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @OnItemClick(R.id.drawer_list)
    public void listClick(int position) {
        drawerItem = adapter.getItem(position);
        startFragment(drawerItem);
        drawerLayout.closeDrawer(drawerLinearLayout);
    }

    private void startFragment(DrawerItems drawerItem) {
        switch (drawerItem) {
            case TRACKING:
                MainFragment mainFragment = new MainFragment();
                runFragment(mainFragment);
                break;
            case PROFILE:
                ProfileFragment profileFragment = new ProfileFragment();
                runFragment(profileFragment);
                break;
            case TASKS:
                TasksListFragment tasksListFragment = new TasksListFragment();
                runFragment(tasksListFragment);
                break;
            case SETTINGS:
                SettingsFragment settingsFragment = new SettingsFragment();
                runFragment(settingsFragment);
                break;
            default:
                runFragment(new MainFragment());
        }
    }

    private void runFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment).commit();
    }

    static class DrawerAdapter extends ArrayAdapter<DrawerItems> {
        private LayoutInflater layoutInflater;


        public DrawerAdapter(Context context) {
            super(context, R.layout.drawer_list_item, DrawerItems.values());
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                view.setTag(new ItemHolder(view));
            } else {
                view = convertView;
            }
            ItemHolder holder = (ItemHolder) view.getTag();
            DrawerItems item = getItem(position);
            holder.drawerItemTitleTextView.setText(item.title);
            holder.drawerItemIconImageView.setImageResource(item.icon);
            return view;
        }

        static class ItemHolder {

            @Bind(R.id.drawer_item_icon_imageView)
            protected ImageView drawerItemIconImageView;
            @Bind(R.id.drawer_list_item_title)
            protected TextView drawerItemTitleTextView;

            public ItemHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }
    }

    public enum DrawerItems {
        TRACKING(R.string.title_main, R.drawable.ic_action_time),
        PROFILE(R.string.title_profile, R.drawable.ic_action_person),
        TASKS(R.string.title_tasks, R.drawable.ic_action_view_as_list),
        SETTINGS(R.string.title_settings, R.drawable.ic_action_settings);

        private int title;
        private int icon;

        DrawerItems(int title, int icon) {
            this.title = title;
            this.icon = icon;
        }

        public int getTitle() {
            return title;
        }
    }
}
