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
        Mood mood = new Mood();

        mood.setType("Happy");

        String type = mood.getType();
        assertEquals(type, "Happy");

        mood.setType("Salty");

        type = mood.getType();
        assertNotSame(type, "Happy");

    }

    public void testSetSituation(){
        Mood mood = new Mood();

        mood.setSituation("In Da Club");

        String situation = mood.getSituation();
        assertEquals(situation, "In Da Club");

        mood.setSituation("");

        situation = mood.getSituation();
        assertEquals(situation, "");

    }

    public void testSetReason(){
        Mood mood = new Mood();

        mood.setReason("Caught Blaziken");
        String reason = mood.getReason();

        assertEquals(reason, "Caught Blaziken");


        mood.setReason("Long Live Valor");
        String reason = mood.getReason();

        assertEquals(reason, "Long Live Valor");
    }

    public void testSetImage(){
        Mood mood = new Mood();

        /**TODO*/
    }

    public void testSetTime(){
        Mood mood = new Mood();

        long time = System.currentTimeMillis();

        mood.setTime(time);
        assertEquals(time, mood.getTime());

    }

    public void testSetLocation(){
        Mood mood = new Mood();

        mood.setLocation("Johto");

        String location = mood.getLocation();
        assertEquals(location, "Johto");

        mood.setLocation("PokeStop");

        location = mood.getLocation();
        assertEquals(location, "PokeStop");
    }
}

