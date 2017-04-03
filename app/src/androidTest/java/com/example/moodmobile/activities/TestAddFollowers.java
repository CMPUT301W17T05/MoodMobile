package com.example.moodmobile.activities;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.moodmobile.Account;
import com.example.moodmobile.AddNewFriendActivity;
import com.example.moodmobile.ElasticsearchAccountController;
import com.example.moodmobile.FriendsActivity;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.example.moodmobile.UserProfile;
import com.robotium.solo.Solo;

import org.junit.Before;

public class TestAddFollowers extends ActivityInstrumentationTestCase2 <AddNewFriendActivity> {

    private Solo solo;

    public TestAddFollowers() {
        super(com.example.moodmobile.AddNewFriendActivity.class);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra("username", "sydia");setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());

    }

    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", AddNewFriendActivity.class);
    }


    public void testInfo(){

        solo.getButton("Send a friend request");

        solo.enterText((EditText) solo.getView(R.id.usernameSearch), "zindi");
        solo.clickOnButton("Send a friend request");

        assertTrue(solo.searchText("Following request sent to zindi."));

        solo.clearEditText((EditText) solo.getView(R.id.usernameSearch));

        solo.enterText((EditText) solo.getView(R.id.usernameSearch), "notexist");
        solo.clickOnButton("Send a friend request");

        assertTrue(solo.searchText("User does not exist!"));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}


