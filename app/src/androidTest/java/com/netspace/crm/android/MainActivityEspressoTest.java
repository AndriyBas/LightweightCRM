package com.netspace.crm.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.netspace.crm.android.ui.MainActivity;
import com.netspace.crm.android.ui.views.WrappingViewPager;

import taskDB.Task;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.netspace.crm.android.ui.MainActivity.DrawerItems;
import static com.netspace.crm.android.ui.MainActivity.DrawerItems.PROFILE;
import static com.netspace.crm.android.ui.MainActivity.DrawerItems.SETTINGS;
import static com.netspace.crm.android.ui.MainActivity.DrawerItems.TASKS;
import static com.netspace.crm.android.ui.MainActivity.DrawerItems.TRACKING;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * created by axel95usa on 09.07.15.
 */
public class MainActivityEspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public final String tag = getClass().getSimpleName();
    private SharedPreferences prefs;

    public MainActivityEspressoTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        prefs = PreferenceManager.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        if (!isLogedIn()) {
            login();
        }
    }

    public void testDrawerOpen() {
        Log.d(tag, "Prefs after login :" + prefs.getString("userId", "-1"));
        Log.i(tag, "testDrawerOpen has started");
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        myWait(1000);
        openDrawer(R.id.drawer_layout);
        myWait(1000);
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        myWait(1000);
        closeDrawer(R.id.drawer_layout);
        myWait(2000);
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        myWait(1000);
    }

    public void testDrawerItems() {
        Log.i(tag, "testDrawerItems has started");

        onView(withId(R.id.start_stop_textView)).check(matches(isDisplayed()));
        myWait(1000);
        openDrawerItem(PROFILE);
        myWait(1000);
        onView(withId(R.id.user_name)).perform(click()).check(matches(withText(startsWith("User"))));
        myWait(1000);
        openDrawerItem(TRACKING);
        myWait(1000);
        onView(withId(R.id.main_fragment_imageButton)).check(matches(isDisplayed()));
        myWait(1000);
        openDrawerItem(TASKS);
        myWait(1000);
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        myWait(1000);
        logout();
    }

    private void openDrawerItem(DrawerItems item) {
        openDrawer(R.id.drawer_layout);
        myWait(1000);
        onData(allOf(is(instanceOf(DrawerItems.class)), is(item))).inAdapterView(withId(R.id.drawer_list)).perform(click());
    }

    public void testUpdateTask() {
        Log.i(tag, "testUpdateTask has started");

        openDrawerItem(TASKS);
        myWait(1000);
        onData(allOf(is(instanceOf(Task.class)))).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
        myWait(1000);

        onView(withId(R.id.detail_activity_task_title)).perform(typeText("+"));
        myWait(1000);
        onView(withId(R.id.detail_activity_task_description)).perform(typeText("-"));
        myWait(1000);
        onView(withId(R.id.detail_activity_start_layout)).perform(click());
        myWait(1000);
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(setTime(10, 15));
        myWait(1000);
        onView(allOf(withClassName(equalTo(WrappingViewPager.class.getName())),
                withId(R.id.view_pager))).perform(swipeLeft());
        myWait(1000);
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(setDate(2015, 7, 30));
        myWait(1000);
        onView(withId(R.id.dialog_ok_btn)).perform(click());
        myWait(1000);

        onView(withId(R.id.detail_activity_end_layout)).perform(click());
        myWait(1000);
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(setTime(12, 0));
        myWait(1000);
        onView(allOf(withClassName(equalTo(WrappingViewPager.class.getName())),
                withId(R.id.view_pager))).perform(swipeLeft());
        myWait(1000);
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(setDate(2015, 7, 29));
        myWait(1000);
        onView(withId(R.id.dialog_ok_btn)).perform(click());
        myWait(3000);
        onView(withId(R.id.detail_activity_task_save_changes)).perform(click());
        myWait(1000);
        pressBack();
        myWait(1000);
        onView(withText(R.string.changes_not_saved_message)).inRoot(isDialog()).check(matches(isDisplayed()));
        myWait(1000);
        onView(withText(R.string.cancel)).perform(click());
        myWait(1000);

        onView(withId(R.id.detail_activity_end_layout)).perform(click());
        myWait(1000);
        onView(allOf(withClassName(equalTo(WrappingViewPager.class.getName())),
                withId(R.id.view_pager))).perform(swipeLeft());
        myWait(1000);
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(setDate(2015, 7, 30));
        myWait(1000);
        onView(withId(R.id.dialog_ok_btn)).perform(click());
        myWait(1000);

//        onView(withId(R.id.detail_activity_task_save_changes)).perform(click());
//        myWait(1000);

//        onData(allOf(is(instanceOf(Task.class)))).inAdapterView(withId(R.id.list)).
//                atPosition(0).perform(click());
//        onView(withId(R.id.detail_activity_task_description)).perform(pressKey(KEYCODE_FORWARD_DEL));
//        onView(withId(R.id.detail_activity_task_save_changes)).perform(click());

    }

    public void testMainFragment() {
        Log.i(tag, "testMainFragment has started");

        onView(withId(R.id.start_stop_textView)).check(matches(withText(R.string.start_tracking)));
        myWait(1000);
        onView(withId(R.id.main_fragment_imageButton)).perform(click());
        myWait(1000);
        onView(withId(R.id.start_stop_textView)).check(matches(withText(R.string.stop_tracking)));
        myWait(1000);
        onView(withId(R.id.start_time_textView)).check(matches(isDisplayed()));
        myWait(1000);
        onView(withId(R.id.main_fragment_chronometer)).check(matches(isDisplayed()));
        myWait(1000);
        onView(withId(R.id.main_fragment_imageButton)).perform(click());
        myWait(1000);
        onView(withId(R.id.detail_activity_task_title)).perform(typeText("Task Title"));
        myWait(1000);
        onView(withId(R.id.detail_activity_task_description)).perform(typeText("Task Description"));
        myWait(1000);
//        onView(withId(R.id.detail_activity_task_save_changes)).perform(click());
//        myWait(1000);
//        onData(instanceOf(Task.class)).inAdapterView(withId(R.id.list)).atPosition(0)
//                .check(matches(hasDescendant(withText("Task Title"))));
//        myWait(1000);
    }

    public void testSettings() {
        Log.i(tag, "testSettings has started");
        openDrawerItem(SETTINGS);
        myWait(1000);
        onView(withText(R.string.pref_title_load_per_page)).perform(click());
        myWait(1000);
        onView(withText("50 per page")).perform(click());
        myWait(1000);
        openDrawerItem(TASKS);
        myWait(1000);
    }

    public void login() {
        onView(withId(R.id.loginEditText)).perform(clearText());
        myWait(1000);
        onView(withId(R.id.loginEditText)).perform(typeText("662DD0C8-9AAE-45AF-BCA5-88CF0BBFB754"));
        Espresso.closeSoftKeyboard();
        myWait(2000);
        onView(withId(R.id.loginButton)).perform(click());
        myWait(1000);
        onView(withId(R.id.start_stop_textView)).check(matches(isDisplayed()));
    }

    public void logout() {
        openDrawerItem(SETTINGS);
        myWait(1000);
        onView(withText(R.string.profile_logout)).perform(click());
        myWait(1000);
//        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
//        myWait(1000);
    }

    public boolean isLogedIn() {
        return !TextUtils.isEmpty(prefs.getString("userId", ""));
    }

    public void myWait(long milis) {
        synchronized (this) {
            try {
                wait(milis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

