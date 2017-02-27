package com.example.moodmobile;

/**
 * Created by juice on 27/02/17.
 */

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;


public class TestAccount extends ActivityInstrumentationTestCase2{

    public TestAccount(Class activityClass) {
        super(activityClass);
    }

    public void testGetUsername(){

        String username = account.getUsername();

        assertNotSame(username, "notreal");
        assertEquals(username, "PokeTrainer");
    }

    public void testGetDeviceIMEI(){
        String IMEI = account.getDeviceIMEI();

        assertEquals(IMEI, "IMEIstringNUMBERS1234567890");
    }

    public void testGetProfilePhoto(){

    }

    public void testGetNickname(){

    }

    public void testGetGender(){

    }

    public void testGetRegion(){

    }

    public void testSetUsername(){
        Account account = new Account();
        account.setUsername("PokeTrainer");

        String username = account.getUsername();
        assertEquals(username, "PokeTrainer");
        assertNotSame(username, "pokeTRAINER");

        account.setUsername("");
        // Should fail to set a blank username and keep old name instead
        assertEquals(username, "PokeTrainer");
    }

    public void testSetDeviceIMEI(){
        Account account = new Account();
        account.setDeviceIMEI("IMEIstringNUMBERS1234567890");

        String IMEI = account.getDeviceIMEI();
        assertEquals(IMEI, "IMEIstringNUMBERS1234567890");
        assertNotSame(IMEI, "imeiSTRINGnumbers1234567890"); // Test for case sensitivity
    }


    public void testSetProfilePhoto(){
        /**TODO**/
    }

    public void testSetNickname(){
        Account account = new Account();
        account.setNickname("Ash Ketchum");

        String nickname = account.getNickname();
        assertEquals(nickname, "Ash Ketchum");

        account.setNickname("Brock");

        nickname = account.getNickname();
        assertEquals(nickname, "Brock");

    }

    public void testSetGender(){
        Account account = new Account();
        account.setGender("female");

        String gender = account.getGender();
        assertEquals(gender, "female");

        account.setGender("male");

        gender = account.getGender();
        assertEquals(gender, "male");

    }

    public void testSetRegion(){
        Account account = new Account();
        account.setRegion("Kanto");

        String region = account.getRegion();
        assertEquals(region, "Kanto");

        account.setRegion("Johto");

        region = account.getRegion();
        assertEquals(region, "Johto");
        assertEquals(region, "johto"); // Testing for caase sensitivity
    }

}















