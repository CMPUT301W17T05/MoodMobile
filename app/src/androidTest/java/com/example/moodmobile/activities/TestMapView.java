package com.example.moodmobile.activities;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.example.moodmobile.MapViewActivity;
import com.robotium.solo.Solo;

public class TestMapView extends ActivityInstrumentationTestCase2 <MapViewActivity> {

    private Solo solo;

    public TestMapView() {
        super(MapViewActivity.class);
    }

    public void setUp() throws Exception{
        Intent intent = new Intent();
        intent.putExtra("username", "haozhou");setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testButton(){
        solo.clickOnButton("MyMood");
        solo.clickOnButton("Following");
        solo.clickOnButton("People Nearby");
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}



