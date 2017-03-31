package com.example.moodmobile;

/**
 * TODO Default File Template
 * Created by juice on 27/02/17.
 */

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;


public class TestAccount extends ActivityInstrumentationTestCase2{

    public TestAccount() {
        super(Account.class);
    }

    public void testGetUsername(){
        Account account = new Account();

        String username = account.getUsername();

        assertNotSame(username, "notreal");
        assertEquals(username, "PokeTrainer");
    }
    /*
    public void testGetDeviceIMEI(){
        Account account = new Account();

        account.setIMEI("IMEIstringNUMBERS1234567890");
        String IMEI = account.getDeviceIMEI();

        assertEquals(IMEI, "IMEIstringNUMBERS1234567890");
    }

    public void testSetDeviceIMEI(){
        Account account = new Account();
        account.setDeviceIMEI("IMEIstringNUMBERS1234567890");

        String IMEI = account.getDeviceIMEI();
        assertEquals(IMEI, "IMEIstringNUMBERS1234567890");
        assertNotSame(IMEI, "imeiSTRINGnumbers1234567890"); // Test for case sensitivity
    }
    */
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

    public void testCheckName(){
        Account account = new Account();

        account.setUsername("Ash");

        String name = account.getUsername();
        assertEquals(name, "Ash");

        account.setUsername("Brock");

        name = account.getUsername();
        assertEquals(name, "Brock");
    }

    public void testCreateAccount(){
        ArrayList<Account> AccountList = new ArrayList<>();

        assertEquals(AccountList.size(), 0);

        AccountList.add(new Account());

        assertEquals(AccountList.size(), 1);

    }

    public void testUpdateProfile(){
        Account account = new Account();

        String nickname = "Ash";
        String region = "Kanto";

        account.setNickname(nickname);
        account.setRegion(region);

        assertEquals(account.getNickname(), nickname);
        assertEquals(account.getRegion(), region);
    }

    public void testGetCurrentAccount(){
        ArrayList<Account> AccountList = new ArrayList<>();

        Account account = new Account();
        account.setUsername("Ash");

        AccountList.add(account);

        Account otherAccount = AccountList.get(0);
        assertSame(account.getUsername(), otherAccount.getUsername());
    }



}













