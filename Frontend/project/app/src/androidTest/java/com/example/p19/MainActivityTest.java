package com.example.p19;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

//    @Test
//    public void listGoesOverTheFold() {
//
//
//        Espresso.onView(withText("Hello world!")).check(matches(isDisplayed()));
//    }

    @Test
    public void useAppContext() {
        Context con = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.p19", ((Context) con).getPackageName());
    }
}