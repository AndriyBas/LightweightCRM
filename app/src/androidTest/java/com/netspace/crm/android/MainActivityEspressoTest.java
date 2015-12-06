package com.netspace.crm.android;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
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
import static android.support.test.espresso.action.ViewActions.pressKey;
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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.view.KeyEvent.KEYCODE_FORWARD_DEL;
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
 * created by Lightweight
 */
public class MainActivityEspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public final String tag = getClass().getSimpleName();

    public MainActivityEspressoTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void testDrawerOpen() {
        login();
        Log.i(tag, "testDrawerOpen has started");
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        openDrawer(R.id.drawer_layout);
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        logout();
    }

    public void testDrawerItems() {
        login();
        Log.i(tag, "testDrawerItems has started");
        onView(withId(R.id.start_stop_textView)).check(matches(isDisplayed()));
        openDrawerItem(PROFILE);
        onView(withId(R.id.user_name)).perform(click()).check(matches(withText(startsWith("User"))));
        openDrawerItem(TRACKING);
        onView(withId(R.id.main_fragment_imageButton)).check(matches(isDisplayed()));
        openDrawerItem(TASKS);
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        logout();
    }

    private void openDrawerItem(DrawerItems item) {
        openDrawer(R.id.drawer_layout);
        onData(allOf(is(instanceOf(DrawerItems.class)), is(item))).inAdapterView(withId(R.id.drawer_list)).perform(click());
    }


    public void login() {
        onView(withId(R.id.loginEditText)).perform(clearText());
        onView(withId(R.id.loginEditText)).perform(typeText("662DD0C8-9AAE-45AF-BCA5-88CF0BBFB754"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.start_stop_textView)).check(matches(isDisplayed()));
    }

}

