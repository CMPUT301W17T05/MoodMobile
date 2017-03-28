package com.example.moodmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;

/**
 * Created by juice on 27/03/17.
 */

public class ViewGuestProfile extends AppCompatActivity {
    //private TextView guestProfileName;
   // private TextView guestLatestMoodTextview;
    //private TextView guestLatestLocationTextview;
    private Account guestUserAccount;
    private Mood LatestMood;

    private Button guestDenyRequest;
    private Button guestAcceptRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_profile);

        TextView guestProfileName = (TextView) findViewById(R.id.GuestProfileName);
        TextView guestLatestMoodTextview = (TextView) findViewById(R.id.GuestLatestMoodTextview);
        TextView guestLatestLocationTextview = (TextView) findViewById(R.id.GuestLatestLocationTextview);

        guestAcceptRequest = (Button) findViewById(R.id.GuestAcceptRequest);
        guestDenyRequest = (Button) findViewById(R.id.GuestDenyRequest);

        final String guestUsername = getIntent().getStringExtra("guestUsername");

        ElasticsearchAccountController.GetUser getUserActivity = new ElasticsearchAccountController.GetUser();
        ElasticsearchMoodController.GetMoodsTaskByName getUserLatestMood = new ElasticsearchMoodController.GetMoodsTaskByName();

        try {
            guestUserAccount = (Account) getUserActivity.execute(guestUsername).get().get(0);
            LatestMood = (Mood) getUserLatestMood.execute(guestUsername).get().get(0);//Should get latest mood

            guestProfileName.setText(guestUserAccount.getUsername());

            /**
             * TODO
             * change getLatitude,Longiture to
             * getLocation once location functionality is implimented.
             */
            guestLatestMoodTextview.setText("Latest Mood: " + LatestMood.toString());
            guestLatestLocationTextview.setText("Latest Location: "
                    + "Latitude: " + LatestMood.getLatitude() + " "
                    + "Longitude: " + LatestMood.getLongitude());
        } catch (Exception e) {
            /**
             * TODO
             * handle this exception
             */
        }



        guestDenyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account loggedInUser;

                ElasticsearchAccountController.GetUser getUserActivity = new ElasticsearchAccountController.GetUser();
                ElasticsearchAccountController.UpdateAccountTask updateAccountTask = new ElasticsearchAccountController.UpdateAccountTask();
                try {
                    loggedInUser = getUserActivity.execute(getIntent().getStringExtra("username")).get().get(0);

                    loggedInUser.getFollowRequests().removeAll(Collections.singleton(guestUsername));

                    updateAccountTask.execute(loggedInUser);

                    finish();
                } catch (Exception e) {
                    /**
                     * Handle this
                     */
                }
            }
        });

    }
}
