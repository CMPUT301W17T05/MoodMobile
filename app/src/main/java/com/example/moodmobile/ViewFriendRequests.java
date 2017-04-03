package com.example.moodmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewFriendRequests extends AppCompatActivity {

    private String username;

    private Account currentlyLoggedIn;

    private ArrayList<String> followRequestsList;
    private ArrayAdapter<String> followRequestsAdapter;
    public TextView followRequestsCountTextView;
    public ListView requestsList;

    /**
     * This method populates the follow requests ListView
     * @param username Takes the users Account as an argument
     */
    public void populateRequestsList(String username){
        ElasticsearchAccountController.GetUser loggedInUserTask = new ElasticsearchAccountController.GetUser();

        try{
            currentlyLoggedIn = loggedInUserTask.execute(username).get().get(0);

        } catch (Exception e) {
            Log.i("Error", "Could not download account details for logged in user..");
        }

        followRequestsList.clear();
        followRequestsList.addAll(currentlyLoggedIn.getFollowRequests());

        if (followRequestsList.size() != 1)
            followRequestsCountTextView.setText("You have "
                + followRequestsList.size() + " pending friend requests");
        else
            followRequestsCountTextView.setText("You have "
                    + followRequestsList.size() + " pending friend request");


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_requests);

        username = getIntent().getStringExtra("username");

        requestsList = (ListView) findViewById(R.id.RequestsList);
        followRequestsCountTextView = (TextView) findViewById(R.id.FollowRequestsCount);

        followRequestsList = new ArrayList<>();
        populateRequestsList(username);


        followRequestsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, followRequestsList);
        requestsList.setAdapter(followRequestsAdapter);



        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * TODO
                 * Allow to view profile of requester
                 */

                Intent intent = new Intent(ViewFriendRequests.this, ViewGuestProfile.class)
                        .putExtra("guestUsername", followRequestsList.get(i))
                        .putExtra("username", username);

                startActivityForResult(intent, 1);
                followRequestsAdapter.notifyDataSetChanged();

            }
        });
    }


    /**
     * Called upon starting/resuming the activity.
     * It creates a new adapter for the ListView
     */
    @Override
    public void onResume(){
        super.onResume();

        populateRequestsList(username);
        followRequestsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, followRequestsList);
        requestsList.setAdapter(followRequestsAdapter);


        followRequestsAdapter.notifyDataSetChanged();

    }
}


