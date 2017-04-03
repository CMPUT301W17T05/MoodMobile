package com.example.moodmobile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by juice on 27/03/17.
 */

/**
 * This class is to view the information of a person who
 * has sent you a following request.
 *
 * It allows you to view their mood and latest location,
 * and also allows you to accept or deny their request.
 *
 */
public class ViewRequest extends AppCompatActivity {
    private Account UserAccount;
    private Mood LatestMood;

    private Button guestDenyRequestButton;
    private Button guestAcceptRequestButton;

    /**
     * Method to log and display an error if the request could not be sent.
     */
    public void displayRequestNotSent(){
        Log.i("Error", "Could not accept follow request.");

        Context context = getApplicationContext();

        CharSequence text = "Error: could not accept request.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Method to display a notification if the request was accepted.
     * @param username is the username of the currently logged in user.
     */
    public void displaySuccessfullyAcceptedRequest(String username){
        Context context = getApplicationContext();

        CharSequence text = "Request Accepted! "
                + username + " is now following you!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Method called upon creation of the class.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_profile);

        final String guestUsername = getIntent().getStringExtra("guestUsername");
        final String loggedInUsername = getIntent().getStringExtra("username");

        TextView guestProfileName = (TextView) findViewById(R.id.GuestProfileName);
        TextView guestLatestMoodTextview = (TextView) findViewById(R.id.GuestLatestMoodTextview);
        TextView guestLatestLocationTextview = (TextView) findViewById(R.id.GuestLatestLocationTextview);

        guestAcceptRequestButton = (Button) findViewById(R.id.GuestAcceptRequest);
        guestDenyRequestButton = (Button) findViewById(R.id.GuestDenyRequest);

        ElasticsearchAccountController.GetUser getUserActivity = new ElasticsearchAccountController.GetUser();
        ElasticsearchMoodController.GetMoodsTaskByName getUserLatestMood = new ElasticsearchMoodController.GetMoodsTaskByName();

        try {
            UserAccount = (Account) getUserActivity.execute(guestUsername).get().get(0);
            guestProfileName.setText(UserAccount.getUsername());
        } catch (Exception e) {
            /**
             */
        }

        try {
            LatestMood = (Mood) getUserLatestMood.execute(guestUsername).get().get(0);//Should get latest mood
            guestLatestMoodTextview.setText("Latest Mood: " + LatestMood.toString());

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(LatestMood.getLatitude(), LatestMood.getLongitude(), 1);
            guestLatestLocationTextview.setText("Latest Location: " + addresses.get(0).getLocality());

        } catch (Exception e) {
            guestLatestLocationTextview.setText("Latest Location:");
        }


        /**
         * Onclick listener for Deny request
         */
        guestDenyRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account loggedInUser;

                ElasticsearchAccountController.GetUser getUserActivity = new ElasticsearchAccountController.GetUser();
                ElasticsearchAccountController.UpdateAccountTask updateAccountTask = new ElasticsearchAccountController.UpdateAccountTask();
                try {
                    loggedInUser = getUserActivity.execute(loggedInUsername).get().get(0);
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

        /**
         * Onclick listener for accept request. 
         */
        guestAcceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account loggedInUser;
                Account guestUser;

                ElasticsearchAccountController.GetUser getUserTask
                        = new ElasticsearchAccountController.GetUser();
                ElasticsearchAccountController.UpdateAccountTask updateAccountTask
                        = new ElasticsearchAccountController.UpdateAccountTask();

                // Remove guest user from the follow requests
                try{
                    loggedInUser = getUserTask.execute(loggedInUsername).get().get(0);
                    loggedInUser.getFollowRequests().removeAll(Collections.singleton(guestUsername));

                    updateAccountTask.execute(loggedInUser);
                } catch (Exception e) {
                    /**
                     *
                     */
                }

                getUserTask = new ElasticsearchAccountController.GetUser();
                updateAccountTask = new ElasticsearchAccountController.UpdateAccountTask();

                // Add logged in user to the guests' following list
                try{
                    guestUser = getUserTask.execute(guestUsername).get().get(0);
                    guestUser.addFollowing(loggedInUsername);

                    updateAccountTask.execute(guestUser);

                    displaySuccessfullyAcceptedRequest(guestUsername);
                } catch (Exception e) {
                    displayRequestNotSent();
                }

                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
