package com.example.moodmobile;

/**
 * Created by juice on 27/02/17.
 */

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;


public class TestMood extends ActivityInstrumentationTestCase2{


    public TestMood(Class activityClass) {
        super(activityClass);
    }

    public void testGetType(){

    }

    public void testGetSituation(){

    }

    public void testGetReason(){

    }

    public void testGetImage(){

    }

    public void testGetTime(){

    }

    public void testGetLocation(){

    }

    public void testSetType(){
        mood.setType("Happy");

        String type = mood.getType();
        assertEquals(type, "Happy");

        mood.setType("Salty");

        type = mood.getType();
        assertNotSame(type, "Happy");

    }

    public void testSetSituation(){
        mood.setType

    }

    public void testSetReason(){

    }

    public void testSetImage(){

    }

    public void testSetTime(){

    }

    public void testSetLocation(){

    }
}

