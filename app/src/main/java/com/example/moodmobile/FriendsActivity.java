package com.example.moodmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class controlls the functionality of displaying your following list.
 * It takes in your username as an argument via an intent.
 *
 * It displays your following list, the number of people you
 * are following, and their latest mood.
 *
 */


/**
 * TODO
 * Write custom adapter for listview
 *
 */
public class FriendsActivity extends AppCompatActivity {

    private String username;

    private Account userAccount;

    private Button AddNewFriendButton;
    private Button FilterFollowingListButton;

    private TextView NumberFriendsTextView;

    private ListView FriendsListView;
    private ArrayList<String> FriendsNames;
    private ArrayList<Mood> FriendsList;
    private ArrayAdapter<Mood> FriendsListViewAdapter;


    /**
     *
     * function which downloads the users following list, and then
     * populates the listview with the followers.
     */
    private void getFriends() {
        ElasticsearchAccountController.GetUser getAccountTask = new ElasticsearchAccountController.GetUser();

        try {
            userAccount = getAccountTask.execute(username).get().get(0);
        } catch (Exception e) {
            Log.i("Error", "Could not download account details for logged in user.");

        }

        FriendsNames.addAll(userAccount.getFollowing());

        /**for (String FriendName : FriendsNames)**/{
            ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
            getMoodsTask.execute("sydia");
            try {
                FriendsList = getMoodsTask.get();
            } catch (Exception e) {

            }
        }

        NumberFriendsTextView.setText("You are following " + FriendsList.size() + " people");
    }

    /**
     * OnCreate method called upon creation of the class
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        username = getIntent().getStringExtra("username");

        FriendsList = new ArrayList<>();
        FriendsNames = new ArrayList<>();

        NumberFriendsTextView = (TextView) findViewById(R.id.NumberFriendsTextView);
        AddNewFriendButton = (Button) findViewById(R.id.AddNewFriendButton);
        FilterFollowingListButton = (Button) findViewById(R.id.FilterFollowingListButton);

        FriendsListView = (ListView) findViewById(R.id.FriendsListView);
        //FriendsListViewAdapter = new ArrayAdapter<>(this, R.layout.list_item, FriendsList);
        FriendsListViewAdapter = new CustomListAdapter(this, FriendsList);

        FriendsListView.setAdapter(FriendsListViewAdapter);

        /** Will populate friendlist**/
        getFriends();
        FriendsListViewAdapter.notifyDataSetChanged();

        AddNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsListViewAdapter.notifyDataSetChanged();
            }
        });

    }

                /**
                 * onStart called every time activity is entered.
                 * It will create a new ArrayAdapter
                 */
        @Override
    public void onStart(){
        super.onStart();

        FriendsListViewAdapter = new CustomListAdapter(this, FriendsList);
        //FriendsListViewAdapter = new ArrayAdapter<>(this, R.layout.list_item, FriendsList);
        FriendsListViewAdapter.notifyDataSetChanged();

    }
}
