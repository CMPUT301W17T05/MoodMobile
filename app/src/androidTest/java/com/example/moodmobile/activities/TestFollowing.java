package com.example.moodmobile.activities;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.moodmobile.Account;
import com.example.moodmobile.ElasticsearchAccountController;
import com.example.moodmobile.FriendsActivity;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.example.moodmobile.UserProfile;
import com.robotium.solo.Solo;

import org.junit.Before;

public class TestFollowing extends ActivityInstrumentationTestCase2 <FriendsActivity> {

    private Solo solo;

    public TestFollowing() {
        super(com.example.moodmobile.FriendsActivity.class);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra("username", "sydia");setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());

        //Activity activity = getActivity();




        //activity.startActivity(intent);
    }

    public void testEdit() {
        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);
    }


    public void testInfo(){
        ElasticsearchAccountController.GetUser getAccountTask = new ElasticsearchAccountController.GetUser();
        getAccountTask.execute("sydia");

        Account sydia;
        int size = 4;

        try {
            sydia = getAccountTask.get().get(0);
            //size = sydia.getFollowing().size();
        } catch (Exception e) {
            /**
             *
             */
        }


        //solo.searchText("You are following 6 people");
        //solo.enterText((EditText) solo.getView(R.id.region), "Alberta");
        //solo.clickOnButton("SAVE");

        assertTrue(solo.searchText("You are following 4 people"));
        //assertTrue(solo.waitForActivity(MainPageActivity.class));

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}


