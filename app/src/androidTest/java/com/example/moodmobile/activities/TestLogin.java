package com.example.moodmobile.activities;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import com.example.moodmobile.LoginPage;
import com.example.moodmobile.MainPageActivity;
import com.example.moodmobile.R;
import com.robotium.solo.Solo;

public class TestLogin extends ActivityInstrumentationTestCase2 <LoginPage> {

    private Solo solo;

    public TestLogin() {
        super(com.example.moodmobile.LoginPage.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testLogin() {
        solo.assertCurrentActivity("Wrong Activity", LoginPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "haozhou");
        solo.clickOnButton("Login");

        assertTrue(solo.waitForActivity(MainPageActivity.class));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


}


