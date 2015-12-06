package com.netspace.crm.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netspace.crm.android.R;
import com.netspace.crm.android.events.TaskUploadedEvent;
import com.netspace.crm.android.utils.DateUtils;
import com.netspace.crm.android.utils.NetworkUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import taskDB.Task;

/**
 * created by Oleh Kolomiets
 */
public class DetailActivity extends BaseActivity implements DateDialogFragment.UpdateDate {

    public static final String PARAM_TASK = "Task";
    public static final String PARAM_IS_TASK_NEW = "boolean";
    public static final String PARAM_PICKER = "picker";
    public static final String PARAM_TASK_FINISH_DATE = "finishDate";
    public static final int PARAM_START_DATE = 1;
    public static final int PARAM_FINISH_DATE = 2;

    @Bind(R.id.detail_activity_task_title)
    protected AutoCompleteTextView titleAutoCompleteTextView;
    @Bind(R.id.detail_activity_task_description)
    protected EditText descriptionEditText;
    @Bind(R.id.detail_activity_start_time)
    protected TextView startTimeTextView;
    @Bind(R.id.detail_activity_end_time)
    protected TextView endTimeTextView;
    @Bind(R.id.detail_activity_start_date)
    protected TextView startDateTextView;
    @Bind(R.id.detail_activity_end_date)
    protected TextView endDateTextView;
    @Bind(R.id.detail_activity_task_save_changes)
    protected Button saveChangeButton;
    @Bind(R.id.detail_activity_progress_bar)
    protected ProgressBar savingChangesProgressBar;

    private Task task;
    private int taskIndex;
    private int whichDateModify;
    private Date tempStartDate;
    private Date tempFinishDate;

    @OnClick(R.id.detail_activity_task_save_changes)
    public void click() {
        if (NetworkUtils.checkInternetConnection(this)) {
            if (!isTextFieldsEmpty(true) && checkTime()) {
                if (updateTask(titleAutoCompleteTextView.getText().toString(),
                        descriptionEditText.getText().toString(),
                        tempStartDate,
                        tempFinishDate)) {
                    saveChangeButton.setEnabled(false);
                    savingChangesProgressBar.setVisibility(View.VISIBLE);
                    if (task.getNewTask()) {
                        Log.d(tag, " POST");
                        syncManager.postTask(task);
                    } else {
                        Log.d(tag, " PUT");
                        syncManager.putTask(task);
                    }
                } else {
                    showToast(R.string.no_changes);
                }
            }
        } else {
            if (!isTextFieldsEmpty(true) && checkTime()) {
                if (updateTask(titleAutoCompleteTextView.getText().toString(),
                        descriptionEditText.getText().toString(),
                        tempStartDate,
                        tempFinishDate)) {
                    syncManager.insertTask(task);
                    Intent i = new Intent();
                    i.putExtra(PARAM_TASK, task);
                    i.putExtra(PARAM_IS_TASK_NEW, getIntent().getBooleanExtra(PARAM_IS_TASK_NEW, true));
                    i.putExtra(TasksListFragment.PARAM_INDEX, taskIndex);
                    setResult(RESULT_OK, i);
                    showToast(R.string.user_offline_message);
                } else {
                    showToast(R.string.no_changes);
                }
            }
        }
    }

    @OnClick(R.id.detail_activity_start_layout)
    public void onStartDateClick() {
        initializeDialog(tempStartDate);
        whichDateModify = PARAM_START_DATE;
    }

