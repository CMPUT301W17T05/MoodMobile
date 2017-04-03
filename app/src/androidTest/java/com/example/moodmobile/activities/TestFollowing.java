package com.example.moodmobile.activities;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.moodmobile.Account;
import com.example.moodmobile.FriendsActivity;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.example.moodmobile.UserProfile;
import com.robotium.solo.Solo;
/**
public class TestFollowing extends ActivityInstrumentationTestCase2 <FriendsActivity> {

    private Solo solo;

    public TestFollowing() {
        super(com.example.moodmobile.FriendsActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);
    }


    public void EditInfo(){

        solo.searchText("You are following 4 people");
        //solo.enterText((EditText) solo.getView(R.id.region), "Alberta");
        //solo.clickOnButton("SAVE");

        assertTrue(solo.searchText("You are following 4 people"));
        assertTrue(solo.waitForActivity(MainPageActivity.class));

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}
**/

