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

public class FriendsActivity extends AppCompatActivity {

    private String username;

    private Account userAccount;

    private Button AddNewFriendButton;
    private Button FilterFollowingListButton;

    private TextView NumberFriendsTextView;

    private ListView FriendsListView;
    private ArrayList<String> FriendsList;
    private ArrayAdapter<String> FriendsListViewAdapter;

    private void getFriends() {
        ElasticsearchAccountController.GetUser getAccountTask = new ElasticsearchAccountController.GetUser();

        try {
            userAccount = getAccountTask.execute(username).get().get(0);
        } catch (Exception e) {
            Log.i("Error", "Could not download account details for logged in user.");

        }

        FriendsList.addAll(userAccount.getFollowing());
        NumberFriendsTextView.setText("You are following " + FriendsList.size() + " people");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        username = getIntent().getStringExtra("username");

        FriendsList = new ArrayList<>();

        NumberFriendsTextView =         (TextView) findViewById(R.id.NumberFriendsTextView);
        AddNewFriendButton =            (Button) findViewById(R.id.AddNewFriendButton);
        FilterFollowingListButton =     (Button) findViewById(R.id.FilterFollowingListButton);

        FriendsListView = (ListView) findViewById(R.id.FriendsListView);
        FriendsListViewAdapter = new ArrayAdapter<>(this, R.layout.list_item, FriendsList);
        FriendsListView.setAdapter(FriendsListViewAdapter);

        /** Will populate friendlist**/
        getFriends();
        FriendsListViewAdapter.notifyDataSetChanged();

        FriendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FriendsListViewAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        FriendsListViewAdapter = new ArrayAdapter<>(this, R.layout.list_item, FriendsList);
        FriendsListViewAdapter.notifyDataSetChanged();

    }
}