package com.example.moodmobile.activities;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.moodmobile.AddNewFriendActivity;
import com.example.moodmobile.LoginPage;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.example.moodmobile.ViewFriendRequests;
import com.robotium.solo.Solo;

public class TestAddFriend extends ActivityInstrumentationTestCase2 <AddNewFriendActivity> {

    private Solo solo;

    public TestAddFriend() {
        super(com.example.moodmobile.AddNewFriendActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        solo.assertCurrentActivity("Wrong Activity", AddNewFriendActivity.class);

    }

    public void testAdd() {
        solo.enterText((EditText) solo.getView(R.id.usernameSearch), "haozhou");
        solo.clickOnButton("Send a friend request");
        solo.waitForText("Following request sent to haozhou");
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


}


