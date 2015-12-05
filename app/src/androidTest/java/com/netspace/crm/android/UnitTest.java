package com.netspace.crm.android;

import com.netspace.crm.android.ui.LoginActivity;
import com.netspace.crm.android.utils.DateUtils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UnitTest {

    @org.junit.Test
    public void testUUIDValidation() {
        assertTrue(LoginActivity.isValid("662DD0C8-9AAE-45AF-BCA5-88CF0BBFB754"));
    }

    @Test
    public void testEmailValidation() {
        assertTrue(LoginActivity.isValidEmailAddress("testEmail@gmail.com"));
    }

    @Test
    public void testDateUtils() {
        assertTrue(!DateUtils.formatTimerTime(2584818).equals("5 dec 2015"));
    }

}