    @OnClick(R.id.detail_activity_end_layout)
    public void onFinishDateClick() {
        initializeDialog(tempFinishDate);
        whichDateModify = PARAM_FINISH_DATE;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().getBooleanExtra(PARAM_IS_TASK_NEW, true)) {
            task = getIntent().getParcelableExtra(PARAM_TASK);
            taskIndex = getIntent().getIntExtra(TasksListFragment.PARAM_INDEX, -1);
        } else {
            long finishDate = getIntent().getLongExtra(PARAM_TASK_FINISH_DATE, System.currentTimeMillis());
            task = new Task(UUID.randomUUID().toString());
            task.setFinishDate(new Date(finishDate));
            task.setStartDate(new Date(finishDate - android.text.format.DateUtils.HOUR_IN_MILLIS));
            task.setCreatedOn(new Date());
            task.setModifiedOn(new Date());
        }
        tempStartDate = new Date(task.getStartDate().getTime());
        tempFinishDate = new Date(task.getFinishDate().getTime());
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (!isTextFieldsEmpty(false)) {
            if (isFieldsModified()) {
                createDialog();
            } else {
                super.onBackPressed();
            }
        } else {
            createDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isTextFieldsEmpty(false)) {
                    if (isFieldsModified()) {
                        createDialog();
                    } else {
                        return super.onOptionsItemSelected(item);
                    }
                } else {
                    createDialog();
                }
                break;
            default:
                return true;
        }
        return true;
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.changes_not_saved_message).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DetailActivity.super.onBackPressed();
                    }
                }).
                setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void initializeDialog(Date date) {
        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.init(date);
        dateDialogFragment.show(getSupportFragmentManager(), PARAM_PICKER);
    }

    private boolean isTextFieldsEmpty(boolean enableToastMessages) {
        boolean emptyFields = false;
        if (TextUtils.isEmpty(titleAutoCompleteTextView.getText().toString().trim())) {
            emptyFields = true;
            if (enableToastMessages) {
                showToast(R.string.title_is_empty_message);
            }
        }

        if (TextUtils.isEmpty(descriptionEditText.getText().toString().trim())) {
            emptyFields = true;
            if (enableToastMessages) {
                showToast(R.string.description_is_empty_message);
            }
        }
        return emptyFields;
    }

    private void initView() {
        List<String> strings = taskLoader.getUniqueTitles();
        titleAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                strings));
        titleAutoCompleteTextView.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
        startTimeTextView.setText(DateUtils.formatTime(task.getStartDate().getTime()));
        endTimeTextView.setText(DateUtils.formatTime(task.getFinishDate().getTime()));
        startDateTextView.setText(DateUtils.formatDate(task.getStartDate().getTime()));
        endDateTextView.setText(DateUtils.formatDate(task.getFinishDate().getTime()));
    }

    @Override
    public void update(Date date) {
        if (whichDateModify == PARAM_START_DATE) {
            tempStartDate = date;
            startTimeTextView.setText(DateUtils.formatTime(tempStartDate.getTime()));
            startDateTextView.setText(DateUtils.formatDate(tempStartDate.getTime()));
        } else if (whichDateModify == PARAM_FINISH_DATE) {
            tempFinishDate = date;
            endTimeTextView.setText(DateUtils.formatTime(tempFinishDate.getTime()));
            endDateTextView.setText(DateUtils.formatDate(tempFinishDate.getTime()));
        }
    }

    private boolean updateTask(String title, String description, Date startDate, Date finishDate) {
        boolean result = false;
        if (TextUtils.isEmpty(task.getTitle()) || !task.getTitle().equals(title)) {
            task.setTitle(title);
            result = true;
        }
        if (TextUtils.isEmpty(task.getDescription()) || !task.getDescription().equals(description)) {
            task.setDescription(description.trim());
            result = true;
        }
        if (!task.getStartDate().equals(startDate)) {
            task.setStartDate(startDate);
            result = true;
        }
        if (!task.getFinishDate().equals(finishDate)) {
            task.setFinishDate(finishDate);
            result = true;
        }
        if (result) {
            task.setSyncronized(false);
            task.setModifiedOn(new Date());
        }
        return result;
    }

    private boolean checkTime() {
        boolean checkTime = (tempStartDate.getTime() < tempFinishDate.getTime());
        if (!checkTime) {
            showToast(R.string.incorrect_time_message);
        }
        return checkTime;
    }

    private void resultSetUp(Task task, boolean isUploaded) {
        saveChangeButton.setEnabled(true);
        savingChangesProgressBar.setVisibility(View.GONE);
        showToast((isUploaded) ? R.string.changes_saved : R.string.changes_not_saved);
        this.task = task;
        if (!getIntent().getBooleanExtra(PARAM_IS_TASK_NEW, true)) {
            Intent i = new Intent();
            i.putExtra(PARAM_TASK, task);
            i.putExtra(TasksListFragment.PARAM_INDEX, taskIndex);
            setResult(RESULT_OK, i);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(PARAM_IS_TASK_NEW, true);
            startActivity(i);
        }
        finish();
    }

    @SuppressWarnings("unused")
    public void onEvent(TaskUploadedEvent event) {
        resultSetUp(event.getTask(), event.isUploaded());
    }

    private boolean isFieldsModified() {
        return task.getTitle() == null
                || task.getDescription() == null
                || !task.getTitle().equals(titleAutoCompleteTextView.getText().toString())
                || !task.getDescription().equals(descriptionEditText.getText().toString())
                || !(task.getStartDate().toString().equals(tempStartDate.toString()))
                || !task.getFinishDate().toString().equals(tempFinishDate.toString());
    }
}
