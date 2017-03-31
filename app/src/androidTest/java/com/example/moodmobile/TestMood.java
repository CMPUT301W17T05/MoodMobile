package com.example.moodmobile;

/**
 * TODO Default File Template
 * Created by juice on 27/02/17.
 */

import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;


public class TestMood extends ActivityInstrumentationTestCase2{


    public TestMood() {
        super(Mood.class);
    }


    public void testToString() {
        Mood mood = new Mood("Happy");
        String shortMessage = "This is good";
        String noMessage = "";

        assertTrue(mood.toString() == null);

        try {
            mood.setMessage(shortMessage);
            assertTrue(mood.toString() == shortMessage);
        } catch (ReasonTooLongException e) {
            //TODO add something
        }

        try {
            mood.setMessage(noMessage);
            assertTrue(mood.toString() == noMessage);
        } catch (ReasonTooLongException e) {
            //TODO add something
        }
    }

    //@Test(expected=ReasonTooLongException.class)
    public void testReasonTooLongException(){
        Mood mood = new Mood("Happy");
        String longMessage = "This Message is way too long and should be shortened";

        assertTrue(mood.toString() == null);

        try {
            mood.setMessage(longMessage);
        } catch (ReasonTooLongException e){
            assertTrue(mood.toString() == null);
        }
    }

    public void testSetMessage(){
        Mood mood = new Mood("Happy");
        String shortMessage = "This is good";
        String noMessage = "";

        assertTrue(mood.getMessage() == null);

        try {
            mood.setMessage(shortMessage);
            assertTrue(mood.getMessage() == shortMessage);
        } catch (ReasonTooLongException e) {
            assertTrue(mood.getMessage() == null);
        }

        try {
            mood.setMessage(noMessage);
            assertTrue(mood.getMessage() == noMessage);
        } catch (ReasonTooLongException e) {
            assertTrue(mood.getMessage() == null);
        }
    }

    public void testSetDate(){
        Mood mood = new Mood("Happy");
        Date date1 = mood.getDate();
        assertTrue(date1 == mood.getDate());
        Date date2 = new Date();
        assertFalse(mood.getDate() == date2);
        mood.setDate(date2);
        assertTrue(mood.getDate() == date2);
    }

    public void testSetFeeling(){
        Mood mood1 = new Mood("Happy");
        Mood mood2 = new Mood("Sad");

        assertTrue(mood1.getFeeling() == "Happy");
        assertTrue(mood2.getFeeling() == "Sad");

        mood1.setFeeling(mood2.getFeeling());
        assertTrue(mood1.getFeeling() == mood2.getFeeling());
        assertFalse(mood1.getFeeling() == "Happy");
    }

    public void testSetSituation(){
        Mood mood = new Mood("Happy");
        assertTrue(mood.getSituation() == null);
        mood.setSituation("Alone");
        assertTrue(mood.getSituation() == "Alone");

        mood = new Mood("Happy", "Message", null, null, "Alone", null);
        assertFalse(mood.getSituation() == null);
        assertTrue(mood.getSituation() == "Alone");
        mood.setSituation("In A Crowd");
        assertFalse(mood.getSituation() == "Alone");
        assertTrue(mood.getSituation() == "In A Crowd");

    }

    public void testGetMessage(){
        Mood mood = new Mood("Happy");
        String shortMessage = "This is good";
        String noMessage = "";

        assertTrue(mood.getMessage() == null);

        try {
            mood.setMessage(shortMessage);
            assertTrue(mood.getMessage() == shortMessage);
        } catch (ReasonTooLongException e) {
            assertTrue(mood.getMessage() == null);
        }

        try {
            mood.setMessage(noMessage);
            assertTrue(mood.getMessage() == noMessage);
        } catch (ReasonTooLongException e) {
            assertTrue(mood.getMessage() == null);
        }
    }

    public void testGetDate(){
        Mood mood = new Mood("Happy");
        Date date1 = mood.getDate();
        assertTrue(date1 == mood.getDate());
        Date date2 = new Date();
        assertFalse(mood.getDate() == date2);
        mood.setDate(date2);
        assertTrue(mood.getDate() == date2);
    }

    public void testGetFeelign(){
        Mood mood1 = new Mood("Happy");
        Mood mood2 = new Mood("Sad");

        assertTrue(mood1.getFeeling() == "Happy");
        assertTrue(mood2.getFeeling() == "Sad");

        mood1.setFeeling(mood2.getFeeling());
        assertTrue(mood1.getFeeling() == mood2.getFeeling());
        assertFalse(mood1.getFeeling() == "Happy");
    }

    public void testGetSituation(){
        Mood mood = new Mood("Happy");
        assertTrue(mood.getSituation() == null);
        mood.setSituation("Alone");
        assertTrue(mood.getSituation() == "Alone");

        mood = new Mood("Happy", "Message", null, null, "Alone", null);
        assertFalse(mood.getSituation() == null);
        assertTrue(mood.getSituation() == "Alone");
        mood.setSituation("In A Crowd");
        assertFalse(mood.getSituation() == "Alone");
        assertTrue(mood.getSituation() == "In A Crowd");
    }

}



















