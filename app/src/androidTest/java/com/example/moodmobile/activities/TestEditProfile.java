package com.example.moodmobile.activities;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.moodmobile.Account;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.example.moodmobile.UserProfile;
import com.robotium.solo.Solo;

public class TestEditProfile extends ActivityInstrumentationTestCase2 <UserProfile> {

    private Solo solo;

    public TestEditProfile() {
        super(com.example.moodmobile.UserProfile.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }


    public void EditInfo(){

        solo.enterText((EditText) solo.getView(R.id.nickname), "Boss");
        solo.enterText((EditText) solo.getView(R.id.region), "Alberta");
        solo.clickOnButton("SAVE");

        assertTrue(solo.waitForText("Edit profile successfully"));
        assertTrue(solo.waitForActivity(MainPageActivity.class));

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}


