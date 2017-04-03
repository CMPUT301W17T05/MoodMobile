package com.example.moodmobile.activities;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import com.example.moodmobile.CreateAccount;
import com.example.moodmobile.R;
import com.robotium.solo.Solo;

public class TestCreateAccount extends ActivityInstrumentationTestCase2 <CreateAccount> {

    private Solo solo;

    public TestCreateAccount() {
        super(com.example.moodmobile.CreateAccount.class);
    }

    public void setUp() throws Exception{

        Intent intent = new Intent();
        intent.putExtra("username", "haozhou");setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCreate() {
        solo.assertCurrentActivity("Wrong Activity", CreateAccount.class);
        solo.enterText((EditText) solo.getView(R.id.username), "haozhou");
        solo.clickOnButton("Save Account");

        assertTrue(solo.waitForText("username already exist"));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}


