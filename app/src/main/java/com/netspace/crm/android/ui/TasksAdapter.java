package com.netspace.crm.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netspace.crm.android.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import taskDB.Task;

/**
 * created by Oleh Kolomiets
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    private final List<Task> tasks;
    private LayoutInflater inflater;

    public TasksAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
        this.tasks = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Task t = tasks.get(position);
        holder.titleTextView.setText(t.getTitle());
        holder.descriptionTextView.setText(t.getDescription());
        holder.syncImage.setImageResource(t.getSyncronized() ? R.drawable.ic_action_accept
                : R.drawable.ic_action_refresh);

        return view;
    }

    public void replace(int pos, Task task) {
        tasks.set(pos, task);
    }

    public void add(Task task) {
        tasks.add(0, task);
    }

    static class ViewHolder {

        @Bind(R.id.item_task_title)
        protected TextView titleTextView;
        @Bind(R.id.item_task_description)
        protected TextView descriptionTextView;
        @Bind(R.id.sync_task_imageView)
        protected ImageView syncImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
