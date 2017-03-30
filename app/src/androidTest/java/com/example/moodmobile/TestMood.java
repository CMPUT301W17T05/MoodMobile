package com.example.moodmobile;

/**
 * TODO Default File Template
 * Created by juice on 27/02/17.
 */

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import java.util.ArrayList;


public class TestMood extends ActivityInstrumentationTestCase2{


    public TestMood(Class activityClass) {
        super(activityClass);
    }

    public void testToString(){
        Mood mood = new Mood("Happy");
        String message1 = "This is good";
        String message2 = "";
        String message3 = "This message is way too long and should throw some type of exception";

        //TODO iterate over messages

    }

    public void testSetMessage(){}
    public void testSetDate(){}
    public void testSetFeeling(){

    }
    public void testSetSituation(){

    }
    public void testGetMessage(){}
    public void testGetDate(){}
    public void testGetFeelign(){}
    public void testGetSituation(){}
    public void testGetMoodImage(){}



    /*public void testSetType(){
        Mood mood = new Mood();

        mood.setFeeling("Happy");

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

    public void testGetCurrentMood(){
        Mood mood = new Mood();

        Mood.updateCurrentMood("Happy");
        String currentMood = Mood.getCurrentMood();
        assertEquals(currentMood, "Happy");

        Mood.updateCurrentMood("Salty");
        currentMood = Mood.getCurrentMood();
        assertEquals(currentMood, "Salty");
    }

    public void testUpdateMood(){
        Moodlist moodlist = new Moodlist();
        Mood mood = new Mood();

        mood.setMood("Happy");
        mood.setReason("Finished Assignment");

        moodlist.add(mood);

        Mood newmood = moodlist.get(0);

        mood.setMood("Sad");
        mood.setReason("Out of money");

        assertNotSame(newmood.getMood(), "Sad");
        assertNotSame(newmood.getReason(), "Out of money");

        moodlist.updateMood();

        newmood = moodlist.get(0);
        assertSame(newmood.getMood(), "Sad");
        assertSame(newmood.getReason(), "Out of money");
    }

    public void testPostMood(){
        Moodlist moodlist = new Moodlist();

        Mood mood = new Mood();

        mood.setMood("Happy");

        assertEquals(moodlist.size(), 0);
        assertTrue(moodlist.postMood(mood)); //Should return true of posted successfully4
        assertTrue(moodlist.size(), 1);


    }*/
}



















