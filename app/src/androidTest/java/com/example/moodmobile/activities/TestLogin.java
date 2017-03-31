package com.example.moodmobile;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.app.Instrumentation;
import android.widget.Toast;


import junit.framework.TestCase;

import static com.example.moodmobile.R.menu.main;


public class TestLogin extends ActivityInstrumentationTestCase2 {
    private EditText username;
    private Button loginButton;

    public TestLogin() {
        super(com.example.moodmobile.LoginPage.class);
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
