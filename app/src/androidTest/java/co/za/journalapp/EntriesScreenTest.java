/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.za.journalapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith

import co.za.journalapp.features.viewAllEntries.EntryListActivity;
import co.za.journalapp.features.viewAllEntries.EntryListViewModel;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;

/**
 * Tests for the entries screen, the main screen which contains a list of all tasks.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntriesScreenTest {

    private final static String DATE = "DATE";

    private final static String TIME = "TIME";

    private final static String ENTRY = "ENTRY";
    private final static String EMAIL = "EMAIL";

    private final static String ENTRY2 = "ENTRY2";



    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<EntryListActivity> mEntriesActivityTestRule =
            new ActivityTestRule<>(EntryListActivity.class);

     @Before
    public void setupInfoRepo(){
         entryListViewModel = mock(EntryListViewModel.class);
    }

    private EntryListViewModel entryListViewModel;



    @Test
    public void clickAddEntryButton_() {
        // Click on the add entry button
        onView(withId(R.id.fab)).perform(click());

        // Check if the add entry screen is displayed
        onView(withId(R.id.et_entry)).check(matches(isDisplayed()));
    }



    @Test
    public void addTask() throws Exception {

        //  add an entry
        createEntry(DATE, TIME,ENTRY, EMAIL);

    }


    private void createEntry(String date, String time, String entry, String email) {
        // Click on the add task button
        onView(withId(R.id.fab)).perform(click());

        // Add task title and description
        onView(withId(R.id.et_entry)).perform(typeText(entry),
                closeSoftKeyboard()); // Type new task titl

        // Save the task
        onView(withId(R.id.btn_post)).perform(click());
    }

}
