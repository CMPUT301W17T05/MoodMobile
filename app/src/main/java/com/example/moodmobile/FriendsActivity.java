package com.example.moodmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * This class is to handle the sending of friend requests,
 * receiving of friend requests
 *
 */
public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend);

        String username = getIntent().getStringExtra("username");


        EditText usernameSearch = (EditText) findViewById(R.id.usernameSearch);
        Button sendRequest = (Button) findViewById(R.id.SendRequest);
        Button getFriendRequests = (Button) findViewById(R.id.GetFriendRequests);
        ListView requestsList = (ListView) findViewById(R.id.RequestsList);






    }
}
