package com.devhch.mirai.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/31/2020.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }


    @Test
    public void clickGridViewItem_OpensActivity() {

        onView(withId(R.id.recipes_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the StepsActivity opens with steps recycler view displayed
        onView(withId(R.id.steps_recycler_view)).check(matches(isDisplayed()));


    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }

}

