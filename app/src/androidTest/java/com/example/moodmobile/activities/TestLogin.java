package com.example.moodmobile.activities;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.moodmobile.LoginPage;


public class TestLogin extends ActivityInstrumentationTestCase2 {
    private EditText username;
    private Button loginButton;

    public TestLogin() {
        super(LoginPage.class);
    }
    
    public void testStart() throws Exception{
        Activity activity = getActivity();
    }
/*
    public void testLogin() {
        LogInPage main = (LoginPage) getActivity();

        main.runOnUiThread()
    }
*/
}
